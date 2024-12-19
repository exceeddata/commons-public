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
 * A data class for Numeric (up to 6-decimal precision floating number).
 *
 */
public final class NumericData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    protected static final int MULTIPLIER = 1000000;
    public static final int SCALE = 6;
    public static final NumericData NULL = new NumericData();
    public static final NumericData ZERO = new NumericData(0d);
    public static final NumericData ONE = new NumericData(1d);
    public static final NumericData NEGATIVE_ONE = new NumericData(-1d);
    public static final NumericData POSITIVE_INFINITY = new NumericData(Float.POSITIVE_INFINITY);
    public static final NumericData NEGATIVE_INFINITY = new NumericData(Float.NEGATIVE_INFINITY);

    protected long unscaledValue = 0l;
    protected double value = 0d;
    protected boolean isnull = true;
    
    public NumericData() {
    }

    public NumericData(final float value) {
        this.unscaledValue = Math.round((double) value * MULTIPLIER);
        this.value = BigDecimal.valueOf(unscaledValue, SCALE).doubleValue();
        this.isnull = false;
    }

    public NumericData(final double value) {
        this.unscaledValue = Math.round(value * MULTIPLIER);
        this.value = BigDecimal.valueOf(unscaledValue, SCALE).doubleValue();
        this.isnull = false;
    }

    protected NumericData(final long unscaledValue) {
        this.unscaledValue = unscaledValue;
        this.value = BigDecimal.valueOf(unscaledValue, SCALE).doubleValue();
        this.isnull = false;
    }
    
    /**
     * Construct a <code>NumericData</code> with String
     * 
     * @param value String
     */
    public NumericData(final String value) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                this.unscaledValue = Math.round(Double.valueOf(value).doubleValue() * MULTIPLIER);
                this.value = BigDecimal.valueOf(unscaledValue, SCALE).doubleValue();
                this.isnull = false;
            } catch (NumberFormatException e) {
            }
        }
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public NumericData abs() {
        return isnull ? NumericData.NULL : unscaledValue >= MULTIPLIER ? this : DataCache.getOrPutNumericData(-unscaledValue);
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public NumericData negate() {
        return isnull ? NumericData.NULL : DataCache.getOrPutNumericData(-unscaledValue);
    }
    
    /** {@inheritDoc} */
    @Override
    public NumericData clone() {
        return this; //immutable, no need to clone
    }
    
    /** {@inheritDoc} */
    @Override
    public NumericData copy() {
        return this; //immutable, no need to copy
    }
    
    /** {@inheritDoc} */
    @Override
    public NumericData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Double getObject() {
        return isnull ? null : value;
    }
    
    /**
     * Get Number value.
     * 
     * @return Double
     */
    public Double getDouble() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType() {
        return Types.NUMERIC;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.numericCompareTo(this, w);
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
                    : anydata.getType() == Types.NUMERIC && !anydata.isNull() && Long.compare(unscaledValue, ((NumericData) anydata).unscaledValue) == 0;
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
            out.writeLong(unscaledValue);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        if (!(isnull = in.readBoolean())) {
            unscaledValue = in.readLong();
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static NumericData readData(final DataInput in) throws IOException {
        if (in.readBoolean()) {
            return NumericData.NULL;
        } else {
            return DataCache.getOrPutNumericData(in.readLong());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<NumericData> iterator() {
        return Arrays.asList(new NumericData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public NumericData first() {
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
        return isnull ? null : BigDecimal.valueOf(unscaledValue, SCALE).floatValue();
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() {
        return isnull ? null : BigDecimal.valueOf(unscaledValue, SCALE);
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        return isnull ? null : unscaledValue != MULTIPLIER;
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
    public List<Double> toList() {
        if (isnull) {
            return null;
        }
        final ArrayList<Double> list = new ArrayList<>(1);
        list.add(value);
        return list;
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Double> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<Double> set = new LinkedHashSet<>(1);
        set.add(value);
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, Double> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(1);
        map.put(0, value);
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        if (isnull) {
            return null;
        }
        
        final long v = Double.doubleToLongBits(value);
        return new byte[] {
                (byte)(v >>> 56),
                (byte)(v >>> 48),
                (byte)(v >>> 40),
                (byte)(v >>> 32),
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
        return isnull ? TypeUtils.NULL_HASH : Double.hashCode(value);
    }
    
    public static final NumericData valueOf(final Integer value) {
        return value == null ? NumericData.NULL : DataCache.getOrPutNumericData((long) value * MULTIPLIER);
    }
    
    public static final NumericData valueOf(final Long value) {
        return value == null ? NumericData.NULL : DataCache.getOrPutNumericData(value * MULTIPLIER);
    }
    
    public static final NumericData valueOf(final Double value) {
        return value == null ? NumericData.NULL : DataCache.getOrPutNumericData(Math.round(value * MULTIPLIER));
    }
    
    public static final NumericData valueOf(final Float value) {
        return value == null ? NumericData.NULL : DataCache.getOrPutNumericData(Math.round(value.doubleValue() * MULTIPLIER));
    }
    
    public static final NumericData valueOf(final BigDecimal value) {
        return value == null ? NumericData.NULL : DataCache.getOrPutNumericData(Math.round(value.doubleValue() * MULTIPLIER));
    }
    
    public static final NumericData nonNullValueOf(final int value) {
        return DataCache.getOrPutNumericData((long) value * MULTIPLIER);
    }
    
    public static final NumericData nonNullValueOf(final long value) {
        return DataCache.getOrPutNumericData(value * MULTIPLIER);
    }
    
    public static final NumericData nonNullValueOf(final double value) {
        return DataCache.getOrPutNumericData(Math.round(value * MULTIPLIER));
    }
    
    public static final NumericData valueOf(final Integer value, final int defaultValue) {
         return DataCache.getOrPutNumericData((value == null ? (long) defaultValue : (long) value) * MULTIPLIER);
    }
    
    public static final NumericData valueOf(final Long value, final long defaultValue) {
         return DataCache.getOrPutNumericData((value == null ? defaultValue : value) * MULTIPLIER);
    }
    
    public static final NumericData valueOf(final Float value, final float defaultValue) {
         return DataCache.getOrPutNumericData(Math.round((value == null ? (double) defaultValue : value.doubleValue()) * MULTIPLIER));
    }
    
    public static final NumericData valueOf(final Double value, final double defaultValue) {
         return DataCache.getOrPutNumericData(Math.round((value == null ? defaultValue : value) * MULTIPLIER));
    }
    
    public static final NumericData valueOf(final BigDecimal value, final BigDecimal defaultValue) {
         return DataCache.getOrPutNumericData(Math.round((value == null ? defaultValue : value).doubleValue() * MULTIPLIER));
    }
    
    public static final NumericData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return NumericData.NULL;
        }
        
        try {
            final double d = Double.valueOf(value.trim());
            final long l = Math.round(d * MULTIPLIER);
            return DataCache.getOrPutNumericData(l);
        } catch (NumberFormatException e) {
            return NumericData.NULL;
        }
    }
}
