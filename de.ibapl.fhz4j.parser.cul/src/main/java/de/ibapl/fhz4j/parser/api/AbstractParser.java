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
package de.ibapl.fhz4j.parser.api;

/**
 *
 * @author Arne Plöse
 */
public abstract class AbstractParser implements Parser {

    private int value;
    private int stackpos;

    protected void setStackSize(int size) {
        value = 0;
        stackpos = size;
    }

    protected int getStackpos() {
        return stackpos;
    }

    /**
     * pushes the byte onto the stack.
     *
     * @param b the byte to push onto the stack.
     * @return true if the stack is ready for read.
     */
    protected boolean push(byte b) {
        assert stackpos > 0;
        value += (b & 0xff) << (stackpos-- - 1) * 8;
        return stackpos == 0;
    }

    protected short getShortValue() {
        assert stackpos == 0;
        return (short) (value & 0x0000FFFF);
    }

    protected byte getByteValue() {
        assert stackpos == 0;
        return (byte) (value & 0x000000FF);
    }

    protected int getIntValue() {
        assert stackpos == 0;
        return value;
    }

    protected static short get3DigitBCD(short bcd) {
        return (short) ((((bcd >> 8) & 0x0f) * 100) + (((bcd >> 4) & 0x0f) * 10) + (bcd & 0x0f));
    }

}
