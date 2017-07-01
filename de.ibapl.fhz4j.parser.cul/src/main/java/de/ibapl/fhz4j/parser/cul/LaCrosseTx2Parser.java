package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Message;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2Property;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import java.util.logging.Logger;

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
/**
 *
 * @author aploese
 */
class LaCrosseTx2Parser extends Parser {

    private enum State {

        START_SEQUENCE_A,
        SENSOR_TYTE,
        COLLECT_SENSOR_ADDRESS,
        COLLECT_DATA_1_3,
        COLLECT_DATA_4,
        COLLECT_DATA_5,
        CHECKSUM,
        PARSE_SUCCESS,
        PARSE_ERROR;
    ;

    }

    LaCrosseTx2Parser(ParserListener parserListener) {
        this.parserListener = parserListener;
    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private final ParserListener parserListener;
    private LaCrosseTx2Message laCrosseTx2Message;
    private State state;
    private int cs;

    @Override
    public void parse(char c) {
        cs += digit2Int(c);
        switch (state) {
            case START_SEQUENCE_A:
                if (c == 'A') {
                    state = State.SENSOR_TYTE;
                } else {
                    LOG.warning(String.format("Collect origin - Wrong char: 0x%02x %s", (byte)c, c));
                    state = State.PARSE_ERROR;
                    parserListener.fail(laCrosseTx2Message);
                    return;
                }
                break;
            case SENSOR_TYTE:
                switch (c) {
                    case '0':
                        laCrosseTx2Message.setProperty(LaCrosseTx2Property.TEMP);
                        break;
                    case 'E':
                        laCrosseTx2Message.setProperty(LaCrosseTx2Property.HUMIDITY);
                        break;
                    default:
                        LOG.warning(String.format("Collect origin - Wrong char: 0x%02x %s", (byte)c, c));
                        state = State.PARSE_ERROR;
                        parserListener.fail(laCrosseTx2Message);
                        return;
                }
                setStackSize(2);
                state = State.COLLECT_SENSOR_ADDRESS;
                break;
            case COLLECT_SENSOR_ADDRESS:
                try {
                    push(digit2Int(c));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect command - Wrong char: 0x%02x %s", (byte)c, c));
                    state = State.PARSE_ERROR;
                    parserListener.fail(laCrosseTx2Message);
                    return;
                }
                if (getStackpos() == 0) {
                    try {
                        laCrosseTx2Message.setAddress((short)(getShortValue() >> 1));
                    } catch (Exception ex) {
                        LOG.warning(String.format("Wrong Command - Wrong number: 0x%04x", getIntValue()));
                        state = State.PARSE_ERROR;
                        parserListener.fail(laCrosseTx2Message);
                        return;
                    }
                    setStackSize(3);
                    state = State.COLLECT_DATA_1_3;
                }
                break;
            case COLLECT_DATA_1_3:
                try {
                    pushBCD(digit2Int(c));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect command - Wrong char: 0x%02x %s", (byte)c, c));
                    state = State.PARSE_ERROR;
                    parserListener.fail(laCrosseTx2Message);
                    return;
                }
                if (getStackpos() == 0) {
                    try {
                        switch (laCrosseTx2Message.getProperty()) {
                            case TEMP:
                            laCrosseTx2Message.setData(0.1f * (getIntValue() - 500));
                            break;
                            case HUMIDITY:
                            laCrosseTx2Message.setData(0.1f * getIntValue());
                            break;
                            default:
                                throw new RuntimeException("Unknown Property: " + laCrosseTx2Message.getProperty());
                        }
                    } catch (Exception ex) {
                        LOG.warning(String.format("Wrong Command - Wrong number: 0x%04x", getIntValue()));
                        state = State.PARSE_ERROR;
                        parserListener.fail(laCrosseTx2Message);
                        return;
                    }
                    state = State.COLLECT_DATA_4;
                }
                break;
            case COLLECT_DATA_4:
                    state = State.COLLECT_DATA_5;
                // data byte 1 repeated
                break;
            case COLLECT_DATA_5:
                    state = State.CHECKSUM;
                // data byte 2 repeated
                break;
            case CHECKSUM:
                if (((cs - digit2Int(c)) & 0X0F) == digit2Int(c)) {
                    state = State.PARSE_SUCCESS;
                    parserListener.success(laCrosseTx2Message);
                } else {
                    LOG.warning("Check sum mismatch");
                    state = State.PARSE_ERROR;
                    parserListener.fail(laCrosseTx2Message);
                }

                break;
            default:
        }
    }

    @Override
    public void init() {
        state = State.START_SEQUENCE_A;
        cs = 0;
        laCrosseTx2Message = new LaCrosseTx2Message();
    }

}
