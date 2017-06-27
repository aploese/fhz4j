package de.ibapl.fhz4j.fht;

/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2017 Arne Plöse
 * %%
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009, 2017, Arne Plöse and individual contributors as indicated
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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import de.ibapl.fhz4j.scada.DataType;
import de.ibapl.fhz4j.scada.ScadaProperty;
import static de.ibapl.fhz4j.fht.FhtDeviceType.*;
import static de.ibapl.fhz4j.scada.DataType.*;

/**
 *
 * @author aploese
 */
public enum FhtProperty implements Serializable, ScadaProperty {

    VALVE(0x00, "%", FLOAT, FHT_8V),
    VALVE_1(0x01, "%", FLOAT, FHT_8V),
    VALVE_2(0x02, "%", FLOAT, FHT_8V),
    VALVE_3(0x03, "%", FLOAT, FHT_8V),
    VALVE_4(0x04, "%", FLOAT, FHT_8V),
    VALVE_5(0x05, "%", FLOAT, FHT_8V),
    VALVE_6(0x06, "%", FLOAT, FHT_8V),
    VALVE_7(0x07, "%", FLOAT, FHT_8V),
    VALVE_8(0x08, "%", FLOAT, FHT_8V),
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
    HOLIDAY_1(0x3f, "", SHORT, FHT_80B), //raw value if mode == party the time and if mode == holiday the day of month of end
    HOLIDAY_2(0x40, "", SHORT, FHT_80B), //raw value if mode == party the day of month and if mode == holiday the month of end
    DESIRED_TEMP(0x41, "°C", FLOAT, FHT_80B),
    //  (0xXX, "measured-temp"),		# sum of next. two, never really sent
    MEASURED_LOW(0x42, "°C", FLOAT, FHT_80B),
    MEASURED_HIGH(0x43, "°C", FLOAT, FHT_80B),
    WARNINGS(0x44, "", BYTE, FHT_80B),
    MANU_TEMP(0x45, "°C", FLOAT, FHT_80B), //# No clue what it does.
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
    DAY_TEMP(0x82, "", FLOAT, FHT_80B),
    NIGHT_TEMP(0x84, "", FLOAT, FHT_80B),
    LOW_TEMP_OFFSET(0x85, "", FLOAT, FHT_80B), //# Alarm-Temp.-Differenz
    WINDOW_OPEN_TEMP(0x8a, "", FLOAT, FHT_80B),
    UNKNOWN_0XFF(0xff, "", BYTE, FHT_80B),
    UNKNOWN(0xFF, "unknown", BYTE, FhtDeviceType.UNKNOWN);
    
    private final byte value;
    private final String unitOfmeasurement;
    private final FhtDeviceType targetDevice;
    private final DataType dataType;
        
    private FhtProperty(int value, String unitOfMeasurement, DataType dataType, FhtDeviceType targetDevice) {
        this.value = (byte) value;
        this.unitOfmeasurement = unitOfMeasurement;
        this.dataType = dataType;
        this.targetDevice = targetDevice;
    }

    public final static FhtProperty valueOf(int value) {
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

    public FhtDeviceType getTagetDevice() {
        return targetDevice;
    }

    public static Collection<FhtProperty> getFhtPropertiesOf(FhtDeviceType type) {
        Set<FhtProperty> result = EnumSet.noneOf(FhtProperty.class);
        for (FhtProperty prop : values()) {
            if (type == prop.targetDevice) {
                result.add(prop);
            }
        }
        return result;
    }

    public static FhtProperty fromLabel(FhtDeviceType deviceType, String propertyLabel) {
        switch (deviceType) {
            case FHT_80B:
            case FHT_8V:
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

    public static String[] getFhtPropertyLabelsOf(FhtDeviceType type) {
        List<String> result = new ArrayList<>();
        for (FhtProperty prop : values()) {
            if (type == prop.targetDevice) {
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
