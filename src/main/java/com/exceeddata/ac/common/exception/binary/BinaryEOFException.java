package com.exceeddata.ac.common.exception.binary;

import com.exceeddata.ac.common.exception.EngineException;

public class BinaryEOFException extends EngineException {
    private static final long serialVersionUID = 1L;

    public BinaryEOFException() {
        super("CONNECTOR_STREAM_EOF_BYTES_READ");
    }
}
