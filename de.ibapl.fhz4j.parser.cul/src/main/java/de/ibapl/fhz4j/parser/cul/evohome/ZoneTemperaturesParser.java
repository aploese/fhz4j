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

import java.math.BigDecimal;
import java.util.LinkedList;

import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.protocol.evohome.ZoneTemperature;

class ZoneTemperaturesParser extends Parser {

	LinkedList<ZoneTemperature> zoneTemperatures;
	//Just cache this ...
	private final static BigDecimal ONE_HUNDRED = new BigDecimal(100.0);

	enum State {

		/**
		 * 
		 */
		COLLECT_ZONEID,
		/**
		 * 
		 */
		COLLECT_TEMPERATURE,
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
				zoneTemperatures.addLast(new ZoneTemperature());
				zoneTemperatures.getLast().zone = getByteValue();
				setStackSize(4);
				state = State.COLLECT_TEMPERATURE;
			}
			break;
		case COLLECT_TEMPERATURE:
			push(digit2Int(c));
			if (getStackpos() == 0) {
				if (getShortValue() == 0x7FFF) {
					zoneTemperatures.getLast().temperature = null;
				}else {
					zoneTemperatures.getLast().temperature = new BigDecimal(getShortValue()).divide(ONE_HUNDRED);
				}
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
		zoneTemperatures = new LinkedList<>();
		this.nibblesConsumed = 0;
		this.nibblesToConsume = (short) (bytesToConsume * 2);
	}

	// TODO change signature ???
	@Override
	public void init() {
		throw new RuntimeException("should not be called");
	}

}