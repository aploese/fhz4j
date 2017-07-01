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


import java.util.ArrayList;
import java.util.List;
import de.ibapl.fhz4j.scada.DataType;
import de.ibapl.fhz4j.scada.ScadaProperty;

/**
 * For values that dont fit into a singel Msg
 * @author aploese
 */
public enum FhtMultiMsgProperty implements ScadaProperty {
    
    TEMP("°C", DataType.FLOAT),
    HOLIDAY_END("", DataType.DATE),
    PARTY_END("", DataType.TIME_STAMP);
    
    private final String unitOfmeasurement;
    private final DataType dataType;
        
    private FhtMultiMsgProperty(String unitOfMeasurement, DataType dataType) {
        this.unitOfmeasurement = unitOfMeasurement;
        this.dataType = dataType;
    }

    public static FhtMultiMsgProperty fromLabel(String label) {
        for (FhtMultiMsgProperty prop : values()) {
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

    public static String[] getFhtMultiMsgPropertyLabelsOf(FhtMultiMsgProperty... fhtMultiProperties) {
        String[] result = new String[fhtMultiProperties.length];
        for (int i = 0; i < fhtMultiProperties.length; i++) {
            result[i] = fhtMultiProperties[i].getLabel();
        }
        return result;
    }

    public static String[] getFhtMultiMagPropertyLabelsOf() {
        List<String> result = new ArrayList<>();
        for (FhtMultiMsgProperty prop : values()) {
                result.add(prop.getLabel());
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
