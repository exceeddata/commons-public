package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.util.XStringUtils;

/**
 * A data class for Float.
 *
 */
public final class FloatData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final FloatData NULL = new FloatData();
    public static final FloatData ZERO = new FloatData(0f, false);
    public static final FloatData ONE = new FloatData(1f, false);
    public static final FloatData NEGATIVE_ONE = new FloatData(-1f, false);
    public static final FloatData POSITIVE_INFINITY = new FloatData(Float.POSITIVE_INFINITY, false);
    public static final FloatData NEGATIVE_INFINITY = new FloatData(Float.NEGATIVE_INFINITY, false);
    
    protected boolean isnull = true;
    protected float value = 0f;
    
    public FloatData() {
    }
    
    /**
     * Construct a <code>FloatData</code> with Float
     * 
     * @param value Float
     */
    public FloatData(final Float value) {
        if (value != null) {
            this.isnull =false;
            this.value = value.floatValue();
        }
    }
    
    private FloatData (final float value, final boolean isnull) {
        this.value = value;
        this.isnull = isnull;
    }
      
    
    /**
     * Construct a <code>FloatData</code> with String
     * 
     * @param value String
     */
    public FloatData(final String value) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                this.value = Float.valueOf(value).floatValue();
                this.isnull = false;
            } catch (NumberFormatException e) {
            }
        }
    }
    
    /**
     * Construct a <code>FloatData</code> with FloatData
     * 
     * @param data the FloatData
     */
    public FloatData(final FloatData data) {
        if (data != null) { 
            this.isnull = data.isnull;
            this.value = data.value;
        }
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public FloatData abs() {
        return isnull ? FloatData.NULL : value >= 0f ? this : new FloatData(-value, false);
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public FloatData negate() {
        return isnull ? FloatData.NULL : new FloatData(-value, false);
    }
    
    /** {@inheritDoc} */
    @Override
    public FloatData clone() {
        return isnull ? FloatData.NULL : FloatData.valueOf(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public FloatData copy() {
        return isnull ? FloatData.NULL : FloatData.valueOf(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public FloatData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Float getObject() {
        return isnull ? null : value;
    }
    
    /**
     * Get Float value.
     * 
     * @return Float
     */
    public Float getFloat() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType() {
        return Types.FLOAT;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.floatCompareTo(this, w);
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
                    : anydata.getType() == Types.FLOAT && !anydata.isNull() && Float.compare(value, ((FloatData) anydata).value) == 0;
        }
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (isnull) {
            out.writeBoolean(true);
        } else {
            out.writeBoolean(false);
            out.writeFloat(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        if (!(isnull = in.readBoolean())) {
            value = in.readFloat();
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static FloatData readData(final DataInput in) throws IOException {
        if (in.readBoolean()) {
            return FloatData.NULL;
        } else {
            return FloatData.nonNullValueOf(in.readFloat());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<FloatData> iterator() {
        return Arrays.asList(new FloatData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public FloatData first() {
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
        return isnull ? null : (long) value;
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() {
        return isnull ? null : new BigDecimal(String.valueOf(value)).doubleValue();
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() {
        return isnull ? null : new BigDecimal (String.valueOf(value));
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        return isnull ? null : value != 0f;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("deprecation")
    @Override
    public java.sql.Date toDate() {
        if (isnull) {
            return null;
        } else {
            final java.sql.Date dt = new java.sql.Date((long) value);
            return new java.sql.Date(dt.getYear(), dt.getMonth(), dt.getDate());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() {
        return isnull ? null : new java.sql.Time((long) value);
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() {
        return isnull ? null : new java.sql.Timestamp((long) value);
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis((long) value);
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
            cal.setTimeInMillis((long) value);
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
    public List<Float> toList() {
        if (isnull) {
            return null;
        }
        final ArrayList<Float> list = new ArrayList<>(1);
        list.add(value);
        return list;
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Float> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<Float> set = new LinkedHashSet<>(1);
        set.add(value);
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, Float> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, Float> map = new LinkedHashMap<>(1);
        map.put(0, value);
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        if (isnull) {
            return null;
        }
        
        final int v = Float.floatToIntBits(value);
        return new byte[] {
                    (byte)(v >>> 24),
                    (byte)(v >>> 16),
                    (byte)(v >>> 8),
                    (byte) v};
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        if (isnull) {
            return StringData.EMPTY_STRING;
        } else {
            final int i = (int) value;
            return value != i ? String.valueOf(value) : String.valueOf(i);
        }
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
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return !isnull;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return isnull ? TypeUtils.NULL_HASH : Float.hashCode(value);
    }
    
    public static final FloatData valueOf(final Float value) {
        return value == null ? FloatData.NULL : value == 0f ? FloatData.ZERO : value == 1f ? FloatData.ONE : new FloatData(value);
    }
    
    public static final FloatData valueOf(final Float value, final float defaultValue) {
        return value == null ? new FloatData(defaultValue, false) : value == 0f ? FloatData.ZERO : value == 1f ? FloatData.ONE : new FloatData(value);
    }
    
    public static final FloatData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return FloatData.NULL;
        } else {
            try {
                final float f = Float.valueOf(value.trim());
                return f == 0f ? FloatData.ZERO : f == 1f ? FloatData.ONE : new FloatData(value);
            } catch (NumberFormatException e) {
                return FloatData.NULL;
            }
        }
    }
    
    public static final FloatData nonNullValueOf(final float value) {
        return value == 0f ? FloatData.ZERO : value == 1f ? FloatData.ONE : new FloatData(value, false);
    }
    
    public static final FloatData random(float base) {
        return new FloatData((float) (Math.random() * base), false);
    }
}
