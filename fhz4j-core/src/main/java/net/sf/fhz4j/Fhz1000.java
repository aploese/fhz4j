/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.fhz4j;

/**
 *
 * @author aploese
 */
public class Fhz1000 {

    public static String houseCodeToString(short housecode) {
        StringBuilder sb = new StringBuilder(4);
        sb.append(String.format("%02d", (housecode >> 8) & 0x00FF));
        sb.append(String.format("%02d", housecode & 0x000FF));
        return sb.toString();
    }

    public static short parseHouseCode(String housecode) {
        if (housecode.length() > 4) {
            throw new IllegalArgumentException(String.format("Houscode %s too long: ", housecode));
        }
        while (housecode.length() < 4) {
            housecode = '0' +housecode;
        }
        short result = (short)( Short.parseShort(housecode.substring(0, 2)) << 8);
        result |= (short)( Short.parseShort(housecode.substring(2, 4)));
        return result;
    }

}
