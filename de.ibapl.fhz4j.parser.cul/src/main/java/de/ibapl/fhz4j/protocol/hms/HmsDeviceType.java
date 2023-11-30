/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2023, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.hms;

import static de.ibapl.fhz4j.protocol.hms.HmsProperty.BATT_STATUS;
import static de.ibapl.fhz4j.protocol.hms.HmsProperty.DOOR_WINDOW_OPEN;
import static de.ibapl.fhz4j.protocol.hms.HmsProperty.HUMIDY;
import static de.ibapl.fhz4j.protocol.hms.HmsProperty.SMOKE_ALERT;
import static de.ibapl.fhz4j.protocol.hms.HmsProperty.TEMP;
import static de.ibapl.fhz4j.protocol.hms.HmsProperty.WATER;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author Arne Plöse
 */
public enum HmsDeviceType {
    HMS_100_TF("HMS 100 TF", TEMP, HUMIDY, BATT_STATUS), HMS_100_WD("HMS 100 WD", WATER, BATT_STATUS), HMS_100_RM(
            "HMS 100 RM", SMOKE_ALERT, BATT_STATUS), HMS_100_TFK("HMS 100 TFK", DOOR_WINDOW_OPEN, BATT_STATUS);

    private final String label;
    private final Set<HmsProperty> hmsProperties;

    private HmsDeviceType(String label, HmsProperty... hmsProperties) {
        this.label = label;
        this.hmsProperties = EnumSet.copyOf(Arrays.asList(hmsProperties));
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }

    public static HmsDeviceType fromLabel(String label) {
        for (HmsDeviceType prop : values()) {
            if (prop.getLabel().equals(label)) {
                return prop;
            }
        }
        return valueOf(label);
    }

    public Set<HmsProperty> getProperties() {
        return hmsProperties;
    }

}
