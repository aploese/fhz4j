package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.cul.FhtParser;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtValveMode;
import de.ibapl.fhz4j.protocol.fht.FhtValvePosMessage;
import de.ibapl.fhz4j.protocol.fht.FhtValveSyncMessage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author aploese
 */
public class FhtValveMessageTest implements ParserListener<FhtMessage> {
    
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
    public void testSync() {
        decode("0302002CE1");
        assertValveSyncMessage(fhtMessage, 302, 112.5f);
        decode("0302002C8F");
        assertValveSyncMessage(fhtMessage, 302, 71.5f);
        decode("0302002C03");
        assertValveSyncMessage(fhtMessage, 302, 1.5f);

        decode("0302002600");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.POSITION, 0.0f);

    }

    @Test
    public void testJumpOffToOn() {
        decode("0302002200");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.OFF, 0.0f);
        decode("030200A200");
        assertValveMessage(fhtMessage, 302, true, false, FhtValveMode.OFF, 0.0f);

        decode("0302002100");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.ON, 100.0f);
        decode("030200A100");
        assertValveMessage(fhtMessage, 302, true, false, FhtValveMode.ON, 100.0f);

        decode("0302002627");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.POSITION, 15.294118f);
        decode("030200A627");
        assertValveMessage(fhtMessage, 302, true, false, FhtValveMode.POSITION, 15.294118f);
    }
    
    @Test
    public void testLimeCycle() {
        decode("0302002A00");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.LIME_CYCLE, 0.0f);
        decode("030200AA00");
        assertValveMessage(fhtMessage, 302, true, false, FhtValveMode.LIME_CYCLE, 0.0f);
    }
    
    @Test
    public void test_0X20() {
        decode("030200B000");
        assertValveMessage(fhtMessage, 302, true, true, FhtValveMode.END_OF_SYNC_SEQUENCE, 0.0f);
    }
          
    @Test
    public void test_ValveOffset() {
        decode("0302022832");
        assertValveOffsetMessage(fhtMessage, 302, false, false, FhtProperty.OFFSET_VALVE_2, FhtValveMode.OFFSET_ADJUST, 50.0f);
        decode("03020628B2");
        assertValveOffsetMessage(fhtMessage, 302, false, false, FhtProperty.OFFSET_VALVE_6, FhtValveMode.OFFSET_ADJUST, -50.0f);
        decode("0302022F32");
        assertValveOffsetMessage(fhtMessage, 302, false, false, FhtProperty.OFFSET_VALVE_2, FhtValveMode.PAIRING, 50.0f);
        decode("0302062FB2");
        assertValveOffsetMessage(fhtMessage, 302, false, false, FhtProperty.OFFSET_VALVE_6, FhtValveMode.PAIRING, -50.0f);
    }
        
    
    @Test
    @Ignore // Was ist das nach dem sync ...? 
    public void testVALVE_1() {
        decode("0302012F00");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.OFF, 0.0f);
    }    
    
    @Test
    public void testFhtValePosMessage() {
        decode("1B0100A600");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 2701, true, false, FhtValveMode.POSITION, 0.0f);

        decode("4D5400A600");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 7784, true, false, FhtValveMode.POSITION, 0.0f);
        decode("435E002626");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 6794, false, false, FhtValveMode.POSITION, 14.901961f);
        decode("212700262C");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 3339, false, false, FhtValveMode.POSITION, 17.254902f);
        decode("1B0100A600");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 2701, true, false, FhtValveMode.POSITION, 0.0f);
        decode("4D5400A600");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 7784, true, false, FhtValveMode.POSITION, 0.0f);
    }


    @Test
    public void testHouseCode() {
        decode("635000A620");
        assertValveMessage(fhtMessage, 9980, true, false, FhtValveMode.POSITION, 12.54902f);
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

    public static void assertValveMessage(FhtMessage fhtMessage, int housecode, boolean repeated, boolean allowLowBatteryBeep, FhtValveMode mode, float position) {
        assertNotNull(fhtMessage);
        final FhtValvePosMessage msg = (FhtValvePosMessage)fhtMessage; 
        assertEquals("housecode", housecode, msg.housecode);
        assertEquals("command", FhtProperty.VALVE, msg.command);
        assertEquals("allowLowBatteryBeep", allowLowBatteryBeep, msg.allowLowBatteryBeep);
        assertEquals("repeated", repeated, msg.repeated);
        assertEquals("mode", mode, msg.mode);
        assertEquals("pos", position, msg.position, Float.MIN_NORMAL);
    }

    public static  void assertValveSyncMessage(FhtMessage fhtMessage, int housecode, float timeLeft) {
        assertNotNull(fhtMessage);
        final FhtValveSyncMessage msg = (FhtValveSyncMessage)fhtMessage;
        assertEquals("housecode", (short)housecode, msg.housecode);
        assertEquals("command", FhtProperty.VALVE, msg.command);
        assertEquals("timeLeft", timeLeft, msg.timeLeft, Float.MIN_NORMAL);
    }

    public static void assertValveOffsetMessage(FhtMessage fhtMessage, int housecode, boolean repeated, boolean allowLowBatteryBeep, FhtProperty command, FhtValveMode mode, float position) {
        assertNotNull(fhtMessage);
        final FhtValvePosMessage msg = (FhtValvePosMessage)fhtMessage; 
        assertEquals("housecode", housecode, msg.housecode);
        assertEquals("command", command, msg.command);
        assertEquals("allowLowBatteryBeep", allowLowBatteryBeep, msg.allowLowBatteryBeep);
        assertEquals("repeated", repeated, msg.repeated);
        assertEquals("mode", mode, msg.mode);
        assertEquals("pos", position, msg.position, Float.MIN_NORMAL);
    }

    @Override
    public void successPartialAssembled(FhtMessage fhzMessage) {
        this.fhtMessage = fhzMessage;
    }
    
}