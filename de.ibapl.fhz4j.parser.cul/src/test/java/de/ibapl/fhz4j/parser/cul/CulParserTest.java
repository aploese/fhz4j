/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2019, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.cul.CulMessage;
import java.time.LocalTime;

import de.ibapl.fhz4j.api.Message;
import de.ibapl.fhz4j.api.Protocol;
import de.ibapl.fhz4j.cul.CulAdapter;
import de.ibapl.fhz4j.protocol.em.EmMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;
import de.ibapl.fhz4j.protocol.hms.HmsMessage;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import de.ibapl.fhz4j.cul.CulMessageListener;
import java.io.IOException;

/**
 *
 * @author Arne Plöse
 */
public class CulParserTest implements CulMessageListener {

    private CulParser<Message> parser;
    private FhtMessage fhtMessage;
    private HmsMessage hmsMsg;
    private EmMessage emMsg;
    private FS20Message fs20Msg;
    private LaCrosseTx2Message laCrosseTx2Message;
    private FhtMessage fhtPartialMessage;
    private CulMessage culMessage;
    private EvoHomeMessage evoHomeMsg;
    private float signalStrength;
    private Protocol receiveEnabled;
    private String helpMessage;

    public CulParserTest() {
    }

    @BeforeEach
    public void setUp() {
        parser = new CulParser<>(this);
    }

    @AfterEach
    public void tearDown() {
        parser = null;
    }

    private void decode(String s) {
        fhtMessage = null;
        fhtPartialMessage = null;
        hmsMsg = null;
        emMsg = null;
        laCrosseTx2Message = null;
        culMessage = null;
        receiveEnabled = null;
        helpMessage = null;
        for (char c : s.toCharArray()) {
            parser.parse(c);
        }
    }

    @Test
    public void decode_CUL_LOVF() {
        decode("LOVF\r\n");
        assertEquals(CulMessage.LOVF, culMessage);
    }

    @Test
    public void decode_CUL_EOB() {
        decode("EOB\r\n");
        assertEquals(CulMessage.EOB, culMessage);
    }

    @Test
    public void decodeModay_Times() {
        // fail();
    }

    @Test
    public void decodeDateAndTime() {
        decode("T0401606912EF\r\n");
        assertNotNull(fhtPartialMessage);
        decode("T0401616901EF\r\n");
        assertNotNull(fhtPartialMessage);
        decode("T0401626911EF\r\n");
        assertNotNull(fhtPartialMessage);
        decode("T040163690BEE\r\n");
        assertNotNull(fhtPartialMessage);
        decode("T0401646930EF\r\n");
        assertNotNull(fhtPartialMessage);
        assertNotNull(fhtMessage);
    }

    @Test
    public void decode_FHT_26_0_Degree_Centigrade() {
        decode("T370A42690406\r\n");
        assertNotNull(fhtPartialMessage);
        decode("T370A43690106\r\n");
        assertNotNull(fhtPartialMessage);
        assertNotNull(fhtMessage);
    }

    @Test
    public void decode_FHT_Holiday_End() {
        decode("T370A3F010106\r\n");
        assertNotNull(fhtPartialMessage);
        decode("T370A40010106\r\n");
        assertNotNull(fhtPartialMessage);
        decode("T370A3E690206\r\n");
        assertNotNull(fhtMessage);
        FhtDateMessageTest.assertDateMessage(fhtMessage, 5510, FhtProperty.HOLIDAY_END_DATE, true, true, 1, 1);
    }

    @Test
    public void decode_FHT_Party_End() {
        decode("T370A3F010106\r\n");
        assertNotNull(fhtPartialMessage);
        decode("T370A40010106\r\n");
        assertNotNull(fhtPartialMessage);
        decode("T370A3E690306\r\n");
        FhtTimeMessageTest.assertTimeMessage(fhtMessage, 5510, FhtProperty.PARTY_END_TIME, true, true,
                LocalTime.of(0, 10));
    }

    @Test
    public void testFHT_HC9876() {
        decode("T624C012F003D\r\n");
        assertNotNull(fhtMessage);
        assertEquals(9876, fhtMessage.housecode);
    }

    /**
     * Only tests if the CulParser works with the HmsParser
     */
    @Test
    public void decode_HMS_100_TF() {
        decode("H7758005282720F\r\n");
        assertNotNull(hmsMsg);
        assertTrue(hmsMsg instanceof HmsMessage);
    }

    @Test
    public void decode_FS20_1() {
        decode("FC04B01002B\r\n");
        assertNotNull(fs20Msg);
        assertTrue(fs20Msg instanceof FS20Message);
    }

    @Test
    public void decode_LA_CROSSE_TX2() {
        decode("tA00E73173D\r\n");
        assertNotNull(laCrosseTx2Message);

//TODO ???		decode("tA00AA002EAE5\r\n");
//	 assertNotNull(laCrosseTx2Message);
    }

    @Test
    public void decode_EM() {
        decode("E010201040004000F0047\r\n");
        assertNotNull(emMsg);
    }

    @Test
    @Disabled
    public void decode_FHT() {
        decode("T01010069B6F8\n");
        assertNotNull(fhtMessage);
    }

    @Test
    public void decode_EvoHome() {
        decode("vr18067AEC067AEC1F0903FF057D\r\n");
        assertNotNull(evoHomeMsg);
    }

    @Test
    public void decodeEvoHomeInit() {
        decode("vr18067AEC067AEC1F0903FF057D\r\n");
        assertNotNull(evoHomeMsg);
        //Answer va is a CUL State ... Message
        decode("vr18067Ava\r\n");
        assertEquals(Protocol.EVO_HOME, receiveEnabled);
        
        decode("vr18895E5D895E5D30C903000927\r\n");
        assertNotNull(evoHomeMsg);
    }

    
    @Test
    public void decodeHelpMessage() {
        decode("? (~ is unknown) Use one of A B C e F G h i K L l M m R T t U u V v W X x Y Z z\r\n");
        assertEquals("? (~ is unknown) Use one of A B C e F G h i K L l M m R T t U u V v W X x Y Z z", helpMessage);
        assertTrue(parser.isIdle());
    }
    
    @Override
    public void emDataParsed(EmMessage emMsg) {
        this.emMsg = emMsg;
    }

    @Override
    public void fhtDataParsed(FhtMessage fhtMessage) {
        this.fhtMessage = fhtMessage;
    }

    @Override
    public void hmsDataParsed(HmsMessage hmsMsg) {
        this.hmsMsg = hmsMsg;
    }

    @Override
    public void fs20DataParsed(FS20Message fs20Msg) {
        this.fs20Msg = fs20Msg;
    }

    @Override
    public void laCrosseTxParsed(LaCrosseTx2Message laCrosseTx2Msg) {
        this.laCrosseTx2Message = laCrosseTx2Msg;
    }

    @Override
    public void fhtPartialDataParsed(FhtMessage fhtMessage) {
        this.fhtPartialMessage = fhtMessage;
    }

    @Override
    public void failed(Throwable t) {
        throw new RuntimeException(t);
    }

    @Override
    public void culMessageParsed(CulMessage culMessage) {
        this.culMessage = culMessage;
    }

    @Override
    public void evoHomeParsed(EvoHomeMessage evoHomeMsg) {
        this.evoHomeMsg = evoHomeMsg;
    }

    @Override
    public void signalStrength(float signalStrength) {
        this.signalStrength = this.signalStrength;
    }

    @Override
    public void receiveEnabled(Protocol protocol) {
        this.receiveEnabled = protocol;
    }

    @Override
    public void helpParsed(String helpMessages) {
        this.helpMessage = helpMessages;
    }

    @Override
    public void onIOException(IOException ioe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
