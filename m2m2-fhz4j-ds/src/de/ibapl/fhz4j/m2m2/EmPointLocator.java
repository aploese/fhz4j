/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ibapl.fhz4j.m2m2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Logger;
import net.sf.fhz4j.FhzProtocol;
import net.sf.fhz4j.em.EmProperty;

/**
 *
 * @author aploese
 */
public class EmPointLocator extends ProtocolLocator<EmProperty> {

    private final static Logger LOG = Logger.getLogger("DS_FHZ4J");
    private short address;

    public String defaultName() {
        return getProperty() == null ? "EM unknown" : getProperty().getLabel();
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
        out.writeShort(address);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        switch (ver) {
            case 1:
                address = in.readShort();
                break;
            default:
                throw new RuntimeException("Cant handle version");
        }
    }

    @Override
    public FhzProtocol getFhzProtocol() {
        return FhzProtocol.EM;
    }

    /**
     * @return the address
     */
    public short getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(short address) {
        this.address = address;
    }

}
