package com.exceeddata.ac.common.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.junit.Test;

import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DenseVectorData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.IntData;
import com.exceeddata.ac.common.data.typedata.ListData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.SetData;
import com.exceeddata.ac.common.data.typedata.SparseVectorData;
import com.exceeddata.ac.common.data.typedata.StringData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class TypeDataUtilsTest {
    @Test
    public void testDenseVectorDeduct() throws EngineException {
        final DenseVectorData d1 = new DenseVectorData(new double [] {0d, 1d, 1D});
        final DenseVectorData d2 = new DenseVectorData(new double [] {0d, 1d, 2d});
        final DenseVectorData dNull = DenseVectorData.NULL;
        
        //normal dv
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toListData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toSetData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toDenseVectorData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toSparseVectorData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, dNull).size(), 3);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.ONE).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.valueOf(2d)).size(), 3);
        assertEquals(XTypeDataUtils.deduct(d1, IntData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.deduct(d1, IntData.ONE).size(), 1);
        
        //null dv
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toListData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toSetData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toDenseVectorData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toSparseVectorData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, dNull).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.ZERO).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.ONE).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.valueOf(2d)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, IntData.ZERO).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, IntData.ONE).isNull(), true);
    }

    @Test
    public void testSparseVectorDeduct() throws EngineException {
        final SparseVectorData d1 = new SparseVectorData(new double[] {0d, 1d, 1d});
        final SparseVectorData d2 = new SparseVectorData(new double[] {0d, 1d, 2d});
        final SparseVectorData dNull = SparseVectorData.NULL;
        
        //normal dv
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toListData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toSetData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toDenseVectorData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toSparseVectorData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, dNull).size(), 3);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.ONE).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.valueOf(2d)).size(), 3);
        assertEquals(XTypeDataUtils.deduct(d1, IntData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.deduct(d1, IntData.ONE).size(), 1);
        
        //null dv
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toListData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toSetData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toDenseVectorData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toSparseVectorData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, dNull).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.ZERO).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.ONE).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.valueOf(2d)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, IntData.ZERO).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, IntData.ONE).isNull(), true);
    }
    
    @Test
    public void testListDeduct() throws EngineException {
        final ListData d1 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {
                DoubleData.ZERO, DoubleData.ONE, DoubleData.ONE, DoubleData.NULL, StringData.valueOf("1")})));
        final ListData d2 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {
                DoubleData.ZERO, DoubleData.ONE, DoubleData.valueOf(2d)})));

        final ListData dNull = ListData.NULL;
        
        //normal dv
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toListData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toSetData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toDenseVectorData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toSparseVectorData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, dNull).size(), 5);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.ZERO).size(), 4);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.ONE).size(), 2);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.valueOf(2d)).size(), 5);
        assertEquals(XTypeDataUtils.deduct(d1, IntData.ZERO).size(), 4);
        assertEquals(XTypeDataUtils.deduct(d1, IntData.ONE).size(), 2);
        
        //null dv
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toListData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toSetData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toDenseVectorData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toSparseVectorData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, dNull).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.ZERO).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.ONE).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.valueOf(2d)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, IntData.ZERO).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, IntData.ONE).isNull(), true);
    }
    
    @Test
    public void testSetDeduct() throws EngineException {
        final SetData d1 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {DoubleData.ZERO, DoubleData.ONE, DoubleData.ONE, StringData.NULL, StringData.valueOf("1")})));
        final SetData d2 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {DoubleData.ZERO, DoubleData.ONE, DoubleData.valueOf(2d)})));
        final SetData dNull = SetData.NULL;
        
        //normal dv
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toListData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toSetData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toDenseVectorData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toSparseVectorData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, dNull).size(), 4);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.ONE).size(), 2);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.valueOf(2d)).size(), 3);
        assertEquals(XTypeDataUtils.deduct(d1, IntData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.deduct(d1, IntData.ONE).size(), 2);
        
        //null dv
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toListData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toSetData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toDenseVectorData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toSparseVectorData(d2)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, dNull).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.ZERO).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.ONE).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.valueOf(2d)).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, IntData.ZERO).isNull(), true);
        assertEquals(XTypeDataUtils.deduct(dNull, IntData.ONE).isNull(), true);
    }
    
    @Test
    public void testPrimitiveDeduct() throws EngineException {
        final LongData d1 = LongData.ONE;
        final SetData d2 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {DoubleData.ZERO, DoubleData.ONE, DoubleData.valueOf(2d)})));
        final LongData dNull = LongData.NULL;
        
        //normal dv
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toListData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toSetData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toDenseVectorData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, DataConv.toSparseVectorData(d2)).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, dNull).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.ONE).size(), 0);
        assertEquals(XTypeDataUtils.deduct(d1, DoubleData.valueOf(2d)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, IntData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.deduct(d1, IntData.ONE).size(), 0);
        
        //null dv
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toListData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toSetData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toDenseVectorData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(dNull, DataConv.toSparseVectorData(d2)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(dNull, dNull).size(), 0);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.ONE).size(), 1);
        assertEquals(XTypeDataUtils.deduct(dNull, DoubleData.valueOf(2d)).size(), 1);
        assertEquals(XTypeDataUtils.deduct(dNull, IntData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.deduct(dNull, IntData.ONE).size(), 1);
    }

    @Test
    public void testDenseVectorAppend() throws EngineException {
        final DenseVectorData d1 = new DenseVectorData(new double [] {0d, 1d, 1D});
        final DenseVectorData d2 = new DenseVectorData(new double [] {0d, 1d, 2d});
        final DenseVectorData dNull = DenseVectorData.NULL;
        
        //normal dv
        assertEquals(XTypeDataUtils.append(d1, DataConv.toListData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toSetData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toDenseVectorData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toSparseVectorData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, dNull).size(), 3);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.ZERO).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.ONE).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.valueOf(2d)).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, IntData.ZERO).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, IntData.ONE).size(), 4);
        
        //null dv
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toListData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toSetData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toDenseVectorData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toSparseVectorData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, dNull).size(), 0);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.ONE).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.valueOf(2d)).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, IntData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, IntData.ONE).size(), 1);
    }

    @Test
    public void testSparseVectorAppend() throws EngineException {
        final DenseVectorData d1 = new DenseVectorData(new double [] {0d, 1d, 1D});
        final DenseVectorData d2 = new DenseVectorData(new double [] {0d, 1d, 2d});
        final DenseVectorData dNull = DenseVectorData.NULL;
        
        //normal dv
        assertEquals(XTypeDataUtils.append(d1, DataConv.toListData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toSetData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toDenseVectorData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toSparseVectorData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, dNull).size(), 3);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.ZERO).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.ONE).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.valueOf(2d)).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, IntData.ZERO).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, IntData.ONE).size(), 4);
        
        //null dv
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toListData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toSetData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toDenseVectorData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toSparseVectorData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, dNull).size(), 0);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.ONE).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.valueOf(2d)).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, IntData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, IntData.ONE).size(), 1);
    }

    @Test
    public void testListAppend() throws EngineException {
        final ListData d1 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {
                DoubleData.ZERO, DoubleData.ONE, DoubleData.ONE})));
        final ListData d2 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {
                DoubleData.ZERO, DoubleData.ONE, DoubleData.valueOf(2d)})));
        final ListData dNull = ListData.NULL;
        
        //normal dv
        assertEquals(XTypeDataUtils.append(d1, DataConv.toListData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toSetData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toDenseVectorData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toSparseVectorData(d2)).size(), 6);
        assertEquals(XTypeDataUtils.append(d1, dNull).size(), 3);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.NULL).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.ZERO).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.ONE).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.valueOf(2d)).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, IntData.ZERO).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, IntData.ONE).size(), 4);
        
        //null dv
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toListData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toSetData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toDenseVectorData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toSparseVectorData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, dNull).size(), 0);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.NULL).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.ONE).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.valueOf(2d)).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, IntData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, IntData.ONE).size(), 1);
    }
    
    @Test
    public void testSetAppend() throws EngineException {
        final SetData d1 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {DoubleData.ZERO, DoubleData.ONE, DoubleData.ONE})));
        final SetData d2 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {DoubleData.ZERO, DoubleData.ONE, DoubleData.valueOf(2d)})));
        final SetData dNull = SetData.NULL;
        
        //normal dv
        assertEquals(XTypeDataUtils.append(d1, DataConv.toListData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toSetData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toDenseVectorData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toSparseVectorData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(d1, dNull).size(), 2);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.NULL).size(), 3);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.ONE).size(), 2);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.valueOf(2d)).size(), 3);
        assertEquals(XTypeDataUtils.append(d1, IntData.ZERO).size(), 3);
        assertEquals(XTypeDataUtils.append(d1, IntData.ONE).size(), 3); //set requires type to be equal
        
        //null dv
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toListData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toSetData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toDenseVectorData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toSparseVectorData(d2)).size(), 3);
        assertEquals(XTypeDataUtils.append(dNull, dNull).size(), 0);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.NULL).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.ONE).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.valueOf(2d)).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, IntData.ZERO).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, IntData.ONE).size(), 1);
    }

    @Test
    public void testPrimitiveAppend() throws EngineException {
        final LongData d1 = LongData.ONE;
        final ListData d2 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {
                DoubleData.ZERO, DoubleData.ONE, DoubleData.valueOf(2d)})));

        final LongData dNull = LongData.NULL;
        
        //normal dv
        assertEquals(XTypeDataUtils.append(d1, DataConv.toListData(d2)).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toSetData(d2)).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toDenseVectorData(d2)).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, DataConv.toSparseVectorData(d2)).size(), 4);
        assertEquals(XTypeDataUtils.append(d1, ListData.NULL).size(), 1);
        assertEquals(XTypeDataUtils.append(d1, dNull).size(), 2);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.NULL).size(), 2);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.ONE).size(), 2);
        assertEquals(XTypeDataUtils.append(d1, DoubleData.valueOf(2d)).size(), 2);
        assertEquals(XTypeDataUtils.append(d1, IntData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.append(d1, IntData.ONE).size(), 2);
        
        //null dv
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toListData(d2)).size(), 4);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toSetData(d2)).size(), 4);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toDenseVectorData(d2)).size(), 4);
        assertEquals(XTypeDataUtils.append(dNull, DataConv.toSparseVectorData(d2)).size(), 4);
        assertEquals(XTypeDataUtils.append(dNull, ListData.NULL).size(), 1);
        assertEquals(XTypeDataUtils.append(dNull, dNull).size(), 2);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.NULL).size(), 2);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.ONE).size(), 2);
        assertEquals(XTypeDataUtils.append(dNull, DoubleData.valueOf(2d)).size(), 2);
        assertEquals(XTypeDataUtils.append(dNull, IntData.ZERO).size(), 2);
        assertEquals(XTypeDataUtils.append(dNull, IntData.ONE).size(), 2);
    }
}
