package com.exceeddata.ac.common.extern;

import java.io.IOException;

public interface ExternBytesReader {
    public boolean next() throws IOException;
    
    public byte[] get();
    
    public Object meta();
    
    public void close();
}
