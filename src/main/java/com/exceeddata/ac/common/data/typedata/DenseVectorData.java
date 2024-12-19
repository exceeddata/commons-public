package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.exception.EngineDataException;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.DataConversionException;
import com.exceeddata.ac.common.util.XTypeDataUtils;

/**
 * A data class for dense vector
 *
 */
public final class DenseVectorData implements TypeData {
    private static final long serialVersionUID = 1L;
    protected static final double[] DEFAULTCAPACITY_EMPTY_DATAS = {};
    public static final DenseVectorData NULL = new DenseVectorData();
    
    protected double[] items = null;
    protected int vsize = 0;
    
    public DenseVectorData() {
        items = DEFAULTCAPACITY_EMPTY_DATAS;
    }
    
    /**
     * Construct a <code>DenseVectorData</code>.
     * 
     * @param dbls the list of doubles
     */
    public DenseVectorData(final double[] dbls) {
        if (dbls !=null) {
            vsize = dbls.length;
            items = dbls;
        } else {
            items = DEFAULTCAPACITY_EMPTY_DATAS;
        }
    }
    
    /**
     * Construct a <code>DenseVectorData</code>.
     * 
     * @param dbls the list of doubles
     */
    public DenseVectorData(final Double[] dbls) {
        if (dbls !=null) {
            vsize = dbls.length;
            items = new double[vsize];
            
            Double dbl;
            for (int i = 0; i < vsize; ++i) {
                dbl = dbls[i];
                items[i] = (dbl != null ? dbl.doubleValue() : 0d);
            }
        } else {
            items = DEFAULTCAPACITY_EMPTY_DATAS;
        }
    }
    
    
    /**
     * Construct a <code>DenseVectorData</code> with a String
     * 
     * @param value the value in string
     */
    public DenseVectorData(final String value) {
        final List<Object> dbls = XTypeDataUtils.jsonToListObject(value);
        if ((vsize = dbls != null ? dbls.size() : 0) > 0) {
            items = new double[vsize];
            
            Object dbl;
            for (int i = 0; i < vsize; ++i) {
                dbl = dbls.get(i);
                try {
                    items[i] = dbl != null ? Double.valueOf(dbl.toString()) : 0d;
                } catch (NumberFormatException e) {
                    items[i] = 0d;
                }
            }
        } else {
            items = DEFAULTCAPACITY_EMPTY_DATAS;
        }
    }
    
    
    /**
     * Construct a <code>DenseVectorData</code> with a DenseVectorData
     * 
     * @param data the DenseVectorData
     */
    public DenseVectorData(final DenseVectorData data) {
        if (data != null && data.vsize > 0) {
            vsize = data.vsize;
            items = new double[vsize = data.vsize];
            System.arraycopy(data.items, 0, this.items, 0, data.vsize);
        } else {
            items = DEFAULTCAPACITY_EMPTY_DATAS;
        }
    }
    
    public DoubleData itemAt(final int position) {
        return DoubleData.nonNullValueOf(items[position]);
    }
    
    /** {@inheritDoc} */
    @Override
    public DenseVectorData clone() {
        return new DenseVectorData(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public DenseVectorData copy() {
        return new DenseVectorData(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public DoubleData get(final TypeData accessor) {
        if (vsize == 0) {
            return DoubleData.NULL;
        }
        
        try {
            final Integer position = accessor.toInt();
            if (position == null || position < 0 || position >= vsize) {
                return DoubleData.NULL;
            } else {
                return DoubleData.nonNullValueOf(items[position]);
            }
        } catch (EngineException e) {
            return DoubleData.NULL;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public double[] getObject() {
        return getDoubles();
    }
    
    public DoubleData get(final int position) {
        return vsize == 0 || position < 0 || position >= vsize ? DoubleData.NULL : DoubleData.nonNullValueOf(items[position]);
    }
    
    public List<Double> getList() {
        return vsize == 0 ? null : DoubleStream.of(items).boxed().collect(Collectors.toList());
    }
    
    public double[] getDoubles() {
        if (vsize == 0) {
            return null;
        }
        final double[] dbls = new double[vsize];
        System.arraycopy(items, 0, dbls, 0, vsize);
        return dbls;
    }
    
    /**
     * Return doubles list
     * 
     * @return double list
     */
    public List<Double> getDoubleList() {
        if (vsize == 0) {
            return null;
        }
        final ArrayList<Double> dbls = new ArrayList<>(vsize);
        for (int i = 0; i < vsize; ++i) {
            dbls.add(items[i]);
        }
        return dbls;
    }

    /**
     * Get unsafe internal access to vector doubles. If it is
     * not identical to length then it will return copy.
     * 
     * @return doubles
     */
    public double[] getUnsafeDoubles() {
        if (vsize == items.length) {
            return items;
        }
        final double[] dbls = new double[vsize];
        System.arraycopy(items, 0, dbls, 0, vsize);
        return dbls;
    }

    public double[] copyDoubles(final double[] dest) {
        if (vsize == 0) {
            return dest == null ? new double[0] : dest;
        }
        final double[] dbls = (dest == null || vsize > dest.length) ? new double[vsize] : dest;
        System.arraycopy(items, 0, dbls, 0, vsize);
        return dbls;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.DENSEVECTOR;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.denseVectorDataCompareTo(this, w);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TypeData) {
            final TypeData w = (TypeData) obj;
            if (vsize == 0) {
                return w.isNull();
            } else if (w.getType() == Types.DENSEVECTOR) {
                final DenseVectorData wdata = (DenseVectorData) w;
                if (vsize == wdata.vsize) {
                    for (int i = 0; i < vsize; ++i) {
                        if (Double.compare(items[i], wdata.items[i]) != 0) {
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
        if (vsize == 0) {
            out.writeBoolean(true);
        } else {
            out.writeBoolean(false);
            out.writeInt(vsize);
            for (int i = 0; i < vsize; ++i) {
                out.writeDouble(items[i]);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        if (in.readBoolean()) {
            items = DEFAULTCAPACITY_EMPTY_DATAS;
            vsize = 0;
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
    public static DenseVectorData readData(final DataInput in) throws IOException {
        if (in.readBoolean()) {
            return DenseVectorData.NULL;
        } else {
            final DenseVectorData dv = new DenseVectorData();
            dv.readItems(in);
            return dv;
        }
    }
    
    private void readItems(final DataInput in) throws IOException {
        vsize = in.readInt();
        items = new double[vsize];
        for (int i = 0; i < vsize; ++i) {
            items[i] = in.readDouble();
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public int size() {
        return vsize;
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<DoubleData> iterator() {
        return new DenseVectorDataIterator(this);
    }
    
    public Iterator<Double> vectorIterator() {
        return new DenseVectorIterator(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData first() {
        return vsize != 0 ? DoubleData.nonNullValueOf(items[0]) : DoubleData.NULL;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_INT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_LONG_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_FLOAT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_DOUBLE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_DECIMAL_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_BOOLEAN_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_DATE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_TIME_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_TIMESTAMP_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_TIMEWITHTIMEZONE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_TIMESTAMPWITHTIMEZONE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() throws EngineDataException {
        throw new DataConversionException ("DATA_DENSEVECTOR_TO_INSTANT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Double> toList() {
        return vsize == 0 ? null : DoubleStream.of(items).boxed().collect(Collectors.toList());
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Double> toSet() {
        return vsize == 0 ? null : DoubleStream.of(items).boxed().collect(Collectors.toSet());
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, Double> toMap() {
        if (vsize == 0) {
            return null;
        }
        final LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(vsize);
        for (int i = 0; i < vsize; ++i) {
            map.put(i, items[i]);
        }
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return vsize == 0 ? null : toString().getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        if (vsize == 0) {
            return "";
        }
        
        final StringBuilder sb = new StringBuilder(512);
        sb.append('[');
        
        double item;
        for (int i = 0; i < vsize; ++i) {
            item = items[i];
            sb.append(item != (int) item ? String.valueOf(item) : String.valueOf((int) item)).append(',');
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
        return vsize == 0;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return vsize == 0;
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
    
    /**
     * Convert vector to array.
     * 
     * @return double array
     */
    public double[] toArray() {
        if (vsize == 0) {
            return new double[0];
        }
        
        final double[] dbls = new double[vsize];
        System.arraycopy(items, 0, dbls, 0, vsize);
        return dbls;
    }
    
    /**
     * Convert vector to array with target capacity.
     * 
     * @return double array
     */
    public double[] toArray(final int capacity) {
        final double[] arr = new double[capacity];
        Arrays.fill(arr, 0d);
        
        final int len = Math.min(vsize == 0 ? 0 : vsize, capacity);
        System.arraycopy(items, 0, arr, 0, len);
        return arr;
    }
    
    public static final DenseVectorData valueOf(final double[] value) {
        return value == null || value.length == 0 ? DenseVectorData.NULL : new DenseVectorData(value);
    }
    
    protected static final class DenseVectorDataIterator implements Iterator<DoubleData>, Serializable {
        private static final long serialVersionUID = 1L;
        
        private final double[] items;
        private final int vsize;
        private int index = 0;
        
        protected DenseVectorDataIterator(final DenseVectorData data) {
            items = data.items;
            vsize = data.vsize;
        }
        
        protected DenseVectorDataIterator(final double[] items, final int vsize, final int index) {
            this.items = items;
            this.vsize = vsize;
            this.index = index;
        }
        
        @Override
        public DenseVectorDataIterator clone() {
            return new DenseVectorDataIterator(items, vsize, index);
        }

        @Override
        public boolean hasNext() {
            return index < vsize;
        }

        @Override
        public DoubleData next() {
            return DoubleData.nonNullValueOf(items[index++]);
        }
    }
    
    protected static final class DenseVectorIterator implements Iterator<Double>, Serializable {
        private static final long serialVersionUID = 1L;
        
        private final double[] items;
        private final int vsize;
        private int index = 0;
        
        protected DenseVectorIterator(final DenseVectorData data) {
            items = data.items;
            vsize = data.vsize;
        }
        
        protected DenseVectorIterator(final double[] items, final int vsize, final int index) {
            this.items = items;
            this.vsize = vsize;
            this.index = index;
        }
        
        @Override
        public DenseVectorDataIterator clone() {
            return new DenseVectorDataIterator(items, vsize, index);
        }

        @Override
        public boolean hasNext() {
            return index < vsize;
        }

        @Override
        public Double next() {
            return items[index++];
        }
    }
}
