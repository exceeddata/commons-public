package com.exceeddata.ac.common.exception.license;

public class SignatureInvalidException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public SignatureInvalidException() {
        super("signature invalid");
    }

}
