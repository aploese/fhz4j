/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2019, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.evohome;

/**
 *
 * @author Arne Plöse
 */
public class EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message extends EvoHomeDeviceMessage {
	
	public byte zone;
	/**
	 * Heatdemand or position ???
	 */
	public short heatDemand;

	public EvoHome_0x18_0x3150_0x02_HEAT_DEMAND_Message() {
		super(EvoHomeProperty._18_3150_HEAT_DEMAND);
	}
	
	@Override
	protected void addToString(StringBuilder sb) {
		super.addToString(sb);
		sb.append(", zone:");
		sb.append(String.format("0x%02x", zone));
		sb.append(", heatDemand:");
		sb.append(heatDemand);
		sb.append(", valvePosition:");
		sb.append(calcValvePosition());
		sb.append("%");
	}
	
	/**
	 * from 0  to 60 => 0%
	 * from 60 to 140 => 0% to 30%
	 * from 140 to 200 => 30% to 100%
	 * @return
	 */
	public float calcValvePosition() {
		if (heatDemand < 0) {
			//TODO Error
			throw new RuntimeException("heatDemand must not be negative");
		} else if (heatDemand < 60) {
			return 0.0f;
		} else if (heatDemand < 140) {
			return 30.0f * (heatDemand - 60) / 80.0f;
		} else if (heatDemand < 200) {
			return 30.0f + 70.0f * (heatDemand - 140) / 60.0f;
		} else {
			//TODO error
			throw new RuntimeException("heatDemand must not be greater 200");
		}
	}
}
