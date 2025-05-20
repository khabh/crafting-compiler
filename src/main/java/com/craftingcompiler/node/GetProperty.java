package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetProperty extends Expression {

    private final Expression sub;
    private final String name;

    @Override
    public void generate() {
        // TODO
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("GET_PROPERTY:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("SUB:");
        sub.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("PROPERTY: " + name);
    }

    @Override
    public Object interpret() {
        var object = sub.interpret();
        if (object instanceof Potato) {
            return ((Potato) object).getProperty(name);
        }
        return null;
    }
}
