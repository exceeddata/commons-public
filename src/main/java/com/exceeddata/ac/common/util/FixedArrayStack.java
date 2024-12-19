package com.exceeddata.ac.common.util;

import java.io.Serializable;

public final class FixedArrayStack<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private T[] items = null;
    private int size = 0;
    private int start = 0;
    
    protected FixedArrayStack() {}
    
    public FixedArrayStack(final T[] items){
        this.items = items;
    }
    
    public T get (final int position) {
        return position >= 0 && position < size ? items[(start + position) % items.length] : null;
    }
    
    public T put(final T item) {
        if (size == items.length) {
            final T first = items[start];
            items[start] = item;
            start = (start + 1) % items.length;
            return first;
        } else {
            items[(start + size++) % items.length] = item;
            return null;
        }
    }
    
    public T poll() {
        if (size > 0) {
            final T item = items[start];
            items[start] = null;
            start = (start + 1) % items.length;
            --size;
            return item;
        } else {
            return null;
        }
    }
    
    public FixedArrayStack<T> clear() {
        start = 0;
        size = 0;
        return this;
    }
    
    public int size() {
        return size;
    }
}
