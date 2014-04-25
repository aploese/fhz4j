package net.sf.fhz4j.hms;

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

import java.util.EnumSet;
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
        Hms100TfMessage instance = new Hms100TfMessage((short)1234, EnumSet.noneOf(HmsDeviceStatus.class));
        instance.setTemp(45);
        instance.setHumidy(22);
        for (HmsProperty prop : HmsDeviceType.HMS_100_TF.getProperties()) {
            instance.toString(prop);
        }
    }
}
