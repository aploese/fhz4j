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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;


import java.util.EnumSet;
import java.util.Set;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.hms.Hms100RmMessage;
import de.ibapl.fhz4j.protocol.hms.Hms100TfMessage;
import de.ibapl.fhz4j.protocol.hms.Hms100TfkMessage;
import de.ibapl.fhz4j.protocol.hms.Hms100WdMessage;
import de.ibapl.fhz4j.protocol.hms.HmsDeviceStatus;
import de.ibapl.fhz4j.protocol.hms.HmsMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class HmsMessageTest implements ParserListener<HmsMessage> {

	private HmsParser parser = new HmsParser(this);
	private HmsMessage hmsMessage;
	private Throwable error;

	private void decode(String s) {
		hmsMessage = null;
		error = null;
		parser.init();
		for (char c : s.toCharArray()) {
			parser.parse(c);
		}
	}

	@Override
	public void success(HmsMessage hmsMessage) {
		this.hmsMessage = hmsMessage;
	}

	@Override
	public void fail(Throwable t) {
		error = t;
	}

	@Test
	@Disabled
	public void decode_HMS_100_RM() {
		decode("DC8723000500");
	 assertHmsRmMessage(hmsMessage, 0xDC87, EnumSet.noneOf(HmsDeviceStatus.class), false);
		decode("DC8723030200");
	 assertHmsRmMessage(hmsMessage, 0xDC87, EnumSet.noneOf(HmsDeviceStatus.class), false);
		decode("DC8723000500");
	 assertHmsRmMessage(hmsMessage, 0xDC87, EnumSet.noneOf(HmsDeviceStatus.class), false);

		decode("707D0300FF00");
	 assertHmsRmMessage(hmsMessage, 0x707D, EnumSet.noneOf(HmsDeviceStatus.class), false);
		decode("DC8703030100");
	 assertHmsRmMessage(hmsMessage, 0x707D, EnumSet.of(HmsDeviceStatus.BATT_LOW), false);

		/*
		 * Byte 9+10 könnte irgendeine Sequence Nr. sein ???
		 * 
		 * Ergebnis wäre: Byte 1-4=Device ID Byte 5 Bit 1 = Battery Byte 8 bit 0 =
		 * Status
		 * 
		 * Gruß Klaus
		 */
	}

	public static void assertHmsTfMessage(HmsMessage hmsMsg, int housecode, Set<HmsDeviceStatus> deviceStatus,
			float temp, float humidy) {
	 assertNotNull(hmsMsg);
		final Hms100TfMessage msg = (Hms100TfMessage) hmsMsg;
	 assertArrayEquals(deviceStatus.toArray(), deviceStatus.toArray(), "deviceStatus");
	 assertEquals((short) housecode, msg.housecode, "housecode");
	 assertEquals(temp, msg.temp, Float.MIN_NORMAL, "temp");
	 assertEquals(humidy, msg.humidy, Float.MIN_NORMAL, "humidy");
	}

	public static void assertHmsWdMessage(HmsMessage hmsMsg, int housecode, Set<HmsDeviceStatus> deviceStatus,
			boolean water) {
	 assertNotNull(hmsMsg);
		final Hms100WdMessage msg = (Hms100WdMessage) hmsMsg;
	 assertArrayEquals(deviceStatus.toArray(), deviceStatus.toArray(), "deviceStatus");
	 assertEquals((short) housecode, msg.housecode, "housecode");
	 assertEquals(water, msg.water, "water");
	}

	public static void assertHmsTkfMessage(HmsMessage hmsMsg, int housecode, Set<HmsDeviceStatus> deviceStatus,
			boolean open) {
	 assertNotNull(hmsMsg);
		final Hms100TfkMessage msg = (Hms100TfkMessage) hmsMsg;
	 assertArrayEquals(deviceStatus.toArray(), deviceStatus.toArray(), "deviceStatus");
	 assertEquals((short) housecode, msg.housecode, "housecode");
	 assertEquals(open, msg.open, "open");
	}

	public static void assertHmsRmMessage(HmsMessage hmsMsg, int housecode, Set<HmsDeviceStatus> deviceStatus,
			boolean smoke) {
	 assertNotNull(hmsMsg);
		final Hms100RmMessage msg = (Hms100RmMessage) hmsMsg;
	 assertArrayEquals(deviceStatus.toArray(), deviceStatus.toArray(), "deviceStatus");
	 assertEquals((short) housecode, msg.housecode, "housecode");
	 assertEquals(smoke, msg.smoke, "smoke");
	}

	@Test
	public void decode_HMS_100_TF() {

		decode("775800528272");
	 assertHmsTfMessage(hmsMessage, 0x7758, EnumSet.noneOf(HmsDeviceStatus.class), 25.2f, 72.8f);

		decode("C25C00098262");
	 assertHmsTfMessage(hmsMessage, 0xC25C, EnumSet.noneOf(HmsDeviceStatus.class), 20.9f, 62.8f);

		decode("C25C20128260");
	 assertHmsTfMessage(hmsMessage, 0xC25C, EnumSet.of(HmsDeviceStatus.BATT_LOW), 21.2f, 60.8f);
	}

	@Test
	public void decode_HMS_100_TFK() {
		decode("7AEF04000000");
	 assertHmsTkfMessage(hmsMessage, 0x7AEF, EnumSet.noneOf(HmsDeviceStatus.class), false);
		decode("7AEF04010000");
	 assertHmsTkfMessage(hmsMessage, 0x7AEF, EnumSet.noneOf(HmsDeviceStatus.class), true);
		decode("7AEF24000000");
	 assertHmsTkfMessage(hmsMessage, 0x7AEF, EnumSet.of(HmsDeviceStatus.BATT_LOW), false);
		decode("7AEF24010000");
	 assertHmsTkfMessage(hmsMessage, 0x7AEF, EnumSet.of(HmsDeviceStatus.BATT_LOW), true);
	}

	@Test
	public void decode_HMS_100_WD() {
		decode("78D10200FA00");
	 assertHmsWdMessage(hmsMessage, 0x78D1, EnumSet.noneOf(HmsDeviceStatus.class), false);
		decode("78D102010000");
	 assertHmsWdMessage(hmsMessage, 0x78D1, EnumSet.noneOf(HmsDeviceStatus.class), true);
		decode("78D122000000");
	 assertHmsWdMessage(hmsMessage, 0x78D1, EnumSet.of(HmsDeviceStatus.BATT_LOW), false);
		decode("78D122010000");
	 assertHmsWdMessage(hmsMessage, 0x78D1, EnumSet.of(HmsDeviceStatus.BATT_LOW), true);
	}

	@Override
	public void successPartial(HmsMessage fhzMessage) {
		throw new RuntimeException("No partial message expected.");
	}

	@Override
	public void successPartialAssembled(HmsMessage fhzMessage) {
		throw new RuntimeException("No partial message expected.");
	}

}
