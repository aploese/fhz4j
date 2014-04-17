/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.fhz4j;

import net.sf.fhz4j.fht.FhtTempMessage;
import net.sf.fhz4j.fht.FhtMessage;
import net.sf.fhz4j.hms.HmsMessage;
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
    private FhtTempMessage temp;
    private HmsMessage hmsMsg;

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
        temp = null;
        hmsMsg = null;
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
    public void decode_FHT_25_9_Degree_Centigrade() {
        decode("T370A42690406\r\n");
        assertNotNull(fhtMessage);
        assertEquals("housecode: 5510, command: measured low, description: from FHT-B data register, value: 0.4°C signal strength: -71.0 dB", fhtMessage.toString());
        decode("T370A43690106\r\n");
        assertNotNull(temp);
        assertEquals("housecode: 5510, measured temperature (combined): 25.9°C signal strength: -71.0 dB and -71.0 dB", temp.toString());
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
    public void fhtDataParsed(FhtMessage fhtMessage) {
        this.fhtMessage =  fhtMessage;
    }

    @Override
    public void fhtCombinedData(FhtTempMessage temp) {
        this.temp = temp;
    }

    @Override
    public void hmsDataParsed(HmsMessage hmsMsg) {
        this.hmsMsg = hmsMsg;
    }
}