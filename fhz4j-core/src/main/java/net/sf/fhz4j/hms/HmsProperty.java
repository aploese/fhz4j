/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

import java.io.Serializable;
import net.sf.fhz4j.scada.DataType;
import net.sf.fhz4j.scada.ScadaProperty;
import static net.sf.fhz4j.scada.DataType.*;

/**
 *
 * @author aploese
 */
public enum HmsProperty implements Serializable, ScadaProperty {
        TEMP("temp", "°C", DOUBLE),
        HUMIDY("humidy", "%", DOUBLE),
        BATT_STATUS("batt low", "", BOOLEAN),
        WATER("water", "", BOOLEAN),
        SMOKE_ALERT("smoke", "", BOOLEAN),
        DOOR_WINDOW_OPEN("window open", "", BOOLEAN),
        RAW_VALUE("raw value", "", STRING);
        
        final private String label;
        final private DataType dataType;
        final private String unitOfMeasurement; 
        
        private HmsProperty(String label, String unitOfMeasurement, DataType dataType) {
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
