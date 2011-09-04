/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author aploese
 */
public enum HmsDeviceStatus {
    BATT_LOW(0x02, "Batt low");
    
    private final byte value;
    private final String label;

    private HmsDeviceStatus(int value, String label) {
        this.value = (byte) value;
        this.label = label;
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

        public static Set<HmsDeviceStatus> valueOf(int value) {
        Set<HmsDeviceStatus> result = EnumSet.noneOf(HmsDeviceStatus.class);
            for (HmsDeviceStatus prop : values()) {
            if (prop.value == value) {
                result.add(prop);
            }
        }
        return result;
    }

    public static HmsDeviceStatus fromLabel(String label) {
        for (HmsDeviceStatus prop : values()) {
            if (prop.getLabel().equals(label)) {
                return prop;
            }
        }
        return valueOf(label);
    }

}
