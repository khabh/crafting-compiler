package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.Statement;
import com.craftingcompiler.node.Variable;

public class VariableDeclarationParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        cursor.consume(Kind.VARIABLE, "변수 선언 키워드(plant)가 필요합니다.");
        String name = cursor.current().getValue();

        cursor.consume(Kind.IDENTIFIER, Kind.ASSIGNMENT);
        Expression expression = parseExpression(cursor);
        if (expression == null) {
            throw new IllegalArgumentException("변수 선언에 초기화식이 없습니다.");
        }
        cursor.consume(Kind.SEMICOLON);
        return new Variable(name, expression);
    }
}
