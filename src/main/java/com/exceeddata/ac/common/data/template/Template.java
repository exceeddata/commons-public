package com.exceeddata.ac.common.data.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class for Record template.
 *
 */
public class Template implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private ArrayList<String> names = null;
    private ArrayList<Desc> descs = null;
    private HashMap<String, Integer> mappings = null;
    
    public Template() {
        this.names = new ArrayList<>();
        this.descs = new ArrayList<>();
        this.mappings = new HashMap<>();
    }
    
    /**
     * Construct a new Template from an existing Template.
     *
     * @param template the template
     */
    public Template (Template template) {
        this.names = new ArrayList<String>(template.names);
        this.descs = new ArrayList<Desc>(template.descs);
        this.mappings = new HashMap<String, Integer>(template.mappings);
    }
    
    /**
     * Clone a copy of the Template.
     */
    @Override
    public Template clone() {
        return new Template (this);
    }
    
    /**
     * Make a copy of the Template.
     * 
     * @return a copy of itself
     */
    public Template copy() {
        return new Template (this);
    }
    
    /**
     * Make a shallow copy of the Template.
     * 
     * @return a shallow copy of itself
     */
    public Template shallowCopy() {
        return new Template (this);
    }
    
    /**
     * Get the Desc associated with an element name.
     * 
     * @param name the element name
     * @return Desc
     */
    public Desc get (String name) {
        final Integer position = mappings.get(name);
        return position != null ? descs.get(position) : null;
    }
    
    /**
     * Get the Desc at a position.
     * 
     * @param position int
     * @return Desc
     */
    public Desc descAt (int position) {
        return descs.get(position);
    }
    
    /**
     * Get the name at a position.
     * 
     * @param position int
     * @return String
     */
    public String nameAt (int position) {
        return names.get(position);
    }
    
    /**
     * Whether the template contains by name.
     * 
     * @param name
     * @return
     */
    public boolean contains(final String name) {
        return mappings.containsKey(name);
    }
    
    /**
     * Get size of the template elements.
     * 
     * @return int
     */
    public int size() { 
        return names.size();
    }
    
    /**
     * Put an element Desc into template.
     * 
     * @param name the element name
     * @param desc the element desc
     * @return the Template
     */
    public Template put (String name, Desc desc) {
        final Integer position = mappings.get(name);
        if (position == null) {
            names.add(name);
            descs.add(desc);
            mappings.put(name, names.size() - 1);
            return this;
        } else {
            descs.set(position, desc);
            return this;
        }
    }
    
    /**
     * Remove a element desc by name.
     * 
     * @param name the name
     * @return Desc
     */
    public Desc remove (String name) {
        final Integer position = mappings.get(name);
        if (position == null) {
            return null;
        }
        
        final Desc desc = descs.get(position);
        names.remove(position.intValue());
        descs.remove(position.intValue());
        final int size = names.size();
        for (int i = position; i < size; ++i) {
            mappings.put(names.get(i), i);
        }
        mappings.remove(name);
        return desc;
    }
    
    /**
     * Remove a element desc by position.
     * 
     * @param position the position
     * @return Desc
     */
    public Desc removeAt (final int position) {
        final String name = names.get(position);
        final Desc desc = descs.get(position);
        names.remove(position);
        descs.remove(position);
        final int size = names.size();
        for (int i = position; i < size; ++i) {
            mappings.put(names.get(i), i);
        }
        mappings.remove(name);
        return desc;
    }
    
    public List<String> names() {
        return names;
    }
    
    public List<Desc> descs() {
        return descs;
    }
    
    public byte[] types() {
        final int size = descs.size();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; ++i) {
            bytes[i] = descs.get(i).getDescType().getType();
        }
        return bytes;
    }
    
    public boolean[] nullables() {
        final int size = descs.size();
        final boolean[] nullables = new boolean[size];
        for (int i = 0; i < size; ++i) {
            nullables[i] = descs.get(i).getNullable();
        }
        return nullables;
    }
    
    /**
     * Clear the template.
     * 
     * @return Template
     */
    public Template clear() {
        names.clear();
        descs.clear();
        mappings.clear();
        return this;
    }
}
