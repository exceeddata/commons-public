package com.exceeddata.ac.common.data.typedata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeDataEquals {
    
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testStringEquals() {
        StringData s1 = StringData.valueOf("foo");
        assertEquals(true, s1.equals(new StringData("foo")));
        assertEquals(false, s1.equals(new StringData("foo ")));
        assertEquals(false, s1.equals(null));
        assertEquals(false, s1.equals(new IntData("1")));
        assertEquals(false, s1.equals(new StringData()));
        assertEquals(false, new StringData().equals(s1));
        assertEquals(true, new StringData().equals(StringData.NULL));
        assertEquals(true, new StringData().equals(IntData.NULL));
        assertEquals(true, new StringData("").equals(StringData.EMPTY));
    }
    
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testIntEquals() {
        IntData s1 = IntData.valueOf(5);
        assertEquals(true, s1.equals(new IntData(5)));
        assertEquals(false, s1.equals(new IntData(10)));
        assertEquals(false, s1.equals(null));
        assertEquals(false, s1.equals(new IntData()));
        assertEquals(false, s1.equals(new StringData()));
        assertEquals(false, new StringData().equals(s1));
        assertEquals(true, new IntData().equals(StringData.NULL));
        assertEquals(true, new StringData().equals(IntData.NULL));
    }
}