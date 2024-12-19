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
import java.util.Collections;
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
 * A data class for sparse vector
 *
 */
public final class SparseVectorData implements TypeData {
    private static final long serialVersionUID = 1L;
    protected static final int[] DEFAULTCAPACITY_EMPTY_INDICES = {};
    protected static final double[] DEFAULTCAPACITY_EMPTY_NONZEROS = {};
    public static final SparseVectorData NULL = new SparseVectorData();
    public static final SparseVectorData ZERO = new SparseVectorData(1, new int[0], new double[0]);
    public static final SparseVectorData ONE = new SparseVectorData(1, new int[] {0}, new double[] {1d});
    
    protected int vsize = 0;
    protected int nzlength = 0;  //nonzero length
    protected int[] indices = null;
    protected double[] nonzeros = null;
    
    public SparseVectorData() {
        indices = DEFAULTCAPACITY_EMPTY_INDICES;
        nonzeros = DEFAULTCAPACITY_EMPTY_NONZEROS;
    }
    
    /**
     * Construct a <code>SparseVectorData</code>.
     * 
     * @param dbls the array of doubles
     */
    public SparseVectorData (final double[] dbls) {
        if (dbls == null || dbls.length == 0) {
            indices = DEFAULTCAPACITY_EMPTY_INDICES;
            nonzeros = DEFAULTCAPACITY_EMPTY_NONZEROS;
        } else {
            vsize = dbls.length;
            indices = new int[Math.min(vsize, 32)];
            nonzeros = new double[indices.length];
            
            for (int i = 0; i < vsize; ++i) {
                if (Double.compare(dbls[i], 0d) != 0) {
                    if (nzlength == indices.length) {
                        final int newCapacity = Math.min(vsize, (int) (nzlength * 1.5));
                        ensureNonzeroCapacity(newCapacity);
                    }
                    indices[nzlength] = i;
                    nonzeros[nzlength++] = dbls[i];
                }
            }
        }
    }
    
    protected void ensureNonzeroCapacity (final int newCapacity) {
        final int target = newCapacity != 0 ? newCapacity : 32;
        final int[] newIndices = new int[target];
        final double[] newNonzeros = new double[target];
        System.arraycopy(indices, 0, newIndices, 0, indices.length);
        System.arraycopy(nonzeros, 0, newNonzeros, 0, nonzeros.length);
        indices = newIndices;
        nonzeros = newNonzeros;
    }
    
    /**
     * Construct a <code>SparseVectorData</code>.
     * 
     * @param vsize the size
     * @param indices the indices of non-zero values
     * @param nonzeros the list of non-zero values
     */
    public SparseVectorData (final int vsize, final int[] indices, final double[] nonzeros) {
        this.vsize = vsize;
        this.indices = indices;
        this.nonzeros = nonzeros;
        this.nzlength = indices.length;
    }
    
    /**
     * Construct a <code>SparseVectorData</code>.
     * 
     * @param value the value in string
     */
   public SparseVectorData(final String value) {
       final List<TypeData> datas = XTypeDataUtils.jsonToTypeDataList(value);
       if (datas == null || datas.size() == 0) {
           indices = DEFAULTCAPACITY_EMPTY_INDICES;
           nonzeros = DEFAULTCAPACITY_EMPTY_NONZEROS;
       } else {
           vsize = datas.size();
           indices = new int[Math.min(vsize, 32)];
           nonzeros = new double[indices.length];
           
           TypeData data;
           double dbl;
           for (int i = 0; i < vsize; ++i) {
               if (!(data = datas.get(i)).isEmpty()) {
                   try {
                       dbl = Double.valueOf(data.toString());
                       if (Double.compare(dbl, 0d) != 0) {
                           if (nzlength == indices.length) {
                               final int newCapacity = Math.min(vsize, (int) (nzlength * 1.5));
                               ensureNonzeroCapacity(newCapacity);
                           }
                           indices[nzlength] = i;
                           nonzeros[nzlength++] = dbl;
                       }
                   } catch (NumberFormatException e) {}
               }
           }
       }
    }
    
    /**
     * Construct a <code>SparseVectorData</code> with a SparseVectorData
     * 
     * @param data the SparseVectorData
     */
    public SparseVectorData (final SparseVectorData data) {
        if (data == null || data.vsize == 0) {
            indices = DEFAULTCAPACITY_EMPTY_INDICES;
            nonzeros = DEFAULTCAPACITY_EMPTY_NONZEROS;
        } else if (data.nzlength == 0) {
            vsize = data.vsize;
            indices = DEFAULTCAPACITY_EMPTY_INDICES;
            nonzeros = DEFAULTCAPACITY_EMPTY_NONZEROS;
        } else {
            vsize = data.vsize;
            nzlength = data.nzlength;
            indices = new int[nzlength];
            nonzeros = new double[nzlength];
            System.arraycopy(data.indices, 0, indices, 0, nzlength);
            System.arraycopy(data.nonzeros, 0, nonzeros, 0, nzlength);
        }
        
    }
    
    public DoubleData itemAt(final int index) {
        return get(index);
    }
    
    /** {@inheritDoc} */
    @Override
    public SparseVectorData clone() {
        return new SparseVectorData(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public SparseVectorData copy() {
        return new SparseVectorData(this);
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
                for (int i = 0; i < nzlength; ++i) {
                    if (position <= indices[i]) {
                        return position < indices[i] ? DoubleData.ZERO : DoubleData.nonNullValueOf(nonzeros[i]);
                    }
                }
                return DoubleData.ZERO;
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
    
    public List<Double> getList() {
        if (vsize == 0) {
            return null;
        }
        
        final ArrayList<Double> dbls = new ArrayList<>(Collections.nCopies(vsize, 0D));
        for (int i = 0; i < nzlength; ++i) {
            dbls.set(indices[i], nonzeros[i]);
        }
        return dbls;
    }
    
    /**
     * Get a copy of indices.
     * 
     * @return indices array 
     */
    public int[] getIndices() {
        if (nzlength == 0) {
            return new int[] {};
        }
        final int[] icopy = new int[nzlength];
        System.arraycopy(indices, 0, icopy, 0, nzlength);
        return icopy;
    }
    
    /**
     * Get unsafe access to indices.
     * 
     * @return indices array 
     */
    public int[] getUnsafeIndices() {
        if (nzlength == indices.length) {
            return indices;
        }
        final int[] icopy = new int[nzlength];
        System.arraycopy(indices, 0, icopy, 0, nzlength);
        return icopy;
    }
    
    /**
     * Get a copy of nonzeros.
     * 
     * @return indices array 
     */
    public double[] getNonzeros() {
        if (nzlength == 0) {
            return new double[] {};
        }
        final double[] ncopy = new double[nzlength];
        System.arraycopy(nonzeros, 0, ncopy, 0, nzlength);
        return ncopy;
    }
    
    /**
     * Get nonzero length.
     * 
     * @return length
     */
    public int getNonzeroLength() {
        return nzlength;
    }
    
    /**
     * Get unsafe access to nonzeros array.  If length is not identical
     * then a copy will be returned.
     * 
     * @return nonzeros
     */
    public double[] getUnsafeNonzeros() {
        if (nzlength == nonzeros.length) {
            return nonzeros;
        }
        final double[] ncopy = new double[nzlength];
        System.arraycopy(nonzeros, 0, ncopy, 0, nzlength);
        return ncopy;
    }
    
    /**
     * Get a copy of nonzeros.
     * 
     * @return indices array 
     */
    public List<DoubleData> getNonzeroDatas() {
        if (nzlength == 0) {
            return new ArrayList<DoubleData>(0);
        }
        final ArrayList<DoubleData> nz = new ArrayList<>(nzlength);
        for (int i = 0; i < nzlength; ++i) {
            nz.add(DoubleData.nonNullValueOf(nonzeros[i]));
        }
        return nz;
    }
    
    /**
     * check whether sparse vector has zero.
     * 
     * @return true or false
     */
    public boolean haszeros() {
        return vsize > nzlength;
    }
    
    public DoubleData get(final int position) {
        if (vsize == 0 || position < 0 || position >= vsize) {
            return DoubleData.NULL;
        }
        for (int i = 0; i < nzlength; ++i) {
            if (position <= indices[i]) {
                return position < indices[i] ? DoubleData.ZERO : DoubleData.nonNullValueOf(nonzeros[i]);
            }
        }
        return DoubleData.ZERO;
    }
    
    public double[] getDoubles() {
        if (vsize == 0) {
            return null;
        } 
        
        final double[] dbls = new double[vsize];
        for (int i = 0; i < nzlength; ++i) {
            dbls[indices[i]] = nonzeros[i];
        }
        return dbls;
    }
    
    public double[] copyDoubles(final double[] dest) {
        final double[] dbls = (dest == null || vsize > dest.length) ? new double[vsize] : dest;
        if (vsize == 0 || nzlength == 0) {
            return dbls;
        }

        for (int i = 0; i < nzlength; ++i) {
            dbls[indices[i]] = nonzeros[i];
        }
        return dbls;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.SPARSEVECTOR;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.sparseVectorCompareTo(this, w);
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
            } else if (w.getType() == Types.SPARSEVECTOR) {
                final SparseVectorData data = (SparseVectorData) w;
                if (vsize == data.vsize && nzlength == data.nzlength) {
                    for (int i = 0; i < nzlength ; ++i) {
                        if (indices[i] != data.indices[i] || Double.compare(nonzeros[i], data.nonzeros[i]) != 0) {
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
            out.writeInt(nzlength);
            if (nzlength > 0) {
                for (int i = 0; i < nzlength; ++i) {
                    out.writeInt(indices[i]);
                }
                for (int i = 0; i < nzlength; ++i) {
                    out.writeDouble(nonzeros[i]);
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(final ObjectInput in) throws IOException {
        if (in.readBoolean()) {
            vsize = 0;
            nzlength = 0;
            indices = DEFAULTCAPACITY_EMPTY_INDICES;
            nonzeros = DEFAULTCAPACITY_EMPTY_NONZEROS;
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
    public static SparseVectorData readData(final DataInput in) throws IOException {
        if (in.readBoolean()) {
            return SparseVectorData.NULL;
        } else {
            final SparseVectorData sv = new SparseVectorData();
            sv.readItems(in);
            return sv;
        }
    }
    
    private void readItems(final DataInput in) throws IOException {
        vsize = in.readInt();
        nzlength = in.readInt();
        if (nzlength > 0) {
            indices = new int[nzlength];
            nonzeros = new double[nzlength];
            
            for (int i = 0; i < nzlength; ++i) {
                indices[i] = in.readInt();
            }
            for (int i = 0; i < nzlength; ++i) {
                nonzeros[i] = in.readDouble();
            }
        } else {
            indices = DEFAULTCAPACITY_EMPTY_INDICES;
            nonzeros = DEFAULTCAPACITY_EMPTY_NONZEROS;
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
        return new SparseVectorDataIterator(this);
    }
    
    public Iterator<Double> vectorIterator() {
        return new SparseVectorIterator(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData first() {
        return vsize == 0 ? DoubleData.NULL : nzlength == 0 || indices[0] != 0 ? DoubleData.ZERO : DoubleData.nonNullValueOf(nonzeros[0]);
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_INT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_LONG_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_FLOAT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_DOUBLE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_DECIMAL_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_BOOLEAN_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_DATE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_TIME_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_TIMESTAMP_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_TIMEWITHTIMEZONE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_TIMESTAMPWITHTIMEZONE_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() throws EngineDataException {
        throw new DataConversionException ("DATA_SPARSEVECTOR_TO_INSTANT_CONVERSION_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Double> toList() {
        return getList();
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<Double> toSet() {
        if (vsize == 0) {
            return null;
        }
        
        final LinkedHashSet<Double> set = new LinkedHashSet<>(nzlength + 1);
        if (nzlength != vsize) {
            set.add(0D);
        }
        for (int i = 0; i < nzlength; ++i) {
            set.add(nonzeros[i]);
        }
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, Double> toMap() {
        if (vsize == 0) {
            return null;
        }

        final LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(vsize);
        if (nzlength == 0) {
            for (int i = 0; i < vsize; ++i) {
                map.put(i, 0D);
            }
            return map;
        }
        
        int index = 0, nzindex = 0, nz = indices[nzindex];
            
        while (true) {
            if (index++ >= nz) {
                map.put(index - 1, nonzeros[nzindex]);
                if (nzindex + 1 < nzlength) {
                    nz = indices[++nzindex];
                } else {
                    break;
                }
            } else {
                map.put(index -1, 0D);
            }
        }
        for (int i = index; i < vsize; ++i) {
            map.put(i, 0D);
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
        
        if (nzlength == 0) {
            for (int i = 0; i < vsize; ++i) {
                sb.append("0,");
            }
        } else {
            int index = 0, nzindex = 0, nz = indices[0];
            
            while (true) {
                if (index++ >= nz) {
                    sb.append(String.valueOf(nonzeros[nzindex])).append(',');
                    if (nzindex + 1 < nzlength) {
                        nz = indices[++nzindex];
                    } else {
                        break;
                    }
                } else {
                    sb.append("0,");
                }
            }
            for (int i = index; i < vsize; ++i) {
                sb.append("0,");
            }
        }
        
        sb.setCharAt(sb.length() - 1, ']');
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
            return null;
        } 
        
        final double[] dbls = new double[vsize];
        for (int i = 0; i < nzlength; ++i) {
            dbls[indices[i]] = nonzeros[i];
        }
        return dbls;
    }
    
    /**
     * Convert vector to array with target capacity.
     * 
     * @return double array
     */
    public double[] toArray(final int capacity) {
        final double[] arr = new double[capacity];
        if (vsize == 0) {
            return arr;
        } 
        
        for (int i = 0; i < nzlength; ++i) {
            if (indices[i] < capacity) {
                arr[indices[i]] = nonzeros[i];
            } else {
                break;
            }
        }
        return arr;
    }
    
    public static final SparseVectorData valueOf(final double[] value) {
        return value == null || value.length == 0 ? SparseVectorData.NULL : new SparseVectorData(value);
    }
    
    protected static final class SparseVectorDataIterator implements Iterator<DoubleData>, Serializable {
        private static final long serialVersionUID = 1L;
        
        private final int[] indices;
        private final double[] nonzeros;
        private final int vsize;
        private int nzlength = 0;
        private int index = 0;
        private int nzindex = 0;
        
        private int nz = 0;
        private boolean allzeros = false;
        
        protected SparseVectorDataIterator(final SparseVectorData data) {
            indices = data.indices;
            nonzeros = data.nonzeros;
            vsize = data.vsize;
            nzlength = data.nzlength;
            allzeros = nzlength == 0;
            if (!allzeros) {
                nz = indices[0];
            }
        }
        
        protected SparseVectorDataIterator(
                final int[] indices, 
                final double[] nonzeros, 
                final int vsize, 
                final int nzlength,
                final int index,
                final int nzindex) {
            this.indices = indices;
            this.nonzeros = nonzeros;
            this.vsize = vsize;
            this.nzlength = nzlength;
            this.index = index;
            this.allzeros = this.nzlength == 0;
            this.nzindex = nzindex;
            if (!this.allzeros) {
                this.nz = this.indices[this.nzindex];
            }
        }
        
        @Override
        public SparseVectorDataIterator clone() {
            return new SparseVectorDataIterator(indices, nonzeros, vsize, nzlength, index, nzindex);
        }

        @Override
        public boolean hasNext() {
            return index < vsize;
        }

        @Override
        public DoubleData next() {
            if (allzeros) {
                ++index;
                return DoubleData.ZERO;
            }
            if (index++ == nz) {
                final double val = nonzeros[nzindex];
                if (nzindex + 1 < nzlength) {
                    nz = indices[++nzindex];
                } else {
                    allzeros = true;
                }
                return DoubleData.nonNullValueOf(val);
            } else {
                return DoubleData.ZERO;
            }
        }
    }
    
    protected static final class SparseVectorIterator implements Iterator<Double>, Serializable {
        private static final long serialVersionUID = 1L;
        
        private final int[] indices;
        private final double[] nonzeros;
        private final int vsize;
        private int nzlength = 0;
        private int index = 0;
        private int nzindex = 0;
        
        private int nz = 0;
        private boolean allzeros = false;
        
        protected SparseVectorIterator(final SparseVectorData data) {
            indices = data.indices;
            nonzeros = data.nonzeros;
            vsize = data.vsize;
            nzlength = data.nzlength;
            allzeros = nzlength == 0;
            if (!allzeros) {
                nz = indices[0];
            }
        }
        
        protected SparseVectorIterator(
                final int[] indices, 
                final double[] nonzeros, 
                final int vsize, 
                final int nzlength,
                final int index,
                final int nzindex) {
            this.indices = indices;
            this.nonzeros = nonzeros;
            this.vsize = vsize;
            this.nzlength = nzlength;
            this.index = index;
            this.allzeros = this.nzlength == 0;
            this.nzindex = nzindex;
            if (!this.allzeros) {
                this.nz = this.indices[nzindex];
            }
        }
        
        @Override
        public SparseVectorIterator clone() {
            return new SparseVectorIterator(indices, nonzeros, vsize, nzlength, index, nzindex);
        }

        @Override
        public boolean hasNext() {
            return index < vsize;
        }

        @Override
        public Double next() {
            if (allzeros) {
                ++index;
                return 0D;
            }
            if (index++ == nz) {
                final double val = nonzeros[nzindex];
                if (nzindex + 1 < nzlength) {
                    nz = indices[++nzindex];
                } else {
                    allzeros = true;
                }
                return val;
            } else {
                return 0D;
            }
        }
    }
}
