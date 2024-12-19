package com.exceeddata.ac.common.util.mutable;

import java.io.Serializable;

/**
 * A mutable int utility class. int value is non-null.
 *
 */
public class MutableInt implements Serializable {
    private static final long serialVersionUID = 1L;
    private int val;
    
    /**
     * Construct a mutable int with default value of zero.
     */
    public MutableInt() {
        val = 0;
    }
    
    /**
     * Construct a mutable int with an initial value.
     * 
     * @param initial value
     */
    public MutableInt(final int initial) {
        val = initial;
    }
    
    /**
     * Increment the value by 1.
     */
    public void increment() {
        ++val;
    }
    
    /**
     * Add the value by x. if x is null then value is unchanged.
     * 
     * @param x the value to add
     */
    public void add(final Integer x) {
        if (x != null) {
            val += x.intValue();
        }
    }
    
    /**
     * Set the value with x.  If x is null then value is unchanged.
     * 
     * @param x the value to set
     */
    public void set(final Integer x) {
        if (x != null) {
            val = x.intValue();
        }
    }
    
    /**
     * Get the underlying value.
     * 
     * @return int
     */
    public int getValue() {
        return val;
    }
    
    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
