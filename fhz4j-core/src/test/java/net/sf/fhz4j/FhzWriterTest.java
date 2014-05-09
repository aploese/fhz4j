/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.fhz4j;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import net.sf.fhz4j.fht.FhtMessage;
import net.sf.fhz4j.fht.FhtProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author aploese
 */
public class FhzWriterTest {
    
    public FhzWriterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of initFhz method, of class FhzWriter.
     */
    @Test
    @Ignore
    public void testInitFhz_short() throws Exception {
        System.out.println("initFhz");
        short fhzHousecode = 0;
        FhzWriter instance = new FhzWriter();
        instance.initFhz(fhzHousecode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initFhz method, of class FhzWriter.
     */
    @Test
    @Ignore
    public void testInitFhz_short_byte() throws Exception {
        System.out.println("initFhz");
        short fhz100Housecode = 0;
        byte initFlags = 0;
        FhzWriter instance = new FhzWriter();
        instance.initFhz(fhz100Housecode, initFlags);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initFhtReporting method, of class FhzWriter.
     */
    @Test
    @Ignore
    public void testInitFhtReporting_Iterable() throws Exception {
        System.out.println("initFhtReporting");
        Iterable<Short> fhtDeviceHomeCodes = null;
        FhzWriter instance = new FhzWriter();
        instance.initFhtReporting(fhtDeviceHomeCodes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of syncFhtClocks method, of class FhzWriter.
     */
    @Test
    @Ignore
    public void testSyncFhtClocks() throws Exception {
        System.out.println("syncFhtClocks");
        Iterable<Short> fhtDeviceHomeCodes = null;
        FhzWriter instance = new FhzWriter();
        instance.syncFhtClocks(fhtDeviceHomeCodes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initFhtReporting method, of class FhzWriter.
     */
    @Test
    @Ignore
    public void testInitFhtReporting_ShortArr() throws Exception {
        System.out.println("initFhtReporting");
        Short[] fhtDeviceHomeCodes = null;
        FhzWriter instance = new FhzWriter();
        instance.initFhtReporting(fhtDeviceHomeCodes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeFhtMsg method, of class FhzWriter.
     */
    @Test
    public void testWriteFhtMsg() throws Exception {
        System.out.println("writeFhtMsg");
        FhtMessage message = new FhtMessage();
        message.setFloat(FhtProperty.DESIRED_TEMP, 24.0f);
        message.setHousecode(Fhz1000.parseHouseCode("9752"));
        FhzWriter instance = new FhzWriter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        instance.setOutputStream(os);
        instance.writeFhtMsg(message);
        assertArrayEquals("T61344130\n".getBytes(), os.toByteArray());
    }
    
}
