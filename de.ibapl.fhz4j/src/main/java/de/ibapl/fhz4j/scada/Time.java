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
public class Time {

    private byte hour;
    private byte min;
    private byte sec;
    private int ms;

    public Time() {
        
    }
    
    public Time(Calendar c) {
        hour = (byte)c.get(Calendar.HOUR_OF_DAY);
        min = (byte)c.get(Calendar.MINUTE);
        sec = (byte)c.get(Calendar.SECOND);
        ms = (byte)c.get(Calendar.MILLISECOND);
    }

    /**
     * @return the hour
     */
    public byte getHour() {
        return hour;
    }

    /**
     * @param hour the hour to set
     */
    public void setHour(byte hour) {
        this.hour = hour;
    }

    /**
     * @return the min
     */
    public byte getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(byte min) {
        this.min = min;
    }

    /**
     * @return the sec
     */
    public byte getSec() {
        return sec;
    }

    /**
     * @param sec the sec to set
     */
    public void setSec(byte sec) {
        this.sec = sec;
    }

    /**
     * @return the ms
     */
    public int getMs() {
        return ms;
    }

    /**
     * @param ms the ms to set
     */
    public void setMs(int ms) {
        this.ms = ms;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d.%03d", hour, min, sec, ms);
    }

    public void setTimeToCalendar(Calendar c) {
        c.set(Calendar.MILLISECOND, ms);
        c.set(Calendar.SECOND, sec);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.HOUR_OF_DAY, hour);
    }
}
