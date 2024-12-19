package com.exceeddata.ac.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.ComplexData;
import com.exceeddata.ac.common.data.typedata.DecimalData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.FloatData;
import com.exceeddata.ac.common.data.typedata.IntData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.NumericData;
import com.exceeddata.ac.common.data.typedata.StringData;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.util.calc.XCalculationUtils;

public class CalculationUtilsTest {
    
    private IntData i1 = IntData.valueOf(1);
    private LongData l1 = LongData.valueOf(4l);
    private FloatData f1 = new FloatData(5.0f);
    private DoubleData d1 = new DoubleData(10.0d);
    private NumericData n1 = new NumericData(100.0d);
    private DecimalData bd1 = new DecimalData(BigDecimal.valueOf(-1l));
    private StringData s1 = StringData.valueOf("5");
    private StringData s2 = StringData.valueOf("5.0");


    @Test
    public void testException() throws EngineException {
        try {
            XCalculationUtils.add(s1, StringData.valueOf("foo"), true, true);
            fail();
        } catch(EngineException e) {
            assertEquals(e.getMessage(), "DATA_ADD: 'foo' IS NOT NUMBER");
        }
        try {
            XCalculationUtils.add(StringData.valueOf("bar"), s1, true, true);
            fail();
        } catch(EngineException e) {
            assertEquals(e.getMessage(), "DATA_ADD: 'bar' IS NOT NUMBER");
        }
        try {
            XCalculationUtils.subtract(s1, StringData.valueOf("foo"), true);
            fail();
        } catch(EngineException e) {
            assertEquals(e.getMessage(), "DATA_SUBTRACT: 'foo' IS NOT NUMBER");
        }
        try {
            XCalculationUtils.subtract(StringData.valueOf("bar"), s1, true);
            fail();
        } catch(EngineException e) {
            assertEquals(e.getMessage(), "DATA_SUBTRACT: 'bar' IS NOT NUMBER");
        }
        try {
            XCalculationUtils.multiply(s1, StringData.valueOf("foo"), true);
            fail();
        } catch(EngineException e) {
            assertEquals(e.getMessage(), "DATA_MULTIPLY: 'foo' IS NOT NUMBER");
        }
        try {
            XCalculationUtils.multiply(StringData.valueOf("bar"), s1, true);
            fail();
        } catch(EngineException e) {
            assertEquals(e.getMessage(), "DATA_MULTIPLY: 'bar' IS NOT NUMBER");
        }
        try {
            XCalculationUtils.divide(s1, StringData.valueOf("foo"), true);
            fail();
        } catch(EngineException e) {
            assertEquals(e.getMessage(), "DATA_DIVIDE: 'foo' IS NOT NUMBER");
        }
        try {
            XCalculationUtils.divide(StringData.valueOf("bar"), s1, true);
            fail();
        } catch(EngineException e) {
            assertEquals(e.getMessage(), "DATA_DIVIDE: 'bar' IS NOT NUMBER");
        }
    }
    
    @Test
    public void testComplex() throws EngineException {
        final ComplexData cd = new ComplexData(4d, 3d);
        final ComplexData cd2 = new ComplexData(2d, 4d);
        final StringData sd = new StringData("4+3i");
        final StringData sd2 = new StringData("2+4i");
        
        assertEquals(XCalculationUtils.add(cd, IntData.TWO, true, true), new ComplexData(6d, 3d));
        assertEquals(XCalculationUtils.subtract(cd, IntData.TWO, true), new ComplexData(2d, 3d));
        assertEquals(XCalculationUtils.multiply(cd, IntData.TWO, true), new ComplexData(8d, 6d));
        assertEquals(XCalculationUtils.divide(cd, IntData.TWO, true), new ComplexData(2d, 1.5d));

        assertEquals(XCalculationUtils.add(IntData.TWO, cd, true, true), new ComplexData(6d, 3d));
        assertEquals(XCalculationUtils.subtract(IntData.TWO, cd, true), new ComplexData(-2d, -3d));
        assertEquals(XCalculationUtils.multiply(IntData.TWO, cd, true), new ComplexData(8d, 6d));
        assertEquals(XCalculationUtils.divide(IntData.TWO, cd, true), new ComplexData(0.32d, -0.24d));
        
        assertEquals(XCalculationUtils.add(cd, LongData.valueOf(2l), true, true), new ComplexData(6d, 3d));
        assertEquals(XCalculationUtils.subtract(cd, LongData.valueOf(2l), true), new ComplexData(2d, 3d));
        assertEquals(XCalculationUtils.multiply(cd, LongData.valueOf(2l), true), new ComplexData(8d, 6d));
        assertEquals(XCalculationUtils.divide(cd, LongData.valueOf(2l), true), new ComplexData(2d, 1.5d));

        assertEquals(XCalculationUtils.add(LongData.valueOf(2l), cd, true, true), new ComplexData(6d, 3d));
        assertEquals(XCalculationUtils.subtract(LongData.valueOf(2l), cd, true), new ComplexData(-2d, -3d));
        assertEquals(XCalculationUtils.multiply(LongData.valueOf(2l), cd, true), new ComplexData(8d, 6d));
        assertEquals(XCalculationUtils.divide(LongData.valueOf(2l), cd, true), new ComplexData(0.32d, -0.24d));
        
        assertEquals(XCalculationUtils.add(cd, DoubleData.valueOf(2d), true, true), new ComplexData(6d, 3d));
        assertEquals(XCalculationUtils.subtract(cd, DoubleData.valueOf(2d), true), new ComplexData(2d, 3d));
        assertEquals(XCalculationUtils.multiply(cd, DoubleData.valueOf(2d), true), new ComplexData(8d, 6d));
        assertEquals(XCalculationUtils.divide(cd, DoubleData.valueOf(2d), true), new ComplexData(2d, 1.5d));

        assertEquals(XCalculationUtils.add(DoubleData.valueOf(2d), cd, true, true), new ComplexData(6d, 3d));
        assertEquals(XCalculationUtils.subtract(DoubleData.valueOf(2d), cd, true), new ComplexData(-2d, -3d));
        assertEquals(XCalculationUtils.multiply(DoubleData.valueOf(2d), cd, true), new ComplexData(8d, 6d));
        assertEquals(XCalculationUtils.divide(DoubleData.valueOf(2d), cd, true), new ComplexData(0.32d, -0.24d));

        assertEquals(XCalculationUtils.add(cd, DecimalData.valueOf("2"), true, true), new ComplexData(6d, 3d));
        assertEquals(XCalculationUtils.subtract(cd, DecimalData.valueOf("2"), true), new ComplexData(2d, 3d));
        assertEquals(XCalculationUtils.multiply(cd, DecimalData.valueOf("2"), true), new ComplexData(8d, 6d));
        assertEquals(XCalculationUtils.divide(cd, DecimalData.valueOf("2"), true), new ComplexData(2d, 1.5d));

        assertEquals(XCalculationUtils.add(DecimalData.valueOf("2"), cd, true, true), new ComplexData(6d, 3d));
        assertEquals(XCalculationUtils.subtract(DecimalData.valueOf("2"), cd, true), new ComplexData(-2d, -3d));
        assertEquals(XCalculationUtils.multiply(DecimalData.valueOf("2"), cd, true), new ComplexData(8d, 6d));
        assertEquals(XCalculationUtils.divide(DecimalData.valueOf("2"), cd, true), new ComplexData(0.32d, -0.24d));
        
        assertEquals(XCalculationUtils.add(cd, StringData.valueOf("2"), true, true), new ComplexData(6d, 3d));
        assertEquals(XCalculationUtils.subtract(cd, StringData.valueOf("2"), true), new ComplexData(2d, 3d));
        assertEquals(XCalculationUtils.multiply(cd, StringData.valueOf("2"), true), new ComplexData(8d, 6d));
        assertEquals(XCalculationUtils.divide(cd, StringData.valueOf("2"), true), new ComplexData(2d, 1.5d));

        assertEquals(XCalculationUtils.add(StringData.valueOf("2"), cd, true, true), new ComplexData(6d, 3d));
        assertEquals(XCalculationUtils.subtract(StringData.valueOf("2"), cd, true), new ComplexData(-2d, -3d));
        assertEquals(XCalculationUtils.multiply(StringData.valueOf("2"), cd, true), new ComplexData(8d, 6d));
        assertEquals(XCalculationUtils.divide(StringData.valueOf("2"), cd, true), new ComplexData(0.32d, -0.24d));
        
        assertEquals(XCalculationUtils.add(cd, cd2, true, true), new ComplexData(6d, 7d));
        assertEquals(XCalculationUtils.subtract(cd, cd2, true), new ComplexData(2d, -1d));
        assertEquals(XCalculationUtils.multiply(cd, cd2, true), new ComplexData(-4d, 22d));
        
        assertEquals(XCalculationUtils.add(cd, sd2, true, true), new ComplexData(6d, 7d));
        assertEquals(XCalculationUtils.subtract(cd, sd2, true), new ComplexData(2d, -1d));
        assertEquals(XCalculationUtils.multiply(cd, sd2, true), new ComplexData(-4d, 22d));
        
        assertEquals(XCalculationUtils.add(sd, sd2, true, true), new ComplexData(6d, 7d));
        assertEquals(XCalculationUtils.add(sd, cd2, true, true), new ComplexData(6d, 7d));
        assertEquals(XCalculationUtils.subtract(sd, cd2, true), new ComplexData(2d, -1d));
        assertEquals(XCalculationUtils.multiply(sd, cd2, true), new ComplexData(-4d, 22d));
    }
    
    @Test
    public void testZero() throws EngineException {
        assertEquals(XCalculationUtils.zero(s1).getType(), Types.INT);
        assertEquals(XCalculationUtils.zero(s1).toInt().intValue(), 0);
        assertEquals(XCalculationUtils.multiply(s1, IntData.NULL, true).toInt().intValue(), 0);
    }
    
    @Test
    public void testIntLong() throws EngineException {
        //int
        assertEquals(XCalculationUtils.add(i1, i1, true, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.add(i1, i1, true, true).toInt().intValue(), 2);
        assertEquals(XCalculationUtils.subtract(i1, i1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.subtract(i1, i1, true).toInt().intValue(), 0);
        assertEquals(XCalculationUtils.multiply(i1, i1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.multiply(i1, i1, true).toInt().intValue(), 1);
        assertEquals(XCalculationUtils.divide(i1, i1, true).getType(), Types.LONG);
        assertEquals(round2(XCalculationUtils.divide(i1, i1, true).toDouble()), Double.valueOf(1.00d));
        
        // long
        assertEquals(XCalculationUtils.add(i1, l1, true, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.add(i1, l1, true, true).toLong(), Long.valueOf(5l));
        assertEquals(XCalculationUtils.subtract(i1, l1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.subtract(i1, l1, true).toLong(), Long.valueOf(-3l));
        assertEquals(XCalculationUtils.multiply(i1, l1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.multiply(i1, l1, true).toLong(), Long.valueOf(4l));
        assertEquals(XCalculationUtils.divide(i1, l1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(i1, l1, true).toDouble()), Double.valueOf(0.25d));
        
        //float
        assertEquals(XCalculationUtils.add(i1, f1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(i1,  f1, true, true).toDouble()), Double.valueOf(6.0d));
        assertEquals(XCalculationUtils.subtract(i1, f1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(i1, f1, true).toDouble(), Double.valueOf(-4.0d));
        assertEquals(XCalculationUtils.multiply(i1, f1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(i1, f1, true).toDouble(), Double.valueOf(5.0d));
        assertEquals(XCalculationUtils.divide(i1, f1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(i1, f1, true).toDouble()), Double.valueOf(0.20d));
        
        //double
        assertEquals(XCalculationUtils.add(i1,  d1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(i1,  d1, true, true).toDouble()), Double.valueOf(11.00d));
        assertEquals(XCalculationUtils.subtract(i1, d1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(i1, d1, true).toDouble(), Double.valueOf(-9.0d));
        assertEquals(XCalculationUtils.multiply(i1, d1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(i1, d1, true).toDouble(), Double.valueOf(10.0d));
        assertEquals(XCalculationUtils.divide(i1, d1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(i1, d1, true).toDouble()), Double.valueOf(0.10d));
        
        //numeric
        assertEquals(XCalculationUtils.add(i1,  n1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(i1,  n1, true, true).toDouble()), Double.valueOf(101.00d));
        assertEquals(XCalculationUtils.subtract(i1, n1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(i1, n1, true).toDouble(), Double.valueOf(-99.0d));
        assertEquals(XCalculationUtils.multiply(i1, n1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(i1, n1, true).toDouble(), Double.valueOf(100.0d));
        assertEquals(XCalculationUtils.divide(i1, n1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(i1, n1, true).toDouble()), Double.valueOf(0.01d));
        
        //decimal
        assertEquals(XCalculationUtils.add(i1,  bd1, true, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.add(i1,  bd1, true, true).toDecimal(), new BigDecimal("0"));
        assertEquals(XCalculationUtils.subtract(i1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(i1, bd1, true).toDouble(), Double.valueOf(2d));
        assertEquals(XCalculationUtils.multiply(i1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(i1, bd1, true).toDouble(), Double.valueOf(-1d));
        assertEquals(XCalculationUtils.divide(i1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(i1, bd1, true).toDouble()), Double.valueOf(-1.00d));
        
        //string1
        assertEquals(XCalculationUtils.add(i1,  s1, true, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.add(i1,  s1, true, true).toLong(), Long.valueOf(6l));
        assertEquals(XCalculationUtils.subtract(i1, s1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.subtract(i1, s1, true).toLong(), Long.valueOf(-4l));
        assertEquals(XCalculationUtils.multiply(i1, s1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.multiply(i1, s1, true).toLong(), Long.valueOf(5l));
        assertEquals(XCalculationUtils.divide(i1, s1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(i1, s1, true).toDouble()), Double.valueOf(0.20d));
        
        //string2
        assertEquals(XCalculationUtils.add(i1,  s2, true, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.add(i1,  s2, true, true).toDecimal(), new BigDecimal("6.0"));
        assertEquals(XCalculationUtils.subtract(i1, s2, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(i1, s2, true).toDouble(), Double.valueOf(-4d));
        assertEquals(XCalculationUtils.multiply(i1, s2, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(i1, s2, true).toDouble(), Double.valueOf(5d));
        assertEquals(XCalculationUtils.divide(i1, s2, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(i1, s2, true).toDouble()), Double.valueOf(0.20d));
    }
    
    @Test
    public void testFloatDouble() throws EngineException {
        //int
        assertEquals(XCalculationUtils.add(d1, i1, true, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.add(d1, i1, true, true).toDouble(), Double.valueOf(11d));
        assertEquals(XCalculationUtils.subtract(d1, i1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(d1, i1, true).toDouble(), Double.valueOf(9d));
        assertEquals(XCalculationUtils.multiply(d1, i1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(d1, i1, true).toDouble(), Double.valueOf(10d));
        assertEquals(XCalculationUtils.divide(d1, i1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(d1, i1, true).toDouble()), Double.valueOf(10.00d));
        
        // long
        assertEquals(XCalculationUtils.add(d1, l1, true, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.add(d1, l1, true, true).toDouble(), Double.valueOf(14l));
        assertEquals(XCalculationUtils.subtract(d1, l1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(d1, l1, true).toDouble(), Double.valueOf(6d));
        assertEquals(XCalculationUtils.multiply(d1, l1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(d1, l1, true).toDouble(), Double.valueOf(40d));
        assertEquals(XCalculationUtils.divide(d1, l1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(d1, l1, true).toDouble()), Double.valueOf(2.50d));
        
        //float
        assertEquals(XCalculationUtils.add(d1, f1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(d1, f1, true, true).toDouble()), Double.valueOf(15.0d));
        assertEquals(XCalculationUtils.subtract(d1, f1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(d1, f1, true).toDouble(), Double.valueOf(5.0d));
        assertEquals(XCalculationUtils.multiply(d1, f1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(d1, f1, true).toDouble(), Double.valueOf(50.0d));
        assertEquals(XCalculationUtils.divide(d1, f1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(d1, f1, true).toDouble()), Double.valueOf(2.00d));
        
        //double
        assertEquals(XCalculationUtils.add(d1, d1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(d1, d1, true, true).toDouble()), Double.valueOf(20.00d));
        assertEquals(XCalculationUtils.subtract(d1, d1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(d1, d1, true).toDouble(), Double.valueOf(0d));
        assertEquals(XCalculationUtils.multiply(d1, d1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(d1, d1, true).toDouble(), Double.valueOf(100.0d));
        assertEquals(XCalculationUtils.divide(d1, d1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(d1, d1, true).toDouble()), Double.valueOf(1.00d));
        
        //numeric
        assertEquals(XCalculationUtils.add(d1, n1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(d1, n1, true, true).toDouble()), Double.valueOf(110.00d));
        assertEquals(XCalculationUtils.subtract(d1, n1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(d1, n1, true).toDouble(), Double.valueOf(-90d));
        assertEquals(XCalculationUtils.multiply(d1, n1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(d1, n1, true).toDouble(), Double.valueOf(1000.0d));
        assertEquals(XCalculationUtils.divide(d1, n1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(d1, n1, true).toDouble()), Double.valueOf(0.10d));
        
        //decimal
        assertEquals(XCalculationUtils.add(d1, bd1, true, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.add(d1, bd1, true, true).toDouble(), Double.valueOf(9.0d));
        assertEquals(XCalculationUtils.subtract(d1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(d1, bd1, true).toDouble(), Double.valueOf(11.0d));
        assertEquals(XCalculationUtils.multiply(d1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(d1, bd1, true).toDouble(), Double.valueOf(-10d));
        assertEquals(XCalculationUtils.divide(d1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(d1, bd1, true).toDouble()), Double.valueOf(-10.00d));
        
        //string1
        assertEquals(XCalculationUtils.add(d1, s1, true, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.add(d1, s1, true, true).toDouble(), Double.valueOf(15d));
        assertEquals(XCalculationUtils.subtract(d1, s1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(d1, s1, true).toDouble(), Double.valueOf(5d));
        assertEquals(XCalculationUtils.multiply(d1, s1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(d1, s1, true).toDouble(), Double.valueOf(50d));
        assertEquals(XCalculationUtils.divide(d1, s1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(d1, s1, true).toDouble()), Double.valueOf(2.00d));
        
        //string2
        assertEquals(XCalculationUtils.add(d1, s2, true, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.add(d1, s2, true, true).toDouble(), Double.valueOf(15d));
        assertEquals(XCalculationUtils.subtract(d1, s2, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(d1, s2, true).toDouble(), Double.valueOf(5d));
        assertEquals(XCalculationUtils.multiply(d1, s2, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(d1, s2, true).toDouble(), Double.valueOf(50d));
        assertEquals(XCalculationUtils.divide(d1, s2, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(d1, s2, true).toDouble()), Double.valueOf(2.00d));
    }

    @Test
    public void testDecimal() throws EngineException {
        //int
        assertEquals(XCalculationUtils.add(bd1, i1, true, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.add(bd1, i1, true, true).toDouble(), Double.valueOf(0d));
        assertEquals(XCalculationUtils.subtract(bd1, i1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(bd1, i1, true).toDouble(), Double.valueOf(-2d));
        assertEquals(XCalculationUtils.multiply(bd1, i1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(bd1, i1, true).toDouble(), Double.valueOf(-1d));
        assertEquals(XCalculationUtils.divide(bd1, i1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(bd1, i1, true).toDouble()), Double.valueOf(-1.00d));
        
        // long
        assertEquals(XCalculationUtils.add(bd1, l1, true, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.add(bd1, l1, true, true).toDouble(), Double.valueOf(3l));
        assertEquals(XCalculationUtils.subtract(bd1, l1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(bd1, l1, true).toDouble(), Double.valueOf(-5d));
        assertEquals(XCalculationUtils.multiply(bd1, l1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(bd1, l1, true).toDouble(), Double.valueOf(-4d));
        assertEquals(XCalculationUtils.divide(bd1, l1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(bd1, l1, true).toDouble()), Double.valueOf(-0.25d));
        
        //float
        assertEquals(XCalculationUtils.add(bd1, f1, true, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.add(bd1, f1, true, true).toDouble()), Double.valueOf(4.0d));
        assertEquals(XCalculationUtils.subtract(bd1, f1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(bd1, f1, true).toDouble(), Double.valueOf(-6.0d));
        assertEquals(XCalculationUtils.multiply(bd1, f1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(bd1, f1, true).toDouble(), Double.valueOf(-5.0d));
        assertEquals(XCalculationUtils.divide(bd1, f1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(bd1, f1, true).toDouble()), Double.valueOf(-0.20d));
        
        //double
        assertEquals(XCalculationUtils.add(bd1, d1, true, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.add(bd1, d1, true, true).toDouble()), Double.valueOf(9.0d));
        assertEquals(XCalculationUtils.subtract(bd1, d1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(bd1, d1, true).toDouble(), Double.valueOf(-11.0d));
        assertEquals(XCalculationUtils.multiply(bd1, d1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(bd1, d1, true).toDouble(), Double.valueOf(-10.0d));
        assertEquals(XCalculationUtils.divide(bd1, d1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(bd1, d1, true).toDouble()), Double.valueOf(-0.10d));
        
        //numeric
        assertEquals(XCalculationUtils.add(bd1, n1, true, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.add(bd1, n1, true, true).toDouble()), Double.valueOf(99.0d));
        assertEquals(XCalculationUtils.subtract(bd1, n1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(bd1, n1, true).toDouble(), Double.valueOf(-101.0d));
        assertEquals(XCalculationUtils.multiply(bd1, n1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(bd1, n1, true).toDouble(), Double.valueOf(-100.0d));
        assertEquals(XCalculationUtils.divide(bd1, n1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(bd1, n1, true).toDouble()), Double.valueOf(-0.01d));
        
        //decimal
        assertEquals(XCalculationUtils.add(bd1, bd1, true, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.add(bd1, bd1, true, true).toDouble(), Double.valueOf(-2.0d));
        assertEquals(XCalculationUtils.subtract(bd1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(bd1, bd1, true).toDouble(), Double.valueOf(0.0d));
        assertEquals(XCalculationUtils.multiply(bd1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(bd1, bd1, true).toDouble(), Double.valueOf(1d));
        assertEquals(XCalculationUtils.divide(bd1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(bd1, bd1, true).toDouble()), Double.valueOf(1.00d));
        
        //string1
        assertEquals(XCalculationUtils.add(bd1, s1, true, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.add(bd1, s1, true, true).toDouble(), Double.valueOf(4d));
        assertEquals(XCalculationUtils.subtract(bd1, s1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(bd1, s1, true).toDouble(), Double.valueOf(-6d));
        assertEquals(XCalculationUtils.multiply(bd1, s1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(bd1, s1, true).toDouble(), Double.valueOf(-5d));
        assertEquals(XCalculationUtils.divide(bd1, s1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(bd1, s1, true).toDouble()), Double.valueOf(-0.20d));
        
        //string2
        assertEquals(XCalculationUtils.add(bd1, s2, true, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.add(bd1, s2, true, true).toDouble(), Double.valueOf(4.0d));
        assertEquals(XCalculationUtils.subtract(bd1, s2, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(bd1, s2, true).toDouble(), Double.valueOf(-6.0d));
        assertEquals(XCalculationUtils.multiply(bd1, s2, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(bd1, s2, true).toDouble(), Double.valueOf(-5.0d));
        assertEquals(XCalculationUtils.divide(bd1, s2, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(bd1, s2, true).toDouble()), Double.valueOf(-0.20d));
    }
    
    @Test
    public void testString1() throws EngineException {
        //int
        assertEquals(XCalculationUtils.add(s1, i1, true, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.add(s1, i1, true, true).toInt().intValue(), 6);
        assertEquals(XCalculationUtils.subtract(s1, i1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.subtract(s1, i1, true).toInt().intValue(), 4);
        assertEquals(XCalculationUtils.multiply(s1, i1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.multiply(s1, i1, true).toLong(), Long.valueOf(5l));
        assertEquals(XCalculationUtils.divide(s1, i1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s1, i1, true).toDouble()), Double.valueOf(5.00d));
        
        // long
        assertEquals(XCalculationUtils.add(s1, l1, true, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.add(s1, l1, true, true).toLong(), Long.valueOf(9l));
        assertEquals(XCalculationUtils.subtract(s1, l1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.subtract(s1, l1, true).toLong(), Long.valueOf(1l));
        assertEquals(XCalculationUtils.multiply(s1, l1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.multiply(s1, l1, true).toLong(), Long.valueOf(20l));
        assertEquals(XCalculationUtils.divide(s1, l1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s1, l1, true).toDouble()), Double.valueOf(1.25d));
        
        //float
        assertEquals(XCalculationUtils.add(s1, f1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(s1, f1, true, true).toDouble()), Double.valueOf(10.0d));
        assertEquals(XCalculationUtils.subtract(s1, f1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s1, f1, true).toDouble(), Double.valueOf(0.0d));
        assertEquals(XCalculationUtils.multiply(s1, f1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s1, f1, true).toDouble(), Double.valueOf(25.0d));
        assertEquals(XCalculationUtils.divide(s1, f1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s1, f1, true).toDouble()), Double.valueOf(1.00d));
        
        //double
        assertEquals(XCalculationUtils.add(s1, d1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(s1, d1, true, true).toDouble()), Double.valueOf(15.00d));
        assertEquals(XCalculationUtils.subtract(s1, d1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s1, d1, true).toDouble(), Double.valueOf(-5.0d));
        assertEquals(XCalculationUtils.multiply(s1, d1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s1, d1, true).toDouble(), Double.valueOf(50d));
        assertEquals(XCalculationUtils.divide(s1, d1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s1, d1, true).toDouble()), Double.valueOf(0.50d));
        
        //numeric
        assertEquals(XCalculationUtils.add(s1, n1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(s1, n1, true, true).toDouble()), Double.valueOf(105.00d));
        assertEquals(XCalculationUtils.subtract(s1, n1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s1, n1, true).toDouble(), Double.valueOf(-95.0d));
        assertEquals(XCalculationUtils.multiply(s1, n1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s1, n1, true).toDouble(), Double.valueOf(500d));
        assertEquals(XCalculationUtils.divide(s1, n1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s1, n1, true).toDouble()), Double.valueOf(0.05d));
        
        //decimal
        assertEquals(XCalculationUtils.add(s1, bd1, true, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.add(s1, bd1, true, true).toDouble(), Double.valueOf(4d));
        assertEquals(XCalculationUtils.subtract(s1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(s1, bd1, true).toDouble(), Double.valueOf(6d));
        assertEquals(XCalculationUtils.multiply(s1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(s1, bd1, true).toDouble(), Double.valueOf(-5d));
        assertEquals(XCalculationUtils.divide(s1, bd1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(s1, bd1, true).toDouble()), Double.valueOf(-5.00d));
        
        //string1
        assertEquals(XCalculationUtils.add(s1, s1, true, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.add(s1, s1, true, true).toLong(), Long.valueOf(10l));
        assertEquals(XCalculationUtils.subtract(s1, s1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.subtract(s1, s1, true).toLong(), Long.valueOf(0l));
        assertEquals(XCalculationUtils.multiply(s1, s1, true).getType(), Types.LONG);
        assertEquals(XCalculationUtils.multiply(s1, s1, true).toLong(), Long.valueOf(25l));
        assertEquals(XCalculationUtils.divide(s1, s1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s1, s1, true).toDouble()), Double.valueOf(1.00d));
        
        //string2
        assertEquals(XCalculationUtils.add(s1, s2, true, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.add(s1, s2, true, true).toDouble(), Double.valueOf(10.0d));
        assertEquals(XCalculationUtils.subtract(s1, s2, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s1, s2, true).toDouble(), Double.valueOf(0.0d));
        assertEquals(XCalculationUtils.multiply(s1, s2, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s1, s2, true).toDouble(), Double.valueOf(25.0d));
        assertEquals(XCalculationUtils.divide(s1, s2, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s1, s2, true).toDouble()), Double.valueOf(1.00d));
    }
    
    @Test
    public void testString2() throws EngineException {
        //int
        assertEquals(XCalculationUtils.add(s2, i1, true, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.add(s2, i1, true, true).toDouble(), Double.valueOf(6.0d));
        assertEquals(XCalculationUtils.subtract(s2, i1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s2, i1, true).toDouble(), Double.valueOf(4.0d));
        assertEquals(XCalculationUtils.multiply(s2, i1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s2, i1, true).toDouble(), Double.valueOf(5.0d));
        assertEquals(XCalculationUtils.divide(s2, i1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s2, i1, true).toDouble()), Double.valueOf(5.00d));
        
        // long
        assertEquals(XCalculationUtils.add(s2, l1, true, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.add(s2, l1, true, true).toDouble(), Double.valueOf(9.0d));
        assertEquals(XCalculationUtils.subtract(s2, l1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s2, l1, true).toDouble(), Double.valueOf(1.0d));
        assertEquals(XCalculationUtils.multiply(s2, l1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s2, l1, true).toDouble(), Double.valueOf(20.0d));
        assertEquals(XCalculationUtils.divide(s2, l1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s2, l1, true).toDouble()), Double.valueOf(1.25d));
        
        //float
        assertEquals(XCalculationUtils.add(s2, f1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(s2, f1, true, true).toDouble()), Double.valueOf(10.0d));
        assertEquals(XCalculationUtils.subtract(s2, f1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s2, f1, true).toDouble(), Double.valueOf(0.0d));
        assertEquals(XCalculationUtils.multiply(s2, f1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s2, f1, true).toDouble(), Double.valueOf(25.0d));
        assertEquals(XCalculationUtils.divide(s2, f1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s2, f1, true).toDouble()), Double.valueOf(1.00d));
        
        //double
        assertEquals(XCalculationUtils.add(s2, d1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(s2, d1, true, true).toDouble()), Double.valueOf(15.00d));
        assertEquals(XCalculationUtils.subtract(s2, d1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s2, d1, true).toDouble(), Double.valueOf(-5.0d));
        assertEquals(XCalculationUtils.multiply(s2, d1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s2, d1, true).toDouble(), Double.valueOf(50d));
        assertEquals(XCalculationUtils.divide(s2, d1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s2, d1, true).toDouble()), Double.valueOf(0.50d));
        
        //numeric
        assertEquals(XCalculationUtils.add(s2, n1, true, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.add(s2, n1, true, true).toDouble()), Double.valueOf(105.00d));
        assertEquals(XCalculationUtils.subtract(s2, n1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s2, n1, true).toDouble(), Double.valueOf(-95.0d));
        assertEquals(XCalculationUtils.multiply(s2, n1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s2, n1, true).toDouble(), Double.valueOf(500d));
        assertEquals(XCalculationUtils.divide(s2, n1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s2, n1, true).toDouble()), Double.valueOf(0.05d));
        
        //decimal
        assertEquals(XCalculationUtils.add(s2, bd1, true, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.add(s2, bd1, true, true).toDouble(), Double.valueOf(4d));
        assertEquals(XCalculationUtils.subtract(s2, bd1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.subtract(s2, bd1, true).toDouble(), Double.valueOf(6d));
        assertEquals(XCalculationUtils.multiply(s2, bd1, true).getType(), Types.DECIMAL);
        assertEquals(XCalculationUtils.multiply(s2, bd1, true).toDouble(), Double.valueOf(-5d));
        assertEquals(XCalculationUtils.divide(s2, bd1, true).getType(), Types.DECIMAL);
        assertEquals(round2(XCalculationUtils.divide(s2, bd1, true).toDouble()), Double.valueOf(-5.00d));
        
        //string1
        assertEquals(XCalculationUtils.add(s2, s1, true, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.add(s2, s1, true, true).toDouble(), Double.valueOf(10.0d));
        assertEquals(XCalculationUtils.subtract(s2, s1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s2, s1, true).toDouble(), Double.valueOf(0l));
        assertEquals(XCalculationUtils.multiply(s2, s1, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s2, s1, true).toDouble(), Double.valueOf(25l));
        assertEquals(XCalculationUtils.divide(s2, s1, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s2, s1, true).toDouble()), Double.valueOf(1.00d));
        
        //string2
        assertEquals(XCalculationUtils.add(s2, s2, true, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.add(s2, s2, true, true).toDouble(), Double.valueOf(10.0d));
        assertEquals(XCalculationUtils.subtract(s2, s2, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.subtract(s2, s2, true).toDouble(), Double.valueOf(0.0d));
        assertEquals(XCalculationUtils.multiply(s2, s2, true).getType(), Types.DOUBLE);
        assertEquals(XCalculationUtils.multiply(s2, s2, true).toDouble(), Double.valueOf(25.0d));
        assertEquals(XCalculationUtils.divide(s2, s2, true).getType(), Types.DOUBLE);
        assertEquals(round2(XCalculationUtils.divide(s2, s2, true).toDouble()), Double.valueOf(1.00d));
    }
    
    private Double round2(double d) {
        return Math.round(d * 100) / 100.0d;
    }
}
