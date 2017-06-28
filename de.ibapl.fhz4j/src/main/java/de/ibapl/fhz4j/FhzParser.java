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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.ibapl.spsw.api.Baudrate;
import de.ibapl.spsw.api.DataBits;
import de.ibapl.spsw.api.FlowControl;
import de.ibapl.spsw.api.Parity;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.StopBits;
import de.ibapl.fhz4j.em.EmMessage;
import de.ibapl.fhz4j.em.EmParser;
import de.ibapl.fhz4j.fht.Fht80bModes;
import de.ibapl.fhz4j.fht.FhtMultiMsgMessage;
import de.ibapl.fhz4j.fht.FhtMessage;
import de.ibapl.fhz4j.fht.FhtMultiMsgProperty;
import de.ibapl.fhz4j.fht.FhtParser;
import de.ibapl.fhz4j.fht.FhtProperty;
import de.ibapl.fhz4j.fs20.FS20Parser;
import de.ibapl.fhz4j.fs20.FS20Message;
import de.ibapl.fhz4j.hms.HmsMessage;
import de.ibapl.fhz4j.hms.HmsParser;

/**
 *
 * @author aploese
 */
public class FhzParser extends Parser implements ParserListener {

    public FhzParser(FhzDataListener dataListener) {
        this.dataListener = dataListener;
    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_CORE);
    private InputStream is;
    private final StreamListener streamListener = new StreamListener();
    private Thread t;
    private boolean closed;
    private final Object closeLock = new Object();
    private final FhzDataListener dataListener;

    private void setState(State state) {
        LOG.log(Level.FINEST, "Set state from {0} to {1}", new Object[]{this.state, state});
        this.state = state;
    }

    /**
     * @return the dataListener
     */
    public FhzDataListener getDataListener() {
        return dataListener;
    }

    @Override
    public void init() {
        fhzMessage = null;
    }

    @Override
    public void success(FhzMessage fhzMessage) {
        this.fhzMessage = fhzMessage;
        setStackSize(2);
        setState(State.SINGNAL_STRENGTH);
    }

    @Override
    public void fail(Object o) {
        setState(State.IDLE);
    }

    private enum State {

        IDLE,
        EM_PARSING,
        FS20_PARSING,
        FHT_PARSING,
        HMS_PARSING,
        SINGNAL_STRENGTH,
        END_CHAR_0X0D,
        END_CHAR_0X0A;
    }
    private State state = State.IDLE;
    private final EmParser emParser = new EmParser(this);
    private final FS20Parser fs20Parser = new FS20Parser(this);
    private final FhtParser fhtParser = new FhtParser(this);
    private final HmsParser hmsParser = new HmsParser(this);

    private FhzMessage fhzMessage;
    private final Map<Short, FhtMessage> fhtTempMap = new HashMap<>();
    private final Map<Short, FhtMessage> fhtHolidayMap = new HashMap<>();
    private final Map<Short, FhtMessage> fhtModeMap = new HashMap<>();

    @Override
    public void parse(int b) {
//        LOG.info(String.format("XXX 0x%02X, %s", b, (char) b));
        switch (state) {
            case IDLE:
                switch ((char) b) {
                    case 'E':
                        emParser.init();
                        setState(State.EM_PARSING);
                        break;
                    case 'F':
                        fs20Parser.init();
                        setState(State.FS20_PARSING);
                        break;
                    case 'T':
                        fhtParser.init();
                        setState(State.FHT_PARSING);
                        break;
                    case 'H':
                        hmsParser.init();
                        setState(State.HMS_PARSING);
                        break;
                    default:
                        LOG.fine(String.format("Discarted: 0x%02x %s", b, (char) b));
                }
                break;
            case EM_PARSING:
                emParser.parse(b);
                break;
            case FS20_PARSING:
                fs20Parser.parse(b);
                break;
            case FHT_PARSING:
                fhtParser.parse(b);
                break;
            case HMS_PARSING:
                hmsParser.parse(b);
                break;

            case SINGNAL_STRENGTH:
                if (b == '\r') {
                    setStackSize(0);
                    setState(State.END_CHAR_0X0A);
                    break;
                }
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.severe(String.format("Signal strenght - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.IDLE);
                    parse(b); // ry to recover
                }
                if (getStackpos() == 0) {
                    fhzMessage.setSignalStrength((float) (((float) getByteValue()) / 2.0 - 74));
                    setStackSize(0);
                    setState(State.END_CHAR_0X0D);
                }
                break;

            case END_CHAR_0X0D:
                if (b == '\r') {
                    setState(State.END_CHAR_0X0A);
                } else {
                    //ERRORhandling
                    setState(State.IDLE);
                }
                break;
            case END_CHAR_0X0A:
                if (b == '\n') {
                    setState(State.IDLE);
                    if (LOG.isLoggable(Level.FINE)) {
                        if (fhzMessage != null) {
                            LOG.fine(fhzMessage.toString());
                        }
                    }

                    if (dataListener != null) {
                        if (fhzMessage instanceof FhtMessage) {
                            final FhtMessage fhtMessage = (FhtMessage) fhzMessage;
                            dataListener.fhtDataParsed(fhtMessage);
                            switch (fhtMessage.getCommand()) {
                                case MEASURED_LOW:
                                    if (fhtTempMap.containsKey(fhtMessage.getHousecode())) {
                                        FhtMessage high = fhtTempMap.get(fhtMessage.getHousecode());
                                        if (FhtProperty.MEASURED_HIGH == high.getCommand()) {
                                            fhtTempMap.remove(fhtMessage.getHousecode());
                                            final FhtMultiMsgMessage temp = new FhtMultiMsgMessage(FhtMultiMsgProperty.TEMP, fhtMessage, high);
                                            if (LOG.isLoggable(Level.FINE)) {
                                                LOG.fine(temp.toString());
                                            }
                                            if (dataListener != null) {
                                                dataListener.fhtMultiMsgParsed(temp);
                                            }

                                        } else {
                                            fhtTempMap.put(fhtMessage.getHousecode(), fhtMessage);
                                        }
                                    } else {
                                        fhtTempMap.put(fhtMessage.getHousecode(), fhtMessage);
                                    }
                                    break;
                                case MEASURED_HIGH:
                                    if (fhtTempMap.containsKey(fhtMessage.getHousecode())) {
                                        FhtMessage low = fhtTempMap.get(fhtMessage.getHousecode());
                                        if (FhtProperty.MEASURED_LOW == low.getCommand()) {
                                            fhtTempMap.remove(fhtMessage.getHousecode());
                                            final FhtMultiMsgMessage temp = new FhtMultiMsgMessage(FhtMultiMsgProperty.TEMP, low, fhtMessage);
                                            if (LOG.isLoggable(Level.FINE)) {
                                                LOG.fine(temp.toString());
                                            }
                                            if (dataListener != null) {
                                                dataListener.fhtMultiMsgParsed(temp);
                                            }

                                        } else {
                                            fhtTempMap.put(fhtMessage.getHousecode(), fhtMessage);
                                        }
                                    } else {
                                        fhtTempMap.put(fhtMessage.getHousecode(), fhtMessage);
                                    }
                                    break;
                                case MODE:
                                    fhtModeMap.put(fhtMessage.getHousecode(), fhtMessage);
                                    break;
                                case HOLIDAY_1:
                                    fhtHolidayMap.put(fhtMessage.getHousecode(), fhtMessage);
                                    break;
                                case HOLIDAY_2:
                                    if (fhtModeMap.containsKey(fhtMessage.getHousecode())) {
                                        FhtMultiMsgProperty p;
                                        switch (Fht80bModes.valueOf(fhtModeMap.get(fhtMessage.getHousecode()).getRawValue())) {
                                            case HOLIDAY:
                                                p = FhtMultiMsgProperty.HOLIDAY_END;
                                                break;
                                            case PARTY:
                                                p = FhtMultiMsgProperty.PARTY_END;
                                                break;
                                            default:
                                                throw new RuntimeException();
                                        }
                                        FhtMultiMsgMessage hm = new FhtMultiMsgMessage(p, fhtHolidayMap.remove(fhtMessage.getHousecode()), fhtMessage);
                                        if (LOG.isLoggable(Level.FINE)) {
                                            LOG.fine(hm.toString());
                                        }
                                        if (dataListener != null) {
                                            dataListener.fhtMultiMsgParsed(hm);
                                        }
                                    }
                                    break;

                                default:
                            }
                        } else if (fhzMessage instanceof HmsMessage) {
                            dataListener.hmsDataParsed((HmsMessage) fhzMessage);
                        } else if (fhzMessage instanceof EmMessage) {
                            dataListener.emDataParsed((EmMessage) fhzMessage);
                        } else if (fhzMessage instanceof FS20Message) {
                            dataListener.fs20DataParsed((FS20Message) fhzMessage);
                        }
                    }
                } else {
                    //ERRORhandling ???
                    setState(State.IDLE);
                    parse(b); // try to recover
                }
            default:
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param portName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static SerialPortSocket openPort(final SerialPortSocket serialPortSocket) throws IOException {

        // Open the port represented by the CommPortIdentifier object. Give
        // the open call a relatively long timeout of 30 seconds to allow
        // a different application to reliquish the port if the user
        // wants to.
        LOG.log(Level.FINE, "open port {0}", serialPortSocket.getPortName());

        serialPortSocket.openRaw(Baudrate.B9600, DataBits.DB_8, StopBits.SB_2, Parity.EVEN, FlowControl.getFC_NONE());
        LOG.log(Level.FINE, "port opend {0}", serialPortSocket.getPortName());

        return serialPortSocket;
    }

    private class StreamListener implements Runnable {

        @Override
        public void run() {
            LOG.log(Level.INFO, "THREAD START {0}", closed);
            try {
                int theData;

                try {
                    while (!closed) {
                        try {
                            theData = is.read();

                            if (theData > -1) {
                                parse(theData);
                            }

                        } catch (NullPointerException npe) {
                            if (!closed) {
                                throw new RuntimeException(npe);
                            }
                        }
                    }
                    synchronized (closeLock) {
                        closeLock.notifyAll();
                    }
                    LOG.info("closing down - finish waiting for new data");
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "run()", e);
                } catch (RuntimeException e) {
                    LOG.log(Level.SEVERE, "finished waiting for packages", e);
                }
            } finally {
            }
        }
    }

    private void start() {
        synchronized (closeLock) {
            closed = false;
            t = new Thread(streamListener);
            t.setDaemon(true);
            t.start();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param is DOCUMENT ME!
     */
    public void setInputStream(InputStream is) {
        this.is = is;
        start();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public void close() throws InterruptedException {
        synchronized (closeLock) {
            closed = true;
            closeLock.wait(2000); //wait max 2 sec
        }
    }
}