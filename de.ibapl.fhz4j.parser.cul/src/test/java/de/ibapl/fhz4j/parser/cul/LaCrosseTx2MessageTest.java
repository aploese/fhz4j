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
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Property;

/**
 *
 * @author Arne Plöse
 */
public class LaCrosseTx2MessageTest implements ParserListener<LaCrosseTx2Message> {

	private LaCrosseTx2Parser parser = new LaCrosseTx2Parser(this);
	private LaCrosseTx2Message laCrosseTx2Message;
	private Throwable error;

	private void decode(String s) {
		laCrosseTx2Message = null;
		error = null;
		parser.init();
		for (char c : s.toCharArray()) {
			parser.parse(c);
		}
	}

	@Override
	public void success(LaCrosseTx2Message laCrosseTx2Message) {
		this.laCrosseTx2Message = laCrosseTx2Message;
	}

	@Override
	public void fail(Throwable t) {
		error = t;
	}

	@Override
	public void successPartial(LaCrosseTx2Message laCrosseTx2Message) {
		throw new RuntimeException("No partial message expected.");
	}

	@Override
	public void successPartialAssembled(LaCrosseTx2Message laCrosseTx2Message) {
		throw new RuntimeException("No partial message expected.");
	}

	public static void assertLaCrosseTx2Message(LaCrosseTx2Message laCrosseTx2Msg, int address,
			LaCrosseTx2Property laCrosseTx2Property, float value) {
		assertNotNull(laCrosseTx2Msg);
		assertEquals("address", (short) address, laCrosseTx2Msg.address);
		assertEquals("laCrosseTx2Property", laCrosseTx2Property, laCrosseTx2Msg.laCrosseTx2Property);
		assertEquals("laCrosseTx2Property", value, laCrosseTx2Msg.value, Float.MIN_NORMAL);
	}

	@Test
	public void decode_LA_CROSSE_TX2() {
		decode("A00E73173D");
		assertLaCrosseTx2Message(laCrosseTx2Message, 7, LaCrosseTx2Property.TEMP, 23.1f);
		decode("AECC60060C");
		assertLaCrosseTx2Message(laCrosseTx2Message, 198, LaCrosseTx2Property.HUMIDITY, 60.0f);
		decode("A00AA002EA");
		assertLaCrosseTx2Message(laCrosseTx2Message, 5, LaCrosseTx2Property.TEMP, 50.0f);
		decode("A00A678E9E");
		assertLaCrosseTx2Message(laCrosseTx2Message, 5, LaCrosseTx2Property.TEMP, 17.800001f);
	}

}
