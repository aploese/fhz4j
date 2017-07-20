package de.ibapl.fhz4j.parser.cul;

/**
 *
 * @author aploese
 */
public class WrongCharException extends RuntimeException {
    
    public WrongCharException(String parsePosition, char c) {
        super(String.format("Collect %s - Wrong char: 0x%02x %s", parsePosition, (byte)c, c));
    }
}
