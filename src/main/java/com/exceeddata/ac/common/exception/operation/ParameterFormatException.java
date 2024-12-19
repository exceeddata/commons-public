package com.exceeddata.ac.common.exception.operation;

import com.exceeddata.ac.common.exception.EngineException;

public class ParameterFormatException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public ParameterFormatException () {
        super();
    }
    
    public ParameterFormatException (String key) {
        super(key);
    }

    public ParameterFormatException (Throwable e) {
        super(e);
    }
    
    public ParameterFormatException (String s, Throwable e) {
        super(s, e);
    }
}
