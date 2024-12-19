package com.exceeddata.ac.common.exception.sql;

import com.exceeddata.ac.common.exception.EngineException;

/**
 * 
 * This is an exception class for unexpected end of query.
 *
 */
public class EndOfQueryException extends EngineException {
    private static final long serialVersionUID = 1L;
    
    public EndOfQueryException () {
        super();
    }
    
    public EndOfQueryException (String key) {
        super(key);
    }

    public EndOfQueryException (Throwable e) {
        super(e);
    }
    
    public EndOfQueryException (String s, Throwable e) {
        super(s, e);
    }
}
