package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.FhtDateMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class FhtDateMessageTest implements ParserListener<FhtMessage> {
    
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
    public void testHollidayEnd() {
        decode("03023F690D");
        decode("0302406907");
        decode("03023E6902");
        assertDateMessage(fhtMessage, (short)302, FhtProperty.HOLIDAY_END_DATE, true, true, 7, 13 );
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

    public static void assertDateMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty, boolean dataRegister, boolean fromFht_8B, int month, int day) {
        assertNotNull(fhtMessage);
        final FhtDateMessage msg = (FhtDateMessage)fhtMessage;
        assertEquals("housecode", (short)housecode, msg.housecode);
        assertEquals("command", fhtProperty, msg.command);
        assertEquals("fromFht_8B", fromFht_8B, msg.fromFht_8B);
        assertEquals("dataRegister", dataRegister, msg.dataRegister);
        assertEquals("month", month, msg.month);
        assertEquals("day", day, msg.day);
    }

    @Override
    public void successPartialAssembled(FhtMessage fhzMessage) {
        this.fhtMessage = fhzMessage;
    }

}
