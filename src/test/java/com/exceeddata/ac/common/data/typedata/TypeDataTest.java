package com.exceeddata.ac.common.data.typedata;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.junit.Test;

import com.exceeddata.ac.common.exception.EngineException;

public class TypeDataTest {
    final ListData list0 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(0d), new DoubleData(1d), new DoubleData(2d), new DoubleData(3d), new DoubleData()})));
    final ListData list1 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(0d), new DoubleData(1d), new DoubleData(2d), new DoubleData(3d)})));

    final ListData list2 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(0d), new DoubleData(1d), new DoubleData(2d)})));
    final ListData list3 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(1d), new DoubleData(2d), new DoubleData(3d)})));
    final ListData list4 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(5d), new DoubleData(2d)})));
    final ListData list5 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(2d), new DoubleData(2d)})));
    final ListData list6 = new ListData();
    final ListData list7 = new ListData(0);
    
    final SetData set1 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(0d), new DoubleData(1d), new DoubleData(2d), new DoubleData(3d)})));
    final SetData set2 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(0d), new DoubleData(1d), new DoubleData(2d)})));
    final SetData set3 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(1d), new DoubleData(2d), new DoubleData(3d)})));

    final DenseVectorData dense1 = new DenseVectorData(new double [] {0d, 1d, 2d, 3D});
    final DenseVectorData dense2 = new DenseVectorData(new double [] {0d, 1d, 2d});
    final DenseVectorData dense3 = new DenseVectorData(new double [] {1d, 2d, 3D});

    final SparseVectorData sparse1 = new SparseVectorData(new double[] {0d, 1d, 2d, 3d});
    final SparseVectorData sparse2 = new SparseVectorData(new double[] {0d, 0d, 3d, 3d});
    final SparseVectorData sparse3 = new SparseVectorData(new double[] {0d, 0d, 3d, 3d});
    final SparseVectorData sparse4 = new SparseVectorData(21, new int[] {20}, new double[] {3d});
    final SparseVectorData sparse5 = new SparseVectorData(21, new int[] {20, 30}, new double[] {3d, 3d});
    /*
     * List is a non-ordered non-unique list of items (equality check)
     * Set is a non-sequential unique list of items (hash check)
     * Dense Vector is a ordered non-unique list of items
     * Sparse Vector is a ordered non-unique list of items but with sparse data structure
     */
    

    @Test
    public void testItemNulls() {
        final ListData list1 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new DoubleData(0d), NullData.INSTANCE})));
        final ListData list2 = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {new StringData(""), NullData.INSTANCE})));
        final SetData set = new SetData(new LinkedHashSet<>(new ArrayList<>(Arrays.asList( new TypeData[] {IntData.NULL, new DoubleData(0d)}))));
        
        final LinkedHashMap<TypeData, TypeData> m = new LinkedHashMap<>();
        m.put(new StringData("a"), IntData.ONE);
        m.put(new StringData("b"), IntData.NULL);
        m.put(new StringData("c"), list1);
        final MapData map = new MapData(m);
        
        assertEquals(list1.toString(), "[0,null]");
        assertEquals(list2.toString(), "[\"\",null]");
        assertEquals(set.toString(), "[null,0]");
        assertEquals(map.toString(), "{\"a\":1,\"b\":null,\"c\":[0,null]}");
    }
    
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testListDataEquals() {
        assertEquals(true, list1.equals(list1));
        assertEquals(false, list1.equals(list2));
        assertEquals(false, list2.equals(list1));
        assertEquals(false, list1.equals(set1));
        assertEquals(false, list1.equals(dense1));
        assertEquals(false, list1.equals(sparse1));
        assertEquals(false, list2.equals(sparse5));
        assertEquals(true, ListData.NULL.equals(ListData.NULL));
        assertEquals(true, ListData.NULL.equals(SetData.NULL));
        assertEquals(true, ListData.NULL.equals(DenseVectorData.NULL));
        assertEquals(true, ListData.NULL.equals(SparseVectorData.NULL));
        assertEquals(true, ListData.NULL.equals(MapData.NULL));
        assertEquals(true, ListData.NULL.equals(StringData.NULL));
    }
    
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testSetDataEquals() {
        assertEquals(true, set1.equals(set1));
        assertEquals(false, set1.equals(set2));
        assertEquals(false, set2.equals(set1));
        assertEquals(false, set1.equals(list1));
        assertEquals(false, set1.equals(dense1));
        assertEquals(false, set1.equals(sparse1));
        assertEquals(false, set2.equals(dense1));
        assertEquals(false, set2.equals(sparse1));
        assertEquals(true, SetData.NULL.equals(ListData.NULL));
        assertEquals(true, SetData.NULL.equals(SetData.NULL));
        assertEquals(true, SetData.NULL.equals(DenseVectorData.NULL));
        assertEquals(true, SetData.NULL.equals(SparseVectorData.NULL));
        assertEquals(true, SetData.NULL.equals(MapData.NULL));
        assertEquals(true, SetData.NULL.equals(StringData.NULL));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testDenseVectorDataEquals() {
        assertEquals(true, dense1.equals(dense1));
        assertEquals(true, dense1.compareTo(sparse1) == 0);
        assertEquals(false, dense1.equals(list1));
        assertEquals(true, dense1.compareTo(list1) == 0);
        assertEquals(false, dense2.equals(list2));
        assertEquals(true, dense2.compareTo(list2) == 0);
        assertEquals(false, dense1.equals(dense2));
        assertEquals(false, dense1.compareTo(dense2) == 0);
        assertEquals(false, dense2.equals(dense1));
        assertEquals(false, dense2.compareTo(dense1) == 0);
        assertEquals(false, dense1.equals(set1));
        assertEquals(true, dense1.compareTo(set1) == 0);
        assertEquals(false, dense1.equals(sparse2));
        assertEquals(false, dense1.compareTo(sparse2) == 0);
        assertEquals(true, DenseVectorData.NULL.equals(ListData.NULL));
        assertEquals(true, DenseVectorData.NULL.equals(SetData.NULL));
        assertEquals(true, DenseVectorData.NULL.equals(DenseVectorData.NULL));
        assertEquals(true, DenseVectorData.NULL.equals(SparseVectorData.NULL));
        assertEquals(true, DenseVectorData.NULL.equals(MapData.NULL));
        assertEquals(true, DenseVectorData.NULL.equals(StringData.NULL));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testSparseVectorDataEquals() {
        assertEquals(false, sparse1.equals(dense1));
        assertEquals(true, sparse1.compareTo(dense1) == 0);
        assertEquals(true, sparse1.equals(sparse1));
        assertEquals(true, sparse1.compareTo(sparse1) == 0);
        assertEquals(false, sparse1.equals(list1));
        assertEquals(true, sparse1.compareTo(list1) == 0);
        assertEquals(false, sparse2.equals(list2));
        assertEquals(false, sparse2.compareTo(list2) == 0);
        assertEquals(false, sparse1.equals(dense2));
        assertEquals(false, sparse1.compareTo(dense2) == 0);
        assertEquals(false, sparse2.equals(dense1));
        assertEquals(false, sparse2.compareTo(dense1) == 0);
        assertEquals(false, sparse1.equals(set1));
        assertEquals(true, sparse1.compareTo(set1) == 0);
        assertEquals(false, sparse1.equals(sparse2));
        assertEquals(false, sparse1.compareTo(sparse2) == 0);
        assertEquals(true, SparseVectorData.NULL.equals(ListData.NULL));
        assertEquals(true, SparseVectorData.NULL.equals(SetData.NULL));
        assertEquals(true, SparseVectorData.NULL.equals(DenseVectorData.NULL));
        assertEquals(true, SparseVectorData.NULL.equals(SparseVectorData.NULL));
        assertEquals(true, SparseVectorData.NULL.equals(MapData.NULL));
        assertEquals(true, SparseVectorData.NULL.equals(StringData.NULL));
    }
    
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testMapDataEquals() {
        final LinkedHashMap<TypeData, TypeData> md1 = new LinkedHashMap<>();
        final LinkedHashMap<TypeData, TypeData> md2 = new LinkedHashMap<>();
        final LinkedHashMap<TypeData, TypeData> md3 = new LinkedHashMap<>();
        final LinkedHashMap<TypeData, TypeData> md4 = new LinkedHashMap<>();
        md1.put(IntData.ONE, IntData.ONE);
        md2.put(IntData.ZERO, IntData.ONE);
        md3.put(IntData.ONE, IntData.ZERO);
        md4.put(IntData.ONE, IntData.ONE);
        md4.put(IntData.ZERO, IntData.ONE);
        
        final MapData map1 = new MapData(md1);
        final MapData map2 = new MapData(md2);
        final MapData map3 = new MapData(md3);
        final MapData map4 = new MapData(md4);
        assertEquals(true, map1.equals(map1));
        assertEquals(false, map1.equals(map2));
        assertEquals(false, map1.equals(map3));
        assertEquals(false, map2.equals(map1));
        assertEquals(false, map3.equals(map1));
        assertEquals(false, map4.equals(map1));
        assertEquals(false, map1.equals(map4));
        assertEquals(true, map4.equals(map4));
        
        assertEquals(false, map1.equals(set1));
        assertEquals(false, map1.equals(set2));
        assertEquals(false, map1.equals(set1));
        assertEquals(false, map1.equals(list1));
        assertEquals(false, map1.equals(dense1));
        assertEquals(false, map1.equals(sparse1));
        assertEquals(false, map1.equals(dense1));
        assertEquals(false, map1.equals(sparse1));
        assertEquals(true, MapData.NULL.equals(ListData.NULL));
        assertEquals(true, MapData.NULL.equals(SetData.NULL));
        assertEquals(true, MapData.NULL.equals(DenseVectorData.NULL));
        assertEquals(true, MapData.NULL.equals(SparseVectorData.NULL));
        assertEquals(true, MapData.NULL.equals(MapData.NULL));
        assertEquals(true, MapData.NULL.equals(StringData.NULL));
    }
    
    @Test
    public void testListDataContains() throws EngineException {
        assertEquals(true, DataContain.contains(list0, NullData.INSTANCE));
        assertEquals(true, DataContain.contains(list0, IntData.NULL));
        
        assertEquals(true, DataContain.contains(list1, new DoubleData(1d)));
        assertEquals(false, DataContain.contains(list1, IntData.valueOf(1)));
        assertEquals(false, DataContain.contains(list1, NullData.INSTANCE));
        
        assertEquals(true, DataContain.contains(list1, list2));
        assertEquals(true, DataContain.contains(list1, list3));
        assertEquals(false, DataContain.contains(list1, list4));
        assertEquals(true, DataContain.contains(list1, list5));
        assertEquals(false, DataContain.contains(list1, list6));
        assertEquals(false, DataContain.contains(list1, list7));
        
        assertEquals(true, DataContain.contains(list1, set1));
        assertEquals(true, DataContain.contains(list1, set2));
        assertEquals(true, DataContain.contains(list1, set3));
        
        assertEquals(true, DataContain.contains(list1, dense1));
        assertEquals(true, DataContain.contains(list1, dense2));
        assertEquals(true, DataContain.contains(list1, dense3));
        
        assertEquals(true, DataContain.contains(list1, sparse1));
        assertEquals(true, DataContain.contains(list1, sparse2));
        assertEquals(true, DataContain.contains(list1, sparse3));
        
    }
    
    @Test
    public void testListDataCompareTo() {
        assertEquals(1, list0.compareTo(NullData.INSTANCE));
        assertEquals(1, list0.compareTo(IntData.NULL));
        
        assertEquals(0, list1.compareTo(list1));
        assertEquals(1, list1.compareTo(list2));
        assertEquals(-1, list2.compareTo(list1));
        assertEquals(-1, list1.compareTo(list4));
        assertEquals(1, list4.compareTo(list1));
        
        assertEquals(-1, list1.compareTo(set1));
        assertEquals(1, list1.compareTo(set2));
        assertEquals(1, list3.compareTo(set2));
        
        assertEquals(1, list1.compareTo(dense1));
        assertEquals(1, list1.compareTo(dense2));
        assertEquals(1, list3.compareTo(dense2));
        
        assertEquals(0, list1.compareTo(sparse1));
        assertEquals(1, list1.compareTo(sparse2));
        assertEquals(1, list3.compareTo(sparse2));
    }

    @Test
    public void testSetDataContains() throws EngineException {
        assertEquals(true, DataContain.contains(set1, new DoubleData(1d)));
        assertEquals(false, DataContain.contains(set1, IntData.ONE));
        assertEquals(false, DataContain.contains(set1, NullData.INSTANCE));
        
        assertEquals(true, DataContain.contains(set1, list2));
        assertEquals(true, DataContain.contains(set1, list3));
        assertEquals(false, DataContain.contains(set1, list4));
        assertEquals(true, DataContain.contains(set1, list5));
        assertEquals(false, DataContain.contains(set1, list6));
        assertEquals(false, DataContain.contains(set1, list7));
        
        assertEquals(true, DataContain.contains(set1, set1));
        assertEquals(true, DataContain.contains(set1, set2));
        assertEquals(true, DataContain.contains(set1, set3));
        
        assertEquals(true, DataContain.contains(set1, dense1));
        assertEquals(true, DataContain.contains(set1, dense2));
        assertEquals(true, DataContain.contains(set1, dense3));
        
        assertEquals(true, DataContain.contains(set1, sparse1));
        assertEquals(true, DataContain.contains(set1, sparse2));
        assertEquals(true, DataContain.contains(set1, sparse3));
    }
    
    @Test
    public void testSetDataCompareTo() {
        assertEquals(1, set1.compareTo(list1));
        assertEquals(1, set1.compareTo(list2));
        assertEquals(-1, set2.compareTo(list1));
        assertEquals(-1, set1.compareTo(list4));
        assertEquals(-1, set2.compareTo(list1));
        
        assertEquals(true, set1.equals(set1));
        assertEquals(false, set1.equals(set2));
        assertEquals(false, set3.equals(set2));
        
        assertEquals(1, set1.compareTo(dense1));
        assertEquals(1, set1.compareTo(dense2));
        assertEquals(1, set3.compareTo(dense2));
        
        assertEquals(0, set1.compareTo(sparse1));
        assertEquals(1, set1.compareTo(sparse2));
        assertEquals(1, set3.compareTo(sparse2));
        
        final SetData outofOrder1 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(3d), new DoubleData(2d), new DoubleData(0d), new DoubleData(1d)})));
        final SetData outofOrder2 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(2d), new DoubleData(0d), new DoubleData(1d)})));
        final SetData outofOrder3 = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {new DoubleData(3d), new DoubleData(1d), new DoubleData(2d)})));;

        assertEquals(true, set1.equals(outofOrder1));
        assertEquals(true, set2.equals(outofOrder2));
        assertEquals(true, set3.equals(outofOrder3));
        assertEquals(false, set1.equals(outofOrder2));
        assertEquals(false, outofOrder2.equals(set3));

    }
    
    @Test
    public void testDenseVectorDataCompareTo() {
        assertEquals(0, dense1.compareTo(list1));
        assertEquals(1, dense1.compareTo(list2));
        assertEquals(-1, dense2.compareTo(list1));
        assertEquals(-1, dense1.compareTo(list4));
        assertEquals(1, dense3.compareTo(list1));
        
        assertEquals(0, dense1.compareTo(set1));
        assertEquals(1, dense1.compareTo(set2));
        assertEquals(1, dense3.compareTo(set2));
        
        assertEquals(0, dense1.compareTo(dense1));
        assertEquals(1, dense1.compareTo(dense2));
        assertEquals(1, dense3.compareTo(dense2));
        
        assertEquals(0, dense1.compareTo(sparse1));
        assertEquals(1, dense1.compareTo(sparse2));
        assertEquals(1, dense3.compareTo(sparse2));
    }
    
    @Test
    public void testSparseVectorDataCompareTo() {
        assertEquals(0, sparse1.compareTo(list1));
        assertEquals(1, sparse1.compareTo(list2));
        assertEquals(-1, sparse2.compareTo(list1));
        assertEquals(-1, sparse1.compareTo(list4));
        assertEquals(-1, sparse3.compareTo(list1));
        
        assertEquals(0, sparse1.compareTo(set1));
        assertEquals(1, sparse1.compareTo(set2));
        assertEquals(-1, sparse3.compareTo(set2));
        
        assertEquals(0, sparse1.compareTo(dense1));
        assertEquals(1, sparse1.compareTo(dense2));
        assertEquals(-1, sparse3.compareTo(dense2));
        
        assertEquals(0, sparse1.compareTo(sparse1));
        assertEquals(1, sparse1.compareTo(sparse2));
        assertEquals(0, sparse3.compareTo(sparse2));
        assertEquals(1, sparse3.compareTo(sparse4));
        assertEquals(1, sparse3.compareTo(sparse5));
        assertEquals(-1, sparse4.compareTo(sparse5));
    }
}
