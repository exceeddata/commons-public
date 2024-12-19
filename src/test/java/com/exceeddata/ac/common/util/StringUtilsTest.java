package com.exceeddata.ac.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {
    public void testquoteStringArray() {
        String[] arr1 = {"", "\"", "\"\"", "\"ab\"c", "abc", "\"abc\""};
        
        String[] newarr1 = XStringUtils.quoteStringArray(arr1, '"');
        assertEquals(newarr1[0], "\"\"");
        assertEquals(newarr1[1], "\"\"\"\"");
        assertEquals(newarr1[2], "\"\"");
        assertEquals(newarr1[3], "\"\"\"ab\"\"c\"");
        assertEquals(newarr1[4], "\"abc\"");
        assertEquals(newarr1[5], "\"abc\"");
    }
    
    public void testIsBlank() {
        assertEquals(true, XStringUtils.isBlank(null));
        assertEquals(true, XStringUtils.isBlank(""));
        assertEquals(true, XStringUtils.isBlank("   "));
        assertEquals(false, XStringUtils.isBlank(" a"));
        assertEquals(false, XStringUtils.isBlank("a"));
        assertEquals(false, XStringUtils.isBlank("a "));
    }
    
    public void testIsNotBlank() {
        assertEquals(false, XStringUtils.isNotBlank(null));
        assertEquals(false, XStringUtils.isNotBlank(""));
        assertEquals(false, XStringUtils.isNotBlank("   "));
        assertEquals(true, XStringUtils.isNotBlank(" a"));
        assertEquals(true, XStringUtils.isNotBlank("a"));
        assertEquals(true, XStringUtils.isNotBlank("a "));
    }
    
    public void testIsEmpty() {
        assertEquals(true, XStringUtils.isEmpty(null));
        assertEquals(true, XStringUtils.isEmpty(""));
        assertEquals(false, XStringUtils.isEmpty("   "));
    }
    
    public void testIsNotEmpty() {
        assertEquals(false, XStringUtils.isNotEmpty(null));
        assertEquals(false, XStringUtils.isNotEmpty(""));
        assertEquals(true, XStringUtils.isNotEmpty("   "));
    }
    
    public void testRegExpPattern() {
        Pattern p = Pattern.compile("student.*(\\d+)");
        Matcher m = p.matcher("student:1");
        assertEquals(m.find(), true);
        assertEquals(m.groupCount(), 1);
        assertEquals(m.group(0), "student:1");
        assertEquals(m.group(1), "1");
    }
    
    public void testDecimalPattern() {
        String r = "\\d*\\.\\d+|\\d+e[\\+|-]?\\d+";
        Pattern p = Pattern.compile(r, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        
        assertEquals(p.matcher("1.5").matches(), true);
        assertEquals(p.matcher("0.5").matches(), true);
        assertEquals(p.matcher("53.75").matches(), true);
        assertEquals(p.matcher(".0").matches(), true);
        assertEquals(p.matcher("1").matches(), false);
        assertEquals(p.matcher("1e-5").matches(), true);
        assertEquals(p.matcher("1e5").matches(), true);
    }
    
    public void testRemoveCodeComment() {
        String sql;
        
        //remove multi-line comment
        sql = "select /* test */ c from t1 where a = 5 and b ='/* s5 */' /* done */";
        assertEquals(
                XStringUtils.removeCodeComments(sql),
                "select   c from t1 where a = 5 and b ='/* s5 */'  "
                );
    
        //remove multi-line comment with line break
        sql = "select /* test */ c from t1 where a = 5 and b ='/* s5 */'; \n"
                + " /* done \n\n */ \n" // line break
                + "select c2, c3 /* test2 */ from t2 where a = 5 and b ='/* s5 */'; /* foo */";
        assertEquals(
                XStringUtils.removeCodeComments(sql),
                "select   c from t1 where a = 5 and b ='/* s5 */'; \n"
                        + "   \n"
                        + "select c2, c3   from t2 where a = 5 and b ='/* s5 */';  "
                );
        
        //remove line comment
        sql = "select c from t1 // foo \n"
                + "where a = 5 // b = 5\n"
                + "      and b ='/* s5 */' // done";
        assertEquals(
                XStringUtils.removeCodeComments(sql),
                "select c from t1  \n"
                        + "where a = 5  \n"
                        + "      and b ='/* s5 */'  "
                );
        
        sql = "select c from t1 // /* foo */ \n"
                + "where a = 5 // b = 5 /* // foo */\n"
                + "      and b ='/* s5 */' // done";
        assertEquals(
                XStringUtils.removeCodeComments(sql),
                "select c from t1  \n"
                        + "where a = 5  \n"
                        + "      and b ='/* s5 */'  "
                );
    }
}
