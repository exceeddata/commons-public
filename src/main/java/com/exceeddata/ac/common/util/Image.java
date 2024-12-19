package com.exceeddata.ac.common.util;

import java.io.Serializable;
import java.util.Arrays;

public class Image implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int height;
    private int width;
    private byte[] data;
    private int frameid;
    
    public Image () {
        height = 0;
        width = 0;
        data = null;
        frameid = 0;
    }
    
    public Image(final int height, final int width, final byte[] data) {
        this.height = height;
        this.width = width;
        this.data = data;
        this.frameid = 0;
    }
    
    public Image(final int height, final int width, final byte[] data, final int frameid) {
        this.height = height;
        this.width = width;
        this.data = data;
        this.frameid = frameid;
    }
    
    public Image(final Image img) {
        if (img != null) {
            this.height = img.height;
            this.width = img.width;
            this.data = img.data != null && img.data.length > 0 ? Arrays.copyOf(img.data, img.data.length) : null;
            this.frameid = img.frameid;
        } else {
            this.height = 0;
            this.width = 0;
            this.data = null;
            this.width = 0;
        }
    }
    
    @Override
    public Image clone() {
        return new Image(this);
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public int getFrameid() {
        return frameid;
    }
}
