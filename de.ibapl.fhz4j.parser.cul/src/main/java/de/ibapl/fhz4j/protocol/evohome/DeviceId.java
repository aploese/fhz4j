/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2023, Arne Plöse and individual contributors as indicated
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
 * @author aploese
 */
public class DeviceId {

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DeviceId other = (DeviceId) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public final int id;
    public final DeviceType type;

    public DeviceId(int deviceId) {
        this.id = deviceId;
        switch (deviceId & 0xFF0000) {
            case 0x110000:
            case 0x130000:
                type = DeviceType.RADIATOR_CONTROLLER;
                break;
            case 0x890000:
                type = DeviceType.SINGLE_ZONE_THERMOSTAT;
                break;
            case 0x060000:
                type = DeviceType.MULTI_ZONE_CONTROLLER;
                break;
            default:
                type = DeviceType.UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return String.format("{id : 0x%06x, type : \"%s\"}", id, type);
    }
}
