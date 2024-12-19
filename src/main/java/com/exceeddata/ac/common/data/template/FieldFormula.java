package com.exceeddata.ac.common.data.template;

import java.io.Serializable;

import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public interface FieldFormula extends Serializable {
    public TypeData calculate(final TypeData data) throws EngineException;
}
