package net.sf.fhz4j.console;

/*
 * #%L
 * fhz4j Console
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.atmodem4j.spsw.SerialPortSocket;
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

import net.sf.fhz4j.FhzDataListener;
import net.sf.fhz4j.FhzParser;
import net.sf.fhz4j.FhzWriter;
import net.sf.fhz4j.LogUtils;
import net.sf.fhz4j.em.EmMessage;
import net.sf.fhz4j.fht.FhtMultiMsgMessage;
import net.sf.fhz4j.fs20.FS20Message;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_CONSOLE);


    static class FhzListener implements FhzDataListener {

        @Override
        public void fhtDataParsed(FhtMessage fhtMessage) {
            DEVICES_HOME_CODE.add(fhtMessage.getHousecode());
            printTimeStamp();
            System.out.println(fhtMessage.toString());
        }

        @Override
        public void hmsDataParsed(HmsMessage hmsMsg) {
            printTimeStamp();
            System.out.println(hmsMsg);
        }

        @Override
        public void fhtMultiMsgParsed(FhtMultiMsgMessage fhtTempMessage) {
            printTimeStamp();
            System.out.println(fhtTempMessage.toString());
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

        SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        
        private void printTimeStamp() {
            System.out.print(df.format(new Date()));
            System.out.print(": ");
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
    
    private final static Set<Short> DEVICES_HOME_CODE = new HashSet<>();


    /**
     * DOCUMENT ME!
     *
     * @param port DOCUMENT ME!
     */
    public static void run(String port) {
        SerialPortSocket masterPort = null;
        FhzParser p = new FhzParser(new FhzListener());
        FhzWriter w = new FhzWriter();
        try {
            masterPort = FhzParser.openPort(port);
            p.setInputStream(masterPort.getInputStream());
            w.setOutputStream(masterPort.getOutputStream());
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            w.initFhz((short)1234);
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
                        w.initFhtReporting(DEVICES_HOME_CODE);
                        break;
                    default:
                }
            } while (c != 'q');

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        System.out.println("CLOSE");
        try {
            p.close();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        

        try {
            masterPort.close();
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
