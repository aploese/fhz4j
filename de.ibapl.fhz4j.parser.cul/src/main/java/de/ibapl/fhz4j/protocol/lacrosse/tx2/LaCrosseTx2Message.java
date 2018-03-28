/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2018 Arne Plöse
 * %%
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009, 2017, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.lacrosse.tx2;

import de.ibapl.fhz4j.api.FhzMessage;
import de.ibapl.fhz4j.api.FhzProtocol;

/**
 *
 * @author Arne Plöse
 */
public class LaCrosseTx2Message extends FhzMessage {

	public LaCrosseTx2Property laCrosseTx2Property;
	public short address;
	public float value;

	public LaCrosseTx2Message(LaCrosseTx2Property laCrosseTx2Property) {
		super(FhzProtocol.LA_CROSSE_TX2);
		this.laCrosseTx2Property = laCrosseTx2Property;
	}

}
