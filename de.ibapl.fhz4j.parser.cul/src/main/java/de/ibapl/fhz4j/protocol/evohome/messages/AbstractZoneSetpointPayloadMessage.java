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
import de.ibapl.fhz4j.protocol.evohome.ZoneTemperature;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Arne Plöse
 * <a href="https://github.com/zxdavb/ramses_protocol/wiki/2309:-Zone-Setpoint">2309:
 * Zone Setpoint</a>
 * @param <T>
 */
public abstract class AbstractZoneSetpointPayloadMessage<T extends AbstractZoneSetpointPayloadMessage<T>> extends AbstractZoneSetpointMessage<T> {

    public LinkedList<ZoneTemperature> zoneTemperatures = new LinkedList<>();

    protected AbstractZoneSetpointPayloadMessage(EvoHomeMsgType msgType, EvoHomeMsgParam0 msgParam0) {
        super(msgType, msgParam0);
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", zoneTemperatures : [");
        boolean first = true;
        for (ZoneTemperature zt : zoneTemperatures) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(zt);
        }
        sb.append("]");
    }

    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        return HASH_MULTIPLIER * hash + Objects.hashCode(this.zoneTemperatures);
    }

    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        return Objects.equals(this.zoneTemperatures, other.zoneTemperatures);
    }

}
