package com.exceeddata.ac.common.message;

import java.io.Serializable;
import java.util.Set;

import com.exceeddata.ac.common.data.record.Record;

public interface MessageDecoder extends Serializable {
    
    /**
     * Get whether to output time offset.
     * 
     * @return true or false
     */
    public boolean getOutputOffset();
    
    /**
     * Wrapper method for decode or interpret.
     * 
     * @param desc the desc
     * @param message the message
     * @param applyFormula if true then code else interpret
     * @return Record
     */
    public Record compute(
            final MessageDesc desc, 
            final MessageContent message,
            final boolean applyFormula);
    
    /**
     * Wrapper method for decode or interpret.
     * 
     * @param desc the desc
     * @param message the message
     * @param target the target record
     * @param applyFormula if true then code else interpret
     * @return Record
     */
    public Record compute(
            final MessageDesc desc, 
            final MessageContent message,
            final Record target,
            final boolean applyFormula);
    
    /**
     * Decode the Message to formula-computed values.
     * 
     * @param desc the desc
     * @param message the message
     * @return Record
     */
    public Record decode(
            final MessageDesc desc, 
            final MessageContent message);
    
    /**
     * Decode the Message to formula-computed values into the target record.
     * 
     * @param desc the desc
     * @param message the message
     * @param target the target record
     * @return Record
     */
    public Record decode(
            final MessageDesc desc, 
            final MessageContent message,
            final Record target);
    
    /**
     * Interpret the Message to raw-uncomputed values.
     * 
     * @param desc the desc
     * @param message the message
     * @return Record
     */
    public Record interpret(
            final MessageDesc desc, 
            final MessageContent message);
    
    /**
     * Interpret the Message to raw-uncomputed values into the target record.
     * 
     * @param desc the desc
     * @param message the message
     * @param target the target record
     * @return Record
     */
    public Record interpret(
            final MessageDesc desc, 
            final MessageContent message,
            final Record target);
    
    /**
     * Select (filter) the message by attribute names.
     * 
     * @param selectedAttributes the selected attributes
     */
    public void select(final Set<String> selectedAttributes);
}
