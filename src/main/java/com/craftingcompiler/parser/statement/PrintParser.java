package com.craftingcompiler.parser.statement;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.Print;
import com.craftingcompiler.node.Statement;
import com.craftingcompiler.parser.TokenCursor;
import java.util.ArrayList;
import java.util.List;

public class PrintParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        boolean lineFeed = cursor.is(Kind.PRINT_LINE);
        List<Expression> arguments = new ArrayList<>();
        cursor.next();
        if (!cursor.is(Kind.SEMICOLON)) {
            do {
                arguments.add(parseExpression(cursor));
            } while (cursor.tryConsume(Kind.COMMA));
        }
        cursor.consume(Kind.SEMICOLON);
        return new Print(lineFeed, arguments);
    }
}
