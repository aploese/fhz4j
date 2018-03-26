package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.Fht80bMode;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtModeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import java.time.LocalTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class FhtModeMessageTest implements ParserListener<FhtMessage> {

    private FhtParser parser = new FhtParser(this);
    private FhtMessage partialFhtMessage;
    private FhtMessage assembledFhtMessage;
    private FhtMessage fhtMessage;
    private Throwable error;
    
        private void decode(String s) {
        fhtMessage = null;
        partialFhtMessage = null;
        assembledFhtMessage = null;
        error = null;
        parser.init();
        for (char c : s.toCharArray()) {
            parser.parse(c);
        }
    }

    
        
    @Test
    public void decode_AutoMatik() {
        decode("03023E6900");
        FhtModeMessageTest.assertModeMessage(fhtMessage, 302, true, true, Fht80bMode.AUTO);
    }

    @Test
    public void decode_Party() {
        decode("0302416922");
        FhtTempMessageTest.assertTempMessage(fhtMessage, 302, FhtProperty.DESIRED_TEMP, true, true, 17.0f);
        decode("03023F698D");
        FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.HOLIDAY_1, true, true, 0x8D);
        decode("0302406913");
        FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.HOLIDAY_2, true, true, 0x13);
        decode("03023E6903");
        FhtTimeMessageTest.assertTimeMessage(assembledFhtMessage, 302, FhtProperty.PARTY_END_TIME, true, true, LocalTime.of(23, 30));
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
    public void successPartialAssembled(FhtMessage fhzMessage) {
        this.assembledFhtMessage = fhzMessage;
    }

    @Override
    public void fail(Throwable t) {
        error = t;
    }

    public static void assertModeMessage(FhtMessage fhtMessage, int housecode, boolean dataRegister, boolean fromFht_8B, Fht80bMode mode) {
        assertNotNull(fhtMessage);
        final FhtModeMessage msg = (FhtModeMessage)fhtMessage;
        assertEquals("housecode", (short)housecode, msg.housecode);
        assertEquals("command", FhtProperty.MODE, msg.command);
        assertEquals("fromFht_8B", fromFht_8B, msg.fromFht_8B);
        assertEquals("dataRegister", dataRegister, msg.dataRegister);
        assertEquals("mode", mode, msg.mode);
    }
 
}
