package com.exceeddata.ac.common.data.record;

import java.io.Serializable;
import java.util.Arrays;

import com.exceeddata.ac.common.data.typedata.NullData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.util.XStringUtils;
import com.exceeddata.ac.common.util.XTypeDataUtils;

/**
 * Defines the specification for an ordered Map.
 * 
 * MAX_LEN = 0x40000000,  Largest power of 2 that fits in an int
 */
public final class TypeDataMap implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double incrementLoadFactor = 0.8;  // 0 < loadFactor < 1
    
    private static final int TOMBSTONE_POSITION = -1;
    private static final int EMPTY_POSITION = -2;
    
    private static final String[] DEFAULTCAPACITY_EMPTY_NAMES = {};
    private static final TypeData[] DEFAULTCAPACITY_EMPTY_DATAS = {};
    private static final int DEFAULTCAPACITY_TABLE = 5;
    private static final int SMALL_CAPACITY_LIMIT = 20;
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
    
    public TypeDataMap () {
        names = new String[DEFAULTCAPACITY_TABLE];
        datas = new TypeData[DEFAULTCAPACITY_TABLE];
        resize(DEFAULTCAPACITY_TABLE);
    }
    
    public TypeDataMap (final int initialCapacity) {
        names = new String[initialCapacity];
        datas = new TypeData[initialCapacity];
        resize(initialCapacity);
    }
    
    protected TypeDataMap (
            final String[] names, 
            final TypeData[] datas,
            final int[] positions,
            final int size) {
        this.names = names;
        this.datas = datas;
        this.positions = positions;
        this.rsize = size;
    }
    
    public TypeDataMap (final TypeDataMap map) {
        rsize = map.rsize;
        
        if (map.positions != null) {
            positions = new int[map.positions.length];
            if (map.positions.length > 0) {
                System.arraycopy(map.positions, 0, positions, 0, map.positions.length);
            }
        } else {
            resize(DEFAULTCAPACITY_TABLE);
        }
        
        if (map.names != null && map.names.length > 0) {
            names = new String[map.names.length];
            datas = new TypeData[map.datas.length];
            System.arraycopy(map.names, 0, names, 0, map.rsize);
            System.arraycopy(map.datas, 0, datas, 0, map.rsize);
        } else {
            names = DEFAULTCAPACITY_EMPTY_NAMES;
            datas = DEFAULTCAPACITY_EMPTY_DATAS;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public TypeDataMap clone () {
        return new TypeDataMap (this);
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
     */
    public void put(final String name, final TypeData data) {
        put(name, Hashing.getHash(name), data);
    }
    
    /**
     * Add a name with hash, and data.
     * 
     * @param name the name
     * @param nameHash the hash
     * @param data the data
     */
    public void put(final String name, final int nameHash, final TypeData data) {
        int index = probe(name, nameHash);
        if (index < 0) { //new
            if (rsize >= datas.length) {
                int additional = (int) (rsize * 0.2);
                if (rsize <= SMALL_CAPACITY_LIMIT) {
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
    }
    
    /**
     * Add all contents from another map.
     * 
     * @param map another map
     */
    public void putAll(final TypeDataMap map) {
        if (map == null || map.rsize == 0) {
            return;
        }
        
        final int nsize = this.rsize + map.rsize;
        if (nsize > datas.length) {
            ensureCapacity(nsize);
        }
        for (int i = 0; i < map.rsize; ++i) {
            put(map.nameAt(i), map.dataAt(i));
        }
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
     */
    public void setAt (final int position, final TypeData data) {
        datas[position] = data;
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
    public boolean containsKey (final String name) {
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
     */
    public void clear() {
        names = DEFAULTCAPACITY_EMPTY_NAMES;
        datas = DEFAULTCAPACITY_EMPTY_DATAS;
        rsize = 0;
        names = null;
        positions = null;
        resize(1);
    }
    
    /**
     * Nullify all data.
     * 
     */
    public void nullifyData () {
        if (rsize == 0) {
            return;
        }
        
        final int remainder = rsize % 100;
        if (remainder > 0) { 
            System.arraycopy(NULLS, 0, datas, 0, remainder); 
        }
        for (int i = 0, cnt = rsize / 100; i < cnt; ++i) {
            System.arraycopy(NULLS, 0, datas, remainder + 100 * i, 100);
        }
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
     */
    public void unsafeSetDatas(final TypeData[] datas) {
        System.arraycopy(datas, 0, this.datas, 0, this.datas.length);
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
