/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.cul;

import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author aploese
 */
public class CulSetSlowRfSettingsRequest extends CulRequest implements Iterable<SlowRfFlag> {

    private final Set<SlowRfFlag> initFlags;

    public CulSetSlowRfSettingsRequest(Set<SlowRfFlag> initFlags) {
        this.initFlags = initFlags;
    }

    @Override
    public Iterator<SlowRfFlag> iterator() {
        return initFlags.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{culrequest : \"FHT device output buffer content\"");
        sb.append(", slowRfFlags : ");
        boolean first = true;
        for (SlowRfFlag slowRfFlag : initFlags) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(slowRfFlag);
        }
        sb.append("]}");
        return sb.toString();
    }

}
