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

import com.serotonin.m2m2.rt.dataSource.PointLocatorRT;
import net.sf.fhz4j.FhzProtocol;

public class Fhz4JPointLocatorRT extends PointLocatorRT {

    protected final Fhz4JPointLocatorVO vo;

    public Fhz4JPointLocatorRT(Fhz4JPointLocatorVO vo) {
        super();
        this.vo = vo;
    }

    public FhzProtocol getFhzProtocol() {
        return vo.getFhzProtocol();
    }

    public Fhz4JPointLocatorVO getVo() {
        return vo;
    }

    @Override
    public boolean isSettable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
