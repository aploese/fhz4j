/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.parser.em;

import java.util.logging.Logger;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.parser.api.AbstractParser;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.em.EmDeviceType;
import de.ibapl.fhz4j.protocol.em.EmMessage;

/**
 *
 * @author Arne Plöse
 */
public class EmParser extends AbstractParser {

    /*
	 * For EM: Ettaacc111122223333
	 *
	 * tt:type 01=EM-1000s, 02=EM-100-EM, 03=1000GZ aa:address, depending on the
	 * type above 01:01-04, 02:05-08, 03:09-12 cc:counter, will be incremented by
	 * one for each message 1111: cumulated value 2222: last value (Not set for type
	 * 2) 3333: top value (Not set for type 2)
	 *
     */
    @Override
    public void init() {
        state = State.COLLECT_TYPE;
        emMessage = null;
    }

    // swap bytes of short
    private int reorderBytes(int value) {
        // order in stack n, n+1, n+2, n+3, but we need n+2, n+3, n, n+1
        return (value & 0xFF00) >> 8 | (value & 0x00FF) << 8;
    }

    private enum State {

        COLLECT_TYPE, COLLECT_ADDRESS, COLLECT_COUNTER, COLLECT_CUMULATED_VALUE, COLLECT_5MIN_VALUE, COLLECT_5MIN_PEAK_VALUE, PARSE_SUCCESS, PARSE_ERROR;

    }

    public EmParser(ParserListener<EmMessage> parserListener) {
        this.parserListener = parserListener;
    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private final ParserListener<EmMessage> parserListener;
    private State state;
    private EmMessage emMessage;

    @Override
    public void parse(byte b) {
        try {
            switch (state) {
                case COLLECT_TYPE:
                    switch (b) {
                        case 1:
                            emMessage = new EmMessage(EmDeviceType.EM_1000_S);
                            break;
                        case 2:
                            emMessage = new EmMessage(EmDeviceType.EM_1000_EM);
                            break;
                        case 3:
                            emMessage = new EmMessage(EmDeviceType.EM_1000_GZ);
                            break;
                        default:
                            throw new RuntimeException("Wrong Type");
                    }
                    state = State.COLLECT_ADDRESS;
                    break;
                case COLLECT_ADDRESS:
                    emMessage.address = (short) (b & 0xff);
                    state = State.COLLECT_COUNTER;
                    break;
                case COLLECT_COUNTER:
                    emMessage.counter = (short) (b & 0xff);
                    setStackSize(2);
                    state = State.COLLECT_CUMULATED_VALUE;
                    break;
                case COLLECT_CUMULATED_VALUE:
                    if (push(b)) {
                        emMessage.valueCummulated = reorderBytes(getIntValue());
                        setStackSize(2);
                        state = State.COLLECT_5MIN_VALUE;
                    }
                    break;
                case COLLECT_5MIN_VALUE:
                    if (push(b)) {
                        emMessage.value5Min = reorderBytes(getIntValue());
                        setStackSize(2);
                        state = State.COLLECT_5MIN_PEAK_VALUE;
                    }
                    break;
                case COLLECT_5MIN_PEAK_VALUE:
                    if (push(b)) {
                        emMessage.value5MinPeak = reorderBytes(getIntValue());
                        state = State.PARSE_SUCCESS;
                        parserListener.success(emMessage);
                    }
                    break;
            }
        } catch (Throwable t) {
            parserListener.fail(new RuntimeException(String.format("State: %s last byte 0x%02x", state, b), t));
            state = State.PARSE_ERROR;
        }

    }

}
