package com.exceeddata.ac.common.exception.data;

import com.exceeddata.ac.common.exception.EngineDataException;

public class DataFormatException extends EngineDataException {
    private static final long serialVersionUID = 1L;
    
    public DataFormatException () {
        super();
    }
    
    public DataFormatException (String key) {
        super(key);
    }

    public DataFormatException (Throwable e) {
        super(e);
    }
    
    public DataFormatException (String s, Throwable e) {
        super(s, e);
    }
}
