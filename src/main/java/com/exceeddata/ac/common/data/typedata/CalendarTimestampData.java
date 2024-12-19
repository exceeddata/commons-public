package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.util.XNumberUtils;
import com.exceeddata.ac.common.util.XStringUtils;
import com.exceeddata.ac.common.util.XTemporalUtils;

/**
 * A data class for calendar timestamp.
 *
 */
public final class CalendarTimestampData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final CalendarTimestampData NULL = new CalendarTimestampData();
    public static final CalendarTimestampData ZERO = new CalendarTimestampData(new GregorianCalendar(1970, 0, 1, 0, 0, 0));
    public static final CalendarTimestampData ONE = new CalendarTimestampData(new GregorianCalendar(1970, 0, 1, 0, 0, 1));
    
    protected boolean isnull = true;
    protected long value = 0l;
    protected String timezone = null;
    
    public CalendarTimestampData() {
    }
    
    /**
     * Construct a <code>CalendarTimestampData</code> with calendar
     * 
     * @param cal the calendar
     */
    public CalendarTimestampData(final Calendar cal) {
        if (cal != null) {
            this.value = cal.getTime().getTime();
            this.timezone = cal.getTimeZone().getID();
            this.isnull = false;
        }
    }
    
    /**
     * Construct a <code>CalendarTimestampData</code> with ZonedDateTime
     * 
     * @param zdt the ZonedDateTime
     */
    public CalendarTimestampData(final ZonedDateTime zdt) {
        if (zdt != null) {
            final GregorianCalendar cal = GregorianCalendar.from(zdt.truncatedTo(ChronoUnit.MILLIS));
            this.value = cal.getTime().getTime();
            this.timezone = cal.getTimeZone().getID();
            this.isnull = false;
        }
    }
    
    /**
     * Construct a <code>CalendarTimestampData</code> with milliseconds
     * 
     * @param millis the time in milliseconds
     */
    public CalendarTimestampData(final long millis) {
        this.value = millis;
        this.timezone = XTemporalUtils.SYSTEM_TIMEZONE;
        this.isnull = false;
    }
    
    protected CalendarTimestampData(final long millis, final TimeZone timezone, final boolean isnull) {
        this.value = millis;
        this.timezone = timezone.getID();
        this.isnull = isnull;
    }
    
    /**
     * Construct a <code>CalendarTimestampData</code> with String
     * 
     * @param value String
     */
    public CalendarTimestampData(final String value) {
        if (XStringUtils.isNotBlank(value)) {
            final ZonedDateTime zdt = XTemporalUtils.parseCalendarTimestamp(value);
            if (zdt != null) {
                final GregorianCalendar cal = GregorianCalendar.from(zdt);
                this.value = cal.getTimeInMillis();
                this.timezone = cal.getTimeZone().getID();
                this.isnull = false;
            }
        }
    }
    
    /**
     * Construct a <code>CalendarTimestampData</code> with String and timestamp formatter.
     * 
     * @param value the value in string
     * @param timestampFormatter the timestampFormat formatter
     */
    public CalendarTimestampData(final String value, final DateTimeFormatter timestampFormatter) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final ZonedDateTime zdt = XTemporalUtils.parseCalendarTimestamp(value, timestampFormatter);
                if (zdt != null) {
                    final GregorianCalendar cal = GregorianCalendar.from(zdt);
                    this.value = cal.getTimeInMillis();
                    this.timezone = cal.getTimeZone().getID();
                    this.isnull = false;
                }
            } catch (DateTimeParseException e) {
            }
        }
    }
    
    /**
     * Construct a <code>CalendarTimestampData</code> with String and timestamp format.
     * 
     * @param value the value in string
     * @param timestampFormat the timestampFormat format
     */
    public CalendarTimestampData(final String value, final String timestampFormat) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final ZonedDateTime zdt = XTemporalUtils.parseCalendarTimestamp(value, DateTimeFormatter.ofPattern(timestampFormat));
                if (zdt != null) {
                    final GregorianCalendar cal = GregorianCalendar.from(zdt);
                    this.value = cal.getTimeInMillis();
                    this.timezone = cal.getTimeZone().getID();
                    this.isnull = false;
                }
            } catch (IllegalArgumentException | DateTimeParseException e) {
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public CalendarTimestampData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public CalendarTimestampData copy() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public CalendarTimestampData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar getObject() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeZone(TimeZone.getTimeZone(timezone));
            cal.setTimeInMillis(value);
            return cal;
        }
    }
    
    /**
     * Get gregorian calendar value.
     * 
     * @return GregorianCalendar
     */
    public GregorianCalendar getCalendar() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeZone(TimeZone.getTimeZone(timezone));
            cal.setTimeInMillis(value);
            return cal;
        }
    }
    
    /**
     * Get gregorian calendar value.
     * 
     * @return GregorianCalendar
     */
    public GregorianCalendar getGregorianCalendar() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeZone(TimeZone.getTimeZone(timezone));
            cal.setTimeInMillis(value);
            return cal;
        }
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
        return Types.CALENDAR_TIMESTAMP;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.calendarTimestampCompareTo(this, w);
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
                    : anydata.getType() == Types.CALENDAR_TIMESTAMP && !anydata.isNull() && value == ((CalendarTimestampData) anydata).value;
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
            if (timezone==null) {
                out.writeByte(XNumberUtils.BYTE_NULL);
                return;
            } else if (timezone.length() == 0) {
                out.writeByte(XNumberUtils.BYTE_ZERO);
                return;
            }
            
            final byte[] b = timezone.getBytes(StandardCharsets.UTF_8);
            final int length = b.length;
            //max byte is 127, max short is 32767
            if (length >> 7 == 0) {
                out.writeByte(XNumberUtils.BYTE_ONE); 
                out.writeByte(length);
            } else {
                if (length >> 15 == 0) {
                    out.writeByte(XNumberUtils.BYTE_TWO); 
                    out.writeShort(length);
                } else {
                    out.writeByte(XNumberUtils.BYTE_FOUR); 
                    out.writeInt(length);
                }
            }
            out.write(b, 0, length);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        if (!(isnull = in.readBoolean())) {
            value = in.readLong();
            final int len;
            switch(in.readByte()) {
                case XNumberUtils.BYTE_NULL:
                case XNumberUtils.BYTE_ZERO:     timezone = XTemporalUtils.SYSTEM_TIMEZONE; return;
                case XNumberUtils.BYTE_ONE:      len = in.readByte(); break;
                case XNumberUtils.BYTE_TWO:      len = in.readShort(); break;
                default:                        len = in.readInt();
            }
            final byte[] bytes = new byte[len];
            in.readFully(bytes, 0, len);
            timezone = DataCache.getOrPutTimeZone(new String(bytes, StandardCharsets.UTF_8));
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static CalendarTimestampData readData(final DataInput in) throws IOException {
        if (in.readBoolean()) {
            return CalendarTimestampData.NULL;
        } else {
            final long millis = in.readLong();
            final int len;
            switch(in.readByte()) {
                case XNumberUtils.BYTE_NULL:
                case XNumberUtils.BYTE_ZERO:     len = 0; break;
                case XNumberUtils.BYTE_ONE:      len = in.readByte(); break;
                case XNumberUtils.BYTE_TWO:      len = in.readShort(); break;
                default:                        len = in.readInt();
            }
            
            String timezone;
            if (len > 0) {
                final byte[] bytes = new byte[len];
                in.readFully(bytes, 0, len);
                timezone = DataCache.getOrPutTimeZone(new String(bytes, StandardCharsets.UTF_8));
            } else {
                timezone = XTemporalUtils.SYSTEM_TIMEZONE;
            }
            final TimeZone tz = TimeZone.getTimeZone(timezone);
            return CalendarTimestampData.valueOf(millis, tz, false);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<CalendarTimestampData> iterator() {
        return Arrays.asList(new CalendarTimestampData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public CalendarTimestampData first() {
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
    @SuppressWarnings("deprecation")
    @Override
    public java.sql.Date toDate() {
        if (isnull) {
            return null;
        } else {
            final java.util.Date dt = new java.util.Date(value);
            return new java.sql.Date(dt.getYear(), dt.getMonth(), dt.getDate());
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
            cal.setTimeZone(TimeZone.getTimeZone(timezone));
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
            cal.setTimeZone(TimeZone.getTimeZone(timezone));
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
            cal.setTimeZone(TimeZone.getTimeZone(timezone));
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
    public List<Calendar> toList() {
        if (isnull) {
            return null;
        }
        final ArrayList<Calendar> list = new ArrayList<>(1);
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone(timezone));
        cal.setTimeInMillis(value);
        list.add(cal);
        return list;
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Calendar> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<Calendar> set = new LinkedHashSet<>(1);
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone(timezone));
        cal.setTimeInMillis(value);
        set.add(cal);
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, Calendar> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, Calendar> map = new LinkedHashMap<>(1);
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone(timezone));
        cal.setTimeInMillis(value);
        map.put(0, cal);
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return isnull ? null : ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), TimeZone.getTimeZone(timezone).toZoneId())
                                            .format(XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER)
                                            .getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return isnull ? "" : ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), TimeZone.getTimeZone(timezone).toZoneId())
                                          .format(XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER);
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
    
    public static final CalendarTimestampData valueOf(final Calendar value) {
        return value != null ? new CalendarTimestampData(value) : CalendarTimestampData.NULL;
    }
    
    public static final CalendarTimestampData valueOf(final long millis) {
        return new CalendarTimestampData(millis) ;
    }
    
    public static final CalendarTimestampData valueOf(final long millis, final boolean isnull) {
        return isnull ? CalendarTimestampData.NULL : new CalendarTimestampData(millis) ;
    }
    
    protected static final CalendarTimestampData valueOf(final long millis, final TimeZone timezone, final boolean isnull) {
        return new CalendarTimestampData(millis, timezone, isnull) ;
    }
    
    public static final CalendarTimestampData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return CalendarTimestampData.NULL;
        } else {
            final CalendarTimestampData data = new CalendarTimestampData(value);
            return data.isNull() ? CalendarTimestampData.NULL : data;
        }
    }
    
    public static final CalendarTimestampData valueOf(final ZonedDateTime zdt) {
        return zdt != null ? new CalendarTimestampData(zdt) : CalendarTimestampData.NULL;
    }

    public static final CalendarTimestampData valueOf(final String value, final DateTimeFormatter formatter) {
        if (XStringUtils.isBlank(value)) {
            return CalendarTimestampData.NULL;
        } else {
            final CalendarTimestampData data = new CalendarTimestampData(value, formatter);
            return data.isNull() ? CalendarTimestampData.NULL : data;
        }
    }

    public static final CalendarTimestampData valueOf(final String value, final String format) {
        if (XStringUtils.isBlank(value)) {
            return CalendarTimestampData.NULL;
        } else {
            final CalendarTimestampData data = new CalendarTimestampData(value, format);
            return data.isNull() ? CalendarTimestampData.NULL : data;
        }
    }
}
