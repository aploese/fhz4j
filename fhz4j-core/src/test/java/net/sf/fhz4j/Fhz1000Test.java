/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.fhz4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class Fhz1000Test {

    public Fhz1000Test() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testHouseCodeToStr() {
        assertEquals("6794", Fhz1000.houseCodeToString((short)0x435E));
        assertEquals("0000", Fhz1000.houseCodeToString((short)0x0000));
        assertEquals("0001", Fhz1000.houseCodeToString((short)0x0001));
    }

    @Test
    public void testparseHouseCode() {
        assertEquals(0x435E, Fhz1000.parseHouseCode("6794"));
        assertEquals(0x0001, Fhz1000.parseHouseCode("1"));
    }
}