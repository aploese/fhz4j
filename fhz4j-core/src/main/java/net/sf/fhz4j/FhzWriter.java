package net.sf.fhz4j;

/*
 * #%L
 * fhz4j Core
 * %%
 * Copyright (C) 2009 - 2014 fhz4j
 * %%
 * fhz4j - Drivers for the Wireless FS20, FHT and HMS protocol http://fhz4j.sourceforge.net/
 * Copyright (C) 2009-2014, fhz4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
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
 * #L%
 */

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.fhz4j.fht.FhtProperty;


/**
 *
 * @author aploese
 */
public class FhzWriter {

    private final static Logger LOG = Logger.getLogger(LogUtils.FHZ_CORE);
    final static byte INIT_MSG_PACKAGE_OK = 0x01;
    final static byte INIT_MSG_ALL_REPEATED = 0x02;
    final static byte INIT_MSG_REPORT_ALL = 0x04;
    final static byte INIT_MSG_MONITOR_MODE = 0x08;
    final static byte INIT_MSG_WITH_TIMING = 0x10;
    final static byte INIT_MSG_WITH_RSSI = 0x20;
    final static byte INIT_MSG_REPORT_FHT_ALL_MESSAGES = 0x40;
    final static byte INIT_MSG_REPORT_RAW_RSSI = (byte) 0x80;
    final static byte ORIGIN_CUL = 0x79;
    final static byte ALL_REPORTS = (byte) 0xFF;
    private OutputStream os;

    public void setOutputStream(OutputStream os) {
        this.os = os;
    }

    public void initFhz(short fhzHousecode) throws IOException {
        try {
            initFhz(fhzHousecode, (byte) (INIT_MSG_PACKAGE_OK | INIT_MSG_WITH_RSSI));
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "EX during init", ex);
        }
    }

    public void initFhz(short fhz100Housecode, byte initFlags) throws IOException, InterruptedException {
        LOG.info("INIT 1");
        os.write("\r\n".getBytes());
        Thread.sleep(1000);
        LOG.info("INIT 2");
        os.write(String.format("X%02X\r\n", initFlags).getBytes());
        Thread.sleep(1000);
        LOG.info("INIT Housecode");
        setFhzHousecode(fhz100Housecode);
        Thread.sleep(1000);
        LOG.info("INIT End");
    }

    public void writeFhtCmd(short housecode, FhtProperty property, byte origin, byte value) throws IOException {
        os.write(String.format("T%s%02X%02X%02X\n", Fhz1000.houseCodeToString(housecode), property.getValue(), origin, value).getBytes());
    }

    public void initFhtReporting(Iterable<Short> fhtDeviceHomeCodes) throws IOException {
        LOG.info("Send: request report to: ");
        for (short homecode : fhtDeviceHomeCodes) {
            writeFhtCmd(homecode, FhtProperty.REPORT_1, ORIGIN_CUL, ALL_REPORTS);
            writeFhtCmd(homecode, FhtProperty.REPORT_2, ORIGIN_CUL, ALL_REPORTS);
        }
    }
    public void initFhtReporting(Short... fhtDeviceHomeCodes) throws IOException {
        initFhtReporting(Arrays.asList(fhtDeviceHomeCodes));
    }

    private void setFhzHousecode(short fhz100Housecode) throws IOException {
        os.write(String.format("T01%s\r\n", Fhz1000.houseCodeToString(fhz100Housecode)).getBytes());
    }
}
