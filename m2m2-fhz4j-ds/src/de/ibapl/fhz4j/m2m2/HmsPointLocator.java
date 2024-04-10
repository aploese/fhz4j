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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ibapl.fhz4j.m2m2;

import com.serotonin.m2m2.i18n.TranslatableMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import net.sf.fhz4j.Fhz1000;
import net.sf.fhz4j.FhzProtocol;
import net.sf.fhz4j.hms.HmsDeviceType;
import net.sf.fhz4j.hms.HmsProperty;

/**
 *
 * @author aploese
 */
public class HmsPointLocator extends ProtocolLocator<HmsProperty> {

    private short housecode;
    private HmsDeviceType hmsDeviceType;

    /**
     * @return the housecode
     */
    public short getHousecode() {
        return housecode;
    }

    public String defaultName() {
        return getProperty() == null ? "HMS unknown" : getProperty().getLabel();
    }

    /**
     * @param housecode the housecode to set
     */
    public void setHousecode(short housecode) {
        this.housecode = housecode;
    }

    public void setHousecodeStr(String housecode) {
        this.housecode = (short) Integer.parseInt(housecode, 16);
    }

    public String getHousecodeStr() {
        return String.format("%04X", housecode);
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
        out.writeObject(hmsDeviceType);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        switch (ver) {
            case 1:
                housecode = in.readShort();
                hmsDeviceType = (HmsDeviceType) in.readObject();
                break;
            default:
                throw new RuntimeException("Cant handle version");
        }
    }

    @Override
    public FhzProtocol getFhzProtocol() {
        return FhzProtocol.HMS;
    }

    /**
     * @return the hmsDeviceType
     */
    public HmsDeviceType getHmsDeviceType() {
        return hmsDeviceType;
    }

    /**
     * @param hmsDeviceType the hmsDeviceType to set
     */
    public void setHmsDeviceType(HmsDeviceType hmsDeviceType) {
        this.hmsDeviceType = hmsDeviceType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HmsPointLocator other = (HmsPointLocator) obj;
        if (this.housecode != other.housecode) {
            return false;
        }
        if (this.hmsDeviceType != other.hmsDeviceType) {
            return false;
        }
        if (this.getProperty() != other.getProperty()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.housecode;
        hash = 43 * hash + (this.hmsDeviceType != null ? this.hmsDeviceType.hashCode() : 0);
        hash = 43 * hash + (this.getProperty() != null ? this.getProperty().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s [housecode: %s, devicetype: property %s]", getClass().getName(), getHousecodeStr(), hmsDeviceType, getProperty());
    }

}
