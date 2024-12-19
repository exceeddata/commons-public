package com.exceeddata.ac.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A util class for parsing space-separated string fields, this is needed because csv parser
 * has problems dealing with non-quoted backslash.
 */
public final class SpaceSimpleParser {
    private SpaceSimpleParser() {}
    
    /**
     * Split a optionally-quoted space delimited string into string array.
     *  
     * @param str delimited string
     * @return String[]
     */
    public static List<String> split (final String str) {
        if (XStringUtils.isBlank(str)) {
            return new ArrayList<>();
        }
        
        final ArrayList<String>  pieces = new ArrayList<String>();
        final StringBuilder sb = new StringBuilder();
        final int length = str.length();
        char c;
        
        for (int i = 0; i < length; ++i) {
            c = str.charAt(i);
            if ( c == ' ' || c == '\t') {
                if (sb.length() != 0) {  //ignore continuous space
                    pieces.add(sb.toString());
                    sb.setLength(0);
                }
            } else {
                sb.append(c);
            }
        }
        
        if (sb.length() != 0) {
            pieces.add(sb.toString());
        } else {
            c = str.charAt(length - 1);
            if (c == ' ' || c == '\t') {
                pieces.add(""); //protect against null last entry
            }
        }
        
        return pieces;
    }
}
