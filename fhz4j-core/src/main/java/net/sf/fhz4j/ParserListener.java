/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j;

/**
 *
 * @author aploese
 */
public interface ParserListener {
    
    void success(FhzMessage fhzMessage);
    
    void fail(Object o);

}
