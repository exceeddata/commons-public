package com.exceeddata.ac.common.data.typedata;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.junit.Test;

import com.exceeddata.ac.common.exception.EngineException;

public class ComplexTypeData {
    
    @Test
    public void testComplexData() throws EngineException {
        ComplexData cd = new ComplexData(4d, -3d);
        ComplexData cd2 = new ComplexData(-4d, 3d);
        ComplexData cd3 = new ComplexData("4-3i");
        ComplexData cd4 = new ComplexData("4.0-3.0i");
        assertEquals(cd.abs().toString(), "5");
        assertEquals(cd.negate(), cd2);
        assertEquals(cd.toString(), "4-3i");
        assertEquals(cd2.toString(), "-4+3i");
        assertEquals(cd3.toString(), "4-3i");
        assertEquals(cd4.toString(), "4-3i");
        assertEquals(DataConv.toStringData(cd).toString(), "4-3i");
        assertEquals(DataConv.toIntData(cd).toInt().intValue(), 4);
        assertEquals(DataConv.toDoubleData(cd).toString(), "4");
        assertEquals(DataConv.toDoubleData(cd3).toString(), "4");
        
        ComplexData cd5 = new ComplexData("4");
        assertEquals(DataConv.toDoubleData(cd5).toString(), "4");
        ComplexData cd6 = new ComplexData("-3i");
        assertEquals(DataConv.toDoubleData(cd6).toString(), "0");
        
        assertEquals(cd.equals(cd4), true);
        assertEquals(cd3.equals(cd4), true);
        assertEquals(cd.compareTo(cd4), 0);
        assertEquals(cd3.compareTo(cd4), 0);
        assertEquals(cd3.compareTo(cd5), -1);
        assertEquals(cd5.compareTo(cd3), 1);
        assertEquals(cd6.compareTo(cd3), -1);
    }
    
    @Test
    public void testMapDataContains() {
        LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        map.put(IntData.ONE, IntData.ONE);
        map.put(StringData.valueOf("1"), IntData.TWO);
        MapData mt = new MapData(map);
        
        assertEquals(mt.get(StringData.valueOf("1")), IntData.TWO);
        assertEquals(mt.get(IntData.valueOf(1)), IntData.ONE);
        
    }
    
    @Test
    public void testListData() {
        ListData ints = new ListData(new ArrayList<>(Arrays.asList(new TypeData[] {new IntData(1), new IntData(2), new IntData(3)})));        
        ListData list1 = new ListData(ints);
        assertEquals(list1.get(1), IntData.TWO);
        
        ListData list2 = new ListData(ints);
        assertEquals(list2.get(1), IntData.TWO);
        
        ListData list3 = list2.copy(); 
        assertEquals(list3.get(1), IntData.TWO);
    }
    
    @Test
    public void testSetData() throws EngineException {
        ListData ints = new ListData(new ArrayList<>(Arrays.asList(new TypeData[] {new IntData(1), new IntData(2), new IntData(3)})));        
        final SetData sets = new SetData(new LinkedHashSet<>(Arrays.asList(new TypeData[] {IntData.valueOf(1), IntData.valueOf(2), IntData.valueOf(3)})));
        SetData sets1 = new SetData(DataConv.toSetData(ints));
        assertEquals(DataContain.contains(sets1, IntData.TWO), true);

        SetData sets2 = new SetData(sets);
        assertEquals(DataContain.contains(sets2, IntData.TWO), true);
        
        SetData sets3 = sets2.copy(); 
        assertEquals(DataContain.contains(sets3, IntData.TWO), true);
        
        SetData sets4 = new SetData(DataConv.toSetData(ints));
        assertEquals(DataContain.contains(sets4, IntData.TWO), true);

        SetData sets5 = new SetData(sets);
        assertEquals(DataContain.contains(sets5, IntData.TWO), true);
    }
    
    @Test
    public void testMapData() {
        LinkedHashMap<TypeData, TypeData> ints = new LinkedHashMap<TypeData, TypeData>();
        ints.put(IntData.valueOf(1), IntData.valueOf(1));
        ints.put(IntData.valueOf(2), IntData.valueOf(2));
        ints.put(IntData.valueOf(3), IntData.valueOf(3));
        
        MapData map1 = new MapData(ints);
        assertEquals(map1.get(IntData.TWO), IntData.TWO);
        
        MapData map2 = new MapData(ints);
        assertEquals(map2.get(IntData.TWO), IntData.TWO);
        
        MapData map3 = map2.copy();
        assertEquals(map3.get(IntData.TWO), IntData.TWO);
    }
     
    @Test
    public void testToMapData() throws EngineException {
        ListData list = new ListData(new ArrayList<>(Arrays.asList(new TypeData[] {DoubleData.ONE, DoubleData.valueOf(2d)})));        
        LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        map.put(StringData.valueOf("c1"), StringData.valueOf("1"));
        map.put(StringData.valueOf("c2"), DoubleData.valueOf(10d));
        map.put(StringData.valueOf("c3"), list);
        MapData mt = new MapData(map);
        
        assertEquals(list.toString(), DataConv.toListData(DataConv.toStringData(list)).toString());
        assertEquals(mt.toString(), DataConv.toMapData(DataConv.toStringData(mt)).toString());
    }
}
