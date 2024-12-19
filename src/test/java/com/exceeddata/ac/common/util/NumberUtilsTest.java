package com.exceeddata.ac.common.util;

import junit.framework.TestCase;

public class NumberUtilsTest extends TestCase {
    public void testIsDigit() {
        assertEquals(true, XNumberUtils.isDigits("9"));
        assertEquals(true, XNumberUtils.isDigits("91"));
        assertEquals(true, XNumberUtils.isDigits("-9"));
        assertEquals(false, XNumberUtils.isDigits("10.0"));
        assertEquals(false, XNumberUtils.isDigits(""));
        assertEquals(false, XNumberUtils.isDigits(null));
        assertEquals(false, XNumberUtils.isDigits("a"));
        assertEquals(false, XNumberUtils.isDigits("10l"));
    }
    
    public void testIsNumber() {
        assertEquals(true, XNumberUtils.isNumber("9"));
        assertEquals(true, XNumberUtils.isNumber("91"));
        assertEquals(true, XNumberUtils.isNumber("-9"));
        assertEquals(true, XNumberUtils.isNumber("10.0"));
        assertEquals(true, XNumberUtils.isNumber("-10.0"));
        assertEquals(true, XNumberUtils.isNumber(".0"));
        assertEquals(false, XNumberUtils.isNumber(".0d"));
        assertEquals(false, XNumberUtils.isNumber(".0f"));
        assertEquals(false, XNumberUtils.isNumber("10l"));
        assertEquals(false, XNumberUtils.isNumber("10L"));
        assertEquals(true, XNumberUtils.isNumber("10e5"));
        assertEquals(true, XNumberUtils.isNumber("10e-5"));
        assertEquals(false, XNumberUtils.isNumber(""));
        assertEquals(false, XNumberUtils.isNumber(null));
        assertEquals(false, XNumberUtils.isNumber("a"));
    }
}
