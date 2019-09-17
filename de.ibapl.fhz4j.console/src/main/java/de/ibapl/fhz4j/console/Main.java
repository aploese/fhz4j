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
package de.ibapl.fhz4j.console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.DefaultParser;

import de.ibapl.fhz4j.api.Protocol;
import de.ibapl.fhz4j.cul.CulAdapter;
import de.ibapl.fhz4j.cul.CulMessage;
import de.ibapl.fhz4j.protocol.em.EmMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;
import de.ibapl.fhz4j.protocol.hms.HmsMessage;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.SerialPortSocketFactory;
import de.ibapl.spsw.logging.LoggingSerialPortSocket;
import de.ibapl.spsw.logging.TimeStampLogging;
import de.ibapl.spsw.ser2net.Ser2NetProvider;
import de.ibapl.fhz4j.api.FhzHandler;
import de.ibapl.fhz4j.cul.CulMessageListener;

/**
 * DOCUMENT ME!
 *
 * @author Arne Plöse
 */
public class Main {

    public final static String FHZ_CONSOLE = "fhz-console";

    private static final Logger LOG = Logger.getLogger(FHZ_CONSOLE);

    private class FhzListener implements CulMessageListener {

        FhzHandler fhzAdapter;

        @Override
        public void fhtDataParsed(FhtMessage fhtMessage) {
            printTimeStamp();
            System.out.println(fhtMessage.toString());
            //Just testing
            if (true) {
                return;
            }

            if (DEVICES_HOME_CODE.add(fhtMessage.housecode)) {
                try {
                    if (fhzAdapter != null) {
                        fhzAdapter.writeFhtTimeAndDate(fhtMessage.housecode, LocalDateTime.now());
                        fhzAdapter.initFhtReporting(fhtMessage.housecode);
                    }
                } catch (Throwable t) {
                    //no-op
                }
            }
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
            printTimeStamp();
            System.err.print(": ");
            while (t != null) {
                t.printStackTrace();
                System.err.println("");
                t = t.getCause();
            }
        }

        @Override
        public void culMessageParsed(CulMessage culMessage) {
            printTimeStamp();
            System.err.println(": CUL " + culMessage);
        }

        @Override
        public void evoHomeParsed(EvoHomeMessage evoHomeMsg) {
            printTimeStamp();
            System.out.println(evoHomeMsg.toString());
        }

        @Override
        public void signalStrength(float signalStrength) {
            printTimeStamp();
            System.out.append("Signal strength: ").println(signalStrength);
        }

        @Override
        public void receiveEnabled(Protocol protocol) {
            printTimeStamp();
            System.out.append("enabled receivement of RF-protocol: ").println(protocol);
        }

        @Override
        public void helpParsed(String helpMessages) {
            printTimeStamp();
            System.out.append("CUL Help: \"").append(helpMessages).println("\"");
        }

        @Override
        public void onIOException(IOException ioe) {
            printTimeStamp();
            System.err.println("Serial port error - try to recover");
            try {
                fhzAdapter.close();
                // Linux 1 min to recover ???
                Thread.sleep(600000);
            } catch (Exception e) {
                System.err.println(e);
            }
            try {
                fhzAdapter.open();
            } catch (Exception e) {
            System.err.println("Serial port error - closing down");
                System.err.println(e);
                System.exit(1);
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
        optg.setRequired(false);

        opt = new Option("p", "port", true, "serial port to use");
        opt.setArgName("port");
        opt.setType(String.class);
        optg.addOption(opt);

        opt = new Option("r", "ser2net", true, "ser2net host:port");
        opt.setArgName("ser2net");
        opt.setType(String.class);
        optg.addOption(opt);

        options.addOptionGroup(optg);

        opt = new Option(null, "enable-fht", false, "enable receive of fht messages");
        options.addOption(opt);

        opt = new Option(null, "enable-evo-home", false, "enable receive of evo home messages");
        options.addOption(opt);

        opt = new Option("s", "scan", false, "scan for serial ports");

        options.addOption(opt);

        opt = null;
        optg = null;

        CommandLineParser cmdParser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException ex) {
            printHelp(options);

            return;
        }

        if (cmd.hasOption("help") || args.length == 0) {
            printHelp(options);

            return;
        }
        ServiceLoader<SerialPortSocketFactory> sl = ServiceLoader.load(SerialPortSocketFactory.class);
        Iterator<SerialPortSocketFactory> i = sl.iterator();
        if (!i.hasNext()) {
            throw new RuntimeException("No provider for SerialPortSocketFactory found, pleas add one to you class path ");
        }
        final SerialPortSocketFactory serialPortSocketFactory = i.next();

        if (cmd.hasOption("scan")) {
            System.out.println("Java Properties:");
            System.getProperties().forEach((Object key, Object value) -> {
                System.out.printf("\t\"%s\" = \"%s\"\n", key, value);
            });

            final List<String> ports = serialPortSocketFactory.getPortNames(false);
            System.out.println("Serial ports available");
            for (String port : ports) {
                System.out.println("Serial port: " + port);
            }
            System.out.println("Done.");

        }

        Set<Protocol> protocols = EnumSet.noneOf(Protocol.class);
        if (cmd.hasOption("enable-fht")) {
            protocols.add(Protocol.FHT);
        }

        if (cmd.hasOption("enable-evo-home")) {
            protocols.add(Protocol.EVO_HOME);
        }

        final File logFile = File.createTempFile("cul_", ".txt", Paths.get(".").toFile());

        if (cmd.hasOption("ser2net")) {
            new Main().runSer2Net(cmd.getOptionValue("ser2net"), protocols, logFile);
        }

        if (cmd.hasOption("port")) {
            new Main().runLocalPort(cmd.getOptionValue("port"), protocols, logFile);
        }

    }

    private final Set<Short> DEVICES_HOME_CODE = new HashSet<>();

    public void runSer2Net(String ser2net, Set<Protocol> protocols, File logFile) {
        try {
            LOG.info("LOG File: " + logFile.getAbsolutePath());
            String[] split = ser2net.split(":");
            SerialPortSocket serialPort = LoggingSerialPortSocket.wrapWithAsciiOutputStream(new Ser2NetProvider(split[0], Integer.valueOf(split[1])), new FileOutputStream(logFile), false, TimeStampLogging.NONE);
            run(serialPort, protocols);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void runLocalPort(String port, Set<Protocol> protocols, File logFile) {
        try {
            ServiceLoader<SerialPortSocketFactory> sl = ServiceLoader.load(SerialPortSocketFactory.class);
            Iterator<SerialPortSocketFactory> i = sl.iterator();
            if (!i.hasNext()) {
                throw new RuntimeException("No provider for SerialPortSocketFactory found, pleas add one to you class path ");
            }
            final SerialPortSocketFactory serialPortSocketFactory = i.next();

            LOG.info("LOG File: " + logFile.getAbsolutePath());
            SerialPortSocket serialPort = LoggingSerialPortSocket.wrapWithAsciiOutputStream(serialPortSocketFactory.createSerialPortSocket(port), new FileOutputStream(logFile), false, TimeStampLogging.UTC);
            run(serialPort, protocols);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void run(SerialPortSocket serialPortSocket, Set<Protocol> protocols) throws Exception {
        final FhzListener listener = new FhzListener();
        try ( CulAdapter culAddapter = new CulAdapter(serialPortSocket, listener)) {
            culAddapter.open();
            listener.fhzAdapter = culAddapter;
            try {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
                if (protocols.contains(Protocol.FHT)) {
                    culAddapter.initFhz((short) 0001);
                    culAddapter.initFhtReporting((short) 302);
//            fhzAddapter.writeFhtTimeAndDate((short) 302, LocalDateTime.now());
//            fhzAddapter.writeFhtCycle((short) 302, DayOfWeek.MONDAY, LocalTime.of(5, 0), LocalTime.of(8, 30), null, null);
//            fhzAddapter.writeFht((short)302, FhtProperty.DESIRED_TEMP, 24.0f);
//            fhzAddapter.writeFhtModeManu((short)302);
//            fhzAddapter.writeFhtModeHoliday((short) 302, 16.0f, LocalDate.now().plusMonths(2).plusDays(4));
                }
                if (protocols.contains(Protocol.EVO_HOME)) {
                    culAddapter.initEvoHome();
                }

            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
                throw new RuntimeException("Can't init fhzAdapter" + ex);
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
                            culAddapter.initFhtReporting(DEVICES_HOME_CODE);
                            break;
                        default:
                    }
                } while (c != 'q');

            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            System.out.println("CLOSE");

        }
    }

    private static void printHelp(Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(300);
        formatter.printHelp("java -jar fhz4j-console-{VERSION}-jar-with-dependencies.jar ", opts);
    }
}
