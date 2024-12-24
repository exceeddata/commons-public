package com.exceeddata.ac.common.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
    
    @Test
    public void testConstructNames() {
        final List<String> names = new ArrayList<>();
        //basic
        names.add("t1");
        names.add("t2");
        
        String ns = XNameUtils.constructNames(names.toArray(new String[] {}));
        assertEquals(ns, "t1,t2");
        
        //ok characters
        names.add("_t3_");
        names.add("t4.s");
        
        ns = XNameUtils.constructNames(names.toArray(new String[] {}));
        assertEquals(ns, "t1,t2,_t3_,t4.s");
        
        //number and dot rules
        names.add(".t5");
        names.add("1t");
        names.add("12");
        ns = XNameUtils.constructNames(names.toArray(new String[] {}));
        assertEquals(ns, "t1,t2,_t3_,t4.s,'.t5','1t','12'");
        
        //special characters
        names.add(",t6");
        names.add("t 7");
        names.add("'t8'");
        names.add("\"t9\"");
        ns = XNameUtils.constructNames(names.toArray(new String[] {}));
        assertEquals(ns, "t1,t2,_t3_,t4.s,'.t5','1t','12',',t6','t 7','''t8''','\"t9\"'");
    }
    
    @Test
    public void testConstrucSelects() throws Exception {
        final List<String> names = new ArrayList<>();
        //basic
        names.add("t1");
        names.add("t2");
        
        String ns = XNameUtils.constructSelects(names.toArray(new String[] {}));
        assertEquals(ns, "t1,t2");
        
        //ok characters
        names.add("_t3_");
        names.add("t4.s");
        
        ns = XNameUtils.constructSelects(names.toArray(new String[] {}));
        assertEquals(ns, "t1,t2,_t3_,t4.s");
        
        //number and dot rules
        names.add(".t5");
        names.add("1t");
        names.add("12");
        ns = XNameUtils.constructSelects(names.toArray(new String[] {}));
        assertEquals(ns, "t1,t2,_t3_,t4.s,n'.t5',n'1t',n'12'");
        
        //special characters
        names.add(",t6");
        names.add("t 7");
        names.add("'t8'");
        names.add("\"t9\"");
        ns = XNameUtils.constructSelects(names.toArray(new String[] {}));
        assertEquals(ns, "t1,t2,_t3_,t4.s,n'.t5',n'1t',n'12',n',t6',n't 7',n'''t8''',n'\"t9\"'");
    }
}
