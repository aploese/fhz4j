/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.fht;

import net.sf.fhz4j.Parser;
import net.sf.fhz4j.ParserListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aploese
 */
public class FhtParser extends Parser {

    @Override
    public void init() {
        setStackSize(4);
        setState(State.COLLECT_HOUSECODE);
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
    
    private static final Logger LOG = LoggerFactory.getLogger(FhtParser.class);
    private final ParserListener parserListener;
    private State state;
    private FhtMessage fhtMessage;

    private void setState(State state) {
        LOG.trace(String.format("Set state from %s to %s", this.state, state));
        this.state = state;
    }


    @Override
    public void parse(int b) {
        switch(state) {
                        //FHT
            case COLLECT_HOUSECODE:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warn(String.format("Collect housecode - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(fhtMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    fhtMessage.setHousecode(getShortValue());
                    setStackSize(2);
                    setState(State.COLLECT_COMMAND);
                }
                break;
            case COLLECT_COMMAND:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warn(String.format("Collect command - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(fhtMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    try {
                        fhtMessage.setCommand(FhtProperty.valueOf(getIntValue()));
                    } catch (Exception ex) {
                        LOG.warn(String.format("Wrong Command - Wrong number: 0x%04x", getIntValue()));
                        setState(State.PARSE_ERROR);
                    parserListener.fail(fhtMessage);
                    return;
                    }
                    setStackSize(2);
                    setState(State.COLLECT_ORIGIN);
                }
                break;
            case COLLECT_ORIGIN:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warn(String.format("Collect origin - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(fhtMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    fhtMessage.setDescription(getByteValue());
                    setStackSize(2);
                    setState(State.COLLECT_VALUE);
                }
                break;
            case COLLECT_VALUE:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warn(String.format("Collect value - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(fhtMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    fhtMessage.setRawValue(getIntValue());
                    setStackSize(2);
                    setState(State.PARSE_SUCCESS);
                    parserListener.success(fhtMessage);
                }
                break;

        }
    }
    
}
