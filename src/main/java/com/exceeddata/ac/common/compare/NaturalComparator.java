package com.exceeddata.ac.common.compare;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("rawtypes")
public final class NaturalComparator implements Comparator<Comparable>, Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final NaturalComparator INSTANCE = new NaturalComparator();

    @SuppressWarnings("unchecked")
    public static <T> Comparator<T> get() { return (Comparator<T>) INSTANCE; }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(final Comparable left, final Comparable right) {
        if (left == null) throw new NullPointerException();
        if (right == null) throw new NullPointerException();
        return left.compareTo(right);
    }
}
