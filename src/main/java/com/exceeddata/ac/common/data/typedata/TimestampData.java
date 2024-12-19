package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import com.exceeddata.ac.common.util.XTemporalUtils;

/**
 * A data class for Timestamp.
 *
 */
public final class TimestampData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final TimestampData NULL = new TimestampData();
    public static final TimestampData ZERO = new TimestampData(
            ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, XTemporalUtils.SYSTEM_ZONEID)
                         .toInstant()
                         .toEpochMilli(), false);
    public static final TimestampData ONE = new TimestampData(
            ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 1000000, XTemporalUtils.SYSTEM_ZONEID)
                         .toInstant()
                         .toEpochMilli(), false);
    
    protected boolean isnull = true;
    protected long value = 0l;
    
    public TimestampData() {
    }
    
    /**
     * Construct a <code>TimestampData</code> with Timestamp
     * 
     * @param value the timestamp value
     */
    public TimestampData(final java.sql.Timestamp value) {
        if (value != null) {
            this.value = value.getTime();
            this.isnull = false;
        }
    }
    
    /**
     * Construct a <code>TimestampData</code> with milliseconds.
     * 
     * @param millis the time in milliseconds
     */
    public TimestampData(final long millis) {
        this.value = millis;
        this.isnull = false;
    }
    
    public TimestampData(final long millis, final boolean isnull) {
        this.value = millis;
        this.isnull = isnull;
    }
    
    /**
     * Construct a <code>TimestampData</code> with String.
     * 
     * @param value String
     */
    public TimestampData(final String value) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Long result = XTemporalUtils.parseTimestamp(value);
                if (result != null) {
                    this.value = result.longValue();
                    this.isnull = false;
                }
            } catch (DateTimeParseException e) {
            }
        }
    }
    
    /**
     * Construct a <code>TimestampData</code> with String and Timestamp Formatter.
     * 
     * @param value the value in string
     * @param timestampFormatter the timestamp formatter
     */
    public TimestampData(final String value, final DateTimeFormatter timestampFormatter) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Long result = XTemporalUtils.parseTimestamp(value, timestampFormatter);
                if (result != null) {
                    this.value = result.longValue();
                    this.isnull = false;
                }
            } catch (DateTimeParseException e) {
            }
        }
    }
    
    /**
     * Construct a <code>TimestampData</code> with String and Timestamp Format.
     * 
     * @param value the value in string
     * @param timestampFormat the timestamp format
     */
    public TimestampData(final String value, final String timestampFormat) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Long result = XTemporalUtils.parseTimestamp(value, DateTimeFormatter.ofPattern(timestampFormat));
                if (result != null) {
                    this.value = result.longValue();
                    this.isnull = false;
                }
            } catch (IllegalArgumentException | DateTimeParseException e) {
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public TimestampData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public TimestampData copy() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public TimestampData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp getObject() {
        return isnull ? null : new java.sql.Timestamp(value);
    }
    
    /**
     * Get Timestamp value.
     * 
     * @return java.sql.Timestamp
     */
    public java.sql.Timestamp getTimestamp() {
        return isnull ? null : new java.sql.Timestamp(value);
    }
    
    /**
     * Get Millisecond value.
     * 
     * @return Long
     */
    public Long getTimeInMillis() {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.TIMESTAMP;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.timestampCompareTo(this, w);
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
                    : anydata.getType() == Types.TIMESTAMP && !anydata.isNull() && value == ((TimestampData) anydata).value;
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
            out.writeLong(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        if (!(isnull = in.readBoolean())) {
            this.value = in.readLong();
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static TimestampData readData(final DataInput in) throws IOException {
        if (in.readBoolean()) {
            return TimestampData.NULL;
        } else {
            return TimestampData.valueOf(in.readLong());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<TimestampData> iterator() {
        return Arrays.asList(new TimestampData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public TimestampData first() {
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
        return isnull ? null : value != ZERO.value;
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.clear();
            cal.setTimeInMillis(value);
            cal.set(GregorianCalendar.HOUR, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MILLISECOND, 0);
            return new java.sql.Date(cal.getTimeInMillis());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.clear();
            cal.setTimeInMillis(value);
            cal.set(GregorianCalendar.YEAR, 1970);
            cal.set(GregorianCalendar.DAY_OF_YEAR, 1);
            return new java.sql.Time(cal.getTimeInMillis());
        }
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
            cal.clear();
            cal.setTimeInMillis(value);
            cal.set(GregorianCalendar.YEAR, 1970);
            cal.set(GregorianCalendar.DAY_OF_YEAR, 1);
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
            cal.clear();
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
    public List<java.sql.Timestamp> toList() {
        return isnull ? null : Arrays.asList(new java.sql.Timestamp[] {new java.sql.Timestamp(value)});
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<java.sql.Timestamp> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<java.sql.Timestamp> set = new LinkedHashSet<>(1);
        set.add(new java.sql.Timestamp(value));
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, java.sql.Timestamp> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, java.sql.Timestamp> map = new LinkedHashMap<>(1);
        map.put(0, new java.sql.Timestamp(value));
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return isnull ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(value), XTemporalUtils.SYSTEM_ZONEID)
                                            .format(XTemporalUtils.INSTANT_FORMATTER)
                                            .getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return isnull ? "" : LocalDateTime.ofInstant(Instant.ofEpochMilli(value), XTemporalUtils.SYSTEM_ZONEID)
                                          .format(XTemporalUtils.INSTANT_FORMATTER);
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
    
    public static final TimestampData valueOf(final java.sql.Timestamp value) {
        return value == null ? TimestampData.NULL : DataCache.getOrPutTimestampData(value.getTime());
    }
    
    public static final TimestampData valueOf(final long millis) {
        return DataCache.getOrPutTimestampData(millis);
    }
    
    protected static final TimestampData valueOf(final long millis, final boolean isnull) {
        return isnull ? TimestampData.NULL : DataCache.getOrPutTimestampData(millis);
    }
    
    public static final TimestampData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return TimestampData.NULL;
        }
        try {
            final Long millis = XTemporalUtils.parseTimestamp(value);
            if (millis != null) {
                return DataCache.getOrPutTimestampData(millis);
            }
        } catch (DateTimeParseException e) {
        }
        return TimestampData.NULL;
    }

    public static final TimestampData valueOf(final String value, final DateTimeFormatter formatter) {
        if (XStringUtils.isBlank(value)) {
            return TimestampData.NULL;
        }
        try {
            final Long millis = XTemporalUtils.parseTimestamp(value, formatter);
            if (millis != null) {
                return DataCache.getOrPutTimestampData(millis);
            }
        } catch (DateTimeParseException e) {
        }
        return TimestampData.NULL;
    }

    public static final TimestampData valueOf(final String value, final String format) {
        if (XStringUtils.isBlank(value)) {
            return TimestampData.NULL;
        }
        try {
            final Long millis = XTemporalUtils.parseTimestamp(value, DateTimeFormatter.ofPattern(format));
            if (millis != null) {
                return DataCache.getOrPutTimestampData(millis);
            }
        } catch (IllegalArgumentException | DateTimeParseException e) {
        }
        return TimestampData.NULL;
    }
}
