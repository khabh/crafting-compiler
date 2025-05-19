package com.craftingcompiler.parser.statement;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Return;
import com.craftingcompiler.node.Statement;
import com.craftingcompiler.parser.TokenCursor;

public class ReturnParser extends StatementParser {

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
