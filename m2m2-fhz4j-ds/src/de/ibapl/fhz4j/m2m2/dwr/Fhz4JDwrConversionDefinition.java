/*
 *   Copyright (C) 2010 Arne Pl\u00f6se
 *   @author Arne Pl\u00f6se
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
