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

import java.math.BigDecimal;
import java.util.Objects;

public class ZoneTemperature {

    public byte zone;
    public BigDecimal temperature;

    public ZoneTemperature() {
    }

    public ZoneTemperature(byte zone, BigDecimal temperature) {
        this.zone = zone;
        this.temperature = temperature;
    }

    public ZoneTemperature(byte zone) {
        this.zone = zone;
    }

    protected void addToString(StringBuilder sb) {
        //no-op must be overwritten ... in subclasses
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(String.format("zone : 0x%02x", zone));
        sb.append(", temperature : ").append(temperature);
        addToString(sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, zone);
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
        ZoneTemperature other = (ZoneTemperature) obj;
        return Objects.equals(temperature, other.temperature) && zone == other.zone;
    }

}
