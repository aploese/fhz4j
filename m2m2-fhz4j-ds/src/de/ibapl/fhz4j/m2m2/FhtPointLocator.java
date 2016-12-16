/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ibapl.fhz4j.m2m2;

import com.serotonin.ShouldNeverHappenException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Logger;
import net.sf.fhz4j.Fhz1000;
import net.sf.fhz4j.FhzProtocol;

import net.sf.fhz4j.fht.FhtDeviceType;
import net.sf.fhz4j.fht.FhtProperty;

/**
 *
 * @author aploese
 */
public class FhtPointLocator extends ProtocolLocator<FhtProperty> {

    private final static Logger LOG = Logger.getLogger("DS_FHZ4J");
    private FhtDeviceType fhtDeviceType;
    private short housecode;
    private String fhtDeviceTypeLabel;
    private String propertyLabel;

    /**
     * @return the housecode
     */
    public short getHousecode() {
        return housecode;
    }

    public String defaultName() {
        return getProperty() == null ? "FHT unknown" : getProperty().getLabel();
    }

    /**
     * @param housecode the housecode to set
     */
    public void setHousecode(short housecode) {
        this.housecode = housecode;
    }

    /**
     * @return the fhtDeviceType
     */
    public FhtDeviceType getFhtDeviceType() {
        return fhtDeviceType;
    }

    /**
     * @param fhtDeviceType the fhtDeviceType to set
     */
    public void setFhtDeviceType(FhtDeviceType fhtDeviceType) {
        this.fhtDeviceType = fhtDeviceType;
    }

    public void setHousecodeStr(String deviceHousecode) {
        this.housecode = Fhz1000.parseHouseCode(deviceHousecode);
    }

    public String getHousecodeStr() {
        return Fhz1000.houseCodeToString(housecode);
    }

    public String getFhtDeviceTypeLabel() {
        return fhtDeviceType.getLabel();
    }

    public void setFhtDeviceTypeLabel(String label) {
        fhtDeviceTypeLabel = label;
        tryFromDwr();
    }

    public String getPropertyLabel() {
        return getProperty().getLabel();
    }

    public void setPropertyLabel(String label) {
        propertyLabel = label;
        tryFromDwr();
    }

    private void tryFromDwr() {
        if ((propertyLabel != null) && (fhtDeviceTypeLabel != null)) {
            fhtDeviceType = FhtDeviceType.fromLabel(fhtDeviceTypeLabel);
            fhtDeviceTypeLabel = null;
            propertyLabel = null;
        }
    }

    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int SERIAL_VERSION = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(SERIAL_VERSION);
        out.writeShort(housecode);
        out.writeObject(fhtDeviceType);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        switch (ver) {
            case 1:
                housecode = in.readShort();
                fhtDeviceType = (FhtDeviceType) in.readObject();
                break;
            default:
                throw new RuntimeException("Cant handle version");
        }
    }

    @Override
    public FhzProtocol getFhzProtocol() {
        return FhzProtocol.FHT;
    }

    @Override
    public boolean isSettable() {
        switch (getProperty()) {
            case ACK:
                return false;
            case ACK_2:
                return false;
            case CAN_CMIT:
                return false;
            case CAN_RCV:
                return false;
            case START_XMIT:
                return false;
            case END_XMIT:
                return false;

            case REPORT_1:
                return true;
            case REPORT_2:
                return true;
            case WARNINGS:
                return false;

            case LOW_TEMP_OFFSET:
                return true;

            case UNKNOWN:
                return false;
            case UNKNOWN_0XFF:
                return false;

            case VALVE:
                return false;
            case VALVE_1:
                return false;
            case VALVE_2:
                return false;
            case VALVE_3:
                return false;
            case VALVE_4:
                return false;
            case VALVE_5:
                return false;
            case VALVE_6:
                return false;
            case VALVE_7:
                return false;
            case VALVE_8:
                return false;

            case HOLIDAY_1:
                return true;
            case HOLIDAY_2:
                return true;

            case MO_FROM_1:
                return true;
            case MO_TO_1:
                return true;
            case MO_FROM_2:
                return true;
            case MO_TO_2:
                return true;
            case TUE_FROM_1:
                return true;
            case TUE_TO_1:
                return true;
            case TUE_FROM_2:
                return true;
            case TUE_TO_2:
                return true;
            case WED_FROM_1:
                return true;
            case WED_TO_1:
                return true;
            case WED_FROM_2:
                return true;
            case WED_TO_2:
                return true;
            case THU_FROM_1:
                return true;
            case THU_TO_1:
                return true;
            case THU_FROM_2:
                return true;
            case THU_TO_2:
                return true;
            case FRI_FROM_1:
                return true;
            case FRI_TO_1:
                return true;
            case FRI_FROM_2:
                return true;
            case FRI_TO_2:
                return true;
            case SAT_FROM_1:
                return true;
            case SAT_TO_1:
                return true;
            case SAT_FROM_2:
                return true;
            case SAT_TO_2:
                return true;
            case SUN_FROM_1:
                return true;
            case SUN_TO_1:
                return true;
            case SUN_FROM_2:
                return true;
            case SUN_TO_2:
                return true;

            case MODE:
                return true;

            case MANU_TEMP:
                return true;
            case DAY_TEMP:
                return true;
            case NIGHT_TEMP:
                return true;
            case DESIRED_TEMP:
                return true;
            case WINDOW_OPEN_TEMP:
                return true;
            case MEASURED_HIGH:
                return false;
            case MEASURED_LOW:
                return false;

                
            case YEAR:
                return true;
            case MONTH:
                return true;
            case DAY:
                return true;
            case HOUR:
                return true;
            case MINUTE:
                return true;
            default:
                throw new ShouldNeverHappenException("Dont know if: " + getPropertyLabel() + " is setable");

        }
    }

}
