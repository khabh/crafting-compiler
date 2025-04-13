package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Call extends Expression {

    private Expression sub;
    List<Expression> arguments;

    @Override
    public Object interpret() {
        return null;
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
