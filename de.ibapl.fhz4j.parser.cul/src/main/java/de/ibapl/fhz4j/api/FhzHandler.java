/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2019-2024, Arne Plöse and individual contributors as indicated
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
import de.ibapl.fhz4j.protocol.fht.Fht80TfValue;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public interface FhzHandler extends Adapter {

    class ResponseFuture<T extends Response> implements Future<T>, Consumer<T> {

        private T result;

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isCancelled() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isDone() {
            return result != null;
        }

        @Override
        public synchronized T get() throws InterruptedException, ExecutionException {
            if (isDone()) {
                return result;
            } else {
                wait();
                return result;
            }
        }

        @Override
        public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (isDone()) {
                return result;
            } else {
                switch (unit) {
                    case MILLISECONDS -> {
                        wait(timeout);
                        return result;
                    }
                    case SECONDS -> {
                        wait(timeout * 1000);
                        return result;
                    }
                    default ->
                        throw new IllegalArgumentException("Only ms and s are allowed");
                }
            }
        }

        @Override
        public synchronized void accept(T t) {
            result = t;
            notifyAll();
        }

    }

    void initFhtReporting(Set<Short> housecode) throws IOException;

    void initFhtReporting(short housecode) throws IOException;

    void initFhz(short fhzHousecode) throws IOException;

    void initFhz(short fhzHousecode, Set<SlowRfFlag> slowRfFlags) throws IOException;

    void writeFht(short housecode, FhtProperty fhtProperty, float value) throws IOException;

    void writeFht80Tf(int address, Fht80TfValue fht80TfValue, boolean lowBattery) throws IOException;

    void writeCulTimeSlotRequest() throws IOException;

    void gatherCulDebugInfos() throws IOException;

    Future<Response> sendRequest(Request request) throws IOException;

    void sendRequest(Request request, Consumer<Response> c) throws IOException;

    void writeFhtCycle(short housecode, DayOfWeek dayOfWeek, LocalTime from1, LocalTime to1, LocalTime from2,
            LocalTime to2) throws IOException;

    void writeFhtModeHoliday(short housecode, float temp, LocalDate date) throws IOException;

    void writeFhtModeParty(short housecode, float temp, LocalDateTime to) throws IOException;

    void writeFhtModeAuto(short housecode) throws IOException;

    void writeFhtModeManu(short housecode) throws IOException;

    void writeFhtTimeAndDate(short housecode, LocalDateTime ts) throws IOException;
}
