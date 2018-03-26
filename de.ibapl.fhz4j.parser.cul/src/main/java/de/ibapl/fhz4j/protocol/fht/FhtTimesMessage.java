package de.ibapl.fhz4j.protocol.fht;

/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2017 Arne Plöse
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

import java.time.LocalTime;

/**
 *
 * @author aploese
 */
public class FhtTimesMessage extends Fht8bMessage {
    
    public LocalTime timeFrom1;
    public LocalTime timeTo1;
    public LocalTime timeFrom2;
    public LocalTime timeTo2;

    public FhtTimesMessage(short housecode, FhtProperty command, boolean fromFht_8B, boolean dataRegister, LocalTime timeFrom1, LocalTime timeTo1, LocalTime timeFrom2, LocalTime timeTo2) {
        super(housecode, command,fromFht_8B, dataRegister);
        this.timeFrom1 = timeFrom1;
        this.timeTo1 = timeTo1;
        this.timeFrom2 = timeFrom2;
        this.timeTo2 = timeTo2;
    }

}
