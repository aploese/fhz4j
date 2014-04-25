package net.sf.fhz4j.em;

/*
 * #%L
 * fhz4j Core
 * %%
 * Copyright (C) 2009 - 2014 fhz4j
 * %%
 * fhz4j - Drivers for the Wireless FS20, FHT and HMS protocol http://fhz4j.sourceforge.net/
 * Copyright (C) 2009-2014, fhz4j.sf.net, and individual contributors as indicated
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
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.fhz4j.LogUtils;
import net.sf.fhz4j.Parser;
import net.sf.fhz4j.ParserListener;

/**
 *
 * @author aploese
 */
public class EmParser extends Parser {

    /*    
     For EM:
     Ettaacc111122223333

     tt:type 01=EM-1000s, 02=EM-100-EM, 03=1000GZ
     aa:address, depending on the type above 01:01-04, 02:05-08, 03:09-12
     cc:counter, will be incremented by one for each message
     1111: cumulated value
     2222: last value (Not set for type 2)
     3333: top value (Not set for type 2) 

     */
    @Override
    public void init() {
        setStackSize(2);
        setState(State.COLLECT_TYPE);
        emMessage = null;
    }

    private int reorderBytes(int value) {
        // order in stack n, n+1, n+2, n+3, but we need n+2, n+3, n, n+1
        return (value & 0xFF00) >> 8 | (value & 0x00FF) << 8;
    }

    private enum State {

        COLLECT_TYPE,
        COLLECT_ADDRESS,
        COLLECT_COUNTER,
        COLLECT_CUMULATED_VALUE,
        COLLECT_LAST_VALUE,
        COLLECT_LAST_MAX_VALUE,
        PARSE_SUCCESS,
        PARSE_ERROR;

    }

    public EmParser(ParserListener parserListener) {
        this.parserListener = parserListener;
    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_CORE);
    private final ParserListener parserListener;
    private State state;
    private EmMessage emMessage;

    private void setState(State state) {
        LOG.log(Level.FINEST, "Set state from {0} to {1}", new Object[]{this.state, state});
        this.state = state;
    }

    @Override
    public void parse(int b) {
        switch (state) {
            case COLLECT_TYPE:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect type - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(emMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    switch (getShortValue()) {
                        case 1:
                            emMessage = new Em1000SMessage();
                            break;
                        case 2:
                            emMessage = new Em1000EmMessage();
                            break;
                        case 3:
                            emMessage = new Em1000GzMessage();
                            break;
                        default:
                            LOG.warning(String.format("Collect command - Wrong char: 0x%02x %s", b, (char) b));
                            setState(State.PARSE_ERROR);
                            parserListener.fail(emMessage);
                            return;
                    }
                    setStackSize(2);
                    setState(State.COLLECT_ADDRESS);
                }
                break;
            case COLLECT_ADDRESS:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect address - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(emMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    try {
                        emMessage.setAddress(getShortValue());
                    } catch (Exception ex) {
                        LOG.warning(String.format("Wrong address - Wrong number: 0x%02x", getShortValue()));
                        setState(State.PARSE_ERROR);
                        parserListener.fail(emMessage);
                        return;
                    }
                    setStackSize(2);
                    setState(State.COLLECT_COUNTER);
                }
                break;
            case COLLECT_COUNTER:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect counter - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(emMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    emMessage.setCounter(getShortValue());
                    setStackSize(4);
                    setState(State.COLLECT_CUMULATED_VALUE);
                }
                break;
            case COLLECT_CUMULATED_VALUE:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect cumulated value - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(emMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    emMessage.setCumulatedValue(reorderBytes(getIntValue()));
                    setStackSize(4);
                    setState(State.COLLECT_LAST_VALUE);
                }
                break;
            case COLLECT_LAST_VALUE:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect last value - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(emMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    emMessage.setLastValue(reorderBytes(getIntValue()));
                    setStackSize(4);
                    setState(State.COLLECT_LAST_MAX_VALUE);
                }
                break;
            case COLLECT_LAST_MAX_VALUE:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect top value - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(emMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    emMessage.setMaxLastValue(reorderBytes(getIntValue()));
                    setStackSize(2);
                    setState(State.PARSE_SUCCESS);
                    parserListener.success(emMessage);
                }
                break;
        }
    }

}
