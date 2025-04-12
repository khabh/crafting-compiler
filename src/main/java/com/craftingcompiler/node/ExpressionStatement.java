package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class ExpressionStatement extends Statement {

    private final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("EXPRESSION:");
        expression.print(depth + 1);
    }
}
