package com.exceeddata.ac.common.util;

import java.util.ArrayList;

/**
 * A util class for parsing space-separated string fields, this is needed because csv parser
 * has problems dealing with non-quoted backslash.
 */
public final class SpaceParser {
    private char spaceChar = ' ';
    private char quoteChar = '\"';
    private char escapeChar = '\\';
    private boolean keepEscapeChar = false;
    
    public SpaceParser() {}
    
    /**
     * Construct a <code>SpaceParser</code> with parameters.
     * 
     * @param spaceChar the space character
     * @param quoteChar the quote character
     * @param escapeChar the escape character
     */
    public SpaceParser (
            final char spaceChar, 
            final char quoteChar,
            final char escapeChar) {
        this.spaceChar = spaceChar;
        this.quoteChar = quoteChar;
        this.escapeChar = escapeChar;
    }
    
    /**
     * Construct a <code>SpaceParser</code> with parameters.
     * 
     * @param spaceChar the space character
     * @param quoteChar the quote character
     * @param escapeChar the escape character
     * @param keepEscapeChar whether to keep the escape character
     * @param trimWhiteSpaces whether to trim white spaces
     */
    public SpaceParser (
            final char spaceChar, 
            final char quoteChar,
            final char escapeChar, 
            final boolean keepEscapeChar,
            final boolean trimWhiteSpaces) {
        this.spaceChar = spaceChar;
        this.quoteChar = quoteChar;
        this.escapeChar = escapeChar;
        this.keepEscapeChar = keepEscapeChar;
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
     * @return SpaceParser
     */
    public final SpaceParser setQuoteChar(final char quoteChar) { 
        this.quoteChar = quoteChar; 
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
     * @return SpaceParser
     */
    public final SpaceParser setEscapeChar(char escapeChar) { 
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
     * @return SpaceParser
     */
    public final SpaceParser setSpaceChar(char spaceChar) { 
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
     * @return SpaceParser
     */
    public final SpaceParser setKeepEscapeChar(boolean keepEscapeChar) { 
        this.keepEscapeChar = keepEscapeChar; 
        return this;
    }
    
    /**
     * Split a optionally-quoted space delimited string into string array.
     *  
     * @param str delimited string
     * @return String[]
     */
    public final String[] split (final String str) {
        if (XStringUtils.isNotBlank(str)) {
            final ArrayList<String>  pieces = new ArrayList<String>();
            final StringBuilder sb = new StringBuilder();
            final int length = str.length();
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
                            if ((next = peekSeparator(str, i+1)) > 0) {
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
                } else if (c==spaceChar) {
                    if (quoted) {
                        sb.append(c);
                    } else {
                        if (sb.length() > 0) {  //ignore continous space
                            pieces.add(sb.toString());
                            sb.setLength(0);
                        }
                        badquotes = false;
                    }
                } else {
                    sb.append(c);
                }
            }
            
            if (sb.length() > 0) {
                pieces.add(sb.toString());
            } else if (str.charAt(length - 1) == spaceChar) {
                pieces.add(""); //protect against null last entry
            }

            return pieces.toArray(new String[] {});
        } else {
            return new String[] {};
        }
    }
    
    private int peekSeparator(final String str, final int index) {
        final int length = str.length();
        if (index >= length) {
            return index;  //already at end.
        }
        int offset = index;
        char c = str.charAt(offset);
        while (offset < length && c != spaceChar) {
            c = str.charAt(++offset);
        }
        
        if (offset == length || c == spaceChar) { //end or finding separator
            return offset;
        } else {
            return -1;
        }
    }
}
