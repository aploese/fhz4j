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
package de.ibapl.fhz4j.protocol.evohome;

import de.ibapl.fhz4j.api.Message;
import de.ibapl.fhz4j.api.Protocol;

/**
 *
 * @author Arne Plöse
 */
public abstract class EvoHomeMessage extends Message {

    public final EvoHomeCommand command;
    public final EvoHomeMsgType msgType;
    public final EvoHomeMsgParam0 msgParam0;

    protected EvoHomeMessage(EvoHomeCommand command, EvoHomeMsgType msgType, EvoHomeMsgParam0 msgParam0) {
        super(Protocol.EVO_HOME);
        this.command = command;
        this.msgType = msgType;
        this.msgParam0 = msgParam0;
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", msgType : ").append(msgType);
        sb.append(", command : ").append(command);
        sb.append(", msgParam0 : ").append(msgParam0);
    }
}
