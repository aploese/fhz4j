/*
 * FHZ4J - Drivers for the Wireless FS20, FHT and HMS protocol https://github.com/aploese/fhz4j/
 * Copyright (C) 2023-2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.fhz4j.protocol.evohome.messages;

import de.ibapl.fhz4j.protocol.evohome.EvoHomeCommand;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeDeviceMessage;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgParam0;
import de.ibapl.fhz4j.protocol.evohome.EvoHomeMsgType;
import java.util.Objects;

/**
 *
 * @author Arne Plöse
 * <a href="https://github.com/zxdavb/ramses_protocol/wiki/0100:-Localisation-(language)">0100:
 * Localisation (language)</a>
 * @param <T>
 */
public class LocalizationRequestMessage<T extends LocalizationRequestMessage<T>> extends EvoHomeDeviceMessage<T> {

    public byte unused0;
    public String language;
    public byte unused1;

    public LocalizationRequestMessage(EvoHomeMsgParam0 msgParam0) {
        super(EvoHomeCommand.LOCALIZATION, EvoHomeMsgType.REQUEST, msgParam0);
    }

    @Override
    protected void addToJsonString(StringBuilder sb) {
        super.addToJsonString(sb);
        sb.append(String.format(", unused0 : 0x%02x", unused0));
        sb.append(", language : \"").append(language).append("\"");
        sb.append(String.format(", unused1 : 0x%02x", unused1));
    }

    @Override
    protected int subClassHashCode(int hash) {
        hash = super.subClassHashCode(hash);
        hash = HASH_MULTIPLIER * hash + this.unused0;
        hash = HASH_MULTIPLIER * hash + Objects.hashCode(this.language);
        return HASH_MULTIPLIER * hash + this.unused1;
    }

    @Override
    protected boolean subClassEquals(T other) {
        if (!super.subClassEquals(other)) {
            return false;
        }
        if (this.unused0 != other.unused0) {
            return false;
        }
        if (this.unused1 != other.unused1) {
            return false;
        }
        return Objects.equals(this.language, other.language);
    }

}
