/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.scada;

import java.util.Set;

/**
 *
 * @author aploese
 */
public interface ScadaPropertyProvider<T extends ScadaProperty> {

    double getDouble(T property);

    float getFloat(T property);

    long getLong(T property);

    int getInt(T property);

    short getShort(T property);

    byte getByte(T property);

    boolean getBoolean(T property);

    char getChar(T property);

    String getString(T property);

    Time getTime(T property);

    Date getDate(T property);

    Timestamp getTimestamp(T property);

    String asString(T property);
    
    double asDouble(T property);
    
    long asLong(T property);
    
    int asInt(T property);
    
    short asShort(T property);

    String toString(T property);

    Set<T> getProperties();
}
