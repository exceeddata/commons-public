package com.exceeddata.ac.common.exception.license;


public class CpuExceededException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public CpuExceededException() {
        super("CPU exceeded");
    }

}
