package com.exceeddata.ac.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SeparatorParserCsvTest {
    @Test
    public void testBackslash() {
        final SeparatorParser parser = new SeparatorParser().setTrimWhiteSpaces(false);
        String[] parsed;
        
        parsed = parser.split("abc,\"\\\\def\"");
        assertEquals("abc", parsed[0]);
        assertEquals("\\def", parsed[1]);
    }
    
    @Test
    public void test() {
        final SeparatorParser parser = new SeparatorParser().setTrimWhiteSpaces(false);
        String[] parsed;

        //non-quotes
        parsed = parser.split("abc,def");
        assertEquals("abc", parsed[0]);
        assertEquals("def", parsed[1]);
        
        //quotes
        parsed = parser.split("\"abc\",\"def\"");
        assertEquals("abc", parsed[0]);
        assertEquals("def", parsed[1]);
        
        //inside quotes
        parsed = parser.split("\"\"\"abc\",\"def\"\"\"");
        assertEquals("\"abc", parsed[0]);
        assertEquals("def\"", parsed[1]);
        
        //null last entry
        parsed = parser.split("\"\"\"abc\",\"def\"\"\",");
        assertEquals(3, parsed.length);
        assertEquals("\"abc", parsed[0]);
        assertEquals("def\"", parsed[1]);
        assertEquals("", parsed[2]);
        
        parsed = parser.split(",");
        assertEquals(2, parsed.length);
        assertEquals("", parsed[0]);
        assertEquals("", parsed[1]);
        
        parsed = parser.split(",,");
        assertEquals(3, parsed.length);
        assertEquals("", parsed[0]);
        assertEquals("", parsed[1]);
        assertEquals("", parsed[2]);
        
        parsed = parser.split("a,b,");
        assertEquals(3, parsed.length);
        assertEquals("a", parsed[0]);
        assertEquals("b", parsed[1]);
        assertEquals("", parsed[2]);
        
        //mixed quotes
        parsed = parser.split("1,\"def\",c,\"ghi\", jkl , 5  , \" mno \"");
        assertEquals("1", parsed[0]);
        assertEquals("def", parsed[1]);
        assertEquals("c", parsed[2]);
        assertEquals("ghi", parsed[3]);
        assertEquals(" jkl ", parsed[4]);
        assertEquals(" 5  ", parsed[5]);
        assertEquals(" mno ", parsed[6]);

        //different separator
        parser.setSeparatorChar(':');
        parsed = parser.split("\"\\\"abc\\\".\\\"def\\\"\":\"def\"");
        assertEquals("\"abc\".\"def\"", parsed[0]);
        assertEquals("def", parsed[1]);
    }

}
