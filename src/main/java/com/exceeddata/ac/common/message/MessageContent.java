package com.exceeddata.ac.common.message;

import java.io.Serializable;

public interface MessageContent extends Serializable {
    /**
     * Get the channel ID.
     * 
     * @return channel id
     */
    public int getChannelID();

    /**
     * Get the Message ID.
     * 
     * @return message id
     */
    public long getMessageID();
    
    /**
     * Is the content an error frame.
     * 
     * @return true or false
     */
    public boolean isError();
    
    /**
     * Get the Nanos Offset
     * @return offset
     */
    public long getNanosOffset();

    /**
     * Get the data length.
     * 
     * @return length
     */
    public int getDataLength();

    /**
     * Get the message direction.
     * 
     * @return MessageDirection
     */
    public MessageDirection getDirection();
    
    /**
     * Get the data bytes.
     * 
     * @return byte[]
     */
    public byte[] getData();
}
