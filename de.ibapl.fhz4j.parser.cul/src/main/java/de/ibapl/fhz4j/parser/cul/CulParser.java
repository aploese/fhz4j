/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2017-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.api.Message;
import de.ibapl.fhz4j.api.Protocol;
import de.ibapl.fhz4j.api.Response;
import de.ibapl.fhz4j.cul.CulEobMessage;
import de.ibapl.fhz4j.cul.CulLovfMessage;
import de.ibapl.fhz4j.cul.CulMessage;
import de.ibapl.fhz4j.cul.CulMessageListener;
import de.ibapl.fhz4j.cul.CulRequest;
import de.ibapl.fhz4j.cul.CulResponse;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.em.EmParser;
import de.ibapl.fhz4j.parser.evohome.EvoHomeParser;
import de.ibapl.fhz4j.parser.fht.FhtParser;
import de.ibapl.fhz4j.parser.fs20.FS20Parser;
import de.ibapl.fhz4j.parser.hms.HmsParser;
import de.ibapl.fhz4j.parser.lacrosse.tx2l.LaCrosseTx2Parser;
import de.ibapl.fhz4j.protocol.em.EmMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessage;
import de.ibapl.fhz4j.protocol.fht.AbstractFhtMessage;
import de.ibapl.fhz4j.protocol.fht.Fht80TfMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;
import de.ibapl.fhz4j.protocol.hms.HmsMessage;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parses CUL from www.busware.de Commands see http://culfw.de/commandref.html
 * and https://github.com/mhop/fhem-mirror/blob/master/fhem/FHEM/11_FHT.pm
 * partial implemented.
 *
 * @author Arne Plöse
 * @param <T>
 */
public class CulParser<T extends Message> extends AbstractCulParser {

    class QueueEntry<T extends CulRequest> {

        final CulRequest request;
        final Consumer<Response> consumer;

        private QueueEntry(CulRequest request, Consumer<Response> consumer) {
            this.request = request;
            this.consumer = consumer;
        }
    }

    private class SubParserListener implements ParserListener<T> {

        @Override
        public void success(T fhzMessage) {
            CulParser.this.fhzMessage = fhzMessage;
            CulParser.this.state = State.SINGNAL_STRENGTH;
        }

        @Override
        public void fail(Throwable t) {
            CulParser.this.state = State.IDLE;
            CulParser.this.culMessageListener.failed(t);
        }

        @Override
        public void successPartial(T fhzMessage) {
            CulParser.this.partialFhzMessage = fhzMessage;
            CulParser.this.state = State.SINGNAL_STRENGTH;
        }

        @Override
        public void successPartialAssembled(T fhzMessage) {
            CulParser.this.fhzMessage = fhzMessage;
            CulParser.this.state = State.SINGNAL_STRENGTH;
        }
    }

    private final Queue<QueueEntry> pendingRequests = new LinkedList<>();

    /**
     * Returns whether or not the parser is idle.
     *
     * @return true if state is idle otherwise false;
     */
    boolean isIdle() {
        return state == State.IDLE;
    }

    private enum State {
        IDLE,
        PARSER_PARSING,
        CUL_PARSER_PARSING,
        CUL_L_PARSED,
        CUL_LO_PARSED,//TODO move to culParser
        CUL_LOV_PARSED,//TODO move to culParser
        CUL_E_PARSED,
        CUL_EO_PARSED,//TODO move to culParser
        EVO_HOME_START,
        EVO_HOME_READ_ERROR_CHAR,
        EVO_HOME_READ_GARBAGE,
        SINGNAL_STRENGTH,
        CUL_HELP_MESSAGE,
        END_CHAR_0X0D,
        END_CHAR_0X0A;
    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);

    private StringBuilder helpStringBuilder;
    private final SubParserListener subParserListener = new SubParserListener();
    private final CulMessageListener culMessageListener;
    private State state = State.IDLE;
    private Parser currentParser;
    private CulResponseParser culResponseParser;
    @SuppressWarnings("unchecked")
    private final EmParser emParser = new EmParser((ParserListener<EmMessage>) subParserListener);
    @SuppressWarnings("unchecked")
    private final FS20Parser fs20Parser = new FS20Parser((ParserListener<FS20Message>) subParserListener);
    @SuppressWarnings("unchecked")
    private final FhtParser fhtParser = new FhtParser((ParserListener<AbstractFhtMessage>) subParserListener);
    @SuppressWarnings("unchecked")
    private final HmsParser hmsParser = new HmsParser((ParserListener<HmsMessage>) subParserListener);
    @SuppressWarnings("unchecked")
    private final LaCrosseTx2Parser laCrosseTx2Parser = new LaCrosseTx2Parser((ParserListener<LaCrosseTx2Message>) subParserListener);
    @SuppressWarnings("unchecked")
    private final EvoHomeParser evoHomeParser = new EvoHomeParser((ParserListener<EvoHomeMessage>) subParserListener);

    private Message fhzMessage;

    private T partialFhzMessage;

    private final StringBuilder errorGarbageCollector = new StringBuilder();

    public CulParser(CulMessageListener dataListener) {
        this.culMessageListener = dataListener;
    }

    /**
     * @return the dataListener
     */
    public CulMessageListener getCulMessageListener() {
        return culMessageListener;
    }

    public void init() {
        partialFhzMessage = null;
        fhzMessage = null;
    }

    @Override
    public void parse(char c) {
        OUTER:
        switch (state) {
            case IDLE -> {
                init();
                switch (c) {
                    case '?' -> {
                        state = State.CUL_HELP_MESSAGE;
                        helpStringBuilder = new StringBuilder();
                        helpStringBuilder.append(c);
                    }
                    case 'E' ->
                        state = State.CUL_E_PARSED;
                    case 'F' -> {
                        initParser(fs20Parser);
                        state = State.PARSER_PARSING;
                    }
                    case 'T' -> {
                        initParser(fhtParser);
                        state = State.PARSER_PARSING;
                    }
                    case 'H' -> {
                        initParser(hmsParser);
                        state = State.PARSER_PARSING;
                    }
                    case 't' -> {
                        initParser(laCrosseTx2Parser);
                        state = State.PARSER_PARSING;
                    }
                    case 'L' ->
                        state = State.CUL_L_PARSED;
                    case 'v' -> {
                        evoHomeParser.init();
                        state = State.EVO_HOME_START;
                    }
                    case '\n' -> {
                    }
                    case '\r' -> {
                    }
                    default -> {
                        if (!pendingRequests.isEmpty()) {
                            culResponseParser = CulResponseParser.of(pendingRequests.peek().request, pendingRequests.peek().consumer);
                            state = State.CUL_PARSER_PARSING;
                            culResponseParser.parse(c);
                        } else {
                            LOG.fine(String.format("Discarted: 0x%02x %s", (byte) c, c));
                        }
                    }
                }
            }
            case EVO_HOME_START -> {
                switch (c) {
                    case 'r' -> {
                        initParser(evoHomeParser);
                        state = State.PARSER_PARSING;
                    }
                    case 'a' -> {
                        culMessageListener.receiveEnabled(Protocol.EVO_HOME);
                        state = State.IDLE;
                    }
                    case '!' ->
                        state = State.EVO_HOME_READ_ERROR_CHAR;
                    default ->
                        throw new IllegalArgumentException(String.format("unexpected char EVO_HOME_START: 0x%02x %s", (byte) c, c));
                }
            }
            case EVO_HOME_READ_ERROR_CHAR -> {
                switch (c) {
                    case 'M' -> {
                        //Manchester coding error in received data
                        errorGarbageCollector.append("EvoHome Manchester coding error:");
                        state = State.EVO_HOME_READ_GARBAGE;
                    }
                    case 'F' -> {
                        //Framing error, where received data doesn't decode to 1start-8data-1stop at 38,400bps
                        errorGarbageCollector.append("EvoHome Framing error:");
                        state = State.EVO_HOME_READ_GARBAGE;
                    }
                    case 'C' -> {
                        //Checksum error over completed message
                        errorGarbageCollector.append("EvoHome Checksum error:");
                        state = State.EVO_HOME_READ_GARBAGE;
                    }
                    case 'L' -> {
                        //Message exceed expected maximum length
                        errorGarbageCollector.append("EvoHome Msg too long:");
                        state = State.EVO_HOME_READ_GARBAGE;
                    }
                    case 'O' -> {
                        //verrun, where a second data byte arrived before the ISR processing the first was able to complete
                        errorGarbageCollector.append("EvoHome  overrun:");
                        state = State.EVO_HOME_READ_GARBAGE;
                    }
                    default -> {
                        errorGarbageCollector.append("EvoHome  unknown :").append(c);
                        state = State.EVO_HOME_READ_GARBAGE;
                    }
                }
            }
            case EVO_HOME_READ_GARBAGE -> {
                if (c == '\n' || c == '\r') {
                    //TODO pass to culListener???
                    LOG.warning(errorGarbageCollector.toString());
                    errorGarbageCollector.setLength(0);
                    state = State.IDLE;
                } else {
                    errorGarbageCollector.append(c);
                }
            }
            case PARSER_PARSING -> {
                switch (c) {
                    case 'v' ->
                        state = State.EVO_HOME_START;
                    case '?' -> {
                        state = State.CUL_HELP_MESSAGE;
                        helpStringBuilder = new StringBuilder();
                        helpStringBuilder.append(c);
                    }
                    case '\n', '\r' -> {
                        LOG.log(Level.SEVERE, "In state {0} for protocol {1} unexpected end of message received", new Object[]{state, currentParser.getClass().getName()});
                        state = State.IDLE;
                    }
                    default -> {
                        if (isFirstNibble) {
                            firstNibble = digit2Byte(c);
                            isFirstNibble = false;
                        } else {
                            isFirstNibble = true;
                            currentParser.parse((byte) ((firstNibble << 4) | digit2Byte(c)));
                        }
                    }
                }
            }
            case CUL_PARSER_PARSING -> {
                try {
                    culResponseParser.parse(c);
                    switch (c) {
                        case '\n' -> {
                            state = State.IDLE;
                            if (culResponseParser.isSuccess()) {
                                //On success remove pending parser
                                pendingRequests.remove();
                                if (culResponseParser.consumer != null) {
                                    culResponseParser.consumer.accept(culResponseParser.response);
                                }
                            } else {
                                LOG.log(Level.SEVERE, "In state {0} for CUL request {1} unexpected end of message received", new Object[]{state, culResponseParser.getClass().getName()});
                                state = State.IDLE;
                                break;
                            }
                        }
                        case '\r' -> {
                        }
                        default -> {
                        }
                    }
                    //no-op
                    //no-op
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "In state {0} for CUL request {1} unexpected parser error", new Object[]{state, culResponseParser.getClass().getName()});
                    state = State.IDLE;
                    //TODO notify failure??
                }
            }
            case CUL_E_PARSED -> {
                if (c == 'O') {
                    state = State.CUL_EO_PARSED;
                } else {
                    initParser(emParser);
                    state = State.PARSER_PARSING;
                    try {
                        firstNibble = digit2Byte(c);
                        isFirstNibble = false;
                    } catch (IllegalArgumentException iae) {
                        LOG.log(Level.SEVERE, "In state {0} for CUL unexpected parser error", state);
                        state = State.IDLE;
                    }
                }
            }
            case CUL_EO_PARSED -> {
                if (c == 'B') {
                    state = State.END_CHAR_0X0D;
                    fhzMessage = CulEobMessage.EOB;
                } else {
                    // ERROR???
                    state = State.IDLE;
                }
            }
            case CUL_L_PARSED -> {
                if (c == 'O') {
                    state = State.CUL_LO_PARSED;
                } else {
                    // ERROR???
                    state = State.IDLE;
                }
            }
            case CUL_LO_PARSED -> {
                if (c == 'V') {
                    state = State.CUL_LOV_PARSED;
                } else {
                    // ERROR???
                    state = State.IDLE;
                }
            }
            case CUL_LOV_PARSED -> {
                if (c == 'F') {
                    state = State.END_CHAR_0X0D;
                    fhzMessage = CulLovfMessage.LOVF;
                } else {
                    // ERROR???
                    state = State.IDLE;
                }
            }
            case SINGNAL_STRENGTH -> {
                if (c == '\r') {
                    state = State.END_CHAR_0X0A;
                    break;
                } else {
                    if (isFirstNibble) {
                        firstNibble = digit2Byte(c);
                        isFirstNibble = false;
                    } else {
                        isFirstNibble = true;
                        final byte value = (byte) ((firstNibble << 4) | digit2Byte(c));
                        culMessageListener.signalStrength((float) (value / 2.0 - 74));
                        state = State.END_CHAR_0X0D;
                    }
                }
            }
            case CUL_HELP_MESSAGE -> {
                if (c == '\n' || c == '\r') {
                    state = State.IDLE;
                    culMessageListener.helpParsed(helpStringBuilder.toString());
                    helpStringBuilder = null;
                } else {
                    helpStringBuilder.append(c);
                }
            }
            case END_CHAR_0X0D -> {
                switch (c) {
                    case '\r' -> {
                        state = State.END_CHAR_0X0A;
                        break OUTER;
                    }
                    case '\n' ->
                        finishParsingAndNotify();
                    default -> {
                        // ERRORhandling
                        state = State.IDLE;
                        break OUTER;
                    }
                }
            }

            case END_CHAR_0X0A -> {
                if (c == '\n') {
                    finishParsingAndNotify();
                } else {
                    // ERRORhandling ???
                    state = State.IDLE;
                    parse(c); // try to recover
                }
            }
            default -> {
            }
        }
    }

    private void finishParsingAndNotify() throws RuntimeException {
        state = State.IDLE;
        if (LOG.isLoggable(Level.FINE)) {
            if (fhzMessage != null) {
                LOG.fine(fhzMessage.toString());
            }
        }
        if (culMessageListener != null) {

            if (partialFhzMessage instanceof FhtMessage fhtMessage) {
                culMessageListener.fhtPartialDataParsed(fhtMessage);
            }

            if (fhzMessage != null) {
                switch (fhzMessage.protocol) {
                    case FHT ->
                        culMessageListener.fhtDataParsed((FhtMessage) fhzMessage);
                    case FHT_TF ->
                        culMessageListener.fht80TfDataParsed((Fht80TfMessage) fhzMessage);
                    case HMS ->
                        culMessageListener.hmsDataParsed((HmsMessage) fhzMessage);
                    case EM ->
                        culMessageListener.emDataParsed((EmMessage) fhzMessage);
                    case FS20 ->
                        culMessageListener.fs20DataParsed((FS20Message) fhzMessage);
                    case LA_CROSSE_TX2 ->
                        culMessageListener.laCrosseTxParsed((LaCrosseTx2Message) fhzMessage);
                    case CUL ->
                        culMessageListener.culMessageParsed((CulMessage) fhzMessage);
                    case EVO_HOME ->
                        culMessageListener.evoHomeParsed((EvoHomeMessage) fhzMessage);
                    default ->
                        throw new RuntimeException();
                }
            }
        }
    }

    private void initParser(Parser parser) {
        currentParser = parser;
        currentParser.init();
        isFirstNibble = true;
        firstNibble = 0;
    }

    /**
     * Add a request for which we expect a response.
     *
     * @param request
     * @param consumer
     * @return
     */
    public boolean addCulRequest(CulRequest request, Consumer<Response> consumer) {
        return pendingRequests.add(new QueueEntry(request, consumer));
    }

    /**
     * Add the ordered request for which we expect a response.
     *
     * @param requests
     * @param consumer
     */
    public void addCulRequests(Queue<CulRequest> requests, Consumer<CulResponse> consumer) {
        for (CulRequest request : requests) {
            pendingRequests.add(new QueueEntry(request, consumer));
        }
    }
}
