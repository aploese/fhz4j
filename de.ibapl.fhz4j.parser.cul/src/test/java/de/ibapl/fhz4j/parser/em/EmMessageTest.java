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
package de.ibapl.fhz4j.parser.em;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.cul.DataSource;
import de.ibapl.fhz4j.protocol.em.EmDeviceType;
import de.ibapl.fhz4j.protocol.em.EmMessage;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class EmMessageTest implements ParserListener<EmMessage> {

    private EmParser parser = new EmParser(this);
    private EmMessage emMessage;

    private void decode(String s) {
        emMessage = null;
        parser.init();
        new DataSource(s).iterate(parser);
    }

    @Override
    public void success(EmMessage emMessage) {
        this.emMessage = emMessage;
    }

    @Override
    public void fail(Throwable t) {
        throw new RuntimeException(t);
    }

    @Override
    public void successPartial(EmMessage emMessage) {
        throw new RuntimeException("No partial message expected.");
    }

    @Override
    public void successPartialAssembled(EmMessage emMessage) {
        throw new RuntimeException("No partial message expected.");
    }

    public static void assertEm1000Message(EmMessage emMsg, EmDeviceType emDeviceType, int address, short counter,
            int valueCummulated, int value5Min, int value5MinPeak) {
        assertNotNull(emMsg);
        assertEquals((short) address, emMsg.address, "address");
        assertEquals(emDeviceType, emMsg.emDeviceType, "emDeviceType");
        assertEquals(counter, emMsg.counter, "counter");
        assertEquals(valueCummulated, emMsg.valueCummulated, "valueCummulated");
        assertEquals(value5Min, emMsg.value5Min, "value5Min");
        assertEquals(value5MinPeak, emMsg.value5MinPeak, "value5MinPeak");
    }

    @Test
    public void decode_EM_1() {
        decode("020571241000000000");
        assertEm1000Message(emMessage, EmDeviceType.EM_1000_EM, 5, (short) 113, 4132, 0, 0);
    }

    @Test
    public void decode_EM_2() {
        decode("0205ADC91008000B00");
        assertEm1000Message(emMessage, EmDeviceType.EM_1000_EM, 5, (short) 173, 4297, 8, 11);
    }

    @Test
    public void decode_EM_3() {
        decode("010201040004000F00");
        assertEm1000Message(emMessage, EmDeviceType.EM_1000_S, 2, (short) 1, 4, 4, 15);
    }

    @Test
    public void decode_EM_4() {
        decode("010229ED0003005200");
        assertEm1000Message(emMessage, EmDeviceType.EM_1000_S, 2, (short) 41, 237, 3, 82);
    }

    @Test
    public void decode_EM_5() {
        decode("010201040004000F0047");
        assertEm1000Message(emMessage, EmDeviceType.EM_1000_S, 2, (short) 1, 4, 4, 15);
    }

}
