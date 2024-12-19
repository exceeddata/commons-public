package com.exceeddata.ac.common.exception;

import java.io.IOException;

import com.exceeddata.ac.common.util.PropertiesUtil;

/**
 * An Exception implementation.
 *
 */
public class EngineException extends IOException {
 
    private static final long serialVersionUID = -3528014035477945025L;
    
    public static final int DEFAULT_ERROR_CODE = 10000;
    protected static final String DEFAULT_ERROR_MESSAGE = "Unknown Error";
    
    protected int errorCode = DEFAULT_ERROR_CODE;
    protected String errorMessage = DEFAULT_ERROR_MESSAGE;
    
    public EngineException () {
        super();
    }
    
    public EngineException (String key) {
        super(key);
        String property;
        int index;

        String exdLang = System.getenv("exd_lang");
        if("zh".equals(exdLang)){
            // throw new EngineException ("JOB_OPERATION_DEFINITION_INVALID:" + operationClassName);
            if((index = key.indexOf(":")) > 0 ){
                property = key.substring(0,index);
                final String property1 = PropertiesUtil.getProperty(property);
                this.errorMessage = property1 == null ? key : property1 + key.substring(index);
            }else{
                final String property1 = PropertiesUtil.getProperty(key);
                this.errorMessage = property1 == null ? key : property1;
            }
        }else {
            this.errorMessage = key;
        }
    }

    public EngineException (Throwable e) {
        super(e);

        if (e instanceof EngineException) {
            errorCode = ((EngineException) e).getCode();
            errorMessage = ((EngineException) e).getMessage();
        } else {
            errorMessage = e.getMessage();
        }
    }
    
    public EngineException (String s, Throwable e) {
        super(s, e);

        if (e instanceof EngineException) {
            errorCode = ((EngineException) e).getCode();
            errorMessage = s;
        } else {
            errorMessage = s;
        }
    }
    
    public int getCode() {
        return errorCode;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return errorMessage;
    }
}
