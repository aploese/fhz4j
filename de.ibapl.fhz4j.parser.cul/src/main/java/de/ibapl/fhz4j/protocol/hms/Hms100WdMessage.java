package de.ibapl.fhz4j.protocol.hms;

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


/**
 *
 * @author aploese
 */
public class Hms100WdMessage extends HmsMessage {
    
    public Hms100WdMessage(short housecode, Set<HmsDeviceStatus> deviceStatus) {
        super(housecode, deviceStatus);
    }
    
    private boolean water;

    @Override
    public void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(" water: ").append(water);
    }

    /**
     * @return the water
     */
    public boolean isWater() {
        return water;
    }

    /**
     * @param water the water to set
     */
    public void setWater(boolean water) {
        this.water = water;
    }

    @Override
    public boolean getBoolean(HmsProperty prop) {
        switch (prop) {
            case WATER:
                return isWater();
            default:
                return super.getBoolean(prop);
        }
    }

    @Override
    public HmsDeviceType getDeviceType() {
        return HmsDeviceType.HMS_100_WD;
    }

}
