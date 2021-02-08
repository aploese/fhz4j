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

import de.ibapl.fhz4j.api.Message;
import de.ibapl.fhz4j.api.Protocol;
import de.ibapl.fhz4j.cul.CulEobMessage;
import de.ibapl.fhz4j.cul.CulFhtDeviceOutBufferContentRequest;
import de.ibapl.fhz4j.cul.CulFhtDeviceOutBufferContentResponse;
import de.ibapl.fhz4j.cul.CulGetSlowRfSettingsRequest;
import de.ibapl.fhz4j.cul.CulGetSlowRfSettingsResponse;
import de.ibapl.fhz4j.cul.CulLovfMessage;
import de.ibapl.fhz4j.cul.CulMessage;
import de.ibapl.fhz4j.cul.CulMessageListener;
import de.ibapl.fhz4j.cul.CulRemainingFhtDeviceOutBufferSizeRequest;
import de.ibapl.fhz4j.cul.CulRemainingFhtDeviceOutBufferSizeResponse;
import de.ibapl.fhz4j.cul.CulRequest;
import de.ibapl.fhz4j.cul.SlowRfFlag;
import de.ibapl.fhz4j.protocol.em.EmMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessage;
import de.ibapl.fhz4j.protocol.fht.Fht80bRawMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;
import de.ibapl.fhz4j.protocol.hms.HmsMessage;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class CulParserTest implements CulMessageListener {

    private Throwable throwable;
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

    private void assertCulFhtDeviceOutBufferContentResponse(FhtMessage... expectedMessages) {
        assertNotNull(culMessage);
        final CulFhtDeviceOutBufferContentResponse response = (CulFhtDeviceOutBufferContentResponse) culMessage;
        Assertions.assertArrayEquals(expectedMessages, response.pendingMessages.toArray());
    }

    private void assertCulCulRemainingFhtDeviceOutBufferSizeResponse(short expectedSize) {
        assertNotNull(culMessage);
        final CulRemainingFhtDeviceOutBufferSizeResponse response = (CulRemainingFhtDeviceOutBufferSizeResponse) culMessage;
        Assertions.assertEquals(expectedSize, response.buffSize);
    }

    private void assertCulGetSlowRfSettingsResponse(int milliTimeToSend, Set<SlowRfFlag> slowRfFlags) {
        assertNotNull(culMessage);
        final CulGetSlowRfSettingsResponse response = (CulGetSlowRfSettingsResponse) culMessage;
        assertEquals(milliTimeToSend, response.milliTimeToSend);
        assertEquals(slowRfFlags, response.slowRfFlags);
    }

    @BeforeEach
    public void setUp() {
        parser = new CulParser<>(this);
    }

    @AfterEach
    public void tearDown() {
        parser = null;
    }

    private void decode(String s, CulRequest ...requests) {
        for (CulRequest request : requests) {
        	parser.addCulRequest(request);
        }

    	fhtMessage = null;
        fhtPartialMessage = null;
        hmsMsg = null;
        emMsg = null;
        laCrosseTx2Message = null;
        culMessage = null;
        receiveEnabled = null;
        helpMessage = null;
        throwable = null;
        for (char c : s.toCharArray()) {
            parser.parse(c);
        }
    }

    @Test
    public void decode_CUL_LOVF() {
        decode("LOVF\r\n");
        assertEquals(CulLovfMessage.LOVF, culMessage);
    }

    @Test
    public void decode_CUL_EOB() {
        decode("EOB\r\n");
        assertEquals(CulEobMessage.EOB, culMessage);
    }

    @Test
    public void decode_Test() {
        //SEND "X\r\n" "T02\r\n" "T03\r\n"
        decode("T030153770CFA\r\n61  892\r\n0301:412B\r\nA9\r\n");
        FhtDateMessageTest.assertProtocolMessage(fhtMessage, 301, FhtProperty.CAN_CMIT, 0x77, false, true, 0x0C);
        //TODO decode X and  T
    }

    @Test
    public void decode_Test_T__AND__X() {
        //SEND "X\r\n" "T02\r\n" "T03\r\n"
        decode("01  861\r\n", new CulGetSlowRfSettingsRequest());
        assertCulGetSlowRfSettingsResponse(861, EnumSet.of(SlowRfFlag.REPORT_PACKAGE));
        decode("N/A\r\n", new CulFhtDeviceOutBufferContentRequest());
        assertCulFhtDeviceOutBufferContentResponse();
        decode("AE\r\n", new CulRemainingFhtDeviceOutBufferSizeRequest());
        assertCulCulRemainingFhtDeviceOutBufferSizeResponse((short) 0xae);
        decode("T0203447901FA\r\n");
        decode("61  853\r\n", new CulGetSlowRfSettingsRequest());
        assertCulGetSlowRfSettingsResponse(853, EnumSet.of(SlowRfFlag.REPORT_PACKAGE, SlowRfFlag.WITH_RSSI, SlowRfFlag.REPORT_FHT_PROTOCOL_MESSAGES));
        decode("N/A\r\n", new CulFhtDeviceOutBufferContentRequest());
        assertCulFhtDeviceOutBufferContentResponse();
        decode("0103:6014,6103 0401:6301\r\n", new CulFhtDeviceOutBufferContentRequest());
        assertCulFhtDeviceOutBufferContentResponse(new Fht80bRawMessage((short) 103, FhtProperty.YEAR, (byte) 0, false, false, (byte) 20), new Fht80bRawMessage((short) 103, FhtProperty.MONTH, (byte) 0, false, false, (byte) 3), new Fht80bRawMessage((short) 401, FhtProperty.HOUR, (byte) 0, false, false, (byte) 1));
    }

    /*
@2020-03-24T00:05:00.347443Z    CH      call write:     "X\r\nT02\r\nT03\r\n"
@2020-03-24T00:05:00.348913Z    CH      return write    duration: PT0.00147S
@2020-03-24T00:05:00.349564Z    CH      call write:     "T010360146103621863016405\n"
@2020-03-24T00:05:00.351956Z    CH      return write    duration: PT0.002392S
@2020-03-24T00:05:00.352541Z    CH      call write:     "X\r\nT02\r\nT03\r\n"
@2020-03-24T00:05:00.353861Z    CH      return write    duration: PT0.00132S
@2020-03-24T00:05:00.354457Z    CH      call write:     "X\r\nT02\r\nT03\r\n"
@2020-03-24T00:05:00.355858Z    CH      return write    duration: PT0.001401S
@2020-03-24T00:05:00.356453Z    CH@2020-03-24T00:05:00.356578Z  CH      return read:    "61  139\r\nN/A\r\nAE\r\n61  139\r\n0103:6014,6103,6218,6301,6405\r\nA1\r\n61   call write:     "T010365FF66FF\n"       dur
ation: PT10.087149S
"
@2020-03-24T00:05:00.364153Z    CH      return write    duration: PT0.0077S
@2020-03-24T00:05:00.364767Z    CH      call write:     "X\r\nT02\r\nT03\r\n"
@2020-03-24T00:05:00.366220Z    CH      return write    duration: PT0.001453S
@2020-03-24T00:05:00.377694Z    CH      call read
@2020-03-24T00:05:00.378206Z    CH      return read:    "  139\r\n0103:6014,6103,6218,6301,6405\r\nA1\r\n61  139\r\n0103:6014,610"      duration: PT0.000512S
@2020-03-24T00:05:00.386175Z    CH      call read
@2020-03-24T00:05:00.486911Z    CH      return read:    "3,6218,6301,6405 0103:65FF,66FF\r\n9A\r\n"     duration: PT0.100736S
     */
    
    
    
    @Test
    public void recoverFromFailure() {
        decode("LOVF\r\n");
        assertEquals(culMessage, CulLovfMessage.LOVF);
        //arm the parser to expect CUL requests...
        decode("T02024B7700FA\r\n", new CulGetSlowRfSettingsRequest(), new CulFhtDeviceOutBufferContentRequest(), new CulRemainingFhtDeviceOutBufferSizeRequest());
        assertNotNull(fhtMessage);
        decode("tA96678030A29\r\n");
        assertNotNull(throwable);
        decode("61    3\r\n");
        assertCulGetSlowRfSettingsResponse(3, EnumSet.of(SlowRfFlag.REPORT_PACKAGE, SlowRfFlag.WITH_RSSI, SlowRfFlag.REPORT_FHT_PROTOCOL_MESSAGES));
        decode("N/A\r\n");
        decode("AE\r\n");
        decode("T020700A60D11\r\n");
        assertNotNull(fhtMessage);
        assertEquals(207, fhtMessage.housecode);
        assertEquals(FhtProperty.VALVE, fhtMessage.command);
    }
    
    @Test
    public void decodeModay_Times() {
        // fail();
    }

    @Test
    public void decodeDateAndTime() {
        decode("T0401606912EF\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        decode("T0401616901EF\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        decode("T0401626911EF\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        decode("T040163690BEE\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        decode("T0401646930EF\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        assertNotNull(fhtMessage);
    }

    @Test
    public void decode_FHT_26_0_Degree_Centigrade() {
        decode("T370A42690406\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        decode("T370A43690106\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        assertNotNull(fhtMessage);
    }

    @Test
    public void decode_FHT_Holiday_End() {
        decode("T370A3F010106\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        decode("T370A40010106\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        decode("T370A3E690206\r\n");
        assertNull(throwable);
        assertNotNull(fhtMessage);
        FhtDateMessageTest.assertDateMessage(fhtMessage, 5510, FhtProperty.HOLIDAY_END_DATE, true, true, 1, 1);
    }

    @Test
    public void decode_FHT_Party_End() {
        decode("T370A3F010106\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        decode("T370A40010106\r\n");
        assertNull(throwable);
        assertNotNull(fhtPartialMessage);
        decode("T370A3E690306\r\n");
        assertNull(throwable);
        assertNotNull(fhtMessage);
        FhtTimeMessageTest.assertTimeMessage(fhtMessage, 5510, FhtProperty.PARTY_END_TIME, true, true,
                LocalTime.of(0, 10));
    }

    @Test
    public void testFHT_HC9876() {
        decode("T624C012F003D\r\n");
        assertNull(throwable);
        assertNotNull(fhtMessage);
        assertEquals(9876, fhtMessage.housecode);
    }

    /**
     * Only tests if the CulParser works with the HmsParser
     */
    @Test
    public void decode_HMS_100_TF() {
        decode("H7758005282720F\r\n");
        assertNull(throwable);
        assertNotNull(hmsMsg);
        assertTrue(hmsMsg instanceof HmsMessage);
    }

    @Test
    public void decode_FS20_1() {
        decode("FC04B01002B\r\n");
        assertNull(throwable);
        assertNotNull(fs20Msg);
        assertTrue(fs20Msg instanceof FS20Message);
    }

    @Test
    public void decode_LA_CROSSE_TX2() {
        decode("tA00E73173D\r\n");
        assertNull(throwable);
        assertNotNull(laCrosseTx2Message);

//TODO ???		decode("tA00AA002EAE5\r\n");
//	 assertNotNull(laCrosseTx2Message);
    }

    @Test
    public void decode_LA_CROSSE_TX2_FHT() {
        decode("tA003280104FE\r\nT0102003200FE\r\n");
        assertNull(laCrosseTx2Message);
        assertNotNull(throwable);
        assertNotNull(fhtMessage);
    }

    @Test
    public void decode_EM() {
        decode("E010201040004000F0047\r\n");
        assertNull(throwable);
        assertNotNull(emMsg);
    }

    @Test
    @Disabled
    public void decode_FHT() {
        decode("T01010069B6F8\n");
        assertNull(throwable);
        assertNotNull(fhtMessage);
    }

    @Test
    public void decode_EvoHome() {
        decode("vr18067AEC067AEC1F0903FF057D\r\n");
        assertNull(throwable);
        assertNotNull(evoHomeMsg);
    }

    @Test
    public void decodeEvoHomeInit() {
        decode("vr18067AEC067AEC1F0903FF057D\r\n");
        assertNull(throwable);
        assertNotNull(evoHomeMsg);
        //Answer va is a CUL State ... Message
        decode("vr18067Ava\r\n");
        assertNull(throwable);
        assertEquals(Protocol.EVO_HOME, receiveEnabled);

        decode("vr18895E5D895E5D30C903000927\r\n");
        assertNull(throwable);
        assertNotNull(evoHomeMsg);
    }

    @Test
    public void decodeHelpMessage() {
        decode("? (~ is unknown) Use one of A B C e F G h i K L l M m R T t U u V v W X x Y Z z\r\n");
        assertNull(throwable);
        assertEquals("? (~ is unknown) Use one of A B C e F G h i K L l M m R T t U u V v W X x Y Z z", helpMessage);
        assertTrue(parser.isIdle());
    }

    @Test
    public void decodeFhtMessage_5() throws Exception {
        ByteBuffer b = ByteBuffer.allocateDirect(1024);
        FileChannel fc = FileChannel.open(Path.of("/home/aploese/eno1.txt"));
        fc.read(b);
        
        decode("T0101436900F9\r\nT0101446901F9\r\nT0101856904F9\r\n");
        assertNull(throwable);
        assertNotNull(fhtMessage);
    }

    @Test
    public void decodeEMMessage_5() throws Throwable {
        decode("T02002E6990EF\r\nT02002F6990EE\r\nT0200826924EF\r\n");
        assertNull(throwable);
        assertNotNull(fhtMessage);
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
        throwable = t;
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
