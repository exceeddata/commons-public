package com.exceeddata.ac.common.exception.expression;

import com.exceeddata.ac.common.exception.EngineException;

public class InvalidWindowException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public InvalidWindowException () {
        super();
    }
    
    public InvalidWindowException (String key) {
        super(key);
    }

    public InvalidWindowException (Throwable e) {
        super(e);
    }
    
    public InvalidWindowException (String s, Throwable e) {
        super(s, e);
    }
}
