package com.exceeddata.ac.common.util.writer;

import java.io.Serializable;
import java.math.BigInteger;

public interface BinaryBytesWriter extends Serializable {
    
    /**
     * Write an ANSI String.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param maxlen the max length of string's bytes to write
     * @param val the value to write
     */
    public void writeANSI(final byte[] buffer, final int offset, final int maxlen, final String val);
    
    /**
     * Write an ANSI String.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param maxlen the max length of string's bytes to write
     * @param val the value to write
     */
    public void writeUTF8(final byte[] buffer, final int offset, final int maxlen, final String val);

    /**
     * Write a boolean.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeBool(final byte[] buffer, final int offset, final boolean val);

    /**
     * Write an signed int8.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeINT8(final byte[] buffer, final int offset, final byte val);

    /**
     * Write an signed short / int16.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeINT16(final byte[] buffer, final int offset, final short val);

    /**
     * Write an signed int32.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeINT32(final byte[] buffer, final int offset, final int val);

    /**
     * Write an signed int64.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeINT64(final byte[] buffer, final int offset, final long val);

    /**
     * Write an signed int64.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     * @param byteLen the actual reduced byte len
     */
    public void writeLessINT64(final byte[] buffer, final int offset, final long val, final int byteLen);

    /**
     * Write an unsigned int8.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeUINT8(final byte[] buffer, final int offset, final short val);

    /**
     * Write an unsigned int16.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeUINT16(final byte[] buffer, final int offset, final int val);

    /**
     * Write an unsigned int32.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeUINT32(final byte[] buffer, final int offset, final long val);

    /**
     * Write an unsigned int64.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeUINT64(final byte[] buffer, final int offset, final BigInteger val);

    /**
     * Write an unsigned link32.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeLink32(final byte[] buffer, final int offset, final long val);

    /**
     * Write an unsigned link64.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeLink64(final byte[] buffer, final int offset, final BigInteger val);

    /**
     * Write a float.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeFloat(final byte[] buffer, final int offset, final float val);

    /**
     * Write a double.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param val the value to write
     */
    public void writeDouble(final byte[] buffer, final int offset, final double val);

    /**
     * Write bytes.
     * 
     * @param buffer the target byte buffer to write
     * @param offset the offset from the start of the buffer
     * @param maxlen the max length of bytes to write
     * @param val the value to write
     */
    public void writeBytes(final byte[] buffer, final int offset, final int maxlen, final byte[] val);
}
