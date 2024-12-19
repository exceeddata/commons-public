package com.exceeddata.ac.common.compare;

import java.io.Serializable;
import java.util.Comparator;

import com.exceeddata.ac.common.data.record.Ident;

/**
 * Comparator for ident by an element position.
 *
 */
public final class IdentElementComparator implements Comparator<Ident>, Serializable { 
    private static final long serialVersionUID = 1L;
    
    private int position1 = 0;
    private int position2 = 0;
    private ComparatorEnum order = ComparatorEnum.ASCENDING;
    
    protected IdentElementComparator() {}
    
    protected IdentElementComparator(final int position1, final int position2) {
        this.position1 = position1;
        this.position2 = position2;
    }
    
    public IdentElementComparator(final int position, final ComparatorEnum order) {
        this.position1 = position;
        this.position2 = position;
        this.order = order;
    }
    
    public IdentElementComparator(final int position1, final int position2, final ComparatorEnum order) {
        this.position1 = position1;
        this.position2 = position2;
        this.order = order;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(Ident r1, Ident r2) {
        if (r2 == null) {
            return (r1 == null) ? 0 : (order == ComparatorEnum.ASCENDING) ? 1 : -1;
        } else if (r1 == null) {
            return order == ComparatorEnum.ASCENDING ? -1 : 1;
        }  else {
            return order == ComparatorEnum.ASCENDING
                    ? r1.dataAt(position1).compareTo(r2.dataAt(position2))
                    : -r1.dataAt(position1).compareTo(r2.dataAt(position2));
        }
    }
}
