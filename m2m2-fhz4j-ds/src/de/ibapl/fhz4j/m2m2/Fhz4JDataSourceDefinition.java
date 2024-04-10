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
package de.ibapl.fhz4j.m2m2;

import com.serotonin.m2m2.module.DataSourceDefinition;
import com.serotonin.m2m2.module.ModuleRegistry;
import com.serotonin.m2m2.module.license.DataSourceTypePointsLimit;
import com.serotonin.m2m2.vo.dataSource.DataSourceVO;
import com.serotonin.m2m2.web.mvc.rest.v1.model.dataSource.AbstractDataSourceModel;
import de.ibapl.fhz4j.m2m2.dwr.Fhz4JEditDwr;

public class Fhz4JDataSourceDefinition extends DataSourceDefinition {

    public static final String DATA_SOURCE_TYPE = "FHZ4J";

    @Override
    public void preInitialize() {
        ModuleRegistry.addLicenseEnforcement(new DataSourceTypePointsLimit(getModule().getName(), DATA_SOURCE_TYPE, 20, null));
    }

    @Override
    public String getDataSourceTypeName() {
        return DATA_SOURCE_TYPE;
    }

    @Override
    public String getDescriptionKey() {
        return "dsEdit.fhz4j";
    }

    @Override
    protected DataSourceVO<?> createDataSourceVO() {
        return new Fhz4JDataSourceVO();
    }

    @Override
    public String getEditPagePath() {
        return "web/editFhz4J.jsp";
    }

    @Override
    public Class<?> getDwrClass() {
        return Fhz4JEditDwr.class;
    }

    @Override
    public Class<? extends AbstractDataSourceModel<?>> getModelClass() {
        return Fhz4JDataSourceModel.class;
    }

}
