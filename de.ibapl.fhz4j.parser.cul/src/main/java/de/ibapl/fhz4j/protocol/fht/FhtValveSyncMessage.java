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
package de.ibapl.fhz4j.protocol.fht;

/**
 *
 * @author Arne Plöse
 * @param <T>
 */
public class FhtValveSyncMessage<T extends FhtValveSyncMessage<T>> extends FhtMessage<T> {

    public float timeLeft;
    public boolean bit5;

    public FhtValveSyncMessage(short housecode, FhtProperty command, byte description, float timeLeft, boolean bit5) {
        super(housecode, command, description);
        this.timeLeft = timeLeft;
        this.bit5 = bit5;
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", timeLeft : ").append(timeLeft);
        sb.append(", bit5 : ").append(bit5);
    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        hash = HASH_MULTIPLIER * hash + Float.floatToIntBits(this.timeLeft);
        return HASH_MULTIPLIER * hash + (this.bit5 ? 1 : 0);
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        if (Float.floatToIntBits(this.timeLeft) != Float.floatToIntBits(other.timeLeft)) {
            return false;
        }
        return this.bit5 == other.bit5;
    }

}
