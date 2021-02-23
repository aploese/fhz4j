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
package de.ibapl.fhz4j.writer.cul;

import de.ibapl.fhz4j.protocol.evohome.DeviceId;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeProperty;
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

        writer.putByte((byte) 0x18);
        writeDeviceId(deviceId);
        writeDeviceId(deviceId);
        writeEvoPropertyAndLength(EvoHomeProperty._18_2349_ZONE_SETPOINT_PERMANENT);
        writeZoneTemperature(temperature);
        writer.putInt(0x00ffffff);

        writer.finishEvoHomeMessage();
        writer.doWrite();
    }

    private void writeDeviceId(DeviceId deviceId) throws IOException {
        writer.putByte((byte) (deviceId.id >>> 16));
        writer.putByte((byte) (deviceId.id >>> 8));
        writer.putByte((byte) deviceId.id);
    }

    private void writeEvoPropertyAndLength(EvoHomeProperty evoHomeProperty) throws IOException {
        switch (evoHomeProperty) {
            case _18_2349_ZONE_SETPOINT_PERMANENT:
                writer.putByte((byte) 0x23);
                writer.putByte((byte) 0x49);
                writer.putByte((byte) 0x07);
                break;
            case _18_2349_ZONE_SETPOINT_UNTIL:
                writer.putByte((byte) 0x23);
                writer.putByte((byte) 0x49);
                writer.putByte((byte) 0x0D);
                break;
            default:
                throw new IllegalArgumentException("Cant send evo home property: " + evoHomeProperty);
        }
    }

    private void writeZoneTemperature(ZoneTemperature temperature) throws IOException {
        writer.putByte(temperature.zone);
        writer.putShort(temperature.temperature.multiply(ONE_HUNDRED).shortValue());
    }

    public void writeEvoHomeZoneSetpointUntil(DeviceId deviceId, ZoneTemperature temperature, LocalDateTime localDateTime) throws IOException {

        writer.startEvoHomeMessage();

        writer.putByte((byte) 0x18);
        writeDeviceId(deviceId);
        writeDeviceId(deviceId);
        writeEvoPropertyAndLength(EvoHomeProperty._18_2349_ZONE_SETPOINT_UNTIL);
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
