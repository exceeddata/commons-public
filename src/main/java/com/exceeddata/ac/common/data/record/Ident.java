package com.exceeddata.ac.common.data.record;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.Serializable;

import com.exceeddata.ac.common.data.typedata.TypeData;

/**
 * <code>Ident</code> defines the specification for a sortable and self-describable structure without name.
 */
public interface Ident extends 
        Serializable, 
        Externalizable,
        Comparable<Ident> {
    
    public static final int EMPTY_HASH = 2147483647;
    
    /**
     * Get a shallow copy of the ident with additional capacity.
     * 
     * @param additions the additional datas
     * @return Ident
     */
    public Ident additionCopy (final TypeData ... additions);

    /**
     * Make a sub copy of the ident. 
     * 
     * @param beginPosition the begin position
     * @param endPosition the end position.
     * @return Ident
     */
    public Ident subCopy (final int beginPosition, final int endPosition);
    
    /**
     * Merge all contents from another ident into a new copy.
     * 
     * @param ident another ident
     * @return Ident
     */
    public Ident mergeCopy(final Ident ident);
    
    /**
     * Get a shallow copy of the record.
     * 
     * @return Ident
     */
    public Ident copy ();
    
    /**
     * Get the number of the elements.
     * 
     * @return int
     */
    public int size ();
    
    /**
     * Get the data at an specified position.
     * 
     * @param position the position
     * @return TypeData
     */
    public TypeData dataAt (final int position);
    
    /**
     * Set a data at the specified position.
     * 
     * @param position the position
     * @param data the data
     * @return Record
     */
    public Ident setAt (final int position, final TypeData data);
    
    @Override
    public void readExternal(final ObjectInput in) throws IOException;
}
