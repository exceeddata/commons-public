package com.exceeddata.ac.common.data.template;

import java.io.Serializable;

public abstract class DescType implements Serializable  {
    private static final long serialVersionUID = 1L;

    /**
     * Get column type.
     * 
     * @return byte
     */
    public abstract byte getType();
    
    /**
     * Get whether the Desc is primitive.
     * 
     * @return true or false
     */
    public abstract boolean isPrimitive();
    
    /**
     * Get whether the Desc is group.
     * 
     * @return true or false
     */
    public abstract boolean isGroup();
    
    /**
     * Get whether the Desc is key-value.
     * 
     * @return true or false
     */
    public abstract boolean isKeyValue();
    
    /**
     * Convert to GroupDescType instance.
     * 
     * @return GroupDescType
     */
    public abstract GroupDescType asGroupDescType();
    
    /**
     * Convert to KeyValueDescType instance.
     * 
     * @return KeyValueDescType
     */
    public abstract KeyValueDescType asKeyValueDescType();
    
    /**
     * Convert to PrimitiveDescType instance.
     * 
     * @return PrimitiveDescType
     */
    public abstract PrimitiveDescType asPrimitiveDescType();
    
}
