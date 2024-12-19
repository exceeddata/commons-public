package com.exceeddata.ac.common.exception.license;

public class UnavailableLicenseManagerException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public UnavailableLicenseManagerException() {
        super("license manager is unavailable");
    }

}
