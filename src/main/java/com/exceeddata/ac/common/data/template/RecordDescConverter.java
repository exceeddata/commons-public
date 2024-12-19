package com.exceeddata.ac.common.data.template;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.NumericData;

/**
 * A class for converting record element desc;
 *
 */
public class RecordDescConverter implements DescConverter {
    public static final int SCALE_DEFAULT = 6;
    
    /** {@inheritDoc} */
    @Override
    public Desc describe(final String definition, final int position) {
        String term = definition.trim();
        if (term.length() == 0) {
            return new Desc("field" + position, typify("string"), false);
        }
        
        final StringBuilder sb = new StringBuilder();
        String name = null;
        boolean nullable = false;
        int index = 0, len = term.length();
        if (term.charAt(0) == '\'') { //quoted
            boolean escaped = false;
            char c;
            index = 1;
            while (index < len) {
                c = term.charAt(index++);
                if (escaped) {
                    sb.append(c);
                    escaped = false;
                } else if (c == '\\') {
                    if (index == len) {
                        sb.append(c);
                    } else {
                        escaped = true;
                    }
                } else if (c  == '\'') {
                    if (index == len) {
                        return new Desc(sb.toString(), typify("string"), false); //bad syntax
                    } else if ((c = term.charAt(index)) == ' ' || c == '\t') {
                        ++index;
                        break;
                    } else if (c == '\'') {
                        ++index;
                        sb.append('\'');
                    } else {
                        return new Desc("field" + position, typify("string"), false); //bad syntax
                    }
                } else {
                    sb.append(c);
                }
            }
        } else {
            char c;
            while (index < len) {
                if ((c = term.charAt(index++)) == ' ' || c == '\t') {
                    break;
                } else {
                    sb.append(c);
                }
            }
        }
        
        name = sb.toString();
        if (index == len || (term = term.substring(index).trim()).length() == 0) {
            return new Desc(name, typify("string"), false); //bad syntax
        }
        
        if (term.toLowerCase().endsWith(" nullable")) {
            nullable = true;
            term = term.substring(0, term.length() - 8).trim();
        }
        
        return new Desc(name, typify(term), nullable);
    }

    /** {@inheritDoc} */
    @Override
    public DescType typify(String type) {
        return itemify(type);
    }
    
    public static DescType itemify(String type) {
        switch (type.toLowerCase()) {
            case "binary": 
            case "bytes": return new PrimitiveDescType(Types.BINARY);
            case "boolean": return new PrimitiveDescType(Types.BOOLEAN);
            case "bigdecimal": 
            case "decimal": return new PrimitiveDescType(Types.DECIMAL).setScale(SCALE_DEFAULT);
            case "complex": return new PrimitiveDescType(Types.COMPLEX).setScale(SCALE_DEFAULT);
            case "date": return new PrimitiveDescType(Types.DATE);
            case "densevector": return new DenseVectorDescType();
            case "double": return new PrimitiveDescType(Types.DOUBLE).setScale(SCALE_DEFAULT);
            case "numeric": return new PrimitiveDescType(Types.NUMERIC).setScale(SCALE_DEFAULT);
            case "float": return new PrimitiveDescType(Types.FLOAT).setScale(SCALE_DEFAULT);
            case "instant": return new PrimitiveDescType(Types.INSTANT);
            case "int":
            case "integer": return new PrimitiveDescType(Types.INT);
            case "list": return new ListDescType();
            case "long": return new PrimitiveDescType(Types.LONG);
            case "map": return new MapDescType();
            case "null": return new PrimitiveDescType(Types.NULL);
            case "set": return new SetDescType();
            case "sparsevector": return new SparseVectorDescType();
            case "string": return new PrimitiveDescType(Types.STRING);
            case "time": return new PrimitiveDescType(Types.TIME);
            case "timestamp": return new PrimitiveDescType(Types.TIMESTAMP);
            case "timewithtimezone": return new PrimitiveDescType(Types.CALENDAR_TIME);
            case "timestampwithtimezone": return new PrimitiveDescType(Types.CALENDAR_TIMESTAMP);
            default:
                int opens, closes;
                if ((opens = type.indexOf('(')) > 0 && (closes = type.lastIndexOf(')')) > 0 && closes > opens + 1) {
                    try {
                        final String t = type.substring(0, opens).trim().toLowerCase();
                        final String s = type.substring(opens + 1, closes).trim();
                        final int scale = Integer.parseInt(s);
                        switch(t) {
                            case "bigdecimal":
                            case "decimal": return new PrimitiveDescType(Types.DECIMAL).setScale(scale);
                            case "float": return new PrimitiveDescType(Types.FLOAT).setScale(scale);
                            case "numeric": return new PrimitiveDescType(Types.NUMERIC).setScale(NumericData.SCALE);
                            case "double": return new PrimitiveDescType(Types.DOUBLE).setScale(scale);
                        }
                    } catch (NumberFormatException e) {
                    }
                } else if ((opens = type.indexOf('<')) > 0 && (closes = type.lastIndexOf('>')) > 0 && closes > opens + 1) {
                    final String t = type.substring(0, opens).trim().toLowerCase();
                    final String s = type.substring(opens + 1, closes).trim();
                    switch(t) {
                        case "list": return listify(s);
                        case "set": return setify(s);
                        case "map": return mapify(s);
                    }
                }
                return new PrimitiveDescType(Types.STRING);
        }
    }
    
    public static ListDescType listify (final String subtype) {
        return new ListDescType();
    }
    
    public static SetDescType setify (final String subtype) {
        return new SetDescType();
    }
    
    public static MapDescType mapify (final String subtypes) {
        return new MapDescType();
    }
}
