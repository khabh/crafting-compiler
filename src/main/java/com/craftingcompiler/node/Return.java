package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class Return extends Statement {

    private Expression expression;

    public Return(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("RETURN:");
        expression.print(depth + 1);
    }
}
