package com.craftingcompiler.node;

import static com.craftingcompiler.interpreter.Interpreter.local;

import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Variable extends Statement {

    private final String name;
    private final Expression expression;

    @Override
    public void interpret() {
        local.getLast().getFirst().put(name, expression.interpret());
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("VAR " + name + ":");
        expression.print(depth + 1);
    }
}
