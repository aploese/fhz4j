/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2022-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.fht;

import de.ibapl.fhz4j.api.Protocol;
import java.util.Objects;

/**
 *
 * @author Arne Plöse
 * @param <T>
 */
public class Fht80TfMessage<T extends Fht80TfMessage<T>> extends AbstractFhtMessage<T> {

    public Fht80TfValue value;
    public boolean lowBattery;
    public int address;

    public Fht80TfMessage(int address, Fht80TfValue value, boolean lowBattery) {
        super(Protocol.FHT_TF);
        this.address = address;
        this.value = value;
        this.lowBattery = lowBattery;
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(String.format(", address : 0x%06x", address));
        sb.append(", value : ").append(value);
        sb.append(", lowBattery : ").append(lowBattery);

    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.value);
        hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.lowBattery);
        return HASH_MULTIPLIER * hash + this.address;
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        if (this.value != other.value) {
            return false;
        }
        if (this.lowBattery != other.lowBattery) {
            return false;
        }
        return this.address == other.address;
    }

}
