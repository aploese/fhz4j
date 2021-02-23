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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.lacrosse.tx2l.LaCrosseTx2Parser;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Property;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class LaCrosseTx2MessageTest implements ParserListener<LaCrosseTx2Message> {

    private LaCrosseTx2Parser parser = new LaCrosseTx2Parser(this);
    private LaCrosseTx2Message laCrosseTx2Message;

    private void decode(String s) {
        laCrosseTx2Message = null;
        parser.init();
        new DataSource(s).iterate(parser);
    }

    @Override
    public void success(LaCrosseTx2Message laCrosseTx2Message) {
        this.laCrosseTx2Message = laCrosseTx2Message;
    }

    @Override
    public void fail(Throwable t) {
        throw new RuntimeException(t);
    }

    @Override
    public void successPartial(LaCrosseTx2Message laCrosseTx2Message) {
        throw new RuntimeException("No partial message expected.");
    }

    @Override
    public void successPartialAssembled(LaCrosseTx2Message laCrosseTx2Message) {
        throw new RuntimeException("No partial message expected.");
    }

    public static void assertLaCrosseTx2Message(LaCrosseTx2Message laCrosseTx2Msg, byte address,
            LaCrosseTx2Property laCrosseTx2Property, float value) {
        assertNotNull(laCrosseTx2Msg);
        assertEquals((short) address, laCrosseTx2Msg.address, "address");
        assertEquals(laCrosseTx2Property, laCrosseTx2Msg.laCrosseTx2Property, "laCrosseTx2Property");
        assertEquals(value, laCrosseTx2Msg.value, Float.MIN_NORMAL, "laCrosseTx2Property");
    }

    @Test
    public void decode_LA_CROSSE_TX2() {

        decode("A0 0E 731 73 D");
        assertLaCrosseTx2Message(laCrosseTx2Message, (byte) 0x07, LaCrosseTx2Property.TEMP, 23.1f);

        decode("AE CC 600 60 C");
        assertLaCrosseTx2Message(laCrosseTx2Message, (byte) 0xC6, LaCrosseTx2Property.HUMIDITY, 60.0f);

        decode("A0 44 723 72 7");
        assertLaCrosseTx2Message(laCrosseTx2Message, (byte) 0x42, LaCrosseTx2Property.TEMP, 22.300001f);

        decode("AE 0F 520 52 5");
        assertLaCrosseTx2Message(laCrosseTx2Message, (byte) 0x07, LaCrosseTx2Property.HUMIDITY, 52.0f);
    }

}
