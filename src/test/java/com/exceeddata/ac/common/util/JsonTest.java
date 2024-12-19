package com.exceeddata.ac.common.util;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;


public class JsonTest {
    
    @Test
    public void mapTest() {
        final String s = "{ \"a\" : 5, \"b\": [ { \"c\" : \"3\" } ], \"d\": { \"e\": 5 }}";
        final Map<String, Object> deserialized = XJsonUtils.fromJsonObject(s);
        final List<Object> b = XJsonUtils.getList(deserialized, "b");
        final Map<String, Object> c = XJsonUtils.getMap(b, 0);
        final Map<String, Object> d = XJsonUtils.getMap(deserialized, "d");

        assertEquals(c.get("c").toString(), "3");
        assertEquals(((Number) d.get("e")).intValue(), 5);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void arrayTest() {
        final JsonObject serialized = new JsonObject();
        final JsonArray dbls = new JsonArray();
        dbls.add(new JsonPrimitive(2.0d));
        dbls.add(new JsonPrimitive(3.0d));
        serialized.add("dbls", dbls);
        
        final String s = serialized.toString();
        final Map<String, Object> deserialized = XJsonUtils.fromJsonObject(s);
        final List<Object> arr = (List<Object>)deserialized.get("dbls");
        assertEquals(String.valueOf(arr.get(0)), "2.0");
        assertEquals(String.valueOf(arr.get(1)), "3.0");
        
        //empty array
        final JsonObject serialized2 = new JsonObject();
        serialized2.add("dbls", new JsonArray());
        
        final Map<String, Object> deserialized2 = XJsonUtils.fromJsonObject(serialized2.toString());
        final List<?> arr2 = (List<?>)deserialized2.get("dbls");
        assertEquals(arr2.size() == 0, true);
    }

    @Test
    public void nullTest() {
        final JsonObject serialized = new JsonObject();
        assertEquals(serialized.get("foo"), null);
        
        serialized.add("foo", JsonNull.INSTANCE);
        assertEquals(serialized.get("foo").isJsonNull(), true);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void numberTest() {
        final JsonObject serialized = new JsonObject();
        final JsonArray dbls = new JsonArray();
        dbls.add(new JsonPrimitive(1.0d));
        dbls.add(new JsonPrimitive(2.0d));
        dbls.add(new JsonPrimitive(3.0d));
        serialized.add("dbls", dbls);
        
        final Map<String, Object> deserialized = XJsonUtils.fromJsonObject(serialized.toString());
        final List<?> arr = (List<?>)deserialized.get("dbls");
        assertEquals(String.valueOf(arr.get(0)), "1.0");
        

        final JsonObject serialized2 = new JsonObject();
        final JsonArray ints = new JsonArray();
        ints.add(new JsonPrimitive(new Integer(1)));
        ints.add(new JsonPrimitive(new Integer(2)));
        ints.add(new JsonPrimitive(new Integer(3)));
        serialized2.add("ints", ints);
        
        final Map<String, Object> deserialized2 = XJsonUtils.fromJsonObject(serialized2.toString());
        final List<Object> arr2 = (List<Object>)deserialized2.get("ints");
        assertEquals(String.valueOf(arr2.get(0)), "1.0");
        
    }
}
