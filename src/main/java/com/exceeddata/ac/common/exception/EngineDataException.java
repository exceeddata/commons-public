package com.exceeddata.ac.common.exception;

public class EngineDataException extends EngineException {
    private static final long serialVersionUID = 6262415698951100631L;
    
    public EngineDataException () {
        super();
    }
    
    public EngineDataException (String key) {
        super(key);
    }

    public EngineDataException (Throwable e) {
        super(e);
    }
    
    public EngineDataException (String s, Throwable e) {
        super(s, e);
    }
}
