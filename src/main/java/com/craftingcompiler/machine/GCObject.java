package com.craftingcompiler.machine;

public class GCObject {

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
