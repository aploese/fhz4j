package de.ibapl.fhz4j.m2m2;

import com.serotonin.m2m2.module.ModelDefinition;
import com.serotonin.m2m2.web.mvc.rest.v1.model.AbstractRestModel;

public class Fhz4JPointLocatorModelDefinition extends ModelDefinition{

	public static final String TYPE_NAME = "PL.FHZ4J";
	@Override
	public String getModelKey() {
		return ""; //TODO
	}

	@Override
	public String getModelTypeName() {
		return TYPE_NAME;
	}

	@Override
	public AbstractRestModel<?> createModel() {
		return new Fhz4JPointLocatorModel(new Fhz4JPointLocatorVO());
	}

	@Override
	public boolean supportsClass(Class<?> clazz) {
		return Fhz4JPointLocatorModel.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.ModelDefinition#getModelClass()
	 */
	@Override
	public Class<? extends AbstractRestModel<?>> getModelClass() {
		return Fhz4JPointLocatorModel.class;
	}

}
