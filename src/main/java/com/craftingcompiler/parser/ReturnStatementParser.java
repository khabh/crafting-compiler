package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Return;
import com.craftingcompiler.node.Statement;

public class ReturnStatementParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        cursor.consume(Kind.RETURN);
        var expression = parseExpression(cursor);
        if (expression == null) {
            throw new IllegalArgumentException("return문에 식이 없습니다.");
        }
        cursor.consume(Kind.SEMICOLON);
        return new Return(expression);
    }
}
