package net.sf.fhz4j;

/*
 * #%L
 * fhz4j Core
 * %%
 * Copyright (C) 2009 - 2014 fhz4j
 * %%
 * fhz4j - Drivers for the Wireless FS20, FHT and HMS protocol http://fhz4j.sourceforge.net/
 * Copyright (C) 2009-2014, fhz4j.sf.net, and individual contributors as indicated
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

        assertEquals("9752", Fhz1000.houseCodeToString((short)0x6134));
        assertEquals(0X6134, Fhz1000.parseHouseCode("9752"));

        assertEquals("1852", Fhz1000.houseCodeToString((short)0x1234));
        assertEquals(0X1234, Fhz1000.parseHouseCode("1852"));
    
    }

    @Test
    public void testparseHouseCode() {
        assertEquals(0x435E, Fhz1000.parseHouseCode("6794"));
        assertEquals(0x0001, Fhz1000.parseHouseCode("1"));
    }
}