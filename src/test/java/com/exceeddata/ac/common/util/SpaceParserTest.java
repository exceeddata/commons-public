package com.exceeddata.ac.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SpaceParserTest {
    @Test
    public void testDoubleQuoteEscape() {
        final SpaceParser parser = new SpaceParser();
        String[] parsed;
        
        parsed = parser.split("a integer  nullable");
        assertEquals(3, parsed.length);
        assertEquals("a", parsed[0]);
        assertEquals("integer", parsed[1]);
        assertEquals("nullable", parsed[2]);
        
        parsed = parser.split("\"a \" integer  nullable ");
        assertEquals(4, parsed.length);
        assertEquals("a ", parsed[0]);
        assertEquals("integer", parsed[1]);
        assertEquals("nullable", parsed[2]);
        assertEquals("", parsed[3]);
    }
}
