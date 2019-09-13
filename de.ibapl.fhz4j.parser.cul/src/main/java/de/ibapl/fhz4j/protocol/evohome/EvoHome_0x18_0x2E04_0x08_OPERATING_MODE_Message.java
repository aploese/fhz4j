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
public class EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message extends EvoHomeDeviceMessage {
	
	public static enum Mode {
		NORMAL,
		HEATING_OFF,
		ECONOMY,
		AWAY,
		EXCEPTION_DAY,
		SPECIAL_PROGRAMME;
	}
	
	public Mode mode;
	public final byte[] value = new byte[0x08 - 1];

	public EvoHome_0x18_0x2E04_0x08_OPERATING_MODE_Message() {
		super(EvoHomeProperty._18_2E04_OPERATING_MODE);
	}

	@Override
	protected void addToJsonString(StringBuilder sb) {
		super.addToJsonString(sb);
		sb.append(", mode : ").append(mode);
		appendByteArray(sb, "value", value);
	}
}
