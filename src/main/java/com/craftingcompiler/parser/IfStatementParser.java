package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.If;
import com.craftingcompiler.node.Statement;
import java.util.ArrayList;
import java.util.List;

public class IfStatementParser extends StatementParser {

    @Override
    public If parse(TokenCursor cursor) {
        cursor.consume(Kind.IF);
        List<Expression> conditions = new ArrayList<>();
        List<List<Statement>> blocks = new ArrayList<>();
        List<Statement> elseBlock = new ArrayList<>();
        do {
            Expression condition = parseExpression(cursor);
            if (condition == null) {
                throw new IllegalArgumentException("if문에 조건식이 없습니다.");
            }
            conditions.add(condition);
            blocks.add(parseBlock(cursor));
        } while (cursor.tryConsume(Kind.ELIF));
        if (cursor.tryConsume(Kind.ELSE)) {
            blocks.add(parseBlock(cursor));
        }
        return new If(conditions, blocks, elseBlock);
    }
}
