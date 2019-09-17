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
package de.ibapl.fhz4j.cul;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.api.Adapter;
import de.ibapl.fhz4j.parser.cul.CulParser;
import de.ibapl.fhz4j.writer.cul.CulWriter;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.spsw.api.DataBits;
import de.ibapl.spsw.api.FlowControl;
import de.ibapl.spsw.api.Parity;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.Speed;
import de.ibapl.spsw.api.StopBits;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import de.ibapl.fhz4j.api.FhzHandler;
import de.ibapl.fhz4j.api.EvoHomeHandler;
import java.io.InterruptedIOException;

public class CulAdapter implements Adapter, FhzHandler, EvoHomeHandler {

    private class StreamListener implements Runnable {

        @Override
        public void run() {
            LOG.log(Level.FINE, "THREAD START {0}", open);
            while (open) {
                try {
                    serialPortSocket.read(inBuffer);
                    inBuffer.flip();
                    while (inBuffer.hasRemaining()) {
                        try {
                            culParser.parse((char) inBuffer.get());
                        } catch (Exception e) {
                            LOG.log(Level.SEVERE, "caught unexcpected exception during waiting for packages", e);
                        }
                    }
                    inBuffer.clear();
                } catch (InterruptedIOException iioe) {
                    if (CulAdapter.this.open) {
                        CulAdapter.this.fhzDataListener.onIOException(iioe);
                    }
                } catch (Throwable t) {
                    LOG.log(Level.SEVERE, "caught unexcpected exception during waiting for packages", t);
                }
            }
            LOG.info("closing down - finish waiting for new data");
        }

    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);

    private CulParser<?> culParser;
    private CulWriter culWriter;
    private final CulMessageListener fhzDataListener;
    private final ByteBuffer inBuffer = ByteBuffer.allocateDirect(64);
    private boolean open;
    private Thread parserThread;
    private final SerialPortSocket serialPortSocket;
    private final StreamListener streamListener = new StreamListener();

    public CulAdapter(SerialPortSocket serialPortSocket, CulMessageListener fhzDataListener) {
        this.serialPortSocket = serialPortSocket;
        this.fhzDataListener = fhzDataListener;
    }

    @Override
    public void close() throws Exception {
        if (open) {
            open = false;
            try {
                culWriter.close();
            } finally {
                serialPortSocket.close();
            }
        }
    }

    @Override
    public void initFhtReporting(Set<Short> housecode) throws IOException {
        culWriter.initFhtReporting(housecode);
    }

    @Override
    public void initFhtReporting(short housecode) throws IOException {
        culWriter.initFhtReporting(housecode);
    }

    @Override
    public void initFhz(short fhzHousecode) throws IOException {
        culWriter.initFhz(fhzHousecode);
    }

    @Override
    public void open() throws IOException {
        serialPortSocket.open(Speed._9600_BPS, DataBits.DB_8, StopBits.SB_1, Parity.NONE, FlowControl.getFC_NONE());
        open = true;
        culParser = new CulParser<>(fhzDataListener);
        culWriter = new CulWriter(serialPortSocket, 64);
        parserThread = new Thread(streamListener);
        parserThread.setDaemon(true);
        parserThread.start();
    }

    @Override
    public void writeFht(short housecode, FhtProperty fhtProperty, float value) throws IOException {
        culWriter.writeFht(housecode, fhtProperty, value);
    }

    @Override
    public void writeFhtCycle(short housecode, DayOfWeek dayOfWeek, LocalTime from1, LocalTime to1, LocalTime from2,
            LocalTime to2) throws IOException {
        culWriter.writeFhtCycle(housecode, dayOfWeek, from1, to1, from2, to2);
    }

    @Override
    public void writeFhtModeHoliday(short housecode, float temp, LocalDate date) throws IOException {
        culWriter.writeFhtModeHoliday(housecode, temp, date);
    }

    @Override
    public void writeFhtModeParty(short housecode, float temp, LocalDateTime to) throws IOException {
        culWriter.writeFhtModeParty(housecode, temp, to);
    }

    @Override
    public void writeFhtModeAuto(short housecode) throws IOException {
        culWriter.writeFhtModeAuto(housecode);
    }

    @Override
    public void writeFhtModeManu(short housecode) throws IOException {
        culWriter.writeFhtModeManu(housecode);
    }

    @Override
    public void writeFhtTimeAndDate(short housecode, LocalDateTime ts) throws IOException {
        culWriter.writeFhtTimeAndDate(housecode, ts);
    }

    @Override
    public void initEvoHome() throws IOException {
        culWriter.initEvoHome();
        //TODO wait for "va" for success or handle error
    }

}
