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
 * A data class for Integer.
 *
 */
public final class IntData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final IntData NULL = new IntData();
    public static final IntData ZERO = new IntData(0, false);
    public static final IntData ONE = new IntData(1, false);
    public static final IntData TWO = new IntData(2, false);
    public static final IntData TEN = new IntData(10, false);
    public static final IntData NEGATIVE_ONE = new IntData(-1, false);
    public static final IntData MAX_VALUE = new IntData(Integer.MAX_VALUE, false);
    public static final IntData MIN_VALUE = new IntData(Integer.MIN_VALUE, false);
                        
    protected boolean isnull = true;
    protected int value = 0;
    
    public IntData() { 
    }
    
    /**
     * Construct a <code>IntData</code> with Integer
     * 
     * @param value Integer
     */
    public IntData (final Integer value) {
        if (value != null) {
            this.isnull =false;
            this.value = value.intValue();
        }
    }
    
    protected IntData (final int value, final boolean isnull) {
        this.value = value;
        this.isnull = isnull;
    }
    
    /**
     * Construct a <code>IntData</code> with String
     * 
     * @param value String
     */
    public IntData(final String value) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                this.value = Integer.valueOf(value);
                this.isnull =false;
            } catch (NumberFormatException e) {
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public IntData clone() {
        return this; // immutable, no need to clone
    }
    
    /** {@inheritDoc} */
    @Override
    public IntData copy() {
        return this; //immutable, no need to copy
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public IntData abs() {
        return isnull ? IntData.NULL : value >= 0 ? this : IntData.nonNullValueOf(-value);
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public IntData negate() {
        return isnull ? IntData.NULL : IntData.nonNullValueOf(-value);
    }
    
    /** {@inheritDoc} */
    @Override
    public IntData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer getObject() {
        return isnull ? null : value;
    }
    
    /**
     * Get Integer value.
     * 
     * @return Integer
     */
    public Integer getInteger() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.INT;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.intCompareTo(this, w);
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
                    : anydata.getType() == Types.INT && !anydata.isNull() && value == ((IntData) anydata).value;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (isnull) {
            out.writeByte(XNumberUtils.BYTE_NULL);
            return;
        } else if (value == 0) {
            out.writeByte(XNumberUtils.BYTE_ZERO);
            return;
        }
        //max byte is 127, max short is 32767
        if (value >> 7 == 0) {
            out.writeByte(XNumberUtils.BYTE_ONE); 
            out.writeByte(value);
        } else {
            if (value >> 15 == 0) {
                out.writeByte(XNumberUtils.BYTE_TWO); 
                out.writeShort(value);
            } else {
                out.writeByte(XNumberUtils.BYTE_FOUR); 
                out.writeInt(value);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        switch(in.readByte()) {
            case XNumberUtils.BYTE_NULL:     isnull = true;  value = 0; return;
            case XNumberUtils.BYTE_ZERO:     isnull = false; value = 0; return;
            case XNumberUtils.BYTE_ONE:      isnull = false; value = in.readByte(); break;
            case XNumberUtils.BYTE_TWO:      isnull = false; value = in.readShort(); break;
            default:                        isnull = false; value = in.readInt();
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static IntData readData(final DataInput in) throws IOException {
        switch(in.readByte()) {
            case XNumberUtils.BYTE_NULL:     return IntData.NULL;
            case XNumberUtils.BYTE_ZERO:     return IntData.ZERO;
            case XNumberUtils.BYTE_ONE:      return DataCache.getOrPutIntData((int) in.readByte());
            case XNumberUtils.BYTE_TWO:      return DataCache.getOrPutIntData((int) in.readShort());
            default:                        return DataCache.getOrPutIntData(in.readInt());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<IntData> iterator() {
        return Arrays.asList(new IntData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public IntData first() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() {
        return isnull ? null : (long) value;
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() {
        return isnull ? null : Float.valueOf(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() {
        return isnull ? null : Double.valueOf(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() {
        return isnull ? null : BigDecimal.valueOf(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        return isnull ? null : value != 0;
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
        return isnull ? null : Instant.ofEpochMilli((long) value);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Integer> toList() {
        return isnull ? null : Arrays.asList(new Integer[] {value});
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Integer> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<Integer> set = new LinkedHashSet<>(1);
        set.add(value);
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, Integer> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>(1);
        map.put(0, value);
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return isnull 
                ? null
                : new byte[] {
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
        return isnull ? TypeUtils.NULL_HASH : Integer.hashCode(value);
    }
    
    public static final IntData valueOf(final Integer value) {
        return value == null ? IntData.NULL : DataCache.getOrPutIntData(value);
    }
    
    public static final IntData valueOf(final Integer value, final int defaultValue) {
        return DataCache.getOrPutIntData(value == null ? defaultValue : value);
    }
    
    public static final IntData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return IntData.NULL;
        }
        try {
            return DataCache.getOrPutIntData(XNumberUtils.isDigits(value) ? Long.valueOf(value).intValue() : new BigDecimal(value).intValue());
        } catch (NumberFormatException e) {
            return IntData.NULL;
        }
    }
    
    public static final IntData nonNullValueOf(final int value) {
        return DataCache.getOrPutIntData(value);
    }
    
    public static final IntData random(int base) {
        return new IntData((int) (Math.random() * base));
    }
}
