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
import de.ibapl.fhz4j.protocol.fht.FhtDateMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtProtocolMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class FhtDateMessageTest implements ParserListener<AbstractFhtMessage> {

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
    public void testHolidayEnd() {
        decode("03023F690D");
        decode("0302406907");
        decode("03023E6902");
        assertDateMessage(fhtMessage, (short) 302, FhtProperty.HOLIDAY_END_DATE, true, true, 7, 13);
        assertEquals("{protocol : FHT, housecode : 302, command : HOLIDAY_END_DATE, description : 0x69, fromFht_8b : true, dataRegister : true, day : 13, month : 7}", fhtMessage.toString());
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

    public static void assertDateMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty,
            boolean dataRegister, boolean fromFht_8B, int month, int day) {
        assertNotNull(fhtMessage);
        final FhtDateMessage msg = (FhtDateMessage) fhtMessage;
        assertEquals((short) housecode, msg.housecode, "housecode");
        assertEquals(fhtProperty, msg.command, "command");
        assertEquals(fromFht_8B, msg.fromFht_8B, "fromFht_8B");
        assertEquals(dataRegister, msg.dataRegister, "dataRegister");
        assertEquals(month, msg.month, "month");
        assertEquals(day, msg.day, "day");
    }

    public static void assertProtocolMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty, int description, boolean fromFht_8B, boolean dataRegister, int data) {
        assertNotNull(fhtMessage);
        final FhtProtocolMessage msg = (FhtProtocolMessage) fhtMessage;
        assertEquals((short) housecode, msg.housecode, "housecode");
        assertEquals(fhtProperty, msg.command, "command");
        assertEquals(fromFht_8B, msg.fromFht_8B, "fromFht_8B");
        assertEquals(dataRegister, msg.dataRegister, "dataRegister");
        assertEquals(description, msg.description, "description");
        assertEquals(data, msg.data, "data");
    }

    @Override
    public void successPartialAssembled(AbstractFhtMessage fhzMessage) {
        this.fhtMessage = (FhtMessage) fhzMessage;
    }

}
