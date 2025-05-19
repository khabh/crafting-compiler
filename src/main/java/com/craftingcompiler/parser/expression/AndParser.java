package com.craftingcompiler.parser.expression;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.And;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.parser.TokenCursor;

public class AndParser implements ExpressionParserBase {
    private final TokenCursor cursor;
    private final ExpressionParserBase next;

    public AndParser(TokenCursor cursor) {
        this(cursor, new RelationalParser(cursor));
    }

    public AndParser(TokenCursor cursor, ExpressionParserBase next) {
        this.cursor = cursor;
        this.next = next;
    }

    @Override
    public Expression parse() {
        var expression = next.parse();
        while (cursor.tryConsume(Kind.LOGICAL_AND)) {
            expression = new And(expression, next.parse());
        }
        return expression;
    }
}
