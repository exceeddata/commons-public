package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.exception.EngineDataException;
import com.exceeddata.ac.common.exception.data.DataConversionException;
import com.exceeddata.ac.common.util.XNumberUtils;
import com.exceeddata.ac.common.util.XStringUtils;
import com.exceeddata.ac.common.util.XTemporalUtils;
import com.exceeddata.ac.common.util.binary.BinaryBigEndianUtils;

/**
 * A data class for byte[].
 *
 */
public final class BinaryData implements TypeData {
    private static final long serialVersionUID = 1L;
    private static final byte[] DEFAULT_EMPTY_BYTES = new byte[0];
    private static final byte ZERO = (byte) 0;
    
    public static final BinaryData NULL = new BinaryData();
    public static final BinaryData EMPTY = new BinaryData(DEFAULT_EMPTY_BYTES);
    
    protected byte[] value = null;
    
    public BinaryData() {
    }
    
    /**
     * Construct a <code>BinaryData</code> with byte[]
     * 
     * @param value byte[]
     */
    public BinaryData(final byte[] value) {
        this.value = value;
    }
    
    /**
     * Construct a <code>BinaryData</code> with ByteBuffer
     * 
     * @param value String
     */
    public BinaryData(final ByteBuffer value) {
        if (value != null) {
            this.value = value.array();
        }
    }
    
    /**
     * Construct a <code>BinaryData</code> with String
     * 
     * @param value String
     */
    public BinaryData(final String value) {
        if (value != null) {
            this.value = value.length() == 0 
                            ? DEFAULT_EMPTY_BYTES
                            : value.getBytes(StandardCharsets.UTF_8);
        }
    }
    
    /**
     * Construct a <code>BinaryData</code> with BinaryData
     * 
     * @param data the BinaryData
     */
    public BinaryData(final BinaryData data) {
        if (data != null && data.value != null) {
            System.arraycopy(data.value, 0, this.value = new byte[data.value.length], 0, data.value.length);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Object clone() {
        return new BinaryData(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public BinaryData copy() {
        return new BinaryData(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public BinaryData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] getObject() {
        if (value == null) {
            return null;
        }
        return Arrays.copyOf(value, value.length);
    }
    
    /**
     * Get byte array value.
     * 
     * @return byte[]
     */
    public byte[] getBytes() {
        return value;
    }
    
    /**
     * Get ByteBuffer value.
     * 
     * @return ByteBuffer
     */
    public ByteBuffer getByteBuffer() {
        return (value != null) ? ByteBuffer.wrap(value) : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.BINARY;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        if (this == w) {
            return 0;
        }
        if (w == null) {
            return 1;
        } else if (w.isNull()) {
            return value == null ? 0 : 1;
        } else if (value == null) {
            return -1;
        }
        return DataCompare.binaryCompareTo(this, w);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TypeData) {
            final TypeData anydata = (TypeData) obj;
            return value == null 
                    ? anydata.isNull() 
                    : anydata.getType() == Types.BINARY && !anydata.isNull() && ByteBuffer.wrap(value).compareTo(ByteBuffer.wrap(((BinaryData) anydata).value)) == 0;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (value==null) {
            out.writeByte(XNumberUtils.BYTE_NULL);
            return;
        } else if (value.length == 0) {
            out.writeByte(XNumberUtils.BYTE_ZERO);
            return;
        }
        
        //max byte is 127, max short is 32767
        if (value.length >> 7 == 0) {
            out.writeByte(XNumberUtils.BYTE_ONE); 
            out.writeByte(value.length);
        } else {
            if (value.length >> 15 == 0) {
                out.writeByte(XNumberUtils.BYTE_TWO); 
                out.writeShort(value.length);
            } else {
                out.writeByte(XNumberUtils.BYTE_FOUR); 
                out.writeInt(value.length);
            }
        }
        
        out.write(value, 0, value.length);
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        final int len;
        switch(in.readByte()) {
            case XNumberUtils.BYTE_NULL:     value = null; return;
            case XNumberUtils.BYTE_ZERO:     value = DEFAULT_EMPTY_BYTES; return;
            case XNumberUtils.BYTE_ONE:      len = in.readByte(); break;
            case XNumberUtils.BYTE_TWO:      len = in.readShort(); break;
            default:                        len = in.readInt();
        }
        in.readFully(value = new byte[len], 0, len);
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static BinaryData readData(final DataInput in) throws IOException {
        final int len;
        switch(in.readByte()) {
            case XNumberUtils.BYTE_NULL:     return BinaryData.NULL;
            case XNumberUtils.BYTE_ZERO:     return BinaryData.EMPTY;
            case XNumberUtils.BYTE_ONE:      len = in.readByte(); break;
            case XNumberUtils.BYTE_TWO:      len = in.readShort(); break;
            default:                        len = in.readInt();
        }
        final byte[] b = new byte[len];
        in.readFully(b, 0, len);
        return BinaryData.valueOf(b);
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<BinaryData> iterator() {
        return Arrays.asList(new BinaryData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData first() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() {
        if (value == null || value.length == 0) {
            return null;
        }

        switch(value.length) {
            case 1:  return BinaryBigEndianUtils.makeInt(ZERO, ZERO, ZERO, value[0]);
            case 2:  return BinaryBigEndianUtils.makeInt(ZERO, ZERO, value[0], value[1]);
            case 3:  return BinaryBigEndianUtils.makeInt(ZERO, value[0], value[1], value[2]);
            default: return BinaryBigEndianUtils.makeInt(value[0], value[1], value[2], value[3]);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() {
        if (value == null || value.length == 0) {
            return null;
        }
        
        switch(value.length) {
            case 1:  return BinaryBigEndianUtils.makeLong(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, value[0]);
            case 2:  return BinaryBigEndianUtils.makeLong(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, value[0], value[1]);
            case 3:  return BinaryBigEndianUtils.makeLong(ZERO, ZERO, ZERO, ZERO, ZERO, value[0], value[1], value[2]);
            case 4:  return BinaryBigEndianUtils.makeLong(ZERO, ZERO, ZERO, ZERO, value[0], value[1], value[2], value[3]);
            case 5:  return BinaryBigEndianUtils.makeLong(ZERO, ZERO, ZERO, value[0], value[1], value[2], value[3], value[4]);
            case 6:  return BinaryBigEndianUtils.makeLong(ZERO, ZERO, value[0], value[1], value[2], value[3], value[4], value[5]);
            case 7:  return BinaryBigEndianUtils.makeLong(ZERO, value[0], value[1], value[2], value[3], value[4], value[5], value[6]);
            default: return BinaryBigEndianUtils.makeLong(value[0], value[1], value[2], value[3], value[4], value[5], value[6], value[7]);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() throws EngineDataException {
        if (value == null) {
            return null;
        } else if (value.length < 4) {
            if (value.length == 0) {
                return null;
            }
            throw new DataConversionException ("DATA_BINARY_TO_FLOAT_INSUFFICIENT_BYTES");
        }

        return Float.intBitsToFloat(BinaryBigEndianUtils.makeInt(value[0], value[1], value[2], value[3]));
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() throws EngineDataException {
        if (value == null) {
            return null;
        } else if (value.length < 8) {
            if (value.length == 0) {
                return null;
            }
            throw new DataConversionException ("DATA_BINARY_TO_DOUBLE_INSUFFICIENT_BYTES");
        }
        
        return Double.longBitsToDouble(BinaryBigEndianUtils.makeLong(value[0], value[1], value[2], value[3], value[4], value[5], value[6], value[7]));
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() throws EngineDataException {
        if (value != null && value.length > 0) {
            try {
                return new BigDecimal(deserializeToString(value));
            } catch (NumberFormatException e) {
                throw new DataConversionException(e.getMessage(), e);
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() throws EngineDataException {
        if (value == null || value.length == 0) {
            return null;
        }
        final String s = deserializeToString(value);
        switch (s.trim().toLowerCase()) {
            case "false":
            case "no":
            case "0":
            case "否":
                return Boolean.FALSE;
            default: return Boolean.TRUE;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Date toDate() throws EngineDataException {
        if (value != null && value.length > 0) {
            try {
                final Long result = XTemporalUtils.parseDate(deserializeToString(value));
                return result != null ? new java.sql.Date(result) : null;
            } catch (DateTimeParseException e) {
                throw new DataConversionException(e.getMessage(), e);
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() throws EngineDataException {
        if (value != null && value.length > 0) {
            try {
                final Long result = XTemporalUtils.parseTime(deserializeToString(value));
                return result != null ? new java.sql.Time(result) : null;
            } catch (DateTimeParseException e) {
                throw new DataConversionException(e.getMessage(), e);
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() throws EngineDataException {
        if (value != null && value.length > 0) {
            try {
                final Long result = XTemporalUtils.parseTimestamp(deserializeToString(value));
                return result != null ? new java.sql.Timestamp(result) : null;
            } catch (DateTimeParseException e) {
                throw new DataConversionException(e.getMessage(), e);
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() throws EngineDataException {
        if (value != null && value.length > 0) {
            final ZonedDateTime zdt = XTemporalUtils.parseCalendarTime(deserializeToString(value));
            if (zdt != null) {
                return GregorianCalendar.from(zdt);
            }
        }
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() throws EngineDataException {
        if (value != null && value.length > 0) {
            final ZonedDateTime zdt = XTemporalUtils.parseCalendarTimestamp(deserializeToString(value));
            if (zdt != null) {
                return GregorianCalendar.from(zdt);
            }
        }
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() throws EngineDataException {
        if (value != null && value.length > 0) {
            final String s = deserializeToString(value);
            if (XStringUtils.isNotBlank(s)) {
                try {
                    return XTemporalUtils.parseInstant(s);
                } catch (DateTimeParseException e) {
                    throw new DataConversionException (e.getMessage(), e);
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public List<byte[]> toList() {
        if (value == null) {
            return null;
        }
        final List<byte[]> list = new ArrayList<>(1);
        list.add(value);
        return list;
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<byte[]> toSet() {
        if (value == null) {
            return null;
        }
        final Set<byte[]> set = new LinkedHashSet<>(1);
        set.add(value);
        return set;
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<Integer, byte[]> toMap() {
        if (value == null) {
            return null;
        }
        final LinkedHashMap<Integer, byte[]> map = new LinkedHashMap<>(1);
        map.put(0, value);
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() throws EngineDataException {
        return value;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return value != null ? deserializeToString(value) : "";
    }
    
    /**
     * To HEX string.
     * 
     * @return String
     */
    public String toHexString() {
        return value != null && value.length > 0 ? XStringUtils.bytesToHex(value) :  "";
    }
    
    /** {@inheritDoc} */
    @Override
    public int size() {
        return 1;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNull() {
        return value == null;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return value == null || value.length == 0;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isDigits() {
        return value != null ? XNumberUtils.isDigits(deserializeToString(value)) : false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return value != null ? XNumberUtils.isNumber(deserializeToString(value)) : false;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : TypeUtils.NULL_HASH;
    }
    
    public static final BinaryData valueOf(final byte[] value) {
        return value != null ? (value.length == 0 ? BinaryData.EMPTY : new BinaryData(value)) : BinaryData.NULL;
    }
    
    public static final BinaryData valueOf(final ByteBuffer value) {
        if (value != null) {
            final byte[] bytes = value.array();
            return bytes.length == 0 ? BinaryData.EMPTY: new BinaryData(bytes);
        } else {
            return BinaryData.NULL;
        }
    }
    
    public static final BinaryData valueOf(final String value) {
        return value != null && value.length() > 0 ? new BinaryData(value.getBytes(StandardCharsets.UTF_8)) : BinaryData.NULL;
    }
    
    public static final BinaryData hexValueOf(final String hex) {
        return XStringUtils.isNotBlank(hex) ? new BinaryData(XStringUtils.hexToBytes(hex)) : BinaryData.NULL;
    }
    
    protected static String deserializeToString(final byte[] bytes) {
        if (bytes == null) {
            return null;
        } else if (bytes.length == 0) {
            return "";
        } else {
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
}
