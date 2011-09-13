/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.scada;

/**
 *
 * @author aploese
 */
public class Time {

    private byte hour;
    private byte min;
    private byte sec;
    private int ms;

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
        return String.format("%02d : %02d", hour, min);
    }
}
