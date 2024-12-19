package com.exceeddata.ac.common.data.template;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class FieldFormulaInterpolate implements FieldFormula {
    private static final long serialVersionUID = 1L;
    
    private byte type = Types.ANY;
    private List<BigDecimal> list = null;
    private int lsize = 0;
    
    public FieldFormulaInterpolate(
            final DescType descType,
            final List<TypeData> list) throws EngineException {
        if (descType != null) {
            this.type = descType.getType();
        }
        if (list == null || list.size() == 0) {
            throw new EngineException("FIELD_FORMULA_INTERPOLATE_LIST_EMPTY");
        }
        
        final int size = list.size();
        if (size % 2 != 0) {
            throw new EngineException("FIELD_FORMULA_INTERPOLATE_LIST_ODD_SIZE");
        }
        
        this.lsize = size / 2;
        this.list = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            this.list.add(list.get(i).toDecimal());
        }
    }
    
    public FieldFormulaInterpolate(
            final DescType descType,
            final ArrayList<BigDecimal> list) throws EngineException {
        if (descType != null) {
            this.type = descType.getType();
        }
        if (list == null || list.size() == 0) {
            throw new EngineException("FIELD_FORMULA_VALUES_LIST_EMPTY");
        }
        
        final int size = list.size();
        this.lsize = size / 2;
        this.list = new ArrayList<>(list);
    }
    
    @Override
    public TypeData calculate(TypeData data) throws EngineException {
        if (data.isEmpty()) {
            return DoubleData.NULL;
        }
        
        //value-to-value with interpolation
        final BigDecimal value = (type == data.getType() || type == Types.ANY) ? data.toDecimal() : DataConv.convert(data, type).toDecimal();
        BigDecimal x1, x2, y1, y2, a;
        x1 = list.get(0);
        y1 = list.get(1);
        int compare = x1.compareTo(value);
        if (compare > 0) {
            x2 = list.get(2);
            y2 = list.get(3);
            a = y2.subtract(y1).divide(x2.subtract(x1));
            return DoubleData.nonNullValueOf(y1.add(a.multiply(value.subtract(x1))).doubleValue());
        } else if (compare == 0) {
            return DoubleData.nonNullValueOf(y1.doubleValue());
        }
        for (int i = 1; i < lsize - 1; ++i) {
            x1 = list.get(2 * i);
            y1 = list.get(2 * i + 1);
            compare = x1.compareTo(value);
            if (compare > 0) {
                x2 = list.get(2 * i + 2);
                y2 = list.get(2 * i + 3);
                a = y2.subtract(y1).divide(x2.subtract(x1));
                return DoubleData.nonNullValueOf(y1.add(a.multiply(value.subtract(x1))).doubleValue());
            } else if (compare == 0) {
                return DoubleData.nonNullValueOf(y1.doubleValue());
            }
        }
        x2 = list.get(2 * lsize - 2);
        y2 = list.get(2 * lsize - 1);
        compare = x2.compareTo(value);
        if (compare == 0) {
            return DoubleData.nonNullValueOf(y2.doubleValue());
        } else {
            a = y2.subtract(y1).divide(x2.subtract(x1));
            return DoubleData.nonNullValueOf(y1.add(a.multiply(value.subtract(x1))).doubleValue());
        }
    }
}
