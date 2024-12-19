package com.exceeddata.ac.common.util;

import java.util.Iterator;

/**
 * A util class for iterables.
 *
 */
public final class IterableUtils {

    private IterableUtils() {}
    
    /**
     * Get the iterable for an iterator.
     * 
     * @param iterator the iterator
     * @param <E> the iterator generic type
     * @return Iterable
     */
    public static final <E> Iterable<E> iterable(final Iterator<E> iterator) {
        if (iterator == null) {
            throw new NullPointerException();
        }
        return new Iterable<E>() {
            public Iterator<E> iterator() {
                return iterator;
            }
        };
    }
}
