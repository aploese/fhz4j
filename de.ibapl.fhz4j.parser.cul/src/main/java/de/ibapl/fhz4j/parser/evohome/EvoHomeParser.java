/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2019-2024, Arne Plöse and individual contributors as indicated
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

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.parser.api.AbstractParser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.evohome.DeviceId;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeCommand;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeDeviceMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgParam0;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgType;
import de.ibapl.fhz4j.protocol.evohome.ZoneTemperature;
import de.ibapl.fhz4j.protocol.evohome.messages.AbstractControllerModeMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.AbstractControllerModePayloadMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.AbstractRfBindPayloadMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.AbstractWindowSensorPayloadMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.AbstractZoneSetpointOverrideMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.AbstractZoneSetpointPayloadMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.AbstractZoneTemperaturePayloadMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ActuatorSyncInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.BoilerRelayInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ControllerModeInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.DeviceBatteryStatusInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.DeviceInformationInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.LocalizationRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.LocalizationResponseMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.RelayFailsaveInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.RelayHeatDemandInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.RfBindInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.RfBindWriteMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.RfCheckWriteMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.RfSignalTestRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.RfSignalTestResponseMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.SystemSynchronizationPayloadMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.SystemSynchronizationRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.SystemTimestampRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.SystemTimestampResponseMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.T87RF_Startup_0x000E_InformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.T87RF_Startup_0x042F_InformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.Unknown_0x3120InformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.WindowSensorInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.WindowSensorRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.WindowSensorResponseMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneActuatorsInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneActuatorsRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneConfigPayloadMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneConfigRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneHeatDemandInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneManagementInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneNamePayloadMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneNameRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointOverrideInformationMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointOverrideRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointOverrideResponseMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointOverrideWriteMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointResponseMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointWriteMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneTemperatureInformationMessage;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Logger;

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
        PARSE__RELAY_HEAT_DEMAND__DOMAIN_ID,
        /**
         *
         */
        PARSE__RELAY_HEAT_DEMAND__DEMAND,
        /**
         *
         */
        PARSE__RELAY_FAILSAVE__DOMAIN_ID,
        /**
         *
         */
        COLLECT__RELAY_FAILSAVE__VALUE,
        /**
         *
         */
        ZONE_TEMPERATURE__PAYLOAD__ZONE_ID,
        ZONE_TEMPERATURE__PAYLOAD__TEMPERATURE,
        /**
         *
         */
        PARSE__SYSTEM_SYNCHRONIZATION__PAYLOAD__DEVICE_ID,
        /**
         *
         */
        PARSE__SYSTEM_SYNCHRONIZATION__PAYLOAD__COUNTDOWN,
        /**
         *
         */
        CONTROLLER_MODE__ALL__MODE,
        CONTROLLER_MODE__PAYLOAD__TIME,
        CONTROLLER_MODE__PAYLOAD__DATE,
        CONTROLLER_MODE__PAYLOAD__PROGRAM_TYPE,
        /**
         *
         */
        RF_BIND__PAYLOAD__ELEMENTS__ZONE_ID,
        RF_BIND__PAYLOAD__ELEMENTS__COMMAND,
        RF_BIND__PAYLOAD__ELEMENTS__DEVICE_ID,
        /**
         *
         */
        COLLECT__ZONE_NAME__REQUEST__ZONEID,
        /**
         *
         */
        COLLECT_ZONE_NAME_REQ_UNKNOWN,
        /**
         *
         */
        PARSE__ZONE_NAME__PAYLOAD__DATA,
        /**
         *
         */
        ZONE_HEAT_DEMAND__INFORMATION__ZONE_ID,
        ZONE_HEAT_DEMAND__INFORMATION__HEAT_DEMAND,
        /**
         *
         */
        ZONE_SETPOINT_OVERRIDE__ALL__ZONE_ID,
        /**
         *
         */
        ZONE_SETPOINT_OVERRIDE__ALL__TEMPERATURE,
        /**
         *
         */
        ZONE_SETPOINT_OVERRIDE__ALL__ZONE_MODE,
        /**
         *
         */
        ZONE_SETPOINT_OVERRIDE__ALL__COUNTDOWN,
        /**
         *
         */
        ZONE_SETPOINT_OVERRIDE__ALL_TIME,
        /**
         *
         */
        ZONE_SETPOINT_OVERRIDE__ALL_DATE,
        /**
         *
         */
        PARSE_ZONE_CONFIG_ELEMENTS,
        /**
         *
         */
        PARSE_LOCALIZATION_ELEMENTS,
        /**
         *
         */
        PARSE_SUCCESS,
        /**
         *
         */
        PARSE_ERROR,
        ZONE_ACTUATORS__INFORMATION__ZONE_IDX,
        ZONE_ACTUATORS__INFORMATION__UNKNOWN0,
        ZONE_ACTUATORS__INFORMATION__DEVICEID,
        ZONE_ACTUATORS__REQUEST__ZONE_IDX,
        ZONE_ACTUATORS__REQUEST__UNKNOWN0,
        DEVICE_BATTERY_STATUS__INFORMATION__ZONE_ID,
        DEVICE_BATTERY_STATUS__INFORMATION__LEVEL,
        DEVICE_BATTERY_STATUS__INFORMATION__UNKNOWN0,
        DEVICE_INFORMATION__INFORMATION__UNKNOWN0,
        DEVICE_INFORMATION__INFORMATION__FIRMWARE,
        DEVICE_INFORMATION__INFORMATION__MANUFACTURED,
        DEVICE_INFORMATION__INFORMATION__DESCRIPTION,
        BOILER_RELAY_INFORMATION__X__DOMAIN_ID,
        BOILER_RELAY_INFORMATION__X__CYCLE_RATE,
        BOILER_RELAY_INFORMATION__X__MINIMUM_ON_TIME,
        BOILER_RELAY_INFORMATION__X__MINIMUM_OFF_TIME,
        BOILER_RELAY_INFORMATION__X__UNKNOWN0,
        BOILER_RELAY_INFORMATION__X__PROPORTIONAL_BANDWITH,
        BOILER_RELAY_INFORMATION__X__UNKNOWN1,
        WINDOW_SENSOR__REQUEST__ZONE_ID,
        WINDOW_SENSOR__PAYLOAD__ZONE_ID,
        WINDOW_SENSOR__PAYLOAD__UNKNOWN0,
        ZONE_SETPOINT__REQUEST__ZONE_ID,
        ZONE_SETPOINT__PAYLOAD__ZONE_TEMPERATURES__ZONE_ID,
        ZONE_SETPOINT__PAYLOAD__ZONE_TEMPERATURES__SETPOINT,
        UNKNOWN_3120__INFORMATION__UNUSED0,
        UNKNOWN_3120__INFORMATION__FIXED1,
        UNKNOWN_3120__INFORMATION__UNUSED2,
        UNKNOWN_3120__INFORMATION__FIXED3,
        SYSTEM_TIMESTAMP__REQUEST__ZONE_ID,
        SYSTEM_TIMESTAMP__RESPONSE__ZONE_ID,
        SYSTEM_TIMESTAMP__RESPONSE__DIRECTION,
        SYSTEM_TIMESTAMP__RESPONSE__TIMESTAMP,
        ACTUATOR_SYNC__INFORMATION__DOMAIN_ID,
        ACTUATOR_SYNC__INFORMATION__STATE,
        SYSTEM_SYNCHRONIZATION__REQUEST__DOMAIN_ID;
    }
    //Just cache this ...
    private final static BigDecimal ONE_HUNDRED = new BigDecimal(100.0);
    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private final ParserListener<EvoHomeMessage> parserListener;
    private State state;
    //the remaining lenght of data must be updated individually
    private short remainingDataLength;
    //the position of data i.e. in strings or byte arrays or times during parsing of that data structure
    private final ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder();
    private final DateParser dateParser = new DateParser();
    private final TimeParser timeParser = new TimeParser();
    private final TimeStampParser timeStampParser = new TimeStampParser();
    private final StringBuilder stringBuilder = new StringBuilder();
    private final LocalizationParser localizationParser = new LocalizationParser();
    private final ZonesParamParser zonesParamParser = new ZonesParamParser();
    private final ZoneNameParser zoneNameParser = new ZoneNameParser();
    private EvoHomeMessage evoHomeMessage;
    private EvoHomeMsgType evoHomeMsgType;
    private EvoHomeMsgParam0 evoHomeMsgParam0;
    private EvoHomeCommand evoHomeCommand;
    private byte evoHomeMsgParam1;
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
        evoHomeMsgType = null;
        evoHomeMsgParam0 = null;
        deviceId1 = 0;
        deviceId2 = 0;
        copyDataIndex = 0;
    }

    private void success() {
        parserListener.success(evoHomeMessage);
        state = State.PARSE_SUCCESS;
    }

    private BigDecimal getTemperature() {
        if (getShortValue() == 0x7FFF) {
            return null;
        } else {
            return new BigDecimal(getShortValue()).divide(ONE_HUNDRED);
        }
    }

    @Override
    public void parse(byte b) {
        try {
            switch (state) {
                case COLLECT_HEADER -> {
                    decodeHeader(b);
                    setStackSize(3);
                    state = State.COLLECT_DEVICEID_1;
                }
                case COLLECT_DEVICEID_1 -> {
                    if (push(b)) {
                        deviceId1 = getIntValue();
                        setStackSize(3);
                        state = State.COLLECT_DEVICEID_2;
                    }
                }
                case COLLECT_DEVICEID_2 -> {
                    if (push(b)) {
                        deviceId2 = getIntValue();
                        setStackSize(2);
                        state = State.COLLECT_COMMAND;
                    }
                }
                case COLLECT_COMMAND -> {
                    if (push(b)) {
                        decodeCommand();
                        state = State.COLLECT_DATA_LENGTH;
                    }
                }
                case COLLECT_DATA_LENGTH -> {
                    remainingDataLength = (short) (b & 0xff);
                    buildMessage();
                }
                case COLLECT_DATA_BYTES ->
                    setByteArrayValue(b);
                case CONTROLLER_MODE__ALL__MODE -> {
                    remainingDataLength--;
                    ((AbstractControllerModeMessage) evoHomeMessage).mode = switch (b & 0xff) {
                        case 0x00 ->
                            AbstractControllerModeMessage.Mode.NORMAL;
                        case 0x01 ->
                            AbstractControllerModeMessage.Mode.HEATING_OFF;
                        case 0x02 ->
                            AbstractControllerModeMessage.Mode.ECONOMY;
                        case 0x03 ->
                            AbstractControllerModeMessage.Mode.AWAY;
                        case 0x04 ->
                            AbstractControllerModeMessage.Mode.EXCEPTION_DAY;
                        case 0x07 ->
                            AbstractControllerModeMessage.Mode.SPECIAL_PROGRAMME;
                        default ->
                            throw new IllegalStateException("Unknown Mode: " + b);
                    };
                    if (remainingDataLength == 0) {
                        success();
                    } else {
                        timeParser.init();
                        state = State.CONTROLLER_MODE__PAYLOAD__TIME;
                    }
                }
                case CONTROLLER_MODE__PAYLOAD__TIME -> {
                    remainingDataLength--;
                    timeParser.parse(b);
                    if (timeParser.state == TimeParser.State.PARSE_SUCCESS) {
                        dateParser.init();
                        state = State.CONTROLLER_MODE__PAYLOAD__DATE;
                    } else if (timeParser.state == TimeParser.State.PARSE_ERROR) {
                        throw new IllegalStateException();
                    }
                }
                case CONTROLLER_MODE__PAYLOAD__DATE -> {
                    remainingDataLength--;
                    dateParser.parse(b);
                    if (dateParser.state == DateParser.State.PARSE_SUCCESS) {
                        if (timeParser.isFF_FF() && dateParser.isFF_FF_FFFF()) {
                            // all fields are ff so there is nothing ...
                            ((AbstractControllerModePayloadMessage) evoHomeMessage).dateTime = null;
                        } else {
                            ((AbstractControllerModePayloadMessage) evoHomeMessage).dateTime = LocalDateTime.of(dateParser.getDate(), timeParser.getTime());
                        }
                        state = State.CONTROLLER_MODE__PAYLOAD__PROGRAM_TYPE;
                    } else if (dateParser.state == DateParser.State.PARSE_ERROR) {
                        throw new IllegalStateException();
                    }
                }
                case CONTROLLER_MODE__PAYLOAD__PROGRAM_TYPE -> {
                    remainingDataLength--;
                    ((AbstractControllerModePayloadMessage) evoHomeMessage).programm_type = switch (b & 0xff) {
                        case 0x00 ->
                            AbstractControllerModePayloadMessage.ProgrammType.PERMANENT;
                        case 0x01 ->
                            AbstractControllerModePayloadMessage.ProgrammType.TIMED;
                        default ->
                            throw new IllegalStateException();
                    };
                    success();
                }
                case PARSE__RELAY_HEAT_DEMAND__DOMAIN_ID -> {
                    ((RelayHeatDemandInformationMessage) evoHomeMessage).domainId = b;
                    state = State.PARSE__RELAY_HEAT_DEMAND__DEMAND;
                }
                case PARSE__RELAY_HEAT_DEMAND__DEMAND -> {
                    ((RelayHeatDemandInformationMessage) evoHomeMessage).demand = (float) ((b & 0x00FF) / 2.0);
                    success();
                }
                case PARSE__RELAY_FAILSAVE__DOMAIN_ID -> {
                    ((RelayFailsaveInformationMessage) evoHomeMessage).domainId = b;
                    setStackSize(2);
                    state = State.COLLECT__RELAY_FAILSAVE__VALUE;
                }
                case COLLECT__RELAY_FAILSAVE__VALUE -> {
                    if (push(b)) {
                        ((RelayFailsaveInformationMessage) evoHomeMessage).value = getShortValue();
                        success();
                    }
                }
                case PARSE__SYSTEM_SYNCHRONIZATION__PAYLOAD__DEVICE_ID -> {
                    ((SystemSynchronizationPayloadMessage) evoHomeMessage).deviceId = b;
                    setStackSize(2);
                    state = State.PARSE__SYSTEM_SYNCHRONIZATION__PAYLOAD__COUNTDOWN;
                }
                case PARSE__SYSTEM_SYNCHRONIZATION__PAYLOAD__COUNTDOWN -> {
                    if (push(b)) {
                        ((SystemSynchronizationPayloadMessage) evoHomeMessage).countdown = getShortValue();
                        success();
                    }
                }
                case ZONE_TEMPERATURE__PAYLOAD__ZONE_ID -> {
                    remainingDataLength--;
                    ((AbstractZoneTemperaturePayloadMessage) evoHomeMessage).zoneTemperatures.add(new ZoneTemperature(b));
                    setStackSize(2);
                    state = State.ZONE_TEMPERATURE__PAYLOAD__TEMPERATURE;
                }
                case ZONE_TEMPERATURE__PAYLOAD__TEMPERATURE -> {
                    remainingDataLength--;
                    if (push(b)) {
                        ((AbstractZoneTemperaturePayloadMessage<?>) evoHomeMessage).zoneTemperatures.getLast().temperature = getTemperature();
                        if (remainingDataLength == 0) {
                            success();
                        } else {
                            state = State.ZONE_TEMPERATURE__PAYLOAD__ZONE_ID;
                        }
                    }
                }
                case RF_BIND__PAYLOAD__ELEMENTS__ZONE_ID -> {
                    remainingDataLength--;
                    ((AbstractRfBindPayloadMessage<?>) evoHomeMessage).elements.add(new AbstractRfBindPayloadMessage.Data());
                    ((AbstractRfBindPayloadMessage<?>) evoHomeMessage).elements.getLast().zoneId = b;
                    setStackSize(2);
                    state = State.RF_BIND__PAYLOAD__ELEMENTS__COMMAND;
                }
                case RF_BIND__PAYLOAD__ELEMENTS__COMMAND -> {
                    remainingDataLength--;
                    if (push(b)) {
                        ((AbstractRfBindPayloadMessage<?>) evoHomeMessage).elements.getLast().command = getShortValue();
                        setStackSize(3);
                        state = State.RF_BIND__PAYLOAD__ELEMENTS__DEVICE_ID;
                    }
                }
                case RF_BIND__PAYLOAD__ELEMENTS__DEVICE_ID -> {
                    remainingDataLength--;
                    if (push(b)) {
                        ((AbstractRfBindPayloadMessage<?>) evoHomeMessage).elements.getLast().deviceId = getIntValue();
                        if (remainingDataLength == 0) {
                            success();
                        } else {
                            state = State.RF_BIND__PAYLOAD__ELEMENTS__ZONE_ID;
                        }
                    }
                }
                case COLLECT__ZONE_NAME__REQUEST__ZONEID -> {
                    ((ZoneNameRequestMessage) evoHomeMessage).zoneId = b;
                    state = State.COLLECT_ZONE_NAME_REQ_UNKNOWN;
                }
                case COLLECT_ZONE_NAME_REQ_UNKNOWN -> {
                    ((ZoneNameRequestMessage) evoHomeMessage).unused = b;
                    if (((ZoneNameRequestMessage) evoHomeMessage).unused != 0) {
                        throw new IllegalStateException();
                    }
                    success();
                }
                case PARSE__ZONE_NAME__PAYLOAD__DATA -> {
                    zoneNameParser.parse(b);
                    if (zoneNameParser.state == ZoneNameParser.State.PARSE_SUCCESS) {
                        ((ZoneNamePayloadMessage) evoHomeMessage).zoneId = zoneNameParser.zoneId;
                        ((ZoneNamePayloadMessage) evoHomeMessage).unused = zoneNameParser.unused;
                        ((ZoneNamePayloadMessage) evoHomeMessage).zoneName = zoneNameParser.zoneNameBuilder.toString();
                        success();
                    }
                }
                case ZONE_HEAT_DEMAND__INFORMATION__ZONE_ID -> {
                    ((ZoneHeatDemandInformationMessage) evoHomeMessage).zoneId = b;
                    state = State.ZONE_HEAT_DEMAND__INFORMATION__HEAT_DEMAND;
                }
                case ZONE_HEAT_DEMAND__INFORMATION__HEAT_DEMAND -> {
                    ((ZoneHeatDemandInformationMessage) evoHomeMessage).heatDemand = (short) (b & 0xFF);
                    success();
                }
                case ZONE_SETPOINT_OVERRIDE__ALL__ZONE_ID -> {
                    remainingDataLength--;
                    ((AbstractZoneSetpointOverrideMessage) evoHomeMessage).zone_id = b;
                    setStackSize(2);
                    state = State.ZONE_SETPOINT_OVERRIDE__ALL__TEMPERATURE;
                }
                case ZONE_SETPOINT_OVERRIDE__ALL__TEMPERATURE -> {
                    remainingDataLength--;
                    if (push(b)) {
                        ((AbstractZoneSetpointOverrideMessage) evoHomeMessage).setpoint = new BigDecimal(getShortValue()).divide(ONE_HUNDRED);
                        state = State.ZONE_SETPOINT_OVERRIDE__ALL__ZONE_MODE;
                    }
                }
                case ZONE_SETPOINT_OVERRIDE__ALL__ZONE_MODE -> {
                    remainingDataLength--;
                    ((AbstractZoneSetpointOverrideMessage) evoHomeMessage).zone_mode = switch (b & 0xff) {
                        case 0x00 ->
                            AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.FOLLOW_SCHEDULE;
                        case 0x01 ->
                            AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.ADVANCED_OVERRIDE;
                        case 0x02 ->
                            AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.PERMANENT_OVERRIDE;
                        case 0x03 ->
                            AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.COUNTDOWN_OVERRIDE;
                        case 0x04 ->
                            AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.TEMPORARY_OVERRIDE;
                        default ->
                            throw new RuntimeException("Can't handle zone_mode from: " + b);
                    };
                    setStackSize(3);
                    state = State.ZONE_SETPOINT_OVERRIDE__ALL__COUNTDOWN;
                }
                case ZONE_SETPOINT_OVERRIDE__ALL__COUNTDOWN -> {
                    remainingDataLength--;
                    if (push(b)) {
                        ((AbstractZoneSetpointOverrideMessage) evoHomeMessage).countdown = Duration.ofMinutes(getIntValue());
                        if (remainingDataLength == 0) {
                            success();
                        } else {
                            timeParser.init();
                            state = State.ZONE_SETPOINT_OVERRIDE__ALL_TIME;
                        }
                    }
                }
                case ZONE_SETPOINT_OVERRIDE__ALL_TIME -> {
                    remainingDataLength--;
                    timeParser.parse(b);
                    if (timeParser.state == TimeParser.State.PARSE_SUCCESS) {
                        dateParser.init();
                        state = State.ZONE_SETPOINT_OVERRIDE__ALL_DATE;
                    } else if (timeParser.state == TimeParser.State.PARSE_ERROR) {
                        throw new IllegalStateException();
                    }
                }
                case ZONE_SETPOINT_OVERRIDE__ALL_DATE -> {
                    remainingDataLength--;
                    dateParser.parse(b);
                    if (dateParser.state == DateParser.State.PARSE_SUCCESS) {
                        ((AbstractZoneSetpointOverrideMessage) evoHomeMessage).time_until = LocalDateTime.of(dateParser.getDate(), timeParser.getTime());
                        success();
                    } else if (dateParser.state == DateParser.State.PARSE_ERROR) {
                        throw new IllegalStateException();
                    }
                }
                case PARSE_LOCALIZATION_ELEMENTS -> {
                    localizationParser.parse(b);
                    if (localizationParser.state == LocalizationParser.State.PARSE_SUCCESS) {
                        if (evoHomeMessage instanceof LocalizationRequestMessage localizationRequestMessage) {
                            localizationRequestMessage.unused0 = localizationParser.unused0;
                            localizationRequestMessage.language = localizationParser.nameBuilder.toString();
                            localizationRequestMessage.unused1 = localizationParser.unused1;
                        } else {
                            ((LocalizationResponseMessage) evoHomeMessage).unused0 = localizationParser.unused0;
                            ((LocalizationResponseMessage) evoHomeMessage).language = localizationParser.nameBuilder.toString();
                            ((LocalizationResponseMessage) evoHomeMessage).unused1 = localizationParser.unused1;
                        }
                        success();
                    }
                }
                case PARSE_ZONE_CONFIG_ELEMENTS -> {
                    zonesParamParser.parse(b);
                    if (zonesParamParser.state == ZonesParamParser.State.PARSE_SUCCESS) {
                        ((ZoneConfigPayloadMessage) evoHomeMessage).zones = zonesParamParser.zoneParams;
                        success();
                    }
                }
                case COLLECT_SINGLE_VALUE -> {
                    if (push(b)) {
                        setSingleValueAndNotify();
                        state = State.PARSE_SUCCESS;
                    }
                }
                case ZONE_ACTUATORS__INFORMATION__ZONE_IDX -> {
                    remainingDataLength--;
                    ((ZoneActuatorsInformationMessage<?>) evoHomeMessage).actuators.add(new ZoneActuatorsInformationMessage.ZoneActuator());
                    ((ZoneActuatorsInformationMessage<?>) evoHomeMessage).actuators.getLast().zoneIdx = b;
                    state = State.ZONE_ACTUATORS__INFORMATION__UNKNOWN0;
                }
                case ZONE_ACTUATORS__INFORMATION__UNKNOWN0 -> {
                    remainingDataLength--;
                    ((ZoneActuatorsInformationMessage<?>) evoHomeMessage).actuators.getLast().unknown0 = b;
                    setStackSize(4);
                    state = State.ZONE_ACTUATORS__INFORMATION__DEVICEID;
                }
                case ZONE_ACTUATORS__INFORMATION__DEVICEID -> {
                    remainingDataLength--;
                    if (push(b)) {
                        ((ZoneActuatorsInformationMessage<?>) evoHomeMessage).actuators.getLast().deviceId = new DeviceId(getShortValue());
                        if (remainingDataLength == 0) {
                            success();
                        } else {
                            state = State.ZONE_ACTUATORS__INFORMATION__ZONE_IDX;
                        }
                    }
                }
                case ZONE_ACTUATORS__REQUEST__ZONE_IDX -> {
                    remainingDataLength--;
                    ((ZoneActuatorsRequestMessage) evoHomeMessage).zoneIdx = b;
                    state = State.ZONE_ACTUATORS__REQUEST__UNKNOWN0;
                }
                case ZONE_ACTUATORS__REQUEST__UNKNOWN0 -> {
                    remainingDataLength--;
                    ((ZoneActuatorsRequestMessage) evoHomeMessage).unknown0 = b;
                    if (remainingDataLength == 0) {
                        success();
                    } else {
                        throw new IllegalStateException("ZONE_ACTUATORS__REQUEST__UNKNOWN0 has remaining data " + remainingDataLength);
                    }
                }
                case DEVICE_BATTERY_STATUS__INFORMATION__ZONE_ID -> {
                    remainingDataLength--;
                    ((DeviceBatteryStatusInformationMessage) evoHomeMessage).zone_id = b;
                    state = State.DEVICE_BATTERY_STATUS__INFORMATION__LEVEL;
                }
                case DEVICE_BATTERY_STATUS__INFORMATION__LEVEL -> {
                    remainingDataLength--;
                    ((DeviceBatteryStatusInformationMessage) evoHomeMessage).level = (float) ((b & 0xff) / 2.0);
                    state = State.DEVICE_BATTERY_STATUS__INFORMATION__UNKNOWN0;
                }
                case DEVICE_BATTERY_STATUS__INFORMATION__UNKNOWN0 -> {
                    remainingDataLength--;
                    ((DeviceBatteryStatusInformationMessage) evoHomeMessage).unknown0 = b;
                    if (remainingDataLength == 0) {
                        success();
                    } else {
                        throw new IllegalStateException("DEVICE_BATTERY_STATUS__INFORMATION__UNKNOWN0 has remaining data " + remainingDataLength);
                    }
                }
                case DEVICE_INFORMATION__INFORMATION__UNKNOWN0 -> {
                    remainingDataLength--;
                    if (byteArrayBuilder.push(b)) {
                        ((DeviceInformationInformationMessage) evoHomeMessage).unknown0 = byteArrayBuilder.getData();
                        dateParser.init();
                        state = State.DEVICE_INFORMATION__INFORMATION__FIRMWARE;
                    }
                }
                case DEVICE_INFORMATION__INFORMATION__FIRMWARE -> {
                    remainingDataLength--;
                    dateParser.parse(b);
                    if (dateParser.state == DateParser.State.PARSE_SUCCESS) {
                        ((DeviceInformationInformationMessage) evoHomeMessage).firmware = dateParser.getDate();
                        dateParser.init();
                        state = State.DEVICE_INFORMATION__INFORMATION__MANUFACTURED;
                    }
                }
                case DEVICE_INFORMATION__INFORMATION__MANUFACTURED -> {
                    remainingDataLength--;
                    dateParser.parse(b);
                    if (dateParser.state == DateParser.State.PARSE_SUCCESS) {
                        ((DeviceInformationInformationMessage) evoHomeMessage).manufactured = dateParser.getDate();
                        stringBuilder.setLength(0);
                        state = State.DEVICE_INFORMATION__INFORMATION__DESCRIPTION;
                    }
                }
                case DEVICE_INFORMATION__INFORMATION__DESCRIPTION -> {
                    remainingDataLength--;
                    if (b != 0) {//skip 0 bytes at the end the length is fixed to 20 chars?
                        stringBuilder.append((char) b);
                    }
                    if (remainingDataLength == 0) {
                        ((DeviceInformationInformationMessage) evoHomeMessage).description = stringBuilder.toString();
                        success();
                    }
                }
                case BOILER_RELAY_INFORMATION__X__DOMAIN_ID -> {
                    remainingDataLength--;
                    ((BoilerRelayInformationMessage) evoHomeMessage).domain_id = b;
                    state = State.BOILER_RELAY_INFORMATION__X__CYCLE_RATE;
                }
                case BOILER_RELAY_INFORMATION__X__CYCLE_RATE -> {
                    remainingDataLength--;
                    ((BoilerRelayInformationMessage) evoHomeMessage).cycle_rate = (float) ((b & 0xFF) / 4.0);
                    state = State.BOILER_RELAY_INFORMATION__X__MINIMUM_ON_TIME;
                }
                case BOILER_RELAY_INFORMATION__X__MINIMUM_ON_TIME -> {
                    remainingDataLength--;
                    ((BoilerRelayInformationMessage) evoHomeMessage).minimum_on_time = Duration.ofSeconds((b & 0xFF) * 15); // b is in min/4 so 60s/4 is 15s
                    state = State.BOILER_RELAY_INFORMATION__X__MINIMUM_OFF_TIME;
                }
                case BOILER_RELAY_INFORMATION__X__MINIMUM_OFF_TIME -> {
                    remainingDataLength--;
                    ((BoilerRelayInformationMessage) evoHomeMessage).minimum_off_time = Duration.ofSeconds((b & 0xFF) * 15); // b is in min/4 so 60s/4 is 15s
                    state = State.BOILER_RELAY_INFORMATION__X__UNKNOWN0;
                }
                case BOILER_RELAY_INFORMATION__X__UNKNOWN0 -> {
                    remainingDataLength--;
                    ((BoilerRelayInformationMessage) evoHomeMessage).unknown0 = b;
                    if (remainingDataLength == 0) {
                        success();
                    } else {
                        setStackSize(2);
                        state = State.BOILER_RELAY_INFORMATION__X__PROPORTIONAL_BANDWITH;
                    }
                }
                case BOILER_RELAY_INFORMATION__X__PROPORTIONAL_BANDWITH -> {
                    remainingDataLength--;
                    if (push(b)) {
                        ((BoilerRelayInformationMessage) evoHomeMessage).proportional_band_width = getShortValue();
                        state = State.BOILER_RELAY_INFORMATION__X__UNKNOWN1;
                    }
                }
                case BOILER_RELAY_INFORMATION__X__UNKNOWN1 -> {
                    remainingDataLength--;
                    ((BoilerRelayInformationMessage) evoHomeMessage).unknown1 = b;
                    if (remainingDataLength == 0) {
                        success();
                    }
                }
                case WINDOW_SENSOR__REQUEST__ZONE_ID -> {
                    remainingDataLength--;
                    ((WindowSensorRequestMessage) evoHomeMessage).zoneId = b;
                    success();
                }
                case WINDOW_SENSOR__PAYLOAD__ZONE_ID -> {
                    remainingDataLength--;
                    ((AbstractWindowSensorPayloadMessage) evoHomeMessage).zoneId = b;
                    setStackSize(2);
                    state = State.WINDOW_SENSOR__PAYLOAD__UNKNOWN0;
                }
                case WINDOW_SENSOR__PAYLOAD__UNKNOWN0 -> {
                    remainingDataLength--;
                    if (push(b)) {
                        ((AbstractWindowSensorPayloadMessage) evoHomeMessage).unknown0 = getShortValue();
                        success();
                    }
                }
                case ZONE_SETPOINT__REQUEST__ZONE_ID -> {
                    remainingDataLength--;
                    ((ZoneSetpointRequestMessage) evoHomeMessage).zoneId = b;
                    success();
                }
                case ZONE_SETPOINT__PAYLOAD__ZONE_TEMPERATURES__ZONE_ID -> {
                    remainingDataLength--;
                    ((AbstractZoneSetpointPayloadMessage) evoHomeMessage).zoneTemperatures.add(new ZoneTemperature(b));
                    setStackSize(2);
                    state = State.ZONE_SETPOINT__PAYLOAD__ZONE_TEMPERATURES__SETPOINT;
                }
                case ZONE_SETPOINT__PAYLOAD__ZONE_TEMPERATURES__SETPOINT -> {
                    remainingDataLength--;
                    if (push(b)) {
                        if (getShortValue() == 0x7FFF) {
                            ((AbstractZoneSetpointPayloadMessage<?>) evoHomeMessage).zoneTemperatures.getLast().temperature = null;
                        } else {
                            ((AbstractZoneSetpointPayloadMessage<?>) evoHomeMessage).zoneTemperatures.getLast().temperature = new BigDecimal(getShortValue()).divide(ONE_HUNDRED);
                        }
                        if (remainingDataLength == 0) {
                            success();
                        } else {
                            state = State.ZONE_SETPOINT__PAYLOAD__ZONE_TEMPERATURES__ZONE_ID;
                        }
                    }
                }
                case UNKNOWN_3120__INFORMATION__UNUSED0 -> {
                    remainingDataLength--;
                    ((Unknown_0x3120InformationMessage) evoHomeMessage).unused0 = b;
                    if (b != 0x00) {
                        throw new IllegalStateException("Unknown_0x3120InformationMessage.unused0 is set to " + b);
                    }
                    setStackSize(2);
                    state = State.UNKNOWN_3120__INFORMATION__FIXED1;
                }
                case UNKNOWN_3120__INFORMATION__FIXED1 -> {
                    remainingDataLength--;
                    if (push(b)) {
                        ((Unknown_0x3120InformationMessage) evoHomeMessage).fixed1 = getShortValue();
                        if (getShortValue() != 0x70B0) {
                            throw new IllegalStateException("Unknown_0x3120InformationMessage.fixed1 is set to " + getShortValue());
                        }
                        setStackSize(3);
                        state = State.UNKNOWN_3120__INFORMATION__UNUSED2;
                    }
                }
                case UNKNOWN_3120__INFORMATION__UNUSED2 -> {
                    remainingDataLength--;
                    if (push(b)) {
                        ((Unknown_0x3120InformationMessage) evoHomeMessage).unused2 = getIntValue();
                        if (getIntValue() != 0x000000) {
                            throw new IllegalStateException("Unknown_0x3120InformationMessage.unused2 is set to " + getIntValue());
                        }
                        state = State.UNKNOWN_3120__INFORMATION__FIXED3;
                    }
                }
                case UNKNOWN_3120__INFORMATION__FIXED3 -> {
                    remainingDataLength--;
                    ((Unknown_0x3120InformationMessage) evoHomeMessage).fixed3 = b;
                    if (b != (byte) 0xff) {
                        throw new IllegalStateException("Unknown_0x3120InformationMessage.fixed3 is set to " + b);
                    }
                    success();
                }
                case SYSTEM_TIMESTAMP__REQUEST__ZONE_ID -> {
                    remainingDataLength--;
                    ((SystemTimestampRequestMessage) evoHomeMessage).zoneId = b;
                    success();
                }
                case SYSTEM_TIMESTAMP__RESPONSE__ZONE_ID -> {
                    remainingDataLength--;
                    ((SystemTimestampResponseMessage) evoHomeMessage).zoneId = b;
                    state = State.SYSTEM_TIMESTAMP__RESPONSE__DIRECTION;
                }
                case SYSTEM_TIMESTAMP__RESPONSE__DIRECTION -> {
                    remainingDataLength--;
                    ((SystemTimestampResponseMessage) evoHomeMessage).direction = switch (b & 0xff) {
                        case 0x60 ->
                            SystemTimestampResponseMessage.Direction.TO_CONTROLLER;
                        case 0xfc ->
                            SystemTimestampResponseMessage.Direction.TO_DEVICE;
                        default ->
                            throw new IllegalStateException("Can't handle direction of: " + b);
                    };
                    timeStampParser.init();
                    state = State.SYSTEM_TIMESTAMP__RESPONSE__TIMESTAMP;
                }
                case SYSTEM_TIMESTAMP__RESPONSE__TIMESTAMP -> {
                    remainingDataLength--;
                    timeStampParser.parse(b);
                    if (timeStampParser.state == TimeStampParser.State.PARSE_SUCCESS) {
                        ((SystemTimestampResponseMessage) evoHomeMessage).timestamp = timeStampParser.getTimestamp();
                        success();
                    }
                }
                case ACTUATOR_SYNC__INFORMATION__DOMAIN_ID -> {
                    remainingDataLength--;
                    ((ActuatorSyncInformationMessage) evoHomeMessage).domainId = b;
                    state = State.ACTUATOR_SYNC__INFORMATION__STATE;
                }
                case ACTUATOR_SYNC__INFORMATION__STATE -> {
                    remainingDataLength--;
                    ((ActuatorSyncInformationMessage) evoHomeMessage).state = b;
                    success();
                }
                case SYSTEM_SYNCHRONIZATION__REQUEST__DOMAIN_ID -> {
                    remainingDataLength--;
                    ((SystemSynchronizationRequestMessage) evoHomeMessage).domainId = b;
                    success();
                }
                default -> // TODO
                    throw new IllegalStateException("Cant handle state: " + state);

            }
        } catch (Throwable t) {
            parserListener.fail(new RuntimeException(String.format("State: %s last byte 0x%02x", state, b), t));
            state = State.PARSE_ERROR;
        }
    }

    @Deprecated
    private void setByteArrayValue(byte b) {
        final byte[] theArray;
        theArray = switch (evoHomeCommand) {
            case RF_CHECK ->
                ((RfCheckWriteMessage) evoHomeMessage).value;
            case ZONE_MANAGEMENT ->
                ((ZoneManagementInformationMessage) evoHomeMessage).value;
            case T87RF_STARTUP_042F ->
                ((T87RF_Startup_0x042F_InformationMessage) evoHomeMessage).value;
            default ->
                throw new IllegalStateException("Unknown property: " + evoHomeMessage);
        };

        theArray[copyDataIndex++] = b;
        if (theArray.length == copyDataIndex) {
            parserListener.success(evoHomeMessage);
            state = State.PARSE_SUCCESS;
        }

    }

    private void decodeCommand() {
        evoHomeCommand = switch (getShortValue()) {
            case 0x0001 ->
                EvoHomeCommand.RF_CHECK;
            case 0x0004 ->
                EvoHomeCommand.ZONE_NAME;
            case 0x0005 ->
                EvoHomeCommand.ZONE_MANAGEMENT;
            case 0x0008 ->
                EvoHomeCommand.RELAY_HEAT_DEMAND;
            case 0x0009 ->
                EvoHomeCommand.RELAY_FAILSAVE;
            case 0x000A ->
                EvoHomeCommand.ZONE_CONFIG;
            case 0x000C ->
                EvoHomeCommand.ZONE_ACTUATORS;
            case 0x000E ->
                EvoHomeCommand.T87RF_STARTUP_000E;
            case 0x0016 ->
                EvoHomeCommand.RF_SIGNAL_TEST;
            case 0x0100 ->
                EvoHomeCommand.LOCALIZATION;
            case 0x042f ->
                EvoHomeCommand.T87RF_STARTUP_042F;
            case 0x1060 ->
                EvoHomeCommand.DEVICE_BATTERY_STATUS;
            case 0x10e0 ->
                EvoHomeCommand.DEVICE_INFORMATION;
            case 0x1100 ->
                EvoHomeCommand.BOILER_RELAY_INFORMATION;
            case 0x12b0 ->
                EvoHomeCommand.WINDOW_SENSOR;
            case 0x1f09 ->
                EvoHomeCommand.SYSTEM_SYNCHRONIZATION;
            case 0x1fc9 ->
                EvoHomeCommand.RF_BIND;
            case 0x2309 ->
                EvoHomeCommand.ZONE_SETPOINT;
            case 0x2349 ->
                EvoHomeCommand.ZONE_SETPOINT_OVERRIDE;
            case 0x2e04 ->
                EvoHomeCommand.CONTROLLER_MODE;
            case 0x30c9 ->
                EvoHomeCommand.ZONE_TEMPERATURE;
            case 0x3120 ->
                EvoHomeCommand.UNKNOWN_3120;
            case 0x313f ->
                EvoHomeCommand.SYSTEM_TIMESTAMP;
            case 0x3150 ->
                EvoHomeCommand.ZONE_HEAT_DEMAND;
            case 0x3b00 ->
                EvoHomeCommand.ACTUATOR_SYNC;
            default ->
                throw new IllegalArgumentException(String.format("Unknown command 0x%04x", getShortValue()));
        };

    }

    /**
     * Build the message. To build the message one needs the header , the
     * command and sometime the length. Here the appropriate stack size must be
     * set ant the state.
     */
    private void buildMessage() {
        switch (evoHomeCommand) {
            case RF_CHECK -> {
                switch (evoHomeMsgType) {
                    case WRITE -> {
                        checkLength(0x05, remainingDataLength, "RfCheckWriteMessage");
                        evoHomeMessage = new RfCheckWriteMessage(evoHomeMsgType, evoHomeMsgParam0);
                        state = State.COLLECT_DATA_BYTES;
                    }
                    default ->
                        throw new RuntimeException("RF_CHECK -> no case for " + evoHomeMsgType);
                }
            }
            case ZONE_NAME -> {
                switch (evoHomeMsgType) {
                    case REQUEST -> {
                        checkLength(0x02, remainingDataLength, "ZoneNameRequestMessage");
                        evoHomeMessage = new ZoneNameRequestMessage(evoHomeMsgParam0);
                        state = State.COLLECT__ZONE_NAME__REQUEST__ZONEID;
                    }
                    case INFORMATION, WRITE, RESPONSE -> {
                        checkLength(0x16, remainingDataLength, "ZoneNamePayloadMessage");
                        evoHomeMessage = new ZoneNamePayloadMessage(evoHomeMsgType, evoHomeMsgParam0);
                        zoneNameParser.init(remainingDataLength);
                        state = State.PARSE__ZONE_NAME__PAYLOAD__DATA;
                    }
                    default ->
                        throw new RuntimeException("ZONE_NAME -> no case for " + evoHomeMsgType);
                }
            }
            case ZONE_CONFIG -> {
                switch (evoHomeMsgType) {
                    case REQUEST -> {
                        checkLength(0x01, remainingDataLength, "ZoneConfigRequestMessage");
                        evoHomeMessage = new ZoneConfigRequestMessage(evoHomeMsgParam0);
                        setStackSize(1);
                        state = State.COLLECT_SINGLE_VALUE;
                    }
                    case RESPONSE, INFORMATION -> {
                        //must be a multiple of 6
                        if ((remainingDataLength % 6) != 0) {
                            checkLength(0x06 * (remainingDataLength / 6), remainingDataLength, "ZoneConfigPayLoadMessage");
                        }
                        evoHomeMessage = new ZoneConfigPayloadMessage(evoHomeMsgType, evoHomeMsgParam0);
                        zonesParamParser.init(remainingDataLength);
                        state = State.PARSE_ZONE_CONFIG_ELEMENTS;
                    }
                    default ->
                        throw new RuntimeException("ZONE_CONFIG -> no case for " + evoHomeMsgType);
                }
            }
            case ZONE_MANAGEMENT -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        evoHomeMessage = new ZoneManagementInformationMessage(evoHomeMsgParam0, remainingDataLength);
                        state = State.COLLECT_DATA_BYTES;
                    }
                    default ->
                        throw new RuntimeException("ZONE_MANAGEMENT -> no case for " + evoHomeMsgType);
                }
            }
            case RF_SIGNAL_TEST -> {
                switch (evoHomeMsgType) {
                    case REQUEST -> {
                        checkLength(0x02, remainingDataLength, "RfSignalTestRequestMessage");
                        evoHomeMessage = new RfSignalTestRequestMessage(evoHomeMsgParam0);
                        setStackSize(2);
                        state = State.COLLECT_SINGLE_VALUE;
                    }
                    case RESPONSE -> {
                        checkLength(0x02, remainingDataLength, "RfSignalTestResponseMessage");
                        evoHomeMessage = new RfSignalTestResponseMessage(evoHomeMsgParam0);
                        setStackSize(2);
                        state = State.COLLECT_SINGLE_VALUE;
                    }
                    default ->
                        throw new RuntimeException(" -> no case for " + evoHomeMsgType);
                }
            }
            case LOCALIZATION -> {
                switch (evoHomeMsgType) {
                    case REQUEST -> {
                        checkLength(0x02, 0x05, remainingDataLength, "LocalizationRequestMessage");
                        evoHomeMessage = new LocalizationRequestMessage(evoHomeMsgParam0);
                        localizationParser.init(remainingDataLength);
                        state = State.PARSE_LOCALIZATION_ELEMENTS;
                    }
                    case RESPONSE -> {
                        checkLength(0x05, remainingDataLength, "LocalizationResponseMessage");
                        evoHomeMessage = new LocalizationResponseMessage(evoHomeMsgParam0);
                        localizationParser.init(remainingDataLength);
                        state = State.PARSE_LOCALIZATION_ELEMENTS;
                    }
                    default ->
                        throw new RuntimeException("LOCALIZATION -> no case for " + evoHomeMsgType);
                }
            }
            case SYSTEM_SYNCHRONIZATION -> {
                switch (evoHomeMsgType) {
                    case REQUEST -> {
                        checkLength(0x01, remainingDataLength, "SystemSynchronizationRequestMessage");
                        evoHomeMessage = new SystemSynchronizationRequestMessage(evoHomeMsgParam0);
                        setStackSize(1);
                        state = State.SYSTEM_SYNCHRONIZATION__REQUEST__DOMAIN_ID;
                    }
                    case INFORMATION, WRITE, RESPONSE -> {
                        checkLength(0x03, remainingDataLength, "SystemSynchronizationPayloadMessage");
                        evoHomeMessage = new SystemSynchronizationPayloadMessage(evoHomeMsgType, evoHomeMsgParam0);
                        setStackSize(1);
                        state = State.PARSE__SYSTEM_SYNCHRONIZATION__PAYLOAD__DEVICE_ID;
                    }
                    default ->
                        throw new RuntimeException("SYSTEM_SYNCHRONIZATION -> no case for " + evoHomeMsgType);
                }
            }
            case RELAY_HEAT_DEMAND -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        checkLength(0x02, remainingDataLength, "RelayHeatDemandInformationMessage");
                        evoHomeMessage = new RelayHeatDemandInformationMessage(evoHomeMsgParam0);
                        state = State.PARSE__RELAY_HEAT_DEMAND__DOMAIN_ID;
                    }
                    default ->
                        throw new RuntimeException("RELAY_HEAT_DEMAND -> no case for " + evoHomeMsgType);
                }
            }
            case RELAY_FAILSAVE -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        checkLength(0x03, remainingDataLength, "RelayFailsaveInformationMessage");
                        evoHomeMessage = new RelayFailsaveInformationMessage(evoHomeMsgParam0);
                        state = State.PARSE__RELAY_FAILSAVE__DOMAIN_ID;
                    }
                    default ->
                        throw new RuntimeException("RELAY_FAILSAVE -> no case for " + evoHomeMsgType);
                }
            }
            case ZONE_ACTUATORS -> {
                switch (evoHomeMsgType) {
                    case REQUEST -> {
                        checkLength(0x02, remainingDataLength, "ZoneActuatorsRequestMessage");
                        evoHomeMessage = new ZoneActuatorsRequestMessage(evoHomeMsgParam0);
                        state = State.ZONE_ACTUATORS__REQUEST__ZONE_IDX;
                    }
                    case INFORMATION -> {
                        if (remainingDataLength % 6 != 0) {
                            throw new RuntimeException("ZoneActuatorsInformationMessage length must be a multiple of 6");
                        }
                        evoHomeMessage = new ZoneActuatorsInformationMessage(evoHomeMsgParam0);
                        state = State.ZONE_ACTUATORS__INFORMATION__ZONE_IDX;
                    }
                    default ->
                        throw new RuntimeException("RELAY_FAILSAVE -> no case for " + evoHomeMsgType);
                }
            }
            case T87RF_STARTUP_000E -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        checkLength(0x03, remainingDataLength, "T87RF_Startup_0x000E_InformationMessage");
                        evoHomeMessage = new T87RF_Startup_0x000E_InformationMessage(evoHomeMsgParam0);
                        setStackSize(3);
                        state = State.COLLECT_SINGLE_VALUE;
                    }
                    default ->
                        throw new RuntimeException("T87RF_STARTUP_000E -> no case for " + evoHomeMsgType);
                }
            }
            case T87RF_STARTUP_042F -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        checkLength(0x08, remainingDataLength, "T87RF_Startup_0x042F_InformationMessage");
                        evoHomeMessage = new T87RF_Startup_0x042F_InformationMessage(evoHomeMsgParam0, remainingDataLength);
                        state = State.COLLECT_DATA_BYTES;
                    }
                    default ->
                        throw new RuntimeException("T87RF_STARTUP_042F -> no case for " + evoHomeMsgType);
                }
            }
            case DEVICE_BATTERY_STATUS -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        checkLength(0x03, remainingDataLength, "EvoHome_0x18_0x1060_0x03_Message");
                        evoHomeMessage = new DeviceBatteryStatusInformationMessage(evoHomeMsgParam0);
                        state = State.DEVICE_BATTERY_STATUS__INFORMATION__ZONE_ID;
                    }
                    default ->
                        throw new RuntimeException("DEVICE_BATTERY_STATUS -> no case for " + evoHomeMsgType);
                }
            }
            case DEVICE_INFORMATION -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        checkLength(0x26, remainingDataLength, "DeviceInformationInformationMessage");
                        evoHomeMessage = new DeviceInformationInformationMessage(evoHomeMsgParam0);
                        byteArrayBuilder.init(10);
                        state = State.DEVICE_INFORMATION__INFORMATION__UNKNOWN0;
                    }
                    default ->
                        throw new RuntimeException("DEVICE_INFORMATION -> no case for " + evoHomeMsgType);
                }
            }
            case BOILER_RELAY_INFORMATION -> {
                checkLength(0x08, remainingDataLength, "BoilerRelayInformationMessage");
                evoHomeMessage = new BoilerRelayInformationMessage(evoHomeMsgType, evoHomeMsgParam0);
                state = State.BOILER_RELAY_INFORMATION__X__DOMAIN_ID;
            }
            case WINDOW_SENSOR -> {
                switch (evoHomeMsgType) {
                    case REQUEST -> {
                        checkLength(0x01, remainingDataLength, "WindowSensorRequestMessage");
                        evoHomeMessage = new WindowSensorRequestMessage(evoHomeMsgParam0);
                        state = State.WINDOW_SENSOR__REQUEST__ZONE_ID;
                    }
                    case INFORMATION -> {
                        checkLength(0x03, remainingDataLength, "WindowSensorInformationMessage");
                        evoHomeMessage = new WindowSensorInformationMessage(evoHomeMsgParam0);
                        state = State.WINDOW_SENSOR__PAYLOAD__ZONE_ID;
                    }
                    case RESPONSE -> {
                        checkLength(0x03, remainingDataLength, "WindowSensorResponseMessage");
                        evoHomeMessage = new WindowSensorResponseMessage(evoHomeMsgParam0);
                        state = State.WINDOW_SENSOR__PAYLOAD__ZONE_ID;
                    }
                    default ->
                        throw new RuntimeException("WINDOW_SENSOR -> no case for " + evoHomeMsgType);
                }
            }
            case RF_BIND -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        if (remainingDataLength % 6 != 0) {
                            throw new RuntimeException("RfBindInformationMessage length must be a multiple of 6");
                        }
                        evoHomeMessage = new RfBindInformationMessage(evoHomeMsgParam0);
                        state = State.RF_BIND__PAYLOAD__ELEMENTS__ZONE_ID;
                    }
                    case WRITE -> {
                        if (remainingDataLength % 6 != 0) {
                            throw new RuntimeException("RfBindWriteMessage length must be a multiple of 6");
                        }
                        evoHomeMessage = new RfBindWriteMessage(evoHomeMsgParam0);
                        state = State.RF_BIND__PAYLOAD__ELEMENTS__ZONE_ID;
                    }
                    default ->
                        throw new RuntimeException("RF_BIND -> no case for " + evoHomeMsgType);
                }
            }
            case ZONE_SETPOINT -> {
                switch (evoHomeMsgType) {
                    case REQUEST -> {
                        checkLength(0x01, remainingDataLength, "ZoneSetpointRequestMessage");
                        evoHomeMessage = new ZoneSetpointRequestMessage(evoHomeMsgParam0);
                        state = State.ZONE_SETPOINT__REQUEST__ZONE_ID;
                    }
                    case INFORMATION -> {
                        if (remainingDataLength % 3 != 0) {
                            throw new RuntimeException("ZoneSetpointInformationMessage length must be a multiple of 6");
                        }
                        evoHomeMessage = new ZoneSetpointInformationMessage(evoHomeMsgParam0);
                        state = State.ZONE_SETPOINT__PAYLOAD__ZONE_TEMPERATURES__ZONE_ID;
                    }
                    case WRITE -> {
                        checkLength(0x03, remainingDataLength, "ZoneSetpointWriteMessage");
                        evoHomeMessage = new ZoneSetpointWriteMessage(evoHomeMsgParam0);
                        state = State.ZONE_SETPOINT__PAYLOAD__ZONE_TEMPERATURES__ZONE_ID;
                    }
                    case RESPONSE -> {
                        checkLength(0x03, remainingDataLength, "ZoneSetpointResponseMessage");
                        evoHomeMessage = new ZoneSetpointResponseMessage(evoHomeMsgParam0);
                        state = State.ZONE_SETPOINT__PAYLOAD__ZONE_TEMPERATURES__ZONE_ID;
                    }

                    default ->
                        throw new RuntimeException("ZONE_SETPOINT -> no case for " + evoHomeMsgType);
                }
            }
            case ZONE_SETPOINT_OVERRIDE -> {
                switch (evoHomeMsgType) {
                    case REQUEST -> {
                        checkLength(0x07, 0x0D, remainingDataLength, "ZoneSetpointOverrideRequestMessage");
                        evoHomeMessage = new ZoneSetpointOverrideRequestMessage(evoHomeMsgParam0);
                        state = State.ZONE_SETPOINT_OVERRIDE__ALL__ZONE_ID;
                    }
                    case INFORMATION -> {
                        checkLength(0x07, 0x0D, remainingDataLength, "ZoneSetpointOverrideInformationMessage");
                        evoHomeMessage = new ZoneSetpointOverrideInformationMessage(evoHomeMsgParam0);
                        state = State.ZONE_SETPOINT_OVERRIDE__ALL__ZONE_ID;
                    }
                    case WRITE -> {
                        checkLength(0x07, 0x0D, remainingDataLength, "ZoneSetpointOverrideWriteMessage");
                        evoHomeMessage = new ZoneSetpointOverrideWriteMessage(evoHomeMsgParam0);
                        state = State.ZONE_SETPOINT_OVERRIDE__ALL__ZONE_ID;
                    }
                    case RESPONSE -> {
                        checkLength(0x07, 0x0D, remainingDataLength, "ZoneSetpointOverrideResponseMessage");
                        evoHomeMessage = new ZoneSetpointOverrideResponseMessage(evoHomeMsgParam0);
                        state = State.ZONE_SETPOINT_OVERRIDE__ALL__ZONE_ID;
                    }
                    default ->
                        throw new RuntimeException("ZONE_SETPOINT_OVERRIDE -> no case for " + evoHomeMsgType);
                }
            }
            case CONTROLLER_MODE -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        checkLength(0x08, remainingDataLength, "ControllerModeInformationMessage");
                        evoHomeMessage = new ControllerModeInformationMessage(evoHomeMsgParam0);
                        state = State.CONTROLLER_MODE__ALL__MODE;
                    }
                    default ->
                        throw new RuntimeException("CONTROLLER_MODE -> no case for " + evoHomeMsgType);
                }
            }
            case ZONE_TEMPERATURE -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        if (remainingDataLength % 3 != 0) {
                            throw new RuntimeException("ZoneTemperatureInformationMessage length must be a multiple of 6");
                        }
                        evoHomeMessage = new ZoneTemperatureInformationMessage(evoHomeMsgParam0);
                        state = State.ZONE_TEMPERATURE__PAYLOAD__ZONE_ID;
                    }
                    default ->
                        throw new RuntimeException("ZONE_TEMPERATURE -> no case for " + evoHomeMsgType);
                }
            }
            case UNKNOWN_3120 -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        checkLength(0x07, remainingDataLength, "Unknown_0x3120InformationMessage");
                        evoHomeMessage = new Unknown_0x3120InformationMessage(evoHomeMsgParam0);
                        state = State.UNKNOWN_3120__INFORMATION__UNUSED0;
                    }
                    default ->
                        throw new RuntimeException("UNKNOWN_3120 -> no case for " + evoHomeMsgType);
                }
            }
            case SYSTEM_TIMESTAMP -> {
                switch (evoHomeMsgType) {
                    case REQUEST -> {
                        checkLength(0x01, remainingDataLength, "SystemTimestampRequestMessage");
                        evoHomeMessage = new SystemTimestampRequestMessage(evoHomeMsgParam0);
                        setStackSize(1);
                        state = State.SYSTEM_TIMESTAMP__REQUEST__ZONE_ID;
                    }
                    case RESPONSE -> {
                        checkLength(0x09, remainingDataLength, "SystemTimestampResponseMessage");
                        evoHomeMessage = new SystemTimestampResponseMessage(evoHomeMsgParam0);
                        state = State.SYSTEM_TIMESTAMP__RESPONSE__ZONE_ID;
                    }
                    default ->
                        throw new RuntimeException("SYSTEM_TIMESTAMP -> no case for " + evoHomeMsgType);
                }
            }
            case ZONE_HEAT_DEMAND -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        checkLength(0x02, remainingDataLength, "ZoneHeatDemandInformationMessage");
                        evoHomeMessage = new ZoneHeatDemandInformationMessage(evoHomeMsgParam0);
                        state = State.ZONE_HEAT_DEMAND__INFORMATION__ZONE_ID;
                    }
                    default ->
                        throw new RuntimeException("ZONE_HEAT_DEMAND -> no case for " + evoHomeMsgType);
                }
            }
            case ACTUATOR_SYNC -> {
                switch (evoHomeMsgType) {
                    case INFORMATION -> {
                        checkLength(0x02, remainingDataLength, "ActuatorSyncInformationMessage");
                        evoHomeMessage = new ActuatorSyncInformationMessage(evoHomeMsgParam0);
                        setStackSize(2);
                        state = State.ACTUATOR_SYNC__INFORMATION__DOMAIN_ID;
                    }
                    default ->
                        throw new RuntimeException("ZONE_HEAT_DEMAND -> no case for " + evoHomeMsgType);
                }
            }
            default ->
                throw new IllegalArgumentException(String.format("Can't handle msgType %s with param0 %s Command %s and length %d",
                        evoHomeMsgType, evoHomeMsgParam0, evoHomeCommand, remainingDataLength));
        }

        final EvoHomeDeviceMessage msg = (EvoHomeDeviceMessage) evoHomeMessage;
        msg.deviceId1 = new DeviceId(deviceId1);
        msg.deviceId2 = new DeviceId(deviceId2);
    }

    private void checkLength(int expectedLength, int actualLength, String className) {
        if (expectedLength != actualLength) {
            throw new RuntimeException(String.format("Expected length of %s differs, expected %d, but was %d ", className, expectedLength, actualLength));
        }
    }

    private void checkLength(int expectedLength0, int expectedLength1, int actualLength, String className) {
        if (expectedLength0 != actualLength && expectedLength1 != actualLength) {
            throw new RuntimeException(String.format("Expected length of %s differs, expected %d or %d, but was %d ", className, expectedLength0, expectedLength1, actualLength));
        }
    }

    private void decodeHeader(byte b) {
        evoHomeMsgType = switch (b & 0xf0) {
            case 0x00 ->
                EvoHomeMsgType.REQUEST;
            case 0x10 ->
                EvoHomeMsgType.INFORMATION;
            case 0x20 ->
                EvoHomeMsgType.WRITE;
            case 0x30 ->
                EvoHomeMsgType.RESPONSE;
            default ->
                throw new IllegalArgumentException(String.format("Unknown EvoHome message type 0x%02x", b & 0x0f));
        };
        evoHomeMsgParam0 = switch (b & 0x0f) {
            case 0x08 ->
                EvoHomeMsgParam0._8;
            case 0x0C ->
                EvoHomeMsgParam0._C;
            default ->
                throw new IllegalArgumentException(String.format("Unknown EvoHome param0  0x%02x", b & 0x0f));
        };
    }

    private void setSingleValueAndNotify() {
        switch (evoHomeCommand) {
            case RF_SIGNAL_TEST -> {
                if (evoHomeMessage instanceof RfSignalTestRequestMessage rfSignalTestRequestMessage) {
                    rfSignalTestRequestMessage.value = getShortValue();
                } else {
                    ((RfSignalTestResponseMessage) evoHomeMessage).value = getShortValue();
                }
            }
            case ZONE_CONFIG ->
                ((ZoneConfigRequestMessage) evoHomeMessage).value = getByteValue();
            case T87RF_STARTUP_000E ->
                ((T87RF_Startup_0x000E_InformationMessage) evoHomeMessage).value = getIntValue();

            default ->
                throw new RuntimeException("Unhandled command: " + evoHomeCommand);
        }
        /*TODO
        case _0C_1F09_REQUEST_FOR_3C_1F09_:
        ((EvoHome_0x0C_0x1F09_0x01_REQUEST_0C_1F09_Message) evoHomeMessage).value = getByteValue();
        break;
        case _0C_313F_REQUEST_FOR_3C_313F:
        ((EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message) evoHomeMessage).value = getByteValue();
        break;
        case _18_1F09_BROADCAST_18_1F09_:
        ((EvoHome_0x18_0x1F09_0x03_BROADCAST_18_1F09_Message) evoHomeMessage).value = getIntValue();
        break;
        case _18_3B00:
        ((EvoHome_0x18_0x3B00_0x02_Message) evoHomeMessage).value = getIntValue();
        break;
        case _28_1F09:
        ((EvoHome_0x28_0x1F09_0x03_Message) evoHomeMessage).unknown = getIntValue();
        break;
        case _3C_1F09_RESPONSE_TO_0C_1F09:
        ((EvoHome_0x3C_0x1F09_0x03_RESPONSE_3C_1F09_Message) evoHomeMessage).value = getIntValue();
        break;
         */
        parserListener.success(evoHomeMessage);
    }
}
