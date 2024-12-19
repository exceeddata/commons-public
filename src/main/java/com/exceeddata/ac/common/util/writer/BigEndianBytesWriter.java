package com.exceeddata.ac.common.util.writer;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.exceeddata.ac.common.util.binary.BinaryBigEndianUtils;

public class BigEndianBytesWriter implements BinaryBytesWriter {
    private static final long serialVersionUID = 1L;

    @Override
    public void writeANSI(final byte[] buffer, final int offset, final int maxlen, final String val) {
        BinaryBigEndianUtils.ansiToBytes(buffer, offset, maxlen, val);
    }

    @Override
    public void writeUTF8(final byte[] buffer, final int offset, final int maxlen, final String val) {
        BinaryBigEndianUtils.utf8ToBytes(buffer, offset, maxlen, val);
    }

    @Override
    public void writeBool(final byte[] buffer, final int offset, final boolean val) {
        BinaryBigEndianUtils.boolToBytes(buffer, offset, val);
    }

    @Override
    public void writeINT8(final byte[] buffer, final int offset, final byte val) {
        BinaryBigEndianUtils.int8ToBytes(buffer, offset, val);
    }

    @Override
    public void writeINT16(final byte[] buffer, final int offset, final short val) {
        BinaryBigEndianUtils.int16ToBytes(buffer, offset, val);
    }

    @Override
    public void writeINT32(final byte[] buffer, final int offset, final int val) {
        BinaryBigEndianUtils.int32ToBytes(buffer, offset, val);
    }

    @Override
    public void writeINT64(final byte[] buffer, final int offset, final long val) {
        BinaryBigEndianUtils.int64ToBytes(buffer, offset, val);
    }

    @Override
    public void writeLessINT64(final byte[] buffer, final int offset, final long val, final int byteLen) {
        BinaryBigEndianUtils.int64ToLessBytes(buffer, offset, val, byteLen);
    }

    @Override
    public void writeUINT8(final byte[] buffer, final int offset, final short val) {
        BinaryBigEndianUtils.uint8ToBytes(buffer, offset, val);
    }

    @Override
    public void writeUINT16(final byte[] buffer, final int offset, final int val) {
        BinaryBigEndianUtils.uint16ToBytes(buffer, offset, val);
    }

    @Override
    public void writeUINT32(final byte[] buffer, final int offset, final long val) {
        BinaryBigEndianUtils.uint32ToBytes(buffer, offset, val);
    }

    @Override
    public void writeUINT64(final byte[] buffer, final int offset, final BigInteger val) {
        BinaryBigEndianUtils.uint64ToBytes(buffer, offset, val);
    }

    @Override
    public void writeLink32(final byte[] buffer, final int offset, final long val) {
        BinaryBigEndianUtils.link32ToBytes(buffer, offset, val);
    }

    @Override
    public void writeLink64(final byte[] buffer, final int offset, final BigInteger val) {
        BinaryBigEndianUtils.link64ToBytes(buffer, offset, val);
    }

    @Override
    public void writeFloat(final byte[] buffer, final int offset, final float val) {
        BinaryBigEndianUtils.floatToBytes(buffer, offset, val);
    }

    @Override
    public void writeDouble(final byte[] buffer, final int offset, final double val) {
        BinaryBigEndianUtils.realToBytes(buffer, offset, val);
    }

    @Override
    public void writeBytes(final byte[] buffer, final int offset, final int maxlen, final byte[] val) {
        Arrays.fill(buffer, offset, offset + maxlen, (byte) 0);
        if (val != null && val.length > 0) {
            ByteBuffer.wrap(buffer, offset, maxlen).put(val, 0, Math.min(val.length, maxlen));
        }
    }

}
