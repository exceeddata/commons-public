package com.exceeddata.ac.common.exception.license;

public class LicenseMissingException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public LicenseMissingException() {
        super("license missing");
    }

}
