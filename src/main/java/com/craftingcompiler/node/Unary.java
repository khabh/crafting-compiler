package com.craftingcompiler.node;

import com.craftingcompiler.Kind;
import com.craftingcompiler.util.SyntaxPrinter;

public class Unary extends Expression {

    private Kind kind;
    private Expression sub;

    public Unary(Kind kind, Expression sub) {
        this.kind = kind;
        this.sub = sub;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println(kind.toString());
        sub.print(depth + 1);
    }
}
