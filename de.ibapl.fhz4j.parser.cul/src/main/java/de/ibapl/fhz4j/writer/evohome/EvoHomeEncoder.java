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
package de.ibapl.fhz4j.writer.evohome;

import de.ibapl.fhz4j.protocol.evohome.DeviceId;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeCommand;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgParam0;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgType;
import de.ibapl.fhz4j.protocol.evohome.ZoneTemperature;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author aploese
 */
public class EvoHomeEncoder {

    //Just cache this ...
    private final static BigDecimal ONE_HUNDRED = new BigDecimal(100.0);

    private final EvoHomeWriter writer;

    public EvoHomeEncoder(EvoHomeWriter writer) {
        this.writer = writer;
    }

    public void writeEvoHomeZoneSetpointPermanent(DeviceId deviceId, ZoneTemperature temperature) throws IOException {
        writer.startEvoHomeMessage();

        writeHeader(EvoHomeMsgType.I, EvoHomeMsgParam0._8);
        writeDeviceId(deviceId);
        writeDeviceId(deviceId);
        writeEvoCommand(EvoHomeCommand.ZONE_SETPOINT_OVERRIDE);
        writeDataLength(0x07);
        writeZoneTemperature(temperature);
        //TODO use meaningful values
        writer.putInt(0x00ffffff);

        writer.finishEvoHomeMessage();
        writer.doWrite();
    }

    private void writeHeader(EvoHomeMsgType msgType, EvoHomeMsgParam0 msgParam0) throws IOException {
        byte data;
        data = switch (msgType) {
            case REQUEST ->
                0x00;
            case INFORMATION ->
                0x10;
            case WRITE ->
                0x10;
            case RESPONSE ->
                0x30;
            default ->
                throw new IllegalArgumentException();
        };
        data |= switch (msgParam0) {
            case _8 ->
                0x08;
            case _C ->
                0x0C;
            default ->
                throw new IllegalArgumentException();
        };
        writer.putByte(data);
    }

    private void writeDeviceId(DeviceId deviceId) throws IOException {
        writer.putByte((byte) (deviceId.id >>> 16));
        writer.putByte((byte) (deviceId.id >>> 8));
        writer.putByte((byte) deviceId.id);
    }

    private void writeDataLength(int length) throws IOException {
        if (length < 0 || length > 255) {
            throw new IllegalArgumentException("length outside 0 ... 255");
        }
        writer.putByte((byte) length);
    }

    private void writeEvoCommand(EvoHomeCommand command) throws IOException {
        final short cmd = (short) switch (command) {
            case ZONE_NAME ->
                0x0004;
            case ZONE_MANAGEMENT ->
                0x0005;
            case RELAY_HEAT_DEMAND ->
                0x0008;
            case RELAY_FAILSAVE ->
                0x0009;
            case ZONE_CONFIG ->
                0x000A;
            case ZONE_ACTUATORS ->
                0x000C;
            case T87RF_STARTUP_000E ->
                0x000E;
            case RF_SIGNAL_TEST ->
                0x0016;
            case LOCALIZATION ->
                0x0100;
            case T87RF_STARTUP_042F ->
                0x042F;
            case DEVICE_BATTERY_STATUS ->
                0x1060;
            case DEVICE_INFORMATION ->
                0x10E0;
            case BOILER_RELAY_INFORMATION ->
                0x1100;
            case WINDOW_SENSOR ->
                0x12B0;
            case SYSTEM_SYNCHRONIZATION ->
                0x1F09;
            case RF_BIND ->
                0x1FC9;
            case ZONE_SETPOINT ->
                0x2309;
            case ZONE_SETPOINT_OVERRIDE ->
                0x2349;
            case CONTROLLER_MODE ->
                0x2E04;
            case ZONE_TEMPERATURE ->
                0x30C9;
            case UNKNOWN_3120 ->
                0x3120;
            case SYSTEM_TIMESTAMP ->
                0x313F;
            case ZONE_HEAT_DEMAND ->
                0x3150;
            case ACTUATOR_SYNC ->
                0x3B00;
            default ->
                throw new IllegalArgumentException("Cant send evo home command: " + command);
        };
        writer.putShort(cmd);
    }

    private void writeZoneTemperature(ZoneTemperature temperature) throws IOException {
        writer.putByte(temperature.zone);
        writer.putShort(temperature.temperature.multiply(ONE_HUNDRED).shortValue());
    }

    public void writeEvoHomeZoneSetpointUntil(DeviceId deviceId, ZoneTemperature temperature, LocalDateTime localDateTime) throws IOException {

        writer.startEvoHomeMessage();

        writeHeader(EvoHomeMsgType.I, EvoHomeMsgParam0._8);
        writeDeviceId(deviceId);
        writeDeviceId(deviceId);
        writeEvoCommand(EvoHomeCommand.ZONE_SETPOINT_OVERRIDE);
        writeDataLength(0x0D);

        writeZoneTemperature(temperature);
        writer.putInt(0x04ffffff);
        writeLocalDateTime(localDateTime);

        writer.finishEvoHomeMessage();
        writer.doWrite();
    }

    private void writeLocalDateTime(LocalDateTime localDateTime) throws IOException {
        writer.putByte((byte) localDateTime.getMinute());
        writer.putByte((byte) localDateTime.getHour());
        writer.putByte((byte) localDateTime.getDayOfMonth());
        writer.putByte((byte) localDateTime.getMonthValue());
        writer.putShort((short) localDateTime.getYear());
    }

}
