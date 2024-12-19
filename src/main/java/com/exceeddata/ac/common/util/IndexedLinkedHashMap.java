package com.exceeddata.ac.common.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A util class for indexed linked hashmap.
 */
public final class IndexedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    protected ArrayList<K> indexes = new ArrayList<K>();
    
    private static final long serialVersionUID = 1L;

    public IndexedLinkedHashMap () {
        super();
    }

    /**
     * Construct an <code>IndexedLinkedHashMap</code> from an Map.
     * 
     * @param map the map
     */
    public IndexedLinkedHashMap (final Map<K, V> map) {
        putAll(map);
        for (final K key : map.keySet()) {
            indexes.add(key);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public V put (K key, V val){
        if (!indexes.contains(key)) {
            indexes.add(key);
        }
        return super.put(key,val);
    }

    /** {@inheritDoc} */
    @Override
    public V remove (Object key) {
        indexes.remove(key);
        return super.remove(key);
    }
    
    /** {@inheritDoc} */
    @Override
    public void clear () {
        indexes.clear();
        super.clear();
    }
    
    /**
     * Get value at an index.
     * 
     * @param index the index
     * @return V
     */
    public V getValueAt(final Integer index){
        return (index < 0 || index > indexes.size()) ? null : super.get(indexes.get(index));
    }
    
    /**
     * Get key at an index.
     * 
     * @param index the index
     * @return V
     */
    public K getKeyAt(final Integer index){
        return (index < 0 || index > indexes.size()) ?  null : indexes.get(index);
    }
}
