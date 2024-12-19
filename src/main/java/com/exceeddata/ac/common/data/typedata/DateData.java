package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.exception.EngineDataException;
import com.exceeddata.ac.common.util.XStringUtils;
import com.exceeddata.ac.common.util.XTemporalUtils;

/**
 * A data class for Date.
 *
 */
public final class DateData implements TypeData {
    private static final long serialVersionUID = 1L;

    public static final DateData NULL = new DateData();
    public static final DateData ZERO = new DateData(
            ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, XTemporalUtils.SYSTEM_ZONEID)
                         .toInstant()
                         .toEpochMilli(), false);
    public static final DateData ONE = new DateData(
            ZonedDateTime.of(1970, 1, 2, 0, 0, 0, 0, XTemporalUtils.SYSTEM_ZONEID)
                         .toInstant()
                         .toEpochMilli(), false);
                        
    protected boolean isnull = true;
    protected long value = 0l;
    
    public DateData() {
    }
    
    /**
     * Construct a <code>DateData</code> with Date
     * 
     * @param value java.sql.Date
     */
    public DateData(final java.sql.Date value) {
        if (value != null) {
            this.value = value.getTime();
            this.isnull = false;
        }
    }
    
    /**
     * Construct a <code>DateData</code> with Date
     * 
     * @param value java.util.Date
     */
    public DateData(final java.util.Date value) {
        if (value != null) {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(value.getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            this.value = cal.getTimeInMillis();
            this.isnull = false;
        }
    }
    
    /**
     * Construct a <code>DateData</code> with Date
     * 
     * @param millis the time in milliseconds
     */
    public DateData(final long millis) {
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(millis);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.value = cal.getTimeInMillis();
        this.isnull = false;
    }
    
    protected DateData(final long millis, final boolean isnull) {
        this.value = millis;
        this.isnull = isnull;
    }
    
    /**
     * Construct a <code>DateData</code> with String
     * 
     * @param value String
     */
    public DateData(final String value) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Long result = XTemporalUtils.parseDate(value);
                if (result != null) {
                    this.value = result.longValue();
                    this.isnull = false;
                }
            } catch (DateTimeParseException e) {
            }
        }
    }
    
    /**
     * Construct a <code>DateData</code> with String and Date Formatter
     * e
     * @param value the value in string
     * @param dateFormatter the formatter to apply, if empty then default format is used
     */
    public DateData(final String value, final DateTimeFormatter dateFormatter) {
        if (value != null) {
            try {
                this.value = LocalDate.parse(value, dateFormatter)
                        .atStartOfDay()
                        .atZone(XTemporalUtils.SYSTEM_ZONEID)
                        .toInstant()
                        .toEpochMilli();
                this.isnull = false;
            } catch (DateTimeParseException e) {
            }
        }
    }
    /**
     * Construct a <code>DateData</code> with String and Date Format
     * e
     * @param value the value in string
     * @param dateFormat the format to apply, if empty then default format is used
     */
    public DateData(final String value, final String dateFormat) {
        if (value != null) {
            try {
                this.value = LocalDate.parse(value, DateTimeFormatter.ofPattern(dateFormat))
                        .atStartOfDay()
                        .atZone(XTemporalUtils.SYSTEM_ZONEID)
                        .toInstant()
                        .toEpochMilli();
                this.isnull = false;
            } catch (IllegalArgumentException | DateTimeParseException e) {
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public DateData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public DateData copy() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public DateData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date getObject() {
        return isnull ? null : new java.sql.Date(value);
    }
    
    /**
     * Get Date value.
     * 
     * @return java.sql.Date
     */
    public java.sql.Date getDate() {
        return isnull ? null : new java.sql.Date(value);
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
        return Types.DATE;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.dateCompareTo(this, w);
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
                    : anydata.getType() == Types.DATE && !anydata.isNull() && value == ((DateData) anydata).value;
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
    public static DateData readData(final DataInput in) throws IOException {
        if (in.readBoolean()) {
            return DateData.NULL;
        } else {
            return new DateData(in.readLong(), false);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<DateData> iterator() {
        return Arrays.asList(new DateData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public DateData first() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() throws EngineDataException {
        return isnull ? null : (int) (value);
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() throws EngineDataException {
        return isnull ? null : value;
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() throws EngineDataException {
        return isnull ? null : (float) (value);
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() throws EngineDataException {
        return isnull ? null : (double) (value);
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() throws EngineDataException {
        return isnull ? null : BigDecimal.valueOf(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() throws EngineDataException {
        return isnull ? null : value != ZERO.value;
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() throws EngineDataException {
        return isnull ? null : new java.sql.Date(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() throws EngineDataException {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.clear();
            return new java.sql.Time(cal.getTimeInMillis());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() throws EngineDataException {
        return isnull ? null : new java.sql.Timestamp(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() throws EngineDataException {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.clear();
            return cal;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() throws EngineDataException {
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
    public Instant toInstant() throws EngineDataException {
        return isnull ? null : Instant.ofEpochMilli(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<java.sql.Date> toList() {
        return isnull ? null : Arrays.asList(new java.sql.Date[] {new java.sql.Date(value)});
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<java.sql.Date> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<java.sql.Date> set = new LinkedHashSet<>(1);
        set.add(new java.sql.Date(value));
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, java.sql.Date> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, java.sql.Date> map = new LinkedHashMap<>(1);
        map.put(0, new java.sql.Date(value));
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return isnull ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(value), XTemporalUtils.SYSTEM_ZONEID).format( XTemporalUtils.DATE_FORMATTER).getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return isnull ? "" : LocalDateTime.ofInstant(Instant.ofEpochMilli(value), XTemporalUtils.SYSTEM_ZONEID).format( XTemporalUtils.DATE_FORMATTER);
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
    
    public static final DateData valueOf(final java.sql.Date value) {
        return value == null ? DateData.NULL : DataCache.getOrPutDateData(value.getTime());
    }
    
    public static final DateData valueOf(final java.util.Date value) {
        if (value == null) {
            return DateData.NULL;
        }
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(value.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return DataCache.getOrPutDateData(cal.getTimeInMillis());
    }
    
    public static final DateData valueOf(final long millis) {
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(millis);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return DataCache.getOrPutDateData(millis);
    }
    
    public static final DateData valueOf(final String value) {
        return XStringUtils.isBlank(value) ? DateData.NULL : DataCache.getOrPutDateData(value);
    }
    
    public static final DateData valueOf(final String value, final DateTimeFormatter formatter) {
        if (XStringUtils.isBlank(value)) {
            return DateData.NULL;
        } else {
            DateData dd = DataCache.getDateData(value);
            if (dd == null) {
                dd = new DateData(value, formatter);
                DataCache.putDateData(value, dd.isNull() ? DateData.NULL : dd);
            }
            return dd;
        }
    }
    
    public static final DateData valueOf(final String value, final String format) {
        if (XStringUtils.isBlank(value)) {
            return DateData.NULL;
        } else if (XStringUtils.isBlank(format)){
            DateData dd = DataCache.getDateData(value);
            if (dd == null) {
                if ((dd = new DateData(value)).isNull()) {
                    DataCache.putDateData(value, DateData.NULL);
                    return DateData.NULL;
                } else {
                    DataCache.putDateData(value, dd);
                    return dd;
                }
            } else {
                return dd;
            }
        } else {
            return new DateData(value, format);
        }
    }
}
