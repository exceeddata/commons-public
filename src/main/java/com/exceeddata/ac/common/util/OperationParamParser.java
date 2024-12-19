package com.exceeddata.ac.common.util;

import java.util.ArrayList;

/**
 * A util class for parsing operation param.
 */
public final class OperationParamParser {
    private char quoteChar = '`';
    private char separatorChar = ',';
    private char escapeChar = '\\';
    private boolean keepEscapeCharacter = false;
    
    public OperationParamParser() {}
    
    /**
     * Construct a <code>ParentheseParser</code> with parameters.
     * 
     * @param quoteChar the quote character
     * @param separatorChar the separator character
     * @param escapeChar  the escape character
     */
    public OperationParamParser (final char quoteChar, final char separatorChar, final char escapeChar) {
        this.quoteChar = quoteChar;
        this.separatorChar = separatorChar;
        this.escapeChar = escapeChar;
    }
    
    /**
     * Get quote character.
     * 
     * @return char
     */
    public final char getQuoteChar() { 
        return quoteChar; 
    }
    
    /**
     * Set quote character.
     * 
     * @param quoteChar the quote character
     * @return OperationParamParser
     */
    public final OperationParamParser setQuoteChar(final char quoteChar) { 
        this.quoteChar = quoteChar; 
        return this;
    }
    
    /**
     * Get separator character.
     * 
     * @return char
     */
    public final char getSeparatorChar() { return separatorChar; }
    
    /**
     * Set separator characters.
     * 
     * @param separatorChar the separator character
     * @return OperationParamParser
     */
    public final OperationParamParser setSeparatorChar(final char separatorChar) { 
        this.separatorChar = separatorChar; 
        return this;
    }
    
    /**
     * Get escape character.
     * 
     * @return char
     */
    public final char getEscapeChar() { return escapeChar; }
    
    /**
     * Set escape characters.
     * 
     * @param escapeChar the escape character
     * @return OperationParamParser
     */
    public final OperationParamParser setEscapeChar(final char escapeChar) { 
        this.escapeChar = escapeChar; 
        return this;
    }
    
    /**
     * Get whether to keep escape character.
     * 
     * @return true or false
     */
    public final boolean getKeepEscapeCharacter() {
        return keepEscapeCharacter;
    }
    
    /**
     * Set whether to keep escape character.
     * 
     * @param keep whether to keep the escape character
     * @return OperationParamParser
     */
    public final OperationParamParser setKeepEscapeChar(final boolean keep) {
        this.keepEscapeCharacter = keep;
        return this;
    }
    /**
     * Split a delimited string into string array.
     *  
     * @param str delimited string
     * @return String[]
     */
    public final String[] split (final String str) {
        if (XStringUtils.isNotBlank(str)) {
            final ArrayList<String>  pieces = new ArrayList<String>();
            final StringBuilder sb = new StringBuilder();
            final int length = str.length();
            boolean quoted = false, escaped = false;
            char c;
            
            for (int i = 0; i < length; ++i) {
                c = str.charAt(i);
                if (escaped) {
                    sb.append(c);
                    escaped = false;
                } else if (c == escapeChar) {
                    if (keepEscapeCharacter || i+1 >= length) {
                        sb.append(c);
                    }
                    escaped = true;
                } else if (c == quoteChar) {
                    if (quoted) {
                        if (i + 1 < length && str.charAt(i + 1) == quoteChar) {
                            //double quote escape, not yet unquote
                            if (keepEscapeCharacter) {
                                sb.append(c);
                            }
                            sb.append(c);
                            ++i;
                        } else {
                            sb.append(c);
                            quoted = false;
                        }
                    } else {
                        sb.append(c);
                        quoted = true;
                    }
                } else if (quoted) {
                    sb.append(c);
                } else if (c==separatorChar) {
                    pieces.add(sb.toString().trim());
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            }
            
            /* add last piece */
            if (sb.length()>0) {
                pieces.add(sb.toString().trim());
            }
            
            return pieces.toArray(new String[] {});
        } else {
            return new String[] {};
        }
    }
}
