package com.craftingcompiler.parser.statement;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.Statement;
import com.craftingcompiler.parser.ParserRegistry;
import com.craftingcompiler.parser.TokenCursor;
import java.util.ArrayList;
import java.util.List;

public abstract class StatementParser {

    public abstract Statement parse(TokenCursor cursor);

    protected List<Statement> parseBlock(TokenCursor cursor) {
        cursor.consume(Kind.LEFT_BRACE);
        List<Statement> statements = new ArrayList<>();
        while (!cursor.is(Kind.RIGHT_BRACE)) {
            if (cursor.is(Kind.END_OF_TOKEN)) {
                throw new IllegalArgumentException(cursor.current().getValue() + " 잘못된 구문입니다.");
            }

            Kind currentKind = cursor.current().getKind();
            StatementParser parser = ParserRegistry.getParser(currentKind);
            statements.add(parser.parse(cursor));
        }
        cursor.consume(Kind.RIGHT_BRACE);

        return statements;
    }

    protected Expression parseExpression(TokenCursor cursor) {
        return ParserRegistry.getExpressionParser().parse(cursor);
    }
}
