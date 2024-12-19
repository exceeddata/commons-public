package com.exceeddata.ac.common.data.typedata;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.junit.Test;

import com.exceeddata.ac.common.exception.EngineException;

public class TypeDataTest2 {
    final ListData list1 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(1d)})));
    final ListData list2a = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(2d)})));
    final ListData list2b = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(1d), new DoubleData(2d)})));
    final ListData list3a = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(0d), new DoubleData(1d)})));
    final ListData list3b = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(0d)})));
    
    final SetData set1 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(1d)})));
    final SetData set2a = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(2d)})));
    final SetData set2b = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(1d), new DoubleData(2d)})));
    final SetData set3a = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(0d), new DoubleData(1d)})));
    final SetData set3b = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(0d)})));
    
    final DenseVectorData dense1 = new DenseVectorData(new double [] {1d});
    final DenseVectorData dense2a = new DenseVectorData(new double [] {2d});
    final DenseVectorData dense2b = new DenseVectorData(new double [] {1d, 2d});
    final DenseVectorData dense3a = new DenseVectorData(new double [] {0d, 1d});
    final DenseVectorData dense3b = new DenseVectorData(new double [] {0d});
    
    final SparseVectorData sparse1 = new SparseVectorData(new double[] {1d});
    final SparseVectorData sparse2a = new SparseVectorData(new double[] {2d});
    final SparseVectorData sparse2b = new SparseVectorData(new double[] {1d, 2d});
    final SparseVectorData sparse3a = new SparseVectorData(new double[] {0d, 1d});
    final SparseVectorData sparse3b = new SparseVectorData(new double[] {0d});

    /*
     * List is a non-ordered non-unique list of items (equality check)
     * Set is a non-sequential unique list of items (hash check)
     * Dense Vector is a ordered non-unique list of items
     * Sparse Vector is a ordered non-unique list of items but with sparse data structure
     */
    
    @Test
    public void testToCalendar() throws EngineException {
        final StringData s = new StringData("2018-11-06 15:23:16:022");
        final CalendarTimestampData cst = DataConv.toCalendarTimestampData(s);
        assertEquals(cst.isNull(), true);
    }
    
        
    @Test
    public void testTypeCompare() {
        testBasic(new DecimalData(BigDecimal.valueOf(1)));
        testBasic(IntData.valueOf(1));
        testBasic(LongData.valueOf(1l));
        testBasic(new FloatData(1f));
        testBasic(new DoubleData(1d));
        testBasic(new NumericData(1d));
        testBasic(StringData.valueOf("1"));
        
        testCollection1(list1);
        testCollection1(set1);
        testCollection1(dense1);
        testCollection1(sparse1);
        
        testCollection2b(list2b);
        testCollection2b(set2b);
        testCollection2b(dense2b);
        testCollection2b(sparse2b);
        
        
    }
    
    private void testBasic(final TypeData testant) {
        assertEquals(0, testant.compareTo(new DecimalData(BigDecimal.valueOf(1))));
        assertEquals(-1, testant.compareTo(new DecimalData(BigDecimal.valueOf(2))));
        assertEquals(1, testant.compareTo(new DecimalData(BigDecimal.valueOf(0))));
        assertEquals(0, testant.compareTo(IntData.valueOf(1)));
        assertEquals(-1, testant.compareTo(IntData.valueOf(2)));
        assertEquals(1, testant.compareTo(IntData.valueOf(0)));
        assertEquals(0, testant.compareTo(LongData.valueOf(1l)));
        assertEquals(-1, testant.compareTo(LongData.valueOf(2l)));
        assertEquals(1, testant.compareTo(LongData.valueOf(0l)));
        assertEquals(0, testant.compareTo(new FloatData(1f)));
        assertEquals(-1, testant.compareTo(new FloatData(2f)));
        assertEquals(1, testant.compareTo(new FloatData(0f)));
        assertEquals(0, testant.compareTo(new DoubleData(1d)));
        assertEquals(-1, testant.compareTo(new DoubleData(2d)));
        assertEquals(1, testant.compareTo(new DoubleData(0d)));
        assertEquals(0, testant.compareTo(new NumericData(1d)));
        assertEquals(-1, testant.compareTo(new NumericData(2d)));
        assertEquals(1, testant.compareTo(new NumericData(0d)));
        assertEquals(0, testant.compareTo(StringData.valueOf("1")));
        assertEquals(-1, testant.compareTo(StringData.valueOf("2")));
        assertEquals(1, testant.compareTo(StringData.valueOf("0")));
        
        assertEquals(-1, testant.compareTo(list1));
        assertEquals(-1, testant.compareTo(list2a));
        assertEquals(-1, testant.compareTo(list2b));
        assertEquals(1, testant.compareTo(list3a));
        assertEquals(1, testant.compareTo(list3b));
        
        assertEquals(-1, testant.compareTo(set1));
        assertEquals(-1, testant.compareTo(set2a));
        assertEquals(-1, testant.compareTo(set2b));
        assertEquals(1, testant.compareTo(set3a));
        assertEquals(1, testant.compareTo(set3b));
        
        assertEquals(-1, testant.compareTo(dense1));
        assertEquals(-1, testant.compareTo(dense2a));
        assertEquals(-1, testant.compareTo(dense2b));
        assertEquals(1, testant.compareTo(dense3a));
        assertEquals(1, testant.compareTo(dense3b));
        
        assertEquals(-1, testant.compareTo(sparse1));
        assertEquals(-1, testant.compareTo(sparse2a));
        assertEquals(-1, testant.compareTo(sparse2b));
        assertEquals(1, testant.compareTo(sparse3a));
        assertEquals(1, testant.compareTo(sparse3b));
    }
    
    private void testCollection1(final TypeData testant) {
        assertEquals(1, testant.compareTo(new DecimalData(BigDecimal.valueOf(1))));
        assertEquals(-1, testant.compareTo(new DecimalData(BigDecimal.valueOf(2))));
        assertEquals(1, testant.compareTo(new DecimalData(BigDecimal.valueOf(0))));
        assertEquals(1, testant.compareTo(IntData.valueOf(1)));
        assertEquals(-1, testant.compareTo(IntData.valueOf(2)));
        assertEquals(1, testant.compareTo(IntData.valueOf(0)));
        assertEquals(1, testant.compareTo(LongData.valueOf(1l)));
        assertEquals(-1, testant.compareTo(LongData.valueOf(2l)));
        assertEquals(1, testant.compareTo(LongData.valueOf(0l)));
        assertEquals(1, testant.compareTo(new FloatData(1f)));
        assertEquals(-1, testant.compareTo(new FloatData(2f)));
        assertEquals(1, testant.compareTo(new FloatData(0f)));
        assertEquals(1, testant.compareTo(new DoubleData(1d)));
        assertEquals(-1, testant.compareTo(new DoubleData(2d)));
        assertEquals(1, testant.compareTo(new DoubleData(0d)));
        assertEquals(1, testant.compareTo(new NumericData(1d)));
        assertEquals(-1, testant.compareTo(new NumericData(2d)));
        assertEquals(1, testant.compareTo(new NumericData(0d)));
        assertEquals(1, testant.compareTo(StringData.valueOf("1")));
        assertEquals(-1, testant.compareTo(StringData.valueOf("2")));
        assertEquals(1, testant.compareTo(StringData.valueOf("0")));
    }
    
    private void testCollection2b(final TypeData testant) {
        assertEquals(1, testant.compareTo(new DecimalData(BigDecimal.valueOf(1))));
        assertEquals(-1, testant.compareTo(new DecimalData(BigDecimal.valueOf(2))));
        assertEquals(1, testant.compareTo(new DecimalData(BigDecimal.valueOf(0))));
        assertEquals(1, testant.compareTo(IntData.valueOf(1)));
        assertEquals(-1, testant.compareTo(IntData.valueOf(2)));
        assertEquals(1, testant.compareTo(IntData.valueOf(0)));
        assertEquals(1, testant.compareTo(LongData.valueOf(1l)));
        assertEquals(-1, testant.compareTo(LongData.valueOf(2l)));
        assertEquals(1, testant.compareTo(LongData.valueOf(0l)));
        assertEquals(1, testant.compareTo(new FloatData(1f)));
        assertEquals(-1, testant.compareTo(new FloatData(2f)));
        assertEquals(1, testant.compareTo(new FloatData(0f)));
        assertEquals(1, testant.compareTo(new DoubleData(1d)));
        assertEquals(-1, testant.compareTo(new DoubleData(2d)));
        assertEquals(1, testant.compareTo(new DoubleData(0d)));
        assertEquals(1, testant.compareTo(new NumericData(1d)));
        assertEquals(-1, testant.compareTo(new NumericData(2d)));
        assertEquals(1, testant.compareTo(new NumericData(0d)));
        assertEquals(1, testant.compareTo(StringData.valueOf("1")));
        assertEquals(-1, testant.compareTo(StringData.valueOf("2")));
        assertEquals(1, testant.compareTo(StringData.valueOf("0")));
    }
}
