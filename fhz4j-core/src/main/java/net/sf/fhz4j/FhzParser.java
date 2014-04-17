/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.atmodem4j.spsw.Baudrate;
import net.sf.atmodem4j.spsw.DataBits;
import net.sf.atmodem4j.spsw.FlowControl;
import net.sf.atmodem4j.spsw.Parity;
import net.sf.atmodem4j.spsw.SerialPortSocket;
import net.sf.atmodem4j.spsw.StopBits;
import net.sf.fhz4j.fht.FhtTempMessage;
import net.sf.fhz4j.fht.FhtMessage;
import net.sf.fhz4j.fht.FhtParser;
import net.sf.fhz4j.fht.FhtProperty;
import net.sf.fhz4j.hms.HmsMessage;
import net.sf.fhz4j.hms.HmsParser;

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
        LOG.log(Level.FINEST, "Set state from {0} to {1}", new Object[] {this.state, state});
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
        //FS20,
        //EM,
        FHT_PARSING,
        HMS_PARSING,
        SINGNAL_STRENGTH,
        END_CHAR_0X0D,
        END_CHAR_0X0A;
    }
    private State state = State.IDLE;
    private final FhtParser fhtParser = new FhtParser(this);
    private final HmsParser hmsParser = new HmsParser(this);
    private FhzMessage fhzMessage;
    private final Map<Short, FhtMessage> tempMap = new HashMap<>();

    @Override
    public void parse(int b) {
//        LOG.info(String.format("XXX 0x%02X, %s", b, (char) b));
        switch (state) {
            case IDLE:
                switch ((char) b) {
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
                }
                if (getStackpos() == 0) {
                    fhzMessage.setSignalStrength((float)(((float)getByteValue()) / 2.0 - 74));
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
                    return;
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
                            if (fhtMessage.getCommand() == FhtProperty.MEASURED_LOW) {
                                tempMap.put(fhtMessage.getHousecode(), fhtMessage);
                            }
                            if (fhtMessage.getCommand() == FhtProperty.MEASURED_HIGH) {
                                if (tempMap.containsKey(fhtMessage.getHousecode())) {
                                    final FhtTempMessage temp = new FhtTempMessage(tempMap.remove(fhtMessage.getHousecode()), fhtMessage);
                                    if (LOG.isLoggable(Level.FINE)) {
                                        LOG.fine(temp.toString());
                                    }
                                    if (dataListener != null) {
                                        dataListener.fhtCombinedData(temp);
                                    }
                                }
                            }
                        } else if (fhzMessage instanceof HmsMessage) {
                            dataListener.hmsDataParsed((HmsMessage) fhzMessage);
                        }
                    }
                } else {
                    //ERRORhandling ???
                    setState(State.IDLE);
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
    public static SerialPortSocket openPort(String portName) throws IOException {

        // Open the port represented by the CommPortIdentifier object. Give
        // the open call a relatively long timeout of 30 seconds to allow
        // a different application to reliquish the port if the user
        // wants to.
        LOG.log(Level.FINE, "open port {0}", portName);

        SerialPortSocket sPort = SerialPortSocket.FACTORY.createSerialPortSocket(portName);
        sPort.openRaw(Baudrate.B9600, DataBits.DB_8, StopBits.SB_2, Parity.EVEN, FlowControl.getFC_NONE());
        LOG.log(Level.FINE, "port opend {0}", portName);

        return sPort;
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
