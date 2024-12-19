package com.exceeddata.ac.common.data.template;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class FieldFormulaDecode implements FieldFormula {
    private static final long serialVersionUID = 1L;
    
    private byte type = Types.ANY;
    private List<BigDecimal> list = null;
    private List<TypeData> datas = null;
    private int lsize = 0;
    
    public FieldFormulaDecode(
            final DescType descType,
            final List<TypeData> list) throws EngineException {
        if (descType != null) {
            this.type = descType.getType();
        }
        if (list == null || list.size() == 0) {
            throw new EngineException("FIELD_FORMULA_VALUES_LIST_EMPTY");
        }
        
        final int size = list.size();
        this.lsize = size / 2;
        this.list = new ArrayList<>(size);
        this.datas = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            this.list.add(list.get(i).toDecimal());
            this.datas.add(list.get(i));
        }
    }
    
    public FieldFormulaDecode(
            final DescType descType,
            final ArrayList<BigDecimal> list) throws EngineException {
        if (descType != null) {
            this.type = descType.getType();
        }
        if (list == null || list.size() == 0) {
            throw new EngineException("FIELD_FORMULA_DECODE_LIST_EMPTY");
        }
        
        final int size = list.size();
        if (size % 2 != 0) {
            throw new EngineException("FIELD_FORMULA_DECODE_LIST_ODD_SIZE");
        }
        
        this.lsize = size / 2;
        this.list = new ArrayList<>(list);
        this.datas = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            this.datas.add(decimalToTypeData(list.get(i)));
        }
    }
    
    @Override
    public TypeData calculate(TypeData data) throws EngineException {
        if (data.isEmpty()) {
            return DoubleData.NULL;
        }
        
        //value-to-value without interpolation
        final BigDecimal value = (type == data.getType() || type == Types.ANY) ? data.toDecimal() : DataConv.convert(data, type).toDecimal();
        BigDecimal x = list.get(0);
        TypeData y = datas.get(1);
        int compare = x.compareTo(value);
        if (compare >= 0) {
            return y;
        }
        for (int i = 1; i < lsize; ++i) {
            compare = list.get(2 * i).compareTo(value);
            if (compare < 0) {
                x = list.get(2 * i);
                y = datas.get(2 * i + 1);
            } else if (compare == 0) {
                return datas.get(2 * i + 1);
            } else {
                if (list.get(2 * i).subtract(value).compareTo(value.subtract(x)) > 0) {
                    return y;
                } else {
                    return datas.get(2 * i + 1);
                }
            }
        }
        return datas.get(2 * lsize -1); //last ditch
    }
    
    private static TypeData decimalToTypeData(final BigDecimal val) {
        if (val.signum() == 0 || val.scale() <= 0 || val.stripTrailingZeros().scale() <= 0) {
            return LongData.nonNullValueOf(val.longValue());
        } else {
            return DoubleData.nonNullValueOf(val.doubleValue());
        }
    }
}
