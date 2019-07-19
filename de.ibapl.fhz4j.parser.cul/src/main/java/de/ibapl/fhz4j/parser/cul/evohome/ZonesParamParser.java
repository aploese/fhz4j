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

import java.util.LinkedList;

import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0xXX_0x000A_0xXX_ZONES_PARAMS_Message.ZoneParams;

class ZonesParamParser extends Parser {

	LinkedList<ZoneParams> zoneParams;

	enum State {

		/**
		 * 
		 */
		COLLECT_ZONEID,
		/**
		 * 
		 */
		COLLECT_FLAGS,
		/**
		 * 
		 */
		COLLECT_MIN_TEMP,
		/**
		 * 
		 */
		COLLECT_MAX_TEMP, PARSE_SUCCESS, PARSE_ERROR;

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
				zoneParams.addLast(new ZoneParams());
				zoneParams.getLast().zoneId = getByteValue();
				setStackSize(2);
				state = State.COLLECT_FLAGS;
			}
			break;
		case COLLECT_FLAGS:
			push(digit2Int(c));
			if (getStackpos() == 0) {
				zoneParams.getLast().flags = getByteValue();
				setStackSize(4);
				state = State.COLLECT_MIN_TEMP;
			}
			break;
		case COLLECT_MIN_TEMP:
			push(digit2Int(c));
			if (getStackpos() == 0) {
				zoneParams.getLast().minTemperature = 0.01f * getShortValue();
				setStackSize(4);
				state = State.COLLECT_MAX_TEMP;
			}
			break;
		case COLLECT_MAX_TEMP:
			push(digit2Int(c));
			if (getStackpos() == 0) {
				zoneParams.getLast().maxTemperature = 0.01f * getShortValue();
				if (nibblesConsumed == nibblesToConsume) {
					state = State.PARSE_SUCCESS;
				} else {
					setStackSize(2);
					state = State.COLLECT_ZONEID;
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
		zoneParams = new LinkedList<>();
		this.nibblesConsumed = 0;
		this.nibblesToConsume = (short) (bytesToConsume * 2);
	}

	// TODO change signature ???
	@Override
	public void init() {
		throw new RuntimeException("should not be called");
	}

}