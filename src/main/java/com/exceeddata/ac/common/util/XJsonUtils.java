package com.exceeddata.ac.common.util;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public final class XJsonUtils {
    public static final Gson JSON = new GsonBuilder().disableHtmlEscaping().create();
    @SuppressWarnings("deprecation")
    public static final JsonParser PARSER = new JsonParser();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Object>>(){}.getType();
    private static final Type ARRAY_TYPE = new TypeToken<List<Object>>(){}.getType();
    private static final int FLOATING_DIGIT = 32;
    
    private XJsonUtils() {}
    
    public static Map<String, Object> fromJsonObject(final String s) {
        if (XStringUtils.isBlank(s)) {
            return null;
        }
        return JSON.fromJson(s, MAP_TYPE);
    }
    
    public static List<Object> fromJsonArray(final String s) {
        if (XStringUtils.isBlank(s)) {
            return null;
        }
        return JSON.fromJson(s, ARRAY_TYPE);
    }
    
    public static String toJson(final Map<?, ?> map) {
        return JSON.toJson(map);
    }
    
    public static String toJson(final List<?> list) {
        return JSON.toJson(list);
    }
    
    public static String getString(final Map<String, Object> map, final String key) {
        final Object v = map.get(key);
        return v == null ? null : v.toString();
    }
    
    public static String getString(final List<Object> list, final int index) {
        final Object v = list.get(index);
        return v == null ? null : v.toString();
    }
    
    public static Integer getInteger(final Map<String, Object> map, final String key) {
        final Object v = map.get(key);
        if (v == null) {
            return null;
        }
        
        if (v instanceof Number) {
            return ((Number) v).intValue();
        } else if (v instanceof Boolean) {
            return Boolean.TRUE.equals(v) ? 1 : 0;
        } else if (v instanceof String) {
            try {
                return new BigDecimal(v.toString()).intValue();
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    public static int getInt(final Map<String, Object> map, final String key, final int defaultValue) {
        final Object v = map.get(key);
        if (v == null) {
            return defaultValue;
        }
        
        if (v instanceof Number) {
            return ((Number) v).intValue();
        } else if (v instanceof Boolean) {
            return Boolean.TRUE.equals(v) ? 1 : 0;
        } else if (v instanceof String) {
            try {
                return new BigDecimal(v.toString()).intValue();
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
    
    public static int getInt(final List<Object> list, final int index, final int defaultValue) {
        final Object v = list.get(index);
        if (v == null) {
            return defaultValue;
        }
        
        if (v instanceof Number) {
            return ((Number) v).intValue();
        } else if (v instanceof Boolean) {
            return Boolean.TRUE.equals(v) ? 1 : 0;
        } else if (v instanceof String) {
            try {
                return new BigDecimal(v.toString()).intValue();
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
    
    public static long getLong(final Map<String, Object> map, final String key, final long defaultValue) {
        final Object v = map.get(key);
        if (v == null) {
            return defaultValue;
        }
        
        if (v instanceof Number) {
            return ((Number) v).longValue();
        } else if (v instanceof Boolean) {
            return Boolean.TRUE.equals(v) ? 1 : 0;
        } else if (v instanceof String) {
            try {
                return new BigDecimal(v.toString()).longValue();
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
    
    public static long getLong(final List<Object> list, final int index, final long defaultValue) {
        final Object v = list.get(index);
        if (v == null) {
            return defaultValue;
        }
        
        if (v instanceof Number) {
            return ((Number) v).longValue();
        } else if (v instanceof Boolean) {
            return Boolean.TRUE.equals(v) ? 1 : 0;
        } else if (v instanceof String) {
            try {
                return new BigDecimal(v.toString()).longValue();
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(final Map<String, Object> map, final String key) {
        final Object v = map.get(key);
        if (v == null) {
            return null;
        }
        return (Map<String, Object>) v;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(final List<Object> list, final int index) {
        final Object v = list.get(index);
        if (v == null) {
            return null;
        }
        return (Map<String, Object>) v;
    }
    
    @SuppressWarnings("unchecked")
    public static List<Object> getList(final Map<String, Object> map, final String key) {
        final Object v = map.get(key);
        if (v == null) {
            return null;
        }
        return (List<Object>) v;
    }
    

    
    public static String[] getStringArray(final Map<String, Object> map, final String key) {
        final Object v = map.get(key);
        if (!(v instanceof List)) {
            return null;
        }
        
        final List<?> list = (List<?>) v;
        final ArrayList<String> strs = new ArrayList<>();
        for (int i = 0, s = list.size(); i < s; ++i) {
            if (list.get(i) != null) {
                final String item = list.get(i).toString();
                if (XStringUtils.isNotBlank(item)) {
                    strs.add(item);
                }
            }
        }
        return strs.toArray(new String[strs.size()]);
    }
    
    public static String[][] getStringArrays(final Map<String, Object> map, final String key) {
        final Object v = map.get(key);
        if (!(v instanceof List)) {
            return null;
        }

        final List<?> list = (List<?>) v;
        final int size = list.size();

        final List<List<String>> strlists = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            final Object o = list.get(i);
            if (o instanceof List) {
                final List<?> list1 = (List<?>) o;
                final int size1 = list1.size();

                final List<String> strlist = new ArrayList<>();
                for (int j = 0; j < size1; ++j) {
                    if (list1.get(j) != null) {
                        final String item = list1.get(j).toString();
                        if (XStringUtils.isNotBlank(item)) {
                            strlist.add(item);
                        }
                    }
                }
                strlists.add(strlist);
            }
        }

        final String[][] res = new String[strlists.size()][];
        for (int i = 0; i < strlists.size(); ++i) {
            res[i] = strlists.get(i).toArray(new String[0]);
        }

        return res;
    }
    
    public static String toJsonValue(final double val, final DecimalFormat df) {
        final long vi = (long) val;
        return val != vi ? df.format(val) : df.format(vi);
    }
    
    public static String toJsonValue(final long val, final DecimalFormat df) {
        return df.format(val);
    }
    
    public static String toJsonValue(final int val, final DecimalFormat df) {
        return df.format(val);
    }
    
    public static String toJsonValue(final BigDecimal val) {
        return val.toPlainString();
    }
    
    public static String toJsonValue(final float val) {
        final long vi = (long) val;
        return val != vi ? String.valueOf(val) : String.valueOf(vi);
    }
    
    public static String toJsonValue(final double val) {
        final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(FLOATING_DIGIT);
        return toJsonValue(val, df);
    }
    
    public static String toJsonValue(final int val) {
        final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(0);
        return toJsonValue(val, df);
    }
    
    public static String toJsonValue(final long val) {
        final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(0);
        return toJsonValue(val, df);
    }
    
    public static String toJsonValue(final boolean val) {
        return val ? "1" : "0";
    }
    
    public static String[] toJsonValue(final double[] arr) {
        final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(FLOATING_DIGIT);
        
        final String[] vals = new String[arr.length];
        for (int i = 0, s = arr.length; i < s; ++i) {
            vals[i] = toJsonValue(arr[i], df);
        }
        return vals;
    }
    
    public static String[] toJsonValue(final float[] arr) {
        final String[] vals = new String[arr.length];
        for (int i = 0, s = arr.length; i < s; ++i) {
            vals[i] = toJsonValue(arr[i]);
        }
        return vals;
    }
    
    public static String[] toJsonValue(final int[] arr) {
        final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(0);
        
        final String[] vals = new String[arr.length];
        for (int i = 0, s = arr.length; i < s; ++i) {
            vals[i] = toJsonValue(arr[i], df);
        }
        return vals;
    }
    
    public static String[] toJsonValue(final long[] arr) {
        final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(0);
        
        final String[] vals = new String[arr.length];
        for (int i = 0, s = arr.length; i < s; ++i) {
            vals[i] = toJsonValue(arr[i], df);
        }
        return vals;
    }
    
    public static String[] toJsonValue(final BigDecimal[] arr) {
        final String[] vals = new String[arr.length];
        for (int i = 0, s = arr.length; i < s; ++i) {
            vals[i] = arr[i] == null ?  "0" : toJsonValue(arr[i]);
        }
        return vals;
    }
    
    public static String[] toJsonValue(final Double[] arr) {
        final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(FLOATING_DIGIT);
        
        final String[] vals = new String[arr.length];
        for (int i = 0, s = arr.length; i < s; ++i) {
            vals[i] = arr[i] == null ?  "0" : toJsonValue(arr[i], df);
        }
        return vals;
    }
    
    public static String[] toJsonValue(final Float[] arr) {
        final String[] vals = new String[arr.length];
        for (int i = 0, s = arr.length; i < s; ++i) {
            vals[i] = arr[i] == null ?  "0" : toJsonValue(arr[i]);
        }
        return vals;
    }
    
    public static String[] toJsonValue(final Integer[] arr) {
        final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(0);
        
        final String[] vals = new String[arr.length];
        for (int i = 0, s = arr.length; i < s; ++i) {
            vals[i] = arr[i] == null ?  "0" : toJsonValue(arr[i], df);
        }
        return vals;
    }
    
    public static double[] getDoubleArray(final Map<String, Object> map, final String key) {
        return getDoubleArray(map, key, null);
    }
    
    public static double[] getDoubleArray(final Map<String, Object> map, final String key, final double[] defaultValues) {
        final Object v = map.get(key);
        if (v == null) {
            return defaultValues;
        }
        
        final List<?> list = (List<?>) v;
        final int size = list.size();
        final double[] array = new double[size];
        for (int i = 0; i < size; ++i) {
            final Object item = list.get(i);
            if (item instanceof Number) {
                array[i] = ((Number) item).doubleValue();
            } else if (item instanceof Boolean) {
                array[i] = Boolean.TRUE.equals(item) ? 1d : 0d;
            } else if (item instanceof String) {
                try {
                    array[i] = new BigDecimal(item.toString()).doubleValue();
                } catch (NumberFormatException e) {
                }
            }
        }
        return array;
    }
    
    public static int[] getIntArray(final Map<String, Object> map, final String key) {
        return getIntArray(map, key, null);
    }
    
    public static int[] getIntArray(final Map<String, Object> map, final String key, final int[] defaultValues) {
        final Object v = map.get(key);
        if (v == null) {
            return defaultValues;
        }
        
        final List<?> list = (List<?>) v;
        final int size = list.size();
        final int[] array = new int[size];
        for (int i = 0; i < size; ++i) {
            final Object item = list.get(i);
            if (item instanceof Number) {
                array[i] = ((Number) item).intValue();
            } else if (item instanceof Boolean) {
                array[i] = Boolean.TRUE.equals(item) ? 1 : 0;
            } else if (item instanceof String) {
                try {
                    array[i] = new BigDecimal(item.toString()).intValue();
                } catch (NumberFormatException e) {
                }
            }
        }
        return array;
    }
    
    public static long[] getLongArray(final Map<String, Object> map, final String key) {
        return getLongArray(map, key, null);
    }
    
    public static long[] getLongArray(final Map<String, Object> map, final String key, final long[] defaultValues) {
        final Object v = map.get(key);
        if (v == null) {
            return defaultValues;
        }
        
        final List<?> list = (List<?>) v;
        final int size = list.size();
        final long[] array = new long[size];
        for (int i = 0; i < size; ++i) {
            final Object item = list.get(i);
            if (item instanceof Number) {
                array[i] = ((Number) item).longValue();
            } else if (item instanceof Boolean) {
                array[i] = Boolean.TRUE.equals(item) ? 1l : 0l;
            } else if (item instanceof String) {
                try {
                    array[i] = new BigDecimal(item.toString()).longValue();
                } catch (NumberFormatException e) {
                }
            }
        }
        return array;
    }
    
    public static boolean getBoolean(final Map<String, Object> map, final String key, final boolean defaultValue) {
        final Object v = map.get(key);
        if (v == null) {
            return defaultValue;
        }
        
        if (v instanceof Number) {
            return new BigDecimal(v.toString()).signum() != 0;
        } else if (v instanceof Boolean) {
            return Boolean.TRUE.equals(v);
        } else if (v instanceof String) {
            final String s = v.toString().trim().toLowerCase();
            if (XNumberUtils.isNumber(s)) {
                return new BigDecimal(s).signum() != 0;
            } else if ("false".equals(s) || "no".equals(s)) {
                return false;
            } else {
                return true;
            }
        }
        return defaultValue;
    }
    
    public static boolean getBoolean(final List<Object> list, final int index, final boolean defaultValue) {
        final Object v = list.get(index);
        if (v == null) {
            return defaultValue;
        }
        
        if (v instanceof Number) {
            return new BigDecimal(v.toString()).signum() != 0;
        } else if (v instanceof Boolean) {
            return Boolean.TRUE.equals(v);
        } else if (v instanceof String) {
            final String s = v.toString().trim().toLowerCase();
            if (XNumberUtils.isNumber(s)) {
                return new BigDecimal(s).signum() != 0;
            } else if ("false".equals(s) || "no".equals(s)) {
                return false;
            } else {
                return true;
            }
        }
        return defaultValue;
    }
    
    
    public static double getDouble(final Map<String, Object> map, final String key, final double defaultValue) {
        final Object v = map.get(key);
        if (v == null) {
            return defaultValue;
        }
        
        if (v instanceof Number) {
            return ((Number) v).doubleValue();
        } else if (v instanceof Boolean) {
            return Boolean.TRUE.equals(v) ? 1 : 0;
        } else if (v instanceof String) {
            try {
                return new BigDecimal(v.toString()).doubleValue();
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
    
    public static double getDouble(final List<Object> list, final int index, final double defaultValue) {
        final Object v = list.get(index);
        if (v == null) {
            return defaultValue;
        }
        
        if (v instanceof Number) {
            return ((Number) v).doubleValue();
        } else if (v instanceof Boolean) {
            return Boolean.TRUE.equals(v) ? 1 : 0;
        } else if (v instanceof String) {
            try {
                return new BigDecimal(v.toString()).doubleValue();
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
}
