package com.exceeddata.ac.common.compare;

import java.io.Serializable;
import java.util.Comparator;

import com.exceeddata.ac.common.data.record.Record;

/**
 * Comparator for record by a field.
 *
 */
public final class RecordFieldComparator implements Comparator<Record>, Serializable { 
    private static final long serialVersionUID = 1L;
    
    private String name = null;
    private ComparatorEnum order = ComparatorEnum.ASCENDING;
    
    protected RecordFieldComparator() {}
    
    protected RecordFieldComparator(final String name) {
        this.name = name;
    }
    
    public RecordFieldComparator(String name, final ComparatorEnum order) {
        this.name = name;
        this.order = order;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final Record r1, final Record r2) {
        return order == ComparatorEnum.ASCENDING
                ? r1.get(name).compareTo(r2.get(name))
                : r2.get(name).compareTo(r1.get(name));
    }
}
