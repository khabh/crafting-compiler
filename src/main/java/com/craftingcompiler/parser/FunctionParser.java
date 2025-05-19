package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Function;
import com.craftingcompiler.node.Statement;
import java.util.ArrayList;
import java.util.List;

public class FunctionParser extends StatementParser {

    @Override
    public Statement parse(TokenCursor cursor) {
        cursor.consume(Kind.FUNCTION);
        String functionName = cursor.current().getValue();
        cursor.consume(Kind.IDENTIFIER);
        List<String> parameters = parseParameters(cursor);
        List<Statement> block = parseBlock(cursor);

        return new Function(functionName, parameters, block);
    }

    private List<String> parseParameters(TokenCursor cursor) {
        cursor.consume(Kind.LEFT_PAREN);
        if (cursor.is(Kind.RIGHT_PAREN)) {
            cursor.consume(Kind.RIGHT_PAREN);
            return List.of();
        }

        List<String> parameters = new ArrayList<>();
        do {
            parameters.add(cursor.current().getValue());
            cursor.consume(Kind.IDENTIFIER);
        } while (cursor.tryConsume(Kind.COMMA));
        cursor.consume(Kind.RIGHT_PAREN);

        return parameters;
    }
}
