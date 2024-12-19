package com.exceeddata.ac.common.util;

/**
 * A class of static util functions for File and Directories
 */
public final class XFileUtils {
    private XFileUtils() {}
    
    /**
     * Get a POSIX folder's cleaned directory name.
     * 
     * @param directory the directory
     * @return String
     */
    public static String getPosixFolderDirectory(final String directory) {
        if (XStringUtils.isNotBlank(directory)) {
            final String f = directory.trim();
            return (f.charAt(f.length() - 1) != '/') ? f + "/" : f;
        }
        
        return "";
    }
    
    /**
     * Get a POSIX file's directory path.
     * 
     * @param file the file
     * @return String
     */
    public static String getPosixFileDirectory(final String file) {
        if (XStringUtils.isNotBlank(file)) {
            final String f = file.trim();
            for (int i = f.length() - 1; i >= 0; --i) {
                if (f.charAt(i) == '/') {
                    return f.substring(0, i);
                }
            }
        }
        
        return "";
    }
    
    /**
     * Get a OS-dependent folder's cleaned directory name.
     * 
     * @param directory the directory
     * @return String
     */
    public static String getOsFolderDirectory(final String directory) {
        if (XStringUtils.isNotBlank(directory)) {
            final String f = directory.trim();
            final char slash = XOsUtils.getFileSlashChar();
            return (f.charAt(f.length() - 1) != slash) ? f + slash : f;
        }
        
        return "";
    }
    
    /**
     * Get a file's directory path.
     * 
     * @param file the file
     * @return String
     */
    public static String getOsFileDirectory(final String file) {
        if (XStringUtils.isNotBlank(file)) {
            final String f = file.trim();
            final char slash = XOsUtils.getFileSlashChar();
            for (int i = f.length() - 1; i >= 0; --i) {
                if (f.charAt(i) == slash) {
                    return f.substring(0, i);
                }
            }
        }
        
        return "";
    }
}
