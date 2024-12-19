package com.exceeddata.ac.common.util.calc;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import com.exceeddata.ac.common.data.typedata.TimeData;
import com.exceeddata.ac.common.data.typedata.TimestampData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.UnsupportedDataOperationException;
import com.exceeddata.ac.common.util.XComplexUtils;
import com.exceeddata.ac.common.util.XNumberUtils;

public final class XCalcSubtract {
    private XCalcSubtract() {}
    
    public static TypeData subtract(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        if (d1.isNull()) {
            return nullAsZero ? XCalcGeneral.negate(d2) : d1;
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
                        return subtractLongs(d1, d2);
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: 
                        return subtractDoubles(d1, d2);
                    case Types.COMPLEX: {
                        final ComplexData cd2 = (ComplexData) d2;
                        return ComplexData.nonNullValueOf(d1.toDouble() - cd2.getReal(), cd2.getImaginary() * -1d);
                    }
                    case Types.BINARY:
                    case Types.STRING:  {
                        final String s2 = d2.toString().trim();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); 
                        }
                        
                        final char c = s2.charAt(s2.length() - 1);
                        if (c != 'i' && c != 'I') {
                            return XNumberUtils.isDigits(s2) ? subtractLongs(d1, d2) : subtractDoubles(d1, d2);
                        } else {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() - cd2.getReal(), cd2.getImaginary() * -1d);
                        }
                    }
                    case Types.INSTANT:
                    case Types.DECIMAL: 
                        return subtractDecimals(d1, d2);
                    case Types.LIST: {
                        final LongData d1d = DataConv.toLongData(d1);
                        final ListData ld2 = (ListData) d2;
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(subtract(d1d, ld2.get(i), nullAsZero));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final double ld1 = d1.toDouble();
                        final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = ld1 - arr[i];
                        }
                        return new DenseVectorData(arr);
                    }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
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
                        return subtractDoubles(d1, d2);
                    case Types.COMPLEX: {
                        final ComplexData cd2 = (ComplexData) d2;
                        return ComplexData.nonNullValueOf(d1.toDouble() - cd2.getReal(), cd2.getImaginary() * -1d);
                    }
                    case Types.BINARY:
                    case Types.STRING:  {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            return subtractDoubles(d1, d2);
                        } else {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() - cd2.getReal(), cd2.getImaginary() * -1d);
                        }
                    }
                    case Types.INSTANT:
                    case Types.DECIMAL:
                        return DecimalData.nonNullValueOf(d1.toDecimal().subtract(d2.toDecimal()));
                    case Types.LIST: {
                        final DoubleData d1d = DataConv.toDoubleData(d1);
                        final ListData ld2 = (ListData) d2;
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(subtract(d1d, ld2.get(i), nullAsZero));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final double ld1 = d1.toDouble();
                        final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = ld1 - arr[i];
                        }
                        return new DenseVectorData(arr);
                    }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                    }
            case Types.DECIMAL:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.LONG:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: 
                    case Types.BOOLEAN:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME: 
                    case Types.CALENDAR_TIMESTAMP:  
                    case Types.INSTANT: 
                    case Types.DECIMAL:
                        return subtractDecimals(d1, d2);
                    case Types.COMPLEX: {
                        final ComplexData cd2 = (ComplexData) d2;
                        return ComplexData.nonNullValueOf(d1.toDouble() - cd2.getReal(), cd2.getImaginary() * -1d);
                    }
                    case Types.BINARY:
                    case Types.STRING:  {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            return subtractDecimals(d1, d2);
                        } else {
                            final ComplexData cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(d1.toDouble() - cd2.getReal(), cd2.getImaginary() * -1d);
                        }
                    }
                    case Types.LIST: {
                        final DecimalData d1d = DataConv.toDecimalData(d1);
                        final ListData ld2 = (ListData) d2;
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(subtract(d1d, ld2.get(i), nullAsZero));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final double ld1 = d1.toDouble();
                        final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = ld1 - arr[i];
                        }
                        return new DenseVectorData(arr);
                    }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                }

            case Types.COMPLEX:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.LONG:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: 
                    case Types.BOOLEAN:
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME: 
                    case Types.CALENDAR_TIMESTAMP:  
                    case Types.INSTANT: 
                    case Types.DECIMAL: {
                        final ComplexData cd1 = (ComplexData) d1;
                        return ComplexData.nonNullValueOf(cd1.getReal() - d2.toDouble(), cd1.getImaginary());
                    }
                    case Types.COMPLEX: {
                        final ComplexData cd1 = (ComplexData) d1, cd2 = (ComplexData) d2;
                        return ComplexData.nonNullValueOf(cd1.getReal() - cd2.getReal(), cd1.getImaginary() - cd2.getImaginary());
                    }
                    case Types.BINARY:
                    case Types.STRING:  {
                        final String s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s2)) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); 
                        } else if (XNumberUtils.isNumber(s2)) {
                            final ComplexData cd1 = (ComplexData) d1;
                            return ComplexData.nonNullValueOf(cd1.getReal() - d2.toDouble(), cd1.getImaginary());
                        } else {
                            final ComplexData cd1 = (ComplexData) d1, cd2 = DataConv.toComplexData(d2);
                            return ComplexData.nonNullValueOf(cd1.getReal() - cd2.getReal(), cd1.getImaginary() - cd2.getImaginary());
                        }
                    }
                    case Types.LIST: {
                        final ListData ld2 = (ListData) d2;
                        final int size = ld2.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            arr.add(subtract(d1, ld2.get(i), nullAsZero));
                        }
                        return new ListData(arr);
                    }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final double ld1 = d1.toDouble();
                        final DenseVectorData vd = DataConv.toDenseVectorData(d2);
                        final double[] arr = vd.getDoubles();
                        for (int i = 0, len = arr.length; i < len; ++i) {
                            arr[i] = ld1 - arr[i];
                        }
                        return new DenseVectorData(arr);
                    }
                    default:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                }
            case Types.DATE:
                switch (d2.getType()) {
                    case Types.BINARY:
                    case Types.STRING: if (!d2.isNumber()) { throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); }
                    case Types.INT:
                    case Types.LONG: 
                    case Types.BOOLEAN:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: 
                    case Types.DECIMAL: 
                    case Types.COMPLEX: {
                        final long val1 = d1.toLong(), val2 = d2.toLong();
                        final long result = val1 - val2;
                        if ((val1 >= 0l) != (val2 >= 0l) && (result >= 0l) != (val1 >=0l)) {
                            //overflow
                            return DateData.valueOf(BigDecimal.valueOf(val1).subtract(BigDecimal.valueOf(val2)).longValue());
                        } else {
                            return DateData.valueOf(result);
                        }
                    }
                    case Types.DATE:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIMESTAMP:  
                    case Types.INSTANT: 
                        return subtractLongs(d1, d2);
                    default:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                }
            case Types.TIME:
                switch (d2.getType()) {
                    case Types.BINARY:
                    case Types.STRING: if (!d2.isNumber()) { throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); }
                    case Types.INT:
                    case Types.LONG: 
                    case Types.BOOLEAN:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: 
                    case Types.DECIMAL: 
                    case Types.COMPLEX: {
                        final long val1 = d1.toLong(), val2 = d2.toLong();
                        final long result = val1 - val2;
                        if ((val1 >= 0l) != (val2 >= 0l) && (result >= 0l) != (val1 >=0l)) {
                            //overflow
                            return new TimeData(BigDecimal.valueOf(val1).subtract(BigDecimal.valueOf(val2)).longValue());
                        } else {
                            return new TimeData(result);
                        }
                    }
                    case Types.TIME:
                    case Types.CALENDAR_TIME: 
                        return subtractLongs(d1, d2);
                    default:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                }
            case Types.TIMESTAMP:
                switch (d2.getType()) {
                    case Types.BINARY:
                    case Types.STRING: if (!d2.isNumber()) { throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); }
                    case Types.INT:
                    case Types.LONG:
                    case Types.BOOLEAN:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: 
                    case Types.DECIMAL: 
                    case Types.COMPLEX: {
                        final long val1 = d1.toLong(), val2 = d2.toLong();
                        final long result = val1 - val2;
                        if ((val1 >= 0l) != (val2 >= 0l) && (result >= 0l) != (val1 >=0l)) {
                            //overflow
                            return TimestampData.valueOf(BigDecimal.valueOf(val1).subtract(BigDecimal.valueOf(val2)).longValue());
                        } else {
                            return TimestampData.valueOf(result);
                        }
                    }
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: 
                    case Types.INSTANT: 
                        return subtractLongs(d1, d2);
                    default:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                }
            case Types.CALENDAR_TIME:
                switch (d2.getType()) {
                    case Types.BINARY:
                    case Types.STRING: if (!d2.isNumber()) { throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); }
                    case Types.INT:
                    case Types.BOOLEAN:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: 
                    case Types.DECIMAL:
                    case Types.LONG: 
                    case Types.COMPLEX: {
                        final Calendar ts = DataConv.toCalendarTimeData(d1).getCalendar();
                        final long val1 = d1.toLong(), val2 = d2.toLong();
                        final long result = val1 - val2, millis;
                        if ((val1 >= 0l) != (val2 >= 0l) && (result >= 0l) != (val1 >=0l)) {
                            //overflow
                            millis = BigDecimal.valueOf(val1).subtract(BigDecimal.valueOf(val2)).longValue();
                        } else {
                            millis = result;
                        }
                        final GregorianCalendar gcal = new GregorianCalendar();
                        gcal.setTimeInMillis(millis);
                        gcal.setTimeZone(ts.getTimeZone());
                        return new CalendarTimeData(gcal);
                    }
                    case Types.TIME:
                    case Types.CALENDAR_TIME: 
                        return subtractLongs(d1, d2);
                    default:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                }
            case Types.CALENDAR_TIMESTAMP:
                switch (d2.getType()) {
                    case Types.BINARY:
                    case Types.STRING: if (!d2.isNumber()) { throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); }
                    case Types.INT:
                    case Types.LONG:
                    case Types.BOOLEAN:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: 
                    case Types.DECIMAL:
                    case Types.TIME:
                    case Types.CALENDAR_TIME: 
                    case Types.COMPLEX: {
                        final Calendar ts = DataConv.toCalendarTimestampData(d1).getCalendar();
                        final long val1 = d1.toLong(), val2 = d2.toLong();
                        final long result = val1 - val2, millis;
                        if ((val1 >= 0l) != (val2 >= 0l) && (result >= 0l) != (val1 >=0l)) {
                            //overflow
                            millis = BigDecimal.valueOf(val1).subtract(BigDecimal.valueOf(val2)).longValue();
                        } else {
                            millis = result;
                        }
                        final GregorianCalendar gcal = new GregorianCalendar();
                        gcal.setTimeInMillis(millis);
                        gcal.setTimeZone(ts.getTimeZone());
                        return new CalendarTimestampData(gcal);
                    }
                    case Types.DATE:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIMESTAMP: 
                    case Types.INSTANT:
                        return subtractLongs(d1, d2);
                    default:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                }
            case Types.INSTANT:
                switch (d2.getType()) {
                    case Types.INT:
                    case Types.BOOLEAN: 
                        return new InstantData(d1.toInstant().minusNanos(d2.toLong()));
                    case Types.BINARY:
                    case Types.STRING: if (!d2.isNumber()) { throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); }
                    case Types.LONG:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC:  
                    case Types.DECIMAL:
                    case Types.COMPLEX:
                    case Types.TIME:
                    case Types.CALENDAR_TIME:
                        return new InstantData(d1.toInstant().minusMillis(d2.toLong()));
                    case Types.DATE:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIMESTAMP: 
                        return subtractDecimals(d1, d2);
                    case Types.INSTANT:
                        return LongData.nonNullValueOf(Duration.between(d2.toInstant(), d1.toInstant()).toNanos());
                    default:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                }
            case Types.STRING:
                switch (d2.getType()) {
                    case Types.BINARY:
                    case Types.STRING: {
                        final String s1 = d1.toString(), s2 = d2.toString();
                        if (!XComplexUtils.isComplexNumber(s1)) { 
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d1)); 
                        } else if (!XComplexUtils.isComplexNumber(s2)) { 
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2)); 
                        } else {
                            if (XNumberUtils.isNumber(s1) && XNumberUtils.isNumber(s2)) {
                                return XNumberUtils.isDigits(s1) && XNumberUtils.isDigits(s2) ? subtractLongs(d1, d2) : subtractDoubles(d1, d2);
                            } else {
                                final ComplexData cd1 = new ComplexData(s1);
                                return ComplexData.nonNullValueOf(cd1.getDouble() - d2.toDouble(), cd1.getImaginary() * -1d);
                            }
                        }
                    }
                    case Types.INT:
                    case Types.BOOLEAN:
                    case Types.LONG: 
                        if (!XComplexUtils.isComplexNumber(d1.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d1));
                        } else {
                            final String s1 = d1.toString();
                            if (XNumberUtils.isNumber(s1)) {
                                return XNumberUtils.isDigits(s1) ? subtractLongs(d1, d2) : subtractDoubles(d1, d2);
                            } else {
                                final ComplexData cd1 = new ComplexData(s1);
                                return ComplexData.nonNullValueOf(cd1.getDouble() - d2.toDouble(), cd1.getImaginary() * -1d);
                            }
                        }
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.NUMERIC: 
                        if (!XComplexUtils.isComplexNumber(d1.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d1));
                        } else {
                            final String s1 = d1.toString();
                            if (XNumberUtils.isNumber(s1)) {
                                return subtractDoubles(d1, d2);
                            } else {
                                final ComplexData cd1 = new ComplexData(s1);
                                return ComplexData.nonNullValueOf(cd1.getDouble() - d2.toDouble(), cd1.getImaginary() * -1d);
                            }
                        }
                    case Types.DECIMAL:
                        if (!XComplexUtils.isComplexNumber(d1.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d1));
                        } else {
                            final String s1 = d1.toString();
                            if (XNumberUtils.isNumber(s1)) {
                                return subtractDecimals(d1, d2);
                            }
                            final ComplexData cd1 = new ComplexData(s1);
                            return ComplexData.nonNullValueOf(cd1.getDouble() - d2.toDouble(), cd1.getImaginary() * -1d);
                        }
                    case Types.COMPLEX:
                        if (!XComplexUtils.isComplexNumber(d1.toString())) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d1));
                        } else {
                            final ComplexData cd1 = new ComplexData(d1.toString()), cd2 = (ComplexData) d2;
                            return ComplexData.nonNullValueOf(cd1.getReal() - cd2.getReal(), cd1.getImaginary() - cd2.getImaginary());
                        }
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                    case Types.CALENDAR_TIME:
                    case Types.CALENDAR_TIMESTAMP: {
                        final String s1 = d1.toString();
                        if (XNumberUtils.isNumber(s1)) {
                            return XNumberUtils.isDigits(s1) ? subtractLongs(d1, d2) : subtractDoubles(d1, d2);
                        }
                        final CalendarTimestampData td = DataConv.toCalendarTimestampData(d1);
                        if (td.isEmpty()) {
                            throw new UnsupportedDataOperationException("DATA_SUBTRACT: not convertible - " + d1.toString());
                        }
                        return subtractDecimals(td, d2);
                    }
                    case Types.INSTANT: {
                        final String s1 = d1.toString();
                        if (XNumberUtils.isNumber(s1)) {
                            return subtractDecimals(d1, d2);
                        }

                        final InstantData td = DataConv.toInstantData(d1);
                        if (td.isEmpty()) {
                            throw new UnsupportedDataOperationException("DATA_SUBTRACT: not convertible - " + d1.toString());
                        }
                        return subtractDecimals(td, d2);
                    }
                    case Types.LIST:
                        if (d1.isNumber()) {
                            final TypeData d1n = d1.isDigits() && d2.isDigits() ? subtractLongs(d1, d2) : subtractDoubles(d1, d2);
                            final ListData ld2 = (ListData) d2;
                            final int size = ld2.size();
                            final ArrayList<TypeData> arr = new ArrayList<>(size);
                            for (int i = 0; i < size; ++i) {
                                arr.add(subtract(d1n, ld2.get(i), nullAsZero));
                            }
                            return new ListData(arr);
                        } else {
                            throw new UnsupportedDataOperationException("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                        }
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:
                        if (d1.isNumber()) {
                            final TypeData d1n = d1.isDigits() && d2.isDigits() ? subtractLongs(d1, d2) : subtractDoubles(d1, d2);
                            final DenseVectorData vd2 = DataConv.toDenseVectorData(d2);
                            final double ld1 = d1n.toDouble();
                            final double[] arr = vd2.getDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i) {
                                arr[i] = ld1 - arr[i];
                            }
                            return new DenseVectorData(arr);
                        } else {
                            throw new UnsupportedDataOperationException("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                        }
                    default:
                        throw new UnsupportedDataOperationException("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d2));
                }
            case Types.LIST:
                switch (d2.getType()) {
                    case Types.SET:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: LIST SUBTRACT SET - " + d2.toString());
                    case Types.MAP:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: LIST SUBTRACT MAP - " + d2.toString());
                    case Types.LIST: 
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: 
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final ListData ld1 = (ListData) d1;
                            final ListData ld2 = DataConv.toListData(d2);
                            final int size = ld1.size();
                            final ArrayList<TypeData> arr = new ArrayList<>(size);
                            for (int i = 0; i < size; ++i ) {
                                arr.add(subtract(ld1.get(i), ld2.get(i), nullAsZero));
                            }
                            return new ListData(arr);
                        }
                    default: {
                        final ListData ld1 = (ListData) d1;
                        final int size = ld1.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i ) {
                            arr.add(subtract(ld1.get(i), d2, nullAsZero));
                        }
                        return new ListData(arr);
                    }
                }
            case Types.SET: 
                throw new UnsupportedDataOperationException ("DATA_SUBTRACT: SET SUBTRACT IS INVALID, USE DEDUCT");
            case Types.MAP: 
                throw new UnsupportedDataOperationException ("DATA_SUBTRACT: MAP SUBTRACT IS INVALID, USE DEDUCT");
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR:
                switch (d2.getType()) {
                    case Types.SET:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: VECTOR SUBTRACT SET - " + d2.toString());
                    case Types.MAP:
                        throw new UnsupportedDataOperationException ("DATA_SUBTRACT: VECTOR SUBTRACT MAP - " + d2.toString());
                    case Types.LIST:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final ListData ld2 = (ListData) d2;
                            final int size = vd1.size();
                            final double[] result = new double[size];
                            Double d;
                            for (int i = 0; i < size; ++i ) {
                                d = subtract(vd1.get(i), ld2.get(i), nullAsZero).toDouble();
                                result[i] = d != null ? d.doubleValue() : 0d;
                            }
                            return new DenseVectorData(result);
                        }
                    case Types.DENSEVECTOR: 
                    case Types.SPARSEVECTOR:
                        if (d1.size() != d2.size()) {
                            throw new UnsupportedDataOperationException ("DATA_SUBTRACT: SIZE MISMATCH - " + d2.toString());
                        } else {
                            final DenseVectorData vd1 = DataConv.toDenseVectorData(d1);
                            final DenseVectorData vd2 = DataConv.toDenseVectorData(d2);
                            final double[] arr = vd1.getDoubles(), arr2 = vd2.getUnsafeDoubles();
                            for (int i = 0, len = arr.length; i < len; ++i ) {
                                arr[i] = arr[i] - arr2[i];
                            }
                            return new DenseVectorData(arr);
                        }
                    default: {
                        final DenseVectorData ld1 = DataConv.toDenseVectorData(d1);
                        final int size = ld1.size();
                        final ArrayList<TypeData> arr = new ArrayList<>(size);
                        for (int i = 0; i < size; ++i ) {
                            arr.add(subtract(ld1.get(i), d2, true));
                        }
                        return new ListData(arr);
                    }
                }
            default:
                throw new UnsupportedDataOperationException ("DATA_SUBTRACT: " + XCalcException.getNonNumericException(d1));
        }
    }
    
    private static TypeData subtractLongs(final TypeData d1, final TypeData d2) throws EngineException {
        final long val1 = d1.toLong(), val2 = d2.toLong();
        final long result = val1 - val2;
        if ((val1 >= 0l) != (val2 >= 0l) && (result >= 0l) != (val1 >=0l)) {
            //overflow
            return DecimalData.nonNullValueOf(BigDecimal.valueOf(val1).subtract(BigDecimal.valueOf(val2)));
        } else {
            return LongData.nonNullValueOf(result);
        }
    }
    
    private static TypeData subtractDoubles(final TypeData d1, final TypeData d2) throws EngineException {
        final double val1 = d1.toDouble(), val2 = d2.toDouble();
        final double result = val1 - val2;
        if ((val1 >= 0d) != (val2 >= 0d) && (result >= 0d) != (val1 >=0d)) {
            //overflow
            return DecimalData.nonNullValueOf(d1.toDecimal().subtract(d2.toDecimal()));
        } else {
            return DoubleData.nonNullValueOf(result);
        }
    }

    private static TypeData subtractDecimals(final TypeData d1, final TypeData d2) throws EngineException {
        return DecimalData.nonNullValueOf(d1.toDecimal().subtract(d2.toDecimal()));
    }
}
