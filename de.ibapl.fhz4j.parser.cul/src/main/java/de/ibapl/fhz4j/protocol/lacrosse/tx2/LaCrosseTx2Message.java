package de.ibapl.fhz4j.protocol.lacrosse.tx2;

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

import de.ibapl.fhz4j.api.FhzMessage;
import java.util.Set;

/**
 *
 * @author aploese
 */
public class LaCrosseTx2Message extends FhzMessage<LaCrosseTx2Property>{

    private LaCrosseTx2Property laCrosseTx2Property;
    private short address;
    private float value;
    
    @Override
    protected void toString(StringBuilder sb) {
        sb.append("address: ").append(Integer.toHexString(address)).append(", type: ").append(laCrosseTx2Property).append(", value: ").append(value);
    }

    @Override
    public Set<LaCrosseTx2Property> getSupportedProperties() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setProperty(LaCrosseTx2Property laCrosseTx2Property) {
        this.laCrosseTx2Property = laCrosseTx2Property;
    }

    public void setAddress(short address) {
        this.address = address;
    }
    
    public void setData(float value) {
        this.value = value;
    }

    public LaCrosseTx2Property getProperty() {
        return laCrosseTx2Property;
    }
    
}
