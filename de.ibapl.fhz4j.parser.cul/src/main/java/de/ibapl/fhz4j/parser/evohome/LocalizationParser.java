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
package de.ibapl.fhz4j.parser.evohome;

import de.ibapl.fhz4j.parser.api.Parser;

/**
 * Header 3C or 18 command 0004
 *
 * @author aploese
 *
 */
class LocalizationParser implements Parser {

    StringBuilder nameBuilder = new StringBuilder();
    byte unused0;
    byte unused1;

    enum State {

        /**
         *
         */
        COLLECT_UNKNOWN0,
        /**
         *
         */
        COLLECT_LOCALIZATION_NAME,
        /**
         *
         */
        COLLECT_UNKNOWN1,
        /**
         *
         */
        PARSE_SUCCESS, PARSE_ERROR;

    }

    State state;
    private int bytesToConsume;
    private int bytesConsumed;

    @Override
    public void parse(byte b) {
        bytesConsumed++;
        switch (state) {
            case COLLECT_UNKNOWN0:
                unused0 = b;
                if (unused0 != 0) {
                    throw new RuntimeException("unused0 != 0x00");
                }
                if (bytesConsumed == bytesToConsume - 1) { //unknown1 ist the last byte to consume
                    state = State.COLLECT_UNKNOWN1;
                } else {
                    state = State.COLLECT_LOCALIZATION_NAME;
                }
                break;
            case COLLECT_LOCALIZATION_NAME:
                if (b != (byte) 0xFF) {// last 2 bytes are 0xFF, so skip them
                    nameBuilder.append((char) b);
                }
                if (bytesConsumed == bytesToConsume - 1) { //unknown1 ist the last byte to consume
                    state = State.COLLECT_UNKNOWN1;
                } else {
                }
                break;
            case COLLECT_UNKNOWN1:
                unused1 = b;
                if (unused1 != (byte) 0xFF) {
                    throw new RuntimeException("unused1 != 0xFF");
                }
                state = State.PARSE_SUCCESS;
                break;
            case PARSE_SUCCESS:
                throw new RuntimeException("PARSE_SUCCESS should not be called");
            case PARSE_ERROR:
                throw new RuntimeException("PARSE_ERROR should not be called");

        }
    }

    public void init(short bytesToConsume) {
        state = State.COLLECT_UNKNOWN0;
        nameBuilder.setLength(0);
        unused0 = 0;
        unused1 = 0;
        this.bytesConsumed = 0;
        this.bytesToConsume = bytesToConsume;
    }

    // TODO change signature ???
    @Override
    public void init() {
        throw new RuntimeException("should not be called");
    }

}
