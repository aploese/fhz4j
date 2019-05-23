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

import java.util.logging.Logger;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fs20.FS20CommandValue;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;

/**
 *
 * @author Arne Plöse
 */
public class FS20Parser extends Parser {

	@Override
	public void init() {
		setStackSize(4);
		state = State.COLLECT_HOUSECODE;
		fs20Message = null;
	}

	private FS20CommandValue getFS20CommandValue(int intValue) {
		switch (intValue) {
		case 0x00:
			return FS20CommandValue.OFF;
		case 0x01:
			return FS20CommandValue.DIM_6_PERCENT;
		case 0x02:
			return FS20CommandValue.DIM_12_PERCENT;
		case 0x03:
			return FS20CommandValue.DIM_18_PERCENT;
		case 0x04:
			return FS20CommandValue.DIM_25_PERCENT;
		case 0x05:
			return FS20CommandValue.DIM_31_PERCENT;
		case 0x06:
			return FS20CommandValue.DIM_37_PERCENT;
		case 0x07:
			return FS20CommandValue.DIM_43_PERCENT;
		case 0x08:
			return FS20CommandValue.DIM_50_PERCENT;
		case 0x09:
			return FS20CommandValue.DIM_56_PERCENT;
		case 0x0a:
			return FS20CommandValue.DIM_62_PERCENT;
		case 0x0b:
			return FS20CommandValue.DIM_68_PERCENT;
		case 0x0c:
			return FS20CommandValue.DIM_75_PERCENT;
		case 0x0d:
			return FS20CommandValue.DIM_81_PERCENT;
		case 0x0e:
			return FS20CommandValue.DIM_87_PERCENT;
		case 0x0f:
			return FS20CommandValue.DIM_93_PERCENT;
		case 0x10:
			return FS20CommandValue.DIM_100_PERCENT;
		case 0x11:
			return FS20CommandValue.ON;
		case 0x12:
			return FS20CommandValue.TOGGLE;
		case 0x13:
			return FS20CommandValue.DIM_UP;
		case 0x14:
			return FS20CommandValue.DIM_DOWN;
		case 0x15:
			return FS20CommandValue.DIM_UP_DOWN;
		case 0x16:
			return FS20CommandValue.TIMER;
		case 0x17:
			return FS20CommandValue.SENDSATE;
		case 0x18:
			return FS20CommandValue.OFF_FOR_TIMER;
		case 0x19:
			return FS20CommandValue.ON_FOR_TIMER;
		case 0x1a:
			return FS20CommandValue.ON_OLD_FOR_TIMER;
		case 0x1b:
			return FS20CommandValue.RESET;
		case 0x1c:
			return FS20CommandValue.RAMP_ON_TIME;
		case 0x1d:
			return FS20CommandValue.RAMP_OFF_TIME;
		case 0x1e:
			return FS20CommandValue.ON_OLD_FOR_TIMER_PREV;
		case 0x1f:
			return FS20CommandValue.ON_100_FOR_TIMER_PREV;
		default:
			throw new RuntimeException();
		}
	}

	private enum State {

		COLLECT_HOUSECODE, COLLECT_OFFSET, COLLECT_COMMAND, PARSE_SUCCESS, PARSE_ERROR;

	}

	public FS20Parser(ParserListener<FS20Message> parserListener) {
		this.parserListener = parserListener;
	}

	private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
	private final ParserListener<FS20Message> parserListener;
	private State state;
	private FS20Message fs20Message;
	private short housecode;
	private byte offset;

	@Override
	public void parse(char c) {
		try {
			switch (state) {
			// FHT
			case COLLECT_HOUSECODE:
				push(digit2Int(c));
				if (getStackpos() == 0) {
					housecode = getShortValue();
					setStackSize(2);
					state = State.COLLECT_OFFSET;
				}
				break;
			case COLLECT_OFFSET:
				push(digit2Int(c));
				if (getStackpos() == 0) {
					offset = getByteValue();
					setStackSize(2);
					state = State.COLLECT_COMMAND;
				}
				break;
			case COLLECT_COMMAND:
				push(digit2Int(c));
				if (getStackpos() == 0) {
					fs20Message = new FS20Message(housecode, getFS20CommandValue(getIntValue()), offset);
					state = State.PARSE_SUCCESS;
					parserListener.success(fs20Message);
					setStackSize(0);
				}
				break;

			}
		} catch (Throwable t) {
			parserListener.fail(new RuntimeException(String.format("State: %s last char %s", state, c), t));
			state = State.PARSE_ERROR;
		}
	}

}
