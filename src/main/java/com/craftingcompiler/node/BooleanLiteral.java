package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class BooleanLiteral extends Expression {

    private boolean value = false;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println(value ? "true" : "false");
    }
}
