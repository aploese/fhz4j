/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
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
 */
package de.ibapl.fhz4j.protocol.evohome;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author aploese
 */
public class DeviceIdTest {

    /**
     * multi zone controller
     *
     * THR99C3100
     *
     */
    final static int x067aec_ATC928G300_1837_4 = 0x067aec;

    /**
     * Radiator Head or Radiator Controller
     *
     * THR092HRT also known as HR92WE, HR92EE, HR92UK
     *
     */
    final static int x110e1b_HR92WE_1803_1_ST3 = 0x110e1b;
    final static int x114977_HR92WE_1807_2_ST3 = 0x114977;
    final static int x131483_HR92WE_1851_7_ST3 = 0x131483;
    final static int x131589_HR92WE_1851_7_ST3 = 0x131589;
    final static int x13159d_HR92WE_1851_7_ST3 = 0x13159d;

    /**
     * Single Zone Thermostat
     *
     * T87RF2059
     */
    final static int x895fab_T87RF2059_1847_2 = 0x895fab;
    final static int x895e5d_T87RF2059_1847_2 = 0x895e5d;

    @Test
    public void testController() {
        assertEquals(DeviceType.MULTI_ZONE_CONTROLLER, new DeviceId(0x067aec).type);
    }

    @Test
    public void testRoomThermostat() {
        assertEquals(DeviceType.SINGLE_ZONE_THERMOSTAT, new DeviceId(0x895fab).type);
        assertEquals(DeviceType.SINGLE_ZONE_THERMOSTAT, new DeviceId(0x895e5d).type);
    }

    @Test
    public void testRadiatorType() {
        assertEquals(DeviceType.RADIATOR_CONTROLLER, new DeviceId(0x110e1b).type);
        assertEquals(DeviceType.RADIATOR_CONTROLLER, new DeviceId(0x131483).type);
    }

    /**
     * Test of toString method, of class DeviceId.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        assertEquals("{id : 0x067aec, type : \"MULTI_ZONE_CONTROLLER\"}", new DeviceId(0x067aec).toString());
        assertEquals("{id : 0x895fab, type : \"SINGLE_ZONE_THERMOSTAT\"}", new DeviceId(0x895fab).toString());
        assertEquals("{id : 0x110e1b, type : \"RADIATOR_CONTROLLER\"}", new DeviceId(0x110e1b).toString());
    }

}
