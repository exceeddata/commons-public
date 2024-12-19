package com.exceeddata.ac.common.data.typedata;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.util.XComplexUtils;
import com.exceeddata.ac.common.util.XNumberUtils;

public final class DataCompare {
    private DataCompare() {}
    
    public static int binaryCompareTo(final BinaryData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.value == null ? 0 : 1;
        } else if (x.value == null) {
            return -1;
        }
        switch (w.getType()) {
            case Types.BINARY:
                return ByteBuffer.wrap(x.value).compareTo(ByteBuffer.wrap(((BinaryData) w).getBytes()));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = binaryCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default:
                return ByteBuffer.wrap(x.value).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
        }
    }
    
    public static int booleanCompareTo(final BooleanData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            case Types.INT: return Integer.compare(x.value ? 1 : 0, ((IntData) w).value);
            case Types.LONG: return Long.compare(x.value ? 1l : 0l, ((LongData) w).value);
            case Types.FLOAT: return Float.compare(x.value ? 1f : 0f, ((FloatData) w).value);
            case Types.DOUBLE: return Double.compare(x.value ? 1d : 0d, ((DoubleData) w).value);
            case Types.NUMERIC: return Double.compare(x.value ? 1d : 0d, ((NumericData) w).getDouble());
            case Types.COMPLEX: return Double.compare(x.value ? 1d : 0d, ((ComplexData) w).getDouble());
            case Types.DECIMAL: return BigDecimal.valueOf(x.value ? 1l : 0l).subtract(((DecimalData) w).value).signum();
            case Types.BOOLEAN: return Boolean.compare(x.value, ((BooleanData) w).value);
            case Types.TIME: return Long.compare(x.value ? 1l : 0l, ((TimeData) w).value);
            case Types.TIMESTAMP: return Long.compare(x.value ? 1l : 0l, ((TimestampData) w).value);
            case Types.DATE: return Long.compare(x.value ? 1l : 0l, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Long.compare(x.value ? 1l : 0l, ((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP: return Long.compare(x.value ? 1l : 0l, ((CalendarTimeData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Long.compare(x.value ? 1l : 0l, i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = booleanCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default: {
                final String s = w.toString();
                switch(s.toLowerCase()) {
                    case "true":
                    case "yes":
                    case "1": 
                    case "是":
                        return x.value ? 0 : 1;
                    case "false":
                    case "no":
                    case "0": 
                    case "否":
                        return !x.value ? 0 : -1;
                    default: 
                        if (XNumberUtils.isNumber(s)) {
                            return x.value ? BigDecimal.ONE.compareTo(new BigDecimal(s)) : BigDecimal.ZERO.compareTo(new BigDecimal(s));
                        } else {
                            return String.valueOf(x.value).compareTo(s);
                        }
                }
            }
        }
    }
    
    public static int calendarTimeCompareTo(final CalendarTimeData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            /*
             * Apply offsets to local time
             */
            case Types.INT: return Long.compare(x.value, ((IntData) w).getInteger());
            case Types.LONG: return Long.compare(x.value, ((LongData) w).getLong());
            case Types.FLOAT: return Double.compare(x.value, ((FloatData) w).getFloat());
            case Types.DOUBLE: return Double.compare(x.value, ((DoubleData) w).getDouble());
            case Types.NUMERIC: return Double.compare(x.value, ((NumericData) w).getDouble());
            case Types.DECIMAL: return BigDecimal.valueOf(x.value).subtract(((DecimalData) w).getDecimal()).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare(x.value, data.real);
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return Long.compare(x.value, ((BooleanData) w).getBoolean() ? 1L : 0L);
            /*
             * All date/time are the same offsets to UTC, so no need to adjust.
             */
            case Types.TIME: return Long.compare(x.value, ((TimeData) w).value);
            case Types.TIMESTAMP: return Long.compare(x.value, ((TimestampData) w).value);
            case Types.DATE: return Long.compare(x.value, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Long.compare(x.value, ((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP: return Long.compare(x.value, ((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Long.compare(x.value, i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP: 
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = calendarTimeCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default:
                return x.toString().compareTo(w.toString());
        }
    }
    
    public static int calendarTimestampCompareTo(final CalendarTimestampData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            /*
             * Apply offsets to local time
             */
            case Types.INT: return Long.compare(x.value, ((IntData) w).getInteger());
            case Types.LONG: return Long.compare(x.value, ((LongData) w).getLong());
            case Types.FLOAT: return Double.compare(x.value, ((FloatData) w).getFloat());
            case Types.DOUBLE: return Double.compare(x.value, ((DoubleData) w).getDouble());
            case Types.NUMERIC: return Double.compare(x.value, ((NumericData) w).getDouble());
            case Types.DECIMAL: return BigDecimal.valueOf(x.value).subtract(((DecimalData) w).getDecimal()).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare(x.value, data.real);
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return Long.compare(x.value, ((BooleanData) w).getBoolean() ? 1L : 0L);
            /*
             * All date/time are the same offsets to UTC, so no need to adjust.
             */
            case Types.TIME: return Long.compare(x.value, ((TimeData) w).value);
            case Types.TIMESTAMP: return Long.compare(x.value, ((TimestampData) w).value);
            case Types.DATE: return Long.compare(x.value, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Long.compare(x.value, ((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP: return Long.compare(x.value, ((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Long.compare(x.value, i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP: 
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = calendarTimestampCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default:
                return x.toString().compareTo(w.toString());
        }
    }
    
    public static int complexCompareTo(final ComplexData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        
        int compare = 0;
        switch (w.getType()) {
            case Types.INT:
                if ((compare = Double.compare(x.real, ((IntData) w).value)) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.LONG:
                if ((compare = Double.compare(x.real, ((LongData) w).value)) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.FLOAT:
                if ((compare = Double.compare(x.real, Double.valueOf(w.toString()))) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.DOUBLE:
                if ((compare = Double.compare(x.real, ((DoubleData) w).value)) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.NUMERIC:
                if ((compare = Double.compare(x.real, ((NumericData) w).getDouble())) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.DECIMAL:
                if ((compare = new BigDecimal(String.valueOf(x.real)).subtract(((DecimalData) w).value).signum()) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.BOOLEAN: 
                if ((compare = Double.compare(x.real, ((BooleanData) w).value? 1d : 0d)) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.COMPLEX: {
                final ComplexData complex = (ComplexData) w;
                if ((compare = Double.compare(x.real, complex.real)) == 0) {
                    return Double.compare(x.imag, complex.imag);
                } else {
                    return compare;
                }
            }
            /*
             * Adjust for local time offsets
             */
            case Types.TIME:
                if ((compare = Double.compare(x.real, ((TimeData) w).value)) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.TIMESTAMP:
                if ((compare = Double.compare(x.real, ((TimestampData) w).value)) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.DATE:
                if ((compare = Double.compare(x.real, ((DateData) w).value)) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.CALENDAR_TIME:
                if ((compare = Double.compare(x.real, ((CalendarTimeData) w).value)) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.CALENDAR_TIMESTAMP:
                if ((compare = Double.compare(x.real, ((CalendarTimestampData) w).value)) == 0) {
                    return Double.compare(x.imag, 0d);
                } else {
                    return compare;
                }
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                compare = Double.compare(x.real, i.millis);
                if (compare == 0) {
                    return Double.compare(x.imag, i.nanos / 1000000);
                } else {
                    return compare;
                }
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.nonNullToString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    compare = complexCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default: {
                final String s= w.toString();
                final ComplexData complex = new ComplexData(s);
                if (complex.isnull) { //not number
                    return x.nonNullToString().compareTo(s);
                } else if ((compare = Double.compare(x.real, complex.real)) == 0) {
                    return Double.compare(x.imag, complex.imag);
                } else {
                    return compare;
                }
            }
        }
    }
    
    public static int dateCompareTo(final DateData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            /*
             * Apply offsets to local time
             */
            case Types.INT: return Long.compare(x.value, ((IntData) w).getInteger());
            case Types.LONG: return Long.compare(x.value, ((LongData) w).getLong());
            case Types.FLOAT: return Double.compare(x.value, Double.valueOf(w.toString()));
            case Types.DOUBLE: return Double.compare(x.value, ((DoubleData) w).getDouble());
            case Types.NUMERIC: return Double.compare(x.value, ((NumericData) w).getDouble());
            case Types.DECIMAL: return BigDecimal.valueOf(x.value).subtract(((DecimalData) w).getDecimal()).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare(x.value, data.real);
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return Long.compare(x.value, ((BooleanData) w).getBoolean() ? 1L : 0L);
            /*
             * All date/time are the same offsets to UTC, so no need to adjust.
             */
            case Types.TIME: return Long.compare(x.value, ((TimeData) w).value);
            case Types.TIMESTAMP: return Long.compare(x.value, ((TimestampData) w).value);
            case Types.DATE: return Long.compare(x.value, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Long.compare(x.value, ((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP: return Long.compare(x.value, ((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Long.compare(x.value, i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP: 
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = dateCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default:
                return x.toString().compareTo(w.toString());
        }
    }
    
    public static int decimalCompareTo(final DecimalData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.value == null ? 0 : 1;
        } else if (x.value == null) {
            return -1;
        }
        switch (w.getType()) {
            case Types.INT: return x.value.subtract(BigDecimal.valueOf(((IntData) w).value)).signum();
            case Types.LONG: return x.value.subtract(BigDecimal.valueOf(((LongData) w).value)).signum();
            case Types.FLOAT:
            case Types.DOUBLE: 
            case Types.NUMERIC: return x.value.subtract(new BigDecimal(w.toString())).signum();
            case Types.DECIMAL: return x.value.subtract(((DecimalData) w).value).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = x.value.subtract(new BigDecimal(String.valueOf(data.real))).signum();
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return ((BooleanData) w).getBoolean()? x.value.subtract(BigDecimal.ONE).signum() : x.value.signum();
            /*
             * Adjust for local time offsets
             */
            case Types.TIME: return x.value.subtract(BigDecimal.valueOf(((TimeData) w).value)).signum();
            case Types.TIMESTAMP: return x.value.subtract(BigDecimal.valueOf(((TimestampData) w).value)).signum();
            case Types.DATE: return x.value.subtract(BigDecimal.valueOf(((DateData) w).value)).signum();
            case Types.CALENDAR_TIME: return x.value.subtract(BigDecimal.valueOf(((CalendarTimeData) w).value)).signum();
            case Types.CALENDAR_TIMESTAMP: return x.value.subtract(BigDecimal.valueOf(((CalendarTimestampData) w).value)).signum();
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = x.value.subtract(BigDecimal.valueOf(i.millis)).signum();
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR:
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = decimalCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default: {
                final String s = w.toString();
                if (XNumberUtils.isNumber(s)) {
                    return x.value.subtract(new BigDecimal(s)).signum();
                } else {
                    return x.toString().compareTo(s);
                }
            }
        }
    }
    
    public static int denseVectorDataCompareTo(final DenseVectorData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.vsize == 0 ? 0 : 1;
        }
        switch (w.getType()) {
            case Types.INT:
            case Types.LONG:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC: 
            case Types.DECIMAL:
            case Types.COMPLEX:
            case Types.BOOLEAN:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.DATE:
            case Types.CALENDAR_TIME: 
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
            case Types.BINARY: 
            case Types.STRING:
                if (x.vsize == 0) {
                    return -1;
                } else {
                    final int compare = x.first().compareTo(w);
                    return compare != 0 ? compare : 1;
                }
            case Types.LIST:
            case Types.SET: {
                final int wsize = w.size();
                if (wsize == 0) {
                    return x.vsize == 0 ? 0 : 1;
                }
                
                final Iterator<DoubleData> iterator = x.iterator();
                final Iterator<? extends TypeData> wIterator = w.iterator();
                int result = 0;
                
                while (iterator.hasNext() && wIterator.hasNext()) {
                    if ((result = iterator.next().compareTo(wIterator.next())) != 0) {
                        return result;
                    }
                }
                
                //list/set will never equal vector and vector will be smaller if all items equals
                return x.vsize == wsize ? 0 : x.vsize < wsize ? -1 : 1;
            }
            case Types.DENSEVECTOR:{
                final DenseVectorData vdata = (DenseVectorData) w;
                for (int i = 0, result = 0, len = Math.min(x.vsize,  vdata.vsize); i < len; ++i) {
                    if ((result = Double.compare(x.items[i], vdata.items[i])) != 0) {
                        return result;
                    }
                }
                return x.vsize == vdata.vsize ? 0 : x.vsize < vdata.vsize ? -1 : 1;
            }
            case Types.SPARSEVECTOR: 
                return -1 * sparseVectorCompareTo((SparseVectorData) w, x);
            default:
                return x.toString().compareTo(w.toString());
        }
    }
    
    public static int doubleCompareTo(final DoubleData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            case Types.INT: return Double.compare(x.value, ((IntData) w).value);
            case Types.LONG: return Double.compare(x.value, ((LongData) w).value);
            case Types.FLOAT: return Double.compare(x.value, Double.valueOf(w.toString()));
            case Types.DOUBLE: return Double.compare(x.value, ((DoubleData) w).value);
            case Types.NUMERIC: return Double.compare(x.value, ((NumericData) w).getDouble());
            case Types.DECIMAL: return new BigDecimal(x.toString()).subtract(((DecimalData) w).value).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare(x.value, data.real);
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return Double.compare(x.value, ((BooleanData) w).value? 1d : 0d);
            /*
             * Adjust for local time offsets
             */
            case Types.TIME: return Double.compare(x.value, ((TimeData) w).value);
            case Types.TIMESTAMP: return Double.compare(x.value, ((TimestampData) w).value);
            case Types.DATE: return Double.compare(x.value, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Double.compare(x.value,((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP:  return Double.compare(x.value,((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Double.compare(x.value, i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = doubleCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default: {
                final String s = w.toString();
                if (XNumberUtils.isNumber(s)) {
                   return new BigDecimal(String.valueOf(x.value)).subtract(new BigDecimal(s)).signum();
                } else {
                    return String.valueOf(x.value).compareTo(s);
                }
            }
        }
    }
    
    public static int numericCompareTo(final NumericData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        
        final double dbl = x.getDouble();
        switch (w.getType()) {
            case Types.INT: return Double.compare(dbl, ((IntData) w).value);
            case Types.LONG: return Double.compare(dbl, ((LongData) w).value);
            case Types.FLOAT: return Double.compare(dbl, Double.valueOf(w.toString()));
            case Types.DOUBLE: return Double.compare(dbl, ((DoubleData) w).value);
            case Types.NUMERIC: return Double.compare(dbl, ((NumericData) w).getDouble());
            case Types.DECIMAL: return new BigDecimal(x.toString()).subtract(((DecimalData) w).value).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare(dbl, data.real);
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return Double.compare(dbl, ((BooleanData) w).value? 1d : 0d);
            /*
             * Adjust for local time offsets
             */
            case Types.TIME: return Double.compare(dbl, ((TimeData) w).value);
            case Types.TIMESTAMP: return Double.compare(dbl, ((TimestampData) w).value);
            case Types.DATE: return Double.compare(dbl, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Double.compare(dbl,((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP:  return Double.compare(dbl,((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Double.compare(dbl, i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = numericCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default: {
                final String s = w.toString();
                if (XNumberUtils.isNumber(s)) {
                   return new BigDecimal(String.valueOf(dbl)).subtract(new BigDecimal(s)).signum();
                } else {
                    return String.valueOf(dbl).compareTo(s);
                }
            }
        }
    }
    
    public static int floatCompareTo(final FloatData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            case Types.INT: return Float.compare(x.value, ((IntData) w).value);
            case Types.LONG: return Double.compare(Double.valueOf(String.valueOf(x.value)), ((LongData) w).value);
            case Types.FLOAT: return Float.compare(x.value, ((FloatData) w).value);
            case Types.DOUBLE: return Double.compare(Double.valueOf(String.valueOf(x.value)), ((DoubleData) w).value);
            case Types.NUMERIC: return Double.compare(Double.valueOf(String.valueOf(x.value)), ((NumericData) w).getDouble());
            case Types.DECIMAL: return new BigDecimal(String.valueOf(x.value)).subtract(((DecimalData) w).value).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare(Double.valueOf(String.valueOf(x.value)), data.real);
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return Float.compare(x.value, ((BooleanData) w).value ? 1f : 0f);
            /*
             * Adjust for local time offsets
             */
            case Types.TIME: return Double.compare(Double.valueOf(String.valueOf(x.value)), ((TimeData) w).value);
            case Types.TIMESTAMP: return Double.compare(Double.valueOf(String.valueOf(x.value)), ((TimestampData) w).value);
            case Types.DATE: return Double.compare(Double.valueOf(String.valueOf(x.value)), ((DateData) w).value);
            case Types.CALENDAR_TIME: return Double.compare(Double.valueOf(String.valueOf(x.value)),((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP:  return Double.compare(Double.valueOf(String.valueOf(x.value)),((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Double.compare(Double.valueOf(String.valueOf(x.value)), i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(String.valueOf(x.value).getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = floatCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default: {
                final String s = w.toString();
                if (XNumberUtils.isNumber(s)) {
                    return new BigDecimal(String.valueOf(x.value)).subtract(new BigDecimal(s)).signum();
                 } else {
                     return String.valueOf(x.value).compareTo(s);
                 }
            }
        }
    }
    
    public static int instantCompareTo(final InstantData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            case Types.INT: return Long.compare(x.millis, ((IntData) w).getInteger());
            case Types.LONG: return Long.compare(x.millis, ((LongData) w).getLong());
            case Types.FLOAT: return Double.compare(x.millis, ((FloatData) w).getFloat());
            case Types.DOUBLE: return Double.compare(x.millis, ((DoubleData) w).getDouble());
            case Types.NUMERIC: return Double.compare(x.millis, ((NumericData) w).getDouble());
            case Types.DECIMAL: return BigDecimal.valueOf(x.millis).subtract(((DecimalData) w).getDecimal()).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare(x.millis, data.real);
                return compare == 0 ? Double.compare(x.nanos / 1000000, data.imag) : compare;
            }
            case Types.BOOLEAN: return Long.compare(x.millis, ((BooleanData) w).getBoolean() ? 1L : 0L);
            /*
             * All date/time are the same offsets to UTC, so no need to adjust.
             */
            case Types.TIME: return Long.compare(x.millis, ((TimeData) w).value);
            case Types.TIMESTAMP: return Long.compare(x.millis, ((TimestampData) w).value);
            case Types.DATE: return Long.compare(x.millis, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Long.compare(x.millis, ((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP: return Long.compare(x.millis, ((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Long.compare(x.seconds, i.seconds);
                return result != 0 ? result : Integer.compare(x.nanos, i.nanos);
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = instantCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default:
                return x.toString().compareTo(w.toString());
        }
    }
    
    public static int intCompareTo(final IntData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            case Types.INT: return Integer.compare(x.value, ((IntData) w).value);
            case Types.LONG: return Long.compare(x.value, ((LongData) w).value);
            case Types.FLOAT: return Float.compare(x.value, ((FloatData) w).value);
            case Types.DOUBLE: return Double.compare(x.value, ((DoubleData) w).value);
            case Types.NUMERIC: return Double.compare(x.value, ((NumericData) w).getDouble());
            case Types.DECIMAL: return BigDecimal.valueOf(x.value).subtract(((DecimalData) w).value).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare((double) x.value, data.real);
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return Integer.compare(x.value, ((BooleanData) w).value ? 1 : 0);
            /*
             * Adjust for local time offsets
             */
            case Types.TIME: return Long.compare(x.value, ((TimeData) w).value);
            case Types.TIMESTAMP: return Long.compare(x.value, ((TimestampData) w).value);
            case Types.DATE: return Long.compare(x.value, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Long.compare(x.value,((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP:  return Long.compare(x.value,((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Long.compare(x.value, i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = intCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default: {
                final String s = w.toString();
                if (XNumberUtils.isNumber(s)) {
                   return new BigDecimal(String.valueOf(x.value)).subtract(new BigDecimal(s)).signum();
                } else {
                    return x.toString().compareTo(s);
                }
            }
        }
    }
    
    public static int listCompareTo(final ListData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.items == null || x.items.size() == 0 ? 0 : 1;
        }
        switch (w.getType()) {
            case Types.INT:
            case Types.LONG:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.COMPLEX:
            case Types.BOOLEAN:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.DATE:
            case Types.CALENDAR_TIME: 
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
            case Types.BINARY: 
            case Types.STRING:
                if (x.items.size() == 0) {
                    return -1;
                } else {
                    final int compare = x.first().compareTo(w);
                    return compare != 0 ? compare : 1;
                }
            case Types.LIST: {
                final int size = x.items.size();
                final int wsize = w.size();
                if (wsize == 0) {
                    return size == 0 ? 0 : 1;
                }
                
                final Iterator<? extends TypeData> iterator = x.iterator();
                final Iterator<? extends TypeData> wIterator = w.iterator();
                int result = 0;
                
                while (iterator.hasNext() && wIterator.hasNext()) {
                    if ((result = iterator.next().compareTo(wIterator.next())) != 0) {
                        return result;
                    }
                }
                
                return size == wsize ? 0 : size < wsize ? -1 : 1;
            }
            case Types.SET: {
                final int size = x.items.size();
                final int wsize = w.size();
                if (wsize == 0) {
                    return size == 0 ? 0 : 1;
                }
                
                final Iterator<? extends TypeData> iterator = x.iterator();
                final Iterator<? extends TypeData> wIterator = w.iterator();
                int result = 0;
                
                while (iterator.hasNext() && wIterator.hasNext()) {
                    if ((result = iterator.next().compareTo(wIterator.next())) != 0) {
                        return result;
                    }
                }
                
                //list will never equal set and list will be smaller if all items equals
                return size <= wsize ?  -1 : 1;
            }
            case Types.DENSEVECTOR: {
                final int size = x.items.size();
                final int wsize = w.size();
                if (wsize == 0) {
                    return size == 0 ? 0 : 1;
                }
                
                final Iterator<? extends TypeData> iterator = x.iterator();
                final Iterator<? extends TypeData> wIterator = w.iterator();
                int result = 0;
                
                while (iterator.hasNext() && wIterator.hasNext()) {
                    if ((result = iterator.next().compareTo(wIterator.next())) != 0) {
                        return result;
                    }
                }
                
                //list will never equal vector and vector will be smaller if all items equals
                return size < wsize ? -1 : 1;
            }
            case Types.SPARSEVECTOR: 
                return -1 * sparseVectorCompareTo((SparseVectorData) w, x);
            default:
                return x.toString().compareTo(w.toString());
        }
    }
    
    public static int longCompareTo(final LongData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            case Types.INT: return Long.compare(x.value, ((IntData) w).value);
            case Types.LONG: return Long.compare(x.value, ((LongData) w).value);
            case Types.FLOAT: return Double.compare(x.value, Double.valueOf(w.toString()));
            case Types.DOUBLE: return Double.compare(x.value, ((DoubleData) w).value);
            case Types.NUMERIC: return Double.compare(x.value, ((NumericData) w).getDouble());
            case Types.DECIMAL: return BigDecimal.valueOf(x.value).subtract(((DecimalData) w).value).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare((double) x.value, data.real);
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return Long.compare(x.value, ((BooleanData) w).value ? 1L : 0L);
            /*
             * Adjust for local time offsets
             */
            case Types.TIME: return Long.compare(x.value, ((TimeData) w).value);
            case Types.TIMESTAMP: return Long.compare(x.value, ((TimestampData) w).value);
            case Types.DATE: return Long.compare(x.value, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Long.compare(x.value,((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP:  return Long.compare(x.value,((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Long.compare(x.value, i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = longCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default: {
                final String s = w.toString();
                if (XNumberUtils.isNumber(s)) {
                   return BigDecimal.valueOf(x.value).subtract(new BigDecimal(s)).signum();
                } else {
                    return Long.valueOf(x.value).toString().compareTo(s);
                }
            }
        }
    }
    
    public static int mapCompareTo(final MapData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.maps == null || x.maps.size() == 0 ? 0 : 1;
        }
        
        switch (w.getType()) {
            case Types.INT:
            case Types.LONG:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.COMPLEX:
            case Types.BOOLEAN:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.DATE:
            case Types.CALENDAR_TIME: 
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
            case Types.BINARY: 
            case Types.STRING: 
                if (x.maps.size() == 0) {
                    return -1;
                } else {
                    final int compare = x.first().compareTo(w);
                    return compare != 0 ? compare : 1;
                }
            case Types.MAP: 
                throw new IllegalArgumentException("DATA_MAP_COMPARE_IS_INDETERMINSTIC");
            default:
                return x.toString().compareTo(w.toString());
        }
    }
    
    public static int nullCompareTo(final TypeData w) {
        return w == null ? 1 : w.isNull() ? 0 : -1;
    }
    
    public static int setCompareTo(final SetData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.items == null || x.items.size() == 0 ? 0 : 1;
        }
        switch (w.getType()) {
            case Types.INT:
            case Types.LONG:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.COMPLEX:
            case Types.BOOLEAN:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.DATE:
            case Types.CALENDAR_TIME: 
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
            case Types.BINARY: 
            case Types.STRING:
                if (x.items.size() == 0) {
                    return -1;
                } else {
                    final int compare = x.first().compareTo(w);
                    return compare != 0 ? compare : 1;
                }
            case Types.SET:
                throw new IllegalArgumentException("DATA_SET_COMPARE_IS_INDETERMINSTIC");
            case Types.LIST:
            case Types.DENSEVECTOR: {
                final int size = x.items.size();
                final int wsize = w.size();
                if (wsize == 0) {
                    return size == 0 ? 0 : 1;
                }
                
                final Iterator<TypeData> iterator = x.items.iterator();
                final Iterator<? extends TypeData> wIterator = w.iterator();
                int result = 0;
                
                while (iterator.hasNext() && wIterator.hasNext()) {
                    if ((result = iterator.next().compareTo(wIterator.next())) != 0) {
                        return result;
                    }
                }
                
                //set will never equal list/vector and list/vector will be smaller if all items equals
                return size < wsize ?  -1 : 1;
            }
            case Types.SPARSEVECTOR:
                return -1 * sparseVectorCompareTo((SparseVectorData) w, x);
            default:
                return x.toString().compareTo(w.toString());
        }
    }
    
    public static int sparseVectorCompareTo(final SparseVectorData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.vsize == 0 ? 0 : 1;
        }
        switch (w.getType()) {
            case Types.INT:
            case Types.LONG:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.COMPLEX:
            case Types.BOOLEAN:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.DATE:
            case Types.CALENDAR_TIME: 
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
            case Types.BINARY: 
            case Types.STRING:
                if (x.vsize == 0) {
                    return -1;
                } else {
                    final int compare = x.first().compareTo(w);
                    return compare != 0 ? compare : 1;
                }
            case Types.LIST: 
            case Types.SET: {
                final int wsize = w.size();
                if (x.vsize == 0) {
                    return wsize == 0 ? 0 : -1;
                } else if (wsize == 0) {
                    return 1;
                }
                
                final ListData wListData;
                if (w.getType() == Types.LIST) {
                    wListData = (ListData) w;
                } else {
                    final SetData sd = (SetData) w;
                    final ArrayList<TypeData> list = new ArrayList<>(sd.items);
                    wListData = new ListData(list);
                }
                if (x.nzlength == 0) {
                    int result = 0;
                    for (int i = 0, len = Math.min(x.vsize, wsize); i < len; ++i) {
                        if ((result = DoubleData.ZERO.compareTo(wListData.get(i))) != 0) {
                            return result;
                        }
                    }
                    return x.vsize == wsize ? 0 : x.vsize < wsize ? -1 : 1;
                }
                
                int result = 0, index = 0, nzindex = 0, nz = x.indices[0];
                while (result == 0 && index < wsize) {
                    if (index++ >= nz) {
                        if ((result = DoubleData.nonNullValueOf(x.nonzeros[nzindex]).compareTo(wListData.get(index - 1))) != 0) {
                            return result;
                        }
                        if (nzindex + 1 < x.nzlength) {
                            nz = x.indices[++nzindex];
                        } else {
                            break;
                        }
                    } else if ((result = DoubleData.ZERO.compareTo(wListData.get(index - 1))) != 0) {
                        return result;
                    }
                }
                for (int i = index, len = Math.min(x.vsize, wsize); i < len; ++i) {
                    if ((result = DoubleData.ZERO.compareTo(wListData.get(i))) != 0) {
                        return result;
                    }
                }
                
                return x.vsize == wsize ? 0 : x.vsize < wsize ? -1 : 1;
            }
            case Types.DENSEVECTOR: {
                final int wsize = w.size();
                if (x.vsize == 0) {
                    return wsize == 0 ? 0 : -1;
                } else if (wsize == 0) {
                    return 1;
                }
                
                final DenseVectorData wVectorData = (DenseVectorData) w;
                final double[] wdbls = wVectorData.getUnsafeDoubles();
                if (x.nzlength == 0) {
                    int result = 0;
                    for (int i = 0, len = Math.min(x.vsize, wsize); i < len; ++i) {
                        if ((result = Double.compare(0d, wdbls[i])) != 0) {
                            return result;
                        }
                    }
                    return x.vsize == wsize ? 0 : x.vsize < wsize ? -1 : 1;
                }
                
                int result = 0, index = 0, nzindex = 0, nz = x.indices[0];
                while (result == 0 && index < wsize) {
                    if (index++ >= nz) {
                        if ((result = Double.compare(x.nonzeros[nzindex], wdbls[index - 1])) != 0) {
                            return result;
                        }
                        if (nzindex + 1 < x.nzlength) {
                            nz = x.indices[++nzindex];
                        } else {
                            break;
                        }
                    } else if ((result = Double.compare(0d, wdbls[index - 1])) != 0) {
                        return result;
                    }
                }
                for (int i = index, len = Math.min(x.vsize, wsize); i < len; ++i) {
                    if ((result = Double.compare(0d, wdbls[i])) != 0) {
                        return result;
                    }
                }
                
                return x.vsize == wsize ? 0 : x.vsize < wsize ? -1 : 1;
            }
            case Types.SPARSEVECTOR: {
                final int wsize = w.size();
                if (x.vsize == 0) {
                    return wsize == 0 ? 0 : -1;
                } else if (wsize == 0) {
                    return 1;
                }
                
                final SparseVectorData wVectorData = (SparseVectorData) w;
                if (x.nzlength == 0) {
                    return wVectorData.nzlength == 0
                            ? Integer.compare(x.vsize, wVectorData.vsize)
                            : wVectorData.indices[0] < x.nzlength
                                ? Double.compare(0d, wVectorData.nonzeros[0])
                                : -1;
                } else if (wVectorData.nzlength == 0) {
                    return x.indices[0] < wVectorData.nzlength
                            ? Double.compare(x.nonzeros[0], 0d) 
                            : 1;
                }
                
                int result = 0;
                for (int i = 0, len = Math.min(x.nzlength, wVectorData.nzlength); i < len; ++i) {
                    if (x.indices[i] != wVectorData.indices[i]) {
                        return x.indices[i] < wVectorData.indices[i]
                                ? Double.compare(x.nonzeros[i], 0d)
                                : Double.compare(0d, wVectorData.nonzeros[i]);
                    } else if ((result = Double.compare(x.nonzeros[i], wVectorData.nonzeros[i])) != 0) {
                        return result;
                    }
                }
                
                if (x.nzlength == wVectorData.nzlength) {
                    return Integer.compare(x.vsize, wVectorData.vsize);
                } else if (x.nzlength < wVectorData.nzlength) {
                    return wVectorData.indices[x.nzlength] <= x.vsize 
                            ? Double.compare(0d, wVectorData.nonzeros[x.nzlength])
                            : -1;
                } else {
                    return x.indices[wVectorData.nzlength] <= wVectorData.vsize 
                            ? Double.compare(x.nonzeros[wVectorData.nzlength], 0d)
                            : 1;
                }
            }
            default:
                return x.toString().compareTo(w.toString());
        }
    }
    
    public static int stringCompareTo(final StringData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.value == null ? 0 : 1;
        } else if (x.value == null) {
            return -1;
        }
        switch (w.getType()) {
            case Types.INT: 
                if (XNumberUtils.isNumber(x.value)) {
                    return new BigDecimal(x.value).subtract(BigDecimal.valueOf((Integer) w.getObject())).signum();
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.LONG:
                if (XNumberUtils.isNumber(x.value)) {
                    return new BigDecimal(x.value).subtract(BigDecimal.valueOf((Long) w.getObject())).signum();
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.FLOAT: 
            case Types.DOUBLE:
            case Types.NUMERIC:
                if (XNumberUtils.isNumber(x.value)) {
                    return new BigDecimal(x.value).subtract(new BigDecimal(w.toString())).signum();
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.DECIMAL: 
                if (XNumberUtils.isNumber(x.value)) {
                    return new BigDecimal(x.value).subtract((BigDecimal) w.getObject()).signum();
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.COMPLEX:
                if (XComplexUtils.isComplexNumber(x.value)) {
                    final ComplexData data = (ComplexData) w;
                    if (XNumberUtils.isNumber(x.value)) {
                        final int compare = Double.compare(Double.valueOf(x.value), data.real);
                        return compare == 0 ? Double.compare(0d, data.imag) : compare;
                    } else {
                        final ComplexData t = new ComplexData(x.value);
                        final int compare = Double.compare(t.real, data.real);
                        return compare == 0 ? Double.compare(t.imag, data.imag) : compare;
                    }
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.BOOLEAN:
                switch(x.value.toLowerCase()) {
                    case "true":
                    case "yes":
                    case "1":
                    case "是":
                        return ((Boolean) w.getObject()) ? 0 : 1;
                    case "false":
                    case "no":
                    case "0": 
                    case "否":
                        return ((Boolean) w.getObject()) ? -1 : 0;
                    default: 
                        if (XNumberUtils.isNumber(x.value)) {
                            return new BigDecimal(x.value).compareTo(((Boolean) w.getObject()) ? BigDecimal.ONE : BigDecimal.ZERO);
                        } else {
                            return x.value.compareTo(w.toString());
                        }
                }
            case Types.TIME:
                if (XNumberUtils.isNumber(x.value)) {
                    final TimeData tw = (TimeData) w;
                    return new BigDecimal(x.value).subtract(BigDecimal.valueOf(tw.value)).signum();
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.TIMESTAMP:
                if (XNumberUtils.isNumber(x.value)) {
                    final TimestampData tw = (TimestampData) w;
                    return new BigDecimal(x.value).subtract(BigDecimal.valueOf(tw.value)).signum();
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.DATE:
                if (XNumberUtils.isNumber(x.value)) {
                    final DateData dw = (DateData) w;
                    return new BigDecimal(x.value).subtract(BigDecimal.valueOf(dw.value)).signum();
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.CALENDAR_TIME:
                if (XNumberUtils.isNumber(x.value)) {
                    final CalendarTimeData dw = (CalendarTimeData) w;
                    return new BigDecimal(x.value).subtract(BigDecimal.valueOf(dw.value)).signum();
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.CALENDAR_TIMESTAMP: 
                if (XNumberUtils.isNumber(x.value)) {
                    final CalendarTimestampData dw = (CalendarTimestampData) w;
                    return new BigDecimal(x.value).subtract(BigDecimal.valueOf(dw.value)).signum();
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.INSTANT: 
                if (XNumberUtils.isNumber(x.value)) {
                    final InstantData dw = (InstantData) w;
                    final int result = new BigDecimal(x.value).subtract(BigDecimal.valueOf(dw.millis)).signum();
                    return result != 0 ? result : dw.nanos % 1000000 == 0 ? 0 : -1;
                } else {
                    return x.value.compareTo(w.toString());
                }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.value.getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = stringCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default:
                return x.value.compareTo(w.toString());
        }
    }
    
    public static int timeCompareTo(final TimeData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            case Types.INT: return Long.compare(x.value, ((IntData) w).getInteger());
            case Types.LONG: return Long.compare(x.value, ((LongData) w).getLong());
            case Types.FLOAT: return Double.compare(x.value, Double.valueOf(w.toString()));
            case Types.DOUBLE: return Double.compare(x.value, ((DoubleData) w).getDouble());
            case Types.NUMERIC: return Double.compare(x.value, ((NumericData) w).getDouble());
            case Types.DECIMAL: return BigDecimal.valueOf(x.value).subtract(((DecimalData) w).getDecimal()).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare((double) x.value, data.real);
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return Long.compare(x.value, ((BooleanData) w).getBoolean() ? 1L : 0L);
            /*
             * All date/time are the same offsets to UTC, so no need to adjust.
             */
            case Types.TIME: return Long.compare(x.value, ((TimeData) w).value);
            case Types.TIMESTAMP: return Long.compare(x.value, ((TimestampData) w).value);
            case Types.DATE: return Long.compare(x.value, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Long.compare(x.value, ((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP: return Long.compare(x.value, ((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Long.compare(x.value, i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = timeCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default:
                return x.toString().compareTo(w.toString());
        }
    }
    
    public static int timestampCompareTo(final TimestampData x, final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return x.isnull ? 0 : 1;
        } else if (x.isnull) {
            return -1;
        }
        switch (w.getType()) {
            case Types.INT: return Long.compare(x.value, ((IntData) w).getInteger());
            case Types.LONG: return Long.compare(x.value, ((LongData) w).getLong());
            case Types.FLOAT: return Double.compare(x.value, ((FloatData) w).getFloat());
            case Types.DOUBLE: return Double.compare(x.value, ((DoubleData) w).getDouble());
            case Types.NUMERIC: return Double.compare(x.value, ((NumericData) w).getDouble());
            case Types.DECIMAL: return BigDecimal.valueOf(x.value).subtract(((DecimalData) w).getDecimal()).signum();
            case Types.COMPLEX: {
                final ComplexData data = (ComplexData) w;
                final int compare = Double.compare((double) x.value, data.real);
                return compare == 0 ? Double.compare(0d, data.imag) : compare;
            }
            case Types.BOOLEAN: return Long.compare(x.value, ((BooleanData) w).getBoolean() ? 1L : 0L);
            /*
             * All date/time are the same offsets to UTC, so no need to adjust.
             */
            case Types.TIME: return Long.compare(x.value, ((TimeData) w).value);
            case Types.TIMESTAMP: return Long.compare(x.value, ((TimestampData) w).value);
            case Types.DATE: return Long.compare(x.value, ((DateData) w).value);
            case Types.CALENDAR_TIME: return Long.compare(x.value, ((CalendarTimeData) w).value);
            case Types.CALENDAR_TIMESTAMP: return Long.compare(x.value, ((CalendarTimestampData) w).value);
            case Types.INSTANT: {
                final InstantData i = (InstantData) w;
                final int result = Long.compare(x.value, i.millis);
                return result != 0 ? result : i.nanos % 1000000 == 0 ? 0 : -1;
            }
            case Types.BINARY: 
                return ByteBuffer.wrap(x.toString().getBytes(StandardCharsets.UTF_8)).compareTo(ByteBuffer.wrap(w.toString().getBytes(StandardCharsets.UTF_8)));
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: 
            case Types.MAP:
                if (w.size() == 0) {
                    return 1;
                } else {
                    final int compare = timestampCompareTo(x, w.first());
                    return compare != 0 ? compare : -1;
                }
            default:
                return x.toString().compareTo(w.toString());
        }
    }
}
