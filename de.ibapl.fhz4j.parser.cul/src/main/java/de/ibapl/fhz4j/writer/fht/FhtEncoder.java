/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2022-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.writer.fht;

import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.Fht80TfValue;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author aploese
 */
public class FhtEncoder {

    final static byte ALL_REPORTS = (byte) 0xFF;

    private final FhtWriter writer;

    public FhtEncoder(FhtWriter writer) {
        this.writer = writer;
    }

    private void writeFhtProperty(FhtProperty fhtProperty, byte value) throws IOException {
        final byte property = (byte) switch (fhtProperty) {
            case VALVE ->
                0x00;
            case OFFSET_VALVE_1 ->
                0x01;
            case OFFSET_VALVE_2 ->
                0x02;
            case OFFSET_VALVE_3 ->
                0x03;
            case OFFSET_VALVE_4 ->
                0x04;
            case OFFSET_VALVE_5 ->
                0x05;
            case OFFSET_VALVE_6 ->
                0x06;
            case OFFSET_VALVE_7 ->
                0x07;
            case OFFSET_VALVE_8 ->
                0x08;
            case MON_FROM_1 ->
                0x14;
            case MON_TO_1 ->
                0x15;
            case MON_FROM_2 ->
                0x16;
            case MON_TO_2 ->
                0x17;
            case TUE_FROM_1 ->
                0x18;
            case TUE_TO_1 ->
                0x19;
            case TUE_FROM_2 ->
                0x1a;
            case TUE_TO_2 ->
                0x1b;
            case WED_FROM_1 ->
                0x1c;
            case WED_TO_1 ->
                0x1d;
            case WED_FROM_2 ->
                0x1e;
            case WED_TO_2 ->
                0x1f;
            case THU_FROM_1 ->
                0x20;
            case THU_TO_1 ->
                0x21;
            case THU_FROM_2 ->
                0x22;
            case THU_TO_2 ->
                0x23;
            case FRI_FROM_1 ->
                0x24;
            case FRI_TO_1 ->
                0x25;
            case FRI_FROM_2 ->
                0x26;
            case FRI_TO_2 ->
                0x27;
            case SAT_FROM_1 ->
                0x28;
            case SAT_TO_1 ->
                0x29;
            case SAT_FROM_2 ->
                0x2a;
            case SAT_TO_2 ->
                0x2b;
            case SUN_FROM_1 ->
                0x2c;
            case SUN_TO_1 ->
                0x2d;
            case SUN_FROM_2 ->
                0x2e;
            case SUN_TO_2 ->
                0x2f;
            case MODE ->
                0x3e;
            case HOLIDAY_1 ->
                0x3f;
            case HOLIDAY_2 ->
                0x40;
            case DESIRED_TEMP ->
                0x41;
            case MEASURED_LOW ->
                0x42;
            case MEASURED_HIGH ->
                0x43;
            case WARNINGS ->
                0x44;
            case MANU_TEMP ->
                0x45;
            case ACK ->
                0x4b;
            case CAN_CMIT ->
                0x53;
            case CAN_RCV ->
                0x54;
            case YEAR ->
                0x60;
            case MONTH ->
                0x61;
            case DAY_OF_MONTH ->
                0x62;
            case HOUR ->
                0x63;
            case MINUTE ->
                0x64;
            case REPORT_1 ->
                0x65;
            case REPORT_2 ->
                0x66;
            case ACK_2 ->
                0x69;
            case START_XMIT ->
                0x7d;
            case END_XMIT ->
                0x7e;
            case DAY_TEMP ->
                0x82;
            case NIGHT_TEMP ->
                0x84;
            case LOW_TEMP_OFFSET ->
                0x85;
            case WINDOW_OPEN_TEMP ->
                0x8a;
            default ->
                throw new UnsupportedOperationException("Not supported yet.");
        };
        writer.putByte(property);
        writer.putByte(value);
    }

    public void initFhtReporting(short housecode) throws IOException {
        startFhtMessage(housecode);
        writeFhtProperty(FhtProperty.REPORT_1, ALL_REPORTS);
        writeFhtProperty(FhtProperty.REPORT_2, ALL_REPORTS);
        finishFhtMessage();
    }

    private void startFhtMessage(short housecode) throws IOException {
        writer.startFhtMessage();
        writer.putByte((byte) (housecode / 100));
        writer.putByte((byte) (housecode % 100));
    }

    private void finishFhtMessage() throws IOException {
        writer.finishFhtMessage();
        writer.doWrite();
    }

    public void writeFhtTimeAndDate(short housecode, LocalDateTime ts) throws IOException {
        startFhtMessage(housecode);
        writeFhtProperty(FhtProperty.YEAR, (byte) (ts.getYear() - 2000));
        writeFhtProperty(FhtProperty.MONTH, (byte) ts.getMonthValue());
        writeFhtProperty(FhtProperty.DAY_OF_MONTH, (byte) ts.getDayOfMonth());
        writeFhtProperty(FhtProperty.HOUR, (byte) ts.getHour());
        writeFhtProperty(FhtProperty.MINUTE, (byte) ts.getMinute());
        finishFhtMessage();
    }

    public void syncFhtClocks(Iterable<Short> fhtDeviceHomeCodes, LocalDateTime ts) throws IOException {

        for (short housecode : fhtDeviceHomeCodes) {
            writeFhtTimeAndDate(housecode, ts);
        }

    }

    public void initFhtReporting(Iterable<Short> housecodes) throws IOException {
        for (Short housecode : housecodes) {
            initFhtReporting(housecode);
        }
    }

    public void writeFhtModeAuto(short housecode) throws IOException {
        startFhtMessage(housecode);
        writeFhtProperty(FhtProperty.MODE, (byte) 0x00);
        finishFhtMessage();
    }

    public void writeFhtModeManu(short housecode) throws IOException {
        startFhtMessage(housecode);
        writeFhtProperty(FhtProperty.MODE, (byte) 0x01);
        finishFhtMessage();
    }

    public void writeFhtModeHoliday(short housecode, float temp, LocalDate date) throws IOException {
        startFhtMessage(housecode);
        writeFhtProperty(FhtProperty.DESIRED_TEMP, (byte) (temp * 2));
        writeFhtProperty(FhtProperty.HOLIDAY_1, (byte) date.getDayOfMonth());
        writeFhtProperty(FhtProperty.HOLIDAY_2, (byte) date.getMonthValue());
        writeFhtProperty(FhtProperty.MODE, (byte) 0x02);
        finishFhtMessage();
    }

    public void writeFhtModeParty(short housecode, float temp, LocalDateTime ts) throws IOException {
        startFhtMessage(housecode);
        writeFhtProperty(FhtProperty.DESIRED_TEMP, (byte) (temp * 2));
        writeFhtProperty(FhtProperty.HOLIDAY_1, (byte) (ts.getHour() * 6 + ts.getMinute() / 10));
        writeFhtProperty(FhtProperty.HOLIDAY_2, (byte) ts.getDayOfMonth());
        writeFhtProperty(FhtProperty.MODE, (byte) 0x03);
        finishFhtMessage();
    }

    public void writeFhtCycle(short housecode, DayOfWeek dayOfWeek, LocalTime from1, LocalTime to1, LocalTime from2,
            LocalTime to2) throws IOException {
        startFhtMessage(housecode);
        FhtProperty from1Property, to1Property, from2Property, to2Property;
        byte from1Value, to1Value, from2Value, to2Value;

        if (from1 == null) {
            if (to1 != null) {
                throw new IllegalArgumentException("Both from1 and to1 must be null or set!");
            } else {
                from1Value = (byte) 0x90; // this is 24:00 or 24 * 6 = 144 the meaning is not set ....
                to1Value = (byte) 0x90;
            }
        } else {
            from1Value = (byte) (from1.getHour() * 6 + from1.getMinute() / 10);
            to1Value = (byte) (to1.getHour() * 6 + to1.getMinute() / 10);
        }

        if (from2 == null) {
            if (to2 != null) {
                throw new IllegalArgumentException("Both from2 and to2 must be null or set!");
            } else {
                from2Value = (byte) 0x90; // this is 24:00 or 24 * 6 = 144 the meaning is not set ....
                to2Value = (byte) 0x90;
            }
        } else {
            from2Value = (byte) (from2.getHour() * 6 + from2.getMinute() / 10);
            to2Value = (byte) (to2.getHour() * 6 + to2.getMinute() / 10);
        }

        switch (dayOfWeek) {
            case MONDAY -> {
                from1Property = FhtProperty.MON_FROM_1;
                to1Property = FhtProperty.MON_TO_1;
                from2Property = FhtProperty.MON_FROM_2;
                to2Property = FhtProperty.MON_TO_2;
            }
            case TUESDAY -> {
                from1Property = FhtProperty.TUE_FROM_1;
                to1Property = FhtProperty.TUE_TO_1;
                from2Property = FhtProperty.TUE_FROM_2;
                to2Property = FhtProperty.TUE_TO_2;
            }
            case WEDNESDAY -> {
                from1Property = FhtProperty.WED_FROM_1;
                to1Property = FhtProperty.WED_TO_1;
                from2Property = FhtProperty.WED_FROM_2;
                to2Property = FhtProperty.WED_TO_2;
            }
            case THURSDAY -> {
                from1Property = FhtProperty.THU_FROM_1;
                to1Property = FhtProperty.THU_TO_1;
                from2Property = FhtProperty.THU_FROM_2;
                to2Property = FhtProperty.THU_TO_2;
            }
            case FRIDAY -> {
                from1Property = FhtProperty.FRI_FROM_1;
                to1Property = FhtProperty.FRI_TO_1;
                from2Property = FhtProperty.FRI_FROM_2;
                to2Property = FhtProperty.FRI_TO_2;
            }
            case SATURDAY -> {
                from1Property = FhtProperty.SAT_FROM_1;
                to1Property = FhtProperty.SAT_TO_1;
                from2Property = FhtProperty.SAT_FROM_2;
                to2Property = FhtProperty.SAT_TO_2;
            }
            case SUNDAY -> {
                from1Property = FhtProperty.SUN_FROM_1;
                to1Property = FhtProperty.SUN_TO_1;
                from2Property = FhtProperty.SUN_FROM_2;
                to2Property = FhtProperty.SUN_TO_2;
            }
            default ->
                throw new IllegalArgumentException("Unknown dayOfweek: " + dayOfWeek);

        }
        writeFhtProperty(from1Property, from1Value);
        writeFhtProperty(to1Property, to1Value);
        writeFhtProperty(from2Property, from2Value);
        writeFhtProperty(to2Property, to2Value);
        finishFhtMessage();
    }

    public void writeFht(short housecode, FhtProperty fhtProperty, float value) throws IOException {
        startFhtMessage(housecode);
        switch (fhtProperty) {
            case DAY_TEMP, NIGHT_TEMP, DESIRED_TEMP, LOW_TEMP_OFFSET, WINDOW_OPEN_TEMP, MANU_TEMP ->
                writeFhtProperty(fhtProperty, (byte) (value * 2));
            default ->
                throw new IllegalArgumentException("Wrong fht property for temp: " + fhtProperty);
        }
        finishFhtMessage();
    }

    public void writeFht80Tf(int address, Fht80TfValue fht80TfValue, boolean lowBattery) throws IOException {
        writer.startFhtMessage();
        writer.putByte((byte) (address >> 16));
        writer.putByte((byte) (address >> 8));
        writer.putByte((byte) address);
        final byte flag = switch (fht80TfValue) {
            case WINDOW_INTERNAL_OPEN ->
                0x01;
            case WINDOW_INTERNAL_CLOSED ->
                0x02;
            case WINDOW_EXTERNAL_OPEN ->
                (byte) 0x81;
            case WINDOW_EXTERNAL_CLOSED ->
                (byte) 0x82;
            case SYNC ->
                0x0C;
            case FINISH ->
                0x0F;
            default ->
                throw new IllegalArgumentException("Wrong fht tf value for temp: " + fht80TfValue);
        };
        final byte lowBattFlag = lowBattery ? (byte) 0x10 : 0x00;
        writer.putByte((byte) (lowBattFlag | flag));
        finishFhtMessage();
    }

    public void writeFht(short housecode, FhtProperty fhtProperty, LocalTime value) throws IOException {
        startFhtMessage(housecode);
        switch (fhtProperty) {
            case MON_FROM_1, MON_TO_1, MON_FROM_2, MON_TO_2, TUE_FROM_1, TUE_TO_1, TUE_FROM_2, TUE_TO_2, WED_FROM_1, WED_TO_1, WED_FROM_2, WED_TO_2, THU_FROM_1, THU_TO_1, THU_FROM_2, THU_TO_2, FRI_FROM_1, FRI_TO_1, FRI_FROM_2, FRI_TO_2, SAT_FROM_1, SAT_TO_1, SAT_FROM_2, SAT_TO_2, SUN_FROM_1, SUN_TO_1, SUN_FROM_2, SUN_TO_2 -> {
                if (value == null) {
                    writeFhtProperty(fhtProperty, (byte) 0x90); // this is 24:00 or 24 * 6 = 144 the meaning is not set ....
                } else {
                    writeFhtProperty(fhtProperty, (byte) (value.getHour() * 6 + value.getMinute() / 10));
                }
            }
            default ->
                throw new IllegalArgumentException("Wrong fht property for temp: " + fhtProperty);
        }
        finishFhtMessage();
    }

}
