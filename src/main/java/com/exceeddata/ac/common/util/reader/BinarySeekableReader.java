package com.exceeddata.ac.common.util.reader;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * Binary Seekable reader interface specification
 *
 */
public interface BinarySeekableReader extends Serializable {
    
    /**
     * Read an ANSI String.
     * 
     * @param len the length of bytes
     * @return String
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public String readANSI(final int len) throws IOException;

    /**
     * Read a boolean
     * 
     * @return boolean
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public boolean readBool() throws IOException;

    /**
     * Read a signed byte / int8.
     * 
     * @return short
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public short readINT8() throws IOException;

    /**
     * Read a signed short / int16.
     * 
     * @return short
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public short readINT16() throws IOException;

    /**
     * Read a signed int / int32.
     * 
     * @return int
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public int readINT32() throws IOException;

    /**
     * Read a signed long / int64.
     * 
     * @return long
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public long readINT64() throws IOException;

    /**
     * Read an unsigned byte / uint8.
     * 
     * @return short
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public short readUINT8() throws IOException;

    /**
     * Read an unsigned short / uint16, up-cast to Java int as Java is signed.
     * 
     * @return int
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public int readUINT16() throws IOException;

    /**
     * Read an unsigned int / uint32, up-cast to Java long as Java is signed.
     * 
     * @return long
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public long readUINT32() throws IOException;

    /**
     * Read an unsigned long / uint64, up-cast to Java BigInteger as Java is signed.
     * 
     * @return BigInteger
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public BigInteger readUINT64() throws IOException;

    /**
     * Read a double.
     * 
     * @return double
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public double readDouble() throws IOException;

    /**
     * Read a float.
     * 
     * @return float
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public float readFloat() throws IOException;

    /**
     * Read a byte array.
     * 
     * @param len the length of bytes
     * @return byte array
     * @throws IOException if EOF (insufficient number of bytes to be read) or I/O error
     */
    public byte[] readBytes(int len) throws IOException;
    
    /**
     * Seek to the target position.
     * 
     * @param pos the position offset or pointer
     * @throws IOException if EOF (beyond the end of seekable stream) or I/O error
     */
    public void seek(final long pos) throws IOException;
    
    /**
     * Skip ahead.
     * 
     * @param len the number of bytes to skip
     * @throws IOException if EOF (insufficient number of bytes to be skip) or I/O error
     */
    public void skipBytes(int len) throws IOException;
    
    /**
     * Get the current position of the reader.
     * 
     * @return long
     * @throws IOException I/O error
     */
    public long getPos() throws IOException;
    
    /**
     * Close the seekable stream.
     */
    public void close();
}
