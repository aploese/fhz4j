/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2023-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.evohome.messages;

import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgParam0;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgType;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Arne Plöse
 * <a href="https://github.com/zxdavb/ramses_protocol/wiki/1FC9:-RF-Bind">1FC9:
 * RF Bind</a>
 * @param <T>
 */
public abstract class AbstractRfBindPayloadMessage<T extends AbstractRfBindPayloadMessage<T>> extends AbstractRfBindMessage<T> {

    public static class Data {

        public byte zoneId;
        public short command;
        public int deviceId;

        public Data() {

        }

        public Data(byte zoneId, short command, int deviceId) {
            this.zoneId = zoneId;
            this.command = command;
            this.deviceId = deviceId;
        }

        @Override
        public String toString() {
            return String.format("{zoneId : 0x%02x, command : 0x%04x, deviceId : 0x%06x}", zoneId, command, deviceId);
        }

        @Override
        public int hashCode() {
            int hash = HASH_MULTIPLIER * INITIAL_HASH + this.zoneId;
            hash = HASH_MULTIPLIER * hash + this.command;
            return HASH_MULTIPLIER * hash + this.deviceId;
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
            final Data other = (Data) obj;
            if (this.zoneId != other.zoneId) {
                return false;
            }
            if (this.command != other.command) {
                return false;
            }
            return this.deviceId == other.deviceId;
        }

    }

    public LinkedList<Data> elements = new LinkedList<>();

    protected AbstractRfBindPayloadMessage(EvoHomeMsgType msgType, EvoHomeMsgParam0 msgParam0) {
        super(msgType, msgParam0);
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", elements : [");
        boolean firstRun = true;
        for (Data d : elements) {
            if (firstRun) {
                firstRun = false;
            } else {
                sb.append(", ");
            }
            sb.append(d);
        }
        sb.append("]");
    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        return HASH_MULTIPLIER * hash + Objects.hashCode(this.elements);
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        return Objects.equals(this.elements, other.elements);
    }

}
