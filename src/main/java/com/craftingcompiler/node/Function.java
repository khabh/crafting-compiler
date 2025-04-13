package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Function extends Statement {

    private final String name;
    private final List<String> parameters;
    private final List<Statement> block;

    @Override
    public void interpret() {

    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("FUNCTION " + name + ":");
        if (!parameters.isEmpty()) {
            SyntaxPrinter.indent(depth + 1);
            System.out.print("PARAMETERS: ");
            for (String param : parameters) {
                System.out.print(param + " ");
            }
            System.out.println();
        }
        SyntaxPrinter.indent(depth + 1);
        System.out.println("BLOCK:");
        for (Statement node : block) {
            node.print(depth + 2);
        }
    }
}
