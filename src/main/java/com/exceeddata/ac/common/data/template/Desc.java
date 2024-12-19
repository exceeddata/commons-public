package com.exceeddata.ac.common.data.template;

import java.io.Serializable;

import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.util.XStringUtils;

public final class Desc implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String sourceName = null;
    private String targetName = null;
    private DescType descType = null;
    private boolean nullable = true;
    private String nullString = null;
    private FieldFormula transform = FieldFormulaNone.getInstance();
    
    public Desc(final String name, final DescType descType) {
        this.sourceName = this.targetName = name;
        this.descType = descType;
    }
    
    public Desc(final String name, final DescType descType, final boolean nullable) {
        this.sourceName = this.targetName = name;
        this.descType = descType;
        this.nullable = nullable;
    }
    
    public Desc(final String name, final DescType descType, final boolean nullable, final String nullString) {
        this.sourceName = this.targetName = name;
        this.descType = descType;
        this.nullable = nullable;
        this.nullString = nullString;
    }
    
    public Desc(
            final String sourceName,
            final DescType sourceDescType,
            final boolean nullable,
            final String nullString,
            final String targetName,
            final FieldFormula transform) {
        this.sourceName = sourceName;
        this.descType = sourceDescType;
        this.nullable = nullable;
        this.nullString = nullString;
        this.targetName = XStringUtils.isNotBlank(targetName) ? targetName : sourceName;
        this.transform = transform;
    }
    
    /**
     * Get field name.
     * 
     * @return String
     */
    public String getName() { return targetName; }
    
    /**
     * Get original field name.
     * 
     * @return String
     */
    public String getSourceName() { return sourceName; }
    
    /**
     * Get target field name.
     * 
     * @return String
     */
    public String getTargetName() { return targetName; }
    
    /**
     * Set field name.
     * 
     * @param name String
     */
    public void setName (final String name) { this.sourceName = this.targetName = name; }
    
    /**
     * Get field desc type.
     * 
     * @return DescType
     */
    public DescType getDescType() { return descType; }
    
    /**
     * Set field desc type.
     * 
     * @param descType DescType
     */
    public void setDescType (final DescType descType) { this.descType = descType; }

    /**
     * Get field nullable.
     * 
     * @return Boolean
     */
    public boolean getNullable() { return nullable; }
    
    /**
     * Set field nullable.
     * 
     * @param nullable Boolean
     */
    public void setNullable (final boolean nullable) { this.nullable = nullable; }

    /**
     * Get field null string.
     * @return String
     */
    public String getNullString() { return nullString; }
    
    /**
     * Set field null string.
     * 
     * @param nullable Boolean
     */
    public void setNullString (final String nullString) { this.nullString = nullString; }

    /**
     * Get field transform formula.
     * @return Boolean
     */
    public FieldFormula getTransform() { return transform; }
    
    /**
     * Set field transform formula.
     * 
     * @param nullable Boolean
     */
    public void setTransform (final FieldFormula transform) { this.transform = transform; }
    
    /**
     * Transform the incoming data.
     * 
     * @param data the data
     * @return TypeData
     */
    public TypeData transform(final TypeData data) throws EngineException {
        return transform.calculate(data);
    }
}
