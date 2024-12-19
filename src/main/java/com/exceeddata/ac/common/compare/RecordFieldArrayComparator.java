package com.exceeddata.ac.common.compare;

import java.io.Serializable;
import java.util.Comparator;

import com.exceeddata.ac.common.data.record.Record;

/**
 * Comparator for record by field array.
 *
 */
public final class RecordFieldArrayComparator implements Comparator<Record>, Serializable { 
    private static final long serialVersionUID = 1L;
    
    private final String[] names;
    private final ComparatorEnum[] orders;
    
    public RecordFieldArrayComparator(final String[] names) {
        this.names = names;
        
        this.orders = new ComparatorEnum[names.length];
        for (int i = 0; i < orders.length; ++i) {
            this.orders[i] = ComparatorEnum.ASCENDING;
        }
    }
    
    public RecordFieldArrayComparator(final String[] names, final ComparatorEnum[] orders, final ComparatorEnum defaultOrder) {
        this.names = names;
        if (orders == null) {
            this.orders = new ComparatorEnum[names.length];
            for (int i = 0; i < names.length; ++i) {
                this.orders[i] = defaultOrder;
            }
        } else if (orders.length < names.length) {
            this.orders = new ComparatorEnum[names.length];
            for (int i = 0; i < orders.length; ++i) {
                this.orders[i] = orders[i];
            }
            for (int i = orders.length; i < names.length; ++i) {
                this.orders[i] = defaultOrder;
            }
        } else {
            this.orders = orders;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(Record r1, Record r2) {
        if (r2 == null) {
            return (r1 == null) ? 0 : (orders[0] == ComparatorEnum.ASCENDING) ? 1 : -1;
        } else if (r1 == null) {
            return orders[0] == ComparatorEnum.ASCENDING ? -1 : 1;
        } else {
            int c = 0;
            for (int i = 0; i < orders.length; ++i) {
                if ((c = r1.get(names[i]).compareTo(r2.get(names[i]))) != 0) {
                    return orders[i] == ComparatorEnum.ASCENDING ? c : -c;
                }
            }
        }
        
        return 0;
    }
}
