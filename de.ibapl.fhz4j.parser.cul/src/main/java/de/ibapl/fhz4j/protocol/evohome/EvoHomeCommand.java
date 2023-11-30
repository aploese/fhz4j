/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2009-2023, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.evohome;

public enum EvoHomeCommand {
    RF_CHECK,
    ZONE_NAME,
    ZONE_MANAGEMENT,
    RELAY_HEAT_DEMAND,
    RELAY_FAILSAVE,
    ZONE_CONFIG,
    ZONE_ACTUATORS,
    T87RF_STARTUP_000E,
    RF_SIGNAL_TEST,
    LOCALIZATION,
    T87RF_STARTUP_042F,
    DEVICE_BATTERY_STATUS,
    DEVICE_INFORMATION,
    BOILER_RELAY_INFORMATION,
    WINDOW_SENSOR,
    SYSTEM_SYNCHRONIZATION,
    RF_BIND,
    ZONE_SETPOINT,
    ZONE_SETPOINT_OVERRIDE,
    CONTROLLER_MODE,
    ZONE_TEMPERATURE,
    UNKNOWN_3120,
    SYSTEM_TIMESTAMP,
    ZONE_HEAT_DEMAND,
    ACTUATOR_SYNC;

}
