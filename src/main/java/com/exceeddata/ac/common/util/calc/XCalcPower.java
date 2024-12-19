package com.exceeddata.ac.common.util.calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DecimalData;
import com.exceeddata.ac.common.data.typedata.DenseVectorData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.IntData;
import com.exceeddata.ac.common.data.typedata.ListData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.UnsupportedDataOperationException;
import com.exceeddata.ac.common.util.XNumberUtils;

public final class XCalcPower {
    private XCalcPower() {}
    

    
    protected static TypeData power(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        if (d1.isNull()) {
            return nullAsZero ? XCalcGeneral.zero(d2) : DoubleData.NULL;
        } else if (d2.isNull()) {
            return nullAsZero ? IntData.ONE : DoubleData.NULL;
        }
        switch (d1.getType()) {
            case Types.INT:
            case Types.LONG:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.LONG:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP:
                        return LongData.nonNullValueOf(powerLongs(d1, d2));
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.COMPLEX:
                        return DoubleData.nonNullValueOf(powerDoubles(d1, d2));
                    case Types.DECIMAL:
                    case Types.INSTANT:
                        return DecimalData.nonNullValueOf(powerDecimals(d1, d2));
                    case Types.BOOLEAN:
                        return d2.toBoolean() ? d1 : IntData.ONE;
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (XNumberUtils.isNumber(s2)) {
                            return XNumberUtils.isDigits(s2) ? LongData.nonNullValueOf(powerLongs(d1, d2)) : DoubleData.nonNullValueOf(powerDoubles(d1, d2));
                        }
                        throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                    }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                }
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
                switch (d2.getType()) {
                    case Types.INT: 
                    case Types.LONG: 
                    case Types.FLOAT: 
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.COMPLEX:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP:
                        return DoubleData.nonNullValueOf(powerDoubles(d1, d2));
                    case Types.DECIMAL:
                    case Types.INSTANT:
                        return DecimalData.nonNullValueOf(powerDecimals(d1, d2)); 
                    case Types.BOOLEAN:
                        return d2.toBoolean() ? d1 : IntData.ONE;
                    case Types.BINARY:
                    case Types.STRING: 
                        if (XNumberUtils.isNumber(d2.toString())) {
                            return DoubleData.nonNullValueOf(powerDoubles(d1, d2)); 
                        }
                        throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                    default:
                        throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                }
            case Types.DECIMAL:
                switch (d2.getType()) {
                    case Types.INT: 
                    case Types.LONG: 
                    case Types.FLOAT: 
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.COMPLEX:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP:
                    case Types.DECIMAL:
                    case Types.INSTANT:
                        return DecimalData.nonNullValueOf(powerDecimals(d1, d2)); 
                    case Types.BOOLEAN:
                        return d2.toBoolean() ? d1 : IntData.ONE;
                    case Types.BINARY:
                    case Types.STRING: 
                        if (XNumberUtils.isNumber(d2.toString())) {
                            return DoubleData.nonNullValueOf(powerDoubles(d1, d2)); 
                        }
                        throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                    default:
                        throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                }
            case Types.COMPLEX:
                switch (d2.getType()) {
                    case Types.BINARY:
                    case Types.STRING: 
                        if (!XNumberUtils.isDigits(d2.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                        }
                    case Types.INT: 
                    case Types.LONG:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: {
                        final long pd2 = d2.toLong();
                        if (pd2 < 0l) {
                            throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                        } else if (pd2 == 0l) {
                            return IntData.ONE;
                        }
                        TypeData result = d1; 
                        for (long l = 1; l < pd2; ++l) {
                            result = XCalcMultiply.multiply(result, d1, nullAsZero);
                        }
                        return result;
                    }
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.DECIMAL: {
                        final BigDecimal pd2 = d2.toDecimal();
                        if (pd2.signum() < 0) {
                            throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                        } else if (pd2.signum() == 0) {
                            return IntData.ONE;
                        } else if (pd2.scale() <= 0 || pd2.stripTrailingZeros().scale() <= 0) {
                            TypeData result = d1; 
                            for (long l = 1, pwr = pd2.longValue(); l < pwr; ++l) {
                                result = XCalcMultiply.multiply(result, d1, nullAsZero);
                            }
                            return result;
                        } else {
                            throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                        } 
                    }
                    case Types.BOOLEAN:
                        return Boolean.TRUE.equals(d2.toBoolean()) ? d1 : IntData.ONE;
                    default:
                        throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d2));
                }
            case Types.BOOLEAN:
                return XCalcGeneral.isZero(d2) ? IntData.ONE : d1;
            case Types.STRING:
                return (d1.isDigits() && d2.isDigits())
                        ? LongData.nonNullValueOf(BigDecimalPower(d1.toDecimal(), d2.toDecimal(), MathContext.DECIMAL128).longValue())
                        : DecimalData.nonNullValueOf(BigDecimalPower(d1.toDecimal(), d2.toDecimal(), MathContext.DECIMAL128)); 
            case Types.LIST:
                switch (d2.getType()) {
                    case Types.SET:
                        throw new UnsupportedDataOperationException ("DATA_POWER: LIST POWER SET - " + d2.toString());
                    case Types.MAP:
                        throw new UnsupportedDataOperationException ("DATA_POWER: LIST POWER MAP - " + d2.toString());
                    case Types.LIST: 
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: 
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_POWER: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final ListData ld1 = (ListData) d1;
                            final ListData ld2 = DataConv.toListData(d2);
                            final int size = ld1.size();
                            final ArrayList<TypeData> result = new ArrayList<>(size);
                            for (int i = 0; i < size; ++i ) {
                                result.add(power(ld1.get(i), ld2.get(i), nullAsZero));
                            }
                            return new ListData(result);
                        }
                    default: {
                        final ListData ld1 = (ListData) d1;
                        final int size = ld1.size();
                        final ArrayList<TypeData> result = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i ) {
                            result.add(power(ld1.get(i), d2, nullAsZero));
                        }
                        return new ListData(result);
                    }
                }
            case Types.SET: 
                throw new UnsupportedDataOperationException ("DATA_POWER: SET POWER IS INVALID");
            case Types.MAP: 
                throw new UnsupportedDataOperationException ("DATA_POWER: MAP POWER IS INVALID");
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR:
                switch (d2.getType()) {
                    case Types.SET:
                        throw new UnsupportedDataOperationException ("DATA_POWER: VECTOR POWER SET - " + d2.toString());
                    case Types.MAP:
                        throw new UnsupportedDataOperationException ("DATA_POWER: VECTOR POWER MAP - " + d2.toString());
                    case Types.LIST:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_POWER: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final ListData ld2 = (ListData) d2;
                            final int size = vd1.size();
                            final double[] result = new double[size];
                            Double d;
                            for (int i = 0; i < size; ++i ) {
                                d = power(vd1.get(i), ld2.get(i), nullAsZero).toDouble();
                                result[i] = d != null ? d.doubleValue() : 0d;
                            }
                            return new DenseVectorData(result);
                        }
                    case Types.DENSEVECTOR: 
                    case Types.SPARSEVECTOR:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_POWER: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final DenseVectorData vd2 = DataConv.toDenseVectorData(d2);
                            final int size = vd1.size();
                            final double[] result = new double[size];
                            Double d;
                            for (int i = 0; i < size; ++i ) {
                                d = power(vd1.get(i), vd2.get(i), true).toDouble();
                                result[i] = d != null ? d.doubleValue() : 0d;
                            }
                            return new DenseVectorData(result);
                        }
                    default: {
                        final DenseVectorData ld1 = DataConv.toDenseVectorData(d1);
                        final int size = ld1.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i ) {
                            arr.add(power(ld1.get(i), d2, true));
                        }
                        return new ListData(arr);
                    }
                }
            default:
                throw new UnsupportedDataOperationException ("DATA_POWER: " + XCalcException.getNonNumericException(d1));
        }
    }

    protected static TypeData ln(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_LN: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_LN: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> arr = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        arr.add(ln(ld.get(i)));
                    }
                    return new ListData(arr);
                }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData) d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.log(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                    final DenseVectorData vd = DataConv.toDenseVectorData(d);
                    final double[] arr = vd.getDoubles();
                    for (int i = 0, len = arr.length; i < len; ++i) {
                        arr[i] = Math.log(arr[i]);
                    }
                    return new DenseVectorData(arr);
                }
            default:
                return DoubleData.nonNullValueOf(Math.log(d.toDouble()));
        }
    }
    
    protected static TypeData log10(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_LOG10: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_LOG10: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> arr = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        arr.add(log10(ld.get(i)));
                    }
                    return new ListData(arr);
                }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData) d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.log10(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                    final DenseVectorData vd = DataConv.toDenseVectorData(d);
                    final double[] arr = vd.getDoubles();
                    for (int i = 0, len = arr.length; i < len; ++i) {
                        arr[i] = Math.log10(arr[i]);
                    }
                    return new DenseVectorData(arr);
                }
            default:
                return DoubleData.nonNullValueOf(Math.log10(d.toDouble()));
        }
    }
    
    protected static TypeData log(final TypeData b, final TypeData a) throws EngineException {
        if (b.isNull() || a.isNull()) {
            return DoubleData.NULL;
        }
        switch (b.getType()) {
            case Types.LIST:
                switch (a.getType()) {
                    case Types.SET:
                        throw new UnsupportedDataOperationException ("DATA_LOG: LIST LOG SET - " + a.toString());
                    case Types.MAP:
                        throw new UnsupportedDataOperationException ("DATA_LOG: LIST LOG MAP - " + a.toString());
                    case Types.LIST: 
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: 
                        if (b.size() != a.size()) {
                            throw new UnsupportedDataOperationException ("DATA_LOG: SIZE MISMATCH - " + a.toString());
                        } else {
                            final ListData ldb = (ListData) b;
                            final ListData lda = DataConv.toListData(a);
                            final int size = ldb.size();
                            final ArrayList<TypeData> arr = new ArrayList<>(size);
                            for (int i = 0; i < size; ++i ) {
                                arr.add(log(ldb.get(i), lda.get(i)));
                            }
                            return new ListData(arr);
                        }
                    default: {
                        final ListData ldb = (ListData) b;
                        final int size = ldb.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i ) {
                            arr.add(log(ldb.get(i), a));
                        }
                        return new ListData(arr);
                    }
                }
            case Types.SET: 
                throw new UnsupportedDataOperationException ("DATA_LOG: SET LOG IS INVALID");
            case Types.MAP: 
                throw new UnsupportedDataOperationException ("DATA_LOG: MAP LOG IS INVALID");
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR:
                switch (a.getType()) {
                    case Types.SET:
                        throw new UnsupportedDataOperationException ("DATA_LOG: VECTOR LOG SET - " + a.toString());
                    case Types.MAP:
                        throw new UnsupportedDataOperationException ("DATA_LOG: VECTOR LOG MAP - " + a.toString());
                    case Types.LIST:
                        if (b.size() != a.size()) {
                            throw new UnsupportedDataOperationException ("DATA_LOG: SIZE MISMATCH - " + a.toString());
                        } else {
                            final DenseVectorData vdb = DataConv.toDenseVectorData(b);
                            final ListData lda = (ListData) a;
                            final int size = vdb.size();
                            final double[] result = new double[size];
                            Double d;
                            for (int i = 0; i < size; ++i ) {
                                d = log(vdb.get(i), lda.get(i)).toDouble();
                                result[i] = d != null ? d.doubleValue() : 0d;
                            }
                            return new DenseVectorData(result);
                        }
                    case Types.DENSEVECTOR: 
                    case Types.SPARSEVECTOR:
                        if (b.size() != a.size()) {
                            throw new UnsupportedDataOperationException ("DATA_LOG: SIZE MISMATCH - " + a.toString());
                        } else {
                            final DenseVectorData vdb = DataConv.toDenseVectorData(b);
                            final DenseVectorData vda = DataConv.toDenseVectorData(a);
                            final double[] arrb = vdb.getDoubles(), arra = vda.getUnsafeDoubles();
                            for (int i = 0, len = arrb.length; i < len; ++i ) {
                                arrb[i] = Math.log(arra[i]) / Math.log(arrb[i]);
                            }
                            return new DenseVectorData(arrb);
                        }
                    default: {
                        final DenseVectorData ldb = DataConv.toDenseVectorData(b);
                        final int size = ldb.size();
                        final ArrayList<TypeData> result = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i ) {
                            result.add(log(ldb.get(i), a));
                        }
                        return new ListData(result);
                    }
                }
            default:
                return DoubleData.nonNullValueOf(Math.log(a.toDouble()) / Math.log(b.toDouble()));
        }
    }
    
    protected static TypeData sqrt(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_SQRT: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_SQRT: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> arr = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        arr.add(sqrt(ld.get(i)));
                    }
                    return new ListData(arr);
                }
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: {
                    final DenseVectorData vd = DataConv.toDenseVectorData(d);
                    final double[] arr = vd.getDoubles();
                    for (int i = 0, len = arr.length; i < len; ++i) {
                        arr[i] = Double.compare(arr[i], 0d) >= 0 ? Math.sqrt(arr[i]) : 0d;
                    }
                    return new DenseVectorData(arr);
                }
            case Types.BOOLEAN:
                return d.toBoolean() ? DoubleData.ONE : DoubleData.ZERO;
            case Types.INT: 
            case Types.LONG: 
            case Types.DATE: 
            case Types.TIME: 
            case Types.TIMESTAMP: 
            case Types.CALENDAR_TIME: 
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT: {
                final long val = d.toLong();
                if (val > 0l) {
                    return DoubleData.nonNullValueOf(Math.sqrt(val));
                } else if (val == 0l) {
                    return DoubleData.ZERO;
                }
                return DoubleData.NULL;
            }
            case Types.DECIMAL: {
                final BigDecimal val = d.toDecimal();
                final int signum = val.signum();
                if (signum > 0) {
                    final BigDecimal x = new BigDecimal(Math.sqrt(val.doubleValue()));
                    return DecimalData.nonNullValueOf(
                            x.add(new BigDecimal(val.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)))
                             .round(MathContext.DECIMAL128)
                             .stripTrailingZeros()
                            );
                } else if (signum == 0) {
                    return DoubleData.ZERO;
                }
                return DoubleData.NULL;
            }
            default: {
                final double dbl = d.toDouble();
                if(Double.compare(dbl, 0.0d) < 0 ){
                    return DoubleData.NULL;
                }
                return DoubleData.nonNullValueOf(Math.sqrt(dbl));
            }
        }
    }
    

    
    protected static TypeData exp(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_EXP: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_EXP: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> result = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        result.add(exp(ld.get(i)));
                    }
                    return new ListData(result);
                }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData) d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.exp(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                    final DenseVectorData vd = DataConv.toDenseVectorData(d);
                    final double[] arr = vd.getDoubles();
                    for (int i = 0, len = arr.length; i < len; ++i) {
                        arr[i] = Math.exp(arr[i]);
                    }
                    return new DenseVectorData(arr);
                }
            default:
                return DoubleData.nonNullValueOf(Math.exp(d.toDouble()));
        }
    }
    
    protected static TypeData expm1(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_EXPM1: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_EXPM1: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> arr = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        arr.add(expm1(ld.get(i)));
                    }
                    return new ListData(arr);
                }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData) d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.expm1(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                    final DenseVectorData vd = DataConv.toDenseVectorData(d);
                    final double[] arr = vd.getDoubles();
                    for (int i = 0, len = arr.length; i < len; ++i) {
                        arr[i] = Math.expm1(arr[i]);
                    }
                    return new DenseVectorData(arr);
                }
            default:
                return DoubleData.nonNullValueOf(Math.expm1(d.toDouble()));
        }
    }
    private static BigDecimal BigDecimalPower(final BigDecimal v1, final BigDecimal v2, final MathContext mc) {
        final int signOf2 = v2.signum();
        final double dn1 = v1.doubleValue();
        final BigDecimal v2_2 = v2.multiply(new BigDecimal(signOf2)); // n2 is now positive
        final BigDecimal remainderOf2 = v2_2.remainder(BigDecimal.ONE);
        final BigDecimal n2IntPart = v2_2.subtract(remainderOf2);
        final BigDecimal intPow = v1.pow(n2IntPart.intValueExact(), mc);
        final BigDecimal doublePow = new BigDecimal(Math.pow(dn1, remainderOf2.doubleValue()));

        final BigDecimal result = intPow.multiply(doublePow, mc);
        return signOf2 == -1
            ? BigDecimal.ONE.divide(result, mc.getPrecision(), RoundingMode.HALF_UP)
            : result;
    }
    
    private static long powerLongs(final TypeData d1, final TypeData d2) throws EngineException {
        return (long) Math.pow(d1.toDouble().doubleValue(), d2.toDouble().doubleValue());
    }
    
    private static double powerDoubles(final TypeData d1, final TypeData d2) throws EngineException {
        return Math.pow(d1.toDouble().doubleValue(), d2.toDouble().doubleValue());
    }

    private static BigDecimal powerDecimals(final TypeData d1, final TypeData d2) throws EngineException {
        return BigDecimalPower(d1.toDecimal(), d2.toDecimal(), MathContext.DECIMAL128);
    }
}
