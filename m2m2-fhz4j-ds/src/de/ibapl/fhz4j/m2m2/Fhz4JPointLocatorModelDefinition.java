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
package de.ibapl.fhz4j.m2m2;

import com.serotonin.m2m2.module.ModelDefinition;
import com.serotonin.m2m2.web.mvc.rest.v1.model.AbstractRestModel;

public class Fhz4JPointLocatorModelDefinition extends ModelDefinition {

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
