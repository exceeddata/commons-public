package com.exceeddata.ac.common.data.record;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.exceeddata.ac.common.data.typedata.NullData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.util.XTypeDataUtils;

/**
 * One-Field Ident
 */
public final class OneIdent implements Ident {
    private static final long serialVersionUID = 1L;
    
    private TypeData data;
    
    public OneIdent () {
         data = NullData.INSTANCE;
    }
    
    public OneIdent (final TypeData data) {
        this.data = data;
    }
    
    public OneIdent (final OneIdent ident) {
        data = ident.data;
    }
    
    /** {@inheritDoc} */
    @Override
    public OneIdent clone () {
        return new OneIdent (this);
    }
    
    /** {@inheritDoc} */
    @Override
    public Ident additionCopy (final TypeData... additions) {
        if (additions == null) {
            return new OneIdent(data);
        } 
        switch(additions.length) {
            case 0: return new OneIdent(data);
            case 1: return new TwoIdent(data, additions[0]);
            default:
                return new ArrayIdent(additions.length + 1)
                            .add(data)
                            .additionCopy(additions);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Ident subCopy (final int beginPosition, final int endPosition) {
        return beginPosition == endPosition - 1 && beginPosition == 0 ? new OneIdent(data) : EmptyIdent.INSTANCE;
    }
    
    /** {@inheritDoc} */
    @Override
    public Ident mergeCopy(final Ident ident) {
        final int osize = ident.size();
        switch(osize) {
            case 0: return new OneIdent(data);
            case 1: return new TwoIdent(data, ident.dataAt(0));
            default:
                return new ArrayIdent(osize + 1)
                        .add(data)
                        .addAll(ident);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public OneIdent copy () {
        return new OneIdent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public int size () {
        return 1;
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData dataAt (final int position) {
        assert(position == 0);
        return data;
    }
    
    public TwoIdent add(final TypeData data) {
        return new TwoIdent(this.data, data);
    }
    
    /**
     * Set a data at the specified position.
     * 
     * @param position the position
     * @param data the data
     * @return Record
     */
    public OneIdent setAt (final int position, final TypeData data) {
        assert(position == 0);
        this.data = data;
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeByte(data.getType());
        data.writeExternal(out);
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        data = XTypeDataUtils.readTypeData(in, in.readByte());
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(Ident ident) {
        if (ident == null) {
            return 1;
        } else {
            final int thisSize = 1, otherSize = ident.size();
            
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
        if (obj instanceof Ident) {
            final Ident ident = (Ident) obj;
            return ident.size() == 1 && data.equals(ident.dataAt(0));
        }
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return data.hashCode();
    }
}
