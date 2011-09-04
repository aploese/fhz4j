/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.fhz4j;

import net.sf.fhz4j.fht.FhtMeasuredTempMessage;
import net.sf.fhz4j.fht.FhtMessage;
import net.sf.fhz4j.hms.HmsMessage;

/**
 *
 * @author aploese
 */
public interface FhzDataListener {

    void fhtDataParsed(FhtMessage fhtMessage);

    public void fhtMeasuredTempData(FhtMeasuredTempMessage temp);

    public void hmsDataParsed(HmsMessage hmsMsg);

}
