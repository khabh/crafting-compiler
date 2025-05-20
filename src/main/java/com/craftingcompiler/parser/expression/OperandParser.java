package com.craftingcompiler.parser.expression;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.ArrayLiteral;
import com.craftingcompiler.node.BooleanLiteral;
import com.craftingcompiler.node.Call;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.GetElement;
import com.craftingcompiler.node.GetProperty;
import com.craftingcompiler.node.GetVariable;
import com.craftingcompiler.node.MapLiteral;
import com.craftingcompiler.node.NullLiteral;
import com.craftingcompiler.node.NumberLiteral;
import com.craftingcompiler.node.StringLiteral;
import com.craftingcompiler.parser.ParserRegistry;
import com.craftingcompiler.parser.TokenCursor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class OperandParser implements ExpressionParserBase {

    private final Map<Kind, Supplier<Expression>> operandParsers;
    private final TokenCursor cursor;

    public OperandParser(TokenCursor cursor) {
        this.cursor = cursor;
        this.operandParsers = Map.of(
                Kind.NULL_LITERAL, this::parseNullLiteral,
                Kind.TRUE_LITERAL, this::parseBooleanLiteral,
                Kind.FALSE_LITERAL, this::parseBooleanLiteral,
                Kind.NUMBER_LITERAL, this::parseNumberLiteral,
                Kind.STRING_LITERAL, this::parseStringLiteral,
                Kind.LEFT_BRACKET, this::parseListLiteral,
                Kind.LEFT_BRACE, this::parseMapLiteral,
                Kind.IDENTIFIER, this::parseIdentifier,
                Kind.LEFT_PAREN, this::parseInnerExpression
        );
    }

    @Override
    public Expression parse() {
        Kind currentKind = cursor.current().getKind();
        Supplier<Expression> parser = operandParsers.get(currentKind);
        if (parser != null) {
            return parsePostfix(parser.get());
        }
        throw new IllegalArgumentException("잘못된 식입니다: " + currentKind);
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
            if (cursor.is(Kind.DOT)) {
                sub = parseProperty(sub);
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

    private Expression parseProperty(Expression sub) {
        cursor.consume(Kind.DOT);
        String name = cursor.current().getValue();
        cursor.consume(Kind.IDENTIFIER);
        return new GetProperty(sub, name);
    }

    private Expression parseExpression() {
        return ParserRegistry.getExpressionParser().parse(cursor);
    }
}
