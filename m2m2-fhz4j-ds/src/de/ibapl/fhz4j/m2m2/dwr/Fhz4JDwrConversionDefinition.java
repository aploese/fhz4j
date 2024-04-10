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
package de.ibapl.fhz4j.m2m2.dwr;

import com.serotonin.m2m2.module.DwrConversionDefinition;

import de.ibapl.fhz4j.m2m2.EmPointLocator;
import de.ibapl.fhz4j.m2m2.FS20PointLocator;
import de.ibapl.fhz4j.m2m2.FhtMultiMsgPointLocator;
import de.ibapl.fhz4j.m2m2.FhtPointLocator;
import net.sf.fhz4j.FhzProtocol;
import net.sf.fhz4j.fht.FhtDeviceType;
import net.sf.fhz4j.fht.FhtMultiMsgProperty;
import net.sf.fhz4j.fht.FhtProperty;
import net.sf.fhz4j.scada.DataType;

public class Fhz4JDwrConversionDefinition extends DwrConversionDefinition {

    @Override
    public void addConversions() {
        addConversion(FhzProtocol.class, "enum");
        addConversion(FhtProperty.class, "enum");
        addConversion(FhtPointLocator.class);
        addConversion(FhtDeviceType.class);
        addConversion(FhtMultiMsgPointLocator.class);
        addConversion(FhtMultiMsgProperty.class);
        addConversion(FS20PointLocator.class);
        addConversion(EmPointLocator.class);
        addConversion(DataType.class, "enum");

    }

}
