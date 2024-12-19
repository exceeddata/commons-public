package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.util.XNumberUtils;
import com.exceeddata.ac.common.util.XStringUtils;

/**
 * A data class for Long.
 *
 */
public final class LongData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final LongData NULL = new LongData();
    public static final LongData ZERO = new LongData(0l, false);
    public static final LongData ONE = new LongData(1l, false);
    public static final LongData NEGATIVE_ONE = new LongData(-1l, false);
    public static final LongData MAX_INT_VALUE = new LongData(Long.valueOf(Integer.MAX_VALUE), false);
    public static final LongData MIN_INT_VALUE = new LongData(Long.valueOf(Integer.MIN_VALUE), false);
    public static final LongData MAX_VALUE = new LongData(Long.MAX_VALUE, false);
    public static final LongData MIN_VALUE = new LongData(Long.MIN_VALUE, false);
    
    protected boolean isnull = true;
    protected long value = 0l;
    
    public LongData() {
    }
    
    /**
     * Construct a <code>LongData</code> with Long
     * 
     * @param value Long
     */
    public LongData(final Long value) {
        if (value != null) {
            this.isnull =false;
            this.value = value.longValue();
        }
    }
    
    protected LongData (final long value, final boolean isnull) {
        this.value = value;
        this.isnull = isnull;
    }
    
    /**
     * Construct a <code>LongData</code> with String
     * 
     * @param value String
     */
    public LongData(final String value) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                this.value = Long.valueOf(value).longValue();
                this.isnull =false;
            } catch (NumberFormatException e) {
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public LongData clone() {
        return this; //immutable no need to clone
    }
    
    /** {@inheritDoc} */
    @Override
    public LongData copy() {
        return this; //immutable, no need to copy
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public LongData abs() {
        return isnull ? LongData.NULL : value >= 0l ? this : LongData.nonNullValueOf(-value);
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public LongData negate() {
        return isnull ? LongData.NULL : LongData.nonNullValueOf(-value);
    }
    
    /** {@inheritDoc} */
    @Override
    public LongData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Long getObject() {
        return isnull ? null : value;
    }
    
    /**
     * Get Long value.
     * 
     * @return Long
     */
    public Long getLong() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType() {
        return Types.LONG;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return isnull ? 0 : 1;
        } else if (isnull) {
            return -1;
        }
        return DataCompare.longCompareTo(this, w);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TypeData) {
            final TypeData anydata = (TypeData) obj;
            return isnull 
                    ? anydata.isNull() 
                    : anydata.getType() == Types.LONG && !anydata.isNull() && value == ((LongData) anydata).value;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (isnull) {
            out.writeByte(XNumberUtils.BYTE_NULL);
            return;
        } else if (value == 0l) {
            out.writeByte(XNumberUtils.BYTE_ZERO);
            return;
        }
        
        //max byte is 127, max short is 32767
        if (value >> 15 == 0) {
            if (value >> 7 == 0) {
                out.writeByte(XNumberUtils.BYTE_ONE); 
                out.writeByte((int) value);
            } else {
                out.writeByte(XNumberUtils.BYTE_TWO); 
                out.writeShort((int) value);
            }
        } else {
            if (value >> 31 == 0) {
                out.writeByte(XNumberUtils.BYTE_FOUR); 
                out.writeInt((int) value);
            } else {
                out.writeByte(XNumberUtils.BYTE_EIGHT); 
                out.writeLong(value);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        switch(in.readByte()) {
            case XNumberUtils.BYTE_NULL:     isnull = true;  value = 0l; return;
            case XNumberUtils.BYTE_ZERO:     isnull = false; value = 0l; return;
            case XNumberUtils.BYTE_ONE:      isnull = false; value = in.readByte(); break;
            case XNumberUtils.BYTE_TWO:      isnull = false; value = in.readShort(); break;
            case XNumberUtils.BYTE_FOUR:     isnull = false; value = in.readInt(); break;
            default:                        isnull = false; value = in.readLong();
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static LongData readData(final DataInput in) throws IOException {
        switch(in.readByte()) {
            case XNumberUtils.BYTE_NULL:     return LongData.NULL;
            case XNumberUtils.BYTE_ZERO:     return LongData.ZERO;
            case XNumberUtils.BYTE_ONE:      return DataCache.getOrPutLongData((long) in.readByte());
            case XNumberUtils.BYTE_TWO:      return DataCache.getOrPutLongData((long) in.readShort());
            case XNumberUtils.BYTE_FOUR:     return DataCache.getOrPutLongData((long) in.readInt());
            default:                        return DataCache.getOrPutLongData(in.readLong());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<LongData> iterator() {
        return Arrays.asList(new LongData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public LongData first() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() {
        return isnull ? null : (int) value;
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() {
        return isnull ? null : (float) value;
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() {
        return isnull ? null : (double) value;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() {
        return isnull ? null : BigDecimal.valueOf(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        return isnull ? null : value != 0l;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("deprecation")
    @Override
    public java.sql.Date toDate() {
        if (isnull) {
            return null;
        } else {
            final java.sql.Date dt = new java.sql.Date(value);
            return new java.sql.Date(dt.getYear(), dt.getMonth(), dt.getDate());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() {
        return isnull ? null : new java.sql.Time(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() {
        return isnull ? null : new java.sql.Timestamp(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(value);
            cal.set(GregorianCalendar.YEAR, 1970);
            cal.set(GregorianCalendar.MONTH, 0);
            cal.set(GregorianCalendar.DATE, 1);
            return cal;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(value);
            return cal;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() {
        return isnull ? null : Instant.ofEpochMilli(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Long> toList() {
        return isnull ? null : Arrays.asList(new Long[] {value});
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Long> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<Long> set = new LinkedHashSet<Long>(1);
        set.add(value);
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, Long> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, Long> map = new LinkedHashMap<>(1);
        map.put(0, value);
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return isnull 
                ? null
                : new byte[] {
                    (byte)(value >>> 56),
                    (byte)(value >>> 48),
                    (byte)(value >>> 40),
                    (byte)(value >>> 32),
                    (byte)(value >>> 24),
                    (byte)(value >>> 16),
                    (byte)(value >>> 8),
                    (byte) value};
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return isnull ? StringData.EMPTY_STRING : String.valueOf(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public int size() {
        return 1;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNull() {
        return isnull;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return isnull;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isDigits() {
        return !isnull;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return !isnull;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return isnull ? TypeUtils.NULL_HASH : Long.hashCode(value);
    }
    
    public static final LongData valueOf(final Long value) {
        return value == null ? LongData.NULL : DataCache.getOrPutLongData(value);
    }
    
    public static final LongData valueOf(final Long value, final long defaultValue) {
        return DataCache.getOrPutLongData(value == null ? defaultValue : value);
    }
    
    public static final LongData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return LongData.NULL;
        } else {
            try {
                return DataCache.getOrPutLongData(XNumberUtils.isDigits(value) ? Long.valueOf(value) : new BigDecimal(value).longValue());
            } catch (NumberFormatException e) {
                return LongData.NULL;
            }
        }
    }
    
    public static final LongData nonNullValueOf(final long value) {
        return DataCache.getOrPutLongData(value);
    }
    
    public static final LongData random(long base) {
        return new LongData((long) (Math.random() * base), false);
    }
}
