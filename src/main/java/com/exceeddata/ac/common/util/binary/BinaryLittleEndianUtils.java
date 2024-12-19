package com.exceeddata.ac.common.util.binary;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class BinaryLittleEndianUtils {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    
    public final static int LEN_INT8 = Byte.SIZE/8;
    public final static int LEN_UINT8 = Byte.SIZE/8;
    public final static int LEN_BOOL = Byte.SIZE/8;
    public final static int LEN_INT16 = Short.SIZE/8;
    public final static int LEN_UINT16 = Short.SIZE/8;
    public final static int LEN_INT32 = Integer.SIZE/8;
    public final static int LEN_UINT32 = Integer.SIZE/8;
    public final static int LEN_INT64 = Long.SIZE/8;
    public final static int LEN_UINT64 = Long.SIZE/8; //do not support unsigned long
    public final static int LEN_LINK32 = Integer.SIZE/8;
    public final static int LEN_LINK64 = Long.SIZE/8; //do not support unsigned long
    public final static int LEN_FLOAT = Float.SIZE/8;
    public final static int LEN_REAL = Double.SIZE/8; //do not support unsigned long
    
    private BinaryLittleEndianUtils() {}
    
    public static int byteToUINT4Low(final byte arr[], final int offset) {
        return arr[offset] & 0x0F;
    }
    
    public static int byteToUINT4High(final byte arr[], final int offset) {
        return arr[offset] >> 4;
    }
    
    public static short bytesToINT8(final byte arr[], final int offset) {
        return (short) ByteBuffer.wrap(arr, offset, LEN_INT8).get();
    }
    
    public static short bytesToINT8(final byte arr[], final long offset) {
        return (short) ByteBuffer.wrap(arr, (int) offset, LEN_INT8).get();
    }
    
    public static short bytesToINT8(final byte arr[], final BigInteger offset) {
        return (short) ByteBuffer.wrap(arr, offset.intValue(), LEN_INT8).get();
    }
    
    public static short bytesToUINT8(final byte arr[], final int offset) {
        return (short) (ByteBuffer.wrap(arr, offset, LEN_UINT8).get() & 0xFF);
    }
    
    public static short bytesToUINT8(final byte arr[], final long offset) {
        return (short) (ByteBuffer.wrap(arr, (int) offset, LEN_UINT8).get() & 0xFF);
    }
    
    public static short bytesToUINT8(final byte arr[], final BigInteger offset) {
        return (short) (ByteBuffer.wrap(arr, offset.intValue(), LEN_UINT8).get() & 0xFF);
    }
    
    public static int bytesToUINT8int(final byte arr[], final int offset) {
        return ByteBuffer.wrap(arr, offset, LEN_UINT8).get() & 0xFF;
    }
    
    public static int bytesToUINT8int(final byte arr[], final long offset) {
        return ByteBuffer.wrap(arr, (int) offset, LEN_UINT8).get() & 0xFF;
    }
    
    public static int bytesToUINT8int(final byte arr[], final BigInteger offset) {
        return ByteBuffer.wrap(arr, offset.intValue(), LEN_UINT8).get() & 0xFF;
    }

    public static boolean bytesToBool(final byte arr[], final int offset) {
        return ByteBuffer.wrap(arr, offset, LEN_BOOL).get() != 0x00;
    }

    public static boolean bytesToBool(final byte arr[], final long offset) {
        return ByteBuffer.wrap(arr, (int) offset, LEN_BOOL).get() != 0x00;
    }

    public static boolean bytesToBool(final byte arr[], final BigInteger offset) {
        return ByteBuffer.wrap(arr, offset.intValue(), LEN_BOOL).get() != 0x00;
    }
    
    public static short bytesToINT16(final byte arr[], final int offset) {
        return ByteBuffer.wrap(arr, offset, LEN_INT16).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }
    
    public static short bytesToINT16(final byte arr[], final long offset) {
        return ByteBuffer.wrap(arr, (int) offset, LEN_INT16).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public static short bytesToINT16(final byte arr[], final BigInteger offset) {
        return ByteBuffer.wrap(arr, offset.intValue(), LEN_INT16).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public static int bytesToUINT16(final byte arr[], final int offset) {
        return (int) (ByteBuffer.wrap(arr, offset, LEN_UINT16).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF);
    }

    public static int bytesToUINT16(final byte arr[], final long offset) {
        return (int) (ByteBuffer.wrap(arr, (int) offset, LEN_UINT16).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF);
    }

    public static int bytesToUINT16(final byte arr[], final BigInteger offset) {
        return (int) (ByteBuffer.wrap(arr, offset.intValue(), LEN_UINT16).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF);
    }

    public static int bytesToINT32(final byte arr[], final int offset) {
        return ByteBuffer.wrap(arr, offset, LEN_INT32).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static int bytesToINT32(final byte arr[], final long offset) {
        return ByteBuffer.wrap(arr, (int) offset, LEN_INT32).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static int bytesToINT32(final byte arr[], final BigInteger offset) {
        return ByteBuffer.wrap(arr, offset.intValue(), LEN_INT32).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static long bytesToUINT32(final byte arr[], final int offset) {
        return ((long) ByteBuffer.wrap(arr, offset, LEN_UINT32).order(ByteOrder.LITTLE_ENDIAN).getInt()) & 0xFFFFFFFFL;
    }

    public static long bytesToUINT32(final byte arr[], final long offset) {
        return ((long) ByteBuffer.wrap(arr, (int) offset, LEN_UINT32).order(ByteOrder.LITTLE_ENDIAN).getInt()) & 0xFFFFFFFFL;
    }

    public static long bytesToUINT32(final byte arr[], final BigInteger offset) {
        return ((long) ByteBuffer.wrap(arr, offset.intValue(), LEN_UINT32).order(ByteOrder.LITTLE_ENDIAN).getInt()) & 0xFFFFFFFFL;
    }

    public static long bytesToINT64(final byte arr[], final int offset) {
        return ByteBuffer.wrap(arr, offset, LEN_INT64).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public static long bytesToINT64(final byte arr[], final long offset) {
        return ByteBuffer.wrap(arr, (int) offset, LEN_INT64).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public static long bytesToINT64(final byte arr[], final BigInteger offset) {
        return ByteBuffer.wrap(arr, offset.intValue(), LEN_INT64).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }
    
    public static BigInteger bytesToUINT64(final byte arr[], final int offset) {
        final byte[] uarr = new byte[LEN_UINT64];
        for (int i = 0; i < LEN_UINT64; ++i) {
            uarr[i] = arr[offset + LEN_UINT64 - i - 1];
        }
        return new BigInteger(1, uarr);
    }
    
    public static BigInteger bytesToUINT64(final byte arr[], final long offset) {
        final int ioffset = (int) offset;
        final byte[] uarr = new byte[LEN_UINT64];
        for (int i = 0; i < LEN_UINT64; ++i) {
            uarr[i] = arr[ioffset + LEN_UINT64 - i - 1];
        }
        return new BigInteger(1, uarr);
    }
    
    public static BigInteger bytesToUINT64(final byte arr[], final BigInteger offset) {
        final int ioffset = offset.intValue();
        final byte[] uarr = new byte[LEN_UINT64];
        for (int i = 0; i < LEN_UINT64; ++i) {
            uarr[i] = arr[ioffset + LEN_UINT64 - i - 1];
        }
        return new BigInteger(1, uarr);
    }

    public static float bytesToFloat(final byte arr[], final int offset) {
        return ByteBuffer.wrap(arr, offset, LEN_FLOAT).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public static float bytesToFloat(final byte arr[], final long offset) {
        return ByteBuffer.wrap(arr, (int) offset, LEN_FLOAT).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public static float bytesToFloat(final byte arr[], final BigInteger offset) {
        return ByteBuffer.wrap(arr, offset.intValue(), LEN_FLOAT).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public static double bytesToReal(final byte arr[], final int offset) {
        return ByteBuffer.wrap(arr, offset, LEN_REAL).order(ByteOrder.LITTLE_ENDIAN).getDouble();
    }

    public static double bytesToReal(final byte arr[], final long offset) {
        return ByteBuffer.wrap(arr, (int) offset, LEN_REAL).order(ByteOrder.LITTLE_ENDIAN).getDouble();
    }

    public static double bytesToReal(final byte arr[], final BigInteger offset) {
        return ByteBuffer.wrap(arr, offset.intValue(), LEN_REAL).order(ByteOrder.LITTLE_ENDIAN).getDouble();
    }
    
    public static long bytesToLink32(final byte arr[], final int offset) {
        return ((long) ByteBuffer.wrap(arr, offset, LEN_UINT32).order(ByteOrder.LITTLE_ENDIAN).getInt()) & 0xFFFFFFFFL;
    }
    
    public static long bytesToLink32(final byte arr[], final long offset) {
        return ((long) ByteBuffer.wrap(arr, (int) offset, LEN_UINT32).order(ByteOrder.LITTLE_ENDIAN).getInt()) & 0xFFFFFFFFL;
    }
    
    public static BigInteger bytesToLink64(final byte arr[], final int offset) {
        final byte[] uarr = new byte[LEN_LINK64];
        for (int i = 0; i < LEN_LINK64; ++i) {
            uarr[i] = arr[offset + LEN_LINK64 - i - 1];
        }
        return new BigInteger(1, uarr);
    }

    public static String bytesToANSI(final byte arr[], final int offset, final int len) {
        int nonNullLen = len;
        for (int i = 0; i < len; ++i) {
            if (arr[offset + i] == 0x00) {
                nonNullLen = i;
                break;
            }
        }
        
        final byte[] bytes = new byte[nonNullLen];
        System.arraycopy(arr, offset, bytes, 0, nonNullLen);
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    public static String bytesToUTF8(final byte arr[], final int offset, final int len) {
        int nonNullLen = len;
        for (int i = 0; i < len; ++i) {
            if (arr[offset + i] == 0x00) {
                nonNullLen = i;
                break;
            }
        }
        final byte[] bytes = new byte[nonNullLen];
        System.arraycopy(arr, offset, bytes, 0, nonNullLen);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    
    public static byte[] bytes(final byte arr[], final int offset, final int len) {
        final byte[] bytes = new byte[len];
        System.arraycopy(arr, offset, bytes, 0, len);
        return bytes;
    }
    
    public static byte[] bytes(final byte arr[], final long offset, final long len) {
        final int ioffset = (int) offset;
        final int ilen = (int) len;
        final byte[] bytes = new byte[ilen];
        System.arraycopy(arr, ioffset, bytes, 0, ilen);
        return bytes;
    }
    
    public static void copyBytes(final byte src[], final int srcOffset, final byte target[], final int targetOffset, final int len) {
        System.arraycopy(src, srcOffset, target, targetOffset, len);
    }
    
    public static byte[] bytes(final byte arr[], final BigInteger offset, final BigInteger len) {
        final int ioffset = offset.intValue();
        final int ilen = len.intValue();
        final byte[] bytes = new byte[ilen];
        System.arraycopy(arr, ioffset, bytes, 0, ilen);
        return bytes;
    }
    
    public static int bitsToBytes(final long bits) {
        if (bits <=16l) {
            return bits <=8 ? 1 : 2;
        } else if (bits <= 64l) {
            return bits <=32 ? 4 : 8;
        } else {
            final int bytes = (int) (bits / 8);
            return bits % 8 == 0 ? bytes : bytes + 1;
        }
    }
    
    public static String bytesToHex(final byte[] bytes) {
        return bytesToHex(bytes, 0, bytes.length);
    }
    
    public static String bytesToHex(final byte[] bytes, final int offset, final int count) {
        char[] hexChars = new char[count * 2];
        for ( int j = 0; j < count; j++ ) {
            int v = bytes[j+offset] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static long makeLong(
            final byte b0, final byte b1, final byte b2, final byte b3,
            final byte b4, final byte b5, final byte b6, final byte b7) {
        return ((((long)b7       ) << 56) |
                (((long)b6 & 0xff) << 48) |
                (((long)b5 & 0xff) << 40) |
                (((long)b4 & 0xff) << 32) |
                (((long)b3 & 0xff) << 24) |
                (((long)b2 & 0xff) << 16) |
                (((long)b1 & 0xff) <<  8) |
                (((long)b0 & 0xff)      ));
    }
    
    public static int makeInt(final byte b0, final byte b1, final byte b2, final byte b3) {
        return (((b3       ) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) <<  8) |
                ((b0 & 0xff)      ));
    }
    
    public static void int8ToBytes(final byte arr[], final int offset, final byte val) {
        arr[offset] = val;
    }
    
    public static void int8ToBytes(final byte arr[], final long offset, final byte val) {
        arr[(int) offset] = val;
    }
    
    public static void int8ToBytes(final byte arr[], final BigInteger offset, final byte val) {
        arr[offset.intValue()] = val;
    }
    
    public static void int8ToBytes(final byte arr[], final int offset, final short val) {
        arr[offset] = (byte) val;
    }
    
    public static void int8ToBytes(final byte arr[], final long offset, final short val) {
        arr[(int) offset] = (byte) val;
    }
    
    public static void int8ToBytes(final byte arr[], final BigInteger offset, final short val) {
        arr[offset.intValue()] = (byte) val;
    }
    
    public static void uint8ToBytes(final byte arr[], final int offset, final short val) {
        arr[offset] = (byte) (val & 0xFF);
    }
    
    public static void uint8ToBytes(final byte arr[], final long offset, final short val) {
        arr[(int) offset] = (byte) (val & 0xFF);
    }
    
    public static void uint8ToBytes(final byte arr[], final BigInteger offset, final short val) {
        arr[offset.intValue()] = (byte) (val & 0xFF);
    }
    
    public static void uint8IntToBytes(final byte arr[], final int offset, final int val) {
        arr[offset] = (byte) (val & 0xFFFF);
    }
    
    public static void uint8IntToBytes (byte arr[], final long offset, final int val) {
        arr[(int) offset] = (byte) (val & 0xFFFF);
    }
    
    public static void uint8IntToBytes(final byte arr[], final BigInteger offset, final int val) {
        arr[offset.intValue()] = (byte) (val & 0xFFFF);
    }

    public static void boolToBytes(final byte arr[], final int offset, final boolean val) {
        arr[offset] = val ? (byte) 1 : (byte) 0;
    }

    public static void boolToBytes(final byte arr[], final long offset, final boolean val) {
        arr[(int) offset] = val ? (byte) 1 : (byte) 0;
    }

    public static void boolToBytes(final byte arr[], final BigInteger offset, final boolean val) {
        arr[offset.intValue()] = val ? (byte) 1 : (byte) 0;
    }
    
    public static void int16ToBytes(final byte arr[], final int offset, final short val) {
        ByteBuffer.wrap(arr, offset, LEN_INT16).order(ByteOrder.LITTLE_ENDIAN).putShort(val);
    }

    public static void int16ToBytes(final byte arr[], final long offset, final short val) {
        ByteBuffer.wrap(arr, (int) offset, LEN_INT16).order(ByteOrder.LITTLE_ENDIAN).putShort(val);
    }

    public static void int16ToBytes(final byte arr[], final BigInteger offset, final short val) {
        ByteBuffer.wrap(arr, offset.intValue(), LEN_INT16).order(ByteOrder.LITTLE_ENDIAN).putShort(val);
    }

    public static void uint16ToBytes(final byte arr[], final int offset, final int val) {
        ByteBuffer.wrap(arr, offset, LEN_UINT16).order(ByteOrder.LITTLE_ENDIAN).putShort((short) (val & 0xFFFF));
    }

    public static void uint16ToBytes(final byte arr[], final long offset, final int val) {
        ByteBuffer.wrap(arr, (int) offset, LEN_UINT16).order(ByteOrder.LITTLE_ENDIAN).putShort((short) (val & 0xFFFF));
    }

    public static void uint16ToBytes(final byte arr[], final BigInteger offset, final int val) {
        ByteBuffer.wrap(arr, offset.intValue(), LEN_UINT16).order(ByteOrder.LITTLE_ENDIAN).putShort((short) (val & 0xFFFF));
    }

    public static void int32ToBytes(final byte arr[], final int offset, final int val) {
        ByteBuffer.wrap(arr, offset, LEN_INT32).order(ByteOrder.LITTLE_ENDIAN).putInt(val);
    }

    public static void int32ToBytes(final byte arr[], final long offset, final int val) {
        ByteBuffer.wrap(arr, (int) offset, LEN_INT32).order(ByteOrder.LITTLE_ENDIAN).putInt(val);
    }

    public static void int32ToBytes(final byte arr[], final BigInteger offset, final int val) {
        ByteBuffer.wrap(arr, offset.intValue(), LEN_INT32).order(ByteOrder.LITTLE_ENDIAN).putInt(val);
    }

    public static void uint32ToBytes(final byte arr[], final int offset, final long val) {
        ByteBuffer.wrap(arr, offset, LEN_UINT32).order(ByteOrder.LITTLE_ENDIAN).putInt((int) (val & 0xFFFFFFFFL));
    }

    public static void uint32ToBytes(final byte arr[], final long offset, final long val) {
        ByteBuffer.wrap(arr, (int) offset, LEN_UINT32).order(ByteOrder.LITTLE_ENDIAN).putInt((int) (val & 0xFFFFFFFFL));
    }

    public static void uint32ToBytes(final byte arr[], final BigInteger offset, final long val) {
        ByteBuffer.wrap(arr, offset.intValue(), LEN_UINT32).order(ByteOrder.LITTLE_ENDIAN).putInt((int) (val & 0xFFFFFFFFL));
    }

    public static void int64ToBytes(final byte arr[], final int offset, final long val) {
        ByteBuffer.wrap(arr, offset, LEN_INT64).order(ByteOrder.LITTLE_ENDIAN).putLong(val);
    }

    public static void int64ToBytes(final byte arr[], final long offset, final long val) {
        ByteBuffer.wrap(arr, (int) offset, LEN_INT64).order(ByteOrder.LITTLE_ENDIAN).putLong(val);
    }

    public static void int64ToBytes(final byte arr[], final BigInteger offset, final long val) {
        ByteBuffer.wrap(arr, offset.intValue(), LEN_INT64).order(ByteOrder.LITTLE_ENDIAN).putLong(val);
    }

    public static void int64ToLessBytes(final byte arr[], final int offset, final long val, final int byteLen) {
        if (byteLen > 0) {
            int pos = offset + Math.min(byteLen, LEN_INT64) - 1;
            switch(byteLen) {
                case 8: arr[pos--] = (byte)(val >> 56);
                case 7: arr[pos--] = (byte)(val >> 48);
                case 6: arr[pos--] = (byte)(val >> 40);
                case 5: arr[pos--] = (byte)(val >> 32);
                case 4: arr[pos--] = (byte)(val >> 24);
                case 3: arr[pos--] = (byte)(val >> 16);
                case 2: arr[pos--] = (byte)(val >> 8);
                case 1: arr[pos] = (byte) val; break;
            }
        }
    }

    public static void int64ToLessBytes(final byte arr[], final long offset, final long val, final int byteLen) {
        if (byteLen > 0l) {
            int pos = ((int) offset) + Math.min(byteLen, LEN_INT64) - 1;
            switch(byteLen) {
                case 8: arr[pos--] = (byte)(val >> 56);
                case 7: arr[pos--] = (byte)(val >> 48);
                case 6: arr[pos--] = (byte)(val >> 40);
                case 5: arr[pos--] = (byte)(val >> 32);
                case 4: arr[pos--] = (byte)(val >> 24);
                case 3: arr[pos--] = (byte)(val >> 16);
                case 2: arr[pos--] = (byte)(val >> 8);
                case 1: arr[pos] = (byte) val; break;
            }
        }
    }

    public static void int64ToLessBytes(final byte arr[], final BigInteger offset, final long val, final int byteLen) {
        if (byteLen > 0) {
            int pos = offset.intValue() + Math.min(byteLen, LEN_INT64) - 1;
            switch(byteLen) {
                case 8: arr[pos--] = (byte)(val >> 56);
                case 7: arr[pos--] = (byte)(val >> 48);
                case 6: arr[pos--] = (byte)(val >> 40);
                case 5: arr[pos--] = (byte)(val >> 32);
                case 4: arr[pos--] = (byte)(val >> 24);
                case 3: arr[pos--] = (byte)(val >> 16);
                case 2: arr[pos--] = (byte)(val >> 8);
                case 1: arr[pos] = (byte) val; break;
            }
        }
    }

    public static void uint64ToBytes(final byte arr[], final int offset, final BigInteger val) {
      //big integer java guarantees unsigned value so no & needed
        ByteBuffer.wrap(arr, offset, LEN_UINT64).order(ByteOrder.LITTLE_ENDIAN).putLong(val.longValue());
    }

    public static void uint64ToBytes(final byte arr[], final long offset, final BigInteger val) {
        //big integer java guarantees unsigned value so no & needed
        ByteBuffer.wrap(arr, (int) offset, LEN_UINT64).order(ByteOrder.LITTLE_ENDIAN).putLong(val.longValue());
    }

    public static void uint64ToBytes(final byte arr[], final BigInteger offset, final BigInteger val) {
        //big integer java guarantees unsigned value so no & needed
        ByteBuffer.wrap(arr, offset.intValue(), LEN_UINT64).order(ByteOrder.LITTLE_ENDIAN).putLong(val.longValue());
    }

    public static void floatToBytes(final byte arr[], final int offset, final float val) {
        ByteBuffer.wrap(arr, offset, LEN_FLOAT).order(ByteOrder.LITTLE_ENDIAN).putFloat(val);
    }

    public static void floatToBytes(final byte arr[], final long offset, final float val) {
        ByteBuffer.wrap(arr, (int) offset, LEN_FLOAT).order(ByteOrder.LITTLE_ENDIAN).putFloat(val);
    }

    public static void floatToBytes(final byte arr[], final BigInteger offset, final float val) {
        ByteBuffer.wrap(arr, offset.intValue(), LEN_FLOAT).order(ByteOrder.LITTLE_ENDIAN).putFloat(val);
    }

    public static void realToBytes(final byte arr[], final int offset, final double val) {
        ByteBuffer.wrap(arr, offset, LEN_REAL).order(ByteOrder.LITTLE_ENDIAN).putDouble(val);
    }

    public static void realToBytes(final byte arr[], final long offset, final double val) {
        ByteBuffer.wrap(arr, (int) offset, LEN_REAL).order(ByteOrder.LITTLE_ENDIAN).putDouble(val);
    }

    public static void realToBytes(final byte arr[], final BigInteger offset, final double val) {
        ByteBuffer.wrap(arr, offset.intValue(), LEN_REAL).order(ByteOrder.LITTLE_ENDIAN).putDouble(val);
    }

    public static void link32ToBytes(final byte arr[], final int offset, final long val) {
        ByteBuffer.wrap(arr, offset, LEN_UINT32).order(ByteOrder.LITTLE_ENDIAN).putInt((int) (val & 0xFFFFFFFFL));
    }

    public static void link32ToBytes(final byte arr[], final long offset, final long val) {
        ByteBuffer.wrap(arr, (int) offset, LEN_UINT32).order(ByteOrder.LITTLE_ENDIAN).putInt((int) (val & 0xFFFFFFFFL));
    }

    public static void link64ToBytes(final byte arr[], final int offset, final BigInteger val) {
        //big integer java guarantees unsigned value so no & needed
        ByteBuffer.wrap(arr, offset, LEN_UINT64).order(ByteOrder.LITTLE_ENDIAN).putLong(val.longValue());
    }

    public static void ansiToBytes(final byte arr[], final int offset, final int maxlen, final String val) {
        Arrays.fill(arr, offset, offset + maxlen, (byte) 0);
        if (val != null && val.length() > 0) {
            final byte[] bytes = val.getBytes(StandardCharsets.ISO_8859_1);
            ByteBuffer.wrap(arr, offset, maxlen).put(bytes, 0, Math.min(bytes.length, maxlen));
        }
    }

    public static void utf8ToBytes(final byte arr[], final int offset, final int maxlen, final String val) {
        Arrays.fill(arr, offset, offset + maxlen, (byte) 0);
        if (val != null && val.length() > 0) {
            final byte[] bytes = val.getBytes(StandardCharsets.UTF_8);
            ByteBuffer.wrap(arr, offset, maxlen).put(bytes, 0, Math.min(bytes.length, maxlen));
        }
    }
}
