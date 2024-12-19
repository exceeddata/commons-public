package com.exceeddata.ac.common.data.typedata;

/**
 * Data Cache static methods, to be overridden in each library
 *
 */
public final class DataCache {
    private DataCache() {}                    
                        
    public static DateData getOrPutDateData(final long val) {
        return new DateData(val);
    }
    
    public static DateData getOrPutDateData(final String val) {
        return new DateData(val);
    }
    
    public static DateData getDateData(final String val) {
        return new DateData(val);
    }
    
    public static void putDateData(final String key, final DateData val) {
    }
    
    public static IntData getOrPutIntData(final int val) {
        return new IntData(val);
    }
    
    public static LongData getOrPutLongData(final long val) {
        return new LongData(val);
    }
    
    public static NumericData getOrPutNumericData(final long val) {
        return new NumericData(val);
    }
    
    public static StringData getOrPutStringData(final String val) {
        return new StringData(val);
    }
    
    public static TimestampData getOrPutTimestampData(final long val) {
        return new TimestampData(val);
    }
    
    public static String getOrPutTimeZone(final String val) {
        return val;
    }
}
