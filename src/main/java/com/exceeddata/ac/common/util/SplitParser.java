package com.exceeddata.ac.common.util;

import java.util.ArrayList;

/**
 * A util class for parsing separated string fields enclosed in pair of parenthese.
 */
public final class SplitParser {
    private char parentheseOpenChar = '(';
    private char parentheseCloseChar = ')';
    private char separatorChar = ',';
    private char escapeChar = '\\';
    private boolean keepEscapeChar = false;
    
    public SplitParser() {}
    
    /**
     * Construct a <code>ParentheseParser</code> with parameters.
     * 
     * @param parentheseOpenChar the parentheses open character
     * @param parentheseCloseChar  the parentheses close character
     * @param separatorChar the separator character
     * @param escapeChar the escape character
     */
    public SplitParser (final char parentheseOpenChar, final char parentheseCloseChar, 
            final char separatorChar, final char escapeChar) {
        this.separatorChar = separatorChar;
        this.parentheseOpenChar = parentheseOpenChar;
        this.parentheseCloseChar = parentheseCloseChar;
        this.escapeChar = escapeChar;
    }
    
    /**
     * Construct a <code>ParentheseParser</code> with parameters.
     * 
     * @param parentheseOpenChar the parentheses open character
     * @param parentheseCloseChar the parentheses close character
     * @param separatorChar the separator character
     * @param escapeChar the escape character
     * @param keepEscapeChar, whether to keep the escape character
     */
    public SplitParser (final char parentheseOpenChar, final char parentheseCloseChar, 
            final char separatorChar, final char escapeChar, final boolean keepEscapeChar) {
        this.separatorChar = separatorChar;
        this.parentheseOpenChar = parentheseOpenChar;
        this.parentheseCloseChar = parentheseCloseChar;
        this.escapeChar = escapeChar;
        this.keepEscapeChar = keepEscapeChar;
    }
    
    /**
     * Get parentheses open character.
     * 
     * @return char
     */
    public final char getParentheseOpenChar() { 
        return parentheseOpenChar; 
    }
    
    /**
     * Set parentheses open characters.
     * 
     * @param parentheseOpenChar the parathese open character
     * @return SplitParser
     */
    public final SplitParser setParentheseOpenChar(final char parentheseOpenChar) { 
        this.parentheseOpenChar = parentheseOpenChar; 
        return this;
    }
    
    /**
     * Get parentheses close character.
     * 
     * @return char
     */
    public final char getParentheseCloseChar() { return parentheseCloseChar; }
    
    /**
     * Set parentheses close characters.
     * 
     * @param parentheseCloseChar the parathese close character
     * @return SplitParser
     */
    public final SplitParser setParentheseCloseChar(final char parentheseCloseChar) { 
        this.parentheseCloseChar = parentheseCloseChar; 
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
     * @return SplitParser
     */
    public final SplitParser setSeparatorChar(char separatorChar) { 
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
     * @return SplitParser
     */
    public final SplitParser setEscapeChar(char escapeChar) { 
        this.escapeChar = escapeChar; 
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
     * @param keepEscapeChar whether to keep escape char
     * @return SplitParser
     */
    public final SplitParser setKeepEscapeChar(boolean keepEscapeChar) { 
        this.keepEscapeChar = keepEscapeChar; 
        return this;
    }   
    
    /**
     * Split a parentheses-enclosed delimited string into string array.
     *  
     * @param str delimited string
     * @return String[]
     */
    public final String[] split (final String str) {
        if (XStringUtils.isNotBlank(str)) {
            final ArrayList<String>  pieces = new ArrayList<String>();
            final StringBuilder sb = new StringBuilder();
            
            final int length = str.length();
            int beginPieceIndex = 0;
            
            /*
             * For the case of [xx [xxx]]
             */
            int parentheseNestDepth = 0;
            
            /* 
             * For the case for [xxxx][xxx],[xxx], where the first piece [xxxx][xxx]
             * should not have parenthese removed.
             */
            int parentheseNumber = 0; 
            
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
                } else if (c==separatorChar && parentheseNestDepth == 0 ) {
                    if (i == beginPieceIndex) {
                        pieces.add("");
                    } else if (parentheseNumber > 1) {
                        pieces.add(sb.toString().trim());
                    } else {
                        final String substr = sb.toString().trim();
                        final int sublen = substr.length();
                        int contentBeginIndex = 0;
                        int contentEndIndex = sublen-1;
                        
                        if (sublen > 0 && substr.charAt(contentBeginIndex) == parentheseOpenChar
                                && substr.charAt(contentEndIndex) == parentheseCloseChar) {
                            if (++contentBeginIndex == contentEndIndex) {
                                pieces.add("");
                            } else {
                                pieces.add(substr.substring(contentBeginIndex, contentEndIndex));
                            }
                        } else {
                            pieces.add(substr);
                        }
                    }
                    
                    beginPieceIndex = i+1;
                    parentheseNumber = 0;
                    sb.setLength(0);
                } else {
                    sb.append(c);
                    
                    if (c == parentheseOpenChar) {
                        parentheseNestDepth++;
                    } else if (c == parentheseCloseChar) {
                        if (parentheseNestDepth > 0) {
                            parentheseNestDepth--;
                        }
                        
                        if (parentheseNestDepth == 0) {
                            parentheseNumber++;
                        }
                    }
                }
            }
            
            /* add last piece */
            if (beginPieceIndex == length) {
                pieces.add("");
            } else if (parentheseNumber > 1) {
                pieces.add(sb.toString().trim());
            } else {
                final String substr = sb.toString().trim();
                final int sublen = substr.length();
                int contentBeginIndex = 0;
                int contentEndIndex = sublen-1;

                if (sublen > 0 && substr.charAt(contentBeginIndex) == parentheseOpenChar
                        && substr.charAt(contentEndIndex) == parentheseCloseChar) {
                    if (++contentBeginIndex == contentEndIndex) {
                        pieces.add("");
                    } else if (parentheseNestDepth > 0) {
                        pieces.add(substr);
                    } else {
                        pieces.add(substr.substring(contentBeginIndex, contentEndIndex));
                    }
                } else {
                    pieces.add(substr);
                }
            }

            return pieces.toArray(new String[] {});
        } else {
            return new String[] {};
        }
    }
}
