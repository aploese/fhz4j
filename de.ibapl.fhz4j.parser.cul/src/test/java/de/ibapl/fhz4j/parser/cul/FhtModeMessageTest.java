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


import java.time.LocalTime;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.Fht80bMode;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtModeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class FhtModeMessageTest implements ParserListener<FhtMessage> {

	private FhtParser parser = new FhtParser(this);
	private FhtMessage partialFhtMessage;
	private FhtMessage assembledFhtMessage;
	private FhtMessage fhtMessage;
	private Throwable error;

	private void decode(String s) {
		fhtMessage = null;
		partialFhtMessage = null;
		assembledFhtMessage = null;
		error = null;
		parser.init();
		for (char c : s.toCharArray()) {
			parser.parse(c);
		}
	}

	@Test
	public void decode_AutoMatik() {
		decode("03023E6900");
		FhtModeMessageTest.assertModeMessage(fhtMessage, 302, true, true, Fht80bMode.AUTO);
	}

	@Test
	public void decode_Party() {
		decode("0302416922");
		FhtTempMessageTest.assertTempMessage(fhtMessage, 302, FhtProperty.DESIRED_TEMP, true, true, 17.0f);
		decode("03023F698D");
		FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.HOLIDAY_1, true, true, 0x8D);
		decode("0302406913");
		FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.HOLIDAY_2, true, true, 0x13);
		decode("03023E6903");
		FhtTimeMessageTest.assertTimeMessage(assembledFhtMessage, 302, FhtProperty.PARTY_END_TIME, true, true,
				LocalTime.of(23, 30));
	}

	@Override
	public void success(FhtMessage fhzMessage) {
		this.fhtMessage = fhzMessage;
	}

	@Override
	public void successPartial(FhtMessage fhzMessage) {
		this.partialFhtMessage = fhzMessage;
	}

	@Override
	public void successPartialAssembled(FhtMessage fhzMessage) {
		this.assembledFhtMessage = fhzMessage;
	}

	@Override
	public void fail(Throwable t) {
		error = t;
	}

	public static void assertModeMessage(FhtMessage fhtMessage, int housecode, boolean dataRegister, boolean fromFht_8B,
			Fht80bMode mode) {
	 assertNotNull(fhtMessage);
		final FhtModeMessage msg = (FhtModeMessage) fhtMessage;
	 assertEquals((short) housecode, msg.housecode, "housecode");
	 assertEquals(FhtProperty.MODE, msg.command, "command");
	 assertEquals(fromFht_8B, msg.fromFht_8B, "fromFht_8B");
	 assertEquals(dataRegister, msg.dataRegister, "dataRegister");
	 assertEquals(mode, msg.mode, "mode");
	}

}
