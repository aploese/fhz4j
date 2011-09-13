/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.scada;

/**
 *
 * @author aploese
 */
public class Date {
    private int year;
    private byte month;
    private byte day;

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
}
