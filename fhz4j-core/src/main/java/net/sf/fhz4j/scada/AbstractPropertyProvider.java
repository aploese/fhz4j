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

/**
 *
 * @author aploese
 */
public abstract class AbstractPropertyProvider<T extends ScadaProperty> implements ScadaPropertyProvider<T> {
    
    @Override
    public double getDouble(T property) {
        throw new UnsupportedOperationException("cant get double of:" + property.getName());
    }

    @Override
    public float getFloat(T property) {
        throw new UnsupportedOperationException("cant get float of:" + property.getName());
    }

    @Override
    public long getLong(T property) {
        throw new UnsupportedOperationException("cant get long of:" + property.getName());
    }

    @Override
    public int getInt(T property) {
        throw new UnsupportedOperationException("cant get int of:" + property.getName());
    }

    @Override
    public short getShort(T property) {
        throw new UnsupportedOperationException("cant get short of:" + property.getName());
    }

    @Override
    public byte getByte(T property) {
        throw new UnsupportedOperationException("cant get byte of:" + property.getName());
    }

    @Override
    public boolean getBoolean(T property) {
        throw new UnsupportedOperationException("cant get boolean of:" + property.getName());
    }

    @Override
    public char getChar(ScadaProperty property) {
        throw new UnsupportedOperationException("cant get char of:" + property.getName());
    }

    @Override
    public String getString(T property) {
        throw new UnsupportedOperationException("cant get String of:" + property.getName());
    }

    @Override
    public Time getTime(T property) {
        throw new UnsupportedOperationException("cant get Time of:" + property.getName());
    }

    @Override
    public Date getDate(T property) {
        throw new UnsupportedOperationException("cant get Date of:" + property.getName());
    }

    @Override
    public Timestamp getTimestamp(T property) {
        throw new UnsupportedOperationException("cant get Timestamp of:" + property.getName());
    }

    @Override
    public String asString(T property) {
        switch (property.getDataType()) {
            case BOOLEAN:
                return String.valueOf(getBoolean(property));
            case BYTE:
                return String.valueOf(getByte(property));
            case CHAR:
                return String.valueOf(getChar(property));
            case DATE:
                return String.valueOf(getDate(property));
            case DOUBLE:
                return String.valueOf(getDouble(property));
            case FLOAT:
                return String.valueOf(getFloat(property));
            case INT:
                return String.valueOf(getInt(property));
            case LONG:
                return String.valueOf(getLong(property));
            case SHORT:
                return String.valueOf(getShort(property));
            case STRING:
                return String.valueOf(getString(property));
            case TIME:
                return String.valueOf(getTime(property));
            case TIME_STAMP:
                return String.valueOf(getTimestamp(property));
               default: throw new RuntimeException("Cant do toString() " + property); 
        }
        
    }
    @Override
    public String toString(T property) {
          return String.format("%s = %s %s", property.getLabel(), asString(property), property.getUnitOfMeasurement());
    }

    @Override
    public double asDouble(T property) {
        switch (property.getDataType()) {
            case BYTE:
                return getByte(property);
            case DOUBLE:
                return getDouble(property);
            case FLOAT:
                return getFloat(property);
            case INT:
                return getInt(property);
            case LONG:
                return getLong(property);
            case SHORT:
                return getShort(property);
            default: throw new RuntimeException("Cant do adDouble() " + property); 
        }
    }

    @Override
    public long asLong(T property) {
        switch (property.getDataType()) {
            case BYTE:
                return getByte(property);
            case INT:
                return getInt(property);
            case LONG:
                return getLong(property);
            case SHORT:
                return getShort(property);
            default: throw new RuntimeException("Cant do adDouble() " + property); 
        }
    }

    @Override
    public int asInt(T property) {
        switch (property.getDataType()) {
            case BYTE:
                return getByte(property);
            case INT:
                return getInt(property);
            case SHORT:
                return getShort(property);
            default: throw new RuntimeException("Cant do adDouble() " + property); 
        }
    }

    @Override
    public short asShort(T property) {
        switch (property.getDataType()) {
            case BYTE:
                return getByte(property);
            case SHORT:
                return getShort(property);
            default: throw new RuntimeException("Cant do adDouble() " + property); 
        }
    }

}
