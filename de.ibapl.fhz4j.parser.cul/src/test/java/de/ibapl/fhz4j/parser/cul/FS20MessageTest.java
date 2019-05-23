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

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fs20.FS20CommandValue;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class FS20MessageTest implements ParserListener<FS20Message> {

	private FS20Parser parser = new FS20Parser(this);
	private FS20Message fS20Message;
	private Throwable error;

	private void decode(String s) {
		fS20Message = null;
		error = null;
		parser.init();
		for (char c : s.toCharArray()) {
			parser.parse(c);
		}
	}

	@Override
	public void success(FS20Message fS20Message) {
		this.fS20Message = fS20Message;
	}

	@Override
	public void fail(Throwable t) {
		error = t;
	}

	@Override
	public void successPartial(FS20Message fhzMessage) {
		throw new RuntimeException("No partial message expected.");
	}

	@Override
	public void successPartialAssembled(FS20Message fhzMessage) {
		throw new RuntimeException("No partial message expected.");
	}

	public static void assertFs20Message(FS20Message fs20Msg, int housecode, int offset, FS20CommandValue command) {
	 assertNotNull(fs20Msg);
		final FS20Message msg = fs20Msg;
		// TODO Housecode?? assertEquals("housecode", (short)housecode, msg.housecode);
	 assertEquals(offset, msg.offset, "offset");
	 assertEquals(command, msg.command, "command");
	}

	@Test
	public void decode_FS20_1() {
		decode("C04B0100");
	 assertFs20Message(fS20Message, 19275, 1, FS20CommandValue.OFF);
	}

	@Test
	public void decode_FS20_2() {
		decode("C04B0111");
	 assertFs20Message(fS20Message, 19275, 1, FS20CommandValue.ON);
	}

	@Test
	public void decode_FS20_3() {
		decode("C04B0313");
	 assertFs20Message(fS20Message, 19275, 3, FS20CommandValue.DIM_UP);
	}

}
