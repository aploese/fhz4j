/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j;

import java.io.Serializable;
import static net.sf.fhz4j.FhzProtocol.*;

/**
 *
 * @author aploese
 */
public enum FhzDeviceTypes implements Serializable {

    FHT_8("FHT 8", FHT),
    FHT_80B("FHT 80B", FHT),
    UNKNOWN("Unknown", FhzProtocol.UNKNOWN);

    public static FhzDeviceTypes fromLabel(String label) {
        for (FhzDeviceTypes deviceType : values()) {
            if (deviceType.getLabel().equals(label)) {
                return deviceType;
            }
        }
        return valueOf(label);
    }
    private final String label;
    private final FhzProtocol protocol;

    private FhzDeviceTypes(String label, FhzProtocol protocol) {
        this.label = label;
        this.protocol = protocol;
    }

    public String getLabel() {
        return label;
    }

    public FhzProtocol getProtocol() {
        return protocol;
    }

    public String getName() {
        return name();
    }
}
