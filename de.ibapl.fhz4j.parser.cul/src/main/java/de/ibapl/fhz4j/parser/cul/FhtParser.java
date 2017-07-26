package de.ibapl.fhz4j.parser.cul;

/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2017 Arne Plöse
 * %%
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009, 2017, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
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
 * #L%
 */
import de.ibapl.fhz4j.protocol.fht.FhtValveSyncMessage;
import java.util.logging.Logger;
import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.Fht80bMode;
import de.ibapl.fhz4j.protocol.fht.Fht80bRawMessage;
import de.ibapl.fhz4j.protocol.fht.Fht80bWarning;
import de.ibapl.fhz4j.protocol.fht.FhtDateMessage;
import de.ibapl.fhz4j.protocol.fht.FhtDateTimeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtModeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtTempMessage;
import de.ibapl.fhz4j.protocol.fht.FhtTimeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtValveMode;
import de.ibapl.fhz4j.protocol.fht.FhtValvePosMessage;
import de.ibapl.fhz4j.protocol.fht.FhtWarningMessage;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author aploese
 */
public class FhtParser extends Parser {

    @Override
    public void init() {
        setStackSize(2);
        state = State.COLLECT_HOUSECODE_HIGH;
        housecode = 0;
        command = null;
    }

    private Fht80bRawMessage removePartialMsg(short housecode, FhtProperty fhtProperty) {
        Iterator<Fht80bRawMessage> iter = partialMessages.iterator();
        while (iter.hasNext()) {
            final Fht80bRawMessage msg = iter.next();
            if (msg.housecode == housecode && msg.command == fhtProperty) {
                iter.remove();
                return msg;
            }
        }
        return null;
    }

    private FhtProperty getCommand(int command) {
        switch (command) {
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
                throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private boolean hasPartialMsgs (short housecode, FhtProperty ... fhtProperty) {
        EnumSet props = EnumSet.copyOf(Arrays.asList(fhtProperty)); 
        for (Fht80bRawMessage msg : partialMessages) {
            if (msg.housecode == housecode) {
                props.remove(msg.command);
                if (props.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void addOrReplacePartial(Fht80bRawMessage fht80bRawMessage) {
        Iterator<Fht80bRawMessage> iter = partialMessages.iterator();
        while (iter.hasNext()) {
            final FhtMessage msg = iter.next();
            if (msg.housecode == fht80bRawMessage.housecode && msg.command == fht80bRawMessage.command) {
                iter.remove();
            }
        }
        partialMessages.add(fht80bRawMessage);
    }

    private void buildRawTimeAndDateMessage() {
        final Fht80bRawMessage msg = new Fht80bRawMessage(housecode, command, isFromFht_8B(), isDataRegister(), getIntValue());
        addOrReplacePartial(msg);
        parserListener.successPartial(msg);
        if (hasPartialMsgs(housecode, FhtProperty.YEAR, FhtProperty.MONTH, FhtProperty.DAY_OF_MONTH, FhtProperty.HOUR, FhtProperty.MINUTE)) {
            final Fht80bRawMessage year = removePartialMsg(housecode, FhtProperty.YEAR);
            final Fht80bRawMessage month = removePartialMsg(housecode, FhtProperty.MONTH);
            final Fht80bRawMessage dayOfMonth = removePartialMsg(housecode, FhtProperty.DAY_OF_MONTH);
            final Fht80bRawMessage hour = removePartialMsg(housecode, FhtProperty.HOUR);
            final Fht80bRawMessage minute = removePartialMsg(housecode, FhtProperty.MINUTE);
            
            parserListener.successPartialAssembled(new FhtDateTimeMessage(housecode, FhtProperty.CURRENT_DATE_AND_TIME, isFromFht_8B(), isDataRegister(), LocalDateTime.of(year.data + 2000, Month.of(month.data), dayOfMonth.data, hour.data, minute.data)));
        }
    }

    private void buildRawReportMessage() {
        final Fht80bRawMessage msg = new Fht80bRawMessage(housecode, command, isFromFht_8B(), isDataRegister(), getIntValue());
        parserListener.successPartial(msg);
        if (command == FhtProperty.REPORT_1) {
            Fht80bRawMessage msg_2 = removePartialMsg(housecode, FhtProperty.REPORT_2);
            if (msg_2 != null) {
                return;
            }
        } else {
            Fht80bRawMessage msg_1 = removePartialMsg(housecode, FhtProperty.REPORT_2);
            if (msg_1 != null) {
                return;
            }
        }
        addOrReplacePartial(msg);
    }

    private enum State {

        COLLECT_HOUSECODE_HIGH,
        COLLECT_HOUSECODE_LOW,
        COLLECT_COMMAND,
        COLLECT_ORIGIN,
        COLLECT_VALUE,
        PARSE_SUCCESS,
        PARSE_ERROR;

    }

    public FhtParser(ParserListener parserListener) {
        this.parserListener = parserListener;
    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private final ParserListener parserListener;
    private State state;
    private short housecode;
    private FhtProperty command;
    private byte description;
    private LinkedList<Fht80bRawMessage> partialMessages = new LinkedList<>();

    @Override
    public void parse(char c) {
        try {
            switch (state) {
                //FHT
                case COLLECT_HOUSECODE_HIGH:
                    push(digit2Int(c));
                    if (getStackpos() == 0) {
                        housecode = (short) (getShortValue() * 100);
                        setStackSize(2);
                        state = State.COLLECT_HOUSECODE_LOW;
                    }
                    break;
                case COLLECT_HOUSECODE_LOW:
                    push(digit2Int(c));
                    if (getStackpos() == 0) {
                        housecode += getShortValue();
                        setStackSize(2);
                        state = State.COLLECT_COMMAND;
                    }
                    break;
                case COLLECT_COMMAND:
                    push(digit2Int(c));
                    if (getStackpos() == 0) {
                        command = getCommand(getIntValue());
                        setStackSize(2);
                        state = State.COLLECT_ORIGIN;
                    }
                    break;
                case COLLECT_ORIGIN:
                    push(digit2Int(c));
                    if (getStackpos() == 0) {
                        description = getByteValue();
                        setStackSize(2);
                        state = State.COLLECT_VALUE;
                    }
                    break;
                case COLLECT_VALUE:
                    push(digit2Int(c));
                    if (getStackpos() == 0) {
                        buildAndNotify();
                        state = State.PARSE_SUCCESS;
                    }
                    break;

            }
        } catch (Throwable t) {
            parserListener.fail(new RuntimeException(String.format("State: %s last char %s", state, c), t));
            state = State.PARSE_ERROR;
        }
    }

    private void buildAndNotify() {
        switch (command) {
            case VALVE:
                buildValveMessage();
                break;
            case OFFSET_VALVE_1:
            case OFFSET_VALVE_2:
            case OFFSET_VALVE_3:
            case OFFSET_VALVE_4:
            case OFFSET_VALVE_5:
            case OFFSET_VALVE_6:
            case OFFSET_VALVE_7:
            case OFFSET_VALVE_8:
                buildValveOffsetMessage();
                break;
            case NIGHT_TEMP:
            case DAY_TEMP:
            case DESIRED_TEMP:
            case WINDOW_OPEN_TEMP:
            case LOW_TEMP_OFFSET:
                parserListener.success(new FhtTempMessage(housecode, command, isFromFht_8B(), isDataRegister(), 0.5f * getIntValue()));
                break;
            //TODO 
            case MEASURED_LOW:
                buildRawTempLowMessage();
                break;
            case MEASURED_HIGH:
                buildRawTempHighMessage();
                break;
            case WARNINGS:
                buildWarningsMessage();
                break;
            case MON_FROM_1:
            case MON_TO_1:
            case MON_FROM_2:
            case MON_TO_2:
            case TUE_FROM_1:
            case TUE_TO_1:
            case TUE_FROM_2:
            case TUE_TO_2:
            case WED_FROM_1:
            case WED_TO_1:
            case WED_FROM_2:
            case WED_TO_2:
            case THU_FROM_1:
            case THU_TO_1:
            case THU_FROM_2:
            case THU_TO_2:
            case FRI_FROM_1:
            case FRI_TO_1:
            case FRI_FROM_2:
            case FRI_TO_2:
            case SAT_FROM_1:
            case SAT_TO_1:
            case SAT_FROM_2:
            case SAT_TO_2:
            case SUN_FROM_1:
            case SUN_TO_1:
            case SUN_FROM_2:
            case SUN_TO_2:
                parserListener.success(new FhtTimeMessage(housecode, command, isFromFht_8B(), isDataRegister(), getIntValue() / 6, (getIntValue() % 6) * 10));
                break;
            case MINUTE:
            case HOUR:
            case MONTH:
            case DAY_OF_MONTH:
            case YEAR:
                buildRawTimeAndDateMessage();
                break;
// collect FhtDateTime                
            case MODE:
                buildModeMessage();
                break;
//Sniffer Mode
            case REPORT_1:
            case REPORT_2:
                buildRawReportMessage();
                break;
//collect Report
            case HOLIDAY_1:
            case HOLIDAY_2:
                buildRawHollidayMessage();
                break;
            default:
                throw new RuntimeException("Cant handle unknown command: " + command);
        }

    }

    private void buildRawHollidayMessage() {
        final Fht80bRawMessage fht80bRawMessage = new Fht80bRawMessage(housecode, command, isFromFht_8B(), isDataRegister(), getIntValue());
        parserListener.successPartial(fht80bRawMessage);
        addOrReplacePartial(fht80bRawMessage);
    }

    private void buildModeMessage() {
        Fht80bMode fhtMode;
        switch (getIntValue()) {
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
        FhtModeMessage fhtModeMessage = new FhtModeMessage(housecode, isFromFht_8B(), isDataRegister(), fhtMode);
        parserListener.success(fhtModeMessage);
        switch (fhtModeMessage.mode) {
            case HOLIDAY: {
                Fht80bRawMessage day = removePartialMsg(housecode, FhtProperty.HOLIDAY_1);
                Fht80bRawMessage month = removePartialMsg(housecode, FhtProperty.HOLIDAY_2);
                parserListener.successPartialAssembled(new FhtDateMessage(housecode, FhtProperty.HOLIDAY_END_DATE, isFromFht_8B(), isDataRegister(), month.data, day.data));
            }
            case PARTY: {
                Fht80bRawMessage time = removePartialMsg(housecode, FhtProperty.HOLIDAY_1);
                Fht80bRawMessage day = removePartialMsg(housecode, FhtProperty.HOLIDAY_2);
                //TODO Fht PartyMessage? ?? day is missing ...
                parserListener.successPartialAssembled(new FhtTimeMessage(housecode, FhtProperty.PARTY_END_TIME, isFromFht_8B(), isDataRegister(), time.data / 6, (time.data % 6) * 10));
            }
        }
    }

    private void buildWarningsMessage() {
        EnumSet warnings = EnumSet.noneOf(Fht80bWarning.class);
        if ((getIntValue() & 0x01) == 0x01) {
            warnings.add(Fht80bWarning.BATT_LOW);
        }

        parserListener.success(new FhtWarningMessage(housecode, isFromFht_8B(), isDataRegister(), warnings));
    }

    private void buildRawTempHighMessage() {
        final Fht80bRawMessage measuredHigh = new Fht80bRawMessage(housecode, command, isFromFht_8B(), isDataRegister(), getIntValue());
        parserListener.successPartial(measuredHigh);
        final Fht80bRawMessage measuredLow = removePartialMsg(housecode, FhtProperty.MEASURED_LOW);
        if (measuredLow != null) {
            parserListener.successPartialAssembled(new FhtTempMessage(housecode, FhtProperty.MEASURED_TEMP, isFromFht_8B(), isDataRegister(), 0.1f * (measuredLow.data | measuredHigh.data << 8)));
        } else {
            partialMessages.addLast(measuredHigh);
        }
    }

    private void buildRawTempLowMessage() {
        final Fht80bRawMessage measuredLow = new Fht80bRawMessage(housecode, command, isFromFht_8B(), isDataRegister(), getIntValue());
        parserListener.successPartial(measuredLow);
        final Fht80bRawMessage measuredHigh = removePartialMsg(housecode, FhtProperty.MEASURED_HIGH);
        if (measuredHigh != null) {
            parserListener.successPartialAssembled(new FhtTempMessage(housecode, FhtProperty.MEASURED_TEMP, isFromFht_8B(), isDataRegister(), 0.1f * (measuredLow.data | measuredHigh.data << 8)));
        } else {
            partialMessages.addLast(measuredLow);
        }
    }

    private void buildValveOffsetMessage() throws RuntimeException {
        final boolean allowLowBatteryBeep = (description & 0x10) == 0x10;
        int offset;
        if ((getIntValue() & 0x80) == 0x80) {
            offset = -(getIntValue() & 0x7F);
        } else {
            offset = getIntValue() & 0x7F; 
        }
        switch (description & 0x6F) {
            case 0x28:
                parserListener.success(new FhtValvePosMessage(housecode, command, false, FhtValveMode.OFFSET_ADJUST, offset, allowLowBatteryBeep));
                return;
            case 0x2F:
                parserListener.success(new FhtValvePosMessage(housecode, command, false, FhtValveMode.PAIRING, offset, allowLowBatteryBeep));
                return;
            default:
                throw new RuntimeException("Unknown Description");
        }
    }

    private void buildValveMessage() throws RuntimeException {
        final boolean repeated = (description & 0x80) == 0x80;
        final boolean allowLowBatteryBeep = (description & 0x10) == 0x10;
        switch (description & 0x0F) {
            case 0x00:
                parserListener.success(new FhtValvePosMessage(housecode, command, repeated, FhtValveMode.END_OF_SYNC_SEQUENCE, 0.5f * getIntValue(), allowLowBatteryBeep));
                return;
            case 0x01:
                parserListener.success(new FhtValvePosMessage(housecode, command, repeated, FhtValveMode.ON, 100.0f, allowLowBatteryBeep));
                return;
            case 0x02:
                parserListener.success(new FhtValvePosMessage(housecode, command, repeated, FhtValveMode.OFF, 0.0f, allowLowBatteryBeep));
                return;
            case 0x06:
                //100.0 / 255.0 = 0,392156863;
                parserListener.success(new FhtValvePosMessage(housecode, command, repeated, FhtValveMode.POSITION, 0.392156863f * getIntValue(), allowLowBatteryBeep));
                return;
            case 0x08:
                parserListener.success(new FhtValvePosMessage(housecode, command, repeated, FhtValveMode.OFFSET_ADJUST, 0.0f, allowLowBatteryBeep));
                return;
            case 0x0a:
                parserListener.success(new FhtValvePosMessage(housecode, command, repeated, FhtValveMode.LIME_CYCLE, 0.5f * getIntValue(), allowLowBatteryBeep));
                return;
            case 0x0c:
                parserListener.success(new FhtValveSyncMessage(housecode, command, 0.5f * getIntValue(), allowLowBatteryBeep));
                return;
            case 0x0e:
                parserListener.success(new FhtValvePosMessage(housecode, command, repeated, FhtValveMode.BEEP, 0.5f * getIntValue(), allowLowBatteryBeep));
                return;
            default:
                throw new RuntimeException("Unknown Description");
        }
    }

    private boolean isFromFht_8B() {
        if ((description & 0x60) == 0x60) {
            return true;
        } else if ((description & 0x70) == 0x70) {
            return false;
        } else {
            throw new RuntimeException();
        }
    }

    private boolean isDataRegister() {
        if ((description & 0x07) == 0x07) {
            //protocoll
            return false;
        } else if ((description & 0x09) == 0x09) {
            return true;
        } else {
            throw new RuntimeException();
        }
    }

}
