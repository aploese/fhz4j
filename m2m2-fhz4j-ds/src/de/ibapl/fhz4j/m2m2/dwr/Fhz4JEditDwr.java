/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2016-2021, Arne Plöse and individual contributors as indicated
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

import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.vo.DataPointVO;
import com.serotonin.m2m2.web.dwr.DataSourceEditDwr;
import com.serotonin.m2m2.web.dwr.util.DwrPermission;
import de.ibapl.fhz4j.m2m2.FhtPointLocator;
import de.ibapl.fhz4j.m2m2.Fhz4JDataSourceVO;
import de.ibapl.fhz4j.m2m2.Fhz4JPointLocatorVO;
import net.sf.fhz4j.fht.FhtDeviceType;
import net.sf.fhz4j.fht.FhtProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Fhz4JEditDwr extends DataSourceEditDwr {

    private final static Log LOG = LogFactory.getLog(DataSourceEditDwr.class);

    //
    // /
    // / FHZ 4 J stuff
    // /
    //
    @DwrPermission(user = true)
    public ProcessResult saveFhz4JDataSource(String name, String xid, String commPort, String fhzHousecode, boolean fhtMaster) {
        Fhz4JDataSourceVO ds = (Fhz4JDataSourceVO) Common.getUser().getEditDataSource();

        ds.setXid(xid);
        ds.setName(name);
        ds.setCommPort(commPort);
        ds.setFhzHousecode(fhzHousecode);
        ds.setFhtMaster(fhtMaster);
        return tryDataSourceSave(ds);
    }

    @DwrPermission(user = true)
    public DataPointVO addFhz4JFhtPoint(String housecode, String deviceLocation, String deviceTypeLabel, String propertyLabel) {
        //TODO what happends, if user edits 2 Datasources???
        DataPointVO result = getPoint(Common.NEW_ID, null);
        FhtPointLocator locator = (FhtPointLocator) result.getPointLocator();
        locator.setHousecodeStr(housecode);
        locator.setFhtDeviceTypeLabel(deviceTypeLabel);
        locator.setPropertyLabel(propertyLabel);

        //  result.setName(locator.defaultName());
        return result;
    }

    @DwrPermission(user = true)
    public String[] getFht4JProperties(String deviceTypeLabel) {
        return FhtProperty.getFhtPropertyLabelsOf(FhtDeviceType.fromLabel(deviceTypeLabel));
    }

    public ProcessResult saveFhz4JPointLocator(int id, String xid, String name, Fhz4JPointLocatorVO locator) {
        return validatePoint(id, xid, name, locator, null);
    }
}
