package com.exceeddata.ac.common.data.typedata;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.exception.EngineException;

public final class DataContain {
    private DataContain() {}

    public static boolean binaryContains(final BinaryData x, final TypeData d) {
        return DataCompare.binaryCompareTo(x, d) == 0;
    }

    public static boolean booleanContains(final BooleanData x, final TypeData d) {
        return DataCompare.booleanCompareTo(x, d) == 0;
    }

    public static boolean calendarTimeContains(final CalendarTimeData x, final TypeData d) {
        return DataCompare.calendarTimeCompareTo(x, d) == 0;
    }

    public static boolean calendarTimestampContains(final CalendarTimestampData x, final TypeData d) {
        return DataCompare.calendarTimestampCompareTo(x, d) == 0;
    }
    
    public static boolean complexContains(final ComplexData x, final TypeData d) {
        return DataCompare.complexCompareTo(x, d) == 0;
    }
    
    public static boolean dateContains(final DateData x, final TypeData d) {
        return DataCompare.dateCompareTo(x, d) == 0;
    }
    
    public static boolean decimalContains(final DecimalData x, final TypeData d) {
        return DataCompare.decimalCompareTo(x, d) == 0;
    }

    public static boolean denseVectorContains(final DenseVectorData x, final TypeData d) {
        if (x.vsize == 0) {
            return d.isNull();
        } else if (d.isNull() || d.getType() == Types.MAP) {
            return false;
        }
        
        switch (d.getType()) {
            case Types.LIST: 
            case Types.SET: {
                final Iterator<DoubleData> iter = x.iterator();
                Iterator<? extends TypeData> diter;
                DoubleData data;
                boolean matched;
                while (iter.hasNext()) {
                    data = iter.next(); 
                    diter = d.iterator();
                    matched = false;
                    d: while (diter.hasNext()) {
                        if (data.compareTo(diter.next()) == 0) {
                            matched = true;
                            break d;
                        }
                    }
                    if (!matched) {
                        return false;
                    }
                }
                return true;
            }
            case Types.DENSEVECTOR: {
                final DenseVectorData dv = (DenseVectorData) d;
                final HashSet<Double> vdata1 = new HashSet<>(), vdata2 = new HashSet<>();
                for (final double item : x.items) {
                    vdata1.add(item);
                }
                for (final double item : dv.items) {
                    vdata2.add(item);
                }
                return vdata1.containsAll(vdata2);
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData sv = (SparseVectorData) d;
                final HashSet<Double> vdata1 = new HashSet<>(), vdata2 = new HashSet<>();
                for (final double item : x.items) {
                    vdata1.add(item);
                }
                if (sv.haszeros()) {
                    vdata2.add(0D);
                }
                for (final double item : sv.nonzeros) {
                    vdata2.add(item);
                }
                return vdata1.containsAll(vdata2);
            }
            default:
                final Iterator<DoubleData> iter = x.iterator();
                while (iter.hasNext()) {
                    if (iter.next().compareTo(d) == 0) {
                        return true;
                    }
                }
                return false;
        }
    }
    
    public static boolean doubleContains(final DoubleData x, final TypeData d) {
        return DataCompare.doubleCompareTo(x, d) == 0;
    }
    
    public static boolean numericContains(final NumericData x, final TypeData d) {
        return DataCompare.numericCompareTo(x, d) == 0;
    }
    
    public static boolean floatContains(final FloatData x, final TypeData d) {
        return DataCompare.floatCompareTo(x, d) == 0;
    }
    
    public static boolean instantContains(final InstantData x, final TypeData d) {
        return DataCompare.instantCompareTo(x, d) == 0;
    }
    
    public static boolean intContains(final IntData x, final TypeData d) {
        return DataCompare.intCompareTo(x, d) == 0;
    }

    public static boolean listContains(final ListData x, final TypeData d) {
        if (x.items == null || x.items.size() == 0) {
            return d.isNull();
        } else if (d.getType() == Types.MAP) {
            return false;
        } else if (d.isNull()) {
            for (final TypeData item : x.items) {
                if (item.isNull()) {
                    return true;
                }
            }
            return false;
        }
        
        switch (d.getType()) {
            case Types.LIST: {
                final ListData data = (ListData) d;
                return x.items.containsAll(data.items);
            }
            case Types.SET: {
                final SetData data = (SetData) d;
                return x.items.containsAll(data.items);
            }
            case Types.DENSEVECTOR: {
                final DenseVectorData dv = (DenseVectorData) d;
                final HashSet<TypeData> vdata1 = new HashSet<>(), vdata2 = new HashSet<>();
                for (final TypeData item : x.items) {
                    vdata1.add(item);
                }
                for (final double item : dv.items) {
                    vdata2.add(new DoubleData(item));
                }
                return vdata1.containsAll(vdata2);
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData data = (SparseVectorData) d;
                return x.items.containsAll(data.getNonzeroDatas()) && (!data.haszeros() || x.items.contains(DoubleData.ZERO));
            }
            default:
                return x.items.contains(d);
        }
    }
    
    public static boolean longContains(final LongData x, final TypeData d) {
        return DataCompare.longCompareTo(x, d) == 0;
    }
    
    public static boolean mapContains(final MapData x, final TypeData d) {
        if (x.maps == null || x.maps.size() == 0) {
            return d.isNull();
        }
        if (d.isNull()) {
            for (Map.Entry<TypeData, TypeData> entry : x.maps.entrySet()) {
                if (entry.getValue().compareTo(NullData.INSTANCE) == 0) {
                    return true;
                }
            }
            return false;
        }
        switch (d.getType()) {
            case Types.LIST:
            case Types.SET:
            case Types.DENSEVECTOR:
                for (final Iterator<? extends TypeData> diter = d.iterator(); diter.hasNext();) {
                    if (!x.maps.containsValue(diter.next())) {
                        return false;
                    }
                }
                return true;
            case Types.SPARSEVECTOR: {
                final SparseVectorData vdata = (SparseVectorData) d;
                if (vdata.haszeros() && !x.maps.containsValue(DoubleData.ZERO)) {
                    return false;
                }
                for (final DoubleData data : vdata.getNonzeroDatas()) {
                    if (!x.maps.containsValue(data)) {
                        return false;
                    }
                }
                return true;
            }
            case Types.MAP: {
                final MapData mdata = (MapData) d;
                for (final TypeData key : mdata.keySet()) {
                    if (!x.maps.containsKey(key) || x.maps.get(key).compareTo(mdata.get(key)) != 0) {
                        return false;
                    }
                }
                return true;
            }
            default:
                return x.maps.containsValue(d);
        }
    }
    public static boolean nullContains(final TypeData d) {
        return d.isNull();
    }
    
    public static boolean setContains(final SetData x, final TypeData d) {
        if (x.items == null || x.items.size() == 0) {
            return d.isNull();
        } else if (d.getType() == Types.MAP) {
            return false;
        } else if (d.isNull()) {
            final int size = x.items.size();
            final Iterator<TypeData> iter = x.items.iterator();
            for (int i = 0; i < size; ++i) {
                if (iter.next().compareTo(NullData.INSTANCE) == 0) {
                    return true;
                }
            }
            return false;
        }
        
        switch (d.getType()) {
            case Types.LIST: {
                final ListData data = (ListData) d;
                return x.items.containsAll(data.items);
            }
            case Types.SET: {
                final SetData data = (SetData) d;
                return x.items.containsAll(data.items);
            }
            case Types.DENSEVECTOR: {
                final DenseVectorData data = (DenseVectorData) d;
                for (final double item : data.items) {
                    if (!x.items.contains(new DoubleData(item))) {
                        return false;
                    }
                }
                return true;
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData vectorData = (SparseVectorData) d;
                return x.items.containsAll(vectorData.getNonzeroDatas()) && (!vectorData.haszeros() || x.items.contains(DoubleData.ZERO));
            }
            default:
                return x.items.contains(d);
        }
    }

    public static boolean sparseVectorContains(final SparseVectorData x, final TypeData d) {
        if (x.vsize == 0) {
            return d.isNull();
        } else if (d.isNull()) {
            return false;
        }
        
        switch (d.getType()) {
            case Types.LIST: {
                final List<TypeData> items = ((ListData) d).getUnsafeItems();
                if (x.haszeros()) {
                    boolean matched = false;
                    t: for (int i = 0, s = items.size(); i < s; ++i) {
                        if (items.get(i).compareTo(DoubleData.ZERO) == 0) {
                            matched = true;
                            break t;
                        }
                    }
                    if (!matched) {
                        return false;
                    }
                }
                return items.containsAll(x.getNonzeroDatas());
            }
            case Types.SET: {
                final Set<TypeData> items = ((SetData) d).getUnsafeItems();
                if (x.haszeros()) {
                    boolean matched = false;
                    final Iterator<TypeData> iter = items.iterator();
                    t: for (int i = 0, s = items.size(); i < s; ++i) {
                        if (iter.next().compareTo(DoubleData.ZERO) == 0) {
                            matched = true;
                            break t;
                        }
                    }
                    if (!matched) {
                        return false;
                    }
                }
                return items.containsAll(x.getNonzeroDatas());
            }
            case Types.DENSEVECTOR: {
                final List<Double> nonzeros1 = DoubleStream.of(x.getNonzeros()).boxed().collect(Collectors.toList());
                final DenseVectorData data = (DenseVectorData) d;
                final boolean haszero = x.vsize > x.nzlength;
                for (double item : data.items) {
                    if (Double.compare(item, 0d) == 0) {
                        if (!haszero) {
                            return false;
                        }
                    } else {
                        if (!nonzeros1.contains(item)) {
                            return false;
                        }
                    }
                }
                return true;
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData vdata2 = (SparseVectorData) d;
                if (x.haszeros() != vdata2.haszeros()) {
                    return false;
                }
                
                final List<Double> nonzeros1 = DoubleStream.of(x.getNonzeros()).boxed().collect(Collectors.toList());
                final List<Double> nonzeros2 = DoubleStream.of(vdata2.getNonzeros()).boxed().collect(Collectors.toList());
                return nonzeros1.containsAll(nonzeros2);
            }
            default:
                if (x.haszeros() && d.compareTo(DoubleData.ZERO) == 0) {
                    return true;
                }
                for (int i = 0; i < x.nzlength; ++i) {
                    if (DoubleData.nonNullValueOf(x.nonzeros[i]).compareTo(d) == 0) {
                        return true;
                    }
                }
                return false;
        }
    }

    public static boolean stringContains(final StringData x, final TypeData d) {
        return DataCompare.stringCompareTo(x, d) == 0;
    }

    public static boolean timeContains(final TimeData x, final TypeData d) {
        return DataCompare.timeCompareTo(x, d) == 0;
    }

    public static boolean timestampContains(final TimestampData x, final TypeData d) {
        return DataCompare.timestampCompareTo(x, d) == 0;
    }
    
    public static boolean contains(final TypeData x, final TypeData d) throws EngineException {
        switch (x.getType()) {
            case Types.INT: return intContains((IntData) x, d);
            case Types.LONG: return longContains((LongData) x, d);
            case Types.FLOAT: return floatContains((FloatData) x, d);
            case Types.DOUBLE: return doubleContains((DoubleData) x, d);
            case Types.NUMERIC: return numericContains((NumericData) x, d);
            case Types.DECIMAL: return decimalContains((DecimalData) x, d);
            case Types.COMPLEX: return complexContains((ComplexData) x, d);
            case Types.BOOLEAN: return booleanContains((BooleanData) x, d);
            case Types.DATE: return dateContains((DateData) x, d);
            case Types.TIME: return timeContains((TimeData) x, d);
            case Types.TIMESTAMP: return timestampContains((TimestampData) x, d);
            case Types.CALENDAR_TIME: return calendarTimeContains((CalendarTimeData) x, d);
            case Types.CALENDAR_TIMESTAMP: return calendarTimestampContains((CalendarTimestampData) x, d);
            case Types.INSTANT: return instantContains((InstantData) x, d);
            case Types.BINARY: return binaryContains((BinaryData) x, d);
            case Types.STRING: return stringContains((StringData) x, d);
            case Types.LIST: return listContains((ListData) x, d);
            case Types.SET: return setContains((SetData) x, d);
            case Types.MAP: return mapContains((MapData) x, d);
            case Types.DENSEVECTOR: return denseVectorContains((DenseVectorData) x, d);
            case Types.SPARSEVECTOR: return sparseVectorContains((SparseVectorData) x, d);
            case Types.NULL: return d.isNull();
            default:
                return false;
        }
    }
}
