package de.ibapl.fhz4j;

/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2017 Arne Plöse
 * %%
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009, 2017, Arne Plöse and individual contributors as indicated
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
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.ibapl.fhz4j.fht.FhtMessage;
import de.ibapl.fhz4j.fht.FhtProperty;

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
        final String data = String.format("X%02X\r\n", initFlags);
        os.write(data.getBytes());
        LOG.log(Level.INFO, "Data sent: {0}", new Object[]{data});
        Thread.sleep(1000);
        LOG.info("INIT Housecode");
        setFhzHousecode(fhz100Housecode);
        Thread.sleep(1000);
        LOG.info("INIT End");
    }

    private void writeFhtCmd8v(short housecode, FhtProperty property, byte origin, byte value) throws IOException {
        final String data = String.format("T%04X%02X%02X%02X\n", housecode, property.getValue(), origin, value);
        os.write(data.getBytes());
        LOG.log(Level.INFO, "Data sent to {0}: {1}", new Object[]{Fhz1000.houseCodeToString(housecode), data});
    }

    private void writeFhtCmd8b(short housecode, FhtProperty property, byte value) throws IOException {
        final String data = String.format("T%04X%02X%02X\n", housecode, property.getValue(), value);
        os.write(data.getBytes());
        LOG.log(Level.INFO, "Data sent to {0}: {1}", new Object[]{Fhz1000.houseCodeToString(housecode), data});
    }

    public void initFhtReporting(Iterable<Short> fhtDeviceHomeCodes) throws IOException {
        LOG.info("Send: request report to: ");
        for (short housecode : fhtDeviceHomeCodes) {
            final String data = String.format("T%04X%02X%02X%02X%02X\n", housecode, FhtProperty.REPORT_1.getValue(), ALL_REPORTS, FhtProperty.REPORT_2.getValue(), ALL_REPORTS);
            os.write(data.getBytes());
            LOG.log(Level.INFO, "Data sent to {0}: {1}", new Object[]{Fhz1000.houseCodeToString(housecode), data});
        }
    }

    public void syncFhtClocks(Iterable<Short> fhtDeviceHomeCodes) throws IOException {
        LOG.info("Send: request report to: ");
        final Calendar c = Calendar.getInstance();

        final byte year = (byte) (c.get(Calendar.YEAR) - 2000);
        final byte month = (byte) (c.get(Calendar.MONTH) + 1);
        final byte dayOfMonth = (byte) c.get(Calendar.DAY_OF_MONTH);
        final byte minute = (byte) c.get(Calendar.MINUTE);
        final byte hour = (byte) c.get(Calendar.HOUR);

        for (short housecode : fhtDeviceHomeCodes) {
            final String data = String.format("T%04X02X%02X%02X%02X%02X%02X%02X%02X%02X%02X\n", housecode, FhtProperty.YEAR.getValue(), year, FhtProperty.MONTH.getValue(), month, FhtProperty.DAY.getValue(), dayOfMonth, FhtProperty.HOUR.getValue(), hour, FhtProperty.MINUTE.getValue(), minute);
            os.write(data.getBytes());
            LOG.log(Level.INFO, "Data sent to {0}: {1}", new Object[]{Fhz1000.houseCodeToString(housecode), data});
        }
    }

    public void initFhtReporting(Short... fhtDeviceHomeCodes) throws IOException {
        initFhtReporting(Arrays.asList(fhtDeviceHomeCodes));
    }

    public void writeFhtMsg(FhtMessage message) throws IOException {
        writeFhtCmd8b(message.getHousecode(), message.getCommand(), (byte) message.getRawValue());
    }

    private void setFhzHousecode(short fhz100Housecode) throws IOException {
        final String data = String.format("T01%04X\r\n", fhz100Housecode);
        os.write(data.getBytes());
        LOG.log(Level.INFO, "Set my housecode to {0}: {1}", new Object[]{Fhz1000.houseCodeToString(fhz100Housecode), data});
    }
}
