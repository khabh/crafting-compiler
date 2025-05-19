package com.craftingcompiler.parser.expression;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.Or;
import com.craftingcompiler.parser.TokenCursor;

public class OrParser implements ExpressionParserBase {
    private final TokenCursor cursor;
    private final ExpressionParserBase next;

    public OrParser(TokenCursor cursor) {
        this(cursor, new AndParser(cursor));
    }

    public OrParser(TokenCursor cursor, ExpressionParserBase next) {
        this.cursor = cursor;
        this.next = next;
    }

    @Override
    public Expression parse() {
        var expression = next.parse();
        while (cursor.tryConsume(Kind.LOGICAL_OR)) {
            expression = new Or(expression, next.parse());
        }
        return expression;
    }
}

