package com.craftingcompiler.parser.statement;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.For;
import com.craftingcompiler.node.Statement;
import com.craftingcompiler.node.Variable;
import com.craftingcompiler.parser.TokenCursor;
import java.util.List;

public class ForParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        cursor.consume(Kind.FOR);

        String variableName = cursor.current().getValue();
        cursor.consume(Kind.IDENTIFIER, Kind.ASSIGNMENT);
        Expression variableExpression = parseExpression(cursor);

        if (variableExpression == null) {
            throw new IllegalArgumentException(cursor.current().getValue() + "for문에 초기화식이 없습니다.");
        }

        Variable variable = new Variable(variableName, variableExpression);
        cursor.consume(Kind.COMMA);
        Expression condition = parseExpression(cursor);
        if (condition == null) {
            throw new IllegalArgumentException(cursor.current().getValue() + "for문에 조건식이 없습니다.");
        }

        cursor.consume(Kind.COMMA);
        Expression expression = parseExpression(cursor);
        if (expression == null) {
            throw new IllegalArgumentException(cursor.current().getValue() + "for문에 증감식이 없습니다.");
        }

        List<Statement> block = parseBlock(cursor);

        return new For(variable, condition, expression, block);
    }
}
