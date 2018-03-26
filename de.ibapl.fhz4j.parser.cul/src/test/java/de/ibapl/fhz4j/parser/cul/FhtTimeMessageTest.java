package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtTimeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtTimesMessage;

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
    private FhtMessage assembledfhtMessage;
    private FhtMessage fhtMessage;
    private Throwable error;

    private void decode(String s) {
        fhtMessage = null;
        partialFhtMessage = null;
        assembledfhtMessage = null;
        error = null;
        parser.init();
        for (char c : s.toCharArray()) {
            parser.parse(c);
        }
    }

    @Test
    public void decodeThursdayTimes() {
        decode("030220692A");
        decode("030221697E");
        decode("0302226990");
        decode("0302236990");
        FhtTimeMessageTest.assertTimesMessage(assembledfhtMessage, 302, FhtProperty.THURSDAY_TIMES, true, true, LocalTime.of(7, 0), LocalTime.of(21, 0), null, null);
    }

    @Test
    public void decodePartyEnd() {
        decode("03023F696F");
        decode("0302406906");
        decode("03023E6903");
        FhtTimeMessageTest.assertTimeMessage(assembledfhtMessage, 302, FhtProperty.PARTY_END_TIME, true, true, LocalTime.of(18, 30));
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

    public static void assertTimesMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty, boolean dataRegister, boolean fromFht_8B, LocalTime timeFrom1, LocalTime timeTo1, LocalTime timeFrom2, LocalTime timeTo2) {
        assertNotNull(fhtMessage);
        final FhtTimesMessage msg = (FhtTimesMessage) fhtMessage;
        assertEquals("housecode", (short) housecode, msg.housecode);
        assertEquals("command", fhtProperty, msg.command);
        assertEquals("fromFht_8B", fromFht_8B, msg.fromFht_8B);
        assertEquals("dataRegister", dataRegister, msg.dataRegister);
        assertEquals("timeFrom1", timeFrom1, msg.timeFrom1);
        assertEquals("timeTo1", timeTo1, msg.timeTo1);
        assertEquals("timeFrom2", timeFrom2, msg.timeFrom2);
        assertEquals("timeTo2", timeTo2, msg.timeTo2);
    }

    @Override
    public void successPartialAssembled(FhtMessage fhzMessage) {
        this.assembledfhtMessage = fhzMessage;
    }

}
