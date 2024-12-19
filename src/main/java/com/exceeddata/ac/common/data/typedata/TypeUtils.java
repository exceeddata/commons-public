package com.exceeddata.ac.common.data.typedata;

import com.exceeddata.ac.common.data.type.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TypeUtils {
    private TypeUtils(){}
    
    protected static final int NULL_HASH = 1327;
    
    public static boolean isNonPrimitiveTypeData(final TypeData t) {
        switch(t.getType()) {
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR:
            case Types.MAP:
                return true;
            default:
                return false;
        }
    }

    public static TypeData dataOf(final Object o) {
        return o == null ? NullData.INSTANCE : nonNullDataOf(o);
    }

    public static TypeData nonNullDataOf(final Object o) {
        if (o instanceof Number) {
            if (o instanceof Integer) {
                return IntData.nonNullValueOf((Integer) o);
            } else {
                return DoubleData.nonNullValueOf(((Number) o).doubleValue());
            }
        }
        if (o instanceof Number[]) {
            final Number[] ns = (Number[]) o;
            final double[] dbls = new double[ns.length];
            for (int i = 0, s = ns.length; i < s; ++i) {
                dbls[i] = ns[i].doubleValue();
            }
            return DenseVectorData.valueOf(dbls);
        }
        if (o instanceof byte[]) {
            final byte[] ns = (byte[]) o;
            final double[] dbls = new double[ns.length];
            for (int i = 0, s = ns.length; i < s; ++i) {
                dbls[i] = ns[i];
            }
            return DenseVectorData.valueOf(dbls);
        }
        if (o instanceof Map) {
            return nonNullMapDataOf(o);
        }
        if (o instanceof String[]) {
            final String[] ns = (String[]) o;
            final ArrayList<TypeData> ts = new ArrayList<>();
            for (int i = 0, s = ns.length; i < s; ++i) {
                ts.add(StringData.valueOf(ns[i]));
            }
            return ListData.valueOf(ts);
        }

        return StringData.nonNullValueOf(o.toString());
    }

    public static TypeData nonNullMapDataOf(final Object o) {
        final Map<?, ?> ms = (Map<?, ?>) o;
        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        for (final Map.Entry<?, ?> entry : ms.entrySet()) {
            final StringData key = StringData.valueCacheOf(entry.getKey().toString());
            final TypeData value = dataOf(entry.getValue());
            map.put(key, value);
        }
        return new MapData(map);
    }
}
