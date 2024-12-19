package com.exceeddata.ac.common.data.template;

import java.math.BigDecimal;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class FieldFormulaLogarithmic implements FieldFormula {
    private static final long serialVersionUID = 1L;

    private byte type;
    private final BigDecimal p0;
    private final BigDecimal p1;
    private final BigDecimal p2;
    private final BigDecimal p3;
    private final BigDecimal p4;
    private final BigDecimal p5;
    private final BigDecimal p6;
    private boolean p3zero = false;
    private boolean p0zero = false;
    private final double p1d;
    private final double p4d;
    
    public FieldFormulaLogarithmic(
            final DescType descType,
            final TypeData p0,
            final TypeData p1,
            final TypeData p2,
            final TypeData p3,
            final TypeData p4,
            final TypeData p5,
            final TypeData p6) throws EngineException {
        this(descType, p0.toDecimal(), p1.toDecimal(), p2.toDecimal(), p3.toDecimal(), p4.toDecimal(), p5.toDecimal(), p6.toDecimal());
    }
    
    public FieldFormulaLogarithmic(
            final DescType descType,
            final BigDecimal p0,
            final BigDecimal p1,
            final BigDecimal p2,
            final BigDecimal p3,
            final BigDecimal p4,
            final BigDecimal p5,
            final BigDecimal p6) throws EngineException {
        if (descType != null) {
            this.type = descType.getType();
        }
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        this.p6 = p6;
        
        if (this.p0 == null) {
            throw new EngineException("FIELD_FORMULA_EXPONENTIAL_P0_EMPTY");
        }
        if (this.p1 == null) {
            throw new EngineException("FIELD_FORMULA_EXPONENTIAL_P1_EMPTY");
        }
        if (this.p2 == null) {
            throw new EngineException("FIELD_FORMULA_EXPONENTIAL_P2_EMPTY");
        }
        if (this.p3 == null) {
            throw new EngineException("FIELD_FORMULA_EXPONENTIAL_P3_EMPTY");
        }
        if (this.p4 == null) {
            throw new EngineException("FIELD_FORMULA_EXPONENTIAL_P4_EMPTY");
        }
        if (this.p5 == null) {
            throw new EngineException("FIELD_FORMULA_EXPONENTIAL_P5_EMPTY");
        }
        if (this.p6 == null) {
            throw new EngineException("FIELD_FORMULA_EXPONENTIAL_P6_EMPTY");
        }
        this.p3zero = iszero(this.p3);
        this.p0zero = iszero(this.p0);
        
        if (p3zero && p0zero) {
            throw new EngineException("FIELD_FORMULA_EXPONENTIAL_P0_ZERO_WHEN_P3_ZERO");
        }
        if (p0zero) {
            if (p3zero) {
                throw new EngineException("FIELD_FORMULA_EXPONENTIAL_P3_ZERO_WHEN_P0_ZERO");
            }
            if (iszero(this.p4)) {
                throw new EngineException("FIELD_FORMULA_EXPONENTIAL_P4_ZERO_WHEN_P0_ZERO");
            }
        }
        this.p1d = this.p1.doubleValue();
        this.p4d = this.p4.doubleValue();
    }
    
    @Override
    public TypeData calculate(final TypeData data) throws EngineException {
        if (data.isEmpty()) {
            return DoubleData.NULL;
        }
        final BigDecimal value = type == Types.ANY ? data.toDecimal() : DataConv.convert(data, type).toDecimal();
        if (p3zero) {
            return DoubleData.nonNullValueOf(Math.exp(value.subtract(p6).multiply(p5).subtract(p2).divide(p0).doubleValue()) / p1d);
        }
        if (p0zero) {
            final BigDecimal denom = value.subtract(p6);
            if (iszero(denom)) {
                return DoubleData.NULL;
            }
            return DoubleData.nonNullValueOf(Math.exp(p2.divide(denom).subtract(p5).divide(p3).doubleValue()) / p4d);
        }
        return DoubleData.NULL;
    }
    
    private static boolean iszero(final BigDecimal val) {
        return val.signum() == 0 || val.scale() <= 0 || val.stripTrailingZeros().scale() <= 0;
    }
    
    public BigDecimal p0() {
        return p0;
    }
    
    public BigDecimal p1() {
        return p1;
    }
    
    public BigDecimal p2() {
        return p2;
    }
    
    public BigDecimal p3() {
        return p3;
    }
    
    public BigDecimal p4() {
        return p4;
    }
    
    public BigDecimal p5() {
        return p5;
    }
}
