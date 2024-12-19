package com.exceeddata.ac.common.data.record;

public final class Hashing {
    private Hashing() {}
    
    public static int getHash(final String key) {
        // better hash function
        // https://vanilla-java.github.io/2018/08/12/Why-do-I-think-Stringhash-Code-is-poor.html
        int state = 0, length = key.length();
        for (int i = 0; i < length; ++i) {
            state = state * 109 + key.charAt(i);
        }
        return state;
    } 
}
