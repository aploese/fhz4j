package de.ibapl.fhz4j.protocol.fht;

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
import static de.ibapl.fhz4j.protocol.fht.FhtDeviceType.*;
import static de.ibapl.fhz4j.scada.DataType.*;

/**
 *
 * @author aploese
 */
public enum FhtProperty implements Serializable, ScadaProperty {

    VALVE("%", FLOAT, FHT_8V),
    OFFSET_VALVE_1("%", FLOAT, FHT_8V),
    OFFSET_VALVE_2("%", FLOAT, FHT_8V),
    OFFSET_VALVE_3("%", FLOAT, FHT_8V),
    OFFSET_VALVE_4("%", FLOAT, FHT_8V),
    OFFSET_VALVE_5("%", FLOAT, FHT_8V),
    OFFSET_VALVE_6("%", FLOAT, FHT_8V),
    OFFSET_VALVE_7("%", FLOAT, FHT_8V),
    OFFSET_VALVE_8("%", FLOAT, FHT_8V),
    MON_FROM_1("", LOCAL_TIME, FHT_80B),
    MON_TO_1("", LOCAL_TIME, FHT_80B),
    MON_FROM_2("", LOCAL_TIME, FHT_80B),
    MON_TO_2("", LOCAL_TIME, FHT_80B),
    TUE_FROM_1("", LOCAL_TIME, FHT_80B),
    TUE_TO_1("", LOCAL_TIME, FHT_80B),
    TUE_FROM_2("", LOCAL_TIME, FHT_80B),
    TUE_TO_2("", LOCAL_TIME, FHT_80B),
    WED_FROM_1("", LOCAL_TIME, FHT_80B),
    WED_TO_1("", LOCAL_TIME, FHT_80B),
    WED_FROM_2("", LOCAL_TIME, FHT_80B),
    WED_TO_2("", LOCAL_TIME, FHT_80B),
    THU_FROM_1("", LOCAL_TIME, FHT_80B),
    THU_TO_1("", LOCAL_TIME, FHT_80B),
    THU_FROM_2("", LOCAL_TIME, FHT_80B),
    THU_TO_2("", LOCAL_TIME, FHT_80B),
    FRI_FROM_1("", LOCAL_TIME, FHT_80B),
    FRI_TO_1("", LOCAL_TIME, FHT_80B),
    FRI_FROM_2("", LOCAL_TIME, FHT_80B),
    FRI_TO_2("", LOCAL_TIME, FHT_80B),
    SAT_FROM_1("", LOCAL_TIME, FHT_80B),
    SAT_TO_1("", LOCAL_TIME, FHT_80B),
    SAT_FROM_2("", LOCAL_TIME, FHT_80B),
    SAT_TO_2("", LOCAL_TIME, FHT_80B),
    SUN_FROM_1("", LOCAL_TIME, FHT_80B),
    SUN_TO_1("", LOCAL_TIME, FHT_80B),
    SUN_FROM_2("", LOCAL_TIME, FHT_80B),
    SUN_TO_2("", LOCAL_TIME, FHT_80B),
    MODE("", SHORT, FHT_80B),
    HOLIDAY_1("", SHORT, FHT_80B), //raw value if mode == party the time and if mode == holiday the day of month of end
    HOLIDAY_2("", SHORT, FHT_80B), //raw value if mode == party the day of month and if mode == holiday the month of end
    HOLIDAY_END_DATE("", LOCAL_DATE, FHT_80B),
    PARTY_END_TIME("", LOCAL_TIME, FHT_80B),
    DESIRED_TEMP("°C", FLOAT, FHT_80B),
    MEASURED_TEMP("°C", FLOAT, FHT_80B),		 //sum of next. two, never really sent
    MEASURED_LOW("°C", FLOAT, FHT_80B),
    MEASURED_HIGH("°C", FLOAT, FHT_80B),
    WARNINGS("", BYTE, FHT_80B),
    MANU_TEMP("°C", FLOAT, FHT_80B), //# No clue what it does.
    ACK("", SHORT, FHT_80B),
    CAN_CMIT("", SHORT, FHT_80B),
    CAN_RCV("", SHORT, FHT_80B),
    YEAR("", SHORT, FHT_80B),
    MONTH("", SHORT, FHT_80B),
    DAY("", SHORT, FHT_80B),
    HOUR("", SHORT, FHT_80B),
    MINUTE("", SHORT, FHT_80B),
    REPORT_1("", SHORT, FHT_80B),
    REPORT_2("", SHORT, FHT_80B),
    ACK_2("", SHORT, FHT_80B),
    START_XMIT("", SHORT, FHT_80B),
    END_XMIT("", SHORT, FHT_80B),
    DAY_TEMP("", FLOAT, FHT_80B),
    NIGHT_TEMP("", FLOAT, FHT_80B),
    LOW_TEMP_OFFSET("", FLOAT, FHT_80B), //# Alarm-Temp.-Differenz
    WINDOW_OPEN_TEMP("", FLOAT, FHT_80B),
    UNKNOWN_0XFF("", BYTE, FHT_80B),
    UNKNOWN("unknown", BYTE, FhtDeviceType.UNKNOWN);
    
    private final String unitOfmeasurement;
    private final FhtDeviceType targetDevice;
    private final DataType dataType;
        
    private FhtProperty(String unitOfMeasurement, DataType dataType, FhtDeviceType targetDevice) {
        this.unitOfmeasurement = unitOfMeasurement;
        this.dataType = dataType;
        this.targetDevice = targetDevice;
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
