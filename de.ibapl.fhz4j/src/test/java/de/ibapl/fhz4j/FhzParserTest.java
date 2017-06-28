package de.ibapl.fhz4j;

/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2017 Arne Plöse
 * %%
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009, 2017, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
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
 * #L%
 */

import de.ibapl.fhz4j.em.EmMessage;
import de.ibapl.fhz4j.fht.FhtMultiMsgMessage;
import de.ibapl.fhz4j.fht.FhtMessage;
import de.ibapl.fhz4j.fs20.FS20Message;
import de.ibapl.fhz4j.hms.HmsMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class FhzParserTest implements FhzDataListener {

    private FhzParser parser;
    private FhtMessage fhtMessage;
    private FhtMultiMsgMessage fhtMultiMsgMessage;
    private HmsMessage hmsMsg;
    private EmMessage emMsg;
    private FS20Message fs20Msg;

    public FhzParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        parser = new FhzParser(this);
    }

    @After
    public void tearDown() {
        parser = null;
    }

    private void decode(String s) {
        fhtMessage = null;
        fhtMultiMsgMessage = null;
        hmsMsg = null;
        emMsg = null;
        for (byte b : s.getBytes()) {
            parser.parse(0x00FF & b);
        }
    }

    @Test
    public void testSomeMethod() {
        decode("T1B0100A600\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 2701, command: valve, description: same pos absolute, value: 0.0% signal strength: 0.0 dB", fhtMessage.toString());

        decode("T4D5400A600\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 7784, command: valve, description: same pos absolute, value: 0.0% signal strength: 0.0 dB", fhtMessage.toString());
        decode("T435E002626\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 6794, command: valve, description: changed pos absolute, value: 14.9% signal strength: 0.0 dB", fhtMessage.toString());
        decode("T212700262C\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 3339, command: valve, description: changed pos absolute, value: 17.3% signal strength: 0.0 dB", fhtMessage.toString());
        decode("T1B0100A600\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 2701, command: valve, description: same pos absolute, value: 0.0% signal strength: 0.0 dB", fhtMessage.toString());
        decode("T4D5400A600\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 7784, command: valve, description: same pos absolute, value: 0.0% signal strength: 0.0 dB", fhtMessage.toString());

    }

    @Test
    public void decode_FHT_25_1_Degree_Centigrade() {
        decode("T61344269FBFC\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 9752, command: measured low, description: from FHT-B data register, value: 25.1°C signal strength: -76.0 dB", fhtMessage.toString());
    }

    @Test
    public void decode_FHT_26_0_Degree_Centigrade() {
        decode("T370A42690406\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 5510, command: measured low, description: from FHT-B data register, value: 0.4°C signal strength: -71.0 dB", fhtMessage.toString());
        decode("T370A43690106\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 5510, command: measured high, description: from FHT-B data register, value: 25.0°C signal strength: -71.0 dB", fhtMessage.toString());
        assertNotNull(fhtMultiMsgMessage);
        assertEquals("housecode: 5510, measured temperature (combined): 26.0°C", fhtMultiMsgMessage.toString());
    }

    @Test
    @Ignore//TODO inject calendar
    public void decode_FHT_Holiday_End() {
        decode("T370A3E690206\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 5510, command: mode, description: from FHT-B data register, value: 2 signal strength: -71.0 dB", fhtMessage.toString());
        decode("T370A3F010106\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 5510, command: holiday 1, description:  0x0 0x1, value: 1 signal strength: -71.0 dB", fhtMessage.toString());
        decode("T370A40010106\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 5510, command: holiday 2, description:  0x0 0x1, value: 1 signal strength: -71.0 dB", fhtMessage.toString());
        assertNotNull(fhtMultiMsgMessage);
        assertEquals("", fhtMultiMsgMessage.toString());
    }

    @Test
    @Ignore//TODO inject calendar
    public void decode_FHT_Party_End() {
        decode("T370A3E690306\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 5510, command: mode, description: from FHT-B data register, value: 3 signal strength: -71.0 dB", fhtMessage.toString());
        decode("T370A3F010106\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 5510, command: holiday 1, description:  0x0 0x1, value: 1 signal strength: -71.0 dB", fhtMessage.toString());
        decode("T370A40010106\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 5510, command: holiday 2, description:  0x0 0x1, value: 1 signal strength: -71.0 dB", fhtMessage.toString());
        assertNotNull(fhtMultiMsgMessage);
        assertEquals("", fhtMultiMsgMessage.toString());
    }

    @Test
    public void decode_HMS_100_TF() {
        decode("H7758005282720F\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 7758, device type: HMS 100 TF status: [ ] temp: 25.2, humidy: 72.8 signal strength: -66.5 dB", hmsMsg.toString());

        decode("HC25C00098262F6\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: C25C, device type: HMS 100 TF status: [ ] temp: 20.9, humidy: 62.8 signal strength: -79.0 dB", hmsMsg.toString());

        decode("HC25C20128260EA\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: C25C, device type: HMS 100 TF status: [Batt low ] temp: 21.2, humidy: 60.8 signal strength: -85.0 dB", hmsMsg.toString());
    }

    @Test
    public void decode_HMS_100_TFK() {

//        H 7AEF 0400 0000F3 off
        decode("H7AEF04000000F3\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 7AEF, device type: HMS 100 TFK status: [ ] open: false signal strength: -80.5 dB", hmsMsg.toString());
//H 7AEF 0401 0000DD on
        decode("H7AEF04010000DD\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 7AEF, device type: HMS 100 TFK status: [ ] open: true signal strength: -91.5 dB", hmsMsg.toString());
//H 7AEF 2400 0000E5 off, Bat low
        decode("H7AEF24000000E5\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 7AEF, device type: HMS 100 TFK status: [Batt low ] open: false signal strength: -87.5 dB", hmsMsg.toString());
//H 7AEF 2401 0000DD on, Bat low
        decode("H7AEF24010000DD\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 7AEF, device type: HMS 100 TFK status: [Batt low ] open: true signal strength: -91.5 dB", hmsMsg.toString());
    }

    @Test
    public void decode_HMS_100_WD() {

//H 78D1 0200 FA00ED no water
        decode("H78D10200FA00ED\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 78D1, device type: HMS 100 WD status: [ ] water: false signal strength: -83.5 dB", hmsMsg.toString());
//H 78D1 0201 0000F3 water
        decode("H78D102010000F3\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 78D1, device type: HMS 100 WD status: [ ] water: true signal strength: -80.5 dB", hmsMsg.toString());
//H 78D1 2200 0000F2 no water, Bat low
        decode("H78D122000000F2\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 78D1, device type: HMS 100 WD status: [Batt low ] water: false signal strength: -81.0 dB", hmsMsg.toString());
//H 78D1 2201 0000F3
        decode("H78D122010000F3\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 78D1, device type: HMS 100 WD status: [Batt low ] water: true signal strength: -80.5 dB", hmsMsg.toString());
    }

    @Test
    public void decode_EM_1() {
        decode("E020571241000000000F1\r\n");
        assertNotNull(emMsg);
        assertEquals("type: EM 1000 EM, address: 5, counter: 113, energy: 4.132, energyLast5Min: 0.0, maxPowerLast5Min: 0.0 signal strength: -81.5 dB", emMsg.toString());
    }

    @Test
    public void decode_EM_2() {
        decode("E0205ADC91008000B00F6\r\n");
        assertNotNull(emMsg);
        assertEquals("type: EM 1000 EM, address: 5, counter: 173, energy: 4.2970004, energyLast5Min: 0.08, maxPowerLast5Min: 0.11 signal strength: -79.0 dB", emMsg.toString());
    }

    /*
     E0205AEC91000000000F8
     E0205AFC91000000000F7
     E0205B0CC100300B600F1
     E0205B1D31009000A00F0
     E0205B2D71004000900F0
     E0205B3D71000000000F6        
     E0205B4D71000000000F0
    
     E020514991100000000F2
     E020515991100000000FB
     */
    
    @Test
    public void decode_FS20_1() {
        decode("FC04B01002B\r\n");
        assertNotNull(fs20Msg);
        assertEquals("housecode: 19275, offset: 1, command: off signal strength: -52.5 dB", fs20Msg.toString());
    }

    @Test
    public void decode_FS20_2() {
        decode("FC04B01112C\r\n");
        assertNotNull(fs20Msg);
        assertEquals("housecode: 19275, offset: 1, command: on signal strength: -52.0 dB", fs20Msg.toString());
    }

        @Test
    public void decode_FS20_3() {
        decode("FC04B03132C\r\n");
        assertNotNull(fs20Msg);
        assertEquals("housecode: 19275, offset: 3, command: dimup signal strength: -52.0 dB", fs20Msg.toString());
    }

    /*  
     FC04B01002B 
     FC04B01112C
     FC04B02002C
     FC04B02112C
     FC04B03002B
     FC04B03112F
    */

    @Ignore
    @Test
    public void decode_HMS_100_RM() {

//H 707D 0300 FF00F6 kein Feuer
        decode("H707D0300FF00F6\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 707D, device type: HMS 100 RM status: [ ] smoke: false signal strength: -79.0 dB", hmsMsg.toString());
//H DC87 0300 0000F6
//H DC87 0300 0100F6
//H DC87 0300 0500E6 21.16h
//H DC87 0303 0100F2 Feuer, bat low
        decode("HDC8703030100F2\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: 707D, device type: HMS 100 RM status: [Batt low ] smoke: false signal strength: -81.0 dB", hmsMsg.toString());
//H DC87 0303 0200F4
//H DC87 0303 0400F4 bis auf 2.8V runtergeregelt
//H DC87 0303 0500F3 runter auf 2.5V
//bei 2.1V Sendeabbruch, aber Device ID nicht verloren!
//H DC87 0303 0600F7 wieder auf 4.5 Volt hochgeregelt.
//H DC87 2300 0500F6 kein Feuer, jetzt erst Bat empty
        decode("HDC8723000500F6\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: C25C, device type: HMS 100 TF temp: 20.9, humidy: 61.8 signal strength: -66.5 dB", hmsMsg.toString());
//H DC87 2303 0200F8 Feuer
        decode("HDC8723030200F8\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: C25C, device type: HMS 100 TF temp: 20.9, humidy: 61.8 signal strength: -66.5 dB", hmsMsg.toString());
//H DC87 2300 0500F6 kein Feuer
        decode("HDC8723000500F6\r\n");
        assertNotNull(hmsMsg);
        assertEquals("housecode: C25C, device type: HMS 100 TF temp: 20.9, humidy: 61.8 signal strength: -66.5 dB", hmsMsg.toString());

        /*
         Byte 9+10 könnte irgendeine Sequence Nr. sein ???

         Ergebnis wäre:
         Byte 1-4=Device ID
         Byte 5 Bit 1 = Battery
         Byte 8 bit 0 = Status

         Gruß
         Klaus
         */
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
    public void fhtMultiMsgParsed(FhtMultiMsgMessage temp) {
        this.fhtMultiMsgMessage = temp;
    }

    @Override
    public void hmsDataParsed(HmsMessage hmsMsg) {
        this.hmsMsg = hmsMsg;
    }

    @Override
    public void fs20DataParsed(FS20Message fs20Msg) {
        this.fs20Msg = fs20Msg;
    }
}
