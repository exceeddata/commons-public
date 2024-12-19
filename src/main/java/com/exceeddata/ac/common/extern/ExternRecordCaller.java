package com.exceeddata.ac.common.extern;

import com.exceeddata.ac.common.data.record.Record;
import com.exceeddata.ac.common.exception.EngineException;

public interface ExternRecordCaller {
    public Record call(Record record) throws EngineException;
    
    public void close();
}
