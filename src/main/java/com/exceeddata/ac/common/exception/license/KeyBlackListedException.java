package com.exceeddata.ac.common.exception.license;


public class KeyBlackListedException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public KeyBlackListedException() {
        super("black listed key");
    }
}