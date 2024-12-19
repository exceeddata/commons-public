package com.exceeddata.ac.common.extern;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public final class ExternDataConv {
    private ExternDataConv() {}
    
    public static Boolean toBoolean(final TypeData data) {
        try {
            return data.toBoolean();
        } catch (EngineException e) {
            return null;
        }
    }
    
    public static Boolean toBoolean(final TypeData data, final Boolean defaultValue) {
        try {
            final Boolean val = data.toBoolean();
            return val != null ? val : defaultValue;
        } catch (EngineException e) {
            return defaultValue;
        }
    }
    
    public static Integer toInt(final TypeData data) {
        try {
            return data.toInt();
        } catch (EngineException e) {
            return null;
        }
    }
    
    public static Integer toInt(final TypeData data, final Number defaultValue) {
        try {
            final Integer val = data.toInt();
            return val != null ? val.intValue() : defaultValue != null ? defaultValue.intValue() : null;
        } catch (EngineException e) {
            return defaultValue != null ? defaultValue.intValue() : null;
        }
    }
    
    public static Long toLong(final TypeData data) {
        try {
            return data.toLong();
        } catch (EngineException e) {
            return null;
        }
    }
    
    public static Long toLong(final TypeData data, final Number defaultValue) {
        try {
            final Long val = data.toLong();
            return val != null ? val.longValue() : defaultValue != null ? defaultValue.longValue() : null;
        } catch (EngineException e) {
            return defaultValue != null ? defaultValue.longValue() : null;
        }
    }
    
    public static Float toFloat(final TypeData data) {
        try {
            return data.toFloat();
        } catch (EngineException e) {
            return null;
        }
    }
    
    public static Float toFloat(final TypeData data, final Number defaultValue) {
        try {
            final Float val = data.toFloat();
            return val != null ? val.floatValue() : defaultValue != null ? defaultValue.floatValue() : null;
        } catch (EngineException e) {
            return defaultValue != null ? defaultValue.floatValue() : null;
        }
    }
    
    public static Double toDouble(final TypeData data) {
        try {
            return data.toDouble();
        } catch (EngineException e) {
            return null;
        }
    }
    
    public static Double toDouble(final TypeData data, final Number defaultValue) {
        try {
            final Double val = data.toDouble();
            return val != null ? val.doubleValue() : defaultValue != null ? defaultValue.doubleValue() : null;
        } catch (EngineException e) {
            return defaultValue != null ? defaultValue.doubleValue() : null;
        }
    }
    
    public static String toString(final TypeData data) {
        return data.toString();
    }
    
    public static String toString(final TypeData data, final String defaultValue) {
        final String val = data.toString();
        return val != null && val.trim().length() > 0 ? val : defaultValue;
    }
    
    public static byte[] toBytes(final TypeData data) {
        try {
            return data.toBytes();
        } catch (EngineException e) {
            return null;
        }
    }
    
    public static byte[] toBytes(final TypeData data, final byte[] defaultValue) {
        try {
            final byte[] val = data.toBytes();
            return val != null && val.length > 0 ? val : defaultValue;
        } catch (EngineException e) {
            return defaultValue;
        }
    }
    
    public static Date toDate(final TypeData data) {
        try {
            return data.toDate();
        } catch (EngineException e) {
            return null;
        }
    }
    
    public static Date toDate(final TypeData data, final Date defaultValue) {
        try {
            final Date val = data.toDate();
            return val != null ? val : defaultValue;
        } catch (EngineException e) {
            return defaultValue;
        }
    }
    
    public static java.sql.Timestamp toTimestamp(final TypeData data) {
        try {
            return data.toTimestamp();
        } catch (EngineException e) {
            return null;
        }
    }
    
    public static java.sql.Timestamp toTimestamp(final TypeData data, final java.sql.Timestamp defaultValue) {
        try {
            final java.sql.Timestamp val = data.toTimestamp();
            return val != null ? val : defaultValue;
        } catch (EngineException e) {
            return defaultValue;
        }
    }
    
    public static List<?> toList(final TypeData data) {
        try {
            return data.toList();
        } catch (EngineException e) {
            return null;
        }
    }
    
    public static List<?> toList(final TypeData data, final List<?> defaultValue) {
        try {
            final List<?> val = data.toList();
            return val != null && val.size() > 0 ? val : defaultValue;
        } catch (EngineException e) {
            return defaultValue;
        }
    }
    
    public static Map<?, ?> toMap(final TypeData data) {
        try {
            return data.toMap();
        } catch (EngineException e) {
            return null;
        }
    }
    
    public static Map<?, ?> toList(final TypeData data, final Map<?, ?> defaultValue) {
        try {
            final Map<?, ?> val = data.toMap();
            return val != null && val.size() > 0 ? val : defaultValue;
        } catch (EngineException e) {
            return defaultValue;
        }
    }
}
