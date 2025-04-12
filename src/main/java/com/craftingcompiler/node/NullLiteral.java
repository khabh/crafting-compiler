package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class NullLiteral extends Expression {

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("null");
    }
}
