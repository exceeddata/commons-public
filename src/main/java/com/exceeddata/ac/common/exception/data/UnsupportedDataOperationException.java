package com.exceeddata.ac.common.exception.data;

import com.exceeddata.ac.common.exception.EngineDataException;

public class UnsupportedDataOperationException extends EngineDataException {
    private static final long serialVersionUID = 1L;
    
    public UnsupportedDataOperationException () {
        super();
    }
    
    public UnsupportedDataOperationException (String key) {
        super(key);
    }

    public UnsupportedDataOperationException (Throwable e) {
        super(e);
    }
    
    public UnsupportedDataOperationException (String s, Throwable e) {
        super(s, e);
    }
}
