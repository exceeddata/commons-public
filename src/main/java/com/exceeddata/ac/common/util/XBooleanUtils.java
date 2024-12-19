package com.exceeddata.ac.common.util;

public final class XBooleanUtils {
    private XBooleanUtils() {}
    
    public static Boolean getBoolean(final String s) {
        if (XStringUtils.isNotBlank(s)) {
            switch(s.trim().toLowerCase()) {
                case "false":
                case "no":
                case "0" : 
                case "否":
                    return Boolean.FALSE;
                default: return Boolean.TRUE;
            }
        } else {
            return null;
        }
    }
    
    public static boolean getBoolean(final String s, boolean defaultValue) {
        if (XStringUtils.isNotBlank(s)) {
            switch(s.trim().toLowerCase()) {
                case "false":
                case "no":
                case "0" :  
                case "否": return false;
                default: return true;
            }
        } else {
            return defaultValue;
        }
    }
}
