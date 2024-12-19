package com.exceeddata.ac.common.util;

import java.math.BigDecimal;
import java.util.Iterator;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DenseVectorData;
import com.exceeddata.ac.common.data.typedata.SparseVectorData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

/**
 * A class of static util functions for manipulating number
 */
public final class XNumberUtils {
    private XNumberUtils(){}
    
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String BIN_PADDING1 = "0";
    public static final String BIN_PADDING2 = "00";
    public static final String BIN_PADDING3 = "000";
    public static final String BIN_PADDING4 = "0000";
    public static final String BIN_PADDING5 = "00000";
    public static final String BIN_PADDING6 = "000000";
    public static final String BIN_PADDING7 = "0000000";
    
    public static final byte BYTE_ZERO = (byte) 0;
    public static final byte BYTE_ONE = (byte) 1;
    public static final byte BYTE_TWO = (byte) 2;
    public static final byte BYTE_FOUR = (byte) 4;
    public static final byte BYTE_EIGHT = (byte) 8;
    public static final byte BYTE_NULL = (byte) -128;
    
    public static boolean isDigits(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        
        final int len = cs.length();
        if (len == 0) {
            return false;
        }
        
        final char first = cs.charAt(0);
        int begin = 0;
        if (first < '0' || first > '9') {
            if (first == '-' || first == '+') {
                if (len == 1) {
                    return false;
                }
                begin = 1;
            } else {
                return false;
            }
        }
        
        for (int i = begin; i < len; ++i) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    //note: this method from Apache Lang 3 and modified
    public static boolean isNumber(final CharSequence cs) {
        int sz, len;
        if (cs == null || (sz = len = cs.length()) == 0) {
            return false;
        } else {
            char ch = cs.charAt(0);
            boolean hasExp = false;
            boolean hasDecPoint = false;
            boolean allowSigns = false;
            boolean foundDigit = false;
            // deal with any possible sign up front
            final int start = (ch == '-' || ch == '+') ? 1 : 0;
            sz--; // don't want to loop to the last char, check it afterwords
                  // for type qualifiers
            int i = start;
            // loop to the next to last char or to the last char if we need another digit to
            // make a valid number (e.g. chars[0..5] = "1234E")
            while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
                if ((ch = cs.charAt(i)) >= '0' && ch <= '9') {
                    foundDigit = true;
                    allowSigns = false;
    
                } else if (ch == '.') {
                    if (hasDecPoint || hasExp) {
                        // two decimal points or dec in exponent   
                        return false;
                    }
                    hasDecPoint = true;
                } else if (ch == 'e' || ch == 'E') {
                    // we've already taken care of hex.
                    if (hasExp) {
                        // two E's
                        return false;
                    }
                    if (!foundDigit) {
                        return false;
                    }
                    hasExp = true;
                    allowSigns = true;
                } else if (ch == '+' || ch == '-') {
                    if (!allowSigns) {
                        return false;
                    }
                    allowSigns = false;
                    foundDigit = false; // we need a digit after the E
                } else {
                    return false;
                }
                i++;
            }
            if (i < len) {
                if ((ch = cs.charAt(i)) >= '0' && ch <= '9') {
                    // no type qualifier, OK
                    return true;
                }
                if (ch == 'e' || ch == 'E') {
                    // can't have an E at the last byte
                    return false;
                }
                if (ch == '.') {
                    if (hasDecPoint || hasExp) {
                        // two decimal points or dec in exponent
                        return false;
                    }
                    // single trailing decimal point after non-exponent is ok
                    return foundDigit;
                }
                // last character is illegal
                return false;
            }
            // allowSigns is true iff the val ends in 'E'
            // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
            return !allowSigns && foundDigit;
        }
    }
    
    public static double[] toDoubles(final TypeData data, final int spaceMultiplier) throws EngineException {
        switch (data.getType()) {
            case Types.LIST: 
            case Types.SET: {
                final int size = data.size();
                if (size < 1) {
                    return null;
                }
                final double[] doubles = new double[size * spaceMultiplier];
                final Iterator<? extends TypeData> iter = data.iterator();
                Double dbl;
                int i = 0;
                
                while (iter.hasNext()) {
                    try {
                        dbl = iter.next().toDouble();
                        doubles[i] = dbl == null ? 0d : dbl.doubleValue();
                    } catch(EngineException e) {
                        doubles[i] = 0d;
                    }
                    ++i;
                }
                return doubles;
            }
            case Types.DENSEVECTOR: {
                final DenseVectorData vector = DataConv.toDenseVectorData(data);
                final int size = vector.size();
                if (size < 1) {
                    return null;
                }
                return vector.copyDoubles(new double[size * spaceMultiplier]);
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData vector = DataConv.toSparseVectorData(data);
                final int size = vector.size();
                if (size < 1) {
                    return null;
                }
                return vector.copyDoubles(new double[size * spaceMultiplier]);
            }
            default: return null;
        }
    }
    
    public static boolean isWholeNumber(TypeData td) throws EngineException {
        if (td.isNull()) {
            return false;
        } else {
            switch (td.getType()) {
                case Types.DECIMAL: {
                    final BigDecimal bd = td.toDecimal();
                    return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
                }
                case Types.BOOLEAN:
                case Types.INT:
                case Types.LONG: 
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                case Types.CALENDAR_TIME:
                case Types.CALENDAR_TIMESTAMP: return true;
                case Types.FLOAT:
                case Types.DOUBLE: 
                case Types.NUMERIC:
                case Types.STRING: {
                    final Double d = td.toDouble();
                    return d == Math.floor(d) && !Double.isInfinite(d);
                }
                default: return false; 
            }
        }
    }
}
