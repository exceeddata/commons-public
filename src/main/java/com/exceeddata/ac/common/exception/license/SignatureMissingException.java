package com.exceeddata.ac.common.exception.license;

public class SignatureMissingException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public SignatureMissingException() {
        super("signature missing");
    }

}
