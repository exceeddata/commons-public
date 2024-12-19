package com.exceeddata.ac.common.util.calc;

import com.exceeddata.ac.common.data.typedata.TypeData;

public final class XCalcException {
    private XCalcException() {}
    
    public static String getNonNumericException(final TypeData d) {
        final String s= d.toString();
        if (s.length() > 10) {
            return "'" + s.substring(0, 10) + "...' IS NOT NUMBER";
        } else {
            return "'" + s + "' IS NOT NUMBER";
        }
    }
}
