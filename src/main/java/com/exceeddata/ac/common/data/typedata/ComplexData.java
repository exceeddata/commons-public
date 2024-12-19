package com.exceeddata.ac.common.data.typedata;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.util.XStringUtils;

/**
 * A data class for Complex numbers.
 *
 */
public final class ComplexData implements TypeData {
    private static final long serialVersionUID = 1L;
    
    public static final ComplexData NULL = new ComplexData();
    public static final ComplexData ZERO = new ComplexData(0d, 0d, false);
    public static final ComplexData ONE = new ComplexData(1d, 0d, false);
    public static final ComplexData NEGATIVE_ONE = new ComplexData(-1d, 0d, false);
    public static final ComplexData POSITIVE_INFINITY = new ComplexData(Double.POSITIVE_INFINITY);
    public static final ComplexData NEGATIVE_INFINITY = new ComplexData(Double.NEGATIVE_INFINITY);
    
    protected boolean isnull = true;
    protected double real = 0d;
    protected double imag = 0d;
    
    public ComplexData() {
    }
    
    /**
     * Construct a <code>ComplexData</code> with Double real portion
     * 
     * @param real Double
     */
    public ComplexData (final Double real) {
        if (real != null) {
            this.isnull =false;
            this.real = real.doubleValue();
        }
    }
    
    /**
     * Construct a <code>ComplexData</code> with Double real and imaginary
     * 
     * @param real Double
     * @param imag Double
     */
    public ComplexData (final Double real, final Double imag) {
        if (real != null || imag != null) {
            this.isnull =false;
            if (real != null) {
                this.real = real.doubleValue();
            }
            if (imag != null) {
                this.imag = imag.doubleValue();
            }
        }
    }
    
    private ComplexData (final double real, final double imag, final boolean isnull) {
        this.real = real;
        this.imag = imag;
        this.isnull = isnull;
    }
    
    /**
     * Construct a <code>ComplexData</code> with String.
     *   It supports integer and double complex number format such as -2i, 2 - 3i, -2.038E-5 + 3.029291i, etc.
     *   It supports only real portion such as 3, 3.2818, 3.2E-5, etc.
     * 
     * @param value String
     */
    public ComplexData(final String value) {
        final String s = value != null ? value.trim() : "";
        final int len = s.length();
        if (len == 0) {
            return; //invalid
        }

        char c = s.charAt(len - 1);
        if (len == 1) {
            if (c == 'i' || c == 'I') {
                imag = 1d;
                isnull = false;
            } else if (c >= '0' && c <= '9') {
                real = Double.parseDouble(s);
                isnull = false;
            }
            return;
        } else if (c != 'i' && c != 'I') {
            try {
                real = Double.parseDouble(s);
                isnull = false;
            } catch (NumberFormatException e) {
            }
            return;
        }
        c = s.charAt(0);
        
        final StringBuilder realsb = new StringBuilder();
        final StringBuilder imagsb = new StringBuilder();
        boolean signed = false, dot = false;
        int index = 0;
        if (signed = (c == '+' || c == '-')) {
            realsb.append(c);
            index = 1;
        }
        
        while (index < len) {
            c = s.charAt(index++);
            if (c >= '0' && c <= '9') {
                realsb.append(c);
            } else if (c == '.') {
                if (dot) { 
                    return; //invalid 
                }
                realsb.append(c);
                dot = true;
            } else if ( c == 'E' || c == 'e') {
                if (index == len) { 
                    return; //invalid 
                }
                realsb.append(c);
                
                if ((c = s.charAt(index)) == '+' || c == '-') {
                    realsb.append(c);
                    if (++index == len) { 
                        return; //invalid 
                    }
                    if ((c = s.charAt(index)) >= '0' && c <= '9') {
                        realsb.append(c);
                        ++index;
                    }
                }
            } else if ( c == ' ') {
                while (s.charAt(index) == ' ') { //trim the space
                    ++index;
                }
                if (index != len && (c = s.charAt(index)) == '+' || c == '-') {
                    imagsb.append(c);
                    if (++index == len) {
                        return; //invalid
                    }
                    break;
                }
                return; //invalid 
            } else if (c == '+' || c == '-') {
                if (index == len) {
                    return; //invalid
                }
                imagsb.append(c);
                break;
            } else if ( c == 'i' || c == 'I') { //in fact, this should be imaginary
                if (index != len) {
                    return; //invalid 
                }
                try {
                    if (realsb.length() == 1 && signed) {
                        imag = realsb.charAt(0) == '-' ? -1d : 1d;
                    } else {
                        imag = Double.parseDouble(realsb.toString());
                    }
                    isnull = false;
                    return;
                } catch (NumberFormatException e) {
                    return; //invalid
                }
            } else {
                return; //invalid
            }
        }
        
        if (index == len) {
            try { //real only
                real = Double.parseDouble(realsb.toString());
                isnull = false;
            } catch (NumberFormatException e) {
                real = imag = 0d; //invalid
            }
            return;
        }
        
        if (((c = s.charAt(len - 1)) != 'i' && c != 'I') || (realsb.length() == 1 && signed)) {
            return; //invalid
        }

        //validate real portion is correct
        try {
            real = Double.parseDouble(realsb.toString());
        } catch (NumberFormatException e) {
            real = imag = 0d;
            return; //invalid
        }
        
        while (s.charAt(index) == ' ') { //trim the space after +/- sign
            ++index;
        }
        
        if (index == len - 1) {
            imag = imagsb.charAt(0) == '-' ? -1d : 1d;
            isnull = false;
            return;  //real only
        }
        
        imagsb.append(s.substring(index, len - 1));
        try {   
            if (imagsb.length() == 1 && ((c = imagsb.charAt(0)) == '+' || c == '-')) {
                imag = imagsb.charAt(0) == '-' ? -1d : 1d;
            } else {
                imag = Double.parseDouble(imagsb.toString());
            }
            isnull = false;
        } catch (NumberFormatException e) {
            real = imag = 0d;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public ComplexData clone() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public ComplexData copy() {
        return this;
    }
    
    /**
     * Returns absolute value.
     * 
     * @return the absolute value
     */
    public DoubleData abs() {
        return isnull 
                ? DoubleData.NULL 
                : DoubleData.nonNullValueOf(Math.sqrt(real * real + imag * imag));
    }
    
    /**
     * Returns negated value.
     * 
     * @return the negated value
     */
    public ComplexData negate() {
        return isnull 
                ? ComplexData.NULL 
                : new ComplexData(-real, -imag, false);
    }
    
    /** {@inheritDoc} */
    @Override
    public ComplexData get(final TypeData accessor) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Double getObject() {
        return isnull ? null : real;
    }
    
    /**
     * Get Double value.
     * 
     * @return Double
     */
    public Double getDouble() {
        return isnull ? null : real;
    }
    
    /**
     * Get Real value.
     * 
     * @return Double
     */
    public Double getReal() {
        return isnull ? null : real;
    }
    
    /**
     * Get Imaginary value.
     * 
     * @return Double
     */
    public Double getImaginary() {
        return isnull ? null : imag;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType () {
        return Types.COMPLEX;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compare(final TypeData data1, final TypeData data2) {
        return data1.compareTo(data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final TypeData w) {
        return DataCompare.complexCompareTo(this, w);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TypeData) {
            final TypeData anydata = (TypeData) obj;
            return isnull 
                    ? anydata.isNull() 
                    : anydata.getType() == Types.COMPLEX && !anydata.isNull() 
                        && Double.compare(real, ((ComplexData) anydata).real) == 0
                        && Double.compare(imag, ((ComplexData) anydata).imag) == 0;
        }
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (isnull) {
            out.writeBoolean(true);
        } else {
            out.writeBoolean(false);
            out.writeDouble(real);
            out.writeDouble(imag);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        if (!(isnull = in.readBoolean())) {
            real = in.readDouble();
            imag = in.readDouble();
        }
    }
    
    /**
     * Read data from serialization.
     * 
     * @param in the serialization input
     * @return data
     * @throws IOException if an exception occurs
     */
    public static ComplexData readData(final DataInput in) throws IOException {
        if (in.readBoolean()) {
            return ComplexData.NULL;
        } else {
            return new ComplexData(in.readDouble(), in.readDouble(), false);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<ComplexData> iterator() {
        return Arrays.asList(new ComplexData[] {this}).iterator();
    }
    
    /** {@inheritDoc} */
    @Override
    public ComplexData first() {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Integer toInt() {
        return isnull ? null : (int) real;
    }
    
    /** {@inheritDoc} */
    @Override
    public Long toLong() {
        return isnull ? null : (long) real;
    }
    
    /** {@inheritDoc} */
    @Override
    public Float toFloat() {
        return isnull ? null : new BigDecimal(String.valueOf(real)).floatValue();
    }
    
    /** {@inheritDoc} */
    @Override
    public Double toDouble() {
        return isnull ? null : real;
    }
    
    /** {@inheritDoc} */
    @Override
    public BigDecimal toDecimal() {
        return isnull ? null : new BigDecimal(String.valueOf(real));
    }
    
    /** {@inheritDoc} */
    @Override
    public Boolean toBoolean() {
        return isnull ? null : real != 0d || imag != 0d;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("deprecation")
    @Override
    public java.sql.Date toDate() {
        if (isnull) {
            return null;
        } else {
            final java.sql.Date dt = new java.sql.Date((long) real);
            return new java.sql.Date(dt.getYear(), dt.getMonth(), dt.getDate());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Time toTime() {
        return isnull ? null : new java.sql.Time((long) real);
    }
    
    /** {@inheritDoc} */
    @Override
    public java.sql.Timestamp toTimestamp() {
        return isnull ? null : new java.sql.Timestamp((long) real);
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimeWithTimeZone() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis((long) real);
            cal.set(GregorianCalendar.YEAR, 1970);
            cal.set(GregorianCalendar.MONTH, 0);
            cal.set(GregorianCalendar.DATE, 1);
            return cal;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public GregorianCalendar toTimestampWithTimeZone() {
        if (isnull) {
            return null;
        } else {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis((long) real);
            return cal;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Instant toInstant() {
        return isnull ? null : Instant.ofEpochMilli((long) real);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<String> toList() {
        return isnull ? null : Arrays.asList(new String[] {nonNullToString()});
    }
    
    /** {@inheritDoc} */
    @Override
    public Set<String> toSet() {
        if (isnull) {
            return null;
        } else {
            final LinkedHashSet<String> set = new LinkedHashSet<>(1);
            set.add(nonNullToString());
            return set;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Map<? extends Object, ? extends Object> toMap() {
        if (isnull) {
            return null;
        }
        final LinkedHashMap<Integer, String> map = new LinkedHashMap<>(1);
        map.put(0, toString());
        return map;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte[] toBytes() {
        return isnull ? null : nonNullToString().getBytes(StandardCharsets.UTF_8);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        if (isnull) {
            return StringData.EMPTY_STRING;
        } else {
            final int r = (int) real, i = (int) imag;
            final String sr = (real != r) ? String.valueOf(real) : String.valueOf(r);
            final String si = (imag != i) ? String.valueOf(imag) : String.valueOf(i);
            if (si.charAt(0) == '-' ) {
                return sr + si + "i";
            } else {
                return sr  + "+" + si + "i";
            }
        }
    }
    
    protected String nonNullToString() {
        final String si = String.valueOf(imag);
        if (si.charAt(0) == '-' ) {
            return String.valueOf(real) + si + "i";
        } else {
            return String.valueOf(real)  + "+" + si + "i";
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public int size() {
        return 1;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNull() {
        return isnull;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return isnull;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isDigits() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return !isnull;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return isnull ? TypeUtils.NULL_HASH : Double.hashCode(real) * 17 + Double.hashCode(imag);
    }
    
    public static final ComplexData valueOf(final Double real) {
        return real != null ? ( real == 0d ? ComplexData.ZERO : new ComplexData(real.doubleValue(), 0d, false)) : ComplexData.NULL;
    }
    
    public static final ComplexData valueOf(final double real, final boolean isnull) {
        return real == 0d ? ComplexData.ZERO : new ComplexData(real, 0d, isnull);
    }
    
    public static final ComplexData nonNullValueOf(final double real) {
        return real == 0d ? ComplexData.ZERO : new ComplexData(real, 0d, false);
    }
    
    public static final ComplexData nonNullValueOf(final double real, final double imag) {
        return real == 0d && imag == 0d ? ComplexData.ZERO : new ComplexData(real, imag, false);
    }
    
    public static final ComplexData valueOf(final Double real, final Double imag) {
        if (real != null || imag != null) {
            return new ComplexData(real != null ? real.doubleValue() : 0d, imag != null ? imag.doubleValue() : 0d, false);
        } else {
            return ComplexData.NULL;
        }
    }
    
    public static final ComplexData valueOf(final double real, final double imag, final boolean isnull) {
        return isnull ? ComplexData.NULL : real == 0d && imag == 0d ? ComplexData.ZERO : new ComplexData(real, imag, isnull);
    }
    
    public static final ComplexData valueOf(final Double value, final double defaultValue) {
        return value == null ? new ComplexData(defaultValue) : value == 0d ? ComplexData.ZERO : new ComplexData(value);
    }
    
    public static final ComplexData valueOf(final String value) {
        if (XStringUtils.isBlank(value)) {
            return ComplexData.NULL;
        } else {
            final ComplexData data = new ComplexData(value);
            return data.isNull() ? ComplexData.NULL : data;
        }
    }
}
