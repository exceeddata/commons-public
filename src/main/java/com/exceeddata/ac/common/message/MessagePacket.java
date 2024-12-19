package com.exceeddata.ac.common.message;

import java.util.Arrays;

public class MessagePacket implements MessageContent {
    private static final long serialVersionUID = 1L;
    
    private int channelID;
    private long messageID;
    private boolean error;
    private long offset;
    private int length;
    private MessageDirection direction;
    private byte[] data;
    
    public MessagePacket() {
        channelID = 1;
        messageID = 0l;
        error = false;
        offset = 0l;
        length = 0;
        direction = MessageDirection.TX;
        data = null;
    }
    
    public MessagePacket(final MessagePacket packet) {
        if (packet != null) {
            channelID = packet.channelID;
            messageID = packet.messageID;
            error = packet.error;
            offset = packet.offset;
            length = packet.length;
            direction = packet.direction;
            data = packet.data != null ? Arrays.copyOf(packet.data, packet.data.length) : null;
        } else {
            channelID = 1;
            messageID = 0l;
            error = false;
            offset = 0l;
            length = 0;
            direction = MessageDirection.TX;
            data = null;
        }
    }
    
    @Override
    public MessagePacket clone() {
        return new MessagePacket(this);
    }
    
    @Override
    public int getChannelID() {
        return channelID;
    }
    
    public void setChannelID(final int channelID) {
        this.channelID = channelID;
    }

    @Override
    public long getMessageID() {
        return messageID;
    }
    
    public void setMessageID(final long messageID) {
        this.messageID = messageID;
    }

    @Override
    public boolean isError() {
        return error;
    }
    
    public void setError(final boolean error) {
        this.error = error;
    }

    @Override
    public long getNanosOffset() {
        return offset;
    }
    
    public void setNanosOffset(final long offset) {
        this.offset = offset;
    }

    @Override
    public int getDataLength() {
        return length;
    }
    
    public void setDataLength(final int length) {
        this.length = length;
    }

    @Override
    public MessageDirection getDirection() {
        return direction;
    }
    
    public void setDirection(final MessageDirection direction) {
        this.direction = direction;
    }

    @Override
    public byte[] getData() {
        return data;
    }
    
    public void setData(final byte[] data) {
        this.data = data;
    }
}
