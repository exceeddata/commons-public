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
import com.exceeddata.ac.common.util.XStringUtils;

/**
 * A data class for Double.
 *
 */
public final class DoubleData implements TypeData {
    private static final long serialVersionUID = 1L;

    public static final DoubleData NULL = new DoubleData();
    public static final DoubleData ZERO = new DoubleData(0d, false);
    public static final DoubleData ONE = new DoubleData(1d, false);
    public static final DoubleData NEGATIVE_ONE = new DoubleData(-1d, false);
    public static final DoubleData POSITIVE_INFINITY = new DoubleData(Double.POSITIVE_INFINITY, false);
    public static final DoubleData NEGATIVE_INFINITY = new DoubleData(Double.NEGATIVE_INFINITY, false);
    
    protected boolean isnull = true;
    protected double value = 0d;
    
    public DoubleData() {
    }
    
    /**
     * Construct a <code>DoubleData</code> with Double
     * 
     * @param value Double
     */
    public DoubleData (final Double value) {
        if (value != null) {
            this.isnull =false;
            this.value = value.doubleValue();
        }
    }
    
    private DoubleData (final double value, final boolean isnull) {
        this.value = value;
        this.isnull = isnull;
    }
    
    /**
     * Construct a <code>DoubleData</code> with String
     * 
     * @param value String
     */
    public DoubleData(final String value) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                this.value = Double.valueOf(value).doubleValue();
                this.isnull = false;
            } catch (NumberFormatException e) {
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public DoubleData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public DoubleData copy() {
        return this;
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public DoubleData abs() {
        return isnull ? DoubleData.NULL : value >= 0d ? this : new DoubleData(-value);
    }
    
    /**
     * Returns negated value.
     * 
     * @return the negated value
     */
    public DoubleData negate() {
        return isnull ? DoubleData.NULL : new DoubleData(-value);
    }
    
    /** {@inheritDoc} */
    @Override
    public DoubleData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Double getObject() {
        return isnull ? null : value;
    }
    
    /**
     * Get Double value.
     * 
     * @return Double
     */
    public Double getDouble() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.DOUBLE;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.doubleCompareTo(this, w);
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
                    : anydata.getType() == Types.DOUBLE && !anydata.isNull() && Double.compare(value, ((DoubleData) anydata).value) == 0;
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
            out.writeDouble(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        if (!(isnull = in.readBoolean())) {
            value = in.readDouble();
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static DoubleData readData(final DataInput in) throws IOException {
        if (in.readBoolean()) {
            return DoubleData.NULL;
        } else {
            return DoubleData.nonNullValueOf(in.readDouble());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<DoubleData> iterator() {
        return Arrays.asList(new DoubleData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public DoubleData first() {
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
        return isnull ? null : new BigDecimal(String.valueOf(value)).floatValue();
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() {
        return isnull ? null : new BigDecimal(String.valueOf(value));
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        return isnull ? null : value != 0d;
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
        return isnull ? null : Arrays.asList(new Double[] {value});
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Double> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<Double> set = new LinkedHashSet<Double>(1);
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
    
    public static final DoubleData valueOf(final Double value) {
        return value != null ? ( Double.compare(value, 0d) == 0 ? DoubleData.ZERO : Double.compare(value, 1d) == 0 ? DoubleData.ONE : new DoubleData(value)) : DoubleData.NULL;
    }
    
    public static final DoubleData valueOf(final Double value, final double defaultValue) {
        return value == null 
                ? new DoubleData(defaultValue, false) 
                : Double.compare(value, 0d) == 0 ? DoubleData.ZERO : Double.compare(value, 1d) == 0 ? DoubleData.ONE : new DoubleData(value.doubleValue(), false);
    }
    
    public static final DoubleData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return DoubleData.NULL;
        } else {
            try {
                final double d = Double.valueOf(value.trim());
                return Double.compare(d, 0d) == 0 ? DoubleData.ZERO : Double.compare(d, 1d) == 0 ? DoubleData.ONE : new DoubleData(d, false);
            } catch (NumberFormatException e) {
                return DoubleData.NULL;
            }
        }
    }
    
    public static final DoubleData valueOf(final String value, final DoubleData defaultValue) {
        if (XStringUtils.isBlank(value)) {
            return defaultValue;
        } else {
            try {
                final double d = Double.valueOf(value.trim());
                return d == 0d ? DoubleData.ZERO : d == 1d ? DoubleData.ONE : new DoubleData(d, false);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }
    
    public static final DoubleData nonNullValueOf(final double value) {
        return Double.compare(value, 0d) == 0 ? DoubleData.ZERO : Double.compare(value, 1d) == 0 ? DoubleData.ONE : new DoubleData(value, false);
    }
    
    public static final DoubleData random(double base) {
        return DoubleData.nonNullValueOf(Math.random() * base);
    }
}
