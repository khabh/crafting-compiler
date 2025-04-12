package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class StringLiteral extends Expression {

    private String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("\"" + value + "\"");
    }
}
