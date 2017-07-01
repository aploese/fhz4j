package de.ibapl.fhz4j.fs20;

/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2017 Arne Plöse
 * %%
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009, 2017, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
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
 * #L%
 */

import java.util.EnumSet;
import java.util.Set;
import de.ibapl.fhz4j.Fhz1000;
import de.ibapl.fhz4j.FhzMessage;

/**
 *
 * @author aploese
 */
public class FS20Message extends FhzMessage<FS20DeviceType> {

    private short housecode;
    private byte offset;
    private FS20DeviceType deviceType;
    private FS20CommandValues command;
    
    
    @Override
    protected void toString(StringBuilder sb) {
        sb.append("housecode: ").append(Fhz1000.houseCodeToString(housecode));
        sb.append(", offset: ").append(offset);
        sb.append(", command: ").append(command.getLabel());
    }

    @Override
    public byte getByte(FS20DeviceType property) {
        return command.getValue();
    }

    @Override
    public Set<FS20DeviceType> getSupportedProperties() {
        return EnumSet.of(deviceType); 
    }

    /**
     * @return the housecode
     */
    public short getHousecode() {
        return housecode;
    }

    /**
     * @param housecode the housecode to set
     */
    public void setHousecode(short housecode) {
        this.housecode = housecode;
    }

    /**
     * @return the offset
     */
    public byte getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(byte offset) {
        this.offset = offset;
    }

    /**
     * @return the command
     */
    public FS20CommandValues getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(FS20CommandValues command) {
        this.command = command;
    }

    public FS20DeviceType getDeviceType() {
        return FS20DeviceType.valueOf(command);
    }

    public String getHousecodeStr() {
        return String.format("%04X", housecode);
    }

}
