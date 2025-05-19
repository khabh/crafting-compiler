package com.craftingcompiler.parser.expression;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Arithmetic;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.parser.TokenCursor;
import java.util.Set;

public class MultiplicativeParser implements ExpressionParserBase {

    private static final Set<Kind> OPERATORS = Set.of(Kind.MULTIPLY, Kind.DIVIDE, Kind.MODULO);

    private final TokenCursor cursor;
    private final ExpressionParserBase next;

    public MultiplicativeParser(TokenCursor cursor) {
        this(cursor, new UnaryParser(cursor));
    }

    public MultiplicativeParser(TokenCursor cursor, ExpressionParserBase next) {
        this.cursor = cursor;
        this.next = next;
    }

    @Override
    public Expression parse() {
        var expression = next.parse();
        while (OPERATORS.contains(cursor.current().getKind())) {
            Kind kind = cursor.current().getKind();
            cursor.next();
            expression = new Arithmetic(kind, expression, next.parse());
        }
        return expression;
    }
}
