package com.exceeddata.ac.common.exception.license;


public class HardwareMismatchedException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public HardwareMismatchedException() {
        super("Hardware mismatch");
    }

}
