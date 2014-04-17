/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j;

import net.sf.fhz4j.scada.AbstractPropertyProvider;
import net.sf.fhz4j.scada.ScadaProperty;

/**
 *
 * @author aploese
 */
public abstract class FhzMessage<T extends ScadaProperty> extends AbstractPropertyProvider<T> {
    
    private float signalStrength;

    /**
     * @return the signalStrength
     */
    public float getSignalStrength() {
        return signalStrength;
    }

    /**
     * @param signalStrength the signalStrength to set
     */
    public void setSignalStrength(float signalStrength) {
        this.signalStrength = signalStrength;
    }
    
    protected abstract void toString(StringBuilder sb);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
        sb.append(" signal strength: ").append(getSignalStrength()).append(" dB");
        return sb.toString();
    }

}
