/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

import java.util.EnumSet;
import java.util.Set;
import net.sf.fhz4j.Fhz1000;
import net.sf.fhz4j.FhzMessage;


/**
 *
 * @author aploese
 */
public class HmsMessage extends FhzMessage {
    
    private short devicecode;
    private HmsDeviceType deviceType;
    private Set<HmsDeviceStatus> deviceStatus = EnumSet.noneOf(HmsDeviceStatus.class);
 
    HmsMessage(HmsMessage hmsMessage) {
        this();
        devicecode = hmsMessage.devicecode;
        deviceType = hmsMessage.deviceType;
        deviceStatus = hmsMessage.deviceStatus;
    }

    HmsMessage() {
        super();
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(String.format("devicecode: %04X, device type: %s", devicecode, deviceType.getLabel()));
        sb.append(" status: [");
        boolean first = true;
        for (HmsDeviceStatus status : deviceStatus) {
            if (first) {
                first = false;
            } else {
                sb.append(" ,");
            }
            sb.append(status.getLabel());
        }
        sb.append(" ]");
    }

    /**
     * @return the devicecode
     */
    public short getDevicecode() {
        return devicecode;
    }

    /**
     * @param devicecode the devicecode to set
     */
    public void setDevicecode(short devicecode) {
        this.devicecode = devicecode;
    }

    /**
     * @return the deviceType
     */
    public HmsDeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType the deviceType to set
     */
    public void setDeviceType(HmsDeviceType deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return the deviceStatus
     */
    public Set<HmsDeviceStatus> getDeviceStatus() {
        return deviceStatus;
    }

    /**
     * @param deviceStatus the deviceStatus to set
     */
    public void setDeviceStatus(Set<HmsDeviceStatus> deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
    
}
