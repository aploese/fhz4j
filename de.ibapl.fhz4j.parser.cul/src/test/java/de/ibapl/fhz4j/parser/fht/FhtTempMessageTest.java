/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2022-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.parser.fht;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.cul.DataSource;
import de.ibapl.fhz4j.protocol.fht.AbstractFhtMessage;
import de.ibapl.fhz4j.protocol.fht.Fht80bRawMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtTempMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class FhtTempMessageTest implements ParserListener<AbstractFhtMessage> {

    private FhtParser parser = new FhtParser(this);
    private FhtMessage partialFhtMessage;
    private FhtMessage fhtMessage;

    private void decode(String s) {
        fhtMessage = null;
        partialFhtMessage = null;
        parser.init();
        new DataSource(s).iterate(parser);
    }

    @Test
    public void testDesiredTemp() {
        decode("0302416934");
        assertTempMessage(fhtMessage, (short) 302, FhtProperty.DESIRED_TEMP, true, true, 26.0f);
    }

    @Test
    public void testTemp() {
        decode("0302426901");
        assertRawMessage(partialFhtMessage, (short) 302, FhtProperty.MEASURED_LOW, true, true, (byte) 1);
        decode("0302436901");
        assertRawMessage(partialFhtMessage, (short) 302, FhtProperty.MEASURED_HIGH, true, true, (byte) 1);
        assertTempMessage(fhtMessage, (short) 302, FhtProperty.MEASURED_TEMP, true, true, 25.7f);
    }

    @Test
    public void decode_FHT_25_1_Degree_Centigrade() {
        decode("61344269FB");
        assertRawMessage(partialFhtMessage, 9752, FhtProperty.MEASURED_LOW, true, true, (byte) 251);
    }

    @Override
    public void success(AbstractFhtMessage fhzMessage) {
        this.fhtMessage = (FhtMessage) fhzMessage;
    }

    @Override
    public void successPartial(AbstractFhtMessage fhzMessage) {
        this.partialFhtMessage = (FhtMessage) fhzMessage;
    }

    @Override
    public void fail(Throwable t) {
        throw new RuntimeException(t);
    }

    public static void assertTempMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty,
            boolean dataRegister, boolean fromFht_8B, float temp) {
        assertNotNull(fhtMessage);
        final FhtTempMessage msg = (FhtTempMessage) fhtMessage;
        assertEquals((short) housecode, msg.housecode, "housecode");
        assertEquals(fhtProperty, msg.command, "command");
        assertEquals(fromFht_8B, msg.fromFht_8B, "fromFht_8B");
        assertEquals(dataRegister, msg.dataRegister, "dataRegister");
        assertEquals(temp, msg.temp, Float.MIN_NORMAL, "temp");
    }

    public static void assertRawMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty,
            boolean dataRegister, boolean fromFht_8B, byte value) {
        assertNotNull(fhtMessage);
        final Fht80bRawMessage msg = (Fht80bRawMessage) fhtMessage;
        assertEquals((short) housecode, msg.housecode, "housecode");
        assertEquals(fhtProperty, msg.command, "command");
        assertEquals(fromFht_8B, msg.fromFht_8B, "fromFht_8B");
        assertEquals(dataRegister, msg.dataRegister, "dataRegister");
        assertEquals(value, msg.getSignedValue(), "value");
    }

    @Override
    public void successPartialAssembled(AbstractFhtMessage fhzMessage) {
        this.fhtMessage = (FhtMessage) fhzMessage;
    }

}
