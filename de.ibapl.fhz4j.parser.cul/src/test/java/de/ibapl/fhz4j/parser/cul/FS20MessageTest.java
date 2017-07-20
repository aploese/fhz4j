package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fs20.FS20CommandValue;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;
import java.util.EnumSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author aploese
 */
public class FS20MessageTest implements ParserListener<FS20Message> {
    
    private FS20Parser parser = new FS20Parser(this);
    private FS20Message fS20Message;
    private Throwable error;
    
        private void decode(String s) {
        fS20Message = null;
        error = null;
        parser.init();
        for (char c : s.toCharArray()) {
            parser.parse(c);
        }
    }

    
        
    @Override
    public void success(FS20Message fS20Message) {
        this.fS20Message = fS20Message;
    }

    @Override
    public void fail(Throwable t) {
        error = t;
    }

    @Override
    public void successPartial(FS20Message fhzMessage) {
        throw new RuntimeException("No partial message expected."); 
    }

    @Override
    public void successPartialAssembled(FS20Message fhzMessage) {
        throw new RuntimeException("No partial message expected."); 
    }

     public static void assertFs20Message(FS20Message fs20Msg, int housecode, int offset, FS20CommandValue command) {
        assertNotNull(fs20Msg);
        final FS20Message msg = (FS20Message)fs20Msg;
  //TODO Housecode??      assertEquals("housecode", (short)housecode, msg.housecode);
        assertEquals("offset", offset, msg.offset);
        assertEquals("command", command, msg.command);
    }

    @Test
    public void decode_FS20_1() {
        decode("C04B0100");
        assertFs20Message(fS20Message, 19275, 1, FS20CommandValue.OFF);
    }

    @Test
    public void decode_FS20_2() {
        decode("C04B0111");
        assertFs20Message(fS20Message, 19275, 1, FS20CommandValue.ON);
    }

    @Test
    public void decode_FS20_3() {
        decode("C04B0313");
        assertFs20Message(fS20Message, 19275, 3, FS20CommandValue.DIM_UP);
    }
    
    

}
