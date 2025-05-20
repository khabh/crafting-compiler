package com.craftingcompiler.parser.statement;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.Statement;
import com.craftingcompiler.node.Wash;
import com.craftingcompiler.parser.ParserRegistry;
import com.craftingcompiler.parser.TokenCursor;

public class WashParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        cursor.consume(Kind.WASH);
        Expression assignable = ParserRegistry.ASSIGNABLE_PARSER.parse(cursor);
        cursor.consume(Kind.SEMICOLON);

        return new Wash(assignable);
    }
}
