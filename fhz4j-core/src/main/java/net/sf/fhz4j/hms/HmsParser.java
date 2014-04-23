package net.sf.fhz4j.hms;

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
public class HmsParser extends Parser {

    private int[] rawValues;
    private int currentRawDataPos;
    private StringBuilder rawValuesSb = new StringBuilder();
    
    private void setRaWDataSize(int newLength) {
        if (rawValues == null || rawValues.length != newLength) {
            rawValues = new int[newLength];
        }
        currentRawDataPos = 0;
    }

    private void setRawData(int value) {
        rawValues[currentRawDataPos++] = value;
    }

    private boolean isRawDataCollected() {
        return currentRawDataPos >= rawValues.length;
    }

    private enum State {

        COLLECT_DEVICE_CODE,
        DEVICE_STATUS,
        DEVICE_TYPE,
        COLLECT_HMS_100_TF_DATA,
        COLLECT_HMS_100_TFK_DATA,
        COLLECT_HMS_100_WD_DATA,
        COLLECT_HMS_100_RM_DATA,
        PARSE_SUCCESS,
        PARSE_ERROR;
    }

    public HmsParser(ParserListener parserListener) {
        this.parserListener = parserListener;
    }
    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_CORE);
    private final ParserListener parserListener;
    private State state;
    private HmsMessage hmsMessage;

    private void setState(State state) {
        LOG.log(Level.FINEST, "Set state from {0} to {1}", new Object[] {this.state, state});
        this.state = state;
    }

    @Override
    public void parse(int b) {
        rawValuesSb.append((char)b);
        switch (state) {
            case COLLECT_DEVICE_CODE:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect device code - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(hmsMessage);
                    return;
                }
                if (getStackpos() == 0) {
                    hmsMessage.setHousecode(getShortValue());
                    setStackSize(1);
                    setState(State.DEVICE_STATUS);
                }
                break;
            case DEVICE_STATUS:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect device type - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(hmsMessage);
                    return;
                }
                try {
                    hmsMessage.setDeviceStatus(HmsDeviceStatus.valueOf(getIntValue()));
                } catch (Exception ex) {
                    LOG.warning(String.format("Wrong device type - Wrong number: 0x%04x", getIntValue()));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(hmsMessage);
                    return;
                }
                setStackSize(1);
                setState(State.DEVICE_TYPE);
                break;
            case DEVICE_TYPE:
                try {
                    push(digit2Int(b));
                } catch (RuntimeException ex) {
                    LOG.warning(String.format("Collect device type - Wrong char: 0x%02x %s", b, (char) b));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(hmsMessage);
                    return;
                }
                try {
                    hmsMessage.setDeviceType(HmsDeviceType.valueOf(getIntValue()));
                } catch (Exception ex) {
                    LOG.warning(String.format("Wrong device type - Wrong number: 0x%04x", getIntValue()));
                    setState(State.PARSE_ERROR);
                    parserListener.fail(hmsMessage);
                    return;
                }
                switch (hmsMessage.getDeviceType()) {
                    case HMS_100_TF:
                        setRaWDataSize(6);
                        setState(State.COLLECT_HMS_100_TF_DATA);
                        break;
                    case HMS_100_TFK:
                        setRaWDataSize(6);
                        setState(State.COLLECT_HMS_100_TFK_DATA);
                        break;
                    case HMS_100_WD:
                        setRaWDataSize(6);
                        setState(State.COLLECT_HMS_100_WD_DATA);
                        break;
                    case HMS_100_RM:
                        setRaWDataSize(6);
                        setState(State.COLLECT_HMS_100_RM_DATA);
                        break;
                    default:
                        setState(State.PARSE_ERROR);
                        parserListener.fail(hmsMessage);
                }
                break;

            case COLLECT_HMS_100_TF_DATA:
                setRawData(digit2Int(b));
                if (isRawDataCollected()) {
                    final Hms100TfMessage hms100TfMessage = new Hms100TfMessage(hmsMessage);
                    hmsMessage = hms100TfMessage;
                    setStackSize(3);
                    pushBCD(rawValues[3]);
                    pushBCD(rawValues[0]);
                    pushBCD(rawValues[1]);
                    hms100TfMessage.setTemp(((double) getIntValue()) / 10.0);
                    setStackSize(3);
                    pushBCD(rawValues[4]);
                    pushBCD(rawValues[5]);
                    pushBCD(rawValues[2]);
                    hms100TfMessage.setHumidy(((double) getIntValue()) / 10.0);
                    hms100TfMessage.setRawValue(rawValuesSb.toString());
                    setState(State.PARSE_SUCCESS);
                    parserListener.success(hmsMessage);
                }
                break;
            case COLLECT_HMS_100_TFK_DATA:
                setRawData(digit2Int(b));
                if (isRawDataCollected()) {
                    final Hms100TfkMessage hms100TfkMessage = new Hms100TfkMessage(hmsMessage);
                    hmsMessage = hms100TfkMessage;
                    setStackSize(1);
                    push(rawValues[1]);
                    hms100TfkMessage.setOpen(getByteValue() == 1);
                    hms100TfkMessage.setRawValue(rawValuesSb.toString());
                    setState(State.PARSE_SUCCESS);
                    parserListener.success(hmsMessage);
                }
                break;
            case COLLECT_HMS_100_WD_DATA:
                setRawData(digit2Int(b));
                if (isRawDataCollected()) {
                    final Hms100WdMessage hms100WdMessage = new Hms100WdMessage(hmsMessage);
                    hmsMessage = hms100WdMessage;
                    setStackSize(1);
                    push(rawValues[1]);
                    hms100WdMessage.setWater(getByteValue() == 1);
                    hms100WdMessage.setRawValue(rawValuesSb.toString());
                    setState(State.PARSE_SUCCESS);
                    parserListener.success(hmsMessage);
                }
                break;
            case COLLECT_HMS_100_RM_DATA:
                setRawData(digit2Int(b));
                if (isRawDataCollected()) {
                    final Hms100RmMessage hms100RmMessage = new Hms100RmMessage(hmsMessage);
                    hmsMessage = hms100RmMessage;
                    setStackSize(1);
                    push(rawValues[1]);
                    hms100RmMessage.setSmoke(getByteValue() == 1);
                    hms100RmMessage.setRawValue(rawValuesSb.toString());
                    setState(State.PARSE_SUCCESS);
                    parserListener.success(hmsMessage);
                }
                break;
        }
    }

    @Override
    public void init() {
        setStackSize(4);
        setState(State.COLLECT_DEVICE_CODE);
        hmsMessage = new HmsMessage();
        rawValuesSb = new StringBuilder('H');
   }
}
