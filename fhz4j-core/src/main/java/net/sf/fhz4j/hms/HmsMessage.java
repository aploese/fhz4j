/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j.hms;

import java.util.EnumSet;
import java.util.Set;
import net.sf.fhz4j.FhzMessage;

/**
 *
 * @author aploese
 */
public class HmsMessage extends FhzMessage<HmsProperty> {

    private short housecode;
    private HmsDeviceType deviceType;
    private Set<HmsDeviceStatus> deviceStatus = EnumSet.noneOf(HmsDeviceStatus.class);
    private String rawValue;

    HmsMessage(HmsMessage hmsMessage) {
        this();
        housecode = hmsMessage.housecode;
        deviceType = hmsMessage.deviceType;
        deviceStatus = hmsMessage.deviceStatus;
    }

    HmsMessage() {
        super();
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(String.format("housecode: %04X, device type: %s", housecode, deviceType.getLabel()));
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
     * @return the housecode
     */
    public short getHousecode() {
        return housecode;
    }

    /**
     * @param housecode the housecode to set
     */
    public void setHousecode(short housecode) {
        this.housecode = housecode;
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

    @Override
    public boolean getBoolean(HmsProperty prop) {
        switch (prop) {
            case BATT_STATUS:
                return getDeviceStatus().contains(HmsDeviceStatus.BATT_LOW);
            default:
                return super.getBoolean(prop);
        }
    }

    @Override
    public String getString(HmsProperty prop) {
        switch (prop) {
            case RAW_VALUE:
                return rawValue;
            default:
                return super.getString(prop);
        }
    }

    /**
     * @return the rawValue
     */
    public String getRawValue() {
        return rawValue;
    }

    /**
     * @param rawValue the rawValue to set
     */
    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

}
