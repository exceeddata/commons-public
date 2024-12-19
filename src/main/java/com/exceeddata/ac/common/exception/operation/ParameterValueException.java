package com.exceeddata.ac.common.exception.operation;

import com.exceeddata.ac.common.exception.EngineException;

public class ParameterValueException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public ParameterValueException () {
        super();
    }
    
    public ParameterValueException (String key) {
        super(key);
    }

    public ParameterValueException (Throwable e) {
        super(e);
    }
    
    public ParameterValueException (String s, Throwable e) {
        super(s, e);
    }
}
