package com.exceeddata.ac.common.data.template;

import java.math.BigDecimal;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class FieldFormulaPolynomial implements FieldFormula {
    private static final long serialVersionUID = 1L;

    private byte type;
    private final BigDecimal p0;
    private final BigDecimal p1;
    private final BigDecimal p2;
    private final BigDecimal p3;
    private final BigDecimal p4;
    private final BigDecimal p5;
    
    public FieldFormulaPolynomial(
            final DescType descType,
            final TypeData p0,
            final TypeData p1,
            final TypeData p2,
            final TypeData p3,
            final TypeData p4,
            final TypeData p5) throws EngineException {
        if (descType != null) {
            this.type = descType.getType();
        }
        this.p0 = p0.toDecimal();
        this.p1 = p1.toDecimal();
        this.p2 = p2.toDecimal();
        this.p3 = p3.toDecimal();
        this.p4 = p4.toDecimal();
        this.p5 = p5.toDecimal();
        
        if (this.p0 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P0_EMPTY");
        }
        if (this.p1 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P1_EMPTY");
        }
        if (this.p2 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P2_EMPTY");
        }
        if (this.p3 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P3_EMPTY");
        }
        if (this.p4 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P4_EMPTY");
        }
        if (this.p5 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P5_EMPTY");
        }
    }
    
    public FieldFormulaPolynomial(
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
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        
        if (this.p0 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P0_EMPTY");
        }
        if (this.p1 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P1_EMPTY");
        }
        if (this.p2 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P2_EMPTY");
        }
        if (this.p3 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P3_EMPTY");
        }
        if (this.p4 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P4_EMPTY");
        }
        if (this.p5 == null) {
            throw new EngineException("FIELD_FORMULA_POLYNOMIAL_P5_EMPTY");
        }
    }
    
    @Override
    public TypeData calculate(final TypeData data) throws EngineException {
        if (data.isEmpty()) {
            return DoubleData.NULL;
        }
        final BigDecimal value = type == Types.ANY ? data.toDecimal() : DataConv.convert(data, type).toDecimal();
        final BigDecimal numer = p1.subtract(p3.multiply(value.subtract(p4).subtract(p5)));
        final BigDecimal denom = p2.multiply(value.subtract(p4).subtract(p5)).subtract(p0);
        return iszero(denom) ? decimalToTypeData(numer.divide(denom)) : DoubleData.NULL;
    }
    
    private static TypeData decimalToTypeData(final BigDecimal val) {
        if (val.signum() == 0 || val.scale() <= 0 || val.stripTrailingZeros().scale() <= 0) {
            return LongData.nonNullValueOf(val.longValue());
        } else {
            return DoubleData.nonNullValueOf(val.doubleValue());
        }
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
