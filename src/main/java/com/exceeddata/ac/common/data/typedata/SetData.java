package com.exceeddata.ac.common.data.typedata;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.exception.EngineDataException;
import com.exceeddata.ac.common.exception.data.DataConversionException;
import com.exceeddata.ac.common.util.XTypeDataUtils;

/**
 * A data class for Set of objects.
 *
 */
public final class SetData implements TypeData {
    private static final long serialVersionUID = 1L;
    public static final SetData NULL = new SetData();
    
    protected LinkedHashSet<TypeData> items;
    
    public SetData() {
        this.items = null;
    }
    
    public SetData(final int capacity) {
        this.items = new LinkedHashSet<TypeData>(capacity);
    }
    
    public SetData(final LinkedHashSet<TypeData> items) {
        this.items = items;
    }
    
    /**
     * Construct a <code>SetData</code>
     * 
     * @param value the value in string
     */
    public SetData(final String value) {
        this.items = XTypeDataUtils.jsonToTypeDataSet(value);
    }
    
    /**
     * Construct a <code>SetData</code> with a SetData
     * 
     * @param data the SetData
     */
    public SetData(final SetData data) {
        this.items = (data != null && data.items != null && data.items.size() > 0) ? new LinkedHashSet<TypeData>(data.items) : null;
    }
    
    /**
     * Get the item at the index.  NullData if outside of range.
     * 
     * @param index the index
     * @return TypeData
     */
    public TypeData itemAt(final int position) {
        if (items == null || position >= items.size()) {
            return NullData.INSTANCE;
        }
        
        final Iterator<TypeData> iter = items.iterator();
        for (int i = 0, s = position - 1; i < s; ++i) {
            iter.next();
        }
        return iter.next();
    }
    
    /** {@inheritDoc} */
    @Override
    public SetData clone() {
        return new SetData(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public SetData copy() {
        return new SetData(this);
    }
    
    /**
     * Create a shallow copy.
     * 
     * @return SetData
     */
    public SetData shallowCopy() {
        if (items == null || items.size() == 0) {
            return SetData.NULL;
        }
        return new SetData(new LinkedHashSet<>(this.items));
    }
    
    /**
     * Get the TypeData if it is in set.
     * 
     * @param accessor the accessor
     * @return TypeData
     */
    @Override
    public TypeData get(final TypeData accessor) {
        return items != null && items.contains(accessor) ? accessor : NullData.INSTANCE;
    }
    
    protected LinkedHashSet<TypeData> getUnsafeItems() {
        return items;
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Object> getObject() {
        if (items == null || items.size() == 0) {
            return null;
        }
        final LinkedHashSet<Object> objs = new LinkedHashSet<Object>(items.size());
        final Iterator<TypeData> iter = items.iterator();
        for (int i = 0, s = items.size(); i < s; ++i) {
            objs.add(iter.next().getObject());
        }
        return objs;
    }
    
    /**
     * Get the list of Objects.
     * 
     * @return Set
     */
    public Set<Object> getSet() {
        if (items == null || items.size() == 0) {
            return null;
        }
        final LinkedHashSet<Object> objs = new LinkedHashSet<Object>(items.size());
        final Iterator<TypeData> iter = items.iterator();
        for (int i = 0, s = items.size(); i < s; ++i) {
            objs.add(iter.next().getObject());
        }
        return objs;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.SET;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.setCompareTo(this, w);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TypeData) {
            final TypeData w = (TypeData) obj;
            final int size = items != null ? items.size() : 0;
            if (size == 0) {
                return w.isNull();
            } else if (w.getType() == Types.SET) {
                final SetData wdata = (SetData) w;
                return items.size() == wdata.items.size() && items.containsAll(wdata.items);
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (items==null || items.size() == 0) {
            out.writeBoolean(true);
        } else {
            final int size = items.size();
            final Iterator<TypeData> iter = items.iterator();
            TypeData item;
            
            out.writeBoolean(false);
            out.writeInt(size);
            for (int i = 0; i < size; ++i) {
                item = iter.next();
                out.writeByte(item.getType());
                item.writeExternal(out);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        if (in.readBoolean()) {
            items = null;
        } else {
            readItems(in);
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static SetData readData(final ObjectInput in) throws IOException {
        if (in.readBoolean()) {
            return SetData.NULL;
        } else {
            final SetData sd = new SetData();
            sd.readItems(in);
            return sd;
        }
    }

    private void readItems(final ObjectInput in) throws IOException {
        final int size = in.readInt();
        this.items = new LinkedHashSet<TypeData>(size);
        
        TypeData itemData;
        for (int i = 0; i < size; ++i) {
            itemData = XTypeDataUtils.getTypeDataInstance(in.readByte());
            itemData.readExternal(in);
            if (!TypeUtils.isNonPrimitiveTypeData(itemData)) {
                items.add(itemData);
            } else {
                throw new IllegalArgumentException("DATA_SET_PRIMITIVE_TYPE_ONLY");
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public int size() {
        return items == null ? 0 : items.size();
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<TypeData> iterator() {
        return items == null ? new HashSet<TypeData>(0).iterator() : items.iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData first() {
        return items == null ? NullData.INSTANCE : items.iterator().next();
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_INT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_LONG_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_FLOAT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_DOUBLE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_DECIMAL_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_BOOLEAN_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_DATE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_TIME_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_TIMESTAMP_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_TIMEWITHTIMEZONE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_TIMESTAMPWITHTIMEZONE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() throws EngineDataException {
        throw new DataConversionException ("DATA_SET_TO_INSTANT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Object> toList() {
        if (items == null || items.size() == 0) {
            return null;
        }
        
        final int size = items.size();
        final ArrayList<Object> list = new ArrayList<>(size);
        final Iterator<TypeData> iter = items.iterator();
        for (int i = 0; i < size; ++i) {
            list.add(iter.next().getObject());
        }
        return list;
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Object> toSet() {
        if (items == null || items.size() == 0) {
            return null;
        }
        
        final int size = items.size();
        final LinkedHashSet<Object> set = new LinkedHashSet<>(size);
        final Iterator<TypeData> iter = items.iterator();
        for (int i = 0; i < size; ++i) {
            set.add(iter.next().getObject());
        }
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, Object> toMap() {
        if (items == null || items.size() == 0) {
            return null;
        }
        
        final int size = items.size();
        final LinkedHashMap<Integer, Object> map = new LinkedHashMap<>(size);
        final Iterator<TypeData> iter = items.iterator();
        for (int i = 0; i < size; ++i) {
            map.put(i, iter.next().getObject());
        }
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return items == null ? null : toString().getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        if (items == null) {
            return "";
        } else if (items.size() == 0) {
            return "[]";
        }
        
        final StringBuilder sb = new StringBuilder(512);
        sb.append('[');

        final Iterator<TypeData> iter = items.iterator();
        TypeData item;
        
        for (int i = 0, s = items.size(); i < s; ++i) {
            item = iter.next();
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
            sb.setCharAt(sb.length() - 1, ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNull() {
        return items == null || items.size() == 0;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return items == null || items.size() == 0;
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
    
    public SetData valueOf(final LinkedHashSet<TypeData> items) {
        return items != null && items.size() > 0 ? new SetData(items) : SetData.NULL;
    }
}
