package net.sf.fhz4j.hms;

import java.util.EnumSet;
import java.util.Set;
import net.sf.fhz4j.scada.Date;
import net.sf.fhz4j.scada.Time;
import net.sf.fhz4j.scada.Timestamp;

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
public class Hms100WdMessage extends HmsMessage {
    
    Hms100WdMessage(short housecode, Set<HmsDeviceStatus> deviceStatus) {
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
