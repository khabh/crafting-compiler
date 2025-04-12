package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class SetVariable extends Expression {

    private String name;
    private Expression value;

    public SetVariable(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("SET_VARIABLE: " + name);
        value.print(depth + 1);
    }
}
