package com.exceeddata.ac.common.exception.expression;

import com.exceeddata.ac.common.exception.EngineException;

public class ExpressionParseException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public ExpressionParseException () {
        super();
    }
    
    public ExpressionParseException (String key) {
        super(key);
    }

    public ExpressionParseException (Throwable e) {
        super(e);
    }
    
    public ExpressionParseException (String s, Throwable e) {
        super(s, e);
    }
}
