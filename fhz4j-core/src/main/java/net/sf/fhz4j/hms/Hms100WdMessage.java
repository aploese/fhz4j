/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

/**
 *
 * @author aploese
 */
class Hms100WdMessage extends HmsMessage {
    
    Hms100WdMessage(HmsMessage hmsMessage) {
        super(hmsMessage);
    }
    
    private boolean water;

    @Override
    public void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(" water: ").append(water);
    }

    /**
     * @return the water
     */
    public boolean isWater() {
        return water;
    }

    /**
     * @param water the water to set
     */
    public void setWater(boolean water) {
        this.water = water;
    }

    
}
