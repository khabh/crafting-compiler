package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArrayLiteral extends Expression {

    private List<Expression> values;

    @Override
    public Object interpret() {
        return null;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("[");
        for (Expression value : values) {
            value.print(depth + 1);
        }
        SyntaxPrinter.indent(depth);
        System.out.println("]");
    }
}
