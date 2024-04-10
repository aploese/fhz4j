/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2016-2024, Arne Plöse and individual contributors as indicated
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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ibapl.fhz4j.m2m2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Logger;
import net.sf.fhz4j.Fhz1000;
import net.sf.fhz4j.FhzProtocol;
import net.sf.fhz4j.fs20.FS20DeviceType;

/**
 *
 * @author aploese
 */
public class FS20PointLocator extends ProtocolLocator<FS20DeviceType> {

    private final static Logger LOG = Logger.getLogger("DS_FHZ4J");
    private short housecode;
    private byte offset;
    private FS20DeviceType deviceType;

    /**
     * @return the housecode
     */
    public short getHousecode() {
        return housecode;
    }

    public String defaultName() {
        return getProperty() == null ? "FS20 unknown" : String.format("%d", offset);
    }

    /**
     * @param housecode the housecode to set
     */
    public void setHousecode(short housecode) {
        this.housecode = housecode;
    }

    public void setHousecodeStr(String deviceHousecode) {
        this.housecode = Fhz1000.parseHouseCode(deviceHousecode);
    }

    public String getHousecodeStr() {
        return String.format("%04x", housecode);
    }

    public String getPropertyLabel() {
        return getProperty().getLabel();
    }

    public void setPropertyLabel(String label) {
        throw new RuntimeException("set Propewrty Label not implemented");
    }

    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int SERIAL_VERSION = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(SERIAL_VERSION);
        out.writeShort(housecode);
        out.writeByte(offset);
        out.writeObject(deviceType);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        switch (ver) {
            case 1:
                housecode = in.readShort();
                offset = in.readByte();
                deviceType = (FS20DeviceType) in.readObject();
                break;
            default:
                throw new RuntimeException("Cant handle version");
        }
    }

    @Override
    public FhzProtocol getFhzProtocol() {
        return FhzProtocol.FS20;
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
     * @return the deviceType
     */
    public FS20DeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType the deviceType to set
     */
    public void setDeviceType(FS20DeviceType deviceType) {
        this.deviceType = deviceType;
    }

}
