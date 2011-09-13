/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.fht;

import java.io.Serializable;
import net.sf.fhz4j.FhzProtocol;
import static net.sf.fhz4j.FhzProtocol.*;

/**
 *
 * @author aploese
 */
public enum FhtDeviceTypes implements Serializable {

    FHT_8("FHT 8", FHT),
    FHT_80B("FHT 80B", FHT),
    UNKNOWN("Unknown", FhzProtocol.UNKNOWN);

    public static FhtDeviceTypes fromLabel(String label) {
        for (FhtDeviceTypes deviceType : values()) {
            if (deviceType.getLabel().equals(label)) {
                return deviceType;
            }
        }
        return valueOf(label);
    }
    private final String label;
    private final FhzProtocol protocol;

    private FhtDeviceTypes(String label, FhzProtocol protocol) {
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
