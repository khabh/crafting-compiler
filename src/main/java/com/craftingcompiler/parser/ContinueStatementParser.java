package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Continue;
import com.craftingcompiler.node.Statement;

public class ContinueStatementParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        cursor.consume(Kind.CONTINUE);
        cursor.consume(Kind.SEMICOLON);

        return new Continue();
    }
}
