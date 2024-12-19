package com.exceeddata.ac.common.data.record;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.exceeddata.ac.common.data.typedata.NullData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.util.XNumberUtils;
import com.exceeddata.ac.common.util.XStringUtils;
import com.exceeddata.ac.common.util.XTypeDataUtils;

/**
 * <code>Record</code> defines the specification for a sortable and self-describable record structure 
 * for containing element.
 * 
 * MAX_LEN = 0x40000000,  Largest power of 2 that fits in an int
 */
public final class Record implements 
        Serializable, 
        Externalizable,
        Comparable<Record> {
    private static final long serialVersionUID = 1L;
    private static final double incrementLoadFactor = 0.8;  // 0 < loadFactor < 1
    
    private static final int TOMBSTONE_POSITION = -1;
    private static final int EMPTY_POSITION = -2;
    
    private static final String[] DEFAULTCAPACITY_EMPTY_NAMES = {};
    private static final TypeData[] DEFAULTCAPACITY_EMPTY_DATAS = {};
    private static final int DEFAULTCAPACITY_TABLE = 10;
    private static final int SMALL_RECORD_LIMIT = 20;
    private static final int SMALL_ADDITIONAL_CAPACITY = 5;
    private static final int MIN_ADDITIONAL_CAPACITY = 10;
    private static final int MAX_ADDITIONAL_CAPACITY = 50;
    private static final TypeData[] NULLS = new TypeData[100];
    
    static {
        Arrays.fill(NULLS, NullData.INSTANCE);
    }
    
    private int[] positions = null;  // Length is always a power of 2. Each element is either null, tombstone, or data. At least one element must be null.
    private String[] names = null; // indexed with data
    private TypeData[] datas = null; //indexed with name
    private int rsize = 0;        // Number of items stored in hash table
    
    public Record () {
        names = new String[DEFAULTCAPACITY_TABLE];
        datas = new TypeData[DEFAULTCAPACITY_TABLE];
        resize(DEFAULTCAPACITY_TABLE);
    }
    
    public Record (final int initialCapacity) {
        names = new String[initialCapacity];
        datas = new TypeData[initialCapacity];
        resize(initialCapacity);
   }
    
    protected Record (
            final String[] names, 
            final TypeData[] datas,
            final int[] positions,
            final int size) {
        this.names = names;
        this.datas = datas;
        this.positions = positions;
        this.rsize = size;
    }
    
    public Record (final Record record) {
        rsize = record.rsize;
        
        if (record.positions != null) {
            positions = new int[record.positions.length];
            if (record.positions.length > 0) {
                System.arraycopy(record.positions, 0, positions, 0, record.positions.length);
            }
        } else {
            resize(DEFAULTCAPACITY_TABLE);
        }
        
        if (record.names != null && record.names.length > 0) {
            names = new String[record.names.length];
            datas = new TypeData[record.datas.length];
            System.arraycopy(record.names, 0, names, 0, record.rsize);
            System.arraycopy(record.datas, 0, datas, 0, record.rsize);
        } else {
            names = DEFAULTCAPACITY_EMPTY_NAMES;
            datas = DEFAULTCAPACITY_EMPTY_DATAS;
        }
    }
    
    public Record (final Record record, final int additionalCapacity) {
        rsize = record.rsize;
        
        final int proposedsize = record.rsize + additionalCapacity;
        final int actualsize = record.datas.length >= proposedsize ? record.datas.length : proposedsize;
        if (rsize > 0) {
            names = new String[actualsize];
            System.arraycopy(record.names, 0, names, 0, rsize);
            
            datas = new TypeData[actualsize];
            System.arraycopy(record.datas, 0, datas, 0, rsize);
     
            //no need to reallocate 2-power, will be done when actual insert
            positions = new int[record.positions.length];
            System.arraycopy(record.positions, 0, positions, 0, record.positions.length);
            
        } else {
            names = new String[record.names.length + additionalCapacity];
            datas = new TypeData[record.datas.length + additionalCapacity];
            clear();
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Record clone () {
        return new Record (this);
    }
    
    /**
     * Get a shallow copy of the record with additional capacity.
     * 
     * @param additionalCapacity the additional capacity to allocate
     * @return Record
     */
    public Record additionCopy (final int additionalCapacity) {
        return new Record(this, additionalCapacity);
    }
    
    /**
     * Advanced usage, use with caution, only if you know the record is fixed schema.
     * 
     * @param beginPosition the begin position
     * @param endPosition the end position
     * @return Record
     */
    public Record subCopy(final int beginPosition, final int endPosition) {
        final int bpos = beginPosition >= 0 ? beginPosition : 0;
        final int epos = endPosition <= rsize ? endPosition : rsize;
        final int subsize = epos - bpos;
        if (subsize <= 0) {
            return new Record();
        }
        
        final Record subrecord = new Record(subsize);
        for (int i = bpos; i < epos; ++i) {
            subrecord.add(names[i], datas[i]);
        }
        
        return subrecord;
    }
    
    /**
     * Get a shallow copy of the record.
     * 
     * @return Record
     */
    public Record shallowCopy () {
        return new Record(this);
    }
    
    /**
     * Get a data copy of the record elements.
     * 
     * @return Record
     */
    public Record dataCopy() {
        final TypeData[] redatas = new TypeData[datas.length];
        System.arraycopy(datas, 0, redatas, 0, datas.length);
        return new Record(names, redatas, positions, rsize);
    }
    
    /**
     * Get a template copy of the record elements.
     * 
     * @return Record
     */
    public Record templateCopy() {
        final TypeData[] redatas = new TypeData[datas.length];
        final int remainder = rsize % 100;
        if (remainder > 0) { 
            System.arraycopy(NULLS, 0, redatas, 0, remainder); 
        }
        for (int i = 0, cnt = rsize / 100; i < cnt; ++i) {
            System.arraycopy(NULLS, 0, redatas, remainder + 100 * i, 100);
        }
        
        return new Record(names, redatas, positions, rsize);
    }
    
    /**
     * Get a unsafe copy of the record elements without data.  This is for performance with known logic that will
     * fill exactly the template.
     * 
     * @return Record
     */
    public Record unsafeNoDataCopy() {
        return new Record(names, new TypeData[datas.length], positions, rsize);
    }
    
    /**
     * Get a additional copy based on template elements. Note, the template must be an addition copy of 
     * the record itself and have equals or more data.
     * 
     * @param template the post-add template record
     * 
     * @return Record
     */
    public Record templateAdditionCopy(final Record template) {
        final TypeData[] redatas = new TypeData[template.datas.length];
        System.arraycopy(datas, 0, redatas, 0, rsize);
        
        final int additionalCapacity = template.rsize - rsize;
        final int remainder = additionalCapacity % 100;
        if (remainder > 0) { 
            System.arraycopy(NULLS, 0, redatas, rsize, remainder); 
        }
        for (int i = 0, cnt = additionalCapacity / 100; i < cnt; ++i) {
            System.arraycopy(NULLS, 0, redatas, rsize + remainder + 100 * i, 100);
        }
        
        return new Record(template.names, redatas, template.positions, template.rsize);
    }
    
    public Record unsafeTemplateAdditionCopy(final Record template) {
        final TypeData[] redatas = new TypeData[template.datas.length];
        System.arraycopy(datas, 0, redatas, 0, rsize);
        
        return new Record(template.names, redatas, template.positions, template.rsize);
    }

    /**
     * Get a sub copy based on template elements. Note, the template must be an sub copy of 
     * the record itself and have equals or less data.
     * 
     * @param template the post-delete template record
     * 
     * @return Record
     */
    public Record templateSubCopy(final Record template) {
        final TypeData[] redatas = new TypeData[template.datas.length];
        System.arraycopy(datas, 0, redatas, 0, template.rsize);
        
        return new Record(template.names, redatas, template.positions, template.rsize);
    
    }
    
    /**
     * Get a delete copy based on template elements. Note, the template must be an post-delete copy of 
     * the record itself and have equals or less data.
     * 
     * @param template the post-delete template record
     * @param indices the sorted list of indices on which to remove data
     * 
     * @return Record
     */
    public Record templateDeletionCopy(final Record template, final List<Integer> indices) {
        final int size = indices.size();
        if (size == 0) {
            final TypeData[] redatas = new TypeData[template.datas.length];
            System.arraycopy(datas, 0, redatas, 0, rsize);
            return new Record(template.names, redatas, template.positions, template.rsize);
        }
        
        final TypeData[] redatas = new TypeData[template.datas.length];
        int cindex = 0, reindex = 0, len, index;
        for (int i = 0; i < size; ++i) {
            index = indices.get(i).intValue();
            if ((len = index - cindex) > 0) {
                System.arraycopy(datas, cindex, redatas, reindex, len);
                reindex = reindex + len;
            } 
            cindex = index + 1;
        }
        if ((len = rsize - cindex) > 0) {
            System.arraycopy(datas, cindex, redatas, reindex, len);
        }
        return new Record(template.names, redatas, template.positions, template.rsize);
    }
    
    /**
     * Get the number of the elements.
     * 
     * @return int
     */
    public int size () {
        return rsize;
    }
    
    /**
     * Get the name at a specified position.
     * 
     * @param position the position
     * @return String
     */
    public String nameAt (final int position) {
        return names[position];
    }
    
    /**
     * Get the data at an specified position.
     * 
     * @param position the position
     * @return TypeData
     */
    public TypeData dataAt (final int position) {
        return datas[position];
    }
    
    /**
     * Get the data with a specified name.
     * 
     * @param name the name
     * @return TypeData
     */
    public TypeData get (final String name) {
        final int position = indexOf(name);
        return position >= 0 ? datas[position] : NullData.INSTANCE;
    }
    
    /**
     * Get the data with a specified name and hash of insertion.
     * 
     * @param name the name
     * @param nameHash the name hash
     * @return TypeData
     */
    public TypeData get (final String name, final int nameHash) {
        final int position = indexOf(name, nameHash);
        return position >= 0 ? datas[position] : NullData.INSTANCE;
    }
    
    /**
     * Get the index with a specified name. -1 if not found
     * 
     * @param name the name
     * @return int
     */
    public int indexOf (final String name) {
        return indexOf(name, Hashing.getHash(name));
    }
    
    /**
     * Get the index with a specified name and hash of insertion. -1 if not found
     * 
     * @param name the name
     * @param nameHash the hash of name.
     * @return int
     */
    public int indexOf (final String name, final int nameHash) {
        final int lengthMask = positions.length - 1;
        final int initIndex = nameHash & lengthMask;
        int position = positions[initIndex];
        
        if (position < 0) {
            if (position == EMPTY_POSITION) {
                return -1;
            }
        } else if (names[position].equals(name)) {
            return position;
        }
        
        final int lengthBits = Integer.bitCount(lengthMask);
        final int increment = Math.max((nameHash >>> lengthBits) & lengthMask, 1);
        int index = (initIndex + increment) & lengthMask;
        int start = index;
        while (true) {
            position = positions[index];
            if (position < 0) {
                if (position == EMPTY_POSITION) {
                    return -1;
                }
            } else if (names[position].equals(name)) {
                return position;
            }
            
            index = (index + 1) & lengthMask;
            if (index == start) {
                throw new AssertionError();
            }
        }
    }
    
    private int probe(final String name) {
        return probe(name, Hashing.getHash(name));
    }
    
    private int probe(final String name, final int nameHash) {
        final int lengthMask = positions.length - 1;
        final int initIndex = nameHash & lengthMask;
        int position = positions[initIndex], emptyIndex = -1;
        
        if (position < 0) {
            if (position == EMPTY_POSITION) {
                return ~initIndex;
            } else {
                emptyIndex = initIndex;
            }
        } else if (names[position].equals(name)) {
            return initIndex;
        }
        
        final int lengthBits = Integer.bitCount(lengthMask);
        final int increment = Math.max((nameHash >>> lengthBits) & lengthMask, 1);
        int index = (initIndex + increment) & lengthMask;
        int start = index;
        while (true) {
            position = positions[index];
            if (position < 0) {
                if (position == EMPTY_POSITION) {
                    return emptyIndex != -1 ? ~emptyIndex : ~index;
                } else if (emptyIndex == -1) {
                    emptyIndex = index;
                }
            } else if (names[position].equals(name)) {
                return index;
            }
            
            index = (index + 1) & lengthMask;
            if (index == start) {
                if (emptyIndex != -1) {
                    return ~emptyIndex;
                }
                throw new AssertionError();
            }
        }
    }
    
    /**
     * Add a name and data.
     * 
     * @param name the name
     * @param data the data
     * @return Record
     */
    public Record add(final String name, final TypeData data) {
        return add(name, Hashing.getHash(name), data);
    }
    
    /**
     * Add a name with hash, and data.
     * 
     * @param name the name
     * @param nameHash the hash
     * @param data the data
     * @return Record
     */
    public Record add(final String name, final int nameHash, final TypeData data) {
        int index = probe(name, nameHash);
        if (index < 0) { //new
            if (rsize >= datas.length) {
                int additional = (int) (rsize * 0.2);
                if (rsize <= SMALL_RECORD_LIMIT) {
                    additional = SMALL_ADDITIONAL_CAPACITY;
                } else if (additional < MIN_ADDITIONAL_CAPACITY) { 
                    additional = MIN_ADDITIONAL_CAPACITY; 
                } else if (additional > MAX_ADDITIONAL_CAPACITY) {
                    additional = MAX_ADDITIONAL_CAPACITY;
                }
                ensureCapacity(rsize + additional);
            }

            index = ~index;
            positions[index] = rsize;
            names[rsize] = name;
            datas[rsize++] = data;
            
            if (rsize > positions.length * incrementLoadFactor) {  // Refresh or expand hash table
                int newLen = positions.length;
                while (rsize > newLen * incrementLoadFactor) {
                    newLen *= 2;
                }
                resize(newLen);
            }
        } else {
            datas[positions[index]] = data;
        }
        return this;
    }
    
    /**
     * Add all contents from another record.
     * 
     * @param record another record
     * @return Record
     */
    public Record addAll(final Record record) {
        if (record == null || record.rsize == 0) {
            return this;
        }
        
        final int nsize = this.rsize + record.rsize;
        if (nsize > datas.length) {
            ensureCapacity(nsize);
        }
        for (int i = 0; i < record.rsize; ++i) {
            add(record.nameAt(i), record.dataAt(i));
        }
        return this;
    }
    
    private void ensureCapacity(final int nsize) {
        final String[] nnames = new String[nsize];
        final TypeData[] ndatas = new TypeData[nsize];

        if (rsize > 0) {
            System.arraycopy(names, 0, nnames, 0, rsize);
            System.arraycopy(datas, 0, ndatas, 0, rsize);
        }

        names = nnames;
        datas = ndatas;
    }
    
    /**
     * Set a data at the specified position.
     * 
     * @param position the position
     * @param data the data
     * @return Record
     */
    public Record setAt (final int position, final TypeData data) {
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
        if (position < 0 || position >= rsize) {
            return NullData.INSTANCE;
        }
        
        final TypeData data = datas[position];
        int index = probe(names[position]);
        positions[index] = TOMBSTONE_POSITION;
        
        //remove remainder
        final int numMoved = rsize - position - 1;
        if (numMoved > 0) {
            System.arraycopy(names, position+1, names, position, numMoved);
            System.arraycopy(datas, position+1, datas, position, numMoved);

            for (int i = 0, s = positions.length; i < s; ++i) {
                if (positions[i] > position) {
                    positions[i] = positions[i] - 1;
                }
            }
        }
        
        names[--rsize] = null;
        datas[rsize] = null;
        
        return data;
    }
    
    /**
     * Remove a data with the specified name.
     * 
     * @param name the name
     * @return TypeData
     */
    public TypeData remove (final String name) {
        return remove(name, Hashing.getHash(name));
    }
    
    /**
     * Remove a data with the specified name and hash.
     * 
     * @param name the name
     * @param nameHash the hash
     * @return TypeData
     */
    public TypeData remove (final String name, final int nameHash) {
        int index = probe(name, nameHash);
        if (index < 0) {
            return NullData.INSTANCE;
        }
        
        final int position = positions[index];
        final TypeData data = datas[position];
        positions[index] = TOMBSTONE_POSITION;
        
        final int numMoved = rsize - position - 1;
        if (numMoved > 0) {
            System.arraycopy(names, position+1, names, position, numMoved);
            System.arraycopy(datas, position+1, datas, position, numMoved);

            for (int i = 0, s = positions.length; i < s; ++i) {
                if (positions[i] > position) {
                    positions[i] = positions[i] - 1;
                }
            }
        }
        
        names[--rsize] = null;
        datas[rsize] = null;
            
        return data;
    }
    
    /**
     * Whether it contains a key.
     * 
     * @param name the key name
     * @return true or false
     */
    public boolean contains (final String name) {
        return name.length() > 0 && indexOf(name) >= 0;
    }
    
    /**
     * Whether it contains a key and its hash.
     * 
     * @param name the key name
     * @param nameHash the name hash
     * @return true or false
     */
    public boolean contains (final String name, final int nameHash) {
        return name.length() > 0 && indexOf(name, nameHash) >= 0;
    }
    
    /**
     * Clear all elements.
     * 
     * @return Record
     * 
     */
    public Record clear () {
        names = DEFAULTCAPACITY_EMPTY_NAMES;
        datas = DEFAULTCAPACITY_EMPTY_DATAS;
        rsize = 0;
        names = null;
        positions = null;
        resize(1);

        return this;
    }
    
    /**
     * Nullify all data.
     * 
     * @return Record
     * 
     */
    public Record nullifyData () {
        final int remainder = rsize % 100;
        if (remainder > 0) { 
            System.arraycopy(NULLS, 0, datas, 0, remainder); 
        }
        for (int i = 0, cnt = rsize / 100; i < cnt; ++i) {
            System.arraycopy(NULLS, 0, datas, remainder + 100 * i, 100);
        }

        return this;
    }
    
    private void resize(int newLen) {
        if (newLen <= rsize) {
            if (newLen == 0) {
                newLen = 1;
            } else {
                throw new AssertionError();
            }
        }
        
        int power2 = roundPower2(newLen);
        while (newLen < power2 && (double) newLen / power2 > incrementLoadFactor) {
            power2 *= 2;
        }
        newLen = power2;
        positions = new int[newLen];
        Arrays.fill(positions, EMPTY_POSITION);
        
        int index;
        //probe new index
        for (int i = 0; i < rsize; ++i) {
            if ((index = probe(names[i])) >= 0) {
                throw new AssertionError();
            }
            index = ~index;
            positions[index] = i;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (rsize == 0) {
            out.writeByte(XNumberUtils.BYTE_ZERO);
            return;
        }
        
        //max byte is 127, max short is 32767
        if (rsize >> 7 == 0) {
            out.writeByte(XNumberUtils.BYTE_ONE); 
            out.writeByte(rsize);
        } else {
            if (rsize >> 15 == 0) {
                out.writeByte(XNumberUtils.BYTE_TWO); 
                out.writeShort(rsize);
            } else {
                out.writeByte(XNumberUtils.BYTE_FOUR); 
                out.writeInt(rsize);
            }
        }
        
        for (int i = 0; i < rsize; ++i) {
            out.writeUTF(names[i]);
            out.writeByte(datas[i].getType());
            datas[i].writeExternal(out);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(final ObjectInput in) throws IOException {
        final int len;
        switch(in.readByte()) {
            case XNumberUtils.BYTE_ZERO:     rsize = len = 0; clear(); datas = DEFAULTCAPACITY_EMPTY_DATAS; names = DEFAULTCAPACITY_EMPTY_NAMES; return;
            case XNumberUtils.BYTE_ONE:      len = in.readByte(); break;
            case XNumberUtils.BYTE_TWO:      len = in.readShort(); break;
            default:                        len = in.readInt();
        }
        
        clear();
        names = new String[len];
        datas = new TypeData[len];
        resize(len);
        
        String name;
        TypeData data;
        for (int i = 0; i < len; ++i) {
            name = in.readUTF();
            data = XTypeDataUtils.readTypeData(in, in.readByte());
            add(name, data);
        }
    }
    
    public static Record readRecord(final ObjectInput in) throws IOException {
        final int len;
        switch(in.readByte()) {
            case XNumberUtils.BYTE_ZERO:     return new Record();
            case XNumberUtils.BYTE_ONE:      len = in.readByte(); break;
            case XNumberUtils.BYTE_TWO:      len = in.readShort(); break;
            default:                        len = in.readInt();
        }
        
        final Record record = new Record(len);        
        String name;
        TypeData data;
        for (int i = 0; i < len; ++i) {
            name = in.readUTF();
            data = XTypeDataUtils.readTypeData(in, in.readByte());
            record.add(name, data);
        }
        return record;
    }
    
    /** {@inheritDoc} */
    @Override
    public int compareTo(final Record record) {
        if (record == null) {
            return 1;
        } else {
            final int thisSize = size(), otherSize = record.size();
            
            if (otherSize == 0) {
                return thisSize == 0 ? 0 : 1;
            } else if (thisSize == 0) {
                return -1;
            } else {
                final int minSize = Math.min(thisSize, otherSize);
                int result = 0;
                for (int i = 0; i < minSize; ++i) {
                    if ((result = this.dataAt(i).compareTo(record.dataAt(i))) != 0) {
                        return result;
                    }
                }
                
                return thisSize > minSize ? 1 : otherSize > minSize ? -1 : 0;
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Record) {
            final Record record = (Record) obj;
            final int thisSize = size(), otherSize = record.size();
            if (thisSize == otherSize) {
                for (int i = 0; i < thisSize; ++i) {
                    if (!dataAt(i).equals(record.dataAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return a copy of the names.
     * 
     * @return String[]
     */
    public String[] names() {
        return Arrays.copyOf(names, rsize);
    }
    
    /**
     * Return unsafe reference to datas.
     * 
     * @return TypeData[]
     */
    public String[] unsafeNames() {
        return names;
    }
    
    
    /**
     * Return a copy of the datas.
     * 
     * @return TypeData[]
     */
    public TypeData[] datas() {
        return Arrays.copyOf(datas, rsize);
    }
    
    /**
     * Return unsafe reference to datas.
     * 
     * @return TypeData[]
     */
    public TypeData[] unsafeDatas() {
        return datas;
    }
    
    @Override
    public String toString() {
        if (rsize == 0) {
            return "";
        }
        
        final StringBuilder sb = new StringBuilder(512);
        sb.append('{');
        
        for (int i = 0; i < rsize; ++i) {
            XStringUtils.escapeJSON(sb, names[i]);
            sb.append(':');
            
            XTypeDataUtils.appendJSONValue(sb, datas[i]);
            sb.append(',');
        }
        
        sb.setCharAt(sb.length() - 1, '}');
        return sb.toString();
    }
    
    /**
     * Set the datas, must be identical data structure.
     * 
     * @param datas the datas
     * @return Record
     */
    public Record unsafeSetDatas(final TypeData[] datas) {
        System.arraycopy(datas, 0, this.datas, 0, this.datas.length);
        return this;
    }
    
    private static int roundPower2(int v) {
        --v;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        return ++v;
    }
}
