package com.exceeddata.ac.common.data.template;

import java.math.BigDecimal;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class FieldFormulaRational implements FieldFormula {
    private static final long serialVersionUID = 1L;

    private byte type = Types.ANY;
    private BigDecimal p0 = BigDecimal.ZERO;
    private BigDecimal p1 = BigDecimal.ZERO;
    private BigDecimal p2 = BigDecimal.ZERO;
    private BigDecimal p3 = BigDecimal.ZERO;
    private BigDecimal p4 = BigDecimal.ZERO;
    private BigDecimal p5 = BigDecimal.ZERO;
    
    
    public FieldFormulaRational(
            final DescType descType,
            final TypeData p0,
            final TypeData p1,
            final TypeData p2,
            final TypeData p3,
            final TypeData p4,
            final TypeData p5) throws EngineException {
        this(descType, p0.toDecimal(), p1.toDecimal(), p2.toDecimal(), p3.toDecimal(), p4.toDecimal(), p5.toDecimal());
    }
    
    public FieldFormulaRational(
            final DescType descType,
            final BigDecimal p0,
            final BigDecimal p1,
            final BigDecimal p2,
            final BigDecimal p3,
            final BigDecimal p4,
            final BigDecimal p5) throws EngineException {
        if (descType != null) {
            this.type = descType.getType();
        }
        this.type = descType != null ? descType.getType() : Types.ANY;
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        
        if (this.p0 == null) {
            throw new EngineException("FIELD_FORMULA_RATIONAL_P0_EMPTY");
        }
        if (this.p1 == null) {
            throw new EngineException("FIELD_FORMULA_RATIONAL_P1_EMPTY");
        }
        if (this.p2 == null) {
            throw new EngineException("FIELD_FORMULA_RATIONAL_P2_EMPTY");
        }
        if (this.p3 == null) {
            throw new EngineException("FIELD_FORMULA_RATIONAL_P3_EMPTY");
        }
        if (this.p4 == null) {
            throw new EngineException("FIELD_FORMULA_RATIONAL_P4_EMPTY");
        }
        if (this.p5 == null) {
            throw new EngineException("FIELD_FORMULA_RATIONAL_P5_EMPTY");
        }
    }
    
    @Override
    public TypeData calculate(final TypeData data) throws EngineException {
        if (data.isEmpty()) {
            return DoubleData.NULL;
        }
        
        final BigDecimal value = (type == data.getType() || type == Types.ANY) ? data.toDecimal() : DataConv.convert(data, type).toDecimal();
        final BigDecimal numer = value.multiply(value).multiply(p0).add(value.multiply(p1)).add(p2);
        final BigDecimal denom = value.multiply(value).multiply(p3).add(value.multiply(p4)).add(p5);
        return denom.signum() != 0 ? decimalToTypeData(numer.divide(denom)) : DoubleData.ZERO;
    }
    
    private static TypeData decimalToTypeData(final BigDecimal val) {
        if (val.signum() == 0 || val.scale() <= 0 || val.stripTrailingZeros().scale() <= 0) {
            return LongData.nonNullValueOf(val.longValue());
        } else {
            return DoubleData.nonNullValueOf(val.doubleValue());
        }
    }
    
    public BigDecimal n0() {
        return p0;
    }
    
    public BigDecimal n1() {
        return p1;
    }
    
    public BigDecimal n2() {
        return p2;
    }
    
    public BigDecimal d3() {
        return p3;
    }
    
    public BigDecimal d4() {
        return p4;
    }
    
    public BigDecimal d5() {
        return p5;
    }
}
