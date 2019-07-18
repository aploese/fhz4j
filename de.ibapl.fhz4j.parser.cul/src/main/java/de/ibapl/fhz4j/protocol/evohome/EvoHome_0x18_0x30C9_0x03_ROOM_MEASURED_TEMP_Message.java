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
 * Room Measured Temp
 * @author Arne Plöse
 */
public class EvoHome_0x18_0x30C9_0x03_ROOM_MEASURED_TEMP_Message extends EvoHomeDeviceMessage {
	
	public byte zone;
	public float temperature;

	public EvoHome_0x18_0x30C9_0x03_ROOM_MEASURED_TEMP_Message() {
		super(EvoHomeProperty.ROOM_MEASURED_TEMP);
	}
	
	@Override
	protected void addToString(StringBuilder sb) {
		super.addToString(sb);
		sb.append(String.format(", zone: 0x%02x", zone));
		sb.append(", temperature:").append(temperature);
	}
}
