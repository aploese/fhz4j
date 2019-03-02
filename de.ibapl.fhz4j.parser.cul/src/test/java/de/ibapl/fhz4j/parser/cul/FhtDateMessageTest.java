/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2018 Arne Plöse
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
package de.ibapl.fhz4j.parser.cul;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.FhtDateMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;

/**
 *
 * @author Arne Plöse
 */
public class FhtDateMessageTest implements ParserListener<FhtMessage> {

	private FhtParser parser = new FhtParser(this);
	private FhtMessage partialFhtMessage;
	private FhtMessage fhtMessage;
	private Throwable error;

	private void decode(String s) {
		fhtMessage = null;
		partialFhtMessage = null;
		error = null;
		parser.init();
		for (char c : s.toCharArray()) {
			parser.parse(c);
		}
	}

	@Test
	public void testHolidayEnd() {
		decode("03023F690D");
		decode("0302406907");
		decode("03023E6902");
		assertDateMessage(fhtMessage, (short) 302, FhtProperty.HOLIDAY_END_DATE, true, true, 7, 13);
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
		error = t;
	}

	public static void assertDateMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty,
			boolean dataRegister, boolean fromFht_8B, int month, int day) {
		assertNotNull(fhtMessage);
		final FhtDateMessage msg = (FhtDateMessage) fhtMessage;
		assertEquals("housecode", (short) housecode, msg.housecode);
		assertEquals("command", fhtProperty, msg.command);
		assertEquals("fromFht_8B", fromFht_8B, msg.fromFht_8B);
		assertEquals("dataRegister", dataRegister, msg.dataRegister);
		assertEquals("month", month, msg.month);
		assertEquals("day", day, msg.day);
	}

	@Override
	public void successPartialAssembled(FhtMessage fhzMessage) {
		this.fhtMessage = fhzMessage;
	}

}
