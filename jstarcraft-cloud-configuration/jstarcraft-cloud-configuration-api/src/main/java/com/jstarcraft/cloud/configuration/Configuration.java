package com.jstarcraft.cloud.configuration;

import java.util.HashMap;
import java.util.Map;

import com.jstarcraft.core.utility.StringUtility;

/**
 * 配置
 * 
 * @author Birdy
 *
 */
public class Configuration {

    private Map<String, String> keyValues;

    public Configuration(Map<String, String> keyValues) {
        this.keyValues = new HashMap<>(keyValues);
    }

    public Boolean getBoolean(String key, Boolean instead) {
        String value = getString(key);
        return StringUtility.isBlank(value) ? instead : Boolean.valueOf(value);
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Character getCharacter(String key, Character instead) {
        String value = getString(key);
        return StringUtility.isBlank(value) ? instead : Character.valueOf(value.charAt(0));
    }

    public Character getCharacter(String key) {
        return getCharacter(key, null);
    }

    public Double getDouble(String key, Double instead) {
        String value = getString(key);
        return StringUtility.isBlank(value) ? instead : Double.valueOf(value);
    }

    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    public Float getFloat(String key, Float instead) {
        String value = getString(key);
        return StringUtility.isBlank(value) ? instead : Float.valueOf(value);
    }

    public Float getFloat(String key) {
        return getFloat(key, null);
    }

    public Integer getInteger(String key, Integer instead) {
        String value = getString(key);
        return StringUtility.isBlank(value) ? instead : Integer.valueOf(value);
    }

    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Long getLong(String key, Long instead) {
        String value = getString(key);
        return StringUtility.isBlank(value) ? instead : Long.valueOf(value);
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public String getString(String key, String instead) {
        String value = getString(key);
        return StringUtility.isBlank(value) ? instead : value;
    }

    public String getString(String key) {
        return keyValues.get(key);
    }

}
