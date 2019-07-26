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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.time.Month;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.fht.FhtParser;
import de.ibapl.fhz4j.protocol.fht.FhtDateTimeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class FhtDateTimeMessageTest implements ParserListener<FhtMessage> {

	private FhtParser parser = new FhtParser(this);
	private FhtMessage partialFhtMessage;
	private FhtMessage assembledfhtMessage;
	private FhtMessage fhtMessage;

	private void decode(String s) {
		fhtMessage = null;
		partialFhtMessage = null;
		assembledfhtMessage = null;
		parser.init();
		new DataSource(s).iterate(parser);
	}

	@Test
	public void decodeCurrentTime() {
		decode("0302606911");
		FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.YEAR, true, true, (byte)17);
		decode("0302616907");
		FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.MONTH, true, true, (byte)7);
		decode("0302626914");
		FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.DAY_OF_MONTH, true, true, (byte)20);
		decode("030263690E");
		FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.HOUR, true, true, (byte)14);
		decode("0302646917");
		FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.MINUTE, true, true, (byte)23);
	 assertDateTimeMessage(assembledfhtMessage, 302, FhtProperty.CURRENT_DATE_AND_TIME, true, true,
				LocalDateTime.of(2017, Month.JULY, 20, 14, 23));
	}

	@Test
	public void decodeDateTime_WrongOrder() {
		decode("0401606912");
		decode("0401626911");
		decode("0401616901");
		decode("040163690B");
		decode("0401646930");
	 assertNull(assembledfhtMessage);
	}

	@Test
	public void decodeDate_Incomplete() {
		decode("0401606912");
		decode("0401626911");
		decode("040163690B");
		decode("0401646930");
	 assertNull(assembledfhtMessage);
	}

	@Test
	public void decodeDate_No_Start() {
		decode("0401616901");
		decode("0401626911");
		decode("040163690B");
		decode("0401646930");
	 assertNull(assembledfhtMessage);
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
	public void fail(Throwable t) {
		throw new RuntimeException(t);
	}

	public static void assertDateTimeMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty,
			boolean dataRegister, boolean fromFht_8B, LocalDateTime ts) {
	 assertNotNull(fhtMessage);
		final FhtDateTimeMessage msg = (FhtDateTimeMessage) fhtMessage;
	 assertEquals((short) housecode, msg.housecode, "housecode");
	 assertEquals(fhtProperty, msg.command, "command");
	 assertEquals(fromFht_8B, msg.fromFht_8B, "fromFht_8B");
	 assertEquals(dataRegister, msg.dataRegister, "dataRegister");
	 assertEquals(ts, msg.ts, "ts");
	}

	@Override
	public void successPartialAssembled(FhtMessage fhzMessage) {
		this.assembledfhtMessage = fhzMessage;
	}

}
