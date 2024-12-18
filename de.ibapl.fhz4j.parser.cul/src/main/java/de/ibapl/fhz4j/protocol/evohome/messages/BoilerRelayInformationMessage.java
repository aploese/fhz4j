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
import java.time.Duration;
import java.util.Objects;

/**
 *
 * @author Arne Plöse
 * <a href="https://github.com/zxdavb/ramses_protocol/wiki/1100:-Boiler-Relay-Information">1100:
 * Boiler Relay Information</a>
 * @param <T>
 */
public class BoilerRelayInformationMessage<T extends BoilerRelayInformationMessage<T>> extends EvoHomeDeviceMessage<T> {

    public byte domain_id;
    public float cycle_rate;
    public Duration minimum_on_time;
    public Duration minimum_off_time;
    public byte unknown0;
    public short proportional_band_width;
    public byte unknown1;

    public BoilerRelayInformationMessage(EvoHomeMsgType msgType, EvoHomeMsgParam0 msgParam0) {
        super(EvoHomeCommand.BOILER_RELAY_INFORMATION, msgType, msgParam0);
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(String.format(", domain_id : 0x%02x", domain_id));
        sb.append(", cycle_rate : ").append(cycle_rate);
        sb.append(", minimum_on_time : ").append(minimum_on_time);
        sb.append(", minimum_off_time : ").append(minimum_off_time);
        sb.append(String.format(", unknown0 : 0x%02x", unknown0));
        sb.append(", proportional_band_width : ").append(proportional_band_width);
        sb.append(String.format(", unknown1 : 0x%02x", unknown1));
    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        hash = HASH_MULTIPLIER * hash + this.domain_id;
        hash = HASH_MULTIPLIER * hash + Float.floatToIntBits(this.cycle_rate);
        hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.minimum_on_time);
        hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.minimum_off_time);
        hash = HASH_MULTIPLIER * hash + this.unknown0;
        hash = HASH_MULTIPLIER * hash + this.proportional_band_width;
        return HASH_MULTIPLIER * hash + this.unknown1;
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        if (this.domain_id != other.domain_id) {
            return false;
        }
        if (Float.floatToIntBits(this.cycle_rate) != Float.floatToIntBits(other.cycle_rate)) {
            return false;
        }
        if (this.unknown0 != other.unknown0) {
            return false;
        }
        if (this.proportional_band_width != other.proportional_band_width) {
            return false;
        }
        if (this.unknown1 != other.unknown1) {
            return false;
        }
        if (!Objects.equals(this.minimum_on_time, other.minimum_on_time)) {
            return false;
        }
        return Objects.equals(this.minimum_off_time, other.minimum_off_time);
    }

}
