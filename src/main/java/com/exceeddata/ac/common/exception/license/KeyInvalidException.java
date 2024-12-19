package com.exceeddata.ac.common.exception.license;


public class KeyInvalidException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public KeyInvalidException() {
        super("invalid key");
    }
}
