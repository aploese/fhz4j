/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2023, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.fht;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.cul.DataSource;
import de.ibapl.fhz4j.parser.fht.FhtParser;
import de.ibapl.fhz4j.protocol.fht.AbstractFhtMessage;
import de.ibapl.fhz4j.protocol.fht.Fht80TfMessage;
import de.ibapl.fhz4j.protocol.fht.Fht80TfValue;
import de.ibapl.fhz4j.writer.fht.FhtEncoder;
import de.ibapl.fhz4j.writer.fht.FhtWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class Fht80TfMessageTest implements ParserListener<AbstractFhtMessage> {

    class FhtTestWriter implements FhtWriter {

        String written = "";

        StringBuilder sb = new StringBuilder();

        @Override
        public void startFhtMessage() throws IOException {
            //no-op
        }

        @Override
        public void finishFhtMessage() throws IOException {
            //no-op
        }

        @Override
        public void doWrite() throws IOException {
            written += sb.toString();
            sb.setLength(0);
        }

        @Override
        public void putByte(byte value) throws IOException {
            sb.append(String.format("%02X", value));
        }

        @Override
        public void putShort(short value) throws IOException {
            sb.append(String.format("%04X", value));
        }

        @Override
        public void putInt(int value) throws IOException {
            sb.append(String.format("%08X", value));
        }

        @Override
        public void close() throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    private FhtParser parser = new FhtParser(this);
    private Fht80TfMessage fht80TfMessage;
    private FhtTestWriter fhtWriter = new FhtTestWriter();
    private FhtEncoder encoder = new FhtEncoder(fhtWriter);

    private void decode(String s) {
        fht80TfMessage = null;
        parser.init();
        new DataSource(s).iterate(parser);
    }

    @Test
    public void testDecodeMsg() {

        decode("AABBCC1F");
        assertTfMessage(fht80TfMessage, 11189196, true, Fht80TfValue.FINISH);
        // THHHHABCC
        // CC Command (for FHT80TF: 0x0C - Sync, 0x0F - Finish, 0x01 - open, 0x02 - closed)
        // AB Addressbyte
        decode("3B753101"); //FD");
        assertTfMessage(fht80TfMessage, 3896625, false, Fht80TfValue.WINDOW_INTERNAL_OPEN);
        decode("3B753102"); //FD");
        assertTfMessage(fht80TfMessage, 3896625, false, Fht80TfValue.WINDOW_INTERNAL_CLOSED);
        decode("3B753181"); //FE");
        assertTfMessage(fht80TfMessage, 3896625, false, Fht80TfValue.WINDOW_EXTERNAL_OPEN);
        decode("3B753182"); //FE");
        assertTfMessage(fht80TfMessage, 3896625, false, Fht80TfValue.WINDOW_EXTERNAL_CLOSED);
        decode("3B75310C"); //F4");
        assertTfMessage(fht80TfMessage, 3896625, false, Fht80TfValue.SYNC);
        decode("3B75310F"); //F4");
        assertTfMessage(fht80TfMessage, 3896625, false, Fht80TfValue.FINISH);
        decode("3B75311F"); //F4");
        assertTfMessage(fht80TfMessage, 3896625, true, Fht80TfValue.FINISH);
        decode("AABBCC1F");
        assertTfMessage(fht80TfMessage, 11189196, true, Fht80TfValue.FINISH);
    }

    @Test
    public void testEncodeMsg() throws Exception {

        fhtWriter.written = "";
        encoder.writeFht80Tf(3896625, Fht80TfValue.WINDOW_INTERNAL_OPEN, false);
        assertEquals("3B753101", fhtWriter.written);

        fhtWriter.written = "";
        encoder.writeFht80Tf(3896625, Fht80TfValue.WINDOW_INTERNAL_CLOSED, false);
        assertEquals("3B753102", fhtWriter.written);

        fhtWriter.written = "";
        encoder.writeFht80Tf(3896625, Fht80TfValue.WINDOW_EXTERNAL_OPEN, false);
        assertEquals("3B753181", fhtWriter.written);

        fhtWriter.written = "";
        encoder.writeFht80Tf(3896625, Fht80TfValue.WINDOW_EXTERNAL_CLOSED, false);
        assertEquals("3B753182", fhtWriter.written);

        fhtWriter.written = "";
        encoder.writeFht80Tf(3896625, Fht80TfValue.SYNC, false);
        assertEquals("3B75310C", fhtWriter.written);

        fhtWriter.written = "";
        encoder.writeFht80Tf(3896625, Fht80TfValue.FINISH, false);
        assertEquals("3B75310F", fhtWriter.written);

        fhtWriter.written = "";
        encoder.writeFht80Tf(3896625, Fht80TfValue.FINISH, true);
        assertEquals("3B75311F", fhtWriter.written);

        fhtWriter.written = "";
        encoder.writeFht80Tf(11189196, Fht80TfValue.FINISH, true);
        assertEquals("AABBCC1F", fhtWriter.written);
    }

    @Override
    public void success(AbstractFhtMessage fht80TfMessage) {
        this.fht80TfMessage = (Fht80TfMessage) fht80TfMessage;
    }

    @Override
    public void fail(Throwable t) {
        throw new RuntimeException(t);
    }

    public static void assertTfMessage(Fht80TfMessage fht80TfMessage, int address, boolean lowBattery, Fht80TfValue value) {
        assertNotNull(fht80TfMessage);
        assertEquals(address, fht80TfMessage.address, "address");
        assertEquals(lowBattery, fht80TfMessage.lowBattery, "lowBattery");
        assertEquals(value, fht80TfMessage.value, "value");
    }

    @Override
    public void successPartialAssembled(AbstractFhtMessage fhtMessage) {
        throw new RuntimeException("Therer must be no partial message!");
    }

    @Override
    public void successPartial(AbstractFhtMessage fhzMessage) {
        throw new RuntimeException("Therer must be no partial message!");
    }

}
