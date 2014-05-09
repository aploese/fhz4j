package net.sf.fhz4j.scada;

/*
 * #%L
 * fhz4j Core
 * %%
 * Copyright (C) 2009 - 2014 fhz4j
 * %%
 * fhz4j - Drivers for the Wireless FS20, FHT and HMS protocol http://fhz4j.sourceforge.net/
 * Copyright (C) 2009-2014, fhz4j.sf.net, and individual contributors as indicated
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

import java.util.Set;

/**
 *
 * @author aploese
 * @param <T>
 */
public interface ScadaValueAccessor<T extends ScadaProperty> {

    double getDouble(T property);
    
    void setDouble(T property, double value);
    
    float getFloat(T property);
    
    void setFloat(T property, float value);
    
    long getLong(T property);

    void setLong(T property, long value);
    
    int getInt(T property);

    void setInt(T property, int value);
    
    short getShort(T property);
    
    void setShort(T property, short value);

    byte getByte(T property);
    
    void setByte(T property, byte value);

    boolean getBoolean(T property);
    
    void setBoolean(T property, boolean value);

    char getChar(T property);
    
    void setChar(T property, char value);

    String getString(T property);
    
    String setString(T property, String value);

    Time getTime(T property);
    
    void setTime(T property, Time value);

    Date getDate(T property);
    
    void setDate(T property, Date value);

    Timestamp getTimestamp(T property);
    
    Timestamp setTimestamp(T property, Timestamp value);

    String toString(T property);

    Set<T> getSupportedProperties();
}
