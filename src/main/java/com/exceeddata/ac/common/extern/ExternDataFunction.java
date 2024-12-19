package com.exceeddata.ac.common.extern;

import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public interface ExternDataFunction {
    public TypeData apply(TypeData... args) throws EngineException;
}
