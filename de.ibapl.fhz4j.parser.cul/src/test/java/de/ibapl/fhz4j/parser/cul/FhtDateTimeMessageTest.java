package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fht.FhtDateTimeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtTempMessage;
import de.ibapl.fhz4j.protocol.fht.FhtTimeMessage;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class FhtDateTimeMessageTest implements ParserListener<FhtMessage> {

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
    public void decodeCurrentTime() {
        decode("0302606911");
        FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.YEAR, true, true, 17);
        decode("0302616907");
        FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.MONTH, true, true, 7);
        decode("0302626914");
        FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.DAY_OF_MONTH, true, true, 20);
        decode("030263690E");
        FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.HOUR, true, true, 14);
        decode("0302646917");
        FhtTempMessageTest.assertRawMessage(partialFhtMessage, 302, FhtProperty.MINUTE, true, true, 23);
        assertDateTimeMessage(assembledfhtMessage, 302, FhtProperty.CURRENT_DATE_AND_TIME, true, true, LocalDateTime.of(2017, Month.JULY, 20, 14, 23));
    }
    
    @Test
    public void decodeDateTime_WrongOrder() {
        decode("0401606912");
        decode("0401626911");
        decode("0401616901");
        decode("040163690B");
        decode("0401646930");
        assertNull(assembledfhtMessage);
    }

    @Test
    public void decodeDate_Incomplete() {
        decode("0401606912");
        decode("0401626911");
        decode("040163690B");
        decode("0401646930");
        assertNull(assembledfhtMessage);
    }
    
    @Test
    public void decodeDate_No_Start() {
        decode("0401616901");
        decode("0401626911");
        decode("040163690B");
        decode("0401646930");
        assertNull(assembledfhtMessage);
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

    public static void assertDateTimeMessage(FhtMessage fhtMessage, int housecode, FhtProperty fhtProperty, boolean dataRegister, boolean fromFht_8B, LocalDateTime ts) {
        assertNotNull(fhtMessage);
        final FhtDateTimeMessage msg = (FhtDateTimeMessage) fhtMessage;
        assertEquals("housecode", (short) housecode, msg.housecode);
        assertEquals("command", fhtProperty, msg.command);
        assertEquals("fromFht_8B", fromFht_8B, msg.fromFht_8B);
        assertEquals("dataRegister", dataRegister, msg.dataRegister);
        assertEquals("ts", ts, msg.ts);
    }

    @Override
    public void successPartialAssembled(FhtMessage fhzMessage) {
        this.assembledfhtMessage = fhzMessage;
    }

}
