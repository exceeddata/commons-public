package com.exceeddata.ac.common.util;

public final class XComplexUtils {

    private XComplexUtils() {}
    
    public static boolean isComplexNumber(final String value) {
        final String s = value != null ? value.trim() : "";
        final int len = s.length();
        if (len == 0) {
            return false;
        }

        char c = s.charAt(len - 1);
        if (len == 1) {
            return c == 'i' || c == 'I' || (c >= '0' && c <= '9');
        } else if (c != 'i' && c != 'I') {
            try { //validate real
                Double.parseDouble(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        c = s.charAt(0);
        
        final StringBuilder realsb = new StringBuilder();
        final StringBuilder imagsb = new StringBuilder();
        boolean signed = false, dot = false;
        int index = 0;
        if (signed = (c == '+' || c == '-')) {
            realsb.append(c);
            index = 1;
        }
        
        while (index < len) {
            c = s.charAt(index++);
            if (c >= '0' && c <= '9') {
                realsb.append(c);
            } else if (c == '.') {
                if (dot) { 
                    return false; 
                }
                realsb.append(c);
                dot = true;
            } else if ( c == 'E' || c == 'e') {
                if (index == len) { 
                    return false; 
                }
                realsb.append(c);
                
                if ((c = s.charAt(index)) == '+' || c == '-') {
                    realsb.append(c);
                    if (++index == len) { 
                        return false; 
                    }
                    if ((c = s.charAt(index)) >= '0' && c <= '9') {
                        realsb.append(c);
                        ++index;
                    }
                }
            } else if ( c == ' ') {
                while (s.charAt(index) == ' ') { //trim the space
                    ++index;
                }
                if (index != len && (c = s.charAt(index)) == '+' || c == '-') {
                    imagsb.append(c);
                    if (++index == len) {
                        return false;
                    }
                    break;
                }
                return false;
            } else if (c == '+' || c == '-') {
                if (index == len) {
                    return false; //invalid
                }
                imagsb.append(c);
                break;
            } else if ( c == 'i' || c == 'I') { //in fact, this should be imaginary
                if (index != len) {
                    return false;
                }
                try {
                    if (realsb.length() == 1 && signed) {
                        return true;
                    }
                    Double.parseDouble(realsb.toString());
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            } else {
                return false; //bad characters
            }
        }
        
        if (index == len) {
            try { //real only
                Double.parseDouble(realsb.toString());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        if (((c = s.charAt(len - 1)) != 'i' && c != 'I') || (realsb.length() == 1 && signed)) {
            return false;
        }

        //validate real portion is correct
        try {
            Double.parseDouble(realsb.toString());
        } catch (NumberFormatException e) {
            return false;
        }
        
        while (s.charAt(index) == ' ') { //trim the space after +/- sign
            ++index;
        }
        
        if (index == len - 1) {
            return true;
        }
        
        imagsb.append(s.substring(index, len - 1));
        try {
            if (imagsb.length() == 1 && ((c = imagsb.charAt(0)) == '+' || c == '-')) {
                return true;
            }
            Double.parseDouble(imagsb.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
