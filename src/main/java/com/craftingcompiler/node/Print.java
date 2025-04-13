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
