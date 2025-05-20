package com.craftingcompiler.parser.expression;

import com.craftingcompiler.node.Expression;
import com.craftingcompiler.parser.TokenCursor;

public class ExpressionParser {

    public Expression parse(TokenCursor cursor) {
        return new AssignmentParser(cursor).parse();
    }
}
