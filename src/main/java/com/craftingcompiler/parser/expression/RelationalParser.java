package com.craftingcompiler.parser.expression;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.Relational;
import com.craftingcompiler.parser.TokenCursor;
import java.util.Set;

public class RelationalParser implements ExpressionParserBase {

    private final TokenCursor cursor;
    private final ExpressionParserBase next;

    public RelationalParser(TokenCursor cursor) {
        this(cursor, new AdditiveParser(cursor));
    }

    public RelationalParser(TokenCursor cursor, ExpressionParserBase next) {
        this.cursor = cursor;
        this.next = next;
    }

    @Override
    public Expression parse() {
        Set<Kind> operators = Set.of(Kind.EQUAL, Kind.NOT_EQUAL, Kind.LESS_THAN, Kind.GREATER_THAN, Kind.LESS_OR_EQUAL, Kind.GREATER_OR_EQUAL);
        var expression = next.parse();
        while (operators.contains(cursor.current().getKind())) {
            Kind kind = cursor.current().getKind();
            cursor.next();
            expression = new Relational(kind, expression, next.parse());
        }
        return expression;
    }
}
