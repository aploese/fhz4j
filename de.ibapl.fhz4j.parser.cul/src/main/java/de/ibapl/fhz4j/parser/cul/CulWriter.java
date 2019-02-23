/*-
 * #%L
 * FHZ4J Core
 * %%
 * Copyright (C) 2009 - 2018 Arne Plöse
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
package de.ibapl.fhz4j.parser.cul;

import java.io.IOException;
import java.io.OutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.ibapl.fhz4j.LogUtils;
import de.ibapl.fhz4j.protocol.fht.FhtProperty;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 *
 * @author Arne Plöse
 */
public class CulWriter implements AutoCloseable {

	public enum InitFlag {
		PACKAGE_OK(0x01), ALL_REPEATED(0x02), REPORT_ALL(0x04), MONITOR_MODE(0x08), WITH_TIMING(0x10), WITH_RSSI(
				0x20), REPORT_FHT_ALL_MESSAGES(0x40), REPORT_RAW_RSSI(0x80);
		final byte value;

		private InitFlag(int value) {
			this.value = (byte) value;
		}
	}

	private final static Logger LOG = Logger.getLogger(LogUtils.FHZ_PARSER_CUL);
	final static byte ALL_REPORTS = (byte) 0xFF;
	private final WritableByteChannel wbc;
        private final ByteBuffer buffer;
	private boolean open;

	public CulWriter(WritableByteChannel wbc, int bufferSize) {
		this.wbc = wbc;
                this.buffer = ByteBuffer.allocateDirect(bufferSize);
		open = true;
	}

	public void initFhz(short fhzHousecode) throws IOException {
		try {
			initFhz(fhzHousecode, EnumSet.of(InitFlag.PACKAGE_OK, InitFlag.WITH_RSSI));
		} catch (InterruptedException ex) {
			LOG.log(Level.SEVERE, "EX during init", ex);
		}
	}

        void doWrite() throws IOException {
        buffer.flip();
        wbc.write(buffer);
        buffer.clear();
    }

	public void initFhz(short fhz100Housecode, Set<InitFlag> initFlags) throws IOException, InterruptedException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		LOG.info("INIT 1");
		buffer.put("\r\n".getBytes());
		doWrite();
                Thread.sleep(1000);
		LOG.info("INIT 2");
		byte flags = 0;
		for (InitFlag flag : initFlags) {
			flags |= flag.value;
		}
		final String data = String.format("X%02X\r\n", flags);
		buffer.put(data.getBytes());
		doWrite();
		LOG.log(Level.INFO, "Data sent: {0}", new Object[] { data });
		Thread.sleep(1000);
		LOG.info("INIT Housecode");
		setFhzHousecode(fhz100Housecode);
		Thread.sleep(1000);
		LOG.info("INIT End");
	}

	public void initFhtReporting(short housecode) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		startFhtMessage(housecode);
		writeFhtProperty(FhtProperty.REPORT_1, ALL_REPORTS);
		writeFhtProperty(FhtProperty.REPORT_2, ALL_REPORTS);
		finishFhtMessage();
	}

	public void syncFhtClocks(Iterable<Short> fhtDeviceHomeCodes, LocalDateTime ts) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}

		for (short housecode : fhtDeviceHomeCodes) {
			writeFhtTimeAndDate(housecode, ts);
		}

	}

	public void writeFhtTimeAndDate(short housecode, LocalDateTime ts) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		startFhtMessage(housecode);
		writeFhtProperty(FhtProperty.YEAR, (byte) (ts.getYear() - 2000));
		writeFhtProperty(FhtProperty.MONTH, (byte) ts.getMonthValue());
		writeFhtProperty(FhtProperty.DAY_OF_MONTH, (byte) ts.getDayOfMonth());
		writeFhtProperty(FhtProperty.HOUR, (byte) ts.getHour());
		writeFhtProperty(FhtProperty.MINUTE, (byte) ts.getMinute());
		finishFhtMessage();
	}

	public void initFhtReporting(Iterable<Short> housecodes) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		for (Short housecode : housecodes) {
			initFhtReporting(housecode);
		}
	}

	private void setFhzHousecode(short ownHousecode) throws IOException {
		buffer.put((byte)'T');
		buffer.put((byte)'0');
		buffer.put((byte)'1');
		putByte(ownHousecode / 100);
		putByte(ownHousecode % 100);
		buffer.put((byte)'\n');
		doWrite();
	}

	public void writeFhtModeAuto(short housecode) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		startFhtMessage(housecode);
		writeFhtProperty(FhtProperty.MODE, (byte) 0x00);
		finishFhtMessage();
	}

	public void writeFhtModeManu(short housecode) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		startFhtMessage(housecode);
		writeFhtProperty(FhtProperty.MODE, (byte) 0x01);
		finishFhtMessage();
	}

	public void writeFhtModeHoliday(short housecode, float temp, LocalDate date) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		startFhtMessage(housecode);
		writeFhtProperty(FhtProperty.DESIRED_TEMP, (byte) (temp * 2));
		writeFhtProperty(FhtProperty.HOLIDAY_1, (byte) date.getDayOfMonth());
		writeFhtProperty(FhtProperty.HOLIDAY_2, (byte) date.getMonthValue());
		writeFhtProperty(FhtProperty.MODE, (byte) 0x02);
		finishFhtMessage();
	}

	public void writeFhtModeParty(short housecode, float temp, LocalDateTime ts) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		// Check date against ???
		startFhtMessage(housecode);
		writeFhtProperty(FhtProperty.DESIRED_TEMP, (byte) (temp * 2));
		writeFhtProperty(FhtProperty.HOLIDAY_1, (byte) (ts.getHour() * 6 + ts.getMinute() / 10));
		writeFhtProperty(FhtProperty.HOLIDAY_2, (byte) ts.getDayOfMonth());
		writeFhtProperty(FhtProperty.MODE, (byte) 0x03);
		finishFhtMessage();
	}

	public void writeFhtCycle(short housecode, DayOfWeek dayOfWeek, LocalTime from1, LocalTime to1, LocalTime from2,
			LocalTime to2) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		startFhtMessage(housecode);
		FhtProperty from1Property, to1Property, from2Property, to2Property;
		byte from1Value, to1Value, from2Value, to2Value;

		if (from1 == null) {
			if (to1 != null) {
				throw new IllegalArgumentException("Both from1 and to1 must be null or set!");
			} else {
				from1Value = (byte) 0x90; // this is 24:00 or 24 * 6 = 144 the meaning is not set ....
				to1Value = (byte) 0x90;
			}
		} else {
			from1Value = (byte) (from1.getHour() * 6 + from1.getMinute() / 10);
			to1Value = (byte) (to1.getHour() * 6 + to1.getMinute() / 10);
		}

		if (from2 == null) {
			if (to2 != null) {
				throw new IllegalArgumentException("Both from2 and to2 must be null or set!");
			} else {
				from2Value = (byte) 0x90; // this is 24:00 or 24 * 6 = 144 the meaning is not set ....
				to2Value = (byte) 0x90;
			}
		} else {
			from2Value = (byte) (from2.getHour() * 6 + from2.getMinute() / 10);
			to2Value = (byte) (to2.getHour() * 6 + to2.getMinute() / 10);
		}

		switch (dayOfWeek) {
		case MONDAY:
			from1Property = FhtProperty.MON_FROM_1;
			to1Property = FhtProperty.MON_TO_1;
			from2Property = FhtProperty.MON_FROM_2;
			to2Property = FhtProperty.MON_TO_2;
			break;
		case TUESDAY:
			from1Property = FhtProperty.TUE_FROM_1;
			to1Property = FhtProperty.TUE_TO_1;
			from2Property = FhtProperty.TUE_FROM_2;
			to2Property = FhtProperty.TUE_TO_2;
			break;
		case WEDNESDAY:
			from1Property = FhtProperty.WED_FROM_1;
			to1Property = FhtProperty.WED_TO_1;
			from2Property = FhtProperty.WED_FROM_2;
			to2Property = FhtProperty.WED_TO_2;
			break;
		case THURSDAY:
			from1Property = FhtProperty.THU_FROM_1;
			to1Property = FhtProperty.THU_TO_1;
			from2Property = FhtProperty.THU_FROM_2;
			to2Property = FhtProperty.THU_TO_2;
			break;
		case FRIDAY:
			from1Property = FhtProperty.FRI_FROM_1;
			to1Property = FhtProperty.FRI_TO_1;
			from2Property = FhtProperty.FRI_FROM_2;
			to2Property = FhtProperty.FRI_TO_2;
			break;
		case SATURDAY:
			from1Property = FhtProperty.SAT_FROM_1;
			to1Property = FhtProperty.SAT_TO_1;
			from2Property = FhtProperty.SAT_FROM_2;
			to2Property = FhtProperty.SAT_TO_2;
			break;
		case SUNDAY:
			from1Property = FhtProperty.SUN_FROM_1;
			to1Property = FhtProperty.SUN_TO_1;
			from2Property = FhtProperty.SUN_FROM_2;
			to2Property = FhtProperty.SUN_TO_2;
			break;
		default:
			throw new IllegalArgumentException("Unknown dayOfweek: " + dayOfWeek);

		}
		writeFhtProperty(from1Property, from1Value);
		writeFhtProperty(to1Property, to1Value);
		writeFhtProperty(from2Property, from2Value);
		writeFhtProperty(to2Property, to2Value);
		finishFhtMessage();
	}

	public void writeFht(short housecode, FhtProperty fhtProperty, float value) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		startFhtMessage(housecode);
		switch (fhtProperty) {
		case DAY_TEMP:
		case NIGHT_TEMP:
		case DESIRED_TEMP:
		case LOW_TEMP_OFFSET:
		case WINDOW_OPEN_TEMP:
		case MANU_TEMP:
			writeFhtProperty(fhtProperty, (byte) (value * 2));
			break;
		default:
			throw new IllegalArgumentException("Wrong fht property for temp: " + fhtProperty);
		}
		finishFhtMessage();
	}

	public void writeFht(short housecode, FhtProperty fhtProperty, LocalTime value) throws IOException {
		if (!open) {
			throw new IllegalStateException("Closed!");
		}
		startFhtMessage(housecode);
		switch (fhtProperty) {
		case MON_FROM_1:
		case MON_TO_1:
		case MON_FROM_2:
		case MON_TO_2:
		case TUE_FROM_1:
		case TUE_TO_1:
		case TUE_FROM_2:
		case TUE_TO_2:
		case WED_FROM_1:
		case WED_TO_1:
		case WED_FROM_2:
		case WED_TO_2:
		case THU_FROM_1:
		case THU_TO_1:
		case THU_FROM_2:
		case THU_TO_2:
		case FRI_FROM_1:
		case FRI_TO_1:
		case FRI_FROM_2:
		case FRI_TO_2:
		case SAT_FROM_1:
		case SAT_TO_1:
		case SAT_FROM_2:
		case SAT_TO_2:
		case SUN_FROM_1:
		case SUN_TO_1:
		case SUN_FROM_2:
		case SUN_TO_2:
			if (value == null) {
				writeFhtProperty(fhtProperty, (byte) 0x90); // this is 24:00 or 24 * 6 = 144 the meaning is not set ....
			} else {
				writeFhtProperty(fhtProperty, (byte) (value.getHour() * 6 + value.getMinute() / 10));
			}
			break;
		default:
			throw new IllegalArgumentException("Wrong fht property for temp: " + fhtProperty);
		}
		finishFhtMessage();
	}

	private void startFhtMessage(short housecode) throws IOException {
		buffer.put((byte)'T');
		putByte(housecode / 100);
		putByte(housecode % 100);
	}

	private void finishFhtMessage() throws IOException {
		buffer.put((byte)'\n');
		doWrite();
	}

	private void putByte(int value) throws IOException {
		putNibble((0xF0 & value) >> 4);
		putNibble(0x0F & value);
	}

	private void writeFhtProperty(FhtProperty fhtProperty, byte value) throws IOException {
		switch (fhtProperty) {
		case VALVE:
			putByte(0x00);
			break;
		case OFFSET_VALVE_1:
			putByte(0x01);
			break;
		case OFFSET_VALVE_2:
			putByte(0x02);
			break;
		case OFFSET_VALVE_3:
			putByte(0x03);
			break;
		case OFFSET_VALVE_4:
			putByte(0x04);
			break;
		case OFFSET_VALVE_5:
			putByte(0x05);
			break;
		case OFFSET_VALVE_6:
			putByte(0x06);
			break;
		case OFFSET_VALVE_7:
			putByte(0x07);
			break;
		case OFFSET_VALVE_8:
			putByte(0x08);
			break;
		case MON_FROM_1:
			putByte(0x14);
			break;
		case MON_TO_1:
			putByte(0x15);
			break;
		case MON_FROM_2:
			putByte(0x16);
			break;
		case MON_TO_2:
			putByte(0x17);
			break;
		case TUE_FROM_1:
			putByte(0x18);
			break;
		case TUE_TO_1:
			putByte(0x19);
			break;
		case TUE_FROM_2:
			putByte(0x1a);
			break;
		case TUE_TO_2:
			putByte(0x1b);
			break;
		case WED_FROM_1:
			putByte(0x1c);
			break;
		case WED_TO_1:
			putByte(0x1d);
			break;
		case WED_FROM_2:
			putByte(0x1e);
			break;
		case WED_TO_2:
			putByte(0x1f);
			break;
		case THU_FROM_1:
			putByte(0x20);
			break;
		case THU_TO_1:
			putByte(0x21);
			break;
		case THU_FROM_2:
			putByte(0x22);
			break;
		case THU_TO_2:
			putByte(0x23);
			break;
		case FRI_FROM_1:
			putByte(0x24);
			break;
		case FRI_TO_1:
			putByte(0x25);
			break;
		case FRI_FROM_2:
			putByte(0x26);
			break;
		case FRI_TO_2:
			putByte(0x27);
			break;
		case SAT_FROM_1:
			putByte(0x28);
			break;
		case SAT_TO_1:
			putByte(0x29);
			break;
		case SAT_FROM_2:
			putByte(0x2a);
			break;
		case SAT_TO_2:
			putByte(0x2b);
			break;
		case SUN_FROM_1:
			putByte(0x2c);
			break;
		case SUN_TO_1:
			putByte(0x2d);
			break;
		case SUN_FROM_2:
			putByte(0x2e);
			break;
		case SUN_TO_2:
			putByte(0x2f);
			break;
		case MODE:
			putByte(0x3e);
			break;
		case HOLIDAY_1:
			putByte(0x3f);
			break;
		case HOLIDAY_2:
			putByte(0x40);
			break;
		case DESIRED_TEMP:
			putByte(0x41);
			break;
		case MEASURED_LOW:
			putByte(0x42);
			break;
		case MEASURED_HIGH:
			putByte(0x43);
			break;
		case WARNINGS:
			putByte(0x44);
			break;
		case MANU_TEMP:
			putByte(0x45);
			break;
		case ACK:
			putByte(0x4b);
			break;
		case CAN_CMIT:
			putByte(0x53);
			break;
		case CAN_RCV:
			putByte(0x54);
			break;
		case YEAR:
			putByte(0x60);
			break;
		case MONTH:
			putByte(0x61);
			break;
		case DAY_OF_MONTH:
			putByte(0x62);
			break;
		case HOUR:
			putByte(0x63);
			break;
		case MINUTE:
			putByte(0x64);
			break;
		case REPORT_1:
			putByte(0x65);
			break;
		case REPORT_2:
			putByte(0x66);
			break;
		case ACK_2:
			putByte(0x69);
			break;
		case START_XMIT:
			putByte(0x7d);
			break;
		case END_XMIT:
			putByte(0x7e);
			break;
		case DAY_TEMP:
			putByte(0x82);
			break;
		case NIGHT_TEMP:
			putByte(0x84);
			break;
		case LOW_TEMP_OFFSET:
			putByte(0x85);
			break;
		case WINDOW_OPEN_TEMP:
			putByte(0x8a);
			break;
		default:
			throw new UnsupportedOperationException("Not supported yet.");
		}
		putByte(value);
	}

	private void putNibble(int value) throws IOException {
		switch (value) {
		case 0x00:
			buffer.put((byte)'0');
			break;
		case 0x01:
			buffer.put((byte)'1');
			break;
		case 0x02:
			buffer.put((byte)'2');
			break;
		case 0x03:
			buffer.put((byte)'3');
			break;
		case 0x04:
			buffer.put((byte)'4');
			break;
		case 0x05:
			buffer.put((byte)'5');
			break;
		case 0x06:
			buffer.put((byte)'6');
			break;
		case 0x07:
			buffer.put((byte)'7');
			break;
		case 0x08:
			buffer.put((byte)'8');
			break;
		case 0x09:
			buffer.put((byte)'9');
			break;
		case 0x0A:
			buffer.put((byte)'A');
			break;
		case 0x0B:
			buffer.put((byte)'B');
			break;
		case 0x0C:
			buffer.put((byte)'C');
			break;
		case 0x0D:
			buffer.put((byte)'D');
			break;
		case 0x0E:
			buffer.put((byte)'E');
			break;
		case 0x0F:
			buffer.put((byte)'F');
			break;
		default:
			throw new IllegalArgumentException("Not a Number: " + value);
		}
	}

	@Override
	public void close() throws Exception {
		open = false;
		wbc.close();
	}

}
