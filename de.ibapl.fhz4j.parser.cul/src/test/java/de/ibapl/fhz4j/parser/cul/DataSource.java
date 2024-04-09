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
package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.parser.api.Parser;

public class DataSource {

    private final String data;

    public DataSource(String data) {
        this.data = data;
    }

    private byte char2Byte(char c) throws IllegalArgumentException {
        return switch (c) {
            case '0' ->
                0x00;
            case '1' ->
                0x01;
            case '2' ->
                0x02;
            case '3' ->
                0x03;
            case '4' ->
                0x04;
            case '5' ->
                0x05;
            case '6' ->
                0x06;
            case '7' ->
                0x07;
            case '8' ->
                0x08;
            case '9' ->
                0x09;
            case 'A' ->
                0x0a;
            case 'B' ->
                0x0b;
            case 'C' ->
                0x0c;
            case 'D' ->
                0x0d;
            case 'E' ->
                0x0e;
            case 'F' ->
                0x0f;
            default ->
                throw new IllegalArgumentException("Not a Number: " + c);
        };
    }

    public void iterate(Parser parser) {
        int i = 0;
        while (i < data.length() - 1) {
            // skip formating white spaces...
            while (data.charAt(i) == ' ' && i < data.length()) {
                i++;
            }
            if (i < data.length()) {
                int i1 = i + 1;
                while (data.charAt(i1) == ' ' && i1 < data.length()) {
                    i1++;
                }
                final byte b = (byte) ((char2Byte(data.charAt(i)) << 4) | char2Byte(data.charAt(i1)));
                parser.parse(b);
                i = i1 + 1;
            }
        }
    }
}
