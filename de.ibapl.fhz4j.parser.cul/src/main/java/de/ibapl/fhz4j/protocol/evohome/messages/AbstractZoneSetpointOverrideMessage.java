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

import de.ibapl.fhz4j.protocol.evohome.EvoHomeCommand;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeDeviceMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgParam0;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgType;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Arne Plöse
 * <a href="https://github.com/zxdavb/ramses_protocol/wiki/2349:-Setpoint-Override">2349:
 * Setpoint Override</a>
 */
public abstract class AbstractZoneSetpointOverrideMessage extends EvoHomeDeviceMessage {

    public enum SetpointOverrideMode {
        FOLLOW_SCHEDULE,
        /**
         * Until the next scheduled setpoint.
         */
        ADVANCED_OVERRIDE,
        /**
         * indefinitely
         */
        PERMANENT_OVERRIDE,
        /**
         * for a number of minutes
         */
        COUNTDOWN_OVERRIDE,
        /**
         * until a given date/time
         */
        TEMPORARY_OVERRIDE;
    }

    public byte zone_id;
    public BigDecimal setpoint;
    public SetpointOverrideMode zone_mode;
    public Duration countdown;
    public LocalDateTime time_until;

    protected AbstractZoneSetpointOverrideMessage(EvoHomeMsgType msgType, EvoHomeMsgParam0 msgParam0) {
        super(EvoHomeCommand.ZONE_SETPOINT_OVERRIDE, msgType, msgParam0);
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(String.format(", zone_id : 0x%02x", zone_id));
        sb.append(", setpoint : ").append(setpoint);
        sb.append(", zone_mode : ").append(zone_mode);
        sb.append(", countdown : ").append(countdown);
        sb.append(", time_until : ").append(time_until);
    }
}
