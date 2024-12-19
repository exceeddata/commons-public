package com.exceeddata.ac.common.exception.expression;

import com.exceeddata.ac.common.exception.EngineException;

public class DynamicReferenceException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public DynamicReferenceException () {
        super();
    }
    
    public DynamicReferenceException (String key) {
        super(key);
    }

    public DynamicReferenceException (Throwable e) {
        super(e);
    }
    
    public DynamicReferenceException (String s, Throwable e) {
        super(s, e);
    }
}
