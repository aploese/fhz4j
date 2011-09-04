/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j;

/**
 *
 * @author aploese
 */
public abstract class Parser {
    private int value;
    private int stackpos;
    
    protected void setStackSize(int size) {
        value = 0;
        stackpos = size;
    }
    
    protected int getStackpos() {
        return stackpos;
    }

    protected  int digit2Int(int i) {
        switch ((char) i) {
            case '0':
                return 0x00;
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return 0x03;
            case '4':
                return 0x04;
            case '5':
                return 0x05;
            case '6':
                return 0x06;
            case '7':
                return 0x07;
            case '8':
                return 0x08;
            case '9':
                return 0x09;
            case 'A':
                return 0x0a;
            case 'B':
                return 0x0b;
            case 'C':
                return 0x0c;
            case 'D':
                return 0x0d;
            case 'E':
                return 0x0e;
            case 'F':
                return 0x0f;
            default:
                throw new RuntimeException("Not a Number: " + (char) i);
        }
    }

    protected void push(int b) {
        value += b << (stackpos-- - 1) * 4;
    }

    protected void pushBCD(int b) {
        value *= 10;
        value += b;
        stackpos--;
    }

    protected  short getShortValue() {
        return (short) (value & 0x0000FFFF);
    }

    protected  byte getByteValue() {
        return (byte) (value & 0x000000FF);
    }

    protected int getIntValue() {
        return value;
    }


    public abstract void parse(int b);
    
    public abstract void init();
    
}
