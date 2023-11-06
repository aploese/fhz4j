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
package de.ibapl.fhz4j.parser.evohome;

import de.ibapl.fhz4j.parser.api.AbstractParser;
import java.time.LocalDate;
import java.time.Month;

/**
 *
 * @author aploese
 */
public class DateParser extends AbstractParser {

    public boolean isFF_FF_FFFF() {
        return (dayOfMonth == -1) && (month == null) && (year == 0xffff);
    }

    enum State {

        /**
         *
         */
        COLLECT_DAY_OF_MONTH,
        /**
         *
         */
        COLLECT_MONTH,
        /**
         *
         */
        COLLECT_YEAR_HIGH,
        /**
         *
         */
        COLLECT_YEAR_LOW,
        /**
         *
         */
        PARSE_SUCCESS, PARSE_ERROR;

    }

    @Override
    public void parse(byte b) {
        switch (state) {
            case COLLECT_DAY_OF_MONTH:
                dayOfMonth = b;
                state = State.COLLECT_MONTH;
                break;
            case COLLECT_MONTH:
                if (b == -1) {
                    month = null;
                } else {
                    month = Month.of(b);
                }
                setStackSize(2);
                state = State.COLLECT_YEAR_HIGH;
                break;
            case COLLECT_YEAR_HIGH:
                push(b);
                state = State.COLLECT_YEAR_LOW;
                break;
            case COLLECT_YEAR_LOW:
                push(b);
                year = getIntValue();
                state = State.PARSE_SUCCESS;
                break;
            case PARSE_SUCCESS:
                throw new RuntimeException("PARSE_SUCCESS should not be called");
            case PARSE_ERROR:
                throw new RuntimeException("PARSE_ERROR should not be called");

        }
    }

    State state;
    private int dayOfMonth;
    private Month month;
    private int year;

    @Override
    public void init() {
        state = State.COLLECT_DAY_OF_MONTH;
        dayOfMonth = 0;
        month = null;
        year = 0;
    }

    public LocalDate getDate() {
        if (state != State.PARSE_SUCCESS) {
            throw new RuntimeException("State: " + state);
        }
        return LocalDate.of(year, month, dayOfMonth);
    }

}
