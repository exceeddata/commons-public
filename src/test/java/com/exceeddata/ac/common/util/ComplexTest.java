package com.exceeddata.ac.common.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.exceeddata.ac.common.data.typedata.ComplexData;

/**
 * @author liuqiang
 * @since 7/25/20 5:35 PM
 */
public class ComplexTest {
    private static final double DELTA = Math.pow(10, -6);

    @Test
    public void testDivide() {
        Complex complex1 = new Complex(1, 2);
        Complex complex2 = new Complex(2, 3);

        Complex divide = complex1.divide(complex2);

        assertEquals(0.615384615384615, divide.getReal(), DELTA);
        assertEquals(0.076923076923077, divide.getImag(), DELTA);

    }

    @Test
    public void testSort() {
        Complex c1 = new Complex(1, 2);
        Complex c2 = new Complex(2, 0);
        Complex c3 = new Complex(3, 0);
        Complex c4 = new Complex(1, -2);
        Complex c5 = new Complex(4, 5);
        Complex c6 = new Complex(4, -5);
        Complex c7 = new Complex(-2, 3);
        Complex c8 = new Complex(-2, -3);
        Complex c9 = new Complex(-2, 0);
        Complex c10 = new Complex(-2, 0);
        Complex c11 = new Complex(1, -3);
        Complex c12 = new Complex(1, 3);

        List<Complex> list = new ArrayList<Complex>();
        list.add(c1);
        list.add(c2);
        list.add(c3);
        list.add(c4);
        list.add(c5);
        list.add(c6);
        list.add(c7);
        list.add(c8);
        list.add(c9);
        list.add(c10);
        list.add(c11);
        list.add(c12);

        list.sort(new Complex());

        assertArrayEquals(new double[]{-2, -3}, new double[]{list.get(0).getReal(), list.get(0).getImag()}, DELTA);
        assertArrayEquals(new double[]{-2, 3}, new double[]{list.get(1).getReal(), list.get(1).getImag()}, DELTA);
        assertArrayEquals(new double[]{-2, 0}, new double[]{list.get(2).getReal(), list.get(2).getImag()}, DELTA);
        assertArrayEquals(new double[]{-2, 0}, new double[]{list.get(3).getReal(), list.get(3).getImag()}, DELTA);
        assertArrayEquals(new double[]{1, -3}, new double[]{list.get(4).getReal(), list.get(4).getImag()}, DELTA);
        assertArrayEquals(new double[]{1, 3}, new double[]{list.get(5).getReal(), list.get(5).getImag()}, DELTA);
        assertArrayEquals(new double[]{1, -2}, new double[]{list.get(6).getReal(), list.get(6).getImag()}, DELTA);
        assertArrayEquals(new double[]{1, 2}, new double[]{list.get(7).getReal(), list.get(7).getImag()}, DELTA);
        assertArrayEquals(new double[]{2, 0}, new double[]{list.get(8).getReal(), list.get(8).getImag()}, DELTA);
        assertArrayEquals(new double[]{3, 0}, new double[]{list.get(9).getReal(), list.get(9).getImag()}, DELTA);
        assertArrayEquals(new double[]{4, -5}, new double[]{list.get(10).getReal(), list.get(10).getImag()}, DELTA);
        assertArrayEquals(new double[]{4, 5}, new double[]{list.get(11).getReal(), list.get(11).getImag()}, DELTA);


    }

    @Test
    public void testAcos() {
        Complex acos = Complex.acos(new Complex(1, 2));
        assertArrayEquals(new double[]{
                1.14371774040242, -1.52857091948100
        }, new double[]{acos.getReal(), acos.getImag()}, DELTA);
    }

    @Test
    public void testCos() {
        Complex cos = Complex.cos(new Complex(3, 1.96522054313967));
        assertArrayEquals(new double[]{
                -3.60189529350163, -0.493663284352946
        }, new double[]{cos.getReal(), cos.getImag()}, DELTA);
    }

    @Test
    public void testSin() {
        Complex res = Complex.sin(new Complex(1, 2));
        assertArrayEquals(new double[]{
                3.16577851321617, +1.95960104142161
        }, new double[]{res.getReal(), res.getImag()}, DELTA);
    }

    @Test
    public void testComplexCheck() {
        //valid complex
        assertEquals(XComplexUtils.isComplexNumber("i"), true);
        assertEquals(XComplexUtils.isComplexNumber("+i"), true);
        assertEquals(XComplexUtils.isComplexNumber("-I"), true);
        assertEquals(XComplexUtils.isComplexNumber("+1I"), true);
        assertEquals(XComplexUtils.isComplexNumber("-2.0i"), true);
        assertEquals(XComplexUtils.isComplexNumber("-2E5i"), true);
        assertEquals(XComplexUtils.isComplexNumber("-2.324e-5i"), true);
        assertEquals(XComplexUtils.isComplexNumber("2+i"), true);
        assertEquals(XComplexUtils.isComplexNumber("-2-2i"), true);
        assertEquals(XComplexUtils.isComplexNumber("2 + i"), true);
        assertEquals(XComplexUtils.isComplexNumber("2 - 5i"), true);
        assertEquals(XComplexUtils.isComplexNumber("-2.324e-2+5E-3i"), true);
        assertEquals(XComplexUtils.isComplexNumber("-2.324e02 - 5.0i"), true);
        assertEquals(XComplexUtils.isComplexNumber("-2.324e-2 + 5E-3i"), true);
        
        //valid number but not complex
        assertEquals(XComplexUtils.isComplexNumber("127"), true);
        assertEquals(XComplexUtils.isComplexNumber("127.0"), true);
        assertEquals(XComplexUtils.isComplexNumber("-127E-3"), true);
        assertEquals(XComplexUtils.isComplexNumber("-127.32E-3"), true);
        
        //invalid real
        assertEquals(XComplexUtils.isComplexNumber("+"), false);
        assertEquals(XComplexUtils.isComplexNumber("+ -2i"), false);
        assertEquals(XComplexUtils.isComplexNumber("-"), false);
        assertEquals(XComplexUtils.isComplexNumber("127.2.0"), false);
        assertEquals(XComplexUtils.isComplexNumber("127.2.0 +"), false);
        assertEquals(XComplexUtils.isComplexNumber("127.2.0 - 2i"), false);
        assertEquals(XComplexUtils.isComplexNumber("127E-2E-2 - 2i"), false);
        
        //invalid imag
        assertEquals(XComplexUtils.isComplexNumber("-127 +"), false);
        assertEquals(XComplexUtils.isComplexNumber("-127 - 5"), false);
        assertEquals(XComplexUtils.isComplexNumber("-127 - 12.0.0i"), false);
        assertEquals(XComplexUtils.isComplexNumber("-127.35 - 12.0"), false);
        assertEquals(XComplexUtils.isComplexNumber("-I3"), false);
        assertEquals(XComplexUtils.isComplexNumber("-Ie"), false);
        
        //invalid number
        assertEquals(XComplexUtils.isComplexNumber("-127m"), false);
        assertEquals(XComplexUtils.isComplexNumber("-127m + 3mi"), false);
    }

    @Test
    public void testComplexData() {
        //valid complex
        assertEquals(new ComplexData("i").toString(), "0+1i");
        assertEquals(new ComplexData("+i").toString(), "0+1i");
        assertEquals(new ComplexData("-I").toString(), "0-1i");
        assertEquals(new ComplexData("+1I").toString(), "0+1i");
        assertEquals(new ComplexData("-2.0i").toString(), "0-2i");
        assertEquals(new ComplexData("-2E5i").toString(), "0-200000i");
        assertEquals(new ComplexData("-2.324e-5i").toString(), "0-2.324E-5i");
        assertEquals(new ComplexData("2+i").toString(), "2+1i");
        assertEquals(new ComplexData("-2-2i").toString(), "-2-2i");
        assertEquals(new ComplexData("2 + i").toString(), "2+1i");
        assertEquals(new ComplexData("2 - 5i").toString(), "2-5i");
        assertEquals(new ComplexData("-2.324e-2+5E-3i").toString(), "-0.02324+0.005i");
        assertEquals(new ComplexData("-2.324e02 - 5.0i").toString(), "-232.4-5i");
        assertEquals(new ComplexData("-2.324e-2 + 5E-3i").toString(), "-0.02324+0.005i");
        
        //valid number but not complex
        assertEquals(new ComplexData("5").toString(), "5+0i");
        assertEquals(new ComplexData("127").toString(), "127+0i");
        assertEquals(new ComplexData("127.0").toString(), "127+0i");
        assertEquals(new ComplexData("-127E-3").toString(), "-0.127+0i");
        assertEquals(new ComplexData("-127.32E-3").toString(), "-0.12732+0i");
        
        //invalid real
        assertEquals(new ComplexData("+").toString(), "");
        assertEquals(new ComplexData("+ -2i").toString(), "");
        assertEquals(new ComplexData("-").toString(), "");
        assertEquals(new ComplexData("127.2.0").toString(), "");
        assertEquals(new ComplexData("127.2.0 +").toString(), "");
        assertEquals(new ComplexData("127.2.0 - 2i").toString(), "");
        assertEquals(new ComplexData("127E-2E-2 - 2i").toString(), "");
        
        //invalid imag
        assertEquals(new ComplexData("-127 +").toString(), "");
        assertEquals(new ComplexData("-127 - 5").toString(), "");
        assertEquals(new ComplexData("-127 - 12.0.0i").toString(), "");
        assertEquals(new ComplexData("-127.35 - 12.0").toString(), "");
        assertEquals(new ComplexData("-I3").toString(), "");
        assertEquals(new ComplexData("-Ie").toString(), "");
        
        //invalid number
        assertEquals(new ComplexData("-127m").toString(), "");
        assertEquals(new ComplexData("-127 + 3mi").toString(), "");
        
    }

}