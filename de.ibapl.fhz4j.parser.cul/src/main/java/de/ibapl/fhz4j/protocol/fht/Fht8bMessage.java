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
public class Fht8bMessage<T extends Fht8bMessage<T>> extends FhtMessage<T> {

    public boolean fromFht_8B;
    public boolean dataRegister;

    public Fht8bMessage(short housecode, FhtProperty command, byte description, boolean fromFht_8B, boolean dataRegister) {
        super(housecode, command, description);
        this.fromFht_8B = fromFht_8B;
        this.dataRegister = dataRegister;
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", fromFht_8b : ").append(fromFht_8B);
        sb.append(", dataRegister : ").append(dataRegister);
    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        hash = HASH_MULTIPLIER * hash + (this.fromFht_8B ? 1 : 0);
        return HASH_MULTIPLIER * hash + (this.dataRegister ? 1 : 0);
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        if (this.fromFht_8B != other.fromFht_8B) {
            return false;
        }
        return this.dataRegister == other.dataRegister;
    }

}
