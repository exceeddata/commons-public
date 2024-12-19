package com.exceeddata.ac.common.data.collection;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Iterator;

import com.exceeddata.ac.common.data.record.Record;

public final class RecordViewQueue implements Serializable{
    private static final long serialVersionUID = 1L;

    private ArrayDeque<Record> queue = null;
    private Iterator<Record> iter = null;
    
    public RecordViewQueue() {
        this.queue = new ArrayDeque<>();
    }
    
    /**
     * Must call updateDataIterator() before using queues.
     * 
     * @param iter the underlying iterator
     */
    public void updateDataIterator(final Iterator<Record> iter) {
        this.iter = iter;
    }
    
    public void addFirst(final Record record) {
        queue.addFirst(record);
    }
    
    public void addLast(final Record record) {
        queue.addLast(record);
    }
    
    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }
    
    public boolean hasNextIterator() {
        return iter != null && iter.hasNext();
    }
    
    public void clear() {
        queue.clear();
        iter = null;
    }

    public Record poll() {
        return queue.isEmpty() ? iter.next() : queue.poll();
    }

    public Record pollQueue() {
        return queue.poll();
    }
}
