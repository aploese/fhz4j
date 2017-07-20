package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtTimeMessage;
import java.time.LocalTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class FhtTimeMessageTest implements ParserListener<FhtMessage> {

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
    public void decodeThursdayTimes() {
        decode("030220692A");
        FhtTimeMessageTest.assertTimeMessage(fhtMessage, 302, FhtProperty.THU_FROM_1, true, true, LocalTime.of(7, 0));
        decode("030221697E");
        FhtTimeMessageTest.assertTimeMessage(fhtMessage, 302, FhtProperty.THU_TO_1, true, true, LocalTime.of(21, 0));
        decode("0302226990");
        FhtTimeMessageTest.assertTimeMessage(fhtMessage, 302, FhtProperty.THU_FROM_2, true, true, null);
        decode("0302236990");
        FhtTimeMessageTest.assertTimeMessage(fhtMessage, 302, FhtProperty.THU_TO_2, true, true, null);
    }

    @Test
    public void decodePartyEnd() {
        decode("03023F696F");
        decode("0302406906");
        decode("03023E6903");
        FhtTimeMessageTest.assertTimeMessage(fhtMessage, 302, FhtProperty.PARTY_END_TIME, true, true, LocalTime.of(18, 30));
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

    public static void assertTimeMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty, boolean dataRegister, boolean fromFht_8B, LocalTime time) {
        assertNotNull(fhtMessage);
        final FhtTimeMessage msg = (FhtTimeMessage) fhtMessage;
        assertEquals("housecode", (short) housecode, msg.housecode);
        assertEquals("command", fhtProperty, msg.command);
        assertEquals("fromFht_8B", fromFht_8B, msg.fromFht_8B);
        assertEquals("dataRegister", dataRegister, msg.dataRegister);
        assertEquals("time", time, msg.time);
    }

    @Override
    public void successPartialAssembled(FhtMessage fhzMessage) {
        this.fhtMessage = fhzMessage;
    }

}
