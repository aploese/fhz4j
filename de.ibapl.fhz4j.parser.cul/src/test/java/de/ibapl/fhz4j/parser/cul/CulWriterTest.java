package de.ibapl.fhz4j.parser.cul;

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

import de.ibapl.fhz4j.Fhz1000;
import java.io.ByteArrayOutputStream;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
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
public class CulWriterTest {
    
    public CulWriterTest() {
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
        CulWriter instance = new CulWriter();
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
        CulWriter instance = new CulWriter();
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
        CulWriter instance = new CulWriter();
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
        CulWriter instance = new CulWriter();
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
        CulWriter instance = new CulWriter();
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
        CulWriter instance = new CulWriter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        instance.setOutputStream(os);
        instance.writeFhtMsg(message);
        assertArrayEquals("T61344130\n".getBytes(), os.toByteArray());
    }
    
}
