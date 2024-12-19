package com.exceeddata.ac.common.data.template;

import com.exceeddata.ac.common.data.type.Types;

public class PrimitiveDescType extends DescType {
    private static final long serialVersionUID = 1L;

    private byte type = Types.STRING;
    private int scale = 0;
    
    public PrimitiveDescType(final byte type) {
        this.type = type;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isPrimitive() {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isGroup() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isKeyValue() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType() { return type; }

    /**
     * Get the scale.
     * 
     * @return the scale in int
     */
    public int getScale() { return scale; }
    
    /**
     * Set the scale.
     * 
     * @param scale the scale
     * @return PrimitiveDescType
     */
    public PrimitiveDescType setScale(final int scale) {
        if (scale >=0) {
            this.scale = scale;
        }
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public GroupDescType asGroupDescType() {
        throw new IllegalArgumentException("DATA_PRIMITIVE_TO_GROUP_DESC_INVALID");
    }

    /** {@inheritDoc} */
    @Override
    public KeyValueDescType asKeyValueDescType() {
        throw new IllegalArgumentException("DATA_PRIMITIVE_TO_KEYVALUE_DESC_INVALID");
    }

    /** {@inheritDoc} */
    @Override
    public PrimitiveDescType asPrimitiveDescType() {
        return this;
    }
}
