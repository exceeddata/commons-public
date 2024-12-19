package com.exceeddata.ac.common.exception.expression;

import com.exceeddata.ac.common.exception.EngineException;

public class ExpressionExecuteException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public ExpressionExecuteException () {
        super();
    }
    
    public ExpressionExecuteException (String key) {
        super(key);
    }

    public ExpressionExecuteException (Throwable e) {
        super(e);
    }
    
    public ExpressionExecuteException (String s, Throwable e) {
        super(s, e);
    }
}
