package com.craftingcompiler.node;

public abstract class Statement {

    public abstract void generate();

    public abstract void interpret();

    public abstract void print(int indent);
}
