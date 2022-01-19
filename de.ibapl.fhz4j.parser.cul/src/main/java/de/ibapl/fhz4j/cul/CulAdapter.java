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
package de.ibapl.fhz4j.cul;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.api.Adapter;
import de.ibapl.fhz4j.api.EvoHomeHandler;
import de.ibapl.fhz4j.api.FhzHandler;
import de.ibapl.fhz4j.api.Request;
import de.ibapl.fhz4j.api.Response;
import de.ibapl.fhz4j.parser.cul.CulParser;
import de.ibapl.fhz4j.protocol.evohome.DeviceId;
import de.ibapl.fhz4j.protocol.evohome.ZoneTemperature;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtTfValue;
import de.ibapl.fhz4j.writer.cul.CulWriter;
import de.ibapl.fhz4j.writer.evohome.EvoHomeEncoder;
import de.ibapl.fhz4j.writer.fht.FhtEncoder;
import de.ibapl.spsw.api.SerialPortSocket;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private FhtEncoder fhtEncoder;
    private EvoHomeEncoder evoHomeEncoder;
    private final CulMessageListener fhzDataListener;
    private final ByteBuffer inBuffer = ByteBuffer.allocateDirect(64);
    private boolean open;
    private Thread parserThread;
    private SerialPortSocket serialPortSocket;
    private final StreamListener streamListener = new StreamListener();

    public CulAdapter(SerialPortSocket serialPortSocket, CulMessageListener fhzDataListener) {
        if (!serialPortSocket.isOpen()) {
            throw new IllegalStateException("serial port " + serialPortSocket.getPortName() + " is not open");
        }
        this.serialPortSocket = serialPortSocket;
        this.fhzDataListener = fhzDataListener;
        culParser = new CulParser<>(fhzDataListener);
        culWriter = new CulWriter(serialPortSocket, CulWriter.DEFAULT_BUFFER_SIZE);
        fhtEncoder = new FhtEncoder(culWriter);
        evoHomeEncoder = new EvoHomeEncoder(culWriter);
        parserThread = new Thread(streamListener);
        parserThread.setDaemon(true);
        //mark this as open, so the streamListener will not falsely close down imediately
        open = true;
        parserThread.start();
    }

    @Override
    public void close() throws Exception {
        if (open) {
            open = false;
            try {
                parserThread.interrupt();
                culWriter.close();
                evoHomeEncoder = null;
                fhtEncoder = null;
                parserThread = null;
                culWriter = null;
                culParser = null;
            } finally {
                serialPortSocket.close();
                serialPortSocket = null;
            }
        }
    }

    @Override
    public void initFhtReporting(Set<Short> housecode) throws IOException {
        fhtEncoder.initFhtReporting(housecode);
    }

    @Override
    public void initFhtReporting(short housecode) throws IOException {
        fhtEncoder.initFhtReporting(housecode);
    }

    @Override
    public void initFhz(short fhzHousecode) throws IOException {
        culWriter.initFhz(fhzHousecode);
    }

    @Override
    public void initFhz(short fhzHousecode, Set<SlowRfFlag> slowRfFlags) throws IOException {
        culWriter.initFhz(fhzHousecode, slowRfFlags);
    }

    @Override
    public void writeCulTimeSlotRequest() throws IOException {
        final CulGetSlowRfSettingsRequest request = new CulGetSlowRfSettingsRequest();
        culParser.addCulRequest(request, null);
        culWriter.writeCulRequest(request);
    }

    @Override
    public void gatherCulDebugInfos() throws IOException {
        Queue<CulRequest> requests = new LinkedList<>();
        requests.add(new CulGetSlowRfSettingsRequest());
        requests.add(new CulFhtDeviceOutBufferContentRequest());
        requests.add(new CulRemainingFhtDeviceOutBufferSizeRequest());
        culParser.addCulRequests(requests, null);
        culWriter.writeCulRequests(requests);
    }

    @Override
    public void sendRequest(Request request, Consumer<Response> c) throws IOException {
        if (request instanceof CulRequest) {
            culParser.addCulRequest((CulRequest) request, c);
            culWriter.writeCulRequest((CulRequest) request);
        } else {
            throw new IllegalArgumentException("Cant handle request " + request);
        }
    }

    @Override
    public Future<Response> sendRequest(Request request) throws IOException {
        if (request instanceof CulRequest) {

            ResponseFuture f = new ResponseFuture();

            culParser.addCulRequest((CulRequest) request, f);
            culWriter.writeCulRequest((CulRequest) request);

            return f;
        } else {
            throw new IllegalArgumentException("Cant handle request " + request);
        }

    }

    @Override
    public void writeFht(short housecode, FhtProperty fhtProperty, float value) throws IOException {
        fhtEncoder.writeFht(housecode, fhtProperty, value);
    }

    @Override
    public void writeFhtTf(int address, FhtTfValue fhtTfValue, boolean lowBattery) throws IOException {
        fhtEncoder.writeFhtTf(address, fhtTfValue, lowBattery);
    }

    @Override
    public void writeFhtCycle(short housecode, DayOfWeek dayOfWeek, LocalTime from1, LocalTime to1, LocalTime from2,
            LocalTime to2) throws IOException {
        fhtEncoder.writeFhtCycle(housecode, dayOfWeek, from1, to1, from2, to2);
    }

    @Override
    public void writeFhtModeHoliday(short housecode, float temp, LocalDate date) throws IOException {
        fhtEncoder.writeFhtModeHoliday(housecode, temp, date);
    }

    @Override
    public void writeFhtModeParty(short housecode, float temp, LocalDateTime to) throws IOException {
        fhtEncoder.writeFhtModeParty(housecode, temp, to);
    }

    @Override
    public void writeFhtModeAuto(short housecode) throws IOException {
        fhtEncoder.writeFhtModeAuto(housecode);
    }

    @Override
    public void writeFhtModeManu(short housecode) throws IOException {
        fhtEncoder.writeFhtModeManu(housecode);
    }

    @Override
    public void writeFhtTimeAndDate(short housecode, LocalDateTime ts) throws IOException {
        fhtEncoder.writeFhtTimeAndDate(housecode, ts);
    }

    @Override
    public void initEvoHome() throws IOException {
        culWriter.initEvoHome();
        //TODO wait for "va" for success or handle error
    }

    public void writeEvoHomeZoneSetpointPermanent(DeviceId deviceId, ZoneTemperature temperature) throws IOException {
        evoHomeEncoder.writeEvoHomeZoneSetpointPermanent(deviceId, temperature);
        //TODO wait for "va" for success or handle error
    }

    public void writeEvoHomeZoneSetpointUntil(DeviceId deviceId, ZoneTemperature temperature, LocalDateTime localDateTime) throws IOException {
        evoHomeEncoder.writeEvoHomeZoneSetpointUntil(deviceId, temperature, localDateTime);
        //TODO wait for "va" for success or handle error
    }

}
