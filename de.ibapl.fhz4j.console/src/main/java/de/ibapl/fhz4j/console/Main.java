package de.ibapl.fhz4j.console;

/*-
 * #%L
 * FHZ4J Console
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
import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.api.FhzDataListener;
import de.ibapl.fhz4j.parser.cul.CulParser;
import de.ibapl.fhz4j.parser.cul.CulWriter;
import de.ibapl.fhz4j.protocol.em.EmMessage;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.provider.SerialPortSocketFactoryImpl;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;

import de.ibapl.fhz4j.protocol.hms.HmsMessage;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;
import de.ibapl.spsw.logging.LoggingSerialPortSocket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_CONSOLE);

    private class FhzListener implements FhzDataListener {

        @Override
        public void fhtDataParsed(FhtMessage fhtMessage) {
            if (DEVICES_HOME_CODE.add(fhtMessage.housecode)) {
                try {
                    culWriter.writeFhtTimeAndDate(fhtMessage.housecode, LocalDateTime.now());
                    culWriter.initFhtReporting(fhtMessage.housecode);
                } catch (Throwable t) {
                    //no-op
                }
            }
            printTimeStamp();
            System.out.println(fhtMessage.toString());
        }

        @Override
        public void hmsDataParsed(HmsMessage hmsMsg) {
            printTimeStamp();
            System.out.println(hmsMsg.toString());
        }

        @Override
        public void emDataParsed(EmMessage emMsg) {
            printTimeStamp();
            System.out.println(emMsg.toString());
        }

        @Override
        public void fs20DataParsed(FS20Message fs20Msg) {
            printTimeStamp();
            System.out.println(fs20Msg.toString());
        }

        @Override
        public void laCrosseTxParsed(LaCrosseTx2Message laCrosseTx2Msg) {
            printTimeStamp();
            System.out.println(laCrosseTx2Msg.toString());
        }

        DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE_TIME; //("yyyy-MM-dd hh:mm:ss");

        private void printTimeStamp() {
            System.out.print(df.format(LocalDateTime.now()));
            System.out.print(": ");
        }

        @Override
        public void fhtPartialDataParsed(FhtMessage fhtMessage) {
            printTimeStamp();
            System.out.println(fhtMessage.toString());
        }

        @Override
        public void failed(Throwable t) {
            System.err.print(df.format(LocalDateTime.now()));
            System.err.print(": ");
            while (t != null) {
                t.printStackTrace();
                System.err.println("");
                t = t.getCause();
            }
        }

    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments
     *
     * @throws FileNotFoundException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Options options = new Options();
        Option opt;
        OptionGroup optg;

        opt = new Option("h", "help", false, "print this help message");
        options.addOption(opt);

        optg = new OptionGroup();
        optg.setRequired(true);

        opt = new Option("p", "port", true, "serial port to use");
        opt.setArgName("port");
        opt.setType(String.class);
        optg.addOption(opt);

        opt = new Option("s", "scan", false, "scan for serial ports");
        optg.addOption(opt);

        options.addOptionGroup(optg);

        opt = null;
        optg = null;

        CommandLineParser cmdParser = new PosixParser();
        CommandLine cmd = null;

        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException ex) {
            printHelp(options);

            return;
        }

        if (cmd.hasOption("help")) {
            printHelp(options);

            return;
        }

        if (cmd.hasOption("scan")) {
            System.out.println("Java Properties:");
            System.getProperties().forEach((Object key, Object value) -> {
                System.out.printf("\t\"%s\" = \"%s\"\n", key, value);
            });
            final Set<String> ports = SerialPortSocketFactoryImpl.singleton().getPortNames(false);
            System.out.println("Serial ports available");
            for (String port : ports) {
                System.out.println("Serial port: " + port);
            }
            System.out.println("Done.");

            return;
        }

        new Main().run(cmd.getOptionValue("port"));
    }

    private final Set<Short> DEVICES_HOME_CODE = new HashSet<>();
    private SerialPortSocket serialPort;
    private CulParser culParser;
    private CulWriter culWriter;

    /**
     * DOCUMENT ME!
     *
     * @param port DOCUMENT ME!
     */
    public void run(String port) {
        try {
            File logFile = File.createTempFile("cul_", ".txt");
            LOG.info("LOG File: " + logFile.getAbsolutePath());
            serialPort = new LoggingSerialPortSocket(SerialPortSocketFactoryImpl.singleton().createSerialPortSocket(port), new FileOutputStream(logFile), true, true);
        } catch (IOException ex) {
            throw  new RuntimeException(ex);
        }
        culParser = new CulParser(new FhzListener());
        culWriter = new CulWriter();
        try {
            CulParser.openPort(serialPort);
//            culParser.setInputStream(serialPort.getInputStream());
//            culWriter.setOutputStream(serialPort.getOutputStream());
            culParser.setInputStream(new BufferedInputStream(serialPort.getInputStream(), 64));
            culWriter.setOutputStream(new BufferedOutputStream(serialPort.getOutputStream(), 64));
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            culWriter.initFhz((short) 1234);
//        culWriter.writeFhtTimeAndDate((short) 302, LocalDateTime.now());
//        culWriter.writeFhtCycle((short) 302, DayOfWeek.MONDAY, LocalTime.of(5, 0), LocalTime.of(8, 30), null, null);
//            culWriter.writeFht((short)302, FhtProperty.DESIRED_TEMP, 24.0f);
//            w.writeFhtModeManu((short)302);
//            w.writeFhtModeHoliday((short) 302, 16.0f, LocalDate.now().plusMonths(2).plusDays(4));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
        try {
            char c;
            do {
                c = (char) System.in.read();

                switch (c) {
                    case 'q':
                        System.out.print("Bye will close down!");
                        break;
                    case 'r':
                        culWriter.initFhtReporting(DEVICES_HOME_CODE);
                        break;
                    default:
                }
            } while (c != 'q');

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        System.out.println("CLOSE");
        try {
            culParser.close();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        try {
            serialPort.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Ex during close", e);
        }
    }

    private static void printHelp(Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(300);
        formatter.printHelp("openv4j-memory-image", opts);
    }
}
