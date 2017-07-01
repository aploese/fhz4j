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


import java.util.logging.Logger;
import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.protocol.fs20.FS20CommandValues;
import de.ibapl.fhz4j.protocol.fs20.FS20Message;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;

/**
 *
 * @author aploese
 */
public class FS20Parser extends Parser {

    @Override
    public void init() {
        setStackSize(4);
        state = State.COLLECT_HOUSECODE;
        fs20Message = new FS20Message();
    }

    private enum State {

        COLLECT_HOUSECODE,
        COLLECT_OFFSET,
        COLLECT_COMMAND,
        PARSE_SUCCESS,
        PARSE_ERROR;

        }

    
    
    public FS20Parser(ParserListener parserListener) {
        this.parserListener = parserListener;
    }
    
    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private final ParserListener parserListener;
    private State state;
    private FS20Message fs20Message;

    @Override
    public void parse(char c) {
        switch(state) {
                        //FHT
            case COLLECT_HOUSECODE:
                try {
                    push(digit2Int(c));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect housecode - Wrong char: 0x%02x %s", (byte)c, c));
                    state = State.PARSE_ERROR;
                    parserListener.fail(fs20Message);
                    return;
                }
                if (getStackpos() == 0) {
                    fs20Message.setHousecode(getShortValue());
                    setStackSize(2);
                    state = State.COLLECT_OFFSET;
                }
                break;
            case COLLECT_OFFSET:
                try {
                    push(digit2Int(c));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect command - Wrong char: 0x%02x %s", (byte)c, c));
                    state = State.PARSE_ERROR;
                    parserListener.fail(fs20Message);
                    return;
                }
                if (getStackpos() == 0) {
                    try {
                        fs20Message.setOffset(getByteValue());
                    } catch (Exception ex) {
                        LOG.warning(String.format("Wrong Command - Wrong number: 0x%04x", getIntValue()));
                        state = State.PARSE_ERROR;
                    parserListener.fail(fs20Message);
                    return;
                    }
                    setStackSize(2);
                    state = State.COLLECT_COMMAND;
                }
                break;
            case COLLECT_COMMAND:
                try {
                    push(digit2Int(c));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect origin - Wrong char: 0x%02x %s", (byte)c, c));
                    state = State.PARSE_ERROR;
                    parserListener.fail(fs20Message);
                    return;
                }
                if (getStackpos() == 0) {
                    try {
                        fs20Message.setCommand(FS20CommandValues.valueOf(getIntValue()));
                        state = State.PARSE_SUCCESS;
                        parserListener.success(fs20Message);
                    } catch (IllegalArgumentException ex) {
                        LOG.severe(ex.getMessage());
                        state = State.PARSE_ERROR;
                        parserListener.fail(ex);
                    }
                    setStackSize(0);
                }
                break;

        }
    }
    
}
