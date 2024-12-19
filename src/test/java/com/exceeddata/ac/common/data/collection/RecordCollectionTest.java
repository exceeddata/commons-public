package com.exceeddata.ac.common.data.collection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.exceeddata.ac.common.data.record.Record;
import com.exceeddata.ac.common.data.typedata.StringData;

public class RecordCollectionTest {

    @Test
    public void testCompare() {
        final Record r1 = new Record().add("f1", StringData.valueOf("a"));
        final Record r2 = new Record().add("f1", StringData.valueOf("a"))
                                      .add("f2", StringData.valueOf("b"));
        final RecordCollection d1 = new RecordCollection();
        final RecordCollection d2 = new RecordCollection();
        final RecordCollection d3 = new RecordCollection().add(r1);
        final RecordCollection d4 = new RecordCollection().add(r2);
        
        assertEquals(d1.compareTo(d2) == 0, true);
        assertEquals(d1.compareTo(d3) < 0, true);
        assertEquals(d3.compareTo(d4) < 0, true);
        assertEquals(d4.compareTo(d3) > 0, true);
        assertEquals(d4.compareTo(d2) > 0, true);
    }
    
    @Test
    public void testPeekPoll() {
        final Record r1 = new Record().add("f1", StringData.valueOf("a"));
        final Record r2 = new Record().add("f2", StringData.valueOf("b"));
        final Record r3 = new Record().add("f3", StringData.valueOf("c"));
        final Record r4 = new Record().add("f4", StringData.valueOf("d"));
        
        final RecordCollection d = new RecordCollection();
        d.add(r1);
        assertEquals(d.peek().dataAt(0).toString(), "a");
        assertEquals(d.poll().dataAt(0).toString(), "a");
        assertEquals(d.size(), 0);
        
        d.add(r2);
        d.add(r3);
        d.add(r4);
        assertEquals(d.peek().dataAt(0).toString(), "b");
        assertEquals(d.poll().dataAt(0).toString(), "b");
        assertEquals(d.size(), 2);
        assertEquals(d.peekLast().dataAt(0).toString(), "d");
        assertEquals(d.pollLast().dataAt(0).toString(), "d");
        assertEquals(d.size(), 1);
    }
    
    @Test
    public void testCapacity() {
        final Record r1 = new Record().add("f1", StringData.valueOf("a"));
        final Record r2 = new Record().add("f2", StringData.valueOf("b"));
        final Record r3 = new Record().add("f3", StringData.valueOf("c"));
        final Record r4 = new Record().add("f4", StringData.valueOf("d"));
        final Record r5 = new Record().add("f5", StringData.valueOf("e"));
        final Record r6 = new Record().add("f6", StringData.valueOf("f"));
        final Record r7 = new Record().add("f7", StringData.valueOf("g"));
        final Record r8 = new Record().add("f8", StringData.valueOf("h"));
        final Record r9 = new Record().add("f9", StringData.valueOf("i"));
        
        //empty test
        RecordCollection d = new RecordCollection(1);
        d.ensureCapacity(4);
        assertEquals(d.peek(0), null);
        assertEquals(d.peek(1), null);
        assertEquals(d.peek(2), null);
        assertEquals(d.peek(3), null);
        
        //overflow test
        d = new RecordCollection(1);
        d.add(r1).add(r2).add(r3);
        assertEquals(d.peek(0).dataAt(0).toString(), "a");
        assertEquals(d.peek(1).dataAt(0).toString(), "b");
        assertEquals(d.peek(2).dataAt(0).toString(), "c");
        assertEquals(d.peek(3), null);
        d.add(r4);
        assertEquals(d.peek(3).dataAt(0).toString(), "d");
        
        //overflow test 2
        d = new RecordCollection(2);
        d.add(r1).add(r2).add(r3);
        assertEquals(d.peek(0).dataAt(0).toString(), "a");
        assertEquals(d.peek(1).dataAt(0).toString(), "b");
        assertEquals(d.peek(2).dataAt(0).toString(), "c");
        d.add(r4);
        d.add(r5);
        d.add(r6);
        d.add(r7);
        d.add(r8);
        d.poll();
        d.add(r9);
        assertEquals(d.peek(0).dataAt(0).toString(), "b");
        assertEquals(d.peek(1).dataAt(0).toString(), "c");
        assertEquals(d.peek(2).dataAt(0).toString(), "d");
        assertEquals(d.peek(3).dataAt(0).toString(), "e");
        assertEquals(d.peek(4).dataAt(0).toString(), "f");
        assertEquals(d.peek(5).dataAt(0).toString(), "g");
        assertEquals(d.peek(6).dataAt(0).toString(), "h");
        assertEquals(d.peek(7).dataAt(0).toString(), "i");
    }
}
