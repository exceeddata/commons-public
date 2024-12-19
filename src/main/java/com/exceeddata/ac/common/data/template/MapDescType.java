package com.exceeddata.ac.common.data.template;

import com.exceeddata.ac.common.data.type.Types;

/**
 * The Map Desc Type.
 *
 */
public class MapDescType extends KeyValueDescType {
    private static final long serialVersionUID = 1L;
    
    /** {@inheritDoc} */
    @Override
    public boolean isPrimitive() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isGroup() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isKeyValue() {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public byte getType() { return Types.MAP; }
    
    /** {@inheritDoc} */
    @Override
    public GroupDescType asGroupDescType() {
        throw new IllegalArgumentException("DATA_MAP_TO_GROUP_DESC_INVALID");
        }

    /** {@inheritDoc} */
    @Override
    public KeyValueDescType asKeyValueDescType() {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public PrimitiveDescType asPrimitiveDescType() {
        throw new IllegalArgumentException("DATA_MAP_TO_PRIMITIVE_DESC_INVALID");
    }
}
