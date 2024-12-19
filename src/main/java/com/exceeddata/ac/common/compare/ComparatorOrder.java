package com.exceeddata.ac.common.compare;

import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.expression.ExpressionParseException;

/**
 * Comparator order.
 *
 */
public final class ComparatorOrder {
    private ComparatorOrder(){}
    
    public static ComparatorEnum parse(String order) throws EngineException {
        if ("asc".equalsIgnoreCase(order) || "ascending".equalsIgnoreCase(order)) {
            return ComparatorEnum.ASCENDING;
        } else if ("desc".equalsIgnoreCase(order) || "descending".equalsIgnoreCase(order)) {
            return ComparatorEnum.DESCENDING;
        } else {
            throw new ExpressionParseException ("Invalid comparator order");
        }
    }
}
