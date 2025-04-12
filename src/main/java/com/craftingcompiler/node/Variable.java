package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class Variable extends Statement {

    private final String name;
    private final Expression expression;

    public Variable(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("VAR " + name + ":");
        expression.print(depth + 1);
    }
}
