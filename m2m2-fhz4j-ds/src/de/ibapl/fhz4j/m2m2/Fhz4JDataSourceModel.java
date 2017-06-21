/**
 * Copyright (C) 2014 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package de.ibapl.fhz4j.m2m2;

import com.serotonin.m2m2.web.mvc.rest.v1.model.dataSource.AbstractDataSourceModel;

/**
 * @author Terry Packer
 *
 */
public class Fhz4JDataSourceModel extends AbstractDataSourceModel<Fhz4JDataSourceVO>{

	/**
	 * @param data
	 */
	public Fhz4JDataSourceModel(Fhz4JDataSourceVO data) {
		super(data);
	}

	public Fhz4JDataSourceModel() {
		super(new Fhz4JDataSourceVO());
	}


	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.web.mvc.rest.v1.model.AbstractVoModel#getModelType()
	 */
	@Override
	public String getModelType() {
		return Fhz4JDataSourceDefinition.DATA_SOURCE_TYPE;
	}
	/*
	@JsonGetter(value="pollPeriod")
	public TimePeriod getPollPeriod(){
	    return new TimePeriod(this.data.getUpdatePeriods(), 
	            TimePeriodType.convertTo(this.data.getUpdatePeriodType()));
	}

	@JsonSetter(value="pollPeriod")
	public void setPollPeriod(TimePeriod pollPeriod){
	    this.data.setUpdatePeriods(pollPeriod.getPeriods());
	    this.data.setUpdatePeriodType(TimePeriodType.convertFrom(pollPeriod.getType()));
	}
*/
}
