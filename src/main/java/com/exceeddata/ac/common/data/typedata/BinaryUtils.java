package com.exceeddata.ac.common.data.typedata;

import java.nio.charset.StandardCharsets;

public final class BinaryUtils {
    protected static final byte ZERO = (byte) 0;
   
    protected static String deserializeToString(final byte[] bytes) {
        if (bytes == null) {
            return null;
        } else if (bytes.length == 0) {
            return "";
        } else {
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
    
    private BinaryUtils() {}
}
