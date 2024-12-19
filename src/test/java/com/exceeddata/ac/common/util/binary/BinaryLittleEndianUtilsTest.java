package com.exceeddata.ac.common.util.binary;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

public class BinaryLittleEndianUtilsTest {

    @Test
    public void testConvert() throws Exception {
        byte[] falseBytes = new byte[] { (byte) 0x00};
        byte[] trueBytes = new byte[] { (byte) 0xFE};
        byte[] maxInt8Bytes = new byte[] { (byte) 0x7F};
        byte[] maxUint8Bytes = new byte[] { (byte) 0xFF};
        byte[] maxInt16Bytes = new byte[] { (byte) 0xFF, (byte)0x7F};
        byte[] maxUint16Bytes = new byte[] { (byte) 0xFF, (byte)0xFF};
        byte[] maxInt32Bytes = new byte[] { (byte) 0xFF, (byte)0xFF, (byte)0xFF, (byte)0x7F};
        byte[] maxUint32Bytes = new byte[] { (byte) 0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        byte[] maxInt64Bytes = new byte[] { (byte) 0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte) 0xFF, (byte)0xFF, (byte)0xFF, (byte)0x7F};
        byte[] maxUint64Bytes = new byte[] { (byte) 0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte) 0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        byte[] one = new byte[] { (byte) 0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte) 0x00, (byte)0x00, (byte)0x00, (byte)0x00};

        assertEquals(BinaryLittleEndianUtils.bytesToBool(falseBytes, 0), false);
        assertEquals(BinaryLittleEndianUtils.bytesToBool(trueBytes, 0), true);
        assertEquals(BinaryLittleEndianUtils.bytesToINT8(maxInt8Bytes, 0), (short) 127);
        assertEquals(BinaryLittleEndianUtils.bytesToUINT8(maxUint8Bytes, 0), (short) 255);
        assertEquals(BinaryLittleEndianUtils.bytesToINT16(maxInt16Bytes, 0), (short) 32767);
        assertEquals(BinaryLittleEndianUtils.bytesToUINT16(maxUint16Bytes, 0), 65535);
        assertEquals(BinaryLittleEndianUtils.bytesToINT32(maxInt32Bytes, 0), 2147483647);
        assertEquals(BinaryLittleEndianUtils.bytesToUINT32(maxUint32Bytes, 0), 4294967295l);
        assertEquals(BinaryLittleEndianUtils.bytesToINT64(maxInt64Bytes, 0), Long.MAX_VALUE);
        assertEquals(BinaryLittleEndianUtils.bytesToUINT64(maxUint64Bytes, 0).toString(), "18446744073709551615");
        assertEquals(BinaryLittleEndianUtils.bytesToLink64(maxUint64Bytes, 0).toString(), "18446744073709551615");
        assertEquals(BinaryLittleEndianUtils.bytesToLink64(one, 0).toString(), "1");
    }
    
    @Test
    public void testFromTo() throws Exception {
        byte[] falseBytes = new byte[BinaryLittleEndianUtils.LEN_BOOL];
        byte[] trueBytes = new byte[BinaryLittleEndianUtils.LEN_BOOL];
        byte[] maxInt8Bytes = new byte[BinaryLittleEndianUtils.LEN_INT8];
        byte[] maxUint8Bytes = new byte[BinaryLittleEndianUtils.LEN_UINT8];
        byte[] maxInt16Bytes = new byte[BinaryLittleEndianUtils.LEN_INT16];
        byte[] maxUint16Bytes = new byte[BinaryLittleEndianUtils.LEN_UINT16];
        byte[] maxInt32Bytes = new byte[BinaryLittleEndianUtils.LEN_INT32];
        byte[] maxUint32Bytes = new byte[BinaryLittleEndianUtils.LEN_UINT32];
        byte[] maxInt64Bytes = new byte[BinaryLittleEndianUtils.LEN_INT64];
        byte[] maxUint64Bytes = new byte[BinaryLittleEndianUtils.LEN_UINT64];
        byte[] maxLink32Bytes = new byte[BinaryLittleEndianUtils.LEN_LINK32];
        byte[] maxLink64Bytes = new byte[BinaryLittleEndianUtils.LEN_LINK64];
        byte[] one = new byte[BinaryLittleEndianUtils.LEN_LINK64];
        byte[] twoFloat = new byte[BinaryLittleEndianUtils.LEN_FLOAT];
        byte[] twoReal = new byte[BinaryLittleEndianUtils.LEN_REAL];

        BinaryLittleEndianUtils.boolToBytes(falseBytes, 0, false);
        BinaryLittleEndianUtils.boolToBytes(trueBytes, 0, true);
        BinaryLittleEndianUtils.int8ToBytes(maxInt8Bytes, 0, (short) 81);
        BinaryLittleEndianUtils.uint8ToBytes(maxUint8Bytes, 0, (short) 236);
        BinaryLittleEndianUtils.int16ToBytes(maxInt16Bytes, 0, (short) 31827);
        BinaryLittleEndianUtils.uint16ToBytes(maxUint16Bytes, 0, 62492);
        BinaryLittleEndianUtils.int32ToBytes(maxInt32Bytes, 0, 2142443147);
        BinaryLittleEndianUtils.uint32ToBytes(maxUint32Bytes, 0, 4293927241l);
        BinaryLittleEndianUtils.int64ToBytes(maxInt64Bytes, 0, Long.MAX_VALUE - 1l);
        BinaryLittleEndianUtils.uint64ToBytes(maxUint64Bytes, 0, new BigInteger("9223372036854775807"));
        BinaryLittleEndianUtils.link32ToBytes(maxLink32Bytes, 0, 4293927141l);
        BinaryLittleEndianUtils.link64ToBytes(maxLink64Bytes, 0, new BigInteger("18446744073709551615"));
        BinaryLittleEndianUtils.link64ToBytes(one, 0, new BigInteger("1"));
        BinaryLittleEndianUtils.floatToBytes(twoFloat, 0, 2f);
        BinaryLittleEndianUtils.realToBytes(twoReal, 0, 2d);

        assertEquals(BinaryLittleEndianUtils.bytesToBool(falseBytes, 0), false);
        assertEquals(BinaryLittleEndianUtils.bytesToBool(trueBytes, 0), true);
        assertEquals(BinaryLittleEndianUtils.bytesToINT8(maxInt8Bytes, 0), (short) 81);
        assertEquals(BinaryLittleEndianUtils.bytesToUINT8(maxUint8Bytes, 0), (short) 236);
        assertEquals(BinaryLittleEndianUtils.bytesToINT16(maxInt16Bytes, 0), (short) 31827);
        assertEquals(BinaryLittleEndianUtils.bytesToUINT16(maxUint16Bytes, 0), 62492);
        assertEquals(BinaryLittleEndianUtils.bytesToINT32(maxInt32Bytes, 0), 2142443147);
        assertEquals(BinaryLittleEndianUtils.bytesToUINT32(maxUint32Bytes, 0), 4293927241l);
        assertEquals(BinaryLittleEndianUtils.bytesToINT64(maxInt64Bytes, 0), Long.MAX_VALUE - 1l);
        assertEquals(BinaryLittleEndianUtils.bytesToUINT64(maxUint64Bytes, 0).toString(), "9223372036854775807");
        assertEquals(BinaryLittleEndianUtils.bytesToLink32(maxLink32Bytes, 0), 4293927141l);
        assertEquals(BinaryLittleEndianUtils.bytesToLink64(maxLink64Bytes, 0).toString(), "18446744073709551615");
        assertEquals(BinaryLittleEndianUtils.bytesToLink64(one, 0).toString(), "1");
        assertEquals(String.valueOf(BinaryLittleEndianUtils.bytesToFloat(twoFloat, 0)), "2.0");
        assertEquals(String.valueOf(BinaryLittleEndianUtils.bytesToReal(twoReal, 0)), "2.0");
    }
    
    @Test
    public void testLessBytes() throws Exception {
        byte[] maxInt64Bytes = new byte[BinaryLittleEndianUtils.LEN_INT64];
        byte[] lessInt64Bytes = new byte[6];
        
        final long testLong = Long.MAX_VALUE - 1082737l;
        BinaryLittleEndianUtils.int64ToBytes(maxInt64Bytes, 0, testLong);
        BinaryLittleEndianUtils.int64ToLessBytes(lessInt64Bytes, 0, testLong, 6);
        
        for (int i = 0; i < 6; ++i) {
            assertEquals(maxInt64Bytes[i], lessInt64Bytes[i]);
        }
    }
    
    @Test
    public void testString() throws Exception {
        final byte[] strBytes = new byte[8];
        
        //normal
        Arrays.fill(strBytes, (byte) 1);
        BinaryLittleEndianUtils.ansiToBytes(strBytes, 0, 8, "12");
        assertEquals(strBytes[7], (byte) 0);
        assertEquals(BinaryLittleEndianUtils.bytesToANSI(strBytes, 0, 8), "12");
        
        //normal with filler space
        Arrays.fill(strBytes, (byte) 1);
        BinaryLittleEndianUtils.ansiToBytes(strBytes, 0, 8, "12   ");
        assertEquals(strBytes[7], (byte) 0);
        assertEquals(BinaryLittleEndianUtils.bytesToANSI(strBytes, 0, 8), "12   ");
        
        //full
        Arrays.fill(strBytes, (byte) 1);
        BinaryLittleEndianUtils.ansiToBytes(strBytes, 0, 8, "12345678");
        assertEquals(BinaryLittleEndianUtils.bytesToANSI(strBytes, 0, 8), "12345678");
        
        //overflow
        Arrays.fill(strBytes, (byte) 1);
        BinaryLittleEndianUtils.ansiToBytes(strBytes, 0, 8, "123456789");
        assertEquals(BinaryLittleEndianUtils.bytesToANSI(strBytes, 0, 8), "12345678");
    }
}
