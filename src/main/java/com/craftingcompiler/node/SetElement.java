package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class SetElement extends Expression {

    private Expression sub;
    private Expression index;
    private Expression value;

    public SetElement(Expression sub, Expression index, Expression value) {
        this.sub = sub;
        this.index = index;
        this.value = value;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("SET_ELEMENT:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("SUB:");
        sub.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("INDEX:");
        index.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("VALUE:");
        value.print(depth + 2);
    }
}
