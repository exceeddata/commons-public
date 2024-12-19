package com.exceeddata.ac.common.data.record;

import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.exceeddata.ac.common.data.typedata.NullData;
import com.exceeddata.ac.common.data.typedata.TypeData;

/**
 * Empty Ident
 */
public final class EmptyIdent implements Ident {
    private static final long serialVersionUID = 1L;
    public static final EmptyIdent INSTANCE = new EmptyIdent();
    
    public EmptyIdent () {
    }
    
    /** {@inheritDoc} */
    @Override
    public EmptyIdent clone () {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public Ident additionCopy (final TypeData... additions) {
        if (additions == null) {
            return this;
        }
        switch (additions.length) {
            case 0: return this;
            case 1: return new OneIdent(additions[0]);
            case 2: return new TwoIdent(additions[0], additions[1]);
            default: return new ArrayIdent(additions, additions.length);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Ident mergeCopy(final Ident ident) {
        return ident.copy();
    }
    
    /** {@inheritDoc} */
    @Override
    public EmptyIdent subCopy (final int beginIndex, final int endIndex) {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public EmptyIdent copy () {
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public int size () {
        return 0;
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData dataAt (final int position) {
        return NullData.INSTANCE;
    }
    
    /** {@inheritDoc} */
    @Override
    public Ident setAt (final int position, final TypeData data) {
        throw new ArrayIndexOutOfBoundsException ("DATA_EMPTYIDENT_SET_INVALID");
    }
    
    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) {
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(final ObjectInput in) {
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final Ident ident) {
        return this == ident ? 0 : ident == null ? 1 : ident.size() == 0 ? 0 : -1;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof Ident && ((Ident) obj).size() == 0;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return EMPTY_HASH;
    }
}
