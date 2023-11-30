/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2023, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.hms;

import java.util.EnumSet;
import java.util.Set;

import de.ibapl.fhz4j.api.Message;
import de.ibapl.fhz4j.api.Protocol;

/**
 *
 * @author Arne Plöse
 */
public abstract class HmsMessage extends Message {

    public short housecode;
    public Set<HmsDeviceStatus> deviceStatus = EnumSet.noneOf(HmsDeviceStatus.class);
    public HmsDeviceType hmsDeviceType;

    protected HmsMessage(short housecode, HmsDeviceType hmsDeviceType, Set<HmsDeviceStatus> deviceStatus) {
        super(Protocol.HMS);
        this.housecode = housecode;
        this.hmsDeviceType = hmsDeviceType;
        this.deviceStatus = deviceStatus;
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", housecode : ").append(housecode);
        sb.append(", deviceStatus : ").append(deviceStatus).append("");
        sb.append(", hmsDeviceType : ").append(hmsDeviceType);
    }
}
