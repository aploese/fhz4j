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

import java.io.Serializable;
import net.sf.fhz4j.FhzProtocol;
import static net.sf.fhz4j.FhzProtocol.*;

/**
 *
 * @author aploese
 */
public enum FhtDeviceTypes implements Serializable {

    FHT_8("FHT 8", FHT),
    FHT_80B("FHT 80B", FHT),
    UNKNOWN("Unknown", FhzProtocol.UNKNOWN);

    public static FhtDeviceTypes fromLabel(String label) {
        for (FhtDeviceTypes deviceType : values()) {
            if (deviceType.getLabel().equals(label)) {
                return deviceType;
            }
        }
        return valueOf(label);
    }
    private final String label;
    private final FhzProtocol protocol;

    private FhtDeviceTypes(String label, FhzProtocol protocol) {
        this.label = label;
        this.protocol = protocol;
    }

    public String getLabel() {
        return label;
    }

    public FhzProtocol getProtocol() {
        return protocol;
    }

    public String getName() {
        return name();
    }
}
