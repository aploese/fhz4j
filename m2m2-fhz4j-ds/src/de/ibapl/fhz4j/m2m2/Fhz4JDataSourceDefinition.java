/*
 *   Copyright (C) 2010 Arne Pl\u00f6se
 *   @author Arne Pl\u00f6se
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
