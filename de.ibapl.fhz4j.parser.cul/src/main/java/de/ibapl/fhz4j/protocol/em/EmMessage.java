/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2017-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.em;

/*-
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2018 Arne Plöse
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
import de.ibapl.fhz4j.api.Message;
import de.ibapl.fhz4j.api.Protocol;

/**
 *
 * @author Arne Plöse
 */
public class EmMessage extends Message {

    public final static double EM_1000_S_CORR_1 = 12.0 / 150.0;
    public final static double EM_1000_S_CORR_2 = 12.0 / 1800.0;

    public final static double EM_1000_EM_POWER = 0.01;
    public final static double EM_1000_EM_ENERY = 0.001;

    public short address;
    public short counter;
    public EmDeviceType emDeviceType;
    /**
     * EM 1000-EM Energy cumulated valueCummulated * EM_1000_EM_ENERY
     */
    public int valueCummulated;
    /**
     * EM 1000-EM Power average value5Min * EM_1000_EM_POWER
     */
    public int value5Min;
    /**
     * EM 1000-EM Power 5min max value5MinPeak * EM_1000_EM_POWER
     */
    public int value5MinPeak;

    public EmMessage(EmDeviceType emDeviceType) {
        super(Protocol.EM);
        this.emDeviceType = emDeviceType;
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", address : ").append(address);
        sb.append(", counter : ").append(counter);
        sb.append(", emDeviceType : ").append(emDeviceType);
    }

}
