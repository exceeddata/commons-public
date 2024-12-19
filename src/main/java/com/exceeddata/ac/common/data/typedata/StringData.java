package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.exception.EngineDataException;
import com.exceeddata.ac.common.exception.data.DataConversionException;
import com.exceeddata.ac.common.util.XNumberUtils;
import com.exceeddata.ac.common.util.XStringUtils;
import com.exceeddata.ac.common.util.XTemporalUtils;
import com.exceeddata.ac.common.util.XTypeDataUtils;

/**
 * A data class for String.
 *
 */
public final class StringData implements TypeData {
    private static final long serialVersionUID = 1L;
    public static final String EMPTY_STRING = "";
    public static final StringData NULL = new StringData();
    public static final StringData EMPTY = new StringData(EMPTY_STRING);
    public static final StringData ZERO = new StringData("0");
    public static final StringData ONE = new StringData("1");
    
    protected String value = null;
    
    public StringData() {
    }
    
    /**
     * Construct a <code>StringData</code> with String
     * 
     * @param value String
     */
    public StringData(final String value) {
        this.value = value;
    }
    
    protected StringData(final byte[] value) {
        this.value = new String(value, StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public StringData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public StringData copy() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public StringData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getObject() {
        return value;
    }
    
    /**
     * Get String value.
     * 
     * @return String
     */
    public String getString() {
        return value;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType() {
        return Types.STRING;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.stringCompareTo(this, w);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TypeData) {
            final TypeData anydata = (TypeData) obj;
            return value == null 
                    ? anydata.isNull() 
                    : anydata.getType() == Types.STRING && value.equals(((StringData) anydata).value);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (value==null) {
            out.writeByte(XNumberUtils.BYTE_NULL);
            return;
        } else if (value.length() == 0) {
            out.writeByte(XNumberUtils.BYTE_ZERO);
            return;
        }
        
        final byte[] b = value.getBytes(StandardCharsets.UTF_8);
        final int length = b.length;
        //max byte is 127, max short is 32767
        if (length >> 7 == 0) {
            out.writeByte(XNumberUtils.BYTE_ONE); 
            out.writeByte(length);
        } else {
            if (length >> 15 == 0) {
                out.writeByte(XNumberUtils.BYTE_TWO); 
                out.writeShort(length);
            } else {
                out.writeByte(XNumberUtils.BYTE_FOUR); 
                out.writeInt(length);
            }
        }
        out.write(b, 0, length);
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        final int len;
        switch(in.readByte()) {
            case XNumberUtils.BYTE_NULL:     value = null; return;
            case XNumberUtils.BYTE_ZERO:     value = EMPTY_STRING; return;
            case XNumberUtils.BYTE_ONE:      len = in.readByte(); break;
            case XNumberUtils.BYTE_TWO:      len = in.readShort(); break;
            default:                        len = in.readInt();
        }
        final byte[] bytes = new byte[len];
        in.readFully(bytes, 0, len);
        
        value = new String(bytes, StandardCharsets.UTF_8);
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static StringData readData(final DataInput in) throws IOException {
        final int len;
        switch(in.readByte()) {
            case XNumberUtils.BYTE_NULL:     return StringData.NULL;
            case XNumberUtils.BYTE_ZERO:     return StringData.EMPTY;
            case XNumberUtils.BYTE_ONE:      len = in.readByte(); break;
            case XNumberUtils.BYTE_TWO:      len = in.readShort(); break;
            default:                        len = in.readInt();
        }
        if (len == 0) {
            return StringData.EMPTY;
        } else {
            final byte[] b = new byte[len];
            in.readFully(b, 0, len);
            
            final String s = new String(b, StandardCharsets.UTF_8);
            return DataCache.getOrPutStringData(s);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<StringData> iterator() {
        return Arrays.asList(new StringData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public StringData first() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() throws EngineDataException {
        try {
            return XStringUtils.isNotBlank(value) ? (int) Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            throw new DataConversionException(e.getMessage(), e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() throws EngineDataException {
        try {
            return XStringUtils.isNotBlank(value) ? (long) Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            throw new DataConversionException(e.getMessage(), e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() throws EngineDataException {
        try {
            return XStringUtils.isNotBlank(value) ? Float.valueOf(value) : null;
        } catch (NumberFormatException e) {
            throw new DataConversionException(e.getMessage(), e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() throws EngineDataException {
        try {
            return XStringUtils.isNotBlank(value) ? Double.valueOf(value) : null;
        } catch (NumberFormatException e) {
            throw new DataConversionException(e.getMessage(), e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() throws EngineDataException {
        try {
            return XStringUtils.isNotBlank(value) ? new BigDecimal(value) : null;
        } catch (NumberFormatException e) {
            throw new DataConversionException(e.getMessage(), e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        if (XStringUtils.isNotBlank(value)) {
            switch (value.trim().toLowerCase()) {
                case "false":
                case "no":
                case "0": 
                case "否":
                    return Boolean.FALSE;
                default: return Boolean.TRUE;
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() throws EngineDataException {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Long result = XTemporalUtils.parseDate(value);
                return result != null ? new java.sql.Date(result.longValue()) : null;
            } catch (DateTimeParseException e) {
                throw new DataConversionException (e.getMessage(), e);
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() throws EngineDataException {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Long result = XTemporalUtils.parseTime(value);
                return result != null ? new java.sql.Time(result.longValue()) : null;
            } catch (DateTimeParseException e) {
                throw new DataConversionException (e.getMessage(), e);
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() throws EngineDataException {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Long result = XTemporalUtils.parseTimestamp(value);
                return result != null ? new java.sql.Timestamp(result.longValue()) : null;
            } catch (DateTimeParseException e) {
                throw new DataConversionException (e.getMessage(), e);
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() throws EngineDataException {
        if (XStringUtils.isNotBlank(value)) {
            final ZonedDateTime zdt = XTemporalUtils.parseCalendarTime(value);
            if (zdt != null) {
                return GregorianCalendar.from(zdt);
            }
        }
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() throws EngineDataException {
        if (XStringUtils.isNotBlank(value)) {
            final ZonedDateTime zdt = XTemporalUtils.parseCalendarTimestamp(value);
            if (zdt != null) {
                return GregorianCalendar.from(zdt);
            }
        }
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() throws EngineDataException {
        if (XStringUtils.isNotBlank(value)) {
            try {
                return XTemporalUtils.parseInstant(value);
            } catch (DateTimeParseException e) {
                throw new DataConversionException (e.getMessage(), e);
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Object> toList() {
        return XTypeDataUtils.jsonToListObject(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Object> toSet() {
        return XTypeDataUtils.jsonToSetObject(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<String, Object> toMap() {
        return XTypeDataUtils.jsonToMapObject(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return value != null ? value.getBytes() : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return value != null ? value : EMPTY_STRING;
    }
    
    /** {@inheritDoc} */
    @Override
    public int size() {
        return 1;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNull() {
        return value == null;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return value == null || value.length() == 0;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isDigits() {
        return XNumberUtils.isDigits(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return XNumberUtils.isNumber(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        if (value == null) {
            return TypeUtils.NULL_HASH;
        }
        
        int state = 0, length = value.length();
        for (int i = 0; i < length; ++i) {
            state = state * 109 + value.charAt(i);
        }
        
        return state;
    }
    
    /**
     * Return an cached instance of StringData.
     * 
     * @param value the string
     * @return StringData
     */
    public static final StringData valueOf(final String value) {
        if (value == null) {
            return StringData.NULL;
        }
        
        return DataCache.getOrPutStringData(value);
    }
    
    public static final StringData nonNullValueOf(final String value) {
        return DataCache.getOrPutStringData(value);
    }
    
    /**
     * Return a non-cached instance of StringData (always new) when String is not empty.
     * 
     * @param value the string
     * @return StringData
     */
    public static final StringData nonCacheValueOf(final String value) {
        return value == null ? StringData.NULL : value.length() == 0 ? StringData.EMPTY : new StringData(value);
    }
    
    /**
     * Fast method for always cache, use for values.
     * 
     * @param value the value
     * @return StringData
     */
    public static final StringData valueCacheOf(final String value) {
        return DataCache.getOrPutStringData(value);
    }
    
    /**
     * Return an cached instance of StringData when value is known to be non-empty.
     * 
     * @param value the string in bytes
     * @return StringData
     */
    public static final StringData valueOf(final byte[] value) {
        if (value == null) {
            return StringData.NULL;
        } else if (value.length == 0) {
            return StringData.EMPTY;
        } else {
            final String s = new String(value, StandardCharsets.UTF_8);
            return DataCache.getOrPutStringData(s);
        }
    }
    
    /**
     * Return an cached instance of StringData when value is known to be non-empty.
     * 
     * @param value the string in bytes
     * @param charset the charset
     * @return StringData
     */
    public static final StringData valueOf(final byte[] value, final Charset charset) {
        if (value == null) {
            return StringData.NULL;
        } else if (value.length == 0) {
            return StringData.EMPTY;
        } else {
            final String s = new String(value, charset);
            return DataCache.getOrPutStringData(s);
        }
    }
    
    /**
     * Return an non-cached instance of StringData.
     * 
     * @param value the string in bytes
     * @return StringData
     */
    public static final StringData nonCacheValueOf(final byte[] value) {
        return value == null ? StringData.NULL : value.length == 0 ? StringData.EMPTY : new StringData(new String(value, StandardCharsets.UTF_8));
    }
    
    /**
     * Return an non-cached instance of StringData.
     * 
     * @param value the string in bytes
     * @param charset the charset
     * @return StringData
     */
    public static final StringData nonCacheValueOf(final byte[] value, final Charset charset) {
        return value == null ? StringData.NULL : value.length == 0 ? StringData.EMPTY : new StringData(new String(value, charset));
    }
}
