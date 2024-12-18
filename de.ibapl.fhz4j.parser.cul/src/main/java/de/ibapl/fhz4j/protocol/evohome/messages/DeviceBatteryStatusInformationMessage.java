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

/**
 *
 * @author Arne Plöse
 * <a href="https://github.com/zxdavb/ramses_protocol/wiki/1060:-Device-Battery-Status">1060:
 * Device Battery Status</a>
 * @param <T>
 */
public class DeviceBatteryStatusInformationMessage<T extends DeviceBatteryStatusInformationMessage<T>> extends EvoHomeDeviceMessage<T> {

    public byte zone_id;
    public float level;
    public byte unknown0;

    public DeviceBatteryStatusInformationMessage(EvoHomeMsgParam0 msgParam0) {
        super(EvoHomeCommand.DEVICE_BATTERY_STATUS, EvoHomeMsgType.INFORMATION, msgParam0);
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(String.format(", zone_id : 0x%02x", zone_id));
        sb.append(", level : ").append(level);
        sb.append(String.format(", unknown0 : 0x%02x", unknown0));
    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        hash = HASH_MULTIPLIER * hash + this.zone_id;
        hash = HASH_MULTIPLIER * hash + Float.floatToIntBits(this.level);
        return HASH_MULTIPLIER * hash + this.unknown0;
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        if (this.zone_id != other.zone_id) {
            return false;
        }
        if (Float.floatToIntBits(this.level) != Float.floatToIntBits(other.level)) {
            return false;
        }
        return this.unknown0 == other.unknown0;
    }

}
