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

import java.util.Set;
import de.ibapl.fhz4j.FhzMessage;

/**
 *
 * @author aploese
 */
public abstract class EmMessage extends FhzMessage<EmProperty> {

    private short address;
    private short counter;

    @Override
    protected void toString(StringBuilder sb) {
        sb.append("type: ").append(getType().getLabel());
        sb.append(", address: ").append(address);
        sb.append(", counter: ").append(counter);
    }

    /**
     * @return the type
     */
    public abstract EmDeviceType getType();

    /**
     * @return the address
     */
    public short getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(short address) {
        this.address = address;
    }

    /**
     * @return the counter
     */
    public short getCounter() {
        return counter;
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(short counter) {
        this.counter = counter;
    }

    /**
     * 
     * @param cummulatedValue the raw value from parser
     */
    abstract void setCumulatedValue(int cummulatedValue);

    /**
     * 
     * @param lastValue the raw value from parser
     */
    abstract void setLastValue(int lastValue);

    /**
     * 
     * @param maxLastValue the raw value from parser
     */
    abstract void setMaxLastValue(int maxLastValue);

    @Override
    public Set<EmProperty> getSupportedProperties() {
        return getType().getProperties(); 
    }

}
