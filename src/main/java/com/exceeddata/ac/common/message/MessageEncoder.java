package com.exceeddata.ac.common.message;

import java.io.Serializable;
import java.util.ArrayList;

import com.exceeddata.ac.common.data.record.Record;

public interface MessageEncoder extends Serializable {
    /**
     * Encode the Message.
     * 
     * @param record the record
     * @return list of bytes
     */
    public ArrayList<MessageContent> encode(final Record record);
}
