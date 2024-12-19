package com.exceeddata.ac.common.data.collection;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.exceeddata.ac.common.data.record.Record;

/**
 * <code>RecordCollection</code> defines the specification for a queue of records.
 */
public final class RecordCollection implements RecordWindow, Externalizable, Comparable<RecordCollection> {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_CAPACITY = 50;
    
    /* The underlying array of records. */
    private Record[] records = null;
    private int start = 0;
    private int size = 0;
    
    public RecordCollection(){
        records = new Record[DEFAULT_CAPACITY];
    }
    
    public RecordCollection(final List<Record> intialRecords){
        records = intialRecords.toArray(new Record[intialRecords.size()]) ;
        size = records.length;
    }
    
    public RecordCollection (int capacity) {
        records = capacity > 0 ? new Record[capacity] : new Record[DEFAULT_CAPACITY];
    }
    
    public RecordCollection (final RecordCollection collection) {
        if (collection == null) {
            this.records = new Record[DEFAULT_CAPACITY];
        } else {
            this.start = collection.start;
            this.size = collection.size;
            this.records = new Record[collection.records.length];
            System.arraycopy(collection.records, 0, this.records, 0, collection.records.length);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public RecordCollection clone () {
        return new RecordCollection (this);
    }
    
    /**
     * Get a copy of the instrument.
     * 
     * @return RecordCollection
     */
    
    public RecordCollection copy () {
        return new RecordCollection(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<Record> iterator() {
        return new RecordWindowIterator(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<Record> iterator(final int begin, final int end) {
        return new RecordWindowIterator(this, begin, end);
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<Record> reverseIterator(final int begin, final int end) {
        return new RecordWindowReverseIterator(this, begin, end);
    }
    
    /** {@inheritDoc} */
    @Override
    public Record peek() {
        return size != 0 ? records[start] : null;
    }

    /** {@inheritDoc} */
    @Override
    public Record peek(final int n) {
        return get(n);
    }

    /** {@inheritDoc} */
    @Override
    public Record peekLast() {
        return size != 0 ? records[(start + size - 1) % records.length] : null;
    }

    /** {@inheritDoc} */
    @Override
    public Record peekLast(final int n) {
        return get(size - n - 1);
    }

    /**
     * Remove the first record in the instrument.
     * 
     * @return Record
     */
    public Record poll() {
        if (size != 0) {
            final Record record = records[start];
            records[start] = null;
            start = (start + 1) % records.length;
            --size;
            return record;
        } else {
            return null;
        }
    }

    /**
     * Remove the last record in the instrument.
     * 
     * @return Record
     */
    public Record pollLast() {
        if (size != 0) {
            final int index = (start + --size) % records.length;
            final Record record = records[index];
            records[index] = null;
            return record;
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Record get (final int position) {
        return position >= 0 && position < size ? records[(start + position) % records.length] : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public int size () {
        return size;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Ensure capacity
     * 
     * @param capacity the capacity
     */
    public void ensureCapacity(int capacity) {
        if (capacity > records.length) {
            final Record[] newRecords = new Record[capacity];
            if (start + size <= records.length) {
                System.arraycopy(records, start, newRecords, 0, size);
            } else {
                System.arraycopy(records, start, newRecords, 0, records.length - start);
                System.arraycopy(records, 0, newRecords, records.length - start, start + size - records.length);
            }
            records = newRecords;
            start = 0;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void clear() {
        if (size > 0) {
            Arrays.fill(records, null);
            size = 0;
        }
        start = 0;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        if (records == null) {
            return super.hashCode();
        }
        
        switch (size) {
            case 0: return super.hashCode();
            case 1: return get(0).hashCode();
            case 2: 
                return Long.valueOf(
                          29l * get(0).hashCode()
                        + 13l * get(1).hashCode()).intValue();
            case 3: 
                return Long.valueOf(
                          31l * get(0).hashCode()
                        + 29l * get(1).hashCode()
                        + 17l * get(2).hashCode()).intValue();
            default:
                return Long.valueOf(
                          43l * get(0).hashCode()
                        + 19l * get(1).hashCode()
                        + 11l * get(2).hashCode()
                        + 5l  * get(3).hashCode()).intValue();
            
        }
    }
    
    /**
     * Get all records.
     * 
     * @return List of Records
     */
    public List<Record> getAll() {
        if (size == 0) {
            return new ArrayList<Record>(0);
        }
        
        final Record[] newRecords = new Record[size];
        if (start + size <= records.length) {
            System.arraycopy(records, start, newRecords, 0, size);
        } else {
            System.arraycopy(records, start, newRecords, 0, records.length - start);
            System.arraycopy(records, 0, newRecords, records.length - start, start + size - records.length);
        }
        return Arrays.asList(newRecords);
    }
    
    /**
     * Sort a record collection.
     * 
     * @param comparator the comparator
     * @return RecordCollection
     */
    public RecordCollection sort(final Comparator<Record> comparator) {
        if (start + size < records.length) {
            Arrays.sort(records, start, start + size, comparator);
        } else {
            final Record[] tmp = new Record[records.length];
            System.arraycopy(records, start, tmp, 0, records.length - start);
            System.arraycopy(records, 0, tmp, records.length - start, size + start - records.length);
            records = tmp;
            start = 0;
            Arrays.sort(records, 0, size, comparator);
        }
        return this;
    }
    
    /**
     * Add a record.
     * 
     * @param record the record
     * @return RecordCollection
     */
    public RecordCollection add(final Record record) {
        if (size == records.length) {
            ensureCapacity(records.length + Math.max((int) (records.length * 0.15), DEFAULT_CAPACITY));
            records[size++] = record;
        } else {
            records[(start + size++) % records.length] = record;
        }
        return this;
    }
    
    /**
     * Add a record collection
     * 
     * @param collection the collection
     * @return RecordCollection
     */
    public RecordCollection addAll(final RecordCollection collection) {
        if (collection.size == 0) {
            return this;
        }
        
        if (records.length < size + collection.size) {
            ensureCapacity(records.length + Math.max((int) ((size + collection.size) * 0.15), DEFAULT_CAPACITY));
        }
        
        for (int i = 0; i < collection.size; ++i) {
            records[(start + size++) % records.length] = collection.get(i);
        }
        return this;
    }
    
    /**
     * Set a record at the specified position.
     * 
     * @param position the position
     * @param record the input record
     * @return RecordCollection
     */
    public RecordCollection set(final int position, final Record record) {
        records[(start + position) % records.length] = record;
        return this;
    }
    
    /**
     * Remove a record at the specified position.
     * 
     * @param position the position
     * @return Record
     */
    public Record remove (final int position) {
        if (position >= 0 && position < size) {
            if (position == 0) {
                final Record record = records[start];
                records[start] = null;
                start = (start + 1) % records.length;
                --size;
                return record;
            } else if (position == --size) {
                final int index = (start + position) % records.length;
                final Record record = records[index];
                records[index] = null;
                return record;
            }
            
            final int index = start + position;
            if (index < records.length) {
                final Record record = records[index];
                for (int i = position + 1; i <= size; ++i) {
                    records[start + i - 1] = records[start + i];
                }
                records[start + size] = null;
                return record;
            } else {
                final Record record = records[index % records.length];
                for (int i = position + 1; i <= size; ++i) {
                    records[(start + i - 1) % records.length] = records[(start + i) % records.length];
                }
                records[(start + size) % records.length] = null;
                return record;
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(records.length);
        out.writeInt(size);
        if (start + size <= records.length) {
            for (int i = 0; i < size; ++i) {
                records[start + i].writeExternal(out);
            }
        } else {
            for (int i = 0; i < size; ++i) {
                records[(start + i) % records.length].writeExternal(out);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        final int length = in.readInt();
        records = new Record[length];
        
        size = in.readInt();
        start = 0;
        
        for (int i = 0; i < size; ++i) {
            records[i] = Record.readRecord(in);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(RecordCollection collection) {
        if (collection == null) {
            return 1;
        } else {
            final int thisSize = this.size();
            final int otherSize = collection.size();
            
            int result = 0, i = 0;
            for (; result == 0 && i < thisSize && i < otherSize; ++i) {
                result = this.get(i).compareTo(collection.get(i));
            }
            
            return result != 0 ? result : i < thisSize ? 1 : i < otherSize ? -1 : 0;
        }
    }
 
    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RecordCollection)) {
            return false;
        }
        
        return this.compareTo((RecordCollection) obj) == 0;
    }
}
