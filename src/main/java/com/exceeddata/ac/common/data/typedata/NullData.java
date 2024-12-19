package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.exceeddata.ac.common.data.type.Types;
/**
 * A {@link PrimitiveData} class for Null.
 *
 */
public final class NullData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final NullData INSTANCE = new NullData();
    public static final NullData NA = new NullData(); //special non-applicable null, similar to NaN
    
    public NullData() {
    }
    
    /** {@inheritDoc} */
    @Override
    public NullData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public NullData copy() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.NULL;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return w == null ? 1 : w.isNull() ? 0 : -1;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TypeData && ((TypeData) obj).isNull();
    }

    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) {
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) {
    }

    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static NullData readData(final DataInput in) {
        return NullData.INSTANCE;
    }
    
    /** {@inheritDoc} */
    @Override
    public NullData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Object getObject() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<NullData> iterator() {
        return Arrays.asList(new NullData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData first() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public List<? extends Object> toList() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<? extends Object> toSet() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<? extends Object, ? extends Object> toMap() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "";
    }
    
    /** {@inheritDoc} */
    @Override
    public int size() {
        return 1;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNull() {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return true;
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
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return TypeUtils.NULL_HASH;
    }
}
