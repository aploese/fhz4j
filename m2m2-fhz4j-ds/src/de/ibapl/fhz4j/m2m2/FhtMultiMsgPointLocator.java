package de.ibapl.fhz4j.m2m2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Logger;
import net.sf.fhz4j.Fhz1000;
import net.sf.fhz4j.FhzProtocol;
import net.sf.fhz4j.fht.FhtMultiMsgProperty;


/**
 *
 * @author aploese
 */
public class FhtMultiMsgPointLocator extends ProtocolLocator<FhtMultiMsgProperty> {

    private final static Logger LOG = Logger.getLogger("DS_FHZ4J");
    private short housecode;
   
    /**
     * @return the housecode
     */
    public short getHousecode() {
        return housecode;
    }

    public String defaultName() {
        return getProperty() == null ? "FHT Unknown Multi Message " : getProperty().getLabel();
    }
    
    /**
     * @param housecode the housecode to set
     */
    public void setHousecode(short housecode) {
        this.housecode = housecode;
    }

    public void setDeviceHousecodeStr(String deviceHousecode) {
        this.housecode = Fhz1000.parseHouseCode(deviceHousecode);
    }

    public String getDeviceHousecodeStr() {
        return Fhz1000.houseCodeToString(housecode);
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
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        switch (ver) {
            case 1:
                housecode = in.readShort();
         break;
            default:
                throw new RuntimeException("Cant handle version");
        }
    }

    @Override
    public FhzProtocol getFhzProtocol() {
        return FhzProtocol.FHT_MULTI_MSG;
    }

    private String getHousecodeStr() {
        return Fhz1000.houseCodeToString(housecode);
    }
}
