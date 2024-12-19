package com.exceeddata.ac.common.util;

import com.exceeddata.ac.common.data.record.Record;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.NumericData;
import com.exceeddata.ac.common.data.typedata.StringData;

public class SizeOfTest {
    
    //@Test
    public void testRecord() throws Exception {
        // Warm up all classes/methods we will use
        runGC ();
        usedMemory ();
        // Array to keep strong references to allocated objects
        final int count = 100000;
        Record [] objects = new Record [count];
        
        final StringData a = StringData.valueOf("a");
        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        for (int i = -1; i < count; ++ i) {
            // Instantiate your data here and assign it to object
            Record record = new Record();
            record.add("cntbc" + 1, a);
            record.add("cntbc" + 2, a);
            record.add("cntbc" + 3, a);
            record.add("cntbc" + 4, a);
            record.add("cntbc" + 5, a);
            
            
            if (i >= 0) {
                objects [i] = record;
            } else {
                record = null; // Discard the warm up object
                runGC ();
                heap1 = usedMemory (); // Take a before heap snapshot
            }
        }
        runGC ();
        long heap2 = usedMemory (); // Take an after heap snapshot:
        
        final int size = Math.round (((float)(heap2 - heap1))/count);
        System.out.println ("'before' heap: " + heap1 +
                            ", 'after' heap: " + heap2);
        System.out.println ("record heap delta: " + (heap2 - heap1) +
            ", {" + objects [0].getClass () + "} size = " + size + " bytes");
        for (int i = 0; i < count; ++ i) objects [i] = null;
        objects = null;
    }
    
    //@Test
    public void testDoubleData() throws Exception {
        // Warm up all classes/methods we will use
        runGC ();
        usedMemory ();
        // Array to keep strong references to allocated objects
        final int rows = 100;
        final int cols = 10000;
        final int count = rows * cols;
        Object [] objects = new Object [count];
        
        Object object = null;
        object = DoubleData.nonNullValueOf(10d);
        
        object = null; // Discard the warm up object
        runGC ();
        long heap1 = usedMemory (); // Take a before heap snapshot
        
        // Allocate count+1 objects, discard the first one
        for (int i = 0; i < rows; ++ i) {
            for (int j = 0; j < cols; ++j) {
                object = null;
                
                // Instantiate your data here and assign it to object
                
                object = DoubleData.nonNullValueOf((double) j);
                objects [i * cols + j] = object;
            }
        }
        
        runGC ();
        long heap2 = usedMemory (); // Take an after heap snapshot:
        
        final int size = Math.round (((float)(heap2 - heap1))/count);
        System.out.println ("'before' heap: " + heap1 +
                            ", 'after' heap: " + heap2);
        System.out.println ("double heap delta: " + (heap2 - heap1) +
            ", {" + objects [0].getClass () + "} size = " + size + " bytes");
        for (int i = 0; i < count; ++ i) objects [i] = null;
        objects = null;
    }
    
    //@Test
    public void testNumericData() throws Exception {
        // Warm up all classes/methods we will use
        runGC ();
        usedMemory ();
        // Array to keep strong references to allocated objects
        final int rows = 100;
        final int cols = 10000;
        final int count = rows * cols;
        Object [] objects = new Object [count];
        
        Object object = null;
        object = NumericData.nonNullValueOf(10d);
        
        object = null; // Discard the warm up object
        runGC ();
        long heap1 = usedMemory (); // Take a before heap snapshot
        
        // Allocate count+1 objects, discard the first one
        for (int i = 0; i < rows; ++ i) {
            for (int j = 0; j < cols; ++j) {
                object = null;
                
                // Instantiate your data here and assign it to object
                
                object = NumericData.nonNullValueOf((double) j);
                objects [i * cols + j] = object;
            }
        }
        
        runGC ();
        long heap2 = usedMemory (); // Take an after heap snapshot:
        
        final int size = Math.round (((float)(heap2 - heap1))/count);
        System.out.println ("'before' heap: " + heap1 +
                            ", 'after' heap: " + heap2);
        System.out.println ("numeric heap delta: " + (heap2 - heap1) +
            ", {" + objects [0].getClass () + "} size = " + size + " bytes");
        for (int i = 0; i < count; ++ i) objects [i] = null;
        objects = null;
    }
    
    private static void runGC () throws Exception {
        // It helps to call Runtime.gc()
        // using several method calls:
        for (int r = 0; r < 4; ++r) _runGC ();
    }
    
    private static void _runGC () throws Exception {
        long usedMem1 = usedMemory (), usedMem2 = Long.MAX_VALUE;
        for (int i = 0; (usedMem1 < usedMem2) && (i < 500); ++i) {
            s_runtime.runFinalization ();
            s_runtime.gc ();
            Thread.yield ();
            
            usedMem2 = usedMem1;
            usedMem1 = usedMemory ();
        }
    }
    
    private static long usedMemory ()
    {
        return s_runtime.totalMemory () - s_runtime.freeMemory ();
    }
    
    private static final Runtime s_runtime = Runtime.getRuntime ();
}
