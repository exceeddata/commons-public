package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import com.exceeddata.ac.common.util.XTemporalUtils;

/**
 * A data class for Instant.
 *
 */
public final class InstantData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final InstantData NULL = new InstantData();
    public static final InstantData ZERO = new InstantData(0l, 0);
    public static final InstantData ONE = new InstantData(0l, 1);
    
    protected boolean isnull = true;
    protected long seconds = 0l;
    protected long millis = 0l;
    protected int nanos = 0;
    
    public InstantData() {
    }
    
    /**
     * Construct a <code>InstantData</code> with Instant
     * 
     * @param value the instant value
     */
    public InstantData(final Instant value) {
        if (value != null) {
            this.seconds = value.getEpochSecond();
            this.nanos = value.getNano();
            this.millis = value.toEpochMilli();
            this.isnull = false;
        }
    }
    
    /**
     * Construct a <code>InstantData</code> with seconds and nanos
     * 
     * @param seconds the seconds value
     * @param nanos the nanos adjustment
     */
    public InstantData(final long seconds, final int nanos) {
        this.seconds = seconds;
        this.nanos = nanos;
        this.millis = Instant.ofEpochSecond(seconds, nanos).toEpochMilli();
        this.isnull = false;
    }
    
    /**
     * Construct a <code>InstantData</code> with milliseconds.
     * 
     * @param millis the time in milliseconds
     */
    public InstantData(final long millis) {
        final Instant instant = Instant.ofEpochMilli(millis);
        this.seconds = instant.getEpochSecond();
        this.nanos = instant.getNano();
        this.millis = millis;
        this.isnull = false;
    }
    
    protected InstantData(final long millis, final boolean isnull) {
        final Instant instant = Instant.ofEpochMilli(millis);
        this.seconds = instant.getEpochSecond();
        this.nanos = instant.getNano();
        this.millis = millis;
        this.isnull = isnull;
    }
    
    protected InstantData(final long seconds, final int nanos, final boolean isnull) {
        this.seconds = seconds;
        this.nanos = nanos;
        this.millis = Instant.ofEpochSecond(seconds, nanos).toEpochMilli();
        this.isnull = isnull;
    }
    
    /**
     * Construct a <code>InstantData</code> with String.
     * 
     * @param value String
     */
    public InstantData(final String value) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Instant instant = XTemporalUtils.parseInstant(value);
                if (instant != null) {
                     this.seconds = instant.getEpochSecond();
                     this.nanos = instant.getNano();
                     this.millis = instant.toEpochMilli();
                     this.isnull = false;
                }
            } catch (DateTimeParseException e) {
            }
        }
    }
    
    /**
     * Construct a <code>InstantData</code> with String and DateTimeFormatter.
     * 
     * @param value the value in string
     * @param instantFormat the instant format
     */
    public InstantData(final String value, final DateTimeFormatter instantFormat) {
        if (XStringUtils.isNotBlank(value)) {
            try {
                final Instant instant = XTemporalUtils.parseInstant(value, instantFormat);
                if (instant != null) {
                     this.seconds = instant.getEpochSecond();
                     this.nanos = instant.getNano();
                     this.millis = instant.toEpochMilli();
                     this.isnull = false;
                }
            } catch (DateTimeParseException e) {
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public InstantData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public InstantData copy() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public InstantData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant getObject() {
        return isnull ? null : Instant.ofEpochSecond(seconds, nanos);
    }
    
    /**
     * Get Instant value.
     * 
     * @return Instant
     */
    public Instant getInstant() {
        return isnull ? null : Instant.ofEpochSecond(seconds, nanos);
    }
    
    /**
     * Get Seconds value.
     * 
     * @return Long
     */
    public Long getTimeInSeconds() {
        return isnull ? null : seconds;
    }
    
    /**
     * Get Nanos value.
     * 
     * @return Integer
     */
    public Integer getTimeInNanos() {
        return isnull ? null : nanos;
    }
    
    /**
     * Get Millisecond value.
     * 
     * @return Long
     */
    public Long getTimeInMillis() {
        return isnull ? null : millis;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.INSTANT;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.instantCompareTo(this, w);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TypeData) {
            final TypeData anydata = (TypeData) obj;
            if (isnull) {
                return anydata.isNull();
            } else if (anydata.getType() == Types.INSTANT && !anydata.isNull()) {
                final InstantData data = (InstantData) obj;
                return millis == data.millis && nanos == data.nanos;
            }
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
            out.writeLong(seconds);
            out.writeInt(nanos);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        if (!(isnull = in.readBoolean())) {
            this.seconds = in.readLong();
            this.nanos = in.readInt();
            this.millis = Instant.ofEpochSecond(seconds, nanos).toEpochMilli();
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static InstantData readData(final DataInput in) throws IOException {
        if (in.readBoolean()) {
            return InstantData.NULL;
        } else {
            return InstantData.valueOf(in.readLong(), in.readInt(), false);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<InstantData> iterator() {
        return Arrays.asList(new InstantData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public InstantData first() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() {
        return isnull ? null : (int) millis;
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() {
        return isnull ? null : millis;
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() {
        return isnull ? null : (float) millis;
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() {
        return isnull ? null : (double) millis;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() {
        return isnull ? null : BigDecimal.valueOf(millis);
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        return isnull ? null : seconds != ZERO.seconds || nanos != ZERO.nanos;
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() {
        if (isnull) {
            return null;
        } else {
            return new java.sql.Date(millis);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() {
        if (isnull) {
            return null;
        } else {
            return new java.sql.Time(millis);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() {
        return isnull ? null : new java.sql.Timestamp(millis);
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.clear();
            cal.setTimeInMillis(millis);
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
            cal.setTimeInMillis(millis);
            return cal;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() {
        return isnull ? null : Instant.ofEpochSecond(seconds, nanos);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Instant> toList() {
        if (isnull) {
            return null;
        }
        final ArrayList<Instant> list = new ArrayList<>(1);
        list.add(Instant.ofEpochSecond(seconds, nanos));
        return list;
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Instant> toSet() {
        if (isnull) {
            return null;
        }
        final LinkedHashSet<Instant> set = new LinkedHashSet<>(1);
        set.add(Instant.ofEpochSecond(seconds, nanos));
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, Instant> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, Instant> map = new LinkedHashMap<>(1);
        map.put(0, Instant.ofEpochSecond(seconds, nanos));
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return isnull ? null : LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds, nanos), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.INSTANT_FORMATTER).getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return isnull ? "" : LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds, nanos), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.INSTANT_FORMATTER);
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
        return isnull ? TypeUtils.NULL_HASH : Long.hashCode(seconds) * 17 + Integer.hashCode(nanos);
    }
    
    public static final InstantData valueOf(final Instant value) {
        if (value == null) {
            return InstantData.NULL;
        } else if (value.getEpochSecond() == ZERO.seconds && value.getNano() == ZERO.nanos) {
            return InstantData.ZERO;
        } else {
            return new InstantData(value);
        }
    }
    
    public static final InstantData valueOf(final long seconds, final int nanos) {
        return seconds == ZERO.seconds && nanos == ZERO.nanos ? InstantData.ZERO : new InstantData(seconds, nanos);
    }
    
    
    public static final InstantData valueOf(final long millis) {
        return millis == ZERO.millis ? InstantData.ZERO : new InstantData(millis);
    }
    
    protected static final InstantData valueOf(final long seconds, final int nanos, final boolean isnull) {
        return isnull ? InstantData.NULL : seconds == ZERO.seconds && nanos == ZERO.nanos ? InstantData.ZERO : new InstantData(seconds, nanos, isnull);
    }
    
    public static final InstantData valueOf(final long millis, final boolean isnull) {
        return isnull ? InstantData.NULL : millis == ZERO.millis ? InstantData.ZERO : new InstantData(millis, isnull);
    }
    
    public static final InstantData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return InstantData.NULL;
        } else {
            final InstantData data = new InstantData(value);
            return data.isNull() ? InstantData.NULL : data;
        }
    }
}
