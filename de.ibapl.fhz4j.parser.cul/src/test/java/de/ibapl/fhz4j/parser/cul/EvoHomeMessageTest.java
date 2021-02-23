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
package de.ibapl.fhz4j.parser.cul;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.evohome.EvoHomeParser;
import de.ibapl.fhz4j.protocol.evohome.DeviceId;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeDeviceMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessage;
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
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x30C9_0xXX_ROOM_MEASURED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x3120_0x07_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x3B00_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x1C_0x10E0_0x26_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x1C_0x1FC9_0xXX_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x1C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x28_0x0001_0x05__RADIO_TEST_REQUEST_FROM_MASTER_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x28_0x1F09_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x2C_0x1FC9_0xXX_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x2C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x0004_0x16_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x000A_ZONE_PARAMS_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x0016_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x0100_0x05_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x1F09_0x03_RESPONSE_3C_1F09_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x313F_0x09_RESPONSE_3C_313F_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0xXX_0x0004_0x16_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0xXX_0x000A_0xXX_ZONES_PARAMS_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0xXX_0x000A_0xXX_ZONES_PARAMS_Message.ZoneParams;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0xXX_0x10E0_0x26_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0xXX_0x1FC9_0xXX_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0xXX_0x1FC9_0xXX_Message.Data;
import de.ibapl.fhz4j.protocol.evohome.ZoneTemperature;
import de.ibapl.fhz4j.writer.cul.EvoHomeEncoder;
import de.ibapl.fhz4j.writer.cul.EvoHomeWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class EvoHomeMessageTest implements ParserListener<EvoHomeMessage> {

    class EvoHomeTestWriter implements EvoHomeWriter {

        String written = "";

        StringBuilder sb = new StringBuilder();

        @Override
        public void startEvoHomeMessage() {
            //no-op
        }

        @Override
        public void finishEvoHomeMessage() {
            //no-op
        }

        @Override
        public void doWrite() throws IOException {
            written += sb.toString();
        }

        @Override
        public void putByte(byte value) throws IOException {
            sb.append(String.format("%02X", value));
        }

        @Override
        public void putShort(short value) throws IOException {
            sb.append(String.format("%04X", value));
        }

        @Override
        public void putInt(int value) throws IOException {
            sb.append(String.format("%08X", value));
        }

        @Override
        public void close() throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    public static void assertEvoHome_0x0001_Message(EvoHome_0x28_0x0001_0x05__RADIO_TEST_REQUEST_FROM_MASTER_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x0004_Message(EvoHome_0x0C_0x0004_0x02_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte zoneId, byte unknown) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zoneId, evoHomeMessage.zoneId, "zoneId");
        assertEquals(unknown, evoHomeMessage.unknown, "unknown");
    }

    public static void assertEvoHome_0x0004_Message(EvoHome_0xXX_0x0004_0x16_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte zoneId, byte unknown, String zoneName) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zoneId, evoHomeMessage.zoneId, "zoneId");
        assertEquals(unknown, evoHomeMessage.unknown, "unknown");
        assertEquals(zoneName, evoHomeMessage.zoneName, "zoneName");
    }

    public static void assertEvoHome_0x0005_Message(EvoHome_0x18_0x0005_0xXX_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x0008_Message(EvoHome_0x18_0x0008_0x02_Message evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x0009_Message(EvoHome_0x18_0x0009_0x03_Message evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x000A_Message(EvoHome_0x0C_0x000A_0x01_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x000A_Message(EvoHome_0xXX_0x000A_0xXX_ZONES_PARAMS_Message evoHomeMessage,
            int deviceId1, int deviceId2, List<ZoneParams> zones) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        Iterator<ZoneParams> expectedZoneParams = zones.iterator();
        Iterator<ZoneParams> actualZoneParams = evoHomeMessage.zones.iterator();
        while (expectedZoneParams.hasNext()) {
            ZoneParams expected = expectedZoneParams.next();
            assertTrue(actualZoneParams.hasNext());
            ZoneParams actual = actualZoneParams.next();
            assertEquals(expected.zoneId, actual.zoneId, "zoneId");
            assertEquals(expected.operationLock, actual.operationLock, "operationLock");
            assertEquals(expected.windowFunction, actual.windowFunction, "windowFunction");
            assertEquals(expected.minTemperature, actual.minTemperature, "minTemperature");
            assertEquals(expected.maxTemperature, actual.maxTemperature, "maxTemperature");
        }
        assertFalse(actualZoneParams.hasNext());
    }

    public static void assertEvoHome_0x000C_Message(EvoHome_0x18_0x000C_0x12_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x000E_Message(EvoHome_0x18_0x000E_0x03_Message evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x0016_Message(EvoHome_0x0C_0x0016_0x02_Message evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x0016_Message(EvoHome_0x3C_0x0016_0x02_Message evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x0100_Message(EvoHome_0x0C_0x0100_0x05_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x0100_Message(EvoHome_0x3C_0x0100_0x05_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x042F_Message(EvoHome_0x18_0x042F_0x08_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x1060_Message(EvoHome_0x18_0x1060_0x03_Message evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x10E0_Message(EvoHome_0xXX_0x10E0_0x26_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x1100_Message(EvoHome_0x18_0x1100_0x08_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x12B0_Message(EvoHome_0x18_0x12B0_0x03_Message evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x1F09_Message(EvoHome_0x0C_0x1F09_0x01_REQUEST_0C_1F09_Message evoHomeMessage,
            int deviceId1, int deviceId2, byte value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x1F09_Message(EvoHome_0x18_0x1F09_0x03_BROADCAST_18_1F09_Message evoHomeMessage,
            int deviceId1, int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x1F09_Message(EvoHome_0x28_0x1F09_0x03_Message evoHomeMessage, int deviceId1,
            int deviceId2, int unknown) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(unknown, evoHomeMessage.unknown, "unknown");
    }

    public static void assertEvoHome_0x1F09_Message(EvoHome_0x3C_0x1F09_0x03_RESPONSE_3C_1F09_Message evoHomeMessage,
            int deviceId1, int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x1FC9_Message(EvoHome_0xXX_0x1FC9_0xXX_Message evoHomeMessage, int deviceId1,
            int deviceId2, Data... elements) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(elements, evoHomeMessage.elements.toArray(new Data[0]), "elements");
    }

    public static void assertEvoHome_0x2309_Message(EvoHome_0x0C_0x2309_0x01_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x2309_Message(EvoHome_0x18_0x2309_0xXX_ROOM_DESIRED_TEMP_Message evoHomeMessage,
            int deviceId1, int deviceId2, ZoneTemperature... zoneTemperatures) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(zoneTemperatures, evoHomeMessage.zoneTemperatures.toArray(new ZoneTemperature[0]),
                "zoneTemperatures");
    }

    public static void assertEvoHome_0x2309_Message(EvoHome_0x1C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message evoHomeMessage,
            int deviceId1, int deviceId2, ZoneTemperature... zoneTemperatures) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(zoneTemperatures, evoHomeMessage.zoneTemperatures.toArray(new ZoneTemperature[0]),
                "zoneTemperatures");
    }

    public static void assertEvoHome_0x2309_Message(EvoHome_0x2C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message evoHomeMessage,
            int deviceId1, int deviceId2, ZoneTemperature... zoneTemperatures) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(zoneTemperatures, evoHomeMessage.zoneTemperatures.toArray(new ZoneTemperature[0]),
                "zoneTemperatures");
    }

    public static void assertEvoHome_0x2309_Message(EvoHome_0x3C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message evoHomeMessage,
            int deviceId1, int deviceId2, ZoneTemperature... zoneTemperatures) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(zoneTemperatures, evoHomeMessage.zoneTemperatures.toArray(new ZoneTemperature[0]),
                "zoneTemperatures");
    }

    public static void assertEvoHome_0x2349_Message(
            EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message expected, EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message actual) {
        assertEquals(expected.deviceId1, actual.deviceId1, "deviceId1");
        assertEquals(expected.deviceId2, actual.deviceId2, "deviceId2");
        assertEquals(expected.temperature, actual.temperature, "temperature");
        assertEquals(expected.unknown, actual.unknown, "unknown");
    }

    public static void assertEvoHome_0x2349_Message(EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message evoHomeMessage,
            int deviceId1, int deviceId2, ZoneTemperature temperature, int unknown, LocalDateTime until) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(temperature, evoHomeMessage.temperature, "temperature");
        assertEquals(unknown, evoHomeMessage.unknown, "unknown");
        assertEquals(until, evoHomeMessage.until, "until");
    }

    public static void assertEvoHome_0x2E04_Message(EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message evoHomeMessage,
            int deviceId1, int deviceId2, EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message.Mode mode, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(mode, evoHomeMessage.mode, "mode");
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x30C9_Message(EvoHome_0x18_0x30C9_0xXX_ROOM_MEASURED_TEMP_Message evoHomeMessage,
            int deviceId1, int deviceId2, ZoneTemperature... zoneTemperatures) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(zoneTemperatures, evoHomeMessage.zoneTemperatures.toArray(new ZoneTemperature[0]),
                "zoneTemperatures");
    }

    public static void assertEvoHome_0x3120_Message(EvoHome_0x18_0x3120_0x07_Message evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x313F_Message(EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message evoHomeMessage,
            int deviceId1, int deviceId2, byte value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x313F_Message(EvoHome_0x3C_0x313F_0x09_RESPONSE_3C_313F_Message evoHomeMessage,
            int deviceId1, int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x3150_Message(EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message evoHomeMessage,
            int deviceId1, int deviceId2, byte zone, short heatDemand) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zone, evoHomeMessage.zone, "zone");
        assertEquals(heatDemand, evoHomeMessage.heatDemand, "heatDemand");
    }

    public static void assertEvoHome_0x3B00_Message(EvoHome_0x18_0x3B00_0x02_Message evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHomeDeviceMessage(EvoHomeDeviceMessage evoHomeMessage, int deviceId1, int deviceId2) {
        assertNotNull(evoHomeMessage);
        assertEquals(deviceId1, evoHomeMessage.deviceId1.id, "deviceId1");
        assertEquals(deviceId2, evoHomeMessage.deviceId2.id, "deviceId2");
    }

    private EvoHomeParser parser = new EvoHomeParser(this);
    private EvoHomeTestWriter evoHomeWriter = new EvoHomeTestWriter();
    private EvoHomeEncoder encoder = new EvoHomeEncoder(evoHomeWriter);

    private EvoHomeMessage evoHomeMessage;

    /**
     * Decode but skip whitspaces
     *
     * @param s
     */
    private void decode(String s) {
        evoHomeMessage = null;
        parser.init();
        new DataSource(s).iterate(parser);
    }

    @Test
    public void decode_EvoHome_0x0C_0x0004() {
        decode("0C 114977 067AEC 0004 02 0000");
        assertEvoHome_0x0004_Message((EvoHome_0x0C_0x0004_0x02_Message) evoHomeMessage, 0x114977, 0x067AEC, (byte) 0x00,
                (byte) 0x00);
        decode("0C 131589 067AEC 0004 02 01 00");
        assertEvoHome_0x0004_Message((EvoHome_0x0C_0x0004_0x02_Message) evoHomeMessage, 0x131589, 0x067AEC, (byte) 0x01,
                (byte) 0x00);
    }

    @Test
    public void decode_EvoHome_0x0C_0x0005_0x05() {
        decode("18 067AEC 067AEC 0005 04 00000300");
        assertEvoHome_0x0005_Message((EvoHome_0x18_0x0005_0xXX_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
                new byte[]{0x00, 0x00, 0x03, 0x00});

        decode("18 895E5D 895E5D 0005 0C 000A0000 000F0000 00100000");
        assertEvoHome_0x0005_Message((EvoHome_0x18_0x0005_0xXX_Message) evoHomeMessage, 0x895E5D, 0x895E5D,
                new byte[]{0x00, 0x0A, 0x00, 0x00, 0x00, 0x0F, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00});
    }

    @Test
    public void decode_EvoHome_0x0C_0x000A_0x01() {
        decode("0C 895E5D 067AEC 000A 01 01");
        assertEvoHome_0x000A_Message((EvoHome_0x0C_0x000A_0x01_Message) evoHomeMessage, 0x895E5D, 0x067AEC,
                (byte) 0x01);
    }

    @Test
    public void decode_EvoHome_0x0C_0x0016() {
        decode("0C 067AEC 114977 0016 02 00FF");
        assertEvoHome_0x0016_Message((EvoHome_0x0C_0x0016_0x02_Message) evoHomeMessage, 0x067AEC, 0x114977, 0x00FF);
    }

    @Test
    public void decode_EvoHome_0x3C_0x0016() {
        decode("3C 114977 067AEC 0016 02 004D");
        assertEvoHome_0x0016_Message((EvoHome_0x3C_0x0016_0x02_Message) evoHomeMessage, 0x114977, 0x067AEC, 0x004D);
    }

    @Test
    public void decode_EvoHome_0x0C_0x0100() {
        decode("0C 114977 067AEC 0100 05 006465FFFF");
        assertEvoHome_0x0100_Message((EvoHome_0x0C_0x0100_0x05_Message) evoHomeMessage, 0x114977, 0x067AEC,
                new byte[]{0x00, 0x64, 0x65, (byte) 0xFF, (byte) 0xFF});

        decode("0C 131589 067AEC 0100 05 006465FFFF");
        assertEvoHome_0x0100_Message((EvoHome_0x0C_0x0100_0x05_Message) evoHomeMessage, 0x131589, 0x067AEC,
                new byte[]{0x00, 0x64, 0x65, (byte) 0xFF, (byte) 0xFF});
    }

    @Test
    public void decode_EvoHome_0x0C_0x1F09() {
        decode("0C 114977 067AEC 1F09 01 00");
        assertEvoHome_0x1F09_Message((EvoHome_0x0C_0x1F09_0x01_REQUEST_0C_1F09_Message) evoHomeMessage, 0x114977,
                0x067AEC, (byte) 0x00);
    }

    @Test
    public void decode_EvoHome_0x0C_0x2309_0x01() {
        decode("0C 895E5D 067AEC 2309 01 01");
        assertEvoHome_0x2309_Message((EvoHome_0x0C_0x2309_0x01_Message) evoHomeMessage, 0x895E5D, 0x067AEC,
                (byte) 0x01);
    }

    @Test
    public void decode_EvoHome_0x0C_0x313F() {
        decode("0C 114977 067AEC 313F 01 00");
        assertEvoHome_0x313F_Message((EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message) evoHomeMessage, 0x114977,
                0x067AEC, (byte) 0x00);
        decode("0C 1203C3 05B933 313F 01 00");
        assertEvoHome_0x313F_Message((EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message) evoHomeMessage, 0x1203C3,
                0x05B933, (byte) 0x00);
    }

    @Test
    public void decode_EvoHome_0x18_0x0004() {
        decode("18 067AEC 067AEC 0004 16 00 00 576F686E7A696D6D657231000000000000000000");
        assertEvoHome_0x0004_Message((EvoHome_0x18_0x0004_0x16_Message) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0x00,
                (byte) 0x00, "Wohnzimmer1");
        decode("18 067AEC 067AEC 0004 16 01 00 5858587878780000000000000000000000000000");
        assertEvoHome_0x0004_Message((EvoHome_0x18_0x0004_0x16_Message) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0x01,
                (byte) 0x00, "XXXxxx");
        decode("18 067AEC 067AEC 0004 16 0B 007F7F7F7F7F7F7F7F7F7F7F7F7F7F7F7F7F7F7F7F");
        assertEvoHome_0x0004_Message((EvoHome_0x18_0x0004_0x16_Message) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0x0B,
                (byte) 0x00, "");

    }

    @Test
    public void decode_EvoHome_0x18_0x0008() {
        decode("18 067AEC 067AEC 0008 02 FC00");
        assertEvoHome_0x0008_Message((EvoHome_0x18_0x0008_0x02_Message) evoHomeMessage, 0x067AEC, 0x067AEC, 0xFC00);
    }

    @Test
    public void decode_EvoHome_0x18_0x0009() {
        decode("18 067AEC 067AEC 0009 03 FC00FF");
        assertEvoHome_0x0009_Message((EvoHome_0x18_0x0009_0x03_Message) evoHomeMessage, 0x067AEC, 0x067AEC, 0xFC00FF);
    }

    @Test
    public void decode_EvoHome_0x18_0x000A() {
        decode("18 067AEC 067AEC 000A 06 00 10 01F4 0DAC");
        ZoneParams zoneParam = new ZoneParams();
        zoneParam.zoneId = 0;
        zoneParam.windowFunction = true;
        zoneParam.operationLock = false;
        zoneParam.minTemperature = new BigDecimal("5");
        zoneParam.maxTemperature = new BigDecimal("35");
        List<ZoneParams> zoneParams = new LinkedList<>();
        zoneParams.add(zoneParam);
        assertEvoHome_0x000A_Message((EvoHome_0x18_0x000A_0xXX_ZONES_PARAMS_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
                zoneParams);
    }

    @Test
    public void decode_EvoHome_0x18_0x000C_0x12() {
        decode("18 895E5D 895E5D 000C 12 00 0A7FFFFFFF 00 0F7FFFFFFF 00 107FFFFFFF");
        assertEvoHome_0x000C_Message((EvoHome_0x18_0x000C_0x12_Message) evoHomeMessage, 0x895E5D, 0x895E5D,
                new byte[]{0x00, 0x0A, 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x7F, (byte) 0xFF,
                    (byte) 0xFF, (byte) 0xFF, 0x00, 0x10, 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
    }

    @Test
    public void decode_EvoHome_0x18_0x000E_0x03() {
        decode("18 895E5D 895E5D 000E 03 000014");
        assertEvoHome_0x000E_Message((EvoHome_0x18_0x000E_0x03_Message) evoHomeMessage, 0x895E5D, 0x895E5D, 0x000014);
    }

    @Test
    public void decode_EvoHome_0x18_0x042F_0x08() {
        decode("18 895E5D 895E5D 042F 08 000000000B000B1E");
        assertEvoHome_0x042F_Message((EvoHome_0x18_0x042F_0x08_Message) evoHomeMessage, 0x895E5D, 0x895E5D,
                new byte[]{0x00, 0x00, 0x00, 0x00, 0x0B, 0x00, 0x0B, 0x1E});

        decode("18 895E5D 895E5D 042F 08 000000000C000C1C");
        assertEvoHome_0x042F_Message((EvoHome_0x18_0x042F_0x08_Message) evoHomeMessage, 0x895E5D, 0x895E5D,
                new byte[]{0x00, 0x00, 0x00, 0x00, 0x0C, 0x00, 0x0C, 0x1C});
    }

    @Test
    public void decode_EvoHome_0x18_0x1060() {
        decode("18 114977 067AEC 1060 03 006401");
        assertEvoHome_0x1060_Message((EvoHome_0x18_0x1060_0x03_Message) evoHomeMessage, 0x114977, 0x067AEC, 0x006401);

        decode("18 895E5D 895E5D 1060 03 00FF01");
        assertEvoHome_0x1060_Message((EvoHome_0x18_0x1060_0x03_Message) evoHomeMessage, 0x895E5D, 0x895E5D, 0x00FF01);
    }

    @Test
    public void decode_EvoHome_0x18_0x10E0_0x26() {
        decode("18 114977 114977 10E0 26 000002FF0412FFFFFFFF0D0207E20D0307DE48523932205261646961746F72204374726C2E00");
        assertEvoHome_0x10E0_Message((EvoHome_0x18_0x10E0_0x26_Message) evoHomeMessage, 0x114977, 0x114977,
                new byte[]{0x00, 0x00, 0x02, (byte) 0xFF, 0x04, 0x12, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0xFF, 0x0D, 0x02, 0x07, (byte) 0xE2, 0x0D, 0x03, 0x07, (byte) 0xDE, 0x48, 0x52, 0x39,
                    0x32, 0x20, 0x52, 0x61, 0x64, 0x69, 0x61, 0x74, 0x6F, 0x72, 0x20, 0x43, 0x74, 0x72, 0x6C, 0x2E,
                    0x00});
    }

    @Test
    public void decode_EvoHome_0x18_0x1100() {
        decode("18 067AEC 067AEC 1100 08 FC180400007FFF01");
        assertEvoHome_0x1100_Message((EvoHome_0x18_0x1100_0x08_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
                new byte[]{(byte) 0xFC, 0x18, 0x04, 0x00, 0x00, 0x7F, (byte) 0xFF, 0x01});
    }

    @Test
    public void decode_EvoHome_0x18_0x12B0() {
        decode("18 1203C3 05B933 12B0 03 030000");
        assertEvoHome_0x12B0_Message((EvoHome_0x18_0x12B0_0x03_Message) evoHomeMessage, 0x1203C3, 0x05B933, 0x030000);
    }

    @Test
    public void decode_EvoHome_0x18_0x1CF9_0xXX() {
        decode("18 067AEC 067AEC 1FC9 12 01 2309 067AEC 01 30C9 067AEC 01 1FC9 067AEC");
        assertEvoHome_0x1FC9_Message((EvoHome_0x18_0x1FC9_0x12_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
                new Data((byte) 0x01, (short) 0x2309, 0x067AEC), new Data((byte) 0x01, (short) 0x30C9, 0x067AEC),
                new Data((byte) 0x01, (short) 0x1FC9, 0x067AEC));

        decode("18 895E5D 895E5D 1FC9 12 00 0008 895E5D 00 3150 895E5D 00 1FC9 895E5D");
        assertEvoHome_0x1FC9_Message((EvoHome_0x18_0x1FC9_0x12_Message) evoHomeMessage, 0x895E5D, 0x895E5D,
                new Data((byte) 0x00, (short) 0x0008, 0x895E5D), new Data((byte) 0x00, (short) 0x3150, 0x895E5D),
                new Data((byte) 0x00, (short) 0x1FC9, 0x895E5D));

        decode("18 895E5D 895E5D 1FC9 18 00 2309 895E5D 00 30C9 895E5D 00 0008 895E5D 00 1FC9 895E5D");
        assertEvoHome_0x1FC9_Message((EvoHome_0x18_0x1FC9_0x12_Message) evoHomeMessage, 0x895E5D, 0x895E5D,
                new Data((byte) 0x00, (short) 0x2309, 0x895E5D), new Data((byte) 0x00, (short) 0x30C9, 0x895E5D),
                new Data((byte) 0x00, (short) 0x0008, 0x895E5D), new Data((byte) 0x00, (short) 0x1FC9, 0x895E5D));

        decode("18 895E5D 895E5D 1FC9 0C 00 2309 895E5D 00 30C9 895E5D");
        assertEvoHome_0x1FC9_Message((EvoHome_0x18_0x1FC9_0x12_Message) evoHomeMessage, 0x895E5D, 0x895E5D,
                new Data((byte) 0x00, (short) 0x2309, 0x895E5D), new Data((byte) 0x00, (short) 0x30C9, 0x895E5D));
    }

    @Test
    public void decode_EvoHome_0x18_0x1F09() {
        decode("18 067AEC 067AEC 1F09 03 FF057D");
        assertEvoHome_0x1F09_Message((EvoHome_0x18_0x1F09_0x03_BROADCAST_18_1F09_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, 0xFF057D);
    }

    @Test
    public void decode_EvoHome_0x18_0x2309() {
        decode("18 067AEC 067AEC 2309 03 00 0708");
        assertEvoHome_0x2309_Message((EvoHome_0x18_0x2309_0xXX_ROOM_DESIRED_TEMP_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("18")));
        decode("18 067AEC 067AEC 2309 03 01 0708");
        assertEvoHome_0x2309_Message((EvoHome_0x18_0x2309_0xXX_ROOM_DESIRED_TEMP_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x01, new BigDecimal("18")));

        decode("18 067AEC 067AEC 2309 06 00 02EE 01 0640");
        assertEvoHome_0x2309_Message((EvoHome_0x18_0x2309_0xXX_ROOM_DESIRED_TEMP_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("7.5")), new ZoneTemperature((byte) 0x01, new BigDecimal("16")));
        decode("18 114977 067AEC 2309 03 00 02EE");
        assertEvoHome_0x2309_Message((EvoHome_0x18_0x2309_0xXX_ROOM_DESIRED_TEMP_Message) evoHomeMessage, 0x114977,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("7.5")));

        decode("18 067AEC 067AEC 2309 06 00 02EE 01 0708");
        assertEvoHome_0x2309_Message((EvoHome_0x18_0x2309_0xXX_ROOM_DESIRED_TEMP_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("7.5")), new ZoneTemperature((byte) 0x01, new BigDecimal("18")));
    }

    @Test
    public void decode_EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL() throws Exception {

        decode("18 067AEC 067AEC 2349 0D 00 09C4 04FFFFFF 00 06 12 07 07E3");
        assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("25")), 0x04FFFFFF, LocalDateTime.of(2019, 07, 18, 06, 00));

        decode("18 067AEC 067AEC 2349 0D 00 09C4 04FFFFFF 0A 05 12 07 07E3");
        assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("25")), 0x04FFFFFF, LocalDateTime.of(2019, 07, 18, 5, 10));
        decode("18 067AEC 067AEC 2349 0D 00 09C4 04FFFFFF 14 13 11 07 07E3");
        assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("25")), 0x04FFFFFF, LocalDateTime.of(2019, 07, 17, 19, 20));

        decode("18 067AEC 067AEC 2349 0D 00 09C4 04FFFFFF 0A 0F 11 07 07E3");
        assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("25")), 0x04FFFFFF, LocalDateTime.of(2019, 07, 17, 15, 10));

        decode("18 067AEC 067AEC 2349 0D 00 0352 04FFFFFF 00 0C 10 07 07E3");
        assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("8.5")), 0x04FFFFFF, LocalDateTime.of(2019, 07, 16, 12, 00));

        testEncode_EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message(new DeviceId(0x067AEC), new ZoneTemperature((byte) 0x00, new BigDecimal("25")), LocalDateTime.of(2019, 07, 18, 06, 00), "18067AEC067AEC23490D0009C404FFFFFF0006120707E3");

    }

    @Test
    public void decode_EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_PERMANENT() throws Exception {

        EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message msg = new EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message(0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("23")));

        decode("18 067AEC 067AEC 2349 07 00 08FC 00FFFFFF");
        assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message) evoHomeMessage, msg);

        testEncode_EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message(msg.deviceId1, msg.temperature, "18067AEC067AEC2349070008FC00FFFFFF");

    }

    @Test
    public void decode_EvoHome_0x18_0x2E04_0x08() {
        decode("18 067AEC 067AEC 2E04 08 00 FFFFFFFFFFFF00");
        assertEvoHome_0x2E04_Message((EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message.Mode.NORMAL,
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00});

        decode("18 067AEC 067AEC 2E04 08 02 FFFFFFFFFFFF00");
        assertEvoHome_0x2E04_Message((EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message.Mode.ECONOMY,
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00});

        decode("18 067AEC 067AEC 2E04 08 07 FFFFFFFFFFFF00");
        assertEvoHome_0x2E04_Message((EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message.Mode.SPECIAL_PROGRAMME,
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00});

        decode("18 067AEC 067AEC 2E04 08 01 FFFFFFFFFFFF00");
        assertEvoHome_0x2E04_Message((EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message.Mode.HEATING_OFF,
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00});

        decode("18 067AEC 067AEC 2E04 08 03 FFFFFFFFFFFF00");
        assertEvoHome_0x2E04_Message((EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message.Mode.AWAY,
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00});

        decode("18 067AEC 067AEC 2E04 08 04 FFFFFFFFFFFF00");
        assertEvoHome_0x2E04_Message((EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message.Mode.EXCEPTION_DAY,
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00});
    }

    @Test
    public void decode_EvoHome_0x18_0x30C9() {
        decode("18 067AEC 067AEC 30C9 03 0009FA");
        assertEvoHome_0x30C9_Message((EvoHome_0x18_0x30C9_0xXX_ROOM_MEASURED_TEMP_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("25.54")));

        decode("18 067AEC 067AEC 30C9 06 00 0A25 01 0926");
        assertEvoHome_0x30C9_Message((EvoHome_0x18_0x30C9_0xXX_ROOM_MEASURED_TEMP_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("25.97")), new ZoneTemperature((byte) 0x01, new BigDecimal("23.42")));

        // Zone 0x03 to 0x0b are there without connected devices
        decode("18 067AEC 067AEC 30C9 24 00 0AB4 01 0975 02 7FFF 03 7FFF 04 7FFF 05 7FFF 06 7FFF 07 7FFF 08 7FFF 09 7FFF 0A 7FFF 0B 7FFF");
        assertEvoHome_0x30C9_Message((EvoHome_0x18_0x30C9_0xXX_ROOM_MEASURED_TEMP_Message) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("27.4")), new ZoneTemperature((byte) 0x01, new BigDecimal("24.21")),
                new ZoneTemperature((byte) 0x02, null), new ZoneTemperature((byte) 0x03, null),
                new ZoneTemperature((byte) 0x04, null), new ZoneTemperature((byte) 0x05, null),
                new ZoneTemperature((byte) 0x06, null), new ZoneTemperature((byte) 0x07, null),
                new ZoneTemperature((byte) 0x08, null), new ZoneTemperature((byte) 0x09, null),
                new ZoneTemperature((byte) 0x0a, null), new ZoneTemperature((byte) 0x0b, null));
    }

    @Test
    public void decode_EvoHome_0x18_0x3120_0x07() {
        decode("18 895E5D 895E5D 3120 07 0070B0000000FF");
        assertEvoHome_0x3120_Message((EvoHome_0x18_0x3120_0x07_Message) evoHomeMessage, 0x895E5D, 0x895E5D,
                new byte[]{0x00, 0x70, (byte) 0xB0, 0x00, 0x00, 0x00, (byte) 0xFF});
    }

    @Test
    public void decode_EvoHome_0x18_0x3150() {
        decode("18 067AEC 067AEC 3150 02 FC00");
        assertEvoHome_0x3150_Message((EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
                (byte) 0xFC, (short) 0);
        decode("18 114977 067AEC 3150 02 0050");
        assertEvoHome_0x3150_Message((EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message) evoHomeMessage, 0x114977, 0x067AEC,
                (byte) 0x00, (short) 80);
        decode("18 114977 067AEC 3150 02 00BC");
        assertEvoHome_0x3150_Message((EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message) evoHomeMessage, 0x114977, 0x067AEC,
                (byte) 0x00, (short) 188);
    }

    @Test
    public void decode_EvoHome_0x18_0x3B00() {
        decode("18 067AEC 067AEC 3B00 02 FCC8");
        assertEvoHome_0x3B00_Message((EvoHome_0x18_0x3B00_0x02_Message) evoHomeMessage, 0x067AEC, 0x067AEC, 0xFCC8);
    }

    @Test
    public void decode_EvoHome_0x1C_0x10E0_0x26() {
        decode("1C 895E5D FFFFFE 10E0 26 000001C8380F0100F1FF140B07E2030507E15438375246323032350000000000000000000000");
        assertEvoHome_0x10E0_Message((EvoHome_0x1C_0x10E0_0x26_Message) evoHomeMessage, 0x895E5D, 0xFFFFFE,
                new byte[]{0x00, 0x00, 0x01, (byte) 0xC8, 0x38, 0x0F, 0x01, 0x00, (byte) 0xF1, (byte) 0xFF, 0x14,
                    0x0B, 0x07, (byte) 0xE2, 0x03, 0x05, 0x07, (byte) 0xE1, 0x54, 0x38, 0x37, 0x52, 0x46, 0x32,
                    0x30, 0x32, 0x35, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
    }

    @Test
    public void decode_EvoHome_0x1C_0x1CF9_0xXX() {
        decode("1C 131589 FFFFFE 1FC9 06 00 30C9 131589");
        assertEvoHome_0x1FC9_Message((EvoHome_0x1C_0x1FC9_0xXX_Message) evoHomeMessage, 0x131589, 0xFFFFFE,
                new Data((byte) 0x00, (short) 0x30C9, 0x131589));

        decode("1C 067AEC 131589 1FC9 06 00FFFF 067AEC");
        assertEvoHome_0x1FC9_Message((EvoHome_0x1C_0x1FC9_0xXX_Message) evoHomeMessage, 0x067AEC, 0x131589,
                new Data((byte) 0x00, (short) 0xFFFF, 0x067AEC));
    }

    @Test
    public void decode_EvoHome_0x1C_0x2309() {
        decode("1C 067AEC 895E5D 2309 03 00 047E");
        assertEvoHome_0x2309_Message((EvoHome_0x1C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message) evoHomeMessage, 0x067AEC,
                0x895E5D, new ZoneTemperature((byte) 0x00, new BigDecimal("11.5")));
    }

    @Test
    public void decode_EvoHome_0x28_0x0001() {
        decode("28 067AEC 067AEC 0001 05 0000000505");
        assertEvoHome_0x0001_Message((EvoHome_0x28_0x0001_0x05__RADIO_TEST_REQUEST_FROM_MASTER_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
                new byte[]{0x00, 0x00, 0x00, 0x05, 0x05});
    }

    @Test
    public void decode_EvoHome_0x28_0x1F09_0x03() {
        decode("28 067AEC 067AEC 1F09 03 F80429");
        assertEvoHome_0x1F09_Message((EvoHome_0x28_0x1F09_0x03_Message) evoHomeMessage, 0x067AEC, 0x067AEC, 0xF80429);

        decode("28 067AEC 067AEC 1F09 03 FF057D");
        assertEvoHome_0x1F09_Message((EvoHome_0x28_0x1F09_0x03_Message) evoHomeMessage, 0x067AEC, 0x067AEC, 0xFF057D);
    }

    @Test
    public void decode_EvoHome_0x2C_0x1CF9_0xXX() {
        decode("2C 131589 067AEC 1FC9 06 0030C9 131589");
        assertEvoHome_0x1FC9_Message((EvoHome_0x2C_0x1FC9_0xXX_Message) evoHomeMessage, 0x131589, 0x067AEC,
                new Data((byte) 0x00, (short) 0x30C9, 0x131589));
    }

    @Test
    public void decode_EvoHome_0x2C_0x2309() {
        decode("2C 895E5D 067AEC 2309 03 00 047E");
        assertEvoHome_0x2309_Message((EvoHome_0x2C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message) evoHomeMessage, 0x895E5D,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("11.5")));
    }

    @Test
    public void decode_EvoHome_0x3C_0x0004_0x16() {
        decode("3C 067AEC 114977 0004 16 00 00 576F686E7A696D6D657200000000000000000000");
        assertEvoHome_0x0004_Message((EvoHome_0x3C_0x0004_0x16_Message) evoHomeMessage, 0x067AEC, 0x114977, (byte) 0x00,
                (byte) 0x00, "Wohnzimmer");

        decode("3C 067AEC 131589 0004 16 01 00 5361616C00000000000000000000000000000000");
        assertEvoHome_0x0004_Message((EvoHome_0x3C_0x0004_0x16_Message) evoHomeMessage, 0x067AEC, 0x131589, (byte) 0x01,
                (byte) 0x00, "Saal");
    }

    @Test
    public void decode_EvoHome_0x3C_0x000A() {
        decode("3C 067AEC 895E5D 000A 06 00 10 01F4 0DAC");
        ZoneParams zoneParam = new ZoneParams();
        zoneParam.zoneId = 0;
        zoneParam.windowFunction = true;
        zoneParam.operationLock = false;
        zoneParam.minTemperature = new BigDecimal("5");
        zoneParam.maxTemperature = new BigDecimal("35");
        List<ZoneParams> zoneParams = new LinkedList<>();
        zoneParams.add(zoneParam);
        assertEvoHome_0x000A_Message((EvoHome_0x3C_0x000A_ZONE_PARAMS_Message) evoHomeMessage, 0x067AEC, 0x895E5D,
                zoneParams);
    }

    @Test
    public void decode_EvoHome_0x3C_0x0100() {
        decode("3C 067AEC 114977 0100 05 006465FFFF");
        assertEvoHome_0x0100_Message((EvoHome_0x3C_0x0100_0x05_Message) evoHomeMessage, 0x067AEC, 0x114977,
                new byte[]{0x00, 0x64, 0x65, (byte) 0xFF, (byte) 0xFF});

        decode("3C 067AEC 131589 0100 05 006465FFFF");
        assertEvoHome_0x0100_Message((EvoHome_0x3C_0x0100_0x05_Message) evoHomeMessage, 0x067AEC, 0x131589,
                new byte[]{0x00, 0x64, 0x65, (byte) 0xFF, (byte) 0xFF});
    }

    @Test
    public void decode_EvoHome_0x3C_0x1F09() {
        decode("3C 067AEC 114977 1F09 03 000019");
        assertEvoHome_0x1F09_Message((EvoHome_0x3C_0x1F09_0x03_RESPONSE_3C_1F09_Message) evoHomeMessage, 0x067AEC,
                0x114977, 0x000019);
    }

    @Test
    public void decode_EvoHome_0x3C_0x2309() {
        decode("3C 067AEC 895E5D 2309 03 00 0226");
        assertEvoHome_0x2309_Message((EvoHome_0x3C_0x2309_0xXX_ROOM_DESIRED_TEMP_Message) evoHomeMessage, 0x067AEC,
                0x895E5D, new ZoneTemperature((byte) 0x00, new BigDecimal("5.5")));
    }

    @Test
    public void decode_EvoHome_0x3C_0x313F_0x09() {
        decode("3C 067AEC 114977 313F 09 00FC941E46110707E3");
        assertEvoHome_0x313F_Message((EvoHome_0x3C_0x313F_0x09_RESPONSE_3C_313F_Message) evoHomeMessage, 0x067AEC,
                0x114977, new byte[]{0x00, (byte) 0xFC, (byte) 0x94, 0x1E, 0x46, 0x11, 0x07, 0x07, (byte) 0xE3});
    }

    @Override
    public void fail(Throwable t) {
        throw new RuntimeException(t);
    }

    @Override
    public void success(EvoHomeMessage evoHomeMessage) {
        this.evoHomeMessage = evoHomeMessage;
    }

    @Override
    public void successPartial(EvoHomeMessage evoHomeMessage) {
        throw new RuntimeException("No partial message expected.");
    }

    @Override
    public void successPartialAssembled(EvoHomeMessage evoHomeMessage) {
        throw new RuntimeException("No partial message expected.");
    }

    private void testEncode_EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message(DeviceId deviceId, ZoneTemperature temperature, LocalDateTime localDateTime, String expected) throws IOException {
        encoder.writeEvoHomeZoneSetpointUntil(deviceId, temperature, localDateTime);
        assertEquals(expected, evoHomeWriter.written);
    }

    private void testEncode_EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message(DeviceId deviceId, ZoneTemperature temperature, String expected) throws IOException {
        encoder.writeEvoHomeZoneSetpointPermanent(deviceId, temperature);
        assertEquals(expected, evoHomeWriter.written);
    }

}
