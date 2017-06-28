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

import java.util.Calendar;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import de.ibapl.fhz4j.Fhz1000;
import de.ibapl.fhz4j.scada.AbstractValueAccessor;
import de.ibapl.fhz4j.scada.Date;
import de.ibapl.fhz4j.scada.Time;
import de.ibapl.fhz4j.scada.Timestamp;

/**
 * A message that consists of multiple messages ...
 *
 * @author aploese
 */
public class FhtMultiMsgMessage extends AbstractValueAccessor<FhtMultiMsgProperty> {

    private final Map<FhtProperty, FhtMessage> messages = new EnumMap<>(FhtProperty.class);
    private final short houseCode;
    private final FhtMultiMsgProperty property;

    public FhtMultiMsgMessage(FhtMultiMsgProperty prop, FhtMessage... msg) {
        this.houseCode = msg[0].getHousecode();
        this.property = prop;
        for (FhtMessage m : msg) {
            messages.put(m.getCommand(), m);
        }
    }

    public short getHousecode() {
        return houseCode;
    }

    @Override
    public Date getDate(FhtMultiMsgProperty property) {
        switch (property) {
            case HOLIDAY_END:
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, messages.get(FhtProperty.HOLIDAY_1).getRawValue());
                if (messages.get(FhtProperty.HOLIDAY_2).getRawValue() >= c.get(Calendar.MONTH) + 1) {
                    c.set(Calendar.MONTH, messages.get(FhtProperty.HOLIDAY_2).getRawValue() - 1);
                } else {
                    c.set(Calendar.MONTH, messages.get(FhtProperty.HOLIDAY_2).getRawValue() - 1);
                    c.add(Calendar.YEAR, 1);
                }
                return new Date(c);
            default:
                return super.getDate(property);
        }
    }

    @Override
    public Timestamp getTimestamp(FhtMultiMsgProperty property) {
        switch (property) {
            case PARTY_END:
                Calendar c = Calendar.getInstance();
                Time t = messages.get(FhtProperty.HOLIDAY_1).getTime();
                t.setTimeToCalendar(c);
                if (messages.get(FhtProperty.HOLIDAY_2).getRawValue() >= c.get(Calendar.DAY_OF_MONTH)) {
                    c.set(Calendar.DAY_OF_MONTH, messages.get(FhtProperty.HOLIDAY_2).getRawValue());
                } else {
                    c.set(Calendar.DAY_OF_MONTH, messages.get(FhtProperty.HOLIDAY_2).getRawValue());
                    c.add(Calendar.MONTH, 1);
                }
            return new Timestamp(c);
            default:
                return super.getTimestamp(property);
        }
    }

    @Override
    public float getFloat(FhtMultiMsgProperty property) {
        switch (property) {
            case TEMP:
                return ((float) (messages.get(FhtProperty.MEASURED_LOW).getRawValue() + messages.get(FhtProperty.MEASURED_HIGH).getRawValue() * 256) * 0.1f);
            default:
                return super.getFloat(property);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("housecode: ").append(Fhz1000.houseCodeToString(houseCode));
        switch (property) {
            case TEMP:
                sb.append(", measured temperature (combined): ").append(getFloat(FhtMultiMsgProperty.TEMP));
                break;
            case HOLIDAY_END:
                sb.append(", holiday (combined): ").append(asString(FhtMultiMsgProperty.HOLIDAY_END));
                break;
            case PARTY_END:
                sb.append(", party end (combined): ").append(asString(FhtMultiMsgProperty.PARTY_END));
                break;
            default:
                throw new IllegalArgumentException();
        }
        sb.append(property.getUnitOfMeasurement());
        return sb.toString();
    }

    public FhtMultiMsgProperty getProperty() {
        return property;
    }

    @Override
    public Set<FhtMultiMsgProperty> getSupportedProperties() {
        return EnumSet.allOf(FhtMultiMsgProperty.class);
    }

    public String getHousecodeStr() {
        return Fhz1000.houseCodeToString(houseCode);
    }

}
