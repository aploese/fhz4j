/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j;

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
