package com.exceeddata.ac.common.util;

import java.io.IOException;

import junit.framework.TestCase;

public class ParentheseParserTest extends TestCase {
    public void testQuotes() throws IOException{
        ParentheseParser parser = new ParentheseParser('(', ')', ',', '`', '\\');
        String[] pieces = parser.split("(expressions=`compact('[0-9]', 'match(_value, \\\\\\\\'2$\\\\\\\\')') as c`), (foo)");
        assertEquals (pieces.length, 2);
        assertEquals (pieces[0], "expressions=`compact('[0-9]', 'match(_value, \\\\'2$\\\\')') as c`");
        assertEquals (pieces[1], "foo");
    
    }
    public void test() throws IOException{
        ParentheseParser parser = new ParentheseParser('(', ')', ',', '`', '\\');
        
        String[] pieces = parser.split("(field4, field3, field2, field1), (), (field1 String, field2 String, field3 String, field4 String)");
        assertEquals (pieces[0], "field4, field3, field2, field1");
        assertEquals (pieces[1], "");
        assertEquals (pieces[2], "field1 String, field2 String, field3 String, field4 String");
    
        parser.setSeparatorChar(':');
        String[] pieces2 = parser.split("(group1):(group2)");
        assertEquals (pieces2[0], "group1");
        assertEquals (pieces2[1], "group2");
        
        pieces2 = parser.split("group1:group2");
        assertEquals (pieces2[0], "group1");
        assertEquals (pieces2[1], "group2");
        
        pieces2 = parser.split("group1:(group2)");
        assertEquals (pieces2[0], "group1");
        assertEquals (pieces2[1], "group2");
        
        parser.setSeparatorChar('@');
        pieces2 = parser.split("(group1):(group2@(alias3)");
        assertEquals (pieces2[0], "(group1):(group2@(alias3)");
        
        parser.setKeepEscapeChar(true);
        pieces2 = parser.split("(group2@\\(alias3)@alias3");
        assertEquals (pieces2[0], "group2@\\(alias3");
        
        parser.setKeepEscapeChar(false);
        pieces2 = parser.split("(group2@\\(alias3)@alias3");
        assertEquals (pieces2[0], "group2@(alias3");
        
        pieces2 = parser.split("group1:(group2)@alias3");
        assertEquals (pieces2[0], "group1:(group2)");
        assertEquals (pieces2[1], "alias3");
        
        pieces2 = parser.split("(group1):group2@(alias3)");
        assertEquals (pieces2[0], "(group1):group2");
        assertEquals (pieces2[1], "alias3");
        
        parser.setSeparatorChar('@');
        pieces2 = parser.split("(group1):(group2)@(alias3)");
        assertEquals (pieces2[0], "(group1):(group2)");
        assertEquals (pieces2[1], "alias3");
        
        parser.setSeparatorChar(':');
        String[] pieces3 = parser.split(pieces2[0]);
        assertEquals (pieces3[0], "group1");
        assertEquals (pieces3[1], "group2");
        
        parser = new ParentheseParser('(', ')', ',', '\'', '\\', true);
        String[] pieces4 = parser.split("('associate''s test', 'c')");
        SeparatorParser sp = new SeparatorParser(',', '\'', '\\', ' ').setQuoteChar('\'');
        String[] subpieces = sp.split(pieces4[0]);
        assertEquals (subpieces[0], "associate\'s test");
    }
}
