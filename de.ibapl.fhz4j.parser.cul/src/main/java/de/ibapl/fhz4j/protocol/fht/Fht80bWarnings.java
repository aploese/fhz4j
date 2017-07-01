package de.ibapl.fhz4j.protocol.fht;

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
 */
public enum Fht80bWarnings {
    NONE(0, "NONE"),
    BATT_LOW(1, "Batt Low");
    
    
    final private String label;
    final private byte value;

    private Fht80bWarnings(int value, String label) {
        this.label = label;
        this.value = (byte) value;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }

    public final static Fht80bWarnings valueOf(int value) {
        for (Fht80bWarnings prop : values()) {
            if (prop.value == (byte) value) {
                return prop;
            }
        }
        return valueOf(String.valueOf(value));
    }

    public byte getValue() {
        return value;
    }
    
}
