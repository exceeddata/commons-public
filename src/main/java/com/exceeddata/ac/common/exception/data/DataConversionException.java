package com.exceeddata.ac.common.exception.data;

import com.exceeddata.ac.common.exception.EngineDataException;

public class DataConversionException extends EngineDataException {
    private static final long serialVersionUID = 1L;
    
    public DataConversionException () {
        super();
    }
    
    public DataConversionException (String key) {
        super(key);
    }

    public DataConversionException (Throwable e) {
        super(e);
    }
    
    public DataConversionException (String s, Throwable e) {
        super(s, e);
    }
}
