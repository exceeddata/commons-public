package com.exceeddata.ac.common.util.writer;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.exceeddata.ac.common.util.binary.BinaryLittleEndianUtils;

public class LittleEndianBytesWriter implements BinaryBytesWriter {
    private static final long serialVersionUID = 1L;

    @Override
    public void writeANSI(final byte[] buffer, final int offset, final int maxlen, final String val) {
        BinaryLittleEndianUtils.ansiToBytes(buffer, offset, maxlen, val);
    }

    @Override
    public void writeUTF8(final byte[] buffer, final int offset, final int maxlen, final String val) {
        BinaryLittleEndianUtils.utf8ToBytes(buffer, offset, maxlen, val);
    }

    @Override
    public void writeBool(final byte[] buffer, final int offset, final boolean val) {
        BinaryLittleEndianUtils.boolToBytes(buffer, offset, val);
    }

    @Override
    public void writeINT8(final byte[] buffer, final int offset, final byte val) {
        BinaryLittleEndianUtils.int8ToBytes(buffer, offset, val);
    }

    @Override
    public void writeINT16(final byte[] buffer, final int offset, final short val) {
        BinaryLittleEndianUtils.int16ToBytes(buffer, offset, val);
    }

    @Override
    public void writeINT32(final byte[] buffer, final int offset, final int val) {
        BinaryLittleEndianUtils.int32ToBytes(buffer, offset, val);
    }

    @Override
    public void writeINT64(final byte[] buffer, final int offset, final long val) {
        BinaryLittleEndianUtils.int64ToBytes(buffer, offset, val);
    }

    @Override
    public void writeLessINT64(final byte[] buffer, final int offset, final long val, final int byteLen) {
        BinaryLittleEndianUtils.int64ToLessBytes(buffer, offset, val, byteLen);
    }

    @Override
    public void writeUINT8(final byte[] buffer, final int offset, final short val) {
        BinaryLittleEndianUtils.uint8ToBytes(buffer, offset, val);
    }

    @Override
    public void writeUINT16(final byte[] buffer, final int offset, final int val) {
        BinaryLittleEndianUtils.uint16ToBytes(buffer, offset, val);
    }

    @Override
    public void writeUINT32(final byte[] buffer, final int offset, final long val) {
        BinaryLittleEndianUtils.uint32ToBytes(buffer, offset, val);
    }

    @Override
    public void writeUINT64(final byte[] buffer, final int offset, final BigInteger val) {
        BinaryLittleEndianUtils.uint64ToBytes(buffer, offset, val);
    }

    @Override
    public void writeLink32(final byte[] buffer, final int offset, final long val) {
        BinaryLittleEndianUtils.link32ToBytes(buffer, offset, val);
    }

    @Override
    public void writeLink64(final byte[] buffer, final int offset, final BigInteger val) {
        BinaryLittleEndianUtils.link64ToBytes(buffer, offset, val);
    }

    @Override
    public void writeFloat(final byte[] buffer, final int offset, final float val) {
        BinaryLittleEndianUtils.floatToBytes(buffer, offset, val);
    }

    @Override
    public void writeDouble(final byte[] buffer, final int offset, final double val) {
        BinaryLittleEndianUtils.realToBytes(buffer, offset, val);
    }

    @Override
    public void writeBytes(final byte[] buffer, final int offset, final int maxlen, final byte[] val) {
        Arrays.fill(buffer, offset, offset + maxlen, (byte) 0);
        if (val != null && val.length > 0) {
            ByteBuffer.wrap(buffer, offset, maxlen).put(val, 0, Math.min(val.length, maxlen));
        }
    }

}
