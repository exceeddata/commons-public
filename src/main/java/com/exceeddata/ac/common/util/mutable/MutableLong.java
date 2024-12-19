package com.exceeddata.ac.common.util.mutable;

import java.io.Serializable;

/**
 * A mutable long utility class. long value is non-null.
 *
 */
public class MutableLong implements Serializable {
    private static final long serialVersionUID = 1L;
    private long val;
    
    /**
     * Construct a mutable long with default value of zero.
     */
    public MutableLong() {
        val = 0l;
    }
    
    /**
     * Construct a mutable long with an initial value.
     * 
     * @param initial value
     */
    public MutableLong(final long initial) {
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
    public void add(final Long x) {
        if (x != null) {
            val += x.longValue();
        }
    }
    
    /**
     * Set the value with x. if x is null then value is unchanged.
     * 
     * @param x the value to add
     */
    public void set(final Long x) {
        if (x != null) {
            val = x.longValue();
        }
    }
    
    /**
     * Get the underlying value.
     * 
     * @return long
     */
    public long getValue() {
        return val;
    }
    
    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
