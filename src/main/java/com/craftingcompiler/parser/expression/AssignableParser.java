package com.craftingcompiler.parser.expression;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.GetElement;
import com.craftingcompiler.node.GetProperty;
import com.craftingcompiler.node.GetVariable;
import com.craftingcompiler.parser.ParserRegistry;
import com.craftingcompiler.parser.TokenCursor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AssignableParser {

    public Expression parse(TokenCursor cursor) {
        String name = cursor.current().getValue();
        cursor.consume(Kind.IDENTIFIER);
        Expression base = new GetVariable(name);

        while (true) {
            if (cursor.is(Kind.DOT)) {
                base = parseProperty(cursor, base);
                continue;
            }
            if (cursor.is(Kind.LEFT_BRACKET)) {
                base = parseElement(cursor, base);
                continue;
            }
            return base;
        }
    }

    private Expression parseProperty(TokenCursor cursor, Expression base) {
        cursor.consume(Kind.DOT);
        String name = cursor.current().getValue();
        cursor.consume(Kind.IDENTIFIER);
        return new GetProperty(base, name);
    }

    private Expression parseElement(TokenCursor cursor, Expression base) {
        cursor.consume(Kind.LEFT_BRACKET);
        Expression inner = ParserRegistry.getExpressionParser().parse(cursor);
        cursor.consume(Kind.RIGHT_BRACKET);
        return new GetElement(base, inner);
    }
}
