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

import java.time.LocalDateTime;

/**
 * 7 BYTES TEMP 1(3) TEM2 (3) and flags (1)
 *
 * @author Arne Plöse
 */
public class EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message extends EvoHomeDeviceMessage {
	
	public float temperature;
	public int unknown;
	public LocalDateTime until;

	public EvoHome_0x18_0x2349_0x0D_ZONE_SETPOINT_UNTIL_Message() {
		super(EvoHomeProperty.ZONE_SETPOINT_UNTIL);
	}
	
	@Override
	protected void addToString(StringBuilder sb) {
		super.addToString(sb);
		sb.append(", temperature:").append(temperature);
		sb.append(String.format(", unknown: 0x%08x", unknown));
		sb.append(", until:").append(until);
	}
}
