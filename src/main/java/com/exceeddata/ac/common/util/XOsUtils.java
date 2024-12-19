package com.exceeddata.ac.common.util;

import java.util.Locale;

public final class XOsUtils {
    private XOsUtils() {}

    public enum OperatingSystem {
        Windows, Mac, Linux, Other
    }
    
    // cached result of OS detection
    protected static OperatingSystem OS = null;
    
    public static OperatingSystem getOperatingSystem() {
       if (OS == null) { 
           final String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
           if ((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
               OS = OperatingSystem.Mac;
           } else if (os.indexOf("win") >= 0) {
               OS = OperatingSystem.Windows;
           } else if (os.indexOf("nux") >= 0) {
               OS = OperatingSystem.Linux;
           } else {
               OS = OperatingSystem.Other;
           }
       }
       return OS;
    }
    
    public static boolean isWindows() {
        return getOperatingSystem() == OperatingSystem.Windows;
    }
    
    public static boolean isNonWindows() {
        return getOperatingSystem() != OperatingSystem.Windows;
    }
    
    public static boolean isLinux() {
        return getOperatingSystem() == OperatingSystem.Linux;
    }
    
    public static boolean isMac() {
        return getOperatingSystem() == OperatingSystem.Mac;
    }
    
    public static char getFileSlashChar() {
        return getOperatingSystem() == OperatingSystem.Windows ? '\\' : '/';
    }
}
