package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
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
 * A data class for Boolean.
 *
 */
public final class BooleanData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final BooleanData NULL = new BooleanData();
    public static final BooleanData TRUE = new BooleanData(true);
    public static final BooleanData FALSE = new BooleanData(false);
    public static final String TRUE_STRING = "1";
    public static final String FALSE_STRING = "0";
    private static final byte[] TRUE_BYTES = "1".getBytes(StandardCharsets.UTF_8);
    private static final byte[] FALSE_BYTES = "0".getBytes(StandardCharsets.UTF_8);
    
    protected boolean isnull = true;
    protected boolean value = true;
    
    public BooleanData() {
    }
    
    /**
     * Construct a <code>BooleanData</code> with Boolean
     * 
     * @param value Boolean
     */
    public BooleanData(final Boolean value) {
        if (value != null) {
            this.isnull = false;
            this.value = value.booleanValue();
        }
    }
    
    /**
     * Construct a <code>BooleanData</code> with String
     * 
     * @param value String
     */
    public BooleanData (final String value) {
        if (XStringUtils.isNotBlank(value)) {
            switch (value.trim().toLowerCase()) {
                case "false":
                case "no":
                case "0":
                case "否":
                    this.isnull = false;
                    this.value = false; 
                    break;
                default: 
                    this.isnull = false;
                    this.value = true;
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public BooleanData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public BooleanData copy() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public BooleanData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean getObject() {
        return isnull ? null : value;
    }
    
    /**
     * Get Boolean value.
     * 
     * @return Boolean
     */
    public Boolean getBoolean() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType() {
        return Types.BOOLEAN;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.booleanCompareTo(this, w);
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
                    : anydata.getType() == Types.BOOLEAN && !anydata.isNull() && value == ((BooleanData) anydata).value;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeByte(isnull ? XNumberUtils.BYTE_NULL : value ? XNumberUtils.BYTE_ONE : XNumberUtils.BYTE_ZERO);
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        switch(in.readByte()) {
            case XNumberUtils.BYTE_ONE: isnull = false; value = true; break;
            case XNumberUtils.BYTE_ZERO: isnull = false; value = false; break;
            default: isnull = true;
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static BooleanData readData(final DataInput in) throws IOException {
        switch(in.readByte()) {
            case XNumberUtils.BYTE_ONE: return BooleanData.TRUE;
            case XNumberUtils.BYTE_ZERO: return BooleanData.FALSE;
            default: return BooleanData.NULL;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<BooleanData> iterator() {
        return Arrays.asList(new BooleanData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public BooleanData first() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() {
        return isnull ? null : value ? 1 : 0;
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() {
        return isnull ? null : value ? 1L : 0L;
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() {
        return isnull ? null : value ? 1F : 0F;
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() {
        return isnull ? null : value ? 1D : 0D;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() {
        return isnull ? null : value ? BigDecimal.ONE : BigDecimal.ZERO;
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() {
        return isnull ? null : value ? DateData.ONE.getDate() : DateData.ZERO.getDate();
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() {
        return isnull ? null : value ? TimeData.ONE.getTime() : TimeData.ZERO.getTime();
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() {
        return isnull ? null : value ? TimestampData.ONE.getTimestamp() : TimestampData.ZERO.getTimestamp();
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() {
        return isnull ? null : value ? CalendarTimeData.ONE.getCalendar() : CalendarTimeData.ZERO.getCalendar();
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() {
        return isnull ? null : value ? CalendarTimestampData.ONE.getCalendar() : CalendarTimestampData.ZERO.getCalendar();
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() {
        return isnull ? null : value ? InstantData.ONE.getInstant() : InstantData.ZERO.getInstant();
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Boolean> toList() {
        return isnull ? null : Arrays.asList(new Boolean[] {value});
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Boolean> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<Boolean> set = new LinkedHashSet<Boolean>(1);
        set.add(value);
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, Boolean> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, Boolean> map = new LinkedHashMap<>(1);
        map.put(0, value);
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return isnull ? null : value ? TRUE_BYTES : FALSE_BYTES;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return isnull ? StringData.EMPTY_STRING : value ? TRUE_STRING : FALSE_STRING;
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
        return isnull ? TypeUtils.NULL_HASH : Boolean.hashCode(value);
    }
    
    public static final BooleanData valueOf(final Boolean value) {
        return value == null ? BooleanData.NULL : value ? BooleanData.TRUE : BooleanData.FALSE;
    }
    
    public static final BooleanData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return BooleanData.NULL;
        } else {
            switch (value.trim().toLowerCase()) {
                case "false":
                case "no":
                case "0": 
                case "否":
                    return BooleanData.FALSE;
                default: return BooleanData.TRUE;
            }
        }
    }
}
