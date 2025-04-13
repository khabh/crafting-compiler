package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SetElement extends Expression {

    private Expression sub;
    private Expression index;
    private Expression value;

    @Override
    public Object interpret() {
        return null;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("SET_ELEMENT:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("SUB:");
        sub.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("INDEX:");
        index.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("VALUE:");
        value.print(depth + 2);
    }
}
