package de.ibapl.fhz4j.protocol.em;

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


import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author aploese
 */
public enum EmDeviceType {
    EM_1000S("EM 1000s"),
    EM_1000_EM("EM 1000 EM", EmProperty.ELECTRICAL_ENERGY, EmProperty.ELECTRICAL_ENERGY_LAST_5_MIN, EmProperty.ELECTRICAL_POWER_LAST_5_MIN_MAX),
    EM_1000_GZ("EM 1000-GZ");
    
    final String label;
    final Set<EmProperty> emProperties;
    
    private EmDeviceType(String label, EmProperty ... emProperties) {
        this.label = label;
        if (emProperties.length == 0) {
            this.emProperties = EnumSet.noneOf(EmProperty.class);
        }else {
            this.emProperties = EnumSet.copyOf(Arrays.asList(emProperties));
        }
    }

    public String getLabel() {
        return label;
    }

    public Set<EmProperty> getProperties() {
        return emProperties;
    }
    
    
}
