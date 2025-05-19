package com.craftingcompiler.parser.expression;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.Unary;
import com.craftingcompiler.parser.TokenCursor;
import java.util.Set;

public class UnaryParser implements ExpressionParserBase {
    private final TokenCursor cursor;
    private final ExpressionParserBase next;

    public UnaryParser(TokenCursor cursor) {
        this(cursor, new OperandParser(cursor));
    }

    public UnaryParser(TokenCursor cursor, ExpressionParserBase next) {
        this.cursor = cursor;
        this.next = next;
    }

    @Override
    public Expression parse() {
        Set<Kind> operators = Set.of(Kind.ADD, Kind.SUBTRACT);
        if (operators.contains(cursor.current().getKind())) {
            Kind kind = cursor.current().getKind();
            cursor.next();
            Expression sub = parse();
            return new Unary(kind, sub);
        }
        return next.parse();
    }
}
