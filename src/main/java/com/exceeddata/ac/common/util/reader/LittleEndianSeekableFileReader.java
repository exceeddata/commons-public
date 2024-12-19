package com.exceeddata.ac.common.util.reader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Arrays;

import com.exceeddata.ac.common.exception.binary.BinaryEOFException;
import com.exceeddata.ac.common.util.binary.BinaryLittleEndianUtils;

/**
 * Little Endian Seekable File Reader
 *
 */
public class LittleEndianSeekableFileReader implements BinarySeekableReader {
    private static final long serialVersionUID = 1L;
    
    protected RandomAccessFile seekable = null;
    protected byte[] data = new byte[1024]; //max bytes data, bigger than this we use dynamic allocation.
    
    public LittleEndianSeekableFileReader(final RandomAccessFile seekable) {
        this.seekable = seekable;
    }
    
    public LittleEndianSeekableFileReader(LittleEndianSeekableFileReader reader) {
        this.seekable = reader.seekable;
    }
    
    @Override
    public LittleEndianSeekableFileReader clone() {
        return new LittleEndianSeekableFileReader(this);
    }

    @Override
    public String readANSI(final int len) throws IOException {
        final byte[] data;
        if (len <= 1024) {
            data = this.data;
            Arrays.fill(data, (byte) 0);
        } else {
            data = new byte[len];
        }
        read(data, len);
        
        return BinaryLittleEndianUtils.bytesToANSI(data, 0, len);
    }

    @Override
    public boolean readBool() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_BOOL);
        return BinaryLittleEndianUtils.bytesToBool(data, 0);
    }

    @Override
    public short readINT8() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_INT8);
        return BinaryLittleEndianUtils.bytesToINT8(data, 0);
    }

    @Override
    public short readINT16() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_INT16);
        return BinaryLittleEndianUtils.bytesToINT16(data, 0);
    }

    @Override
    public int readINT32() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_INT32);
        return BinaryLittleEndianUtils.bytesToINT32(data, 0);
    }

    @Override
    public long readINT64() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_INT64);
        return BinaryLittleEndianUtils.bytesToINT64(data, 0);
    }

    @Override
    public short readUINT8() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_UINT8);
        return BinaryLittleEndianUtils.bytesToUINT8(data, 0);
    }

    @Override
    public int readUINT16() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_UINT16);
        return BinaryLittleEndianUtils.bytesToUINT16(data, 0);
    }

    @Override
    public long readUINT32() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_UINT32);
        return BinaryLittleEndianUtils.bytesToUINT32(data, 0);
    }

    @Override
    public BigInteger readUINT64() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_UINT64);
        return BinaryLittleEndianUtils.bytesToUINT64(data, 0);
    }

    @Override
    public double readDouble() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_REAL);
        return BinaryLittleEndianUtils.bytesToReal(data, 0);
    }

    @Override
    public float readFloat() throws IOException {
        read(data, BinaryLittleEndianUtils.LEN_FLOAT);
        return BinaryLittleEndianUtils.bytesToFloat(data, 0);
    }

    @Override
    public byte[] readBytes(int len) throws IOException {
        final byte[] data;
        if (len <= 1024) {
            data = this.data;
            Arrays.fill(data, (byte) 0);
        } else {
            data = new byte[len];
        }
        read(data, len);
        
        return BinaryLittleEndianUtils.bytes(data, 0, len);
    }
    
    private void read(final byte[] data, final int len) throws IOException {
        int remaining = len, read = -1, offset = 0;
        do {
            read = seekable.read(data, offset, remaining);
            if (read < 0) {
                throw new BinaryEOFException();
            }
            remaining -= read;
            offset += read;
        } while (remaining > 0);
    }
    
    @Override
    public void seek(final long pos) throws IOException {
        seekable.seek(pos);
    }
    
    @Override
    public void skipBytes(final int len) throws IOException {
        int remaining = len, skipped;
        do {
            skipped = seekable.skipBytes(remaining);
            if (skipped <= 0) {
                throw new BinaryEOFException();
            }
            remaining -= skipped;
        } while (remaining > 0);
    }
    
    @Override
    public long getPos() throws IOException {
        return seekable.getFilePointer();
    }
    
    @Override
    public void close()  {
        if (seekable != null) {
            try {
                seekable.close();
            } catch (IOException e) {
            }
            seekable = null;
        }
    }
};
