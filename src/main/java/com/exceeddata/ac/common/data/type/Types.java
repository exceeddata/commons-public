package com.exceeddata.ac.common.data.type;

/**
 * Definition for supported data types.
 *
 */
public final class Types {
    private Types() {}

    public static final byte INT = 1;
    public static final byte LONG = 2;
    public static final byte FLOAT = 3;
    public static final byte DOUBLE = 4;
    public static final byte DECIMAL = 5;
    public static final byte BOOLEAN = 6;
    public static final byte COMPLEX = 7;
    public static final byte NUMERIC = 8;
    
    public static final byte STRING = 21;
    public static final byte BINARY = 22;
    
    public static final byte DATE = 31;
    public static final byte TIME = 32;
    public static final byte TIMESTAMP = 33;
    public static final byte CALENDAR_TIME = 34;
    public static final byte CALENDAR_TIMESTAMP = 35;
    public static final byte INSTANT = 36;

    public static final byte LIST = 70;
    public static final byte SET = 71;
    public static final byte DENSEVECTOR = 72;
    public static final byte SPARSEVECTOR = 73;
    public static final byte MAP = 74;

    public static final byte EXPRESSION = 97;
    public static final byte NULL = 98;
    public static final byte ANY = 99;
}
