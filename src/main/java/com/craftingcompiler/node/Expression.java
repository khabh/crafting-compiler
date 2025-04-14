package com.craftingcompiler.node;

public abstract class Expression {

    public abstract void generate();

    public abstract void print(int indent);

    public abstract Object interpret();
}
