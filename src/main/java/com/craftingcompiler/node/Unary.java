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
    public void generate() {

    }

    @Override
    public Object interpret() {
        var value = sub.interpret();
        if (kind == Kind.Add && value instanceof Number) {
            return ((Number) value).doubleValue() + 1;
        }
        if (kind == Kind.Subtract && value instanceof Number) {
            return ((Number) value).doubleValue() - 1;
        }
        return 0.0;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println(kind.toString());
        sub.print(depth + 1);
    }
}
