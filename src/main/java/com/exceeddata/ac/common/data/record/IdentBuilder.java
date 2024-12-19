package com.exceeddata.ac.common.data.record;

import com.exceeddata.ac.common.data.typedata.TypeData;

public final class IdentBuilder {
    private IdentBuilder() {}
    
    public static Ident newIdent(final TypeData ... values) {
        switch(values.length) {
            case 0: return EmptyIdent.INSTANCE;
            case 1: return new OneIdent(values[0]);
            case 2: return new TwoIdent(values[0], values[1]);
            default: return new ArrayIdent(values, values.length);
        }
    }
    
    public static Ident toIdent(final Record record) {
        final int size = record.size();
        switch(size) {
            case 0: return EmptyIdent.INSTANCE;
            case 1: return new OneIdent(record.dataAt(0));
            case 2: return new TwoIdent(record.dataAt(0), record.dataAt(1));
            default: return new ArrayIdent(record.datas(), size);
        }
    }
}
