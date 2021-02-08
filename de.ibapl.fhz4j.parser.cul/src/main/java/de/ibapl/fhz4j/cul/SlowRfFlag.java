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

/**
 *
 * @author aploese
 */
public enum SlowRfFlag {
    /**
     * Bit 0: Report known messages (parity & checksum ok), with type prefix.
     */
    REPORT_PACKAGE(0x01),
    /**
     * Bit 1: Report each of the (repeated) packets of a message
     */
    REPORT_REPEATED_PACKAGES(0x02),
    /**
     * Bit 2: Report detailed data, even with wrong parity / checksum.
     */
    REPORT_DETAILED_PACKAGE(0x04),
    /**
     * Bit 3: Monitor mode: output an r on a risings edge and 'f' on a falling
     * edge. Output a '.' at the end of the message (no signal for 4msec).
     */
    MONITOR_MODE(0x08),
    /**
     * Bit 4: Timing: in monitor mode output one additional byte of the internal
     * microsecond timer, divided by 16. This is binary!
     */
    WITH_TIMING(0x10),
    /**
     * Bit 5: RSSI: report RSSI value as an additional HEX byte after digested
     * data or as a separate byte if Bit 3 is set too.
     */
    WITH_RSSI(0x20),
    /**
     * Bit 6: Report FHT protocol messages (ack, etc)
     */
    REPORT_FHT_PROTOCOL_MESSAGES(0x40),
    /**
     * Bit 7: CUL/CUN: Report raw RSSI data (a==weak ... p==strong signal) CUR:
     * Grafic representation: dark==weak ... light == stong signal
     */
    REPORT_RAW_RSSI(0x80);

    public final byte value;

    private SlowRfFlag(int value) {
        this.value = (byte) value;
    }

}
