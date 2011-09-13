/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.fhz4j.fht;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class FhtPropertyTest {

    public FhtPropertyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of getSupportedBy method, of class FhtProperty.
     */
    @Test
    public void testGetSupportedBy() {
        System.out.println("getSupportedBy");
        FhtDeviceTypes[] result = FhtProperty.UNKNOWN.getSupportedBy();
        assertArrayEquals(new FhtDeviceTypes[0], result);
    }

    /**
     * Test of getFhzPropertiesOf method, of class FhtProperty.
     */
    @Test
    public void testGetFhzPropertiesOf() {
        System.out.println("getFhzPropertiesOf");
        FhtDeviceTypes type = FhtDeviceTypes.UNKNOWN;
        FhtProperty[] expResult = new FhtProperty[0];
        FhtProperty[] result = FhtProperty.getFhtPropertiesOf(type);
        assertArrayEquals(expResult, result);
    }

}