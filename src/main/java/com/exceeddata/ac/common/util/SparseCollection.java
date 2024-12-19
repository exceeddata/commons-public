package com.exceeddata.ac.common.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * An data class for storing sparse data in array
 *
 */
public abstract class SparseCollection<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * Get an item at the inex.
     * @param index the index
     * @return T
     */
    public abstract T get(int index);
    
    /**
     * Add an item at the index.
     * 
     * @param index the index
     * @param item the item
     */
    public abstract void add(int index, T item);
    
    /**
     * Get the total size.
     * 
     * @return int
     */
    public abstract int getSize();
    
    /**
     * Set the total size;
     * 
     * @param size
     */
    public abstract void setSize(int size);
    
    /**
     * Get an ordered list of the indices.
     * 
     * @return list
     */
    public abstract List<Integer> indices ();
    
    /**
     * Get an ordered list of the datas.
     * 
     * @return list
     */
    public abstract List<T> datas ();
    
    /**
     * Get an ordered collection of the indices.
     * 
     * @return Collection
     */
    public abstract Collection<Integer> keys();
    
    /**
     * Get an ordered collection of the datas.
     * 
     * @return Collection
     */
    public abstract Collection<T> values();
    
    /**
     * Clear the array.
     */
    public abstract void clear();
}
