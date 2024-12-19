package com.exceeddata.ac.common.exception.license;

import com.exceeddata.ac.common.exception.EngineException;

public class LicenseException extends EngineException {

    private static final long serialVersionUID = 1L;

    public LicenseException() {
        super();
    }

    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(Throwable cause) {
        super(cause);
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }

}