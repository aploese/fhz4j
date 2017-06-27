package de.ibapl.fhz4j.fs20;

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

import java.io.Serializable;

/**
 *
 * @author aploese
 */
public enum FS20CommandValues implements Serializable {

    OFF(0x00, "off"),
    DIM_6_PERCENT(0x01, "dim06%"),
    DIM_12_PERCENT(0x02, "dim12%"),
    DIM_18_PERCENT(0x03, "dim18%"),
    DIM_25_PERCENT(0x04, "dim25%"),
    DIM_31_PERCENT(0x05, "dim31%"),
    DIM_37_PERCENT(0x06, "dim37%"),
    DIM_43_PERCENT(0x07, "dim43%"),
    DIM_50_PERCENT(0x08, "dim50%"),
    DIM_56_PERCENT(0x09, "dim56%"),
    DIM_62_PERCENT(0x0a, "dim62%"),
    DIM_68_PERCENT(0x0b, "dim68%"),
    DIM_75_PERCENT(0x0c, "dim75%"),
    DIM_81_PERCENT(0x0d, "dim81%"),
    DIM_87_PERCENT(0x0e, "dim87%"),
    DIM_93_PERCENT(0x0f, "dim93%"),
    DIM_100_PERCENT(0x10, "dim100%"),
    ON(0x11, "on"), // Set to previous dim value (before switching it off)
    TOGGLE(0x12, "toggle"), // between off and previous dim val
    DIM_UP(0x13, "dimup"),
    DIM_DOWN(0x14, "dimdown"),
    DIM_UP_DOWN(0x15, "dimupdown"),
    TIMER(0x16, "timer"),
    SENDSATE(0x17, "sendstate"),
    OFF_FOR_TIMER(0x18, "off-for-timer"),
    ON_FOR_TIMER(0x19, "on-for-timer"),
    ON_OLD_FOR_TIMER(0x1a, "on-old-for-timer"),
    RESET(0x1b, "reset"),
    RAMP_ON_TIME(0x1c, "ramp-on-time"), //time to reach the desired dim value on dimmers
    RAMP_OFF_TIME(0x1d, "ramp-off-time"), //time to reach the off state on dimmers
    ON_OLD_FOR_TIMER_PREV(0x1e, "on-old-for-timer-prev"), // old val for timer, then go to prev. state
    ON_100_FOR_TIMER_PREV(0x1f, "on-100-for-timer-prev"); // 100% for timer, then go to previous state

    final private String label;
    final private byte value;

    private FS20CommandValues(int value, String label) {
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

    public final static FS20CommandValues valueOf(int value) {
        for (FS20CommandValues prop : values()) {
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
