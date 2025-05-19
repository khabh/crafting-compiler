package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.ExpressionStatement;
import com.craftingcompiler.node.Statement;

public class ExpressionStatementParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        Expression expression = parseExpression(cursor);
        cursor.consume(Kind.SEMICOLON);

        return new ExpressionStatement(expression);
    }
}
