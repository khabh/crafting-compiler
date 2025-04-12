package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;

public class Call extends Expression {

    private Expression sub;
    List<Expression> arguments;

    public Call(Expression sub, List<Expression> arguments) {
        this.sub = sub;
        this.arguments = arguments;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("CALL:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("EXPRESSION:");
        sub.print(depth + 2);
        for (Expression arg : arguments) {
            SyntaxPrinter.indent(depth + 1);
            System.out.println("ARGUMENT:");
            arg.print(depth + 2);
        }
    }
}
