package com.exceeddata.ac.common.data.typedata;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.TimeZone;

import org.junit.Test;

import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.util.DateTimeParser;
import com.exceeddata.ac.common.util.XTemporalUtils;

public class TypeDataConvert {

    @Test
    public void testParseBest() throws EngineException {
        if (Calendar.getInstance().getTimeZone().equals(TimeZone.getTimeZone("Asia/Shanghai"))) {
            //this will be 02 because we are in Asia
            assertEquals("2016-01-02", DateData.valueOf(XTemporalUtils.parseDate("2016-01-01 23:00:00+0200")).toString());
            assertEquals("2016-01-01", DateData.valueOf(XTemporalUtils.parseDate("2016-01-01T10:00:00.58372+0200")).toString());
            assertEquals("2016-01-01", DateData.valueOf(XTemporalUtils.parseDate("2016-01-01")).toString());
            
            //this will be 16 because we are in Asia
            assertEquals("16:00:00", TimeData.valueOf(XTemporalUtils.parseTime("2016-01-01 10:00:00+0200")).toString());
            assertEquals("16:00:00", TimeData.valueOf(XTemporalUtils.parseTime("2016-01-01T10:00:00+0200")).toString());
            assertEquals("01:00:00.238", TimeData.valueOf(XTemporalUtils.parseTime("19:00:00.23828+0200")).toString());
            assertEquals("16:00:00", TimeData.valueOf(XTemporalUtils.parseTime("10:00:00+0200")).toString());
            
            //this will be 16 because we are in Asia
            assertEquals("2016-01-01 16:00:00", TimestampData.valueOf(XTemporalUtils.parseTimestamp("2016-01-01 10:00:00+0200")).toString());
            assertEquals("2016-01-01 16:00:00", TimestampData.valueOf(XTemporalUtils.parseTimestamp("2016-01-01T10:00:00+0200")).toString());
            assertEquals("2016-01-01 00:00:00", TimestampData.valueOf(XTemporalUtils.parseTimestamp("2016-01-01")).toString());
            
            assertEquals("2016-01-01 00:00:00", InstantData.valueOf(XTemporalUtils.parseInstant("2016-01-01")).toString());
            assertEquals("2016-01-01 10:00:00", InstantData.valueOf(XTemporalUtils.parseInstant("2016-01-01 10:00:00+0800".toString())).toString());
            assertEquals("2016-01-01 16:00:00.3828282", InstantData.valueOf(XTemporalUtils.parseInstant("2016-01-01T10:00:00.3828282+0200")).toString());
            assertEquals("2016-01-01 15:00:00.3828282", InstantData.valueOf(XTemporalUtils.parseInstant("2016-01-01T10:00:00.3828282+03:00")).toString());
            
            assertEquals("10:00:00+0200", CalendarTimeData.valueOf(XTemporalUtils.parseCalendarTime("2016-01-01 10:00:00+0200")).toString());
            assertEquals("10:00:00+0200", CalendarTimeData.valueOf(XTemporalUtils.parseCalendarTime("2016-01-01T10:00:00+0200")).toString());
            assertEquals("19:00:00.238+0200", CalendarTimeData.valueOf(XTemporalUtils.parseCalendarTime("19:00:00.23828+0200")).toString());
    
            assertEquals("2016-01-01 10:00:00+0200", CalendarTimestampData.valueOf(XTemporalUtils.parseCalendarTimestamp("2016-01-01 10:00:00+0200")).toString());
            assertEquals("2016-01-01 10:00:00.35+0200", CalendarTimestampData.valueOf(XTemporalUtils.parseCalendarTimestamp("2016-01-01T10:0:0.35+0200")).toString());
            assertEquals("2016-01-01 00:00:00+0800", CalendarTimestampData.valueOf(XTemporalUtils.parseCalendarTimestamp("2016-01-01")).toString());
    
            assertEquals(true, DateTimeParser.parseZonedDateTime("") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("20") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("201") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-0") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-0") == null);
            assertEquals("2016-01-01 00:00:00+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-01-01")));
            assertEquals("2016-01-01 00:00:00+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-01-01")));
            assertEquals("2016-01-01 00:00:00+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1")));
            assertEquals("2016-01-01 00:00:00+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("20160101")));
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-0T-01") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("20%6-01-01") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("20%6-58-01") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-01 05") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("20%6-01-01T05") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-01T05:") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-01T05:03") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-01T05:03:") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T5:3") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T5:3:") == null);
            assertEquals("2016-01-01 05:03:00+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T5:3:0")));
            assertEquals("2016-01-01 05:03:01+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01")));
            assertEquals("2016-01-01 05:03:01+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1 05:03:01")));
            assertEquals("2016-01-01 05:03:01+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("20160101050301")));
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T5:3:t") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T5:3:0s") == null);
            assertEquals("2016-01-01 05:03:01+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.")));
            assertEquals("2016-01-01 05:03:01.5+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.5")));
            assertEquals("2016-01-01 05:03:01.54+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.54")));
            assertEquals("2016-01-01 05:03:01.553828282+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.553828282")));
            assertEquals("2016-01-01 05:03:01.553828282+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55382828210")));
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.553y2828210") == null);
            assertEquals("2016-01-01 05:03:01.553828282+0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55382828210")));
            assertEquals("2016-01-01 05:03:01.55+0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55+02")));
            assertEquals("2016-01-01 05:03:01.55+0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55+02:00")));
            assertEquals("2016-01-01 05:03:01.55-0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55-02:00")));
            assertEquals("2016-01-01 05:03:01.55+0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55+02:00:00")));
            assertEquals("2016-01-01 00:00:00+0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1+02:00")));
            assertEquals("2016-01-01 00:00:00+0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1+02:00:00")));
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1+2:00:00") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-33+2:00:00") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01M5532828210") == null);
            
    
            assertEquals(true, DateTimeParser.parseZonedDateTime("+2:00:00") == null);
            assertEquals("01:02:01+0200", XTemporalUtils.CALENDAR_TIME_FORMATTER.format(
                    DateTimeParser.parseZonedTime("01:2:01+0200")));
            assertEquals("01:02:01+0800", XTemporalUtils.CALENDAR_TIME_FORMATTER.format(
                    DateTimeParser.parseZonedTime("1:2:1")));
            assertEquals("01:02:01.8382+0800", XTemporalUtils.CALENDAR_TIME_FORMATTER.format(
                    DateTimeParser.parseZonedTime("1:2:1.8382")));
            assertEquals("01:02:01.8382-0200", XTemporalUtils.CALENDAR_TIME_FORMATTER.format(
                    DateTimeParser.parseZonedTime("1:2:1.8382-0200")));
        } else if (Calendar.getInstance().getTimeZone().equals(TimeZone.getTimeZone("America/Los_Angeles"))) {
            //this will be 01 because we are in USA PST
            assertEquals("2016-01-01", DateData.valueOf(XTemporalUtils.parseDate("2016-01-02 02:00:00+0200")).toString());
            assertEquals("2016-01-01", DateData.valueOf(XTemporalUtils.parseDate("2016-01-02T02:00:00.58372+0200")).toString());
            assertEquals("2016-01-01", DateData.valueOf(XTemporalUtils.parseDate("2016-01-01")).toString());
            
            //this will be 16 because we are in USA PST
            assertEquals("16:00:00", TimeData.valueOf(XTemporalUtils.parseTime("2016-01-02 02:00:00+0200")).toString());
            assertEquals("16:00:00", TimeData.valueOf(XTemporalUtils.parseTime("2016-01-02T02:00:00+0200")).toString());
            assertEquals("09:00:00.238", TimeData.valueOf(XTemporalUtils.parseTime("19:00:00.23828+0200")).toString());
            assertEquals("00:00:00", TimeData.valueOf(XTemporalUtils.parseTime("10:00:00+0200")).toString());
            
            //this will be 16 because we are in USA PST
            assertEquals("2016-01-01 00:00:00", TimestampData.valueOf(XTemporalUtils.parseTimestamp("2016-01-01 10:00:00+0200")).toString());
            assertEquals("2016-01-01 00:00:00", TimestampData.valueOf(XTemporalUtils.parseTimestamp("2016-01-01T10:00:00+0200")).toString());
            assertEquals("2016-01-01 00:00:00", TimestampData.valueOf(XTemporalUtils.parseTimestamp("2016-01-01")).toString());
            
            assertEquals("2016-01-01 00:00:00", InstantData.valueOf(XTemporalUtils.parseInstant("2016-01-01")).toString());
            assertEquals("2016-01-01 18:00:00", InstantData.valueOf(XTemporalUtils.parseInstant("2016-01-02 10:00:00+0800".toString())).toString());
            assertEquals("2016-01-01 00:00:00.3828282", InstantData.valueOf(XTemporalUtils.parseInstant("2016-01-01T10:00:00.3828282+0200")).toString());
            assertEquals("2016-01-01 01:00:00.3828282", InstantData.valueOf(XTemporalUtils.parseInstant("2016-01-01T12:00:00.3828282+03:00")).toString());
            
            assertEquals("10:00:00+0200", CalendarTimeData.valueOf(XTemporalUtils.parseCalendarTime("2016-01-01 10:00:00+0200")).toString());
            assertEquals("10:00:00+0200", CalendarTimeData.valueOf(XTemporalUtils.parseCalendarTime("2016-01-01T10:00:00+0200")).toString());
            assertEquals("19:00:00.238+0200", CalendarTimeData.valueOf(XTemporalUtils.parseCalendarTime("19:00:00.23828+0200")).toString());
    
            assertEquals("2016-01-01 10:00:00+0200", CalendarTimestampData.valueOf(XTemporalUtils.parseCalendarTimestamp("2016-01-01 10:00:00+0200")).toString());
            assertEquals("2016-01-01 10:00:00.35+0200", CalendarTimestampData.valueOf(XTemporalUtils.parseCalendarTimestamp("2016-01-01T10:0:0.35+0200")).toString());
            assertEquals("2016-01-01 00:00:00-0800", CalendarTimestampData.valueOf(XTemporalUtils.parseCalendarTimestamp("2016-01-01")).toString());
    
            assertEquals(true, DateTimeParser.parseZonedDateTime("") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("20") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("201") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-0") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-0") == null);
            assertEquals("2016-01-01 00:00:00-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-01-01")));
            assertEquals("2016-01-01 00:00:00-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-01-01")));
            assertEquals("2016-01-01 00:00:00-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1")));
            assertEquals("2016-01-01 00:00:00-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("20160101")));
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-0T-01") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("20%6-01-01") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("20%6-58-01") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-01 05") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("20%6-01-01T05") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-01T05:") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-01T05:03") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-01-01T05:03:") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T5:3") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T5:3:") == null);
            assertEquals("2016-01-01 05:03:00-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T5:3:0")));
            assertEquals("2016-01-01 05:03:01-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01")));
            assertEquals("2016-01-01 05:03:01-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1 05:03:01")));
            assertEquals("2016-01-01 05:03:01-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("20160101050301")));
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T5:3:t") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T5:3:0s") == null);
            assertEquals("2016-01-01 05:03:01-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.")));
            assertEquals("2016-01-01 05:03:01.5-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.5")));
            assertEquals("2016-01-01 05:03:01.54-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.54")));
            assertEquals("2016-01-01 05:03:01.553828282-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.553828282")));
            assertEquals("2016-01-01 05:03:01.553828282-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55382828210")));
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.553y2828210") == null);
            assertEquals("2016-01-01 05:03:01.553828282-0800", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55382828210")));
            assertEquals("2016-01-01 05:03:01.55+0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55+02")));
            assertEquals("2016-01-01 05:03:01.55+0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55+02:00")));
            assertEquals("2016-01-01 05:03:01.55-0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55-02:00")));
            assertEquals("2016-01-01 05:03:01.55+0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01.55+02:00:00")));
            assertEquals("2016-01-01 00:00:00+0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1+02:00")));
            assertEquals("2016-01-01 00:00:00+0200", XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER.format(
                    DateTimeParser.parseZonedDateTime("2016-1-1+02:00:00")));
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1+2:00:00") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-33+2:00:00") == null);
            assertEquals(true, DateTimeParser.parseZonedDateTime("2016-1-1T05:03:01M5532828210") == null);
            
    
            assertEquals(true, DateTimeParser.parseZonedDateTime("+2:00:00") == null);
            assertEquals("01:02:01+0200", XTemporalUtils.CALENDAR_TIME_FORMATTER.format(
                    DateTimeParser.parseZonedTime("01:2:01+0200")));
            assertEquals("01:02:01-0800", XTemporalUtils.CALENDAR_TIME_FORMATTER.format(
                    DateTimeParser.parseZonedTime("1:2:1")));
            assertEquals("01:02:01.8382-0800", XTemporalUtils.CALENDAR_TIME_FORMATTER.format(
                    DateTimeParser.parseZonedTime("1:2:1.8382")));
            assertEquals("01:02:01.8382-0200", XTemporalUtils.CALENDAR_TIME_FORMATTER.format(
                    DateTimeParser.parseZonedTime("1:2:1.8382-0200")));
        }
    }
    
    @Test
    public void testConvertBigDecimal() throws EngineException {
        StringData sd = StringData.valueOf("3.31E-4");
        assertEquals(DataConv.toDecimalData(sd).getDecimal(), new BigDecimal("3.31E-4"));
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testConvertDate() throws EngineException, ParseException {
        final DateData dt = DateData.valueOf(new java.sql.Date(2016-1900, 4, 15));
        final DateData dt2 = DateData.valueOf(new java.util.Date(2016-1900, 4, 15, 3, 12, 11));
        final DateData dt3 = DateData.valueOf("2016-05-15");
        
        //to string
        assertEquals("2016-06-05", new DateData("2016-06-05").toString());
        assertEquals("2016-06-05", new DateData("2016-6-5").toString());
        
        //related
        final TimeData tm = new TimeData(new java.util.Date(2016-1900, 4, 15).getTime());
        final TimestampData ts = new TimestampData(new java.sql.Timestamp(2016-1900, 4, 15, 0, 0, 0, 0).getTime());
        final CalendarTimeData ctm = new CalendarTimeData(new java.util.Date(2016-1900, 4, 15).getTime());
        final CalendarTimestampData cts = new CalendarTimestampData(new java.sql.Timestamp(2016-1900, 4, 15, 0, 0, 0, 0).getTime());
        final LongData l = LongData.valueOf(new java.sql.Date(2016-1900, 4, 15).getTime());
        final DoubleData d = new DoubleData((double) new java.sql.Date(2016-1900, 4, 15).getTime());
        final NumericData n = new NumericData((double) new java.sql.Date(2016-1900, 4, 15).getTime());
        
        //to identical
        assertEquals(dt.compareTo(dt2) == 0, Boolean.TRUE);
        assertEquals(DataConv.toLongData(dt).compareTo(DataConv.toLongData(dt2)) == 0, Boolean.TRUE);
        assertEquals(dt.compareTo(dt3) == 0, Boolean.TRUE);
        assertEquals(DataConv.toLongData(dt).compareTo(DataConv.toLongData(dt3)) == 0, Boolean.TRUE);
        assertEquals(dt.compareTo(ts) == 0, Boolean.TRUE);
        assertEquals(DataConv.toLongData(dt).compareTo(DataConv.toLongData(ts)) == 0, Boolean.TRUE);
        
        //to convertible
        assertEquals(DataConv.toDateData(DataConv.toLongData(dt)).compareTo(dt) == 0, Boolean.TRUE);
        assertEquals(DataConv.toDateData(DataConv.toDoubleData(dt)).compareTo(dt) == 0, Boolean.TRUE);
        assertEquals(DataConv.toDateData(DataConv.toDecimalData(dt)).compareTo(dt) == 0, Boolean.TRUE);
        assertEquals(DataConv.toDateData(DataConv.toDateData(dt)).compareTo(dt) == 0, Boolean.TRUE);
        assertEquals(DataConv.toDateData(DataConv.toTimestampData(dt)).compareTo(dt) == 0, Boolean.TRUE);
        assertEquals(DataConv.toDateData(DataConv.toCalendarTimestampData(dt)).compareTo(dt) == 0, Boolean.TRUE);
        
        //to time
        assertEquals(DataConv.toDateData(DataConv.toTimeData(dt)).compareTo(DateData.ZERO) == 0, Boolean.TRUE);
        assertEquals(DataConv.toDateData(DataConv.toCalendarTimeData(dt)).compareTo(DateData.ZERO) == 0, Boolean.TRUE);
        
        //to convertible
        assertEquals(DataConv.toLongData(dt), l);
        assertEquals(DataConv.toDoubleData(dt), d);
        assertEquals(DataConv.toNumericData(dt), n);
        assertEquals(DataConv.toTimeData(dt).compareTo(tm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimestampData(dt).compareTo(ts) == 0, Boolean.TRUE);
        assertEquals(DataConv.toCalendarTimeData(dt).compareTo(ctm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toCalendarTimestampData(dt).compareTo(cts) == 0, Boolean.TRUE);
        
        //to primitive
        assertEquals(dt.toLong(), l.getLong());
        assertEquals(dt.toLong(), d.toLong());
        assertEquals(dt.toDouble(), l.toDouble());
        assertEquals(dt.toDouble(), d.getDouble());
        assertEquals(dt.toDouble(), n.getDouble());
        assertEquals(dt.toDecimal(), l.toDecimal());
        assertEquals(dt.toBoolean(), l.toBoolean());
        assertEquals(dt.toBoolean(), d.toBoolean());
        
        //reverse
        assertEquals(dt.getTimeInMillis(), DataConv.toDateData(l).getTimeInMillis());
        assertEquals(dt.getTimeInMillis(), DataConv.toDateData(d).getTimeInMillis());
        
        //to overflow
        assertEquals(dt.toInt(), l.toInt());
        assertEquals(dt.toFloat(), l.toFloat());
        
        //to binary
        assertEquals(1, DataConv.toBinaryData(DataConv.toIntData(BinaryData.valueOf(new byte[] {(byte) 1}))).toInt().intValue());
        assertEquals(1l, DataConv.toBinaryData(DataConv.toLongData(BinaryData.valueOf(new byte[] {(byte) 1}))).toLong().longValue());
        assertEquals(dt.toInt(), DataConv.toBinaryData(DataConv.toIntData(dt)).toInt());
        assertEquals(dt.toLong(), DataConv.toBinaryData(DataConv.toLongData(dt)).toLong());
        

        assertEquals(15375, (int) (DataConv.toBinaryData(FloatData.valueOf(1.5375F)).toFloat() * 10000));
        assertEquals(15375, (int) (DataConv.toBinaryData(DoubleData.valueOf(1.5375D)).toDouble() * 10000));
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testConvertTime() throws EngineException, ParseException {
        final TimeData tm = new TimeData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
        final TimeData tm2 = new TimeData(new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
        final TimeData tm3 = new TimeData("13:11:15.7");
        
        //to string
        assertEquals("13:11:15.782", LocalDateTime.ofInstant(new TimeData("13:11:15.78282").toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.TIME_FORMATTER));
        assertEquals("03:01:05.7", LocalDateTime.ofInstant(new TimeData("03:01:05.7").toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.TIME_FORMATTER));
        assertEquals("03:01:05.7", LocalDateTime.ofInstant(new TimeData("3:1:5.7").toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.TIME_FORMATTER));
        assertEquals("13:11:15", LocalDateTime.ofInstant(new TimeData("13:11:15.").toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.TIME_FORMATTER));
        assertEquals("13:11:15", LocalDateTime.ofInstant(new TimeData("13:11:15").toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.TIME_FORMATTER));
        assertEquals("13:11:15", new TimeData("13:11:15").toString());
        assertEquals("13:11:15", new TimeData("13:11:15.").toString());
        assertEquals("03:01:05.7", new TimeData("3:1:5.7").toString());
        
        //related
        final TimestampData ts = new TimestampData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
        final DateData dt = DateData.valueOf(new java.sql.Date(70, 0, 1));
        final CalendarTimeData ctm = new CalendarTimeData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
        final CalendarTimestampData cts = new CalendarTimestampData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
        final LongData l = LongData.valueOf( new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
        final DoubleData d = new DoubleData((double) new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
        final NumericData n = new NumericData((double) new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
        
        //to identical
        assertEquals(tm.compareTo(tm2) == 0, Boolean.TRUE);
        assertEquals(DataConv.toLongData(tm).compareTo(DataConv.toLongData(tm2)) == 0, Boolean.TRUE);
        assertEquals(tm.compareTo(tm3) == 0, Boolean.TRUE);
        assertEquals(DataConv.toLongData(tm).compareTo(DataConv.toLongData(tm3)) == 0, Boolean.TRUE);
        assertEquals(tm.compareTo(ctm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toLongData(tm).compareTo(DataConv.toLongData(ctm)) == 0, Boolean.TRUE);
        
        //to convertible
        assertEquals(DataConv.toTimeData(DataConv.toLongData(tm)).compareTo(tm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimeData(DataConv.toDoubleData(tm)).compareTo(tm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimeData(DataConv.toDecimalData(tm)).compareTo(tm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimeData(DataConv.toTimeData(tm)).compareTo(tm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimeData(DataConv.toTimestampData(tm)).compareTo(tm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimeData(DataConv.toCalendarTimeData(tm)).compareTo(tm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimeData(DataConv.toCalendarTimestampData(tm)).compareTo(tm) == 0, Boolean.TRUE);
        
        //to date
        assertEquals(DataConv.toTimeData(DataConv.toDateData(tm)).compareTo(TimeData.ZERO) == 0, Boolean.TRUE);
        assertEquals(DataConv.toDateData(tm).compareTo(dt) == 0, Boolean.TRUE);
        
        //to convertible
        assertEquals(DataConv.toLongData(tm), l);
        assertEquals(DataConv.toDoubleData(tm), d);
        assertEquals(DataConv.toNumericData(tm), n);
        assertEquals(DataConv.toDateData(tm).compareTo(DateData.ZERO) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimestampData(tm).compareTo(ts) == 0, Boolean.TRUE);
        assertEquals(DataConv.toCalendarTimeData(tm).compareTo(ctm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toCalendarTimestampData(tm).compareTo(cts) == 0, Boolean.TRUE);
        
        //to primitive
        assertEquals(tm.toLong(), l.getLong());
        assertEquals(tm.toLong(), d.toLong());
        assertEquals(tm.toDouble(), l.toDouble());
        assertEquals(tm.toDouble(), d.getDouble());
        assertEquals(tm.toDouble(), n.getDouble());
        assertEquals(tm.toDecimal(), l.toDecimal());
        assertEquals(tm.toBoolean(), l.toBoolean());
        assertEquals(tm.toBoolean(), d.toBoolean());
        
        //reverse
        assertEquals(tm.getTimeInMillis(), DataConv.toTimeData(l).getTimeInMillis());
        assertEquals(tm.getTimeInMillis(), DataConv.toTimeData(d).getTimeInMillis());
        
        //to overflow
        assertEquals(tm.toInt(), l.toInt());
        assertEquals(tm.toFloat(), l.toFloat());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testConvertTimestamp() throws EngineException, ParseException {
        final TimestampData ts = new TimestampData(new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
        final TimestampData ts2 = new TimestampData("2016-5-15 13:11:15.7");
        
        //to string
        assertEquals("2016-05-15 13:11:15.782", LocalDateTime.ofInstant(new TimestampData("2016-5-15 13:11:15.78282").toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.INSTANT_FORMATTER));
        assertEquals("2016-05-05 03:01:05.7", LocalDateTime.ofInstant(new TimestampData("2016-05-05 03:01:05.7").toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.INSTANT_FORMATTER));
        assertEquals("2016-05-05 03:01:05.7", LocalDateTime.ofInstant(new TimestampData("2016-5-5 3:1:5.7").toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.INSTANT_FORMATTER));
        assertEquals("2016-05-15 13:11:15", LocalDateTime.ofInstant(new TimestampData("2016-05-15 13:11:15.").toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.INSTANT_FORMATTER));
        assertEquals("2016-05-15 13:11:15", LocalDateTime.ofInstant(new TimestampData("2016-05-15 13:11:15").toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.INSTANT_FORMATTER));
        assertEquals("2016-05-15 13:11:15", new TimestampData("2016-05-15 13:11:15").toString());
        assertEquals("2016-05-05 03:01:05.78", new TimestampData("2016-5-5 3:1:5.78").toString());
        assertEquals("2016-05-05 03:01:05", new TimestampData("2016-5-5 3:1:5.000").toString());
        
        //related
        final TimestampData tsd = new TimestampData(new java.sql.Timestamp(2016-1900, 4, 15, 0, 0, 0, 0).getTime());
        final TimestampData tsm = new TimestampData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
        final TimeData tm = new TimeData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
        final DateData dt = DateData.valueOf(new java.sql.Date(2016-1900, 4, 15));
        final CalendarTimeData ctm = new CalendarTimeData(new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
        final CalendarTimestampData cts = new CalendarTimestampData(new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
        final LongData l = LongData.valueOf( new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
        final DoubleData d = new DoubleData((double) new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
        final NumericData n = new NumericData((double) new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
        
        //to identical
        assertEquals(ts.compareTo(ts2) == 0, Boolean.TRUE);
        assertEquals(DataConv.toLongData(ts).compareTo(DataConv.toLongData(ts2)) == 0, Boolean.TRUE);
        assertEquals(ts.compareTo(cts) == 0, Boolean.TRUE);
        assertEquals(DataConv.toLongData(ts).compareTo(DataConv.toLongData(cts)) == 0, Boolean.TRUE);
        
        //to convertible
        assertEquals(DataConv.toTimestampData(DataConv.toLongData(ts)).compareTo(ts) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimestampData(DataConv.toDoubleData(ts)).compareTo(ts) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimestampData(DataConv.toDecimalData(ts)).compareTo(ts) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimestampData(DataConv.toTimestampData(ts)).compareTo(ts) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimestampData(DataConv.toCalendarTimestampData(ts)).compareTo(ts) == 0, Boolean.TRUE);
        
        //to date & time
        assertEquals(DataConv.toTimestampData(DataConv.toDateData(ts)).compareTo(tsd) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimestampData(DataConv.toTimeData(ts)).compareTo(tsm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimestampData(DataConv.toCalendarTimeData(ts)).compareTo(tsm) == 0, Boolean.TRUE);
        
        //to convertible
        assertEquals(DataConv.toLongData(ts), l);
        assertEquals(DataConv.toDoubleData(ts), d);
        assertEquals(DataConv.toNumericData(ts), n);
        assertEquals(DataConv.toDateData(ts).compareTo(dt) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimeData(ts).compareTo(tm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toTimestampData(ts).compareTo(ts) == 0, Boolean.TRUE);
        assertEquals(DataConv.toCalendarTimeData(ts).compareTo(ctm) == 0, Boolean.TRUE);
        assertEquals(DataConv.toCalendarTimestampData(ts).compareTo(cts) == 0, Boolean.TRUE);
        
        //to primitive
        assertEquals(ts.toLong(), l.getLong());
        assertEquals(ts.toLong(), d.toLong());
        assertEquals(ts.toDouble(), l.toDouble());
        assertEquals(ts.toDouble(), d.getDouble());
        assertEquals(ts.toDecimal(), l.toDecimal());
        assertEquals(ts.toBoolean(), l.toBoolean());
        assertEquals(ts.toBoolean(), d.toBoolean());
        
        //reverse
        assertEquals(ts.getTimeInMillis(), DataConv.toTimestampData(l).getTimeInMillis());
        assertEquals(ts.getTimeInMillis(), DataConv.toTimestampData(d).getTimeInMillis());
        
        //to overflow
        assertEquals(ts.toInt(), l.toInt());
        assertEquals(ts.toFloat(), l.toFloat());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testConvertCalendarTime() throws EngineException, ParseException {
        if (Calendar.getInstance().getTimeZone().equals(TimeZone.getTimeZone("Asia/Shanghai"))) {
            final CalendarTimeData ctm = new CalendarTimeData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
            final CalendarTimeData ctm2 = new CalendarTimeData(new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
            final CalendarTimeData ctm3 = new CalendarTimeData("13:11:15.7+0800");
            final CalendarTimeData ctm4 = new CalendarTimeData("13:11:15.7");
            
            //related
            final TimestampData ts = new TimestampData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
            final DateData dt = DateData.valueOf(new java.sql.Date(70, 0, 1));
            final TimeData tm = new TimeData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
            final CalendarTimestampData cts = new CalendarTimestampData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
            final LongData l = LongData.valueOf( new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
            final DoubleData d = new DoubleData((double) new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
            
            //to identical
            assertEquals(ctm.compareTo(ctm2) == 0, Boolean.TRUE);
            assertEquals(DataConv.toLongData(ctm).compareTo(DataConv.toLongData(ctm2)) == 0, Boolean.TRUE);
            assertEquals(ctm.compareTo(ctm3) == 0, Boolean.TRUE);
            assertEquals(DataConv.toLongData(ctm).compareTo(DataConv.toLongData(ctm3)) == 0, Boolean.TRUE);
            assertEquals(ctm.compareTo(ctm4) == 0, Boolean.TRUE);
            assertEquals(DataConv.toLongData(ctm).compareTo(DataConv.toLongData(ctm4)) == 0, Boolean.TRUE);
            assertEquals(ctm.compareTo(tm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toLongData(ctm).compareTo(DataConv.toLongData(tm)) == 0, Boolean.TRUE);
            
            //to convertible
            assertEquals(DataConv.toCalendarTimeData(DataConv.toLongData(ctm)).compareTo(ctm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimeData(DataConv.toDoubleData(ctm)).compareTo(ctm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimeData(DataConv.toDecimalData(ctm)).compareTo(ctm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimeData(DataConv.toTimeData(ctm)).compareTo(ctm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimeData(DataConv.toTimestampData(ctm)).compareTo(ctm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimeData(DataConv.toCalendarTimeData(ctm)).compareTo(ctm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimeData(DataConv.toCalendarTimestampData(ctm)).compareTo(ctm) == 0, Boolean.TRUE);
            
            //to date
            assertEquals(DataConv.toTimeData(DataConv.toDateData(ctm)).compareTo(CalendarTimeData.ZERO) == 0, Boolean.TRUE);
            assertEquals(DataConv.toDateData(ctm).compareTo(dt) == 0, Boolean.TRUE);
            
            //to convertible
            assertEquals(DataConv.toLongData(ctm), l);
            assertEquals(DataConv.toDoubleData(ctm), d);
            assertEquals(DataConv.toDateData(ctm).compareTo(DateData.ZERO) == 0, Boolean.TRUE);
            assertEquals(DataConv.toTimestampData(ctm).compareTo(ts) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimeData(ctm).compareTo(ctm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimestampData(ctm).compareTo(cts) == 0, Boolean.TRUE);
            
            //to primitive
            assertEquals(ctm.toLong(), l.getLong());
            assertEquals(ctm.toLong(), d.toLong());
            assertEquals(ctm.toDouble(), l.toDouble());
            assertEquals(ctm.toDouble(), d.getDouble());
            assertEquals(ctm.toDecimal(), l.toDecimal());
            assertEquals(ctm.toBoolean(), l.toBoolean());
            assertEquals(ctm.toBoolean(), d.toBoolean());
            
            //reverse
            assertEquals(ctm.getTimeInMillis(), DataConv.toTimeData(l).getTimeInMillis());
            assertEquals(ctm.getTimeInMillis(), DataConv.toTimeData(d).getTimeInMillis());
            
            //to overflow
            assertEquals(ctm.toInt(), l.toInt());
            assertEquals(ctm.toFloat(), l.toFloat());
        }
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testConvertCalendarTimestamp() throws EngineException, ParseException {
        if (Calendar.getInstance().getTimeZone().equals(TimeZone.getTimeZone("Asia/Shanghai"))) {
            final CalendarTimestampData cts = new CalendarTimestampData(new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
            final CalendarTimestampData cts2 = new CalendarTimestampData("2016-5-15 13:11:15.7+0800");
            final CalendarTimestampData cts3 = new CalendarTimestampData("2016-5-15 13:11:15.7");
            
            //related
            final TimestampData ts = new TimestampData(new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
            final TimestampData tsd = new TimestampData(new java.sql.Timestamp(2016-1900, 4, 15, 0, 0, 0, 0).getTime());
            final TimestampData tsm = new TimestampData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
            final TimeData tm = new TimeData(new java.sql.Timestamp(70, 0, 1, 13, 11, 15, 700000000).getTime());
            final DateData dt = DateData.valueOf(new java.sql.Date(2016-1900, 4, 15));
            final CalendarTimeData ctm = new CalendarTimeData(new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
            final LongData l = LongData.valueOf( new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
            final DoubleData d = new DoubleData((double) new java.sql.Timestamp(2016-1900, 4, 15, 13, 11, 15, 700000000).getTime());
            
            //to identical
            assertEquals(cts.compareTo(cts2) == 0, Boolean.TRUE);
            assertEquals(DataConv.toLongData(cts).compareTo(DataConv.toLongData(cts2)) == 0, Boolean.TRUE);
            assertEquals(cts.compareTo(cts3) == 0, Boolean.TRUE);
            assertEquals(DataConv.toLongData(cts).compareTo(DataConv.toLongData(cts3)) == 0, Boolean.TRUE);
            assertEquals(cts.compareTo(ts) == 0, Boolean.TRUE);
            assertEquals(DataConv.toLongData(cts).compareTo(DataConv.toLongData(ts)) == 0, Boolean.TRUE);
            
            //to convertible
            assertEquals(DataConv.toCalendarTimestampData(DataConv.toLongData(cts)).compareTo(cts) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimestampData(DataConv.toDoubleData(cts)).compareTo(cts) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimestampData(DataConv.toDecimalData(cts)).compareTo(cts) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimestampData(DataConv.toTimestampData(cts)).compareTo(cts) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimestampData(DataConv.toCalendarTimestampData(cts)).compareTo(cts) == 0, Boolean.TRUE);
            
            //to date & time
            assertEquals(DataConv.toCalendarTimestampData(DataConv.toDateData(cts)).compareTo(tsd) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimestampData(DataConv.toTimeData(cts)).compareTo(tsm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimestampData(DataConv.toCalendarTimeData(cts)).compareTo(tsm) == 0, Boolean.TRUE);
            
            //to convertible
            assertEquals(DataConv.toLongData(cts), l);
            assertEquals(DataConv.toDoubleData(cts), d);
            assertEquals(DataConv.toDateData(cts).compareTo(dt) == 0, Boolean.TRUE);
            assertEquals(DataConv.toTimeData(cts).compareTo(tm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toTimestampData(cts).compareTo(cts) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimeData(cts).compareTo(ctm) == 0, Boolean.TRUE);
            assertEquals(DataConv.toCalendarTimestampData(cts).compareTo(cts) == 0, Boolean.TRUE);
            
            //to primitive
            assertEquals(cts.toLong(), l.getLong());
            assertEquals(cts.toLong(), d.toLong());
            assertEquals(cts.toDouble(), l.toDouble());
            assertEquals(cts.toDouble(), d.getDouble());
            assertEquals(cts.toDecimal(), l.toDecimal());
            assertEquals(cts.toBoolean(), l.toBoolean());
            assertEquals(cts.toBoolean(), d.toBoolean());
            
            //reverse
            assertEquals(cts.getTimeInMillis(), DataConv.toTimestampData(l).getTimeInMillis());
            assertEquals(cts.getTimeInMillis(), DataConv.toTimestampData(d).getTimeInMillis());
            
            //to overflow
            assertEquals(cts.toInt(), l.toInt());
            assertEquals(cts.toFloat(), l.toFloat());
        }
    }
    
    public void testConvertVectorNull() throws EngineException {
        //full null -> null
        ListData a;
        SetData b;
        SetData c;
        
        a = new ListData(new ArrayList<TypeData>(Arrays.asList(new TypeData[] { IntData.NULL, DoubleData.NULL })));
        b = new SetData(new LinkedHashSet<TypeData>(Arrays.asList(new TypeData[] { IntData.NULL, DoubleData.NULL })));
        
        assertEquals(DataConv.toDenseVectorData(a).isNull(), true);
        assertEquals(DataConv.toDenseVectorData(b).isNull(), true);
        
        //partial null -> 0d
        a = new ListData(new ArrayList<TypeData>(Arrays.asList(new TypeData[] { IntData.NULL, DoubleData.ONE })));
        b = new SetData(new LinkedHashSet<TypeData>(Arrays.asList(new TypeData[] { IntData.NULL, DoubleData.ONE })));
        c = new SetData(new LinkedHashSet<TypeData>(Arrays.asList(new TypeData[] { IntData.NULL, DoubleData.ZERO })));
        
        assertArrayEquals(DataConv.toDenseVectorData(a).getUnsafeDoubles(), new double[] {0, 1}, 0.0001);
        assertArrayEquals(DataConv.toDenseVectorData(b).getUnsafeDoubles(), new double[] {0, 1}, 0.0001);
        assertArrayEquals(DataConv.toDenseVectorData(c).getUnsafeDoubles(), new double[] {0}, 0.0001);
        
        assertArrayEquals(DataConv.toSparseVectorData(a).getDoubles(), new double[] {0, 1}, 0.0001);
        assertArrayEquals(DataConv.toSparseVectorData(b).getDoubles(), new double[] {0, 1}, 0.0001);
        assertArrayEquals(DataConv.toSparseVectorData(c).getDoubles(), new double[] {0}, 0.0001);
        
        
        
    }
}
