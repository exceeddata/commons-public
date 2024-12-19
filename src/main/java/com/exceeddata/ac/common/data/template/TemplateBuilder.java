package com.exceeddata.ac.common.data.template;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.exceeddata.ac.common.data.record.Record;
import com.exceeddata.ac.common.data.typedata.NullData;
import com.exceeddata.ac.common.exception.EngineException;
import com.exceeddata.ac.common.util.ParentheseParser;
import com.exceeddata.ac.common.util.XJsonUtils;
import com.exceeddata.ac.common.util.XNumberUtils;
import com.exceeddata.ac.common.util.XStringUtils;

public final class TemplateBuilder {
    private TemplateBuilder(){}
    
    public static Template build(final String definition) throws EngineException {
        if (!definition.trim().startsWith("{")) {
            return buildLineFormatTemplate(definition, new RecordDescConverter()); //old style hand-definition: name type nullable
        }
        return buildJsonFormatTemplate(definition);
    }
    
    public static Template build(final String definition, final DescConverter lineFormatConverter) throws EngineException {
        if (!definition.trim().startsWith("{")) {
            return buildLineFormatTemplate(definition, lineFormatConverter); //old style hand-definition: name type nullable
        }
        return buildJsonFormatTemplate(definition);
    }
    
    @SuppressWarnings("unchecked")
    private static Template buildJsonFormatTemplate(final String definition) throws EngineException {
        final Map<String, Object> json = (Map<String, Object>) XJsonUtils.JSON.fromJson(definition, Map.class);
        if (json == null || json.size() == 0) {
            throw new EngineException("SCHEMA_CONTENT_EMPTY");
        }
        if (!json.containsKey("schema") || ((json.get("schema") instanceof Map) == false)) {
            throw new EngineException("SCHEMA_MISSING_SCHEMA");
        }
        
        final Map<String, Object> schema = (Map<String, Object>) json.get("schema");
        if (!schema.containsKey("format") || !"exceeddata".equals(getString(schema.get("format")))) {
            throw new EngineException("SCHEMA_FORMAT_NOT_EXCEEDDATA");
        }
        if (!schema.containsKey("descs") || ((schema.get("descs") instanceof List) == false)) {
            throw new EngineException("SCHEMA_MISSING_DESCS");
        }
        
        final Template template = new Template();
        for (final Object desc : (List<Object>) schema.get("descs")) {
            final Map<String, Object> m = (Map<String, Object>) desc;
            if (!m.containsKey("source") || ((m.get("source") instanceof Map) == false)) {
                throw new EngineException("SCHEMA_MISSING_SOURCE");
            }
            
            final Map<String, Object> source = (Map<String, Object>) m.get("source");
            final String sourceName = getString(source.get("name"));
            final String descTypeName = getString(source.get("type"));
            final String nullableValue = getString(source.get("nullable"));
            final String nullString = getString(source.get("nullstring"));
            final DescType descType = XStringUtils.isNotBlank(descTypeName) ? RecordDescConverter.itemify(descTypeName) : null;
            final Boolean nullable = XStringUtils.isNotBlank(nullableValue) ? Boolean.valueOf(nullableValue) : true;
            
            if (!m.containsKey("target") || ((m.get("target") instanceof Map) == false)) {
                template.put(sourceName, new Desc(sourceName, descType, nullable, nullString));
                continue;
            }
            final Map<String, Object> target = (Map<String, Object>) m.get("target");
            final String targetName = getString(target.get("name"));
            final FieldFormula transform;
            if (!target.containsKey("transform") || ((target.get("transform") instanceof Map) == false)) {
                transform = new FieldFormulaNone(descType);
            } else {
                final Map<String, Object> t = (Map<String, Object>) target.get("transform");
                final String transformType = getString(t.get("type"));
                if (XStringUtils.isBlank(transformType)) {
                    transform = new FieldFormulaNone(descType);
                } else {
                    try {
                        switch(transformType.toLowerCase()) {
                            case "linear": {
                                final String a = getString(t.get("a"));
                                final String b = getString(t.get("b"));
                                if (!XNumberUtils.isNumber(a)) {
                                    throw new EngineException("SCHEMA_INVALID: a = '" + a + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(b)) {
                                    throw new EngineException("SCHEMA_INVALID: b = '" + b + "' is not number");
                                }
                                transform = new FieldFormulaLinear(descType, new BigDecimal(a), new BigDecimal(b));
                            } break;
                            case "polynomial": {
                                final String p0 = getString(t.get("p0"));
                                final String p1 = getString(t.get("p1"));
                                final String p2 = getString(t.get("p2"));
                                final String p3 = getString(t.get("p3"));
                                final String p4 = getString(t.get("p4"));
                                final String p5 = getString(t.get("p5"));
                                if (!XNumberUtils.isNumber(p0)) {
                                    throw new EngineException("SCHEMA_INVALID: p0 = '" + p0 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p1)) {
                                    throw new EngineException("SCHEMA_INVALID: p1 = '" + p1 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p2)) {
                                    throw new EngineException("SCHEMA_INVALID: p2 = '" + p2 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p3)) {
                                    throw new EngineException("SCHEMA_INVALID: p3 = '" + p3 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p4)) {
                                    throw new EngineException("SCHEMA_INVALID: p4 = '" + p4 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p5)) {
                                    throw new EngineException("SCHEMA_INVALID: p5 = '" + p5 + "' is not number");
                                }
                                transform = new FieldFormulaPolynomial(descType,
                                        new BigDecimal(p0), new BigDecimal(p1), new BigDecimal(p2),
                                        new BigDecimal(p3), new BigDecimal(p4), new BigDecimal(p5)
                                        );
                            } break;
                            case "exponential": {
                                final String p0 = getString(t.get("p0"));
                                final String p1 = getString(t.get("p1"));
                                final String p2 = getString(t.get("p2"));
                                final String p3 = getString(t.get("p3"));
                                final String p4 = getString(t.get("p4"));
                                final String p5 = getString(t.get("p5"));
                                final String p6 = getString(t.get("p6"));
                                if (!XNumberUtils.isNumber(p0)) {
                                    throw new EngineException("SCHEMA_INVALID: p0 = '" + p0 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p1)) {
                                    throw new EngineException("SCHEMA_INVALID: p1 = '" + p1 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p2)) {
                                    throw new EngineException("SCHEMA_INVALID: p2 = '" + p2 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p3)) {
                                    throw new EngineException("SCHEMA_INVALID: p3 = '" + p3 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p4)) {
                                    throw new EngineException("SCHEMA_INVALID: p4 = '" + p4 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p5)) {
                                    throw new EngineException("SCHEMA_INVALID: p5 = '" + p5 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p6)) {
                                    throw new EngineException("SCHEMA_INVALID: p6 = '" + p6 + "' is not number");
                                }
                                transform = new FieldFormulaExponential(descType,
                                        new BigDecimal(p0), new BigDecimal(p1), new BigDecimal(p2),
                                        new BigDecimal(p3), new BigDecimal(p4), new BigDecimal(p5),
                                        new BigDecimal(p6)
                                        );
                            } break;
                            case "logarithmic": {
                                final String p0 = getString(t.get("p0"));
                                final String p1 = getString(t.get("p1"));
                                final String p2 = getString(t.get("p2"));
                                final String p3 = getString(t.get("p3"));
                                final String p4 = getString(t.get("p4"));
                                final String p5 = getString(t.get("p5"));
                                final String p6 = getString(t.get("p6"));
                                if (!XNumberUtils.isNumber(p0)) {
                                    throw new EngineException("SCHEMA_INVALID: p0 = '" + p0 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p1)) {
                                    throw new EngineException("SCHEMA_INVALID: p1 = '" + p1 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p2)) {
                                    throw new EngineException("SCHEMA_INVALID: p2 = '" + p2 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p3)) {
                                    throw new EngineException("SCHEMA_INVALID: p3 = '" + p3 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p4)) {
                                    throw new EngineException("SCHEMA_INVALID: p4 = '" + p4 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p5)) {
                                    throw new EngineException("SCHEMA_INVALID: p5 = '" + p5 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(p6)) {
                                    throw new EngineException("SCHEMA_INVALID: p6 = '" + p6 + "' is not number");
                                }
                                transform = new FieldFormulaLogarithmic(descType,
                                        new BigDecimal(p0), new BigDecimal(p1), new BigDecimal(p2),
                                        new BigDecimal(p3), new BigDecimal(p4), new BigDecimal(p5),
                                        new BigDecimal(p6)
                                        );
                            } break;
                            case "rational": {
                                final String n0 = getString(t.get("n0"));
                                final String n1 = getString(t.get("n1"));
                                final String n2 = getString(t.get("n2"));
                                final String d3 = getString(t.get("d3"));
                                final String d4 = getString(t.get("d4"));
                                final String d5 = getString(t.get("d5"));
                                if (!XNumberUtils.isNumber(n0)) {
                                    throw new EngineException("SCHEMA_INVALID: p0 = '" + n0 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(n1)) {
                                    throw new EngineException("SCHEMA_INVALID: p1 = '" + n1 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(n2)) {
                                    throw new EngineException("SCHEMA_INVALID: p2 = '" + n2 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(d3)) {
                                    throw new EngineException("SCHEMA_INVALID: p3 = '" + d3 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(d4)) {
                                    throw new EngineException("SCHEMA_INVALID: p4 = '" + d4 + "' is not number");
                                }
                                if (!XNumberUtils.isNumber(d5)) {
                                    throw new EngineException("SCHEMA_INVALID: p5 = '" + d5 + "' is not number");
                                }
                                transform = new FieldFormulaRational(descType,
                                        new BigDecimal(n0), new BigDecimal(n1), new BigDecimal(n2),
                                        new BigDecimal(d3), new BigDecimal(d4), new BigDecimal(d5)
                                        );
                            } break;
                            case "decode": {
                                if (!t.containsKey("values") || ((t.get("values") instanceof List) == false)) {
                                    throw new EngineException("SCHEMA_INVALID_DECODE_VALUES");
                                }
                                final List<Object> values = (List<Object>) t.get("values");
                                final ArrayList<BigDecimal> list = new ArrayList<>();
                                for (final Object v : values) {
                                    if (v != null) {
                                        list.add(new BigDecimal(v.toString()));
                                    }
                                }
                                if (list.isEmpty()) {
                                    throw new EngineException("SCHEMA_INVALID_DECODE_VALUES");
                                }
                                transform = new FieldFormulaDecode(descType, list);
                            } break;
                            case "interpolate": {
                                if (!t.containsKey("values") || ((t.get("values") instanceof List) == false)) {
                                    throw new EngineException("SCHEMA_INVALID_INTERPOLATE_VALUES");
                                }
                                final List<Object> values = (List<Object>) t.get("values");
                                final ArrayList<BigDecimal> list = new ArrayList<>();
                                for (final Object v : values) {
                                    if (v != null) {
                                        list.add(new BigDecimal(v.toString()));
                                    }
                                }
                                if (list.isEmpty()) {
                                    throw new EngineException("SCHEMA_INVALID_INTERPOLATE_VALUES");
                                }
                                transform = new FieldFormulaInterpolate(descType, list);
                            } break;
                            default:
                                transform = new FieldFormulaNone(descType);
                        }
                    } catch (NumberFormatException e) {
                        throw new EngineException(e.getMessage(), e);
                    }
                }
            }
            template.put(sourceName, new Desc(sourceName, descType, nullable, nullString, targetName, transform));
        }
        return template;
    }
    
    private static Template buildLineFormatTemplate(final String schema, final DescConverter converter) {
        final Template template = new Template();
        final ParentheseParser parser = new ParentheseParser('<', '>', ',', '\'', '\\', true);
        final String[] definitions = parser.split(schema);
        Desc desc;
        
        for (int i = 0; i < definitions.length; ++i) {
            if (XStringUtils.isNotBlank(definitions[i])) {
                desc = converter.describe(definitions[i], i);
                template.put(desc.getName(), desc);
            }
        }

        return template;
    }
    
    private static String getString(final Object o) {
        return o != null ? o.toString() : "";
    }
    
    public static Record buildTemplateRecord (final Template template) {
        final int size = template.size();
        final Record record = new Record(size);
        for (int i = 0; i < size; ++i) {
            record.add(template.descAt(i).getTargetName(), NullData.INSTANCE);
        }
        return record;
    }
}
