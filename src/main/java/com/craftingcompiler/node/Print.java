package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Print extends Statement {

    private boolean lineFeed = false;
    List<Expression> arguments;

    @Override
    public void interpret() {
        StringBuilder output = new StringBuilder();
        for (Expression argument : arguments) {
            Object value = argument.interpret();
            output.append(value);
        }
        if (lineFeed) {
            output.append("\n");
        }
        System.out.print(output);
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
