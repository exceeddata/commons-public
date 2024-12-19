package com.exceeddata.ac.common.data.record;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

import com.exceeddata.ac.common.data.typedata.NullData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.util.XNumberUtils;
import com.exceeddata.ac.common.util.XTypeDataUtils;

/**
 * Array Ident
 */
public class ArrayIdent implements Ident {
    private static final long serialVersionUID = 1L;
    private static final TypeData[] DEFAULTCAPACITY_EMPTY_DATAS = {};
    
    private TypeData[] datas;
    private int isize;
    
    public ArrayIdent () {
         datas = DEFAULTCAPACITY_EMPTY_DATAS;
         isize = 0;
    }
    
    public ArrayIdent (int initialCapacity) {
        datas = new TypeData[initialCapacity];
        isize = 0;
   }
    
    public ArrayIdent (
            final TypeData[] datas,
            final int size) {
        this.datas = datas;
        this.isize = size;
    }
    
    public ArrayIdent (final ArrayIdent ident) {
        isize = ident.isize;
        if (ident.datas != null && ident.datas.length > 0) {
            datas = new TypeData[ident.isize];
            System.arraycopy(ident.datas, 0, datas, 0, isize);
        } else {
            datas = DEFAULTCAPACITY_EMPTY_DATAS;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public ArrayIdent clone () {
        return new ArrayIdent (this);
    }
    
    /** {@inheritDoc} */
    @Override
    public ArrayIdent additionCopy (final TypeData... additions) {
        if (additions == null || additions.length == 0) {
            return copy();
        }
        
        final int proposedsize = this.isize + additions.length;
        final int actualsize = this.datas.length >= proposedsize ? this.datas.length : proposedsize;
        
        if (isize > 0) {
            final TypeData[] newDatas = new TypeData[actualsize];
            System.arraycopy(datas, 0, newDatas, 0, isize);
            System.arraycopy(additions, 0, newDatas, isize, additions.length);
            return new ArrayIdent(newDatas, isize + additions.length);
        } else {
            return new ArrayIdent(additions, additions.length);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Ident subCopy (final int beginPosition, final int endPosition) {
        final int bindex = beginPosition >= 0 ? beginPosition : 0;
        final int epos = endPosition <= isize ? endPosition : isize;
        final int subsize = epos - bindex;
        if (subsize <= 0) {
            return EmptyIdent.INSTANCE;
        } else if (subsize == 1) {
            return new OneIdent(datas[bindex]);
        } else if (subsize == 2) {
            return new TwoIdent(datas[bindex], datas[epos - 1]);
        }
        
        final TypeData[] subdatas = new TypeData[subsize];
        System.arraycopy(datas, bindex, subdatas, 0, subsize);
        
        return new ArrayIdent(subdatas, subsize);
    }
    
    /** {@inheritDoc} */
    @Override
    public Ident mergeCopy(final Ident ident) {
        final int osize = ident.size();
        switch (osize) {
            case 0: return copy();
            case 1: return additionCopy(ident.dataAt(0));
            case 2: return additionCopy(ident.dataAt(0), ident.dataAt(1));
            default:
                final int actualsize = isize + osize;
                final TypeData[] additions = ((ArrayIdent) ident).unsafeDatas();
                final TypeData[] newDatas = new TypeData[actualsize];
                System.arraycopy(datas, 0, newDatas, 0, isize);
                System.arraycopy(additions, 0, newDatas, isize, osize);
                return new ArrayIdent(newDatas, actualsize);       
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public ArrayIdent copy () {
        return new ArrayIdent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public int size () {
        return isize;
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeData dataAt (final int position) {
        return datas[position];
    }
    
    /**
     * Add a data.
     * 
     * @param data the data
     * @return Record
     */
    public ArrayIdent add(final TypeData data) {
        if (data == null) {
            throw new IllegalArgumentException("DATA_IDENT_DATA_UNEXPECTEDLY_NULL");
        }
        if (isize >= datas.length) {
            ensureCapacity(isize + 2);
        }
        datas[isize++] = data;
        return this;
    }
    
    /**
     * Add all contents from another ident.
     * 
     * @param ident another ident
     * @return Ident
     */
    public ArrayIdent addAll(final Ident ident) {
        final int osize = ident.size();
        final int nsize = this.isize + osize;
        if (nsize > datas.length) {
            ensureCapacity(nsize);
        }
        for (int i = 0; i < osize; ++i) {
            add(ident.dataAt(i));
        }
        return this;
    }
    
    private void ensureCapacity(final int nsize) {
        final TypeData[] ndatas = new TypeData[nsize];
        if (isize > 0) {
            System.arraycopy(datas, 0, ndatas, 0, isize);
        }
        
        datas = ndatas;
    }
    
    /** {@inheritDoc} */
    @Override
    public ArrayIdent setAt (final int position, final TypeData data) {
        assert(position >= 0 && position < isize);
        datas[position] = data;
        return this;
    }
    
    /**
     * Remove a data at the specified position.
     * 
     * @param position the position
     * @return TypeData
     */
    public TypeData removeAt (final int position) {
        if (position < 0 || position >= isize) {
            return NullData.INSTANCE;
        }
        
        final TypeData data = datas[position];
        final int numMoved = isize - position - 1;
        if (numMoved > 0) {
            System.arraycopy(datas, position+1, datas, position, numMoved);
        }
        
        datas[--isize] = null;
        
        return data;
    }
    
    /**
     * Clear all elements.
     * 
     * @return Record
     * 
     */
    public ArrayIdent clear () {
        datas = DEFAULTCAPACITY_EMPTY_DATAS;
        isize = 0;

        return this;
    } 
    
    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (isize == 0) {
            out.writeByte(XNumberUtils.BYTE_ZERO);
            return;
        }
        
        //max byte is 127, max short is 32767
        if (isize >> 7 == 0) {
            out.writeByte(XNumberUtils.BYTE_ONE); 
            out.writeByte(isize);
        } else {
            if (isize >> 15 == 0) {
                out.writeByte(XNumberUtils.BYTE_TWO); 
                out.writeShort(isize);
            } else {
                out.writeByte(XNumberUtils.BYTE_FOUR); 
                out.writeInt(isize);
            }
        }
        
        for (int i = 0; i < isize; ++i) {
            out.writeByte(datas[i].getType());
            datas[i].writeExternal(out);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        switch(in.readByte()) {
            case XNumberUtils.BYTE_ZERO:     isize = 0; datas = DEFAULTCAPACITY_EMPTY_DATAS; return;
            case XNumberUtils.BYTE_ONE:      isize = in.readByte(); break;
            case XNumberUtils.BYTE_TWO:      isize = in.readShort(); break;
            default:                        isize = in.readInt();
        }
        
        datas = new TypeData[isize];
        for (int i = 0; i < isize; ++i) {
            datas[i] = XTypeDataUtils.readTypeData(in, in.readByte());
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final Ident ident) {
        if (ident == null) {
            return 1;
        } else {
            final int thisSize = size(), otherSize = ident.size();
            
            if (otherSize == 0) {
                return thisSize == 0 ? 0 : 1;
            } else if (thisSize == 0) {
                return -1;
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
            final int thisSize = size(), otherSize = ident.size();
            if (thisSize == otherSize) {
                for (int i = 0; i < thisSize; ++i) {
                    if (!dataAt(i).equals(ident.dataAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        if (isize == 0) {
            return EMPTY_HASH;
        }
        
        int state = datas[0].hashCode();
        for (int i = 1; i < isize; ++i) {
            state = state * 109 + datas[i].hashCode();
        }
        
        return state;
    }
    
    /**
     * Return a copy of the datas.
     * 
     * @return TypeData[]
     */
    public TypeData[] datas() {
        return Arrays.copyOf(datas, isize);
    }
    
    /**
     * Return a copy of the datas.
     * 
     * @return TypeData[]
     */
    public TypeData[] unsafeDatas() {
        return datas;
    }
}
