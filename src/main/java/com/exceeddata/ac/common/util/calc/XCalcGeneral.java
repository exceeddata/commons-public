package com.exceeddata.ac.common.util.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.BooleanData;
import com.exceeddata.ac.common.data.typedata.ComplexData;
import com.exceeddata.ac.common.data.typedata.DecimalData;
import com.exceeddata.ac.common.data.typedata.DenseVectorData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.FloatData;
import com.exceeddata.ac.common.data.typedata.IntData;
import com.exceeddata.ac.common.data.typedata.ListData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.NumericData;
import com.exceeddata.ac.common.data.typedata.SparseVectorData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.DataConversionException;
import com.exceeddata.ac.common.exception.data.UnsupportedDataOperationException;

public final class XCalcGeneral {
    private XCalcGeneral() {}
    
    protected static TypeData abs(final TypeData d) throws EngineException {
        if (d.isEmpty()) {
            return d;
        }
        switch (d.getType()) {
            case Types.INT: return ((IntData) d).abs();
            case Types.LONG: return ((LongData) d).abs();
            case Types.FLOAT: return ((FloatData) d).abs();
            case Types.DOUBLE: return ((DoubleData) d).abs();
            case Types.NUMERIC: return ((NumericData) d).abs();
            case Types.DECIMAL: return ((DecimalData) d).abs();
            case Types.COMPLEX: return ((ComplexData) d).abs();
            case Types.BOOLEAN: return d.toBoolean() ? BooleanData.TRUE : BooleanData.FALSE;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
                return LongData.nonNullValueOf(Math.abs(d.toLong()));
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> result = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        result.add(abs(ld.get(i)));
                    }
                    return new ListData(result);
                }
            case Types.DENSEVECTOR: {
                    final double[] arr = ((DenseVectorData) d).getDoubles();
                    for (int i = 0, size = arr.length; i < size; ++i) {
                        arr[i] = Math.abs(arr[i]);
                    }
                    return new DenseVectorData(arr);
                }
            case Types.SPARSEVECTOR: {
                final SparseVectorData vd = (SparseVectorData) d;
                final int size = vd.size();
                final int[] indices = vd.getIndices();
                final double[] nonzeros = vd.getNonzeros();
                for (int i = 0, len = nonzeros.length; i < len; ++i) {
                    nonzeros[i] = Math.abs(nonzeros[i]);
                }
                return new SparseVectorData(size, indices, nonzeros);
            }
            default:
                throw new UnsupportedDataOperationException ("DATA_ABS: " + XCalcException.getNonNumericException(d));
        }
    }
    
    protected static TypeData ceil(final TypeData d, final int scale) throws EngineException {
        if (d.isNull()) {
            return d;
        }
        switch (d.getType()) {
            case Types.BOOLEAN:
            case Types.INT: return scale >= -8 
                                ? IntData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.CEILING).intValue())
                                : LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.CEILING).longValue());
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
            case Types.LONG: return LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.CEILING).longValue());
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.COMPLEX: return scale <= 0
                                ? LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.CEILING).longValue())
                                : DoubleData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.CEILING).doubleValue());
            case Types.DECIMAL: return DecimalData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.CEILING));
            case Types.BINARY:
            case Types.STRING:
                if (d.isNumber()) {
                    if (d.isDigits()) {
                        return LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.CEILING).longValue());
                    } else {
                        return DecimalData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.CEILING));
                    }
                }
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> result = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        result.add(ceil(ld.get(i), scale));
                    }
                    return new ListData(result);
                }
            case Types.DENSEVECTOR: {
                final double[] arr = ((DenseVectorData) d).getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = new BigDecimal(String.valueOf(arr[i])).setScale(scale, RoundingMode.CEILING).doubleValue();
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData vd = (SparseVectorData) d;
                final int size = vd.size();
                final int[] indices = vd.getIndices();
                final double[] nonzeros = vd.getNonzeros();
                for (int i = 0, len = nonzeros.length; i < len; ++i) {
                    nonzeros[i] = new BigDecimal(String.valueOf(nonzeros[i])).setScale(scale, RoundingMode.CEILING).doubleValue();
                }
                return new SparseVectorData(size, indices, nonzeros);
                }
            default:
                throw new UnsupportedDataOperationException ("DATA_CEIL: " + XCalcException.getNonNumericException(d));
        }
    }
    
    protected static TypeData floor(final TypeData d, final int scale) throws EngineException {
        if (d.isNull()) {
            return d;
        }
        switch (d.getType()) {
            case Types.BOOLEAN: 
            case Types.INT: return IntData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.FLOOR).intValue());
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
            case Types.LONG: return LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.FLOOR).longValue());
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.COMPLEX: return scale <= 0
                                ? LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.FLOOR).longValue())
                                : DoubleData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.FLOOR).doubleValue());
            case Types.DECIMAL: return DecimalData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.FLOOR));
            case Types.BINARY:
            case Types.STRING:
                if (d.isNumber()) {
                    if (d.isDigits()) {
                        return LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.FLOOR).longValue());
                    } else {
                        return DecimalData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.FLOOR));
                    }
                }
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> result = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        result.add(floor(ld.get(i), scale));
                    }
                    return new ListData(result);
                }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData) d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = new BigDecimal(String.valueOf(arr[i])).setScale(scale, RoundingMode.FLOOR).doubleValue();
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData vd = (SparseVectorData) d;
                final int size = vd.size();
                final int[] indices = vd.getIndices();
                final double[] nonzeros = vd.getNonzeros();
                for (int i = 0, len = nonzeros.length; i < len; ++i) {
                    nonzeros[i] = new BigDecimal(String.valueOf(nonzeros[i])).setScale(scale, RoundingMode.FLOOR).doubleValue();
                }
                return new SparseVectorData(size, indices, nonzeros);
            }
            default:
                throw new UnsupportedDataOperationException ("DATA_FLOOR: " + XCalcException.getNonNumericException(d));
        }
    }
    
    protected static TypeData round(final TypeData d, final int scale) throws EngineException {
        if (d.isNull()) {
            return d;
        }
        switch (d.getType()) {
            case Types.BOOLEAN:
            case Types.INT: return scale >= -8 
                                ? IntData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.HALF_UP).intValue())
                                : LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.HALF_UP).longValue());
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
            case Types.LONG: return LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.HALF_UP).longValue());
            case Types.FLOAT: 
            case Types.DOUBLE: 
            case Types.NUMERIC:
            case Types.COMPLEX: return scale <= 0
                                ? LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.HALF_UP).longValue())
                                : DoubleData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.HALF_UP).doubleValue());
            case Types.DECIMAL: return DecimalData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.HALF_UP));
            case Types.BINARY:
            case Types.STRING:
                if (d.isNumber()) {
                    if (d.isDigits()) {
                        return LongData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.HALF_UP).longValue());
                    } else {
                        return DecimalData.nonNullValueOf(d.toDecimal().setScale(scale, RoundingMode.HALF_UP));
                    }
                }
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> result = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        result.add(round(ld.get(i), scale));
                    }
                    return new ListData(result);
                }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData) d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = new BigDecimal(String.valueOf(arr[i])).setScale(scale, RoundingMode.HALF_UP).doubleValue();
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData vd = (SparseVectorData) d;
                final int size = vd.size();
                final int[] indices = vd.getIndices();
                final double[] nonzeros = vd.getNonzeros();
                for (int i = 0, len = nonzeros.length; i < len; ++i) {
                    nonzeros[i] = new BigDecimal(String.valueOf(nonzeros[i])).setScale(scale, RoundingMode.HALF_UP).doubleValue();
                }
                return new SparseVectorData(size, indices, nonzeros);
            }
            default:
                throw new UnsupportedDataOperationException ("DATA_ROUND: " + XCalcException.getNonNumericException(d));
        }
    }
    
    protected static TypeData truncate(final TypeData d, final int scale) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_TRUNCATE: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_TRUNCATE: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> result = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        result.add(truncate(ld.get(i), scale));
                    }
                    return new ListData(result);
                }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData) d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = new BigDecimal(String.valueOf(arr[i]))
                                .setScale(scale, Double.compare(arr[i], 0d) < 0 ? RoundingMode.CEILING : RoundingMode.FLOOR)
                                .doubleValue();
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                    final SparseVectorData vd = (SparseVectorData) d;
                    final int size = vd.size();
                    final int[] indices = vd.getIndices();
                    final double[] nonzeros = vd.getNonzeros();
                    for (int i = 0, len = nonzeros.length; i < len; ++i) {
                        nonzeros[i] = new BigDecimal(String.valueOf(nonzeros[i]))
                                        .setScale(scale, Double.compare(nonzeros[i], 0d) < 0 ? RoundingMode.CEILING : RoundingMode.FLOOR)
                                        .doubleValue();
                    }
                    return new SparseVectorData(size, indices, nonzeros);
                }
            default:
                return isNegative(d) ? ceil(d, scale) : floor(d, scale);
        }
    }
    
    protected static TypeData negate(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return d;
        }
        switch (d.getType()) {
            case Types.INT: return ((IntData) d).negate();
            case Types.LONG: return ((LongData) d).negate();
            case Types.FLOAT: return ((FloatData) d).negate();
            case Types.DOUBLE: return ((DoubleData) d).negate();
            case Types.NUMERIC: return ((NumericData) d).negate();
            case Types.DECIMAL: return ((DecimalData) d).negate();
            case Types.COMPLEX: return ((ComplexData) d).negate();
            case Types.BOOLEAN: return d.toBoolean() ? BooleanData.FALSE : BooleanData.TRUE;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.CALENDAR_TIME:
            case Types.CALENDAR_TIMESTAMP:
            case Types.INSTANT:
                return LongData.nonNullValueOf(-d.toLong().longValue());
            case Types.LIST: {
                    final ListData ld = (ListData) d;
                    final int size = ld.size();
                    final ArrayList<TypeData> result = new ArrayList<>(size);
                    for (int i = 0; i < size; ++i) {
                        result.add(negate(ld.get(i)));
                    }
                    return new ListData(result);
                }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData) d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Double.compare(0d, arr[i]) == 0 ? 0d : arr[i] * -1d;
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                final SparseVectorData vd = (SparseVectorData) d;
                final int size = vd.size();
                final int[] indices = vd.getIndices();
                final double[] nonzeros = vd.getNonzeros();
                for (int i = 0, len = nonzeros.length; i < len; ++i) {
                    nonzeros[i] = Double.compare(0d, nonzeros[i]) == 0 ? 0d : nonzeros[i] * -1d;
                }
                return new SparseVectorData(size, indices, nonzeros);
            }
            default:
                throw new UnsupportedDataOperationException ("DATA_NEGATE: " + XCalcException.getNonNumericException(d));
        }
    }
    
    protected static TypeData and(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        if (d1.isNull() || d2.isNull()) {
            return nullAsZero ? BooleanData.FALSE : BooleanData.NULL;
        } else {
            return !isZero(d1) && !isZero(d2) ? BooleanData.TRUE : BooleanData.FALSE;
        }
    }
    
    protected static TypeData or(final TypeData d1, final TypeData d2, final boolean nullAsZero) throws EngineException {
        if (d1.isNull() || d2.isNull()) {
            return nullAsZero ? BooleanData.FALSE : BooleanData.NULL;
        } else {
            return !isZero(d1) || !isZero(d2) ? BooleanData.TRUE : BooleanData.FALSE;
        }
    }
    
    protected static TypeData zero(final TypeData d) throws EngineException {
        switch (d.getType()) {
            case Types.INT: return IntData.ZERO;
            case Types.LONG: return LongData.ZERO;
            case Types.FLOAT: return FloatData.ZERO;
            case Types.DOUBLE:
            case Types.COMPLEX: return DoubleData.ZERO;
            case Types.NUMERIC: return NumericData.ZERO;
            case Types.DECIMAL: return DecimalData.ZERO;
            case Types.BOOLEAN: return BooleanData.FALSE;
            case Types.NULL: return d;
            default: return IntData.ZERO;
        }
    }
    
    protected static boolean isNegative(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return false;
        }
        switch (d.getType()) {
            case Types.INT: return d.compareTo(IntData.ZERO) < 0;
            case Types.LONG: return d.compareTo(LongData.ZERO) < 0;
            case Types.FLOAT: return d.compareTo(FloatData.ZERO) < 0;
            case Types.DOUBLE: 
            case Types.COMPLEX: return d.compareTo(DoubleData.ZERO) < 0;
            case Types.NUMERIC: return d.compareTo(NumericData.ZERO) < 0;
            case Types.DECIMAL: return d.toDecimal().signum() < 0;
            case Types.BOOLEAN: return !d.toBoolean();
            case Types.STRING: 
                try {
                    return d.toDecimal().signum() < 0;
                } catch (NumberFormatException | DataConversionException e) {
                    return false;
                }
            default:
                return false;
        }
    }
    
    protected static boolean isPositive(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return false;
        }
        switch (d.getType()) {
            case Types.INT: return d.compareTo(IntData.ZERO) > 0;
            case Types.LONG: return d.compareTo(LongData.ZERO) > 0;
            case Types.FLOAT: return d.compareTo(FloatData.ZERO) > 0;
            case Types.DOUBLE: 
            case Types.COMPLEX: return d.compareTo(DoubleData.ZERO) > 0;
            case Types.NUMERIC: return d.compareTo(NumericData.ZERO) > 0;
            case Types.DECIMAL: return d.toDecimal().signum() > 0;
            case Types.BOOLEAN: return d.toBoolean();
            case Types.STRING: 
                try {
                    return d.toDecimal().signum() > 0;
                } catch (NumberFormatException | DataConversionException e) {
                    return false;
                }
            default:
                return false;
        }
    }
    
    protected static TypeData gt(final TypeData d1, final TypeData d2) throws EngineException {
        if (d2.isNull()) {
            return d1.isNull() ? BooleanData.FALSE : BooleanData.TRUE;
        } else if (d1.isNull()) {
            return BooleanData.FALSE;
        }
        switch (d2.getType()) {
            case Types.LIST: 
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: {
                switch (d1.getType()) {
                    case Types.LIST:
                    case Types.DENSEVECTOR: 
                    case Types.SPARSEVECTOR: {
                        final Iterator<? extends TypeData> iter1 = d1.iterator(), iter2 = d2.iterator();
                        while (true) {
                            if (!iter1.hasNext()) {
                                return BooleanData.FALSE;
                            } else if (!iter2.hasNext()) {
                                return BooleanData.TRUE;
                            }
                            final int compare = iter1.next().compareTo(iter2.next());
                            if (compare > 0) {
                                return BooleanData.TRUE;
                            } else if (compare < 0) {
                                return BooleanData.FALSE;
                            }
                        }
                    }
                    case Types.SET:
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (!(data1.compareTo(iter2.next()) > 0)) {
                                    return BooleanData.FALSE;
                                }
                            }
                        }
                        return BooleanData.TRUE;
                    default:
                        for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                            if (!(d1.compareTo(iter2.next()) > 0)) {
                                return BooleanData.FALSE;
                            }
                        }
                        return BooleanData.TRUE;
                }
            }
            case Types.SET: {
                switch (d1.getType()) {
                    case Types.LIST:
                    case Types.DENSEVECTOR: 
                    case Types.SPARSEVECTOR:
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (data1.compareTo(iter2.next()) > 0) {
                                    return BooleanData.TRUE;
                                }
                            }
                        }
                        return BooleanData.FALSE;
                    case Types.SET:
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (data1.compareTo(iter2.next()) > 0) {
                                    return BooleanData.TRUE;
                                }
                            }
                        }
                        return BooleanData.FALSE;
                    default:
                        for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                            if (d1.compareTo(iter2.next()) > 0) {
                                return BooleanData.TRUE;
                            }
                        }
                        return BooleanData.FALSE;
                }
            }
            default:
                return d1.compareTo(d2) > 0 ? BooleanData.TRUE : BooleanData.FALSE;
        }
    }
    
    protected static TypeData ge(final TypeData d1, final TypeData d2) throws EngineException {
        if (d2.isNull()) {
            return d1.isNull() ? BooleanData.FALSE : BooleanData.TRUE;
        } else if (d1.isNull()) {
            return BooleanData.FALSE;
        }
        switch (d2.getType()) {
            case Types.LIST:
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR: {
                switch (d1.getType()) {
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final Iterator<? extends TypeData> iter1 = d1.iterator(), iter2 = d2.iterator();
                        while (true) {
                            if (!iter1.hasNext()) {
                                return iter2.hasNext() ? BooleanData.FALSE : BooleanData.TRUE;
                            } else if (!iter2.hasNext()) {
                                return BooleanData.TRUE;
                            }
                            final int compare = iter1.next().compareTo(iter2.next());
                            if (compare > 0) {
                                return BooleanData.TRUE;
                            } else if (compare < 0) {
                                return BooleanData.FALSE;
                            }
                        }
                    }
                    case Types.SET:
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (!(data1.compareTo(iter2.next()) >= 0)) {
                                    return BooleanData.FALSE;
                                }
                            }
                        }
                        return BooleanData.TRUE;
                    default:
                        for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                            if (!(d1.compareTo(iter2.next()) >= 0)) {
                                return BooleanData.FALSE;
                            }
                        }
                        return BooleanData.TRUE;
                }
            }
            case Types.SET: {
                switch (d1.getType()) {
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: 
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (data1.compareTo(iter2.next()) >= 0) {
                                    return BooleanData.TRUE;
                                }
                            }
                        }
                        return BooleanData.FALSE;
                    case Types.SET:
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (data1.compareTo(iter2.next()) >= 0) {
                                    return BooleanData.TRUE;
                                }
                            }
                        }
                        return BooleanData.FALSE;
                    default:
                        for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                            if (d1.compareTo(iter2.next()) >= 0) {
                                return BooleanData.TRUE;
                            }
                        }
                        return  BooleanData.FALSE;
                }
            }
            default:
                return d1.compareTo(d2) >= 0 ? BooleanData.TRUE : BooleanData.FALSE;
        }
    }
    
    protected static TypeData lt(final TypeData d1, final TypeData d2) throws EngineException {
        if (d2.isNull()) {
            return d1.isNull() ? BooleanData.FALSE : BooleanData.TRUE;
        } else if (d1.isNull()) {
            return BooleanData.FALSE;
        }
        switch (d2.getType()) {
            case Types.LIST: 
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR: {
                switch (d1.getType()) {
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: {
                        final Iterator<? extends TypeData> iter1 = d1.iterator(), iter2 = d2.iterator();
                        while (true) {
                            if (!iter1.hasNext()) {
                                return iter2.hasNext() ? BooleanData.TRUE : BooleanData.FALSE;
                            } else if (!iter2.hasNext()) {
                                return BooleanData.FALSE;
                            }
                            final int compare = iter1.next().compareTo(iter2.next());
                            if (compare > 0) {
                                return BooleanData.FALSE;
                            } else if (compare < 0) {
                                return BooleanData.TRUE;
                            }
                        }
                    }
                    case Types.SET:
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (!(data1.compareTo(iter2.next()) < 0)) {
                                    return BooleanData.FALSE;
                                }
                            }
                        }
                        return BooleanData.TRUE;
                    default:
                        for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                            if (!(d1.compareTo(iter2.next()) < 0)) {
                                return BooleanData.FALSE;
                            }
                        }
                        return BooleanData.TRUE;
                }
            }
            case Types.SET: {
                switch (d1.getType()) {
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: 
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (data1.compareTo(iter2.next()) < 0) {
                                    return BooleanData.TRUE;
                                }
                            }
                        }
                        return BooleanData.FALSE;
                    case Types.SET:
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (data1.compareTo(iter2.next()) < 0) {
                                    return BooleanData.TRUE;
                                }
                            }
                        }
                        return BooleanData.FALSE;
                    default:
                        for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                            if (d1.compareTo(iter2.next()) < 0) {
                                return BooleanData.TRUE;
                            }
                        }
                        return BooleanData.FALSE;
                }
            }
            default:
                return d1.compareTo(d2) < 0 ? BooleanData.TRUE : BooleanData.FALSE;
        }
    }
    
    protected static TypeData le(final TypeData d1, final TypeData d2) throws EngineException {
        if (d2.isNull()) {
            return d1.isNull() ? BooleanData.FALSE : BooleanData.TRUE;
        } else if (d1.isNull()) {
            return BooleanData.FALSE;
        }
        switch (d2.getType()) {
            case Types.LIST: 
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR: {
                switch (d1.getType()) {
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR:  {
                        final Iterator<? extends TypeData> iter1 = d1.iterator(), iter2 = d2.iterator();
                        while (true) {
                            if (!iter1.hasNext()) {
                                return BooleanData.TRUE;
                            } else if (!iter2.hasNext()) {
                                return BooleanData.FALSE;
                            }
                            final int compare = iter1.next().compareTo(iter2.next());
                            if (compare > 0) {
                                return BooleanData.FALSE;
                            } else if (compare < 0) {
                                return BooleanData.TRUE;
                            }
                        }
                    }
                    case Types.SET:
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (!(data1.compareTo(iter2.next()) <= 0)) {
                                    return BooleanData.FALSE;
                                }
                            }
                        }
                        return BooleanData.TRUE;
                    default:
                        for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                            if (!(d1.compareTo(iter2.next()) <= 0)) {
                                return BooleanData.FALSE;
                            }
                        }
                        return BooleanData.TRUE;
                }
            }
            case Types.SET: {
                switch (d1.getType()) {
                    case Types.LIST:
                    case Types.DENSEVECTOR:
                    case Types.SPARSEVECTOR: 
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (data1.compareTo(iter2.next()) <= 0) {
                                    return BooleanData.TRUE;
                                }
                            }
                        }
                        return BooleanData.FALSE;
                    case Types.SET:
                        for (final Iterator<? extends TypeData> iter1 = d1.iterator(); iter1.hasNext(); ) {
                            final TypeData data1 = iter1.next();
                            for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                                if (data1.compareTo(iter2.next()) <= 0) {
                                    return BooleanData.TRUE;
                                }
                            }
                        }
                        return BooleanData.FALSE;
                    default:
                        for (final Iterator<? extends TypeData> iter2 = d2.iterator(); iter2.hasNext(); ) {
                            if (d1.compareTo(iter2.next()) <= 0) {
                                return BooleanData.TRUE;
                            }
                        }
                        return BooleanData.FALSE;
                }
            }
            default:
                return d1.compareTo(d2) <= 0 ? BooleanData.TRUE : BooleanData.FALSE;
        }
    }
    
    protected static boolean isZero(final TypeData d) throws EngineException {
        switch (d.getType()) {
            case Types.INT: return d.compareTo(IntData.ZERO) == 0;
            case Types.LONG: return d.compareTo(LongData.ZERO) == 0;
            case Types.FLOAT: return d.compareTo(FloatData.ZERO) == 0;
            case Types.DOUBLE: 
            case Types.COMPLEX: return d.compareTo(DoubleData.ZERO) == 0;
            case Types.NUMERIC: return d.compareTo(NumericData.ZERO) == 0;
            case Types.DECIMAL: return d.toDecimal().signum() == 0;
            case Types.BOOLEAN: return !d.toBoolean();
            case Types.NULL: return false;
            case Types.STRING: 
                try {
                    return d.toDecimal().signum() == 0;
                } catch (NumberFormatException | DataConversionException e) {
                    return false;
                }
            default:
                return false;
        }
    }
}
