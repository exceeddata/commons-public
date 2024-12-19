package com.exceeddata.ac.common.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * A class of static util functions for manipulating date times.
 */
public final class XTemporalUtils {
    private XTemporalUtils() {}
    
    private static final long NANOS_PER_SECOND = 1000000000l;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;
    private static int JGREG= 15 + 31*(10+12*1582);
    private static double HALFSECOND = 0.5;
    
    public static final String SYSTEM_TIMEZONE = TimeZone.getDefault().getID();
    public static final ZoneId SYSTEM_ZONEID = ZoneId.systemDefault();
    public static final LocalDate EPOCH_DATE = LocalDate.of(1970, 1, 1);
    public static final ZoneOffset EPOCH_OFFSET = ZoneId.systemDefault().getRules().getOffset(EPOCH_DATE.atStartOfDay());
    public static final long EPOCH_MILLIS = EPOCH_DATE.atStartOfDay(SYSTEM_ZONEID).toInstant().toEpochMilli();
    public static final long LAUNCH_MILLIS = System.currentTimeMillis();
    
    /*
     * number of days since epoch because java doesn't cal correctly that far back.
     * The days also is adjusted by one because serial date as it is calculated by Excel & others 
     *   are counting a phantom Feb 29 1900 due to old code compatibility.
     */
    private static final int SERIAL_EPOCH_DAYS = 719529; 
    public static final int MILLIS_DAY = 86400000; 
    
    public static final DateTimeFormatter DATE_FORMATTER =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .parseLenient()
                    .appendPattern("yyyy-MM-dd")
                    .toFormatter();
    public static final DateTimeFormatter TIME_FORMATTER = 
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .parseLenient()
                    .appendPattern("HH:mm:ss")
                    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .toFormatter();
    public static final DateTimeFormatter INSTANT_FORMATTER =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .parseLenient()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .toFormatter();
    public static final DateTimeFormatter CALENDAR_TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .parseLenient()
                    .appendPattern("HH:mm:ss")
                    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .appendOffset("+HHMM", "0000")
                    .toFormatter();
    public static final DateTimeFormatter CALENDAR_TIMESTAMP_FORMATTER =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .parseLenient()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .appendOffset("+HHMM", "0000")
                    .toFormatter();
    public static final DateTimeFormatter TIMESTAMP_NUMBER_FORMATTER =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .parseLenient()
                    .appendPattern("yyyyMMddHHmmss")
                    .appendFraction(ChronoField.NANO_OF_SECOND, 3, 3, false)
                    .toFormatter();
    
    public static Long parseDate(final String value) {
        try {
            ZonedDateTime zdt = DateTimeParser.parseZonedDateTime(value);
            if (zdt == null) {
                return null;
            }
            if (!zdt.getZone().equals(SYSTEM_ZONEID)) {
                zdt = zdt.toInstant().atZone(SYSTEM_ZONEID);
            }
            return zdt.truncatedTo(ChronoUnit.DAYS).toInstant().toEpochMilli();
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static Long parseDate(final String value, final DateTimeFormatter formatter) {
        try {
            final TemporalAccessor temporalAccessor = formatter.parseBest(value, ZonedDateTime::from, LocalDateTime::from, OffsetDateTime::from, LocalDate::from);
            if (temporalAccessor instanceof LocalDate) {
                return ((LocalDate) temporalAccessor)
                            .atStartOfDay(SYSTEM_ZONEID)
                            .toInstant()
                            .toEpochMilli();
            }
            if (temporalAccessor instanceof LocalDateTime) {
                return ((LocalDateTime) temporalAccessor)
                            .atZone(SYSTEM_ZONEID)
                            .truncatedTo(ChronoUnit.DAYS)
                            .toInstant()
                            .toEpochMilli();
            }
            if (temporalAccessor instanceof ZonedDateTime) {
                return ((ZonedDateTime) temporalAccessor)
                            .toInstant()
                            .atZone(SYSTEM_ZONEID)
                            .truncatedTo(ChronoUnit.DAYS)
                            .toInstant()
                            .toEpochMilli();
            }
            if (temporalAccessor instanceof OffsetDateTime) {
                return ((OffsetDateTime) temporalAccessor)
                            .toInstant()
                            .atZone(SYSTEM_ZONEID)
                            .truncatedTo(ChronoUnit.DAYS)
                            .toInstant()
                            .toEpochMilli();
            }
            return null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static Long parseTime(final String value) {
        try {
            ZonedDateTime zdt = DateTimeParser.parseZonedTime(value);
            if (zdt == null) {
                return null;
            }
            if (!zdt.getZone().equals(SYSTEM_ZONEID)) {
                zdt = zdt.toInstant().atZone(SYSTEM_ZONEID);
            }
            return zdt.withYear(1970)
                      .withDayOfYear(1)
                      .truncatedTo(ChronoUnit.MILLIS)
                      .toInstant()
                      .toEpochMilli();
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static Long parseTime(final String value, final DateTimeFormatter formatter) {
        try {
            final TemporalAccessor temporalAccessor = formatter.parseBest(value, ZonedDateTime::from, LocalDateTime::from, OffsetTime::from, LocalTime::from);
            if (temporalAccessor instanceof LocalTime) {
                return ((LocalTime) temporalAccessor)
                            .atDate(EPOCH_DATE)
                            .truncatedTo(ChronoUnit.MILLIS)
                            .atZone(SYSTEM_ZONEID)
                            .toInstant()
                            .toEpochMilli();
            }
            if (temporalAccessor instanceof OffsetTime) {
                return ((OffsetTime) temporalAccessor)
                            .withOffsetSameInstant(EPOCH_OFFSET)
                            .atDate(EPOCH_DATE)
                            .toLocalDateTime()
                            .truncatedTo(ChronoUnit.MILLIS)
                            .atZone(SYSTEM_ZONEID)
                            .toInstant()
                            .toEpochMilli();
            }
            if (temporalAccessor instanceof ZonedDateTime) {
                return ((ZonedDateTime) temporalAccessor)
                            .toInstant()
                            .atZone(SYSTEM_ZONEID)
                            .withYear(1970)
                            .withDayOfYear(1)
                            .truncatedTo(ChronoUnit.MILLIS)
                            .toInstant()
                            .toEpochMilli();
            }
            if (temporalAccessor instanceof LocalDateTime) {
                return ((LocalDateTime) temporalAccessor)
                            .atZone(SYSTEM_ZONEID)
                            .withYear(1970)
                            .withDayOfYear(1)
                            .truncatedTo(ChronoUnit.MILLIS)
                            .toInstant()
                            .toEpochMilli();
            }
            return null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static Long parseTimestamp(final String value) {
        try {
            ZonedDateTime zdt = DateTimeParser.parseZonedDateTime(value);
            if (zdt == null) {
                return null;
            }
            if (!zdt.getZone().equals(SYSTEM_ZONEID)) {
                zdt = zdt.toInstant().atZone(SYSTEM_ZONEID);
            }
            return zdt.truncatedTo(ChronoUnit.MILLIS).toInstant().toEpochMilli();
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static Long parseTimestamp(final String value, final DateTimeFormatter formatter) {
        try {
            final TemporalAccessor temporalAccessor = formatter.parseBest(value, ZonedDateTime::from, LocalDateTime::from, LocalDate::from);
            if (temporalAccessor instanceof LocalDateTime) {
                return ((LocalDateTime) temporalAccessor)
                            .atZone(SYSTEM_ZONEID)
                            .truncatedTo(ChronoUnit.MILLIS)
                            .toInstant()
                            .toEpochMilli();
            }
            if (temporalAccessor instanceof ZonedDateTime) {
                return ((ZonedDateTime) temporalAccessor)
                            .toInstant()
                            .atZone(SYSTEM_ZONEID)
                            .truncatedTo(ChronoUnit.MILLIS)
                            .toInstant()
                            .toEpochMilli();
            }
            if (temporalAccessor instanceof LocalDate) {
                return ((LocalDate) temporalAccessor)
                            .atStartOfDay(SYSTEM_ZONEID)
                            .toInstant()
                            .toEpochMilli();
            }
            return null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static long parseLenientTimestamp(final String value, final long minValue, final long defaultValue) {
        if (XStringUtils.isBlank(value)) {
            return defaultValue;
        }
        if (XNumberUtils.isNumber(value)) {
            final long tsp = Double.valueOf(value).longValue();
            return tsp < minValue ? defaultValue : tsp;
        } else {
            try {
                final ZonedDateTime zdt = parseCalendarTimestamp(value);
                if (zdt != null) {
                    final GregorianCalendar cal = GregorianCalendar.from(zdt);
                    final long tsp = cal.getTimeInMillis();
                    return tsp < minValue ? defaultValue : tsp;
                }
                return defaultValue;
            } catch (DateTimeParseException e) {
                return defaultValue;
            }
        }
    }

    public static Instant parseInstant(final String value) {
        try {
            final ZonedDateTime zdt = DateTimeParser.parseZonedDateTime(value);
            return zdt == null ? null : zdt.toInstant();
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static Instant parseInstant(final String value, final DateTimeFormatter formatter) {
        try {
            final TemporalAccessor temporalAccessor = formatter.parseBest(value, ZonedDateTime::from, LocalDateTime::from, LocalDate::from);
            if (temporalAccessor instanceof ZonedDateTime) {
                return ((ZonedDateTime) temporalAccessor)
                            .toInstant();
            }
            if (temporalAccessor instanceof LocalDateTime) {
                return ((LocalDateTime) temporalAccessor)
                            .atZone(SYSTEM_ZONEID)
                            .toInstant();
            }
            if (temporalAccessor instanceof LocalDate) {
                return ((LocalDate) temporalAccessor)
                            .atStartOfDay(SYSTEM_ZONEID)
                            .toInstant();
            }
            return null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static ZonedDateTime parseCalendarTime(final String value) {
        try {
            final ZonedDateTime zdt = DateTimeParser.parseZonedTime(value);
            return zdt == null ? null : zdt.withYear(1970).withDayOfYear(1).truncatedTo(ChronoUnit.MILLIS);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static ZonedDateTime parseCalendarTime(final String value, final DateTimeFormatter formatter) {
        try {
            final TemporalAccessor temporalAccessor = formatter.parseBest(value, ZonedDateTime::from, LocalDateTime::from, OffsetTime::from, LocalTime::from);
            if (temporalAccessor instanceof LocalTime) {
                return ((LocalTime) temporalAccessor)
                            .atDate(EPOCH_DATE)
                            .truncatedTo(ChronoUnit.MILLIS)
                            .atZone(SYSTEM_ZONEID);
            }
            if (temporalAccessor instanceof OffsetTime) {
                return ((OffsetTime) temporalAccessor)
                            .atDate(EPOCH_DATE)
                            .toZonedDateTime()
                            .truncatedTo(ChronoUnit.MILLIS);
            }
            if (temporalAccessor instanceof ZonedDateTime) {
                return ((ZonedDateTime) temporalAccessor)
                            .withYear(1970)
                            .withDayOfYear(1)
                            .truncatedTo(ChronoUnit.MILLIS);
            }
            if (temporalAccessor instanceof LocalDateTime) {
                return ((LocalDateTime) temporalAccessor)
                            .atZone(SYSTEM_ZONEID)
                            .withYear(1970)
                            .withDayOfYear(1)
                            .truncatedTo(ChronoUnit.MILLIS);
            }
            return null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static ZonedDateTime parseCalendarTimestamp(final String value) {
        try {
            final ZonedDateTime zdt = DateTimeParser.parseZonedDateTime(value);
            return zdt == null ? null : zdt.truncatedTo(ChronoUnit.MILLIS);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static ZonedDateTime parseCalendarTimestamp(final String value, final DateTimeFormatter formatter) {
        try {
            final TemporalAccessor temporalAccessor = formatter.parseBest(value, ZonedDateTime::from, LocalDateTime::from, LocalDate::from);
            if (temporalAccessor instanceof ZonedDateTime) {
                return ((ZonedDateTime) temporalAccessor)
                        .truncatedTo(ChronoUnit.MILLIS);
            }
            if (temporalAccessor instanceof LocalDateTime) {
                return ((LocalDateTime) temporalAccessor)
                            .atZone(SYSTEM_ZONEID)
                            .truncatedTo(ChronoUnit.MILLIS);
            }
            if (temporalAccessor instanceof LocalDate) {
                return ((LocalDate) temporalAccessor)
                            .atStartOfDay(SYSTEM_ZONEID);
            }
            return null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static String formatDate(final java.util.Date dt) {
        return LocalDateTime.ofInstant(dt.toInstant(), SYSTEM_ZONEID).format(DATE_FORMATTER);
    }

    public static String formatDate(final long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), SYSTEM_ZONEID).format(DATE_FORMATTER);
    }
    
    public static String formatTime(final java.util.Date dt) {
        return LocalDateTime.ofInstant(dt.toInstant(), SYSTEM_ZONEID).format(TIME_FORMATTER);
    }

    public static String formatTime(final long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), SYSTEM_ZONEID).format(TIME_FORMATTER);
    }
    
    public static String formatTimestamp(final java.util.Date dt) {
        return LocalDateTime.ofInstant(dt.toInstant(), SYSTEM_ZONEID).format(INSTANT_FORMATTER);
    }

    public static String formatTimestamp(final long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), SYSTEM_ZONEID).format(INSTANT_FORMATTER);
    }
    
    public static int toJulianDays(final Calendar cal) {
        final int year=cal.get(Calendar.YEAR);
        final int month=cal.get(Calendar.MONTH) + 1; // jan=1, feb=2,...
        final int day=cal.get(Calendar.DAY_OF_MONTH);
        return toJulianDays(year, month, day);
    }

    @SuppressWarnings("deprecation")
    public static int toJulianDays(final java.sql.Date dt) { 
        return toJulianDays(dt.getYear() + 1900, dt.getMonth() + 1, dt.getDay());
    }
    
    public static int toJulianDays(final int year, final int month, final int day) {  
        final int julianYear = year + (year < 0 ? 1 : 0) + (month <= 2 ? -1 : 0);
        final int julianMonth = month + (month <= 2 ? 13 : 1);

        double julian = (java.lang.Math.floor(365.25 * julianYear)
             + java.lang.Math.floor(30.6001*julianMonth) + day + 1720995.0);
        if (day + 31 * (month + 12 * year) >= JGREG) {
            // change over to Gregorian calendar
            int ja = (int)(0.01 * julianYear);
            julian += 2 - ja + (0.25 * ja);
        }
        return (int) Math.floor(julian);
    }
    
    @SuppressWarnings("deprecation")
    public static java.sql.Date fromJulianDays(final int julianDays) {
        int jalpha,ja,jb,jc,jd,je,year,month,day;
        double julian = julianDays + HALFSECOND / 86400.0;
        
        ja = (int) julian;
        if (ja>= JGREG) {
            jalpha = (int) (((ja - 1867216) - 0.25) / 36524.25);
            ja = ja + 1 + jalpha - jalpha / 4;
        }

        jb = ja + 1524;
        jc = (int) (6680.0 + ((jb - 2439870) - 122.1) / 365.25);
        jd = 365 * jc + jc / 4;
        je = (int) ((jb - jd) / 30.6001);
        day = jb - jd - (int) (30.6001 * je);
        month = je - 1;
        if (month > 12) {
            month = month - 12;
        }
        year = jc - 4715;
        if (month > 2) {
            year--;
        }
        if (year <= 0) {
            year--;
        }
        
        return new java.sql.Date(year - 1900, month - 1, day);
    }
    
    public static byte[] toJulianNanos(final Instant instant) {
        final LocalDateTime dt = LocalDateTime.ofInstant(instant, XTemporalUtils.SYSTEM_ZONEID);
        final int julianDays = toJulianDays(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth());
        final int nanos = dt.getNano();
        final int second = dt.getSecond();
        final int minute = dt.getMinute();
        final int hour = dt.getHour();
        final long nanosOfDay = hour * NANOS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR
                              + minute * NANOS_PER_SECOND * SECONDS_PER_MINUTE
                              + second * NANOS_PER_SECOND
                              + nanos;
        final ByteBuffer buf = ByteBuffer.allocate(12);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putLong(nanosOfDay);
        buf.putInt(julianDays);
        buf.flip();
        return buf.array();
    }
    
    @SuppressWarnings("deprecation")
    public static Instant fromJulianNanos(final byte[] bytes) {
        if (bytes.length != 12) {
            throw new IllegalArgumentException("CONNECTOR_HDFS_DATA_NANOS_LENGTH_INVALID");
        }
        final ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        final long nanosOfDay = buf.getLong();
        final int julianDays = buf.getInt();
        
        final java.sql.Date dt = fromJulianDays(julianDays);
        final int nanos = (int) (nanosOfDay % NANOS_PER_SECOND);
        final int seconds = (int) (nanosOfDay / NANOS_PER_SECOND);
        final int second = seconds % SECONDS_PER_MINUTE;
        final int minutes = seconds / SECONDS_PER_MINUTE;
        final int minute = minutes % MINUTES_PER_HOUR;
        final int hour = (minutes / MINUTES_PER_HOUR) % HOURS_PER_DAY;

        return LocalDateTime.of(
                dt.getYear() + 1900, dt.getMonth() + 1, dt.toLocalDate().getDayOfMonth(),
                hour, minute, second, nanos).atZone(XTemporalUtils.SYSTEM_ZONEID).toInstant();
    }
    
    public static long fromSerialTime(final double serial) {
        final int daysFromAD = (int) serial;
        final int millis = (int) ((serial - daysFromAD) * MILLIS_DAY);
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        cal.add(Calendar.DAY_OF_YEAR, daysFromAD - SERIAL_EPOCH_DAYS);
        cal.add(Calendar.MILLISECOND, millis);
        return cal.getTimeInMillis();
    }
    
    public static long fromSerialTime(final float serial) {
        final int daysFromAD = (int) serial;
        final int millis = (int) ((serial - daysFromAD) * MILLIS_DAY);
        final Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DAY_OF_YEAR, daysFromAD - SERIAL_EPOCH_DAYS);
        cal.add(Calendar.MILLISECOND, millis);
        return cal.getTimeInMillis();
    }
    
    public static double toSerialTime(final long millis) {
        final Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTimeInMillis(millis);
        
        final int daysFromAD = cal.get(Calendar.YEAR) + SERIAL_EPOCH_DAYS;

        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        
        final double time = (double) cal.getTimeInMillis() / MILLIS_DAY;
        return daysFromAD + (double) Math.round(time * 100000000d) / 100000000d;
    }
}