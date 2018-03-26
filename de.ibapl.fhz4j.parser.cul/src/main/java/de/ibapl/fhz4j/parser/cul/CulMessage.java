/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ibapl.fhz4j.parser.cul;

import de.ibapl.fhz4j.api.FhzMessage;
import de.ibapl.fhz4j.api.FhzProtocol;

/**
 *
 * @author aploese
 */
public class CulMessage extends FhzMessage {
    public final static CulMessage LOVF = new CulMessage("Limit Overflow");
    public final static CulMessage EOB = new CulMessage("End Of Buffer");
    
    public String message;
    
    private CulMessage(String message) {
        super(FhzProtocol.CUL);
        this.message = message;
    }
}
