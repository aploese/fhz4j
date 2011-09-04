/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

/**
 *
 * @author aploese
 */
class Hms100TfMessage extends HmsMessage {
    
    private double temp;
    private double humidy;

    Hms100TfMessage(HmsMessage hmsMessage) {
        super(hmsMessage);
    }

    /**
     * @return the temp
     */
    public double getTemp() {
        return temp;
    }

    /**
     * @param temp the temp to set
     */
    public void setTemp(double temp) {
        this.temp = temp;
    }

    /**
     * @return the humidy
     */
    public double getHumidy() {
        return humidy;
    }

    /**
     * @param humidy the humidy to set
     */
    public void setHumidy(double humidy) {
        this.humidy = humidy;
    }
    
    @Override
    public void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(" temp: ").append(temp);
        sb.append(", humidy: ").append(humidy);
    }

    
}
