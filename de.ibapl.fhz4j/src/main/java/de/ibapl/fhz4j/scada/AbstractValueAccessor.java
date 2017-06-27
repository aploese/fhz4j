package de.ibapl.fhz4j.scada;

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


/**
 *
 * @author aploese
 * @param <T>
 */
public abstract class AbstractValueAccessor<T extends ScadaProperty> implements ScadaValueAccessor<T> {
    
    @Override
    public double getDouble(T property) {
        throw new IllegalArgumentException("can't get double of:" + property.getName());
    }

    @Override
    public void setDouble(T property, double value) {
        throw new IllegalArgumentException("can't set double of:" + property.getName());
    }

    @Override
    public float getFloat(T property) {
        throw new IllegalArgumentException("can't get float of:" + property.getName());
    }

    @Override
    public void setFloat(T property, float value) {
        throw new IllegalArgumentException("can't set float of:" + property.getName());
    }

    @Override
    public long getLong(T property) {
        throw new IllegalArgumentException("can't get long of:" + property.getName());
    }

    @Override
    public void setLong(T property, long value) {
        throw new IllegalArgumentException("can't set long of:" + property.getName());
    }

    @Override
    public int getInt(T property) {
        throw new IllegalArgumentException("can't get int of:" + property.getName());
    }

    @Override
    public void setInt(T property, int value) {
        throw new IllegalArgumentException("can't set int of:" + property.getName());
    }

    @Override
    public short getShort(T property) {
        throw new IllegalArgumentException("can't get short of:" + property.getName());
    }

    @Override
    public void setShort(T property, short value) {
        throw new IllegalArgumentException("can't set short of:" + property.getName());
    }

    @Override
    public byte getByte(T property) {
        throw new IllegalArgumentException("can't get byte of:" + property.getName());
    }

    @Override
    public void setByte(T property, byte value) {
        throw new IllegalArgumentException("can't set byte of:" + property.getName());
    }

    @Override
    public boolean getBoolean(T property) {
        throw new IllegalArgumentException("can't get boolean of:" + property.getName());
    }

    @Override
    public void setBoolean(T property, boolean value) {
        throw new IllegalArgumentException("can't set boolean of:" + property.getName());
    }

    @Override
    public char getChar(ScadaProperty property) {
        throw new IllegalArgumentException("can't get char of:" + property.getName());
    }

    @Override
    public void setChar(T property, char value) {
        throw new IllegalArgumentException("can't set char of:" + property.getName());
    }

    @Override
    public String getString(T property) {
        throw new IllegalArgumentException("can't get String of:" + property.getName());
    }

    @Override
    public String setString(T property, String value) {
        throw new IllegalArgumentException("can't set String of:" + property.getName());
    }

    @Override
    public Time getTime(T property) {
        throw new IllegalArgumentException("can't get Time of:" + property.getName());
    }

    @Override
    public void setTime(T property, Time value) {
        throw new IllegalArgumentException("can't set Time of:" + property.getName());
    }

    @Override
    public Date getDate(T property) {
        throw new IllegalArgumentException("can't get Date of:" + property.getName());
    }

    @Override
    public void setDate(T property, Date value) {
        throw new IllegalArgumentException("can't get Date of:" + property.getName());
    }

    @Override
    public Timestamp getTimestamp(T property) {
        throw new UnsupportedOperationException("cant get Timestamp of:" + property.getName());
    }

    @Override
    public Timestamp setTimestamp(T property, Timestamp value) {
        throw new IllegalArgumentException("can't set Timestamp of:" + property.getName());
    }

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

}
