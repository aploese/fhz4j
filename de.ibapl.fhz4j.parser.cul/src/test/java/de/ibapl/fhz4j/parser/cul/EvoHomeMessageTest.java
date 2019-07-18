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
package de.ibapl.fhz4j.parser.cul;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x0004_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x000A_0x01_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x0016_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x0100_0x05_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x1F09_0x01_REQUEST_0C_1F09_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x2309_0x01_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x0004_0x16_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x0005_0x04_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x0008_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x0009_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x000A_0xXX_ZONES_PARAMS_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x000A_0xXX_ZONES_PARAMS_Message.ZoneParams;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x000E_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x042F_0x08_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x1060_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x10E0_0x26_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x1100_0x08_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x12B0_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x1F09_0x03_BROADCAST_18_1F09_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x1FC9_0x12_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x2309_0x03_ROOM_DESIRED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x30C9_0x03_ROOM_MEASURED_TEMP_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x18_0x3B00_0x02_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x1C_0x1FC9_0x06_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x28_0x0001_0x05_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x28_0x1F09_0x03_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x2C_0x1FC9_0x06_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x0004_0x16_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x0100_0x05_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x1F09_0x03_RESPONSE_3C_1F09_Message;
import de.ibapl.fhz4j.protocol.evohome.EvoHome_0x3C_0x313F_0x09_RESPONSE_3C_313F_Message;

import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class EvoHomeMessageTest implements ParserListener<EvoHomeMessage> {

	private EvoHomeParser parser = new EvoHomeParser(this);
	private EvoHomeMessage evoHomeMessage;
	private Throwable error;

	/**
	 * Decode but skip whitspaces
	 * 
	 * @param s
	 */
	private void decode(String s) {
		evoHomeMessage = null;
		error = null;
		parser.init();
		for (char c : s.toCharArray()) {
			if (c != ' ') {
				parser.parse(c);
			}
		}
	}

	@Override
	public void success(EvoHomeMessage evoHomeMessage) {
		this.evoHomeMessage = evoHomeMessage;
	}

	@Override
	public void fail(Throwable t) {
		error = t;
	}

	@Override
	public void successPartial(EvoHomeMessage evoHomeMessage) {
		throw new RuntimeException("No partial message expected.");
	}

	@Override
	public void successPartialAssembled(EvoHomeMessage evoHomeMessage) {
		throw new RuntimeException("No partial message expected.");
	}

	public static void assertEvoHome_0x0001_Message(EvoHome_0x28_0x0001_0x05_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte[] value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertArrayEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x0004_Message(EvoHome_0x0C_0x0004_0x02_Message evoHomeMessage, int deviceId1,
			int deviceId2, int value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x042F_Message(EvoHome_0x18_0x042F_0x08_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte[] value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertArrayEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x0004_Message(EvoHome_0x18_0x0004_0x16_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte[] value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertArrayEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x0004_Message(EvoHome_0x3C_0x0004_0x16_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte[] value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertArrayEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x0005_Message(EvoHome_0x18_0x0005_0x04_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte[] value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertArrayEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x0016_Message(EvoHome_0x0C_0x0016_0x02_Message evoHomeMessage, int deviceId1,
			int deviceId2, int value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x0100_Message(EvoHome_0x0C_0x0100_0x05_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte[] value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertArrayEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x0100_Message(EvoHome_0x3C_0x0100_0x05_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte[] value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertArrayEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x1F09_Message(EvoHome_0x0C_0x1F09_0x01_REQUEST_0C_1F09_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x1F09_Message(EvoHome_0x28_0x1F09_0x03_Message evoHomeMessage, int deviceId1,
			int deviceId2, int unknown) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(unknown, evoHomeMessage.unknown, "unknown");
	}

	public static void assertEvoHome_0x1F09_Message(EvoHome_0x3C_0x1F09_0x03_RESPONSE_3C_1F09_Message evoHomeMessage, int deviceId1,
			int deviceId2, int value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x1F09_Message(EvoHome_0x18_0x1F09_0x03_BROADCAST_18_1F09_Message evoHomeMessage, int deviceId1,
			int deviceId2, int value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x0008_Message(EvoHome_0x18_0x0008_0x02_Message evoHomeMessage, int deviceId1,
			int deviceId2, int value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x0009_Message(EvoHome_0x18_0x0009_0x03_Message evoHomeMessage, int deviceId1,
			int deviceId2, int value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x000A_Message(EvoHome_0x18_0x000A_0xXX_ZONES_PARAMS_Message evoHomeMessage, int deviceId1,
			int deviceId2, List<ZoneParams> zones) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		Iterator<ZoneParams> expectedZoneParams = zones.iterator();
		Iterator<ZoneParams> actualZoneParams = evoHomeMessage.zones.iterator();
		while (expectedZoneParams.hasNext()) {
			ZoneParams expected = expectedZoneParams.next();
			assertTrue(actualZoneParams.hasNext());
			ZoneParams actual = actualZoneParams.next();
			assertEquals(expected.zoneId, actual.zoneId,"zoneId");
			assertEquals(expected.flags, actual.flags,"flags");
			assertEquals(expected.minTemperature, actual.minTemperature,"minTemperature");
			assertEquals(expected.maxTemperature, actual.maxTemperature,"maxTemperature");
		}
		assertFalse(actualZoneParams.hasNext());
	}

	public static void assertEvoHome_0x000A_Message(EvoHome_0x0C_0x000A_0x01_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x2309_Message(EvoHome_0x0C_0x2309_0x01_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x1060_Message(EvoHome_0x18_0x1060_0x03_Message evoHomeMessage, int deviceId1,
			int deviceId2, int value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x10E0_Message(EvoHome_0x18_0x10E0_0x26_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte[] value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertArrayEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x1100_Message(EvoHome_0x18_0x1100_0x08_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte[] value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertArrayEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x12B0_Message(EvoHome_0x18_0x12B0_0x03_Message evoHomeMessage, int deviceId1,
			int deviceId2, int value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x2309_Message(EvoHome_0x18_0x2309_0x03_ROOM_DESIRED_TEMP_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte zone, float temperature) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(zone, evoHomeMessage.zone, "zone");
		assertEquals(temperature, evoHomeMessage.temperature, "temperature");
	}

	public static void assertEvoHome_0x2349_Message(EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message evoHomeMessage, int deviceId1,
			int deviceId2, float temperature, int unknown, LocalDateTime until) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(temperature, evoHomeMessage.temperature, "temperature");
		assertEquals(unknown, evoHomeMessage.unknown, "unknown");
		assertEquals(until, evoHomeMessage.until, "until");
	}

	public static void assertEvoHome_0x2349_Message(EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message evoHomeMessage, int deviceId1,
			int deviceId2, float temperature, int unknown) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(temperature, evoHomeMessage.temperature, "temperature");
		assertEquals(unknown, evoHomeMessage.unknown, "unknown");
	}

	public static void assertEvoHome_0x30C9_Message(EvoHome_0x18_0x30C9_0x03_ROOM_MEASURED_TEMP_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte zone, float temperature) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(zone, evoHomeMessage.zone, "zone");
		assertEquals(temperature, evoHomeMessage.temperature, "temperature");
	}

	public static void assertEvoHome_0x3150_Message(EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte zone, short heatDemand) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(zone, evoHomeMessage.zone, "zone");
		assertEquals(heatDemand, evoHomeMessage.heatDemand, "heatDemand");
	}

	public static void assertEvoHome_0x313F_Message(EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x313F_Message(EvoHome_0x3C_0x313F_0x09_RESPONSE_3C_313F_Message evoHomeMessage, int deviceId1,
			int deviceId2, byte[] value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertArrayEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x1FC9_Message(EvoHome_0x1C_0x1FC9_0x06_Message evoHomeMessage, int deviceId1,
			int deviceId2, int unknown, int deviceId) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(unknown, evoHomeMessage.unknown, "unknown");
		assertEquals(deviceId, evoHomeMessage.deviceId, "deviceId");
	}

	public static void assertEvoHome_0x1FC9_Message(EvoHome_0x2C_0x1FC9_0x06_Message evoHomeMessage, int deviceId1,
			int deviceId2, int unknown, int deviceId) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(unknown, evoHomeMessage.unknown, "unknown");
		assertEquals(deviceId, evoHomeMessage.deviceId, "deviceId");
	}

	public static void assertEvoHome_0x1FC9_Message(EvoHome_0x18_0x1FC9_0x12_Message evoHomeMessage, int deviceId1,
			int deviceId2, int unknownFlags1, int unknownDeviceId1, int unknownFlags2, int unknownDeviceId2, int unknownFlags3, int unknownDeviceId3) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(unknownFlags1, evoHomeMessage.unknownFlags1, "unknownFlags1");
		assertEquals(unknownDeviceId1, evoHomeMessage.unknownDeviceId1, "unknownDeviceId1");
		assertEquals(unknownFlags2, evoHomeMessage.unknownFlags2, "unknownFlags2");
		assertEquals(unknownDeviceId2, evoHomeMessage.unknownDeviceId2, "unknownDeviceId2");
		assertEquals(unknownFlags3, evoHomeMessage.unknownFlags3, "unknownFlags3");
		assertEquals(unknownDeviceId3, evoHomeMessage.unknownDeviceId3, "unknownDeviceId3");
	}

	public static void assertEvoHome_0x3B00_Message(EvoHome_0x18_0x3B00_0x02_Message evoHomeMessage, int deviceId1,
			int deviceId2, int value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	public static void assertEvoHome_0x000E_Message(EvoHome_0x18_0x000E_0x03_Message evoHomeMessage, int deviceId1,
			int deviceId2, int value) {
		assertNotNull(evoHomeMessage);
		assertEquals(deviceId1, evoHomeMessage.deviceId1, "deviceId1");
		assertEquals(deviceId2, evoHomeMessage.deviceId2, "deviceId2");
		assertEquals(value, evoHomeMessage.value, "value");
	}

	@Test
	public void decode_EvoHome_0x18_0x1F09() {
		decode("18 067AEC 067AEC 1F09 03 FF057D");
		assertEvoHome_0x1F09_Message((EvoHome_0x18_0x1F09_0x03_BROADCAST_18_1F09_Message) evoHomeMessage, 0x067AEC, 0x067AEC, 0xFF057D);
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
		decode("18 067AEC 067AEC 000A 06 001001F40DAC");
		ZoneParams zoneParam = new ZoneParams();
		zoneParam.zoneId = 0;
		zoneParam.flags = 0x10;
		zoneParam.minTemperature = 5.0f;
		zoneParam.maxTemperature = 35.0f;
		List<ZoneParams> zoneParams = new LinkedList<>();
		zoneParams.add(zoneParam);
		assertEvoHome_0x000A_Message((EvoHome_0x18_0x000A_0xXX_ZONES_PARAMS_Message) evoHomeMessage, 0x067AEC, 0x067AEC, zoneParams);
	}

	@Test
	public void decode_EvoHome_0x18_0x2309() {
		decode("18 067AEC 067AEC 2309 03 00 0708");
		assertEvoHome_0x2309_Message((EvoHome_0x18_0x2309_0x03_ROOM_DESIRED_TEMP_Message) evoHomeMessage, 0x067AEC, 0x067AEC, (byte)0x00, 18.0f);
		decode("18 067AEC 067AEC 2309 03 01 0708");
		assertEvoHome_0x2309_Message((EvoHome_0x18_0x2309_0x03_ROOM_DESIRED_TEMP_Message) evoHomeMessage, 0x067AEC, 0x067AEC, (byte)0x01, 18.0f);
	}

	@Test
	public void decode_EvoHome_0x18_0x30C9() {
		decode("18 067AEC 067AEC 30C9 03 0009FA");
		assertEvoHome_0x30C9_Message((EvoHome_0x18_0x30C9_0x03_ROOM_MEASURED_TEMP_Message) evoHomeMessage, 0x067AEC, 0x067AEC, (byte)0x00, 25.54f);
	}

	@Test
	public void decode_EvoHome_0x18_0x3150() {
		decode("18 067AEC 067AEC 3150 02 FC00");
		assertEvoHome_0x3150_Message((EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message) evoHomeMessage, 0x067AEC, 0x067AEC, (byte)0xFC, (short) 0);
		decode("18 114977 067AEC 3150 02 0050");
		assertEvoHome_0x3150_Message((EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message) evoHomeMessage, 0x114977, 0x067AEC, (byte)0x00, (short) 80);
		decode("18 114977 067AEC 3150 02 00BC");
		assertEvoHome_0x3150_Message((EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message) evoHomeMessage, 0x114977, 0x067AEC, (byte)0x00, (short) 188);
	}

	@Test
	public void decode_EvoHome_0x18_0x3B00() {
		decode("18 067AEC 067AEC 3B00 02 FCC8");
		assertEvoHome_0x3B00_Message((EvoHome_0x18_0x3B00_0x02_Message) evoHomeMessage, 0x067AEC, 0x067AEC, 0xFCC8);
	}

	@Test
	public void decode_EvoHome_0x18_0x1100() {
		decode("18 067AEC 067AEC 1100 08 FC180400007FFF01");
		assertEvoHome_0x1100_Message((EvoHome_0x18_0x1100_0x08_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
				new byte[] { (byte) 0xFC, 0x18, 0x04, 0x00, 0x00, 0x7F, (byte) 0xFF, 0x01 });
	}

	@Test
	public void decode_EvoHome_0x18_0x12B0() {
		decode("18 1203C3 05B933 12B0 03 030000");
		assertEvoHome_0x12B0_Message((EvoHome_0x18_0x12B0_0x03_Message) evoHomeMessage, 0x1203C3, 0x05B933, 0x030000);
	}

	@Test
	public void decode_EvoHome_0x18_0x1060() {
		decode("18 114977 067AEC 1060 03 006401");
		assertEvoHome_0x1060_Message((EvoHome_0x18_0x1060_0x03_Message) evoHomeMessage, 0x114977, 0x067AEC, 0x006401);

		decode("18 895E5D 895E5D 1060 03 00FF01");
		assertEvoHome_0x1060_Message((EvoHome_0x18_0x1060_0x03_Message) evoHomeMessage, 0x895E5D, 0x895E5D, 0x00FF01);
	}

	@Test
	public void decode_EvoHome_0x18_0x10E0() {
		decode("18 114977 114977 10E0 26 000002FF0412FFFFFFFF0D0207E20D0307DE48523932205261646961746F72204374726C2E00");
		assertEvoHome_0x10E0_Message((EvoHome_0x18_0x10E0_0x26_Message) evoHomeMessage, 0x114977, 0x114977,
				new byte[] { 0x00, 0x00, 0x02, (byte) 0xFF, 0x04, 0x12, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
						(byte) 0xFF, 0x0D, 0x02, 0x07, (byte) 0xE2, 0x0D, 0x03, 0x07, (byte) 0xDE, 0x48, 0x52, 0x39,
						0x32, 0x20, 0x52, 0x61, 0x64, 0x69, 0x61, 0x74, 0x6F, 0x72, 0x20, 0x43, 0x74, 0x72, 0x6C, 0x2E,
						0x00 });
	}

	@Test
	public void decode_EvoHome_0x0C_0x0004() {
		decode("0C 114977 067AEC 0004 02 0000");
		assertEvoHome_0x0004_Message((EvoHome_0x0C_0x0004_0x02_Message) evoHomeMessage, 0x114977, 0x067AEC, 0x0000);
	}

	@Test
	public void decode_EvoHome_0x0C_0x0100() {
		decode("0C 114977 067AEC 0100 05 006465FFFF");
		assertEvoHome_0x0100_Message((EvoHome_0x0C_0x0100_0x05_Message) evoHomeMessage, 0x114977, 0x067AEC,
				new byte[] { 0x00, 0x64, 0x65, (byte) 0xFF, (byte) 0xFF });

		decode("0C 131589 067AEC 0100 05 006465FFFF");
		assertEvoHome_0x0100_Message((EvoHome_0x0C_0x0100_0x05_Message) evoHomeMessage, 0x131589, 0x067AEC,
				new byte[] { 0x00, 0x64, 0x65, (byte)0xFF, (byte)0xFF});
	}

	@Test
	public void decode_EvoHome_0x0C_0x1F09() {
		decode("0C 114977 067AEC 1F09 01 00");
		assertEvoHome_0x1F09_Message((EvoHome_0x0C_0x1F09_0x01_REQUEST_0C_1F09_Message) evoHomeMessage, 0x114977, 0x067AEC, (byte) 0x00);
	}

	@Test
	public void decode_EvoHome_0x3C_0x0100() {
		decode("3C 067AEC 114977 0100 05 006465FFFF");
		assertEvoHome_0x0100_Message((EvoHome_0x3C_0x0100_0x05_Message) evoHomeMessage, 0x067AEC, 0x114977,
				new byte[] { 0x00, 0x64, 0x65, (byte) 0xFF, (byte) 0xFF });

		decode("3C 067AEC 131589 0100 05 006465FFFF");
		assertEvoHome_0x0100_Message((EvoHome_0x3C_0x0100_0x05_Message) evoHomeMessage, 0x067AEC, 0x131589, 
				new byte[] { 0x00, 0x64, 0x65, (byte)0xFF, (byte)0xFF});
	}

	@Test
	public void decode_EvoHome_0x3C_0x0004() {
		decode("3C 067AEC 114977 0004 16 0000576F686E7A696D6D657200000000000000000000");
		assertEvoHome_0x0004_Message((EvoHome_0x3C_0x0004_0x16_Message) evoHomeMessage, 0x067AEC, 0x114977,
				new byte[] {0x00, 0x00, 0x57, 0x6F, 0x68, 0x6E, 0x7A, 0x69, 0x6D, 0x6D, 0x65, 0x72, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
	}

	@Test
	public void decode_EvoHome_0x3C_0x1F09() {
		decode("3C 067AEC 114977 1F09 03 000019");
		assertEvoHome_0x1F09_Message((EvoHome_0x3C_0x1F09_0x03_RESPONSE_3C_1F09_Message) evoHomeMessage, 0x067AEC, 0x114977, 0x000019);
	}

	@Test
	public void decode_EvoHome_0x18_0x0004() {
		decode("18 067AEC 067AEC 0004 16 0000576F686E7A696D6D657231000000000000000000");
		assertEvoHome_0x0004_Message((EvoHome_0x18_0x0004_0x16_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
				new byte[] { 0x00, 0x00, 0x57, 0x6F, 0x68, 0x6E, 0x7A, 0x69, 0x6D, 0x6D, 0x65, 0x72, 0x31, 0x00, 0x00,
						0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });
	}

	@Test
	public void decode_EvoHome_0x18_0x2349() {
		
		decode("18 067AEC 067AEC 2349 0D 0009C4 04FFFFFF 00 06 12 07 07E3");
		assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
				25.0f, 0x04FFFFFF, LocalDateTime.of(2019, 07, 18, 06, 00));
		
		decode("18 067AEC 067AEC 2349 0D 0009C4 04FFFFFF 0A 05 12 07 07E3");
				assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
						25.0f, 0x04FFFFFF, LocalDateTime.of(2019, 07, 18, 5, 10));
		decode("18 067AEC 067AEC 2349 0D 0009C4 04FFFFFF 14 13 11 07 07E3");
				assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
						25.0f, 0x04FFFFFF, LocalDateTime.of(2019, 07, 17, 19, 20));

		decode("18 067AEC 067AEC 2349 0D 0009C4 04FFFFFF 0A 0F 11 07 07E3");
				assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
						25.0f, 0x04FFFFFF, LocalDateTime.of(2019, 07, 17, 15, 10));

		decode("18 067AEC 067AEC 2349 0D 000352 04FFFFFF 00 0C 10 07 07E3");
		assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
				8.5f, 0x04FFFFFF, LocalDateTime.of(2019, 07, 16, 12, 00));
		
		
		
		decode("18 067AEC 067AEC 2349 07 0008FC 00FFFFFF");
		assertEvoHome_0x2349_Message((EvoHome_0x18_0x2349_0x07_ZONE_SETPOINT_PERMANENT_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
				23.0f, 0x00FFFFFF);
	}
	
	
	@Test
	public void decode_EvoHome_0x0C_0x0016() {
		decode("0C 067AEC 114977 0016 02 00FF");
		assertEvoHome_0x0016_Message((EvoHome_0x0C_0x0016_0x02_Message) evoHomeMessage, 0x067AEC, 0x114977, 0x00FF);
	}

	
	@Test
	public void decode_EvoHome_0x28_0x0001() {
		decode("28 067AEC 067AEC 0001 05 0000000505");
		assertEvoHome_0x0001_Message((EvoHome_0x28_0x0001_0x05_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
				new byte[] { 0x00, 0x00, 0x00, 0x05, 0x05});
	}


	@Test
	public void decode_EvoHome_0x0C_0x313F() {
		decode("0C 114977 067AEC 313F 01 00");
				assertEvoHome_0x313F_Message((EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message) evoHomeMessage, 0x114977, 0x067AEC, (byte)0x00);
		decode("0C 1203C3 05B933 313F 01 00");
		assertEvoHome_0x313F_Message((EvoHome_0x0C_0x313F_0x01_REQUEST_0C_313F_Message) evoHomeMessage, 0x1203C3, 0x05B933, (byte)0x00);
	}

	@Test
	public void decode_EvoHome_0x3C_0x313F_0x09() {
		decode("3C 067AEC 114977 313F 09 00FC941E46110707E3");
		assertEvoHome_0x313F_Message((EvoHome_0x3C_0x313F_0x09_RESPONSE_3C_313F_Message) evoHomeMessage, 0x067AEC, 0x114977,
				new byte[] { 0x00, (byte)0xFC, (byte)0x94, 0x1E, 0x46, 0x11, 0x07, 0x07, (byte)0xE3});
	}

	@Test
	public void decode_EvoHome_0x0C_0x0100_0x05() {
		decode("18 067AEC 067AEC 0005 04 00000300");
		assertEvoHome_0x0005_Message((EvoHome_0x18_0x0005_0x04_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
				new byte[] { 0x00, 0x00, 0x03, 0x00});
	}

	@Test
	public void decode_EvoHome_0x1C_0x1CF9_0x06() {
		decode("1C 131589 FFFFFE 1FC9 06 0030C9 131589");
		assertEvoHome_0x1FC9_Message((EvoHome_0x1C_0x1FC9_0x06_Message) evoHomeMessage, 0x131589, 0xFFFFFE,
				0x0030C9, 0x131589);

		decode("1C 067AEC 131589 1FC9 06 00FFFF 067AEC");
		assertEvoHome_0x1FC9_Message((EvoHome_0x1C_0x1FC9_0x06_Message) evoHomeMessage, 0x067AEC, 0x131589,
				0x00FFFF, 0x067AEC);
	}

	@Test
	public void decode_EvoHome_0x18_0x1CF9_0x12() {
		decode("18 067AEC 067AEC 1FC9 12 012309 067AEC 0130C9 067AEC 011FC9 067AEC");
		assertEvoHome_0x1FC9_Message((EvoHome_0x18_0x1FC9_0x12_Message) evoHomeMessage, 0x067AEC, 0x067AEC,
				0x012309, 0x067AEC, 0x0130C9, 0x067AEC, 0x011FC9, 0x067AEC);
	}
	
	@Test
	public void decode_EvoHome_0x2C_0x1CF9_0x06() {
		decode("2C 131589 067AEC 1FC9 06 0030C9 131589");
		assertEvoHome_0x1FC9_Message((EvoHome_0x2C_0x1FC9_0x06_Message) evoHomeMessage, 0x131589, 0x067AEC,
				0x0030C9, 0x131589);
	}	
	
	@Test
	public void decode_EvoHome_0x28_0x1F09_0x03() {
		decode("28 067AEC 067AEC 1F09 03 F80429");
		assertEvoHome_0x1F09_Message((EvoHome_0x28_0x1F09_0x03_Message) evoHomeMessage, 0x067AEC, 0x067AEC, 0xF80429);
	}

	@Test
	public void decode_EvoHome_0x18_0x042F_0x08() {
		decode("18 895E5D 895E5D 042F 08 000000000B000B1E");
		assertEvoHome_0x042F_Message((EvoHome_0x18_0x042F_0x08_Message) evoHomeMessage, 0x895E5D, 0x895E5D,
				new byte[] { 0x00, 0x00, 0x00, 0x00, 0x0B, 0x00, 0x0B, 0x1E});
		
		decode("18 895E5D 895E5D 042F 08 000000000C000C1C"); 
						assertEvoHome_0x042F_Message((EvoHome_0x18_0x042F_0x08_Message) evoHomeMessage, 0x895E5D, 0x895E5D, 
								new byte[] { 0x00, 0x00, 0x00, 0x00, 0x0C, 0x00, 0x0C, 0x1C});
	}

	@Test
	public void decode_EvoHome_0x18_0x000E_0x03() {
		decode("18 895E5D 895E5D 000E 03 000014");
		assertEvoHome_0x000E_Message((EvoHome_0x18_0x000E_0x03_Message) evoHomeMessage, 0x895E5D, 0x895E5D, 0x000014);
	}

	@Test
	public void decode_EvoHome_0x0C_0x000A_0x01() {
		decode("0C 895E5D 067AEC 000A 01 01");
		assertEvoHome_0x000A_Message((EvoHome_0x0C_0x000A_0x01_Message) evoHomeMessage, 0x895E5D, 0x067AEC, (byte)0x01);
	}
	
	@Test
	public void decode_EvoHome_0x0C_0x2309_0x01() {
		decode("0C 895E5D 067AEC 2309 01 01");
		assertEvoHome_0x2309_Message((EvoHome_0x0C_0x2309_0x01_Message) evoHomeMessage, 0x895E5D, 0x067AEC, (byte)0x01);
	}
	
	//TODO Thermostat
	/*
	
	1C 895E5D FFFFFE 10E0 26 000001C8380F0100F1FF140B07E2030507E15438375246323032350000000000000000000000
	
	18 895E5D 895E5D 3120 07 0070B0000000FF
	
	18 895E5D 895E5D 1FC9 18 002309 895E5D 0030C9 895E5D 000008 895E5D 00 1FC9 895E5D
	
	18 895E5D 895E5D 1FC9 12 000008 895E5D 003150 895E5D 00 1FC9 895E5D
	
	18 895E5D 895E5D 1FC9 0C 002309 895E5D 00 30C9 895E5D
	
	18 895E5D 895E5D 0005 0C 000A0000000F000000100000
	
	18 895E5D 895E5D 000C 12 000A7FFFFFFF000F7FFFFFFF00107FFFFFFF
	*/
	
}
