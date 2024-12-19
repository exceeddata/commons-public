package com.exceeddata.ac.common.logging;

public enum LoggingLevel {
    INFO(1),
    WARN(2),
    ERROR(3),
    DEBUG(4),
    TRACE(5);
    
    private int severity;
    
    LoggingLevel(final int severity) {
        this.severity = severity;
    }
    
    public int getSeverity() {
        return severity;
    }
    
    public static int compare(final LoggingLevel level1, final LoggingLevel level2) {
        return level1.severity < level2.severity ? -1 : level1.severity == level2.severity ? 0 : 1;
    }
    
    public static LoggingLevel get(final String level) {
        if (level == null) {
            return INFO;
        }
        
        switch (level.trim().toLowerCase()) {
            case "warn": return WARN;
            case "error": return ERROR;
            case "debug": return DEBUG;
            case "trace": return TRACE;
            default:
                return INFO;
        }
    }
}
