package de.ibapl.fhz4j.m2m2;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.serotonin.m2m2.web.mvc.rest.v1.csv.CSVColumnGetter;
import com.serotonin.m2m2.web.mvc.rest.v1.csv.CSVColumnSetter;
import com.serotonin.m2m2.web.mvc.rest.v1.csv.CSVEntity;
import com.serotonin.m2m2.web.mvc.rest.v1.model.dataPoint.PointLocatorModel;

@CSVEntity(typeName = Fhz4JPointLocatorModelDefinition.TYPE_NAME)
public class Fhz4JPointLocatorModel extends PointLocatorModel<Fhz4JPointLocatorVO> {

    public Fhz4JPointLocatorModel(Fhz4JPointLocatorVO data) {
        super(data);
    }

    public Fhz4JPointLocatorModel() {
        super(new Fhz4JPointLocatorVO());
    }

    @Override
    public String getTypeName() {
        return Fhz4JPointLocatorModelDefinition.TYPE_NAME;
    }

/**
    @JsonGetter("address")
    @CSVColumnGetter(order = 18, header = "address")
    public byte getAddress() {
        return this.data.getAddress();
    }

    @JsonSetter("address")
    @CSVColumnSetter(order = 18, header = "address")
    public void setAddress(byte address) {
        this.data.setAddress(address);
    }
**/
}
