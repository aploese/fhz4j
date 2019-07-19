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
package de.ibapl.fhz4j.parser.cul.evohome;

import de.ibapl.fhz4j.parser.api.Parser;

/**
 * Header 3C or 18 command 0004
 * 
 * @author aploese
 *
 */
class _XX_0004_16_Parser extends Parser {

	StringBuilder zoneNameBuilder = new StringBuilder();
	byte zoneId;
	byte unknown;

	enum State {

		/**
		 * 
		 */
		COLLECT_ZONEID,
		/**
		 * 
		 */
		COLLECT_UNKNOWN,
		/**
		 * 
		 */
		COLLECT_ZONE_NAME,
		/**
		 * 
		 */
		PARSE_SUCCESS, PARSE_ERROR;

	}

	State state;
	private short nibblesToConsume;
	private short nibblesConsumed;

	@Override
	public void parse(char c) {
		nibblesConsumed++;
		switch (state) {
		case COLLECT_ZONEID:
			push(digit2Int(c));
			if (getStackpos() == 0) {
				zoneId = getByteValue();
				setStackSize(2);
				state = State.COLLECT_UNKNOWN;
			}
			break;
		case COLLECT_UNKNOWN:
			push(digit2Int(c));
			if (getStackpos() == 0) {
				unknown = getByteValue();
				setStackSize(2);
				state = State.COLLECT_ZONE_NAME;
			}
			break;
		case COLLECT_ZONE_NAME:
			push(digit2Int(c));
			if (getStackpos() == 0) {
				byte b = getByteValue();
				if (b != 0) {
					zoneNameBuilder.append((char)b);
				}
				if (nibblesConsumed == nibblesToConsume) {
					state = State.PARSE_SUCCESS;
				} else {
					setStackSize(2);
				}
			}
			break;
		case PARSE_SUCCESS:
			throw new RuntimeException("PARSE_SUCCESS should not be called");
		case PARSE_ERROR:
			throw new RuntimeException("PARSE_ERROR should not be called");

		}
	}

	public void init(short bytesToConsume) {
		setStackSize(2);
		state = State.COLLECT_ZONEID;
		zoneNameBuilder.setLength(0);
		zoneId = 0;
		unknown = 0;
		this.nibblesConsumed = 0;
		this.nibblesToConsume = (short) (bytesToConsume * 2);
	}

	// TODO change signature ???
	@Override
	public void init() {
		throw new RuntimeException("should not be called");
	}

}