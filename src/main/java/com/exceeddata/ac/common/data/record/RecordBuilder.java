package com.exceeddata.ac.common.data.record;

import com.exceeddata.ac.common.data.typedata.NullData;

public final class RecordBuilder {
    private RecordBuilder() {}
    
    public static Record newTemplateRecord(final String[] names) {
        final Record record = new Record(names.length);
        for (int i = 0; i < names.length; ++i) {
            record.add(names[i], NullData.INSTANCE);
        }
        return record;
    }
    
    public static Record newTemplateRecord(final String[] names, final String... additionals) {
        final Record record = new Record(names.length + additionals.length);
        for (int i = 0; i < names.length; ++i) {
            record.add(names[i], NullData.INSTANCE);
        }
        for (int i = 0; i < additionals.length; ++i) {
            record.add(additionals[i], NullData.INSTANCE);
        }
        return record;
    }
    
    public static Record mergeRecords(final Record first, final Record second) {
        if (second == null) {
            return first.shallowCopy();
        }
        
        final int ssize = second.size();
        if (ssize == 0) {
            return first.shallowCopy();
        }
        final Record value = first.additionCopy(ssize);
        for (int i = 0; i < ssize; ++i) {
            value.add(second.nameAt(i), second.dataAt(i));
        }
        return value;
    }
    
    public static Record combineRecords(final Record first, final Record... additionals) {
        if (additionals == null || additionals.length == 0) {
            return first.shallowCopy();
        }
        
        int asize = 0;
        for (final Record record : additionals) {
            asize += record.size();
        }
        final Record value = first.additionCopy(asize);
        for (final Record record : additionals) {
            for (int i = 0, ssize = record.size(); i < ssize; ++i) {
                value.add(record.nameAt(i), record.dataAt(i));
            }
        }
        return value;
    }
}
