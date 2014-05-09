/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.fs20;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import net.sf.fhz4j.scada.DataType;
import net.sf.fhz4j.scada.ScadaProperty;
import static net.sf.fhz4j.scada.DataType.*;

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
