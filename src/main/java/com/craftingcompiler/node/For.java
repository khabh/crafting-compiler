package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;

public class For extends Statement {

    private Variable variable;
    private Expression condition;
    private Expression expression;
    List<Statement> block;

    public For(Variable variable, Expression condition, Expression expression, List<Statement> block) {
        this.variable = variable;
        this.condition = condition;
        this.expression = expression;
        this.block = block;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("FOR:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("VARIABLE:");
        variable.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("CONDITION:");
        condition.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("EXPRESSION:");
        expression.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("BLOCK:");
        for (Statement stmt : block) {
            stmt.print(depth + 2);
        }
    }
}
