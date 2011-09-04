/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.fht;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import net.sf.fhz4j.FhzDeviceTypes;
import net.sf.fhz4j.FhzProperty;

import static net.sf.fhz4j.FhzDeviceTypes.*;

/**
 *
 * @author aploese
 */
public enum FhtProperty implements Serializable, FhzProperty {

    VALVE(0x00, "%", FHT_8, FHT_80B),
    VALVE_1(0x01, "%", FHT_8, FHT_80B),
    VALVE_2(0x02, "%", FHT_8, FHT_80B),
    VALVE_3(0x03, "%", FHT_8, FHT_80B),
    VALVE_4(0x04, "%", FHT_8, FHT_80B),
    VALVE_5(0x05, "%", FHT_8, FHT_80B),
    VALVE_6(0x06, "%", FHT_8, FHT_80B),
    VALVE_7(0x07, "%", FHT_8, FHT_80B),
    VALVE_8(0x08, "%", FHT_8, FHT_80B),
    MO_FROM_1(0x14, "", FHT_80B),
    MO_TO_1(0x15, "", FHT_80B),
    MO_FROM_2(0x16, "", FHT_80B),
    MO_TO_2(0x17, "", FHT_80B),
    TUE_FROM_1(0x18, "", FHT_80B),
    TUE_TO_1(0x19, "", FHT_80B),
    TUE_FROM_2(0x1a, "", FHT_80B),
    TUE_TO_2(0x1b, "", FHT_80B),
    WED_FROM_1(0x1c, "", FHT_80B),
    WED_TO_1(0x1d, "", FHT_80B),
    WED_FROM_2(0x1e, "", FHT_80B),
    WED_TO_2(0x1f, "", FHT_80B),
    THU_FROM_1(0x20, "", FHT_80B),
    THU_TO_1(0x21, "", FHT_80B),
    THU_FROM_2(0x22, "", FHT_80B),
    THU_TO_2(0x23, "", FHT_80B),
    FRI_FROM_1(0x24, "", FHT_80B),
    FRI_TO_1(0x25, "", FHT_80B),
    FRI_FROM_2(0x26, "", FHT_80B),
    FRI_TO_2(0x27, "", FHT_80B),
    SAT_FROM_1(0x28, "", FHT_80B),
    SAT_TO_1(0x29, "", FHT_80B),
    SAT_FROM_2(0x2a, "", FHT_80B),
    SAT_TO_2(0x2b, "", FHT_80B),
    SUN_FROM_1(0x2c, "", FHT_80B),
    SUN_TO_1(0x2d, "", FHT_80B),
    SUN_FROM_2(0x2e, "", FHT_80B),
    SUN_TO_2(0x2f, "", FHT_80B),
    MODE(0x3e, "", FHT_80B),
    HOLIDAY_1(0x3f, "", FHT_80B), //# Not verified
    HOLIDAY_2(0x40, "", FHT_80B), //# Not verified
    DESIRED_TEMP(0x41, "°C", FHT_80B),
    //  (0xXX, "measured-temp"),		# sum of next. two, never really sent
    MEASURED_LOW(0x42, "°C", FHT_80B),
    MEASURED_HIGH(0x43, "°C", FHT_80B),
    WARNINGS(0x44, "", FHT_80B),
    MANU_TEMP(0x45, "°C", FHT_80B), //# No clue what it does.
    ACK(0x4b, "", FHT_80B),
    CAN_CMIT(0x53, "", FHT_80B),
    CAN_RCV(0x54, "", FHT_80B),
    YEAR(0x60, "", FHT_80B),
    MONTH(0x61, "", FHT_80B),
    DAY(0x62, "", FHT_80B),
    HOUR(0x63, "", FHT_80B),
    MINUTE(0x64, "", FHT_80B),
    REPORT_1(0x65, "", FHT_80B),
    REPORT_2(0x66, "", FHT_80B),
    ACK_2(0x69, "", FHT_80B),
    START_XMIT(0x7d, "", FHT_80B),
    END_XMIT(0x7e, "", FHT_80B),
    DAY_TEMP(0x82, "", FHT_80B),
    NIGHT_TEMP(0x84, "", FHT_80B),
    LOW_TEMP_OFFSET(0x85, "", FHT_80B), //# Alarm-Temp.-Differenz
    WINDOW_OPEN_TEMP(0x8a, "", FHT_80B),
    UNKNOWN_0XFF(0xff, "", FHT_80B),
    UNKNOWN(0xFF, "unknown");
    private final byte value;
    private final String unitOfmeasurement;
    private final Set<FhzDeviceTypes> supportedBy;

    private FhtProperty(int value, String unitOfMeasurement, FhzDeviceTypes... supportedBy) {
        this.value = (byte) value;
        this.unitOfmeasurement = unitOfMeasurement;
        if (supportedBy.length > 0) {
            this.supportedBy = EnumSet.copyOf(Arrays.asList(supportedBy));
        } else {
            this.supportedBy = EnumSet.noneOf(FhzDeviceTypes.class);
        }
    }

    public static FhtProperty valueOf(int value) {
        for (FhtProperty prop : values()) {
            if (prop.value == value) {
                return prop;
            }
        }
        return valueOf(String.valueOf(value));
    }

    public static FhtProperty fromLabel(String label) {
        for (FhtProperty prop : values()) {
            if (prop.getLabel().equals(label)) {
                return prop;
            }
        }
        return valueOf(label);
    }

    @Override
    public String getLabel() {
        return name().toLowerCase().replaceAll("_", " ");
    }

    @Override
    public String getName() {
        return name();
    }

    public byte getValue() {
        return value;
    }

    public String getUnitOfMeasurement() {
        return unitOfmeasurement;
    }

    public FhzDeviceTypes[] getSupportedBy() {
        if (supportedBy != null) {
            return supportedBy.toArray(new FhzDeviceTypes[supportedBy.size()]);
        } else {
            return new FhzDeviceTypes[0];
        }
    }

    public static FhtProperty[] getFhzPropertiesOf(FhzDeviceTypes type) {
        List<FhtProperty> result = new ArrayList<FhtProperty>();
        for (FhtProperty prop : values()) {
            if (prop.supportedBy.contains(type)) {
                result.add(prop);
            }
        }
        return result.toArray(new FhtProperty[result.size()]);
    }
}
