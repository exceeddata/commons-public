package com.exceeddata.ac.common.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

public class OperationParamParserTest {

    @Test
    public void testBackslash() {
        final OperationParamParser parser = new OperationParamParser();
        HashMap<String, String> map;
        String[] params;
        
        params = parser.split("expressions=`compact('[0-9]', 'match(_value, \\\\\\\\'2$\\\\\\\\')') as c`");
        map = parseParams(params, false);
        assertEquals(map.get("expressions"), "compact('[0-9]', 'match(_value, \\'2$\\')') as c");
        
        params = parser.split("expressions=`compact('[0-9]', 'match(_value, ''2$'')') as c`");
        map = parseParams(params, false);
        assertEquals(map.get("expressions"), "compact('[0-9]', 'match(_value, ''2$'')') as c");
        
        parser.setKeepEscapeChar(true);
        params = parser.split("expressions=`compact('[0-9]', 'match(_value, \\'2$\\')') as c`");
        map = parseParams(params, true);
        assertEquals(map.get("expressions"), "compact('[0-9]', 'match(_value, \\'2$\\')') as c");
    }
    
    @Test
    public void test() {
        final OperationParamParser parser = new OperationParamParser();
        HashMap<String, String> map;
        String[] params;
        
        //non-quotes
        params = parser.split("keys=bar,selects=tow,expr=something");
        assertEquals("keys=bar", params[0]);
        assertEquals("selects=tow", params[1]);
        assertEquals("expr=something", params[2]);
        
        map = parseParams(params, true);
        assertEquals(map.get("keys"), "bar");
        assertEquals(map.get("selects"), "tow");
        assertEquals(map.get("expr"), "something");
        
        //quotes
        params = parser.split("keys=`bar`,selects=`tow`,expr=`something`");
        assertEquals("keys=`bar`", params[0]);
        assertEquals("selects=`tow`", params[1]);
        assertEquals("expr=`something`", params[2]);
        
        map = parseParams(params, true);
        assertEquals(map.get("keys"), "bar");
        assertEquals(map.get("selects"), "tow");
        assertEquals(map.get("expr"), "something");
        
        //mixed quotes
        params = parser.split("keys=`bar` , selects=tow ,expr=`something`");
        assertEquals("keys=`bar`", params[0]);
        assertEquals("selects=tow", params[1]);
        assertEquals("expr=`something`", params[2]);
        
        map = parseParams(params, true);
        assertEquals(map.get("keys"), "bar");
        assertEquals(map.get("selects"), "tow");
        assertEquals(map.get("expr"), "something");
    }

    private HashMap<String, String> parseParams(final String[] paramstrs, final boolean keepEscape) {
        final SeparatorParser parser = new SeparatorParser('=', '`', '\\', ' ', keepEscape, true);
        final HashMap<String, String> params = new HashMap<String, String>();
        String[] kv;
        if (paramstrs != null) {
            for (final String paramstr : paramstrs) {
                kv = parser.split(paramstr);
                if (kv.length == 2 && XStringUtils.isNotBlank(kv[0])) {
                    params.put(kv[0].trim(), kv[1]);
                }
            }
        }
        return params;
    }
}
