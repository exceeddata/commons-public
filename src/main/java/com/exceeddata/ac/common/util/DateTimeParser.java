package com.exceeddata.ac.common.util;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class DateTimeParser {
    private static final ZoneId SYSTEM_ZONEID = ZoneId.systemDefault();

    public static ZonedDateTime parseZonedDateTime(final String val) {
        final String value = val == null ? "" : val.trim();
        final int length = value.length();
        if (length < 8) { return null; } //minimum, 2016-1-1 or 20160101
        
        ZoneId zoneid = SYSTEM_ZONEID;
        int year = 1970, month = 1, day = 1, hour = 0, minute = 0, second = 0, nanos = 0;
        int index = -1, index2 = -1, timeEndIndex = -1;
        char c0 = 0, c1 = 0;
        
        /*
         * all number cases
         */
        if (isWholeDigits(value)) {
            if (length == 8 || length == 14 || length == 17 || length == 20 || length == 23) {
                year = Integer.parseInt(value.substring(0, 4));
                month = Integer.parseInt(value.substring(4, 6));
                day = Integer.parseInt(value.substring(6, 8));
                if (length > 8) {
                    hour = Integer.parseInt(value.substring(8, 10));
                    minute = Integer.parseInt(value.substring(10, 12));
                    second = Integer.parseInt(value.substring(12, 14));
                    if (length == 17) {
                        nanos = Integer.parseInt(value.substring(14, 17)) * 1000000;
                    } else if (length == 20) {
                        nanos = Integer.parseInt(value.substring(14, 20)) * 1000;
                    } else if (length == 23) {
                        nanos = Integer.parseInt(value.substring(14, 23));
                    }
                }
                try {
                    return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                } catch (DateTimeException e) {
                    return null;
                }
            } else {
                return null; //invalid format
            }
        }
        
        /*
         * extract year
         */
        try {
            year = Integer.parseInt(value.substring(index = 0, 4));
            if ((c1 = value.charAt(4)) != '-' && c1 != '/' && c1 != '年') {
                return null; //invalid format
            }
            index = 5;
            
            /*
             * extract month
             */
            c1 = value.charAt(index + 1);
            index2 = index + ((c1 == '-' || c1 == '/' || c1 == '月') ? 1 : 2);
            month = Integer.parseInt(value.substring(index, index2));
            index = index2 + 1;
            
            /*
             * extract day
             */
            if (length <= index + 1) {
                if(length == index) {
                    return null; 
                }
                day = Integer.parseInt(value.substring(index, length));
                return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
            } else {
                c1 = value.charAt(index + 1);
                index2 = index + ((c1 < '0' || c1 > '9') ? 1 : 2);
                day = Integer.parseInt(value.substring(index, index2));
                index = index2;
            }
            
            if (length <= index) { //ended
                return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
            }
            
            c1 = value.charAt(index);
            if (c1 == ' ' || c1 == 'T' || c1 == 't' || c1 == '日' || c1 == '+' || c1 == '-') {
                timeEndIndex = index;
            } else {
                //lenient parsing
                return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
            }
            
            if (c1 == ' ' || c1 == 'T' || c1 == 't' || c1 == '日') {
                //timestamp
                index = timeEndIndex + 1;
                if (length == index) {
                    return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                }

                index2 = value.indexOf("小时", index);
                if (index2 >= 0 && index2 - index <=2) {
                    hour = Integer.parseInt(value.substring(index, index2));
                    index = index2 + 2;
                } else {
                    index2 = value.indexOf(':', index);
                    if (index2 >= 0 && index2 - index <=2) {
                        hour = Integer.parseInt(value.substring(index, index2));
                        index = index2 + 1;
                    } else {
                        return null; //do not allow portion time
                    }
                }
                
                index2 = value.indexOf("分", index);
                if (index2 >= 0 && index2 - index <=2) {
                    minute = Integer.parseInt(value.substring(index, index2));
                    index = index2 + 1;
                } else {
                    index2 = value.indexOf(':', index + 1);
                    if (index2 >= 0 && index2 - index <=2) {
                        minute = Integer.parseInt(value.substring(index, index2));
                        index = index2 + 1;
                    } else {
                        return null; //do not allow portion time
                    }
                }

                if (length <= index + 1) {
                    if (length == index + 1) {
                        second = Integer.parseInt(value.substring(index, index + 1));
                        return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                    } else {
                        return null; //do not allow portion time
                    }
                }
                
                c1 = value.charAt(index + 1);
                index2 = index + ((c1 < '0' || c1 > '9') ? 1 : 2);
                second = Integer.parseInt(value.substring(index, index2));
                index = index2;
                
                if (length == index) {
                    return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                }
                
                c0 = value.charAt(index);
                if (c0 == '秒') {
                    if (length > index + 1) {
                        c0 = value.charAt(++index);
                    } else {
                        return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                    }
                }
                if (c0 == ' ' || c0 == '.' || c0 == 'Z' || c0 == 'z' || c0 == '+' || c0 == '-') {
                    timeEndIndex = index;
                } else {
                    return null; //bad characters
                }
                if (c0 == '.') {
                    int lastIndex = index2 = index + 1;
                    while (lastIndex < length && (c0 = value.charAt(lastIndex)) >= '0' && c0 <= '9') {
                        ++lastIndex;
                    }
                    if (lastIndex == index2 && lastIndex == length) {
                        //ended without number
                        return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                    } else {
                        final String n = value.substring(index2, Math.min(lastIndex, index2 + 9));
                        final int power = 9 - n.length();
                        nanos = Integer.parseInt(n) * ((int) Math.pow(10, power)); //protect against overflow
                        if (lastIndex == length) {
                            return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                        }
                        timeEndIndex = lastIndex;
                    }
                }
            }
            
            c0 = value.charAt(timeEndIndex);
            if (c0 == '+' || c0 == '-') {
                final String offset = getZoneOffset(value.substring(timeEndIndex));
                if (XStringUtils.isNotBlank(offset)) {
                    zoneid = ZoneOffset.of(offset);
                }
            } else {
                return null; //bad characters
            }
            
            return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
        } catch (NumberFormatException | DateTimeException e) {
            return null;
        }
    }
    
    public static ZonedDateTime parseZonedTime(final String val) {
        final String value = val == null ? "" : val.trim();
        final int length = value.length();
        if (length < 5) { return null; } //minimum, 1:1:1 or 100101
        
        final int year = 1970, month = 1, day = 1;
        ZoneId zoneid = SYSTEM_ZONEID;
        String separator = "";
        int hour = 0, minute = 0, second = 0, nanos = 0;
        int index = -1, index2 = -1, timeEndIndex = -1;
        char c0 = 0, c1 = 0;
        
        /*
         * all number cases
         */
        if (isWholeDigits(value)) {
            if (length == 6 || length == 9 || length == 12 || length == 15) {
                hour = Integer.parseInt(value.substring(0, 2));
                minute = Integer.parseInt(value.substring(2, 4));
                second = Integer.parseInt(value.substring(4, 6));
                if (length > 6) {
                    if (length == 17) {
                        nanos = Integer.parseInt(value.substring(6, 9)) * 1000000;
                    } else if (length == 20) {
                        nanos = Integer.parseInt(value.substring(9, 12)) * 1000;
                    } else if (length == 23) {
                        nanos = Integer.parseInt(value.substring(12, 15));
                    }
                }
                try {
                    return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                } catch (DateTimeException e) {
                    return null;
                }
            } else {
                return null; //invalid format
            }
        }
        
        try {
            if ((index2 = value.indexOf(separator = "小时")) < 0 && (index2 = value.indexOf(separator = ":")) < 0) {
                return null; //bad 
            }
            if (index2 > 2) {
                if ((index = value.lastIndexOf(' ', index2)) < 0 
                        && (index = value.lastIndexOf('T', index2)) < 0
                        && (index = value.lastIndexOf('t', index2)) < 0) {
                    return null; //bad hour
                }
                if (index2 - index > 3) {
                    return null; //bad hour
                }
                hour = Integer.parseInt(value.substring(index + 1, index2));
                index = index2 + separator.length();
            } else {
                hour = Integer.parseInt(value.substring(0, index2));
                index = index2 + separator.length();
            }
            
            if ((index2 = value.indexOf("分", index)) < 0 && (index2 = value.indexOf(':', index)) < 0) {
                return null; //bad hour
            }
            if (index2 >= 0 && index2 - index <=2) {
                minute = Integer.parseInt(value.substring(index, index2));
                index = index2 + 1;
            } else {
                return null;
            }
    
            if (length <= index + 1) {
                if (length == index + 1) {
                    second = Integer.parseInt(value.substring(index, index + 1));
                    return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                } else {
                    return null; //do not allow portion time
                }
            }
                    
            c1 = value.charAt(index + 1);
            index2 = index + ((c1 < '0' || c1 > '9') ? 1 : 2);
            second = Integer.parseInt(value.substring(index, index2));
            index = index2;
                    
            if (length == index) {
                return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
            }
            
            c0 = value.charAt(index);
            if (c0 == '秒') {
                if (length > index + 1) {
                    c0 = value.charAt(++index);
                } else {
                    return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                }
            }
            if (c0 == ' ' || c0 == '.' || c0 == 'Z' || c0 == 'z' || c0 == '+' || c0 == '-') {
                timeEndIndex = index;
            } else {
                return null; //bad characters
            }
            if (c0 == '.') {
                int lastIndex = index2 = index + 1;
                while (lastIndex < length && (c0 = value.charAt(lastIndex)) >= '0' && c0 <= '9') {
                    ++lastIndex;
                }
                if (lastIndex == index2 && lastIndex == length) {
                    //ended without number
                    return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                } else {
                    final String n = value.substring(index2, Math.min(lastIndex, index2 + 9));
                    final int power = 9 - n.length();
                    nanos = Integer.parseInt(n) * ((int) Math.pow(10, power)); //protect against overflow
                    if (lastIndex == length) {
                        return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
                    }
                    timeEndIndex = lastIndex;
                }
            }
                
            c0 = value.charAt(timeEndIndex);
            if (c0 == '+' || c0 == '-') {
                final String offset = getZoneOffset(value.substring(timeEndIndex));
                if (XStringUtils.isNotBlank(offset)) {
                    zoneid = ZoneOffset.of(offset);
                }
            } else {
                return null; //bad characters
            }
                
            return ZonedDateTime.of(year, month, day, hour, minute, second, nanos, zoneid);
        } catch (NumberFormatException | DateTimeException e) {
            return null;
        }
    }
    
    private static boolean isWholeDigits(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        
        for (int i = 0, len = cs.length(); i < len; ++i) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private static String getZoneOffset(final String val) {
        StringBuilder sb = new StringBuilder();
        sb.append(val.charAt(0));
        
        String remains = val.substring(1);
        int index = remains.indexOf(':');
        if (index < 0) { //no :
            int i = 0, len = Math.min(remains.length(), 6); //max 6
            char c;
            while (i < len) {
                c = remains.charAt(i++);
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                } else if (c == ' ' || c == '\t') {
                    break;
                } else {
                    return null; //invalid string
                }
            }
            final int sl = sb.length();
            if (sl == 1 || (sl != 2 && (sl / 2) * 2 == sl)) {
                return null;
            }
            return sb.toString();
        } else if (index == 1 || index == 2) {
            int i = 0, end = index + 3;
            if (end > remains.length()) {
                return null; //invalid string
            }
            
            char c;
            while (i < end){
                c = remains.charAt(i);
                if (Character.isDigit(c) || (c == ':' && i == index)) {
                    sb.append(c);
                    ++i;
                } else {
                    return null; //invalid string
                }
            }
            
            if (end == remains.length() || (c = remains.charAt(end)) == ' ' || c == '\t') {
                return sb.toString();
            }
            
            if (c == ':' && remains.length() > end + 2
                    && Character.isDigit(remains.charAt(end + 1)) && Character.isDigit(remains.charAt(end+2))
                    ) {
                sb.append(':').append(remains.charAt(end + 1)).append(remains.charAt(end + 2));
                if (remains.length() == end + 3 || (c = remains.charAt(end+3)) == ' ' ||  c == '\t') {
                    return sb.toString();
                }
                return null; //invalid string
            }
            return sb.toString();
        }
        return null;
    }
}
