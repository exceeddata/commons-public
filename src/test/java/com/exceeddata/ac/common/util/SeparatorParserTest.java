package com.exceeddata.ac.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SeparatorParserTest {
    @Test
    public void testClassDefinitions() {
        final String definitions = "HDFSOutputConnector?id=c58,input=c60(output.path=`/user/root/nick/partition2`,output.format=`csv`)";
        final String[] defstrs = new SeparatorParser(';', '`', '\\', ' ', true, true).split(definitions);
        assertEquals(defstrs[0], definitions);
    }
    
    @Test
    public void testDoubleQuoteEscape()  {
        final SeparatorParser parser = new SeparatorParser('=', '`', '\\', ' ');
        String[] parsed;
        
        parser.setKeepEscapeChar(true);
        parsed = parser.split("expressions=`compact('[0-9]', 'match(_value, ``2$``)') as c`");
        assertEquals("expressions", parsed[0]);
        assertEquals("compact('[0-9]', 'match(_value, ``2$``)') as c", parsed[1]);
        
        parser.setKeepEscapeChar(false);
        parsed = parser.split("expressions=`compact('[0-9]', 'match(_value, ``2$``)') as c`");
        assertEquals("expressions", parsed[0]);
        assertEquals("compact('[0-9]', 'match(_value, `2$`)') as c", parsed[1]);
        
        final SeparatorParser parser2 = new SeparatorParser(',', '\'', '\\', ' ', false, true);
        parsed = parser2.split("compact('[0-9]', 'match(_value, ''2$'')') as c");
        assertEquals("compact('[0-9]'", parsed[0]);
        assertEquals("'match(_value, '2$')') as c", parsed[1]);
    }
    
    @Test
    public void testParam() {
        final SeparatorParser parser = new SeparatorParser('=', '`', '\\', ' ');
        String[] parsed;
        
        parsed = parser.split("expressions=`compact('[0-9]', 'match(_value, \\\\'2$\\\\')') as c`");
        assertEquals("expressions", parsed[0]);
        assertEquals("compact('[0-9]', 'match(_value, \\'2$\\')') as c", parsed[1]);
    }
    
    @Test
    public void testBackslash() {
        final SeparatorParser parser = new SeparatorParser(',', '"', '\\', ' ');
        String[] parsed;
        
        parsed = parser.split("abc,\"\\\\def\"");
        assertEquals("abc", parsed[0]);
        assertEquals("\\def", parsed[1]);
    }
    
    @Test
    public void test() {
        final SeparatorParser parser = new SeparatorParser(',', '"', '\\', ' ');
        String[] parsed;

        //non-quotes
        parsed = parser.split("abc,def");
        assertEquals("abc", parsed[0]);
        assertEquals("def", parsed[1]);
        
        //quotes
        parsed = parser.split("\"abc\",\"def\"");
        assertEquals("abc", parsed[0]);
        assertEquals("def", parsed[1]);
        
        //mixed quotes
        parsed = parser.split("1,\"def\",c,\"ghi\", jkl , 5  , \" mno \"");
        assertEquals("1", parsed[0]);
        assertEquals("def", parsed[1]);
        assertEquals("c", parsed[2]);
        assertEquals("ghi", parsed[3]);
        assertEquals("jkl", parsed[4]);
        assertEquals("5", parsed[5]);
        assertEquals(" mno ", parsed[6]);

        //separator in quotes quotes
        parsed = parser.split("1,\"def, c\",c,\"ghi\", jkl , 5  , \" mno \"");
        assertEquals("1", parsed[0]);
        assertEquals("def, c", parsed[1]);

        //escaped quotes in quotes
        parsed = parser.split("1,\"def, c\",c,\" \\\"ghi\", jkl , 5  , \" mno \"");
        assertEquals("1", parsed[0]);
        assertEquals(" \"ghi", parsed[3]);

        //bad quoted
        parsed = parser.split("1,\"def, c\" \"bac\" ,c,\" \\\"ghi\", jkl , 5  , \" mno \"");
        assertEquals("1", parsed[0]);
        assertEquals("\"def, c\" \"bac\"", parsed[1]);
        
        //unended quoted
        parsed = parser.split("1,\"def, c\" \"bac\" ,c,\" \\\"ghi\", jkl , 5  , \" mno ");
        assertEquals("1", parsed[0]);
        assertEquals("\" mno", parsed[6]);
        
        //different separator
        parser.setSeparatorChar(':');
        parsed = parser.split("\"\\\"abc\\\".\\\"def\\\"\":\"def\"");
        assertEquals("\"abc\".\"def\"", parsed[0]);
        assertEquals("def", parsed[1]);
    }

}
