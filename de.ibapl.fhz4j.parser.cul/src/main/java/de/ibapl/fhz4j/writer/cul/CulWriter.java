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
package de.ibapl.fhz4j.writer.cul;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.ibapl.fhz4j.LogUtils;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 *
 * @author Arne Plöse
 */
public class CulWriter implements FhtWriter, EvoHomeWriter {

    public static final int DEFAULT_BUFFER_SIZE = 64;

    public enum InitFlag {
        PACKAGE_OK(0x01), ALL_REPEATED(0x02), REPORT_ALL(0x04), MONITOR_MODE(0x08), WITH_TIMING(0x10), WITH_RSSI(
                0x20), REPORT_FHT_ALL_MESSAGES(0x40), REPORT_RAW_RSSI(0x80);
        final byte value;

        private InitFlag(int value) {
            this.value = (byte) value;
        }
    }

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
        putNibble((0xF0 & value) >> 4);
        putNibble(0x0F & value);
    }

    private void putNibble(int value) {
        switch (value) {
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
            Thread.sleep(1000);
            byte flags = 0;
            final String data = String.format("vd\r\n", flags);
            buffer.put(data.getBytes());
            doWrite();
            LOG.log(Level.INFO, "Data sent: {0}", new Object[]{data});
            Thread.sleep(1000);
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
        try {
            initFhz(fhzOwnHousecode, EnumSet.of(InitFlag.PACKAGE_OK, InitFlag.WITH_RSSI));
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "EX during init", ex);
        }
    }

    public void initFhz(short fhzOwnHousecode, Set<InitFlag> initFlags) throws IOException, InterruptedException {
        LOG.info("initFhz");
        buffer.put("\r\n".getBytes());
        doWrite();
        Thread.sleep(1000);
        LOG.info("INIT 2");
        byte flags = 0;
        for (InitFlag flag : initFlags) {
            flags |= flag.value;
        }
        final String data = String.format("X%02X\r\n", flags);
        buffer.put(data.getBytes());
        doWrite();
        LOG.log(Level.INFO, "Data sent: {0}", new Object[]{data});
        Thread.sleep(1000);
        LOG.info("INIT Housecode");
        setOwnFhzHousecode(fhzOwnHousecode);
        Thread.sleep(1000);
        LOG.info("INIT End");
    }

    private void setOwnFhzHousecode(short ownHousecode) throws IOException {
        buffer.put((byte) 'T');
        buffer.put((byte) '0');
        buffer.put((byte) '1');
        putByte((byte)(ownHousecode / 100));
        putByte((byte)(ownHousecode % 100));
        buffer.put((byte) '\n');
        doWrite();
    }

    @Override
    public void putShort(short value) throws IOException {
        putByte((byte)(value >>> 8));
        putByte((byte)value);
    }

    @Override
    public void putInt(int value) throws IOException {
        putByte((byte)(value >>> 24));
        putByte((byte)(value >>> 16));
        putByte((byte)(value >>> 8));
        putByte((byte)value);
    }

    
}
