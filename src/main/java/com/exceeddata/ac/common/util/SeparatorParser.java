package com.exceeddata.ac.common.util;

import java.util.ArrayList;

/**
 * An util class for parsing separated string fields, this is needed because csv parser
 * has problems dealing with non-quoted backslash.
 */
public final class SeparatorParser {
    private char separatorChar = ',';
    private char quoteChar = '\"';
    private char escapeChar = '\\';
    private char spaceChar = ' ';
    private boolean keepEscapeChar = false;
    private boolean trimWhiteSpaces = true;
    
    public SeparatorParser() {}
    
    /**
     * Construct a <code>SeparatorParser</code> with parameters.
     * 
     * @param separatorChar the separator character
     * @param quoteChar the quote character
     * @param escapeChar the escape character
     * @param spaceChar the space character
     */
    public SeparatorParser (
            final char separatorChar, 
            final char quoteChar,
            final char escapeChar,
            final char spaceChar) {
        this.separatorChar = separatorChar;
        this.quoteChar = quoteChar;
        this.escapeChar = escapeChar;
        this.spaceChar = spaceChar;
    }
    
    /**
     * Construct a <code>SeparatorParser</code> with parameters.
     * 
     * @param separatorChar the separator character
     * @param quoteChar the quote character
     * @param escapeChar the escape character
     * @param spaceChar the space character
     * @param keepEscapeChar whether to keep the escape character
     * @param trimWhiteSpaces whether to trim white spaces
     */
    public SeparatorParser (
            final char separatorChar, 
            final char quoteChar,
            final char escapeChar, 
            final char spaceChar, 
            final boolean keepEscapeChar,
            final boolean trimWhiteSpaces) {
        this.separatorChar = separatorChar;
        this.quoteChar = quoteChar;
        this.escapeChar = escapeChar;
        this.spaceChar = spaceChar;
        this.keepEscapeChar = keepEscapeChar;
        this.trimWhiteSpaces = trimWhiteSpaces;
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
     * @return SeparatorParser
     */
    public final SeparatorParser setQuoteChar(final char quoteChar) { 
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
     * Set separator character.
     * 
     * @param separatorChar the separator character
     * @return SeparatorParser
     */
    public final SeparatorParser setSeparatorChar(char separatorChar) { 
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
     * Set escape character.
     * 
     * @param escapeChar the escape character
     * @return SeparatorParser
     */
    public final SeparatorParser setEscapeChar(char escapeChar) { 
        this.escapeChar = escapeChar; 
        return this;
    }
    
    /**
     * Get space character.
     * 
     * @return char
     */
    public final char getSpaceChar() { return spaceChar; }
    
    /**
     * Set space character.
     * 
     * @param spaceChar the space character
     * @return SeparatorParser
     */
    public final SeparatorParser setSpaceChar(char spaceChar) { 
        this.spaceChar = spaceChar; 
        return this;
    }
    
    /**
     * Get whether to keep escape character, default is false. Set 
     * to true if the string needs to be be parsed again.
     * 
     * @return true or false
     */
    public final boolean getKeepEscapeChar() { return keepEscapeChar; }
    
    /**
     * Set whether to keep escape characters.
     * 
     * @param keepEscapeChar whether to keep the escape character
     * @return SeparatorParser
     */
    public final SeparatorParser setKeepEscapeChar(boolean keepEscapeChar) { 
        this.keepEscapeChar = keepEscapeChar; 
        return this;
    }
    
    /**
     * Get whether to trim white space characters when not quoted, default is true. Set 
     * to false if space is meaningful.
     * 
     * @return true or false
     */
    public final boolean getTrimWhiteSpaces() { return trimWhiteSpaces; }
    
    /**
     * Set whether to trim white space characters when not quoted.
     * 
     * @param trimWhiteSpaces whether to trim white spaces
     * @return SeparatorParser
     */
    public final SeparatorParser setTrimWhiteSpaces(boolean trimWhiteSpaces) { 
        this.trimWhiteSpaces = trimWhiteSpaces; 
        return this;
    }
    
    /**
     * Split a optionally-quoted delimited string into string array.
     *  
     * @param str delimited string
     * @return String[]
     */
    public final String[] split (final String str) {
        if (XStringUtils.isNotBlank(str)) {
            final ArrayList<String>  pieces = new ArrayList<String>();
            final StringBuilder sb = new StringBuilder();
            final int length = str.length(), lastIndex = length - 1;
            int next = 0;
            boolean quoted = false, badquotes = false;
            String piece;
            
            for (int i = 0; i < length; ++i) {
                final char c = str.charAt(i);
                if (c == escapeChar) {
                    if (++i < length) {
                        if (keepEscapeChar) {
                            sb.append(c);
                        }
                        sb.append(str.charAt(i));
                    } else {
                        sb.append(c);
                    }
                } else if (c == quoteChar) {
                    if (quoted) {
                        if (i+1 < length && str.charAt(i+1) == quoteChar) {
                            //double quote escape, not yet unquote
                            if (keepEscapeChar) {
                                sb.append(c);
                            }
                            sb.append(c);
                            ++i;
                        } else {
                            // ready to unquote
                            if ((next = peekSeparator(str, lastIndex, i+1)) > 0) {
                                sb.append(c);
                                piece = sb.toString().trim();
                                if (badquotes == false && piece.charAt(0) == quoteChar) {
                                    piece= piece.substring(1, piece.length() - 1);
                                }
                                pieces.add(piece);
                                sb.setLength(0);
                                badquotes = false; //clear bad quotes
                                i = next;
                            } else {
                                sb.append(c);
                                badquotes = true;
                            }
                            quoted = false;
                        }
                    } else {
                        sb.append(c);
                        quoted = true;
                    }
                } else if (c==separatorChar) {
                    if (quoted) {
                        sb.append(c);
                    } else {
                        pieces.add(trimWhiteSpaces ? sb.toString().trim() : sb.toString());
                        sb.setLength(0);
                        badquotes = false;
                    }
                } else {
                    sb.append(c);
                }
            }
            
            if (sb.length() > 0) {
                pieces.add(trimWhiteSpaces ? sb.toString().trim() : sb.toString());
            } else if (str.charAt(length - 1) == separatorChar) {
                pieces.add(""); //protect against null last entry
            }

            return pieces.toArray(new String[] {});
        } else {
            return new String[] {};
        }
    }
    
    private int peekSeparator(final String str, final int lastIndex, final int index) {
        if (index > lastIndex) {
            return index;  //already at end.
        }
        int offset = index;
        char c = str.charAt(offset);
        while (c != separatorChar && c == spaceChar && offset != lastIndex) {
            c = str.charAt(++offset);
        }
        
        if (offset > lastIndex || c == separatorChar) { //end or finding separator
            return offset;
        } else {
            return -1;
        }
    }
}
