package com.exceeddata.ac.common.util.reader;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import com.exceeddata.ac.common.exception.binary.BinaryEOFException;
import com.exceeddata.ac.common.util.binary.BinaryLittleEndianUtils;

/**
 * Little Endian Input Stream (not seekable) reader
 *
 */
public class LittleEndianInputStreamReader implements BinaryStreamReader {
    private static final long serialVersionUID = 1L;
    
    protected InputStream stream = null;
    protected byte[] data = new byte[1024]; //max bytes data, bigger than this we use dynamic allocation.
    protected long sizeRead = 0l;
    
    public LittleEndianInputStreamReader(final InputStream stream) {
        this.stream = stream;
    }
    
    public LittleEndianInputStreamReader(LittleEndianInputStreamReader reader) {
        this.stream = reader.stream;
        this.sizeRead = reader.sizeRead;
    }
    
    @Override
    public LittleEndianInputStreamReader clone() {
        return new LittleEndianInputStreamReader(this);
    }

    @Override
    public String readANSI(final int len) throws IOException {
        final byte[] data = len <= 1024 ? this.data : new byte[len];
        if (stream.read(data, 0, len) != len) {
            throw new BinaryEOFException();
        }
        sizeRead += len;
        
        return BinaryLittleEndianUtils.bytesToANSI(data, 0, len);
    }

    @Override
    public boolean readBool() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_BOOL) != BinaryLittleEndianUtils.LEN_BOOL) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_BOOL;
        
        return BinaryLittleEndianUtils.bytesToBool(data, 0);
    }

    @Override
    public short readINT8() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_INT8) != BinaryLittleEndianUtils.LEN_INT8) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_INT8;
        
        return BinaryLittleEndianUtils.bytesToINT8(data, 0);
    }

    @Override
    public short readINT16() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_INT16) != BinaryLittleEndianUtils.LEN_INT16) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_INT16;
        
        return BinaryLittleEndianUtils.bytesToINT16(data, 0);
    }

    @Override
    public int readINT32() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_INT32) != BinaryLittleEndianUtils.LEN_INT32) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_INT32;
        
        return BinaryLittleEndianUtils.bytesToINT32(data, 0);
    }

    @Override
    public long readINT64() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_INT64) != BinaryLittleEndianUtils.LEN_INT64) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_INT64;
        
        return BinaryLittleEndianUtils.bytesToINT64(data, 0);
    }

    @Override
    public short readUINT8() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_UINT8) != BinaryLittleEndianUtils.LEN_UINT8) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_UINT8;
        
        return BinaryLittleEndianUtils.bytesToUINT8(data, 0);
    }

    @Override
    public int readUINT16() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_UINT16) != BinaryLittleEndianUtils.LEN_UINT16) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_UINT16;
        
        return BinaryLittleEndianUtils.bytesToUINT16(data, 0);
    }

    @Override
    public long readUINT32() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_UINT32) != BinaryLittleEndianUtils.LEN_UINT32) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_UINT32;
        
        return BinaryLittleEndianUtils.bytesToUINT32(data, 0);
    }

    @Override
    public BigInteger readUINT64() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_UINT64) != BinaryLittleEndianUtils.LEN_UINT64) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_UINT64;
        
        return BinaryLittleEndianUtils.bytesToUINT64(data, 0);
    }

    @Override
    public double readDouble() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_REAL) != BinaryLittleEndianUtils.LEN_REAL) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_REAL;
        
        return BinaryLittleEndianUtils.bytesToReal(data, 0);
    }

    @Override
    public float readFloat() throws IOException {
        if (stream.read(data, 0, BinaryLittleEndianUtils.LEN_FLOAT) != BinaryLittleEndianUtils.LEN_FLOAT) {
            throw new BinaryEOFException();
        }
        sizeRead += BinaryLittleEndianUtils.LEN_FLOAT;
        
        return BinaryLittleEndianUtils.bytesToFloat(data, 0);
    }

    @Override
    public byte[] readBytes(int len) throws IOException {
        final byte[] data = len <= 1024 ? this.data : new byte[len];
        if (stream.read(data, 0, len)  != len) {
            throw new BinaryEOFException();
        }
        sizeRead += len;
        
        return BinaryLittleEndianUtils.bytes(data, 0, len);
    }
    
    @Override
    public long getSizeRead() {
        return sizeRead;
    }
    
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