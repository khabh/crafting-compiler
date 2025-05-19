package com.craftingcompiler.parser.expression;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.GetElement;
import com.craftingcompiler.node.GetVariable;
import com.craftingcompiler.node.SetElement;
import com.craftingcompiler.node.SetVariable;
import com.craftingcompiler.parser.TokenCursor;

public class AssignmentParser implements ExpressionParserBase {

    private final TokenCursor cursor;
    private final ExpressionParserBase next;

    public AssignmentParser(TokenCursor cursor) {
        this(cursor, new OrParser(cursor));
    }

    public AssignmentParser(TokenCursor cursor, ExpressionParserBase next) {
        this.cursor = cursor;
        this.next = next;
    }

    @Override
    public Expression parse() {
        Expression expression = next.parse();
        if (!cursor.is(Kind.ASSIGNMENT)) {
            return expression;
        }
        cursor.consume(Kind.ASSIGNMENT);
        if (expression instanceof GetVariable getVariable) {
            return new SetVariable(getVariable.getName(), parse());
        }
        if (expression instanceof GetElement getElement) {
            return new SetElement(getElement.getSub(), getElement.getIndex(), parse());
        }
        throw new IllegalArgumentException("잘못된 연산식입니다.");
    }
}
