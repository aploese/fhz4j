/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2023-2024, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
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
 */
package de.ibapl.fhz4j.protocol.evohome.messages;

import de.ibapl.fhz4j.protocol.evohome.EvoHomeCommand;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeDeviceMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgParam0;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgType;

/**
 *
 * @author Arne Plöse
 * <a href="https://github.com/zxdavb/ramses_protocol/wiki/3150:-Zone-Heat-Demand">3150:
 * Zone Heat Demand</a>
 */
public class ZoneHeatDemandInformationMessage extends EvoHomeDeviceMessage {

    //TODO array for UFH
    public byte zone_id; //zone_idx or device_id???
    /**
     * Heatdemand or position ???
     */
    public short heatDemand;

    public ZoneHeatDemandInformationMessage(EvoHomeMsgParam0 msgParam0) {
        super(EvoHomeCommand.ZONE_HEAT_DEMAND, EvoHomeMsgType.INFORMATION, msgParam0);
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(String.format(", zone_id : 0x%02x", zone_id));
        sb.append(", heatDemand : ").append(heatDemand);
        sb.append(", valvePosition : ").append(calcValvePosition()).append("%");
    }

    /**
     * from 0 to 60 => 0% from 60 to 140 => 0% to 30% from 140 to 200 => 30% to
     * 100% TODO is this right ?? or simply halv ???
     *
     * @return
     */
    public float calcValvePosition() {
        return (float) heatDemand / 2.0f;
        //TODO THis is in the multi zone controller???
        /*
		if (heatDemand < 0) {
			//TODO Error
			throw new RuntimeException("heatDemand must not be negative");
		} else if (heatDemand < 60) {
			return 0.0f;
		} else if (heatDemand < 140) {
			return 30.0f * (heatDemand - 60) / 80.0f;
		} else if (heatDemand <= 200) {
			return 30.0f + 70.0f * (heatDemand - 140) / 60.0f;
		} else {
			//TODO error
			throw new RuntimeException("heatDemand must not be greater 200");
		}
         */
    }
}
