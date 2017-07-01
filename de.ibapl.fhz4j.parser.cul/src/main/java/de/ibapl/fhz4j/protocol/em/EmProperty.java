package de.ibapl.fhz4j.em;

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
import de.ibapl.fhz4j.scada.DataType;
import static de.ibapl.fhz4j.scada.DataType.FLOAT;
import de.ibapl.fhz4j.scada.ScadaProperty;

/**
 *
 * @author aploese
 */
public enum EmProperty implements ScadaProperty, Serializable {

    ELECTRICAL_ENERGY("energy", "kWh", FLOAT),
    ELECTRICAL_ENERGY_LAST_5_MIN("energy last 5 min", "kWh", FLOAT),
    ELECTRICAL_POWER_LAST_5_MIN_MAX("max power last 5 min", "kW", FLOAT);

    final private String label;
    final private DataType dataType;
    final private String unitOfMeasurement;

    private EmProperty(String label, String unitOfMeasurement, DataType dataType) {
        this.label = label;
        this.unitOfMeasurement = unitOfMeasurement;
        this.dataType = dataType;
    }

    /**
     * @return the label
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * @return the dataType
     */
    @Override
    public DataType getDataType() {
        return dataType;
    }

    /**
     * @return the unitOfMeasurement
     */
    @Override
    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    @Override
    public String getName() {
        return name();
    }

}
