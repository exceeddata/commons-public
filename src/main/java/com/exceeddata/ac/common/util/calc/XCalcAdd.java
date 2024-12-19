package com.exceeddata.ac.common.util.calc;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.CalendarTimeData;
import com.exceeddata.ac.common.data.typedata.CalendarTimestampData;
import com.exceeddata.ac.common.data.typedata.ComplexData;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DateData;
import com.exceeddata.ac.common.data.typedata.DecimalData;
import com.exceeddata.ac.common.data.typedata.DenseVectorData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.InstantData;
import com.exceeddata.ac.common.data.typedata.ListData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.StringData;
import com.exceeddata.ac.common.data.typedata.TimeData;
import com.exceeddata.ac.common.data.typedata.TimestampData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.UnsupportedDataOperationException;
import com.exceeddata.ac.common.util.XComplexUtils;
import com.exceeddata.ac.common.util.XNumberUtils;

public final class XCalcAdd {
    private XCalcAdd() {}
    
    public static TypeData add(final TypeData d1, final TypeData d2, final boolean numeric, final boolean nullAsZero) throws EngineException {
        if (d1.isNull()) {
            return nullAsZero ? d2 : d1;
        } else if (d2.isNull()) {
            return nullAsZero ? d1 : d2;
        }
        switch (d1.getType()) {
            case Types.INT:
            case Types.BOOLEAN:
            case Types.LONG:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.BOOLEAN:
                    case Types.LONG:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: 
                        return addLongs(d1, d2);
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                        return addDoubles(d1, d2);
                    case Types.COMPLEX: {
                        final ComplexData cd2 = DataConv.toComplexData(d2);
                        return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (XNumberUtils.isNumber(s2)) {
                            return XNumberUtils.isDigits(s2) ? addLongs(d1, d2) : addDoubles(d1, d2);
                        } else {
                            throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); 
                        }
                    }
                    case Types.INSTANT:
                    case Types.DECIMAL:
                        return addDecimals(d1, d2);
                    case Types.LIST: {
                        final LongData d1l = DataConv.toLongData(d1);
                        if (LongData.ZERO.equals(d1l)) {
                            return d2;
                        } 
                        final ListData ld2 = DataConv.toListData(d2);
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(add(d1l, ld2.get(i), numeric, nullAsZero));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:
                        if (d1.toLong().equals(0L)) {
                            return d2;
                        } else {
                            final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                            final double ld1 = d1.toDouble();
                            final double[] arr = vd.getDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = ld1 + arr[i];
                            }
                            return new DenseVectorData(arr);
                        }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
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
                        return addDoubles(d1, d2);
                    case Types.COMPLEX: {
                        final ComplexData cd2 = DataConv.toComplexData(d2);
                        return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString().trim();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); 
                        }
                        
                        final char c = s2.charAt(s2.length() - 1);
                        if (c != 'i' && c != 'I') {
                            return addDoubles(d1, d2);
                        } else {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                        }
                    }
                    case Types.INSTANT:
                    case Types.DECIMAL: 
                        return addDecimals(d1, d2);
                    case Types.LIST: {
                        final DoubleData d1d = DataConv.toDoubleData(d1);
                        final ListData ld2 = DataConv.toListData(d2);
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(add(d1d, ld2.get(i), numeric, nullAsZero));
                        }
                        return new ListData(arr);
                    } 
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:
                        if (d1.toDouble().equals(0d)) {
                            return d2;
                        } else {
                            final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                            final double ld1 = d1.toDouble();
                            final double[] arr = vd.getDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = ld1 + arr[i];
                            }
                            return new DenseVectorData(arr);
                        }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                }
            case Types.DECIMAL:
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
                        return addDecimals(d1, d2);
                    case Types.COMPLEX: {
                        final ComplexData cd2 = DataConv.toComplexData(d2);
                        return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            return DecimalData.nonNullValueOf(d1.toDecimal().add(d2.toDecimal()));
                        } else {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                        }
                    }
                    case Types.LIST: {
                        final DecimalData d1d = DataConv.toDecimalData(d1);
                        final ListData ld2 = DataConv.toListData(d2);
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(add(d1d, ld2.get(i), numeric, nullAsZero));
                        }
                        return new ListData(arr);
                    } 
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:
                        if (BigDecimal.ZERO.equals(d1.toDecimal())) {
                            return d2;
                        } else {
                            final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                            final double ld1 = d1.toDouble();
                            final double[] arr = vd.getDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = ld1 + arr[i];
                            }
                            return new DenseVectorData(arr);
                        }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
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
                        final ComplexData cd1 = DataConv.toComplexData(d1);
                        return ComplexData.nonNullValueOf(cd1.getReal() + d2.toDouble(), cd1.getImaginary());
                    }
                    case Types.COMPLEX: {
                        final ComplexData cd1 = DataConv.toComplexData(d1);
                        final ComplexData cd2 = DataConv.toComplexData(d2);
                        return ComplexData.nonNullValueOf(cd1.getReal() + cd2.getReal(), cd1.getImaginary() + cd2.getImaginary());
                    }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            final ComplexData cd1 = DataConv.toComplexData(d1);
                            return ComplexData.nonNullValueOf(cd1.getReal() + d2.toDouble(), cd1.getImaginary());
                        } else {
                            final ComplexData cd1 = DataConv.toComplexData(d1);
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(cd1.getReal() + cd2.getReal(), cd1.getImaginary() + cd2.getImaginary());
                        }
                    }
                    case Types.LIST: {
                        final ListData ld2 = DataConv.toListData(d2);
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(add(d1, ld2.get(i), numeric, nullAsZero));
                        }
                        return new ListData(arr);
                    } 
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                        final int size = vd.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(add(d1, vd.get(i), numeric, true));
                        }
                        return new ListData(arr);
                    }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                }
            case Types.BINARY:
            case Types.STRING:
                if (numeric) {
                    if (!XComplexUtils.isComplexNumber(d1.toString())) {
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d1));
                    }
                    switch (d2.getType()) {
                        case Types.INT:
                        case Types.BOOLEAN:
                        case Types.LONG: 
                        case Types.DATE: 
                        case Types.TIME:
                        case Types.TIMESTAMP:
                        case Types.CALENDAR_TIME:
                        case Types.CALENDAR_TIMESTAMP: {
                            final String s1 = d1.toString();
                            if (XNumberUtils.isNumber(s1)) {
                                return XNumberUtils.isDigits(s1) ? addLongs(d1, d2) : addDoubles(d1, d2);
                            } else {
                                final ComplexData cd1 = new ComplexData(d1.toString());
                                return ComplexData.nonNullValueOf(cd1.getDouble() + d2.toDouble(), cd1.getImaginary());
                            }
                        }
                        case Types.FLOAT:
                        case Types.DOUBLE:
                        case Types.NUMERIC:{
                            final String s1 = d1.toString();
                            if (XNumberUtils.isNumber(s1)) {
                                return addDoubles(d1, d2);
                            } else {
                                final ComplexData cd1 = new ComplexData(d1.toString());
                                return ComplexData.nonNullValueOf(cd1.getDouble() + d2.toDouble(), cd1.getImaginary());
                            }
                        }
                        case Types.INSTANT:
                        case Types.DECIMAL: {
                            final String s1 = d1.toString();
                            if (XNumberUtils.isNumber(s1)) {
                                return addDecimals(d1, d2);
                            }
                            final ComplexData cd1 = new ComplexData(d1.toString());
                            return ComplexData.nonNullValueOf(cd1.getDouble() + d2.toDouble(), cd1.getImaginary());
                        }
                        case Types.COMPLEX: {
                            final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(cd1.getReal() + cd2.getReal(), cd1.getImaginary() + cd2.getImaginary());
                        }
                        case Types.BINARY:
                        case Types.STRING: {
                            final String s1 = d1.toString(), s2 = d2.toString();
                            if (!XComplexUtils.isComplexNumber(s2)) {
                                throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); 
                            } else if (XNumberUtils.isNumber(s1) && XNumberUtils.isNumber(s2)) {
                                return XNumberUtils.isDigits(s1) && XNumberUtils.isDigits(s2) ? addLongs(d1, d2) : addDoubles(d1, d2);
                            } else {
                                final ComplexData cd1 = DataConv.toComplexData(d1), cd2 = DataConv.toComplexData(d2);
                                return ComplexData.nonNullValueOf(cd1.getReal() + cd2.getReal(), cd1.getImaginary() + cd2.getImaginary());
                            }
                        }
                        case Types.LIST:
                            if (d1.isNumber()) {
                                final TypeData d1n = d1.isDigits() && d2.isDigits() ? addLongs(d1, d2) : addDoubles(d1, d2);
                                final ListData ld2 = DataConv.toListData(d2);
                                final int size = ld2.size();
                                final ArrayList<TypeData> arr = new ArrayList<>(size);
                                for (int i = 0; i < size; ++i) {
                                    arr.add(add(d1n, ld2.get(i), numeric, nullAsZero));
                                }
                                return new ListData(arr);
                            } else {
                                throw new UnsupportedDataOperationException("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                            }
                        case Types.DENSEVECTOR:
                        case Types.SPARSEVECTOR:
                            if (d1.isNumber()) {
                                final TypeData d1n = d1.isDigits() && d2.isDigits() ? addLongs(d1, d2) : addDoubles(d1, d2);
                                final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                                final double ld1 = d1n.toDouble();
                                final double[] arr = vd.getDoubles();
                                for (int i = 0, len = arr.length; i < len; ++i) {
                                    arr[i] = ld1 + arr[i];
                                }
                                return new DenseVectorData(arr);
                            } else {
                                throw new UnsupportedDataOperationException("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                            }
                        default:
                            throw new UnsupportedDataOperationException("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                    }
                } else {
                    return StringData.valueOf(d1.toString() + d2.toString());
                }
            case Types.DATE:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.LONG:
                    case Types.BOOLEAN:
                    case Types.DATE:
                        return numeric ? addLongs(d1, d2) : DateData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                        return numeric ? addDoubles(d1, d2) : DateData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.COMPLEX:
                        if (numeric) {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                        } else {
                            return DateData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                        }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); 
                        } else if (numeric) {
                            if (XNumberUtils.isNumber(s2)) {
                                return XNumberUtils.isDigits(s2) ? addLongs(d1, d2) : addDoubles(d1, d2);
                            } else {
                                final ComplexData cd2 = DataConv.toComplexData(d2);
                                return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                            }
                        } else {
                            return DateData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                        }
                    }
                    case Types.DECIMAL: 
                        return numeric
                            ? DecimalData.nonNullValueOf(d1.toDecimal().add(d2.toDecimal()))
                            : DateData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.TIME:
                    case Types.CALENDAR_TIME:
                        return numeric ? addLongs(d1, d2) : d1;
                    case Types.TIMESTAMP:
                        return numeric ? addLongs(d1, d2) : TimestampData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.CALENDAR_TIMESTAMP:
                        return numeric ? addLongs(d1, d2) : new CalendarTimestampData(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.INSTANT:
                        return numeric ? addDecimals(d1, d2) : new InstantData(d2.toInstant().plusMillis(d1.toLong()));
                    default:
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                }
            case Types.TIME:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.LONG:
                    case Types.BOOLEAN:
                    case Types.TIME:
                    case Types.CALENDAR_TIME:
                        return numeric ? addLongs(d1, d2) : new TimeData(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                        return numeric ? addDoubles(d1, d2) : new TimeData(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.COMPLEX:
                        if (numeric) {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                        } else {
                            return new TimeData(d1.toDecimal().add(d2.toDecimal()).longValue());
                        }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); 
                        } else if (numeric) {
                            if (XNumberUtils.isNumber(s2)) {
                                return addDecimals(d1, d2);
                            }
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                        } else {
                            return new TimeData(d1.toDecimal().add(d2.toDecimal()).longValue());
                        }
                    }
                    case Types.DECIMAL: 
                        return numeric ? addDecimals(d1, d2) : new TimeData(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.DATE:
                        return numeric ? addLongs(d1, d2) : d1;
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIMESTAMP:
                        return numeric ? addLongs(d1, d2) : DataConv.toTimestampData(addLongs(d1, d2));
                    case Types.INSTANT:
                        return numeric ? addDecimals(d1, d2) : new InstantData(d2.toInstant().plusMillis(d1.toLong()));
                    default:
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                }
            case Types.TIMESTAMP:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.LONG:
                    case Types.BOOLEAN:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP:
                        return numeric ? addLongs(d1, d2) : TimestampData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                        return numeric ? addDoubles(d1, d2) : TimestampData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.COMPLEX:
                        if (numeric) {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                        } else {
                            return TimestampData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                        }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); 
                        } else if (numeric) {
                            if (XNumberUtils.isNumber(s2)) {
                                return XNumberUtils.isDigits(s2) ? addLongs(d1, d2) : addDoubles(d1, d2);
                            } else {
                                final ComplexData cd2 = DataConv.toComplexData(d2);
                                return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                            }
                        } else {
                            return TimestampData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                        }
                    }
                    case Types.DECIMAL: 
                        return numeric ? addDecimals(d1, d2) : TimestampData.valueOf(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.INSTANT:
                        return numeric ? addDecimals(d1, d2) : new InstantData(d2.toInstant().plusMillis(d1.toLong()));
                    default:
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                }
            case Types.CALENDAR_TIME:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.LONG:
                    case Types.BOOLEAN:
                    case Types.TIME:
                    case Types.CALENDAR_TIME:
                        return numeric ? addLongs(d1, d2) : new CalendarTimeData(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                        return numeric ? addDoubles(d1, d2) : new CalendarTimeData(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.COMPLEX:
                        if (numeric) {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                        } else {
                            return new CalendarTimeData(d1.toDecimal().add(d2.toDecimal()).longValue());
                        }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); 
                        } else if (numeric) {
                            if (XNumberUtils.isNumber(s2)) {
                                return XNumberUtils.isDigits(s2) ? addLongs(d1, d2) : addDoubles(d1, d2);
                            } else {
                                final ComplexData cd2 = DataConv.toComplexData(d2);
                                return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                            }
                        } else {
                            return new CalendarTimeData(d1.toDecimal().add(d2.toDecimal()).longValue());
                        }
                    }
                    case Types.DECIMAL: 
                        return numeric ? addDecimals(d1, d2) : new CalendarTimeData(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.DATE:
                        return numeric ? LongData.nonNullValueOf(d1.toLong() + d2.toLong())  : d1;
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIMESTAMP:
                        return numeric ? addLongs(d1, d2) : DataConv.toCalendarTimestampData(addLongs(d1, d2));
                    case Types.INSTANT:
                        return numeric ? addDecimals(d1, d2) : new InstantData(d2.toInstant().plusMillis(d1.toLong()));
                    default:
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                }
            case Types.CALENDAR_TIMESTAMP:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.LONG:
                    case Types.BOOLEAN:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP:
                        return numeric ? addLongs(d1, d2) : new CalendarTimestampData(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                        return numeric ? addDoubles(d1, d2) : new CalendarTimestampData(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.COMPLEX:
                        if (numeric) {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                        } else {
                            return new CalendarTimestampData(d1.toDecimal().add(d2.toDecimal()).longValue());
                        }
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            return XNumberUtils.isDigits(s2) ? addLongs(d1, d2) : addDoubles(d1, d2);
                        } else {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() + cd2.getReal(), cd2.getImaginary());
                        }
                    }
                    case Types.DECIMAL: 
                        return numeric ? addDecimals(d1, d2) : new CalendarTimestampData(d1.toDecimal().add(d2.toDecimal()).longValue());
                    case Types.INSTANT:
                        return numeric ? addDecimals(d1, d2) : new InstantData(d2.toInstant().plusMillis(d1.toLong()));
                    default:
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                }
            case Types.INSTANT:
                if (numeric) {
                    throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                }
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.BOOLEAN:
                        return new InstantData(d1.toInstant().plusNanos(d2.toLong()));
                    case Types.BINARY:
                    case Types.STRING: if (!d2.isNumber()) {  throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2)); }
                    case Types.FLOAT:
                    case Types.DOUBLE: 
                    case Types.NUMERIC:
                    case Types.LONG:
                    case Types.DECIMAL:
                    case Types.COMPLEX:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP:
                        return new InstantData(d1.toInstant().plusMillis(d2.toLong()));
                    case Types.INSTANT:
                        return new InstantData(d1.toInstant().plusSeconds(d2.toInstant().getEpochSecond()).plusNanos(d2.toInstant().getNano()));
                    default:
                        throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d2));
                }
            case Types.LIST:
                switch (d2.getType()) {
                    case Types.SET:
                        throw new UnsupportedDataOperationException ("DATA_ADD: LIST ADD SET - " + d2.toString());
                    case Types.MAP:
                        throw new UnsupportedDataOperationException ("DATA_ADD: LIST ADD MAP - " + d2.toString());
                    case Types.LIST: 
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: 
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final ListData ld1 = DataConv.toListData(d1);
                            final ListData ld2 = DataConv.toListData(d2);
                            final int size = ld1.size();
                            final ArrayList<TypeData> arr = new ArrayList<>(size);
                            for (int i = 0; i < size; ++i ) {
                                arr.add(add(ld1.get(i), ld2.get(i), numeric, nullAsZero));
                            }
                            return new ListData(arr);
                        }
                    default: {
                        final ListData ld1 = DataConv.toListData(d1);
                        final int size = ld1.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i ) {
                            arr.add(add(ld1.get(i), d2, numeric, nullAsZero));
                        }
                        return new ListData(arr);
                    }
                }
            case Types.SET: 
                throw new UnsupportedDataOperationException ("DATA_ADD: SET ADD IS INVALID, USE APPEND");
            case Types.MAP: 
                throw new UnsupportedDataOperationException ("DATA_ADD: MAP ADD IS INVALID, USE APPEND");
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR:
                switch (d2.getType()) {
                    case Types.SET:
                        throw new UnsupportedDataOperationException ("DATA_ADD: VECTOR ADD SET - " + d2.toString());
                    case Types.MAP:
                        throw new UnsupportedDataOperationException ("DATA_ADD: VECTOR ADD MAP - " + d2.toString());
                    case Types.LIST:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final ListData ld2 = DataConv.toListData(d2);
                            final int size = vd1.size();
                            final double[] result = new double[size];
                            Double d;
                            for (int i = 0; i < size; ++i ) {
                                d = add(vd1.get(i), ld2.get(i), numeric, nullAsZero).toDouble();
                                result[i] = d != null ? d.doubleValue() : 0d;
                            }
                            return new DenseVectorData(result);
                        }
                    case Types.DENSEVECTOR: 
                    case Types.SPARSEVECTOR:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_ADD: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final DenseVectorData vd2 = DataConv.toDenseVectorData(d2);
                            final int size = vd1.size();
                            final double[] arr = vd1.getDoubles(), arr2 = vd2.getUnsafeDoubles();
                            for (int i = 0; i < size; ++i ) {
                                arr[i] = arr[i] + arr2[i]; 
                            }
                            return new DenseVectorData(arr);
                        }
                    default: {
                        final DenseVectorData ld1 = DataConv.toDenseVectorData(d1);
                        final int size = ld1.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i ) {
                            arr.add(add(ld1.get(i), d2, true, true));
                        }
                        return new ListData(arr);
                    }
                }
            default:
                throw new UnsupportedDataOperationException ("DATA_ADD: " + XCalcException.getNonNumericException(d1));
        }
    }
    
    private static TypeData addLongs(final TypeData d1, final TypeData d2) throws EngineException {
        final long val1 = d1.toLong(), val2 = d2.toLong();
        final long result = val1 + val2;
        if ((result >= 0l) != (val1 >= 0l) && (result >= 0l) != (val2 >=0l)) {
            //overflow
            return DecimalData.nonNullValueOf(BigDecimal.valueOf(val1).add(BigDecimal.valueOf(val2)));
        } else {
            return LongData.nonNullValueOf(result);
        }
    }
    
    private static TypeData addDoubles(final TypeData d1, final TypeData d2) throws EngineException {
        final double val1 = d1.toDouble(), val2 = d2.toDouble();
        final double result = val1 + val2;
        if ((result >= 0d) != (val1 >= 0d) && (result >= 0d) != (val2 >=0d)) {
            //overflow
            return DecimalData.nonNullValueOf(d1.toDecimal().add(d2.toDecimal()));
        } else {
            return DoubleData.nonNullValueOf(result);
        }
    }
    
    private static TypeData addDecimals(final TypeData d1, final TypeData d2) throws EngineException {
        return DecimalData.nonNullValueOf(d1.toDecimal().add(d2.toDecimal()));
    }
}
