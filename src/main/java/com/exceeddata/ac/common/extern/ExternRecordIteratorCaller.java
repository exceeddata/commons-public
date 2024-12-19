package com.exceeddata.ac.common.extern;

import java.util.ArrayList;
import java.util.Iterator;

import com.exceeddata.ac.common.data.record.Record;
import com.exceeddata.ac.common.exception.EngineException;

public interface ExternRecordIteratorCaller {
    public ArrayList<Record> call(Iterator<Record> iter) throws EngineException;
    
    public void close();
}
