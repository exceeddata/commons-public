package com.exceeddata.ac.common.util.reader;

import java.io.IOException;
import java.math.BigInteger;

import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.binary.BinaryEOFException;
import com.exceeddata.ac.common.util.binary.BinaryBigEndianUtils;

/**
 * Big Endian Seekable Bytes Reader
 *
 */
public class BigEndianSeekableBytesReader implements BinarySeekableReader {
    private static final long serialVersionUID = 1L;
    
    protected byte[] data = null;
    protected int position = 0;
    
    public BigEndianSeekableBytesReader(final byte[] data) {
        this.data = data;
    }
    
    public BigEndianSeekableBytesReader(BigEndianSeekableBytesReader reader) {
        this.data = reader.data;
        this.position = reader.position;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigEndianSeekableBytesReader clone() {
        return new BigEndianSeekableBytesReader(this);
    }

    /** {@inheritDoc} */
    @Override
    public String readANSI(final int len) throws IOException {
        if (position + len > data.length) {
            throw new BinaryEOFException();
        }
        
        final String ret = BinaryBigEndianUtils.bytesToANSI(data, position, len);
        position += len;
        
        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public boolean readBool() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_BOOL > data.length) {
            throw new BinaryEOFException();
        }
        
        final boolean ret = BinaryBigEndianUtils.bytesToBool(data, position);
        position += BinaryBigEndianUtils.LEN_BOOL;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public short readINT8() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_INT8 > data.length) {
            throw new BinaryEOFException();
        }
        
        final short ret = BinaryBigEndianUtils.bytesToINT8(data, position);
        position += BinaryBigEndianUtils.LEN_INT8;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public short readINT16() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_INT16 > data.length) {
            throw new BinaryEOFException();
        }
        
        final short ret = BinaryBigEndianUtils.bytesToINT16(data, position);
        position += BinaryBigEndianUtils.LEN_INT16;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public int readINT32() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_INT32 > data.length) {
            throw new BinaryEOFException();
        }
        
        final int ret = BinaryBigEndianUtils.bytesToINT32(data, position);
        position += BinaryBigEndianUtils.LEN_INT32;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public long readINT64() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_INT64 > data.length) {
            throw new BinaryEOFException();
        }
        
        final long ret = BinaryBigEndianUtils.bytesToINT64(data, position);
        position += BinaryBigEndianUtils.LEN_INT64;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public short readUINT8() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_UINT8 > data.length) {
            throw new BinaryEOFException();
        }
        
        final short ret = BinaryBigEndianUtils.bytesToUINT8(data, position);
        position += BinaryBigEndianUtils.LEN_UINT8;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public int readUINT16() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_UINT16 > data.length) {
            throw new BinaryEOFException();
        }
        
        final int ret = BinaryBigEndianUtils.bytesToUINT16(data, position);
        position += BinaryBigEndianUtils.LEN_UINT16;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public long readUINT32() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_UINT32 > data.length) {
            throw new BinaryEOFException();
        }
        
        final long ret = BinaryBigEndianUtils.bytesToUINT32(data, position);
        position += BinaryBigEndianUtils.LEN_UINT32;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readUINT64() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_UINT64 > data.length) {
            throw new BinaryEOFException();
        }
        
        final BigInteger ret = BinaryBigEndianUtils.bytesToUINT64(data, position);
        position += BinaryBigEndianUtils.LEN_UINT64;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public double readDouble() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_REAL > data.length) {
            throw new BinaryEOFException();
        }
        
        final double ret = BinaryBigEndianUtils.bytesToReal(data, position);
        position += BinaryBigEndianUtils.LEN_REAL;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public float readFloat() throws IOException {
        if (position + BinaryBigEndianUtils.LEN_FLOAT > data.length) {
            throw new BinaryEOFException();
        }
        
        final float ret = BinaryBigEndianUtils.bytesToFloat(data, position);
        position += BinaryBigEndianUtils.LEN_FLOAT;

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public byte[] readBytes(int len) throws EngineException {
        if (position + len > data.length) {
            throw new BinaryEOFException();
        }

        final byte[] bytes = BinaryBigEndianUtils.bytes(data, position, len);
        position += len;

        return bytes;
    }
    
    /** {@inheritDoc} */
    @Override
    public void seek(final long pos) throws IOException {
        if ((int) pos >= data.length) {
            throw new BinaryEOFException();
        }

        position = (int) pos;
    }
    
    /** {@inheritDoc} */
    @Override
    public void skipBytes(int len) throws IOException {
        if (position + len > data.length) {
            throw new BinaryEOFException();
        }
        position += len;
    }
    
    /** {@inheritDoc} */
    @Override
    public long getPos() {
        return position;
    }
    
    /**
     * Return the underlying data.
     * 
     * @return data
     */
    public byte[] getData() {
    	return data;
    }
    
    /** {@inheritDoc} */
    @Override
    public void close()  {
    }
    
    /**
     * Get the total length of the bytes.
     * 
     * @return long
     */
    public long getLength() {
        return data.length;
    }
}