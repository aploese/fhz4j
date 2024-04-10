/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2017-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.writer.cul.CulWriter;
import de.ibapl.fhz4j.cul.SlowRfFlag;
import java.nio.channels.WritableByteChannel;
import java.util.EnumSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class CulWriterTest {

    public CulWriterTest() {
    }

    /**
     * Test of initFhz method, of class FhzWriter.
     */
    @Test
    @Disabled
    public void testInitFhz_short() throws Exception {
        System.out.println("initFhz");
        short fhzHousecode = 0;
        WritableByteChannel wbc = null;
        CulWriter instance = new CulWriter(wbc, 64);

        instance.initFhz(fhzHousecode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
        instance.close();
    }

    /**
     * Test of initFhz method, of class FhzWriter.
     */
    @Test
    @Disabled
    public void testInitFhz_short_byte() throws Exception {
        System.out.println("initFhz");
        short fhz100Housecode = 0;
        Set<SlowRfFlag> initFlags = EnumSet.noneOf(SlowRfFlag.class);
        WritableByteChannel wbc = null;
        CulWriter instance = new CulWriter(wbc, 64);

        instance.initFhz(fhz100Housecode, initFlags);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
        instance.close();
    }

    /**
     * Test of syncFhtClocks method, of class FhzWriter.
     */
    @Test
    @Disabled
    public void testSyncFhtClocks() throws Exception {
        System.out.println("syncFhtClocks");
        Iterable<Short> fhtDeviceHomeCodes = null;

        WritableByteChannel wbc = null;
        CulWriter instance = new CulWriter(wbc, 64);
        // instance.syncFhtClocks(fhtDeviceHomeCodes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
        instance.close();
    }

    /**
     * Test of initFhtReporting method, of class FhzWriter.
     */
    @Test
    @Disabled
    public void testInitFhtReporting_ShortArr() throws Exception {
        System.out.println("initFhtReporting");
        WritableByteChannel wbc = null;
        CulWriter instance = new CulWriter(wbc, 64);

//TODO		instance.initFhtReporting((short) 302);
//TODO apl	 assertEquals("T030265FF66FF\n", os.toString());
//TODO		instance.close();
    }

    @Test
    @Disabled //TODO apl
    public void testwriteDateAndTime() throws Exception {
        System.out.println("testwriteDateAndTime");
        WritableByteChannel wbc = null;
        CulWriter instance = new CulWriter(wbc, 64);

//TODO		FhtEnhcoder.writeFhtTimeAndDate((short) 302, LocalDateTime.of(2017, Month.JULY, 20, 14, 23));
//TODO apl	 assertEquals("T0302601161076214630E6417\n", os.toString());
        instance.close();
    }

    /**
     * Test of writeFhtMsg method, of class FhzWriter.
     */
    @Test
    @Disabled //TODO apl
    public void testWriteMonCycle() throws Exception {
        System.out.println("testWriteMonCycle");
        /*		WritableByteChannel wbc = null;
		CulWriter instance = new CulWriter(wbc, 64);

		instance.writeFhtCycle((short) 302, DayOfWeek.MONDAY, LocalTime.of(5, 0), LocalTime.of(8, 30),
				LocalTime.of(17, 20), LocalTime.of(22, 50));
//TODO apl	 assertEquals("T0302141E153316681789\n", os.toString());
//TODO apl		os.reset();

		instance.writeFhtCycle((short) 302, DayOfWeek.MONDAY, LocalTime.of(5, 0), LocalTime.of(8, 30), null, null);
	 assertEquals("T0302141E153316901790\n", os.toString());
		os.reset();

		instance.close();
         */
    }

    @Test
    @Disabled
    public void testwriteFhtThursdayTimes() throws Exception {
        System.out.println("testwriteFhtThursdayTimes");
        WritableByteChannel wbc = null;
        CulWriter instance = new CulWriter(wbc, 64);
        /*
		instance.writeFht((short) 302, FhtProperty.THU_FROM_1, LocalTime.of(7, 0));
	 assertEquals("T0302202A\n", os.toString());
		os.reset();

		instance.writeFht((short) 302, FhtProperty.THU_TO_1, LocalTime.of(21, 0));
	 assertEquals("T0302217E\n", os.toString());
		os.reset();

		instance.writeFht((short) 302, FhtProperty.THU_FROM_2, null);
	 assertEquals("T03022290\n", os.toString());
		os.reset();

		instance.writeFht((short) 302, FhtProperty.THU_TO_2, null);
	 assertEquals("T03022390\n", os.toString());

         */
        instance.close();
    }

    @Test
    @Disabled //TODO apl
    public void testwriteFhtModes() throws Exception {
        System.out.println("testwriteFhtModes");
        WritableByteChannel wbc = null;
        CulWriter instance = new CulWriter(wbc, 64);

        /*		instance.writeFhtModeAuto((short) 302);
	 assertEquals("T03023E00\n", os.toString());
		os.reset();

		instance.writeFhtModeManu((short) 302);
	 assertEquals("T03023E01\n", os.toString());
		os.reset();

		instance.writeFhtModeParty((short) 302, 17.0f, LocalDateTime.of(2017, Month.JULY, 19, 23, 30));
	 assertEquals("T030241223F8D40133E03\n", os.toString());
		os.reset();

		instance.writeFhtModeHoliday((short) 302, 17.0f, LocalDate.of(2017, Month.JULY, 19));
	 assertEquals("T030241223F1340073E02\n", os.toString());

         */
        instance.close();
    }

}
