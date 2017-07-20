package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.Fht80bWarning;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtWarningMessage;
import java.util.EnumSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class FhtWarningsMessageTest implements ParserListener<FhtMessage> {
    
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
    public void decode_NoWarnings() {
        decode("0302446900");
        FhtWarningsMessageTest.assertWarningsMessage(fhtMessage, 302, true, true, EnumSet.noneOf(Fht80bWarning.class));
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

    public static void assertWarningsMessage(FhtMessage fhtMessage, int housecode, boolean dataRegister, boolean fromFht_8B, Set<Fht80bWarning> warnings) {
        assertNotNull(fhtMessage);
        final FhtWarningMessage msg = (FhtWarningMessage)fhtMessage;
        assertEquals("housecode", (short)housecode, msg.housecode);
        assertEquals("command", FhtProperty.WARNINGS, msg.command);
        assertEquals("fromFht_8B", fromFht_8B, msg.fromFht_8B);
        assertEquals("dataRegister", dataRegister, msg.dataRegister);
        assertEquals("warnings", warnings, msg.warnings);
    }

    @Override
    public void successPartialAssembled(FhtMessage fhzMessage) {
        this.fhtMessage = fhzMessage;
    }

}
