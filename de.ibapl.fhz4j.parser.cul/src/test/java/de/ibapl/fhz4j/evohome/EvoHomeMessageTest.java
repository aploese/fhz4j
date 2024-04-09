/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2022-2023, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.evohome;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.cul.DataSource;
import de.ibapl.fhz4j.parser.evohome.EvoHomeParser;
import de.ibapl.fhz4j.protocol.evohome.DeviceId;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeDeviceMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessage;
import de.ibapl.fhz4j.protocol.evohome.ZoneTemperature;
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
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointRequestMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointResponseMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneSetpointWriteMessage;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneTemperatureInformationMessage;
import de.ibapl.fhz4j.writer.evohome.EvoHomeEncoder;
import de.ibapl.fhz4j.writer.evohome.EvoHomeWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    public static void assertRfSignalTestRequestMessage(RfSignalTestRequestMessage evoHomeMessage, int deviceId1,
            int deviceId2, short value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertRfSignalTestResponseMessage(RfSignalTestResponseMessage evoHomeMessage, int deviceId1,
            int deviceId2, short value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertZoneNameRequestMessage(ZoneNameRequestMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte zoneId) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zoneId, evoHomeMessage.zoneId, "zoneId");
        assertEquals(0, evoHomeMessage.unused, "unused");
    }

    public static void assertZoneNamePayloadMessage(ZoneNamePayloadMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte zoneId, byte unknown, String zoneName) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zoneId, evoHomeMessage.zoneId, "zoneId");
        assertEquals(unknown, evoHomeMessage.unused, "unused");
        assertEquals(zoneName, evoHomeMessage.zoneName, "zoneName");
    }

    public static void assertZoneManagementInformationMessage(ZoneManagementInformationMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x0008_RelayHeatDemandInformationMessage(RelayHeatDemandInformationMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte domain_id, short demand) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(domain_id, evoHomeMessage.domain_id, "domain_id");
        assertEquals(demand, evoHomeMessage.demand, "demand");
    }

    public static void assertEvoHome_0x0009_RelayFailsaveInformationMessage(RelayFailsaveInformationMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte domain_id, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(domain_id, evoHomeMessage.domain_id, "domain_id");
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assert_0x000A_ZoneConfigRequestMessage(ZoneConfigRequestMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assert_0x000A_ZoneConfigPayloadMessage(ZoneConfigPayloadMessage evoHomeMessage,
            int deviceId1, int deviceId2, List<ZoneConfigPayloadMessage.ZoneParams> zones) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        Iterator<ZoneConfigPayloadMessage.ZoneParams> expectedZoneParams = zones.iterator();
        Iterator<ZoneConfigPayloadMessage.ZoneParams> actualZoneParams = evoHomeMessage.zones.iterator();
        while (expectedZoneParams.hasNext()) {
            ZoneConfigPayloadMessage.ZoneParams expected = expectedZoneParams.next();
            assertTrue(actualZoneParams.hasNext());
            ZoneConfigPayloadMessage.ZoneParams actual = actualZoneParams.next();
            assertEquals(expected.zoneId, actual.zoneId, "zoneId");
            assertEquals(expected.operationLock, actual.operationLock, "operationLock");
            assertEquals(expected.windowFunction, actual.windowFunction, "windowFunction");
            assertEquals(expected.minTemperature, actual.minTemperature, "minTemperature");
            assertEquals(expected.maxTemperature, actual.maxTemperature, "maxTemperature");
        }
        assertFalse(actualZoneParams.hasNext());
    }

    public static void assertEvoHome_0x000C_ZoneActuatorsRequestMessage(ZoneActuatorsRequestMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte zone_idx, byte unknown0) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zone_idx, evoHomeMessage.zone_idx, "zone_idx");
        assertEquals(unknown0, evoHomeMessage.unknown0, "unknown0");
    }

    public static void assertEvoHome_0x000C_ZoneActuatorsInformationMessage(ZoneActuatorsInformationMessage evoHomeMessage, ZoneActuatorsInformationMessage expectedMessage) {
        assertEvoHomeDeviceMessage(evoHomeMessage, expectedMessage.deviceId1.id, expectedMessage.deviceId2.id);
        Iterator<ZoneActuatorsInformationMessage.ZoneActuator> expectedZoneActuators = expectedMessage.actuators.iterator();
        Iterator<ZoneActuatorsInformationMessage.ZoneActuator> actualZoneActuators = evoHomeMessage.actuators.iterator();
        while (expectedZoneActuators.hasNext()) {
            ZoneActuatorsInformationMessage.ZoneActuator expected = expectedZoneActuators.next();
            assertTrue(actualZoneActuators.hasNext());
            ZoneActuatorsInformationMessage.ZoneActuator actual = actualZoneActuators.next();
            assertEquals(expected.zone_idx, actual.zone_idx, "zone_idx");
            assertEquals(expected.unknown0, actual.unknown0, "unknown0");
        }
        assertFalse(actualZoneActuators.hasNext());
    }

    public static void assertEvoHome_0x000E_T87RF_Startup_0x000E_InformationMessage(T87RF_Startup_0x000E_InformationMessage evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assert_0x0016_RfSignalTestResponseMessage(RfSignalTestResponseMessage evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assert_0x0016_RfSignalTestRequestMessage(RfSignalTestRequestMessage evoHomeMessage, int deviceId1,
            int deviceId2, int value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(value, evoHomeMessage.value, "value");
    }

    public static void assert_0x0100_LocalizationResponseMessage(LocalizationResponseMessage evoHomeMessage, int deviceId1,
            int deviceId2, String language) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(0, evoHomeMessage.unused0, "unused0");
        assertEquals(language, evoHomeMessage.language, "language");
        assertEquals((byte) 0xFF, evoHomeMessage.unused1, "unused1");
    }

    public static void assert_0x0100_LocalizationRequestMessage(LocalizationRequestMessage evoHomeMessage, int deviceId1,
            int deviceId2, String language) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(0x00, evoHomeMessage.unused0, "unused0");
        assertEquals(language, evoHomeMessage.language, "language");
        assertEquals((byte) 0xff, evoHomeMessage.unused1, "unused1");
    }

    public static void assertEvoHome_0x042F_T87RF_Startup_0x042F_InformationMessage(T87RF_Startup_0x042F_InformationMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertEvoHome_0x1060_Message(DeviceBatteryStatusInformationMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte zone_id, float level, byte unknown0) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zone_id, evoHomeMessage.zone_id, "zone_id");
        assertEquals(level, evoHomeMessage.level, "level");
        assertEquals(unknown0, evoHomeMessage.unknown0, "unknown0");
    }

    public static void assertEvoHome_0x10E0_DeviceInformationInformationMessage(DeviceInformationInformationMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte[] unknown0, LocalDate firmware, LocalDate manufactured, String description) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(unknown0, evoHomeMessage.unknown0, "unknown0");
        assertEquals(firmware, evoHomeMessage.firmware, "firmware");
        assertEquals(manufactured, evoHomeMessage.manufactured, "manufactured");
        assertEquals(description, evoHomeMessage.description, "description");
    }

    public static void assertEvoHome_0x1100_BoilerRelayInformationMessage(BoilerRelayInformationMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte domain_id, float cycle_rate, Duration minimum_on_time, Duration minimum_off_time, byte unknown0, short proportional_band_width, byte unknown1) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(domain_id, evoHomeMessage.domain_id, "domain_id");
        assertEquals(cycle_rate, evoHomeMessage.cycle_rate, "cycle_rate");
        assertEquals(minimum_on_time, evoHomeMessage.minimum_on_time, "minimum_on_time");
        assertEquals(minimum_off_time, evoHomeMessage.minimum_off_time, "minimum_off_time");
        assertEquals(unknown0, evoHomeMessage.unknown0, "unknown0");
        assertEquals(proportional_band_width, evoHomeMessage.proportional_band_width, "proportional_band_width");
        assertEquals(unknown1, evoHomeMessage.unknown1, "unknown1");
    }

    public static void assertEvoHome_0x12B0_AbstractWindowSensorPayloadMessage(AbstractWindowSensorPayloadMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte zone_id, short unknown0) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zone_id, evoHomeMessage.zone_id, "zone_id");
        assertEquals(unknown0, evoHomeMessage.unknown0, "unknown0");
    }

    public static void assert_0x1F09_SystemSynchronizationRequestMessage(SystemSynchronizationRequestMessage evoHomeMessage,
            int deviceId1, int deviceId2, byte domain_id) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(domain_id, evoHomeMessage.domain_id, "domain_id");
    }

    public static void assertRfCheckWriteMessage(RfCheckWriteMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte[] value) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(value, evoHomeMessage.value, "value");
    }

    public static void assertSystemSynchronizationPayloadMessage(SystemSynchronizationPayloadMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte device_id, short countdown) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(device_id, evoHomeMessage.device_id, "device_id");
        assertEquals(countdown, evoHomeMessage.countdown, "countdown");
    }

    public static void assertEvoHome_0x1FC9_AbstractRfBindPayloadMessage(AbstractRfBindPayloadMessage evoHomeMessage, int deviceId1,
            int deviceId2, AbstractRfBindPayloadMessage.Data... elements) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(elements, evoHomeMessage.elements.toArray(AbstractRfBindPayloadMessage.Data[]::new), "elements");
    }

    public static void assertEvoHome_0x2309_ZoneSetpointRequestMessage(ZoneSetpointRequestMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte zone_id) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zone_id, evoHomeMessage.zone_id, "zone_id");
    }

    public static void assertEvoHome_0x2309_ZoneSetpointPayloadMessage(AbstractZoneSetpointPayloadMessage evoHomeMessage,
            int deviceId1, int deviceId2, ZoneTemperature... zoneTemperatures) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(zoneTemperatures, evoHomeMessage.zoneTemperatures.toArray(ZoneTemperature[]::new),
                "zoneTemperatures");
    }

    public static void assertEvoHome_0x2349_AbstractZoneSetpointOverrideMessage(
            AbstractZoneSetpointOverrideMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte zone_id, BigDecimal setpoint, AbstractZoneSetpointOverrideMessage.SetpointOverrideMode zone_mode, Duration countdown, LocalDateTime time_until) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zone_id, evoHomeMessage.zone_id, "zone_id");
        assertEquals(setpoint, evoHomeMessage.setpoint, "setpoint");
        assertEquals(zone_mode, evoHomeMessage.zone_mode, "zone_mode");
        assertEquals(countdown, evoHomeMessage.countdown, "countdown");
        assertEquals(time_until, evoHomeMessage.time_until, "time_until");
    }

    public static void assertEvoHome_0x2E04_AbstractControllerModePayloadMessage(AbstractControllerModePayloadMessage evoHomeMessage,
            int deviceId1, int deviceId2, AbstractControllerModePayloadMessage.Mode mode, LocalDateTime dateTime, AbstractControllerModePayloadMessage.ProgrammType programm_type) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(mode, evoHomeMessage.mode, "mode");
        assertEquals(dateTime, evoHomeMessage.dateTime, "dateTime");
        assertEquals(programm_type, evoHomeMessage.programm_type, "programm_type");
    }

    public static void assertEvoHome_0x30C9_AbstractZoneTemperaturePayloadMessage(AbstractZoneTemperaturePayloadMessage evoHomeMessage,
            int deviceId1, int deviceId2, ZoneTemperature... zoneTemperatures) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertArrayEquals(zoneTemperatures, evoHomeMessage.zoneTemperatures.toArray(ZoneTemperature[]::new),
                "zoneTemperatures");
    }

    public static void assertEvoHome_0x3120_Unknown_0x3120InformationMessage(Unknown_0x3120InformationMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte unused0, short fixed1, int unused2, byte fixed3) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(unused0, evoHomeMessage.unused0, "unused0");
        assertEquals(fixed1, evoHomeMessage.fixed1, "fixed1");
        assertEquals(unused2, evoHomeMessage.unused2, "unused2");
        assertEquals(fixed3, evoHomeMessage.fixed3, "fixed3");
    }

    public static void assertEvoHome_0x313F_SystemTimestampRequestMessage(SystemTimestampRequestMessage evoHomeMessage,
            int deviceId1, int deviceId2, byte zone_id) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zone_id, evoHomeMessage.zone_id, "zone_id");
    }

    public static void assertEvoHome_0x313F_SystemTimestampResponseMessage(SystemTimestampResponseMessage evoHomeMessage,
            int deviceId1, int deviceId2, byte zone_id, SystemTimestampResponseMessage.Direction direction, LocalDateTime timestamp) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zone_id, evoHomeMessage.zone_id, "zone_id");
        assertEquals(direction, evoHomeMessage.direction, "direction");
        assertEquals(timestamp, evoHomeMessage.timestamp, "timestamp");
    }

    public static void assertEvoHome_0x3150_ZoneHeatDemandInformationMessage(ZoneHeatDemandInformationMessage evoHomeMessage,
            int deviceId1, int deviceId2, byte zone_id, short heatDemand) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(zone_id, evoHomeMessage.zone_id, "zone_id");
        assertEquals(heatDemand, evoHomeMessage.heatDemand, "heatDemand");
    }

    public static void assertEvoHome_0x3B00_ActuatorSyncInformationMessage(ActuatorSyncInformationMessage evoHomeMessage, int deviceId1,
            int deviceId2, byte domain_id, byte state) {
        assertEvoHomeDeviceMessage(evoHomeMessage, deviceId1, deviceId2);
        assertEquals(domain_id, evoHomeMessage.domain_id, "domain_id");
        assertEquals(state, evoHomeMessage.state, "state");
    }

    public static void assertEvoHomeDeviceMessage(EvoHomeDeviceMessage evoHomeMessage, int deviceId1, int deviceId2) {
        assertNotNull(evoHomeMessage, "evo Home message is null");
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
    public void decode_EvoHome_0xXX_0x0004_0x02_ZoneNameRequestMessage() {
        decode("0C 114977 067AEC 0004 02 0000");
        assertZoneNameRequestMessage((ZoneNameRequestMessage) evoHomeMessage, 0x114977, 0x067AEC, (byte) 0x00);
        decode("0C 131589 067AEC 0004 02 01 00");
        assertZoneNameRequestMessage((ZoneNameRequestMessage) evoHomeMessage, 0x131589, 0x067AEC, (byte) 0x01);
    }

    @Test
    public void decode_EvoHome_0x0C_0x0005_0x05_ZoneManagementInformationMessage() {
        decode("18 067AEC 067AEC 0005 04 00000300");
        assertZoneManagementInformationMessage((ZoneManagementInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                new byte[]{0x00, 0x00, 0x03, 0x00});

        decode("18 895E5D 895E5D 0005 0C 000A0000 000F0000 00100000");
        assertZoneManagementInformationMessage((ZoneManagementInformationMessage) evoHomeMessage, 0x895E5D, 0x895E5D,
                new byte[]{0x00, 0x0A, 0x00, 0x00, 0x00, 0x0F, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00});
    }

    @Test
    public void decode_EvoHome_0x0C_0x000A_0x01_ZoneConfigRequestMessage() {
        decode("0C 895E5D 067AEC 000A 01 01");
        assert_0x000A_ZoneConfigRequestMessage((ZoneConfigRequestMessage) evoHomeMessage, 0x895E5D, 0x067AEC,
                (byte) 0x01);
    }

    @Test
    public void decode_EvoHome_0x0C_0x0016_0x02_RfSignalTestRequestMessage() {
        decode("0C 067AEC 114977 0016 02 00FF");
        assertRfSignalTestRequestMessage((RfSignalTestRequestMessage) evoHomeMessage, 0x067AEC, 0x114977, (short) 0x00FF);
    }

    @Test
    public void decode_EvoHome_0x3C_0x0016_0x02_RfSignalTestResponseMessage() {
        decode("3C 114977 067AEC 0016 02 004D");
        assertRfSignalTestResponseMessage((RfSignalTestResponseMessage) evoHomeMessage, 0x114977, 0x067AEC, (short) 0x004D);
    }

    @Test
    public void decode_EvoHome_0x0C_0x0100_0x05_LocalizationRequestMessage() {
        decode("0C 114977 067AEC 0100 05 006465FFFF");
        assert_0x0100_LocalizationRequestMessage((LocalizationRequestMessage) evoHomeMessage, 0x114977, 0x067AEC,
                "de");

        decode("0C 131589 067AEC 0100 05 006465FFFF");
        assert_0x0100_LocalizationRequestMessage((LocalizationRequestMessage) evoHomeMessage, 0x131589, 0x067AEC,
                "de");
    }

    @Test
    public void decode_EvoHome_0x0C_0x1F09_0x01_SystemSynchronizationRequestMessage() {
        decode("0C 114977 067AEC 1F09 01 00");
        assert_0x1F09_SystemSynchronizationRequestMessage((SystemSynchronizationRequestMessage) evoHomeMessage, 0x114977,
                0x067AEC, (byte) 0x00);
    }

    @Test
    public void decode_EvoHome_0x0C_0x2309_0x01_ZoneSetpointRequestMessage() {
        decode("0C 895E5D 067AEC 2309 01 01");
        assertEvoHome_0x2309_ZoneSetpointRequestMessage((ZoneSetpointRequestMessage) evoHomeMessage, 0x895E5D, 0x067AEC,
                (byte) 0x01);
    }

    @Test
    public void decode_EvoHome_0x0C_0x313F_0x01_SystemTimestampRequestMessage() {
        decode("0C 114977 067AEC 313F 01 00");
        assertEvoHome_0x313F_SystemTimestampRequestMessage((SystemTimestampRequestMessage) evoHomeMessage, 0x114977,
                0x067AEC, (byte) 0x00);
        decode("0C 1203C3 05B933 313F 01 00");
        assertEvoHome_0x313F_SystemTimestampRequestMessage((SystemTimestampRequestMessage) evoHomeMessage, 0x1203C3,
                0x05B933, (byte) 0x00);
    }

    @Test
    public void decode_EvoHome_0x18_0x0004_0x16_ZoneNamePayloadMessage() {
        decode("18 067AEC 067AEC 0004 16 00 00 576F686E7A696D6D657231000000000000000000");
        assertZoneNamePayloadMessage((ZoneNamePayloadMessage) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0x00,
                (byte) 0x00, "Wohnzimmer1");
        decode("18 067AEC 067AEC 0004 16 01 00 5858587878780000000000000000000000000000");
        assertZoneNamePayloadMessage((ZoneNamePayloadMessage) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0x01,
                (byte) 0x00, "XXXxxx");
        decode("18 067AEC 067AEC 0004 16 0B 007F7F7F7F7F7F7F7F7F7F7F7F7F7F7F7F7F7F7F7F");
        assertZoneNamePayloadMessage((ZoneNamePayloadMessage) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0x0B,
                (byte) 0x00, "");

    }

    @Test
    public void decode_EvoHome_0x18_0x0008_0x02_RelayHeatDemandInformationMessage() {
        decode("18 067AEC 067AEC 0008 02 FC00");
        assertEvoHome_0x0008_RelayHeatDemandInformationMessage((RelayHeatDemandInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0xFC, (short) 0);
    }

    @Test
    public void decode_EvoHome_0x18_0x0009_0x03_RelayFailsaveInformationMessage() {
        decode("18 067AEC 067AEC 0009 03 FC00FF");
        assertEvoHome_0x0009_RelayFailsaveInformationMessage((RelayFailsaveInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0xFC, (short) 0x00FF);
    }

    @Test
    public void decode_EvoHome_0x18_0x000A_0x06_ZoneConfigPayloadMessage() {
        decode("18 067AEC 067AEC 000A 06 00 10 01F4 0DAC");
        ZoneConfigPayloadMessage.ZoneParams zoneParam = new ZoneConfigPayloadMessage.ZoneParams();
        zoneParam.zoneId = 0;
        zoneParam.windowFunction = true;
        zoneParam.operationLock = false;
        zoneParam.minTemperature = new BigDecimal("5");
        zoneParam.maxTemperature = new BigDecimal("35");
        List<ZoneConfigPayloadMessage.ZoneParams> zoneParams = new LinkedList<>();
        zoneParams.add(zoneParam);
        assert_0x000A_ZoneConfigPayloadMessage((ZoneConfigPayloadMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                zoneParams);
    }

    @Test
    public void decode_EvoHome_0x18_0x000C_0xXX_ZoneActuatorsInformationMessage() {
        decode("18 895E5D 895E5D 000C 12 00 0A7FFFFFFF 00 0F7FFFFFFF 00 107FFFFFFF");
        ZoneActuatorsInformationMessage expected = new ZoneActuatorsInformationMessage(de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgParam0._8);
        expected.actuators.add(new ZoneActuatorsInformationMessage.ZoneActuator((byte) 0x00, (byte) 0x0A, new DeviceId(0x7FFFFFFF)));
        expected.actuators.add(new ZoneActuatorsInformationMessage.ZoneActuator((byte) 0x00, (byte) 0x0F, new DeviceId(0x7FFFFFFF)));
        expected.actuators.add(new ZoneActuatorsInformationMessage.ZoneActuator((byte) 0x00, (byte) 0x10, new DeviceId(0x7FFFFFFF)));
        expected.deviceId1 = new DeviceId(0x895E5D);
        expected.deviceId2 = new DeviceId(0x895E5D);
        assertEvoHome_0x000C_ZoneActuatorsInformationMessage((ZoneActuatorsInformationMessage) evoHomeMessage, expected);
    }

    @Test
    public void decode_EvoHome_0x18_0x000E_0x03_T87RF_Startup_0x000E_InformationMessage() {
        decode("18 895E5D 895E5D 000E 03 000014");
        assertEvoHome_0x000E_T87RF_Startup_0x000E_InformationMessage((T87RF_Startup_0x000E_InformationMessage) evoHomeMessage, 0x895E5D, 0x895E5D, 0x000014);
    }

    @Test
    public void decode_EvoHome_0x18_0x042F_0x08_T87RF_Startup_0x042F_InformationMessage() {
        decode("18 895E5D 895E5D 042F 08 000000000B000B1E");
        assertEvoHome_0x042F_T87RF_Startup_0x042F_InformationMessage((T87RF_Startup_0x042F_InformationMessage) evoHomeMessage, 0x895E5D, 0x895E5D,
                new byte[]{0x00, 0x00, 0x00, 0x00, 0x0B, 0x00, 0x0B, 0x1E});

        decode("18 895E5D 895E5D 042F 08 000000000C000C1C");
        assertEvoHome_0x042F_T87RF_Startup_0x042F_InformationMessage((T87RF_Startup_0x042F_InformationMessage) evoHomeMessage, 0x895E5D, 0x895E5D,
                new byte[]{0x00, 0x00, 0x00, 0x00, 0x0C, 0x00, 0x0C, 0x1C});
    }

    @Test
    public void decode_EvoHome_0x18_0x1060() {
        decode("18 114977 067AEC 1060 03 00 64 01");
        assertEvoHome_0x1060_Message((DeviceBatteryStatusInformationMessage) evoHomeMessage, 0x114977, 0x067AEC, (byte) 0x00, 50.0f, (byte) 0x01);

        //TODO 127,5 % what does this mean full empty?
        decode("18 895E5D 895E5D 1060 03 00 FF 01");
        assertEvoHome_0x1060_Message((DeviceBatteryStatusInformationMessage) evoHomeMessage, 0x895E5D, 0x895E5D, (byte) 0x00, 127.5f, (byte) 0x01);
    }

    @Test
    public void decode_EvoHome_0x18_0x10E0_0x26_DeviceInformationInformationMessage() {
        decode("18 114977 114977 10E0 26 000002FF0412FFFFFFFF 0D0207E2 0D0307DE 48523932205261646961746F72204374726C2E00");
        assertEvoHome_0x10E0_DeviceInformationInformationMessage((DeviceInformationInformationMessage) evoHomeMessage, 0x114977, 0x114977,
                new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0xFF, (byte) 0x04, (byte) 0x12, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF},
                LocalDate.of(2018, 2, 13),
                LocalDate.of(2014, 3, 13),
                "HR92 Radiator Ctrl.");
    }

    @Test
    public void decode_EvoHome_0x18_0x1100_0x08_BoilerRelayInformationMessage() {
        decode("18 067AEC 067AEC 1100 08 FC180400007FFF01");
        assertEvoHome_0x1100_BoilerRelayInformationMessage((BoilerRelayInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                (byte) 0xFC, (float) 6.0, Duration.ofSeconds(60), Duration.ofSeconds(0), (byte) 0x00, (short) 0x7FFF, (byte) 0x01);
    }

    @Test
    public void decode_EvoHome_0x18_0x12B0_0x03_AbstractWindowSensorPayloadMessage() {
        decode("18 1203C3 05B933 12B0 03 030000");
        assertEvoHome_0x12B0_AbstractWindowSensorPayloadMessage((AbstractWindowSensorPayloadMessage) evoHomeMessage, 0x1203C3, 0x05B933, (byte) 0x03, (short) 0x0000);
    }

    @Test
    public void decode_EvoHome_0x18_0x1CF9_0xXX__RfBindInformationMessage() {
        decode("18 067AEC 067AEC 1FC9 12 01 2309 067AEC 01 30C9 067AEC 01 1FC9 067AEC");
        assertEvoHome_0x1FC9_AbstractRfBindPayloadMessage((RfBindInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                new AbstractRfBindPayloadMessage.Data((byte) 0x01, (short) 0x2309, 0x067AEC), new AbstractRfBindPayloadMessage.Data((byte) 0x01, (short) 0x30C9, 0x067AEC),
                new AbstractRfBindPayloadMessage.Data((byte) 0x01, (short) 0x1FC9, 0x067AEC));

        decode("18 895E5D 895E5D 1FC9 12 00 0008 895E5D 00 3150 895E5D 00 1FC9 895E5D");
        assertEvoHome_0x1FC9_AbstractRfBindPayloadMessage((RfBindInformationMessage) evoHomeMessage, 0x895E5D, 0x895E5D,
                new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x0008, 0x895E5D), new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x3150, 0x895E5D),
                new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x1FC9, 0x895E5D));

        decode("18 895E5D 895E5D 1FC9 18 00 2309 895E5D 00 30C9 895E5D 00 0008 895E5D 00 1FC9 895E5D");
        assertEvoHome_0x1FC9_AbstractRfBindPayloadMessage((RfBindInformationMessage) evoHomeMessage, 0x895E5D, 0x895E5D,
                new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x2309, 0x895E5D), new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x30C9, 0x895E5D),
                new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x0008, 0x895E5D), new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x1FC9, 0x895E5D));

        decode("18 895E5D 895E5D 1FC9 0C 00 2309 895E5D 00 30C9 895E5D");
        assertEvoHome_0x1FC9_AbstractRfBindPayloadMessage((RfBindInformationMessage) evoHomeMessage, 0x895E5D, 0x895E5D,
                new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x2309, 0x895E5D), new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x30C9, 0x895E5D));
    }

    @Test
    public void decode_EvoHome_0x18_0x1F09_0x03_SystemSynchronizationPayloadMessage() {
        decode("18 067AEC 067AEC 1F09 03 FF057D");
        assertSystemSynchronizationPayloadMessage((SystemSynchronizationPayloadMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, (byte) 0xFF, (short) 0x057D);
    }

    @Test
    public void decode_EvoHome_0x1X_0x2309_0xXX_ZoneSetpointInformationMessage() {
        decode("18 067AEC 067AEC 2309 03 00 0708");
        assertEvoHome_0x2309_ZoneSetpointPayloadMessage((ZoneSetpointInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("18")));
        decode("18 067AEC 067AEC 2309 03 01 0708");
        assertEvoHome_0x2309_ZoneSetpointPayloadMessage((ZoneSetpointInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x01, new BigDecimal("18")));

        decode("1C 067AEC 895E5D 2309 03 00 047E");
        assertEvoHome_0x2309_ZoneSetpointPayloadMessage((ZoneSetpointInformationMessage) evoHomeMessage, 0x067AEC,
                0x895E5D, new ZoneTemperature((byte) 0x00, new BigDecimal("11.5")));

        decode("18 067AEC 067AEC 2309 06 00 02EE 01 0640");
        assertEvoHome_0x2309_ZoneSetpointPayloadMessage((ZoneSetpointInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("7.5")), new ZoneTemperature((byte) 0x01, new BigDecimal("16")));
        decode("18 114977 067AEC 2309 03 00 02EE");
        assertEvoHome_0x2309_ZoneSetpointPayloadMessage((ZoneSetpointInformationMessage) evoHomeMessage, 0x114977,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("7.5")));

        decode("18 067AEC 067AEC 2309 06 00 02EE 01 0708");
        assertEvoHome_0x2309_ZoneSetpointPayloadMessage((ZoneSetpointInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("7.5")), new ZoneTemperature((byte) 0x01, new BigDecimal("18")));

    }

    @Test
    public void decode_EvoHome_0x18_0x2349_0x0D_ZoneSetpointOverrideInformationMessage() throws Exception {
        decode("18 067AEC 067AEC 2349 0D 00 09C4 04 FFFFFF 0006 120707E3");
        assertEvoHome_0x2349_AbstractZoneSetpointOverrideMessage((ZoneSetpointOverrideInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, (byte) 0x00, new BigDecimal("25"), AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.TEMPORARY_OVERRIDE, Duration.ofMinutes(0xFFFFFF), LocalDateTime.of(2019, 07, 18, 06, 00));

        decode("18 067AEC 067AEC 2349 0D 00 09C4 04 FFFFFF 0A05 120707E3");
        assertEvoHome_0x2349_AbstractZoneSetpointOverrideMessage((ZoneSetpointOverrideInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, (byte) 0x00, new BigDecimal("25"), AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.TEMPORARY_OVERRIDE, Duration.ofMinutes(0xFFFFFF), LocalDateTime.of(2019, 07, 18, 5, 10));

        decode("18 067AEC 067AEC 2349 0D 00 09C4 04 FFFFFF 1413 110707E3");
        assertEvoHome_0x2349_AbstractZoneSetpointOverrideMessage((ZoneSetpointOverrideInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, (byte) 0x00, new BigDecimal("25"), AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.TEMPORARY_OVERRIDE, Duration.ofMinutes(0xFFFFFF), LocalDateTime.of(2019, 07, 17, 19, 20));

        decode("18 067AEC 067AEC 2349 0D 00 09C4 04 FFFFFF 0A0F 110707E3");
        assertEvoHome_0x2349_AbstractZoneSetpointOverrideMessage((ZoneSetpointOverrideInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, (byte) 0x00, new BigDecimal("25"), AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.TEMPORARY_OVERRIDE, Duration.ofMinutes(0xFFFFFF), LocalDateTime.of(2019, 07, 17, 15, 10));

        decode("18 067AEC 067AEC 2349 0D 00 0352 04 FFFFFF 000C 100707E3");
        assertEvoHome_0x2349_AbstractZoneSetpointOverrideMessage((ZoneSetpointOverrideInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, (byte) 0x00, new BigDecimal("8.5"), AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.TEMPORARY_OVERRIDE, Duration.ofMinutes(0xFFFFFF), LocalDateTime.of(2019, 07, 16, 12, 00));

        testEncode_EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message(new DeviceId(0x067AEC), new ZoneTemperature((byte) 0x00, new BigDecimal("25")), LocalDateTime.of(2019, 07, 18, 06, 00), "18067AEC067AEC23490D0009C404FFFFFF0006120707E3");
    }

    @Test
    public void decode_EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_PERMANENT() throws Exception {
        decode("18 067AEC 067AEC 2349 07 00 08FC 00FFFFFF");
        assertEvoHome_0x2349_AbstractZoneSetpointOverrideMessage((ZoneSetpointOverrideInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0x00, new BigDecimal("23"), AbstractZoneSetpointOverrideMessage.SetpointOverrideMode.FOLLOW_SCHEDULE, Duration.ofMinutes(0xFFFFFF), null);

//        testEncode_EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message(msg.deviceId1, msg.temperature, "18067AEC067AEC2349070008FC00FFFFFF");
    }

    @Test
    public void decode_EvoHome_0x18_0x2E04_0x08_ControllerModeInformationMessage() {
        decode("18 067AEC 067AEC 2E04 08 00 FFFF FFFFFFFF 00");
        assertEvoHome_0x2E04_AbstractControllerModePayloadMessage((ControllerModeInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                AbstractControllerModePayloadMessage.Mode.NORMAL, null, AbstractControllerModePayloadMessage.ProgrammType.PERMANENT);

        decode("18 067AEC 067AEC 2E04 08 02 FFFF FFFFFFFF 00");
        assertEvoHome_0x2E04_AbstractControllerModePayloadMessage((ControllerModeInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                AbstractControllerModePayloadMessage.Mode.ECONOMY, null, AbstractControllerModePayloadMessage.ProgrammType.PERMANENT);

        decode("18 067AEC 067AEC 2E04 08 07 FFFF FFFFFFFF 00");
        assertEvoHome_0x2E04_AbstractControllerModePayloadMessage((ControllerModeInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                AbstractControllerModePayloadMessage.Mode.SPECIAL_PROGRAMME, null, AbstractControllerModePayloadMessage.ProgrammType.PERMANENT);

        decode("18 067AEC 067AEC 2E04 08 01 FFFF FFFFFFFF 00");
        assertEvoHome_0x2E04_AbstractControllerModePayloadMessage((ControllerModeInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                AbstractControllerModePayloadMessage.Mode.HEATING_OFF, null, AbstractControllerModePayloadMessage.ProgrammType.PERMANENT);

        decode("18 067AEC 067AEC 2E04 08 03 FFFF FFFFFFFF 00");
        assertEvoHome_0x2E04_AbstractControllerModePayloadMessage((ControllerModeInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                AbstractControllerModePayloadMessage.Mode.AWAY, null, AbstractControllerModePayloadMessage.ProgrammType.PERMANENT);

        decode("18 067AEC 067AEC 2E04 08 04 FFFF FFFFFFFF 00");
        assertEvoHome_0x2E04_AbstractControllerModePayloadMessage((ControllerModeInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                AbstractControllerModePayloadMessage.Mode.EXCEPTION_DAY, null, AbstractControllerModePayloadMessage.ProgrammType.PERMANENT);
    }

    @Test
    public void decode_EvoHome_0x18_0x30C9_0xXX_ZoneTemperatureInformationMessage() {
        decode("18 067AEC 067AEC 30C9 03 0009FA");
        assertEvoHome_0x30C9_AbstractZoneTemperaturePayloadMessage((ZoneTemperatureInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("25.54")));

        decode("18 067AEC 067AEC 30C9 06 00 0A25 01 0926");
        assertEvoHome_0x30C9_AbstractZoneTemperaturePayloadMessage((ZoneTemperatureInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("25.97")), new ZoneTemperature((byte) 0x01, new BigDecimal("23.42")));

        // Zone 0x03 to 0x0b are there without connected devices
        decode("18 067AEC 067AEC 30C9 24 00 0AB4 01 0975 02 7FFF 03 7FFF 04 7FFF 05 7FFF 06 7FFF 07 7FFF 08 7FFF 09 7FFF 0A 7FFF 0B 7FFF");
        assertEvoHome_0x30C9_AbstractZoneTemperaturePayloadMessage((ZoneTemperatureInformationMessage) evoHomeMessage, 0x067AEC,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("27.4")), new ZoneTemperature((byte) 0x01, new BigDecimal("24.21")),
                new ZoneTemperature((byte) 0x02, null), new ZoneTemperature((byte) 0x03, null),
                new ZoneTemperature((byte) 0x04, null), new ZoneTemperature((byte) 0x05, null),
                new ZoneTemperature((byte) 0x06, null), new ZoneTemperature((byte) 0x07, null),
                new ZoneTemperature((byte) 0x08, null), new ZoneTemperature((byte) 0x09, null),
                new ZoneTemperature((byte) 0x0a, null), new ZoneTemperature((byte) 0x0b, null));
    }

    @Test
    public void decode_EvoHome_0x18_0x3120_0x07() {
        decode("18 895E5D 895E5D 3120 07 00 70B0 000000 FF");
        assertEvoHome_0x3120_Unknown_0x3120InformationMessage((Unknown_0x3120InformationMessage) evoHomeMessage, 0x895E5D, 0x895E5D,
                (byte) 0x00, (short) 0x70B0, 0x000000, (byte) 0xFF);
    }

    @Test
    public void decode_EvoHome_0x18_0x3150_0x02_ZoneHeatDemandInformationMessage() {
        decode("18 067AEC 067AEC 3150 02 FC00");
        assertEvoHome_0x3150_ZoneHeatDemandInformationMessage((ZoneHeatDemandInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC,
                (byte) 0xFC, (short) 0);
        decode("18 114977 067AEC 3150 02 0050");
        assertEvoHome_0x3150_ZoneHeatDemandInformationMessage((ZoneHeatDemandInformationMessage) evoHomeMessage, 0x114977, 0x067AEC,
                (byte) 0x00, (short) 80);
        decode("18 114977 067AEC 3150 02 00BC");
        assertEvoHome_0x3150_ZoneHeatDemandInformationMessage((ZoneHeatDemandInformationMessage) evoHomeMessage, 0x114977, 0x067AEC,
                (byte) 0x00, (short) 188);
    }

    @Test
    public void decode_EvoHome_0x18_0x3B00_0x02_ActuatorSyncInformationMessage() {
        decode("18 067AEC 067AEC 3B00 02 FC C8");
        assertEvoHome_0x3B00_ActuatorSyncInformationMessage((ActuatorSyncInformationMessage) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0xFC, (byte) 0xC8);
    }

    @Test
    public void decode_EvoHome_0x1C_0x10E0_0x26_DeviceInformationInformationMessage() {
        decode("1C 895E5D FFFFFE 10E0 26 000001C8380F0100F1FF 140B07E2 030507E1 5438375246323032350000000000000000000000");
        assertEvoHome_0x10E0_DeviceInformationInformationMessage((DeviceInformationInformationMessage) evoHomeMessage, 0x895E5D, 0xFFFFFE,
                new byte[]{0x00, 0x00, 0x01, (byte) 0xC8, 0x38, 0x0F, 0x01, 0x00, (byte) 0xF1, (byte) 0xFF},
                LocalDate.of(2018, 11, 20),
                LocalDate.of(2017, 5, 3),
                "T87RF2025");
    }

    @Test
    public void decode_EvoHome_0x1C_0x1CF9_0xXX_RfBindInformationMessage() {
        decode("1C 131589 FFFFFE 1FC9 06 00 30C9 131589");
        assertEvoHome_0x1FC9_AbstractRfBindPayloadMessage((RfBindInformationMessage) evoHomeMessage, 0x131589, 0xFFFFFE,
                new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x30C9, 0x131589));

        decode("1C 067AEC 131589 1FC9 06 00FFFF 067AEC");
        assertEvoHome_0x1FC9_AbstractRfBindPayloadMessage((RfBindInformationMessage) evoHomeMessage, 0x067AEC, 0x131589,
                new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0xFFFF, 0x067AEC));
    }

    @Test
    public void decode_EvoHome_0x28_0x0001_RfCheckWriteMessage() {
        decode("28 067AEC 067AEC 0001 05 0000000505");
        assertRfCheckWriteMessage((RfCheckWriteMessage) evoHomeMessage, 0x067AEC, 0x067AEC, new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x05});
    }

    @Test
    public void decode_EvoHome_0x28_0x1F09_0x03_SystemSynchronizationPayloadMessage() {
        decode("28 067AEC 067AEC 1F09 03 F80429");
        assertSystemSynchronizationPayloadMessage((SystemSynchronizationPayloadMessage) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0xF8, (short) 0x0429);

        decode("28 067AEC 067AEC 1F09 03 FF057D");
        assertSystemSynchronizationPayloadMessage((SystemSynchronizationPayloadMessage) evoHomeMessage, 0x067AEC, 0x067AEC, (byte) 0xFF, (short) 0x057D);
    }

    @Test
    public void decode_EvoHome_0x2C_0x1CF9_0xXX_RfBindWriteMessage() {
        decode("2C 131589 067AEC 1FC9 06 0030C9 131589");
        assertEvoHome_0x1FC9_AbstractRfBindPayloadMessage((RfBindWriteMessage) evoHomeMessage, 0x131589, 0x067AEC,
                new AbstractRfBindPayloadMessage.Data((byte) 0x00, (short) 0x30C9, 0x131589));
    }

    @Test
    public void decode_EvoHome_0x2C_0x2309_0x03_ZoneSetpointWriteMessage() {
        decode("2C 895E5D 067AEC 2309 03 00 047E");
        assertEvoHome_0x2309_ZoneSetpointPayloadMessage((ZoneSetpointWriteMessage) evoHomeMessage, 0x895E5D,
                0x067AEC, new ZoneTemperature((byte) 0x00, new BigDecimal("11.5")));
    }

    @Test
    public void decode_EvoHome_0x3C_0x0004_0x16() {
        decode("3C 067AEC 114977 0004 16 00 00 576F686E7A696D6D657200000000000000000000");
        assertZoneNamePayloadMessage((ZoneNamePayloadMessage) evoHomeMessage, 0x067AEC, 0x114977, (byte) 0x00,
                (byte) 0x00, "Wohnzimmer");

        decode("3C 067AEC 131589 0004 16 01 00 5361616C00000000000000000000000000000000");
        assertZoneNamePayloadMessage((ZoneNamePayloadMessage) evoHomeMessage, 0x067AEC, 0x131589, (byte) 0x01,
                (byte) 0x00, "Saal");
    }

    @Test
    public void decode_EvoHome_0x3C_0x000A() {
        decode("3C 067AEC 895E5D 000A 06 00 10 01F4 0DAC");
        ZoneConfigPayloadMessage.ZoneParams zoneParam = new ZoneConfigPayloadMessage.ZoneParams();
        zoneParam.zoneId = 0;
        zoneParam.windowFunction = true;
        zoneParam.operationLock = false;
        zoneParam.minTemperature = new BigDecimal("5");
        zoneParam.maxTemperature = new BigDecimal("35");
        List<ZoneConfigPayloadMessage.ZoneParams> zoneParams = new LinkedList<>();
        zoneParams.add(zoneParam);
        assert_0x000A_ZoneConfigPayloadMessage((ZoneConfigPayloadMessage) evoHomeMessage, 0x067AEC, 0x895E5D,
                zoneParams);
    }

    @Test
    public void decode_EvoHome_0x3C_0x0100_LocalizationResponseMessage() {
        decode("3C 067AEC 114977 0100 05 006465FFFF");
        assert_0x0100_LocalizationResponseMessage((LocalizationResponseMessage) evoHomeMessage, 0x067AEC, 0x114977, "de");

        decode("3C 067AEC 131589 0100 05 006465FFFF");
        assert_0x0100_LocalizationResponseMessage((LocalizationResponseMessage) evoHomeMessage, 0x067AEC, 0x131589, "de");
    }

    @Test
    public void decode_EvoHome_0x3C_0x1F09() {
        decode("3C 067AEC 114977 1F09 03 000019");
        assertSystemSynchronizationPayloadMessage((SystemSynchronizationPayloadMessage) evoHomeMessage, 0x067AEC,
                0x114977, (byte) 0x00, (short) 0x0019);
    }

    @Test
    public void decode_EvoHome_0x3C_0x2309_0x03_ZoneSetpointResponseMessage() {
        decode("3C 067AEC 895E5D 2309 03 00 0226");
        assertEvoHome_0x2309_ZoneSetpointPayloadMessage((ZoneSetpointResponseMessage) evoHomeMessage, 0x067AEC,
                0x895E5D, new ZoneTemperature((byte) 0x00, new BigDecimal("5.5")));
    }

    @Test
    public void decode_EvoHome_0x3C_0x313F_0x09_SystemTimestampResponseMessage() {
        decode("3C 067AEC 114977 313F 09 00 FC 941E46 110707E3");
//        decode("3C 067AEC 114977 313F 09 00 FC 2F0024 070107E4");

        assertEvoHome_0x313F_SystemTimestampResponseMessage((SystemTimestampResponseMessage) evoHomeMessage, 0x067AEC,
                0x114977, (byte) 0x00, SystemTimestampResponseMessage.Direction.TO_DEVICE, LocalDateTime.of(2019, Month.JULY, 17, 6, 30, 20));
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
