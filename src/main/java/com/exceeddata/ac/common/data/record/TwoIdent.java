package com.exceeddata.ac.common.data.record;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.exceeddata.ac.common.data.typedata.NullData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.util.XTypeDataUtils;

/**
 * Two-field ident
 */
public final class TwoIdent implements Ident {
    private static final long serialVersionUID = 1L;
    
    private TypeData data1;
    private TypeData data2;
    
    public TwoIdent () {
         data1 = NullData.INSTANCE;
         data2 = NullData.INSTANCE;
    }
    
    public TwoIdent (
            final TypeData data1,
            final TypeData data2) {
        this.data1 = data1;
        this.data2 = data2;
    }
    
    public TwoIdent (final TwoIdent ident) {
        this.data1 = ident.data1;
        this.data2 = ident.data2;
    }
    
    /** {@inheritDoc} */
    @Override
    public TwoIdent clone () {
        return new TwoIdent (this);
    }
    
    /** {@inheritDoc} */
    @Override
    public Ident additionCopy (final TypeData... additions) {
        if (additions == null || additions.length == 0) {
            return new TwoIdent(data1, data2);
        }
        
        return new ArrayIdent(additions.length + 2)
                    .add(data1)
                    .add(data2)
                    .additionCopy(additions);
    }

    /** {@inheritDoc} */
    @Override
    public Ident subCopy (final int beginPosition, final int endPosition) {
        final int bindex = beginPosition >= 0 ? beginPosition : 0;
        final int epos = endPosition <= 2 ? endPosition : 2;
        final int subsize = epos - bindex;
        if (subsize <= 0) {
            return EmptyIdent.INSTANCE;
        } else if (subsize == 1) {
            return bindex == 0 ? new OneIdent(data1) : new OneIdent(data2);
        } else {
            return new TwoIdent(data1, data2);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Ident mergeCopy(final Ident ident) {
        final int osize = ident.size();
        if (osize == 0) {
            return new TwoIdent(data1, data2);
        }
        
        return new ArrayIdent(osize + 2)
                .add(data1)
                .add(data2)
                .addAll(ident);
    }
    
    /** {@inheritDoc} */
    @Override
    public TwoIdent copy () {
        return new TwoIdent(data1, data2);
    }
    
    /** {@inheritDoc} */
    @Override
    public int size () {
        return 2;
    }
    
    /**
     * Get the data at an specified position.
     * 
     * @param position the position
     * @return TypeData
     */
    public TypeData dataAt (final int position) {
        assert (position == 0 || position == 1);
        return position == 0 ? data1 : data2;
    }
    
    /**
     * Set a data at the specified position.
     * 
     * @param position the position
     * @param data the data
     * @return Record
     */
    public TwoIdent setAt (final int position, final TypeData data) {
        assert (position == 0 || position == 1);
        if (position == 0) {
            data1 = data;
        } else {
            data2 = data;
        }
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeByte(data1.getType());
        data1.writeExternal(out);
        out.writeByte(data2.getType());
        data2.writeExternal(out);
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        data1 = XTypeDataUtils.readTypeData(in, in.readByte());
        data2 = XTypeDataUtils.readTypeData(in, in.readByte());
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final Ident ident) {
        if (ident == null) {
            return 1;
        } else {
            final int thisSize = 2, otherSize = ident.size();
            
            if (otherSize == 0) {
                return 1;
            } else {
                final int minSize = Math.min(thisSize, otherSize);
                int result = 0;
                for (int i = 0; i < minSize; ++i) {
                    if ((result = this.dataAt(i).compareTo(ident.dataAt(i))) != 0) {
                        return result;
                    }
                }
                
                return thisSize > minSize ? 1 : otherSize > minSize ? -1 : 0;
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TwoIdent) {
            final TwoIdent ident = (TwoIdent) obj;
            return data1.equals(ident.data1) && data2.equals(ident.data2);
        }
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return 109 * data1.hashCode() + data2.hashCode();
    }
}
