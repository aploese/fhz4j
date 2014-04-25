package net.sf.fhz4j.fs20;

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
import net.sf.fhz4j.Fhz1000;
import net.sf.fhz4j.FhzMessage;

/**
 *
 * @author aploese
 */
public class FS20Message extends FhzMessage<FS20Property> {

    private short housecode;
    private byte offset;
    private FS20Property property;
    
    
    @Override
    protected void toString(StringBuilder sb) {
        sb.append("housecode: ").append(Fhz1000.houseCodeToString(housecode));
        sb.append(", offset: ").append(offset);
        sb.append(", command: ").append(property.getLabel());
    }

    @Override
    public Set<FS20Property> getProperties() {
        return EnumSet.of(property); 
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
     * @return the property
     */
    public FS20Property getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(FS20Property property) {
        this.property = property;
    }

}
