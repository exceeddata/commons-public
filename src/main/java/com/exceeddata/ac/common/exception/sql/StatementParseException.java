package com.exceeddata.ac.common.exception.sql;

import com.exceeddata.ac.common.exception.EngineException;

public class StatementParseException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public StatementParseException () {
        super();
    }
    
    public StatementParseException (String key) {
        super(key);
    }

    public StatementParseException (Throwable e) {
        super(e);
    }
    
    public StatementParseException (String s, Throwable e) {
        super(s, e);
    }
}
