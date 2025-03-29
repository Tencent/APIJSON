/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/

package apijson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import apijson.orm.exception.UnsupportedDataTypeException;

/**
 * Custom JSONArray implementation based on ArrayList to replace com.alibaba.fastjson.JSONArray
 * Maintains same API as fastjson but uses standard Java List implementation
 * @author Lemon
 */
public class JSONArray extends ArrayList<Object> implements JSON {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "JSONArray";
    
    /**
     * Create an empty JSONArray
     */
    public JSONArray() {
        super();
    }
    
    /**
     * Create a JSONArray with initial capacity
     * @param initialCapacity the initial capacity
     */
    public JSONArray(int initialCapacity) {
        super(initialCapacity);
    }
    
    /**
     * Create a JSONArray from a Collection
     * @param collection the collection to copy from
     */
    public JSONArray(Collection<?> collection) {
        super();
        if (collection != null) {
            addAll(collection);
        }
    }
    
    /**
     * Create a JSONArray from a JSON string
     * @param json JSON string
     */
    public JSONArray(String json) {
        this();
        List<Object> list = JSON.parseArray(json);
        if (list != null) {
            addAll(list);
        }
    }
    
    /**
     * Get a JSONObject at the specified index
     * @param index the index
     * @return the JSONObject or null if not a JSONObject
     */
    public JSONObject getJSONObject(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        
        Object obj = get(index);
        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            return new JSONObject(map);
        } else if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        return null;
    }
    
    /**
     * Get a JSONArray at the specified index
     * @param index the index
     * @return the JSONArray or null if not a JSONArray
     */
    public JSONArray getJSONArray(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        
        Object obj = get(index);
        if (obj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) obj;
            return new JSONArray(list);
        } else if (obj instanceof List<?>) {
            return (JSONArray) obj;
        }
        return null;
    }
    
    /**
     * Get a boolean value at the specified index
     * @param index the index
     * @return the boolean value or false if not found
     */
    public boolean getBooleanValue(int index) {
        if (index < 0 || index >= size()) {
            return false;
        }
        
        Object obj = get(index);
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue() != 0;
        } else if (obj instanceof String) {
            return Boolean.parseBoolean((String) obj);
        }
        return false;
    }
    
    /**
     * Get an integer value at the specified index
     * @param index the index
     * @return the integer value or 0 if not found
     */
    public int getIntValue(int index) {
        if (index < 0 || index >= size()) {
            return 0;
        }
        
        Object obj = get(index);
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        } else if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        return 0;
    }
    
    /**
     * Get a long value at the specified index
     * @param index the index
     * @return the long value or 0 if not found
     */
    public long getLongValue(int index) {
        if (index < 0 || index >= size()) {
            return 0L;
        }
        
        Object obj = get(index);
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        return 0L;
    }
    
    /**
     * Get a double value at the specified index
     * @param index the index
     * @return the double value or 0 if not found
     */
    public double getDoubleValue(int index) {
        if (index < 0 || index >= size()) {
            return 0.0;
        }
        
        Object obj = get(index);
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        } else if (obj instanceof String) {
            try {
                return Double.parseDouble((String) obj);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        return 0.0;
    }
    
    /**
     * Get a string value at the specified index
     * @param index the index
     * @return the string value or null if not found
     */
    public String getString(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        
        Object obj = get(index);
        return obj != null ? obj.toString() : null;
    }
    
    /**
     * Add a value to the JSONArray
     * @param obj the value to add
     * @return this JSONArray
     */
    public JSONArray fluentAdd(Object obj) {
        add(obj);
        return this;
    }
    
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
} 