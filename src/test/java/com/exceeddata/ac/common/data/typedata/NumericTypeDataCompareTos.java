package com.exceeddata.ac.common.data.typedata;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.junit.Test;

import com.exceeddata.ac.common.exception.EngineException;

public class NumericTypeDataCompareTos {
    
    @Test
    public void testBigDecimalCompareTo() throws EngineException, ParseException {
        //int
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(IntData.valueOf(1)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(IntData.valueOf(0)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(IntData.valueOf(2)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.0")).compareTo(IntData.valueOf(1)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1.1")).compareTo(IntData.valueOf(1)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(IntData.valueOf(0)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("0.1")).compareTo(IntData.valueOf(0)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal(1838382828292929l)).compareTo(IntData.valueOf((int) 1838382828292929l)) == 0);
        
        //long
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(LongData.valueOf(2l)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.0")).compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1.1")).compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("0.1")).compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal(1838382828292929l)).compareTo(LongData.valueOf(1838382828292929l)) == 0);
        
        //float
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(FloatData.valueOf(1f)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(FloatData.valueOf(2f)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.0")).compareTo(FloatData.valueOf(1f)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1.1")).compareTo(FloatData.valueOf(1f)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(FloatData.valueOf(0.00f)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.1")).compareTo(FloatData.valueOf(0.10f)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("0.1")).compareTo(FloatData.valueOf(0.11f)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal(1838382828292929.0d)).compareTo(FloatData.valueOf((float) 1838382828292929d)) == 0);

        //double
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(DoubleData.valueOf(1d)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(DoubleData.valueOf(2d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.0")).compareTo(DoubleData.valueOf(1d)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1.1")).compareTo(DoubleData.valueOf(1d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(DoubleData.valueOf(0.00d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.1")).compareTo(DoubleData.valueOf(0.10d)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("0.1")).compareTo(DoubleData.valueOf(0.11d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1838382828292929.0")).compareTo(DoubleData.valueOf(1838382828292929d)) == 0);
        
        //numeric
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(NumericData.valueOf(1d)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(NumericData.valueOf(2d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.0")).compareTo(NumericData.valueOf(1d)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1.1")).compareTo(NumericData.valueOf(1d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(NumericData.valueOf(0.00d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.1")).compareTo(NumericData.valueOf(0.10d)) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("0.1")).compareTo(NumericData.valueOf(0.11d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("92929.0")).compareTo(NumericData.valueOf(92929d)) == 0);
        
        //complex
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(ComplexData.valueOf(1d)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(ComplexData.valueOf(1d, -1d, false)) > 0);
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(ComplexData.valueOf(-1d, -1d, false)) > 0);
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(ComplexData.valueOf(2d, -1d, false)) < 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.835")).compareTo(ComplexData.valueOf(1.835d, -1.37d, false)) > 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.835")).compareTo(ComplexData.valueOf(1.835d, 0.000d, false)) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.835")).compareTo(ComplexData.valueOf(1.835d, 1.37d, false)) < 0);
        
        
        //BigDecimal
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(DecimalData.valueOf(new BigDecimal("1"))) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(DecimalData.valueOf(new BigDecimal("2.0"))) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.0")).compareTo(DecimalData.valueOf(new BigDecimal("1.0000"))) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1.1")).compareTo(DecimalData.valueOf(new BigDecimal("1.000000"))) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(DecimalData.valueOf(new BigDecimal("0.00000"))) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.1")).compareTo(DecimalData.valueOf(new BigDecimal("0.10"))) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("0.1")).compareTo(DecimalData.valueOf(new BigDecimal("0.11"))) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1838382828292929.0")).compareTo(DecimalData.valueOf(new BigDecimal("1838382828292929"))) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1292929.038000005")).compareTo(DecimalData.valueOf(new BigDecimal("1292929.038000005"))) == 0);
        
        //Boolean
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(BooleanData.FALSE) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("0")).compareTo(BooleanData.TRUE) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.0")).compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1.1")).compareTo(BooleanData.TRUE) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(BooleanData.FALSE) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("0.1")).compareTo(BooleanData.FALSE) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("11")).compareTo(BooleanData.TRUE) == 0);
        
        //String
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(StringData.valueOf("1")) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(StringData.valueOf("0")) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(StringData.valueOf("2")) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("1.0")).compareTo(StringData.valueOf("1")) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1.1")).compareTo(StringData.valueOf("1.11")) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(StringData.valueOf("0")) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.1")).compareTo(StringData.valueOf("0.10")) == 0);
        assertEquals(true, new DecimalData(new BigDecimal(1838382828292929l)).compareTo(StringData.valueOf("1838382828292929")) == 0);
        assertEquals(false, new DecimalData(new BigDecimal(1838382828292929l)).compareTo(StringData.valueOf("13bar")) == 0);
        
        //Bytes, string bytes comaprison
        assertEquals(true, new DecimalData(new BigDecimal("1")).compareTo(BinaryData.valueOf("1".getBytes())) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(BinaryData.valueOf("0".getBytes())) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(BinaryData.valueOf("2".getBytes())) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1.0")).compareTo(BinaryData.valueOf("1".getBytes())) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1.1")).compareTo(BinaryData.valueOf("1.11".getBytes())) == 0);
        assertEquals(true, new DecimalData(new BigDecimal("0.0")).compareTo(BinaryData.valueOf("0.0".getBytes())) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("0.1")).compareTo(BinaryData.valueOf("0.10".getBytes())) == 0);
        assertEquals(true, new DecimalData(new BigDecimal(1838382828292929l)).compareTo(BinaryData.valueOf("1838382828292929".getBytes())) == 0);
        
        //Null
        assertEquals(true, DecimalData.NULL.compareTo(NullData.INSTANCE) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(IntData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(LongData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(FloatData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(DoubleData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(NumericData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(BooleanData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(BinaryData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(StringData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(DateData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(TimeData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(TimestampData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(ListData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(MapData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(SetData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(true, DecimalData.NULL.compareTo(SparseVectorData.NULL) == 0);
        
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(NullData.INSTANCE) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(IntData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(LongData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(FloatData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(DoubleData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(NumericData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(BooleanData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(BinaryData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(StringData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(DateData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(TimeData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(TimestampData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(ListData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(MapData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(SetData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(DenseVectorData.NULL) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(SparseVectorData.NULL) == 0);
        
        //Complex (List, Set, DenseVector, SparseVector
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {DecimalData.valueOf(new BigDecimal("1"))})))
                ) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {DecimalData.valueOf(new BigDecimal("1"))})))
                ) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(
                new DenseVectorData(new double[] {1d})
                ) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {DecimalData.valueOf(new BigDecimal("1")), DecimalData.valueOf(new BigDecimal("2"))})))
                ) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {DecimalData.valueOf(new BigDecimal("1")), DecimalData.valueOf(new BigDecimal("2"))})))
                ) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(
                new DenseVectorData(new double[] {1d, 2d})
                ) == 0);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        map.put(DecimalData.valueOf(new BigDecimal("1")), DecimalData.valueOf(new BigDecimal("1")));
        map.put(DecimalData.valueOf(new BigDecimal("2")), DecimalData.valueOf(new BigDecimal("2")));
        
        final MapData md = new MapData(map);
        assertEquals(false, new DecimalData(new BigDecimal("1")).compareTo(md) == 0);
        
    }
    
    @Test
    public void testIntDataCompareTo() throws EngineException, ParseException {
        //int
        assertEquals(true, IntData.ONE.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(false, IntData.ONE.compareTo(IntData.valueOf(0)) == 0);
        assertEquals(false, IntData.ONE.compareTo(IntData.valueOf(2)) == 0);
        assertEquals(false, IntData.ZERO.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(true, IntData.ZERO.compareTo(IntData.valueOf(0)) == 0);
        
        //long
        assertEquals(true, IntData.ONE.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(false, IntData.ONE.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false,IntData.ONE.compareTo(LongData.valueOf(2l)) == 0);
        assertEquals(true, IntData.ZERO.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false,IntData.ZERO.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(false, IntData.valueOf((int) 1838382828292929l).compareTo(LongData.valueOf(1838382828292929l)) == 0);
        
        //float
        assertEquals(true, IntData.ONE.compareTo(FloatData.valueOf(1f)) == 0);
        assertEquals(false, IntData.ONE.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(false, IntData.ONE.compareTo(FloatData.valueOf(2f)) == 0);
        assertEquals(true, IntData.ONE.compareTo(FloatData.valueOf(1.00f)) == 0);
        assertEquals(true, IntData.ZERO.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(true, IntData.ZERO.compareTo(FloatData.valueOf(0.00f)) == 0);

        //double
        assertEquals(true, IntData.ONE.compareTo(DoubleData.valueOf(1d)) == 0);
        assertEquals(false, IntData.ONE.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(false,IntData.ONE.compareTo(DoubleData.valueOf(2d)) == 0);
        assertEquals(false, IntData.ONE.compareTo(DoubleData.valueOf(1.1d)) == 0);
        assertEquals(true, IntData.ZERO.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(true,IntData.ZERO.compareTo(DoubleData.valueOf(0.00d)) == 0);
        assertEquals(false, IntData.ZERO.compareTo(DoubleData.valueOf(0.10d)) == 0);

        //numeric
        assertEquals(true, IntData.ONE.compareTo(NumericData.valueOf(1d)) == 0);
        assertEquals(false, IntData.ONE.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(false,IntData.ONE.compareTo(NumericData.valueOf(2d)) == 0);
        assertEquals(false, IntData.ONE.compareTo(NumericData.valueOf(1.1d)) == 0);
        assertEquals(true, IntData.ZERO.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(true,IntData.ZERO.compareTo(NumericData.valueOf(0.00d)) == 0);
        assertEquals(false, IntData.ZERO.compareTo(NumericData.valueOf(0.10d)) == 0);
        
        //complex
        assertEquals(true, IntData.ONE.compareTo(ComplexData.valueOf(1d)) == 0);
        assertEquals(true, IntData.ONE.compareTo(ComplexData.valueOf(1d, -1d, false)) > 0);
        assertEquals(true, IntData.ONE.compareTo(ComplexData.valueOf(-1d, -1d, false)) > 0);
        assertEquals(true, IntData.ONE.compareTo(ComplexData.valueOf(1d, 1.37d, false)) < 0);
        assertEquals(true, IntData.ONE.compareTo(ComplexData.valueOf(1d, -1.37d, false)) > 0);
        assertEquals(true, IntData.ONE.compareTo(ComplexData.valueOf(1d, 0.000d, false)) == 0);
        assertEquals(true, IntData.ONE.compareTo(ComplexData.valueOf(2, 1.37d, false)) < 0);
        
        //BigDecimal
        assertEquals(true, IntData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("1"))) == 0);
        assertEquals(false,IntData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(false, IntData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("2.0"))) == 0);
        assertEquals(true, IntData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("1.0000"))) == 0);
        assertEquals(true, IntData.ZERO.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(true, IntData.ZERO.compareTo(DecimalData.valueOf(new BigDecimal("0.00000"))) == 0);
        assertEquals(false, IntData.ZERO.compareTo(DecimalData.valueOf(new BigDecimal("0.10"))) == 0);
        
        //Boolean
        assertEquals(true, IntData.ONE.compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, IntData.ONE.compareTo(BooleanData.FALSE) == 0);
        assertEquals(true, IntData.ZERO.compareTo(BooleanData.FALSE) == 0);
        assertEquals(false, IntData.ZERO.compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, IntData.TWO.compareTo(BooleanData.TRUE) == 0);
        
        //String
        assertEquals(true, IntData.ONE.compareTo(StringData.valueOf("1")) == 0);
        assertEquals(false, IntData.ONE.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(false, IntData.ONE.compareTo(StringData.valueOf("2")) == 0);
        assertEquals(true, IntData.ONE.compareTo(StringData.valueOf("1.0")) == 0);
        assertEquals(false, IntData.ONE.compareTo(StringData.valueOf("1.11")) == 0);
        assertEquals(true,IntData.ZERO.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(true, IntData.ZERO.compareTo(StringData.valueOf("0.0000")) == 0);
        assertEquals(false, IntData.valueOf(13).compareTo(StringData.valueOf("13bar")) == 0);
        
        //Bytes, string bytes comparison
        assertEquals(true,IntData.ONE.compareTo(BinaryData.valueOf("1".getBytes())) == 0);
        assertEquals(false, IntData.ONE.compareTo(BinaryData.valueOf("0".getBytes())) == 0);
        assertEquals(false,IntData.ONE.compareTo(BinaryData.valueOf("2".getBytes())) == 0);
        assertEquals(false,IntData.ONE.compareTo(BinaryData.valueOf("1.0".getBytes())) == 0);
        
        //Null
        assertEquals(true, IntData.NULL.compareTo(NullData.INSTANCE) == 0);
        assertEquals(true, IntData.NULL.compareTo(IntData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(LongData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(FloatData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(DoubleData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(NumericData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(BooleanData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(BinaryData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(StringData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(DateData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(TimeData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(TimestampData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(ListData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(MapData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(SetData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(true, IntData.NULL.compareTo(SparseVectorData.NULL) == 0);
        
        assertEquals(false, IntData.ONE.compareTo(NullData.INSTANCE) == 0);
        assertEquals(false, IntData.ONE.compareTo(IntData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(LongData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(FloatData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(DoubleData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(NumericData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(BooleanData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(BinaryData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(StringData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(DateData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(TimeData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(TimestampData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(ListData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(MapData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(SetData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(false, IntData.ONE.compareTo(SparseVectorData.NULL) == 0);
        
        //Complex (List, Set, DenseVector, SparseVector
        assertEquals(false, IntData.ONE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {IntData.ONE})))
                ) == 0);
        assertEquals(false, IntData.ONE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {DecimalData.valueOf(new BigDecimal("1"))})))
                ) == 0);
        assertEquals(false, IntData.ONE.compareTo(
                new DenseVectorData(new double[] {1d})
                ) == 0);
        assertEquals(false, IntData.ONE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        assertEquals(false, IntData.ONE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {IntData.valueOf(1), IntData.valueOf(2)})))
                ) == 0);
        assertEquals(false, IntData.ONE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {IntData.ONE, IntData.valueOf(2)})))
                ) == 0);
        assertEquals(false, IntData.ONE.compareTo(
                new DenseVectorData(new double[] {1d, 2d})
                ) == 0);
        assertEquals(false, IntData.ONE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);

        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        map.put(IntData.ONE, IntData.ONE);
        map.put(IntData.TWO, IntData.TWO);
        
        final MapData md = new MapData(map);
        assertEquals(false, IntData.ONE.compareTo(md) == 0);
    }
    
    @Test
    public void testLongDataCompareTo() throws EngineException, ParseException {
        //int
        assertEquals(true,  LongData.ONE.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(false, LongData.ONE.compareTo(IntData.valueOf(0)) == 0);
        assertEquals(false, LongData.ONE.compareTo(IntData.valueOf(2)) == 0);
        assertEquals(false, LongData.ZERO.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(IntData.valueOf(0)) == 0);
        
        //long
        assertEquals(true,  LongData.ONE.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(false, LongData.ONE.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, LongData.ONE.compareTo(LongData.valueOf(2l)) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, LongData.ZERO.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(true,  LongData.valueOf(1838382828292929l).compareTo(LongData.valueOf(1838382828292929l)) == 0);
        
        //float
        assertEquals(true,  LongData.ONE.compareTo(FloatData.valueOf(1f)) == 0);
        assertEquals(false, LongData.ONE.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(false, LongData.ONE.compareTo(FloatData.valueOf(2f)) == 0);
        assertEquals(true,  LongData.ONE.compareTo(FloatData.valueOf(1.00f)) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(FloatData.valueOf(0.00f)) == 0);

        //double
        assertEquals(true,  LongData.ONE.compareTo(DoubleData.valueOf(1d)) == 0);
        assertEquals(false, LongData.ONE.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(false, LongData.ONE.compareTo(DoubleData.valueOf(2d)) == 0);
        assertEquals(false, LongData.ONE.compareTo(DoubleData.valueOf(1.1d)) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(DoubleData.valueOf(0.00d)) == 0);
        assertEquals(false, LongData.ZERO.compareTo(DoubleData.valueOf(0.10d)) == 0);
        assertEquals(true,  LongData.valueOf(1838382828292929l).compareTo(DoubleData.valueOf(1838382828292929d)) == 0);
        
        //numeric
        assertEquals(true,  LongData.ONE.compareTo(NumericData.valueOf(1d)) == 0);
        assertEquals(false, LongData.ONE.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(false, LongData.ONE.compareTo(NumericData.valueOf(2d)) == 0);
        assertEquals(false, LongData.ONE.compareTo(NumericData.valueOf(1.1d)) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(NumericData.valueOf(0.00d)) == 0);
        assertEquals(false, LongData.ZERO.compareTo(NumericData.valueOf(0.10d)) == 0);
        assertEquals(true,  LongData.valueOf(92929l).compareTo(NumericData.valueOf(92929d)) == 0);

        //complex
        assertEquals(true, LongData.ONE.compareTo(ComplexData.valueOf(1d)) == 0);
        assertEquals(true, LongData.ONE.compareTo(ComplexData.valueOf(1d, -1d, false)) > 0);
        assertEquals(true, LongData.ONE.compareTo(ComplexData.valueOf(-1d, -1d, false)) > 0);
        assertEquals(true, LongData.ONE.compareTo(ComplexData.valueOf(1d, 1.37d, false)) < 0);
        assertEquals(true, LongData.ONE.compareTo(ComplexData.valueOf(1d, -1.37d, false)) > 0);
        assertEquals(true, LongData.ONE.compareTo(ComplexData.valueOf(1d, 0.000d, false)) == 0);
        assertEquals(true, LongData.ONE.compareTo(ComplexData.valueOf(2, 1.37d, false)) < 0);
        
        //BigDecimal
        assertEquals(true,  LongData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("1"))) == 0);
        assertEquals(false, LongData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(false, LongData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("2.0"))) == 0);
        assertEquals(true,  LongData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("1.0000"))) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(DecimalData.valueOf(new BigDecimal("0.00000"))) == 0);
        assertEquals(false, LongData.ZERO.compareTo(DecimalData.valueOf(new BigDecimal("0.10"))) == 0);
        assertEquals(true,  LongData.valueOf(1838382828292929l).compareTo(DecimalData.valueOf(BigDecimal.valueOf(1838382828292929l))) == 0);
        
        //Boolean
        assertEquals(true,  LongData.ONE.compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, LongData.ONE.compareTo(BooleanData.FALSE) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(BooleanData.FALSE) == 0);
        assertEquals(false, LongData.ZERO.compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, LongData.valueOf(2l).compareTo(BooleanData.TRUE) == 0);
        
        //String
        assertEquals(true,  LongData.ONE.compareTo(StringData.valueOf("1")) == 0);
        assertEquals(false, LongData.ONE.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(false, LongData.ONE.compareTo(StringData.valueOf("2")) == 0);
        assertEquals(true,  LongData.ONE.compareTo(StringData.valueOf("1.0")) == 0);
        assertEquals(false, LongData.ONE.compareTo(StringData.valueOf("1.11")) == 0);
        assertEquals(true  ,LongData.ZERO.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(true,  LongData.ZERO.compareTo(StringData.valueOf("0.0000")) == 0);
        assertEquals(true,  LongData.valueOf(1838382828292929l).compareTo(StringData.valueOf("1838382828292929")) == 0);
        assertEquals(false, LongData.valueOf(13l).compareTo(StringData.valueOf("13bar")) == 0);
        
        //Bytes, string bytes comparison
        assertEquals(true,  LongData.ONE.compareTo(BinaryData.valueOf("1".getBytes())) == 0);
        assertEquals(false, LongData.ONE.compareTo(BinaryData.valueOf("0".getBytes())) == 0);
        assertEquals(false, LongData.ONE.compareTo(BinaryData.valueOf("2".getBytes())) == 0);
        assertEquals(false, LongData.ONE.compareTo(BinaryData.valueOf("1.0".getBytes())) == 0);
        
        //Null
        assertEquals(true, LongData.NULL.compareTo(NullData.INSTANCE) == 0);
        assertEquals(true, LongData.NULL.compareTo(IntData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(LongData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(FloatData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(DoubleData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(NumericData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(BooleanData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(BinaryData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(StringData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(DateData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(TimeData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(TimestampData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(ListData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(MapData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(SetData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(true, LongData.NULL.compareTo(SparseVectorData.NULL) == 0);
        
        assertEquals(false, LongData.ONE.compareTo(NullData.INSTANCE) == 0);
        assertEquals(false, LongData.ONE.compareTo(IntData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(LongData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(FloatData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(DoubleData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(NumericData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(BooleanData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(BinaryData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(StringData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(DateData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(TimeData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(TimestampData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(ListData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(MapData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(SetData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(false, LongData.ONE.compareTo(SparseVectorData.NULL) == 0);
        
        //Complex (List, Set, DenseVector, SparseVector
        assertEquals(false, LongData.ONE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {LongData.ONE})))
                ) == 0);
        assertEquals(false, LongData.ONE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {LongData.ONE})))
                ) == 0);
        assertEquals(false, LongData.ONE.compareTo(
                new DenseVectorData(new double[] {1d})
                ) == 0);
        assertEquals(false, LongData.ONE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        assertEquals(false, LongData.ONE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {LongData.ONE, LongData.valueOf(2l)})))
                ) == 0);
        assertEquals(false, LongData.ONE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {LongData.ONE, LongData.valueOf(2l)})))
                ) == 0);
        assertEquals(false, LongData.ONE.compareTo(
                new DenseVectorData(new double[] {1d, 2d})
                ) == 0);
        assertEquals(false, LongData.ONE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        map.put(LongData.ONE, LongData.ONE);
        map.put(LongData.valueOf(2l), LongData.valueOf(2l));
        
        final MapData md = new MapData(map);
        assertEquals(false,LongData.ONE.compareTo(md) == 0);
    }
    
    @Test
    public void testFloatDataCompareTo() throws EngineException, ParseException {
        //int
        assertEquals(true,  FloatData.ONE.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(IntData.valueOf(0)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(IntData.valueOf(2)) == 0);
        assertEquals(false, FloatData.ZERO.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(IntData.valueOf(0)) == 0);
        assertEquals(true,  FloatData.valueOf(1.00f).compareTo(IntData.valueOf(1)) == 0);
        
        //long
        assertEquals(true,  FloatData.ONE.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(LongData.valueOf(2l)) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, FloatData.ZERO.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(true,  FloatData.valueOf(18382f).compareTo(LongData.valueOf(18382l)) == 0);
        assertEquals(true,  FloatData.valueOf(1.00f).compareTo(LongData.valueOf(1l)) == 0);
        
        //float
        assertEquals(true,  FloatData.ONE.compareTo(FloatData.valueOf(1f)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(FloatData.valueOf(2f)) == 0);
        assertEquals(true,  FloatData.ONE.compareTo(FloatData.valueOf(1.00f)) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(FloatData.valueOf(0.00f)) == 0);
        assertEquals(true,  FloatData.valueOf(3.50f).compareTo(FloatData.valueOf(3.5f)) == 0);

        //double
        assertEquals(true,  FloatData.ONE.compareTo(DoubleData.valueOf(1d)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(DoubleData.valueOf(2d)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(DoubleData.valueOf(1.1d)) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(DoubleData.valueOf(0.00d)) == 0);
        assertEquals(false, FloatData.ZERO.compareTo(DoubleData.valueOf(0.10d)) == 0);
        assertEquals(true,  FloatData.valueOf(18382.05f).compareTo(DoubleData.valueOf(18382.05d)) == 0);
        
        //double
        assertEquals(true,  FloatData.ONE.compareTo(NumericData.valueOf(1d)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(NumericData.valueOf(2d)) == 0);
        assertEquals(false, FloatData.ONE.compareTo(NumericData.valueOf(1.1d)) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(NumericData.valueOf(0.00d)) == 0);
        assertEquals(false, FloatData.ZERO.compareTo(NumericData.valueOf(0.10d)) == 0);
        assertEquals(true,  FloatData.valueOf(18382.05f).compareTo(NumericData.valueOf(18382.05d)) == 0);
        
        //complex
        assertEquals(true, FloatData.ONE.compareTo(ComplexData.valueOf(1d)) == 0);
        assertEquals(true, FloatData.ONE.compareTo(ComplexData.valueOf(1d, -1d, false)) > 0);
        assertEquals(true, FloatData.ONE.compareTo(ComplexData.valueOf(-1d, -1d, false)) > 0);
        assertEquals(true, FloatData.ONE.compareTo(ComplexData.valueOf(1d, 1.37d, false)) < 0);
        assertEquals(true, FloatData.ONE.compareTo(ComplexData.valueOf(1d, -1.37d, false)) > 0);
        assertEquals(true, FloatData.ONE.compareTo(ComplexData.valueOf(1d, 0.000d, false)) == 0);
        assertEquals(true, FloatData.ONE.compareTo(ComplexData.valueOf(2d, 1.37d, false)) < 0);
        
        //BigDecimal
        assertEquals(true,  FloatData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("1"))) == 0);
        assertEquals(false, FloatData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(false, FloatData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("2.0"))) == 0);
        assertEquals(true,  FloatData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("1.0000"))) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(true,  FloatData.valueOf(0.00f).compareTo(DecimalData.valueOf(new BigDecimal("0.00000"))) == 0);
        assertEquals(false, FloatData.valueOf(0.101f).compareTo(DecimalData.valueOf(new BigDecimal("0.10"))) == 0);
        assertEquals(true,  FloatData.valueOf(18382f).compareTo(DecimalData.valueOf(BigDecimal.valueOf(18382l))) == 0);
        
        //Boolean
        assertEquals(true,  FloatData.ONE.compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, FloatData.ONE.compareTo(BooleanData.FALSE) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(BooleanData.FALSE) == 0);
        assertEquals(false, FloatData.ZERO.compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, FloatData.valueOf(2f).compareTo(BooleanData.TRUE) == 0);
        assertEquals(true,  FloatData.valueOf(1.0f).compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, FloatData.valueOf(1.1f).compareTo(BooleanData.TRUE) == 0);
        
        //String
        assertEquals(true,  FloatData.ONE.compareTo(StringData.valueOf("1")) == 0);
        assertEquals(false, FloatData.ONE.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(false, FloatData.ONE.compareTo(StringData.valueOf("2")) == 0);
        assertEquals(true,  FloatData.ONE.compareTo(StringData.valueOf("1.0")) == 0);
        assertEquals(false, FloatData.ONE.compareTo(StringData.valueOf("1.11")) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(StringData.valueOf("0.0000")) == 0);
        assertEquals(true,  FloatData.valueOf(18382.05f).compareTo(StringData.valueOf("18382.05")) == 0);
        assertEquals(false, FloatData.valueOf(13f).compareTo(StringData.valueOf("13bar")) == 0);
        
        //Bytes, string bytes comparison
        assertEquals(true,  FloatData.ONE.compareTo(BinaryData.valueOf("1.0".getBytes())) == 0);
        assertEquals(false, FloatData.ONE.compareTo(BinaryData.valueOf("0.0".getBytes())) == 0);
        assertEquals(true,  FloatData.ZERO.compareTo(BinaryData.valueOf("0.0".getBytes())) == 0);
        assertEquals(false, FloatData.ONE.compareTo(BinaryData.valueOf("2.0".getBytes())) == 0);
        
        //Null
        assertEquals(true, FloatData.NULL.compareTo(NullData.INSTANCE) == 0);
        assertEquals(true, FloatData.NULL.compareTo(IntData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(LongData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(FloatData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(DoubleData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(NumericData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(BooleanData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(BinaryData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(StringData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(DateData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(TimeData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(TimestampData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(ListData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(MapData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(SetData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(true, FloatData.NULL.compareTo(SparseVectorData.NULL) == 0);
        
        assertEquals(false, FloatData.ONE.compareTo(NullData.INSTANCE) == 0);
        assertEquals(false, FloatData.ONE.compareTo(IntData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(LongData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(FloatData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(DoubleData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(NumericData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(BooleanData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(BinaryData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(StringData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(DateData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(TimeData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(TimestampData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(ListData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(MapData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(SetData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(false, FloatData.ONE.compareTo(SparseVectorData.NULL) == 0);
        
        //Complex (List, Set, DenseVector, SparseVector
        assertEquals(false, FloatData.ONE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {FloatData.ONE})))
                ) == 0);
        assertEquals(false, FloatData.ONE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {FloatData.ONE})))
                ) == 0);
        assertEquals(false, FloatData.ONE.compareTo(
                new DenseVectorData(new double[] {1d})
                ) == 0);
        assertEquals(false, LongData.ONE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        assertEquals(false, FloatData.ONE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {FloatData.ONE, FloatData.valueOf(2f)})))
                ) == 0);
        assertEquals(false, FloatData.ONE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {FloatData.ONE, FloatData.valueOf(2f)})))
                ) == 0);
        assertEquals(false, FloatData.ONE.compareTo(
                new DenseVectorData(new double[] {1d, 2d})
                ) == 0);
        assertEquals(false, FloatData.ONE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        map.put(FloatData.ONE, FloatData.ONE);
        map.put(FloatData.valueOf(2f), FloatData.valueOf(2f));
        
        final MapData md = new MapData(map);
        assertEquals(false,FloatData.ONE.compareTo(md) == 0);
    }
    
    @Test
    public void testDoubleDataCompareTo() throws EngineException, ParseException {
        //int
        assertEquals(true,  DoubleData.ONE.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(IntData.valueOf(0)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(IntData.valueOf(2)) == 0);
        assertEquals(false, DoubleData.ZERO.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(IntData.valueOf(0)) == 0);
        assertEquals(true,  DoubleData.valueOf(1.00d).compareTo(IntData.valueOf(1)) == 0);
        
        //long
        assertEquals(true,  DoubleData.ONE.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(LongData.valueOf(2l)) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, DoubleData.ZERO.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(true,  DoubleData.valueOf(18382d).compareTo(LongData.valueOf(18382l)) == 0);
        
        //float
        assertEquals(true,  DoubleData.ONE.compareTo(FloatData.valueOf(1f)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(FloatData.valueOf(2f)) == 0);
        assertEquals(true,  DoubleData.ONE.compareTo(FloatData.valueOf(1.00f)) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(FloatData.valueOf(0.00f)) == 0);
        assertEquals(true,  DoubleData.valueOf(3.50d).compareTo(FloatData.valueOf(3.5f)) == 0);

        //double
        assertEquals(true,  DoubleData.ONE.compareTo(DoubleData.valueOf(1d)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(DoubleData.valueOf(2d)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(DoubleData.valueOf(1.1d)) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(DoubleData.valueOf(0.00d)) == 0);
        assertEquals(false, DoubleData.ZERO.compareTo(DoubleData.valueOf(0.10d)) == 0);
        assertEquals(true,  DoubleData.valueOf(18382.05d).compareTo(DoubleData.valueOf(18382.0500d)) == 0);

        //numeric
        assertEquals(true,  DoubleData.ONE.compareTo(NumericData.valueOf(1d)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(NumericData.valueOf(2d)) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(NumericData.valueOf(1.1d)) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(NumericData.valueOf(0.00d)) == 0);
        assertEquals(false, DoubleData.ZERO.compareTo(NumericData.valueOf(0.10d)) == 0);
        assertEquals(true,  DoubleData.valueOf(18382.05d).compareTo(NumericData.valueOf(18382.0500d)) == 0);

        //complex
        assertEquals(true, DoubleData.ONE.compareTo(ComplexData.valueOf(1d)) == 0);
        assertEquals(true, DoubleData.ONE.compareTo(ComplexData.valueOf(1d, -1d, false)) > 0);
        assertEquals(true, DoubleData.ONE.compareTo(ComplexData.valueOf(-1d, -1d, false)) > 0);
        assertEquals(true, DoubleData.ONE.compareTo(ComplexData.valueOf(1d, 1.37d, false)) < 0);
        assertEquals(true, DoubleData.ONE.compareTo(ComplexData.valueOf(1d, -1.37d, false)) > 0);
        assertEquals(true, DoubleData.ONE.compareTo(ComplexData.valueOf(1d, 0.000d, false)) == 0);
        assertEquals(true, DoubleData.ONE.compareTo(ComplexData.valueOf(2, 1.37d, false)) < 0);
        
        //BigDecimal
        assertEquals(true,  DoubleData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("1"))) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("2.0"))) == 0);
        assertEquals(true,  DoubleData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("1.0000"))) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(true,  DoubleData.valueOf(0.00d).compareTo(DecimalData.valueOf(new BigDecimal("0.00000"))) == 0);
        assertEquals(false, DoubleData.valueOf(0.101d).compareTo(DecimalData.valueOf(new BigDecimal("0.10"))) == 0);
        assertEquals(true,  DoubleData.valueOf(18382d).compareTo(DecimalData.valueOf(BigDecimal.valueOf(18382l))) == 0);
        
        //Boolean
        assertEquals(true,  DoubleData.ONE.compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(BooleanData.FALSE) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(BooleanData.FALSE) == 0);
        assertEquals(false, DoubleData.ZERO.compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, DoubleData.valueOf(2d).compareTo(BooleanData.TRUE) == 0);
        assertEquals(true,  DoubleData.valueOf(1.0d).compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, DoubleData.valueOf(1.1d).compareTo(BooleanData.TRUE) == 0);
        
        //String
        assertEquals(true,  DoubleData.ONE.compareTo(StringData.valueOf("1")) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(StringData.valueOf("2")) == 0);
        assertEquals(true,  DoubleData.ONE.compareTo(StringData.valueOf("1.0")) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(StringData.valueOf("1.11")) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(StringData.valueOf("0.0000")) == 0);
        assertEquals(true,  DoubleData.valueOf(18382.05d).compareTo(StringData.valueOf("18382.05")) == 0);
        assertEquals(false, DoubleData.valueOf(13d).compareTo(StringData.valueOf("13bar")) == 0);
        
        //Bytes, string bytes comparison
        assertEquals(true,  DoubleData.ONE.compareTo(BinaryData.valueOf("1".getBytes())) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(BinaryData.valueOf("0".getBytes())) == 0);
        assertEquals(true,  DoubleData.ZERO.compareTo(BinaryData.valueOf("0".getBytes())) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(BinaryData.valueOf("2".getBytes())) == 0);
        
        //Null
        assertEquals(true, DoubleData.NULL.compareTo(NullData.INSTANCE) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(IntData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(LongData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(FloatData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(DoubleData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(NumericData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(ComplexData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(BooleanData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(BinaryData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(StringData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(DateData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(TimeData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(TimestampData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(ListData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(MapData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(SetData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(true, DoubleData.NULL.compareTo(SparseVectorData.NULL) == 0);
        
        assertEquals(false, DoubleData.ONE.compareTo(NullData.INSTANCE) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(IntData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(LongData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(FloatData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(DoubleData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(NumericData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(BooleanData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(BinaryData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(StringData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(DateData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(TimeData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(TimestampData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(ListData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(MapData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(SetData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(SparseVectorData.NULL) == 0);
        
        //Complex (List, Set, DenseVector, SparseVector
        assertEquals(false, DoubleData.ONE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {DoubleData.ONE})))
                ) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {DoubleData.ONE})))
                ) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(
                new DenseVectorData(new double[] {1d})
                ) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        assertEquals(false, DoubleData.ONE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {DoubleData.ONE, DoubleData.valueOf(2d)})))
                ) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {DoubleData.ONE, DoubleData.valueOf(2d)})))
                ) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(
                new DenseVectorData(new double[] {1d, 2d})
                ) == 0);
        assertEquals(false, DoubleData.ONE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        map.put(DoubleData.ONE, DoubleData.ONE);
        map.put(DoubleData.valueOf(2d), DoubleData.valueOf(2d));
        
        final MapData md = new MapData(map);
        assertEquals(false, DoubleData.ONE.compareTo(md) == 0);
    }
    
    @Test
    public void testNumericDataCompareTo() throws EngineException, ParseException {
        //int
        assertEquals(true,  NumericData.ONE.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(IntData.valueOf(0)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(IntData.valueOf(2)) == 0);
        assertEquals(false, NumericData.ZERO.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(IntData.valueOf(0)) == 0);
        assertEquals(true,  NumericData.valueOf(1.00d).compareTo(IntData.valueOf(1)) == 0);
        
        //long
        assertEquals(true,  NumericData.ONE.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(LongData.valueOf(2l)) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, NumericData.ZERO.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(true,  NumericData.valueOf(18382d).compareTo(LongData.valueOf(18382l)) == 0);
        
        //float
        assertEquals(true,  NumericData.ONE.compareTo(FloatData.valueOf(1f)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(FloatData.valueOf(2f)) == 0);
        assertEquals(true,  NumericData.ONE.compareTo(FloatData.valueOf(1.00f)) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(FloatData.valueOf(0.00f)) == 0);
        assertEquals(true,  NumericData.valueOf(3.50d).compareTo(FloatData.valueOf(3.5f)) == 0);

        //double
        assertEquals(true,  NumericData.ONE.compareTo(DoubleData.valueOf(1d)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(DoubleData.valueOf(2d)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(DoubleData.valueOf(1.1d)) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(DoubleData.valueOf(0.00d)) == 0);
        assertEquals(false, NumericData.ZERO.compareTo(DoubleData.valueOf(0.10d)) == 0);
        assertEquals(true,  NumericData.valueOf(18382.05d).compareTo(DoubleData.valueOf(18382.0500d)) == 0);

        //numeric
        assertEquals(true,  NumericData.ONE.compareTo(NumericData.valueOf(1d)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(NumericData.valueOf(2d)) == 0);
        assertEquals(false, NumericData.ONE.compareTo(NumericData.valueOf(1.1d)) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(NumericData.valueOf(0.00d)) == 0);
        assertEquals(false, NumericData.ZERO.compareTo(NumericData.valueOf(0.10d)) == 0);
        assertEquals(true,  NumericData.valueOf(18382.05d).compareTo(NumericData.valueOf(18382.0500d)) == 0);

        //complex
        assertEquals(true, NumericData.ONE.compareTo(ComplexData.valueOf(1d)) == 0);
        assertEquals(true, NumericData.ONE.compareTo(ComplexData.valueOf(1d, -1d, false)) > 0);
        assertEquals(true, NumericData.ONE.compareTo(ComplexData.valueOf(-1d, -1d, false)) > 0);
        assertEquals(true, NumericData.ONE.compareTo(ComplexData.valueOf(1d, 1.37d, false)) < 0);
        assertEquals(true, NumericData.ONE.compareTo(ComplexData.valueOf(1d, -1.37d, false)) > 0);
        assertEquals(true, NumericData.ONE.compareTo(ComplexData.valueOf(1d, 0.000d, false)) == 0);
        assertEquals(true, NumericData.ONE.compareTo(ComplexData.valueOf(2, 1.37d, false)) < 0);
        
        //BigDecimal
        assertEquals(true,  NumericData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("1"))) == 0);
        assertEquals(false, NumericData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(false, NumericData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("2.0"))) == 0);
        assertEquals(true,  NumericData.ONE.compareTo(DecimalData.valueOf(new BigDecimal("1.0000"))) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(true,  NumericData.valueOf(0.00d).compareTo(DecimalData.valueOf(new BigDecimal("0.00000"))) == 0);
        assertEquals(false, NumericData.valueOf(0.101d).compareTo(DecimalData.valueOf(new BigDecimal("0.10"))) == 0);
        assertEquals(true,  NumericData.valueOf(18382d).compareTo(DecimalData.valueOf(BigDecimal.valueOf(18382l))) == 0);
        
        //Boolean
        assertEquals(true,  NumericData.ONE.compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, NumericData.ONE.compareTo(BooleanData.FALSE) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(BooleanData.FALSE) == 0);
        assertEquals(false, NumericData.ZERO.compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, NumericData.valueOf(2d).compareTo(BooleanData.TRUE) == 0);
        assertEquals(true,  NumericData.valueOf(1.0d).compareTo(BooleanData.TRUE) == 0);
        assertEquals(false, NumericData.valueOf(1.1d).compareTo(BooleanData.TRUE) == 0);
        
        //String
        assertEquals(true,  NumericData.ONE.compareTo(StringData.valueOf("1")) == 0);
        assertEquals(false, NumericData.ONE.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(false, NumericData.ONE.compareTo(StringData.valueOf("2")) == 0);
        assertEquals(true,  NumericData.ONE.compareTo(StringData.valueOf("1.0")) == 0);
        assertEquals(false, NumericData.ONE.compareTo(StringData.valueOf("1.11")) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(StringData.valueOf("0.0000")) == 0);
        assertEquals(true,  NumericData.valueOf(18382.05d).compareTo(StringData.valueOf("18382.05")) == 0);
        assertEquals(false, NumericData.valueOf(13d).compareTo(StringData.valueOf("13bar")) == 0);
        
        //Bytes, string bytes comparison
        assertEquals(true,  NumericData.ONE.compareTo(BinaryData.valueOf("1".getBytes())) == 0);
        assertEquals(false, NumericData.ONE.compareTo(BinaryData.valueOf("0".getBytes())) == 0);
        assertEquals(true,  NumericData.ZERO.compareTo(BinaryData.valueOf("0".getBytes())) == 0);
        assertEquals(false, NumericData.ONE.compareTo(BinaryData.valueOf("2".getBytes())) == 0);
        
        //Null
        assertEquals(true, NumericData.NULL.compareTo(NullData.INSTANCE) == 0);
        assertEquals(true, NumericData.NULL.compareTo(IntData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(LongData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(FloatData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(DoubleData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(NumericData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(ComplexData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(BooleanData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(BinaryData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(StringData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(DateData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(TimeData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(TimestampData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(ListData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(MapData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(SetData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(true, NumericData.NULL.compareTo(SparseVectorData.NULL) == 0);
        
        assertEquals(false, NumericData.ONE.compareTo(NullData.INSTANCE) == 0);
        assertEquals(false, NumericData.ONE.compareTo(IntData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(LongData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(FloatData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(DoubleData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(NumericData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(BooleanData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(BinaryData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(StringData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(DateData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(TimeData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(TimestampData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(ListData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(MapData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(SetData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(false, NumericData.ONE.compareTo(SparseVectorData.NULL) == 0);
        
        //Complex (List, Set, DenseVector, SparseVector
        assertEquals(false, NumericData.ONE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {DoubleData.ONE})))
                ) == 0);
        assertEquals(false, NumericData.ONE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {DoubleData.ONE})))
                ) == 0);
        assertEquals(false, NumericData.ONE.compareTo(
                new DenseVectorData(new double[] {1d})
                ) == 0);
        assertEquals(false, NumericData.ONE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        assertEquals(false, NumericData.ONE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {DoubleData.ONE, DoubleData.valueOf(2d)})))
                ) == 0);
        assertEquals(false, NumericData.ONE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {DoubleData.ONE, DoubleData.valueOf(2d)})))
                ) == 0);
        assertEquals(false, NumericData.ONE.compareTo(
                new DenseVectorData(new double[] {1d, 2d})
                ) == 0);
        assertEquals(false, NumericData.ONE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        map.put(NumericData.ONE, NumericData.ONE);
        map.put(NumericData.valueOf(2d), NumericData.valueOf(2d));
        
        final MapData md = new MapData(map);
        assertEquals(false, NumericData.ONE.compareTo(md) == 0);
    }

    @Test
    public void testDoubleComplexCompareTo() throws EngineException, ParseException {
        final ComplexData cd = new ComplexData(1d, 1d);
        
        //int
        assertEquals(true,  cd.compareTo(IntData.valueOf(1)) > 0);
        assertEquals(true,  cd.compareTo(IntData.valueOf(0)) > 0);
        assertEquals(true,  cd.compareTo(IntData.valueOf(2)) < 0);
        assertEquals(true,  cd.compareTo(IntData.valueOf(1)) > 0);
        assertEquals(true,  ComplexData.ZERO.compareTo(IntData.valueOf(0)) == 0);
        assertEquals(true,  ComplexData.valueOf(1.00d).compareTo(IntData.valueOf(1)) == 0);
        
        //long
        assertEquals(true,  cd.compareTo(LongData.valueOf(1l)) > 0);
        assertEquals(true,  cd.compareTo(LongData.valueOf(0l)) > 0);
        assertEquals(true,  cd.compareTo(LongData.valueOf(2l)) < 0);
        assertEquals(true,  cd.compareTo(LongData.valueOf(0l)) > 0);
        assertEquals(true,  ComplexData.ZERO.compareTo(LongData.valueOf(1l)) < 0);
        assertEquals(true,  ComplexData.valueOf(18382d).compareTo(LongData.valueOf(18382l)) == 0);

        //float
        assertEquals(true,  cd.compareTo(FloatData.valueOf(1f)) > 0);
        assertEquals(true,  cd.compareTo(FloatData.valueOf(0f)) > 0);
        assertEquals(true,  cd.compareTo(FloatData.valueOf(2f)) < 0);
        assertEquals(true,  cd.compareTo(FloatData.valueOf(0f)) > 0);
        assertEquals(true,  ComplexData.ZERO.compareTo(FloatData.valueOf(1f)) < 0);
        assertEquals(true,  ComplexData.valueOf(18382d).compareTo(FloatData.valueOf(18382f)) == 0);

        //double
        assertEquals(true,  cd.compareTo(DoubleData.valueOf(1d)) > 0);
        assertEquals(true,  cd.compareTo(DoubleData.valueOf(0d)) > 0);
        assertEquals(true,  cd.compareTo(DoubleData.valueOf(2d)) < 0);
        assertEquals(true,  cd.compareTo(DoubleData.valueOf(0d)) > 0);
        assertEquals(true,  ComplexData.ZERO.compareTo(DoubleData.valueOf(1d)) < 0);
        assertEquals(true,  ComplexData.valueOf(18382d).compareTo(DoubleData.valueOf(18382d)) == 0);
        
        //double
        assertEquals(true,  cd.compareTo(NumericData.valueOf(1d)) > 0);
        assertEquals(true,  cd.compareTo(NumericData.valueOf(0d)) > 0);
        assertEquals(true,  cd.compareTo(NumericData.valueOf(2d)) < 0);
        assertEquals(true,  cd.compareTo(NumericData.valueOf(0d)) > 0);
        assertEquals(true,  ComplexData.ZERO.compareTo(NumericData.valueOf(1d)) < 0);
        assertEquals(true,  ComplexData.valueOf(18382d).compareTo(NumericData.valueOf(18382d)) == 0);
        
        
        //Decimal
        assertEquals(true,  cd.compareTo(DecimalData.valueOf(new BigDecimal("1"))) > 0);
        assertEquals(true,  cd.compareTo(DecimalData.valueOf(new BigDecimal("0"))) > 0);
        assertEquals(true,  cd.compareTo(DecimalData.valueOf(new BigDecimal("2.0"))) < 0);
        assertEquals(true,  cd.compareTo(DecimalData.valueOf(new BigDecimal("1.0000"))) > 0);
        assertEquals(true,  cd.compareTo(DecimalData.valueOf(new BigDecimal("0"))) > 0);
        assertEquals(true,  cd.compareTo(DecimalData.valueOf(new BigDecimal("0.00000"))) > 0);
        assertEquals(true,  cd.compareTo(DecimalData.valueOf(new BigDecimal("0.10"))) > 0);
        assertEquals(true,  cd.compareTo(DecimalData.valueOf(BigDecimal.valueOf(18382l))) < 0);
        
    }
    
    @Test
    public void testBooleanDataCompareTo() throws EngineException, ParseException {
        //int
        assertEquals(true,  BooleanData.TRUE.compareTo(IntData.valueOf(1)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(IntData.valueOf(0)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(IntData.valueOf(2)) == 0);
        assertEquals(false, BooleanData.FALSE.compareTo(IntData.valueOf(1)) == 0);
        
        //long
        assertEquals(true,  BooleanData.TRUE.compareTo(LongData.valueOf(1l)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(LongData.valueOf(2l)) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(LongData.valueOf(0l)) == 0);
        assertEquals(false, BooleanData.FALSE.compareTo(LongData.valueOf(1l)) == 0);
        
        //float
        assertEquals(true,  BooleanData.TRUE.compareTo(FloatData.valueOf(1f)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(FloatData.valueOf(2f)) == 0);
        assertEquals(true,  BooleanData.TRUE.compareTo(FloatData.valueOf(1.00f)) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(FloatData.valueOf(0f)) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(FloatData.valueOf(0.00f)) == 0);

        //double
        assertEquals(true,  BooleanData.TRUE.compareTo(DoubleData.valueOf(1d)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(DoubleData.valueOf(2d)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(DoubleData.valueOf(1.1d)) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(DoubleData.valueOf(0d)) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(DoubleData.valueOf(0.00d)) == 0);
        assertEquals(false, BooleanData.FALSE.compareTo(DoubleData.valueOf(0.10d)) == 0);
        
        //double
        assertEquals(true,  BooleanData.TRUE.compareTo(NumericData.valueOf(1d)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(NumericData.valueOf(2d)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(NumericData.valueOf(1.1d)) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(NumericData.valueOf(0d)) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(NumericData.valueOf(0.00d)) == 0);
        assertEquals(false, BooleanData.FALSE.compareTo(NumericData.valueOf(0.10d)) == 0);
        
        //Decimal
        assertEquals(true,  BooleanData.TRUE.compareTo(DecimalData.valueOf(new BigDecimal("1"))) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(DecimalData.valueOf(new BigDecimal("2.0"))) == 0);
        assertEquals(true,  BooleanData.TRUE.compareTo(DecimalData.valueOf(new BigDecimal("1.0000"))) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(DecimalData.valueOf(new BigDecimal("0"))) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(DecimalData.valueOf(new BigDecimal("0.00000"))) == 0);
        assertEquals(false, BooleanData.FALSE.compareTo(DecimalData.valueOf(new BigDecimal("0.10"))) == 0);
        
        //Boolean
        assertEquals(true,  BooleanData.TRUE.compareTo(BooleanData.valueOf(true)) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(BooleanData.valueOf(false)) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(BooleanData.valueOf(false)) == 0);
        assertEquals(false, BooleanData.FALSE.compareTo(BooleanData.valueOf(true)) == 0);
        
        //String
        assertEquals(true,  BooleanData.TRUE.compareTo(StringData.valueOf("1")) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(StringData.valueOf("2")) == 0);
        assertEquals(true,  BooleanData.TRUE.compareTo(StringData.valueOf("1.0")) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(StringData.valueOf("1.11")) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(StringData.valueOf("0")) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(StringData.valueOf("0.0000")) == 0);
        assertEquals(false, BooleanData.FALSE.compareTo(StringData.valueOf("1.0000")) == 0);
        assertEquals(true, BooleanData.TRUE.compareTo(StringData.valueOf("TRUE")) == 0);
        assertEquals(true, BooleanData.FALSE.compareTo(StringData.valueOf("FALSE")) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(StringData.valueOf("TRUE1")) == 0);
        assertEquals(false, BooleanData.FALSE.compareTo(StringData.valueOf("FALSE0")) == 0);
        
        //Bytes, string bytes comparison
        assertEquals(true,  BooleanData.FALSE.compareTo(BinaryData.valueOf("0".getBytes())) == 0);
        assertEquals(true, BooleanData.TRUE.compareTo(BinaryData.valueOf("1".getBytes())) == 0);
        assertEquals(true,  BooleanData.FALSE.compareTo(BinaryData.valueOf("0".getBytes())) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(BinaryData.valueOf("0".getBytes())) == 0);
        
        //Null
        assertEquals(true, BooleanData.NULL.compareTo(NullData.INSTANCE) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(IntData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(LongData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(FloatData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(DoubleData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(NumericData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(BooleanData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(BinaryData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(StringData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(DateData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(TimeData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(TimestampData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(ListData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(MapData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(SetData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(true, BooleanData.NULL.compareTo(SparseVectorData.NULL) == 0);
        
        assertEquals(false, BooleanData.TRUE.compareTo(NullData.INSTANCE) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(IntData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(LongData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(FloatData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(DoubleData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(NumericData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(BooleanData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(BinaryData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(StringData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(DateData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(TimeData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(TimestampData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(CalendarTimeData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(CalendarTimestampData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(ListData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(MapData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(SetData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(DenseVectorData.NULL) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(SparseVectorData.NULL) == 0);
        
        //Complex (List, Set, DenseVector, SparseVector
        assertEquals(false, BooleanData.TRUE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {BooleanData.TRUE})))
                ) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {BooleanData.TRUE})))
                ) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(
                new DenseVectorData(new double[] {1d})
                ) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        assertEquals(false, BooleanData.TRUE.compareTo(
                new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {BooleanData.TRUE, BooleanData.FALSE})))
                ) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(
                new SetData(new LinkedHashSet<>(Arrays.asList( new TypeData[] {BooleanData.TRUE, BooleanData.FALSE})))
                ) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(
                new DenseVectorData(new double[] {1d, 2d})
                ) == 0);
        assertEquals(false, BooleanData.TRUE.compareTo(
                new SparseVectorData(new double[] {1d})
                ) == 0);
        
        final LinkedHashMap<TypeData, TypeData> map = new LinkedHashMap<>();
        map.put(BooleanData.TRUE, BooleanData.TRUE);
        map.put(BooleanData.FALSE, BooleanData.FALSE);
        
        final MapData md = new MapData(map);
        assertEquals(false,BooleanData.TRUE.compareTo(md) == 0);
    }
}
