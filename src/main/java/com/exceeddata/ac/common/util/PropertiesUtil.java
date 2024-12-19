package com.exceeddata.ac.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties properties;

    public static Properties init(String configName) {
        properties = new Properties();
        InputStream in = null;

        try {
            in = PropertiesUtil.class.getClassLoader().getResourceAsStream(configName);
            if(null == in){
                return properties;
            }else{
                properties.load(new InputStreamReader(in,"utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public static String getProperty(String key) {
        if (properties == null) {
            properties = init("exdException.properties");
        }
        return properties.getProperty(key);
    }
}
