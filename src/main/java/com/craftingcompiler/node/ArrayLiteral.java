package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;

public class ArrayLiteral extends Expression {

    private List<Expression> values;

    public ArrayLiteral(List<Expression> values) {
        this.values = values;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("[");
        for (Expression value : values) {
            value.print(depth + 1);
        }
        SyntaxPrinter.indent(depth);
        System.out.println("]");
    }
}
