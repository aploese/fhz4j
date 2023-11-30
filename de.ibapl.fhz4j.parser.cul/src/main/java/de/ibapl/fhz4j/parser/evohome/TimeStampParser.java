/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2023, Arne Plöse and individual contributors as indicated
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
import java.time.LocalDateTime;
import java.time.Month;

/**
 *
 * @author aploese
 */
public class TimeStampParser extends AbstractParser {

    enum State {

        SECOND,
        MINUTE,
        HOUR,
        DAY_OF_MONTH,
        MONTH,
        YEAR,
        PARSE_SUCCESS, PARSE_ERROR;

    }

    @Override
    public void parse(byte b) {
        switch (state) {
            case SECOND:
                //TODO upper bit DST -> DaylightSavingTime?
                second = b & 0x7f;
                state = State.MINUTE;
                break;
            case MINUTE:
                minute = b & 0xff;
                state = State.HOUR;
                break;
            case HOUR:
                hour = b & 0x1f;
                state = State.DAY_OF_MONTH;
                break;
            case DAY_OF_MONTH:
                dayOfMonth = b & 0xff;
                state = State.MONTH;
                break;
            case MONTH:
                month = Month.of(b & 0xff);
                setStackSize(2);
                state = State.YEAR;
                break;
            case YEAR:
                if (push(b)) {
                    year = getIntValue();
                    state = State.PARSE_SUCCESS;
                }
                break;
            case PARSE_SUCCESS:
                throw new RuntimeException("PARSE_SUCCESS should not be called");
            case PARSE_ERROR:
                throw new RuntimeException("PARSE_ERROR should not be called");

        }
    }

    State state;
    private int year;
    private Month month;
    private int dayOfMonth;
    private int hour;
    private int minute;
    private int second;

    @Override
    public void init() {
        state = State.SECOND;
        year = 0;
        month = null;
        dayOfMonth = 0;
        hour = 0;
        minute = 0;
        second = 0;
    }

    public LocalDateTime getTimestamp() {
        if (state != State.PARSE_SUCCESS) {
            throw new RuntimeException("State: " + state);
        }
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
    }

}
