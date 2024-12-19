package com.exceeddata.ac.common.util;

import java.io.IOException;
import java.io.ObjectInput;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.exceeddata.ac.common.data.collection.RecordCollection;
import com.exceeddata.ac.common.data.record.Record;
import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.BinaryData;
import com.exceeddata.ac.common.data.typedata.BooleanData;
import com.exceeddata.ac.common.data.typedata.CalendarTimeData;
import com.exceeddata.ac.common.data.typedata.CalendarTimestampData;
import com.exceeddata.ac.common.data.typedata.ComplexData;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DateData;
import com.exceeddata.ac.common.data.typedata.DecimalData;
import com.exceeddata.ac.common.data.typedata.DenseVectorData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.FloatData;
import com.exceeddata.ac.common.data.typedata.InstantData;
import com.exceeddata.ac.common.data.typedata.IntData;
import com.exceeddata.ac.common.data.typedata.ListData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.MapData;
import com.exceeddata.ac.common.data.typedata.NullData;
import com.exceeddata.ac.common.data.typedata.NumericData;
import com.exceeddata.ac.common.data.typedata.SetData;
import com.exceeddata.ac.common.data.typedata.SparseVectorData;
import com.exceeddata.ac.common.data.typedata.StringData;
import com.exceeddata.ac.common.data.typedata.TimeData;
import com.exceeddata.ac.common.data.typedata.TimestampData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.data.typedata.TypeUtils;
import com.exceeddata.ac.common.exception.EngineDataException;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.DataConversionException;
import com.exceeddata.ac.common.exception.data.DataFormatException;
import com.exceeddata.ac.common.exception.data.UnsupportedDataOperationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * A utility class for manipulating TypeData.
 *
 */
public final class XTypeDataUtils {
    private XTypeDataUtils() {}
    
    /** 
     * Get a type by type name
     * 
     * @param typeName the name of the type
     * @return int
     */
    public static byte getType(final String typeName) {
        switch(typeName) {
            case "int":
            case "integer":                 return Types.INT;
            case "long":                    return Types.LONG;
            case "float":                   return Types.FLOAT;
            case "double":                  return Types.DOUBLE;
            case "numeric":                 return Types.NUMERIC;
            case "decimal":
            case "bigdecimal":              return Types.DECIMAL;
            case "boolean":                 return Types.BOOLEAN;
            case "date":                    return Types.DATE;
            case "time":                    return Types.TIME;
            case "timestamp":               return Types.TIMESTAMP;
            case "calendar_time":
            case "timewithtimezone":        return Types.CALENDAR_TIME;
            case "calendar_timestamp":
            case "timestampwithtimezone":   return Types.CALENDAR_TIMESTAMP;
            case "instant":                 return Types.INSTANT;
            case "list":                    return Types.LIST;
            case "set":                     return Types.SET;
            case "map":                     return Types.MAP;
            case "densevector":             return Types.DENSEVECTOR;
            case "sparsevector":            return Types.SPARSEVECTOR;
            case "binary":                  return Types.BINARY;
            case "null":                    return Types.NULL;
            default:                        return Types.STRING;
        }
    }
    
    /** 
     * Get a type by type name
     * 
     * @param type the value of the type
     * @return String
     */
    public static String getTypeName(final byte type) {
        switch(type) {
            case Types.INT: return "int";
            case Types.LONG: return "long";
            case Types.FLOAT: return "float";
            case Types.DOUBLE: return "double";
            case Types.NUMERIC: return "numeric";
            case Types.DECIMAL: return "decimal";
            case Types.COMPLEX: return "complex";
            case Types.BOOLEAN: return "boolean";
            case Types.DATE: return "date";
            case Types.TIME: return "time";
            case Types.TIMESTAMP: return "timestamp";
            case Types.CALENDAR_TIME: return "timewithtimezone";
            case Types.CALENDAR_TIMESTAMP: return "timestapwithtimezone";
            case Types.INSTANT: return "instant";
            case Types.LIST: return "list";
            case Types.SET: return "set";
            case Types.MAP: return "map";
            case Types.DENSEVECTOR: return "densevector";
            case Types.SPARSEVECTOR: return "sparsevector";
            case Types.BINARY: return "binary";
            case Types.NULL: return "null";
            default: return "string";
        }
    }
    
    /**
     * Get an instance of TypeData by type.
     * 
     * @param type the data type
     * @return TypeData
     */
    public static TypeData getTypeDataInstance (final byte type) {
        switch (type) {
            case Types.INT: return new IntData();
            case Types.LONG: return new LongData();
            case Types.FLOAT: return new FloatData();
            case Types.DOUBLE: return new DoubleData();
            case Types.NUMERIC: return new NumericData();
            case Types.DECIMAL: return new DecimalData();
            case Types.COMPLEX: return new ComplexData();
            case Types.BOOLEAN: return new BooleanData();
            case Types.STRING: return new StringData();
            case Types.BINARY: return new BinaryData();
            case Types.DATE: return new DateData();
            case Types.TIME: return new TimeData();
            case Types.TIMESTAMP: return new TimestampData();
            case Types.CALENDAR_TIME: return new CalendarTimeData();
            case Types.CALENDAR_TIMESTAMP: return new CalendarTimestampData();
            case Types.INSTANT: return new InstantData();
            case Types.LIST: return new ListData();
            case Types.SET: return new SetData();
            case Types.MAP: return new MapData();
            case Types.DENSEVECTOR: return new DenseVectorData();
            case Types.SPARSEVECTOR: return new SparseVectorData();
            case Types.NULL: return NullData.INSTANCE;
            default: 
                return NullData.INSTANCE;
        }
    }
    
    /**
     * Get whether a type is whole number type
     * 
     * @param type the data type
     * @return TypeData
     */
    public static boolean isWholeNumberType (final byte type) {
        switch (type) {
            case Types.INT: 
            case Types.LONG: 
            case Types.BOOLEAN: 
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP: 
            case Types.CALENDAR_TIME: 
            case Types.CALENDAR_TIMESTAMP: return true;
            default:
                return false;
        }
    }
    
    /**
     * Get an instance of TypeData by type.
     * 
     * @param object the object
     * @param type the object type
     * @return TypeData
     */
    public static TypeData getObjectTypeData (final Object object) {
        if (object == null) {
            return NullData.INSTANCE;
        } else if (object instanceof Number) {
            if (object instanceof Integer) {
                return IntData.nonNullValueOf((Integer) object);
            } else if (object instanceof Long) {
                return LongData.nonNullValueOf((Long) object);
            } else if (object instanceof Double) {
                return DoubleData.nonNullValueOf((Double) object);
            } else if (object instanceof BigDecimal) {
                return DecimalData.nonNullValueOf((BigDecimal) object);
            } else if (object instanceof BigInteger) {
                return DecimalData.nonNullValueOf(new BigDecimal((BigInteger) object));
            } else if (object instanceof Float) {
                return FloatData.nonNullValueOf((Float) object);
            } else {
                return DoubleData.nonNullValueOf(((Number) object).doubleValue());
            }
        } else if (object instanceof Boolean) {
            return ((Boolean) object) ? BooleanData.TRUE : BooleanData.FALSE;
        } else if (object instanceof String) {
            final String s = (String) object;
            if (XComplexUtils.isComplexNumber(s)) {
                return new ComplexData(s);
            } else {
                return StringData.nonNullValueOf(s);
            }
        } else if (object instanceof java.sql.Date) {
            return DateData.valueOf((java.sql.Date) object);
        } else if (object instanceof java.sql.Time) {
            return TimeData.valueOf((java.sql.Time) object);
        } else if (object instanceof java.sql.Timestamp) {
            return TimestampData.valueOf((java.sql.Timestamp) object);
        } else if (object instanceof java.util.Date) {
            return DateData.valueOf((java.util.Date) object);
        } else if (object instanceof Calendar) {
            return CalendarTimestampData.valueOf((Calendar) object);
        } else if (object instanceof Instant) {
            return InstantData.valueOf((Instant) object);
        } else if (object instanceof byte[]) {
            return BinaryData.valueOf((byte[]) object);
        } else if (object instanceof Collection) {
            if (object instanceof Set) {
                return toSetData((Set<?>) object);
            } else {
                return toListData((List<?>) object);
            }
        } else if (object instanceof Map) {
            return toMapData((Map<?, ?>) object);
        } else if (object instanceof double[]) {
            return new DenseVectorData((double[]) object);
        } else if (object instanceof int[]) {
            final int[] objs = (int[]) object;
            final double[] dbls = new double[objs.length];
            for (int i = 0; i < objs.length; ++i) {
                dbls[i] = objs[i];
            }
            return new DenseVectorData(dbls);
        } else if (object instanceof long[]) {
            final long[] objs = (long[]) object;
            final double[] dbls = new double[objs.length];
            for (int i = 0; i < objs.length; ++i) {
                dbls[i] = objs[i];
            }
            return new DenseVectorData(dbls);
        } else if (object instanceof float[]) {
            final float[] objs = (float[]) object;
            final double[] dbls = new double[objs.length];
            for (int i = 0; i < objs.length; ++i) {
                dbls[i] = objs[i];
            }
            return new DenseVectorData(dbls);
        } else if (object instanceof short[]) {
            final short[] objs = (short[]) object;
            final double[] dbls = new double[objs.length];
            for (int i = 0; i < objs.length; ++i) {
                dbls[i] = objs[i];
            }
            return new DenseVectorData(dbls);
        } else if (object instanceof Object[]) {
            final Object[] objs = (Object[]) object;
            final ArrayList<TypeData> list = new ArrayList<>(objs.length);
            for (int i = 0; i < objs.length; ++i) {
                list.add(getObjectTypeData(objs[i]));
            }
            return new ListData(list);
        } else { 
            return StringData.nonNullValueOf(object.toString());
        }
    }
    
    /**
     * Convert an object to JSON.
     * 
     * @param obj the object
     * @return String
     * @throws DataFormatException throws EngineException if problems occur
     */
    public static String toJSON(final Object obj) throws DataFormatException {
        return XJsonUtils.JSON.toJson(toJsonObject(obj));
    }

    private static Object toJsonObject(final Object object) {
        if (object == null) {
            return object;
        } else if (object instanceof Instant) {
            return LocalDateTime.ofInstant((Instant) object, XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.INSTANT_FORMATTER);
        } else if (object instanceof java.sql.Date) {
            return LocalDateTime.ofInstant(((java.sql.Date) object).toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.DATE_FORMATTER);
        } else if (object instanceof java.sql.Time) {
            return LocalDateTime.ofInstant(((java.sql.Time) object).toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.TIME_FORMATTER);
        } else if (object instanceof java.sql.Timestamp) {
            return LocalDateTime.ofInstant(((java.sql.Timestamp) object).toInstant(), XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.INSTANT_FORMATTER);
        } else if (object instanceof Calendar) {
            final Calendar cal = (Calendar) object;
            if (cal.get(Calendar.YEAR) == 1970) {
                return ZonedDateTime.ofInstant(cal.toInstant(), cal.getTimeZone().toZoneId())
                                    .format(XTemporalUtils.CALENDAR_TIME_FORMATTER);
            } else {
                return ZonedDateTime.ofInstant(cal.toInstant(), cal.getTimeZone().toZoneId())
                        .format(XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER);
            }
        } else if (object instanceof Integer || object instanceof Long || object instanceof Float
                || object instanceof Double) {
            return object;
        } else if (object instanceof BigDecimal) {
            final BigDecimal bd = (BigDecimal) object;
            return bd.scale() <= 0 ? Long.valueOf(bd.toString()) : Double.valueOf(bd.toString());
        } else if (object instanceof List) {
            final ArrayList<Object> jsonArray = new ArrayList<>();
            final List<?> objs = (List<?>) object;
            for (final Object item : objs) {
                jsonArray.add(toJsonObject(item));
            }
            return jsonArray;
        } else if (object instanceof Set) {
            final ArrayList<Object> jsonArray = new ArrayList<>();
            final Set<?> objs = (Set<?>) object;
            for (final Object item : objs) {
                jsonArray.add(toJsonObject(item));
            }
            return jsonArray;
        } else if (object instanceof Map) {
            final LinkedHashMap<String, Object> jsonMap = new LinkedHashMap<>();
            final Map<?, ?> map = (Map<?, ?>) object;
            Map.Entry<?, ?> entry;
            for (final Object entryObject : map.entrySet()) {
                entry = (Map.Entry<?, ?>) entryObject;
                jsonMap.put(entry.getKey().toString(), toJsonObject(entry.getValue()));
            }
            return jsonMap;
        } else {
            return object.toString();
        }
    }
    
    /**
     * Convert JSON to TypeData
     * 
     * @param json the JSON string
     * @return TypeData
     */
    @SuppressWarnings("deprecation")
    public static TypeData jsonToTypeData(final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            return jsonElement == null ? NullData.INSTANCE : jsonElementToTypeData(jsonElement);
        } catch (Exception e) {
            return NullData.INSTANCE;
        }
    }
    
    /**
     * Convert JSON to Map of TypeData
     * 
     * @param json the JSON string
     * @return LinkedHashMap of values
     */
    @SuppressWarnings("deprecation")
    public static LinkedHashMap<TypeData, TypeData> jsonToTypeDataMap(final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonObject() == false) {
                return new LinkedHashMap<TypeData, TypeData>(0);
            }
        } catch(Exception e) {
            return new LinkedHashMap<TypeData, TypeData>(0);
        }
        
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final Iterator<Map.Entry<String, JsonElement>> iter = jsonObject.entrySet().iterator();
        if (!iter.hasNext()) {
            return new LinkedHashMap<TypeData, TypeData>(0);
        }
        
        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        Map.Entry<String, JsonElement> entry = iter.next();
        map.put(StringData.nonNullValueOf(entry.getKey()), jsonElementToTypeData(entry.getValue()));
        
        while(iter.hasNext()) {
            entry = iter.next();
            map.put(StringData.nonNullValueOf(entry.getKey()), jsonElementToTypeData(entry.getValue()));
        }
        return map;
    }
    
    /**
     * Convert JSON to Map of TypeData
     * 
     * @param json the JSON string
     * @return LinkedHashMap of values
     */
    @SuppressWarnings("deprecation")
    public static LinkedHashMap<String, TypeData> jsonToStringDataMap(final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonArray() == false) {
                return new LinkedHashMap<String, TypeData>(0);
            }
        } catch(Exception e) {
            return new LinkedHashMap<String, TypeData>(0);
        }
        
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final Iterator<Map.Entry<String, JsonElement>> iter = jsonObject.entrySet().iterator();
        if (!iter.hasNext()) {
            return new LinkedHashMap<String, TypeData>(0);
        }
        
        final LinkedHashMap<String, TypeData> map = new LinkedHashMap<>();
        Map.Entry<String, JsonElement> entry = iter.next();
        map.put(entry.getKey(), jsonElementToTypeData(entry.getValue()));
        
        while(iter.hasNext()) {
            entry = iter.next();
            map.put(entry.getKey(), jsonElementToTypeData(entry.getValue()));
        }
        return map;
    }
    
    /**
     * Convert JSON to List of TypeData
     * 
     * @param json the JSON string
     * @return List of values
     */
    @SuppressWarnings("deprecation")
    public static ArrayList<TypeData> jsonToTypeDataList(final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonArray() == false) {
                return new ArrayList<TypeData>(0);
            }
        } catch(Exception e) {
            return new ArrayList<TypeData>(0);
        }
        
        final JsonArray jsonObject = jsonElement.getAsJsonArray();
        final Iterator<JsonElement> iter = jsonObject.iterator();
        if (!iter.hasNext()) {
            return new ArrayList<TypeData>(0);
        }

        final ArrayList<TypeData> list = new ArrayList<>();
        JsonElement item = iter.next();
        list.add(jsonElementToTypeData(item));
        
        while(iter.hasNext()) {
            list.add(jsonElementToTypeData(iter.next()));
        }
        return list;
    }
    
    /**
     * Convert JSON to List of TypeData
     * 
     * @param json the JSON string
     * @return List of values
     */
    @SuppressWarnings("deprecation")
    public static LinkedHashSet<TypeData> jsonToTypeDataSet(final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonArray() == false) {
                return new LinkedHashSet<TypeData>(0);
            }
        } catch(Exception e) {
            return new LinkedHashSet<TypeData>(0);
        }
        
        final JsonArray jsonObject = jsonElement.getAsJsonArray();
        final Iterator<JsonElement> iter = jsonObject.iterator();
        if (!iter.hasNext()) {
            return new LinkedHashSet<TypeData>(0);
        }

        final LinkedHashSet<TypeData> set = new LinkedHashSet<>();
        JsonElement item = iter.next();
        set.add(jsonElementToTypeData(item));
        
        while(iter.hasNext()) {
            set.add(jsonElementToTypeData(iter.next()));
        }
        return set;
    }
    
    /**
     * Convert JSON to Record
     * 
     * @param json the JSON string
     * @return Record
     */
    @SuppressWarnings("deprecation")
    public static Record jsonToRecord(final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonObject() == false) {
                return null;
            }
        } catch(Exception e) {
            return null;
        }
        
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final Set<Map.Entry<String, JsonElement>> jsonSet = jsonObject.entrySet();
        final int jsonSize = jsonSet.size();
        if (jsonSize == 0) {
            return new Record();
        }
        
        final Record record = new Record(jsonSize);
        final Iterator<Map.Entry<String, JsonElement>> iter = jsonSet.iterator();
        Map.Entry<String, JsonElement> entry;
        while(iter.hasNext()) {
            entry = iter.next();
            record.add(entry.getKey(), jsonElementToTypeData(entry.getValue()));
        }
        return record;
    }
    
    /**
     * Convert JSON to Record
     * 
     * @param jsonObject the JSON object
     * @return Record
     */
    public static Record jsonToRecord(final JsonObject jsonObject) {
        final Set<Map.Entry<String, JsonElement>> jsonSet = jsonObject.entrySet();
        final int jsonSize = jsonSet.size();
        if (jsonSize == 0) {
            return new Record();
        }
        
        final Record record = new Record(jsonSize);
        final Iterator<Map.Entry<String, JsonElement>> iter = jsonSet.iterator();
        Map.Entry<String, JsonElement> entry;
        while(iter.hasNext()) {
            entry = iter.next();
            record.add(entry.getKey(), jsonElementToTypeData(entry.getValue()));
        }
        return record;
    }
    
    /**
     * Convert JSON to TypeData
     * 
     * @param jsonElement the JSON element
     * @return TypeData
     */
    public static TypeData jsonElementToTypeData(final JsonElement jsonElement) {
        if (jsonElement instanceof JsonPrimitive) {
            final JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
            if (jsonPrimitive.isString()) {
                return StringData.nonNullValueOf(jsonPrimitive.getAsString());
            } else if (jsonPrimitive.isNumber()) {
                final Number n = jsonPrimitive.getAsNumber();
                if (n instanceof Integer | n instanceof Short | n instanceof Byte) {
                    return IntData.nonNullValueOf(n.intValue());
                } else if (n instanceof Long) {
                    return LongData.nonNullValueOf(n.longValue());
                } else if (n instanceof Double) {
                    return DoubleData.nonNullValueOf(n.doubleValue());
                } else if (n instanceof Float) {
                    return FloatData.nonNullValueOf(n.floatValue());
                } else {
                    return DecimalData.nonNullValueOf(n.toString());
                }
            } else if (jsonPrimitive.isBoolean()) {
                return jsonPrimitive.getAsBoolean() ? BooleanData.TRUE : BooleanData.FALSE;
            } else {
                return StringData.nonNullValueOf(jsonPrimitive.toString());
            }
        } else if (jsonElement instanceof JsonObject) {
            final JsonObject jsonObject = (JsonObject) jsonElement;
            final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                map.put(StringData.nonNullValueOf(entry.getKey()), jsonElementToTypeData(entry.getValue()));
            }
            return new MapData(map);
        } else if (jsonElement instanceof JsonArray) {
            final JsonArray jsonObject = (JsonArray) jsonElement;
            final ArrayList<TypeData> list = new ArrayList<>();
            for (final JsonElement item : jsonObject) {
                list.add(jsonElementToTypeData(item));
            }
            return new ListData(list);
        } else if (jsonElement instanceof JsonNull) {
            return NullData.INSTANCE;
        } else {
            return StringData.nonNullValueOf(jsonElement.toString());
        }
    }
    
    /**
     * Convert JSON element to double
     * 
     * @param jsonElement the JSON element
     * @return double
     */
    public static double jsonElementToDouble(final JsonElement jsonElement) {
        if (jsonElement instanceof JsonPrimitive) {
            final JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
            if (jsonPrimitive.isString()) {
                try {
                    return Double.valueOf(jsonPrimitive.getAsString()).doubleValue();
                } catch (NumberFormatException e) {
                    return 0d;
                }
            } else if (jsonPrimitive.isNumber()) {
                return jsonPrimitive.getAsNumber().doubleValue();
            } else if (jsonPrimitive.isBoolean()) {
                return jsonPrimitive.getAsBoolean() ? 1d : 0d;
            } else {
                try {
                    return Double.valueOf(jsonPrimitive.toString()).doubleValue();
                } catch (NumberFormatException e) {
                    return 0d;
                }
            }
        } else {
            return 0d;
        }
    }
    
    /**
     * Convert JSON to TypeData
     * 
     * @param jsonElement the JSON element
     * @return Object
     */
    public static Object jsonElementToJavaObject(final JsonElement jsonElement) {
        if (jsonElement instanceof JsonPrimitive) {
            final JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
            if (jsonPrimitive.isString()) {
                return jsonPrimitive.getAsString();
            } else if (jsonPrimitive.isNumber()) {
                final Number n = jsonPrimitive.getAsNumber();
                if (n instanceof Integer | n instanceof Short | n instanceof Byte) {
                    return n.intValue();
                } else if (n instanceof Long) {
                    return n.longValue();
                } else if (n instanceof Double) {
                    return n.doubleValue();
                } else if (n instanceof Float) {
                    return n.floatValue();
                } else {
                    return new BigDecimal(n.toString());
                }
            } else if (jsonPrimitive.isBoolean()) {
                return jsonPrimitive.getAsBoolean();
            } else {
                return jsonPrimitive.toString();
            }
        } else if (jsonElement instanceof JsonObject) {
            final JsonObject jsonObject = (JsonObject) jsonElement;
            final LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                map.put(entry.getKey(), jsonElementToJavaObject(entry.getValue()));
            }
            return map;
        } else if (jsonElement instanceof JsonArray) {
            final JsonArray jsonObject = (JsonArray) jsonElement;
            final ArrayList<Object> list = new ArrayList<>();
            for (final JsonElement item : jsonObject) {
                list.add(jsonElementToJavaObject(item));
            }
            return list;
        } else if (jsonElement instanceof JsonNull) {
            return null;
        } else {
            return jsonElement.toString();
        }
    }
    
    /**
     * Convert JSON to List
     * 
     * @param json the JSON string
     * @return List
     */
    @SuppressWarnings("deprecation")
    public static List<Object> jsonToListObject (final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonArray() == false) {
                return null;
            }
        } catch(Exception e) {
            return null;
        }
       
        final JsonArray jsonObject = jsonElement.getAsJsonArray();
        final Iterator<JsonElement> iter = jsonObject.iterator();
        if (!iter.hasNext()) {
            return null;
        }

        final ArrayList<Object> list = new ArrayList<>();
        JsonElement item = iter.next();
        list.add(jsonElementToJavaObject(item));
       
        while(iter.hasNext()) {
            list.add(jsonElementToJavaObject(iter.next()));
        }
        return list;
    }
    
    /**
     * Convert JSON to ListData
     * 
     * @param json the JSON string
     * @return List
     */
    @SuppressWarnings("deprecation")
    public static ListData jsonToListData (final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonArray() == false) {
                return ListData.NULL;
            }
        } catch(Exception e) {
            return ListData.NULL;
        }
       
        final JsonArray jsonObject = jsonElement.getAsJsonArray();
        final Iterator<JsonElement> iter = jsonObject.iterator();
        if (!iter.hasNext()) {
            return ListData.NULL;
        }

        final ArrayList<TypeData> list = new ArrayList<>();
        JsonElement item = iter.next();
        list.add(jsonElementToTypeData(item));
       
        while(iter.hasNext()) {
            list.add(jsonElementToTypeData(iter.next()));
        }
        return new ListData(list);
    }
   
    /**
     * Convert JSON to Set
     * 
     * @param json the JSON string
     * @return List
     */
    @SuppressWarnings("deprecation")
    public static Set<Object> jsonToSetObject (final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonArray() == false) {
                return null;
            }
        } catch(Exception e) {
            return null;
        }
       
        final JsonArray jsonObject = jsonElement.getAsJsonArray();
        final Iterator<JsonElement> iter = jsonObject.iterator();
        if (!iter.hasNext()) {
            return null;
        }

        final LinkedHashSet<Object> set = new LinkedHashSet<>();
        JsonElement item = iter.next();
        set.add(jsonElementToJavaObject(item));
       
        while(iter.hasNext()) {
            set.add(jsonElementToJavaObject(iter.next()));
        }
        return set;
    }
   
    /**
     * Convert JSON to SetData
     * 
     * @param json the JSON string
     * @return List
     */
    @SuppressWarnings("deprecation")
    public static SetData jsonToSetData (final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonArray() == false) {
                return SetData.NULL;
            }
        } catch(Exception e) {
            return SetData.NULL;
        }
       
        final JsonArray jsonObject = jsonElement.getAsJsonArray();
        final Iterator<JsonElement> iter = jsonObject.iterator();
        if (!iter.hasNext()) {
            return SetData.NULL;
        }

        final LinkedHashSet<TypeData> set = new LinkedHashSet<>();
        JsonElement item = iter.next();
        set.add(jsonElementToTypeData(item));
        
        while(iter.hasNext()) {
            set.add(jsonElementToTypeData(iter.next()));
        }
        return new SetData(set);
    }
    
    /**
     * Convert JSON to Map
     * 
     * @param json the JSON string
     * @return Map
     */
    @SuppressWarnings("deprecation")
    public static Map<String, Object> jsonToMapObject (final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonObject() == false) {
                return null;
            }
        } catch(Exception e) {
            return null;
        }
        
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final Iterator<Map.Entry<String, JsonElement>> iter = jsonObject.entrySet().iterator();
        if (!iter.hasNext()) {
            return null;
        }

        final LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Map.Entry<String, JsonElement> entry = iter.next();
        map.put(entry.getKey(), jsonElementToJavaObject(entry.getValue()));
        
        while(iter.hasNext()) {
            entry = iter.next();
            map.put(entry.getKey(), jsonElementToJavaObject(entry.getValue()));
        }
        return map;
    }
    
    /**
     * Convert JSON to MapData
     * 
     * @param json the JSON string
     * @return MapData
     */
    @SuppressWarnings("deprecation")
    public static MapData jsonToMapData (final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonObject() == false) {
                return MapData.NULL;
            }
        } catch(Exception e) {
            return MapData.NULL;
        }
        
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final Iterator<Map.Entry<String, JsonElement>> iter = jsonObject.entrySet().iterator();
        if (!iter.hasNext()) {
            return MapData.NULL;
        }

        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        Map.Entry<String, JsonElement> entry = iter.next();
        map.put(StringData.nonNullValueOf(entry.getKey()), jsonElementToTypeData(entry.getValue()));
        
        while(iter.hasNext()) {
            entry = iter.next();
            map.put(StringData.nonNullValueOf(entry.getKey()), jsonElementToTypeData(entry.getValue()));
        }
        return new MapData(map);
    }
    
    /**
     * Convert JSON to Map of TypeData
     * 
     * @param json the JSON string
     * @return LinkedHashMap
     */
    @SuppressWarnings("deprecation")
    public static LinkedHashMap<TypeData, TypeData> jsonToMapOfTypeData (final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonObject() == false) {
                return new LinkedHashMap<>();
            }
        } catch(Exception e) {
            return new LinkedHashMap<>();
        }
        
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final Iterator<Map.Entry<String, JsonElement>> iter = jsonObject.entrySet().iterator();
        if (!iter.hasNext()) {
            return new LinkedHashMap<>();
        }

        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        Map.Entry<String, JsonElement> entry = iter.next();
        map.put(StringData.nonNullValueOf(entry.getKey()), jsonElementToTypeData(entry.getValue()));
        
        while(iter.hasNext()) {
            entry = iter.next();
            map.put(StringData.nonNullValueOf(entry.getKey()), jsonElementToTypeData(entry.getValue()));
        }
        return map;
    }
      
    /**
     * Convert JSON to DenseVectorData
     * 
     * @param json the JSON string
     * @return DenseVectorData
     */
    @SuppressWarnings("deprecation")
    public static DenseVectorData jsonToDenseVectorData (final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonArray() == false) {
                return DenseVectorData.NULL;
            }
        } catch(Exception e) {
            return DenseVectorData.NULL;
        }
        
        final JsonArray jsonObject = jsonElement.getAsJsonArray();
        final Iterator<JsonElement> iter = jsonObject.iterator();
        if (!iter.hasNext()) {
            return DenseVectorData.NULL;
        }

        final double[] list = new double[jsonObject.size()];
        JsonElement item = iter.next();
        int index = 0;
        list[index++] =jsonElementToDouble(item);
        
        while(iter.hasNext()) {
            list[index++] = jsonElementToDouble(iter.next());
        }
        return new DenseVectorData(list);
    }
    
    /**
     * Convert JSON to SparseVectorData
     * 
     * @param json the JSON string
     * @return SparseVectorData
     */
    @SuppressWarnings("deprecation")
    public static SparseVectorData jsonToSparseVectorData (final String json) {
        final JsonElement jsonElement;
        try {
            jsonElement = XJsonUtils.PARSER.parse(json);
            if (jsonElement == null || jsonElement.isJsonArray() == false) {
                return SparseVectorData.NULL;
            }
        } catch(Exception e) {
            return SparseVectorData.NULL;
        }
      
        final JsonArray jsonObject = jsonElement.getAsJsonArray();
        final Iterator<JsonElement> iter = jsonObject.iterator();
        if (!iter.hasNext()) {
            return SparseVectorData.NULL;
        }

        final int vsize = jsonObject.size();
        final int[] indices = new int[vsize];
        final double[] nonzeros = new double[vsize];
        JsonElement item = iter.next();
        int index = 0, nzlength = 0;
        double d;
        if (Double.compare(d = jsonElementToDouble(item), 0d) != 0)  {
            indices[nzlength] = index;
            nonzeros[nzlength++] = d;
        }
        
        while(iter.hasNext()) {
            ++index;
            item = iter.next();
            if (Double.compare(d = jsonElementToDouble(item), 0d) != 0)  {
                indices[nzlength] = index;
                nonzeros[nzlength++] = d;
            }
        }
        if (vsize != nzlength) {
            final double[] nz = new double[nzlength];
            final int[] id = new int[nzlength];
            System.arraycopy(nonzeros, 0, nz, 0, nzlength);
            System.arraycopy(indices, 0, id, 0, nzlength);
            return new SparseVectorData(vsize, id, nz);
        } else {
            return new SparseVectorData(vsize, indices, nonzeros);
        }
    }
    
    public static String formatDate (java.sql.Date value) {
        return LocalDateTime.ofInstant(value.toInstant(), XTemporalUtils.SYSTEM_ZONEID)
                            .format(XTemporalUtils.DATE_FORMATTER);
    }
    
    public static String formatTime (java.sql.Time value) {
        return LocalDateTime.ofInstant(value.toInstant(), XTemporalUtils.SYSTEM_ZONEID)
                            .format(XTemporalUtils.TIME_FORMATTER);
    }
    
    public static String formatTimestamp (java.sql.Timestamp value) {
        return LocalDateTime.ofInstant(value.toInstant(), XTemporalUtils.SYSTEM_ZONEID)
                            .format(XTemporalUtils.INSTANT_FORMATTER);
    }
    
    public static String formatCalendarTime (GregorianCalendar value) {
        return value.toZonedDateTime().format(XTemporalUtils.CALENDAR_TIME_FORMATTER);
    }
    
    public static String formatCalendarTimestamp (GregorianCalendar value) {
        return value.toZonedDateTime().format(XTemporalUtils.CALENDAR_TIMESTAMP_FORMATTER);
    }
    
    public static String formatInstant (Instant value) {
        return LocalDateTime.ofInstant(value, XTemporalUtils.SYSTEM_ZONEID).format(XTemporalUtils.INSTANT_FORMATTER);
    }
    
    /**
     * Convert a TypeData to String.
     * 
     * @param typeData the data
     * @param defaultValue the default value if string is null
     * @return String
     */
    public static String typeDataToString(final TypeData typeData, final String defaultValue) {
        return typeData.isNull() ? defaultValue : typeData.toString();
    }
    
    /**
     * Convert a TypeData to JSON String.
     * 
     * @param typeData the data
     * @return String
     * @throws EngineDataException if exception occurs
     */
    public static String typeDataToJSON(final TypeData typeData) throws EngineDataException {
        if (typeData.isNull()) {
            return null;
        }
        
        switch (typeData.getType()) {
            case Types.MAP:
            case Types.LIST: 
            case Types.SET: 
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: return typeData.toString();
            case Types.NULL: return null;
            default: throw new DataConversionException ("DATA_UNCONVERTIBLE_TO_JSON"); 
        }
    }
    
    /**
     * Append a TypeData value to JSON String Builder.
     * appendJSONValue
     * @param sb the string builder
     * @param valueData the data value
     * @return String
     */
    public static void appendJSONValue(
            final StringBuilder sb,
            final TypeData valueData) {
        if (valueData.isNull()) {
            sb.append("null");
            return;
        }
        
        switch (valueData.getType()) {
            case Types.BOOLEAN: 
                sb.append(((BooleanData) valueData).getBoolean() ? "true" : "false");
                return;
            case Types.INT:
            case Types.LONG:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.COMPLEX:
                sb.append(valueData.toString());
                return;
            case Types.MAP: {
                final MapData map = (MapData) valueData;
                sb.append('{');
                for (final Map.Entry<TypeData, TypeData> entry : map.entrySet()) {
                    if (entry.getKey().isEmpty()) {
                        sb.append("\"null\":");
                    } else {
                        XStringUtils.escapeJSON(sb, entry.getKey().toString());
                        sb.append(":");
                    }
                    XTypeDataUtils.appendJSONValue(sb, entry.getValue());
                    sb.append(',');
                }
                
                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.setLength(sb.length() - 1);
                }
                sb.append('}');
                return;
            }
            case Types.LIST: {
                final ListData list = (ListData) valueData;
                sb.append('[');
                
                final Iterator<TypeData> iterator = list.iterator();
                TypeData item;
                while (iterator.hasNext()) {
                    item = iterator.next();
                    if (item.isEmpty()) {
                        if (item.isNull() || item.getType() != Types.STRING) {
                            sb.append("null,");
                        } else {
                            sb.append("\"\",");
                        }
                    } else {
                        XTypeDataUtils.appendJSONValue(sb, item);
                        sb.append(',');
                    }
                }
                
                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.setLength(sb.length() - 1);
                }
                sb.append(']');
                return;
            }
            case Types.SET: {
                final SetData list = (SetData) valueData;
                sb.append('[');
                
                final Iterator<TypeData> iterator = list.iterator();
                TypeData item;
                while (iterator.hasNext()) {
                    item = iterator.next();
                    if (item.isEmpty()) {
                        if (item.isNull() || item.getType() != Types.STRING) {
                            sb.append("null,");
                        } else {
                            sb.append("\"\",");
                        }
                    } else {
                        XTypeDataUtils.appendJSONValue(sb, item);
                        sb.append(',');
                    }
                }
                
                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.setLength(sb.length() - 1);
                }
                sb.append(']');
                return;
            }
            case Types.DENSEVECTOR: {
                final DenseVectorData vector = (DenseVectorData) valueData;
                sb.append('[');
                
                for (final double item : vector.getUnsafeDoubles()) {
                    sb.append(item != (int) item ? String.valueOf(item) : String.valueOf((int) item)).append(',');
                }
                
                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.setLength(sb.length() - 1);
                }
                sb.append(']');
                return;
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData vector = (SparseVectorData) valueData;
                sb.append('[');

                final int size = vector.size();
                final int nzlength = vector.getNonzeroLength();
                if (nzlength == 0) {
                    for (int i = 0; i < size; ++i) {
                        sb.append("0,");
                    }
                } else {
                    final double[] nonzeros = vector.getUnsafeNonzeros();
                    final int[] indices = vector.getUnsafeIndices();
                    int index = 0, nzindex = 0, nz = indices[0];
                    
                    t: while (true) {
                        if (index++ >= nz) {
                            sb.append(String.valueOf(nonzeros[nzindex])).append(',');
                            if (nzindex + 1 < nzlength) {
                                nz = indices[++nzindex];
                            } else {
                                break t;
                            }
                        } else {
                            sb.append("0,");
                        }
                    }
                    for (int i = index; i < size; ++i) {
                        sb.append("0,");
                    }
                }

                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.setLength(sb.length() - 1);
                }
                sb.append(']');
                return;
            }
            default:
                sb.append('"').append(valueData.toString()).append('"');
        }
    }
    
    /**
     * Convert a TypeData to String.
     * 
     * @param typeData the data
     * @return String
     * @throws EngineDataException if exception occurs
     */
    public static String typeDataToOtherString(final TypeData typeData) throws EngineDataException {
        if (typeData.isNull()) {
            return null;
        }
        
        switch (typeData.getType()) {
            case Types.STRING:
            case Types.BINARY: return typeData.toString();
            case Types.NULL: return null;
            default: throw new DataConversionException ("DATA_UNCONVERTIBLE_TO_COMPLEX_STRING"); 
        }
    }
    
    /**
     * Convert a TypeData to byte[].
     * 
     * @param typeData the data
     * @return byte[]
     */
    public static byte[] typeDataToBytes(final TypeData typeData) {
        return typeData.isNull() ?  new byte[]{} : typeData.toString().getBytes();
    }
  
    /**
     * Convert a TypeData to Integer.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return Integer
     */
    public static Integer typeDataToInteger (final TypeData typeData, final Integer defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toInt();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Long.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return Long
     */
    public static Long typeDataToLong (final TypeData typeData, final Long defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toLong();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Float.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return Float
     */
    public static Float typeDataToFloat (final TypeData typeData, final Float defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toFloat();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Double.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return Double
     */
    public static Double typeDataToDouble (final TypeData typeData, final Double defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toDouble();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Boolean.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return Boolean
     */
    public static Boolean typeDataToBoolean (final TypeData typeData, final Boolean defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toBoolean();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to BigDecimal.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return BigDecimal
     */
    public static BigDecimal typeDataToDecimal (final TypeData typeData, final BigDecimal defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toDecimal();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Date.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return Date
     */
    public static java.sql.Date typeDataToDate (final TypeData typeData, final java.sql.Date defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toDate();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Time.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return Time
     */
    public static java.sql.Time typeDataToTime (final TypeData typeData, final java.sql.Time defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toTime();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Timestamp.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return Timestamp
     */
    public static java.sql.Timestamp typeDataToTimestamp (final TypeData typeData, final java.sql.Timestamp defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toTimestamp();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Instant.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return Instant
     */
    public static Instant typeDataToInstant (final TypeData typeData, final Instant defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toInstant();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Time with Time Zone.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return GregorianCalendar
     */
    public static GregorianCalendar typeDataToTimeWithTimeZone (final TypeData typeData, final GregorianCalendar defaultValue) {
        try {
            return typeData.isNull() ?  defaultValue : typeData.toTimeWithTimeZone();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Timestamp with Time Zone.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return GregorianCalendar
     */
    public static GregorianCalendar typeDataToTimestampWithTimeZone (final TypeData typeData, final GregorianCalendar defaultValue) {
        try {
            return typeData.isNull() ? defaultValue : typeData.toTimestampWithTimeZone();
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to InetAddress.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return InetAddress
     */
    public static InetAddress typeDataToInetAddress(final TypeData typeData, final InetAddress defaultValue){
        try {
            return typeData.isNull() ? defaultValue : InetAddress.getByName(typeDataToOtherString(typeData));
        } catch (java.net.UnknownHostException | EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to UUID.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return UUID
     */
    public static UUID typeDataToUUID(final TypeData typeData, final UUID defaultValue) {
        try {
            return typeData.isNull() ? defaultValue : UUID.fromString(typeDataToOtherString(typeData));
        } catch (IllegalArgumentException | EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to List.
     * 
     * @param typeData the data
     * @param defaultValue the default data if data is null or not convertible
     * @return List
     */
    public static List<Object> typeDataToList(final TypeData typeData, final List<Object> defaultValue) {
        try {
            return typeData.isNull() ? defaultValue : new ArrayList<Object>(typeData.toList());
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Set.
     * 
     * @param typeData the data
     * @param defaultValue the default value if data is null or not convertible
     * @return Set
     */
    public static Set<Object> typeDataToSet(final TypeData typeData, final Set<Object> defaultValue) {
        try {
            return typeData.isNull() ? defaultValue : new HashSet<Object>(typeData.toSet());
        } catch (EngineDataException e) {
            return defaultValue;
        }
    }
    
    /**
     * Convert a TypeData to Map.
     * 
     * @param typeData the data
     * @param defaultValue the default value if null or not convertible
     * @return Map
     */
    public static Map<Object, Object> typeDataToMap(final TypeData typeData, final Map<Object, Object> defaultValue) {
        if (typeData.isNull()) {
            return defaultValue;
        }
        try {
            return new HashMap<Object, Object>(typeData.toMap());
        } catch (EngineException e) {
            return defaultValue;
        }
    }
    
    public static TypeData append(final TypeData d1, final TypeData d2) throws EngineException {
        switch(d1.getType()) {
            case Types.LIST:
                if (d2.getType() == Types.MAP) {
                    throw new UnsupportedDataOperationException ("DATA_APPEND: LIST APPEND MAP - " + d2.toString());
                } else {
                    final ArrayList<TypeData> result = new ArrayList<>();
                    for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                        result.add(iter1.next());
                    }
                    switch(d2.getType()) {
                        case Types.LIST:
                        case Types.SET:
                        case Types.DENSEVECTOR:
                        case Types.SPARSEVECTOR:
                            if (!d2.isNull()) {
                                for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                    result.add(iter2.next());
                                }
                            }
                            return new ListData(result);
                        default:
                            result.add(d2);
                            return new ListData(result);
                    }
                }
            case Types.SET:
                if (d2.getType() == Types.MAP) {
                    throw new UnsupportedDataOperationException ("DATA_APPEND: SET APPEND MAP - " + d2.toString());
                } else {
                    final LinkedHashSet<TypeData> result = new LinkedHashSet<>();
                    for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                        result.add(iter1.next());
                    }
                    switch(d2.getType()) {
                        case Types.LIST:
                        case Types.SET:
                        case Types.DENSEVECTOR:
                            if (!d2.isNull()) {
                                for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                    result.add(iter2.next());
                                }
                            }
                            return new SetData(result);
                        case Types.SPARSEVECTOR:
                            if (!d2.isNull()) {
                                final SparseVectorData vdata = (SparseVectorData) d2;
                                if (vdata.haszeros()) {
                                    result.add(DoubleData.ZERO);
                                }
                                if (vdata.getNonzeroLength() > 0) {
                                    for (final DoubleData data : vdata.getNonzeroDatas()) {
                                        result.add(data);
                                    }
                                }
                            }
                            return new SetData(result);
                        default:
                            result.add(d2);
                            return new SetData(result);
                    }
                }
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR:
                if (d2.getType() == Types.MAP) {
                    throw new UnsupportedDataOperationException ("DATA_APPEND: VECTOR APPEND MAP - " + d2.toString());
                } else {
                    double[] result = DataConv.toDenseVectorData(d1).toArray();
                    switch(d2.getType()) {
                        case Types.LIST:
                        case Types.SET:
                            if (!d2.isNull()) {
                                final double[] temp = new double[result.length + d2.size()];
                                System.arraycopy(result, 0, temp, 0, result.length);
                                
                                int index = result.length;
                                Double d;
                                for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                    d = iter2.next().toDouble();
                                    temp[index++] = d != null ? d.doubleValue() : 0d;
                                }
                                result = temp;
                            }
                            return new DenseVectorData(result);
                        case Types.DENSEVECTOR:
                        case Types.SPARSEVECTOR:
                            if (!d2.isNull()) {
                                final double[] dbl2 = DataConv.toDenseVectorData(d2).toArray();
                                final double[] temp = new double[result.length + dbl2.length];
                                System.arraycopy(result, 0, temp, 0, result.length);
                                System.arraycopy(dbl2, 0, temp, result.length, dbl2.length);
                                result = temp;
                            }
                            return new DenseVectorData(result);
                        default:
                            final double[] temp = new double[result.length + 1];
                            System.arraycopy(result, 0, temp, 0, result.length);
                            Double d = d2.toDouble();
                            temp[result.length] = d != null ? d.doubleValue() : 0d;
                            result = temp;
                            return new DenseVectorData(result);
                    }
                }
            case Types.MAP:
                if (d2.getType() == Types.MAP) {
                    final LinkedHashMap<TypeData, TypeData> result = new LinkedHashMap<>(((MapData) d1).maps());
                    if (!d2.isNull()) {
                        for (final Map.Entry<TypeData, TypeData> entry : ((MapData) d2).entrySet()) {
                            result.put(entry.getKey(), entry.getValue());
                        }
                    }
                    return new MapData(result);
                } else {
                    throw new UnsupportedDataOperationException ("DATA_APPEND: MAP APPEND NONMAP - " + d2.toString());
                }
            default:
                if (d2.getType() == Types.MAP) {
                    throw new UnsupportedDataOperationException ("DATA_APPEND: PRIMITIVE APPEND MAP - " + d2.toString());
                } else {
                    final ArrayList<TypeData> result = new ArrayList<>();
                    result.add(d1);
                    switch(d2.getType()) {
                        case Types.LIST:
                        case Types.SET:
                        case Types.DENSEVECTOR:
                        case Types.SPARSEVECTOR:
                            if (!d2.isNull()) {
                                for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                    result.add(iter2.next());
                                }
                            }
                            return new ListData(result);
                        default:
                            result.add(d2);
                            return new ListData(result);
                    }
                }
        }
    }
    
    public static TypeData deduct(final TypeData d1, final TypeData d2) throws EngineException {
        switch(d1.getType()) {
            case Types.LIST:
                if (d2.getType() == Types.MAP) {
                    throw new UnsupportedDataOperationException ("DATA_DEDUCT: LIST DEDUCT MAP - " + d2.toString());
                } else {
                    if (d1.isEmpty()) {
                        return ListData.NULL;
                    }
                    switch(d2.getType()) {
                        case Types.LIST:
                        case Types.SET:
                        case Types.DENSEVECTOR: {
                            //compare in loop because different types would have different hash, so set can't work properly
                            if (d2.size() == 0) {
                                return d1;
                            }
                            
                            final Iterator<? extends TypeData> iter1 = d1.iterator();
                            final ArrayList<TypeData> result = new ArrayList<>();
                            Iterator<? extends TypeData> iter2;
                            TypeData val1, val2;
                            
                            t: while(iter1.hasNext()) {
                                val1 = iter1.next();
                                iter2 = d2.iterator();
                                while (iter2.hasNext()) {
                                    val2 = iter2.next();
                                    if (val1.compareTo(val2) == 0) {
                                        continue t;
                                    }
                                }
                                result.add(val1);
                            }
                            return new ListData(result);
                        }
                        case Types.SPARSEVECTOR: {
                                //compare in loop because different types would have different hash, so set can't work properly
                                final List<DoubleData> sd2 = ((SparseVectorData) d2).getNonzeroDatas();
                                final boolean hasZeros = d2.size() != sd2.size();
                                final Iterator<? extends TypeData> iter1 = d1.iterator();
                                final ArrayList<TypeData> result = new ArrayList<>();
                                TypeData val1;
                                
                                t: while(iter1.hasNext()) {
                                    val1 = iter1.next();
                                    for (final TypeData val2 : sd2) {
                                        if (hasZeros && val1.compareTo(DoubleData.ZERO) == 0) {
                                            continue t;
                                        }
                                        if (val1.compareTo(val2) == 0) {
                                            continue t;
                                        }
                                    }
                                    result.add(DataConv.toDoubleData(val1));
                                }
                                return new ListData(result);
                            }
                        default: {
                            final Iterator<? extends TypeData> iter1 = d1.iterator();
                            final ArrayList<TypeData> result = new ArrayList<>();
                            TypeData val1;
                            while(iter1.hasNext()) {
                                val1 = iter1.next();
                                if (val1.compareTo(d2) != 0) {
                                    result.add(DataConv.toDoubleData(val1));
                                }
                            }
                            return new ListData(result);
                        }
                    }
                }
            case Types.SET:
                if (d2.getType() == Types.MAP) {
                    throw new UnsupportedDataOperationException ("DATA_DEDUCT: SET DEDUCT MAP - " + d2.toString());
                } else {
                    if (d1.isEmpty()) {
                        return SetData.NULL;
                    }
                    switch(d2.getType()) {
                        case Types.LIST:
                        case Types.SET:
                        case Types.DENSEVECTOR: {
                                //compare in loop because different types would have different hash, so set can't work properly
                                if (d2.size() == 0) {
                                    return d1;
                                }
                                final Iterator<? extends TypeData> iter1 = d1.iterator();
                                final LinkedHashSet<TypeData> result = new LinkedHashSet<>();
                                Iterator<? extends TypeData> iter2;
                                TypeData val1, val2;
                                t: while(iter1.hasNext()) {
                                    val1 = iter1.next();
                                    iter2 = d2.iterator();
                                    while (iter2.hasNext()) {
                                        val2 = iter2.next();
                                        if (val1.compareTo(val2) == 0) {
                                            continue t;
                                        }
                                    }
                                    result.add(val1);
                                }
                                return new SetData(result);
                            }
                        case Types.SPARSEVECTOR: {
                            //compare in loop because different types would have different hash, so set can't work properly
                            final List<DoubleData> sd2 = ((SparseVectorData) d2).getNonzeroDatas();
                            final boolean hasZeros = d2.size() != sd2.size();
                            final Iterator<? extends TypeData> iter1 = d1.iterator();
                            final LinkedHashSet<TypeData> result = new LinkedHashSet<>();
                            TypeData val1;
                            
                            t: while(iter1.hasNext()) {
                                val1 = iter1.next();
                                for (final TypeData val2 : sd2) {
                                    if (hasZeros && val1.compareTo(DoubleData.ZERO) == 0) {
                                        continue t;
                                    }
                                    if (val1.compareTo(val2) == 0) {
                                        continue t;
                                    }
                                }
                                result.add(DataConv.toDoubleData(val1));
                            }
                            return new SetData(result);
                        }
                        default: {
                            final Iterator<? extends TypeData> iter1 = d1.iterator();
                            final LinkedHashSet<TypeData> result = new LinkedHashSet<>();
                            TypeData val1;
                            
                            while(iter1.hasNext()) {
                                val1 = iter1.next();
                                if (val1.compareTo(d2) != 0) {
                                    result.add(DataConv.toDoubleData(val1));
                                }
                            }
                            return new SetData(result);
                        }
                    }
                }
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR:
                if (d2.getType() == Types.MAP) {
                    throw new UnsupportedDataOperationException ("DATA_DEDUCT: VECTOR DEDUCT MAP - " + d2.toString());
                } else {
                    if (d1.isEmpty()) {
                        return DenseVectorData.NULL;
                    }
                   switch(d2.getType()) {
                        case Types.LIST:
                        case Types.SET:
                        case Types.DENSEVECTOR: {
                            //compare in loop because different types would have different hash, so set can't work properly
                            if (d2.size() == 0) {
                                return d1;
                            }
                            
                            final Iterator<? extends TypeData> iter1 = DataConv.toDenseVectorData(d1).iterator();
                            final ArrayList<Double> result = new ArrayList<>();
                            Iterator<? extends TypeData> iter2;
                            TypeData val1, val2;
                            Double d;
                            t: while (iter1.hasNext()) {
                                val1 = iter1.next();
                                iter2 = d2.iterator();
                                while (iter2.hasNext()) {
                                    val2 = iter2.next();
                                    if (val1.compareTo(val2) == 0) {
                                        continue t;
                                    }
                                }
                                d = val1.toDouble();
                                result.add(d != null ? d : 0D);
                            }
                            return new DenseVectorData(result.toArray(new Double[result.size()]));
                        }
                        case Types.SPARSEVECTOR: {
                            //compare in loop because different types would have different hash, so set can't work properly
                            final List<DoubleData> sd2 = ((SparseVectorData) d2).getNonzeroDatas();
                            final boolean hasZeros = d2.size() != sd2.size();
                            final Iterator<? extends TypeData> iter1 = DataConv.toDenseVectorData(d1).iterator();
                            final ArrayList<Double> result = new ArrayList<>();
                            TypeData val1;
                            Double d;
                            
                            t: while (iter1.hasNext()) {
                                val1 = iter1.next();
                                for (final TypeData val2 : sd2) {
                                    if (hasZeros && val1.compareTo(DoubleData.ZERO) == 0) {
                                        continue t;
                                    }
                                    if (val1.compareTo(val2) == 0) {
                                        continue t;
                                    }
                                }
                                d = val1.toDouble();
                                result.add(d != null ? d : 0D);
                            }
                            return new DenseVectorData(result.toArray(new Double[result.size()]));
                        }
                        default: {
                            final Iterator<? extends TypeData> iter1 = DataConv.toDenseVectorData(d1).iterator();
                            final ArrayList<Double> result = new ArrayList<>();
                            TypeData val1;
                            Double d;
                            while(iter1.hasNext()) {
                                val1 = iter1.next();
                                if (val1.compareTo(d2) != 0) {
                                    d = val1.toDouble();
                                    result.add(d != null ? d : 0D);
                                }
                            }
                            return new DenseVectorData(result.toArray(new Double[result.size()]));
                        }
                    }
                }
            case Types.MAP:
                if (d2.getType() == Types.MAP) {
                    if (d1.isEmpty()) {
                        return MapData.NULL;
                    }
                    if (d2.isEmpty()) {
                        return d1;
                    }
                    final LinkedHashMap<TypeData, TypeData> result = new LinkedHashMap<>(((MapData)d1).maps());
                    for (final Map.Entry<TypeData, TypeData> entry : ((MapData) d2).entrySet()) {
                        result.remove(entry.getKey());
                    }
                    return new MapData(result);
                } else {
                    throw new UnsupportedDataOperationException ("DATA_DEDUCT: MAP DEDUCT NONMAP - " + d2.toString());
                }
            default: {
                switch(d2.getType()) {
                    case Types.LIST: 
                    case Types.SET: 
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:{
                        if (d2.isEmpty()) {
                            return ListData.NULL;
                        }

                        final ArrayList<TypeData> result = new ArrayList<>();
                        Iterator<? extends TypeData> iter2 = d2.iterator();
                        TypeData val2;
                        
                        while (iter2.hasNext()) {
                            val2 = iter2.next();
                            if (d1.compareTo(val2) == 0) {
                                return new ListData(result);
                            }
                        }
                        result.add(d1);
                        return new ListData(result);
                    }
                    default: {
                        final ArrayList<TypeData> result = new ArrayList<>();
                        if (d1.compareTo(d2) != 0) {
                            result.add(d1);
                        }
                        return new ListData(result);
                    }
                }
            }
        }
    }
    
    public static TypeData readTypeData (final ObjectInput in, final byte type) throws IOException {
        switch(type) {
            case Types.INT: return IntData.readData(in);
            case Types.LONG: return LongData.readData(in);
            case Types.FLOAT: return FloatData.readData(in);
            case Types.DOUBLE: return DoubleData.readData(in);
            case Types.NUMERIC: return NumericData.readData(in);
            case Types.DECIMAL: return DecimalData.readData(in);
            case Types.COMPLEX: return ComplexData.readData(in);
            case Types.BOOLEAN: return BooleanData.readData(in);
            case Types.DATE: return DateData.readData(in);
            case Types.TIME: return TimeData.readData(in); 
            case Types.TIMESTAMP: return TimestampData.readData(in);
            case Types.CALENDAR_TIME: return CalendarTimeData.readData(in);
            case Types.CALENDAR_TIMESTAMP: return CalendarTimestampData.readData(in);
            case Types.INSTANT: return InstantData.readData(in);
            case Types.BINARY: return BinaryData.readData(in); 
            case Types.STRING: return StringData.readData(in);
            case Types.LIST: return ListData.readData(in);
            case Types.SET: return SetData.readData(in);
            case Types.MAP: return MapData.readData(in);
            case Types.DENSEVECTOR: return DenseVectorData.readData(in);
            case Types.SPARSEVECTOR: return SparseVectorData.readData(in);
            case Types.NULL: return NullData.INSTANCE;
            default: throw new IOException("READ_TYPEDATA_INVALID_TYPE");
        }
    }
    
    public static IntData toIntData (final Number value) {
        return value == null ? IntData.NULL : IntData.nonNullValueOf(value.intValue());
    }
    
    public static LongData toLongData (final Number value) {
        return value == null ? LongData.NULL : LongData.nonNullValueOf(value.longValue());
    }
    
    public static FloatData toFloatData (final Number value) {
        return value == null ? FloatData.NULL: FloatData.nonNullValueOf(value.floatValue());
    }
    
    public static DoubleData toDoubleData (final Number value) {
        return value == null ? DoubleData.NULL : DoubleData.nonNullValueOf(value.doubleValue());
    }
    
    public static NumericData toNumericData (final Number value) {
        return value == null ? NumericData.NULL : NumericData.nonNullValueOf(value.doubleValue());
    }
    
    public static DecimalData toDecimalData (final Number value) {
        return value == null 
                ? DecimalData.NULL 
                : value instanceof BigDecimal 
                    ? DecimalData.nonNullValueOf((BigDecimal) value)
                    : DecimalData.nonNullValueOf(value.toString());
    }
    
    public static BooleanData toBooleanData (final Boolean value) {
        return BooleanData.valueOf(value);
    }

    public static BinaryData toBinaryData (final byte[] value) {
        return BinaryData.valueOf(value);
    }
    
    public static DateData toDateData (final java.util.Date value) {
        return DateData.valueOf(value.getTime());
    }

    public static TimeData toTimeData (final java.sql.Time value) {
        return TimeData.valueOf(value);
    }
    
    public static TimestampData toTimestampData (final java.sql.Timestamp value) {
        return TimestampData.valueOf(value);
    }

    public static CalendarTimeData toCalendarTimeData (final java.util.Calendar value) {
        return CalendarTimeData.valueOf(value);
    }

    public static CalendarTimestampData toCalendarTimestampData (final java.util.Calendar value) {
        return CalendarTimestampData.valueOf(value);
    }
    
    public static InstantData toInstantData (final Instant value) {
        return InstantData.valueOf(value);
    }

    public static NullData toNullData () {
        return NullData.INSTANCE;
    }

    public static NullData toNullData (Object value) {
        return NullData.INSTANCE;
    }

    public static ListData toListData (final Collection<?> value) {
        if (value ==null || value.size() == 0) {
            return ListData.NULL;
        }

        final ArrayList<TypeData> list = new ArrayList<>(value.size());
        for (final Object obj : value) {
            list.add(XTypeDataUtils.getObjectTypeData(obj));
        }
        return new ListData(list);
    }

    public static SetData toSetData (Collection<?> value) {
        if (value ==null || value.size() == 0) {
            return SetData.NULL;
        }

        final LinkedHashSet<TypeData> set = new LinkedHashSet<>(value.size());
        for (final Object obj : value) {
            set.add(XTypeDataUtils.getObjectTypeData(obj));
        }
        return new SetData(set);
    }

    public static MapData toMapData (java.util.Map<?, ?> value) {
        if (value ==null || value.size() == 0) {
            return MapData.NULL;
        }
        
        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>(value.size());
        TypeData keyData;
        for (final Map.Entry<?, ?> entry : value.entrySet()) {
            if (!TypeUtils.isNonPrimitiveTypeData(keyData = XTypeDataUtils.getObjectTypeData(entry.getKey()))) {
                map.put(keyData, XTypeDataUtils.getObjectTypeData(entry.getValue()));
            } else {
                throw new IllegalArgumentException("DATA_MAP_PRIMITIVE_KEY_ONLY");
            }
        }
        return new MapData(map);
    }

    public static DenseVectorData toDenseVectorData (final Collection<Double> value) {
        if (value == null || value.size() == 0) {
            return DenseVectorData.NULL;
        }
        
        final int vsize = value.size();
        final double[] dbls = new double[vsize];
        int i = 0;
        for (final Double d : value) {
            dbls[i++] = d != null ? d.doubleValue() : 0d;
        }
        return new DenseVectorData(dbls);
    }

    public static DenseVectorData toDenseVectorData (final double[] value) {
        return DenseVectorData.valueOf(value);
    }

    public static SparseVectorData toSparseVectorData (final Collection<Double> value) {
        if (value == null || value.size() == 0) {
            return SparseVectorData.NULL;
        }
        
        final int vsize = value.size();
        int[] indices = new int[vsize];
        double[] nonzeros = new double[vsize];
        int nzlength = 0, i = 0;
        
        for (final Double d : value) {
            if (d != null && 0l != Double.doubleToLongBits(d.doubleValue())) {
                indices[nzlength] = i;
                nonzeros[nzlength++] = d;
            }
            ++i;
        }
        
        if (nzlength != vsize) {
            final int[] newIndices = new int[nzlength];
            final double[] newNonzeros = new double[nzlength];
            System.arraycopy(indices, 0, newIndices, 0, nzlength);
            System.arraycopy(nonzeros, 0, newNonzeros, 0, nzlength);
            indices = newIndices;
            nonzeros = newNonzeros;
        }
        
        return new SparseVectorData(vsize, indices, nonzeros);
    }
    
    public static ListData recordCollectionToListData(final RecordCollection collection) {
        final int size = collection.size();
        if (size == 0) {
            return ListData.NULL;
        }
        
        final ArrayList<TypeData> list = new ArrayList<>(size);
        final Iterator<Record> iter = collection.iterator();
        while (iter.hasNext()) {
            final Record record = iter.next();
            final int rsize = record.size();
            final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>(rsize);
            for (int i = 0; i < rsize; ++i) {
                map.put(StringData.nonNullValueOf(record.nameAt(i)), record.dataAt(i));
            }
            list.add(new MapData(map));
        }
        return new ListData(list);
        
    }
    
    public static MapData recordToMapData(final Record record) {
        final int size = record.size();
        if (size == 0) {
            return MapData.NULL;
        }
        
        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>(size);
        for (int i = 0; i < size; ++i) {
            map.put(StringData.nonNullValueOf(record.nameAt(i)), record.dataAt(i));
        }
        return new MapData(map);
        
    }
}
