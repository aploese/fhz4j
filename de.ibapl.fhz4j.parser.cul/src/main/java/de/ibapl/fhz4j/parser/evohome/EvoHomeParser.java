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
package de.ibapl.fhz4j.parser.evohome;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.parser.api.AbstractParser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeHeaderByte;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeProperty;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x0004_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x000A_0x01_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x0016_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x0100_0x05_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x1F09_0x01_REQUEST_0C_1F09_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x2309_0x01_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x0004_0x16_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x0005_0xXX_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x0008_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x0009_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x000A_0xXX_ZONES_PARAMS_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x000C_0x12_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x000E_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x042F_0x08_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x1060_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x10E0_0x26_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x1100_0x08_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x12B0_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x1F09_0x03_BROADCAST_18_1F09_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x1FC9_0x12_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x2309_0xXX_ROOM_DESIRED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message.Mode;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x30C9_0xXX_ROOM_MEASURED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x3120_0x07_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x3B00_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x1C_0x10E0_0x26_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x1C_0x1FC9_0xXX_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x1C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeCommand;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeDeviceMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x28_0x0001_0x05__RADIO_TEST_REQUEST_FROM_MASTER_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x28_0x1F09_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x2C_0x1FC9_0xXX_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x2C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x0004_0x16_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x000A_0xXX_ZONES_PARAMS_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x0016_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x0100_0x05_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x1F09_0x03_RESPONSE_3C_1F09_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x313F_0x09_RESPONSE_3C_313F_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0xXX_0x0004_0x16_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0xXX_0x000A_0xXX_ZONES_PARAMS_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0xXX_0x1FC9_0xXX_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_AbstractZonesTemperature_Message;
import de.ibapl.fhz4j.protocol.evohome.ZoneTemperature;

public class EvoHomeParser extends AbstractParser {

	private enum State {

		/**
		 * 
		 */
		COLLECT_HEADER,
		/**
		 * 
		 */
		COLLECT_DEVICEID_1,
		/**
		* 
		*/
		COLLECT_DEVICEID_2,
		/**
		* 
		*/
		COLLECT_COMMAND,
		/**
		* 
		*/
		COLLECT_DATA_LENGTH,
		/**
		* 
		*/
		COLLECT_SINGLE_VALUE,
		/**
		 * 
		 */
		COLLECT_DATA_BYTES,
		/**
		 * 
		 */
		PARSE_ZONE_TEMPERATURE_ELEMENTS,
		/**
		 * 
		 */
		COLLECT_18_2E04_OPERATING_MODE,
		/**
		 * 
		 */
		PARSE_XX_1FC9_XX_ELEMENTS,
		/**
		 * 
		 */
		COLLECT_0C_0004_02_ZONEID,
		/**
		 * 
		 */
		COLLECT_0C_0004_02_UNKNOWN,
		/**
		 * 
		 */
		PARSE_XX_0004_16_DATA,
		/**
		 * 
		 */
		COLLECT_HEAT_DEMAND_ZONE,
		/**
		 * 
		 */
		COLLECT_HEAT_DEMAND_VALUE,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_PERMANENT_ZONE,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_PERMANENT_TEMPERATURE,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_PERMANENT_UNKNOWN,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_UNTIL_ZONE,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_UNTIL_TEMPERATURE,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_UNTIL_UNKNOWN,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_UNTIL_MINUTES,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_UNTIL_HOURE,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_UNTIL_DAY_OF_MONTH,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_UNTIL_MONTH,
		/**
		 * 
		 */
		COLLECT_ZONE_SETPOINT_UNTIL_YEAR,
		/**
		 * 
		 */
		PARSE_ZONES_PARAMS_ELEMENTS,
		/**
		 * 
		 */
		PARSE_SUCCESS,
		/**
		 * 
		 */
		PARSE_ERROR;

	}
	//Just cache this ...
	private final static BigDecimal ONE_HUNDRED = new BigDecimal(100.0);
	private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
	private final ParserListener<EvoHomeMessage> parserListener;
	private State state;
	private ZonesParamParser zonesParamParser = new ZonesParamParser();
	private ZoneTemperaturesParser zoneTemperatureParser = new ZoneTemperaturesParser();
	private _XX_0004_16_Parser _18_0004_16_Parser = new _XX_0004_16_Parser();
	private _XX_1FC9_XX_Parser _XX_1FC9_XX_Parser = new _XX_1FC9_XX_Parser();
	private EvoHomeMessage evoHomeMessage;
	private EvoHomeHeaderByte evoHomeHeaderByte;
	private EvoHomeCommand evoHomeCommand;
	private EvoHomeProperty evoHomeProperty;
	private int deviceId1;
	private int deviceId2;
	private int copyDataIndex;

	public EvoHomeParser(ParserListener<EvoHomeMessage> parserListener) {
		this.parserListener = parserListener;
	}

	@Override
	public void init() {
		state = State.COLLECT_HEADER;
		evoHomeMessage = null;
		evoHomeHeaderByte = null;
		evoHomeProperty = null;
		deviceId1 = 0;
		deviceId2 = 0;
		copyDataIndex = 0;
	}

	@Override
	public void parse(byte b) {
		try {
			switch (state) {
			case COLLECT_HEADER:
					decodeHeader(b);
					setStackSize(3);
					state = State.COLLECT_DEVICEID_1;
				break;
			case COLLECT_DEVICEID_1:
				if (push(b)) {
					deviceId1 = getIntValue();
					setStackSize(3);
					state = State.COLLECT_DEVICEID_2;
				}
				break;
			case COLLECT_DEVICEID_2:
				if (push(b)) {
					deviceId2 = getIntValue();
						setStackSize(2);
						state = State.COLLECT_COMMAND;
				}
				break;
			case COLLECT_COMMAND:
				if (push(b)) {
					decodeCommand();
					state = State.COLLECT_DATA_LENGTH;
				}
				break;
			case COLLECT_DATA_LENGTH:
				buildMessage((short)(b & 0xff));
				break;
			case COLLECT_DATA_BYTES:
					setByteArrayValue(b);
				break;
			case COLLECT_18_2E04_OPERATING_MODE:
					final EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message msg = ((EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message) evoHomeMessage);
					switch (b) {
					case 0x00:
						msg.mode = Mode.NORMAL;
					break;
					case 0x01:
						msg.mode = Mode.HEATING_OFF;
					break;
					case 0x02:
						msg.mode = Mode.ECONOMY;
					break;
					case 0x03:
						msg.mode = Mode.AWAY;
					break;
					case 0x04:
						msg.mode = Mode.EXCEPTION_DAY;
					break;
					case 0x07:
						msg.mode = Mode.SPECIAL_PROGRAMME;
					break;
					default:
						throw new RuntimeException("Unknown Mode: " + b);
					}
					state = State.COLLECT_DATA_BYTES;
				break;
			case PARSE_ZONE_TEMPERATURE_ELEMENTS:
				zoneTemperatureParser.parse(b);
				if (zoneTemperatureParser.state == ZoneTemperaturesParser.State.PARSE_SUCCESS) {
					((EvoHome_AbstractZonesTemperature_Message) evoHomeMessage).zoneTemperatures = zoneTemperatureParser.zoneTemperatures;
					parserListener.success(evoHomeMessage);
					state = State.PARSE_SUCCESS;
				}
				break;
			case PARSE_XX_1FC9_XX_ELEMENTS:
				_XX_1FC9_XX_Parser.parse(b);
				if (_XX_1FC9_XX_Parser.state == _XX_1FC9_XX_Parser.state.PARSE_SUCCESS) {
					((EvoHome_0xXX_0x1FC9_0xXX_Message) evoHomeMessage).elements = _XX_1FC9_XX_Parser.elements;
					parserListener.success(evoHomeMessage);
					state = State.PARSE_SUCCESS;
				}
				break;
			case COLLECT_0C_0004_02_ZONEID:
					((EvoHome_0x0C_0x0004_0x02_Message) evoHomeMessage).zoneId = b;
					state = State.COLLECT_0C_0004_02_UNKNOWN;
				break;
			case COLLECT_0C_0004_02_UNKNOWN:
					((EvoHome_0x0C_0x0004_0x02_Message) evoHomeMessage).unknown = b;
					parserListener.success(evoHomeMessage);
					state = State.PARSE_SUCCESS;
				break;
			case PARSE_XX_0004_16_DATA:
				_18_0004_16_Parser.parse(b);
				if (_18_0004_16_Parser.state == _18_0004_16_Parser.state.PARSE_SUCCESS) {
					((EvoHome_0xXX_0x0004_0x16_Message) evoHomeMessage).zoneId = _18_0004_16_Parser.zoneId;
					((EvoHome_0xXX_0x0004_0x16_Message) evoHomeMessage).unknown = _18_0004_16_Parser.unknown;
					((EvoHome_0xXX_0x0004_0x16_Message) evoHomeMessage).zoneName = _18_0004_16_Parser.zoneNameBuilder.toString();
					parserListener.success(evoHomeMessage);
					state = State.PARSE_SUCCESS;
				}
				break;
			case COLLECT_HEAT_DEMAND_ZONE:
					((EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message) evoHomeMessage).zone = b;
					state = State.COLLECT_HEAT_DEMAND_VALUE;
				break;
			case COLLECT_HEAT_DEMAND_VALUE:
					((EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message) evoHomeMessage).heatDemand = (short)(b & 0xFF);
					parserListener.success(evoHomeMessage);
					state = State.PARSE_SUCCESS;
				break;
			case COLLECT_ZONE_SETPOINT_PERMANENT_ZONE:
					((EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message) evoHomeMessage).temperature = new ZoneTemperature();
					((EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message) evoHomeMessage).temperature.zone = b;
					setStackSize(2);
					state = State.COLLECT_ZONE_SETPOINT_PERMANENT_TEMPERATURE;
				break;
			case COLLECT_ZONE_SETPOINT_PERMANENT_TEMPERATURE:
				if (push(b)) {
					((EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message) evoHomeMessage).temperature.temperature = new BigDecimal(getShortValue()).divide(ONE_HUNDRED);
					setStackSize(4);
					state = State.COLLECT_ZONE_SETPOINT_PERMANENT_UNKNOWN;
				}
				break;
			case COLLECT_ZONE_SETPOINT_PERMANENT_UNKNOWN:
				if (push(b)) {
					((EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message) evoHomeMessage).unknown = getIntValue();
					parserListener.success(evoHomeMessage);
					state = State.PARSE_SUCCESS;
				}
				break;
			case COLLECT_ZONE_SETPOINT_UNTIL_ZONE:
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).temperature = new ZoneTemperature();
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).temperature.zone = b;
					setStackSize(2);
					state = State.COLLECT_ZONE_SETPOINT_UNTIL_TEMPERATURE;
				break;
			case COLLECT_ZONE_SETPOINT_UNTIL_TEMPERATURE:
				if (push(b)) {
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).temperature.temperature = new BigDecimal(getShortValue()).divide(ONE_HUNDRED);
					setStackSize(4);
					state = State.COLLECT_ZONE_SETPOINT_UNTIL_UNKNOWN;
				}
				break;
			case COLLECT_ZONE_SETPOINT_UNTIL_UNKNOWN:
				if (push(b)) {
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).unknown = getIntValue();
					state = State.COLLECT_ZONE_SETPOINT_UNTIL_MINUTES;
				}
				break;
			case COLLECT_ZONE_SETPOINT_UNTIL_MINUTES:
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until = LocalDateTime.now();
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until = ((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until.withSecond(0);
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until = ((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until.withNano(0);
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until = ((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until.withMinute(b);
					state = State.COLLECT_ZONE_SETPOINT_UNTIL_HOURE;
				break;
			case COLLECT_ZONE_SETPOINT_UNTIL_HOURE:
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until = ((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until.withHour(b);
					state = State.COLLECT_ZONE_SETPOINT_UNTIL_DAY_OF_MONTH;
				break;
			case COLLECT_ZONE_SETPOINT_UNTIL_DAY_OF_MONTH:
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until = ((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until.withDayOfMonth(b);
					state = State.COLLECT_ZONE_SETPOINT_UNTIL_MONTH;
				break;
			case COLLECT_ZONE_SETPOINT_UNTIL_MONTH:
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until = ((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until.withMonth(b);
					setStackSize(2);
					state = State.COLLECT_ZONE_SETPOINT_UNTIL_YEAR;
				break;
			case COLLECT_ZONE_SETPOINT_UNTIL_YEAR:
				if (push(b)) {
					((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until = ((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage).until.withYear(getIntValue());
					parserListener.success(evoHomeMessage);
					state = State.PARSE_SUCCESS;
				}
				break;
			case PARSE_ZONES_PARAMS_ELEMENTS:
				zonesParamParser.parse(b);
				if (zonesParamParser.state == ZonesParamParser.State.PARSE_SUCCESS) {
					((EvoHome_0xXX_0x000A_0xXX_ZONES_PARAMS_Message) evoHomeMessage).zones = zonesParamParser.zoneParams;
					parserListener.success(evoHomeMessage);
					state = State.PARSE_SUCCESS;
				}
				break;
			case COLLECT_SINGLE_VALUE:
				if (push(b)) {
					setSingleValueAndNotify();
					state = State.PARSE_SUCCESS;
				}
				break;
			default:
				// TODO
				throw new RuntimeException("Cant handle state: " + state);

			}
		} catch (Throwable t) {
			parserListener.fail(new RuntimeException(String.format("State: %s last byte 0x%02x", state, b), t));
			state = State.PARSE_ERROR;
		}
	}

	private void setByteArrayValue(byte b) {
		byte[] theArray;
		switch (evoHomeProperty) {
		case _0C_0100:
			theArray = ((EvoHome_0x0C_0x0100_0x05_Message) evoHomeMessage).value;
			break;
		case _18_0005:
			theArray = ((EvoHome_0x18_0x0005_0xXX_Message) evoHomeMessage).value;
			break;
		case _18_000C:
			theArray = ((EvoHome_0x18_0x000C_0x12_Message) evoHomeMessage).value;
			break;
		case _18_042F:
			theArray = ((EvoHome_0x18_0x042F_0x08_Message) evoHomeMessage).value;
			break;
		case _18_10E0:
			theArray = ((EvoHome_0x18_0x10E0_0x26_Message) evoHomeMessage).value;
			break;
		case _18_1100:
			theArray = ((EvoHome_0x18_0x1100_0x08_Message) evoHomeMessage).value;
			break;
		case _18_2E04_OPERATING_MODE:
			theArray = ((EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message) evoHomeMessage).value;
			break;
		case _18_3120:
			theArray = ((EvoHome_0x18_0x3120_0x07_Message) evoHomeMessage).value;
			break;
		case _1C_10E0:
			theArray = ((EvoHome_0x1C_0x10E0_0x26_Message) evoHomeMessage).value;
			break;
		case _28_0001_RADIO_TEST_REQUEST_FROM_MASTER:
			theArray = ((EvoHome_0x28_0x0001_0x05__RADIO_TEST_REQUEST_FROM_MASTER_Message) evoHomeMessage).value;
			break;
		case _3C_0100:
			theArray = ((EvoHome_0x3C_0x0100_0x05_Message) evoHomeMessage).value;
			break;
		case _3C_313F_RESPONSE_TO_0C_313F:
			theArray = ((EvoHome_0x3C_0x313F_0x09_RESPONSE_3C_313F_Message) evoHomeMessage).value;
			break;
		default:
			throw new RuntimeException("Unknown property: " + evoHomeMessage);
		}
		
		theArray[copyDataIndex++] = b;
		if (theArray.length == copyDataIndex) {
			parserListener.success(evoHomeMessage);
			state = State.PARSE_SUCCESS;
		}

	}

	private void decodeCommand() {
		switch (getShortValue()) {
		case 0x0001:
			evoHomeCommand = EvoHomeCommand._0001;
			break;
		case 0x0004:
			evoHomeCommand = EvoHomeCommand._0004;
			break;
		case 0x0005:
			evoHomeCommand = EvoHomeCommand._0005;
			break;
		case 0x0008:
			evoHomeCommand = EvoHomeCommand._0008;
			break;
		case 0x0009:
			evoHomeCommand = EvoHomeCommand._0009;
			break;
		case 0x000A:
			evoHomeCommand = EvoHomeCommand._000A;
			break;
		case 0x000C:
			evoHomeCommand = EvoHomeCommand._000C;
			break;
		case 0x000E:
			evoHomeCommand = EvoHomeCommand._000E;
			break;
		case 0x0016:
			evoHomeCommand = EvoHomeCommand._0016;
			break;
		case 0x0100:
			evoHomeCommand = EvoHomeCommand._0100;
			break;
		case 0x042f:
			evoHomeCommand = EvoHomeCommand._042F;
			break;
		case 0x1060:
			evoHomeCommand = EvoHomeCommand._1060;
			break;
		case 0x10e0:
			evoHomeCommand = EvoHomeCommand._10E0;
			break;
		case 0x1100:
			evoHomeCommand = EvoHomeCommand._1100;
			break;
		case 0x12b0:
			evoHomeCommand = EvoHomeCommand._12B0;
			break;
		case 0x1f09:
			evoHomeCommand = EvoHomeCommand._1F09;
			break;
		case 0x1fc9:
			evoHomeCommand = EvoHomeCommand._1FC9;
			break;
		case 0x2309:
			evoHomeCommand = EvoHomeCommand._2309;
			break;
		case 0x2349:
			evoHomeCommand = EvoHomeCommand._2349;
			break;
		case 0x2e04:
			evoHomeCommand = EvoHomeCommand._2E04;
			break;
		case 0x30c9:
			evoHomeCommand = EvoHomeCommand._30C9;
			break;
		case 0x3120:
			evoHomeCommand = EvoHomeCommand._3120;
			break;
		case 0x313f:
			evoHomeCommand = EvoHomeCommand._313F;
			break;
		case 0x3150:
			evoHomeCommand = EvoHomeCommand._3150;
			break;
		case 0x3b00:
			evoHomeCommand = EvoHomeCommand._3B00;
			break;
		default:
			throw new IllegalArgumentException(String.format("Unknown command 0x%04x", getShortValue()));
		}

	}

	/**
	 * Build the message. 
	 * To build the message one needs the header , the command and sometime the length.
	 * Here the appropriate stack size must be set ant the state.
	 * @param length
	 */
	private void buildMessage(short length) {
		switch (evoHomeHeaderByte) {
		case _0C:
			switch (evoHomeCommand) {
			case _0004:
				checkLength(0x02, length, "EvoHome_0x0C_0x0004_0x02_Message");
				evoHomeMessage = new EvoHome_0x0C_0x0004_0x02_Message();
				state = State.COLLECT_0C_0004_02_ZONEID;
				break;
			case _000A:
				checkLength(0x01, length, "EvoHome_0x0C_0x000A_0x01_Message");
				evoHomeMessage = new EvoHome_0x0C_0x000A_0x01_Message();
				setStackSize(1);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _0016:
				checkLength(0x02, length, "EvoHome_0x0C_0x0016_0x02_Message");
				evoHomeMessage = new EvoHome_0x0C_0x0016_0x02_Message();
				setStackSize(2);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _0100:
				checkLength(0x05, length, "EvoHome_0x0C_0x0100_0x05_Message");
				evoHomeMessage = new EvoHome_0x0C_0x0100_0x05_Message();
				state = State.COLLECT_DATA_BYTES;
				break;
			case _1F09:
				checkLength(0x01, length, "EvoHome_0x0C_0x1F09_0x01_Message");
				evoHomeMessage = new EvoHome_0x0C_0x1F09_0x01_REQUEST_0C_1F09_Message();
				setStackSize(1);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _2309:
				checkLength(0x01, length, "EvoHome_0x0C_0x2309_0x01_Message");
				evoHomeMessage = new EvoHome_0x0C_0x2309_0x01_Message();
				setStackSize(1);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _313F:
				checkLength(0x01, length, "EvoHome_0x0C_0x313F_0x01_Message");
				evoHomeMessage = new EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message();
				setStackSize(1);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			default:
				throw new IllegalArgumentException(String.format("Can't handle header %s Command %s and length %d",
						evoHomeHeaderByte, evoHomeCommand, length));
			}
			break;
		case _18:
			switch (evoHomeCommand) {
			case _0004:
				checkLength(0x16, length, "EvoHome_0x18_0x0004_0x16_Message");
				evoHomeMessage = new EvoHome_0x18_0x0004_0x16_Message();
				_18_0004_16_Parser.init(length);
				state = State.PARSE_XX_0004_16_DATA;
				break;
			case _0005:
				//checkLength(0x04, length, "EvoHome_0x18_0x0005_0x04_Message");
				evoHomeMessage = new EvoHome_0x18_0x0005_0xXX_Message(length);
				state = State.COLLECT_DATA_BYTES;
				break;
			case _0008:
				checkLength(0x02, length, "EvoHome_0x18_0x0008_0x02_Message");
				evoHomeMessage = new EvoHome_0x18_0x0008_0x02_Message();
				setStackSize(2);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _0009:
				checkLength(0x03, length, "EvoHome_0x18_0x0009_0x03_Message");
				evoHomeMessage = new EvoHome_0x18_0x0009_0x03_Message();
				setStackSize(3);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _000A:
				//TODO ??? multiple of 6 ??? checkLength(0x06, length, "EvoHome_0x18_0x000A_0x06_Message");
				evoHomeMessage = new EvoHome_0x18_0x000A_0xXX_ZONES_PARAMS_Message();
				zonesParamParser.init(length);
				state = State.PARSE_ZONES_PARAMS_ELEMENTS;
				break;
			case _000C:
				checkLength(0x12, length, "EvoHome_0x18_0x000C_0x12_Message");
				evoHomeMessage = new EvoHome_0x18_0x000C_0x12_Message();
				state = State.COLLECT_DATA_BYTES;
				break;
			case _000E:
				checkLength(0x03, length, "EvoHome_0x18_0x000E_0x03_Message");
				evoHomeMessage = new EvoHome_0x18_0x000E_0x03_Message();
				setStackSize(3);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _042F:
				checkLength(0x08, length, "EvoHome_0x18_0x042F_0x08_Message");
				evoHomeMessage = new EvoHome_0x18_0x042F_0x08_Message();
				state = State.COLLECT_DATA_BYTES;
				break;
			case _1060:
				checkLength(0x03, length, "EvoHome_0x18_0x1060_0x03_Message");
				evoHomeMessage = new EvoHome_0x18_0x1060_0x03_Message();
				setStackSize(3);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _10E0:
				checkLength(0x26, length, "EvoHome_0x18_0x10E0_0x26_Message");
				evoHomeMessage = new EvoHome_0x18_0x10E0_0x26_Message();
				state = State.COLLECT_DATA_BYTES;
				break;
			case _1100:
				checkLength(0x08, length, "EvoHome_0x18_0x1100_0x08_Message");
				evoHomeMessage = new EvoHome_0x18_0x1100_0x08_Message();
				state = State.COLLECT_DATA_BYTES;
				break;
			case _12B0:
				checkLength(0x03, length, "EvoHome_0x18_0x12B0_0x03_Message");
				evoHomeMessage = new EvoHome_0x18_0x12B0_0x03_Message();
				setStackSize(3);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _1F09:
				checkLength(0x03, length, "EvoHome_0x18_0x1F09_0x03_Message");
				evoHomeMessage = new EvoHome_0x18_0x1F09_0x03_BROADCAST_18_1F09_Message();
				setStackSize(3);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _1FC9:
				//TODO checkLength(0x12, length, "EvoHome_0x1C_0x1FC9_0x06_Message");
				evoHomeMessage = new EvoHome_0x18_0x1FC9_0x12_Message();
				_XX_1FC9_XX_Parser.init(length);
				state = State.PARSE_XX_1FC9_XX_ELEMENTS;
				break;
			case _2309:
				//TODO ??? multiple of 3 ??? checkLength(0x03, length, "EvoHome_0x18_0x2309_0x02_Message");
				evoHomeMessage = new EvoHome_0x18_0x2309_0xXX_ROOM_DESIRED_TEMP_Message();
				zoneTemperatureParser.init(length);
				state = State.PARSE_ZONE_TEMPERATURE_ELEMENTS;
				break;
			case _2349:
				if (length == 0x0D) {
					evoHomeMessage = new EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message();
					state = State.COLLECT_ZONE_SETPOINT_UNTIL_ZONE;
				} else if (length == 0x07) {
					evoHomeMessage = new EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message();
					state = State.COLLECT_ZONE_SETPOINT_PERMANENT_ZONE;
				} else {
					throw new RuntimeException("Unknown length for 0x18_0x2349: " + length);
				}
				break;
			case _2E04:
				checkLength(0x08, length, "EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message");
				evoHomeMessage = new EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message();
				state = State.COLLECT_18_2E04_OPERATING_MODE;
				break;
			case _30C9:
				//TODO ??? multiple of 3 ??? checkLength(0x03, length, "EvoHome_0x18_0x2309_0x02_Message");
				evoHomeMessage = new EvoHome_0x18_0x30C9_0xXX_ROOM_MEASURED_TEMP_Message();
				zoneTemperatureParser.init(length);
				state = State.PARSE_ZONE_TEMPERATURE_ELEMENTS;
				break;
			case _3120:
				checkLength(0x07, length, "EvoHome_0x18_0x3120_0x07_Message");
				evoHomeMessage = new EvoHome_0x18_0x3120_0x07_Message();
				state = State.COLLECT_DATA_BYTES;
				break;
			case _3150:
				checkLength(0x02, length, "EvoHome_0x18_0x3150_0x02_Message");
				evoHomeMessage = new EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message();
				state = State.COLLECT_HEAT_DEMAND_ZONE;
				break;
			case _3B00:
				checkLength(0x02, length, "EvoHome_0x18_0x3B00_0x02_Message");
				evoHomeMessage = new EvoHome_0x18_0x3B00_0x02_Message();
				setStackSize(2);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			default:
				throw new IllegalArgumentException(String.format("Can't handle header %s Command %s and length %d",
						evoHomeHeaderByte, evoHomeCommand, length));
			}
			break;
		case _1C:
			switch (evoHomeCommand) {
			case _1FC9:
				//TODO checkLength(0x06, length, "EvoHome_0x1C_0x1FC9_0x06_Message");
				evoHomeMessage = new EvoHome_0x1C_0x1FC9_0xXX_Message();
				_XX_1FC9_XX_Parser.init(length);
				state = State.PARSE_XX_1FC9_XX_ELEMENTS;
				break;
			case _10E0:
				checkLength(0x26, length, "EvoHome_0x18_0x10E0_0x26_Message");
				evoHomeMessage = new EvoHome_0x1C_0x10E0_0x26_Message();
				state = State.COLLECT_DATA_BYTES;
				break;
			case _2309:
				//TODO ??? multiple of 3 ??? checkLength(0x03, length, "EvoHome_0x1C_0x2309_0x02_Message");
				evoHomeMessage = new EvoHome_0x1C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message();
				zoneTemperatureParser.init(length);
				state = State.PARSE_ZONE_TEMPERATURE_ELEMENTS;
				break;
			default:
				throw new IllegalArgumentException(String.format("Can't handle header %s Command %s and length %d",
						evoHomeHeaderByte, evoHomeCommand, length));
			}
			break;
		case _28:
			switch (evoHomeCommand) {
			case _0001:
				checkLength(0x05, length, "EvoHome_0x28_0x0001_0x05");
				evoHomeMessage = new EvoHome_0x28_0x0001_0x05__RADIO_TEST_REQUEST_FROM_MASTER_Message();
				state = State.COLLECT_DATA_BYTES;
				break;
			case _1F09:
				checkLength(0x03, length, "EvoHome_0x28_0x1F09_0x03_Message");
				evoHomeMessage = new EvoHome_0x28_0x1F09_0x03_Message();
				setStackSize(3);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			default:
				throw new IllegalArgumentException(String.format("Can't handle header %s Command %s and length %d",
						evoHomeHeaderByte, evoHomeCommand, length));
			}
			break;
		case _2C:
			switch (evoHomeCommand) {
			case _1FC9:
				//TODO checkLength(0x06, length, "EvoHome_0x2C_0x1FC9_0x06_Message");
				evoHomeMessage = new EvoHome_0x2C_0x1FC9_0xXX_Message();
				_XX_1FC9_XX_Parser.init(length);
				state = State.PARSE_XX_1FC9_XX_ELEMENTS;
				break;
			case _2309:
				//TODO ??? multiple of 3 ??? checkLength(0x03, length, "EvoHome_0x2C_0x2309_0x02_Message");
				evoHomeMessage = new EvoHome_0x2C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message();
				zoneTemperatureParser.init(length);
				state = State.PARSE_ZONE_TEMPERATURE_ELEMENTS;
				break;
default:
				throw new IllegalArgumentException(String.format("Can't handle header %s Command %s and length %d",
						evoHomeHeaderByte, evoHomeCommand, length));
			}
			break;
		case _3C:
			switch (evoHomeCommand) {
			case _0004:
				checkLength(0x16, length, "EvoHome_0x18_0x0004_0x16_Message");
				evoHomeMessage = new EvoHome_0x3C_0x0004_0x16_Message();
				_18_0004_16_Parser.init(length);
				state = State.PARSE_XX_0004_16_DATA;
				break;
			case _0016:
				checkLength(0x02, length, "EvoHome_0x3C_0x0016_0x02_Message");
				evoHomeMessage = new EvoHome_0x3C_0x0016_0x02_Message();
				setStackSize(2);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _000A:
				//TODO ??? multiple of 6 ??? checkLength(0x06, length, "EvoHome_0x3C_0x000A_0xXX_ZONES_PARAMS_Message");
				evoHomeMessage = new EvoHome_0x3C_0x000A_0xXX_ZONES_PARAMS_Message();
				zonesParamParser.init(length);
				state = State.PARSE_ZONES_PARAMS_ELEMENTS;
				break;
			case _0100:
				checkLength(0x05, length, "EvoHome_0x3C_0x0100_0x05_Message");
				evoHomeMessage = new EvoHome_0x3C_0x0100_0x05_Message();
				state = State.COLLECT_DATA_BYTES;
				break;
			case _1F09:
				checkLength(0x03, length, "EvoHome_0x3C_0x1F09_0x03_Message");
				evoHomeMessage = new EvoHome_0x3C_0x1F09_0x03_RESPONSE_3C_1F09_Message();
				setStackSize(3);
				state = State.COLLECT_SINGLE_VALUE;
				break;
			case _2309:
				//TODO ??? multiple of 3 ??? checkLength(0x03, length, "EvoHome_0x3C_0x2309_0x02_Message");
				evoHomeMessage = new EvoHome_0x3C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message();
				zoneTemperatureParser.init(length);
				state = State.PARSE_ZONE_TEMPERATURE_ELEMENTS;
				break;
			case _313F:
				checkLength(0x09, length, "EvoHome_0x3C_0x313F_0x09_Message");
				evoHomeMessage = new EvoHome_0x3C_0x313F_0x09_RESPONSE_3C_313F_Message();
				state = State.COLLECT_DATA_BYTES;
				break;
			default:
				throw new IllegalArgumentException(String.format("Can't handle header %s Command %s and length %d",
						evoHomeHeaderByte, evoHomeCommand, length));
			}
			break;
		default:
			throw new IllegalArgumentException(String.format("Can't handle header %s Command %s and length %d",
					evoHomeHeaderByte, evoHomeCommand, length));
		}

		evoHomeProperty = evoHomeMessage.property;
		switch (evoHomeHeaderByte) {
		case _0C: 
		case _18: 
		case _1C: 
		case _28: 
		case _2C: 
		case _3C: 
			final EvoHomeDeviceMessage msg = (EvoHomeDeviceMessage) evoHomeMessage;
			msg.deviceId1 = deviceId1;
			msg.deviceId2 = deviceId2;
			break;
		default:
			throw new RuntimeException("Unexpected Header byte " + evoHomeHeaderByte);
		}
	}

	private void checkLength(int expectedLength, int actualLength, String className) {
		if (expectedLength != actualLength) {
			throw new RuntimeException(String.format("Expected length of %s differs, expected %d, but was %d ", className, expectedLength, actualLength));
		}
	}

	private void decodeHeader(byte b) {
		switch (b) {
		case 0x0C:
			evoHomeHeaderByte = EvoHomeHeaderByte._0C;
			break;
		case 0x18:
			evoHomeHeaderByte = EvoHomeHeaderByte._18;
			break;
		case 0x1C:
			evoHomeHeaderByte = EvoHomeHeaderByte._1C;
			break;
		case 0x28:
			evoHomeHeaderByte = EvoHomeHeaderByte._28;
			break;
		case 0x2C:
			evoHomeHeaderByte = EvoHomeHeaderByte._2C;
			break;
		case 0x3C:
			evoHomeHeaderByte = EvoHomeHeaderByte._3C;
			break;
		default:
			throw new IllegalArgumentException(String.format("Unknown header 0x%02x", b));
		}
	}

	private void setSingleValueAndNotify() {
		switch (evoHomeProperty) {
		case _0C_0016:
			((EvoHome_0x0C_0x0016_0x02_Message) evoHomeMessage).value = getIntValue();
			break;
		case _0C_000A:
			((EvoHome_0x0C_0x000A_0x01_Message) evoHomeMessage).value = getByteValue();
			break;
		case _0C_2309:
			((EvoHome_0x0C_0x2309_0x01_Message) evoHomeMessage).value = getByteValue();
			break;
		case _0C_1F09_REQUEST_FOR_3C_1F09_:
			((EvoHome_0x0C_0x1F09_0x01_REQUEST_0C_1F09_Message) evoHomeMessage).value = getByteValue();
			break;
		case _0C_313F_REQUEST_FOR_3C_313F:
			((EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message) evoHomeMessage).value = getByteValue();
			break;
		case _18_0008:
			((EvoHome_0x18_0x0008_0x02_Message) evoHomeMessage).value = getIntValue();
			break;
		case _18_0009:
			((EvoHome_0x18_0x0009_0x03_Message) evoHomeMessage).value = getIntValue();
			break;
		case _18_000E:
			((EvoHome_0x18_0x000E_0x03_Message) evoHomeMessage).value = getIntValue();
			break;
		case _18_1060:
			((EvoHome_0x18_0x1060_0x03_Message) evoHomeMessage).value = getIntValue();
			break;
		case _18_12B0:
			((EvoHome_0x18_0x12B0_0x03_Message) evoHomeMessage).value = getIntValue();
			break;
		case _18_1F09_BROADCAST_18_1F09_:
			((EvoHome_0x18_0x1F09_0x03_BROADCAST_18_1F09_Message) evoHomeMessage).value = getIntValue();
			break;
		case _18_3B00:
			((EvoHome_0x18_0x3B00_0x02_Message) evoHomeMessage).value = getIntValue();
			break;
		case _28_1F09:
			((EvoHome_0x28_0x1F09_0x03_Message) evoHomeMessage).unknown  = getIntValue();
			break;
		case _3C_0016:
			((EvoHome_0x3C_0x0016_0x02_Message) evoHomeMessage).value = getIntValue();
			break;
		case _3C_1F09_RESPONSE_TO_0C_1F09:
			((EvoHome_0x3C_0x1F09_0x03_RESPONSE_3C_1F09_Message) evoHomeMessage).value = getIntValue();
			break;
		default:
			throw new RuntimeException("Unhandled Property: " + evoHomeProperty);
		}
		parserListener.success(evoHomeMessage);
	}
}
