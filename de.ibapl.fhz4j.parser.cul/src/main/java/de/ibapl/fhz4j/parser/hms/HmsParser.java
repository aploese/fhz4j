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
package de.ibapl.fhz4j.parser.hms;

import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.parser.api.AbstractParser;
import de.ibapl.fhz4j.parser.api.Parser;
import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.protocol.hms.Hms100RmMessage;
import de.ibapl.fhz4j.protocol.hms.Hms100TfMessage;
import de.ibapl.fhz4j.protocol.hms.Hms100TfkMessage;
import de.ibapl.fhz4j.protocol.hms.Hms100WdMessage;
import de.ibapl.fhz4j.protocol.hms.HmsDeviceStatus;
import de.ibapl.fhz4j.protocol.hms.HmsMessage;

/**
 *
 * @author Arne Plöse
 */
public class HmsParser extends AbstractParser {

    private enum State {

        COLLECT_DEVICE_CODE, DEVICE_STATUS_AND_TYPE, COLLECT_HMS_100_TF_DATA_TEMP,
        COLLECT_HMS_100_TF_DATA_HUM,
        COLLECT_HMS_100_TFK_FLAGS,
        COLLECT_HMS_100_TFK_DATA,
        COLLECT_HMS_100_WD_FLAGS, COLLECT_HMS_100_WD_DATA,
        COLLECT_HMS_100_RM_DATA,
        PARSE_SUCCESS, PARSE_ERROR;
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
    public void parse(byte b) {
        try {
            switch (state) {
                case COLLECT_DEVICE_CODE -> {
                    if (push(b)) {
                        housecode = getShortValue();
                        state = State.DEVICE_STATUS_AND_TYPE;
                    }
                }
                case DEVICE_STATUS_AND_TYPE -> {
                    deviceStatus = EnumSet.noneOf(HmsDeviceStatus.class);
                    if ((b & 0x20) == 0x20) {
                        deviceStatus.add(HmsDeviceStatus.BATT_LOW);
                    }
                    switch (b & 0x0f) {
                        case 0x00 -> {
                            setStackSize(2);
                            state = State.COLLECT_HMS_100_TF_DATA_TEMP;
                        }
                        case 0x02 ->
                            state = State.COLLECT_HMS_100_WD_FLAGS;
                        case 0x03 -> {
                            setStackSize(3);
                            state = State.COLLECT_HMS_100_RM_DATA;
                        }
                        case 0x04 ->
                            state = State.COLLECT_HMS_100_TFK_FLAGS;
                        default -> {
                            LOG.warning(String.format("Wrong device type - Wrong number: 0x%02x", b));
                            state = State.PARSE_ERROR;
                            parserListener.fail(null);
                            return;
                        }
                    }
                }
                case COLLECT_HMS_100_TF_DATA_TEMP -> {
                    if (push(b)) {
                        final Hms100TfMessage hms100TfMessage = new Hms100TfMessage(housecode, deviceStatus);
                        hmsMessage = hms100TfMessage;
                        short valueBCD = swapBytes((short) (getShortValue() & 0xff0f));
                        hms100TfMessage.temp = 0.1f * get3DigitBCD(valueBCD);
                        state = State.COLLECT_HMS_100_TF_DATA_HUM;
                        byte humPartial = (byte) (getByteValue() & 0xf0);
                        setStackSize(2);
                        push(humPartial);
                        state = State.COLLECT_HMS_100_TF_DATA_HUM;
                    }
                }
                case COLLECT_HMS_100_TF_DATA_HUM -> {
                    if (push(b)) {
                        final Hms100TfMessage hms100TfMessage = (Hms100TfMessage) hmsMessage;
                        final short valueBCD = (short) (swapBytes(getShortValue()) >> 4);
                        hms100TfMessage.humidy = 0.1f * (get3DigitBCD(valueBCD));
                        state = State.PARSE_SUCCESS;
                        parserListener.success(hmsMessage);
                    }
                }
                case COLLECT_HMS_100_TFK_FLAGS -> {
                    final Hms100TfkMessage hms100TfkMessage = new Hms100TfkMessage(housecode, deviceStatus);
                    hmsMessage = hms100TfkMessage;
                    hms100TfkMessage.open = (b & 0x01) == 0x01;
                    setStackSize(2);
                    state = State.COLLECT_HMS_100_TFK_DATA;
                }
                case COLLECT_HMS_100_TFK_DATA -> {
                    if (push(b)) {
                        state = State.PARSE_SUCCESS;
                        parserListener.success(hmsMessage);
                    }
                }
                case COLLECT_HMS_100_WD_FLAGS -> {
                    final Hms100WdMessage hms100WdMessage = new Hms100WdMessage(housecode, deviceStatus);
                    hmsMessage = hms100WdMessage;
                    hms100WdMessage.water = (b & 0x01) == 0x01;
                    setStackSize(2);
                    state = State.COLLECT_HMS_100_WD_DATA;
                }
                case COLLECT_HMS_100_WD_DATA -> {
                    if (push(b)) {
                        // unknown, just drop it
                        state = State.PARSE_SUCCESS;
                        parserListener.success(hmsMessage);
                    }
                }
                case COLLECT_HMS_100_RM_DATA -> {
                    if (push(b)) {
                        final Hms100RmMessage hms100RmMessage = new Hms100RmMessage(housecode, deviceStatus);
                        hmsMessage = hms100RmMessage;
                        hms100RmMessage.smoke = b == 1;
                        state = State.PARSE_SUCCESS;
                        parserListener.success(hmsMessage);
                    }
                }
                default ->
                    new IllegalStateException(state.name());
            }
        } catch (Throwable t) {
            parserListener.fail(new RuntimeException(String.format("State: %s last byte 0x%02x", state, b), t));
            state = State.PARSE_ERROR;
        }

    }

    private short swapBytes(short s) {
        return (short) (((s & 0xff00) >> 8) | ((s & 0x00ff) << 8));
    }

    @Override
    public void init() {
        setStackSize(2);
        state = State.COLLECT_DEVICE_CODE;
        hmsMessage = null;
    }
}
