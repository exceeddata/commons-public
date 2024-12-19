package com.exceeddata.ac.common.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.exceeddata.ac.common.exception.EngineException;

/**
 * A class definition for the format class?attr1=v1,attr2=v2 (param1=`value1`, param2=value2 ...);
 *
 */
public final class ClassDefinition {
    private String className = null;
    private Map<String, String> attributes = null;
    private Map<String, String> params = null;
    
    public ClassDefinition() {
        this.attributes = new LinkedHashMap<String, String>();
        this.params = new LinkedHashMap<String, String>();
    }
    
    private ClassDefinition(final ClassDefinition definition) {
        this.className = definition.className;
        this.attributes = new LinkedHashMap<String, String>(definition.attributes);
        this.params = new LinkedHashMap<String, String>(definition.params);
    }
    
    @Override
    public ClassDefinition clone() {
        return new ClassDefinition(this);
    }
    
    public ClassDefinition copy() {
        return new ClassDefinition(this);
    }
    
    @Override
    public String toString() {
        if (XStringUtils.isBlank(className)) {
            return "";
        }
        
        final StringBuilder sb = new StringBuilder(1024);
        sb.append(className);
        
        if (attributes.size() > 0) {
            sb.append('?');
            for (final Map.Entry<String, String> entry : attributes.entrySet()) {
                sb.append(entry.getKey()).append('=').append(entry.getValue()).append(',');
            }
            sb.setLength(sb.length() - 1); //remove last comma
        }
        
        sb.append('(');
        if (params.size() > 0) {
            for (final Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=`").append(entry.getValue().replace("`", "``")).append("`,");
            }
            sb.setLength(sb.length() - 1); //remove last comma
        }
        sb.append(')');
        return sb.toString();
    }
    
    /**
     * Get the class name.
     * 
     * @return String
     */
    public final String getClassName() { return className; }
    
    /**
     * Set the class name.
     * 
     * @param className the class name
     */
    public final void setClassName(String className) { this.className = className; }
    
    /**
     * Get the attributes.
     * 
     * @return Map of String key and values
     */
    public final Map<String, String> getAttributes() { return attributes; }
    
    /**
     * Set the attribute map.
     * 
     * @param attributes the attribute map
     */
    public final void setAttributes(final LinkedHashMap<String, String> attributes) { this.attributes = attributes; }
    
    /**
     * Get the map of params.
     * 
     * @return map of params
     */
    public final Map<String, String> getParams() { return params; }
    
    /**
     * Set the class definition params params.
     * 
     * @param params the class definition params map
     */
    public final void setParams(final LinkedHashMap<String, String> params) { this.params = params; }
    
    /**
     * Add an attribute.
     * 
     * @param name the attribute name
     * @param value the attribute value
     */
    public final void addAttribute(final String name, final String value) {
        if (XStringUtils.isNotBlank(value)) {
            this.attributes.put(name, value);
        }
    }
    
    /**
     * Add a parameter.
     * 
     * @param name the attribute name
     * @param value the attribute value
     */
    public final void addParameter(final String name, final String value) {
        if (XStringUtils.isNotBlank(value)) {
            this.params.put(name, value);
        }
    }
    
    /**
     * Parse class definition.
     * 
     * @param definition the class definition string
     * @return ClassDefinition
     */
    public static final ClassDefinition extractDefinition(final String definition) {
        if (XStringUtils.isBlank(definition)) {
            return null;
        }
        
        final ClassDefinition def = new ClassDefinition();
        String defstr = definition;
        int index = defstr.indexOf('('), lastIndex = defstr.length()-1;
        
        if (defstr.charAt(lastIndex)==')' && index > 0) {
            def.setParams(splitParams(defstr.substring(index+1, lastIndex)));
            defstr = defstr.substring(0, index).trim();
        }
        
        index = defstr.indexOf('?');
        if (index < 0) {
            def.setClassName(defstr.trim());
            return def;
        } else if (index == 0) {
            return null;
        } else {
            def.setClassName(defstr.substring(0, index).trim());
        }
        
        if (index == defstr.length()-1) {
            return def;
        }
        
        final Map<String, String> attrMap = def.getAttributes();
        final String[] attrs = defstr.substring(index+1).split(",");
        for (final String attr : attrs) {
            if ((index = attr.indexOf('=')) > 0) {
                attrMap.put(attr.substring(0, index).trim(), 
                        (index == attr.length()-1) ? "" : attr.substring(index+1).trim());
            }
        }
        
        return def;
    }
    
    /**
     * Parse class definitions.
     * 
     * @param definitions the class definition string array
     * @return ClassDefinitions
     * @throws EngineException if exception occurs
     */
    public static final ClassDefinition[] parseClassDefinitions(final String[] definitions) throws EngineException {
        final List<ClassDefinition> defs = new ArrayList<ClassDefinition>();
        final SeparatorParser parser = new SeparatorParser(';', '`', '\\', ' ', true, true);
        ClassDefinition def;
        String[] defstrs;
        
        for (final String definition : definitions) {
            defstrs = parser.split(definition);
            for (final String defstr : defstrs) {
                if ((def = extractDefinition(defstr)) != null) {
                    defs.add(def);
                }
            }
        }
        return defs.toArray(new ClassDefinition[]{});
    }
    
    /**
     * Parse a class definition.
     * 
     * @param definition the class definition string array
     * @return ClassDefinitions
     * @throws EngineException if exception occurs
     */
    public static final ClassDefinition[] parseClassDefinition(final String definition) throws EngineException {
        final List<ClassDefinition> defs = new ArrayList<ClassDefinition>();
        final SeparatorParser parser = new SeparatorParser(';', '`', '\\', ' ', true, true);
        final String[] defstrs = parser.split(definition);
        ClassDefinition def;
        
        for (final String defstr : defstrs) {
            if ((def = extractDefinition(defstr)) != null) {
                defs.add(def);
            }
        }
        return defs.toArray(new ClassDefinition[]{});
    }
    
    private static final LinkedHashMap<String, String> splitParams (final String str) {
        if (XStringUtils.isNotBlank(str)) {
            final String[] paramstrs = new OperationParamParser().setKeepEscapeChar(true).split(str);
            final SeparatorParser parser = new SeparatorParser('=', '`', '\\', ' ', true, true);
            final LinkedHashMap<String, String> params = new LinkedHashMap<>();
            String[] kv;
            
            if (paramstrs != null) {
                for (final String paramstr : paramstrs) {
                    kv = parser.split(paramstr);
                    if (kv.length == 2 && XStringUtils.isNotBlank(kv[0])) {
                        params.put(kv[0].trim(), kv[1]);
                    }
                }
            }
            return params;
        } else {
            return new LinkedHashMap<>();
        }
    }
}
