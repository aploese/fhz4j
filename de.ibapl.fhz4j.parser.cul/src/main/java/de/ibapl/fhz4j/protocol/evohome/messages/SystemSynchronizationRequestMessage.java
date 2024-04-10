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
 * @author Arne Plöse
 * <a href="https://github.com/zxdavb/ramses_protocol/wiki/1F09:-System-Synchronization">1F09:
 * System Synchronization</a>
 */
public class SystemSynchronizationRequestMessage extends EvoHomeDeviceMessage {

    public byte domain_id;

    public SystemSynchronizationRequestMessage(EvoHomeMsgParam0 msgParam0) {
        super(EvoHomeCommand.SYSTEM_SYNCHRONIZATION, EvoHomeMsgType.REQUEST, msgParam0);
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(String.format(", domain_id : 0x%02x", domain_id));
    }
}
