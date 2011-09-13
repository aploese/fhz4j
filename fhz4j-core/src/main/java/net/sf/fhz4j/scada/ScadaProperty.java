/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.scada;

/**
 *
 * @author aploese
 */
public interface ScadaProperty {
    
    String getUnitOfMeasurement();
    
    DataType getDataType();
    
    String getName();
    
    String getLabel();
    
}
