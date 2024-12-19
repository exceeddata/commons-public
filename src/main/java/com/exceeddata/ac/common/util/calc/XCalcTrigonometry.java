package com.exceeddata.ac.common.util.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DenseVectorData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.ListData;
import com.exceeddata.ac.common.data.typedata.SetData;
import com.exceeddata.ac.common.data.typedata.SparseVectorData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.exception.data.UnsupportedDataOperationException;

public final class XCalcTrigonometry {
    private static final BigDecimal DECIMAL_ONE = BigDecimal.ONE;
    private static final BigDecimal DECIMAL_NEG_ONE = BigDecimal.valueOf(-1l);
    
    private XCalcTrigonometry() {}
    
    protected static TypeData sin(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_SIN: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_SIN: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                final ListData ld = (ListData) d;
                final int size = ld.size();
                final ArrayList<TypeData> arr = new ArrayList<TypeData>(size);
                for (int i = 0; i < size; ++i) {
                    arr.add(sin(ld.get(i)));
                }
                return new ListData(arr);
            }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData)d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.sin(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR:{
                final DenseVectorData vd = DataConv.toDenseVectorData(d);
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.sin(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            default:
                return new DoubleData(Math.sin(d.toDouble()));
        }
    }
    
    protected static TypeData cos(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_COS: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_COS: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                final ListData ld = (ListData) d;
                final int size = ld.size();
                final ArrayList<TypeData> arr = new ArrayList<TypeData>(size);
                for (int i = 0; i < size; ++i) {
                    arr.add(cos(ld.get(i)));
                }
                return new ListData(arr);
            }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData) d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.cos(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                final DenseVectorData vd = DataConv.toDenseVectorData(d);
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.cos(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            default:
                return new DoubleData(Math.cos(d.toDouble()));
        }
    }
    
    protected static TypeData tan(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_TAN: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_TAN: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                final ListData ld = (ListData) d;
                final int size = ld.size();
                final ArrayList<TypeData> arr = new ArrayList<TypeData>(size);
                for (int i = 0; i < size; ++i) {
                    arr.add(tan(ld.get(i)));
                }
                return new ListData(arr);
            }
            case Types.DENSEVECTOR: {
                final DenseVectorData vd = (DenseVectorData) d;
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.tan(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            case Types.SPARSEVECTOR: {
                final DenseVectorData vd = DataConv.toDenseVectorData(d);
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.tan(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            default:
                return new DoubleData(Math.tan(d.toDouble()));
        }
    }
    protected static TypeData asin(final TypeData d, final Integer borderScale) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_ASIN: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_ASIN: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                final ListData ld = (ListData) d;
                final int size = ld.size();
                final ArrayList<TypeData> arr = new ArrayList<TypeData>(size);
                for (int i = 0; i < size; ++i) {
                    arr.add(asin(ld.get(i), borderScale));
                }
                return new ListData(arr);
            }
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: {
                final DenseVectorData vd = DataConv.toDenseVectorData(d);
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    if (arr[i] > 1d) {
                        arr[i] = borderScale == null
                                ? 0d
                                : (new BigDecimal(String.valueOf(arr[i]))).setScale(borderScale, RoundingMode.HALF_EVEN).compareTo(DECIMAL_ONE.setScale(borderScale, RoundingMode.HALF_EVEN)) > 0 ? 0d : Math.asin(1d);
                    } else if (arr[i] < -1d) {
                        arr[i] = borderScale == null
                                ? 0d
                                : (new BigDecimal(String.valueOf(arr[i]))).setScale(borderScale, RoundingMode.HALF_EVEN).compareTo(DECIMAL_NEG_ONE.setScale(borderScale, RoundingMode.HALF_EVEN)) < 0 ? 0d : Math.asin(-1d);
                    } else {
                        arr[i] = Math.asin(arr[i]);
                    } 
                }
                return new DenseVectorData(arr);
            }
            default:{
                final Double v = d.toDouble();
                if (v > 1d) {
                    return borderScale == null 
                            ? DoubleData.NULL 
                            : (new BigDecimal(String.valueOf(v))).setScale(borderScale, RoundingMode.HALF_EVEN).compareTo(DECIMAL_ONE.setScale(borderScale, RoundingMode.HALF_EVEN)) > 0 ? DoubleData.NULL : new DoubleData(Math.asin(1d));
                } else if (v < -1d) {
                    return borderScale == null 
                            ? DoubleData.NULL 
                            : (new BigDecimal(String.valueOf(v))).setScale(borderScale, RoundingMode.HALF_EVEN).compareTo(DECIMAL_NEG_ONE.setScale(borderScale, RoundingMode.HALF_EVEN)) < 0 ? DoubleData.NULL : new DoubleData(Math.asin(-1d));
                } else {
                    return new DoubleData(Math.asin(v));
                }
            }
        }
    }
    
    protected static TypeData acos(final TypeData d, final Integer borderScale) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_ACOS: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_ACOS: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                final ListData ld = (ListData) d;
                final int size = ld.size();
                final ArrayList<TypeData> arr = new ArrayList<TypeData>(size);
                for (int i = 0; i < size; ++i) {
                    arr.add(acos(ld.get(i), borderScale));
                }
                return new ListData(arr);
            }
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR: {
                final DenseVectorData vd = DataConv.toDenseVectorData(d);
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    if (arr[i] > 1d) {
                        arr[i] = borderScale == null
                                ? 0d
                                : (new BigDecimal(String.valueOf(arr[i]))).setScale(borderScale, RoundingMode.HALF_EVEN).compareTo(DECIMAL_ONE.setScale(borderScale, RoundingMode.HALF_EVEN)) > 0 ? 0d : Math.acos(1d);
                    } else if (arr[i] < -1d) {
                        arr[i] = borderScale == null
                                ? 0d
                                : (new BigDecimal(String.valueOf(arr[i]))).setScale(borderScale, RoundingMode.HALF_EVEN).compareTo(DECIMAL_NEG_ONE.setScale(borderScale, RoundingMode.HALF_EVEN)) < 0 ? 0d : Math.acos(-1d);
                    } else {
                        arr[i] = Math.acos(arr[i]);
                    } 
                }
                return new DenseVectorData(arr);
            }
            default: {
                final Double v = d.toDouble();
                if (v > 1d) {
                    return borderScale == null 
                            ? DoubleData.NULL 
                            : (new BigDecimal(String.valueOf(v))).setScale(borderScale, RoundingMode.HALF_EVEN).compareTo(DECIMAL_ONE.setScale(borderScale, RoundingMode.HALF_EVEN)) > 0 ? DoubleData.NULL : new DoubleData(Math.acos(1d));
                } else if (v < -1d) {
                    return borderScale == null 
                            ? DoubleData.NULL 
                            : (new BigDecimal(String.valueOf(v))).setScale(borderScale, RoundingMode.HALF_EVEN).compareTo(DECIMAL_NEG_ONE.setScale(borderScale, RoundingMode.HALF_EVEN)) < 0 ? DoubleData.NULL : new DoubleData(Math.acos(-1d));
                } else {
                    return new DoubleData(Math.acos(v));
                }
            }
        }
    }
    
    protected static TypeData atan(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_ATAN: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_ATAN: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                final ListData ld = (ListData) d;
                final int size = ld.size();
                final ArrayList<TypeData> arr = new ArrayList<TypeData>(size);
                for (int i = 0; i < size; ++i) {
                    arr.add(atan(ld.get(i)));
                }
                return new ListData(arr);
            }
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR: {
                final DenseVectorData vd = DataConv.toDenseVectorData(d);
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    final double t = Math.atan(arr[i]);
                    arr[i] = t != Double.NaN ? t : 0d;
                }
                return new DenseVectorData(arr);
            }
            default:{
                final Double v = d.toDouble();
                final double t = Math.atan(v);
                return t != Double.NaN ?  new DoubleData(t) : DoubleData.NULL;
            }
        }
    }
    
    protected static TypeData atan2(final TypeData y, final TypeData x) throws EngineException {
        if (y.isEmpty() || x.isEmpty()) {
            return DoubleData.NULL;
        }
        switch (y.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_ATAN2: SET - " + XCalcException.getNonNumericException(y));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_ATAN2: MAP - " + XCalcException.getNonNumericException(y));
            case Types.LIST: {
                    final ListData ld = (ListData) y;
                    final int size = ld.size();
                    final ArrayList<TypeData> result = new ArrayList<>(size);
                    if (x instanceof DenseVectorData || x instanceof SparseVectorData || x instanceof ListData || x instanceof SetData) {
                        if (size != x.size()) {
                            throw new UnsupportedDataOperationException ("DATA_ATAN2: LIST SIZE MISMATCH - " + XCalcException.getNonNumericException(x));
                        }
                        final Iterator<? extends TypeData> yiter = ld.iterator(), xiter = x.iterator();
                        while (yiter.hasNext()) {
                            result.add(atan2(yiter.next(), xiter.next()));
                        }
                    } else if (x.getType() == Types.MAP) {
                        throw new UnsupportedDataOperationException ("DATA_ATAN2: MAP - " + XCalcException.getNonNumericException(x));
                    } else {
                        for (int i = 0; i < size; ++i) {
                            result.add(atan2(ld.get(i), x));
                        }
                    }
                    return new ListData(result);
                }
            case Types.DENSEVECTOR: 
            case Types.SPARSEVECTOR: {
                    final DenseVectorData vd = DataConv.toDenseVectorData(y);
                    final int size = vd.size();
                    final double[] result = new double[size];
                    int index = 0;
                    Double t;
                    if (x instanceof DenseVectorData || x instanceof SparseVectorData || x instanceof ListData || x instanceof SetData) {
                        if (size != x.size()) {
                            throw new UnsupportedDataOperationException ("DATA_ATAN2: LIST SIZE MISMATCH - " + XCalcException.getNonNumericException(x));
                        }
                        final Iterator<? extends TypeData> yiter = vd.iterator(), xiter = x.iterator();
                        while (yiter.hasNext()) {
                            t = atan2(yiter.next(), xiter.next()).toDouble();
                            result[index++] = t != null && t != Double.NaN ? t.doubleValue() : 0d;
                        }
                    } else if (x.getType() == Types.MAP) {
                        throw new UnsupportedDataOperationException ("DATA_ATAN2: MAP - " + XCalcException.getNonNumericException(x));
                    } else {
                        for (int i = 0; i < size; ++i) {
                            t = atan2(vd.get(i), x).toDouble();
                            result[i] = t != null && t != Double.NaN ? t.doubleValue() : 0d;
                        }
                    }
                    return new DenseVectorData(result);
                }
            default:{
                final double t = Math.atan2(y.toDouble(), x.toDouble());
                return t != Double.NaN ?  new DoubleData(t) : DoubleData.NULL;
            }
        }
    }
    
    protected static TypeData sinh(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_SINH: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_SINH: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                final ListData ld = (ListData) d;
                final int size = ld.size();
                final ArrayList<TypeData> arr = new ArrayList<>(size);
                for (int i = 0; i < size; ++i) {
                    arr.add(sinh(ld.get(i)));
                }
                return new ListData(arr);
            }
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR: {
                final DenseVectorData vd = DataConv.toDenseVectorData(d);
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.sinh(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            default:
                return new DoubleData(Math.sinh(d.toDouble()));
        }
    }
    
    protected static TypeData cosh(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_COSH: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_COSH: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                final ListData ld = (ListData) d;
                final int size = ld.size();
                final ArrayList<TypeData> arr = new ArrayList<>(size);
                for (int i = 0; i < size; ++i) {
                    arr.add(cosh(ld.get(i)));
                }
                return new ListData(arr);
            }
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR: {
                final DenseVectorData vd = DataConv.toDenseVectorData(d);
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.cosh(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            default:
                return new DoubleData(Math.cosh(d.toDouble()));
        }
    }
    
    protected static TypeData tanh(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_TANH: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_TANH: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                final ListData ld = (ListData) d;
                final int size = ld.size();
                final ArrayList<TypeData> arr = new ArrayList<>(size);
                for (int i = 0; i < size; ++i) {
                    arr.add(tanh(ld.get(i)));
                }
                return new ListData(arr);
            }
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR: {
                final DenseVectorData vd = DataConv.toDenseVectorData(d);
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.tanh(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            default:
                return new DoubleData(Math.tanh(d.toDouble()));
        }
    }
    
    protected static TypeData cbrt(final TypeData d) throws EngineException {
        if (d.isNull()) {
            return DoubleData.NULL;
        }
        switch (d.getType()) {
            case Types.SET:
                throw new UnsupportedDataOperationException ("DATA_CBRT: SET - " + XCalcException.getNonNumericException(d));
            case Types.MAP:
                throw new UnsupportedDataOperationException ("DATA_CBRT: MAP - " + XCalcException.getNonNumericException(d));
            case Types.LIST: {
                final ListData ld = (ListData) d;
                final int size = ld.size();
                final ArrayList<TypeData> arr = new ArrayList<>(size);
                for (int i = 0; i < size; ++i) {
                    arr.add(cbrt(ld.get(i)));
                }
                return new ListData(arr);
            }
            case Types.DENSEVECTOR:
            case Types.SPARSEVECTOR: {
                final DenseVectorData vd = DataConv.toDenseVectorData(d);
                final double[] arr = vd.getDoubles();
                for (int i = 0, len = arr.length; i < len; ++i) {
                    arr[i] = Math.cbrt(arr[i]);
                }
                return new DenseVectorData(arr);
            }
            default:
                return new DoubleData(Math.cbrt(d.toDouble()));
        }
    }
}
