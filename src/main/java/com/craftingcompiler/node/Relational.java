package com.craftingcompiler.node;

import com.craftingcompiler.Kind;
import com.craftingcompiler.util.SyntaxPrinter;

public class Relational extends Expression {

    private Kind kind;
    private Expression lhs;
    private Expression rhs;

    public Relational(Kind kind, Expression lhs, Expression rhs) {
        this.kind = kind;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println(kind.toString() + ":");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("LHS:");
        lhs.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("RHS:");
        rhs.print(depth + 2);
    }
}
