package de.ibapl.fhz4j.parser.api;

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


/**
 *
 * @author aploese
 */
public abstract class Parser {
    private int value;
    private int stackpos;
    
    protected void setStackSize(int size) {
        value = 0;
        stackpos = size;
    }
    
    protected int getStackpos() {
        return stackpos;
    }

    public static int digit2Int(char c) {
        switch (c) {
            case '0':
                return 0x00;
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return 0x03;
            case '4':
                return 0x04;
            case '5':
                return 0x05;
            case '6':
                return 0x06;
            case '7':
                return 0x07;
            case '8':
                return 0x08;
            case '9':
                return 0x09;
            case 'A':
                return 0x0a;
            case 'B':
                return 0x0b;
            case 'C':
                return 0x0c;
            case 'D':
                return 0x0d;
            case 'E':
                return 0x0e;
            case 'F':
                return 0x0f;
            default:
                throw new RuntimeException("Not a Number: " + c);
        }
    }

    protected void push(int b) {
        value += b << (stackpos-- - 1) * 4;
    }

    protected void pushBCD(int b) {
        value *= 10;
        value += b;
        stackpos--;
    }

    protected  short getShortValue() {
        return (short) (value & 0x0000FFFF);
    }

    protected  byte getByteValue() {
        return (byte) (value & 0x000000FF);
    }

    protected int getIntValue() {
        return value;
    }

    public abstract void parse(char c);
    
    public abstract void init();
    
}
