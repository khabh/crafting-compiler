package com.craftingcompiler.node;

import com.craftingcompiler.Kind;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Unary extends Expression {

    private Kind kind;
    private Expression sub;

    @Override
    public Object interpret() {
        return null;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println(kind.toString());
        sub.print(depth + 1);
    }
}
