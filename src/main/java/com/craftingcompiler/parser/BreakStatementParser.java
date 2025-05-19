package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Break;
import com.craftingcompiler.node.Statement;

public class BreakStatementParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        cursor.consume(Kind.BREAK);
        cursor.consume(Kind.SEMICOLON);

        return new Break();
    }
}
