package com.exceeddata.ac.common.message;

import com.exceeddata.ac.common.data.record.Hashing;

public final class MessageConstants {
    
    public static final String DEVICE = "msg_device";
    public static final String TIME = "msg_time";
    public static final String OFFSET = "msg_offset";
    public static final String ID = "msg_id";
    public static final String CHANNEL = "channel";
    public static final String ERROR = "error";
    public static final String DIRECTION = "direction";
    public static final String CONTENT = "content";
    public static final String LENGTH = "length";
    
    public static final int DEVICE_HASH = Hashing.getHash(DEVICE);
    public static final int TIME_HASH   = Hashing.getHash(TIME);
    public static final int OFFSET_HASH = Hashing.getHash(OFFSET);
    public static final int ID_HASH   = Hashing.getHash(ID);
    public static final int CHANNEL_HASH = Hashing.getHash(CHANNEL);
    public static final int ERROR_HASH   = Hashing.getHash(ERROR);
    public static final int DIRECTION_HASH = Hashing.getHash(DIRECTION);
    public static final int CONTENT_HASH = Hashing.getHash(CONTENT);
    public static final int LENGTH_HASH = Hashing.getHash(LENGTH);

    private MessageConstants() {}
}
