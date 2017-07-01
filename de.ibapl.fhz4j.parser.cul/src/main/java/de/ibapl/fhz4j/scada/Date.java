package de.ibapl.fhz4j.scada;

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

import java.util.Calendar;


/**
 *
 * @author aploese
 */
public class Date {
    private int year;
    private byte month;
    private byte day;

    public Date(Calendar c) {
        day = (byte)c.get(Calendar.DAY_OF_MONTH);
        month = (byte)(c.get(Calendar.MONTH) + 1);
        year = c.get(Calendar.YEAR);
    }

    public Date() {
    
    }
    
    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the month
     */
    public byte getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(byte month) {
        this.month = month;
    }

    /**
     * @return the day
     */
    public byte getDay() {
        return day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(byte day) {
        this.day = day;
    }
    
    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

}
