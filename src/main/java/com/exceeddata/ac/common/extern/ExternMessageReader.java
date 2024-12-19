package com.exceeddata.ac.common.extern;

import java.io.IOException;

import com.exceeddata.ac.common.message.MessageContent;
import com.exceeddata.ac.common.message.MessageDesc;

public interface ExternMessageReader {
    public boolean next() throws IOException;
    
    public MessageContent get();
    
    public MessageDesc meta();
    
    public void close();
}
