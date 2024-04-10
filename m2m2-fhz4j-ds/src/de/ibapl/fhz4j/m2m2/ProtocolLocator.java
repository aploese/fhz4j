/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2016-2024, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ibapl.fhz4j.m2m2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.sf.fhz4j.FhzProtocol;
import net.sf.fhz4j.scada.ScadaProperty;

/**
 *
 * @author aploese
 */
public class ProtocolLocator<T extends ScadaProperty> implements Serializable {

    private final static Logger LOG = Logger.getLogger("DS_FHZ4J");
    private T property;
    private boolean settable = false;

    /**
     * @return the property
     */
    public T getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(T property) {
        this.property = property;
    }

    public FhzProtocol getFhzProtocol() {
        return FhzProtocol.UNKNOWN;
    }
    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int serialVersion = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(serialVersion);
        out.writeBoolean(settable);
        out.writeObject(property);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        switch (ver) {
            case 1:
                settable = in.readBoolean();
                property = (T) in.readObject();
                break;
            default:
                throw new RuntimeException("Cant handle version");
        }
    }

    /**
     * @return the settable
     */
    public boolean isSettable() {
        return settable;
    }

    /**
     * @param settable the settable to set
     */
    public void setSettable(boolean settable) {
        this.settable = settable;
    }

}
