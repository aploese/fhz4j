/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import net.sf.fhz4j.fht.FhtMeasuredTempMessage;
import net.sf.fhz4j.fht.FhtMessage;
import net.sf.fhz4j.fht.FhtParser;
import net.sf.fhz4j.fht.FhtProperty;
import net.sf.fhz4j.hms.HmsMessage;
import net.sf.fhz4j.hms.HmsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aploese
 */
public class FhzParser extends Parser implements ParserListener {

    public FhzParser(FhzDataListener dataListener) {
        this.dataListener = dataListener;
    }
    
    private static final Logger LOG = LoggerFactory.getLogger(FhzParser.class);
    private InputStream is;
    private StreamListener streamListener = new StreamListener();
    private Thread t;
    private boolean closed;
    private final Object closeLock = new Object();
    private final FhzDataListener dataListener;

    private void setState(State state) {
        LOG.trace(String.format("Set state from %s to %s", this.state, state));
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
    private FhtParser fhtParser = new FhtParser(this);
    private HmsParser hmsParser = new HmsParser(this);
    private FhzMessage fhzMessage;
    private Map<Short, FhtMessage> tempMap = new HashMap<Short, FhtMessage>();

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
                        LOG.debug(String.format("Discarted: 0x%02x %s", b, (char) b));
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
                    LOG.warn(String.format("Signal strenght - Wrong char: 0x%02x %s", b, (char) b));
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
                    if (LOG.isDebugEnabled()) {
                        if (fhzMessage != null) {
                            LOG.debug(fhzMessage.toString());
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
                                    final FhtMeasuredTempMessage temp = new FhtMeasuredTempMessage(tempMap.remove(fhtMessage.getHousecode()), fhtMessage);
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug(temp.toString());
                                    }
                                    if (dataListener != null) {
                                        dataListener.fhtMeasuredTempData(temp);
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
     * @throws NoSuchPortException DOCUMENT ME!
     * @throws PortInUseException DOCUMENT ME!
     * @throws UnsupportedCommOperationException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public static SerialPort openPort(String portName)
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        CommPortIdentifier portId;
        try {
            LOG.info("OLD: gnu.io.rxtx.SerialPorts: " + System.getProperty("gnu.io.rxtx.SerialPorts"));
            System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0:/dev/ttyACM1:/dev/ttyACM2:/dev/ttyACM3:/dev/ttyACM4");

            LOG.info("My: gnu.io.rxtx.SerialPorts: " + System.getProperty("gnu.io.rxtx.SerialPorts"));
            Enumeration<CommPortIdentifier> ports = (Enumeration<CommPortIdentifier>) CommPortIdentifier.getPortIdentifiers();
            while (ports.hasMoreElements()) {
                LOG.info("found port: " + ports.nextElement().getName());
            }

            // Obtain a CommPortIdentifier object for the port you want to open.
            portId = CommPortIdentifier.getPortIdentifier(portName);
        } finally {
            System.clearProperty("gnu.io.rxtx.SerialPorts");
        }

        // Open the port represented by the CommPortIdentifier object. Give
        // the open call a relatively long timeout of 30 seconds to allow
        // a different application to reliquish the port if the user
        // wants to.
        LOG.info("open port " + portName);

        SerialPort sPort = (SerialPort) portId.open(FhzParser.class.getName(), 30000);
        LOG.info("port opend " + portName);
        sPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_EVEN);
        sPort.enableReceiveTimeout(1000);
        sPort.setInputBufferSize(512);
        sPort.setOutputBufferSize(512);
        sPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

        return sPort;
    }

    private class StreamListener implements Runnable {

        @Override
        public void run() {
            LOG.info("THREAD START " + closed);
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
                    LOG.error("run()", e);
                } catch (Exception e) {
                    LOG.info("finished waiting for packages", e);
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
            closeLock.wait(2000); //wait man 2 sec
        }
    }
}
