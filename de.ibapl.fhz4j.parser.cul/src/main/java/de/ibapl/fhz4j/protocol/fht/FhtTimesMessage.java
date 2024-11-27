/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2018-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.fht;

import java.time.LocalTime;
import java.util.Objects;

/**
 *
 * @author Arne Plöse
 * @param <T>
 */
public class FhtTimesMessage<T extends FhtTimesMessage<T>> extends Fht8bMessage<T> {

    public LocalTime timeFrom1;
    public LocalTime timeTo1;
    public LocalTime timeFrom2;
    public LocalTime timeTo2;

    public FhtTimesMessage(short housecode, FhtProperty command, byte description, boolean fromFht_8B, boolean dataRegister,
            LocalTime timeFrom1, LocalTime timeTo1, LocalTime timeFrom2, LocalTime timeTo2) {
        super(housecode, command, description, fromFht_8B, dataRegister);
        this.timeFrom1 = timeFrom1;
        this.timeTo1 = timeTo1;
        this.timeFrom2 = timeFrom2;
        this.timeTo2 = timeTo2;
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(", timeFrom1 : ").append(timeFrom1);
        sb.append(", timeTo1 : ").append(timeTo1);
        sb.append(", timeFrom2 : ").append(timeFrom2);
        sb.append(", timeTo2 : ").append(timeTo2);
    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.timeFrom1);
        hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.timeTo1);
        hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.timeFrom2);
        return HASH_MULTIPLIER * hash + Objects.hashCode(this.timeTo2);
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        if (!Objects.equals(this.timeFrom1, other.timeFrom1)) {
            return false;
        }
        if (!Objects.equals(this.timeTo1, other.timeTo1)) {
            return false;
        }
        if (!Objects.equals(this.timeFrom2, other.timeFrom2)) {
            return false;
        }
        return Objects.equals(this.timeTo2, other.timeTo2);
    }

}
