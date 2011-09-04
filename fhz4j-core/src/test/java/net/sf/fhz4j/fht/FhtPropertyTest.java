/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.fhz4j.fht;

import net.sf.fhz4j.FhzDeviceTypes;
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
        FhzDeviceTypes[] result = FhtProperty.UNKNOWN.getSupportedBy();
        assertArrayEquals(new FhzDeviceTypes[0], result);
    }

    /**
     * Test of getFhzPropertiesOf method, of class FhtProperty.
     */
    @Test
    public void testGetFhzPropertiesOf() {
        System.out.println("getFhzPropertiesOf");
        FhzDeviceTypes type = FhzDeviceTypes.UNKNOWN;
        FhtProperty[] expResult = new FhtProperty[0];
        FhtProperty[] result = FhtProperty.getFhzPropertiesOf(type);
        assertArrayEquals(expResult, result);
    }

}