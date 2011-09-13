/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

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
public class Hms100WdMessageTest {
    
    public Hms100WdMessageTest() {
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
     * Test get all props.
     */
    @Test
    public void testGetProps() {
        System.out.println("get props");
        HmsMessage msg = new HmsMessage();
        msg.setDeviceType(HmsDeviceType.HMS_100_WD);
        msg.setHousecode((short)1234);
        msg.setRawValue("12345678");
        Hms100WdMessage instance = new Hms100WdMessage(new HmsMessage());
        instance.setWater(true);
        for (HmsProperty prop : HmsDeviceType.HMS_100_WD.getHmsProperties()) {
                    instance.toString(prop);
        }
    }

}
