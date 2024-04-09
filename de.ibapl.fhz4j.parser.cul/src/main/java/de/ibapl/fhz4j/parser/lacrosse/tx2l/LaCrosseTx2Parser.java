/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2019-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.parser.lacrosse.tx2l;

import java.util.logging.Logger;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.parser.api.AbstractParser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Property;

/**
 *
 * @author Arne Plöse
 */
public class LaCrosseTx2Parser extends AbstractParser {

    private enum State {

        START_SEQUENCE_A_AND_SENSOR_TYTE, COLLECT_SENSOR_ADDRESS, COLLECT_DATA,
        PARSE_SUCCESS, PARSE_ERROR;

    }

    public LaCrosseTx2Parser(ParserListener<LaCrosseTx2Message> parserListener) {
        this.parserListener = parserListener;
    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private final ParserListener<LaCrosseTx2Message> parserListener;
    private LaCrosseTx2Message laCrosseTx2Message;
    private State state;
    private int cs;

    @Override
    public void parse(byte b) {
        try {
            cs += (b & 0x0F) + ((b >> 4) & 0x0F);
            switch (state) {
                case START_SEQUENCE_A_AND_SENSOR_TYTE -> {
                    laCrosseTx2Message = switch (b) {
                        case (byte) 0xA0 ->
                            new LaCrosseTx2Message(LaCrosseTx2Property.TEMP);
                        case (byte) 0xAE ->
                            new LaCrosseTx2Message(LaCrosseTx2Property.HUMIDITY);
                        default ->
                            throw new RuntimeException("Can't figure out the sensortype");
                    };
                    state = State.COLLECT_SENSOR_ADDRESS;
                }
                case COLLECT_SENSOR_ADDRESS -> {
                    // move only the lower nibble one bit to the right
                    laCrosseTx2Message.address = (byte) ((b & 0xF0) | (b >> 1) & 0x07);
                    setStackSize(3);
                    state = State.COLLECT_DATA;
                }
                case COLLECT_DATA -> {
                    if (push(b)) {
                        switch (laCrosseTx2Message.laCrosseTx2Property) {
                            case TEMP:
                                //Here we are only interested in the 3 highest (out of five) nibbles
                                // lowest nibble is cs
                                // 2 nibbles to the left are just the highest two nibbles repeated
                                laCrosseTx2Message.value = 0.1f * (get3DigitBCD((short) (getIntValue() >> 12)) - 500);
                                break;
                            case HUMIDITY:
                                //Here we are only interested in the 3 highest (out of five) nibbles
                                // lowest nibble is cs
                                // 2 nibbles to the left are just the highest two nibbles repeated
                                laCrosseTx2Message.value = 0.1f * (get3DigitBCD((short) (getIntValue() >> 12)));
                                break;
                            default:
                                throw new RuntimeException("Unknown Property: " + laCrosseTx2Message.laCrosseTx2Property);
                        }
                        if (((cs - (b & 0x0F)) & 0x0F) == (b & 0x0F)) {
                            state = State.PARSE_SUCCESS;
                            parserListener.success(laCrosseTx2Message);
                        } else {
                            // TODO Checksum ????
                            throw new RuntimeException("Check sum mismatch");
                        }
                    }
                }
                default ->
                    throw new RuntimeException("Cant handle stat: " + state);
            }
        } catch (Throwable t) {
            parserListener.fail(new RuntimeException(String.format("State: %s last byte 0x%02x", state, b), t));
            state = State.PARSE_ERROR;
        }
    }

    @Override
    public void init() {
        state = State.START_SEQUENCE_A_AND_SENSOR_TYTE;
        cs = 0;
        laCrosseTx2Message = null;
    }

}
