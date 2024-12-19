package com.exceeddata.ac.common.data.typedata;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

import com.exceeddata.ac.common.exception.EngineException;

public class TemporalTypeDataCompare {
    
    @Test
    public void testParseTimeZone() throws EngineException, ParseException {
        String s1 = "2015-05-11 13:15:12.0-0800";
        String s2 = "13:15:12.0-0800";
        assertEquals(CalendarTimestampData.valueOf(s1).toTime().compareTo(CalendarTimeData.valueOf(s2).toTime()), 0);
    }
    
    private void compareTimeZones(final TypeData t1, final TypeData t2, final GregorianCalendar cal) throws EngineException, ParseException{
        assertEquals(DataConv.toTimestampData(t1), DataConv.toTimestampData(t2));
        assertEquals(DataConv.toCalendarTimestampData(t1), DataConv.toCalendarTimestampData(t2));
        assertEquals(DataConv.toTimeData(t1), DataConv.toTimeData(t2));
        assertEquals(DataConv.toCalendarTimeData(t1), DataConv.toCalendarTimeData(t2));
        assertEquals(t1.toTimeWithTimeZone().compareTo(t2.toTimeWithTimeZone()), 0);
        assertEquals(t1.toTime().compareTo(t2.toTime()), 0);
        assertEquals(t1.toTimestamp().compareTo(t2.toTimestamp()), 0);
        assertEquals(t1.toTimestampWithTimeZone().compareTo(cal), 0);
        assertEquals(t1.toTimestampWithTimeZone().compareTo(t2.toTimestampWithTimeZone()), 0);
        assertEquals(t1.toLong(), t2.toLong());
        assertEquals(t1.toDate(), t2.toDate());
        assertEquals(DataConv.toDateData(t1), DataConv.toDateData(t2));
    }
    
    @Test
    public void testTimeZone() throws EngineException, ParseException {
        final TimeZone tz = TimeZone.getDefault();
        int offset = tz.getRawOffset();
        String tztext = String.format("%s%02d%02d", offset >= 0 ? "+" : "-", offset / 3600000, (offset / 60000) % 60);
         
        final DateData d1 = DateData.valueOf("2016-05-01");
        final CalendarTimestampData ct1 = CalendarTimestampData.valueOf("2016-05-01 00:00:00" + tztext);
        final GregorianCalendar cal = new GregorianCalendar();
        cal.clear();
        cal.setTimeInMillis(d1.getTimeInMillis());
        compareTimeZones(d1, ct1, cal);
        
        
        final TimestampData ts2 = TimestampData.valueOf("2016-05-01 10:04:05");
        final CalendarTimestampData ct2 = CalendarTimestampData.valueOf("2016-05-01 10:04:05" + tztext);
        cal.clear();
        cal.setTimeInMillis(ts2.getTimeInMillis());
        compareTimeZones(ts2, ct2, cal);
        

        final TimeData tm3 = TimeData.valueOf("10:04:05");
        final CalendarTimeData ctm3 = CalendarTimeData.valueOf("10:04:05" + tztext);
        cal.clear();
        cal.setTimeInMillis(tm3.getTimeInMillis());
        compareTimeZones(tm3, ctm3, cal);
        
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCompareInstant() throws EngineException, ParseException {
        final InstantData ins = InstantData.valueOf(new java.util.Date(2016-1900, 4, 1, 10, 15, 20).toInstant());
        
        final DateData d01 = DateData.valueOf(new java.sql.Date(2016-1900, 4, 1));
        final DateData d02 = DateData.valueOf(new java.sql.Date(2016-1900, 4, 2));
        assertEquals(ins.compareTo(d01) > 0, true);
        assertEquals(ins.compareTo(d02) >= 0, false);
        assertEquals(d02.compareTo(ins) > 0, true);
        assertEquals(d01.compareTo(ins) >= 0, false);
        assertEquals(DataConv.toDateData(ins).compareTo(d01) == 0, true);
        
        final TimestampData t01 = TimestampData.valueOf(new java.sql.Timestamp(2016-1900, 4, 1, 10, 14, 15, 0));
        final TimestampData t02 = TimestampData.valueOf(new java.sql.Timestamp(2016-1900, 4, 1, 10, 16, 15, 0));
        assertEquals(ins.compareTo(t01) > 0, true);
        assertEquals(ins.compareTo(t02) >= 0, false);
        assertEquals(t02.compareTo(ins) > 0, true);
        assertEquals(t01.compareTo(ins) >= 0, false);
        
        final InstantData ins02 = InstantData.valueOf(new java.sql.Timestamp(2016-1900, 4, 1, 10, 15, 20, 0).toInstant());
        final InstantData ins03 = InstantData.valueOf(new java.sql.Timestamp(2016-1900, 4, 1, 10, 15, 20, 30).toInstant());

        assertEquals(ins02.compareTo(ins) == 0, true);
        assertEquals(ins03.compareTo(ins) > 0, true);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testCompareDateTime() throws EngineException, ParseException {
        final DateData d01 = DateData.valueOf(new java.sql.Date(2016-1900, 4, 1));
        final DateData d02 = DateData.valueOf(new java.util.Date(2016-1900, 4, 1));
        final DateData d03 = DateData.valueOf(new java.sql.Date(2016-1900, 4, 1).getTime());
        final DateData d04 = DateData.valueOf(new java.util.Date(2016-1900, 4, 1).getTime());
        final DateData d1 = DateData.valueOf("2016-05-01");
        final DateData d2 = DateData.valueOf("2016-05-02");
        final DateData d3 = DateData.valueOf("2016-05-01 19:04:05");
        final DateData d4 = DateData.valueOf("2016-05-01 02:04:05");
        final TimeData tm01 = new TimeData(new java.util.Date(1, 1, 1, 19, 4, 5).getTime());
        final TimeData tm02 = new TimeData(new java.sql.Time(19, 4, 5));
        final TimeData tm03 = new TimeData(new java.sql.Time(19, 4, 5).getTime());
        final TimeData tm1 = new TimeData("19:04:05");
        final TimeData tm2 = new TimeData("19:04:06");
        final TimeData tm3 = new TimeData("00:00:00.00");
        final TimestampData ts01 = new TimestampData(new java.sql.Timestamp(2016-1900, 4, 1, 19, 4, 5, 0));
        final TimestampData ts02 = new TimestampData(new java.util.Date(2016-1900, 4, 1, 19, 4, 5).getTime());
        final TimestampData ts1 = new TimestampData("2016-05-01 19:04:05");
        final TimestampData ts2 = new TimestampData("2016-05-02 19:04:06");
        final TimestampData ts3 = new TimestampData("2016-05-02 00:00:00.00");

        final CalendarTimeData ctm02 = new CalendarTimeData("19:04:05");
        final CalendarTimestampData cts02 = new CalendarTimestampData("2016-05-01 19:04:05");
        final CalendarTimestampData cts3 = new CalendarTimestampData("1970-01-01 19:04:05");
        
        //parsing vs. date creation
        assertEquals(true, d01.compareTo(d1) == 0);
        assertEquals(true, d02.compareTo(d1) == 0);
        assertEquals(true, d03.compareTo(d1) == 0);
        assertEquals(true, d04.compareTo(d1) == 0);
        assertEquals(true, d1.compareTo(d3) == 0);
        assertEquals(true, d1.compareTo(d4) == 0);
        assertEquals(true, tm01.compareTo(tm1) == 0);
        assertEquals(true, tm02.compareTo(tm1) == 0);
        assertEquals(true, tm03.compareTo(tm1) == 0);
        assertEquals(true, ts01.compareTo(ts1) == 0);
        assertEquals(true, ts02.compareTo(ts1) == 0);
        
        //conversion
        assertEquals(true, DataConv.toTimeData(ctm02).compareTo(tm1) == 0);
        assertEquals(true, DataConv.toTimeData(cts02).compareTo(tm1) == 0);
        assertEquals(true, DataConv.toDateData(cts02).compareTo(d1) == 0);
        assertEquals(true, DataConv.toTimestampData(cts02).compareTo(ts1) == 0);
        assertEquals(true, DataConv.toTimestampData(d2).compareTo(ts3) == 0);
        assertEquals(true, DateData.valueOf(0l).compareTo(DataConv.toDateData(tm3)) == 0);
        assertEquals(true, DataConv.toDateData(cts3).compareTo(DateData.ZERO) == 0);
        assertEquals(true, DateData.valueOf(0l).compareTo(TimeData.ZERO) == 0);
        
        //cross type comparison
        assertEquals(true, d2.compareTo(ts3) == 0);     
        assertEquals(true, ts3.compareTo(d2) == 0);
        assertEquals(true, ctm02.compareTo(tm1) == 0);
        assertEquals(true, tm1.compareTo(ctm02) == 0);
        assertEquals(true, cts3.compareTo(tm1) == 0);
        assertEquals(true, tm1.compareTo(cts3) == 0);
        
        //same type comparison
        assertEquals(true, d1.compareTo(d2) < 0);
        assertEquals(true, d1.compareTo(tm1) > 0);
        assertEquals(true, tm2.compareTo(tm1) > 0);
        assertEquals(true, DataConv.toTimeData(d1).compareTo(tm1) < 0);
        assertEquals(true, DataConv.toTimeData(d1).compareTo(DataConv.toTimeData(d2)) == 0);
        assertEquals(true, DataConv.toTimeData(d1).compareTo(tm3) == 0);
        assertEquals(true, DataConv.toTimestampData(d2).compareTo(ts3) == 0);
        assertEquals(true, DataConv.toTimestampData(d1).compareTo(ts1) < 0);
        assertEquals(true, DataConv.toDateData(tm1).compareTo(d2) < 0);
        assertEquals(true, DataConv.toDateData(tm1).compareTo(DataConv.toDateData(tm2)) == 0);
        assertEquals(true, ts1.compareTo(ts2) < 0);
        assertEquals(true, ts1.compareTo(tm1) > 0);
        assertEquals(true, ts1.compareTo(d1) > 0);
        assertEquals(true, DataConv.toDateData(ts1).compareTo(d1) == 0);
        assertEquals(true, DataConv.toTimeData(ts1).compareTo(tm1) == 0);
        assertEquals(true, d1.compareTo(tm1) > 0);
    }
}
