/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2022-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.parser.fht;

import de.ibapl.fhz4j.parser.api.ParserListener;
import de.ibapl.fhz4j.parser.cul.DataSource;
import de.ibapl.fhz4j.protocol.fht.AbstractFhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtMessage;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import de.ibapl.fhz4j.protocol.fht.FhtValveMode;
import de.ibapl.fhz4j.protocol.fht.FhtValvePosMessage;
import de.ibapl.fhz4j.protocol.fht.FhtValveSyncMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class FhtValveMessageTest implements ParserListener<AbstractFhtMessage> {

    private FhtParser parser = new FhtParser(this);
    private FhtMessage partialFhtMessage;
    private FhtMessage fhtMessage;

    private void decode(String s) {
        fhtMessage = null;
        partialFhtMessage = null;
        parser.init();
        new DataSource(s).iterate(parser);
    }

    @Test
    public void testSync() {
        decode("0302002CE1");
        assertValveSyncMessage(fhtMessage, 302, 112.5f);
        decode("0302002C8F");
        assertValveSyncMessage(fhtMessage, 302, 71.5f);
        decode("0302002C03");
        assertValveSyncMessage(fhtMessage, 302, 1.5f);

        decode("0302002600");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.POSITION, 0.0f);

    }

    @Test
    public void testJumpOffToOn() {
        decode("0302002200");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.OFF, 0.0f);
        decode("030200A200");
        assertValveMessage(fhtMessage, 302, true, false, FhtValveMode.OFF, 0.0f);

        decode("0302002100");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.ON, 100.0f);
        decode("030200A100");
        assertValveMessage(fhtMessage, 302, true, false, FhtValveMode.ON, 100.0f);

        decode("0302002627");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.POSITION, 15.294118f);
        decode("030200A627");
        assertValveMessage(fhtMessage, 302, true, false, FhtValveMode.POSITION, 15.294118f);
    }

    @Test
    public void testLimeCycle() {
        decode("0302002A00");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.LIME_CYCLE, 0.0f);
        decode("030200AA00");
        assertValveMessage(fhtMessage, 302, true, false, FhtValveMode.LIME_CYCLE, 0.0f);
    }

    @Test
    public void test_0X20() {
        decode("030200B000");
        assertValveMessage(fhtMessage, 302, true, true, FhtValveMode.END_OF_SYNC_SEQUENCE, 0.0f);
    }

    @Test
    public void test_ValveOffset() {
        decode("0302022832");
        assertValveOffsetMessage(fhtMessage, 302, false, false, FhtProperty.OFFSET_VALVE_2, FhtValveMode.OFFSET_ADJUST,
                50.0f);
        decode("03020628B2");
        assertValveOffsetMessage(fhtMessage, 302, false, false, FhtProperty.OFFSET_VALVE_6, FhtValveMode.OFFSET_ADJUST,
                -50.0f);
        decode("0302022F32");
        assertValveOffsetMessage(fhtMessage, 302, false, false, FhtProperty.OFFSET_VALVE_2, FhtValveMode.PAIRING,
                50.0f);
        decode("0302062FB2");
        assertValveOffsetMessage(fhtMessage, 302, false, false, FhtProperty.OFFSET_VALVE_6, FhtValveMode.PAIRING,
                -50.0f);
    }

    @Test
    @Disabled // Was ist das nach dem sync ...?
    public void testVALVE_1() {
        decode("0302012F00");
        assertValveMessage(fhtMessage, 302, false, false, FhtValveMode.OFF, 0.0f);
    }

    @Test
    public void testFhtValePosMessage() {
        decode("1B0100A600");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 2701, true, false, FhtValveMode.POSITION, 0.0f);

        decode("4D5400A600");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 7784, true, false, FhtValveMode.POSITION, 0.0f);
        decode("435E002626");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 6794, false, false, FhtValveMode.POSITION, 14.901961f);
        decode("212700262C");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 3339, false, false, FhtValveMode.POSITION, 17.254902f);
        decode("1B0100A600");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 2701, true, false, FhtValveMode.POSITION, 0.0f);
        decode("4D5400A600");
        FhtValveMessageTest.assertValveMessage(fhtMessage, 7784, true, false, FhtValveMode.POSITION, 0.0f);
    }

    @Test
    public void testHouseCode() {
        decode("635000A620");
        assertValveMessage(fhtMessage, 9980, true, false, FhtValveMode.POSITION, 12.54902f);
    }

    @Override
    public void success(AbstractFhtMessage fhzMessage) {
        this.fhtMessage = (FhtMessage) fhzMessage;
    }

    @Override
    public void successPartial(AbstractFhtMessage fhzMessage) {
        this.partialFhtMessage = (FhtMessage) fhzMessage;
    }

    @Override
    public void fail(Throwable t) {
        throw new RuntimeException(t);
    }

    public static void assertValveMessage(FhtMessage fhtMessage, int housecode, boolean repeated,
            boolean allowLowBatteryBeep, FhtValveMode mode, float position) {
        assertNotNull(fhtMessage);
        final FhtValvePosMessage msg = (FhtValvePosMessage) fhtMessage;
        assertEquals(housecode, msg.housecode, "housecode");
        assertEquals(FhtProperty.VALVE, msg.command, "command");
        assertEquals(allowLowBatteryBeep, msg.allowLowBatteryBeep, "allowLowBatteryBeep");
        assertEquals(repeated, msg.repeated, "repeated");
        assertEquals(mode, msg.mode, "mode");
        assertEquals(position, msg.position, Float.MIN_NORMAL, "pos");
    }

    public static void assertValveSyncMessage(FhtMessage fhtMessage, int housecode, float timeLeft) {
        assertNotNull(fhtMessage);
        final FhtValveSyncMessage msg = (FhtValveSyncMessage) fhtMessage;
        assertEquals((short) housecode, msg.housecode, "housecode");
        assertEquals(FhtProperty.VALVE, msg.command, "command");
        assertEquals(timeLeft, msg.timeLeft, Float.MIN_NORMAL, "timeLeft");
    }

    public static void assertValveOffsetMessage(FhtMessage fhtMessage, int housecode, boolean repeated,
            boolean allowLowBatteryBeep, FhtProperty command, FhtValveMode mode, float position) {
        assertNotNull(fhtMessage);
        final FhtValvePosMessage msg = (FhtValvePosMessage) fhtMessage;
        assertEquals(housecode, msg.housecode, "housecode");
        assertEquals(command, msg.command, "command");
        assertEquals(allowLowBatteryBeep, msg.allowLowBatteryBeep, "allowLowBatteryBeep");
        assertEquals(repeated, msg.repeated, "repeated");
        assertEquals(mode, msg.mode, "mode");
        assertEquals(position, msg.position, Float.MIN_NORMAL, "pos");
    }

    @Override
    public void successPartialAssembled(AbstractFhtMessage fhzMessage) {
        this.fhtMessage = (FhtMessage) fhzMessage;
    }

}
