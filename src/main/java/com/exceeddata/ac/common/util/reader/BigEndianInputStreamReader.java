package com.exceeddata.ac.common.util.reader;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import com.exceeddata.ac.common.exception.binary.BinaryEOFException;
import com.exceeddata.ac.common.util.binary.BinaryBigEndianUtils;

/**
 * Big Endian Input Stream (not seekable) reader
 *
 */
public class BigEndianInputStreamReader implements BinaryStreamReader {
    private static final long serialVersionUID = 1L;
    
    protected InputStream stream = null;
    protected byte[] data = new byte[1024]; //max bytes data, bigger than this we use dynamic allocation.
    protected long sizeRead = 0l;
    
    public BigEndianInputStreamReader(final InputStream stream) {
        this.stream = stream;
    }
    
    public BigEndianInputStreamReader(BigEndianInputStreamReader reader) {
        this.stream = reader.stream;
        this.sizeRead = reader.sizeRead;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigEndianInputStreamReader clone() {
        return new BigEndianInputStreamReader(this);
    }

    /** {@inheritDoc} */
    @Override
    public String readANSI(final int len) throws IOException {
        final byte[] data = len <= 1024 ? this.data : new byte[len];
        if (stream.read(data, 0, len) != len) {
            throw new BinaryEOFException();
        }
        sizeRead += len;
        
        return BinaryBigEndianUtils.bytesToANSI(data, 0, len);
    }

    /** {@inheritDoc} */
    @Override
    public boolean readBool() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_BOOL) != BinaryBigEndianUtils.LEN_BOOL) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_BOOL;
        
        return BinaryBigEndianUtils.bytesToBool(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public short readINT8() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_INT8) != BinaryBigEndianUtils.LEN_INT8) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_INT8;
        
        return BinaryBigEndianUtils.bytesToINT8(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public short readINT16() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_INT16) != BinaryBigEndianUtils.LEN_INT16) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_INT16;
        
        return BinaryBigEndianUtils.bytesToINT16(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public int readINT32() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_INT32) != BinaryBigEndianUtils.LEN_INT32) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_INT32;
        
        return BinaryBigEndianUtils.bytesToINT32(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public long readINT64() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_INT64) != BinaryBigEndianUtils.LEN_INT64) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_INT64;
        
        return BinaryBigEndianUtils.bytesToINT64(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public short readUINT8() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_UINT8) != BinaryBigEndianUtils.LEN_UINT8) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_UINT8;
        
        return BinaryBigEndianUtils.bytesToUINT8(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public int readUINT16() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_UINT16) != BinaryBigEndianUtils.LEN_UINT16) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_UINT16;
        
        return BinaryBigEndianUtils.bytesToUINT16(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public long readUINT32() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_UINT32) != BinaryBigEndianUtils.LEN_UINT32) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_UINT32;
        
        return BinaryBigEndianUtils.bytesToUINT32(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readUINT64() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_UINT64) != BinaryBigEndianUtils.LEN_UINT64) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_UINT64;
        
        return BinaryBigEndianUtils.bytesToUINT64(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public double readDouble() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_REAL) != BinaryBigEndianUtils.LEN_REAL) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_REAL;
        
        return BinaryBigEndianUtils.bytesToReal(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public float readFloat() throws IOException {
        if (stream.read(data, 0, BinaryBigEndianUtils.LEN_FLOAT) != BinaryBigEndianUtils.LEN_FLOAT) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryBigEndianUtils.LEN_FLOAT;
        
        return BinaryBigEndianUtils.bytesToFloat(data, 0);
    }

    /** {@inheritDoc} */
    @Override
    public byte[] readBytes(int len) throws IOException {
        final byte[] data = len <= 1024 ? this.data : new byte[len];
        if (stream.read(data, 0, len)  != len) {
            throw new BinaryEOFException();
        }
        sizeRead += len;
        
        return BinaryBigEndianUtils.bytes(data, 0, len);
    }
    
    /** {@inheritDoc} */
    @Override
    public long getSizeRead() {
        return sizeRead;
    }
    
    /** {@inheritDoc} */
    @Override
    public void close()  {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
            }
            stream = null;
        }
    }
};