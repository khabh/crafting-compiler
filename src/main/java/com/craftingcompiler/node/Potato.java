package com.craftingcompiler.node;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class Potato {

    protected Object value;
    private final Map<String, Object> properties = new HashMap<>();

    public Potato(Object value) {
        this.value = value;
        properties.put("salted", false);
    }

    public Object getProperty(String name) {
        if (!properties.containsKey(name)) {
            throw new IllegalArgumentException("Property '" + name + "' not found");
        }
        return properties.get(name);
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
