/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2019, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package de.ibapl.fhz4j.parser.cul;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.api.FhzDataListener;
import de.ibapl.fhz4j.api.FhzMessage;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.em.EmMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;
import de.ibapl.fhz4j.protocol.hms.HmsMessage;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;

/**
 * Parses CUL from www.busware.de Commands see http://culfw.de/commandref.html
 * and https://github.com/mhop/fhem-mirror/blob/master/fhem/FHEM/11_FHT.pm
 * partial implemented.
 *
 * @author Arne Plöse
 */
public class CulParser<T extends FhzMessage> extends Parser implements ParserListener<T> {

	private T partialFhzMessage;

	public CulParser(FhzDataListener dataListener) {
		this.dataListener = dataListener;
	}

	private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
	private final FhzDataListener dataListener;

	/**
	 * @return the dataListener
	 */
	public FhzDataListener getDataListener() {
		return dataListener;
	}

	@Override
	public void init() {
		partialFhzMessage = null;
		fhzMessage = null;
	}

	@Override
	public void success(T fhzMessage) {
		this.fhzMessage = fhzMessage;
		setStackSize(2);
		state = State.SINGNAL_STRENGTH;
	}

	@Override
	public void fail(Throwable t) {
		state = State.IDLE;
		dataListener.failed(t);
	}

	@Override
	public void successPartial(T fhzMessage) {
		this.partialFhzMessage = fhzMessage;
		setStackSize(2);
		state = State.SINGNAL_STRENGTH;
	}

	@Override
	public void successPartialAssembled(T fhzMessage) {
		this.fhzMessage = fhzMessage;
		setStackSize(2);
		state = State.SINGNAL_STRENGTH;
	}

	private enum State {

		IDLE, EM_PARSING, FS20_PARSING, FHT_PARSING, HMS_PARSING, LA_CROSSE_TX2_PARSING, CUL_L_PARSED, CUL_LO_PARSED, CUL_LOV_PARSED, CUL_E_PARSED, CUL_EO_PARSED, SINGNAL_STRENGTH, END_CHAR_0X0D, END_CHAR_0X0A;
	}

	private State state = State.IDLE;
	@SuppressWarnings("unchecked")
	private final EmParser emParser = new EmParser((ParserListener<EmMessage>) this);
	@SuppressWarnings("unchecked")
	private final FS20Parser fs20Parser = new FS20Parser((ParserListener<FS20Message>) this);
	@SuppressWarnings("unchecked")
	private final FhtParser fhtParser = new FhtParser((ParserListener<FhtMessage>) this);
	@SuppressWarnings("unchecked")
	private final HmsParser hmsParser = new HmsParser((ParserListener<HmsMessage>) this);
	@SuppressWarnings("unchecked")
	private final LaCrosseTx2Parser laCrosseTx2Parser = new LaCrosseTx2Parser(
			(ParserListener<LaCrosseTx2Message>) this);

	private FhzMessage fhzMessage;

	@Override
	public void parse(char c) {
		switch (state) {
		case IDLE:
			init();
			switch (c) {
			case 'E':
				state = State.CUL_E_PARSED;
				break;
			case 'F':
				fs20Parser.init();
				state = State.FS20_PARSING;
				break;
			case 'T':
				fhtParser.init();
				state = State.FHT_PARSING;
				break;
			case 'H':
				hmsParser.init();
				state = State.HMS_PARSING;
				break;
			case 't':
				laCrosseTx2Parser.init();
				state = State.LA_CROSSE_TX2_PARSING;
				break;
			case 'L':
				state = State.CUL_L_PARSED;
				break;
			default:
				LOG.fine(String.format("Discarted: 0x%02x %s", (byte) c, c));
			}
			break;
		case EM_PARSING:
			emParser.parse(c);
			break;
		case FS20_PARSING:
			fs20Parser.parse(c);
			break;
		case FHT_PARSING:
			fhtParser.parse(c);
			break;
		case HMS_PARSING:
			hmsParser.parse(c);
			break;
		case LA_CROSSE_TX2_PARSING:
			laCrosseTx2Parser.parse(c);
			break;
		case CUL_E_PARSED:
			if (c == 'O') {
				state = State.CUL_EO_PARSED;
			} else {
				emParser.init();
				state = State.EM_PARSING;
				emParser.parse(c);
			}
			break;
		case CUL_EO_PARSED:
			if (c == 'B') {
				state = State.END_CHAR_0X0D;
				fhzMessage = CulMessage.EOB;
			} else {
				// ERROR???
				state = State.IDLE;
			}
			break;
		case CUL_L_PARSED:
			if (c == 'O') {
				state = State.CUL_LO_PARSED;
			} else {
				// ERROR???
				state = State.IDLE;
			}
			break;
		case CUL_LO_PARSED:
			if (c == 'V') {
				state = State.CUL_LOV_PARSED;
			} else {
				// ERROR???
				state = State.IDLE;
			}
			break;
		case CUL_LOV_PARSED:
			if (c == 'F') {
				state = State.END_CHAR_0X0D;
				fhzMessage = CulMessage.LOVF;
			} else {
				// ERROR???
				state = State.IDLE;
			}
			break;
		case SINGNAL_STRENGTH:
			if (c == '\r') {
				setStackSize(0);
				state = State.END_CHAR_0X0A;
				break;
			}
			try {
				push(digit2Int(c));
			} catch (RuntimeException ex) {
				LOG.severe(String.format("Signal strenght - Wrong char: 0x%02x %s", (byte) c, c));
				state = State.IDLE;
				parse(c); // try to recover
			}
			if (getStackpos() == 0) {
				if (partialFhzMessage != null) {
					partialFhzMessage.signalStrength = (float) ((getByteValue()) / 2.0 - 74);
				} else if (fhzMessage != null) {
					fhzMessage.signalStrength = (float) ((getByteValue()) / 2.0 - 74);
				} else {
					throw new RuntimeException("Should never happen");
				}
				setStackSize(0);
				state = State.END_CHAR_0X0D;
			}
			break;

		case END_CHAR_0X0D:
			if (c == '\r') {
				state = State.END_CHAR_0X0A;
				break;
			} else if (c == '\n') {
				// Fall trough
			} else {
				// ERRORhandling
				state = State.IDLE;
				break;
			}
		case END_CHAR_0X0A:
			if (c == '\n') {
				state = State.IDLE;
				if (LOG.isLoggable(Level.FINE)) {
					if (fhzMessage != null) {
						LOG.fine(fhzMessage.toString());
					}
				}

				if (dataListener != null) {

					if (partialFhzMessage instanceof FhtMessage) {
						dataListener.fhtPartialDataParsed((FhtMessage) partialFhzMessage);
					}

					if (fhzMessage instanceof FhtMessage) {
						dataListener.fhtDataParsed((FhtMessage) fhzMessage);
					} else if (fhzMessage instanceof HmsMessage) {
						dataListener.hmsDataParsed((HmsMessage) fhzMessage);
					} else if (fhzMessage instanceof EmMessage) {
						dataListener.emDataParsed((EmMessage) fhzMessage);
					} else if (fhzMessage instanceof FS20Message) {
						dataListener.fs20DataParsed((FS20Message) fhzMessage);
					} else if (fhzMessage instanceof LaCrosseTx2Message) {
						dataListener.laCrosseTxParsed((LaCrosseTx2Message) fhzMessage);
					} else if (fhzMessage instanceof CulMessage) {
						dataListener.culMessageParsed((CulMessage) fhzMessage);
					}

				}
			} else {
				// ERRORhandling ???
				state = State.IDLE;
				parse(c); // try to recover
			}
		default:
		}
	}

}
