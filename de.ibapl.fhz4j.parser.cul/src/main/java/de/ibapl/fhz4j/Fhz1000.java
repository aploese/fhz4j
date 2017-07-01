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


/**
 *
 * @author aploese
 */
public class Fhz1000 {

    public static String houseCodeToString(short housecode) {
        StringBuilder sb = new StringBuilder(4);
        sb.append(String.format("%02d", (housecode >> 8) & 0x00FF));
        sb.append(String.format("%02d", housecode & 0x000FF));
        return sb.toString();
    }

    public static short parseHouseCode(String housecode) {
        if (housecode.length() > 4) {
            throw new IllegalArgumentException(String.format("Houscode %s too long: ", housecode));
        }
        while (housecode.length() < 4) {
            housecode = '0' +housecode;
        }
        short result = (short)( Short.parseShort(housecode.substring(0, 2)) << 8);
        result |= (short)( Short.parseShort(housecode.substring(2, 4)));
        return result;
    }

}
