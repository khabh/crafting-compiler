package com.craftingcompiler.machine;

import com.craftingcompiler.node.Potato;

public class CustomArray extends GCObject {

    private Object[] values;

    public CustomArray(Object[] values) {
        super(values);
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Potato) {
                continue;
            }
            values[i] = new Potato(values[i]);
        }
        this.values = values;
    }

    public Object get(int index) {
        return values[index];
    }

    public Object set(int index, Object value) {
        values[index] = value;
        return value;
    }

    @Override
    public void mark() {
        this.mark();
        for (Object value : values) {
            if (value instanceof GCObject) {
                ((GCObject) value).mark();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i]);
            if (i < values.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
