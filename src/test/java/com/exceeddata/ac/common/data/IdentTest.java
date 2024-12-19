package com.exceeddata.ac.common.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import com.exceeddata.ac.common.data.record.ArrayIdent;
import com.exceeddata.ac.common.data.record.EmptyIdent;
import com.exceeddata.ac.common.data.record.Ident;
import com.exceeddata.ac.common.data.record.OneIdent;
import com.exceeddata.ac.common.data.record.TwoIdent;
import com.exceeddata.ac.common.data.typedata.BooleanData;
import com.exceeddata.ac.common.data.typedata.IntData;
import com.exceeddata.ac.common.data.typedata.StringData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class IdentTest {
    private static final String location = System.getProperty("java.io.tmpdir") + "/ident.ser"; 
    
    @Test
    public void testArrayIdentBasic() {
        int cols = 100;
        ArrayIdent ident = new ArrayIdent();
        
        for (int j = 0; j < cols; ++j) {
            ident.add(IntData.valueOf(j));
        }
        
        assertEquals(ident.size(), cols);
        
        for (int j = 0; j < cols; ++j) {
            assertEquals(ident.dataAt(j).toString(), String.valueOf(j));
        }
        
        for (int j = cols / 5 * 2 - 1; j >= cols /5; --j) {
            assertEquals(ident.removeAt(j).toString(), String.valueOf(j));
        }
        
        for (int j = ident.size() - 1; j >=0; --j) {
            assertEquals(ident.dataAt(j), ident.removeAt(j));
        }
        
        assertEquals(ident.size(), 0);
        
        for (int j = 0; j < cols; ++j) {
            ident.add(IntData.valueOf(j));
        }
        
        assertEquals(ident.size(), cols);
    }

    
    @Test
    public void testCompareBasic() {
        OneIdent oneIdent1, oneIdent3;
        oneIdent1= new OneIdent(IntData.ONE);
        oneIdent3 = new OneIdent(IntData.valueOf(3));
        assertEquals(oneIdent1.compareTo(oneIdent3), -1);
        assertEquals(oneIdent3.compareTo(oneIdent1), 1);
        
        TwoIdent twoIdent1, twoIdent2;
        twoIdent1= new TwoIdent(IntData.ONE, IntData.ZERO);
        twoIdent2 = new TwoIdent(IntData.TWO, IntData.ONE);
        assertEquals(twoIdent1.compareTo(twoIdent2), -1);
        assertEquals(twoIdent2.compareTo(twoIdent1), 1);
        
        ArrayIdent arrayIdent1, arrayIdent2, arrayIdent3;
        arrayIdent1 = new ArrayIdent(new TypeData[] { IntData.ONE }, 1);
        arrayIdent2 = new ArrayIdent(new TypeData[] { IntData.ONE, IntData.ZERO }, 2);
        arrayIdent3 = new ArrayIdent(new TypeData[] { IntData.ZERO, IntData.ONE }, 2);
        assertEquals(arrayIdent1.compareTo(arrayIdent2), -1);
        assertEquals(arrayIdent2.compareTo(arrayIdent1), 1);
        assertEquals(arrayIdent1.compareTo(arrayIdent3), 1);
        assertEquals(arrayIdent3.compareTo(arrayIdent1), -1);
        assertEquals(arrayIdent2.compareTo(arrayIdent3), 1);
        assertEquals(arrayIdent3.compareTo(arrayIdent2), -1);
        
        //cross compare
        assertEquals(oneIdent1.compareTo(twoIdent1), -1);
        assertEquals(twoIdent1.compareTo(oneIdent1), 1);
        assertEquals(oneIdent3.compareTo(twoIdent1), 1);
        assertEquals(twoIdent1.compareTo(oneIdent3), -1);
        assertEquals(oneIdent3.compareTo(twoIdent1), 1);
        assertEquals(twoIdent1.compareTo(oneIdent3), -1);

        assertEquals(oneIdent1.compareTo(arrayIdent1), 0);
        assertEquals(arrayIdent1.compareTo(oneIdent1), 0);
        assertEquals(oneIdent3.compareTo(arrayIdent1), 1);
        assertEquals(arrayIdent1.compareTo(oneIdent3), -1);
        
        assertEquals(twoIdent1.compareTo(arrayIdent2), 0);
        assertEquals(arrayIdent2.compareTo(twoIdent1), 0);
        assertEquals(twoIdent2.compareTo(arrayIdent2), 1);
        assertEquals(arrayIdent2.compareTo(twoIdent2), -1);
    }
    
    @Test
    public void testEqualsCompare() {
        OneIdent oneIdent = new OneIdent(StringData.valueOf("baidu"));
        assertEquals(oneIdent.size(), 1);
        
        TwoIdent twoIdent = new TwoIdent(StringData.valueOf("baidu"), StringData.valueOf("google"));
        assertEquals(twoIdent.size(), 2);
        
        ArrayIdent arrayIdent = new ArrayIdent();
        arrayIdent.add(StringData.valueOf("baidu"));
        assertEquals(arrayIdent.size(), 1);
        assertEquals(arrayIdent, oneIdent);

        arrayIdent.add(StringData.valueOf("google"));
        assertEquals(arrayIdent, twoIdent);
        
        ArrayIdent destIdent = arrayIdent.clone();
        assertEquals(destIdent.size(), 2);
        
        destIdent.add(StringData.valueOf("t"));
        assertEquals(destIdent.compareTo(arrayIdent) > 0, true);
        
        ArrayIdent dest2 = destIdent.copy(), dest3 = dest2.copy();
        
        dest3 = dest3.additionCopy(StringData.valueOf("sohu"));
        assertEquals(dest3.equals(dest2), false);
        assertEquals(dest3.compareTo(dest2) > 0, true);

        dest3 = dest3.additionCopy(StringData.valueOf("sina"), StringData.valueOf("sina"));
        assertEquals(dest3.size(), 6);
        
        dest3 = (ArrayIdent) oneIdent.additionCopy(StringData.valueOf("sina"), StringData.valueOf("sina"));
        assertEquals(dest3.size(), 3);
        
        twoIdent = (TwoIdent) oneIdent.additionCopy(StringData.valueOf("sina"));
        assertEquals(twoIdent.size(), 2);
    }
    
    @Test
    public void hashcodeTest() {
        final OneIdent oneIdent = new OneIdent(StringData.valueOf("baidu"));
        final TwoIdent twoIdent = new TwoIdent(StringData.valueOf("baidu"), StringData.valueOf("baidu.com"));
        final ArrayIdent arrayIdent = new ArrayIdent();
        arrayIdent.add(StringData.valueOf("baidu"));
        assertEquals(oneIdent.hashCode(), arrayIdent.hashCode());
        
        arrayIdent.add(StringData.valueOf("baidu.com"));
        assertEquals(twoIdent.hashCode(), arrayIdent.hashCode());
        
        final ArrayIdent ident1 = new ArrayIdent();
        ident1.add(StringData.valueOf("baidu"))
              .add(StringData.valueOf("baidu.com"))
              .add(StringData.valueOf("/a.html"));
        
        final ArrayIdent ident2 = new ArrayIdent();
        ident2.add(StringData.valueOf("baidu"))
              .add(StringData.valueOf("baidu.com"))
              .add(StringData.valueOf("/b.html"));
        
        final ArrayIdent ident3 = new ArrayIdent();
        ident3.add(StringData.valueOf("baidu"))
              .add(StringData.valueOf("baidu.com"));
        
        final ArrayIdent ident4 = new ArrayIdent();
        ident4.add(StringData.valueOf("baidu"))
              .add(StringData.valueOf("baidu.com"))
              .add(StringData.NULL);
        
        final ArrayIdent ident5 = new ArrayIdent();
        ident5.add(StringData.valueOf("baidu"))
              .add(StringData.valueOf(""));
        
        final ArrayIdent ident6 = new ArrayIdent();
        ident6.add(StringData.valueOf("baidu"))
              .add(StringData.valueOf("baidu.com"));
        
        assertEquals(ident1.equals(ident2), false);
        assertEquals(ident1.compareTo(ident2) < 0, true);
        assertEquals(ident1.compareTo(ident3) > 0, true);
        assertEquals(ident3.compareTo(ident4) < 0, true);
        assertEquals(ident3.compareTo(ident5) > 0, true);
        assertEquals(ident3.compareTo(ident6), 0);
        assertEquals(ident3.hashCode(), ident6.hashCode());
    }
    
    @Test
    public void additionCopyTest() {
        final OneIdent oneIdent1 = new OneIdent(IntData.valueOf(1));
        final TwoIdent twoIdent1 = new TwoIdent(IntData.valueOf(2), IntData.valueOf(3));
        final ArrayIdent arrayIdent1 = new ArrayIdent(new TypeData[] {IntData.valueOf(2), IntData.valueOf(3)}, 2);

        assert(EmptyIdent.INSTANCE.additionCopy(IntData.ONE) instanceof OneIdent);
        assertEquals(EmptyIdent.INSTANCE.additionCopy(IntData.ONE).dataAt(0), IntData.ONE);
        
        assert(oneIdent1.additionCopy(IntData.TWO) instanceof TwoIdent);
        assertEquals(oneIdent1.additionCopy(IntData.TWO).dataAt(0), IntData.ONE);
        assertEquals(oneIdent1.additionCopy(IntData.TWO).dataAt(1), IntData.TWO);
        
        assert(oneIdent1.additionCopy(IntData.TWO, IntData.valueOf(3)) instanceof ArrayIdent);
        assert(oneIdent1.additionCopy(IntData.TWO, IntData.valueOf(3)).size() == 3 );
        assertEquals(oneIdent1.additionCopy(IntData.TWO, IntData.valueOf(3)).dataAt(0), IntData.ONE);
        assertEquals(oneIdent1.additionCopy(IntData.TWO, IntData.valueOf(3)).dataAt(1), IntData.TWO);
        assertEquals(oneIdent1.additionCopy(IntData.TWO, IntData.valueOf(3)).dataAt(2), IntData.valueOf(3));
        
        assert(twoIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)) instanceof ArrayIdent);
        assert(twoIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)).size() == 4 );
        assertEquals(twoIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)).dataAt(0), IntData.TWO);
        assertEquals(twoIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)).dataAt(1), IntData.valueOf(3));
        assertEquals(twoIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)).dataAt(2), IntData.valueOf(4));
        assertEquals(twoIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)).dataAt(3), IntData.valueOf(5));
        
        assert(arrayIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)) instanceof ArrayIdent);
        assert(arrayIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)).size() == 4 );
        assertEquals(arrayIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)).dataAt(0), IntData.TWO);
        assertEquals(arrayIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)).dataAt(1), IntData.valueOf(3));
        assertEquals(arrayIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)).dataAt(2), IntData.valueOf(4));
        assertEquals(arrayIdent1.additionCopy(IntData.valueOf(4), IntData.valueOf(5)).dataAt(3), IntData.valueOf(5));
    }
    
    @Test
    public void mergeCopyTest() {
        final OneIdent oneIdent1 = new OneIdent(IntData.valueOf(1));
        final OneIdent oneIdent2 = new OneIdent(IntData.valueOf(2));
        final OneIdent oneIdent3 = new OneIdent(IntData.valueOf(3));
        final TwoIdent twoIdent1 = new TwoIdent(IntData.valueOf(2), IntData.valueOf(3));
        final ArrayIdent arrayIdent1 = new ArrayIdent(new TypeData[] {IntData.valueOf(2), IntData.valueOf(3)}, 2);

        //zero
        assert(EmptyIdent.INSTANCE.mergeCopy(EmptyIdent.INSTANCE) instanceof EmptyIdent);
        assert(EmptyIdent.INSTANCE.mergeCopy(oneIdent1) instanceof OneIdent);
        assert(EmptyIdent.INSTANCE.mergeCopy(twoIdent1) instanceof TwoIdent);
        assert(EmptyIdent.INSTANCE.mergeCopy(arrayIdent1) instanceof ArrayIdent);
        
        assert(oneIdent1.mergeCopy(EmptyIdent.INSTANCE) instanceof OneIdent);
        assert(twoIdent1.mergeCopy(EmptyIdent.INSTANCE) instanceof TwoIdent);
        assert(arrayIdent1.mergeCopy(EmptyIdent.INSTANCE) instanceof ArrayIdent);
        
        //one one
        assert(oneIdent1.mergeCopy(oneIdent2) instanceof TwoIdent);
        assertEquals(oneIdent1.mergeCopy(oneIdent2).dataAt(0), IntData.valueOf(1));
        assertEquals(oneIdent1.mergeCopy(oneIdent2).dataAt(1), IntData.valueOf(2));
        
        //one one one
        assert(oneIdent1.mergeCopy(oneIdent2).mergeCopy(oneIdent3) instanceof ArrayIdent);
        assert(oneIdent1.mergeCopy(oneIdent2).mergeCopy(oneIdent3).size() == 3);
        assertEquals(oneIdent1.mergeCopy(oneIdent2).mergeCopy(oneIdent3).dataAt(0), IntData.valueOf(1));
        assertEquals(oneIdent1.mergeCopy(oneIdent2).mergeCopy(oneIdent3).dataAt(1), IntData.valueOf(2));
        assertEquals(oneIdent1.mergeCopy(oneIdent2).mergeCopy(oneIdent3).dataAt(2), IntData.valueOf(3));
        
        //one two
        assert(oneIdent1.mergeCopy(twoIdent1) instanceof ArrayIdent);
        assert(oneIdent1.mergeCopy(twoIdent1).size() == 3);
        assertEquals(oneIdent1.mergeCopy(twoIdent1).dataAt(0), IntData.valueOf(1));
        assertEquals(oneIdent1.mergeCopy(twoIdent1).dataAt(1), IntData.valueOf(2));
        assertEquals(oneIdent1.mergeCopy(twoIdent1).dataAt(2), IntData.valueOf(3));
        
        //two one
        assert(twoIdent1.mergeCopy(oneIdent1) instanceof ArrayIdent);
        assert(twoIdent1.mergeCopy(oneIdent1).size() == 3);
        assertEquals(twoIdent1.mergeCopy(oneIdent1).dataAt(0), IntData.valueOf(2));
        assertEquals(twoIdent1.mergeCopy(oneIdent1).dataAt(1), IntData.valueOf(3));
        assertEquals(twoIdent1.mergeCopy(oneIdent1).dataAt(2), IntData.valueOf(1));
        
        //two two
        assert(twoIdent1.mergeCopy(twoIdent1) instanceof ArrayIdent);
        assert(twoIdent1.mergeCopy(twoIdent1).size() == 4);
        assertEquals(twoIdent1.mergeCopy(twoIdent1).dataAt(0), IntData.valueOf(2));
        assertEquals(twoIdent1.mergeCopy(twoIdent1).dataAt(1), IntData.valueOf(3));
        assertEquals(twoIdent1.mergeCopy(twoIdent1).dataAt(2), IntData.valueOf(2));
        assertEquals(twoIdent1.mergeCopy(twoIdent1).dataAt(3), IntData.valueOf(3));
        
        //one array
        assert(oneIdent1.mergeCopy(arrayIdent1) instanceof ArrayIdent);
        assert(oneIdent1.mergeCopy(arrayIdent1).size() == 3);
        assertEquals(oneIdent1.mergeCopy(twoIdent1).dataAt(0), IntData.valueOf(1));
        assertEquals(oneIdent1.mergeCopy(twoIdent1).dataAt(1), IntData.valueOf(2));
        assertEquals(oneIdent1.mergeCopy(twoIdent1).dataAt(2), IntData.valueOf(3));
        
        //two array
        assert(twoIdent1.mergeCopy(arrayIdent1) instanceof ArrayIdent);
        assert(twoIdent1.mergeCopy(arrayIdent1).size() == 4);
        assertEquals(twoIdent1.mergeCopy(twoIdent1).dataAt(0), IntData.valueOf(2));
        assertEquals(twoIdent1.mergeCopy(twoIdent1).dataAt(1), IntData.valueOf(3));
        assertEquals(twoIdent1.mergeCopy(twoIdent1).dataAt(2), IntData.valueOf(2));
        assertEquals(twoIdent1.mergeCopy(twoIdent1).dataAt(3), IntData.valueOf(3));

        //array one
        assert(arrayIdent1.mergeCopy(oneIdent1) instanceof ArrayIdent);
        assert(arrayIdent1.mergeCopy(oneIdent1).size() == 3);
        assertEquals(arrayIdent1.mergeCopy(oneIdent1).dataAt(0), IntData.valueOf(2));
        assertEquals(arrayIdent1.mergeCopy(oneIdent1).dataAt(1), IntData.valueOf(3));
        assertEquals(arrayIdent1.mergeCopy(oneIdent1).dataAt(2), IntData.valueOf(1));
        
        //array two
        assert(arrayIdent1.mergeCopy(twoIdent1) instanceof ArrayIdent);
        assert(arrayIdent1.mergeCopy(twoIdent1).size() == 4);
        assertEquals(arrayIdent1.mergeCopy(twoIdent1).dataAt(0), IntData.valueOf(2));
        assertEquals(arrayIdent1.mergeCopy(twoIdent1).dataAt(1), IntData.valueOf(3));
        assertEquals(arrayIdent1.mergeCopy(twoIdent1).dataAt(2), IntData.valueOf(2));
        assertEquals(arrayIdent1.mergeCopy(twoIdent1).dataAt(3), IntData.valueOf(3));
        
        //array array
        assert(arrayIdent1.mergeCopy(arrayIdent1) instanceof ArrayIdent);
        assert(arrayIdent1.mergeCopy(arrayIdent1).size() == 4);
        assertEquals(arrayIdent1.mergeCopy(arrayIdent1).dataAt(0), IntData.valueOf(2));
        assertEquals(arrayIdent1.mergeCopy(arrayIdent1).dataAt(1), IntData.valueOf(3));
        assertEquals(arrayIdent1.mergeCopy(arrayIdent1).dataAt(2), IntData.valueOf(2));
        assertEquals(arrayIdent1.mergeCopy(arrayIdent1).dataAt(3), IntData.valueOf(3));
    }
    
    @Test
    public void subCopyTest() {
        final ArrayIdent arrayIdent = new ArrayIdent();
        arrayIdent.add(IntData.valueOf(1)).add(IntData.valueOf(2)).add(IntData.valueOf(3));
        
        assert(arrayIdent.subCopy(1, 3) instanceof TwoIdent);
        assertEquals(arrayIdent.subCopy(1, 3).dataAt(0), IntData.valueOf(2));
        assertEquals(arrayIdent.subCopy(1, 3).dataAt(1), IntData.valueOf(3));

        assert(arrayIdent.subCopy(1, 2) instanceof OneIdent);
        assertEquals(arrayIdent.subCopy(1, 2).dataAt(0), IntData.valueOf(2));
        
        assert(arrayIdent.subCopy(1, 3).subCopy(0,  1) instanceof OneIdent);
        assertEquals(arrayIdent.subCopy(1, 3).subCopy(0,  1).dataAt(0), IntData.valueOf(2));
        
        assert ( arrayIdent.subCopy(1, 3).subCopy(0,  1).subCopy(0, 0) instanceof EmptyIdent);
        assert ( arrayIdent.subCopy(1, 3).subCopy(1, 1) instanceof EmptyIdent);
        assert ( arrayIdent.subCopy(4, 4) instanceof EmptyIdent);
        assert ( arrayIdent.subCopy(4, 5) instanceof EmptyIdent);
    }
    
    @Test
    public void addTest() {
        ArrayIdent ident = new ArrayIdent();
        ident.add(StringData.valueOf("foo0"));
        ident.add(StringData.valueOf("foo1"));
        ident.add(StringData.valueOf("foo2"));
        ident.add(StringData.valueOf("bar1"));
        
        assertEquals(ident.dataAt(0).toString(), "foo0");
        assertEquals(ident.dataAt(2).toString(), "foo2");
        assertEquals(ident.dataAt(3).toString(), "bar1");
        
        ident.removeAt(0);
        assertEquals(ident.dataAt(1).toString(), "foo2");

        ident.add(StringData.valueOf("bar2"));
        assertEquals(ident.dataAt(3).toString(), "bar2");

        ident.add(StringData.valueOf("bar3"));
        assertEquals(ident.dataAt(4).toString(), "bar3");
        

        ident.clear();
        assertEquals(ident.size(), 0);
        
        ident.add(IntData.ONE);
        assertEquals(ident.dataAt(0), IntData.valueOf(1));
    }
    
    @Test
    public void addAllTest() {
        ArrayIdent ident1 = new ArrayIdent(2);
        ident1.add(StringData.valueOf("foo0"));
        ident1.add(StringData.valueOf("foo1"));
        
        ArrayIdent ident2 = new ArrayIdent(4);
        ident2.add(StringData.valueOf("foo2"));
        ident2.add(StringData.valueOf("foo3"));
        
        ident1.addAll(ident2);
        assertEquals(ident1.size(), 4);
        assertEquals(ident1.dataAt(0).toString(), "foo0");
        assertEquals(ident1.dataAt(1).toString(), "foo1");
        assertEquals(ident1.dataAt(2).toString(), "foo2");
        assertEquals(ident1.dataAt(3).toString(), "foo3");
        
        ident1.clear();
        ident1.addAll(ident2);
        assertEquals(ident1.size(), 2);
        assertEquals(ident1.dataAt(0).toString(), "foo2");
        assertEquals(ident1.dataAt(1).toString(), "foo3");
    }
    

    @Test
    public void removeTest() {
        ArrayIdent ident = new ArrayIdent();
        ident.add(StringData.valueOf("foo0"));
        ident.add(StringData.valueOf("foo1"));
        ident.add(StringData.valueOf("foo2"));
        ident.add(StringData.valueOf("foo3"));
        ident.add(StringData.valueOf("foo4"));
        ident.add(StringData.valueOf("foo5"));
        ident.add(StringData.valueOf("foo6"));
        
        ident.removeAt(1);
        ident.removeAt(3);
        ident.removeAt(4);
        
        assertEquals(ident.dataAt(3).toString(), "foo5");
        assertEquals(ident.dataAt(0).toString(), "foo0");
        
        ident.add(StringData.valueOf("foo7"));
        ident.add(StringData.valueOf("foo8"));
        ident.removeAt(7);
        
        assertEquals(ident.dataAt(5).toString(), "foo8");
        
    }
    
    @Test
    public void setAtTest() {
        ArrayIdent r1 = new ArrayIdent().add(IntData.valueOf(1)).add(IntData.valueOf(2));
        r1.setAt(1, IntData.valueOf(3));
        assertEquals(r1.dataAt(0), IntData.valueOf(1));
        assertEquals(r1.dataAt(1), IntData.valueOf(3));
        
        TwoIdent r2 = new TwoIdent(IntData.valueOf(1), IntData.valueOf(2));
        r2.setAt(1, IntData.valueOf(3));
        assertEquals(r2.dataAt(0), IntData.valueOf(1));
        assertEquals(r2.dataAt(1), IntData.valueOf(3));
        
        OneIdent r3 = new OneIdent(IntData.valueOf(3));
        r3.setAt(0, IntData.valueOf(3));
        assertEquals(r3.dataAt(0), IntData.valueOf(3));
        
        try {
            r1.setAt(4, IntData.valueOf(3));
            fail("should fail");
        } catch (AssertionError e) {
        }
        
        try {
            r2.setAt(4, IntData.valueOf(3));
            fail("should fail");
        } catch (AssertionError e) {
        }
        
        try {
            r3.setAt(1, IntData.valueOf(3));
            fail("should fail");
        } catch (AssertionError e) {
        }
    }
    
    @Test
    public void copyTest() {
        try {
            ArrayIdent r1 = new ArrayIdent().add(IntData.valueOf(1));
            ArrayIdent r2 = r1.copy();
            r2.add(IntData.valueOf(2));
            
            assertEquals(1, r1.dataAt(0).toInt().intValue());
            assertEquals(2, r2.dataAt(1).toInt().intValue());
            
            ArrayIdent r3 = r1.copy();
            r3.add(IntData.valueOf(3));
            
            assertEquals(1, r1.dataAt(0).toInt().intValue());
            assertEquals(3, r3.dataAt(1).toInt().intValue());
            assertEquals(2, r2.dataAt(1).toInt().intValue());
            
        } catch (EngineException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException, EngineException {
        final ArrayIdent ident = new ArrayIdent().add(BooleanData.TRUE).add(IntData.valueOf(100));
        assertEquals(deserializeExternalizable(serializeExternalizable(location, ident)), ident);
        assertEquals(deserializeExternalizable(serializeExternalizable(location, EmptyIdent.INSTANCE)), EmptyIdent.INSTANCE);
        assertEquals(deserializeExternalizable(serializeExternalizable(location, new OneIdent(IntData.ONE))),new OneIdent(IntData.ONE));
        assertEquals(deserializeExternalizable(serializeExternalizable(location, new TwoIdent(IntData.ONE, IntData.TWO))),new TwoIdent(IntData.ONE, IntData.TWO));
        
        //specific count test, 31, 0 byte
        ident.clear();
        for (int i = 0; i < 31; ++i) {
            ident.add(IntData.valueOf(i));
        }
        assertEquals(deserializeExternalizable(serializeExternalizable(location, ident)), ident);
        
        //first + 1 byte
        ident.clear();
        for (int i = 0; i < 127; ++i) {
            ident.add(IntData.valueOf(i));
        }
        assertEquals(deserializeExternalizable(serializeExternalizable(location, ident)), ident);
     
        //first + 2 byte
        ident.clear();
        for (int i = 0; i < 16383; ++i) {
            ident.add(IntData.valueOf(i));
        }
        assertEquals(deserializeExternalizable(serializeExternalizable(location, ident)), ident);
        

        //first + 3 byte
        ident.clear();
        for (int i = 0; i < 32768; ++i) {
            ident.add(IntData.valueOf(i));
        }
        assertEquals(deserializeExternalizable(serializeExternalizable(location, ident)), ident);
    }
    
    private String serializeExternalizable(final String loc, final Ident ident) throws IOException {
        new File(loc).delete();
        
        final FileOutputStream fileOut = new FileOutputStream(loc);
        final ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
        outStream.writeObject(ident);
        outStream.close();
        fileOut.close();
        return loc;
    }
    
    private Ident deserializeExternalizable(final String loc) throws IOException, ClassNotFoundException {
        final FileInputStream fileIn = new FileInputStream(loc);
        final ObjectInputStream inStream = new ObjectInputStream(fileIn);
        final Ident obj = (Ident) inStream.readObject();
        inStream.close();
        fileIn.close();
        return obj;
    }
}
