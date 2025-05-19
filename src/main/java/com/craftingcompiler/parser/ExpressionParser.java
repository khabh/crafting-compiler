package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.And;
import com.craftingcompiler.node.Arithmetic;
import com.craftingcompiler.node.ArrayLiteral;
import com.craftingcompiler.node.BooleanLiteral;
import com.craftingcompiler.node.Call;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.GetElement;
import com.craftingcompiler.node.GetVariable;
import com.craftingcompiler.node.MapLiteral;
import com.craftingcompiler.node.NullLiteral;
import com.craftingcompiler.node.NumberLiteral;
import com.craftingcompiler.node.Or;
import com.craftingcompiler.node.Relational;
import com.craftingcompiler.node.SetElement;
import com.craftingcompiler.node.SetVariable;
import com.craftingcompiler.node.StringLiteral;
import com.craftingcompiler.node.Unary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpressionParser {

    private TokenCursor cursor;

    Expression parse(TokenCursor cursor) {
        this.cursor = cursor;
        return parseAssignment();
    }

    private Expression parseAssignment() {
        Expression expression = parseOr();
        if (!cursor.is(Kind.ASSIGNMENT)) {
            return expression;
        }
        cursor.consume(Kind.ASSIGNMENT);
        if (expression instanceof GetVariable getVariable) {
            return new SetVariable(getVariable.getName(), parseAssignment());
        }
        if (expression instanceof GetElement getElement) {
            return new SetElement(getElement.getSub(), getElement.getIndex(), parseAssignment());
        }
        throw new IllegalArgumentException("잘못된 연산식입니다.");
    }

    private Expression parseOr() {
        var expression = parseAnd();
        while (cursor.tryConsume(Kind.LOGICAL_OR)) {
            expression = new Or(expression, parseAnd());
        }
        return expression;
    }

    private Expression parseAnd() {
        var expression = parseRelational();
        while (cursor.tryConsume(Kind.LOGICAL_AND)) {
            expression = new And(expression, parseRelational());
        }
        return expression;
    }

    private Expression parseRelational() {
        Set<Kind> operators = Set.of(Kind.EQUAL, Kind.NOT_EQUAL, Kind.LESS_THAN, Kind.GREATER_THAN, Kind.LESS_OR_EQUAL,
                Kind.GREATER_OR_EQUAL);
        var expression = parseArithmetic1();
        while (operators.contains(cursor.current().getKind())) {
            Kind kind = cursor.current().getKind();
            cursor.next();
            expression = new Relational(kind, expression, parseArithmetic1());
        }
        return expression;
    }

    private Expression parseArithmetic1() {
        Set<Kind> operators = Set.of(Kind.ADD, Kind.SUBTRACT);
        var expression = parseArithmetic2();
        while (operators.contains(cursor.current().getKind())) {
            Kind kind = cursor.current().getKind();
            cursor.next();
            expression = new Arithmetic(kind, expression, parseArithmetic2());
        }
        return expression;
    }

    private Expression parseArithmetic2() {
        Set<Kind> operators = Set.of(Kind.MULTIPLY, Kind.DIVIDE, Kind.MODULO);
        var expression = parseUnary();
        while (operators.contains(cursor.current().getKind())) {
            Kind kind = cursor.current().getKind();
            cursor.next();
            expression = new Arithmetic(kind, expression, parseUnary());
        }
        return expression;
    }

    private Expression parseUnary() {
        Set<Kind> operators = Set.of(Kind.ADD, Kind.SUBTRACT);
        if (operators.contains(cursor.current().getKind())) {
            Kind kind = cursor.current().getKind();
            cursor.next();
            Expression sub = parseUnary();
            return new Unary(kind, sub);
        }
        return parseOperand();
    }

    private Expression parseOperand() {
        if (cursor.is(Kind.NULL_LITERAL)) {
            return parsePostfix(parseNullLiteral());
        }
        if (cursor.is(Kind.TRUE_LITERAL) || cursor.is(Kind.FALSE_LITERAL)) {
            return parsePostfix(parseBooleanLiteral());
        }
        if (cursor.is(Kind.NUMBER_LITERAL)) {
            return parsePostfix(parseNumberLiteral());
        }
        if (cursor.is(Kind.STRING_LITERAL)) {
            return parsePostfix(parseStringLiteral());
        }
        if (cursor.is(Kind.LEFT_BRACKET)) {
            return parsePostfix(parseListLiteral());
        }
        if (cursor.is(Kind.LEFT_BRACE)) {
            return parsePostfix(parseMapLiteral());
        }
        if (cursor.is(Kind.IDENTIFIER)) {
            return parsePostfix(parseIdentifier());
        }
        if (cursor.is(Kind.LEFT_PAREN)) {
            return parsePostfix(parseInnerExpression());
        }

        throw new IllegalArgumentException("잘못된 식입니다.");
    }

    private Expression parseNullLiteral() {
        cursor.consume(Kind.NULL_LITERAL);
        return new NullLiteral();
    }

    private Expression parseBooleanLiteral() {
        boolean value = cursor.current().isKindEquals(Kind.TRUE_LITERAL);
        cursor.next();
        return new BooleanLiteral(value);
    }

    private Expression parseNumberLiteral() {
        double value = Double.parseDouble(cursor.current().getValue());
        cursor.consume(Kind.NUMBER_LITERAL);
        return new NumberLiteral(value);
    }

    private Expression parseStringLiteral() {
        String value = cursor.current().getValue();
        cursor.consume(Kind.STRING_LITERAL);
        return new StringLiteral(value);
    }

    private Expression parseListLiteral() {
        List<Expression> expressions = new ArrayList<>();
        cursor.consume(Kind.LEFT_BRACKET);
        if (!cursor.is(Kind.RIGHT_BRACKET)) {
            do {
                expressions.add(parseExpression());
            } while (cursor.tryConsume(Kind.COMMA));
        }
        cursor.consume(Kind.RIGHT_BRACKET);
        return new ArrayLiteral(expressions);
    }

    private Expression parseMapLiteral() {
        Map<String, Expression> map = new HashMap<>();
        cursor.consume(Kind.LEFT_BRACE);
        if (!cursor.is(Kind.RIGHT_BRACE)) {
            do {
                String key = cursor.current().getValue();
                cursor.consume(Kind.STRING_LITERAL);
                cursor.consume(Kind.COLON);
                Expression expression = parseExpression();
                map.put(key, expression);
            } while (cursor.tryConsume(Kind.RIGHT_BRACE));
        }
        cursor.consume(Kind.RIGHT_BRACE);
        return new MapLiteral(map);
    }

    private Expression parseIdentifier() {
        String name = cursor.current().getValue();
        cursor.consume(Kind.IDENTIFIER);
        return new GetVariable(name);
    }

    private Expression parseInnerExpression() {
        cursor.consume(Kind.LEFT_PAREN);
        Expression expression = parseExpression();
        cursor.consume(Kind.RIGHT_PAREN);
        return expression;
    }

    private Expression parsePostfix(Expression sub) {
        while (true) {
            if (cursor.is(Kind.LEFT_PAREN)) {
                sub = parseCall(sub);
                continue;
            }
            if (cursor.is(Kind.LEFT_BRACKET)) {
                sub = parseElement(sub);
                continue;
            }
            return sub;
        }
    }

    private Expression parseElement(Expression sub) {
        cursor.consume(Kind.LEFT_BRACKET);
        Expression inner = parseExpression();
        cursor.consume(Kind.RIGHT_BRACKET);
        return new GetElement(sub, inner);
    }

    private Expression parseCall(Expression sub) {
        cursor.consume(Kind.LEFT_PAREN);
        List<Expression> arguments = new ArrayList<>();
        if (!cursor.is(Kind.RIGHT_PAREN)) {
            do {
                arguments.add(parseExpression());
            } while (cursor.tryConsume(Kind.COMMA));
        }
        cursor.consume(Kind.RIGHT_PAREN);
        return new Call(sub, arguments);
    }

    private Expression parseExpression() {
        return parseAssignment();
    }
}
