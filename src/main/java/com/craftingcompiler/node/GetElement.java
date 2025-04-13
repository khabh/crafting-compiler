package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetElement extends Expression {

    private Expression sub;
    private Expression index;

    @Override
    public Object interpret() {
        var object = sub.interpret();
        var number = index.interpret();
        if (object instanceof CustomArray && number instanceof Number) {
            return ((CustomArray) object).get(((Number) number).intValue());
        }
        return null;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("GET_ELEMENT:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("SUB:");
        sub.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("INDEX:");
        index.print(depth + 2);
    }
}
