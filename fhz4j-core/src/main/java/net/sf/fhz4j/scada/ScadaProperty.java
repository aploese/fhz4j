/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.scada;

import java.io.Serializable;

/**
 *
 * @author aploese
 */
public interface ScadaProperty extends Serializable {
    
    String getUnitOfMeasurement();
    
    DataType getDataType();
    
    String getName();
    
    String getLabel();
    
}
