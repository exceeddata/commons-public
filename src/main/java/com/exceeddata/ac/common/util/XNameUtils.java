package com.exceeddata.ac.common.util;

import java.util.ArrayList;
import java.util.List;

public final class XNameUtils {
    private XNameUtils() {}
    
    public static final char QUOTER = '\'';
    public static final char SEPARATOR = ',';
    public static final char ESCAPER = '\\';
    
    public static final String DEFAULT_KEY_NAME = "_key";
    public static final String DEFAULT_VALUE_NAME = "value";
    public static final String DEFAULT_FAMILY_NAME = "_family";
    public static final String DEFAULT_TIMESTAMP_NAME = "_timestamp";
    public static final String INNER_KEY_NAME = "_key";
    public static final String INNER_VALUE_NAME = "_value";
    
    private static final OperationParamParser NAME_PARSER = new OperationParamParser(
            QUOTER, SEPARATOR, ESCAPER);
    
    /**
     * Quote an name from a name String.
     * 
     * @param name the name
     * @return String
     */
    public static final String quoteName (final String name) {
        final StringBuilder sb = new StringBuilder(name.length() + 10);
        sb.append(QUOTER);
        
        char c;
        for (int i = 0, s = name.length(); i < s; ++i) {
            c = name.charAt(i);
            if (c == ESCAPER) {
                sb.append(ESCAPER).append(ESCAPER);
            } else if (c == QUOTER) {
                sb.append(QUOTER).append(QUOTER);
            } else {
                sb.append(c);
            }
        }
        sb.append(QUOTER);
        return sb.toString();
    }
    
    /**
     * Parse an name from a name String.
     * 
     * @param name the name
     * @return String
     */
    public static final String parseName (final String name) {
        final String[] parsed = NAME_PARSER.split(name);
        if (parsed.length > 0) {
            if (parsed[0].charAt(0) == QUOTER && parsed[0].charAt(parsed[0].length() - 1) == QUOTER) {
                return parsed[0].substring(1, parsed[0].length() - 1);
            } else {
                return parsed[0];
            }
        } else {
            return "";
        }
    }
    
    /**
     * Parse an name array from a String of comma separated names.
     * 
     * @param names the names
     * @return String[] the parsed names
     */
    public static final String[] parseNames (final String names) {
        final String[] parsed = NAME_PARSER.split(names);
        final List<String> contents = new ArrayList<String>();
        for (final String name : parsed) {
            if (XStringUtils.isNotBlank(name)) {
                if (name.charAt(0) == QUOTER && name.charAt(name.length() - 1) == QUOTER) {
                    final String content = name.substring(1, name.length() - 1);
                    if (XStringUtils.isNotBlank(content)) {
                        contents.add(content);
                    }
                } else {
                    contents.add(name);
                }
            }
        }
        return contents.toArray(new String[]{});
    }
    
    public static final String constructNames(final String[] names) {
        final StringBuilder sb = new StringBuilder();
        final StringBuilder tb = new StringBuilder(128);
        boolean specialCharacters;
        char c;
        
        for (final String name : names) {
            if (XStringUtils.isBlank(name)) {
                continue;
            }
            
            specialCharacters = false;
            tb.setLength(0);
            for (int i = 0, s = name.length(); i < s; ++i) {
                c = name.charAt(i);
                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {
                    tb.append(c);
                } else if ((c >= '0' && c <= '9') || c == '.') {
                    tb.append(c);
                    if (i == 0) {
                        specialCharacters = true;
                    }
                } else if (c == ESCAPER || c == QUOTER) {
                    tb.append(c).append(c);
                    specialCharacters = true;
                } else {
                    tb.append(c);
                    specialCharacters = true;
                }
            }
            if (specialCharacters) {
                sb.append(QUOTER).append(tb).append(QUOTER).append(SEPARATOR);
            } else {
                sb.append(tb).append(SEPARATOR);
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
    
    public static final String constructSelects(final String[] names) {
        final StringBuilder sb = new StringBuilder();
        final StringBuilder tb = new StringBuilder(128);
        boolean specialCharacters;
        char c;
        
        for (final String name : names) {
            if (XStringUtils.isBlank(name)) {
                continue;
            }
            
            specialCharacters = false;
            tb.setLength(0);
            for (int i = 0, s = name.length(); i < s; ++i) {
                c = name.charAt(i);
                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {
                    tb.append(c);
                } else if ((c >= '0' && c <= '9') || c == '.') {
                    tb.append(c);
                    if (i == 0) {
                        specialCharacters = true;
                    }
                } else if (c == ESCAPER || c == QUOTER) {
                    tb.append(c).append(c);
                    specialCharacters = true;
                } else {
                    tb.append(c);
                    specialCharacters = true;
                }
            }
            if (specialCharacters) {
                sb.append("n'").append(tb).append("'").append(SEPARATOR);
            } else {
                sb.append(tb).append(SEPARATOR);
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
