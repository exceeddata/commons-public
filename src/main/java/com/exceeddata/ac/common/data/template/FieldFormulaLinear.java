package com.exceeddata.ac.common.data.template;

import java.math.BigDecimal;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class FieldFormulaLinear implements FieldFormula {
    private static final long serialVersionUID = 1L;

    private byte type = Types.ANY;
    private BigDecimal adjustment = BigDecimal.ZERO;
    private BigDecimal multiplier = BigDecimal.ONE;
    private boolean noMultiplier = false;
    private boolean noAdjustment = false;
    
    public FieldFormulaLinear(
            final DescType descType,
            final TypeData a,
            final TypeData b) throws EngineException {
        this(descType, a.toDecimal(), b.toDecimal());
    }
    
    public FieldFormulaLinear(
            final DescType descType,
            final BigDecimal a,
            final BigDecimal b) throws EngineException {
        if (descType != null) {
            this.type = descType.getType();
        }
        this.multiplier = a;
        this.adjustment = b;
        
        if (this.multiplier == null) {
            throw new EngineException("FIELD_FORMULA_LINEAR_A_EMPTY");
        }
        if (this.adjustment == null) {
            throw new EngineException("FIELD_FORMULA_LINEAR_B_EMPTY");
        }
    }
    
    @Override
    public TypeData calculate(final TypeData data) throws EngineException {
        if (data.isEmpty()) {
            return DoubleData.NULL;
        }
        
        final BigDecimal value = (type == data.getType() || type == Types.ANY) ? data.toDecimal() : DataConv.convert(data, type).toDecimal();
        if (noMultiplier) {
            return noAdjustment ? DoubleData.nonNullValueOf(value.doubleValue()) : decimalToTypeData(adjustment.add(value));
        } else {
            return decimalToTypeData(noAdjustment ? multiplier.multiply(value) : multiplier.multiply(value).add(adjustment));
        }
    }
    
    private static TypeData decimalToTypeData(final BigDecimal val) {
        if (val.signum() == 0 || val.scale() <= 0 || val.stripTrailingZeros().scale() <= 0) {
            return LongData.nonNullValueOf(val.longValue());
        } else {
            return DoubleData.nonNullValueOf(val.doubleValue());
        }
    }
    
    public BigDecimal a() {
        return multiplier;
    }
    
    public BigDecimal b() {
        return adjustment;
    }
}
