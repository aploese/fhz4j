/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
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

import de.ibapl.fhz4j.protocol.evohome.DeviceId;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeCommand;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeDeviceMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgParam0;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgType;
import java.util.LinkedList;

/**
 *
 * @author Arne Plöse
 * <a href="https://github.com/zxdavb/ramses_protocol/wiki/000C:-Zone-Actuators">000C:
 * Zone Actuators</a>
 */
public class ZoneActuatorsInformationMessage extends EvoHomeDeviceMessage {

    public static class ZoneActuator {

        public byte zone_idx;
        public byte unknown0;
        public DeviceId deviceId;

        public ZoneActuator() {

        }

        public ZoneActuator(byte zone_idx, byte unknown, DeviceId deviceId) {
            this.zone_idx = zone_idx;
            this.unknown0 = unknown;
            this.deviceId = deviceId;
        }

    }

    public LinkedList<ZoneActuator> actuators = new LinkedList<>();

    public ZoneActuatorsInformationMessage(EvoHomeMsgParam0 msgParam0) {
        super(EvoHomeCommand.ZONE_ACTUATORS, EvoHomeMsgType.INFORMATION, msgParam0);
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", zones:[");
        boolean first = true;
        for (ZoneActuator za : actuators) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append("{");
            sb.append(String.format("zone_idx : 0x%02x", za.zone_idx));
            sb.append(String.format(", unknown0 : 0x%02x", za.unknown0));
            sb.append(", deviceId : ").append(za.deviceId);
            sb.append("}");
        }
        sb.append("]");
    }
}
