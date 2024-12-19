package com.exceeddata.ac.common.data.typedata;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

public class IteratorTest {
    @Test
    public void testDenseVectorIterator() {
        final double[] dbls1 = new double[100];
        final DenseVectorData vdata1 = new DenseVectorData(dbls1);
        
        final Iterator<DoubleData> iter1 = vdata1.iterator();
        while (iter1.hasNext()) {
            assertEquals(iter1.next().compareTo(DoubleData.ZERO), 0);
        }
        
        final Iterator<Double> diter1 = vdata1.vectorIterator();
        while (diter1.hasNext()) {
            assertEquals(Double.compare(diter1.next(), 0d), 0);
        }
        
        final double[] dbls2 = new double[100];
        for (int i = 0; i < 100; i++) { dbls2[i] = i; }
        final DenseVectorData vdata2 = new DenseVectorData(dbls2);
        final Iterator<DoubleData> iter2 = vdata2.iterator();
        
        int j = 0;
        while (iter2.hasNext()) {
            assertEquals(iter2.next().compareTo(DoubleData.valueOf((double) j++)), 0);
        }
        
        final Iterator<Double> diter2 = vdata2.vectorIterator();
        j = 0;
        while (diter2.hasNext()) {
            assertEquals(Double.compare(diter2.next(), j++), 0);
        }
        
        final double[] dbls3 = new double[100];
        for (int i = 0; i < 100; i++) { dbls3[i] =(double) i; }
        final DenseVectorData vdata3 = new DenseVectorData(dbls3);
        
        final Iterator<Double> diter3 = vdata3.vectorIterator();
        j = 0;
        while (diter3.hasNext()) {
            assertEquals(Double.compare(diter3.next(), j++), 0);
        }
    }
    
    @Test
    public void testSparseVectorIterator() {
        final double[] dbls1 = new double[100];
        final SparseVectorData vdata1 = new SparseVectorData(dbls1);
        
        final Iterator<DoubleData> iter1 = vdata1.iterator();
        while (iter1.hasNext()) {
            assertEquals(iter1.next().compareTo(DoubleData.ZERO), 0);
        }
        
        final Iterator<Double> diter1 = vdata1.vectorIterator();
        while (diter1.hasNext()) {
            assertEquals(Double.compare(diter1.next(), 0d), 0);
        }
        
        final double[] dbls2 = new double[100];
        for (int i = 0; i < 100; i++) { dbls2[i] = i; }
        final SparseVectorData vdata2 = new SparseVectorData(dbls2);
        final Iterator<DoubleData> iter2 = vdata2.iterator();
        
        int j = 0;
        while (iter2.hasNext()) {
            assertEquals(iter2.next().compareTo(DoubleData.valueOf((double) j++)), 0);
        }
        
        final Iterator<Double> diter2 = vdata2.vectorIterator();
        j = 0;
        while (diter2.hasNext()) {
            assertEquals(Double.compare(diter2.next(), j++), 0);
        }
        
        final double[] dbls3 = new double[100];
        for (int i = 0; i < 100; i++) { dbls3[i] = (double) i; }
        final SparseVectorData vdata3 = new SparseVectorData(dbls3);
        
        final Iterator<Double> diter3 = vdata3.vectorIterator();
        j = 0;
        while (diter3.hasNext()) {
            assertEquals(Double.compare(diter3.next(), j++), 0);
        }
        
        final double[] dbls4 = new double[400];
        for (int i = 0; i < 100; i++) { dbls4[i] = (double) i; }
        for (int i = 100; i < 200; i++) { dbls4[i] = 0d; }
        for (int i = 200; i < 300; i++) { dbls4[i] = (double) i; }
        for (int i = 300; i < 400; i++) { dbls4[i] = 0d; }
        final SparseVectorData vdata4 = new SparseVectorData(dbls4);
        
        final Iterator<Double> diter4 = vdata4.vectorIterator();
        j = 0;
        while (diter4.hasNext()) {
            if (j < 100) {
                assertEquals(Double.compare(diter4.next(), j++), 0);
            } else if (j < 200) {
                ++j;
                assertEquals(Double.compare(diter4.next(), 0d), 0);
            } else if (j < 300) {
                assertEquals(Double.compare(diter4.next(), j++), 0);
            } else if (j < 400) {
                ++j;
                assertEquals(Double.compare(diter4.next(), 0d), 0);
            } 
        }
        
        final Iterator<DoubleData> diter4d = vdata4.iterator();
        j = 0;
        while (diter4d.hasNext()) {
            if (j < 100) {
                assertEquals(diter4d.next().compareTo(IntData.valueOf(j++)), 0);
            } else if (j < 200) {
                ++j;
                assertEquals(diter4d.next().compareTo(IntData.valueOf(0)), 0);
            } else if (j < 300) {
                assertEquals(diter4d.next().compareTo(IntData.valueOf(j++)), 0);
            } else if (j < 400) {
                ++j;
                assertEquals(diter4d.next().compareTo(IntData.valueOf(0)), 0);
            } 
        }
    }
}
