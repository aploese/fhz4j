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

import java.util.Set;


/**
 *
 * @author aploese
 */
public class Hms100TfMessage extends HmsMessage {

    private float temp;
    private float humidy;

    public Hms100TfMessage(short housecode, Set<HmsDeviceStatus> deviceStatus) {
        super(housecode, deviceStatus);
    }

    /**
     * @return the temp
     */
    public float getTemp() {
        return temp;
    }

    /**
     * @param temp the temp to set
     */
    public void setTemp(float temp) {
        this.temp = temp;
    }

    /**
     * @return the humidy
     */
    public float getHumidy() {
        return humidy;
    }

    /**
     * @param humidy the humidy to set
     */
    public void setHumidy(float humidy) {
        this.humidy = humidy;
    }

    @Override
    public void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(" temp: ").append(temp);
        sb.append(", humidy: ").append(humidy);
    }

    @Override
    public float getFloat(HmsProperty prop) {
        switch (prop) {
            case HUMIDY:
                return getHumidy();
            case TEMP:
                return getTemp();
            default:
                return super.getFloat(prop);
        }
    }

    @Override
    public HmsDeviceType getDeviceType() {
        return HmsDeviceType.HMS_100_TF;
    }

}
