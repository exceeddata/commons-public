package com.exceeddata.ac.common.exception.expression;

import com.exceeddata.ac.common.exception.EngineException;

public class InvalidDataException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public InvalidDataException () {
        super();
    }
    
    public InvalidDataException (String key) {
        super(key);
    }

    public InvalidDataException (Throwable e) {
        super(e);
    }
    
    public InvalidDataException (String s, Throwable e) {
        super(s, e);
    }
}
