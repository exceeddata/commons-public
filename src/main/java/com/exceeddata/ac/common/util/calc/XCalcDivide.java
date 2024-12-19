package com.exceeddata.ac.common.util.calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.ComplexData;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DecimalData;
import com.exceeddata.ac.common.data.typedata.DenseVectorData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.IntData;
import com.exceeddata.ac.common.data.typedata.ListData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.SparseVectorData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.UnsupportedDataOperationException;
import com.exceeddata.ac.common.util.XComplexUtils;
import com.exceeddata.ac.common.util.XNumberUtils;
import com.exceeddata.ac.common.util.XTypeDataUtils;

public final class XCalcDivide {
    private XCalcDivide() {}
    
    public static TypeData divide(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        if (d1.isNull()) {
            return nullAsZero ? XCalcGeneral.zero(d2) : DecimalData.NULL;
        } else if (d2.isNull() || XCalcGeneral.isZero(d2)) {
            return DecimalData.NULL;
        }
        switch (d1.getType()) {
            case Types.INT:
            case Types.BOOLEAN:
            case Types.LONG:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP:
                switch (d2.getType()) {
                    case Types.BOOLEAN: return DataConv.toLongData(d1); //only can be one for boolean at this point
                    case Types.INT: 
                        return d2.toInt() == 1 ? DataConv.toLongData(d1) : DoubleData.nonNullValueOf(divideDecimalToWhole(d1, d2).doubleValue());
                    case Types.LONG:
                        return d2.toLong() == 1l ? DataConv.toLongData(d1) : DoubleData.nonNullValueOf(divideDecimalToWhole(d1, d2).doubleValue());
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP:
                        return DoubleData.nonNullValueOf(divideDecimalToWhole(d1, d2).doubleValue());
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: 
                        return DoubleData.nonNullValueOf(divideDecimals(d1, d2).doubleValue());
                    case Types.INSTANT: 
                    case Types.DECIMAL:
                        return DecimalData.nonNullValueOf(divideDecimals(d1, d2));
                    case Types.COMPLEX: {
                        final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                        final double divisor = cd2.getReal() * cd2.getReal() + cd2.getImaginary() * cd2.getImaginary();
                        return ComplexData.nonNullValueOf(
                                (cd1.getReal() * cd2.getReal() + cd1.getImaginary() * cd2.getImaginary()) / divisor,
                                (cd1.getImaginary() * cd2.getReal() - cd1.getReal() * cd2.getImaginary()) / divisor
                                );
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString().trim();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2)); 
                        }
                        
                        final char c = s2.charAt(s2.length() - 1);
                        if (c != 'i' && c != 'I') {
                            if (XNumberUtils.isDigits(s2)) {
                                final long l = d2.toLong();
                                return l == 1l ? DataConv.toLongData(d1) : DoubleData.nonNullValueOf(divideDecimalToWhole(d1, d2).doubleValue());
                            } else {
                                return DoubleData.nonNullValueOf(divideDecimals(d1, d2).doubleValue());
                            }
                        } else {
                            final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                            final double divisor = cd2.getReal() * cd2.getReal() + cd2.getImaginary() * cd2.getImaginary();
                            return ComplexData.nonNullValueOf(
                                    (cd1.getReal() * cd2.getReal() + cd1.getImaginary() * cd2.getImaginary()) / divisor,
                                    (cd1.getImaginary() * cd2.getReal() - cd1.getReal() * cd2.getImaginary()) / divisor
                                    );
                        }
                    }
                    case Types.LIST: {
                        final LongData d1d = DataConv.toLongData(d1);
                        final ListData ld2 = DataConv.toListData(d2);
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(divide(d1d, ld2.get(i), nullAsZero));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                        final double ld1 = d1.toDouble();
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = Double.compare(arr[i], 0d) == 0 ? 0d : ld1 / arr[i];
                        }
                        return new DenseVectorData(arr);
                    }
                    default: throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2));
                }
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
                switch (d2.getType()) {
                    case Types.BOOLEAN: return d1; //only can be one for boolean at this point
                    case Types.INT: 
                        return d2.toInt() == 1 ? d1 : DoubleData.nonNullValueOf(divideDecimalToWhole(d1, d2).doubleValue());
                    case Types.LONG:
                        return d2.toLong() == 1l ? d1 : DoubleData.nonNullValueOf(divideDecimalToWhole(d1, d2).doubleValue());
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: 
                        return DoubleData.nonNullValueOf(divideDecimalToWhole(d1, d2).doubleValue());
                    case Types.INSTANT:
                    case Types.DECIMAL:
                        return DecimalData.nonNullValueOf(divideDecimals(d1, d2));
                    case Types.COMPLEX: {
                        final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                        final double divisor = cd2.getReal() * cd2.getReal() + cd2.getImaginary() * cd2.getImaginary();
                        return ComplexData.nonNullValueOf(
                                (cd1.getReal() * cd2.getReal() + cd1.getImaginary() * cd2.getImaginary()) / divisor,
                                (cd1.getImaginary() * cd2.getReal() - cd1.getReal() * cd2.getImaginary()) / divisor
                                );
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            if (XNumberUtils.isDigits(s2)) {
                                final long l = d2.toLong();
                                return l == 1l ? d1 : DoubleData.nonNullValueOf(divideDecimalToWhole(d1, d2).doubleValue());
                            } else {
                                return DoubleData.nonNullValueOf(divideDecimals(d1, d2).doubleValue());
                            }
                        } else {
                            final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                            final double divisor = cd2.getReal() * cd2.getReal() + cd2.getImaginary() * cd2.getImaginary();
                            return ComplexData.nonNullValueOf(
                                    (cd1.getReal() * cd2.getReal() + cd1.getImaginary() * cd2.getImaginary()) / divisor,
                                    (cd1.getImaginary() * cd2.getReal() - cd1.getReal() * cd2.getImaginary()) / divisor
                                    );
                        }
                    }
                    case Types.LIST: {
                        final LongData d1d = DataConv.toLongData(d1);
                        final ListData ld2 = DataConv.toListData(d2);
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(divide(d1d, ld2.get(i), nullAsZero));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                        final double ld1 = d1.toDouble();
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = Double.compare(arr[i], 0d) == 0 ? 0d : ld1 / arr[i];
                        }
                        return new DenseVectorData(arr);
                    }
                    default: throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2));
                }
            case Types.DECIMAL:
            case Types.INSTANT:
                switch (d2.getType()) {
                    case Types.BOOLEAN: return d1; //only can be one for boolean at this point
                    case Types.INT: 
                        return d2.toInt() == 1 ? d1 : DecimalData.nonNullValueOf(divideDecimalToWhole(d1, d2));
                    case Types.LONG:
                        return d2.toLong() == 1l ? d1 : DecimalData.nonNullValueOf(divideDecimalToWhole(d1, d2));
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: 
                        return DecimalData.nonNullValueOf(divideDecimalToWhole(d1, d2));
                    case Types.INSTANT:
                    case Types.DECIMAL:
                        return DecimalData.nonNullValueOf(divideDecimals(d1, d2));
                    case Types.COMPLEX: {
                        final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                        final double divisor = cd2.getReal() * cd2.getReal() + cd2.getImaginary() * cd2.getImaginary();
                        return ComplexData.nonNullValueOf(
                                (cd1.getReal() * cd2.getReal() + cd1.getImaginary() * cd2.getImaginary()) / divisor,
                                (cd1.getImaginary() * cd2.getReal() - cd1.getReal() * cd2.getImaginary()) / divisor
                                );
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            return DecimalData.nonNullValueOf(divideDecimals(d1, d2));
                        } else {
                            final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                            final double divisor = cd2.getReal() * cd2.getReal() + cd2.getImaginary() * cd2.getImaginary();
                            return ComplexData.nonNullValueOf(
                                    (cd1.getReal() * cd2.getReal() + cd1.getImaginary() * cd2.getImaginary()) / divisor,
                                    (cd1.getImaginary() * cd2.getReal() - cd1.getReal() * cd2.getImaginary()) / divisor
                                    );
                        }
                    }
                    case Types.LIST: {
                        final DecimalData d1d = DataConv.toDecimalData(d1);
                        final ListData ld2 = DataConv.toListData(d2);
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(DataConv.toDecimalData(divide(d1d, ld2.get(i), nullAsZero)));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                        final double ld1 = d1.toDouble();
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = Double.compare(arr[i], 0d) == 0 ? 0d : ld1 / arr[i];
                        }
                        return new DenseVectorData(arr);
                    }
                    default: throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2));
                }
            case Types.COMPLEX:
                switch (d2.getType()) {
                    case Types.BOOLEAN: return d1; //only can be one for boolean at this point
                    case Types.INT: 
                    case Types.LONG:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: 
                    case Types.INSTANT:
                    case Types.DECIMAL: {
                        final ComplexData cd1 = DataConv.toComplexData(d1);
                        final double val2 = d2.toDouble();
                        return ComplexData.nonNullValueOf(cd1.getReal() / val2, cd1.getImaginary() / val2);
                    }
                    case Types.COMPLEX: {
                        final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                        final double divisor = cd2.getReal() * cd2.getReal() + cd2.getImaginary() * cd2.getImaginary();
                        return ComplexData.nonNullValueOf(
                                (cd1.getReal() * cd2.getReal() + cd1.getImaginary() * cd2.getImaginary()) / divisor,
                                (cd1.getImaginary() * cd2.getReal() - cd1.getReal() * cd2.getImaginary()) / divisor
                                );
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            final ComplexData cd1 = DataConv.toComplexData(d1);
                            final double val2 = d2.toDouble();
                            return ComplexData.nonNullValueOf(cd1.getReal() / val2, cd1.getImaginary() / val2);
                        } else {
                            final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                            final double divisor = cd2.getReal() * cd2.getReal() + cd2.getImaginary() * cd2.getImaginary();
                            return ComplexData.nonNullValueOf(
                                    (cd1.getReal() * cd2.getReal() + cd1.getImaginary() * cd2.getImaginary()) / divisor,
                                    (cd1.getImaginary() * cd2.getReal() - cd1.getReal() * cd2.getImaginary()) / divisor
                                    );
                        }
                    }
                    case Types.LIST: {
                        final ListData ld2 = DataConv.toListData(d2);
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(divide(d1, ld2.get(i), nullAsZero));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                        final int size = vd.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(divide(d1, vd.get(i), true));
                        }
                        return new ListData(arr);
                    }
                    default: throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2));
                }
            case Types.BINARY:
            case Types.STRING: {
                final String s1 = d1.toString(), s2 = d2.toString();
                if (!XComplexUtils.isComplexNumber(s1)) {
                    throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d1));
                }
                if (!XComplexUtils.isComplexNumber(s2)) {
                    throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2));
                }
                
                if (XNumberUtils.isNumber(s1) && XNumberUtils.isNumber(s2)) {
                    if (d2.getType() == Types.DECIMAL || d2.getType() == Types.INSTANT) {
                        return XNumberUtils.isDigits(s2) ? DecimalData.nonNullValueOf(divideDecimalToWhole(d1, d2)) : DecimalData.nonNullValueOf(divideDecimals(d1, d2));
                    } else {
                        return XNumberUtils.isDigits(s2) ? DoubleData.nonNullValueOf(divideDecimalToWhole(d1, d2).doubleValue()) : DoubleData.nonNullValueOf(divideDecimals(d1, d2).doubleValue());
                    }
                } else {
                    final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                    final double divisor = cd2.getReal() * cd2.getReal() + cd2.getImaginary() * cd2.getImaginary();
                    return ComplexData.nonNullValueOf(
                            (cd1.getReal() * cd2.getReal() + cd1.getImaginary() * cd2.getImaginary()) / divisor,
                            (cd1.getImaginary() * cd2.getReal() - cd1.getReal() * cd2.getImaginary()) / divisor
                            );
                }
            }
            case Types.LIST:
                if (XTypeDataUtils.isWholeNumberType(d2.getType())) {
                    final ListData ld1 = DataConv.toListData(d1);
                    final int size = ld1.size();
                    final ArrayList<TypeData> arr = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        arr.add(divide (ld1.get(i), d2, nullAsZero));
                    }
                    return new ListData(arr);
                } else if (d2.getType() == Types.LIST || d2.getType() == Types.DENSEVECTOR || d2.getType() == Types.SPARSEVECTOR) {
                    if (d1.size() != d2.size()) {
                        throw new UnsupportedDataOperationException ("DATA_DIVIDE: SIZE MISMATCH - " + XCalcException.getNonNumericException(d1));
                    }
                    final ListData ld1 = DataConv.toListData(d1);
                    final ListData ld2 = DataConv.toListData(d2);
                    final int size = ld1.size();
                    final ArrayList<TypeData> arr = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        arr.add(divide (ld1.get(i), ld2.get(i), nullAsZero));
                    }
                    return new ListData(arr);
                }
            case Types.DENSEVECTOR:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.BOOLEAN:
                    case Types.LONG:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.COMPLEX: 
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: {
                        final DenseVectorData vd = DataConv.toDenseVectorData(d1);
                        final double ld2 = d2.toDouble();
                        if (Double.compare(ld2, 0d) == 0) {
                            return new DenseVectorData(new double[vd.size()]);
                        }
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = arr[i] / ld2;
                        }
                        return new DenseVectorData(arr);
                    }
                    case Types.INSTANT:
                    case Types.DECIMAL: {
                        final DecimalData d2n = DataConv.toDecimalData(d2);
                        if (DecimalData.ONE.equals(d2n) ) {
                            return d1;
                        }
                        final DenseVectorData vd = DataConv.toDenseVectorData(d1);
                        final double ld2 = d2n.toDouble();
                        if (Double.compare(ld2, 0d) == 0) {
                            return new DenseVectorData(new double[vd.size()]);
                        }
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = arr[i] / ld2;
                        }
                        return new DenseVectorData(arr);
                    }
                    case Types.BINARY:
                    case Types.STRING: 
                        if (!XComplexUtils.isComplexNumber(d2.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2)); 
                        } else {
                            final DecimalData d2n = DataConv.toDecimalData(DataConv.toComplexData(d2));
                            if (DecimalData.ONE.equals(d2n) ) {
                                return d1;
                            }
                            final DenseVectorData vd = DataConv.toDenseVectorData(d1);
                            final double ld2 = d2n.toDouble();
                            if (Double.compare(ld2, 0d) == 0) {
                                return new DenseVectorData(new double[vd.size()]);
                            }
                            final double[] arr = vd.getDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = arr[i] / ld2;
                            }
                            return new DenseVectorData(arr);
                        }
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: SIZE MISMATCH - " + XCalcException.getNonNumericException(d2));
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final DenseVectorData vd2 = DataConv.toDenseVectorData(d2);
                            final double[] arr = vd1.getDoubles(), arr2 = vd2.getUnsafeDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = Double.compare(0d, arr2[i]) == 0 ? 0d : arr[i] / arr2[i];
                            }
                            return new DenseVectorData(arr);
                        }
                }
            case Types.SPARSEVECTOR:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.BOOLEAN:
                    case Types.LONG:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.DECIMAL:
                    case Types.COMPLEX:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: 
                    case Types.INSTANT: {
                        final DecimalData d2n = DataConv.toDecimalData(d2);
                        if (DecimalData.ONE.equals(d2n) ) {
                            return d1;
                        } else if (DecimalData.ZERO.equals(d2n) ) {
                            final SparseVectorData sd = DataConv.toSparseVectorData(d1);
                            return new SparseVectorData(sd.size(), new int[0], new double[0]);
                        } else {
                            final SparseVectorData sd = DataConv.toSparseVectorData(d1);
                            final double[] nonzeros = sd.getNonzeros();
                            final double d2d = d2n.toDouble();
                            final int len = nonzeros.length;
                            for (int i = 0; i < len; ++i) {
                                try {
                                    nonzeros[i] = nonzeros[i] / d2d;
                                } catch (IllegalArgumentException e) {
                                    nonzeros[i] = 0d;
                                }
                            }
                            return new SparseVectorData(sd.size(), sd.getIndices(), nonzeros);
                        }
                    }
                    case Types.BINARY:
                    case Types.STRING: 
                        if (!XComplexUtils.isComplexNumber(d2.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2)); 
                        } else {
                            final DecimalData d2n = DataConv.toDecimalData(DataConv.toComplexData(d2));
                            if (DecimalData.ZERO.equals(d2n) ) {
                                final SparseVectorData sd = DataConv.toSparseVectorData(d1);
                                return new SparseVectorData(sd.size(), new int[0], new double[0]);
                            } else {
                                final SparseVectorData sd = DataConv.toSparseVectorData(d1);
                                final double[] nonzeros = sd.getNonzeros();
                                final double d2d = d2n.toDouble();
                                final int len = nonzeros.length;
                                for (int i = 0; i < len; ++i) {
                                    try {
                                        nonzeros[i] = nonzeros[i] / d2d;
                                    } catch (IllegalArgumentException e) {
                                        nonzeros[i] = 0d;
                                    }
                                }
                                return new SparseVectorData(sd.size(), sd.getIndices(), nonzeros);
                            }
                        }
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d2));
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final DenseVectorData vd2 = DataConv.toDenseVectorData(d2);
                            final double[] arr = vd1.getDoubles(), arr2 = vd2.getUnsafeDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = Double.compare(0d, arr2[i]) == 0 ? 0d : arr[i] / arr2[i];
                            }
                            return new DenseVectorData(arr);
                        }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d1));
                }
            default:
                throw new UnsupportedDataOperationException ("DATA_DIVIDE: " + XCalcException.getNonNumericException(d1));
        }
    }
    
    protected static TypeData remainder(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        if (d1.isNull() || d2.isNull() || XCalcGeneral.isZero(d2)) {
            return nullAsZero ? IntData.ZERO : IntData.NULL;
        }
        switch (d1.getType()) {
            case Types.INT:
            case Types.BOOLEAN:
                switch (d2.getType()) {
                case Types.INT: 
                    return IntData.nonNullValueOf(d1.toInt() % d2.toInt());
                case Types.LONG:
                case Types.DATE: 
                case Types.TIME: 
                case Types.TIMESTAMP: 
                case Types.CALENDAR_TIME: 
                case Types.CALENDAR_TIMESTAMP: 
                case Types.INSTANT: 
                    return LongData.nonNullValueOf(d1.toLong() % d2.toLong());
                case Types.FLOAT:
                case Types.DOUBLE:
                case Types.NUMERIC:
                    return DoubleData.nonNullValueOf(d1.toDecimal().remainder(d2.toDecimal(), MathContext.DECIMAL128).doubleValue());
                case Types.DECIMAL: 
                case Types.COMPLEX: 
                    return DecimalData.nonNullValueOf(d1.toDecimal().remainder(d2.toDecimal(), MathContext.DECIMAL128));
                case Types.BOOLEAN: 
                    return IntData.ZERO;
                case Types.BINARY:
                case Types.STRING:
                    return (d2.isDigits()) 
                            ? LongData.nonNullValueOf(d1.toLong() % d2.toLong())
                            : DecimalData.nonNullValueOf(d1.toDecimal().remainder(d2.toDecimal(), MathContext.DECIMAL128));
                default:
                    throw new UnsupportedDataOperationException ("DATA_REMAINDER: " + XCalcException.getNonNumericException(d2));
            }
            case Types.LONG:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.LONG:
                    case Types.DATE: 
                    case Types.TIME: 
                    case Types.TIMESTAMP: 
                    case Types.CALENDAR_TIME: 
                    case Types.CALENDAR_TIMESTAMP: 
                    case Types.INSTANT: 
                        return LongData.nonNullValueOf(d1.toLong() % d2.toLong());
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                        return DoubleData.nonNullValueOf(d1.toDecimal().remainder(d2.toDecimal(), MathContext.DECIMAL128).doubleValue());
                    case Types.DECIMAL: 
                    case Types.COMPLEX: 
                        return DecimalData.nonNullValueOf(d1.toDecimal().remainder(d2.toDecimal(), MathContext.DECIMAL128));
                    case Types.BOOLEAN: 
                        return LongData.ZERO;
                    case Types.BINARY:
                    case Types.STRING:
                        return (d2.isDigits()) 
                                ? LongData.nonNullValueOf(d1.toLong() % d2.toLong())
                                : DecimalData.nonNullValueOf(d1.toDecimal().remainder(d2.toDecimal(), MathContext.DECIMAL128));
                    default:
                        throw new UnsupportedDataOperationException ("DATA_REMAINDER: " + XCalcException.getNonNumericException(d2));
                }
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
                switch (d2.getType()) {
                    case Types.INT: 
                    case Types.LONG:
                    case Types.DATE: 
                    case Types.TIME: 
                    case Types.TIMESTAMP: 
                    case Types.CALENDAR_TIME: 
                    case Types.CALENDAR_TIMESTAMP:
                    case Types.FLOAT: 
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.DECIMAL:
                    case Types.BOOLEAN:
                    case Types.BINARY:
                    case Types.STRING:
                        return DoubleData.nonNullValueOf(d1.toDecimal().remainder(d2.toDecimal(), MathContext.DECIMAL128).doubleValue()); 
                    case Types.COMPLEX:
                    case Types.INSTANT:
                        return new DecimalData(d1.toDecimal().remainder(d2.toDecimal(), MathContext.DECIMAL128)); 
                    default:
                        throw new UnsupportedDataOperationException ("DATA_REMAINDER: " + XCalcException.getNonNumericException(d2));
                }
            case Types.DECIMAL:
            case Types.COMPLEX:
                switch (d2.getType()) {
                    case Types.INT: 
                    case Types.LONG:
                    case Types.DATE: 
                    case Types.TIME: 
                    case Types.TIMESTAMP: 
                    case Types.CALENDAR_TIME: 
                    case Types.CALENDAR_TIMESTAMP:
                    case Types.FLOAT: 
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.DECIMAL:
                    case Types.BOOLEAN:
                    case Types.BINARY:
                    case Types.STRING:
                    case Types.COMPLEX:
                    case Types.INSTANT:
                        return DecimalData.nonNullValueOf(d1.toDecimal().remainder(d2.toDecimal(), MathContext.DECIMAL128)); 
                    default:
                        throw new UnsupportedDataOperationException ("DATA_REMAINDER: " + XCalcException.getNonNumericException(d2));
                }
            case Types.STRING:
                return (d1.isDigits() && d2.isDigits())
                        ? LongData.nonNullValueOf(d1.toLong() % d2.toLong())
                        : DecimalData.nonNullValueOf(d1.toDecimal().remainder(d2.toDecimal(), MathContext.DECIMAL128)); 
            case Types.LIST:
                switch (d2.getType()) {
                    case Types.SET:
                        throw new UnsupportedDataOperationException ("DATA_REMAINDER: LIST MOD SET - " + d2.toString());
                    case Types.MAP:
                        throw new UnsupportedDataOperationException ("DATA_REMAINDER: LIST MOD MAP - " + d2.toString());
                    case Types.LIST: 
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: 
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_REMAINDER: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final ListData ld1 = DataConv.toListData(d1);
                            final ListData ld2 = DataConv.toListData(d2);
                            final int size = ld1.size();
                            final ArrayList<TypeData> arr = new ArrayList<>(size);
                            for (int i = 0; i < size; ++i ) {
                                arr.add(remainder(ld1.get(i), ld2.get(i), nullAsZero));
                            }
                            return new ListData(arr);
                        }
                    default: {
                        final ListData ld1 = DataConv.toListData(d1);
                        final int size = ld1.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i ) {
                            arr.add(remainder(ld1.get(i), d2, nullAsZero));
                        }
                        return new ListData(arr);
                    }
                }
            case Types.SET: 
                throw new UnsupportedDataOperationException ("DATA_REMAINDER: SET MOD IS INVALID");
            case Types.MAP: 
                throw new UnsupportedDataOperationException ("DATA_REMAINDER: MAP MOD IS INVALID");
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR:
                switch (d2.getType()) {
                    case Types.SET:
                        throw new UnsupportedDataOperationException ("DATA_REMAINDER: VECTOR MOD SET - " + d2.toString());
                    case Types.MAP:
                        throw new UnsupportedDataOperationException ("DATA_REMAINDER: VECTOR MOD MAP - " + d2.toString());
                    case Types.LIST:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_POWER: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final ListData ld2 = DataConv.toListData(d2);
                            final int size = vd1.size();
                            final double[] result = new double[size];
                            Double d;
                            for (int i = 0; i < size; ++i ) {
                                d = remainder(vd1.get(i), ld2.get(i), nullAsZero).toDouble();
                                result[i] = d != null ? d.doubleValue() : 0d;
                            }
                            return new DenseVectorData(result);
                        }
                    case Types.DENSEVECTOR: 
                    case Types.SPARSEVECTOR:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_REMAINDER: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final DenseVectorData vd2 = DataConv.toDenseVectorData(d2);
                            final int size = vd1.size();
                            final double[] result = new double[size];
                            Double d;
                            for (int i = 0; i < size; ++i ) {
                                d = remainder(vd1.get(i), vd2.get(i), true).toDouble();
                                result[i] = d != null ? d.doubleValue() : 0d;
                            }
                            return new DenseVectorData(result);
                        }
                    default: {
                        final DenseVectorData ld1 = DataConv.toDenseVectorData(d1);
                        final int size = ld1.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i ) {
                            arr.add(remainder(ld1.get(i), d2, true));
                        }
                        return new ListData(arr);
                    }
                }
            default:
                throw new UnsupportedDataOperationException ("DATA_REMAINDER: " + XCalcException.getNonNumericException(d2));
        }
    }
    
    private static BigDecimal divideDecimalToWhole(final TypeData d1, final TypeData d2) throws EngineException {
       return d1.toDecimal().divide(d2.toDecimal(), MathContext.DECIMAL128.getPrecision(), RoundingMode.HALF_UP);
    }
    
    private static BigDecimal divideDecimals(final TypeData d1, final TypeData d2) throws EngineException {
        final BigDecimal val1 = d1.toDecimal(), val2 = d2.toDecimal();
        final BigDecimal result = val1.divide(val2, MathContext.DECIMAL128.getPrecision(), RoundingMode.HALF_UP);
        final boolean signtest = (val1.signum() >= 0) == (val2.signum() >= 0);
        if (signtest ? result.signum() < 0 : result.signum() > 0) {
            throw new UnsupportedDataOperationException ("DATA_DIVIDE_OVERFLOW: " + d1 + " / " + d2);
        }
        return result;
    }
}
