package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.em.Em1000EmMessage;
import de.ibapl.fhz4j.protocol.em.EmDeviceType;
import de.ibapl.fhz4j.protocol.em.EmMessage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author aploese
 */
public class EmMessageTest implements ParserListener<EmMessage> {
    
    private EmParser parser = new EmParser(this);
    private EmMessage emMessage;
    private Throwable error;
    
        private void decode(String s) {
        emMessage = null;
        error = null;
        parser.init();
        for (char c : s.toCharArray()) {
            parser.parse(c);
        }
    }

    
        
    @Override
    public void success(EmMessage emMessage) {
        this.emMessage = emMessage;
    }

    @Override
    public void fail(Throwable t) {
        error = t;
    }

    @Override
    public void successPartial(EmMessage emMessage) {
        throw new RuntimeException("No partial message expected."); 
    }

    @Override
    public void successPartialAssembled(EmMessage emMessage) {
        throw new RuntimeException("No partial message expected."); 
    }

     public static void assertEm1000EmMessage(EmMessage emMsg, int address, short counter, float energy, float energyLast5Min, float maxPowerLast5Min) {
        assertNotNull(emMsg);
        assertEquals("address", (short)address, emMsg.address);
        assertEquals("emDeviceType", EmDeviceType.EM_1000_EM, emMsg.emDeviceType);
        final Em1000EmMessage msg = (Em1000EmMessage)emMsg;
        assertEquals("counter", counter, msg.counter);
        assertEquals("energy", energy, msg.energy, Float.MIN_NORMAL);
        assertEquals("energyLast5Min", energyLast5Min, msg.energyLast5Min, Float.MIN_NORMAL);
        assertEquals("maxPowerLast5Min", maxPowerLast5Min, msg.maxPowerLast5Min, Float.MIN_NORMAL);
   
    }

    @Test
    public void decode_EM_1() {
        decode("020571241000000000");
        assertEm1000EmMessage(emMessage, 5, (short)113, 4.132f, 0.0f, 0.0f);
    }

    @Test
    public void decode_EM_2() {
        decode("0205ADC91008000B00");
        assertEm1000EmMessage(emMessage, 5,(short)173, 4.2970004f, 0.08f, 0.11f);
    }


}
