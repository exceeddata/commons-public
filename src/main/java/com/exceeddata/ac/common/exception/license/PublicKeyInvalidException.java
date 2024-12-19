package com.exceeddata.ac.common.exception.license;


public class PublicKeyInvalidException extends LicenseException {

    private static final long serialVersionUID = 1L;

    public PublicKeyInvalidException() {
        super("invalid public key");
    }
    
    public PublicKeyInvalidException(Throwable cause) {
        super("invalid public key", cause);
    }
}
