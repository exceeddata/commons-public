package com.exceeddata.ac.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NameUtilsTest {

    @Test
    public void testParseName() {
        String rf = XNameUtils.parseName("''");
        assertEquals(rf, "");
        
        rf = XNameUtils.parseName("'g'");
        assertEquals(rf, "g");
        
        rf = XNameUtils.parseName("' g '");
        assertEquals(rf, " g ");
        
        rf = XNameUtils.parseName("  ' g '  ");
        assertEquals(rf, " g ");
        
        rf = XNameUtils.parseName("g");
        assertEquals(rf, "g");
        
        rf = XNameUtils.parseName(" g , i");
        assertEquals(rf, "g");
        
        rf = XNameUtils.parseName("g.");
        assertEquals(rf, "g.");
        
        rf = XNameUtils.parseName("'g.");
        assertEquals(rf, "'g.");
        
        rf = XNameUtils.parseName("'g'.");
        assertEquals(rf, "'g'.");
        
        rf = XNameUtils.parseName("'\\'g'");
        assertEquals(rf, "'g");
        
        rf = XNameUtils.parseName("'\\'g\\''");
        assertEquals(rf, "'g'");
        
        rf = XNameUtils.parseName("'\\'g\\'','\\'h\\''");
        assertEquals(rf, "'g'");
    }
    
    @Test
    public void testParseNames() {
        String[] rfs = XNameUtils.parseNames("g, h, i, 'k'");
        assertEquals(rfs.length, 4);
        assertEquals(rfs[0], "g");
        assertEquals(rfs[1], "h");
        assertEquals(rfs[2], "i");
        assertEquals(rfs[3], "k");
        
        rfs = XNameUtils.parseNames("g, h, i, k, ");
        assertEquals(rfs.length, 4);
        assertEquals(rfs[0], "g");
        assertEquals(rfs[1], "h");
        assertEquals(rfs[2], "i");
        assertEquals(rfs[3], "k");
        
        rfs = XNameUtils.parseNames("g, 'h ', i, k, ");
        assertEquals(rfs.length, 4);
        assertEquals(rfs[0], "g");
        assertEquals(rfs[1], "h ");
        assertEquals(rfs[2], "i");
        assertEquals(rfs[3], "k");
        
        rfs = XNameUtils.parseNames("'g'");
        assertEquals(rfs.length, 1);
        assertEquals(rfs[0], "g");
        
        rfs = XNameUtils.parseNames("'g','h'");
        assertEquals(rfs.length, 2);
        assertEquals(rfs[0], "g");
        assertEquals(rfs[1], "h");
        
        rfs = XNameUtils.parseNames(" 'g', 'h', 'i', ");
        assertEquals(rfs.length, 3);
        assertEquals(rfs[0], "g");
        assertEquals(rfs[1], "h");
        assertEquals(rfs[2], "i");
        
        rfs = XNameUtils.parseNames(" 'g', '', 'i', ");
        assertEquals(rfs.length, 2);
        assertEquals(rfs[0], "g");
        assertEquals(rfs[1], "i");
        
        rfs = XNameUtils.parseNames(" 'g',, 'i', ");
        assertEquals(rfs.length, 2);
        assertEquals(rfs[0], "g");
        assertEquals(rfs[1], "i");
        
        rfs = XNameUtils.parseNames(" 'g', , 'i', ");
        assertEquals(rfs.length, 2);
        assertEquals(rfs[0], "g");
        assertEquals(rfs[1], "i");
        
        rfs = XNameUtils.parseNames("'\\'g\\'','\\'h\\''");
        assertEquals(rfs.length, 2);
        assertEquals(rfs[0], "'g'");
        assertEquals(rfs[1], "'h'");
    }
}
