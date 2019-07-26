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
import de.ibapl.fhz4j.parser.em.EmParser;
import de.ibapl.fhz4j.parser.evohome.EvoHomeParser;
import de.ibapl.fhz4j.parser.fht.FhtParser;
import de.ibapl.fhz4j.parser.fs20.FS20Parser;
import de.ibapl.fhz4j.parser.hms.HmsParser;
import de.ibapl.fhz4j.parser.lacrosse.tx2l.LaCrosseTx2Parser;
import de.ibapl.fhz4j.protocol.em.EmMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessage;
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
public class CulParser<T extends FhzMessage> implements ParserListener<T> {

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

	public void init() {
		partialFhzMessage = null;
		fhzMessage = null;
	}

	@Override
	public void success(T fhzMessage) {
		this.fhzMessage = fhzMessage;
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
		state = State.SINGNAL_STRENGTH;
	}

	@Override
	public void successPartialAssembled(T fhzMessage) {
		this.fhzMessage = fhzMessage;
		state = State.SINGNAL_STRENGTH;
	}

	private enum State {
		IDLE, PARSER_PARSING, CUL_L_PARSED, CUL_LO_PARSED, CUL_LOV_PARSED, CUL_E_PARSED, CUL_EO_PARSED, EVO_HOME_START, EVO_HOME_READ_GARBAGE, SINGNAL_STRENGTH, END_CHAR_0X0D, END_CHAR_0X0A;
	}

	private State state = State.IDLE;
	private Parser currentParser; 
	private byte firstNibble;
	private boolean isFirstNibble;
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
	@SuppressWarnings("unchecked")
	private final EvoHomeParser evoHomeParser = new EvoHomeParser((ParserListener<EvoHomeMessage>) this);

	private FhzMessage fhzMessage;

	public void parse(char c) {
		switch (state) {
		case IDLE:
			init();
			switch (c) {
			case 'E':
				state = State.CUL_E_PARSED;
				break;
			case 'F':
				initParser(fs20Parser);
				state = State.PARSER_PARSING;
				break;
			case 'T':
				initParser(fhtParser);
				state = State.PARSER_PARSING;
				break;
			case 'H':
				initParser(hmsParser);
				state = State.PARSER_PARSING;
				break;
			case 't':
				initParser(laCrosseTx2Parser);
				state = State.PARSER_PARSING;
				break;
			case 'L':
				state = State.CUL_L_PARSED;
				break;
			case 'v':
				evoHomeParser.init();
				state = State.EVO_HOME_START;
				break;
			case '\n':
				break;
			case '\r':
				break;
			default:
				LOG.fine(String.format("Discarted: 0x%02x %s", (byte) c, c));
			}
			break;
		case EVO_HOME_START:
			switch (c) {
			case 'r':
				initParser(evoHomeParser);
				state = State.PARSER_PARSING;
				break;
			case 'a':
				//TODO how to handle this?? we switched to Evo Home in the CUL device ... notify ???
				state = State.IDLE;
				break;
			case '!':
				state = State.EVO_HOME_READ_GARBAGE;
				break;
			default:
				throw new IllegalArgumentException(String.format("unexpected char EVO_HOME_START: 0x%02x %s", (byte) c, c));
			}
			break;
		case EVO_HOME_READ_GARBAGE:
			//Do nothing, just wait for end of message;
			if (c == '\n' || c == '\r') {
				state = State.IDLE;
			}
		case PARSER_PARSING:
			if (isFirstNibble) {
				firstNibble = digit2Byte(c);
				isFirstNibble = false;
			} else {
				isFirstNibble = true;
				currentParser.parse((byte)((firstNibble << 4) | digit2Byte(c)));
			}
			break;
		case CUL_E_PARSED:
			if (c == 'O') {
				state = State.CUL_EO_PARSED;
			} else {
				initParser(emParser);
				state = State.PARSER_PARSING;
				firstNibble = digit2Byte(c);
				isFirstNibble = false;
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
				state = State.END_CHAR_0X0A;
				break;
			} else {
				if (isFirstNibble) {
					firstNibble = digit2Byte(c);
					isFirstNibble = false;
				} else {
					isFirstNibble = true;
					final byte value = (byte)((firstNibble << 4) | digit2Byte(c));
					if (partialFhzMessage != null) {
						partialFhzMessage.signalStrength = (float) (value / 2.0 - 74);
					} else if (fhzMessage != null) {
						fhzMessage.signalStrength = (float) (value / 2.0 - 74);
					} else {
						throw new RuntimeException("Should never happen");
					}
					state = State.END_CHAR_0X0D;
				}
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

					if (fhzMessage != null) {
					switch (fhzMessage.fhzProtocol) {
					case FHT:
						dataListener.fhtDataParsed((FhtMessage) fhzMessage);
						break;
					case HMS: 
						dataListener.hmsDataParsed((HmsMessage) fhzMessage);
						break;
					case EM:
						dataListener.emDataParsed((EmMessage) fhzMessage);
						break;
					case FS20:
						dataListener.fs20DataParsed((FS20Message) fhzMessage);
						break;
					case LA_CROSSE_TX2:
						dataListener.laCrosseTxParsed((LaCrosseTx2Message) fhzMessage);
						break;
					case CUL:
						dataListener.culMessageParsed((CulMessage) fhzMessage);
						break;
					case EVO_HOME:
						dataListener.evoHomeParsed((EvoHomeMessage) fhzMessage);
						break;
						default:
							throw new RuntimeException();
				}
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

	private void initParser(Parser parser) {
		currentParser = parser;
		currentParser.init();
		isFirstNibble = true;
		firstNibble = 0;
	}
	
	private byte digit2Byte(char c) {
		switch (c) {
		case '0':
			return 0x00;
		case '1':
			return 0x01;
		case '2':
			return 0x02;
		case '3':
			return 0x03;
		case '4':
			return 0x04;
		case '5':
			return 0x05;
		case '6':
			return 0x06;
		case '7':
			return 0x07;
		case '8':
			return 0x08;
		case '9':
			return 0x09;
		case 'A':
			return 0x0a;
		case 'B':
			return 0x0b;
		case 'C':
			return 0x0c;
		case 'D':
			return 0x0d;
		case 'E':
			return 0x0e;
		case 'F':
			return 0x0f;
		default:
			throw new RuntimeException("Not a Number: " + c);
		}
	}


}
