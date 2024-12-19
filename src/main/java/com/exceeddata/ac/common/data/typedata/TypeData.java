package com.exceeddata.ac.common.data.typedata;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.exceeddata.ac.common.exception.EngineDataException;

/**
 * An abstract class definition for reading and writing typed data.
 *
 */
public interface TypeData extends 
        Serializable, 
        Externalizable, 
        Comparable<TypeData>,
        Comparator<TypeData> {
    
    /** 
     * Return a copy of itself
     * 
     * @return TypeData
     */
    public TypeData copy();
    
    /**
     * Get the iterator to the collection objects (only for complex types)
     * .
     * @return Iterator
     */
    public Iterator<? extends TypeData> iterator();
    
    /**
     * Get the first data in a collection (or the only data)
     * .
     * @return TypeData
     */
    public TypeData first();
    
    /**
     * Convert to Integer.
     * 
     * @return Integer
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public Integer toInt() throws EngineDataException;
    
    /**
     * Convert to Long.
     * 
     * @return Long
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public Long toLong() throws EngineDataException;
    
    /**
     * Convert to Float.
     * 
     * @return Float
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public Float toFloat() throws EngineDataException;
    
    /**
     * Convert to Double.
     * 
     * @return Double
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public Double toDouble() throws EngineDataException;
    
    /**
     * Convert to BigDecimal.
     * 
     * @return BigDecimal
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public BigDecimal toDecimal() throws EngineDataException;
    
    /**
     * Convert to Boolean.
     * 
     * @return Boolean
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public Boolean toBoolean() throws EngineDataException;
    
    /**
     * Convert to bytes.
     * 
     * @return byte[]
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public byte[] toBytes() throws EngineDataException;
    
    /**
     * Convert to Date.
     * 
     * @return Date
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public java.sql.Date toDate() throws EngineDataException;
    
    /**
     * Convert to Time.
     * 
     * @return Time
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public java.sql.Time toTime() throws EngineDataException;
    
    /**
     * Convert to Timestamp.
     * 
     * @return Timestamp
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public java.sql.Timestamp toTimestamp() throws EngineDataException;
    
    /**
     * Convert to Time with Time Zone.
     * 
     * @return GregorianCalendar
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public GregorianCalendar toTimeWithTimeZone() throws EngineDataException;
    
    /**
     * Convert to Timestamp with Time Zone.
     * 
     * @return GregorianCalendar
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public GregorianCalendar toTimestampWithTimeZone() throws EngineDataException;
    
    /**
     * Convert to Instant.
     * 
     * @return Instant
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public Instant toInstant() throws EngineDataException;
    
    /**
     * Convert to List.
     * 
     * @return List
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public List<? extends Object> toList() throws EngineDataException;
    
    /**
     * Convert to Set.
     * 
     * @return Set
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public Set<? extends Object> toSet() throws EngineDataException;
    
    /**
     * Convert to Map.
     * 
     * @return Map
     * @throws EngineDataException throws an EngineDataException if conversion is not valid
     */
    public Map<? extends Object, ? extends Object> toMap() throws EngineDataException;
    
    /**
     * Get the type of the writable.
     * 
     * @return byte
     */
    public byte getType();
    
    /**
     * Get the number of the entries.
     * 
     * @return int
     */
    
    public int size();
    
    /**
     * Get whether the underlying object is null.
     * 
     * @return true or false
     */
    public boolean isNull();
    
    /**
     * Get whether the underlying object is empty.
     * 
     * @return true or false
     */
    public boolean isEmpty();
    
    /**
     * Check if value is digits.
     * 
     * @return true or false;
     */
    public boolean isDigits();
    
    /**
     * Check if value is number.
     * 
     * @return true or false;
     */
    public boolean isNumber();
    
    /**
     * Returns an object for underlying value, this is for complex nested 
     * 
     * @return Object
     */
    public Object getObject();
    
    /**
     * Return a value defined by its accessor. For primitive types this returns
     * itself; For complex types it returns an sub item.
     * 
     * @param accessor the accessor.
     * @return TypeData
     */
    public abstract TypeData get(final TypeData accessor);
    
    @Override
    public abstract void readExternal(ObjectInput in) throws IOException;
}
