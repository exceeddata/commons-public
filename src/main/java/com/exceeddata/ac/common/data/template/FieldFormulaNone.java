package com.exceeddata.ac.common.data.template;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class FieldFormulaNone implements FieldFormula {
    private static final long serialVersionUID = 1L;

    private static final FieldFormulaNone INSTANCE = new FieldFormulaNone();
    
    private byte type;
    
    public FieldFormulaNone () {
        type = Types.ANY;
    }
    
    public FieldFormulaNone (final DescType descType) {
        type = descType != null ? descType.getType() : Types.ANY;
    }
    
    public static FieldFormulaNone getInstance() {
        return INSTANCE;
    }
    
    @Override
    public TypeData calculate(final TypeData data) throws EngineException {
        return type == data.getType() || type == Types.ANY ? data : DataConv.convert(data, type);
    }
}
