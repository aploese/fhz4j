/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2018 Arne Plöse
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
package de.ibapl.fhz4j.protocol.hms;

import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author Arne Plöse
 */
public enum HmsDeviceStatus {
	BATT_LOW(0x02, "Batt low");

	private final byte value;
	private final String label;

	private HmsDeviceStatus(int value, String label) {
		this.value = (byte) value;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getName() {
		return name();
	}

	public byte getValue() {
		return value;
	}

	public static Set<HmsDeviceStatus> valueOf(int value) {
		Set<HmsDeviceStatus> result = EnumSet.noneOf(HmsDeviceStatus.class);
		for (HmsDeviceStatus prop : values()) {
			if (prop.value == value) {
				result.add(prop);
			}
		}
		return result;
	}

	public static HmsDeviceStatus fromLabel(String label) {
		for (HmsDeviceStatus prop : values()) {
			if (prop.getLabel().equals(label)) {
				return prop;
			}
		}
		return valueOf(label);
	}

}
