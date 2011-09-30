/*
 * Copyright 2009, openv4j.sf.net, and individual contributors as indicated
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
 *
 * $Id: $
 *
 * @author arnep
 */
package net.sf.fhz4j.console;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Date;
import java.util.logging.Level;
import net.sf.fhz4j.fht.FhtMessage;

import net.sf.fhz4j.hms.HmsMessage;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import net.sf.fhz4j.FhzDataListener;
import net.sf.fhz4j.FhzParser;
import net.sf.fhz4j.FhzWriter;
import net.sf.fhz4j.fht.FhtTempMessage;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);


    static class FhzListener implements FhzDataListener {

        @Override
        public void fhtDataParsed(FhtMessage fhtMessage) {
            System.out.println(fhtMessage.toString());
        }

        @Override
        public void hmsDataParsed(HmsMessage hmsMsg) {
            System.out.println(hmsMsg);
        }

        @Override
        public void fhtCombinedData(FhtTempMessage fhtTempMessage) {
            System.out.println(fhtTempMessage.toString());
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

        Date startTime = new Date();
        run(cmd.getOptionValue("port"));
    }
    private final static short[] DEVICES_HOME_CODE = new short[]{821, 3339, 5510, 6794, 7149, 7784, 9752};


    /**
     * DOCUMENT ME!
     *
     * @param port DOCUMENT ME!
     * @param dc DOCUMENT ME!
     */
    public static void run(String port) {
        SerialPort masterPort = null;
        FhzParser p = new FhzParser(new FhzListener());
        FhzWriter w = new FhzWriter();
        try {
            masterPort = FhzParser.openPort(port);
            p.setInputStream(masterPort.getInputStream());
            w.setOutputStream(masterPort.getOutputStream());
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            w.initFhz((short)1234);
        } catch (NoSuchPortException ex) {
            LOG.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (PortInUseException ex) {
            LOG.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (UnsupportedCommOperationException ex) {
            LOG.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
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
                        w.initFhtReporting(DEVICES_HOME_CODE);
                        break;
                    default:
                }
            } while (c != 'q');

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("CLOSE");
        try {
            p.close();
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        if (masterPort != null) {
            masterPort.close();
        }
    }

    private static void printHelp(Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(300);
        formatter.printHelp("openv4j-memory-image", opts);
    }
}
