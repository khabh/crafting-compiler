package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.lang.reflect.Array;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArrayLiteral extends Expression {

    private List<Expression> values;

    @Override
    public Object interpret() {
        Object[] array = (Object[]) Array.newInstance(Object.class, values.size());
        for (int i = 0; i < values.size(); i++) {
            Array.set(array, i, values.get(i).interpret());
        }
        return new CustomArray(array);
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
