package com.exceeddata.ac.common.exception.data;

import com.exceeddata.ac.common.exception.EngineDataException;

public class DataContentException extends EngineDataException {
    private static final long serialVersionUID = 1L;
    
    public DataContentException () {
        super();
    }
    
    public DataContentException (String key) {
        super(key);
    }

    public DataContentException (Throwable e) {
        super(e);
    }
    
    public DataContentException (String s, Throwable e) {
        super(s, e);
    }
}
