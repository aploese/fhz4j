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
