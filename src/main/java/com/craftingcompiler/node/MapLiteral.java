package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.Map;

public class MapLiteral extends Expression {

    private Map<String, Expression> values;

    public MapLiteral(Map<String, Expression> values) {
        this.values = values;
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
