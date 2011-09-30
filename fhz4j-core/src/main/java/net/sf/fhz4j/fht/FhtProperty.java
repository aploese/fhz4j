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

import net.sf.fhz4j.scada.DataType;
import net.sf.fhz4j.scada.ScadaProperty;
import static net.sf.fhz4j.fht.FhtDeviceTypes.*;
import static net.sf.fhz4j.scada.DataType.*;

/**
 *
 * @author aploese
 */
public enum FhtProperty implements Serializable, ScadaProperty {

    VALVE(0x00, "%", DOUBLE, FHT_8, FHT_80B),
    VALVE_1(0x01, "%", DOUBLE, FHT_8, FHT_80B),
    VALVE_2(0x02, "%", DOUBLE, FHT_8, FHT_80B),
    VALVE_3(0x03, "%", DOUBLE, FHT_8, FHT_80B),
    VALVE_4(0x04, "%", DOUBLE, FHT_8, FHT_80B),
    VALVE_5(0x05, "%", DOUBLE, FHT_8, FHT_80B),
    VALVE_6(0x06, "%", DOUBLE, FHT_8, FHT_80B),
    VALVE_7(0x07, "%", DOUBLE, FHT_8, FHT_80B),
    VALVE_8(0x08, "%", DOUBLE, FHT_8, FHT_80B),
    MO_FROM_1(0x14, "", TIME, FHT_80B),
    MO_TO_1(0x15, "", TIME, FHT_80B),
    MO_FROM_2(0x16, "", TIME, FHT_80B),
    MO_TO_2(0x17, "", TIME, FHT_80B),
    TUE_FROM_1(0x18, "", TIME, FHT_80B),
    TUE_TO_1(0x19, "", TIME, FHT_80B),
    TUE_FROM_2(0x1a, "", TIME, FHT_80B),
    TUE_TO_2(0x1b, "", TIME, FHT_80B),
    WED_FROM_1(0x1c, "", TIME, FHT_80B),
    WED_TO_1(0x1d, "", TIME, FHT_80B),
    WED_FROM_2(0x1e, "", TIME, FHT_80B),
    WED_TO_2(0x1f, "", TIME, FHT_80B),
    THU_FROM_1(0x20, "", TIME, FHT_80B),
    THU_TO_1(0x21, "", TIME, FHT_80B),
    THU_FROM_2(0x22, "", TIME, FHT_80B),
    THU_TO_2(0x23, "", TIME, FHT_80B),
    FRI_FROM_1(0x24, "", TIME, FHT_80B),
    FRI_TO_1(0x25, "", TIME, FHT_80B),
    FRI_FROM_2(0x26, "", TIME, FHT_80B),
    FRI_TO_2(0x27, "", TIME, FHT_80B),
    SAT_FROM_1(0x28, "", TIME, FHT_80B),
    SAT_TO_1(0x29, "", TIME, FHT_80B),
    SAT_FROM_2(0x2a, "", TIME, FHT_80B),
    SAT_TO_2(0x2b, "", TIME, FHT_80B),
    SUN_FROM_1(0x2c, "", TIME, FHT_80B),
    SUN_TO_1(0x2d, "", TIME, FHT_80B),
    SUN_FROM_2(0x2e, "", TIME, FHT_80B),
    SUN_TO_2(0x2f, "", TIME, FHT_80B),
    MODE(0x3e, "", SHORT, FHT_80B),
    HOLIDAY_1(0x3f, "", SHORT, FHT_80B), //# Not verified
    HOLIDAY_2(0x40, "", SHORT, FHT_80B), //# Not verified
    DESIRED_TEMP(0x41, "°C", DOUBLE, FHT_80B),
    //  (0xXX, "measured-temp"),		# sum of next. two, never really sent
    MEASURED_LOW(0x42, "°C", DOUBLE, FHT_80B),
    MEASURED_HIGH(0x43, "°C", DOUBLE, FHT_80B),
    WARNINGS(0x44, "", BYTE, FHT_80B),
    MANU_TEMP(0x45, "°C", DOUBLE, FHT_80B), //# No clue what it does.
    ACK(0x4b, "", SHORT, FHT_80B),
    CAN_CMIT(0x53, "", SHORT, FHT_80B),
    CAN_RCV(0x54, "", SHORT, FHT_80B),
    YEAR(0x60, "", SHORT, FHT_80B),
    MONTH(0x61, "", SHORT, FHT_80B),
    DAY(0x62, "", SHORT, FHT_80B),
    HOUR(0x63, "", SHORT, FHT_80B),
    MINUTE(0x64, "", SHORT, FHT_80B),
    REPORT_1(0x65, "", SHORT, FHT_80B),
    REPORT_2(0x66, "", SHORT, FHT_80B),
    ACK_2(0x69, "", SHORT, FHT_80B),
    START_XMIT(0x7d, "", SHORT, FHT_80B),
    END_XMIT(0x7e, "", SHORT, FHT_80B),
    DAY_TEMP(0x82, "", DOUBLE, FHT_80B),
    NIGHT_TEMP(0x84, "", DOUBLE, FHT_80B),
    LOW_TEMP_OFFSET(0x85, "", DOUBLE, FHT_80B), //# Alarm-Temp.-Differenz
    WINDOW_OPEN_TEMP(0x8a, "", DOUBLE, FHT_80B),
    UNKNOWN_0XFF(0xff, "", BYTE, FHT_80B),
    UNKNOWN(0xFF, "unknown", BYTE);
    
    private final byte value;
    private final String unitOfmeasurement;
    private final Set<FhtDeviceTypes> supportedBy;
    private final DataType dataType;
        
    private FhtProperty(int value, String unitOfMeasurement, DataType dataType, FhtDeviceTypes... supportedBy) {
        this.value = (byte) value;
        this.unitOfmeasurement = unitOfMeasurement;
        this.dataType = dataType;
        if (supportedBy.length > 0) {
            this.supportedBy = EnumSet.copyOf(Arrays.asList(supportedBy));
        } else {
            this.supportedBy = EnumSet.noneOf(FhtDeviceTypes.class);
        }
    }

    public static FhtProperty valueOf(int value) {
        for (FhtProperty prop : values()) {
            if (prop.value == (byte)value) {
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

    @Override
    public String getUnitOfMeasurement() {
        return unitOfmeasurement;
    }

    public FhtDeviceTypes[] getSupportedBy() {
        if (supportedBy != null) {
            return supportedBy.toArray(new FhtDeviceTypes[supportedBy.size()]);
        } else {
            return new FhtDeviceTypes[0];
        }
    }

    public static FhtProperty[] getFhtPropertiesOf(FhtDeviceTypes type) {
        List<FhtProperty> result = new ArrayList<FhtProperty>();
        for (FhtProperty prop : values()) {
            if (prop.supportedBy.contains(type)) {
                result.add(prop);
            }
        }
        return result.toArray(new FhtProperty[result.size()]);
    }

    public static FhtProperty fromLabel(FhtDeviceTypes deviceType, String propertyLabel) {
        switch (deviceType) {
            case FHT_80B:
            case FHT_8:
                return FhtProperty.fromLabel(propertyLabel);
            default:
                throw new RuntimeException(String.format("Dont know to hande property %s of : %s", propertyLabel, deviceType));
        }
    }

    public static String[] getFhtPropertyLabelsOf(FhtProperty... fhtProperties) {
        String[] result = new String[fhtProperties.length];
        for (int i = 0; i < fhtProperties.length; i++) {
            result[i] = fhtProperties[i].getLabel();
        }
        return result;
    }

    public static String[] getFhtPropertyLabelsOf(FhtDeviceTypes type) {
        List<String> result = new ArrayList<String>();
        for (FhtProperty prop : values()) {
            if (prop.supportedBy.contains(type)) {
                result.add(prop.getLabel());
            }
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * @return the dataType
     */
    @Override
    public DataType getDataType() {
        return dataType;
    }

}
