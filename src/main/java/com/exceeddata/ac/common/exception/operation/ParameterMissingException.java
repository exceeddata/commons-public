package com.exceeddata.ac.common.exception.operation;

import com.exceeddata.ac.common.exception.EngineException;

public class ParameterMissingException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public ParameterMissingException () {
        super();
    }
    
    public ParameterMissingException (String key) {
        super(key);
    }

    public ParameterMissingException (Throwable e) {
        super(e);
    }
    
    public ParameterMissingException (String s, Throwable e) {
        super(s, e);
    }
}
