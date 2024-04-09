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
package de.ibapl.fhz4j.parser.fs20;

import java.util.logging.Logger;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.parser.api.AbstractParser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.fs20.FS20CommandValue;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;

/**
 *
 * @author Arne Plöse
 */
public class FS20Parser extends AbstractParser {

    @Override
    public void init() {
        setStackSize(2);
        state = State.COLLECT_HOUSECODE;
        fs20Message = null;
    }

    private FS20CommandValue getFS20CommandValue(int intValue) {
        return switch (intValue) {
            case 0x00 ->
                FS20CommandValue.OFF;
            case 0x01 ->
                FS20CommandValue.DIM_6_PERCENT;
            case 0x02 ->
                FS20CommandValue.DIM_12_PERCENT;
            case 0x03 ->
                FS20CommandValue.DIM_18_PERCENT;
            case 0x04 ->
                FS20CommandValue.DIM_25_PERCENT;
            case 0x05 ->
                FS20CommandValue.DIM_31_PERCENT;
            case 0x06 ->
                FS20CommandValue.DIM_37_PERCENT;
            case 0x07 ->
                FS20CommandValue.DIM_43_PERCENT;
            case 0x08 ->
                FS20CommandValue.DIM_50_PERCENT;
            case 0x09 ->
                FS20CommandValue.DIM_56_PERCENT;
            case 0x0a ->
                FS20CommandValue.DIM_62_PERCENT;
            case 0x0b ->
                FS20CommandValue.DIM_68_PERCENT;
            case 0x0c ->
                FS20CommandValue.DIM_75_PERCENT;
            case 0x0d ->
                FS20CommandValue.DIM_81_PERCENT;
            case 0x0e ->
                FS20CommandValue.DIM_87_PERCENT;
            case 0x0f ->
                FS20CommandValue.DIM_93_PERCENT;
            case 0x10 ->
                FS20CommandValue.DIM_100_PERCENT;
            case 0x11 ->
                FS20CommandValue.ON;
            case 0x12 ->
                FS20CommandValue.TOGGLE;
            case 0x13 ->
                FS20CommandValue.DIM_UP;
            case 0x14 ->
                FS20CommandValue.DIM_DOWN;
            case 0x15 ->
                FS20CommandValue.DIM_UP_DOWN;
            case 0x16 ->
                FS20CommandValue.TIMER;
            case 0x17 ->
                FS20CommandValue.SENDSATE;
            case 0x18 ->
                FS20CommandValue.OFF_FOR_TIMER;
            case 0x19 ->
                FS20CommandValue.ON_FOR_TIMER;
            case 0x1a ->
                FS20CommandValue.ON_OLD_FOR_TIMER;
            case 0x1b ->
                FS20CommandValue.RESET;
            case 0x1c ->
                FS20CommandValue.RAMP_ON_TIME;
            case 0x1d ->
                FS20CommandValue.RAMP_OFF_TIME;
            case 0x1e ->
                FS20CommandValue.ON_OLD_FOR_TIMER_PREV;
            case 0x1f ->
                FS20CommandValue.ON_100_FOR_TIMER_PREV;
            default ->
                throw new RuntimeException();
        };
    }

    private enum State {

        COLLECT_HOUSECODE, COLLECT_OFFSET, COLLECT_COMMAND, PARSE_SUCCESS, PARSE_ERROR;

    }

    public FS20Parser(ParserListener<FS20Message> parserListener) {
        this.parserListener = parserListener;
    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private final ParserListener<FS20Message> parserListener;
    private State state;
    private FS20Message fs20Message;
    private short housecode;
    private byte offset;

    @Override
    public void parse(byte b) {
        try {
            switch (state) {
                case COLLECT_HOUSECODE -> {
                    if (push(b)) {
                        housecode = getShortValue();
                        state = State.COLLECT_OFFSET;
                    }
                }
                case COLLECT_OFFSET -> {
                    offset = b;
                    state = State.COLLECT_COMMAND;
                }
                case COLLECT_COMMAND -> {
                    fs20Message = new FS20Message(housecode, getFS20CommandValue(b & 0xff), offset);
                    state = State.PARSE_SUCCESS;
                    parserListener.success(fs20Message);
                }

            }
            // FHT
        } catch (Throwable t) {
            parserListener.fail(new RuntimeException(String.format("State: %s last byte 0x%02x", state, b), t));
            state = State.PARSE_ERROR;
        }
    }

}
