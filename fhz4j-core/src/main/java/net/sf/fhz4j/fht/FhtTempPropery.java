/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.fht;

import net.sf.fhz4j.scada.DataType;
import net.sf.fhz4j.scada.ScadaProperty;

/**
 *
 * @author aploese
 */
public class FhtTempPropery implements ScadaProperty {
    
    public final static FhtTempPropery COMBINED_TEMP = new FhtTempPropery();
    
    private FhtTempPropery() {
    }
    
    @Override
    public String getUnitOfMeasurement() {
        return "′C";
    }

    @Override
    public DataType getDataType() {
        return DataType.DOUBLE;
    }

    @Override
    public String getName() {
        return "COMBINED_TEMP";
    }

    @Override
    public String getLabel() {
        return ("actual temperature");
    }

}
