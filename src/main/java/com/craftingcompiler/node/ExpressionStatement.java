package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpressionStatement extends Statement {

    private final Expression expression;

    @Override
    public void interpret() {
        expression.interpret();
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("EXPRESSION:");
        expression.print(depth + 1);
    }
}
