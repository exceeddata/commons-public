package com.exceeddata.ac.common.message;

import java.io.Serializable;
import java.time.Instant;

public interface MessageDesc extends Serializable {
    /**
     * Get the start timestamp.
     * 
     * @return Instant
     */
    public Instant getTimeStart();
}
