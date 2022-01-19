/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.parser.fht;

import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.AbstractFhtMessage;
import de.ibapl.fhz4j.protocol.fht.Fht80bMode;
import de.ibapl.fhz4j.protocol.fht.Fht80bRawMessage;
import de.ibapl.fhz4j.protocol.fht.Fht80bWarning;
import de.ibapl.fhz4j.protocol.fht.FhtDateMessage;
import de.ibapl.fhz4j.protocol.fht.FhtDateTimeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtModeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtProtocolMessage;
import de.ibapl.fhz4j.protocol.fht.FhtTempMessage;
import de.ibapl.fhz4j.protocol.fht.FhtTfMessage;
import de.ibapl.fhz4j.protocol.fht.FhtTfValue;
import de.ibapl.fhz4j.protocol.fht.FhtTimeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtTimesMessage;
import de.ibapl.fhz4j.protocol.fht.FhtValveMode;
import de.ibapl.fhz4j.protocol.fht.FhtValvePosMessage;
import de.ibapl.fhz4j.protocol.fht.FhtValveSyncMessage;
import de.ibapl.fhz4j.protocol.fht.FhtWarningMessage;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Arne Plöse
 */
public class FhtParser implements Parser {

    @Override
    public void init() {
        state = State.COLLECT_HOUSECODE_HIGH;
        tfAddress = 0;
        housecode = 0;
        description = 0;
        command = null;
    }

    public void initWithHousecode(short housecode) {
        state = State.COLLECT_COMMAND;
        this.housecode = housecode;
        description = 0;
        command = null;
    }

    private void removePartialMsg(short housecode, FhtProperty[] fhtProperties) {
        Iterator<Fht80bRawMessage> iter = partialMessages.iterator();
        while (iter.hasNext()) {
            final Fht80bRawMessage msg = iter.next();
            if (msg.housecode == housecode) {
                for (FhtProperty p : fhtProperties) {
                    if (p == msg.command) {
                        iter.remove();
                    }
                }
            }
        }
    }

    private FhtProperty getCommand(byte command) {
        switch (command & 0xFF) {
            case 0x00:
                return FhtProperty.VALVE;
            case 0x01:
                return FhtProperty.OFFSET_VALVE_1;
            case 0x02:
                return FhtProperty.OFFSET_VALVE_2;
            case 0x03:
                return FhtProperty.OFFSET_VALVE_3;
            case 0x04:
                return FhtProperty.OFFSET_VALVE_4;
            case 0x05:
                return FhtProperty.OFFSET_VALVE_5;
            case 0x06:
                return FhtProperty.OFFSET_VALVE_6;
            case 0x07:
                return FhtProperty.OFFSET_VALVE_7;
            case 0x08:
                return FhtProperty.OFFSET_VALVE_8;
            case 0x14:
                return FhtProperty.MON_FROM_1;
            case 0x15:
                return FhtProperty.MON_TO_1;
            case 0x16:
                return FhtProperty.MON_FROM_2;
            case 0x17:
                return FhtProperty.MON_TO_2;
            case 0x18:
                return FhtProperty.TUE_FROM_1;
            case 0x19:
                return FhtProperty.TUE_TO_1;
            case 0x1a:
                return FhtProperty.TUE_FROM_2;
            case 0x1b:
                return FhtProperty.TUE_TO_2;
            case 0x1c:
                return FhtProperty.WED_FROM_1;
            case 0x1d:
                return FhtProperty.WED_TO_1;
            case 0x1e:
                return FhtProperty.WED_FROM_2;
            case 0x1f:
                return FhtProperty.WED_TO_2;
            case 0x20:
                return FhtProperty.THU_FROM_1;
            case 0x21:
                return FhtProperty.THU_TO_1;
            case 0x22:
                return FhtProperty.THU_FROM_2;
            case 0x23:
                return FhtProperty.THU_TO_2;
            case 0x24:
                return FhtProperty.FRI_FROM_1;
            case 0x25:
                return FhtProperty.FRI_TO_1;
            case 0x26:
                return FhtProperty.FRI_FROM_2;
            case 0x27:
                return FhtProperty.FRI_TO_2;
            case 0x28:
                return FhtProperty.SAT_FROM_1;
            case 0x29:
                return FhtProperty.SAT_TO_1;
            case 0x2a:
                return FhtProperty.SAT_FROM_2;
            case 0x2b:
                return FhtProperty.SAT_TO_2;
            case 0x2c:
                return FhtProperty.SUN_FROM_1;
            case 0x2d:
                return FhtProperty.SUN_TO_1;
            case 0x2e:
                return FhtProperty.SUN_FROM_2;
            case 0x2f:
                return FhtProperty.SUN_TO_2;
            case 0x3e:
                return FhtProperty.MODE;
            case 0x3f:
                return FhtProperty.HOLIDAY_1;
            case 0x40:
                return FhtProperty.HOLIDAY_2;
            case 0x41:
                return FhtProperty.DESIRED_TEMP;
            case 0x42:
                return FhtProperty.MEASURED_LOW;
            case 0x43:
                return FhtProperty.MEASURED_HIGH;
            case 0x44:
                return FhtProperty.WARNINGS;
            case 0x45:
                return FhtProperty.MANU_TEMP;
            case 0x4b:
                return FhtProperty.ACK;
            case 0x53:
                return FhtProperty.CAN_CMIT;
            case 0x54:
                return FhtProperty.CAN_RCV;
            case 0x60:
                return FhtProperty.YEAR;
            case 0x61:
                return FhtProperty.MONTH;
            case 0x62:
                return FhtProperty.DAY_OF_MONTH;
            case 0x63:
                return FhtProperty.HOUR;
            case 0x64:
                return FhtProperty.MINUTE;
            case 0x65:
                return FhtProperty.REPORT_1;
            case 0x66:
                return FhtProperty.REPORT_2;
            case 0x69:
                return FhtProperty.ACK_2;
            case 0x7d:
                return FhtProperty.START_XMIT;
            case 0x7e:
                return FhtProperty.END_XMIT;
            case 0x82:
                return FhtProperty.DAY_TEMP;
            case 0x84:
                return FhtProperty.NIGHT_TEMP;
            case 0x85:
                return FhtProperty.LOW_TEMP_OFFSET;
            case 0x8a:
                return FhtProperty.WINDOW_OPEN_TEMP;
            default:
                throw new UnsupportedOperationException(String.format("Unknown command: 0x%02x", command & 0xFF));
        }
    }

    private Map<FhtProperty, Fht80bRawMessage> getPartialMsgs(short housecode, FhtProperty[] fhtProperties) {
        final EnumMap<FhtProperty, Fht80bRawMessage> result = new EnumMap<>(FhtProperty.class);
        int pos = 0;
        Iterator<Fht80bRawMessage> iter = partialMessages.iterator();
        while (iter.hasNext()) {
            final Fht80bRawMessage msg = iter.next();
            if (msg.housecode == housecode) {
                if (msg.command == fhtProperties[pos]) {
                    result.put(msg.command, msg);
                    pos++;
                    if (pos == fhtProperties.length) {
                        return result;
                    }
                }
            }
        }
        // Only partial found so clear it out....
        result.clear();
        return result;
    }

    private void buildTimeAndDateMessage() {
        Map<FhtProperty, Fht80bRawMessage> messages = getPartialMsgs(housecode,
                FhtProperty.CURRENT_DATE_AND_TIME.parts);
        if (messages.isEmpty()) {
            return;
        }
        parserListener.successPartialAssembled(new FhtDateTimeMessage(housecode, FhtProperty.CURRENT_DATE_AND_TIME,
                description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01,
                LocalDateTime.of((messages.get(FhtProperty.YEAR).getUnsignedValue()) + 2000,
                        Month.of(messages.get(FhtProperty.MONTH).getUnsignedValue()), messages.get(FhtProperty.DAY_OF_MONTH).getUnsignedValue(),
                        messages.get(FhtProperty.HOUR).getUnsignedValue(), messages.get(FhtProperty.MINUTE).getUnsignedValue())));
    }

    private void buildAndNotifyPartial(byte b) {
        final Fht80bRawMessage msg = new Fht80bRawMessage(housecode, command, description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01, b);
        partialMessages.add(msg);
        parserListener.successPartial(msg);
    }

    private void buildMessage(FhtProperty fhtProperty) {
        Map<FhtProperty, Fht80bRawMessage> messages = getPartialMsgs(housecode, fhtProperty.parts);
        if (messages.isEmpty()) {
            return;
        }
        parserListener.successPartialAssembled(new FhtTimesMessage(housecode, fhtProperty, description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01, toLocalTime(messages.get(fhtProperty.parts[0]).getUnsignedValue()),
                toLocalTime(messages.get(fhtProperty.parts[1]).getUnsignedValue()),
                toLocalTime(messages.get(fhtProperty.parts[2]).getUnsignedValue()),
                toLocalTime(messages.get(fhtProperty.parts[3]).getUnsignedValue())));
    }

    private LocalTime toLocalTime(short value) {
        final int hour = value / 6;
        final int minute = (value % 6) * 10;

        if (hour == 24 && minute == 0) {
            return null;
        } else {
            return LocalTime.of(hour, minute);
        }

    }

    private enum State {

        COLLECT_HOUSECODE_HIGH,
        COLLECT_HOUSECODE_LOW,
        COLLECT_COMMAND,
        COLLECT_DESCRIPTION,
        COLLECT_VALUE,
        PARSE_SUCCESS,
        PARSE_ERROR,
        COLLECT_TF_ADDRESS_2ND,
        COLLECT_TF_ADDRESS_3RD,
        COLLECT_TF_VALUE;

    }

    public FhtParser(ParserListener<AbstractFhtMessage> parserListener) {
        this.parserListener = parserListener;
        this.parseCULOutBuffer = false;
    }

    /**
     *
     * @param parseCULOutBuffer Are we parsint the CUL out buffer contents? If
     * so the description (origin) is not present.
     * @param parserListener
     */
    public FhtParser(boolean parseCULOutBuffer, ParserListener<AbstractFhtMessage> parserListener) {
        this.parserListener = parserListener;
        this.parseCULOutBuffer = parseCULOutBuffer;
    }

    private final ParserListener<AbstractFhtMessage> parserListener;
    private State state;
    private short housecode;
    private int tfAddress;
    private FhtProperty command;
    private byte description;
    /**
     * If the CUL out buffer is parsed the description (origin) is not present
     * and will be skipped.
     */
    private final boolean parseCULOutBuffer;

    private final LinkedList<Fht80bRawMessage> partialMessages = new LinkedList<>();

    /**
     * parse FHT8b FHT8v and FHT Tf data.
     *
     *
     * Detection of FHT TF: if either of the two housecode bytes id above 99 or
     * 0x63 it must be an FHT TF otherwiose its a FHT8b or FHT8v.
     *
     * @param b
     */
    @Override
    public void parse(byte b) {
        try {
            switch (state) {
                // FHT
                case COLLECT_HOUSECODE_HIGH:
                    if ((b & 0xFF) > 99) { // 0x63 in hex
                        tfAddress = (b & 0xFF) << 16;
                        state = State.COLLECT_TF_ADDRESS_2ND;
                    } else {
                        // 99 is ever smaller than 127 so the sign is always positive
                        housecode = (short) (b * 100);
                        state = State.COLLECT_HOUSECODE_LOW;
                    }
                    break;
                case COLLECT_HOUSECODE_LOW:
                    if ((b & 0xFF) > 99) {// 0x63 in hex
                        tfAddress = ((housecode / 100) << 16) + ((b & 0xFF) << 8);
                        state = State.COLLECT_TF_ADDRESS_3RD;
                    } else {
                        // 99 is ever smaller than 127 so the sign is always positive
                        housecode += (short) b;
                        state = State.COLLECT_COMMAND;
                    }
                    break;
                case COLLECT_COMMAND:
                    command = getCommand(b);
                    if (parseCULOutBuffer) {
                        state = State.COLLECT_VALUE;
                    } else {
                        state = State.COLLECT_DESCRIPTION;
                    }
                    break;
                case COLLECT_DESCRIPTION:
                    description = b;
                    state = State.COLLECT_VALUE;
                    break;
                case COLLECT_VALUE:
                    buildAndNotify(b);
                    state = State.PARSE_SUCCESS;
                    break;
                case COLLECT_TF_ADDRESS_2ND:
                    tfAddress += (b & 0xFF) << 8;
                    state = State.COLLECT_TF_ADDRESS_3RD;
                    break;
                case COLLECT_TF_ADDRESS_3RD:
                    tfAddress += (b & 0xFF);
                    state = State.COLLECT_TF_VALUE;
                    break;
                case COLLECT_TF_VALUE:
                    buildAndNotifyTf(b);
                    state = State.PARSE_SUCCESS;
                    break;

            }
        } catch (Throwable t) {
            parserListener.fail(new RuntimeException(String.format("State: %s command: %s last byte 0x%02x", state, command, b), t));
            state = State.PARSE_ERROR;
        }
    }

    private void buildAndNotify(byte b) {
        switch (command) {
            case VALVE:
                buildValveMessage(b);
                break;
            case OFFSET_VALVE_1:
            case OFFSET_VALVE_2:
            case OFFSET_VALVE_3:
            case OFFSET_VALVE_4:
            case OFFSET_VALVE_5:
            case OFFSET_VALVE_6:
            case OFFSET_VALVE_7:
            case OFFSET_VALVE_8:
                buildValveOffsetMessage(b);
                break;
            case NIGHT_TEMP:
            case DAY_TEMP:
            case DESIRED_TEMP:
            case WINDOW_OPEN_TEMP:
            case LOW_TEMP_OFFSET:
                parserListener.success(
                        new FhtTempMessage(housecode, command, description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01, 0.5f * b));
                break;
            case MEASURED_LOW:
                removePartialMsg(housecode, FhtProperty.MEASURED_TEMP.parts);
                buildAndNotifyPartial(b);
                break;
            case MEASURED_HIGH:
                buildAndNotifyPartial(b);
                buildAndNotifyMeasuredTempMessage();
                break;
            case WARNINGS:
                buildWarningsMessage(b);
                break;
            case MON_FROM_1:
                removePartialMsg(housecode, FhtProperty.MONDAY_TIMES.parts);
                buildAndNotifyPartial(b);
                break;
            case MON_TO_1:
                buildAndNotifyPartial(b);
                break;
            case MON_FROM_2:
                buildAndNotifyPartial(b);
                break;
            case MON_TO_2:
                buildAndNotifyPartial(b);
                buildMessage(FhtProperty.MONDAY_TIMES);
                break;
            case TUE_FROM_1:
                removePartialMsg(housecode, FhtProperty.TUESDAY_TIMES.parts);
                buildAndNotifyPartial(b);
                break;
            case TUE_TO_1:
                buildAndNotifyPartial(b);
                break;
            case TUE_FROM_2:
                buildAndNotifyPartial(b);
                break;
            case TUE_TO_2:
                buildAndNotifyPartial(b);
                buildMessage(FhtProperty.TUESDAY_TIMES);
                break;
            case WED_FROM_1:
                removePartialMsg(housecode, FhtProperty.WEDNESDAY_TIMES.parts);
                buildAndNotifyPartial(b);
                break;
            case WED_TO_1:
                buildAndNotifyPartial(b);
                break;
            case WED_FROM_2:
                buildAndNotifyPartial(b);
                break;
            case WED_TO_2:
                buildAndNotifyPartial(b);
                buildMessage(FhtProperty.WEDNESDAY_TIMES);
                break;
            case THU_FROM_1:
                removePartialMsg(housecode, FhtProperty.THURSDAY_TIMES.parts);
                buildAndNotifyPartial(b);
                break;
            case THU_TO_1:
                buildAndNotifyPartial(b);
                break;
            case THU_FROM_2:
                buildAndNotifyPartial(b);
                break;
            case THU_TO_2:
                buildAndNotifyPartial(b);
                buildMessage(FhtProperty.THURSDAY_TIMES);
                break;
            case FRI_FROM_1:
                removePartialMsg(housecode, FhtProperty.FRIDAY_TIMES.parts);
                buildAndNotifyPartial(b);
                break;
            case FRI_TO_1:
                buildAndNotifyPartial(b);
                break;
            case FRI_FROM_2:
                buildAndNotifyPartial(b);
                break;
            case FRI_TO_2:
                buildAndNotifyPartial(b);
                buildMessage(FhtProperty.FRIDAY_TIMES);
                break;
            case SAT_FROM_1:
                removePartialMsg(housecode, FhtProperty.SATURDAYDAY_TIMES.parts);
                buildAndNotifyPartial(b);
                break;
            case SAT_TO_1:
                buildAndNotifyPartial(b);
                break;
            case SAT_FROM_2:
                buildAndNotifyPartial(b);
                break;
            case SAT_TO_2:
                buildAndNotifyPartial(b);
                buildMessage(FhtProperty.SATURDAYDAY_TIMES);
                break;
            case SUN_FROM_1:
                removePartialMsg(housecode, FhtProperty.SUNDAYDAY_TIMES.parts);
                buildAndNotifyPartial(b);
                break;
            case SUN_TO_1:
                buildAndNotifyPartial(b);
                break;
            case SUN_FROM_2:
                buildAndNotifyPartial(b);
                break;
            case SUN_TO_2:
                buildAndNotifyPartial(b);
                buildMessage(FhtProperty.SUNDAYDAY_TIMES);
                break;
            case MINUTE:
                buildAndNotifyPartial(b);
                buildTimeAndDateMessage();
                break;
            case HOUR:
                buildAndNotifyPartial(b);
                break;
            case MONTH:
                buildAndNotifyPartial(b);
                break;
            case DAY_OF_MONTH:
                buildAndNotifyPartial(b);
                break;
            case YEAR:
                removePartialMsg(housecode, FhtProperty.CURRENT_DATE_AND_TIME.parts);
                buildAndNotifyPartial(b);
                break;
            // collect FhtDateTime
            case MODE:
                buildAndNotifyModeMessage(b);
                break;
            case REPORT_1:
            case REPORT_2:
                final Fht80bRawMessage msg = new Fht80bRawMessage(housecode, command, description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01,
                        b);
                parserListener.success(msg);
                break;
            case HOLIDAY_1:
                removePartialMsg(housecode, FhtProperty.HOLIDAY_END_DATE.parts);
                buildAndNotifyPartial(b);
                break;
            case HOLIDAY_2:
                buildAndNotifyPartial(b);
                // message will be build when mode message arrives.
                break;
            case ACK:
            case ACK_2:
            case CAN_RCV:
            case CAN_CMIT:
            case END_XMIT:
            case START_XMIT:
                buildProtocolMessage(b);
                break;
            default:
                throw new RuntimeException("Cant handle unknown command: " + command);
        }

    }

    private void buildAndNotifyTf(byte b) {
        final boolean lowBattery = (b & 0x10) == 0x10;
        switch (b & 0xEF) {
            case 0x01:
                parserListener.success(new FhtTfMessage(tfAddress, FhtTfValue.WINDOW_INTERNAL_OPEN, lowBattery));
                break;
            case 0x02:
                parserListener.success(new FhtTfMessage(tfAddress, FhtTfValue.WINDOW_INTERNAL_CLOSED, lowBattery));
                break;
            case 0x81:
                parserListener.success(new FhtTfMessage(tfAddress, FhtTfValue.WINDOW_EXTERNAL_OPEN, lowBattery));
                break;
            case 0x82:
                parserListener.success(new FhtTfMessage(tfAddress, FhtTfValue.WINDOW_EXTERNAL_CLOSED, lowBattery));
                break;
            case 0x0C:
                parserListener.success(new FhtTfMessage(tfAddress, FhtTfValue.SYNC, lowBattery));
                break;
            case 0x0F:
                parserListener.success(new FhtTfMessage(tfAddress, FhtTfValue.FINISH, lowBattery));
                break;
            default:
                throw new RuntimeException(String.format("Unknown TF value: %02x ", b));
        }
    }

    private void buildAndNotifyModeMessage(byte b) {
        Fht80bMode fhtMode;
        switch (b) {
            case 0x00:
                fhtMode = Fht80bMode.AUTO;
                break;
            case 0x01:
                fhtMode = Fht80bMode.MANUAL;
                break;
            case 0x02:
                fhtMode = Fht80bMode.HOLIDAY;
                break;
            case 0x03:
                fhtMode = Fht80bMode.PARTY;
                break;
            default:
                throw new RuntimeException("Can't figure out fhtMode");
        }
        FhtModeMessage fhtModeMessage = new FhtModeMessage(housecode, description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01, fhtMode);

        switch (fhtModeMessage.mode) {
            case AUTO:
                parserListener.success(fhtModeMessage);
                break;
            case MANUAL:
                parserListener.success(fhtModeMessage);
                break;
            case HOLIDAY: {
                Map<FhtProperty, Fht80bRawMessage> messages = getPartialMsgs(housecode, FhtProperty.HOLIDAY_END_DATE.parts);
                if (messages.isEmpty()) {
                    return;
                }
                parserListener.successPartialAssembled(
                        new FhtDateMessage(housecode, FhtProperty.HOLIDAY_END_DATE, description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01,
                                messages.get(FhtProperty.HOLIDAY_2).getSignedValue(), messages.get(FhtProperty.HOLIDAY_1).getSignedValue()));
            }
            break;
            case PARTY: {
                Map<FhtProperty, Fht80bRawMessage> messages = getPartialMsgs(housecode, FhtProperty.PARTY_END_TIME.parts);
                if (messages.isEmpty()) {
                    return;
                }
                final short rawTime = messages.get(FhtProperty.HOLIDAY_1).getUnsignedValue();
                parserListener.successPartialAssembled(new FhtTimeMessage(housecode, FhtProperty.PARTY_END_TIME,
                        description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01, (byte) (rawTime / 6), (byte) ((rawTime % 6) * 10)));
            }
            break;
            default:
                throw new RuntimeException();
        }
    }

    private void buildWarningsMessage(byte b) {
        EnumSet<Fht80bWarning> warnings = EnumSet.noneOf(Fht80bWarning.class);
        if ((b & 0x01) == 0x01) {
            warnings.add(Fht80bWarning.BATT_LOW);
        }
        parserListener.success(new FhtWarningMessage(housecode, description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01, warnings));
    }

    private void buildAndNotifyMeasuredTempMessage() {
        Map<FhtProperty, Fht80bRawMessage> messages = getPartialMsgs(housecode, FhtProperty.MEASURED_TEMP.parts);
        if (messages.isEmpty()) {
            return;
        }
        parserListener.successPartialAssembled(new FhtTempMessage(housecode, FhtProperty.MEASURED_TEMP, description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01, 0.1f * (messages.get(FhtProperty.MEASURED_LOW).getUnsignedValue()
                | (messages.get(FhtProperty.MEASURED_HIGH).getUnsignedValue()) << 8)));
    }

    private void buildProtocolMessage(byte b) throws RuntimeException {
        //TODO description ???
        parserListener.success(new FhtProtocolMessage(housecode, command, description, (description & 0xf0) == 0x60, (description & 0x01) == 0x01, b));
    }

    private void buildValveOffsetMessage(byte b) throws RuntimeException {
        final boolean allowLowBatteryBeep = (description & 0x10) == 0x10;
        int offset;
        if ((b & 0x80) == 0x80) {
            offset = -(b & 0x7F);
        } else {
            offset = b & 0x7F;
        }
        switch (description & 0x6F) {
            case 0x28:
                parserListener.success(new FhtValvePosMessage(housecode, command, description, false, FhtValveMode.OFFSET_ADJUST, offset,
                        allowLowBatteryBeep));
                return;
            case 0x2F:
                parserListener.success(new FhtValvePosMessage(housecode, command, description, false, FhtValveMode.PAIRING, offset,
                        allowLowBatteryBeep));
                return;
            default:
                throw new RuntimeException("Unknown Description");
        }
    }

    private void buildValveMessage(byte b) throws RuntimeException {
        final boolean repeated = (description & 0x80) == 0x80;
        final boolean allowLowBatteryBeep = (description & 0x10) == 0x10;
        switch (description & 0x0F) {
            case 0x00:
                parserListener.success(new FhtValvePosMessage(housecode, command, description, repeated,
                        FhtValveMode.END_OF_SYNC_SEQUENCE, 0.5f * b, allowLowBatteryBeep));
                return;
            case 0x01:
                parserListener.success(
                        new FhtValvePosMessage(housecode, command, description, repeated, FhtValveMode.ON, 100.0f, allowLowBatteryBeep));
                return;
            case 0x02:
                parserListener.success(
                        new FhtValvePosMessage(housecode, command, description, repeated, FhtValveMode.OFF, 0.0f, allowLowBatteryBeep));
                return;
            case 0x06:
                // 100.0 / 255.0 = 0,392156863;
                parserListener.success(new FhtValvePosMessage(housecode, command, description, repeated, FhtValveMode.POSITION,
                        0.392156863f * (b & 0xFF), allowLowBatteryBeep));
                return;
            case 0x08:
                parserListener.success(new FhtValvePosMessage(housecode, command, description, repeated, FhtValveMode.OFFSET_ADJUST,
                        0.0f, allowLowBatteryBeep));
                return;
            case 0x0a:
                parserListener.success(new FhtValvePosMessage(housecode, command, description, repeated, FhtValveMode.LIME_CYCLE,
                        0.5f * (b & 0xFF), allowLowBatteryBeep));
                return;
            case 0x0c:
                parserListener.success(new FhtValveSyncMessage(housecode, command, description, 0.5f * (b & 0xff), allowLowBatteryBeep));
                return;
            case 0x0e:
                parserListener.success(new FhtValvePosMessage(housecode, command, description, repeated, FhtValveMode.BEEP,
                        0.5f * (b & 0xFF), allowLowBatteryBeep));
                return;
            default:
                throw new RuntimeException(
                        String.format("Unknown Description %d valve cmd: %02x ", housecode, description));
        }
    }

}
