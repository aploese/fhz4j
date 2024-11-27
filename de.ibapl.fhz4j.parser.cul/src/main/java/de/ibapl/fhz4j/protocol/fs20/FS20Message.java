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
package de.ibapl.fhz4j.protocol.fs20;

import de.ibapl.fhz4j.api.Message;
import de.ibapl.fhz4j.api.Protocol;
import java.util.Objects;

/**
 *
 * @author Arne Plöse
 * @param <T>
 */
public class FS20Message<T extends FS20Message<T>> extends Message<T> {

    public short housecode;
    public byte offset;
    public FS20CommandValue command;

    public FS20Message(short housecode, FS20CommandValue fS20CommandValues, byte offset) {
        super(Protocol.FS20);
        this.housecode = housecode;
        this.command = fS20CommandValues;
        this.offset = offset;
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", housecode : ").append(housecode);
        sb.append(", offset : ").append(offset);
        sb.append(", command : ").append(command);
    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        hash = HASH_MULTIPLIER * hash + this.housecode;
        hash = HASH_MULTIPLIER * hash + this.offset;
        return HASH_MULTIPLIER * hash + Objects.hashCode(this.command);
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        if (this.housecode != other.housecode) {
            return false;
        }
        if (this.offset != other.offset) {
            return false;
        }
        return this.command == other.command;
    }

}
