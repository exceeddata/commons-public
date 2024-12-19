package com.exceeddata.ac.common.data.template;

/**
 * An interface for converting to Desc.
 *
 */
public interface DescConverter {
    /**
     * Convert a element definition into Desc.
     * 
     * @param definition the element definition.
     * @param position the element position.
     * @return Desc
     */
    public Desc describe(final String definition, final int position);
    
    /**
     * Convert a type name into a Record type.
     * 
     * @param type the type name
     * @return DescType
     */
    public DescType typify(final String type);
}
