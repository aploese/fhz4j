/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2023-2024, Arne Plöse and individual contributors as indicated
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
import java.time.LocalTime;

/**
 *
 * @author aploese
 */
public class TimeParser extends AbstractParser {

    public boolean isFF_FF() {
        return (minutes == -1) && (hours == -1);
    }

    enum State {

        /**
         *
         */
        COLLECT_MINUTES,
        /**
         *
         */
        COLLECT_HOURS,
        /**
         *
         */
        PARSE_SUCCESS, PARSE_ERROR;

    }

    @Override
    public void parse(byte b) {
        switch (state) {
            case COLLECT_MINUTES -> {
                minutes = b;
                state = State.COLLECT_HOURS;
            }
            case COLLECT_HOURS -> {
                hours = b;
                state = State.PARSE_SUCCESS;
            }
            case PARSE_SUCCESS ->
                throw new IllegalStateException("PARSE_SUCCESS should not be called");
            case PARSE_ERROR ->
                throw new IllegalStateException("PARSE_ERROR should not be called");
            default ->
                throw new IllegalStateException(state.name());

        }
    }

    State state;
    private int minutes;
    private int hours;

    @Override
    public void init() {
        state = State.COLLECT_MINUTES;
        minutes = 0;
        hours = 0;
    }

    public LocalTime getTime() {
        if (state != State.PARSE_SUCCESS) {
            throw new RuntimeException("State: " + state);
        }
        return LocalTime.of(hours, minutes, 0);
    }

}
