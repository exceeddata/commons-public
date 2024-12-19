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
import java.util.Set;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.exception.EngineDataException;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.DataConversionException;
import com.exceeddata.ac.common.util.XTypeDataUtils;

/**
 * A data class for List of objects.
 *
 */
public final class ListData implements TypeData {
    private static final long serialVersionUID = 1L;
    public static final ListData NULL = new ListData();
    
    protected ArrayList<TypeData> items;
    
    public ListData() {
        this.items = null;
    }
    
    public ListData(final int capacity) {
        this.items = new ArrayList<>(capacity);
    }
    
    public ListData(final ArrayList<TypeData> items) {
        this.items = items;
    }
    
    /**
     * Construct a <code>ListData</code> with a String
     * 
     * @param value the value in string
     */
    public ListData(String value) {
        this.items = XTypeDataUtils.jsonToTypeDataList(value);
    }
    
    /**
     * Construct a <code>ListData</code> with a ListData
     * 
     * @param data the ListData
     */
    public ListData(final ListData data) {
        this.items = (data != null && data.items != null && data.items.size() > 0) ? new ArrayList<TypeData>(data.items) : null;
    }
    
    /**
     * Get a copy of the items.
     * 
     * @return items
     */
    public ArrayList<TypeData> getItems() {
        return items != null ? new ArrayList<TypeData>(items) : null;
    }
    
    /**
     * Get the item at the index.  NullData if outside of range.
     * 
     * @param index the index
     * @return TypeData
     */
    public TypeData itemAt(final int position) {
        return get(position);
    }
    
    /** {@inheritDoc} */
    @Override
    public Object clone() {
        return new ListData(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public ListData copy() {
        return new ListData(this);
    }
    
    /**
     * Create a shallow copy.
     * 
     * @return ListData
     */
    public ListData shallowCopy() {
        return items == null ? new ListData() : new ListData(new ArrayList<TypeData>(items));
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Object> getObject() {
        if (items == null || items.size() == 0) {
            return null;
        }
        
        final int size = items.size();
        final ArrayList<Object> objs = new ArrayList<Object>(size);
        for (int i = 0; i < size; ++i) {
            objs.add(items.get(i).getObject());
        }
        return objs;
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData get(final TypeData accessor) {
        if (items == null || items.size() == 0) {
            return NullData.INSTANCE;
        }
        
        try {
            final Integer position = accessor.toInt();
            if (position == null || position < 0 || position >= items.size()) {
                return NullData.INSTANCE;
            } else {
                return items.get(position);
            }
        } catch (EngineException e) {
            return NullData.INSTANCE;
        }
    }
    
    protected ArrayList<TypeData> getUnsafeItems() {
        return items;
    }
    
    /**
     * Get the TypeData at a given position.
     * 
     * @param position the position
     * @return TypeData
     */
    public TypeData get(final int position) {
       return position >= 0 && position < items.size() ? items.get(position) : NullData.INSTANCE;
    }
    
    /**
     * Get the list of Objects.
     * 
     * @return List of Object
     */
    public List<Object> getList() {
        if (items == null || items.size() == 0) {
            return null;
        }
        
        final int size = items.size();
        final ArrayList<Object> objectList = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            objectList.add(items.get(i).getObject());
        }
        return objectList;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.LIST;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.listCompareTo(this, w);
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
            } else if (w.getType() == Types.LIST) {
                final ListData data = (ListData) w;
                final int wsize = data.items != null ? data.items.size() : 0;
                if (size == wsize) {
                    final Iterator<TypeData> iter = items.iterator(), witer = data.items.iterator();
                    while (iter.hasNext()) {
                        if (!iter.next().equals(witer.next())) {
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
        final int size = items != null ? items.size() : 0;
        if (size == 0) {
            out.writeBoolean(true);
        } else {
            out.writeBoolean(false);
            out.writeInt(size);
            for (final TypeData itemData : items) {
                out.writeByte(itemData.getType());
                itemData.writeExternal(out);
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
    public static ListData readData(final ObjectInput in) throws IOException {
        if (in.readBoolean()) {
            return ListData.NULL;
        } else {
            final ListData ld = new ListData();
            ld.readItems(in);
            return ld;
        }
    }
    
    private void readItems(final ObjectInput in) throws IOException {
        final int len = in.readInt();
        this.items = new ArrayList<TypeData>(len);
        
        for (int i = 0; i < len; ++i) {
            items.add(XTypeDataUtils.readTypeData(in, in.readByte()));
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
        return items == null ? new ArrayList<TypeData>(0).iterator() : items.iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData first() {
        return items.size() == 0 ? NullData.INSTANCE : items.get(0);
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_INT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_LONG_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_FLOAT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_DOUBLE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_DECIMAL_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_BOOLEAN_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_DATE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_TIME_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_TIMESTAMP_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_TIMEWITHTIMEZONE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_TIMESTAMPWITHTIMEZONE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() throws EngineDataException {
        throw new DataConversionException ("DATA_LIST_TO_INSTANT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Object> toList() {
        if (items == null || items.size() == 0) {
            return null;
        }
        
        final int size = items.size();
        final ArrayList<Object> list = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            list.add(items.get(i).getObject());
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
        for (int i = 0; i < size; ++i) {
            set.add(items.get(i).getObject());
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
        for (int i = 0; i < size; ++i) {
            map.put(i, items.get(i).getObject());
        }
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return items == null || items.size() == 0 ? null : toString().getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        if (items == null || items.size() == 0) {
            return "";
        }
        
        final StringBuilder sb = new StringBuilder(512);
        sb.append('[');

        TypeData item;
        for (int i = 0, s = items.size(); i < s; ++i) {
            item = items.get(i);
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
    
    public static ListData valueOf(final ArrayList<TypeData> items) {
        return items != null && items.size() > 0 ? new ListData(items) : ListData.NULL;
    }
}
