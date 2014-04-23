package net.sf.fhz4j.fht;

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

import net.sf.fhz4j.Fhz1000;
import net.sf.fhz4j.scada.AbstractPropertyProvider;

/**
 *
 * @author aploese
 */
public class FhtTempMessage extends AbstractPropertyProvider<FhtTempPropery> {
    
    private final FhtMessage low;
    private final FhtMessage high;
    
    public FhtTempMessage(FhtMessage low, FhtMessage high) {
        this.low = low;
        this.high = high;
    }
    
    public short getHousecode() {
        return high.getHousecode();
    }

    public double getTempValue() {
        return ((double)low.getRawValue()) / 10 + high.getRawValue() * 25.5;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("housecode: ").append(Fhz1000.houseCodeToString(low.getHousecode()));
        sb.append(", measured temperature (combined): ").append(getTempValue());
        sb.append(low.getCommand().getUnitOfMeasurement());
        sb.append(" signal strength: ").append(low.getSignalStrength()).append(" dB and ").append(high.getSignalStrength()).append(" dB");
        return sb.toString();
    }

    @Override
    public double getDouble(FhtTempPropery p) {
            return getTempValue();
    }
    
    public FhtTempPropery getProperty() {
        return FhtTempPropery.COMBINED_TEMP;
    }

    public FhtProperty getCommand() {
        return high.getCommand();
    }

}
