/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2017-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.lacrosse.tx2;

import de.ibapl.fhz4j.api.Message;
import de.ibapl.fhz4j.api.Protocol;
import java.util.Objects;

/**
 *
 * @author Arne Plöse
 * @param <T>
 */
public class LaCrosseTx2Message<T extends LaCrosseTx2Message<T>> extends Message<T> {

    public LaCrosseTx2Property laCrosseTx2Property;
    public byte address;
    public float value;

    public LaCrosseTx2Message(LaCrosseTx2Property laCrosseTx2Property) {
        super(Protocol.LA_CROSSE_TX2);
        this.laCrosseTx2Property = laCrosseTx2Property;
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", laCrosseTx2Property : ").append(laCrosseTx2Property);
        sb.append(", address : ").append(address);
        sb.append(", value : ").append(value);
    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.laCrosseTx2Property);
        hash = HASH_MULTIPLIER * hash + this.address;
        return HASH_MULTIPLIER * hash + Float.floatToIntBits(this.value);
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        if (this.address != other.address) {
            return false;
        }
        if (Float.floatToIntBits(this.value) != Float.floatToIntBits(other.value)) {
            return false;
        }
        return this.laCrosseTx2Property == other.laCrosseTx2Property;
    }

}
