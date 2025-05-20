package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Wash extends Statement {

    private final Expression expression;

    @Override
    public void generate() {
        // TODO
    }

    @Override
    public void interpret() {
        Object value = expression.interpret();
        if (value instanceof Potato potato) {
            potato.setProperty("salted", false);
        }
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("WASH:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("SUB:");
        expression.print(depth + 2);
    }
}
