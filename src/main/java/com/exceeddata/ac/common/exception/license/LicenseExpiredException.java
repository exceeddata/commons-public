package com.exceeddata.ac.common.exception.license;

public class LicenseExpiredException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public LicenseExpiredException() {
        super("license expired");
    }

}
