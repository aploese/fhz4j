package de.ibapl.fhz4j.protocol.hms;

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
import de.ibapl.fhz4j.api.FhzMessage;

/**
 *
 * @author aploese
 */
public abstract class HmsMessage extends FhzMessage<HmsProperty> {

    private short housecode;
    private Set<HmsDeviceStatus> deviceStatus = EnumSet.noneOf(HmsDeviceStatus.class);

    HmsMessage(short housecode, Set<HmsDeviceStatus> deviceStatus) {
        super();
        this.housecode = housecode;
        this.deviceStatus = deviceStatus;
    }

    HmsMessage() {
        super();
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(String.format("housecode: %04X, device type: %s", housecode, getDeviceType().getLabel()));
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
    public abstract HmsDeviceType getDeviceType();

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
    public Set<HmsProperty> getSupportedProperties() {
        return getDeviceType().getProperties();
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

}
