package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.Fht80bRawMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtTempMessage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class FhtTempMessageTest implements ParserListener<FhtMessage> {
    
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
    public void testDesiredTemp() {
        decode("0302416934");
        assertTempMessage(fhtMessage, (short)302, FhtProperty.DESIRED_TEMP, true, true, 26.0f);
    }

    @Test
    public void testTemp() {
        decode("0302426901");
        assertRawMessage(partialFhtMessage, (short)302, FhtProperty.MEASURED_LOW, true, true, 1);
        decode("0302436901");
        assertRawMessage(partialFhtMessage, (short)302, FhtProperty.MEASURED_HIGH, true, true, 1);
        assertTempMessage(fhtMessage, (short)302, FhtProperty.MEASURED_TEMP, true, true, 25.7f);
    }
    
    @Test
    public void decode_FHT_25_1_Degree_Centigrade() {
        decode("61344269FB");
        assertRawMessage(partialFhtMessage, 9752, FhtProperty.MEASURED_LOW, true, true, 251);
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

    public static void assertTempMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty, boolean dataRegister, boolean fromFht_8B, float temp) {
        assertNotNull(fhtMessage);
        final FhtTempMessage msg = (FhtTempMessage)fhtMessage;
        assertEquals("housecode", (short)housecode, msg.housecode);
        assertEquals("command", fhtProperty, msg.command);
        assertEquals("fromFht_8B", fromFht_8B, msg.fromFht_8B);
        assertEquals("dataRegister", dataRegister, msg.dataRegister);
        assertEquals("temp", temp, msg.temp, Float.MIN_NORMAL);
    }

    public static void assertRawMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty, boolean dataRegister, boolean fromFht_8B, int data) {
        assertNotNull(fhtMessage);
        final Fht80bRawMessage msg = (Fht80bRawMessage)fhtMessage;
        assertEquals("housecode", (short)housecode, msg.housecode);
        assertEquals("command", fhtProperty, msg.command);
        assertEquals("fromFht_8B", fromFht_8B, msg.fromFht_8B);
        assertEquals("dataRegister", dataRegister, msg.dataRegister);
        assertEquals("data", data, msg.data);
    }

    @Override
    public void successPartialAssembled(FhtMessage fhzMessage) {
        this.fhtMessage = fhzMessage;
    }

}
