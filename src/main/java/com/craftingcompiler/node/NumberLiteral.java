package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class NumberLiteral extends Expression {

    private double value = 0.0;

    public NumberLiteral(double value) {
        this.value = value;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println(value);
    }
}
