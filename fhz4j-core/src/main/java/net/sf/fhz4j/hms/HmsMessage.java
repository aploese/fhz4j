package net.sf.fhz4j.hms;

/*
 * #%L
 * fhz4j Core
 * %%
 * Copyright (C) 2009 - 2014 fhz4j
 * %%
 * fhz4j - Drivers for the Wireless FS20, FHT and HMS protocol http://fhz4j.sourceforge.net/
 * Copyright (C) 2009-2014, fhz4j.sf.net, and individual contributors as indicated
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
import net.sf.fhz4j.FhzMessage;

/**
 *
 * @author aploese
 */
public class HmsMessage extends FhzMessage<HmsProperty> {

    private short housecode;
    private HmsDeviceType deviceType;
    private Set<HmsDeviceStatus> deviceStatus = EnumSet.noneOf(HmsDeviceStatus.class);
    private String rawValue;

    HmsMessage(HmsMessage hmsMessage) {
        this();
        housecode = hmsMessage.housecode;
        deviceType = hmsMessage.deviceType;
        deviceStatus = hmsMessage.deviceStatus;
    }

    HmsMessage() {
        super();
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(String.format("housecode: %04X, device type: %s", housecode, deviceType.getLabel()));
        sb.append(" status: [");
        boolean first = true;
        for (HmsDeviceStatus status : deviceStatus) {
            if (first) {
                first = false;
            } else {
                sb.append(" ,");
            }
            sb.append(status.getLabel());
        }
        sb.append(" ]");
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
     * @return the deviceType
     */
    public HmsDeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType the deviceType to set
     */
    public void setDeviceType(HmsDeviceType deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return the deviceStatus
     */
    public Set<HmsDeviceStatus> getDeviceStatus() {
        return deviceStatus;
    }

    /**
     * @param deviceStatus the deviceStatus to set
     */
    public void setDeviceStatus(Set<HmsDeviceStatus> deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    @Override
    public boolean getBoolean(HmsProperty prop) {
        switch (prop) {
            case BATT_STATUS:
                return getDeviceStatus().contains(HmsDeviceStatus.BATT_LOW);
            default:
                return super.getBoolean(prop);
        }
    }

    @Override
    public String getString(HmsProperty prop) {
        switch (prop) {
            case RAW_VALUE:
                return rawValue;
            default:
                return super.getString(prop);
        }
    }

    /**
     * @return the rawValue
     */
    public String getRawValue() {
        return rawValue;
    }

    /**
     * @param rawValue the rawValue to set
     */
    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

}
