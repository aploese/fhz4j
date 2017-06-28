package de.ibapl.fhz4j.fht;

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

import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import de.ibapl.fhz4j.Fhz1000;
import de.ibapl.fhz4j.FhzMessage;
import de.ibapl.fhz4j.scada.Time;

/**
 *
 * @author aploese
 */
public class FhtMessage extends FhzMessage<FhtProperty> {

    private short housecode;
    private FhtProperty command;
    private byte description;
    private int rawvalue;

    @Override
    public void toString(StringBuilder sb) {
        sb.append("housecode: ").append(Fhz1000.houseCodeToString(housecode));
        sb.append(", command: ").append(command.getLabel());
        sb.append(", description: ").append(getOriginAsString());
        sb.append(", value: ");
        switch (command) {
            case VALVE:
            case VALVE_1:
            case VALVE_2:
            case VALVE_3:
            case VALVE_4:
            case VALVE_5:
            case VALVE_6:
            case VALVE_7:
            case VALVE_8:
                sb.append(String.format((Locale) null, "%.1f", getActuatorValue()));
                break;
            case DESIRED_TEMP:
            case WINDOW_OPEN_TEMP:
            case LOW_TEMP_OFFSET:
                sb.append(getDesiredTempValue());
                break;
            case MEASURED_LOW:
                sb.append(getLowTempValue());
                break;
            case MEASURED_HIGH:
                sb.append(getHighTempValue());
                break;
            case MO_FROM_1:
            case MO_TO_1:
            case MO_FROM_2:
            case MO_TO_2:
            case TUE_FROM_1:
            case TUE_TO_1:
            case TUE_FROM_2:
            case TUE_TO_2:
            case WED_FROM_1:
            case WED_TO_1:
            case WED_FROM_2:
            case WED_TO_2:
            case THU_FROM_1:
            case THU_TO_1:
            case THU_FROM_2:
            case THU_TO_2:
            case FRI_FROM_1:
            case FRI_TO_1:
            case FRI_FROM_2:
            case FRI_TO_2:
            case SAT_FROM_1:
            case SAT_TO_1:
            case SAT_FROM_2:
            case SAT_TO_2:
            case SUN_FROM_1:
            case SUN_TO_1:
            case SUN_FROM_2:
            case SUN_TO_2:
                sb.append(getTime());
                sb.append(" ");
                break;
            default:
                sb.append(getRawValue());
        }

        sb.append(command.getUnitOfMeasurement());
    }

    public String getOriginAsString() {
        StringBuilder sb = new StringBuilder();
        switch (command) {
            case VALVE:
            case VALVE_1:
            case VALVE_2:
            case VALVE_3:
            case VALVE_4:
            case VALVE_5:
            case VALVE_6:
            case VALVE_7:
            case VALVE_8:
                if ((description & 0x2c) == 0x2c) {
                    return " sync";
                }
                if ((description & 0xa0) == 0xa0) {
                    sb.append("same");
                } else if ((description & 0xb0) == 0xb0) {
                    sb.append("same (b)");
                } else if ((description & 0x20) == 0x20) {
                    sb.append("changed");
                } else {
                    sb.append(String.format(" 0x%01x", description >> 4));
                }
                if ((description & 0x06) == 0x06) {
                    sb.append(" pos absolute");
                } else {
                    sb.append(String.format(" 0x%01x", description & 0x0f));
                }
                break;
            default:
                if ((description & 0x60) == 0x60) {
                    sb.append("from FHT-B");
                } else if ((description & 0x70) == 0x70) {
                    sb.append("from FHZ");
                } else {
                    sb.append(String.format(" 0x%01x", description >> 4));
                }
                if ((description & 0x07) == 0x07) {
                    sb.append(" protocoll");
                } else if ((description & 0x09) == 0x09) {
                    sb.append(" data register");
                } else {
                    sb.append(String.format(" 0x%01x", description & 0x0f));
                }
        }
        return sb.toString();
    }

    /**
     * @return the housecode
     */
    public short getHousecode() {
        return housecode;
    }

    /**
     * @param housecode the housecode to set
     */
    public void setHousecode(short housecode) {
        this.housecode = housecode;
    }

    /**
     * @return the command
     */
    public FhtProperty getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(FhtProperty command) {
        this.command = command;
    }

    /**
     * @return the description
     */
    public byte getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(byte description) {
        this.description = description;
    }

    /**
     * @return the value
     */
    public int getRawValue() {
        return rawvalue;
    }

    public Time getTime() {
        Time result = new Time();
        result.setHour((byte) (rawvalue / 6));
        result.setMin((byte) ((rawvalue % 6) * 10));
        return result;
    }

    public float getDesiredTempValue() {
        return 0.5f * rawvalue;
    }

    public float getLowTempValue() {
        return 0.1f * rawvalue;
    }

    public float getHighTempValue() {
        return (float)(25.6f * rawvalue);
    }

    public float getActuatorValue() {
        //100.0 / 255.0 = 0,392156863;
        return 0.392156863f * rawvalue;
    }

    public void setRawValue(int value) {
        this.rawvalue = value;
    }

    public Time getTimeValue(FhtProperty property) {
        switch (property) {
            default:
                throw new RuntimeException("Not implemented" + property);
        }

    }

    @Override
    public short getShort(FhtProperty property) {
        switch (property) {
            case DAY:
            case HOUR:
            case MINUTE:
            case MODE:
            case MONTH:
            case REPORT_1:
            case REPORT_2:
            case YEAR:
                return (short) rawvalue;
            case HOLIDAY_1:
            case HOLIDAY_2:
                //raw values 
                return (short) rawvalue;
            default:
                return super.getShort(property);
        }
    }

    @Override
    public byte getByte(FhtProperty property) {
        switch (property) {
            case WARNINGS:
            case UNKNOWN:
            case UNKNOWN_0XFF:
                return (byte) rawvalue;
            default:
                return super.getByte(property);
        }
    }

    @Override
    public Time getTime(FhtProperty property) {
        switch (property) {
            case FRI_FROM_1:
            case FRI_FROM_2:
            case FRI_TO_1:
            case FRI_TO_2:
            case MO_FROM_1:
            case MO_FROM_2:
            case MO_TO_1:
            case MO_TO_2:
            case SAT_FROM_1:
            case SAT_FROM_2:
            case SAT_TO_1:
            case SAT_TO_2:
            case SUN_FROM_1:
            case SUN_FROM_2:
            case SUN_TO_1:
            case SUN_TO_2:
            case THU_FROM_1:
            case THU_FROM_2:
            case THU_TO_1:
            case THU_TO_2:
            case TUE_FROM_1:
            case TUE_FROM_2:
            case TUE_TO_1:
            case TUE_TO_2:
            case WED_FROM_1:
            case WED_FROM_2:
            case WED_TO_1:
            case WED_TO_2:
                return getTime();
            default:
                return super.getTime(property);
        }
    }

    @Override
    public float getFloat(FhtProperty property) {
        switch (property) {
            case VALVE:
            case VALVE_1:
            case VALVE_2:
            case VALVE_3:
            case VALVE_4:
            case VALVE_5:
            case VALVE_6:
            case VALVE_7:
            case VALVE_8:
                return getActuatorValue();
            case DAY_TEMP:
            case DESIRED_TEMP:
            case NIGHT_TEMP:
            case MANU_TEMP:
            case WINDOW_OPEN_TEMP:
            case LOW_TEMP_OFFSET:
                return getDesiredTempValue();
            case MEASURED_HIGH:
                return getHighTempValue();
            case MEASURED_LOW:
                return getLowTempValue();
            default:
                return super.getFloat(property);
        }
    }

    @Override
    public void setFloat(FhtProperty property, float value) {
        switch (property) {
            case VALVE:
            case VALVE_1:
            case VALVE_2:
            case VALVE_3:
            case VALVE_4:
            case VALVE_5:
            case VALVE_6:
            case VALVE_7:
            case VALVE_8:
                this.command = property;
                setActuatorValue(value);
                break;
            case DAY_TEMP:
            case DESIRED_TEMP:
            case NIGHT_TEMP:
            case MANU_TEMP:
            case WINDOW_OPEN_TEMP:
            case LOW_TEMP_OFFSET:
                this.command = property;
                setDesiredTempValue(value);
                break;
            case MEASURED_HIGH:
                this.command = property;
                setHighTempValue(value);
                break;
            case MEASURED_LOW:
                this.command = property;
                setLowTempValue(value);
                break;
            default:
                super.setFloat(property, value);
        }
    }

    @Override
    public Set<FhtProperty> getSupportedProperties() {
        return EnumSet.of(command);
    }

    private void setActuatorValue(float value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setDesiredTempValue(float value) {
        rawvalue = Math.round(value * 2.0f);
    }

    private void setHighTempValue(float value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setLowTempValue(float value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getHousecodeStr() {
        return Fhz1000.houseCodeToString(housecode);
    }
}
