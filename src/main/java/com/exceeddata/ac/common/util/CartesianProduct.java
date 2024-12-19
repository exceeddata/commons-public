package com.exceeddata.ac.common.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Cartesian multiple arrays together.
 * 
 */
public final class CartesianProduct implements Iterable<int[]>, Iterator<int[]>, Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int[] lengths;
    private final int[] indices;
    private boolean hasNext = true;

    public CartesianProduct(int[] lengths) {
        this.lengths = lengths;
        this.indices = new int[lengths.length];
    }
    
    /**
     * Reset indices
     */
    public void reset() {
        for (int i = 0; i < indices.length; ++i) {
            indices[i] = 0;
        }
        hasNext = true;
    }
    
    /**
     * Whether there is next array.
     * 
     */
    public boolean hasNext() {
        return hasNext;
    }

    /**
     * Next int array
     */
    public int[] next() {
        final int[] result = Arrays.copyOf(indices, indices.length);
        for (int i = indices.length - 1; i >= 0; --i) {
            if (indices[i] == lengths[i] - 1) {
                indices[i] = 0;
                if (i == 0) {
                    hasNext = false;
                }
            } else {
                indices[i]++;
                break;
            }
        }
        return result;
    }

    /**
     * Return iterator to cartesian
     */
    public Iterator<int[]> iterator() {
        return this;
    }
}