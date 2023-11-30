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
package de.ibapl.fhz4j.cul;

import de.ibapl.fhz4j.api.Protocol;
import de.ibapl.fhz4j.protocol.em.EmMessageListener;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMessageListener;
import de.ibapl.fhz4j.protocol.fht.FhtMessageListener;
import de.ibapl.fhz4j.protocol.fs20.FS20MessageListener;
import de.ibapl.fhz4j.protocol.hms.HmsMessageListener;
import de.ibapl.fhz4j.protocol.lacrosse.tx2.LaCrosseTx2MessageListener;
import java.io.IOException;

/**
 *
 * @author Arne Plöse
 */
public interface CulMessageListener extends EmMessageListener, EvoHomeMessageListener, FhtMessageListener, FS20MessageListener, HmsMessageListener, LaCrosseTx2MessageListener {

    void culMessageParsed(CulMessage culMessage);

    /**
     * The signal strength of the received signal.
     *
     * This will always be called before the received message itself.
     *
     * @param signalStrength
     */
    void signalStrength(float signalStrength);

    void failed(Throwable t);

    /**
     * The receivement of a special RF protocol was enabled
     *
     * @param protocol
     */
    void receiveEnabled(Protocol protocol);

    /**
     * the string of help chars as they arrive
     *
     * @param helpMessages the help message from the CUL command line.
     */
    void helpParsed(String helpMessages);

    void onIOException(IOException ioe);

}
