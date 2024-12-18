/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2019-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.api;

import java.util.Objects;

/**
 *
 * @author Arne Plöse
 * @param <T>
 *
 */
public abstract class Message<T extends Message<T>> {

    protected final static int HASH_MULTIPLIER = 53;
    protected final static int INITIAL_HASH = 7;

    @Override
    final public int hashCode() {
        return subClassHashCode(INITIAL_HASH);
    }

    protected int subClassHashCode(int hash) {
        return HASH_MULTIPLIER * hash + Objects.hashCode(this.protocol);
    }

    @Override
    final public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return subClassEquals((T) obj);
    }

    protected boolean subClassEquals(T other) {
        return this.protocol == other.protocol;
    }

    public Protocol protocol;

    protected Message(Protocol protocol) {
        this.protocol = protocol;
    }

    protected void addToJsonString(StringBuilder sb) {
        sb.append("protocol : ");
        sb.append(protocol);
    }

    @Override
    final public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        addToJsonString(sb);
        sb.append("}");
        return sb.toString();
    }

}
