package com.exceeddata.ac.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An data class for storing sparse data in array
 *
 */
public final class SparseArray<T> extends SparseCollection<T> {
    private static final long serialVersionUID = 1L;
    
    private ArrayList<Integer> indices = null;
    private ArrayList<T> datas = null;
    private int size = 0;
    
    public SparseArray() {
        indices = new ArrayList<Integer>();
        datas = new ArrayList<T>();
    }
    
    /**
     * Get an item at the inex.
     * @param index the index
     * @return T
     */
    public T get(int index) {
        final int length = indices.size();
        for (int i = 0; i < length; ++i) {
            if (indices.get(i) >= index) {
                return indices.get(i) == index ? datas.get(i) : null;
            }
        }
        return null;
    }
    
    /**
     * Add an item at the index.
     * 
     * @param index the index
     * @param item the item
     */
    public void add(int index, T item) {
        indices.add(index);
        datas.add(item);
    }
    
    /**
     * Get the total size.
     * 
     * @return int
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Set the total size;
     * 
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Get an ordered list of the indices.
     * 
     * @return list
     */
    public List<Integer> indices () {
        return indices;
    }
    
    /**
     * Get an ordered list of the datas.
     * 
     * @return list
     */
    public List<T> datas () {
        return datas;
    }
    
    /**
     * Get an ordered collection of the indices.
     * 
     * @return Collection
     */
    public Collection<Integer> keys () {
        return indices;
    }
    
    /**
     * Get an ordered collection of the datas.
     * 
     * @return Collection
     */
    public Collection<T> values() {
        return datas;
    }
    
    /**
     * Clear the array.
     */
    public void clear() {
        indices.clear();
        datas.clear();
        size = 0;
    }
}
