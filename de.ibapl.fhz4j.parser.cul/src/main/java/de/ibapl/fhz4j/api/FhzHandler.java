/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2019, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.api;

import de.ibapl.fhz4j.cul.SlowRfFlag;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public interface FhzHandler extends Adapter {

    void initFhtReporting(Set<Short> housecode) throws IOException;

    void initFhtReporting(short housecode) throws IOException;

    void initFhz(short fhzHousecode) throws IOException;

    void initFhz(short fhzHousecode, Set<SlowRfFlag> slowRfFlags) throws IOException;

    void writeFht(short housecode, FhtProperty fhtProperty, float value) throws IOException;

    void writeCulTimeSlotRequest() throws IOException;

    void gatherCulDebugInfos() throws IOException;

    void writeFhtCycle(short housecode, DayOfWeek dayOfWeek, LocalTime from1, LocalTime to1, LocalTime from2,
            LocalTime to2) throws IOException;

    void writeFhtModeHoliday(short housecode, float temp, LocalDate date) throws IOException;

    void writeFhtModeParty(short housecode, float temp, LocalDateTime to) throws IOException;

    void writeFhtModeAuto(short housecode) throws IOException;

    void writeFhtModeManu(short housecode) throws IOException;

    void writeFhtTimeAndDate(short housecode, LocalDateTime ts) throws IOException;
}
