package com.exceeddata.ac.common.exception.operation;

import com.exceeddata.ac.common.exception.EngineException;

public class SpoolInvalidException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public SpoolInvalidException () {
        super();
    }
    
    public SpoolInvalidException (String key) {
        super(key);
    }

    public SpoolInvalidException (Throwable e) {
        super(e);
    }
    
    public SpoolInvalidException (String s, Throwable e) {
        super(s, e);
    }
}
