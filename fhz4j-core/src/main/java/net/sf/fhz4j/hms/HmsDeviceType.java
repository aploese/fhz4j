/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

/**
 *
 * @author aploese
 */
public enum HmsDeviceType {
    HMS_100_TF(0x00, "HMS 100 TF"),
    HMS_100_WD(0x02, "HMS 100 WD"),
    HMS_100_RM(0x03, "HMS 100 RM"),
    HMS_100_TFK(0x04, "HMS 100 TFK");
    
    private final byte value;
    private final String label;

    private HmsDeviceType(int value, String label) {
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

}
