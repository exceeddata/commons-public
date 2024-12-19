package com.exceeddata.ac.common.data.typedata;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.TimeZone;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.DataConversionException;
import com.exceeddata.ac.common.util.XNumberUtils;
import com.exceeddata.ac.common.util.XStringUtils;
import com.exceeddata.ac.common.util.XTemporalUtils;
import com.exceeddata.ac.common.util.XTypeDataUtils;
import com.exceeddata.ac.common.util.binary.BinaryBigEndianUtils;

public final class DataConv {
    private static final byte[] TRUE_BYTES = "1".getBytes(StandardCharsets.UTF_8);
    private static final byte[] FALSE_BYTES = "0".getBytes(StandardCharsets.UTF_8);
    
    private DataConv() {}
    
    public static NullData toNullData(final TypeData data) {
        return NullData.INSTANCE;
    }
    
    public static BinaryData toBinaryData(final TypeData data) {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? BinaryData.NULL
                                     : new BinaryData(new byte[] {
                                             (byte)(casted.value >>> 24),
                                             (byte)(casted.value >>> 16),
                                             (byte)(casted.value >>> 8),
                                             (byte) casted.value});
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? BinaryData.NULL 
                                     : new BinaryData(new byte[] {
                                             (byte)(casted.value >>> 56),
                                             (byte)(casted.value >>> 48),
                                             (byte)(casted.value >>> 40),
                                             (byte)(casted.value >>> 32),
                                             (byte)(casted.value >>> 24),
                                             (byte)(casted.value >>> 16),
                                             (byte)(casted.value >>> 8),
                                             (byte) casted.value});
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                if (casted.isnull) {
                    return BinaryData.NULL;
                }
                final int v = Float.floatToIntBits(casted.value);
                return new BinaryData(new byte[] {
                            (byte)(v >>> 24),
                            (byte)(v >>> 16),
                            (byte)(v >>> 8),
                            (byte) v});
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                if (casted.isnull) {
                    return BinaryData.NULL;
                }
                final long v = Double.doubleToLongBits(casted.value);
                return new BinaryData(new byte[] {
                        (byte)(v >>> 56),
                        (byte)(v >>> 48),
                        (byte)(v >>> 40),
                        (byte)(v >>> 32),
                        (byte)(v >>> 24),
                        (byte)(v >>> 16),
                        (byte)(v >>> 8),
                        (byte) v});
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                if (casted.isnull) {
                    return BinaryData.NULL;
                }
                final long v = Double.doubleToLongBits(casted.value);
                return new BinaryData(new byte[] {
                        (byte)(v >>> 56),
                        (byte)(v >>> 48),
                        (byte)(v >>> 40),
                        (byte)(v >>> 32),
                        (byte)(v >>> 24),
                        (byte)(v >>> 16),
                        (byte)(v >>> 8),
                        (byte) v});
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ?  BinaryData.NULL : new BinaryData(decimal.toString().getBytes(StandardCharsets.UTF_8));
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? BinaryData.NULL : casted.value ? new BinaryData(TRUE_BYTES) : new BinaryData(FALSE_BYTES);
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                if (casted.isnull) {
                    return BinaryData.NULL;
                }
                final String si = String.valueOf(casted.imag);
                if (si.charAt(0) == '-' ) {
                    return new BinaryData((String.valueOf(casted.real) + si + "i").getBytes(StandardCharsets.UTF_8));
                } else {
                    return new BinaryData((String.valueOf(casted.real)  + "+" + si + "i").getBytes(StandardCharsets.UTF_8));
                }
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? BinaryData.NULL 
                                     : new BinaryData(LocalDateTime.ofInstant(Instant.ofEpochMilli(casted.value), XTemporalUtils.SYSTEM_ZONEID)
                                                    .format( XTemporalUtils.DATE_FORMATTER)
                                                    .getBytes(StandardCharsets.UTF_8));
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? BinaryData.NULL 
                                     : new BinaryData(LocalDateTime.ofInstant(Instant.ofEpochMilli(casted.value), XTemporalUtils.SYSTEM_ZONEID)
                                                    .format(XTemporalUtils.TIME_FORMATTER)
                                                    .getBytes(StandardCharsets.UTF_8));
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? BinaryData.NULL 
                                     : new BinaryData(LocalDateTime.ofInstant(Instant.ofEpochMilli(casted.value), XTemporalUtils.SYSTEM_ZONEID)
                                                    .format(XTemporalUtils.INSTANT_FORMATTER)
                                                    .getBytes(StandardCharsets.UTF_8));
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? BinaryData.NULL 
                                     : new BinaryData(ZonedDateTime.ofInstant(Instant.ofEpochMilli(casted.value), TimeZone.getTimeZone(casted.timezone).toZoneId())
                                                    .format(XTemporalUtils.CALENDAR_TIME_FORMATTER)
                                                    .getBytes(StandardCharsets.UTF_8));
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? BinaryData.NULL 
                                     : new BinaryData(ZonedDateTime.ofInstant(Instant.ofEpochMilli(casted.value), TimeZone.getTimeZone(casted.timezone).toZoneId())
                                                    .format(XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER)
                                                    .getBytes(StandardCharsets.UTF_8));
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? BinaryData.NULL 
                                     : new BinaryData(LocalDateTime.ofInstant(Instant.ofEpochSecond(casted.seconds, casted.nanos), XTemporalUtils.SYSTEM_ZONEID)
                                                    .format(XTemporalUtils.INSTANT_FORMATTER)
                                                    .getBytes(StandardCharsets.UTF_8));
            }
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR:
            case Types.MAP:
                return data.isEmpty() ? BinaryData.NULL : new BinaryData(data.toString().getBytes(StandardCharsets.UTF_8));
            case Types.NULL:
                return BinaryData.NULL;
            case Types.BINARY:
                return (BinaryData) data;
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return BinaryData.NULL;
                }
                return new BinaryData(casted.value.getBytes(StandardCharsets.UTF_8));
            }
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return BinaryData.NULL;
                }
                return new BinaryData(value.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
    
    public static BooleanData toBooleanData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? BooleanData.NULL : casted.value != 0 ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? BooleanData.NULL : casted.value != 0l ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? BooleanData.NULL : Float.compare(casted.value, 0f) != 0 ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? BooleanData.NULL : Double.compare(casted.value, 0d) != 0 ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? BooleanData.NULL : casted.toBoolean() ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? BooleanData.NULL : decimal.signum() != 0 ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.BOOLEAN:
                return (BooleanData) data;
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? BooleanData.NULL : Double.compare(casted.real, 0d) != 0 && Double.compare(casted.imag, 0d) != 0 ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? BooleanData.NULL : casted.value != 0l ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? BooleanData.NULL : casted.value != 0l ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? BooleanData.NULL : casted.value != 0l ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? BooleanData.NULL : casted.value != 0l ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? BooleanData.NULL : casted.value != 0l ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? BooleanData.NULL : casted.millis != 0l && casted.nanos != 0 ? BooleanData.TRUE : BooleanData.FALSE;
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_BOOLEAN_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_BOOLEAN_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_BOOLEAN_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_BOOLEAN_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_BOOLEAN_CONVERSION_INVALID");
            case Types.NULL:
                return BooleanData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null || value.length == 0) {
                    return BooleanData.NULL;
                }
                final String s = BinaryUtils.deserializeToString(value);
                switch (s.trim().toLowerCase()) {
                    case "否":
                    case "no":
                    case "false":
                    case "0":
                        return BooleanData.FALSE;
                    default: return BooleanData.TRUE;
                }
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return BooleanData.NULL;
                }
                switch (casted.value.trim().toLowerCase()) {
                    case "否":
                    case "no":
                    case "false":
                    case "0":
                        return BooleanData.FALSE;
                    default: return BooleanData.TRUE;
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_BOOLEAN_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return BooleanData.NULL;
                }
                switch (value.trim().toLowerCase()) {
                    case "否":
                    case "no":
                    case "false":
                    case "0":
                        return BooleanData.FALSE;
                    default: return BooleanData.TRUE;
                }
            }
        }
    }
    
    public static ComplexData toComplexData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf((double) casted.value);
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf((double) casted.value);
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf(new BigDecimal(String.valueOf(casted.value)).doubleValue());
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf(casted.value);
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf(casted.value);
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? ComplexData.NULL : ComplexData.nonNullValueOf(decimal.doubleValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? ComplexData.NULL : casted.value ? ComplexData.ONE : ComplexData.ZERO;
            }
            case Types.COMPLEX:
                return (ComplexData) data;
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf((double) casted.value);
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf((double) casted.value);
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf((double) casted.value);
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf((double) casted.value);
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf((double) casted.value);
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? ComplexData.NULL : ComplexData.nonNullValueOf((double) casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_COMPLEX_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_COMPLEX_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_COMPLEX_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_COMPLEX_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_COMPLEX_CONVERSION_INVALID");
            case Types.NULL:
                return ComplexData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null || value.length == 0) {
                    return ComplexData.NULL;
                }
                return ComplexData.valueOf(BinaryUtils.deserializeToString(value));
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return ComplexData.NULL;
                }
                return ComplexData.valueOf(casted.value);
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_COMPLEX_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return ComplexData.NULL;
                }
                return ComplexData.valueOf(value);
            }
        }
    }   


    public static DecimalData toDecimalData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(BigDecimal.valueOf((long) casted.value));
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(BigDecimal.valueOf(casted.value));
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(new BigDecimal(String.valueOf(casted.value)));
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(new BigDecimal(String.valueOf(casted.value)));
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(casted.toDecimal());
            }
            case Types.DECIMAL:
                return (DecimalData) data;
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? DecimalData.NULL : casted.value ? DecimalData.ONE : DecimalData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(new BigDecimal(String.valueOf(casted.real)));
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(BigDecimal.valueOf(casted.value));
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(BigDecimal.valueOf(casted.value));
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(BigDecimal.valueOf(casted.value));
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(BigDecimal.valueOf(casted.value));
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(BigDecimal.valueOf(casted.value));
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? DecimalData.NULL : DecimalData.nonNullValueOf(BigDecimal.valueOf(casted.millis));
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_DECIMAL_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_DECIMAL_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_DECIMAL_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_DECIMAL_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_DECIMAL_CONVERSION_INVALID");
            case Types.NULL:
                return DecimalData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null || value.length == 0) {
                    return null;
                }
                try {
                    return DecimalData.nonNullValueOf(new BigDecimal(BinaryUtils.deserializeToString(value)));
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return DecimalData.NULL;
                }
                try {
                    return DecimalData.nonNullValueOf(new BigDecimal(casted.value));
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_DECIMAL_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return DecimalData.NULL;
                }
                try {
                    return DecimalData.nonNullValueOf(new BigDecimal(value));
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }
    
    public static DoubleData toDoubleData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf((double) casted.value);
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf((double) casted.value);
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf(new BigDecimal(String.valueOf(casted.value)).doubleValue());
            }
            case Types.DOUBLE:
                return (DoubleData) data;
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf(casted.value);
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? DoubleData.NULL : DoubleData.nonNullValueOf(decimal.doubleValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? DoubleData.NULL : casted.value ? DoubleData.ONE : DoubleData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf(casted.real);
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf((double) casted.value);
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf((double) casted.value);
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf((double) casted.value);
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf((double) casted.value);
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf((double) casted.value);
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? DoubleData.NULL : DoubleData.nonNullValueOf((double) casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_DOUBLE_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_DOUBLE_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_DOUBLE_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_DOUBLE_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_DOUBLE_CONVERSION_INVALID");
            case Types.NULL:
                return DoubleData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null) {
                    return DoubleData.NULL;
                } else if (value.length < 8) {
                    if (value.length == 0) {
                        return DoubleData.NULL;
                    }
                    throw new DataConversionException ("DATA_BINARY_TO_DOUBLE_INSUFFICIENT_BYTES");
                }
                
                return DoubleData.nonNullValueOf(Double.longBitsToDouble(BinaryBigEndianUtils.makeLong(value[0], value[1], value[2], value[3], value[4], value[5], value[6], value[7])));
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return DoubleData.NULL;
                }
                try {
                    return DoubleData.nonNullValueOf(Double.parseDouble(casted.value));
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_DOUBLE_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return DoubleData.NULL;
                }
                try {
                    return DoubleData.nonNullValueOf(Double.valueOf(value));
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }
    
    public static NumericData toNumericData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(casted.value);
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(casted.value);
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(new BigDecimal(String.valueOf(casted.value)).doubleValue());
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(casted.value);
            }
            case Types.NUMERIC:
                return (NumericData) data;
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? NumericData.NULL : NumericData.nonNullValueOf(decimal.doubleValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? NumericData.NULL : casted.value ? NumericData.ONE : NumericData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(casted.real);
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(casted.value);
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(casted.value);
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(casted.value);
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(casted.value);
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(casted.value);
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? NumericData.NULL : NumericData.nonNullValueOf(casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_NUMERIC_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_NUMERIC_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_NUMERIC_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_NUMERIC_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_NUMERIC_CONVERSION_INVALID");
            case Types.NULL:
                return NumericData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null) {
                    return NumericData.NULL;
                } else if (value.length < 8) {
                    if (value.length == 0) {
                        return NumericData.NULL;
                    }
                    throw new DataConversionException ("DATA_BINARY_TO_NUMERIC_INSUFFICIENT_BYTES");
                }
                
                return NumericData.nonNullValueOf(Double.longBitsToDouble(BinaryBigEndianUtils.makeLong(value[0], value[1], value[2], value[3], value[4], value[5], value[6], value[7])));
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return NumericData.NULL;
                }
                try {
                    return NumericData.nonNullValueOf(Double.parseDouble(casted.value));
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_NUMERIC_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return NumericData.NULL;
                }
                try {
                    return NumericData.nonNullValueOf(Double.valueOf(value));
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }
    
    public static FloatData toFloatData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf((float) casted.value);
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf((float) casted.value);
            }
            case Types.FLOAT:
                return (FloatData) data;
            case Types.DOUBLE:{
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf(casted.toFloat());
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf(casted.toFloat());
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? FloatData.NULL : FloatData.nonNullValueOf(decimal.floatValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? FloatData.NULL : casted.value ? FloatData.ONE : FloatData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf(new BigDecimal(String.valueOf(casted.real)).floatValue());
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf((float) casted.value);
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf((float) casted.value);
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf((float) casted.value);
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf((float) casted.value);
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf((float) casted.value);
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? FloatData.NULL : FloatData.nonNullValueOf((float) casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_FLOAT_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_FLOAT_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_FLOAT_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_FLOAT_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_FLOAT_CONVERSION_INVALID");
            case Types.NULL:
                return FloatData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null) {
                    return FloatData.NULL;
                } else if (value.length < 4) {
                    if (value.length == 0) {
                        return FloatData.NULL;
                    }
                    throw new DataConversionException ("DATA_BINARY_TO_FLOAT_INSUFFICIENT_BYTES");
                }

                return FloatData.nonNullValueOf(Float.intBitsToFloat(BinaryBigEndianUtils.makeInt(value[0], value[1], value[2], value[3])));
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return FloatData.NULL;
                }
                try {
                    return FloatData.nonNullValueOf(Float.parseFloat(casted.value));
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_FLOAT_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return FloatData.NULL;
                }
                try {
                    return FloatData.nonNullValueOf(Double.valueOf(value).floatValue());
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }
     
    public static IntData toIntData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT:
                return (IntData) data;
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf((int) casted.value);
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf((int) casted.value);
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf((int) casted.value);
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf(casted.toInt());
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? IntData.NULL : IntData.nonNullValueOf(decimal.intValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? IntData.NULL : casted.value ? IntData.ONE : IntData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf((int) casted.real);
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf((int) casted.value);
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf((int) casted.value);
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf((int) casted.value);
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf((int) casted.value);
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf((int) casted.value);
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? IntData.NULL : IntData.nonNullValueOf((int) casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_INT_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_INT_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_INT_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_INT_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_INT_CONVERSION_INVALID");
            case Types.NULL:
                return IntData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null) {
                    return IntData.NULL;
                }
                switch(value.length) {
                    case 0:  return null;
                    case 1:  return IntData.nonNullValueOf(BinaryBigEndianUtils.makeInt(BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, value[0]));
                    case 2:  return IntData.nonNullValueOf(BinaryBigEndianUtils.makeInt(BinaryUtils.ZERO, BinaryUtils.ZERO, value[0], value[1]));
                    case 3:  return IntData.nonNullValueOf(BinaryBigEndianUtils.makeInt(BinaryUtils.ZERO, value[0], value[1], value[2]));
                    default: return IntData.nonNullValueOf(BinaryBigEndianUtils.makeInt(value[0], value[1], value[2], value[3]));
                }
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return IntData.NULL;
                }
                try {
                    return IntData.nonNullValueOf((int) Double.parseDouble(casted.value));
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_INT_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return IntData.NULL;
                }
                try {
                    return IntData.nonNullValueOf(Double.valueOf(value).intValue());
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }

    public static LongData toLongData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT:{
                final IntData casted = (IntData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf((long) casted.value);
            }
            case Types.LONG:
                return (LongData) data;
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf((long) casted.value);
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf((long) casted.value);
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf((long) casted.value);
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? LongData.NULL : LongData.nonNullValueOf(decimal.longValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? LongData.NULL : casted.value ? LongData.ONE : LongData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf((long) casted.real);
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf(casted.value);
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf(casted.value);
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf(casted.value);
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf(casted.value);
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf(casted.value);
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? LongData.NULL : LongData.nonNullValueOf(casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_LONG_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_LONG_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_LONG_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_LONG_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_LONG_CONVERSION_INVALID");
            case Types.NULL:
                return LongData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null) {
                    return LongData.NULL;
                }
                switch(value.length) {
                    case 0:  return LongData.NULL;
                    case 1:  return LongData.nonNullValueOf(BinaryBigEndianUtils.makeLong(BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, value[0]));
                    case 2:  return LongData.nonNullValueOf(BinaryBigEndianUtils.makeLong(BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, value[0], value[1]));
                    case 3:  return LongData.nonNullValueOf(BinaryBigEndianUtils.makeLong(BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, value[0], value[1], value[2]));
                    case 4:  return LongData.nonNullValueOf(BinaryBigEndianUtils.makeLong(BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, value[0], value[1], value[2], value[3]));
                    case 5:  return LongData.nonNullValueOf(BinaryBigEndianUtils.makeLong(BinaryUtils.ZERO, BinaryUtils.ZERO, BinaryUtils.ZERO, value[0], value[1], value[2], value[3], value[4]));
                    case 6:  return LongData.nonNullValueOf(BinaryBigEndianUtils.makeLong(BinaryUtils.ZERO, BinaryUtils.ZERO, value[0], value[1], value[2], value[3], value[4], value[5]));
                    case 7:  return LongData.nonNullValueOf(BinaryBigEndianUtils.makeLong(BinaryUtils.ZERO, value[0], value[1], value[2], value[3], value[4], value[5], value[6]));
                    default: return LongData.nonNullValueOf(BinaryBigEndianUtils.makeLong(value[0], value[1], value[2], value[3], value[4], value[5], value[6], value[7]));
                }
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return LongData.NULL;
                }
                try {
                    return LongData.nonNullValueOf((long) Double.parseDouble(casted.value));
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_LONG_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return LongData.NULL;
                }
                try {
                    return LongData.nonNullValueOf(Double.valueOf(value).longValue());
                } catch (NumberFormatException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }
  
    public static DateData toDateData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? DateData.NULL : DateData.valueOf((long) casted.value);
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? DateData.NULL : DateData.valueOf(casted.value);
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? DateData.NULL : DateData.valueOf((long) casted.value);
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? DateData.NULL : DateData.valueOf((long) casted.value);
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? DateData.NULL : DateData.valueOf((long) casted.value);
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? DateData.NULL : DateData.valueOf(decimal.longValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? DateData.NULL : casted.value ? DateData.ONE : DateData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? DateData.NULL : DateData.valueOf((long) casted.real);
            }
            case Types.DATE:
                return (DateData) data;
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? DateData.NULL : DateData.ZERO;
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? DateData.NULL : DateData.valueOf(casted.value);
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? DateData.NULL : DateData.valueOf(casted.value);
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? DateData.NULL : DateData.valueOf(casted.value);
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? DateData.NULL : DateData.valueOf(casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_DATE_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_DATE_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_DATE_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_DATE_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_DATE_CONVERSION_INVALID");
            case Types.NULL:
                return DateData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null || value.length == 0) {
                    return DateData.NULL;
                }
                try {
                    final Long result = XTemporalUtils.parseDate(BinaryUtils.deserializeToString(value));
                    return result != null ? DateData.valueOf(result.longValue()) : DateData.NULL;
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return DateData.NULL;
                }
                try {
                    final Long result = XTemporalUtils.parseDate(casted.value);
                    return result != null ? DateData.valueOf(result.longValue()) : DateData.NULL;
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_DATE_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return DateData.NULL;
                }
                try {
                    final Long result = XTemporalUtils.parseDate(value);
                    return result != null ? DateData.valueOf(result.longValue()) : DateData.NULL;
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }
  
    public static TimeData toTimeData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? TimeData.NULL : TimeData.valueOf((long) casted.value);
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? TimeData.NULL : TimeData.valueOf(casted.value);
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? TimeData.NULL : TimeData.valueOf((long) casted.value);
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? TimeData.NULL : TimeData.valueOf((long) casted.value);
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? TimeData.NULL : TimeData.valueOf((long) casted.value);
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? TimeData.NULL : TimeData.valueOf(decimal.longValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? TimeData.NULL : casted.value ? TimeData.ONE : TimeData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? TimeData.NULL : TimeData.valueOf((long) casted.real);
            }
            case Types.DATE:{
                final DateData casted = (DateData) data;
                return casted.isnull ? TimeData.NULL : TimeData.ZERO;
            }
            case Types.TIME:
                return (TimeData) data;
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? TimeData.NULL : TimeData.valueOf(casted.value);
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? TimeData.NULL : TimeData.valueOf(casted.value);
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? TimeData.NULL : TimeData.valueOf(casted.value);
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? TimeData.NULL : TimeData.valueOf(casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_TIME_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_TIME_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_TIME_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_TIME_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_TIME_CONVERSION_INVALID");
            case Types.NULL:
                return TimeData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null || value.length == 0) {
                    return TimeData.NULL;
                }
                try {
                    final Long result = XTemporalUtils.parseTime(BinaryUtils.deserializeToString(value));
                    return result == null ? TimeData.NULL : TimeData.valueOf(result.longValue());
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return TimeData.NULL;
                }
                try {
                    final Long result = XTemporalUtils.parseTime(casted.value);
                    return result == null ? TimeData.NULL : TimeData.valueOf(result.longValue());
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_TIME_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return TimeData.NULL;
                }
                try {
                    final Long result = XTemporalUtils.parseTime(value);
                    return result == null ? TimeData.NULL : TimeData.valueOf(result.longValue());
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }

    public static TimestampData toTimestampData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf((long) casted.value);
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf(casted.value);
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf((long) casted.value);
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf((long) casted.value);
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf((long) casted.value);
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? TimestampData.NULL : TimestampData.valueOf(decimal.longValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? TimestampData.NULL : casted.value ? TimestampData.ONE : TimestampData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf((long) casted.real);
            }
            case Types.DATE:{
                final DateData casted = (DateData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf(casted.value);
            }
            case Types.TIME:{
                final TimeData casted = (TimeData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf(casted.value);
            }
            case Types.TIMESTAMP:
                return (TimestampData) data;
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf(casted.value);
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf(casted.value);
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? TimestampData.NULL : TimestampData.valueOf(casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_TIMESTAMP_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_TIMESTAMP_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_TIMESTAMP_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_TIMESTAMP_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_TIMESTAMP_CONVERSION_INVALID");
            case Types.NULL:
                return TimestampData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null || value.length == 0) {
                    return TimestampData.NULL;
                }
                try {
                    final Long result = XTemporalUtils.parseTimestamp(BinaryUtils.deserializeToString(value));
                    return result != null ? TimestampData.valueOf(result.longValue()) : TimestampData.NULL;
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return TimestampData.NULL;
                }
                try {
                    final Long result = XTemporalUtils.parseTimestamp(casted.value);
                    return result != null ? TimestampData.valueOf(result.longValue()) : TimestampData.NULL;
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_TIMESTAMP_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return TimestampData.NULL;
                }
                try {
                    final Long result = XTemporalUtils.parseTimestamp(value);
                    return result != null ? TimestampData.valueOf(result.longValue()) : TimestampData.NULL;
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }
 
    public static CalendarTimeData toCalendarTimeData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? CalendarTimeData.NULL : CalendarTimeData.valueOf((long) casted.value);
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? CalendarTimeData.NULL : CalendarTimeData.valueOf(casted.value);
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? CalendarTimeData.NULL : CalendarTimeData.valueOf((long) casted.value);
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? CalendarTimeData.NULL : CalendarTimeData.valueOf((long) casted.value);
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? CalendarTimeData.NULL : CalendarTimeData.valueOf((long) casted.value);
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? CalendarTimeData.NULL : CalendarTimeData.valueOf(decimal.longValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? CalendarTimeData.NULL : casted.value ? CalendarTimeData.ONE : CalendarTimeData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? CalendarTimeData.NULL : CalendarTimeData.valueOf((long) casted.real);
            }
            case Types.DATE:{
                final DateData casted = (DateData) data;
                return casted.isnull ? CalendarTimeData.NULL : CalendarTimeData.ZERO;
            }
            case Types.TIME:{
                final TimeData casted = (TimeData) data;
                return casted.isnull ? CalendarTimeData.NULL : CalendarTimeData.valueOf(casted.value);
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? CalendarTimeData.NULL : CalendarTimeData.valueOf(casted.value);
            }
            case Types.CALENDAR_TIME:
                return (CalendarTimeData) data;
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                if (casted.isnull) {
                    return CalendarTimeData.NULL;
                } else {
                    final GregorianCalendar cal = new GregorianCalendar();
                    cal.setTimeZone(TimeZone.getTimeZone(casted.timezone));
                    cal.setTimeInMillis(casted.value);
                    cal.set(Calendar.YEAR, 1970);
                    cal.set(Calendar.DAY_OF_YEAR, 1);
                    return CalendarTimeData.valueOf(cal);
                }
            }
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? CalendarTimeData.NULL : CalendarTimeData.valueOf(casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_CALENDAR_TIME_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_CALENDAR_TIME_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_CALENDAR_TIME_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_CALENDAR_TIME_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_CALENDAR_TIME_CONVERSION_INVALID");
            case Types.NULL:
                return CalendarTimeData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null || value.length == 0) {
                    return CalendarTimeData.NULL;
                }
                try {
                    final ZonedDateTime zdt = XTemporalUtils.parseCalendarTime(BinaryUtils.deserializeToString(value));
                    return zdt == null ? CalendarTimeData.NULL : new CalendarTimeData(GregorianCalendar.from(zdt));
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return CalendarTimeData.NULL;
                }
                try {
                    final ZonedDateTime zdt = XTemporalUtils.parseCalendarTime(casted.value);
                    if (zdt != null) {
                        return new CalendarTimeData(GregorianCalendar.from(zdt));
                    }

                    // try again
                    final Long tsp = XTemporalUtils.parseTimestamp(casted.value);
                    if (tsp != null) {
                        return new CalendarTimeData(tsp.longValue());
                    }
                    return CalendarTimeData.NULL;
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_CALENDAR_TIME_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return CalendarTimeData.NULL;
                }
                try {
                    final ZonedDateTime zdt = XTemporalUtils.parseCalendarTime(value);
                    if (zdt != null) {
                        return new CalendarTimeData(GregorianCalendar.from(zdt));
                    }

                    // try again
                    final Long tsp = XTemporalUtils.parseTimestamp(value);
                    if (tsp != null) {
                        return new CalendarTimeData(tsp.longValue());
                    }
                    return CalendarTimeData.NULL;
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }
  
    public static CalendarTimestampData toCalendarTimestampData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf((long) casted.value);
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf(casted.value);
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf((long) casted.value);
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf((long) casted.value);
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf((long) casted.value);
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf(decimal.longValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? CalendarTimestampData.NULL : casted.value ? CalendarTimestampData.ONE : CalendarTimestampData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf((long) casted.real);
            }
            case Types.DATE:{
                final DateData casted = (DateData) data;
                return casted.isnull ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf((long) casted.value);
            }
            case Types.TIME:{
                final TimeData casted = (TimeData) data;
                return casted.isnull ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf(casted.value);
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf(casted.value);
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                if (casted.isnull) {
                    return CalendarTimestampData.NULL;
                } else {
                    final GregorianCalendar cal = new GregorianCalendar();
                    cal.setTimeZone(TimeZone.getTimeZone(casted.timezone));
                    cal.setTimeInMillis(casted.value);
                    return CalendarTimestampData.valueOf(cal);
                }
            }
            case Types.CALENDAR_TIMESTAMP:
                return (CalendarTimestampData) data;
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? CalendarTimestampData.NULL : CalendarTimestampData.valueOf(casted.millis);
            }
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_CALENDAR_TIMESTAMP_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_CALENDAR_TIMESTAMP_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_CALENDAR_TIMESTAMP_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_CALENDAR_TIMESTAMP_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_CALENDAR_TIMESTAMP_CONVERSION_INVALID");
            case Types.NULL:
                return CalendarTimestampData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null || value.length == 0) {
                    return CalendarTimestampData.NULL;
                }
                try {
                    final ZonedDateTime zdt = XTemporalUtils.parseCalendarTime(BinaryUtils.deserializeToString(value));
                    return zdt == null ? CalendarTimestampData.NULL : new CalendarTimestampData(GregorianCalendar.from(zdt));
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return CalendarTimestampData.NULL;
                }
                try {
                    final ZonedDateTime zdt = XTemporalUtils.parseCalendarTimestamp(casted.value);
                    if (zdt != null) {
                        return new CalendarTimestampData(GregorianCalendar.from(zdt));
                    }

                    // try again
                    final Long tsp = XTemporalUtils.parseTimestamp(casted.value);
                    if (tsp != null) {
                        return new CalendarTimestampData(tsp.longValue());
                    }
                    return CalendarTimestampData.NULL;
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_CALENDAR_TIMESTAMP_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return CalendarTimestampData.NULL;
                }
                try {
                    final ZonedDateTime zdt = XTemporalUtils.parseCalendarTimestamp(value);
                    if (zdt != null) {
                        return new CalendarTimestampData(GregorianCalendar.from(zdt));
                    }

                    // try again
                    final Long tsp = XTemporalUtils.parseTimestamp(value);
                    if (tsp != null) {
                        return new CalendarTimestampData(tsp.longValue());
                    }
                    return CalendarTimestampData.NULL;
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }
 
    public static InstantData toInstantData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf((long) casted.value);
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf(casted.value);
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf((long) casted.value);
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf((long) casted.value);
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf((long) casted.value);
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? InstantData.NULL : InstantData.valueOf(decimal.longValue());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? InstantData.NULL : casted.value ? InstantData.ONE : InstantData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf((long) casted.real);
            }
            case Types.DATE:{
                final DateData casted = (DateData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf(casted.value);
            }
            case Types.TIME:{
                final TimeData casted = (TimeData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf(casted.value);
            }
            case Types.TIMESTAMP:{
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf(casted.value);
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf(casted.value);
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? InstantData.NULL : InstantData.valueOf(casted.value);
            }
            case Types.INSTANT: 
                return (InstantData) data;
            case Types.LIST:
                throw new DataConversionException ("DATA_LIST_TO_INSTANT_CONVERSION_INVALID");
            case Types.SET:
                throw new DataConversionException ("DATA_SET_TO_INSTANT_CONVERSION_INVALID");
            case Types.DENSEVECTOR:
                throw new DataConversionException ("DATA_DENSEVECTOR_TO_INSTANT_CONVERSION_INVALID");
            case Types.SPARSEVECTOR:
                throw new DataConversionException ("DATA_SPARSEVECTOR_TO_INSTANT_CONVERSION_INVALID");
            case Types.MAP:
                throw new DataConversionException ("DATA_MAP_TO_INSTANT_CONVERSION_INVALID");
            case Types.NULL:
                return InstantData.NULL;
            case Types.BINARY:{
                final BinaryData casted = (BinaryData) data;
                final byte[] value = casted.value;
                if (value == null || value.length == 0) {
                    return InstantData.NULL;
                }
                try {
                    return InstantData.valueOf(XTemporalUtils.parseInstant(BinaryUtils.deserializeToString(value)));
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return InstantData.NULL;
                }
                try {
                    return InstantData.valueOf(XTemporalUtils.parseInstant(casted.value));
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_INSTANT_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return InstantData.NULL;
                }
                try {
                    return InstantData.valueOf(XTemporalUtils.parseInstant(value));
                } catch (DateTimeParseException e) {
                    throw new DataConversionException(e.getMessage(), e);
                }
            }
        }
    }
   
    public static ListData toListData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT:
            case Types.LONG:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.BOOLEAN:
            case Types.COMPLEX:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP: 
            case Types.INSTANT:
                return data.isNull() ? ListData.NULL : new ListData(new ArrayList<>(Collections.nCopies(1, data)));
            case Types.LIST:
                return (ListData) data;
            case Types.SET: {
                final SetData casted = (SetData) data;
                final int size = casted.size();
                if (size == 0) {
                    return ListData.NULL;
                }
                final ArrayList<TypeData> list = new ArrayList<>(size);
                for (final TypeData item : casted.items) {
                    list.add(item);
                }
                return new ListData(list);
            }
            case Types.DENSEVECTOR:{
                final DenseVectorData casted = (DenseVectorData) data;
                final int size = casted.size();
                if (size == 0) {
                    return ListData.NULL;
                }
                final ArrayList<TypeData> list = new ArrayList<>(size);
                for (final double item : casted.items) {
                    list.add(DoubleData.nonNullValueOf(item));
                }
                return new ListData(list);
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData casted = (SparseVectorData) data;
                if (casted.vsize == 0) {
                    return ListData.NULL;
                }
                final ArrayList<TypeData> list = new ArrayList<>(Collections.nCopies(casted.vsize, DoubleData.ZERO));
                for (int i = 0, nzlength = casted.nzlength; i < nzlength; ++i) {
                    list.set(casted.indices[i], DoubleData.nonNullValueOf(casted.nonzeros[i]));
                }
                return new ListData(list);
            }
            case Types.MAP: {
                final MapData casted = (MapData) data;
                final int size = casted.size();
                if (size == 0) {
                    return ListData.NULL;
                }
                final ArrayList<TypeData> list = new ArrayList<>(size);
                for (final Entry<TypeData, TypeData> entry : casted.maps.entrySet()) {
                    list.add(entry.getValue());
                }
                return new ListData(list);
            }
            case Types.NULL:
                return ListData.NULL;
            case Types.BINARY: 
                return data.isNull() ? ListData.NULL : new ListData(new ArrayList<>(Collections.nCopies(1, data)));
            case Types.STRING:{
                final StringData casted = (StringData) data;
                return XTypeDataUtils.jsonToListData(casted.value);
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_LIST_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                return XTypeDataUtils.jsonToListData(value);
            }
        }
    }

    public static SetData toSetData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT:
            case Types.LONG:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.BOOLEAN:
            case Types.COMPLEX:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP: 
            case Types.INSTANT:
                return data.isNull() ? SetData.NULL : new SetData(new LinkedHashSet<>(Collections.nCopies(1, data)));
            case Types.LIST:{
                final ListData casted = (ListData) data;
                final int size = casted.size();
                if (size == 0) {
                    return SetData.NULL;
                }
                final LinkedHashSet<TypeData> set = new LinkedHashSet<>(size);
                for (final TypeData item : casted.items) {
                    set.add(item);
                }
                return new SetData(set);
            }
            case Types.SET:
                return (SetData) data;
            case Types.DENSEVECTOR:{
                final DenseVectorData casted = (DenseVectorData) data;
                final int size = casted.size();
                if (size == 0) {
                    return SetData.NULL;
                }
                final LinkedHashSet<TypeData> set = new LinkedHashSet<>(size);
                for (final double item : casted.items) {
                    set.add(DoubleData.nonNullValueOf(item));
                }
                return new SetData(set);
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData casted = (SparseVectorData) data;
                if (casted.vsize == 0) {
                    return SetData.NULL;
                }
                final LinkedHashSet<TypeData> set = new LinkedHashSet<>(casted.nzlength + 1);
                if (casted.vsize != casted.nzlength) {
                    set.add(DoubleData.ZERO);
                }
                for (int i = 0, nzlength = casted.nzlength; i < nzlength; ++i) {
                    set.add(DoubleData.nonNullValueOf(casted.nonzeros[i]));
                }
                return new SetData(set);
            }
            case Types.MAP: {
                final MapData casted = (MapData) data;
                final int size = casted.size();
                if (size == 0) {
                    return SetData.NULL;
                }
                final LinkedHashSet<TypeData> set = new LinkedHashSet<>();
                for (final Entry<TypeData, TypeData> entry : casted.maps.entrySet()) {
                    set.add(entry.getValue());
                }
                return new SetData(set);
            }
            case Types.NULL:
                return SetData.NULL;
            case Types.BINARY: 
                return data.isNull() ? SetData.NULL : new SetData(new LinkedHashSet<>(Collections.nCopies(1, data)));
            case Types.STRING:{
                final StringData casted = (StringData) data;
                return XTypeDataUtils.jsonToSetData(casted.value);
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_SET_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                return XTypeDataUtils.jsonToSetData(value);
            }
        }
    }
    
    public static DenseVectorData toDenseVectorData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {(double) casted.value});
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {(double) casted.value});
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {new BigDecimal(String.valueOf(casted.value)).doubleValue()});
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {casted.value});
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {casted.value});
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? DenseVectorData.NULL : new DenseVectorData(new double[] {decimal.doubleValue()});
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {casted.value ? 1d : 0d});
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {casted.real});
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {(double) casted.value});
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {(double) casted.value});
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {(double) casted.value});
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {(double) casted.value});
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {(double) casted.value});
            } 
            case Types.INSTANT:{
                final InstantData casted = (InstantData) data;
                return casted.isnull ? DenseVectorData.NULL : new DenseVectorData(new double[] {(double) casted.millis});
            } 
            case Types.LIST:{
                final ListData casted = (ListData) data;
                final int size = casted.size();
                if (size == 0) {
                    return DenseVectorData.NULL;
                }
                final double[] list = new double[size];
                int nullsize = 0;
                Double d;
                
                for (int i = 0; i < size; ++i) {
                    d = casted.items.get(i).toDouble();
                    if (d == null) {
                        list[i] = 0d;
                        ++nullsize;
                    } else {
                        list[i] = d.doubleValue();
                    }
                }
                
                return size != nullsize ? new DenseVectorData(list) : DenseVectorData.NULL;
            }
            case Types.SET: {
                final SetData casted = (SetData) data;
                final int size = casted.size();
                if (size == 0) {
                    return DenseVectorData.NULL;
                }
                final double[] list = new double[size];
                int index = 0;
                int nullsize = 0;
                Double d;
                
                for (final TypeData item : casted.items) {
                    d = item.toDouble();
                    if (d == null) {
                        list[index++] = 0d;
                        ++nullsize;
                    } else {
                        list[index++] = d.doubleValue();
                    }
                }
                return size != nullsize ? new DenseVectorData(list) : DenseVectorData.NULL;
            }
            case Types.DENSEVECTOR:
                return (DenseVectorData) data;
            case Types.SPARSEVECTOR: {
                final SparseVectorData casted = (SparseVectorData) data;
                if (casted.vsize == 0) {
                    return DenseVectorData.NULL;
                }
                final double[] list = new double[casted.vsize];
                for (int i = 0, nzlength = casted.nzlength; i < nzlength; ++i) {
                    list[casted.indices[i]] = casted.nonzeros[i];
                }
                return new DenseVectorData(list);
            }
            case Types.MAP: {
                final MapData casted = (MapData) data;
                final int size = casted.size();
                if (size == 0) {
                    return DenseVectorData.NULL;
                }
                final double[] list = new double[size];
                int index = 0;
                int nullsize = 0;
                Double d;
                
                for (final Entry<TypeData, TypeData> entry : casted.maps.entrySet()) {
                    d = entry.getValue().toDouble();
                    if (d == null) {
                        list[index++] = 0d;
                        ++nullsize;
                    } else {
                        list[index++] = d.doubleValue();
                    }
                }
                return size != nullsize ? new DenseVectorData(list) : DenseVectorData.NULL;
            }
            case Types.NULL:
                return DenseVectorData.NULL;
            case Types.BINARY: {
                final BinaryData casted = (BinaryData) data;
                if (casted.value == null || casted.value.length == 0) {
                    return DenseVectorData.NULL;
                }
                final Double d = casted.toDouble();
                return d != null ? new DenseVectorData (new double[] { d.doubleValue() }) : DenseVectorData.NULL;
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return DenseVectorData.NULL;
                }
                if (XNumberUtils.isNumber(casted.value)) {
                    final double[] list = new double[1];
                    list[0] = casted.toDouble().doubleValue();
                    return new DenseVectorData(list);
                }
                return XTypeDataUtils.jsonToDenseVectorData(casted.value);
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_DENSE_VECTOR_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return DenseVectorData.NULL;
                }
                if (XNumberUtils.isNumber(value)) {
                    final double[] list = new double[1];
                    list[0] = Double.parseDouble(value);
                    return new DenseVectorData(list);
                }
                return XTypeDataUtils.jsonToDenseVectorData(value);
            }
        }
    }
    
    public static SparseVectorData toSparseVectorData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : casted.value == 0 
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] {(double) casted.value});
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : casted.value == 0l 
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] {(double) casted.value});
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : Float.compare(casted.value, 0f) == 0 
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] {new BigDecimal(String.valueOf(casted.value)).doubleValue()});
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : Double.compare(casted.value, 0d) == 0
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { casted.value});
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : Double.compare(casted.value, 0d) == 0
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { casted.value});
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null 
                        ? SparseVectorData.NULL 
                        : decimal.signum() == 0 
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] {(double) decimal.doubleValue()});
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : casted.value ? SparseVectorData.ZERO : SparseVectorData.ONE;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : Double.compare(casted.real, 0d) == 0
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { casted.real});
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : casted.value == 0l
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { (double) casted.value});
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : casted.value == 0l
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { (double) casted.value});
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : casted.value == 0l
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { (double) casted.value});
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : casted.value == 0l
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { (double) casted.value});
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : casted.value == 0l
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { (double) casted.value});
            } 
            case Types.INSTANT:{
                final InstantData casted = (InstantData) data;
                return casted.isnull 
                        ? SparseVectorData.NULL 
                        : casted.millis == 0l
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { (double) casted.millis});
            } 
            case Types.LIST: {
                final ListData casted = (ListData) data;
                final int size = casted.size();
                if (size == 0) {
                    return SparseVectorData.NULL;
                }
                final double[] nonzeros = new double[size];
                final int[] indices = new int[size];
                int nzlength = 0;
                int nullsize = 0;
                Double d;
                
                for (int i = 0; i < size; ++i) {
                    d = casted.items.get(i).toDouble();
                    if (d == null) {
                        ++nullsize;
                    }
                    else if ( Double.compare(d.doubleValue(), 0d) != 0) {
                        indices[nzlength] = i;
                        nonzeros[nzlength++] = d;
                    }
                }
                if (size == nullsize) {
                    return SparseVectorData.NULL;
                }
                if (size != nzlength) {
                    final double[] nz = new double[nzlength];
                    final int[] id = new int[nzlength];
                    System.arraycopy(nonzeros, 0, nz, 0, nzlength);
                    System.arraycopy(indices, 0, id, 0, nzlength);
                    return new SparseVectorData(size, id, nz);
                } else {
                    return new SparseVectorData(size, indices, nonzeros);
                }
            }
            case Types.SET: {
                final SetData casted = (SetData) data;
                final int size = casted.size();
                if (size == 0) {
                    return SparseVectorData.NULL;
                }
                final double[] nonzeros = new double[size];
                final int[] indices = new int[size];
                int nzlength = 0, index = 0;
                int nullsize = 0;
                Double d;
                
                for (final TypeData item : casted.items) {
                    ++index;
                    d = item.toDouble();
                    if (d == null) {
                        ++nullsize;
                    }
                    if (Double.compare(d = item.toDouble().doubleValue(), 0d) != 0) {
                        indices[nzlength] = index;
                        nonzeros[nzlength++] = d;
                    }
                }
                if (size == nullsize) {
                    return SparseVectorData.NULL;
                }
                if (size != nzlength) {
                    final double[] nz = new double[nzlength];
                    final int[] id = new int[nzlength];
                    System.arraycopy(nonzeros, 0, nz, 0, nzlength);
                    System.arraycopy(indices, 0, id, 0, nzlength);
                    return new SparseVectorData(size, id, nz);
                } else {
                    return new SparseVectorData(size, indices, nonzeros);
                }
            }
            case Types.DENSEVECTOR: {
                final DenseVectorData casted = (DenseVectorData) data;
                final int size = casted.size();
                if (size == 0) {
                    return SparseVectorData.NULL;
                }
                final double[] nonzeros = new double[size];
                final int[] indices = new int[size];
                int nzlength = 0;
                double d;
                for (int i = 0; i < size; ++i) {
                    if (Double.compare(d = casted.items[i], 0d) != 0) {
                        indices[nzlength] = i;
                        nonzeros[nzlength++] = d;
                    }
                }
                if (size != nzlength) {
                    final double[] nz = new double[nzlength];
                    final int[] id = new int[nzlength];
                    System.arraycopy(nonzeros, 0, nz, 0, nzlength);
                    System.arraycopy(indices, 0, id, 0, nzlength);
                    return new SparseVectorData(size, id, nz);
                } else {
                    return new SparseVectorData(size, indices, nonzeros);
                }
            }
            case Types.SPARSEVECTOR:
                return (SparseVectorData) data;
            case Types.MAP: {
                final MapData casted = (MapData) data;
                final int size = casted.size();
                if (size == 0) {
                    return SparseVectorData.NULL;
                }
                final double[] nonzeros = new double[size];
                final int[] indices = new int[size];
                int nzlength = 0, index = 0;
                int nullsize = 0;
                Double d;
                
                for (final Entry<TypeData, TypeData> entry : casted.maps.entrySet()) {
                    ++index;
                    d = entry.getValue().toDouble();
                    if (d == null) {
                        ++nullsize;
                    }
                    if (Double.compare(d.doubleValue(), 0d) != 0) {
                        indices[nzlength] = index;
                        nonzeros[nzlength++] = d;
                    }
                }
                if (size == nullsize) {
                    return SparseVectorData.NULL;
                }
                if (size != nzlength) {
                    final double[] nz = new double[nzlength];
                    final int[] id = new int[nzlength];
                    System.arraycopy(nonzeros, 0, nz, 0, nzlength);
                    System.arraycopy(indices, 0, id, 0, nzlength);
                    return new SparseVectorData(size, id, nz);
                } else {
                    return new SparseVectorData(size, indices, nonzeros);
                }
            }
            case Types.NULL:
                return SparseVectorData.NULL;
            case Types.BINARY: {
                final BinaryData casted = (BinaryData) data;
                if (casted.value == null || casted.value.length == 0) {
                    return SparseVectorData.NULL;
                }
                final Double d = casted.toDouble();
                return d == null 
                        ? SparseVectorData.NULL 
                        : Double.compare(d.doubleValue(), 0d) == 0
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { d });
            }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                if (XStringUtils.isBlank(casted.value)) {
                    return SparseVectorData.NULL;
                }
                if (XNumberUtils.isNumber(casted.value)) {
                    final Double d = casted.toDouble();
                    return Double.compare(d.doubleValue(), 0d) == 0
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { d });
                }
                return XTypeDataUtils.jsonToSparseVectorData(casted.value);
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_SPARSE_VECTOR_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return SparseVectorData.NULL;
                }
                if (XNumberUtils.isNumber(value)) {
                    final Double d = Double.parseDouble(value);
                    return Double.compare(d.doubleValue(), 0d) == 0
                            ? SparseVectorData.ZERO 
                            : new SparseVectorData(1, new int[] {0}, new double[] { d });
                }
                return XTypeDataUtils.jsonToSparseVectorData(value);
            }
        }
    }
    
    public static MapData toMapData(final TypeData data) throws EngineException {
        switch(data.getType()) {
            case Types.INT:
            case Types.LONG:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.BOOLEAN:
            case Types.COMPLEX:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP: 
            case Types.INSTANT:
                if (data.isNull()) {
                    return MapData.NULL;
                } else {
                    final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>(1);
                    map.put(IntData.ZERO, data);
                    return new MapData(map);
                }
            case Types.LIST:{
                final ListData casted = (ListData) data;
                final int size = casted.size();
                if (size == 0) {
                    return MapData.NULL;
                }
                final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>(size);
                int index = 0;
                for (final TypeData item : casted.items) {
                    map.put(IntData.nonNullValueOf(index++), item);
                }
                return new MapData(map);
            }
            case Types.SET:{
                final SetData casted = (SetData) data;
                final int size = casted.size();
                if (size == 0) {
                    return MapData.NULL;
                }
                final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>(size);
                int index = 0;
                for (final TypeData item : casted.items) {
                    map.put(IntData.nonNullValueOf(index++), item);
                }
                return new MapData(map);
            }
            case Types.DENSEVECTOR:{
                final DenseVectorData casted = (DenseVectorData) data;
                final int size = casted.size();
                if (size == 0) {
                    return MapData.NULL;
                }
                final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>(size);
                int index = 0;
                for (final double item : casted.items) {
                    map.put(IntData.nonNullValueOf(index++), DoubleData.valueOf(item));
                }
                return new MapData(map);
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData casted = (SparseVectorData) data;
                final int size = casted.vsize;
                if (size == 0) {
                    return MapData.NULL;
                }
                final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>(size);
                if (casted.nzlength == 0) {
                    for (int i = 0; i < size; ++i) {
                        map.put(IntData.nonNullValueOf(i), DoubleData.ZERO);
                    }
                    return new MapData(map);
                }
                
                int index = 0, nzindex = 0, nz = casted.indices[nzindex];
                    
                while (true) {
                    if (index++ >= nz) {
                        map.put(IntData.valueOf(index - 1), DoubleData.nonNullValueOf(casted.nonzeros[nzindex]));
                        if (nzindex + 1 < casted.nzlength) {
                            nz = casted.indices[++nzindex];
                        } else {
                            break;
                        }
                    } else {
                        map.put(IntData.nonNullValueOf(index -1), DoubleData.ZERO);
                    }
                }
                for (int i = index; i < size; ++i) {
                    map.put(IntData.nonNullValueOf(i), DoubleData.ZERO);
                }
                return new MapData(map);
            }
            case Types.MAP:
                return (MapData) data;
            case Types.NULL:
                return MapData.NULL;
            case Types.BINARY: 
                if (data.isNull()) {
                    return MapData.NULL;
                } else {
                    final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>(1);
                    map.put(IntData.ZERO, data);
                    return new MapData(map);
                }
            case Types.STRING:{
                final StringData casted = (StringData) data;
                return XTypeDataUtils.jsonToMapData(casted.value);
            }
            case Types.EXPRESSION:
                throw new DataConversionException ("DATA_EXPRESSION_TO_MAP_CONVERSION_INVALID");
            default:{
                final String value = data.toString();
                return XTypeDataUtils.jsonToMapData(value);
            }
        }
    }
    
    public static StringData toStringData(final TypeData data) {
        switch(data.getType()) {
            case Types.INT: {
                final IntData casted = (IntData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(String.valueOf(casted.value));
            }
            case Types.LONG: {
                final LongData casted = (LongData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(String.valueOf(casted.value));
            }
            case Types.FLOAT: {
                final FloatData casted = (FloatData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(String.valueOf(casted.value));
            }
            case Types.DOUBLE: {
                final DoubleData casted = (DoubleData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(String.valueOf(casted.value));
            }
            case Types.NUMERIC: {
                final NumericData casted = (NumericData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(String.valueOf(casted.value));
            }
            case Types.DECIMAL: {
                final BigDecimal decimal = ((DecimalData) data).value;
                return decimal == null ? StringData.NULL : StringData.nonNullValueOf(decimal.toString());
            }
            case Types.BOOLEAN: {
                final BooleanData casted = (BooleanData) data;
                return casted.isnull ? StringData.NULL : casted.value ? StringData.ONE : StringData.ZERO;
            }
            case Types.COMPLEX: {
                final ComplexData casted = (ComplexData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            }
            case Types.DATE: {
                final DateData casted = (DateData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            }
            case Types.TIME: {
                final TimeData casted = (TimeData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            }
            case Types.TIMESTAMP: {
                final TimestampData casted = (TimestampData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            }
            case Types.CALENDAR_TIME: {
                final CalendarTimeData casted = (CalendarTimeData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            }
            case Types.CALENDAR_TIMESTAMP: {
                final CalendarTimestampData casted = (CalendarTimestampData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            } 
            case Types.INSTANT: {
                final InstantData casted = (InstantData) data;
                return casted.isnull ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            } 
            case Types.LIST: {
                final ListData casted = (ListData) data;
                return casted.isEmpty() ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            }
            case Types.SET: {
                final SetData casted = (SetData) data;
                return casted.isEmpty() ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            }
            case Types.DENSEVECTOR: {
                final DenseVectorData casted = (DenseVectorData) data;
                return casted.isEmpty() ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData casted = (SparseVectorData) data;
                return casted.isEmpty() ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            }
            case Types.MAP: {
                final MapData casted = (MapData) data;
                return casted.isEmpty() ? StringData.NULL : StringData.nonNullValueOf(casted.toString());
            }
            case Types.NULL:
                return StringData.NULL;
            case Types.BINARY: {
                final BinaryData casted = (BinaryData) data;
                if (casted.value == null || casted.value.length == 0) {
                    return StringData.NULL;
                }
                return StringData.nonNullValueOf(BinaryUtils.deserializeToString(casted.value));
            }
            case Types.STRING:
                return (StringData) data;
            case Types.EXPRESSION:
                return new StringData(data.toString());
            default:{
                final String value = data.toString();
                if (XStringUtils.isBlank(value)) {
                    return StringData.NULL;
                }
                return StringData.nonNullValueOf(value);
            }
        }
    }
    
    public static TypeData convert(final TypeData data, final byte type) throws EngineException {
        switch (type) {
            case Types.INT: return toIntData(data);
            case Types.LONG: return toLongData(data);
            case Types.FLOAT: return toFloatData(data);
            case Types.DOUBLE: return toDoubleData(data);
            case Types.NUMERIC: return toNumericData(data);
            case Types.DECIMAL: return toDecimalData(data);
            case Types.COMPLEX: return toComplexData(data);
            case Types.BOOLEAN: return toBooleanData(data);
            case Types.DATE: return toDateData(data);
            case Types.TIME: return toTimeData(data);
            case Types.TIMESTAMP: return toTimestampData(data);
            case Types.CALENDAR_TIME: return toCalendarTimeData(data);
            case Types.CALENDAR_TIMESTAMP: return toCalendarTimestampData(data);
            case Types.INSTANT: return toInstantData(data);
            case Types.BINARY: return toBinaryData(data);
            case Types.STRING: return toStringData(data);
            case Types.LIST: return toListData(data);
            case Types.SET: return toSetData(data);
            case Types.MAP: return toMapData(data);
            case Types.DENSEVECTOR: return toDenseVectorData(data);
            case Types.SPARSEVECTOR: return toSparseVectorData(data);
            case Types.EXPRESSION:
            default:
                return data;
        }
    }
}
