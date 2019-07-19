package de.ibapl.fhz4j.parser.cul.evohome;

import java.util.LinkedList;

import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x000A_0xXX_ZONES_PARAMS_Message.ZoneParams;
import de.ibapl.fhz4j.protocol.evohome.ZoneTemperature;

class RoomDesiredTemperatureParser extends Parser {

	LinkedList<ZoneTemperature> zoneTemperatures;

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
				zoneTemperatures.getLast().temperature = 0.01f * getShortValue();
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