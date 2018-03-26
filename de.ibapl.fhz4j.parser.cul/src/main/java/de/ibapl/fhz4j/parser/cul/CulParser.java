package de.ibapl.fhz4j.parser.cul;

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
import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.api.FhzDataListener;
import de.ibapl.fhz4j.api.FhzMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.ibapl.spsw.api.Speed;
import de.ibapl.spsw.api.DataBits;
import de.ibapl.spsw.api.FlowControl;
import de.ibapl.spsw.api.Parity;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.StopBits;
import de.ibapl.fhz4j.protocol.em.EmMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;
import de.ibapl.fhz4j.protocol.hms.HmsMessage;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;

/**
 * Parses CUL from www.busware.de Commands see http://culfw.de/commandref.html
 * and https://github.com/mhop/fhem-mirror/blob/master/fhem/FHEM/11_FHT.pm
 * partial implemented.
 *
 * @author aploese
 */
public class CulParser<T extends FhzMessage> extends Parser implements ParserListener<T> {

    private T partialFhzMessage;

    public CulParser(FhzDataListener dataListener) {
        this.dataListener = dataListener;
    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private InputStream is;
    private final StreamListener streamListener = new StreamListener();

    private Thread t;
    private boolean closed;
    private final Object closeLock = new Object();
    private final FhzDataListener dataListener;

    /**
     * @return the dataListener
     */
    public FhzDataListener getDataListener() {
        return dataListener;
    }

    @Override
    public void init() {
        partialFhzMessage = null;
        fhzMessage = null;
    }

    @Override
    public void success(T fhzMessage) {
        this.fhzMessage = fhzMessage;
        setStackSize(2);
        state = State.SINGNAL_STRENGTH;
    }

    @Override
    public void fail(Throwable t) {
        state = State.IDLE;
        dataListener.failed(t);
    }

    @Override
    public void successPartial(T fhzMessage) {
        this.partialFhzMessage = fhzMessage;
        setStackSize(2);
        state = State.SINGNAL_STRENGTH;
    }

    @Override
    public void successPartialAssembled(T fhzMessage) {
        this.fhzMessage = fhzMessage;
        setStackSize(2);
        state = State.SINGNAL_STRENGTH;
    }

    private enum State {

        IDLE,
        EM_PARSING,
        FS20_PARSING,
        FHT_PARSING,
        HMS_PARSING,
        LA_CROSSE_TX2_PARSING,
        CUL_L_PARSED,
        CUL_LO_PARSED,
        CUL_LOV_PARSED,
        CUL_E_PARSED,
        CUL_EO_PARSED,
        SINGNAL_STRENGTH,
        END_CHAR_0X0D,
        END_CHAR_0X0A;
    }
    private State state = State.IDLE;
    @SuppressWarnings("unchecked")
    private final EmParser emParser = new EmParser((ParserListener<EmMessage>) this);
    @SuppressWarnings("unchecked")
    private final FS20Parser fs20Parser = new FS20Parser((ParserListener<FS20Message>) this);
    @SuppressWarnings("unchecked")
    private final FhtParser fhtParser = new FhtParser((ParserListener<FhtMessage>) this);
    @SuppressWarnings("unchecked")
    private final HmsParser hmsParser = new HmsParser((ParserListener<HmsMessage>) this);
    @SuppressWarnings("unchecked")
    private final LaCrosseTx2Parser laCrosseTx2Parser = new LaCrosseTx2Parser((ParserListener<LaCrosseTx2Message>) this);

    private FhzMessage fhzMessage;

    @Override
    public void parse(char c) {
//        LOG.info(String.format("XXX 0x%02X, %s", b, (char) b));
        switch (state) {
            case IDLE:
                init();
                switch (c) {
                    case 'E':
                        state = State.CUL_E_PARSED;
                        break;
                    case 'F':
                        fs20Parser.init();
                        state = State.FS20_PARSING;
                        break;
                    case 'T':
                        fhtParser.init();
                        state = State.FHT_PARSING;
                        break;
                    case 'H':
                        hmsParser.init();
                        state = State.HMS_PARSING;
                        break;
                    case 't':
                        laCrosseTx2Parser.init();
                        state = State.LA_CROSSE_TX2_PARSING;
                        break;
                    case 'L':
                        state = State.CUL_L_PARSED;
                        break;
                    default:
                        LOG.fine(String.format("Discarted: 0x%02x %s", (byte) c, c));
                }
                break;
            case EM_PARSING:
                emParser.parse(c);
                break;
            case FS20_PARSING:
                fs20Parser.parse(c);
                break;
            case FHT_PARSING:
                fhtParser.parse(c);
                break;
            case HMS_PARSING:
                hmsParser.parse(c);
                break;
            case LA_CROSSE_TX2_PARSING:
                laCrosseTx2Parser.parse(c);
                break;
            case CUL_E_PARSED:
                if (c == 'O') {
                    state = State.CUL_EO_PARSED;
                } else {
                    emParser.init();
                    state = State.EM_PARSING;
                    emParser.parse(c);
                }
                break;
            case CUL_EO_PARSED:
                if (c == 'B') {
                    state = State.END_CHAR_0X0D;
                    fhzMessage = CulMessage.EOB;
                } else {
                    //ERROR???
                    state = State.IDLE;
                }
                break;
            case CUL_L_PARSED:
                if (c == 'O') {
                    state = State.CUL_LO_PARSED;
                } else {
                    //ERROR???
                    state = State.IDLE;
                }
                break;
            case CUL_LO_PARSED:
                if (c == 'V') {
                    state = State.CUL_LOV_PARSED;
                } else {
                    //ERROR???
                    state = State.IDLE;
                }
                break;
            case CUL_LOV_PARSED:
                if (c == 'F') {
                    state = State.END_CHAR_0X0D;
                    fhzMessage = CulMessage.LOVF;
                } else {
                    //ERROR???
                    state = State.IDLE;
                }
                break;
            case SINGNAL_STRENGTH:
                if (c == '\r') {
                    setStackSize(0);
                    state = State.END_CHAR_0X0A;
                    break;
                }
                try {
                    push(digit2Int(c));
                } catch (RuntimeException ex) {
                    LOG.severe(String.format("Signal strenght - Wrong char: 0x%02x %s", (byte) c, c));
                    state = State.IDLE;
                    parse(c); // try to recover
                }
                if (getStackpos() == 0) {
                    if (partialFhzMessage != null) {
                        partialFhzMessage.signalStrength = (float) (((float) getByteValue()) / 2.0 - 74);
                    } else if (fhzMessage != null) {
                        fhzMessage.signalStrength = (float) (((float) getByteValue()) / 2.0 - 74);
                    } else {
                        throw new RuntimeException("Should never happen");
                    }
                    setStackSize(0);
                    state = State.END_CHAR_0X0D;
                }
                break;

            case END_CHAR_0X0D:
                if (c == '\r') {
                    state = State.END_CHAR_0X0A;
                    break;
                } else if (c == '\n') {
                //Fall trough
                } else {
                    //ERRORhandling
                    state = State.IDLE;
                    break;
                }
            case END_CHAR_0X0A:
                if (c == '\n') {
                    state = State.IDLE;
                    if (LOG.isLoggable(Level.FINE)) {
                        if (fhzMessage != null) {
                            LOG.fine(fhzMessage.toString());
                        }
                    }

                    if (dataListener != null) {

                        if (partialFhzMessage instanceof FhtMessage) {
                            dataListener.fhtPartialDataParsed((FhtMessage) partialFhzMessage);
                        }

                        if (fhzMessage instanceof FhtMessage) {
                            dataListener.fhtDataParsed((FhtMessage) fhzMessage);
                        } else if (fhzMessage instanceof HmsMessage) {
                            dataListener.hmsDataParsed((HmsMessage) fhzMessage);
                        } else if (fhzMessage instanceof EmMessage) {
                            dataListener.emDataParsed((EmMessage) fhzMessage);
                        } else if (fhzMessage instanceof FS20Message) {
                            dataListener.fs20DataParsed((FS20Message) fhzMessage);
                        } else if (fhzMessage instanceof LaCrosseTx2Message) {
                            dataListener.laCrosseTxParsed((LaCrosseTx2Message) fhzMessage);
                        } else if (fhzMessage instanceof CulMessage) {
                            dataListener.culMessageParsed((CulMessage) fhzMessage);
                        }

                    }
                } else {
                    //ERRORhandling ???
                    state = State.IDLE;
                    parse(c); // try to recover
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

        serialPortSocket.open(Speed._9600_BPS, DataBits.DB_8, StopBits.SB_1, Parity.NONE, FlowControl.getFC_NONE());
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
                                parse((char) theData);
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
                } catch (Throwable t) {
                    LOG.log(Level.SEVERE, "finished waiting for packages", t);
                }
            } finally {
                LOG.log(Level.INFO, "finished waiting for packages");
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
