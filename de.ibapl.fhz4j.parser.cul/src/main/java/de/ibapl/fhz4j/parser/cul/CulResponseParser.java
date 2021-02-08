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
package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.cul.CulFhtDeviceOutBufferContentRequest;
import de.ibapl.fhz4j.cul.CulFhtDeviceOutBufferContentResponse;
import de.ibapl.fhz4j.cul.CulGetSlowRfSettingsRequest;
import de.ibapl.fhz4j.cul.CulGetSlowRfSettingsResponse;
import de.ibapl.fhz4j.cul.CulRemainingFhtDeviceOutBufferSizeRequest;
import de.ibapl.fhz4j.cul.CulRemainingFhtDeviceOutBufferSizeResponse;
import de.ibapl.fhz4j.cul.CulRequest;
import de.ibapl.fhz4j.cul.CulResponse;
import de.ibapl.fhz4j.cul.SlowRfFlag;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.fht.FhtParser;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;

/**
 *
 * @author aploese
 */
abstract class CulResponseParser<R extends CulRequest, T extends CulResponse<R>> extends AbstractCulParser {

    private static class CulGetSlowRfSettingsRequestParser extends CulResponseParser<CulGetSlowRfSettingsRequest, CulGetSlowRfSettingsResponse> {

        public CulGetSlowRfSettingsRequestParser(CulGetSlowRfSettingsRequest request) {
            super(new CulGetSlowRfSettingsResponse(request));
        }

        public enum State {
            PARSE_SLOW_RF_SETTINGS,
            WAIT_FOR_SPACE,
            WAIT_FOR_TIMESLOTS,
            PARSE_TIMESLOTS,
            FAIL,
            SUCCESS;
        }

        private State state = State.PARSE_SLOW_RF_SETTINGS;
        private StringBuilder sb = new StringBuilder();

        @Override
        public boolean isSuccess() {
            return state == State.SUCCESS;
        }

        @Override
        public void parse(char c) {
            switch (state) {
                case PARSE_SLOW_RF_SETTINGS:
                    if (isFirstNibble) {
                        firstNibble = digit2Byte(c);
                        isFirstNibble = false;
                    } else {
                        state = State.WAIT_FOR_SPACE;
                        isFirstNibble = true;
                        final byte slowRf = (byte) ((firstNibble << 4) | digit2Byte(c));
                        for (SlowRfFlag slowRfFlag : SlowRfFlag.values()) {
                            if ((slowRf & slowRfFlag.value) != 0) {
                                response.slowRfFlags.add(slowRfFlag);
                            }
                        }
                    }
                    break;
                case WAIT_FOR_SPACE:
                    if (c == ' ') {
                        state = State.WAIT_FOR_TIMESLOTS;
                    } else {
                        state = State.FAIL;
                        throw new RuntimeException("Expected space ");
                    }
                    break;
                case WAIT_FOR_TIMESLOTS:
                    if (c != ' ') {
                        state = State.PARSE_TIMESLOTS;
                        sb.append(c);
                    }
                    break;
                case PARSE_TIMESLOTS:
                    if ((c == '\r') || (c == '\n')) {
                        response.milliTimeToSend = Integer.parseInt(sb.toString());
                        state = State.SUCCESS;
                    } else {
                        sb.append(c);
                    }
                    break;
                case SUCCESS:
                    if (c == '\n') {
                        //no-op
                    } else {
                        state = State.FAIL;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
    }

    private static class CulFhtDeviceOutBufferContentRequestParser extends CulResponseParser<CulFhtDeviceOutBufferContentRequest, CulFhtDeviceOutBufferContentResponse> {

        public CulFhtDeviceOutBufferContentRequestParser(CulFhtDeviceOutBufferContentRequest request) {
            super(new CulFhtDeviceOutBufferContentResponse(request));
        }

        public enum State {
            IDLE,
            N_PARSED,
            N_SLASH_PARSED,
            PARSE_HOUSECODE,
            PARSE_DATA,
            FAIL,
            SUCCESS;
        }

        private State state = State.IDLE;
        private short currentHousecode;
        private FhtParser fhtParser = new FhtParser(true, new ParserListener<FhtMessage>() {
            @Override
            public void fail(Throwable t) {
                state = State.FAIL;
            }

            @Override
            public void success(FhtMessage fhzMessage) {
                currentHousecode = fhzMessage.housecode;
                response.pendingMessages.add(fhzMessage);
            }

            @Override
            public void successPartial(FhtMessage fhzMessage) {
                currentHousecode = fhzMessage.housecode;
                response.pendingMessages.add(fhzMessage);
            }

            @Override
            public void successPartialAssembled(FhtMessage fhzMessage) {
                currentHousecode = fhzMessage.housecode;
                response.pendingMessages.add(fhzMessage);
            }
        });

        @Override
        public boolean isSuccess() {
            return state == State.SUCCESS;
        }

        @Override
        public void parse(char c) {
            switch (state) {
                case IDLE:
                    if (c == 'N') {
                        state = State.N_PARSED;
                    } else {
                        if (isFirstNibble) {
                            state = State.PARSE_HOUSECODE;
                            fhtParser.init();
                            firstNibble = digit2Byte(c);
                            isFirstNibble = false;
                        } else {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                    }
                    break;
                case PARSE_HOUSECODE:
                    if (isFirstNibble) {
                        if (c == ':') {
                            state = State.PARSE_DATA;
                        } else {
                            isFirstNibble = false;
                            firstNibble = digit2Byte(c);
                        }
                    } else {
                        isFirstNibble = true;
                        fhtParser.parse((byte) ((firstNibble << 4) | digit2Byte(c)));
                    }
                    break;
                case PARSE_DATA:
                    if (isFirstNibble) {
                        if (c == ',') {
                            fhtParser.initWithHousecode(currentHousecode);
                        } else if (c == ' ') {
                            state = State.IDLE;
                        } else if (c == '\r') {
                        } else if (c == '\n') {
                            state = State.SUCCESS;
                        } else {
                            isFirstNibble = false;
                            firstNibble = digit2Byte(c);
                        }
                    } else {
                        isFirstNibble = true;
                        fhtParser.parse((byte) ((firstNibble << 4) | digit2Byte(c)));
                    }
                    break;
                case N_PARSED:
                    if (c == '/') {
                        state = State.N_SLASH_PARSED;
                    } else {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                    break;
                case N_SLASH_PARSED:
                    if (c == 'A') {
                        state = State.SUCCESS;
                    } else {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                    break;
                case SUCCESS:
                    if ((c == '\r') || (c == '\n')) {

                    } else {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

            }
        }
    }

    private static class CulRemainingFhtDeviceOutBufferSizeRequestParser extends CulResponseParser<CulRemainingFhtDeviceOutBufferSizeRequest, CulRemainingFhtDeviceOutBufferSizeResponse> {

        public CulRemainingFhtDeviceOutBufferSizeRequestParser(CulRemainingFhtDeviceOutBufferSizeRequest request) {
            super(new CulRemainingFhtDeviceOutBufferSizeResponse(request));
        }

        public enum State {
            PARSE_SIZE,
            FAIL,
            SUCCESS;
        }

        private State state = State.PARSE_SIZE;

        @Override
        public boolean isSuccess() {
            return state == State.SUCCESS;
        }

        @Override
        public void parse(char c) {
            switch (state) {
                case PARSE_SIZE:
                    if (isFirstNibble) {
                        firstNibble = digit2Byte(c);
                        isFirstNibble = false;
                    } else {
                        state = State.SUCCESS;
                        isFirstNibble = true;
                        response.buffSize = (short) ((firstNibble << 4) | digit2Byte(c));
                    }
                    break;
                case SUCCESS:
                    if ((c == '\n') || (c == '\r')) {
                        //no-op
                    } else {
                        state = State.FAIL;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }

    }

    //TODO QAD fix generics
    public static <R extends CulRequest, T extends CulResponse<R>> CulResponseParser<R, T> of(R request) {
        if (request instanceof CulGetSlowRfSettingsRequest) {
            return (CulResponseParser<R, T>) new CulGetSlowRfSettingsRequestParser((CulGetSlowRfSettingsRequest) request);
        } else if (request instanceof CulFhtDeviceOutBufferContentRequest) {
            return (CulResponseParser<R, T>) new CulFhtDeviceOutBufferContentRequestParser((CulFhtDeviceOutBufferContentRequest) request);
        } else if (request instanceof CulRemainingFhtDeviceOutBufferSizeRequest) {
            return (CulResponseParser<R, T>) new CulRemainingFhtDeviceOutBufferSizeRequestParser((CulRemainingFhtDeviceOutBufferSizeRequest) request);
        } else {
            throw new UnsupportedOperationException("Cant find parser for request: " + request);
        }
    }

    final protected T response;

    protected CulResponseParser(T response) {
        this.response = response;
    }

    public abstract boolean isSuccess();
}
