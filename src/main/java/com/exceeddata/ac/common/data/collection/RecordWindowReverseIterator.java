package com.exceeddata.ac.common.data.collection;

import java.io.Serializable;
import java.util.Iterator;

import com.exceeddata.ac.common.data.record.Record;

public final class RecordWindowReverseIterator implements Iterator<Record>, Serializable{
    private static final long serialVersionUID = 1L;

    private RecordWindow window = null;
    private int current = -1;
    private int end = -1; //exclusive
    
    public RecordWindowReverseIterator(final RecordWindow window) {
        this.window = window;
        this.current = window.size() - 1;
    }
    
    public RecordWindowReverseIterator(final RecordWindow window, final int begin, final int end) {
        this.window = window;
        this.current = begin;
        this.end = end;
    }
    
    @Override
    public boolean hasNext() {
        return current > end;
    }

    @Override
    public Record next() {
        return window.get(current--);
    }
}
