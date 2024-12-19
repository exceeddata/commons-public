package com.exceeddata.ac.common.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.exceeddata.ac.common.data.record.Record;
import com.exceeddata.ac.common.data.record.RecordBuilder;
import com.exceeddata.ac.common.data.typedata.BooleanData;
import com.exceeddata.ac.common.data.typedata.DecimalData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.FloatData;
import com.exceeddata.ac.common.data.typedata.IntData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.NullData;
import com.exceeddata.ac.common.data.typedata.NumericData;
import com.exceeddata.ac.common.data.typedata.StringData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class RecordTest {
    private static final String location = System.getProperty("java.io.tmpdir") + "/record.ser"; 

    @Test
    public void testTombStone() {
        //we test 4 because 3/4 = 0.75 is right below 0.80 load factor
        
        Record r = RecordBuilder.newTemplateRecord(new String[] {"a", "b"});
        r.setAt(0, StringData.valueOf("a"));
        r.setAt(1, StringData.valueOf("b"));
        
        Record r2 = r.shallowCopy();
        r2.remove("b");
        
        r2.add("c", StringData.valueOf("c"));
        r2.add("d", StringData.valueOf("d"));
        r2.add("b", StringData.valueOf("b"));
        assertEquals(r2.size(), 4);
        assertEquals(r2.get("b").toString(), "b");

        r2.remove("a");
        r2.remove("b");
        r2.remove("c");
        r2.remove("d");
        r2.add("f", StringData.valueOf("f"));
        r2.add("g", StringData.valueOf("g"));
        r2.add("i", StringData.valueOf("i"));
        r2.add("h", StringData.valueOf("h"));
        
        assertEquals(r2.size(), 4);
        assertEquals(r2.get("f").toString(), "f");
        assertEquals(r2.get("g").toString(), "g");
        assertEquals(r2.get("i").toString(), "i");
        assertEquals(r2.get("h").toString(), "h");
        
        //expand
        r2.add("j", StringData.valueOf("j"));
        r2.add("k", StringData.valueOf("k"));
        assertEquals(r2.size(), 6);
        assertEquals(r2.get("j").toString(), "j");
        assertEquals(r2.get("k").toString(), "k");
        
        Record record = new Record(4);
        record.add("target", StringData.valueOf("target"));
        record.add("features", StringData.valueOf("features"));
        
        Record value = record.shallowCopy();
        value.remove("features");
        value.add("features_1", StringData.valueOf("features_1"));
        value.add("features_2", StringData.valueOf("features_2"));
        value.add("features_3", StringData.valueOf("features_3"));

        assertEquals(value.size(), 4);
        assertEquals(value.get("target").toString(), "target");
        assertEquals(value.get("features_1").toString(), "features_1");
        assertEquals(value.get("features_2").toString(), "features_2");
        assertEquals(value.get("features_3").toString(), "features_3");
        assertEquals(value.get("features").toString(), "");

    }
    
    @Test
    public void testBasicInitialCapacity() {
        int cols = 1000;
        Record record = new Record(1);
        for (int j = 0; j < cols; ++j) {
            record.add("a" + j, IntData.ZERO);
            assert(record.size() == j+1);
        }
        
        record.clear();
        for (int j = 0; j < cols; ++j) {
            record.add("a" + j, IntData.ZERO);
            assert(record.size() == j+1);
        }
        
        record = new Record(2);
        for (int j = 0; j < cols; ++j) {
            record.add("a" + j, IntData.ZERO);
            assert(record.size() == j+1);
        }
    }
        
    @Test
    public void testBasic() {
        int cols = 1000;
        Record record = new Record();
        
        for (int j = 0; j < cols; ++j) {
            record.add("keysomething" + j, IntData.valueOf(j));
        }
        
        assertEquals(record.size(), cols);
        
        for (int j = 0; j < cols; ++j) {
            assertEquals(record.nameAt(j), "keysomething" + j);
            assertEquals(record.dataAt(j).toString(), String.valueOf(j));
        }
        
        for (int j = 0; j < cols; ++j) {
            assertEquals(record.get("keysomething" + j).toString(), String.valueOf(j));
        }
        
        for (int j = cols / 5 * 2 - 1; j >= cols /5; --j) {
            assertEquals(record.removeAt(j).toString(), String.valueOf(j));
        }
        
        for (int j = cols / 5 * 2; j < cols / 5 * 3; ++j) {
            assertEquals(record.remove("keysomething" + j).toString(), String.valueOf(j));
        }
        
        /*
         * final validation
         */
        for (int j = 0; j < record.size(); ++j) {
            assertEquals(record.dataAt(j), record.get(record.nameAt(j)));
        }
        
        for (int j = record.size() - 1; j >=0; --j) {
            assertEquals(record.dataAt(j), record.removeAt(j));
        }
        
        assertEquals(record.size(), 0);
        
        for (int j = 0; j < cols; ++j) {
            record.add("keysomething" + j, IntData.valueOf(j));
        }
        
        assertEquals(record.size(), cols);
        
        for (int j = 0; j < record.size(); ++j) {
            assertEquals(record.get(record.nameAt(j)).toString(), String.valueOf(j));
        }
    }
    @Test
    public void test() {
        Record sourceRecord = new Record();
        sourceRecord.add("key1", StringData.valueOf("baidu"));
        
        Record destRecord = (Record) sourceRecord.clone();
        assertEquals(destRecord.size(), 1);
        
        destRecord.setAt(0, StringData.valueOf("google"));
        assertEquals(destRecord.compareTo(sourceRecord) > 0, true);
        
        destRecord.setAt(0, StringData.valueOf("baidu"));
        assertEquals(destRecord.compareTo(sourceRecord), 0);
        
        destRecord.add("key2", StringData.valueOf("t"));
        assertEquals(destRecord.compareTo(sourceRecord) > 0, true);
        
        Record dest2 = destRecord.shallowCopy();
        Record dest3 = destRecord.additionCopy(2);
        
        assertEquals(destRecord.equals(dest2), true);
        assertEquals(dest3.equals(dest2), true);
        
        dest3.add("key3", StringData.valueOf("sohu"));
        assertEquals(dest3.equals(dest2), false);

        dest3.add("key4", StringData.valueOf("sina"));
        assertEquals(dest3.size(), 4);
        
        dest3.add("key5", StringData.valueOf("qq"));
        assertEquals(dest3.size(), 5);
    }
    
    @Test
    public void hashcodeTest() {
        final Record record1 = new Record();
        record1.add("key1", StringData.valueOf("baidu"))
               .add("key2", StringData.valueOf("baidu.com"))
               .add("key3", StringData.valueOf("/a.html"));
        
        final Record record2 = new Record();
        record2.add("key1", StringData.valueOf("baidu"))
               .add("key2", StringData.valueOf("baidu.com"))
               .add("key3", StringData.valueOf("/b.html"));
        
        final Record record3 = new Record();
        record3.add("key1", StringData.valueOf("baidu"))
               .add("key2", StringData.valueOf("baidu.com"));
        
        final Record record4 = new Record();
        record4.add("key1", StringData.valueOf("baidu"))
               .add("key2", StringData.valueOf("baidu.com"))
               .add("key3", StringData.NULL);
        
        final Record record5 = new Record();
        record5.add("key1", StringData.valueOf("baidu"))
               .add("key2", StringData.valueOf(""));
        
        final Record record6 = new Record();
        record6.add("key1", StringData.valueOf("baidu"))
               .add("key2", StringData.valueOf("baidu.com"));
        
        assertEquals(record1.equals(record2), false);
        assertEquals(record1.compareTo(record2) < 0, true);
        assertEquals(record1.compareTo(record3) > 0, true);
        assertEquals(record3.compareTo(record4) < 0, true);
        assertEquals(record3.compareTo(record5) > 0, true);
        assertEquals(record3.compareTo(record6), 0);
    }
    
    @Test
    public void nullCompareTest2() {
        Object a = null;
        assertEquals(a instanceof TypeData, false);
        
        TypeData b = null;
        assertEquals(b instanceof TypeData, false);
        
    }
    
    @Test
    public void compareTest() {
        assertEquals(IntData.valueOf(1).compareTo(LongData.valueOf(1l)), 0);
        assertEquals(LongData.valueOf(1l).compareTo(new FloatData(1.0f)), 0);
        assertEquals(new FloatData(1.35f).compareTo(new DoubleData(1.35d)), 0);
        assertEquals(new DoubleData(1.35d).compareTo(new FloatData(1.35f)), 0);
        assertEquals(new DoubleData(1.35d).compareTo(new DecimalData(new BigDecimal("1.35"))), 0);
        assertEquals(new DoubleData(1.35d).compareTo(StringData.valueOf("1")), 1);
        assertEquals(new DoubleData(1.35d).compareTo(StringData.valueOf("foo")) < 0, true);
        assertEquals(new NumericData(1.35d).compareTo(new FloatData(1.35f)), 0);
        assertEquals(new NumericData(1.35d).compareTo(new DecimalData(new BigDecimal("1.35"))), 0);
        assertEquals(new NumericData(1.35d).compareTo(StringData.valueOf("1")), 1);
        assertEquals(new NumericData(1.35d).compareTo(StringData.valueOf("foo")) < 0, true);
        assertEquals(StringData.valueOf("1.35").compareTo(new DecimalData(new BigDecimal("1.35"))), 0);
        assertEquals(StringData.valueOf("1.35").compareTo(new DoubleData(1.35d)), 0);
        assertEquals(StringData.valueOf("1.34").compareTo(new DecimalData(new BigDecimal("1.35"))), -1);
    }
    
    @Test
    public void addTest() {
        Record record = new Record();
        record.add("0", StringData.valueOf("foo0"));
        record.add("1", StringData.valueOf("foo1"));
        record.add("2", StringData.valueOf("foo2"));
        record.add("1", StringData.valueOf("bar1"));
        
        assertEquals(record.get("1").toString(), "bar1");
        assertEquals(record.dataAt(1).toString(), "bar1");
        assertEquals(record.dataAt(0).toString(), "foo0");
        assertEquals(record.dataAt(2).toString(), "foo2");

        record.remove("1");
        assertEquals(record.dataAt(1).toString(), "foo2");

        record.add("2", StringData.valueOf("bar2"));
        assertEquals(record.dataAt(1).toString(), "bar2");

        record.add("3", StringData.valueOf("bar3"));
        assertEquals(record.dataAt(2).toString(), "bar3");
        

        record.clear();
        assertEquals(record.size(), 0);
        
        record.add("1", IntData.ONE);
        assertEquals(record.dataAt(0), IntData.valueOf(1));
        assertEquals(record.get("1"), IntData.valueOf(1));
        
        //test boundaries of 2 and more
        int[] sizes = new int[] {1,2,3,4,7,8,16,64,1000};
        for (int s : sizes) {
            Record r = new Record(s);
            //new add
            for (int j = 0; j < s; ++j) {
                r.add("abcdefghi" + j, IntData.valueOf(j));
            }
            for (int j = 0; j < s; ++j) {
                assertEquals(r.get("abcdefghi" + j), IntData.valueOf(j));
            }
            //existing add
            for (int j = 0; j < s; ++j) {
                r.add("abcdefghi" + j, IntData.valueOf(j * 2));
            }
            for (int j = 0; j < s; ++j) {
                assertEquals(r.get("abcdefghi" + j), IntData.valueOf(j * 2));
            }
            //existing remove
            for (int j = 0; j < s; ++j) {
                assertEquals(r.remove("abcdefghi" + j),IntData.valueOf(j * 2));
            }
            for (int j = 0; j < s; ++j) {
                assertEquals(r.get("abcdefghi" + j), NullData.INSTANCE);
            }
            //tombstone add
            for (int j = 0; j < s; ++j) {
                r.add("abcdefghi" + j, IntData.valueOf(j * 3));
            }
            for (int j = 0; j < s; ++j) {
                assertEquals(r.get("abcdefghi" + j), IntData.valueOf(j * 3));
            }
            //not present
            for (int j = 0; j < s; ++j) {
                assertEquals(r.get("abcdek" + j), NullData.INSTANCE);
            }
        }
        
        for (int s : sizes) {
            //expanding record
            Record r = new Record();
            //new add
            for (int j = 0; j < s; ++j) {
                r.add("abcdefghi" + j, IntData.valueOf(j));
            }
            for (int j = 0; j < s; ++j) {
                assertEquals(r.get("abcdefghi" + j), IntData.valueOf(j));
            }
            //existing add
            for (int j = 0; j < s; ++j) {
                r.add("abcdefghi" + j, IntData.valueOf(j * 2));
            }
            for (int j = 0; j < s; ++j) {
                assertEquals(r.get("abcdefghi" + j), IntData.valueOf(j * 2));
            }
            //existing remove
            for (int j = 0; j < s; ++j) {
                assertEquals(r.remove("abcdefghi" + j),IntData.valueOf(j * 2));
            }
            for (int j = 0; j < s; ++j) {
                assertEquals(r.get("abcdefghi" + j), NullData.INSTANCE);
            }
            //tombstone add
            for (int j = 0; j < s; ++j) {
                r.add("abcdefghi" + j, IntData.valueOf(j * 3));
            }
            for (int j = 0; j < s; ++j) {
                assertEquals(r.get("abcdefghi" + j), IntData.valueOf(j * 3));
            }
        }
    }
    
    @Test
    public void addAllTest() {
        Record record1 = new Record(2);
        record1.add("0", StringData.valueOf("foo0"));
        record1.add("1", StringData.valueOf("foo1"));
        
        Record record2 = new Record(4);
        record2.add("2", StringData.valueOf("foo2"));
        record2.add("3", StringData.valueOf("foo3"));
        
        record1.addAll(record2);
        assertEquals(record1.size(), 4);
        assertEquals(record1.dataAt(0).toString(), "foo0");
        assertEquals(record1.dataAt(1).toString(), "foo1");
        assertEquals(record1.dataAt(2).toString(), "foo2");
        assertEquals(record1.dataAt(3).toString(), "foo3");
        
        record1.clear();
        record1.addAll(record2);
        assertEquals(record1.size(), 2);
        assertEquals(record1.dataAt(0).toString(), "foo2");
        assertEquals(record1.dataAt(1).toString(), "foo3");
    }
    
    @Test
    public void removeTest() {
        Record record = new Record();
        record.add("0", StringData.valueOf("foo0"));
        record.add("1", StringData.valueOf("foo1"));
        record.add("2", StringData.valueOf("foo2"));
        record.add("3", StringData.valueOf("foo3"));
        record.add("4", StringData.valueOf("foo4"));
        record.add("5", StringData.valueOf("foo5"));
        record.add("6", StringData.valueOf("foo6"));
        
        record.remove("1");
        record.remove("3");
        record.removeAt(4);
        
        assertEquals(record.get("5").toString(), "foo5");
        assertEquals(record.nameAt(3), "5");
        assertEquals(record.dataAt(3).toString(), "foo5");
        assertEquals(record.nameAt(0), "0");
        assertEquals(record.get("0").toString(), "foo0");
        assertEquals(record.dataAt(0).toString(), "foo0");
        
        record.add("7", StringData.valueOf("foo7"));
        record.add("8", StringData.valueOf("foo8"));
        record.remove("7");
        
        assertEquals(record.get("8").toString(), "foo8");
        assertEquals(record.dataAt(4).toString(), "foo8");
        
        //begin to end
        Record r = new Record(1000);
        for (int j = 0; j < 1000; ++j) {
            r.add("abcdef" + j, IntData.valueOf(j));
        }
        Record r2 = r.shallowCopy();
        //begin to end
        for (int j = 0; j < 1000; ++j) {
            assertEquals(r.remove("abcdef" + j), IntData.valueOf(j));
        }
        //end to begin
        for (int j = 999; j >= 0; --j) {
            assertEquals(r2.remove("abcdef" + j), IntData.valueOf(j));
        }
    }
    
    @Test
    public void copyTest() {
        try {
            Record r1 = new Record(0).add("e1", IntData.valueOf(1)).add("e2", IntData.valueOf(1));
            Record r2 = r1.shallowCopy();
            r2.add("e1", IntData.valueOf(2));
            
            assertEquals(1, r1.dataAt(0).toInt().intValue());
            assertEquals(2, r2.dataAt(0).toInt().intValue());
            
            Record r3 = r1.shallowCopy();
            r3.add("e1", IntData.valueOf(3));
            
            Record r4 = r1.templateCopy();
            r4.add("e1", IntData.valueOf(4));
            r4.add("e2", IntData.valueOf(5));
            r4.add("e3", IntData.valueOf(6));
            r4.add("e4", IntData.valueOf(7));
            
            Record r5 = r1.templateAdditionCopy(r4);
            r5.add("e1", IntData.valueOf(5));
            r5.add("e3", IntData.valueOf(50));
            assertEquals(4, r5.size());
            

            Record r6 = r5.shallowCopy();
            r6.remove("e2");
            r6.remove("e1");
            
            Record r7 = r5.shallowCopy();
            r7.remove("e3");
            r7.remove("e4");
            
            final List<Integer> indices6 = new ArrayList<>();
            indices6.add(0);
            indices6.add(1);
            
            final List<Integer> indices7 = new ArrayList<>();
            indices7.add(2);
            indices7.add(3);
            
            Record r8 = r5.templateDeletionCopy(r6, indices6);
            Record r9 = r5.templateDeletionCopy(r7, indices7);
            
            assertEquals(1, r1.dataAt(0).toInt().intValue());
            assertEquals(3, r3.dataAt(0).toInt().intValue());
            assertEquals(2, r2.dataAt(0).toInt().intValue());
            assertEquals(4, r4.dataAt(0).toInt().intValue());
            assertEquals(5, r5.dataAt(0).toInt().intValue());
            assertEquals(50, r5.dataAt(2).toInt().intValue());
            assertEquals(true, r5.dataAt(3).isNull());
            assertEquals(2, r6.size());
            assertEquals(2, r7.size());
            assertEquals(2, r8.size());
            assertEquals(2, r9.size());
            assertEquals(true, r6.equals(r8));
            assertEquals(true, r7.equals(r9));
            
            Record r10 = r5.shallowCopy();
            r10.remove("e1");
            r10.remove("e3");
            r10.remove("e2");
            r10.remove("e4");
            
            final List<Integer> indices10 = new ArrayList<>();
            indices10.add(0);
            indices10.add(1);
            indices10.add(1);
            indices10.add(3);
            
            Record r11 = r5.templateDeletionCopy(r10, indices10);
            assertEquals(0, r11.size());
            
            r11.add("e1", NullData.INSTANCE);
            assertEquals(1, r11.size());
            
            
        } catch (EngineException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void copyCapacityTest() {
        try {
            Record r1 = new Record(20).add("e1", IntData.valueOf(1));
            Record r2 = r1.shallowCopy();
            r2.add("e1", IntData.valueOf(2));
            
            assertEquals(1, r1.dataAt(0).toInt().intValue());
            assertEquals(2, r2.dataAt(0).toInt().intValue());
            
            Record r3 = r1.shallowCopy();
            r3.add("e1", IntData.valueOf(3));
            
            Record r4 = r1.templateCopy();
            r4.add("e1", IntData.valueOf(4));
            r4.add("e2", IntData.valueOf(5));
            r4.add("e3", IntData.valueOf(6));
            
            Record r5 = r1.templateAdditionCopy(r4);
            r5.add("e1", IntData.valueOf(5));
            r5.add("e3", IntData.valueOf(50));
            assertEquals(3, r5.size());
            
            assertEquals(1, r1.dataAt(0).toInt().intValue());
            assertEquals(3, r3.dataAt(0).toInt().intValue());
            assertEquals(2, r2.dataAt(0).toInt().intValue());
            assertEquals(4, r4.dataAt(0).toInt().intValue());
            assertEquals(5, r5.dataAt(0).toInt().intValue());
            assertEquals(50, r5.dataAt(2).toInt().intValue());
            assertEquals(true, r5.dataAt(1).isNull());
            
        } catch (EngineException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException, EngineException {
        final Record record = new Record().add("a", BooleanData.TRUE).add("b", IntData.valueOf(100));
        
        assertEquals(deserializeExternalizable(serializeExternalizable(location, record)), record);
        assertEquals(deserializeExternalizable(serializeExternalizable(location, new Record())), new Record()
                );
    }
    
    private String serializeExternalizable(final String loc, final Record record) throws IOException {
        final FileOutputStream fileOut = new FileOutputStream(loc);
        final ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
        outStream.writeObject(record);
        outStream.close();
        fileOut.close();
        return loc;
    }
    
    private Record deserializeExternalizable(final String loc) throws IOException, ClassNotFoundException {
        final FileInputStream fileIn = new FileInputStream(loc);
        final ObjectInputStream inStream = new ObjectInputStream(fileIn);
        final Record obj = (Record) inStream.readObject();
        inStream.close();
        fileIn.close();
        return obj;
    }
}
