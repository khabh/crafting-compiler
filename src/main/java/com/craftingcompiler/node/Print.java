package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;

public class Print extends Statement {

    private boolean lineFeed = false;
    List<Expression> arguments;

    public Print(boolean lineFeed, List<Expression> arguments) {
        this.lineFeed = lineFeed;
        this.arguments = arguments;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println(lineFeed ? "PRINT_LINE" : "PRINT:");
        for (Expression arg : arguments) {
            arg.print(depth + 1);
        }
    }
}
