package com.craftingcompiler.parser.statement;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Continue;
import com.craftingcompiler.node.Statement;
import com.craftingcompiler.parser.TokenCursor;

public class ContinueStatementParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        cursor.consume(Kind.CONTINUE);
        cursor.consume(Kind.SEMICOLON);

        return new Continue();
    }
}
