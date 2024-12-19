package com.exceeddata.ac.common.util.calc;

import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public final class XCalculationUtils {
    private XCalculationUtils(){}
    
    public static TypeData abs(final TypeData d) throws EngineException {
        return XCalcGeneral.abs(d);
    }
    
    public static TypeData negate(final TypeData d) throws EngineException {
        return XCalcGeneral.negate(d);
    }
    
    public static TypeData ceil(final TypeData d, final int scale) throws EngineException {
        return XCalcGeneral.ceil(d, scale);
    }
    
    public static TypeData floor(final TypeData d, final int scale) throws EngineException {
        return XCalcGeneral.floor(d, scale);
    }
    
    public static TypeData round(final TypeData d, final int scale) throws EngineException {
        return XCalcGeneral.round(d, scale);
    }
    
    public static TypeData truncate(final TypeData d, final int scale) throws EngineException {
        return XCalcGeneral.truncate(d, scale);
    }
    
    public static TypeData power(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        return XCalcPower.power(d1, d2, nullAsZero);
    }
    
    public static TypeData exp(final TypeData d) throws EngineException {
        return XCalcPower.exp(d);
    }
    
    public static TypeData expm1(final TypeData d) throws EngineException {
        return XCalcPower.expm1(d);
    }
    
    public static TypeData ln(final TypeData d) throws EngineException {
        return XCalcPower.ln(d);
    }
    
    public static TypeData log10(final TypeData d) throws EngineException {
        return XCalcPower.log10(d);
    }
    
    public static TypeData log(final TypeData b, final TypeData a) throws EngineException {
        return XCalcPower.log(b, a);
    }
    
    public static TypeData sqrt(final TypeData d) throws EngineException {
        return XCalcPower.sqrt(d);
    }
    
    public static TypeData sin(final TypeData d) throws EngineException {
        return XCalcTrigonometry.sin(d);
    }
    
    public static TypeData sinh(final TypeData d) throws EngineException {
        return XCalcTrigonometry.sinh(d);
    }
    public static TypeData acos(final TypeData d, final Integer borderScale) throws EngineException {
        return XCalcTrigonometry.acos(d, borderScale);
    }
    
    public static TypeData asin(final TypeData d, final Integer borderScale) throws EngineException {
        return XCalcTrigonometry.asin(d, borderScale);
    }
    
    public static TypeData atan(final TypeData d) throws EngineException {
        return XCalcTrigonometry.atan(d);
    }
    
    public static TypeData atan2(final TypeData y, final TypeData x) throws EngineException {
        return XCalcTrigonometry.atan2(y, x);
    }
    
    public static TypeData cbrt(final TypeData d) throws EngineException {
        return XCalcTrigonometry.cbrt(d);
    }
    
    public static TypeData cos(final TypeData d) throws EngineException {
        return XCalcTrigonometry.cos(d);
    }
    
    public static TypeData cosh(final TypeData d) throws EngineException {
        return XCalcTrigonometry.cosh(d);
    }
    
    public static TypeData tan(final TypeData d) throws EngineException {
        return XCalcTrigonometry.tan(d);
    }
    
    public static TypeData tanh(final TypeData d) throws EngineException {
        return XCalcTrigonometry.tanh(d);
    }
    
    public static TypeData add(final TypeData d1, final TypeData d2, final boolean numeric, final boolean nullAsZero) throws EngineException {
        return XCalcAdd.add(d1, d2, numeric, nullAsZero);
    }
    
    public static TypeData subtract(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        return XCalcSubtract.subtract(d1, d2, nullAsZero);
    }
    
    public static TypeData multiply(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        return XCalcMultiply.multiply(d1, d2, nullAsZero);
    }
    
    public static TypeData divide(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        return XCalcDivide.divide(d1, d2, nullAsZero);
    }
    
    public static TypeData remainder(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        return XCalcDivide.remainder(d1, d2, nullAsZero);
    }
    
    public static TypeData and(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        return XCalcGeneral.and(d1, d2, nullAsZero);
    }
    
    public static TypeData or(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        return XCalcGeneral.or(d1, d2, nullAsZero);
    }
    
    public static TypeData zero(final TypeData d) throws EngineException {
        return XCalcGeneral.zero(d);
    }
    
    public static boolean isNegative(final TypeData d) throws EngineException {
        return XCalcGeneral.isNegative(d);
    }
    
    public static boolean isPositive(final TypeData d) throws EngineException {
        return XCalcGeneral.isPositive(d);
    }
    
    public static TypeData gt(final TypeData d1, final TypeData d2) throws EngineException {
        return XCalcGeneral.gt(d1, d2);
    }
    
    public static TypeData ge(final TypeData d1, final TypeData d2) throws EngineException {
        return XCalcGeneral.ge(d1, d2);
    }
    
    public static TypeData lt(final TypeData d1, final TypeData d2) throws EngineException {
        return XCalcGeneral.lt(d1, d2);
    }
    
    public static TypeData le(final TypeData d1, final TypeData d2) throws EngineException {
        return XCalcGeneral.le(d1, d2);
    }
}
