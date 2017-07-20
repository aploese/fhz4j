package de.ibapl.fhz4j.protocol.fs20;

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
public enum FS20CommandValue implements Serializable {

    OFF("off"),
    DIM_6_PERCENT("dim06%"),
    DIM_12_PERCENT("dim12%"),
    DIM_18_PERCENT("dim18%"),
    DIM_25_PERCENT("dim25%"),
    DIM_31_PERCENT("dim31%"),
    DIM_37_PERCENT("dim37%"),
    DIM_43_PERCENT("dim43%"),
    DIM_50_PERCENT("dim50%"),
    DIM_56_PERCENT("dim56%"),
    DIM_62_PERCENT("dim62%"),
    DIM_68_PERCENT("dim68%"),
    DIM_75_PERCENT("dim75%"),
    DIM_81_PERCENT("dim81%"),
    DIM_87_PERCENT("dim87%"),
    DIM_93_PERCENT("dim93%"),
    DIM_100_PERCENT("dim100%"),
    ON("on"), // Set to previous dim value (before switching it off)
    TOGGLE("toggle"), // between off and previous dim val
    DIM_UP("dimup"),
    DIM_DOWN("dimdown"),
    DIM_UP_DOWN("dimupdown"),
    TIMER("timer"),
    SENDSATE("sendstate"),
    OFF_FOR_TIMER("off-for-timer"),
    ON_FOR_TIMER("on-for-timer"),
    ON_OLD_FOR_TIMER("on-old-for-timer"),
    RESET("reset"),
    RAMP_ON_TIME("ramp-on-time"), //time to reach the desired dim value on dimmers
    RAMP_OFF_TIME("ramp-off-time"), //time to reach the off state on dimmers
    ON_OLD_FOR_TIMER_PREV("on-old-for-timer-prev"), // old val for timer, then go to prev. state
    ON_100_FOR_TIMER_PREV("on-100-for-timer-prev"); // 100% for timer, then go to previous state

    final private String label;

    private FS20CommandValue(String label) {
        this.label = label;
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

}
