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
import java.util.Set;
import java.util.logging.Logger;
import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.protocol.hms.Hms100RmMessage;
import de.ibapl.fhz4j.protocol.hms.Hms100TfMessage;
import de.ibapl.fhz4j.protocol.hms.Hms100TfkMessage;
import de.ibapl.fhz4j.protocol.hms.Hms100WdMessage;
import de.ibapl.fhz4j.protocol.hms.HmsDeviceStatus;
import de.ibapl.fhz4j.protocol.hms.HmsMessage;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;

/**
 *
 * @author aploese
 */
public class HmsParser extends Parser {

    private int[] rawValues;
    private int currentRawDataPos;

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

    public HmsParser(ParserListener<HmsMessage> parserListener) {
        this.parserListener = parserListener;
    }

    private static final Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
    private final ParserListener<HmsMessage> parserListener;
    private State state;
    private HmsMessage hmsMessage;
    private short housecode;
    private Set<HmsDeviceStatus> deviceStatus;

    @Override
    public void parse(char c) {
        try {
            switch (state) {
                case COLLECT_DEVICE_CODE:
                    push(digit2Int(c));
                    if (getStackpos() == 0) {
                        housecode = getShortValue();
                        setStackSize(0);
                        state = State.DEVICE_STATUS;
                    }
                    break;
                case DEVICE_STATUS:
                    deviceStatus = HmsDeviceStatus.valueOf(digit2Int(c));
                    setStackSize(0);
                    state = State.DEVICE_TYPE;
                    break;
                case DEVICE_TYPE:
                    switch (c) {
                        case '0':
                            setRaWDataSize(6);
                            state = State.COLLECT_HMS_100_TF_DATA;
                            break;
                        case '2':
                            setRaWDataSize(6);
                            state = State.COLLECT_HMS_100_WD_DATA;
                            break;
                        case '3':
                            setRaWDataSize(6);
                            state = State.COLLECT_HMS_100_RM_DATA;
                            break;
                        case '4':
                            setRaWDataSize(6);
                            state = State.COLLECT_HMS_100_TFK_DATA;
                            break;
                        default:
                            LOG.warning(String.format("Wrong device type - Wrong number: 0x%04x", digit2Int(c)));
                            state = State.PARSE_ERROR;
                            parserListener.fail(null);
                            return;
                    }
                    break;

                case COLLECT_HMS_100_TF_DATA:
                    setRawData(digit2Int(c));
                    if (isRawDataCollected()) {
                        final Hms100TfMessage hms100TfMessage = new Hms100TfMessage(housecode, deviceStatus);
                        hmsMessage = hms100TfMessage;
                        setStackSize(3);
                        pushBCD(rawValues[3]);
                        pushBCD(rawValues[0]);
                        pushBCD(rawValues[1]);
                        hms100TfMessage.temp = 0.1f * getIntValue();
                        setStackSize(3);
                        pushBCD(rawValues[4]);
                        pushBCD(rawValues[5]);
                        pushBCD(rawValues[2]);
                        hms100TfMessage.humidy = 0.1f * getIntValue();
                        state = State.PARSE_SUCCESS;
                        parserListener.success(hmsMessage);
                    }
                    break;
                case COLLECT_HMS_100_TFK_DATA:
                    setRawData(digit2Int(c));
                    if (isRawDataCollected()) {
                        final Hms100TfkMessage hms100TfkMessage = new Hms100TfkMessage(housecode, deviceStatus);
                        hmsMessage = hms100TfkMessage;
                        setStackSize(1);
                        push(rawValues[1]);
                        hms100TfkMessage.open = getByteValue() == 1;
                        state = State.PARSE_SUCCESS;
                        parserListener.success(hmsMessage);
                    }
                    break;
                case COLLECT_HMS_100_WD_DATA:
                    setRawData(digit2Int(c));
                    if (isRawDataCollected()) {
                        final Hms100WdMessage hms100WdMessage = new Hms100WdMessage(housecode, deviceStatus);
                        hmsMessage = hms100WdMessage;
                        setStackSize(1);
                        push(rawValues[1]);
                        hms100WdMessage.water = getByteValue() == 1;
                        state = State.PARSE_SUCCESS;
                        parserListener.success(hmsMessage);
                    }
                    break;
                case COLLECT_HMS_100_RM_DATA:
                    setRawData(digit2Int(c));
                    if (isRawDataCollected()) {
                        final Hms100RmMessage hms100RmMessage = new Hms100RmMessage(housecode, deviceStatus);
                        hmsMessage = hms100RmMessage;
                        setStackSize(1);
                        push(rawValues[1]);
                        hms100RmMessage.smoke = (getByteValue() == 1);
                        state = State.PARSE_SUCCESS;
                        parserListener.success(hmsMessage);
                    }
                    break;
            }
        } catch (Throwable t) {
            parserListener.fail(new RuntimeException(String.format("State: %s last char %s", state, c), t));
            state = State.PARSE_ERROR;
        }

    }

    @Override
    public void init() {
        setStackSize(4);
        state = State.COLLECT_DEVICE_CODE;
        hmsMessage = null;
    }
}
