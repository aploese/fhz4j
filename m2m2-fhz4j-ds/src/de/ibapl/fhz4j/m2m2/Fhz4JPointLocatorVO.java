/*
 *   Mango - Open Source M2M - http://mango.serotoninsoftware.com
 *   Copyright (C) 2010 Arne Pl\u00f6se
 *   @author Arne Pl\u00f6se
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.ibapl.fhz4j.m2m2;

import com.serotonin.m2m2.DataTypes;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.dataSource.PointLocatorRT;
import com.serotonin.m2m2.vo.dataSource.AbstractPointLocatorVO;
import com.serotonin.m2m2.web.mvc.rest.v1.model.dataPoint.PointLocatorModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Logger;
import net.sf.fhz4j.FhzProtocol;

import net.sf.fhz4j.scada.ScadaProperty;

// Container to move data with json and ajax so ony basic datatypes
public class Fhz4JPointLocatorVO<T extends ScadaProperty> extends AbstractPointLocatorVO  {

    private final static Logger LOG = Logger.getLogger("DS_FHZ4J");
    private ProtocolLocator<T> protocolLocator;

    Fhz4JPointLocatorVO() {
        super();
        this.protocolLocator = new ProtocolLocator<>();
    }

    Fhz4JPointLocatorVO(ProtocolLocator<T> protocolLocator) {
        this.protocolLocator = protocolLocator;
    }

    public String defaultName() {
        return getProperty() == null ? "Fhz4J dataPoint" : getProperty().getLabel();
    }

    @Override
    public int getDataTypeId() {
        if (getProperty() == null) {
            return DataTypes.UNKNOWN;
        }
        switch (getProperty().getDataType()) {
            case BOOLEAN:
                return DataTypes.BINARY;
            case BYTE:
                return DataTypes.MULTISTATE;
            case CHAR:
                return DataTypes.ALPHANUMERIC;
            case DOUBLE:
                return DataTypes.NUMERIC;
            case FLOAT:
                return DataTypes.NUMERIC;
            case LONG:
                return DataTypes.MULTISTATE;
            case INT:
                return DataTypes.MULTISTATE;
            case SHORT:
                return DataTypes.MULTISTATE;
            case STRING:
                return DataTypes.ALPHANUMERIC;
            case TIME:
                return DataTypes.ALPHANUMERIC;
            default:
                throw new RuntimeException("Cant find datatype of " + getProperty());
        }

    }

    @Override
    public TranslatableMessage getConfigurationDescription() {
        return new TranslatableMessage("dsEdit.openv4j", "Something", "I dont know");
    }

    @Override
    public boolean isSettable() {
        return protocolLocator.isSettable();
    }

    @Override
    public PointLocatorRT createRuntime() {
        return new Fhz4JPointLocatorRT(this);
    }

    @Override
    public void validate(ProcessResult response) {
        // no op
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
        out.writeObject(protocolLocator);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        switch (ver) {
            case 1:
                protocolLocator = (ProtocolLocator<T>) in.readObject();
                break;
            default:
                throw new RuntimeException("Cant handle version");
        }
    }

    public FhzProtocol getFhzProtocol() {
        return protocolLocator.getFhzProtocol();
    }

    /**
     * @return the property
     */
    public T getProperty() {
        return protocolLocator.getProperty();
    }

    /**
     * @return the protocolLocator
     */
    public ProtocolLocator<T> getProtocolLocator() {
        return protocolLocator;
    }

    @Override
    public PointLocatorModel<?> asModel() {
        return new Fhz4JPointLocatorModel(this);
    }
    
    @Override
    public void addProperties(List<TranslatableMessage> list) {
    }

    @Override
    public void addPropertyChanges(List<TranslatableMessage> list, Object o) {
    }

}
