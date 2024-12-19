package com.exceeddata.ac.common.util.reader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Arrays;

import com.exceeddata.ac.common.exception.binary.BinaryEOFException;
import com.exceeddata.ac.common.util.binary.BinaryBigEndianUtils;

/**
 * Big Endian Seekable File Reader
 *
 */
public class BigEndianSeekableFileReader implements BinarySeekableReader {
    private static final long serialVersionUID = 1L;
    
    protected RandomAccessFile seekable = null;
    protected byte[] data = new byte[1024]; //max bytes data, bigger than this we use dynamic allocation.
    
    public BigEndianSeekableFileReader(final RandomAccessFile seekable) {
        this.seekable = seekable;
    }
    
    public BigEndianSeekableFileReader(BigEndianSeekableFileReader reader) {
        this.seekable = reader.seekable;
    }
    
    @Override
    public BigEndianSeekableFileReader clone() {
        return new BigEndianSeekableFileReader(this);
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
        
        return BinaryBigEndianUtils.bytesToANSI(data, 0, len);
    }

    @Override
    public boolean readBool() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_BOOL);
        return BinaryBigEndianUtils.bytesToBool(data, 0);
    }

    @Override
    public short readINT8() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_INT8);
        return BinaryBigEndianUtils.bytesToINT8(data, 0);
    }

    @Override
    public short readINT16() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_INT16);
        return BinaryBigEndianUtils.bytesToINT16(data, 0);
    }

    @Override
    public int readINT32() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_INT32);
        return BinaryBigEndianUtils.bytesToINT32(data, 0);
    }

    @Override
    public long readINT64() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_INT64);
        return BinaryBigEndianUtils.bytesToINT64(data, 0);
    }

    @Override
    public short readUINT8() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_UINT8);
        return BinaryBigEndianUtils.bytesToUINT8(data, 0);
    }

    @Override
    public int readUINT16() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_UINT16);
        return BinaryBigEndianUtils.bytesToUINT16(data, 0);
    }

    @Override
    public long readUINT32() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_UINT32);
        return BinaryBigEndianUtils.bytesToUINT32(data, 0);
    }

    @Override
    public BigInteger readUINT64() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_UINT64);
        return BinaryBigEndianUtils.bytesToUINT64(data, 0);
    }

    @Override
    public double readDouble() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_REAL);
        return BinaryBigEndianUtils.bytesToReal(data, 0);
    }

    @Override
    public float readFloat() throws IOException {
        read(data, BinaryBigEndianUtils.LEN_FLOAT);
        return BinaryBigEndianUtils.bytesToFloat(data, 0);
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
        
        return BinaryBigEndianUtils.bytes(data, 0, len);
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
