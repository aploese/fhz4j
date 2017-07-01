package de.ibapl.fhz4j;

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


import de.ibapl.fhz4j.em.EmMessage;
import de.ibapl.fhz4j.fht.FhtMultiMsgMessage;
import de.ibapl.fhz4j.fht.FhtMessage;
import de.ibapl.fhz4j.fs20.FS20Message;
import de.ibapl.fhz4j.hms.HmsMessage;

/**
 *
 * @author aploese
 */
public interface FhzDataListener {

    void emDataParsed(EmMessage emMsg);

    void fs20DataParsed(FS20Message fs20Msg);

    void fhtDataParsed(FhtMessage fhtMessage);

    void fhtMultiMsgParsed(FhtMultiMsgMessage fhtMultiMsgMessage);

    void hmsDataParsed(HmsMessage hmsMsg);

}