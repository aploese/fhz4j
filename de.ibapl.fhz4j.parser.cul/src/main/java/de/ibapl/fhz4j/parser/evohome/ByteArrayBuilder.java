/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2023, Arne Plöse and individual contributors as indicated
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

/**
 *
 * @author aploese
 */
public class ByteArrayBuilder {

    private int currentPos;
    private byte[] theArray;

    public void init(int capacity) {
        theArray = new byte[capacity];
        currentPos = 0;
    }

    public boolean push(byte b) {
        theArray[currentPos++] = b;
        return currentPos == theArray.length;
    }

    public byte[] getData() {
        if (currentPos != theArray.length) {
            throw new IllegalStateException("parsing not finished");
        }
        return theArray;
    }
}
