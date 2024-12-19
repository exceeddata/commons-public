package com.exceeddata.ac.common.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author liuqiang
 * @since 7/24/20 3:52 PM
 */
public final class Complex implements Comparator<Complex>, Serializable {
    private static final long serialVersionUID = 1L;
    
    private double real;
    private double imag;

    public Complex() {
    }

    public Complex(final double real, final double imag) {
        this.real = real;
        this.imag = imag;
    }

    public Complex minus(final Complex other) {
        return new Complex(this.real - other.real, this.imag - other.imag);
    }

    public double mod() {
        return Math.sqrt(this.real * this.real + this.imag * this.imag);
    }

    public Complex multiply(final Complex other) {
        return new Complex(this.real * other.real - this.imag * other.imag,
                this.real * other.imag + this.imag * other.real);
    }

    public Complex multiply(final double other) {
        return new Complex(this.real * other, this.imag * other);
    }

    public Complex divide(final double other) {
        return new Complex(this.real / other, this.imag / other);
    }

    /*
    (a+ib)/(c+id)
        =(ac+bd)/(c^2+d^2) + i(bc-ad)/(c^2+d^2)
     */
    public Complex divide(final Complex other) {
        final double a = this.real;
        final double b = this.imag;
        final double c = other.real;
        final double d = other.imag;

        final Complex res = new Complex();
        res.real = (a * c + b * d) / (c * c + d * d);
        res.imag = (b * c - a * d) / (c * c + d * d);
        return res;
    }

    public Complex plus(final Complex other) {
        return new Complex(this.real + other.real, this.imag + other.imag);
    }

    public Complex negative() {
        return new Complex(-this.real, -this.imag);
    }

    public Complex conj() {
        return new Complex(real, -imag);
    }

    public static Complex conj(final Complex complex) {
        return new Complex(complex.real, -complex.imag);
    }

    public static Complex[] conj(final Complex[] complex) {
        final int len = complex.length;
        final Complex[] res = new Complex[len];

        for (int i = 0; i < len; ++i) {
            res[i] = conj(complex[i]);
        }
        return res;
    }

    public static Complex sin(final Complex src) {
        return new Complex(1d, 0d).divide(new Complex(0d, 2d)).multiply(
                exp(new Complex(0d, 1d).multiply(src)).minus(exp(new Complex(0d, -1d).multiply(src))));
    }

    public static Complex cos(final Complex src) {
        return exp(new Complex(0d, 1d).multiply(src)).plus(exp(new Complex(0d, -1d).multiply(src))).multiply(0.5);
    }

    public static Complex acos(final Complex src) {
        return new Complex(0, -1d).multiply(
                log(
                        new Complex(0, 1d).multiply(sqrt(new Complex(1d, 0d).minus(src.multiply(src)))).plus(src)
                )
        );
    }

    public static Complex sqrt(final Complex src) {
        return exp(new Complex(0.5, 0d).multiply(log(src)));
    }

    public static Complex exp(final Complex src) {
        final double x = src.getReal();
        final double y = src.getImag();

        final double e = Math.exp(x);
        return new Complex(e * Math.cos(y), e * Math.sin(y));
    }

    public static Complex log(final Complex src) {
        final double a = src.getReal();
        final double b = src.getImag();
        final double re = Math.log(a * a + b * b) / 2.;
        final double im = Math.atan(b / a);

        return new Complex(re, im);
    }

    /**
     * exp(ix) = cos(x) + i*sin(x)
     *
     * @return complex
     */
    public static Complex euler(final double x) {
        return new Complex(Math.cos(x), Math.sin(x));
    }

    public static Complex[] euler(final double[] x) {
        final int len = x.length;
        final Complex[] res = new Complex[len];

        for (int i = 0; i < len; ++i) {
            res[i] = euler(x[i]);
        }

        return res;
    }

    public static Complex prod(final Complex[] array) {
        Complex res = new Complex(1d, 1d);
        for (final Complex v : array) {
            res = res.multiply(v);
        }
        return res;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImag() {
        return imag;
    }

    public void setImag(double imag) {
        this.imag = imag;
    }

    @Override
    public String toString() {
        return "Complex{" +
                "real=" + real +
                ", imag=" + imag +
                '}';
    }

    @Override
    public int compare(Complex o1, Complex o2) {
        double res = 0d;

        res = o1.real - o2.real;
        if (res == 0d) {
            double absImag1 = Math.abs(o1.imag);
            double absImag2 = Math.abs(o2.imag);
            res = -absImag1 + absImag2;

            if (res == 0d) {
                res = o1.imag - o2.imag;
            }
        }

        return Double.compare(res, 0d);
    }

}
