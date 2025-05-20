package com.craftingcompiler.machine;

import com.craftingcompiler.node.Potato;

public class GCObject extends Potato {

    public GCObject(Object value) {
        super(value);
    }

    protected boolean isMarked = false;

    public boolean isMarked() {
        return isMarked;
    }

    public void mark() {
        isMarked = true;
    }

    public void unmark() {
        isMarked = false;
    }
}
