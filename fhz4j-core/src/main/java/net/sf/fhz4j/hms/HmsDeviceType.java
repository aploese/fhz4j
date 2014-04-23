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

import static net.sf.fhz4j.hms.HmsProperty.*;

/**
 *
 * @author aploese
 */
public enum HmsDeviceType {
    HMS_100_TF(0x00, "HMS 100 TF", TEMP, HUMIDY, BATT_STATUS, RAW_VALUE),
    HMS_100_WD(0x02, "HMS 100 WD", WATER, BATT_STATUS, RAW_VALUE),
    HMS_100_RM(0x03, "HMS 100 RM", SMOKE_ALERT, BATT_STATUS, RAW_VALUE),
    HMS_100_TFK(0x04, "HMS 100 TFK", DOOR_WINDOW_OPEN, BATT_STATUS, RAW_VALUE);
    
    private final byte value;
    private final String label;
    private final HmsProperty[] hmsProperties;

    private HmsDeviceType(int value, String label, HmsProperty... hmsProperties) {
        this.value = (byte) value;
        this.label = label;
        this.hmsProperties = hmsProperties;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }

    public byte getValue() {
        return value;
    }

        public static HmsDeviceType valueOf(int value) {
        for (HmsDeviceType prop : values()) {
            if (prop.value == value) {
                return prop;
            }
        }
        return valueOf(String.valueOf(value));
    }

    public static HmsDeviceType fromLabel(String label) {
        for (HmsDeviceType prop : values()) {
            if (prop.getLabel().equals(label)) {
                return prop;
            }
        }
        return valueOf(label);
    }

    public HmsProperty[] getProperties() {
        return hmsProperties;
    }

}
