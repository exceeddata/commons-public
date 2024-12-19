package com.exceeddata.ac.common.extern;

import java.io.IOException;

import com.exceeddata.ac.common.data.record.Record;

public interface ExternRecordReader {
    public boolean next() throws IOException;
    
    public Record get();
    
    public void close();
}
