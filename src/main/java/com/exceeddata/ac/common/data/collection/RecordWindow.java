package com.exceeddata.ac.common.data.collection;

import java.io.Serializable;
import java.util.Iterator;

import com.exceeddata.ac.common.data.record.Record;

/**
 * <code>RecordWindow</code> defines the specification for a window of records.
 */
public interface RecordWindow extends Serializable {
	/**
     * Copy the window.
     * 
     * @return Record
     */
    public RecordWindow copy();
    
    /**
     * Get the first record in the window.
     * 
     * @return Record
     */
    public Record peek();
    
    /**
     * Get an iterator.
     * 
     * @return iterator of Record
     */
    public Iterator<Record> iterator();
    
    /**
     * Get an iterator.
     * 
     * @param begin inclusive
     * @param end exclusive
     * 
     * @return iterator of records
     */
    public Iterator<Record> iterator(int begin, int end);
    
    /**
     * Get an reverse iterator.
     * 
     * @param begin inclusive
     * @param end exclusive
     * 
     * @return iterator of records
     */
    public Iterator<Record> reverseIterator(int begin, int end);
    
    /**
     * Get the first nth record in the window.
     * 
     * @param n the record at position n to peek
     * @return Record
     */
    public Record peek(int n);
    
    /**
     * Get the last record in the window.
     * 
     * @return Record
     */
    public Record peekLast();
    

    /**
     * Get the last nth record in the window.
     * 
     * @param n the record at position n to peek.
     * 
     * @param n the last n to peek
     * @return Record
     */
    public Record peekLast(int n);
    
    /**
     * Get the record in the window by the position.
     * 
     * @param position the position
     * @return Record
     */
    public Record get(int position);
    
    /**
     * Get the size of the window.
     * 
     * @return int
     */
    public int size();
    
    /**
     * Get whether the window is empty.
     * 
     * @return true or false
     */
    public boolean isEmpty();
    
    /**
     * Clear the window.
     */
    public void clear();
}
