package de.ibapl.fhz4j.protocol.em;

/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2017 Arne Plöse
 * %%
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009, 2017, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
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
 * #L%
 */

/**
 *
 * @author aploese
 */
public class Em1000EmMessage extends EmMessage {

    private float energy;
    private float energyLast5Min;
    private float maxPowerLast5Min;

    @Override
    protected void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(", energy: ").append(energy);
        sb.append(", energyLast5Min: ").append(energyLast5Min);
        sb.append(", maxPowerLast5Min: ").append(maxPowerLast5Min);
    }

    /**
     * @return the energy
     */
    public float getEnergy() {
        return energy;
    }

    /**
     * @param energy the energy to set
     */
    public void setEnergy(float energy) {
        this.energy = energy;
    }

    /**
     * @return the energyLast5Min
     */
    public float getEnergyLast5Min() {
        return energyLast5Min;
    }

    /**
     * @param energyLast5Min the energyLast5Min to set
     */
    public void setEnergyLast5Min(float energyLast5Min) {
        this.energyLast5Min = energyLast5Min;
    }

    /**
     * @return the maxPowerLast5Min
     */
    public float getMaxPowerLast5Min() {
        return maxPowerLast5Min;
    }

    /**
     * @param maxPowerLast5Min the maxPowerLast5Min to set
     */
    public void setMaxPowerLast5Min(float maxPowerLast5Min) {
        this.maxPowerLast5Min = maxPowerLast5Min;
    }

    @Override
    public float getFloat(EmProperty property) {
        switch (property) {
            case ELECTRICAL_ENERGY:
                return energy;
            case ELECTRICAL_ENERGY_LAST_5_MIN:
                return energyLast5Min;
            case ELECTRICAL_POWER_LAST_5_MIN_MAX:
                return maxPowerLast5Min;
            default:
                return super.getFloat(property);
        }
    }

    @Override
    public EmDeviceType getType() {
        return EmDeviceType.EM_1000_EM;
    }

    @Override
    public void setCumulatedValue(int cummulatedValue) {
        energy = 0.001f * cummulatedValue;
    }

    @Override
    public void setLastValue(int lastValue) {
        energyLast5Min = 0.01f * lastValue;
    }

    @Override
    public void setMaxLastValue(int maxLastValue) {
        maxPowerLast5Min = 0.01f * maxLastValue;
    }
}
