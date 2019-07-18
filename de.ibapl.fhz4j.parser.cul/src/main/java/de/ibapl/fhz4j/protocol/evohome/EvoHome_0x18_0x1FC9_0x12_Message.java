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
public class EvoHome_0x18_0x1FC9_0x12_Message extends EvoHomeDeviceMessage {
	
	public int unknownFlags1;
	public int unknownDeviceId1;
	public int unknownFlags2;
	public int unknownDeviceId2;
	public int unknownFlags3;
	public int unknownDeviceId3;
	

	public EvoHome_0x18_0x1FC9_0x12_Message() {
		super(EvoHomeProperty._18_1FC9);
	}

	@Override
	protected void addToString(StringBuilder sb) {
		super.addToString(sb);
		sb.append(String.format(", unknownFlags1 : 0x%06x", unknownFlags1));
		sb.append(String.format(", unknownDeviceId1 : 0x%06x", unknownDeviceId1));
		sb.append(String.format(", unknownFlags2 : 0x%06x", unknownFlags2));
		sb.append(String.format(", unknownDeviceId2 : 0x%06x", unknownDeviceId2));
		sb.append(String.format(", unknownFlags3 : 0x%06x", unknownFlags3));
		sb.append(String.format(", unknownDeviceId3 : 0x%06x", unknownDeviceId3));
	}
}
