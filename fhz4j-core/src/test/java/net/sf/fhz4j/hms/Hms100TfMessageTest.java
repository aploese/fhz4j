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
public class Hms100TfMessageTest {

    public Hms100TfMessageTest() {
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
        msg.setDeviceType(HmsDeviceType.HMS_100_TF);
        msg.setHousecode((short) 1234);
        msg.setRawValue("12345678");
        Hms100TfMessage instance = new Hms100TfMessage(new HmsMessage());
        instance.setTemp(45);
        instance.setHumidy(22);
        for (HmsProperty prop : HmsDeviceType.HMS_100_TF.getHmsProperties()) {
            instance.toString(prop);
        }
    }
}
