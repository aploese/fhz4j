package de.ibapl.fhz4j.protocol.fs20;

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


import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import de.ibapl.fhz4j.scada.DataType;
import de.ibapl.fhz4j.scada.ScadaProperty;
import static de.ibapl.fhz4j.scada.DataType.*;

/**
 *
 * @author aploese
 */
public enum FS20DeviceType implements ScadaProperty {

    SWITCH_DIMMABLE(FS20CommandValues.OFF, FS20CommandValues.DIM_DOWN, FS20CommandValues.DIM_UP, FS20CommandValues.OFF);

    final private Set<FS20CommandValues> commands;

    private FS20DeviceType(FS20CommandValues... commands) {
        this.commands = EnumSet.copyOf(Arrays.asList(commands));
    }

    @Override
    public String getUnitOfMeasurement() {
        return "";
    }

    @Override
    public DataType getDataType() {
        return BYTE;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getLabel() {
        return name();
    }

    public static FS20DeviceType valueOf(FS20CommandValues command) {
        switch (command) {
            case OFF:
                return SWITCH_DIMMABLE;
            case DIM_DOWN:
                return SWITCH_DIMMABLE;
            case DIM_UP:
                return SWITCH_DIMMABLE;
            case ON:
                return SWITCH_DIMMABLE;
            default:
                throw new RuntimeException("Unkown command " + command);
        }
    }
}
