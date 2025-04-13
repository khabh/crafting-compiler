package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Return extends Statement {

    private Expression expression;

    @Override
    public void interpret() {

    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("RETURN:");
        expression.print(depth + 1);
    }
}
