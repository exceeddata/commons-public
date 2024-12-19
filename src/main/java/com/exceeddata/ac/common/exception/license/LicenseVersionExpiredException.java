package com.exceeddata.ac.common.exception.license;


public class LicenseVersionExpiredException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public LicenseVersionExpiredException() {
        super("license version expired");
    }

}
