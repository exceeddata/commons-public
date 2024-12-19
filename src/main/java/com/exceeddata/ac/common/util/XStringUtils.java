package com.exceeddata.ac.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * A class of static util functions for manipulating String
 */
public final class XStringUtils {
    private XStringUtils() {}
    
    public static final String[] quoteStringArray(final String[] strArray, final char quoteChar) {
        if (strArray == null || strArray.length == 0) {
            return new String[] {};
        }
        
        final String singleQuote = "" + quoteChar;
        final String doubleQuotes = "" + quoteChar + quoteChar;
        final String[] newStrs = new String[strArray.length];
        for (int i = 0; i < strArray.length; ++i) {
            final String str = strArray[i];
            final int len = str.length();
            if (str == null || len == 0) {
                newStrs[i]= doubleQuotes;
            } else  if (str.charAt(0) == quoteChar) {
                if (len > 1) {
                    if (str.charAt(len - 1) == quoteChar) {
                        newStrs[i] = str;
                    } else {
                        newStrs[i] = quoteChar + str.replace(singleQuote, doubleQuotes) + quoteChar;
                    }
                } else {
                    newStrs[i] = doubleQuotes + doubleQuotes;
                }
            } else {
                newStrs[i] = quoteChar + str.replace(singleQuote, doubleQuotes) + quoteChar;
            }
        }
        
        return newStrs;
    }
    
    public static final String[] quoteStringArrayWithCase(final String[] strArray, final char quoteChar, final boolean caseDirection) {
        if (strArray == null || strArray.length == 0) {
            return new String[] {};
        }
        
        final String singleQuote = "" + quoteChar;
        final String doubleQuotes = "" + quoteChar + quoteChar;
        final String[] newStrs = new String[strArray.length];
        for (int i = 0; i < strArray.length; ++i) {
            String str = strArray[i];
            int len = str.length();
            if (str == null || len == 0) {
                newStrs[i]= doubleQuotes;
            } else  if (str.charAt(0) == quoteChar) {
                if (len > 1) {
                    if (str.charAt(len - 1) == quoteChar) {
                        newStrs[i] = caseDirection ? str.toUpperCase() : str.toLowerCase();
                    } else {
                        newStrs[i] = quoteChar 
                                + (caseDirection ? str.toUpperCase().replace(singleQuote, doubleQuotes) : str.toLowerCase().replace(singleQuote, doubleQuotes)) 
                                + quoteChar;
                    }
                } else {
                    newStrs[i] = doubleQuotes + doubleQuotes;
                }
            } else {
                newStrs[i] = quoteChar 
                        + (caseDirection ? str.toUpperCase().replace(singleQuote, doubleQuotes) : str.toLowerCase().replace(singleQuote, doubleQuotes)) 
                        + quoteChar;
            }
        }
        
        return newStrs;
    }
    
    public static final String[] quoteCsvStringToArray(final String str, final char quoteChar) {
        return quoteStringArray(new SeparatorParser().split(str), quoteChar);
    }
    
    public static final String[] quoteCsvStringToArrayWithCase (final String str, final char quoteChar, final boolean caseDirection) {
        return quoteStringArrayWithCase(new SeparatorParser().split(str), quoteChar, caseDirection);
    }
    
    public static final String quoteCsvString (final String str, final char quoteChar) {
        final String [] quotedArray = quoteCsvStringToArray(str, quoteChar);
        final int len = quotedArray.length;
        if (len > 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append(quotedArray[0]);
            for (int i = 1; i < quotedArray.length; ++i) {
                sb.append(", ").append(quotedArray[i]);
            }
            return sb.toString();
        } else {
            return "";
        }
    }
    
    public static final String quoteCsvStringWithCase (final String str, final char quoteChar, final boolean caseDirection) {
        final String [] quotedArray = quoteCsvStringToArrayWithCase(str, quoteChar, caseDirection);
        final int len = quotedArray.length;
        if (len > 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append(quotedArray[0]);
            for (int i = 1; i < quotedArray.length; ++i) {
                sb.append(", ").append(quotedArray[i]);
            }
            return sb.toString();
        } else {
            return "";
        }
    }
    

    public static final String getStackTrace(Throwable t){
        final StringWriter sw = new StringWriter();
        final PrintWriter ps = new PrintWriter(sw);
        t.printStackTrace(ps);
        
        return sw.toString();
    }
    
    public static final String encode (final String str) {
        final StringBuilder sb = new StringBuilder();
        String urlEncodedStr;
        try {
            urlEncodedStr = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            urlEncodedStr = str;
        }
        
        final int len = urlEncodedStr.length();
        for (int i = 0; i< len; ++i) {
            char c = urlEncodedStr.charAt(i);
            switch (c) {
                case '+':
                case '-':
                case '*':
                case '%':
                case '?':
                case '.':
                    if (i == 0) {
                        sb.append('a');
                    }
                    sb.append('_');
                    break;
                default:
                    sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    public static boolean isBlank(final CharSequence cs) {
        final int len;
        if (cs == null || (len = cs.length()) == 0) {
            return true;
        } else {
            for (int i = 0; i < len; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public static boolean isNotBlank(final CharSequence cs) {
        final int len;
        if (cs == null || (len = cs.length()) == 0) {
            return false;
        } else {
            for (int i = 0; i < len; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
    
    public static boolean isNotEmpty(final CharSequence cs) {
        return cs != null && cs.length() > 0;
    }
    
    public static String join(final Iterator<?> iterator, final char separator) {
        if (iterator == null) {
            return null;
        } else if (!iterator.hasNext()) {
            return "";
        } else {
            Object obj = iterator.next();
            if (!iterator.hasNext()) {
                return obj == null ? "" : obj.toString();
            } else {
                final StringBuilder sb = new StringBuilder(256);
                if (obj != null) {
                    sb.append(obj);
                }
        
                while (iterator.hasNext()) {
                    if ((obj = iterator.next()) != null) {
                        sb.append(separator).append(obj);
                    } else {
                        sb.append(separator);
                    }
                }
                
                return sb.toString();
            }
        }
    }
    
    public static String join(final Iterable<?> iterable, final char separator) {
        return iterable != null ? join(iterable.iterator(), separator) : null;
    }
    
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        
        final int len = bytes.length;
        final StringBuilder sb = new StringBuilder(len * 2);
        for(int i=0; i < len; ++i){
            sb.append(Character.toUpperCase(Character.forDigit((bytes[i] >> 4) & 0xF, 16)));
            sb.append(Character.toUpperCase(Character.forDigit((bytes[i] & 0xF), 16)));
        }
        
        return sb.toString();
    }
    
    public static byte[] hexToBytes(String s) {
        if (isBlank(s)) {
            return null;
        }
        
        int len = s.length();
        StringBuilder sb = new StringBuilder(len);
        if((len & 1)!=0) {
        	len=len+1;
        	sb.append("0");
        	for(int i=0;i<len-1;i++) {
        		sb.append(s.charAt(i));
        	}        		
        	      
        }else {
        	for(int i=0;i<len;i++) {
        		sb.append(s.charAt(i));
        	}  
        }     	
        
        final byte[] data = new byte[len / 2];
        
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(sb.charAt(i), 16) << 4)
                                 + Character.digit(sb.charAt(i+1), 16));
        }
        
        return data;
    }
    
    public static void escapeJSON(final StringBuilder sb, final String string) {
        if (string == null || string.length() == 0) {
            sb.append("\"\"");
            return;
        }

        char         c = 0;
        int          i;
        int          len = string.length();
        String       t;

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
            case '\\':
            case '"':
                sb.append('\\');
                sb.append(c);
                break;
            case '/':
                sb.append('\\');
                sb.append(c);
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
               sb.append("\\r");
               break;
            default:
                if (c < ' ') {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
        sb.append('"');
    }
    
    public static String removeCodeComments(final String str) {
        if (XStringUtils.isBlank(str)) {
            return "";
        }
        
        final char escapeChar = '\\', singleQuotechar = '\'', doubleQuoteChar = '"', lineBreak = '\n';
        final StringBuilder sb = new StringBuilder();
        final int length = str.length();
            
        boolean singleQuoted = false, doubleQuoted = false, starCommented = false, lineCommented = false;
            
        for (int i = 0; i < length; ++i) {
            final char c = str.charAt(i);
            if (starCommented) {
                if (c == '*') {
                    if (i + 1 == length) {
                        break; //end
                    } else if (str.charAt(i + 1) == '/') {
                        ++i;
                        starCommented = false;
                    }
                }
            } else if (lineCommented) {
                if (c == lineBreak) {
                    lineCommented = false;
                    sb.append(c);
                }
            } else if (c == escapeChar) {
                if (++i < length) {
                    sb.append(c);
                    sb.append(str.charAt(i));
                } else {
                    sb.append(c);
                }
            } else if (c == singleQuotechar) {
                if (singleQuoted) {
                    if (i+1 < length && str.charAt(i+1) == singleQuotechar) {
                        //double quote escape, not yet unquote
                        sb.append(c);
                        sb.append(c);
                        ++i;
                    } else {
                        // ready to unquote
                        sb.append(c);
                        singleQuoted = false;
                    }
                } else {
                    sb.append(c);
                    singleQuoted = true;
                }
            } else if (singleQuoted) {
                sb.append(c);
            } else if (c == doubleQuoteChar) {
                if (doubleQuoted) {
                    if (i+1 < length && str.charAt(i+1) == doubleQuoteChar) {
                        //double quote escape, not yet unquote
                        sb.append(c);
                        sb.append(c);
                        ++i;
                    } else {
                        // ready to unquote
                        sb.append(c);
                        doubleQuoted = false;
                    }
                } else {
                    sb.append(c);
                    doubleQuoted = true;
                }
            } else if (doubleQuoted) {
                sb.append(c);
            } else if (c == '/') {
                if (i + 1 == length) {
                    sb.append(c); //end
                } else if (str.charAt(i + 1) == '*') {
                    ++i;
                    sb.append(' '); //need to insert a space in case no separation
                    starCommented = true;
                } else if (str.charAt(i + 1) == '/') {
                    ++i;
                    sb.append(' '); //need to insert a space in case no separation
                    lineCommented = true;
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
