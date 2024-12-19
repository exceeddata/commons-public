package com.exceeddata.ac.common.extern;

import java.io.IOException;

import com.exceeddata.ac.common.util.Image;

public interface ExternImageReader {
    public boolean next() throws IOException;
    
    public Image get();
    
    public Object meta();
    
    public void close();
}
