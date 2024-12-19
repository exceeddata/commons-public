package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
 * A data class for Decimal.
 *
 */
public final class DecimalData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final DecimalData NULL = new DecimalData();
    public static final DecimalData ZERO = new DecimalData(BigDecimal.ZERO);
    public static final DecimalData ONE = new DecimalData(BigDecimal.ONE);
    public static final DecimalData TWO = new DecimalData(BigDecimal.valueOf(2));
    
    protected BigDecimal value = null;
    
    public DecimalData() {
    }
    
    /**
     * Construct a <code>DecimalData</code> with BigDecimal
     * 
     * @param value BigDecimal
     */
    public DecimalData(final BigDecimal value) {
        this.value = value;
    }
    
    
    /**
     * Construct a <code>DecimalData</code> with String
     * 
     * @param value String
     */
    public DecimalData (final String value) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                this.value = new BigDecimal(value);
            } catch (NumberFormatException e) {
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public DecimalData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public DecimalData copy() {
        return this;
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public DecimalData abs() {
        return value == null ? DecimalData.NULL : value.signum() >= 0 ? this : new DecimalData(value.abs());
    }
    
    /**
     * Returns negated value.
     * 
     * @return the negated value
     */
    public DecimalData negate() {
        return value == null ? DecimalData.NULL : new DecimalData(value.negate());
    }
    
    /** {@inheritDoc} */
    @Override
    public DecimalData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal getObject() {
        return value;
    }
    
    /**
     * Get Decimal value.
     * 
     * @return BigDecimal
     */
    public BigDecimal getDecimal() {
        return value;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType() {
        return Types.DECIMAL;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.decimalCompareTo(this, w);
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
                    : anydata.getType() == Types.DECIMAL && !anydata.isNull() && value.compareTo(((DecimalData) anydata).value) == 0;
        }
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (value == null) {
            out.writeByte(XNumberUtils.BYTE_NULL);
            return;
        }
        
        final BigInteger unscaled = value.unscaledValue();
        if (unscaled.equals(BigInteger.ZERO)) {
            out.writeByte(XNumberUtils.BYTE_ZERO);
            return;
        }

        final byte[] unscaledb = unscaled.toByteArray();
        final int scale = value.scale();
        if (scale >> 7 == 0) {
            out.writeByte(unscaledb.length << 3 | 1);
            out.write(unscaledb);
            out.writeByte(scale);
        } else {
            if (scale >> 15 == 0) {
                out.writeByte(unscaledb.length << 3 | 2);
                out.write(unscaledb);
                out.writeShort(scale);
            } else {
                out.writeByte(unscaledb.length << 3 | 4);
                out.write(unscaledb);
                out.writeInt(scale);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        final byte combo;
        switch(combo = in.readByte()) {
            case XNumberUtils.BYTE_NULL:     value = null; return;
            case XNumberUtils.BYTE_ZERO:     value = BigDecimal.ZERO; return;
            default:
                final int scalelen = combo & ((1 << 3) - 1);
                final int unscaleb = (combo >> 3) & ((1 << 5) - 1); //right shift 3 and get first 5 unsigned bit
                final byte[] bytes = new byte[unscaleb];
                in.read(bytes, 0, unscaleb);
                
                final int scale = scalelen == 1 ? in.readByte() : scalelen == 2 ? in.readShort() : in.readInt();
                value = new BigDecimal(new BigInteger(bytes), scale);
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static DecimalData readData(final DataInput in) throws IOException {
        final byte combo;
        switch(combo = in.readByte()) {
            case XNumberUtils.BYTE_NULL:     return DecimalData.NULL;
            case XNumberUtils.BYTE_ZERO:     return DecimalData.ZERO;
            default:
                final int scalelen = combo & ((1 << 3) - 1);
                final int unscaleb = (combo >> 3) & ((1 << 5) - 1); //right shift 3 and get first 5 unsigned bit
                final byte[] bytes = new byte[unscaleb];
                in.readFully(bytes, 0, unscaleb);
                
                final int scale = scalelen == 1 ? in.readByte() : scalelen == 2 ? in.readShort() : in.readInt();
                return new DecimalData(new BigDecimal(new BigInteger(bytes), scale));
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<DecimalData> iterator() {
        return Arrays.asList(new DecimalData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public DecimalData first() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() {
        return (value != null) ? value.intValue() : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() {
        return (value != null) ? value.longValue() : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() {
        return (value != null) ? value.floatValue() : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() {
        return (value != null) ? value.doubleValue() : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() {
        return value;
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        return (value != null) ? value.signum() != 0 : null;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("deprecation")
    @Override
    public java.sql.Date toDate() {
        if (value != null) {
            final java.sql.Date dt = new java.sql.Date(value.longValue());
            return new java.sql.Date(dt.getYear(), dt.getMonth(), dt.getDate());
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("deprecation")
    @Override
    public java.sql.Time toTime() {
        if (value != null) {
            final java.sql.Time dt = new java.sql.Time(value.longValue());
            dt.setYear(70);
            dt.setMonth(0);
            dt.setDate(1);
            return dt;
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() {
        return value != null ? new java.sql.Timestamp(value.longValue()) : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() {
        if (value != null) {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(value.longValue());
            cal.set(GregorianCalendar.YEAR, 1970);
            cal.set(GregorianCalendar.MONTH, 0);
            cal.set(GregorianCalendar.DATE, 1);
            return cal;
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() {
        if (value != null) {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(value.longValue());
            return cal;
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() {
        return value != null ? Instant.ofEpochMilli(value.longValue()) : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public List<BigDecimal> toList() {
        return value == null ? null : Arrays.asList(new BigDecimal[] {value});
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<BigDecimal> toSet() {
        if (value == null) {
            return null;
        }
        final LinkedHashSet<BigDecimal> set = new LinkedHashSet<>(1);
        set.add(value);
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, BigDecimal> toMap() {
        if (value == null) {
            return null;
        }
        final LinkedHashMap<Integer, BigDecimal> map = new LinkedHashMap<>(1);
        map.put(0, value);
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return (value == null) ?  null : value.toString().getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return (value != null) ? value.toString() : "";
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
        return value == null;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isDigits() {
        return value != null && (value.signum() == 0 || value.scale() <= 0 || value.stripTrailingZeros().scale() <= 0);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return value != null;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : TypeUtils.NULL_HASH;
    }
    
    public static final DecimalData valueOf(final BigDecimal value) {
        return value != null ? new DecimalData(value) : DecimalData.NULL;
    }
    
    public static final DecimalData nonNullValueOf(final BigDecimal value) {
        return new DecimalData(value);
    }
    
    public static final DecimalData valueOf(final BigDecimal value, final BigDecimal defaultValue) {
        return value != null ? new DecimalData(value) : new DecimalData(defaultValue);
    }
    
    public static final DecimalData valueOf(final String value) {
        try {
            return XStringUtils.isNotBlank(value) ? new DecimalData(new BigDecimal(value)) : DecimalData.NULL;
        } catch(NumberFormatException e) {
            return DecimalData.NULL;
        }
    }
    
    public static final DecimalData nonNullValueOf(final String value) {
        return new DecimalData(new BigDecimal(value));
    }
}
