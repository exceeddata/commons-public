package com.exceeddata.ac.common.util.binary;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

public class BinaryBigEndianUtilsTest {

    @Test
    public void testConvert() throws Exception {
        byte[] falseBytes = new byte[] { (byte) 0x00};
        byte[] trueBytes = new byte[] { (byte) 0xFE};
        byte[] maxInt8Bytes = new byte[] { (byte) 0x7F};
        byte[] maxUint8Bytes = new byte[] { (byte) 0xFF};
        byte[] maxInt16Bytes = new byte[] { (byte)0x7F, (byte) 0xFF};
        byte[] maxUint16Bytes = new byte[] { (byte) 0xFF, (byte)0xFF};
        byte[] maxInt32Bytes = new byte[] { (byte)0x7F, (byte) 0xFF, (byte)0xFF, (byte)0xFF};
        byte[] maxUint32Bytes = new byte[] { (byte) 0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        byte[] maxInt64Bytes = new byte[] { (byte)0x7F, (byte) 0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte) 0xFF, (byte)0xFF, (byte)0xFF};
        byte[] maxUint64Bytes = new byte[] { (byte) 0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte) 0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        byte[] one = new byte[] { (byte) 0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte) 0x00, (byte)0x00, (byte)0x00, (byte)0x01};

        assertEquals(BinaryBigEndianUtils.bytesToBool(falseBytes, 0), false);
        assertEquals(BinaryBigEndianUtils.bytesToBool(trueBytes, 0), true);
        assertEquals(BinaryBigEndianUtils.bytesToINT8(maxInt8Bytes, 0), (short) 127);
        assertEquals(BinaryBigEndianUtils.bytesToUINT8(maxUint8Bytes, 0), (short) 255);
        assertEquals(BinaryBigEndianUtils.bytesToINT16(maxInt16Bytes, 0), (short) 32767);
        assertEquals(BinaryBigEndianUtils.bytesToUINT16(maxUint16Bytes, 0), 65535);
        assertEquals(BinaryBigEndianUtils.bytesToINT32(maxInt32Bytes, 0), 2147483647);
        assertEquals(BinaryBigEndianUtils.bytesToUINT32(maxUint32Bytes, 0), 4294967295l);
        assertEquals(BinaryBigEndianUtils.bytesToINT64(maxInt64Bytes, 0), Long.MAX_VALUE);
        assertEquals(BinaryBigEndianUtils.bytesToUINT64(maxUint64Bytes, 0).toString(), "18446744073709551615");
        assertEquals(BinaryBigEndianUtils.bytesToLink64(maxUint64Bytes, 0).toString(), "18446744073709551615");
        assertEquals(BinaryBigEndianUtils.bytesToLink64(one, 0).toString(), "1");
    }
    
    @Test
    public void testFromTo() throws Exception {
        byte[] falseBytes = new byte[BinaryBigEndianUtils.LEN_BOOL];
        byte[] trueBytes = new byte[BinaryBigEndianUtils.LEN_BOOL];
        byte[] maxInt8Bytes = new byte[BinaryBigEndianUtils.LEN_INT8];
        byte[] maxUint8Bytes = new byte[BinaryBigEndianUtils.LEN_UINT8];
        byte[] maxInt16Bytes = new byte[BinaryBigEndianUtils.LEN_INT16];
        byte[] maxUint16Bytes = new byte[BinaryBigEndianUtils.LEN_UINT16];
        byte[] maxInt32Bytes = new byte[BinaryBigEndianUtils.LEN_INT32];
        byte[] maxUint32Bytes = new byte[BinaryBigEndianUtils.LEN_UINT32];
        byte[] maxInt64Bytes = new byte[BinaryBigEndianUtils.LEN_INT64];
        byte[] maxUint64Bytes = new byte[BinaryBigEndianUtils.LEN_UINT64];
        byte[] maxLink32Bytes = new byte[BinaryBigEndianUtils.LEN_LINK32];
        byte[] maxLink64Bytes = new byte[BinaryBigEndianUtils.LEN_LINK64];
        byte[] one = new byte[BinaryBigEndianUtils.LEN_LINK64];
        byte[] twoFloat = new byte[BinaryBigEndianUtils.LEN_FLOAT];
        byte[] twoReal = new byte[BinaryBigEndianUtils.LEN_REAL];

        BinaryBigEndianUtils.boolToBytes(falseBytes, 0, false);
        BinaryBigEndianUtils.boolToBytes(trueBytes, 0, true);
        BinaryBigEndianUtils.int8ToBytes(maxInt8Bytes, 0, (short) 81);
        BinaryBigEndianUtils.uint8ToBytes(maxUint8Bytes, 0, (short) 236);
        BinaryBigEndianUtils.int16ToBytes(maxInt16Bytes, 0, (short) 31827);
        BinaryBigEndianUtils.uint16ToBytes(maxUint16Bytes, 0, 62492);
        BinaryBigEndianUtils.int32ToBytes(maxInt32Bytes, 0, 2142443147);
        BinaryBigEndianUtils.uint32ToBytes(maxUint32Bytes, 0, 4293927241l);
        BinaryBigEndianUtils.int64ToBytes(maxInt64Bytes, 0, Long.MAX_VALUE);
        BinaryBigEndianUtils.uint64ToBytes(maxUint64Bytes, 0, new BigInteger("9223372036854775807"));
        BinaryBigEndianUtils.link32ToBytes(maxLink32Bytes, 0, 4293927141l);
        BinaryBigEndianUtils.link64ToBytes(maxLink64Bytes, 0, new BigInteger("18446744073709551615"));
        BinaryBigEndianUtils.link64ToBytes(one, 0, new BigInteger("1"));
        BinaryBigEndianUtils.floatToBytes(twoFloat, 0, 2f);
        BinaryBigEndianUtils.realToBytes(twoReal, 0, 2d);

        assertEquals(BinaryBigEndianUtils.bytesToBool(falseBytes, 0), false);
        assertEquals(BinaryBigEndianUtils.bytesToBool(trueBytes, 0), true);
        assertEquals(BinaryBigEndianUtils.bytesToINT8(maxInt8Bytes, 0), (short) 81);
        assertEquals(BinaryBigEndianUtils.bytesToUINT8(maxUint8Bytes, 0), (short) 236);
        assertEquals(BinaryBigEndianUtils.bytesToINT16(maxInt16Bytes, 0), (short) 31827);
        assertEquals(BinaryBigEndianUtils.bytesToUINT16(maxUint16Bytes, 0), 62492);
        assertEquals(BinaryBigEndianUtils.bytesToINT32(maxInt32Bytes, 0), 2142443147);
        assertEquals(BinaryBigEndianUtils.bytesToUINT32(maxUint32Bytes, 0), 4293927241l);
        assertEquals(BinaryBigEndianUtils.bytesToINT64(maxInt64Bytes, 0), Long.MAX_VALUE);
        assertEquals(BinaryBigEndianUtils.bytesToUINT64(maxUint64Bytes, 0).toString(), "9223372036854775807");
        assertEquals(BinaryBigEndianUtils.bytesToLink32(maxLink32Bytes, 0), 4293927141l);
        assertEquals(BinaryBigEndianUtils.bytesToLink64(maxLink64Bytes, 0).toString(), "18446744073709551615");
        assertEquals(BinaryBigEndianUtils.bytesToLink64(one, 0).toString(), "1");
        assertEquals(String.valueOf(BinaryBigEndianUtils.bytesToFloat(twoFloat, 0)), "2.0");
        assertEquals(String.valueOf(BinaryBigEndianUtils.bytesToReal(twoReal, 0)), "2.0");
    }
    
    @Test
    public void testLessBytes() throws Exception {
        byte[] maxInt64Bytes = new byte[BinaryLittleEndianUtils.LEN_INT64];
        byte[] lessInt64Bytes = new byte[6];
        
        final long testLong = Long.MAX_VALUE - 1082737l;
        BinaryBigEndianUtils.int64ToBytes(maxInt64Bytes, 0, testLong);
        BinaryBigEndianUtils.int64ToLessBytes(lessInt64Bytes, 0, testLong, 6);
        
        for (int i = 0; i < 6; ++i) {
            assertEquals(maxInt64Bytes[i+2], lessInt64Bytes[i]);
        }
    }
    
    @Test
    public void testLittleEndianString() throws Exception {
        final byte[] strBytes = new byte[8];
        
        //normal
        Arrays.fill(strBytes, (byte) 1);
        BinaryBigEndianUtils.ansiToBytes(strBytes, 0, 8, "12");
        assertEquals(strBytes[7], (byte) 0);
        assertEquals(BinaryBigEndianUtils.bytesToANSI(strBytes, 0, 8), "12");
        
        //normal with filler space
        Arrays.fill(strBytes, (byte) 1);
        BinaryBigEndianUtils.ansiToBytes(strBytes, 0, 8, "12   ");
        assertEquals(strBytes[7], (byte) 0);
        assertEquals(BinaryBigEndianUtils.bytesToANSI(strBytes, 0, 8), "12   ");
        
        //full
        Arrays.fill(strBytes, (byte) 1);
        BinaryBigEndianUtils.ansiToBytes(strBytes, 0, 8, "12345678");
        assertEquals(BinaryBigEndianUtils.bytesToANSI(strBytes, 0, 8), "12345678");
        
        //overflow
        Arrays.fill(strBytes, (byte) 1);
        BinaryBigEndianUtils.ansiToBytes(strBytes, 0, 8, "123456789");
        assertEquals(BinaryBigEndianUtils.bytesToANSI(strBytes, 0, 8), "12345678");
    }
}
