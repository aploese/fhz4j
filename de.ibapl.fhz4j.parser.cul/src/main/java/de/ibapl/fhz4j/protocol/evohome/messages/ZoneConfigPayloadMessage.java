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

import de.ibapl.fhz4j.protocol.evohome.EvoHomeCommand;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeDeviceMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgParam0;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Arne Plöse
 * <a href="https://github.com/zxdavb/ramses_protocol/wiki/000A:-Zone-Configuration">000A:
 * Zone Configuration</a>
 * @param <T>
 */
public class ZoneConfigPayloadMessage<T extends ZoneConfigPayloadMessage<T>> extends EvoHomeDeviceMessage<T> {

    public static class ZoneParams {

        @Override
        public int hashCode() {
            int hash = 7;
            hash = HASH_MULTIPLIER * hash + this.zoneId;
            hash = HASH_MULTIPLIER * hash + (this.windowFunction ? 1 : 0);
            hash = HASH_MULTIPLIER * hash + (this.operationLock ? 1 : 0);
            hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.minTemperature);
            hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.maxTemperature);
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
            final ZoneParams other = (ZoneParams) obj;
            if (this.zoneId != other.zoneId) {
                return false;
            }
            if (this.windowFunction != other.windowFunction) {
                return false;
            }
            if (this.operationLock != other.operationLock) {
                return false;
            }
            if (!Objects.equals(this.minTemperature, other.minTemperature)) {
                return false;
            }
            return Objects.equals(this.maxTemperature, other.maxTemperature);
        }

        public byte zoneId;
        public boolean windowFunction;
        public boolean operationLock;
        public BigDecimal minTemperature;
        public BigDecimal maxTemperature;

        protected void addToJsonString(StringBuilder sb) {
            sb.append("{");
            sb.append(String.format("zoneId : 0x%02x", zoneId));
            sb.append(String.format(", windowFunction : %b", windowFunction));
            sb.append(String.format(", operationLock : %b", operationLock));
            sb.append(", minTemperature : ").append(minTemperature);
            sb.append(", maxTemperature : ").append(maxTemperature);
            sb.append("}");
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            addToJsonString(sb);
            return sb.toString();
        }

    }

    public List<ZoneParams> zones;

    public ZoneConfigPayloadMessage(EvoHomeMsgType msgType, EvoHomeMsgParam0 msgParam0) {
        super(EvoHomeCommand.ZONE_CONFIG, msgType, msgParam0);
        if (msgType == EvoHomeMsgType.REQUEST) {
            throw new IllegalArgumentException("Request is not supported");
        }
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", zones:[");
        boolean first = true;
        for (ZoneParams zp : zones) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            zp.addToJsonString(sb);
        }
        sb.append("]");
    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        return HASH_MULTIPLIER * hash + Objects.hashCode(this.zones);
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        return Objects.equals(this.zones, other.zones);
    }

}
