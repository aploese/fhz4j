/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2019, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.hms;

import static de.ibapl.fhz4j.scada.DataType.BOOLEAN;
import static de.ibapl.fhz4j.scada.DataType.FLOAT;

import java.io.Serializable;

import de.ibapl.fhz4j.scada.DataType;
import de.ibapl.fhz4j.scada.ScadaProperty;

/**
 *
 * @author Arne Plöse
 */
public enum HmsProperty implements Serializable, ScadaProperty {
	TEMP("temp", "°C", FLOAT), HUMIDY("humidy", "%", FLOAT), BATT_STATUS("batt low", "", BOOLEAN), WATER("water", "",
			BOOLEAN), SMOKE_ALERT("smoke", "", BOOLEAN), DOOR_WINDOW_OPEN("window open", "", BOOLEAN);

	final private String label;
	final private DataType dataType;
	final private String unitOfMeasurement;

	private HmsProperty(String label, String unitOfMeasurement, DataType dataType) {
		this.label = label;
		this.unitOfMeasurement = unitOfMeasurement;
		this.dataType = dataType;
	}

	/**
	 * @return the label
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * @return the dataType
	 */
	@Override
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * @return the unitOfMeasurement
	 */
	@Override
	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	@Override
	public String getName() {
		return name();
	}

}
