package com.exceeddata.ac.common.data.typedata;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.exception.EngineDataException;
import com.exceeddata.ac.common.exception.data.DataConversionException;
import com.exceeddata.ac.common.util.XStringUtils;
import com.exceeddata.ac.common.util.XTypeDataUtils;

/**
 * A data class for Map of key value objects.
 *
 */
public final class MapData implements TypeData {
    private static final long serialVersionUID = 1L;
    public static final MapData NULL = new MapData();
    
    protected LinkedHashMap<TypeData, TypeData> maps;
    
    public MapData() {
        this.maps = null;
    }
    
    public MapData(final int capacity) {
        this.maps = new LinkedHashMap<>(capacity);
    }
    
    public MapData(final LinkedHashMap<TypeData, TypeData> maps) {
        this.maps = maps;
    }
    
    /**
     * Construct a <code>MapData</code> with String.
     * 
     * @param value the value in string
     */
    public MapData(final String value) {
        this.maps = XTypeDataUtils.jsonToTypeDataMap(value);
        if (this.maps.size() == 0) {
            this.maps = null;
        }
    }
    
    
    /**
     * Construct a <code>MapData</code> with MapData
     * 
     * @param data the MapData
     */
    public MapData(final MapData data) {
        this.maps = (data != null && data.maps != null && data.maps.size() > 0) ? new LinkedHashMap<>(data.maps) : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public MapData clone() {
        return new MapData(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public MapData copy() {
        return new MapData(this);
    }
    
    /**
     * Create a shallow copy.
     * 
     * @return MapData
     */
    public MapData shallowCopy() {
        return new MapData(this);
    }
    
    /**
     * Get TypeData entry set.
     * 
     * @return Set of Entry
     */
    public Set<Entry<TypeData, TypeData>> entrySet() {
        return maps == null ? new LinkedHashMap<TypeData, TypeData>(0).entrySet() : maps.entrySet();
    }
    
    /**
     * Get TypeData key set
     * 
     * @return Set of TypeData
     */
    public Set<TypeData> keySet() {
        return maps == null ? new LinkedHashMap<TypeData, TypeData>(0).keySet() : maps.keySet();
    }
    
    /**
     * Get TypeData Map.
     * 
     * @return Set
     */
    public LinkedHashMap<TypeData, TypeData> maps() {
        return maps == null ? new LinkedHashMap<TypeData, TypeData>(0) : maps;
    }
    
    /**
     * Get the TypeData associated with a given key.
     * 
     * @param accessor the key
     * @return TypeData
     */
    @Override
    public TypeData get(final TypeData accessor) {
        if (maps == null || maps.size() == 0) {
            return NullData.INSTANCE;
        }
        final TypeData val = maps.get(accessor);
        return val != null ? val : NullData.INSTANCE;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Object, Object> getObject() {
        if (maps == null || maps.size() == 0) {
            return null;
        }
        final Map<Object, Object> objectMap = new LinkedHashMap<>(maps.size());
        for (final Entry<TypeData, TypeData> entry : maps.entrySet()) {
            if (entry.getKey().isNull()) {
                objectMap.put("null", entry.getValue().getObject());
            } else {
                objectMap.put(entry.getKey().getObject(), entry.getValue().getObject());
            }
        }
        return objectMap;
    }
    
    
    /**
     * Get the list of Objects.
     * 
     * @return Map
     */
    public Map<Object, Object> getMap() {
        if (maps == null || maps.size() == 0) {
            return null;
        }
        
        final Map<Object, Object> objectMap = new LinkedHashMap<Object, Object>(maps.size());
        for (Entry<TypeData, TypeData> entry : maps.entrySet()) {
            objectMap.put(entry.getKey().getObject(), entry.getValue().getObject());
        }
        return objectMap;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType() {
        return Types.MAP;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.mapCompareTo(this, w);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TypeData) {
            final TypeData w = (TypeData) obj;
            if (maps == null || maps.size() == 0) {
                return w.isNull();
            } else if (w.getType() == Types.MAP) {
                final MapData data = (MapData) w;
                final int wsize = data.maps != null ? data.maps.size() : 0;
                if (maps.size() == wsize) {
                    final Iterator<Map.Entry<TypeData,TypeData>> iter = maps.entrySet().iterator(), witer = data.maps.entrySet().iterator();
                    Map.Entry<TypeData, TypeData> entry, wentry;
                    while (iter.hasNext()) {
                        entry = iter.next();
                        wentry = witer.next();
                        if (!entry.getKey().equals(wentry.getKey()) || !entry.getValue().equals(wentry.getValue())) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        final int size = maps != null ? maps.size() : 0;
        if (size == 0) {
            out.writeBoolean(true);
        } else {
            out.writeBoolean(false);
            out.writeInt(size);
            
            TypeData keyData, valueData;
            for (final Entry<TypeData, TypeData> entry : maps.entrySet()) {
                keyData = entry.getKey();
                out.writeByte(keyData.getType());
                keyData.writeExternal(out);

                valueData = entry.getValue();
                out.writeByte(valueData.getType());
                valueData.writeExternal(out);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        if (in.readBoolean()) {
            maps = null;
        } else {
            readEntries(in);
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static MapData readData(final ObjectInput in) throws IOException {
        if (in.readBoolean()) {
            return MapData.NULL;
        } else {
            final MapData md = new MapData();
            md.readEntries(in);
            return md;
        }
    }
    
    private void readEntries(final ObjectInput in) throws IOException {
        final int len = in.readInt();
        this.maps = new LinkedHashMap<TypeData, TypeData>(len);
        
        TypeData keyData, valueData;
        
        for (int i = 0; i < len; ++i) {
            keyData = XTypeDataUtils.readTypeData(in, in.readByte());
            valueData = XTypeDataUtils.readTypeData (in, in.readByte());
            
            maps.put(keyData, valueData);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public int size() {
        return maps == null ? 0 : maps.size();
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<TypeData> iterator() {
        return maps == null ? new ArrayList<TypeData>(0).iterator() : maps.values().iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData first() {
        return maps == null || maps.size() == 0 ? NullData.INSTANCE : maps.entrySet().iterator().next().getValue();
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_INT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_LONG_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_FLOAT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_DOUBLE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_DECIMAL_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_BOOLEAN_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_DATE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_TIME_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_TIMESTAMP_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_TIMEWITHTIMEZONE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_TIMESTAMPWITHTIMEZONE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() throws EngineDataException {
        throw new DataConversionException ("DATA_MAP_TO_INSTANT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Object> toList() {
        if (maps == null || maps.size() == 0) {
            return null;
        }
        
        final ArrayList<Object> list = new ArrayList<>(maps.size());
        for (final TypeData item : maps.values()) {
            list.add(item.getObject());
        }
        return list;
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Object> toSet() {
        if (maps == null || maps.size() == 0) {
            return null;
        }
        
        final LinkedHashSet<Object> set = new LinkedHashSet<>(maps.size());
        for (final TypeData item : maps.values()) {
            set.add(item.getObject());
        }
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Object, Object> toMap() {
        return getObject();
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return maps == null || maps.size() == 0 ? null : toString().getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        if (maps == null || maps.size() == 0) {
            return "";
        }
        
        final StringBuilder sb = new StringBuilder(512);
        sb.append('{');
        
        for (final Map.Entry<TypeData, TypeData> entry : maps.entrySet()) {
            XStringUtils.escapeJSON(sb, entry.getKey().toString());
            sb.append(':');
            
            XTypeDataUtils.appendJSONValue(sb, entry.getValue());
            sb.append(',');
        }
        
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setCharAt(sb.length() - 1, '}');
        } else {
            sb.append('}');
        }
        return sb.toString();
    }
    
    /**
     * Get the item at the index.  NullData if outside of range.
     * 
     * @param index the index
     * @return TypeData
     */
     public TypeData valueAt(final int position) {
        if (maps == null || maps.size() == 0 || position < 0 || position >= maps.size() ) {
            return NullData.INSTANCE;
        }
        
        int index = 0;
        for (final Map.Entry<TypeData, TypeData> entry : maps.entrySet()) {
            if (index++ == position) {
                return entry.getValue();
            }
        }
        return NullData.INSTANCE;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNull() {
        return maps == null || maps.size() == 0;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return maps == null || maps.size() == 0;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isDigits() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return false;
    }
    
    public static MapData valueOf(final LinkedHashMap<TypeData, TypeData> maps) {
        return maps != null && maps.size() > 0 ? new MapData(maps) : MapData.NULL;
    }
}
