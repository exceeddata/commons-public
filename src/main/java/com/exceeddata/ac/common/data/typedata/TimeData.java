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
 * A data class for Time.
 *
 */
public final class TimeData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final TimeData NULL = new TimeData();
    public static final TimeData ZERO = new TimeData(
            ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, XTemporalUtils.SYSTEM_ZONEID)
                         .toInstant()
                         .toEpochMilli(), false);
    public static final TimeData ONE = new TimeData(
            ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 1000000, XTemporalUtils.SYSTEM_ZONEID)
                         .toInstant()
                         .toEpochMilli(), false);
    
    protected boolean isnull = true;
    protected long value = 0l;
    
    public TimeData() {
    }
    
    /**
     * Construct a <code>TimeData</code> with Time
     * 
     * @param value java.sql.Time
     */
    public TimeData(final java.sql.Time value) {
        if (value != null) {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(value.getTime());
            cal.set(GregorianCalendar.YEAR, 1970);
            cal.set(GregorianCalendar.MONTH, 0);
            cal.set(GregorianCalendar.DATE, 1);
            this.value = cal.getTime().getTime();
            this.isnull = false;
        }
    }
    
    /**
     * Construct a <code>TimeData</code> with milliseconds
     * 
     * @param millis the time in milliseconds
     */
    public TimeData(final long millis) {
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(millis);
        cal.set(GregorianCalendar.YEAR, 1970);
        cal.set(GregorianCalendar.MONTH, 0);
        cal.set(GregorianCalendar.DATE, 1);
        this.value = cal.getTime().getTime();
        this.isnull = false;
    }
    
    protected TimeData(final long millis, final boolean isnull) {
        this.value = millis;
        this.isnull = isnull;
    }
    
    /**
     * Construct a <code>TimeData</code> with String
     * 
     * @param value String
     */
    public TimeData(final String value) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Long result = XTemporalUtils.parseTime(value);
                if (result != null) {
                    this.value = result.longValue();
                    this.isnull = false;
                }
            } catch (DateTimeParseException e) {
            }
        }
    }
    
    /**
     * Construct a <code>TimeData</code> with String and Time Formatter.
     * 
     * @param value the value in string
     * @param timeFormatter the time formatter
     */
    public TimeData(final String value, final DateTimeFormatter timeFormatter) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Long result = XTemporalUtils.parseTime(value, timeFormatter);
                if (result != null) {
                    this.value = result.longValue();
                    this.isnull = false;
                }
            } catch (DateTimeParseException e) {
            }
        }
    }
    
    /**
     * Construct a <code>TimeData</code> with String and Time Format.
     * 
     * @param value the value in string
     * @param timeFormat the time format
     */
    public TimeData(final String value, final String timeFormat) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Long result = XTemporalUtils.parseTime(value, DateTimeFormatter.ofPattern(timeFormat));
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
    public TimeData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public TimeData copy() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public TimeData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time getObject() {
        return isnull ? null : new java.sql.Time(value);
    }
    
    /**
     * Get Time value.
     * 
     * @return java.sql.Time
     */
    public java.sql.Time getTime() {
        return isnull ? null : new java.sql.Time(value);
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
    public byte getType() {
        return Types.TIME;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.timeCompareTo(this, w);
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
                    : anydata.getType() == Types.TIME && !anydata.isNull() && value == ((TimeData) anydata).value;
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
            value = in.readLong();
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static TimeData readData(final DataInput in) throws IOException {
        return (in.readBoolean()) ? TimeData.NULL : new TimeData(in.readLong(), false);
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<TimeData> iterator() {
        return Arrays.asList(new TimeData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public TimeData first() {
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
        return isnull ? null : new java.sql.Date(DateData.ZERO.value);
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
    public List<java.sql.Time> toList() {
        return isnull ? null : Arrays.asList(new java.sql.Time[] {new java.sql.Time(value)});
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<java.sql.Time> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<java.sql.Time> set = new LinkedHashSet<>(1);
        set.add(new java.sql.Time(value));
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, java.sql.Time> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, java.sql.Time> map = new LinkedHashMap<>(1);
        map.put(0, new java.sql.Time(value));
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return isnull ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(value), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.TIME_FORMATTER).getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return isnull ? "" : LocalDateTime.ofInstant(Instant.ofEpochMilli(value), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.TIME_FORMATTER);
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
    
    public static final TimeData valueOf(final java.sql.Time value) {
        return value == null ? TimeData.NULL : new TimeData(value);
    }
    
    public static final TimeData valueOf(final long millis) {
        return new TimeData(millis);
    }
    
    public static final TimeData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return TimeData.NULL;
        } else {
            try {
                final Long result = XTemporalUtils.parseTime(value);
                if (result != null) {
                    return new TimeData(result.longValue(), false);
                }
            } catch (DateTimeParseException e) {
            }
            return TimeData.NULL;
        }
    }

    public static final TimeData valueOf(final String value, final DateTimeFormatter formatter) {
        if (XStringUtils.isBlank(value)) {
            return TimeData.NULL;
        } else {
            final TimeData data = new TimeData(value, formatter);
            return data.isNull() ? TimeData.NULL : data;
        }
    }

    public static final TimeData valueOf(final String value, final String format) {
        if (XStringUtils.isBlank(value)) {
            return TimeData.NULL;
        } else {
            final TimeData data = new TimeData(value, format);
            return data.isNull() ? TimeData.NULL : data;
        }
    }
}
