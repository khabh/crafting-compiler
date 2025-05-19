package com.craftingcompiler.parser.statement;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Break;
import com.craftingcompiler.node.Statement;
import com.craftingcompiler.parser.TokenCursor;

public class BreakParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        cursor.consume(Kind.BREAK);
        cursor.consume(Kind.SEMICOLON);

        return new Break();
    }
}
