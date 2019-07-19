package de.ibapl.fhz4j.protocol.evohome;

import java.util.Objects;

public class ZoneTemperature {
	public byte zone;
	public float temperature;
	
	public ZoneTemperature() {
	}

	public ZoneTemperature(byte zone, float temperature) {
		this.zone = zone;
		this.temperature = temperature;
	}

	protected void addToString(StringBuilder sb) {
		//no-op must be overwritten ... in subclasses
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(String.format("zone : 0x%02x", zone ));
		sb.append(", temperature : ").append(temperature);
		addToString(sb);
		sb.append("}");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(temperature, zone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZoneTemperature other = (ZoneTemperature) obj;
		return Float.floatToIntBits(temperature) == Float.floatToIntBits(other.temperature) && zone == other.zone;
	}
	
}
