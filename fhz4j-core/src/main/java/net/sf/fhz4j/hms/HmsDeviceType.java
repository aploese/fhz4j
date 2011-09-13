/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

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
