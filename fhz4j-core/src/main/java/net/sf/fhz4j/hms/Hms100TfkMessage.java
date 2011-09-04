/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

/**
 *
 * @author aploese
 */
class Hms100TfkMessage extends HmsMessage {
    
    Hms100TfkMessage(HmsMessage hmsMessage) {
        super(hmsMessage);
    }
    
    private boolean open;

    @Override
    public void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(" open: ").append(open);
    }

    /**
     * @return the open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * @param open the open to set
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    
}
