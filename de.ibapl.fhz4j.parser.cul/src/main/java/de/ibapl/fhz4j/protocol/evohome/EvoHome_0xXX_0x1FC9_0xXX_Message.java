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

import java.util.List;
import java.util.Objects;

/**
 * TODO Maybe equal to EvoHome_0x2C_0x1FC9_0x06_Message
 *
 *
 * @author Arne Plöse
 */
public class EvoHome_0xXX_0x1FC9_0xXX_Message extends EvoHomeDeviceMessage {

	public static class Data {
		public byte zoneId;
		public short command;
		public int deviceId;

		public Data() {
			
		}
		
		public Data(byte zoneId, short command, int deviceId) {
			this.zoneId = zoneId;
			this.command = command;
			this.deviceId = deviceId;
		}

		public String toString() {
			return String.format("{zoneId : 0x%02x, command : 0x%04x, deviceId : 0x%06x}", zoneId, command, deviceId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(command, deviceId, zoneId);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Data other = (Data) obj;
			return command == other.command && deviceId == other.deviceId && zoneId == other.zoneId;
		}
		
		
	}

	public List<Data> elements;

	public EvoHome_0xXX_0x1FC9_0xXX_Message(EvoHomeProperty evoHomeProperty) {
		super(evoHomeProperty);
	}

	@Override
	protected void addToString(StringBuilder sb) {
		super.addToString(sb);
		sb.append(", elements : [");
		boolean firstRun = true;
		for (Data d : elements) {
			if (firstRun) {
				firstRun = false;
			} else {
				sb.append(", ");
			}
			sb.append(d);
		}
		sb.append("]");
	}
}
