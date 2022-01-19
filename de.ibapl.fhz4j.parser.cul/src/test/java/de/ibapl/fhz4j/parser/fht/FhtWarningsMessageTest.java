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
package de.ibapl.fhz4j.parser.fht;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.cul.DataSource;
import de.ibapl.fhz4j.protocol.fht.AbstractFhtMessage;
import de.ibapl.fhz4j.protocol.fht.Fht80bWarning;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtWarningMessage;
import java.util.EnumSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class FhtWarningsMessageTest implements ParserListener<AbstractFhtMessage> {

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
    public void decode_NoWarnings() {
        decode("0302446900");
        FhtWarningsMessageTest.assertWarningsMessage(fhtMessage, 302, true, true, EnumSet.noneOf(Fht80bWarning.class));
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

    public static void assertWarningsMessage(FhtMessage fhtMessage, int housecode, boolean dataRegister,
            boolean fromFht_8B, Set<Fht80bWarning> warnings) {
        assertNotNull(fhtMessage);
        final FhtWarningMessage msg = (FhtWarningMessage) fhtMessage;
        assertEquals((short) housecode, msg.housecode, "housecode");
        assertEquals(FhtProperty.WARNINGS, msg.command, "command");
        assertEquals(fromFht_8B, msg.fromFht_8B, "fromFht_8B");
        assertEquals(dataRegister, msg.dataRegister, "dataRegister");
        assertEquals(warnings, msg.warnings, "warnings");
    }

    @Override
    public void successPartialAssembled(AbstractFhtMessage fhzMessage) {
        this.fhtMessage = (FhtMessage) fhzMessage;
    }

}
