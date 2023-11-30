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
package de.ibapl.fhz4j.writer.cul;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.cul.CulFhtDeviceOutBufferContentRequest;
import de.ibapl.fhz4j.cul.CulGetFirmwareVersionRequest;
import de.ibapl.fhz4j.cul.CulGetHardwareVersionRequest;
import de.ibapl.fhz4j.cul.CulGetSlowRfSettingsRequest;
import de.ibapl.fhz4j.cul.CulRemainingFhtDeviceOutBufferSizeRequest;
import de.ibapl.fhz4j.cul.CulRequest;
import de.ibapl.fhz4j.cul.CulSetSlowRfSettingsRequest;
import de.ibapl.fhz4j.cul.SlowRfFlag;
import de.ibapl.fhz4j.writer.evohome.EvoHomeWriter;
import de.ibapl.fhz4j.writer.fht.FhtWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.EnumSet;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arne Plöse
 */
public class CulWriter implements FhtWriter, EvoHomeWriter {

    public static final int DEFAULT_BUFFER_SIZE = 64;

    private final static Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private final WritableByteChannel wbc;
    private final ByteBuffer buffer;
    private boolean open;

    public CulWriter(WritableByteChannel wbc, int bufferSize) {
        if (!wbc.isOpen()) {
            throw new IllegalStateException("WritableByteChannel is closed");
        }
        this.wbc = wbc;
        this.buffer = ByteBuffer.allocateDirect(bufferSize);
        open = true;
    }

    @Override
    public void doWrite() throws IOException {
        buffer.flip();
        wbc.write(buffer);
        buffer.clear();
    }

    @Override
    public void putByte(byte value) throws IOException {
        putNibble((byte) (value >> 4));
        putNibble(value);
    }

    private void putNibble(byte value) {
        switch (value & 0x0F) {
            case 0x00:
                buffer.put((byte) '0');
                break;
            case 0x01:
                buffer.put((byte) '1');
                break;
            case 0x02:
                buffer.put((byte) '2');
                break;
            case 0x03:
                buffer.put((byte) '3');
                break;
            case 0x04:
                buffer.put((byte) '4');
                break;
            case 0x05:
                buffer.put((byte) '5');
                break;
            case 0x06:
                buffer.put((byte) '6');
                break;
            case 0x07:
                buffer.put((byte) '7');
                break;
            case 0x08:
                buffer.put((byte) '8');
                break;
            case 0x09:
                buffer.put((byte) '9');
                break;
            case 0x0A:
                buffer.put((byte) 'A');
                break;
            case 0x0B:
                buffer.put((byte) 'B');
                break;
            case 0x0C:
                buffer.put((byte) 'C');
                break;
            case 0x0D:
                buffer.put((byte) 'D');
                break;
            case 0x0E:
                buffer.put((byte) 'E');
                break;
            case 0x0F:
                buffer.put((byte) 'F');
                break;
            default:
                throw new IllegalArgumentException("Not a Number: " + value);
        }
    }

    @Override
    public void close() throws Exception {
        open = false;
        wbc.close();
    }

    public void initEvoHome() throws IOException {
        try {
            if (!open) {
                throw new IllegalStateException("Closed!");
            }
            LOG.info("INIT Evo Home");
            buffer.clear();
            buffer.put("\r\n".getBytes());
            doWrite();
            buffer.put("vd\r\n".getBytes());  //TODO vd is for debugging ....
            doWrite();
            Thread.sleep(100);
            LOG.info("INIT Evo Home End");
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "EX during Evo Home init", ex);
        }
    }

    @Override
    public void startFhtMessage() {
        buffer.clear();
        if (!open) {
            throw new IllegalStateException("Closed!");
        }
        buffer.put((byte) 'T');
    }

    @Override
    public void finishFhtMessage() {
        buffer.put((byte) '\n');
    }

    @Override
    public void startEvoHomeMessage() {
        if (!open) {
            throw new IllegalStateException("Closed!");
        }
        buffer.put((byte) 'v');
        buffer.put((byte) 's');
    }

    @Override
    public void finishEvoHomeMessage() {
        buffer.put((byte) '\n');
    }

    //TODO No RSSI ???
    public void initFhz(short fhzOwnHousecode) throws IOException {
        initFhz(fhzOwnHousecode, EnumSet.of(SlowRfFlag.REPORT_PACKAGE, SlowRfFlag.WITH_RSSI));
    }

    public void writeCulRequests(Queue<CulRequest> requests) throws IOException {
        for (CulRequest request : requests) {
            buffer.put(encodeCulRequest(request));
        }
        doWrite();
    }

    public void writeCulRequest(CulRequest request) throws IOException {
        buffer.put(encodeCulRequest(request));
        doWrite();
    }

    private byte[] encodeCulRequest(CulRequest request) {
        if (request instanceof CulGetSlowRfSettingsRequest) {
            return "X\r\n".getBytes();
        } else if (request instanceof CulSetSlowRfSettingsRequest) {
            final CulSetSlowRfSettingsRequest settingsRequest = (CulSetSlowRfSettingsRequest) request;
            byte flags = 0;
            for (SlowRfFlag flag : settingsRequest) {
                flags |= flag.value;
            }
            return String.format("X%02X\r\n", flags).getBytes();
        } else if (request instanceof CulFhtDeviceOutBufferContentRequest) {
            return "T02\r\n".getBytes();
        } else if (request instanceof CulRemainingFhtDeviceOutBufferSizeRequest) {
            return "T03\r\n".getBytes();
        } else if (request instanceof CulGetFirmwareVersionRequest) {
            return "V\r\n".getBytes();
        } else if (request instanceof CulGetHardwareVersionRequest) {
            return "VH\r\n".getBytes();
        } else {
            throw new IllegalArgumentException("Unknown CulRequest: " + request);
        }
    }

    public void initFhz(short fhzOwnHousecode, Set<SlowRfFlag> initFlags) throws IOException {
        LOG.info("initFhz");
        try {
            buffer.put("".getBytes());
            doWrite();
            LOG.info("INIT 2");
            buffer.put(encodeCulRequest(new CulSetSlowRfSettingsRequest(initFlags)));
            doWrite();
            Thread.sleep(100);
            LOG.info("INIT Housecode");
            setOwnFhzHousecode(fhzOwnHousecode);
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "EX during init", ex);
            throw new RuntimeException(ex);
        } finally {
            LOG.info("INIT End");
        }
    }

    private void setOwnFhzHousecode(short ownHousecode) throws IOException {
        buffer.put((byte) 'T');
        buffer.put((byte) '0');
        buffer.put((byte) '1');
        putByte((byte) (ownHousecode / 100));
        putByte((byte) (ownHousecode % 100));
        buffer.put((byte) '\r');
        buffer.put((byte) '\n');
        doWrite();
    }

    @Override
    public void putShort(short value) throws IOException {
        putNibble((byte) (value >>> 12));
        putNibble((byte) (value >>> 8));
        putNibble((byte) (value >>> 4));
        putNibble((byte) value);
    }

    @Override
    public void putInt(int value) throws IOException {
        putNibble((byte) (value >>> 28));
        putNibble((byte) (value >>> 24));
        putNibble((byte) (value >>> 20));
        putNibble((byte) (value >>> 16));
        putNibble((byte) (value >>> 12));
        putNibble((byte) (value >>> 8));
        putNibble((byte) (value >>> 4));
        putNibble((byte) value);
    }

}
