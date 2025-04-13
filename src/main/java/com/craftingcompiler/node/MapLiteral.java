package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MapLiteral extends Expression {

    private Map<String, Expression> values;

    @Override
    public Object interpret() {
        return null;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("{");
        for (Map.Entry<String, Expression> entry : values.entrySet()) {
            SyntaxPrinter.indent(depth + 1);
            System.out.print(entry.getKey() + ": ");
            entry.getValue().print(depth + 1);
        }
        SyntaxPrinter.indent(depth);
        System.out.println("}");
    }
}
