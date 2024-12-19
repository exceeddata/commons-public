package com.exceeddata.ac.common.util;

import java.math.BigDecimal;

import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DecimalData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

/**
 * A class for simple math utilities.
 * 
 *
 */
public final class XMathUtils {
    private XMathUtils() {}
    
    /**
     * Return a double if the value is non-null or else 0.
     * 
     * @param val the value
     * @return double
     */
    public static double getDouble(final Double val) {
        return val != null ? val.doubleValue() : 0d;
    }
    
    /**
     * Return a double if the value is non-null or else a provided default value.
     * 
     * @param val the value
     * @param defaultValue the default value
     * @return double
     */
    public static double getDouble(final Double val, final double defaultValue) {
        return val != null ? val.doubleValue() : defaultValue;
    }
    
    /**
     * Return a double if the value is non-empty or else a provided default value.
     * 
     * @param val the value
     * @param defaultValue the default value
     * @return double
     * @throws EngineException if occurs
     */
    public static double getDouble(final TypeData val) throws EngineException {
        return val.isEmpty() ? 0d : val.toDouble().doubleValue();
    }
    
    /**
     * Return a DoubleData which contains the double value if it is non-null or else 0.
     * 
     * @param val the value
     * @return DoubleData
     */
    public static DoubleData getDoubleData(final Double val) {
        return val != null ? DoubleData.nonNullValueOf(val.doubleValue()) : DoubleData.ZERO;
    }
    
    /**
     * Return a DoubleData which contains the double value if it is non-null or else a provided default value.
     * 
     * @param val the value
     * @param defaultValue the default value
     * @return DoubleData
     */
    public static DoubleData getDoubleData(final Double val, final double defaultValue) {
        return DoubleData.nonNullValueOf(val != null ? val.doubleValue() : defaultValue);
    }
    
    /**
     * Return a DoubleData which contains the double value if it is non-empty or else 0.
     * 
     * @param val the value
     * @return DoubleData
     * @throws EngineException if occurs
     */
    public static DoubleData getDoubleData(final TypeData val) throws EngineException {
        return val.isEmpty() ? DoubleData.ZERO : DataConv.toDoubleData(val);
    }
    
    /**
     * Return a DoubleData if the value is non-empty or else a provided default value.
     * 
     * @param val the value
     * @param defaultValue the default value
     * @return DoubleData
     * @throws EngineException if occurs
     */
    public static DoubleData getDoubleData(final TypeData val, final double defaultValue) throws EngineException {
        return val.isEmpty() ? DoubleData.nonNullValueOf(defaultValue) : DataConv.toDoubleData(val);
    }
    
    /**
     * Return a BigDecimal if the value is non-null or else 0.
     * 
     * @param val the value
     * @return BigDecimal
     */
    public static BigDecimal getDecimal(final BigDecimal val) {
        return val != null ? val : BigDecimal.ZERO;
    }
    
    /**
     * Return a BigDecimal if the value is non-null or else a provided default value.
     * 
     * @param val the value
     * @param defaultValue the default value
     * @return BigDecimal
     */
    public static BigDecimal getDecimal(final BigDecimal val, final BigDecimal defaultValue) {
        return val != null ? val : defaultValue;
    }
    
    /**
     * Return a BigDecimal which contains the value if it is non-empty or else 0.
     * 
     * @param val the value
     * @return BigDecimal
     * @throws EngineException if occurs
     */
    public static BigDecimal getDecimal(final TypeData val) throws EngineException {
        return val.isEmpty() ? BigDecimal.ZERO : val.toDecimal();
    }
    
    /**
     * Return a BigDecimal which contains the value if it is non-empty or else a provided default value.
     * 
     * @param val the value
     * @param defaultValue the default value
     * @return BigDecimal
     * @throws EngineException if occurs
     */
    public static BigDecimal getDecimal(final TypeData val, final BigDecimal defaultValue) throws EngineException {
        return val.isEmpty() ? defaultValue : val.toDecimal();
    }
    
    /**
     * Return DecimalData which contains the value if it is non-null or else 0.
     * 
     * @param val the value
     * @return DecimalData
     */
    public static DecimalData getDecimalData(final BigDecimal val) {
        return val != null ? new DecimalData(val) :DecimalData.ZERO;
    }
    
    /**
     * Return DecimalData which contains the value if it is non-null or else a provided default value.
     * 
     * @param val the value
     * @param defaultValue the default value
     * @return DecimalData
     */
    public static DecimalData getDecimalData(final BigDecimal val, final BigDecimal defaultValue) {
        return new DecimalData(val != null ? val : defaultValue);
    }
    
    /**
     * Return a DecimalData which contains the value if it is non-empty or else 0.
     * 
     * @param val the value
     * @return DecimalData
     * @throws EngineException if occurs
     */
    public static DecimalData getDecimalData(final TypeData val) throws EngineException {
        return val.isEmpty() ? DecimalData.ZERO : DataConv.toDecimalData(val);
    }
    
    /**
     * Return a DecimalData which contains the value if it is non-empty or else a provided default value.
     * 
     * @param val the value
     * @param defaultValue the default value
     * @return DecimalData
     * @throws EngineException if occurs
     */
    public static DecimalData getDecimalData(final TypeData val, final BigDecimal defaultValue) throws EngineException {
        return val.isEmpty() ? new DecimalData(defaultValue) : DataConv.toDecimalData(val);
    }
}
