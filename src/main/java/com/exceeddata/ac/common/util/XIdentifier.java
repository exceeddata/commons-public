package com.exceeddata.ac.common.util;

import java.util.concurrent.atomic.AtomicLong;

public final class XIdentifier {
    private final static AtomicLong COUNTER = new AtomicLong(System.currentTimeMillis() * 1000l);
    
    private XIdentifier() {}
    
    public static long nextId() {
        return COUNTER.getAndIncrement();
    }
}

