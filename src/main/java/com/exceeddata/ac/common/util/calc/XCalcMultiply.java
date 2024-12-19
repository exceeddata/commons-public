package com.exceeddata.ac.common.util.calc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.ComplexData;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DecimalData;
import com.exceeddata.ac.common.data.typedata.DenseVectorData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.ListData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.SparseVectorData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.UnsupportedDataOperationException;
import com.exceeddata.ac.common.util.XComplexUtils;
import com.exceeddata.ac.common.util.XNumberUtils;

public final class XCalcMultiply {
    private XCalcMultiply() {}
    
    protected static TypeData multiply(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        if (d1.isNull()) {
            return nullAsZero ? XCalcGeneral.zero(d2) : d1;
        } else if (d2.isNull()) {
            return nullAsZero ? XCalcGeneral.zero(d1) : d2;
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
                    case Types.INT:
                    case Types.BOOLEAN:
                    case Types.LONG: 
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP:
                        return multiplyLongs(d1, d2);
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                        return multiplyDoubles(d1, d2);
                    case Types.INSTANT:
                    case Types.DECIMAL:
                        return multiplyDecimals(d1, d2);
                    case Types.COMPLEX: {
                        final double val1 = d1.toDouble();
                        final ComplexData cd2 = (ComplexData) d2;
                        return ComplexData.nonNullValueOf(val1 * cd2.getReal(), val1 * cd2.getImaginary());
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString().trim();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2)); 
                        }
                        
                        final char c = s2.charAt(s2.length() - 1);
                        if (c != 'i' && c != 'I') {
                            return XNumberUtils.isDigits(s2) ? multiplyLongs(d1, d2) : multiplyDoubles(d1, d2);
                        } else {
                            final double val1 = d1.toDouble();
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(val1 * cd2.getReal(), val1 * cd2.getImaginary());
                        }
                    }
                    case Types.LIST:
                        if (d1.toLong().equals(1L)) {
                            return d2;
                        } else {
                            final LongData d1d = DataConv.toLongData(d1);
                            final ListData ld2 = (ListData) d2;
                            final int size = ld2.size();
                            final ArrayList<TypeData> arr = new ArrayList<>(size);
                            for (int i = 0; i < size; ++i) {
                                arr.add(multiply(d1d, ld2.get(i), nullAsZero));
                            }
                            return new ListData(arr);
                        }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:
                        if (d1.toLong().equals(1L)) {
                            return d2;
                        } else {
                            final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                            final double ld1 = d1.toDouble();
                            if (Double.compare(0d, ld1) == 0) {
                                return new DenseVectorData(new double[vd.size()]);
                            }
                            final double[] arr = vd.getDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = ld1 * arr[i];
                            }
                            return new DenseVectorData(arr);
                        }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2));
                }
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
                switch (d2.getType()) {
                    case Types.INT: 
                    case Types.BOOLEAN: 
                    case Types.LONG:
                    case Types.FLOAT:
                    case Types.DOUBLE: 
                    case Types.NUMERIC:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP:
                        return multiplyDoubles(d1, d2);
                    case Types.INSTANT:
                    case Types.DECIMAL:
                        return multiplyDecimals(d1, d2);
                    case Types.COMPLEX: {
                        final double val1 = d1.toDouble();
                        final ComplexData cd2 = (ComplexData) d2;
                        return ComplexData.nonNullValueOf(val1 * cd2.getReal(), val1 * cd2.getImaginary());
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            return multiplyDoubles(d1, d2);
                        } else {
                            final double val1 = d1.toDouble();
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(val1 * cd2.getReal(), val1 * cd2.getImaginary());
                        }
                    }
                    case Types.LIST: {
                        final DoubleData d1d = DataConv.toDoubleData(d1);
                        final ListData ld2 = (ListData) d2;
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(multiply(d1d, ld2.get(i), nullAsZero));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:{
                        final DenseVectorData vd = (DenseVectorData) d2;
                        final double ld1 = d1.toDouble();
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = ld1 * arr[i];
                        }
                        return new DenseVectorData(arr);
                    }
                    case Types.SPARSEVECTOR: {
                        final SparseVectorData vd = (SparseVectorData) d2;
                        final double ld1 = d1.toDouble();
                        final int[] indices = vd.getIndices();
                        final double[] nonzeros = vd.getNonzeros();
                        for (int i = 0, len = nonzeros.length; i < len; ++i) {
                            nonzeros[i] = ld1 * nonzeros[i];
                        }
                        return new SparseVectorData(vd.size(), indices, nonzeros);
                    }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2));
                }
            case Types.DECIMAL:
            case Types.INSTANT:
                switch (d2.getType()) {
                    case Types.INT: 
                    case Types.BOOLEAN: 
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
                    case Types.DECIMAL:
                        return multiplyDecimals(d1, d2);
                    case Types.COMPLEX: {
                        final double val1 = d1.toDouble();
                        final ComplexData cd2 = (ComplexData) d2;
                        return ComplexData.nonNullValueOf(val1 * cd2.getReal(), val1 * cd2.getImaginary());
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            return multiplyDecimals(d1, d2);
                        } else {
                            final double val1 = d1.toDouble();
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(val1 * cd2.getReal(), val1 * cd2.getImaginary());
                        }
                    }
                    case Types.LIST: {
                            final DecimalData d1d = DataConv.toDecimalData(d1);
                            if (DecimalData.ONE.equals(d1d)) {
                                return d2;
                            } else if (DecimalData.ZERO.equals(d1d)) {
                                return new ListData(new ArrayList<TypeData>(Collections.nCopies(d2.size(), DecimalData.ZERO)));
                            } else {
                                final ListData ld2 = (ListData) d2;
                                final int size = ld2.size();
                                final ArrayList<TypeData> arr = new ArrayList<>(size);
                                for (int i = 0; i < size; ++i) {
                                    arr.add(multiply(d1d, ld2.get(i), nullAsZero));
                                }
                                return new ListData(arr);
                            }
                        }
                    case Types.DENSEVECTOR:{
                        final BigDecimal d1d = d1.toDecimal();
                        if (BigDecimal.ONE.equals(d1d)) {
                            return d2;
                        }
                        if (BigDecimal.ZERO.equals(d1d)) {
                            return new SparseVectorData(d2.size(), new int[0], new double[0]);
                        }
                        final DenseVectorData vd = (DenseVectorData) d2;
                        final double ld1 = d1d.doubleValue();
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = ld1 * arr[i];
                        }
                        return new DenseVectorData(arr);
                    }
                    case Types.SPARSEVECTOR: {
                        final BigDecimal d1d = d1.toDecimal();
                        if (BigDecimal.ONE.equals(d1d)) {
                            return d2;
                        }
                        if (BigDecimal.ZERO.equals(d1d)) {
                            return new SparseVectorData(d2.size(), new int[0], new double[0]);
                        }
                        final SparseVectorData vd = (SparseVectorData) d2;
                        final double ld1 = d1d.doubleValue();
                        final int[] indices = vd.getIndices();
                        final double[] nonzeros = vd.getNonzeros();
                        for (int i = 0, len = nonzeros.length; i < len; ++i) {
                            nonzeros[i] = ld1 * nonzeros[i];
                        }
                        return new SparseVectorData(vd.size(), indices, nonzeros);
                    }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2));
                }
            case Types.COMPLEX:
                switch (d2.getType()) {
                    case Types.INT: 
                    case Types.BOOLEAN: 
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
                        final ComplexData cd1 = (ComplexData) d1;
                        final Double val2 = d2.toDouble();
                        return ComplexData.nonNullValueOf(cd1.getReal() * val2, cd1.getImaginary() * val2);
                    }
                    case Types.COMPLEX: {
                        final ComplexData cd1 = (ComplexData) d1, cd2 = (ComplexData) d2;
                        return ComplexData.nonNullValueOf(
                                cd1.getReal() * cd2.getReal() - cd1.getImaginary() * cd2.getImaginary(), 
                                cd1.getReal() * cd2.getImaginary() + cd1.getImaginary() * cd2.getReal()
                                );
                    }
                    case Types.BINARY:
                    case Types.STRING:
                        if (!XComplexUtils.isComplexNumber(d2.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(d2.toString())) {
                            final ComplexData cd1 = (ComplexData) d1;
                            final Double val2 = d2.toDouble();
                            return ComplexData.nonNullValueOf(cd1.getReal() * val2, cd1.getImaginary() * val2);
                        } else {
                            final ComplexData cd1 = (ComplexData) d1, cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(
                                    cd1.getReal() * cd2.getReal() - cd1.getImaginary() * cd2.getImaginary(), 
                                    cd1.getReal() * cd2.getImaginary() + cd1.getImaginary() * cd2.getReal()
                                    );
                        }
                    case Types.LIST: {
                        final ListData ld2 = (ListData) d2;
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(multiply(d1, ld2.get(i), nullAsZero));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                        final int size = vd.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(multiply(d1, vd.get(i), true));
                        }
                        return new ListData(arr);
                    }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2));
                }
            case Types.BINARY:
            case Types.STRING:
                if (!XComplexUtils.isComplexNumber(d1.toString())) {
                    throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d1));
                }
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.LONG:
                    case Types.BOOLEAN:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: {
                        final String s1 = d1.toString();
                        if (XNumberUtils.isNumber(s1)) {
                            return XNumberUtils.isDigits(s1) ? multiplyLongs(d1, d2) : multiplyDoubles(d1, d2);
                        } else {
                            final ComplexData cd1 = new ComplexData(d1.toString());
                            final double val2 = d2.toDouble();
                            return ComplexData.nonNullValueOf(cd1.getReal() * val2, cd1.getImaginary() * val2);
                        }
                    }
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: {
                        final String s1 = d1.toString();
                        if (XNumberUtils.isNumber(s1)) {
                            return multiplyDoubles(d1, d2);
                        } else {
                            final ComplexData cd1 = new ComplexData(d1.toString());
                            final double val2 = d2.toDouble();
                            return ComplexData.nonNullValueOf(cd1.getReal() * val2, cd1.getImaginary() * val2);
                        }
                    }
                    case Types.DECIMAL:
                    case Types.INSTANT:{
                        final String s1 = d1.toString();
                        if (XNumberUtils.isNumber(s1)) {
                            return multiplyDecimals(d1, d2);
                        } else {
                            final ComplexData cd1 = new ComplexData(d1.toString());
                            final double val2 = d2.toDouble();
                            return ComplexData.nonNullValueOf(cd1.getReal() * val2, cd1.getImaginary() * val2);
                        }
                    }
                    case Types.COMPLEX: {
                        final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = (ComplexData) d2;
                        return ComplexData.nonNullValueOf(
                                cd1.getReal() * cd2.getReal() - cd1.getImaginary() * cd2.getImaginary(), 
                                cd1.getReal() * cd2.getImaginary() + cd1.getImaginary() * cd2.getReal()
                                );
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s1 = d1.toString(), s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2)); 
                        }
                        if (XNumberUtils.isNumber(s1) && XNumberUtils.isNumber(s2)) {
                            return (XNumberUtils.isDigits(s1) && XNumberUtils.isDigits(s2)) ? multiplyLongs(d1, d2) : multiplyDoubles(d1, d2);
                        } else {
                            final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(
                                    cd1.getReal() * cd2.getReal() - cd1.getImaginary() * cd2.getImaginary(), 
                                    cd1.getReal() * cd2.getImaginary() + cd1.getImaginary() * cd2.getReal()
                                    );
                        }
                    }
                    case Types.LIST: {
                        final ListData ld2 = (ListData) d2;
                        final int size = ld2.size();
                        final ArrayList<TypeData> result = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            result.add(multiply(d1, ld2.get(i), nullAsZero));
                        }
                        return new ListData(result);
                    }
                    case Types.DENSEVECTOR: {
                        final double ld1 = d1.toDouble();
                        if (Double.compare(0d, ld1) == 0) {
                            return new SparseVectorData(d2.size(), new int[0], new double[0]);
                        } else if (Double.compare(1d, ld1) == 0) {
                            return d2;
                        } else {
                            final DenseVectorData vd2 = (DenseVectorData) d2;
                            final double[] arr = vd2.getDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = ld1 * arr[i];
                            }
                            return new DenseVectorData(arr);
                        }
                    }
                    case Types.SPARSEVECTOR: {
                        final double ld1 = d1.toDouble();
                        if (Double.compare(0d, ld1) == 0) {
                            return new SparseVectorData(d2.size(), new int[0], new double[0]);
                        } else if (Double.compare(1d, ld1) == 0) {
                            return d2;
                        } else {
                            final SparseVectorData vd2 = (SparseVectorData) d2;
                            final int size = vd2.size();
                            final int[] indices = vd2.getIndices();
                            final double[] nonzeros = vd2.getNonzeros();
                            for (int i = 0, len = nonzeros.length; i < len; ++i) {
                                nonzeros[i] = ld1 * nonzeros[i];
                            }
                            return new SparseVectorData(size, indices, nonzeros);
                        }
                    }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2));
                }
            case Types.LIST:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.BOOLEAN:
                    case Types.LONG:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.DECIMAL:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: 
                    case Types.INSTANT: {
                        final ListData vd = (ListData) d1;
                        final int size = vd.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(multiply(vd.get(i), d2, true));
                        }
                        return new ListData(arr);
                    }
                    case Types.BINARY:
                    case Types.STRING: 
                        if (!XComplexUtils.isComplexNumber(d2.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2));
                        } //flow through to complex
                    case Types.COMPLEX: {
                            final ListData vd = (ListData) d1;
                            final int size = vd.size();
                            final ArrayList<TypeData> arr = new ArrayList<>(size);
                            for (int i = 0; i < size; ++i) {
                                arr.add(multiply(vd.get(i), d2, true));
                            }
                            return new ListData(arr);
                        }
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: SIZE MISMATCH - " + XCalcException.getNonNumericException(d2));
                        } else {
                            final ListData vd1 = (ListData) d1;
                            final ListData vd2 = DataConv.toListData(d2);
                            final int size = vd1.size();
                            final ArrayList<TypeData> arr = new ArrayList<>(size);
                            for (int i = 0; i < size; ++i) {
                                arr.add(multiply(vd1.get(i), vd2.get(i), true));
                            }
                            return new ListData(arr);
                        }
                }
            case Types.DENSEVECTOR:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.BOOLEAN:
                    case Types.LONG:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                    case Types.DECIMAL:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: 
                    case Types.INSTANT: {
                        final Double d2n = d2.toDouble();
                        if (Double.compare(0d, d2n) == 0) {
                            return new SparseVectorData(d1.size(), new int[0], new double[0]);
                        } else if (Double.compare(1d, d2n) == 0) {
                            return d1;
                        } else {
                            final DenseVectorData vd = (DenseVectorData) d1;
                            final double[] arr = vd.getDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = d2n * arr[i];
                            }
                            return new DenseVectorData(arr);
                        }
                    }
                    case Types.BINARY:
                    case Types.STRING: 
                        if (!XComplexUtils.isComplexNumber(d2.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2));
                        } //flow through to complex
                    case Types.COMPLEX: { 
                            final Double d2n = d2.toDouble();
                            if (Double.compare(0d, d2n) == 0) {
                                return new SparseVectorData(d1.size(), new int[0], new double[0]);
                            } else if (Double.compare(1d, d2n) == 0) {
                                return d1;
                            } else {
                                final DenseVectorData vd = (DenseVectorData) d1;
                                final double[] arr = vd.getDoubles();
                                for (int i = 0, len = arr.length; i < len; ++i) {
                                    arr[i] = d2n * arr[i];
                                }
                                return new DenseVectorData(arr);
                            }
                        }
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: SIZE MISMATCH - " + XCalcException.getNonNumericException(d2));
                        } else {
                            final DenseVectorData vd1 = (DenseVectorData) d1;
                            final DenseVectorData vd2 = DataConv.toDenseVectorData(d2);
                            final double[] arr = vd1.getDoubles(), arr2 = vd2.getUnsafeDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = arr[i] * arr2[i];
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
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: 
                    case Types.INSTANT: {
                        final Double d2n = d2.toDouble();
                        if (Double.compare(0d, d2n) == 0) {
                            return new SparseVectorData(d1.size(), new int[0], new double[0]);
                        } else if (Double.compare(1d, d2n) == 0) {
                            return d1;
                        } else {
                            final SparseVectorData sd = (SparseVectorData) d1;
                            final double[] nonzeros = sd.getNonzeros();;
                            final int len = nonzeros.length;
                            for (int i = 0; i < len; ++i) {
                                nonzeros[i] = nonzeros[i] * d2n;
                            }
                            return new SparseVectorData(sd.size(), sd.getIndices(), nonzeros);
                        }
                    }
                    case Types.BINARY:
                    case Types.STRING: 
                        if (!XComplexUtils.isComplexNumber(d2.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2));
                        } //flow through to complex
                    case Types.COMPLEX: {
                        final Double d2n = d2.toDouble();
                        if (Double.compare(0d, d2n) == 0) {
                            return new SparseVectorData(d1.size(), new int[0], new double[0]);
                        } else if (Double.compare(1d, d2n) == 0) {
                            return d1;
                        } else {
                            final SparseVectorData sd = (SparseVectorData) d1;
                            final double[] nonzeros = sd.getNonzeros();
                            final int len = nonzeros.length;
                            for (int i = 0; i < len; ++i) {
                                nonzeros[i] = nonzeros[i] * d2n;
                            }
                            return new SparseVectorData(sd.size(), sd.getIndices(), nonzeros);
                        }
                    }
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d2));
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final DenseVectorData vd2 = DataConv.toDenseVectorData(d2);
                            final double[] arr = vd1.getDoubles(), arr2 = vd2.getUnsafeDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = arr[i] * arr2[i];
                            }
                            return new DenseVectorData(arr);
                        }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d1));
                }
            default:
                throw new UnsupportedDataOperationException ("DATA_MULTIPLY: " + XCalcException.getNonNumericException(d1));
        }
    }
    
    private static TypeData multiplyLongs (final TypeData d1, final TypeData d2) throws EngineException {
        final long val1 = d1.toLong(), val2 = d2.toLong();
        final long result = val1 * val2;
        final boolean signtest = (val1 >= 0l) == (val2 >= 0l);
        if (signtest ? result < 0 : result > 0) {
            //overflow
            final BigDecimal result2 = BigDecimal.valueOf(val1).multiply(BigDecimal.valueOf(val2));
            if (signtest ? result2.signum() < 0 : result2.signum() > 0) {
                throw new UnsupportedDataOperationException ("DATA_MULTIPLY_OVERFLOW: " + d1 + " x " + d2);
            }
            return DecimalData.nonNullValueOf(result2);
        } else {
            return LongData.nonNullValueOf(result);
        }
    }
    
    private static TypeData multiplyDoubles (final TypeData d1, final TypeData d2) throws EngineException {
        final double val1 = d1.toDouble(), val2 = d2.toDouble();
        final double result = val1 * val2;
        final boolean signtest = (val1 >= 0l) == (val2 >= 0l);
        if (signtest ? result < 0 : result > 0) {
            //overflow
            final BigDecimal result2 = d1.toDecimal().multiply(d2.toDecimal());
            if (signtest ? result2.signum() < 0 : result2.signum() > 0) {
                throw new UnsupportedDataOperationException ("DATA_MULTIPLY_OVERFLOW: " + d1 + " x " + d2);
            }
            return DecimalData.nonNullValueOf(result2);
        } else {
            return DoubleData.nonNullValueOf(result);
        }
    }
    
    private static TypeData multiplyDecimals (final TypeData d1, final TypeData d2) throws EngineException {
        final BigDecimal val1 = d1.toDecimal(), val2 = d2.toDecimal();
        final BigDecimal result = val1.multiply(val2);
        final boolean signtest = (val1.signum() >= 0) == (val2.signum() >= 0);
        if (signtest ? result.signum() < 0 : result.signum() > 0) {
            throw new UnsupportedDataOperationException ("DATA_MULTIPLY_OVERFLOW: " + d1 + " x " + d2);
        }
        return DecimalData.nonNullValueOf(result);
    }
}
