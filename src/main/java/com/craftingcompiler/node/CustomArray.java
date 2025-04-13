package com.craftingcompiler.node;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomArray {

    private final Object[] values;

    public Object get(int index) {
        return values[index];
    }

    public Object set(int index, Object value) {
        values[index] = value;
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i]);
            if (i < values.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }
}
