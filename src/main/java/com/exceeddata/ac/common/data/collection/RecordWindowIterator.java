package com.exceeddata.ac.common.data.collection;

import java.io.Serializable;
import java.util.Iterator;

import com.exceeddata.ac.common.data.record.Record;

public final class RecordWindowIterator implements Iterator<Record>, Serializable{
    private static final long serialVersionUID = 1L;

    private RecordWindow window = null;
    private int current = 0;
    private int end = 0; //exclusive
    
    public RecordWindowIterator(final RecordWindow window) {
        this.window = window;
        this.end = window.size();
    }
    
    public RecordWindowIterator(final RecordWindow window, final int begin, final int end) {
        this.window = window;
        this.current = begin;
        this.end = end;
    }
    
    @Override
    public boolean hasNext() {
        return current < end;
    }

    @Override
    public Record next() {
        return window.get(current++);
    }
}
