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
public class Hms100RmMessageTest {
    
    public Hms100RmMessageTest() {
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
        msg.setDeviceType(HmsDeviceType.HMS_100_RM);
        msg.setHousecode((short)1234);
        msg.setRawValue("12345678");
        Hms100RmMessage instance = new Hms100RmMessage(new HmsMessage());
        instance.setSmoke(true);
        for (HmsProperty prop : HmsDeviceType.HMS_100_RM.getProperties()) {
            switch (prop.getDataType()) {
                case BOOLEAN:
                    instance.getBoolean(prop);
                    break;
                case DOUBLE:
                    instance.getDouble(prop);
                    break;
                case STRING:
                    instance.getString(prop);
                    break;
            }
        }
    }

}
