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
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;

/**
 *
 * @author aploese
 */
public class FhtParser extends Parser {

    @Override
    public void init() {
        setStackSize(4);
        state = State.COLLECT_HOUSECODE;
        fhtMessage = new FhtMessage();
    }

    private enum State {

        COLLECT_HOUSECODE,
        COLLECT_COMMAND,
        COLLECT_ORIGIN,
        COLLECT_VALUE,
        PARSE_SUCCESS,
        PARSE_ERROR;

        }

    
    
    public FhtParser(ParserListener parserListener) {
        this.parserListener = parserListener;
    }
    
    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private final ParserListener parserListener;
    private State state;
    private FhtMessage fhtMessage;

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
                    parserListener.fail(fhtMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    fhtMessage.setHousecode(getShortValue());
                    setStackSize(2);
                    state = State.COLLECT_COMMAND;
                }
                break;
            case COLLECT_COMMAND:
                try {
                    push(digit2Int(c));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect command - Wrong char: 0x%02x %s", (byte)c, c));
                    state = State.PARSE_ERROR;
                    parserListener.fail(fhtMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    try {
                        fhtMessage.setCommand(FhtProperty.valueOf(getIntValue()));
                    } catch (Exception ex) {
                        LOG.warning(String.format("Wrong Command - Wrong number: 0x%04x", getIntValue()));
                        state = State.PARSE_ERROR;
                    parserListener.fail(fhtMessage);
                    return;
                    }
                    setStackSize(2);
                    state = State.COLLECT_ORIGIN;
                }
                break;
            case COLLECT_ORIGIN:
                try {
                    push(digit2Int(c));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect origin - Wrong char: 0x%02x %s", (byte)c, c));
                    state = State.PARSE_ERROR;
                    parserListener.fail(fhtMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    fhtMessage.setDescription(getByteValue());
                    setStackSize(2);
                    state = State.COLLECT_VALUE;
                }
                break;
            case COLLECT_VALUE:
                try {
                    push(digit2Int(c));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect value - Wrong char: 0x%02x %s", (byte)c, c));
                    state = State.PARSE_ERROR;
                    parserListener.fail(fhtMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    fhtMessage.setRawValue(getIntValue());
                    setStackSize(2);
                    state = State.PARSE_SUCCESS;
                    parserListener.success(fhtMessage);
                }
                break;

        }
    }
    
}
