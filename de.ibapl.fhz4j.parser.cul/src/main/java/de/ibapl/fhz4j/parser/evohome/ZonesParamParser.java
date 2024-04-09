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
package de.ibapl.fhz4j.parser.evohome;

import de.ibapl.fhz4j.parser.api.AbstractParser;
import de.ibapl.fhz4j.protocol.evohome.messages.ZoneConfigPayloadMessage;
import java.math.BigDecimal;
import java.util.LinkedList;

class ZonesParamParser extends AbstractParser {

    LinkedList<ZoneConfigPayloadMessage.ZoneParams> zoneParams;

    enum State {

        /**
         *
         */
        COLLECT_ZONEID,
        /**
         *
         */
        COLLECT_FLAGS,
        /**
         *
         */
        COLLECT_MIN_TEMP,
        /**
         *
         */
        COLLECT_MAX_TEMP, PARSE_SUCCESS, PARSE_ERROR;

    }

    // Just cache this ...
    private final static BigDecimal ONE_HUNDRED = new BigDecimal(100.0);

    State state;
    private short bytesToConsume;
    private short bytesConsumed;

    @Override
    public void parse(byte b) {
        bytesConsumed++;
        switch (state) {
            case COLLECT_ZONEID -> {
                zoneParams.addLast(new ZoneConfigPayloadMessage.ZoneParams());
                zoneParams.getLast().zoneId = b;
                state = State.COLLECT_FLAGS;
            }
            case COLLECT_FLAGS -> {
                zoneParams.getLast().windowFunction = (b & 0x10) == 0x10;
                zoneParams.getLast().operationLock = (b & 0x01) == 0x01;
                if ((b & 0xEE) != 0) {
                    throw new RuntimeException(String.format("Can't handle ZoneParams flags unexpected value: 0x%02x", b));
                }
                setStackSize(2);
                state = State.COLLECT_MIN_TEMP;
            }
            case COLLECT_MIN_TEMP -> {
                if (push(b)) {
                    zoneParams.getLast().minTemperature = new BigDecimal(getShortValue()).divide(ONE_HUNDRED);
                    setStackSize(2);
                    state = State.COLLECT_MAX_TEMP;
                }
            }
            case COLLECT_MAX_TEMP -> {
                if (push(b)) {
                    zoneParams.getLast().maxTemperature = new BigDecimal(getShortValue()).divide(ONE_HUNDRED);
                    if (bytesConsumed == bytesToConsume) {
                        state = State.PARSE_SUCCESS;
                    } else {
                        state = State.COLLECT_ZONEID;
                    }
                }
            }
            case PARSE_SUCCESS ->
                throw new IllegalStateException("PARSE_SUCCESS should not be called");
            case PARSE_ERROR ->
                throw new IllegalStateException("PARSE_ERROR should not be called");
            default ->
                throw new IllegalStateException(state.name());

        }
    }

    public void init(short bytesToConsume) {
        state = State.COLLECT_ZONEID;
        zoneParams = new LinkedList<>();
        this.bytesConsumed = 0;
        this.bytesToConsume = bytesToConsume;
    }

    // TODO change signature ???
    @Override
    public void init() {
        throw new RuntimeException("should not be called");
    }

}
