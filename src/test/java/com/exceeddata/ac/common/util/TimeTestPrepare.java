package com.exceeddata.ac.common.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TimeTestPrepare {

    public static void main(final String[] args) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        String content = "default0813a754-b200-424e-9b6c-612ac42be497,\"{\"\"V183\"\":{\"\"sts\"\":0,\"\"val\"\":\"\"1\"\"},\"\"V182\"\":{\"\"sts\"\":0,\"\"val\"\":\"\"4.59375\"\"},\"\"V181\"\":{\"\"sts\"\":0,\"\"val\"\":\"\"19.68\"\"}}\"\n";
        
        try {
            fw = new FileWriter("/tmp/data");
            bw = new BufferedWriter(fw);
            for (int i = 0; i < 20000000; ++i) {
                bw.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
            }
        }
    }
}
