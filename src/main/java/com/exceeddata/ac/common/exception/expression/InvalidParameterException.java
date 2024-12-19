package com.exceeddata.ac.common.exception.expression;

import com.exceeddata.ac.common.exception.EngineException;

public class InvalidParameterException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public InvalidParameterException () {
        super();
    }
    
    public InvalidParameterException (String key) {
        super(key);
    }

    public InvalidParameterException (Throwable e) {
        super(e);
    }
    
    public InvalidParameterException (String s, Throwable e) {
        super(s, e);
    }
}
