package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class GetElement extends Expression {

    private Expression sub;
    private Expression index;

    public GetElement(Expression sub, Expression index) {
        this.sub = sub;
        this.index = index;
    }

    public Expression getSub() {
        return sub;
    }

    public Expression getIndex() {
        return index;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("GET_ELEMENT:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("SUB:");
        sub.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("INDEX:");
        index.print(depth + 2);
    }
}
