/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

/**
 *
 * @author aploese
 */
class Hms100RmMessage extends HmsMessage {
    
    Hms100RmMessage(HmsMessage hmsMessage) {
        super(hmsMessage);
    }
    
    private boolean smoke;

    @Override
    public void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(" smoke: ").append(smoke);
    }

    /**
     * @return the smoke
     */
    public boolean isSmoke() {
        return smoke;
    }

    /**
     * @param smoke the smoke to set
     */
    public void setSmoke(boolean smoke) {
        this.smoke = smoke;
    }

    
}
