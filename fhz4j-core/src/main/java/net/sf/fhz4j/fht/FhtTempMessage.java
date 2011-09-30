/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.fht;

import net.sf.fhz4j.Fhz1000;
import net.sf.fhz4j.scada.AbstractPropertyProvider;

/**
 *
 * @author aploese
 */
public class FhtTempMessage extends AbstractPropertyProvider<FhtTempPropery> {
    
    private final FhtMessage low;
    private final FhtMessage high;
    
    public FhtTempMessage(FhtMessage low, FhtMessage high) {
        this.low = low;
        this.high = high;
    }
    
    public short getHousecode() {
        return high.getHousecode();
    }

    public double getTempValue() {
        return ((double)low.getRawValue()) / 10 + high.getRawValue() * 25.5;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("housecode: ").append(Fhz1000.houseCodeToString(low.getHousecode()));
        sb.append(", measured temperature (combined): ").append(getTempValue());
        sb.append(low.getCommand().getUnitOfMeasurement());
        sb.append(" signal strength: ").append(low.getSignalStrength()).append(" and ").append(high.getSignalStrength());
        return sb.toString();
    }

    @Override
    public double getDouble(FhtTempPropery p) {
            return getTempValue();
    }
    
    public FhtTempPropery getProperty() {
        return FhtTempPropery.COMBINED_TEMP;
    }

    public FhtProperty getCommand() {
        return high.getCommand();
    }

}
