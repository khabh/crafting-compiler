package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.node.And;
import com.craftingcompiler.node.Arithmetic;
import com.craftingcompiler.node.ArrayLiteral;
import com.craftingcompiler.node.BooleanLiteral;
import com.craftingcompiler.node.Break;
import com.craftingcompiler.node.Call;
import com.craftingcompiler.node.Continue;
import com.craftingcompiler.node.Expression;
import com.craftingcompiler.node.ExpressionStatement;
import com.craftingcompiler.node.For;
import com.craftingcompiler.node.Function;
import com.craftingcompiler.node.GetElement;
import com.craftingcompiler.node.GetVariable;
import com.craftingcompiler.node.If;
import com.craftingcompiler.node.MapLiteral;
import com.craftingcompiler.node.NullLiteral;
import com.craftingcompiler.node.NumberLiteral;
import com.craftingcompiler.node.Or;
import com.craftingcompiler.node.Print;
import com.craftingcompiler.node.Program;
import com.craftingcompiler.node.Relational;
import com.craftingcompiler.node.Return;
import com.craftingcompiler.node.SetElement;
import com.craftingcompiler.node.SetVariable;
import com.craftingcompiler.node.Statement;
import com.craftingcompiler.node.StringLiteral;
import com.craftingcompiler.node.Unary;
import com.craftingcompiler.node.Variable;
import com.craftingcompiler.token.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {

    private final TokenCursor tokenCursor;

    public Parser(List<Token> tokens) {
        this.tokenCursor = new TokenCursor(tokens);
    }

    public Program parse() {
        List<Function> functions = new ArrayList<>();
        while (tokenCursor.current().getKind() != Kind.END_OF_TOKEN) {
            Token token = tokenCursor.current();
            if (token.isKindEquals(Kind.FUNCTION)) {
                functions.add(parsFunction());
                continue;
            }
            throw new IllegalArgumentException(token.getValue() + " 잘못된 구문입니다.");
        }
        return new Program(functions);
    }

    public Function parsFunction() {
        tokenCursor.consume(Kind.FUNCTION);
        String functionName = tokenCursor.current().getValue();
        tokenCursor.consume(Kind.IDENTIFIER, Kind.LEFT_PAREN);
        List<String> parameters = parseParameters();
        tokenCursor.consume(Kind.RIGHT_PAREN, Kind.LEFT_BRACE);
        List<Statement> block = parseBlock();
        tokenCursor.consume(Kind.RIGHT_BRACE);

        return new com.craftingcompiler.node.Function(functionName, parameters, block);
    }

    private List<String> parseParameters() {
        List<String> parameters = new ArrayList<>();
        if (!tokenCursor.is(Kind.RIGHT_PAREN)) {
            do {
                parameters.add(tokenCursor.current().getValue());
                tokenCursor.consume(Kind.IDENTIFIER);
            } while (tokenCursor.tryConsume(Kind.COMMA));
        }

        return parameters;
    }

    private List<Statement> parseBlock() {
        List<Statement> statements = new ArrayList<>();
        while (!tokenCursor.is(Kind.RIGHT_BRACE)) {
            if (tokenCursor.is(Kind.END_OF_TOKEN)) {
                throw new IllegalArgumentException(tokenCursor.current().getValue() + " 잘못된 구문입니다.");
            }
            if (tokenCursor.is(Kind.VARIABLE)) {
                statements.add(parseVariable());
                continue;
            }
            if (tokenCursor.is(Kind.FOR)) {
                statements.add(parseFor());
                continue;
            }
            if (tokenCursor.is(Kind.IF)) {
                statements.add(parseIf());
                continue;
            }
            if (tokenCursor.is(Kind.PRINT) || tokenCursor.is(Kind.PRINT_LINE)) {
                statements.add(parsePrint());
                continue;
            }
            if (tokenCursor.is(Kind.RETURN)) {
                statements.add(parseReturn());
                continue;
            }
            if (tokenCursor.is(Kind.BREAK)) {
                statements.add(parseBreak());
                continue;
            }
            if (tokenCursor.is(Kind.CONTINUE)) {
                statements.add(parseContinue());
                continue;
            }

            statements.add(parseExpressionStatement());
        }
        return statements;
    }

    private For parseFor() {
        tokenCursor.consume(Kind.FOR);

        String variableName = tokenCursor.current().getValue();
        tokenCursor.consume(Kind.IDENTIFIER);
        tokenCursor.consume(Kind.ASSIGNMENT);
        Expression variableExpression = parseExpression();

        if (variableExpression == null) {
            throw new IllegalArgumentException(tokenCursor.current().getValue() + "for문에 초기화식이 없습니다.");
        }

        Variable variable = new Variable(variableName, variableExpression);
        tokenCursor.consume(Kind.COMMA);
        Expression condition = parseExpression();
        if (condition == null) {
            throw new IllegalArgumentException(tokenCursor.current().getValue() + "for문에 조건식이 없습니다.");
        }

        tokenCursor.consume(Kind.COMMA);
        Expression expression = parseExpression();
        if (expression == null) {
            throw new IllegalArgumentException(tokenCursor.current().getValue() + "for문에 증감식이 없습니다.");
        }

        tokenCursor.consume(Kind.LEFT_BRACE);
        List<Statement> block = parseBlock();
        tokenCursor.consume(Kind.RIGHT_BRACE);

        return new For(variable, condition, expression, block);
    }

    private If parseIf() {
        tokenCursor.consume(Kind.IF);
        List<Expression> conditions = new ArrayList<>();
        List<List<Statement>> blocks = new ArrayList<>();
        List<Statement> elseBlock = new ArrayList<>();
        do {
            var condition = parseExpression();
            if (condition == null) {
                throw new IllegalArgumentException("if문에 조건식이 없습니다.");
            }
            conditions.add(condition);
            tokenCursor.consume(Kind.LEFT_BRACE);
            blocks.add(parseBlock());
            tokenCursor.consume(Kind.RIGHT_BRACE);
        } while (tokenCursor.tryConsume(Kind.ELIF));
        if (tokenCursor.tryConsume(Kind.ELSE)) {
            tokenCursor.consume(Kind.LEFT_BRACE);
            elseBlock = parseBlock();
            tokenCursor.consume(Kind.RIGHT_BRACE);
        }
        return new If(conditions, blocks, elseBlock);
    }

    private Print parsePrint() {
        boolean lineFeed = tokenCursor.is(Kind.PRINT_LINE);
        List<Expression> arguments = new ArrayList<>();
        tokenCursor.next();
        if (!tokenCursor.is(Kind.SEMICOLON)) {
            do {
                arguments.add(parseExpression());
            } while (tokenCursor.tryConsume(Kind.COMMA));
        }
        tokenCursor.consume(Kind.SEMICOLON);
        return new Print(lineFeed, arguments);
    }

    private Return parseReturn() {
        tokenCursor.consume(Kind.RETURN);
        var expression = parseExpression();
        if (expression == null) {
            throw new IllegalArgumentException("return문에 식이 없습니다.");
        }
        tokenCursor.consume(Kind.SEMICOLON);
        return new Return(expression);
    }

    private Break parseBreak() {
        tokenCursor.consume(Kind.BREAK);
        tokenCursor.consume(Kind.SEMICOLON);

        return new Break();
    }

    private Continue parseContinue() {
        tokenCursor.consume(Kind.CONTINUE);
        tokenCursor.consume(Kind.SEMICOLON);

        return new Continue();
    }

    private Statement parseVariable() {
        tokenCursor.consume(Kind.VARIABLE);
        String name = tokenCursor.current().getValue();
        tokenCursor.consume(Kind.IDENTIFIER, Kind.ASSIGNMENT);
        Expression expression = parseExpression();
        if (expression == null) {
            throw new IllegalArgumentException("변수 선언에 초기화식이 없습니다.");
        }
        tokenCursor.consume(Kind.SEMICOLON);
        return new Variable(name, expression);
    }

    private Statement parseExpressionStatement() {
        Expression expression = parseExpression();
        tokenCursor.consume(Kind.SEMICOLON);
        return new ExpressionStatement(expression);
    }

    private Expression parseExpression() {
        return parseAssignment();
    }

    private Expression parseAssignment() {
        Expression expression = parseOr();
        if (!tokenCursor.is(Kind.ASSIGNMENT)) {
            return expression;
        }
        tokenCursor.consume(Kind.ASSIGNMENT);
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
        while (tokenCursor.tryConsume(Kind.LOGICAL_OR)) {
            expression = new Or(expression, parseAnd());
        }
        return expression;
    }

    private Expression parseAnd() {
        var expression = parseRelational();
        while (tokenCursor.tryConsume(Kind.LOGICAL_AND)) {
            expression = new And(expression, parseRelational());
        }
        return expression;
    }

    private Expression parseRelational() {
        Set<Kind> operators = Set.of(Kind.EQUAL, Kind.NOT_EQUAL, Kind.LESS_THAN, Kind.GREATER_THAN, Kind.LESS_OR_EQUAL,
                Kind.GREATER_OR_EQUAL);
        var expression = parseArithmetic1();
        while (operators.contains(tokenCursor.current().getKind())) {
            Kind kind = tokenCursor.current().getKind();
            tokenCursor.next();
            expression = new Relational(kind, expression, parseArithmetic1());
        }
        return expression;
    }

    private Expression parseArithmetic1() {
        Set<Kind> operators = Set.of(Kind.ADD, Kind.SUBTRACT);
        var expression = parseArithmetic2();
        while (operators.contains(tokenCursor.current().getKind())) {
            Kind kind = tokenCursor.current().getKind();
            tokenCursor.next();
            expression = new Arithmetic(kind, expression, parseArithmetic2());
        }
        return expression;
    }

    private Expression parseArithmetic2() {
        Set<Kind> operators = Set.of(Kind.MULTIPLY, Kind.DIVIDE, Kind.MODULO);
        var expression = parseUnary();
        while (operators.contains(tokenCursor.current().getKind())) {
            Kind kind = tokenCursor.current().getKind();
            tokenCursor.next();
            expression = new Arithmetic(kind, expression, parseUnary());
        }
        return expression;
    }

    private Expression parseUnary() {
        Set<Kind> operators = Set.of(Kind.ADD, Kind.SUBTRACT);
        if (operators.contains(tokenCursor.current().getKind())) {
            Kind kind = tokenCursor.current().getKind();
            tokenCursor.next();
            Expression sub = parseUnary();
            return new Unary(kind, sub);
        }
        return parseOperand();
    }

    private Expression parseOperand() {
        if (tokenCursor.is(Kind.NULL_LITERAL)) {
            return parsePostfix(parseNullLiteral());
        }
        if (tokenCursor.is(Kind.TRUE_LITERAL) || tokenCursor.is(Kind.FALSE_LITERAL)) {
            return parsePostfix(parseBooleanLiteral());
        }
        if (tokenCursor.is(Kind.NUMBER_LITERAL)) {
            return parsePostfix(parseNumberLiteral());
        }
        if (tokenCursor.is(Kind.STRING_LITERAL)) {
            return parsePostfix(parseStringLiteral());
        }
        if (tokenCursor.is(Kind.LEFT_BRACKET)) {
            return parsePostfix(parseListLiteral());
        }
        if (tokenCursor.is(Kind.LEFT_BRACE)) {
            return parsePostfix(parseMapLiteral());
        }
        if (tokenCursor.is(Kind.IDENTIFIER)) {
            return parsePostfix(parseIdentifier());
        }
        if (tokenCursor.is(Kind.LEFT_PAREN)) {
            return parsePostfix(parseInnerExpression());
        }

        throw new IllegalArgumentException("잘못된 식입니다.");
    }

    private Expression parseNullLiteral() {
        tokenCursor.consume(Kind.NULL_LITERAL);
        return new NullLiteral();
    }

    private Expression parseBooleanLiteral() {
        boolean value = tokenCursor.current().isKindEquals(Kind.TRUE_LITERAL);
        tokenCursor.next();
        return new BooleanLiteral(value);
    }

    private Expression parseNumberLiteral() {
        double value = Double.parseDouble(tokenCursor.current().getValue());
        tokenCursor.consume(Kind.NUMBER_LITERAL);
        return new NumberLiteral(value);
    }

    private Expression parseStringLiteral() {
        String value = tokenCursor.current().getValue();
        tokenCursor.consume(Kind.STRING_LITERAL);
        return new StringLiteral(value);
    }

    private Expression parseListLiteral() {
        List<Expression> expressions = new ArrayList<>();
        tokenCursor.consume(Kind.LEFT_BRACKET);
        if (!tokenCursor.is(Kind.RIGHT_BRACKET)) {
            do {
                expressions.add(parseExpression());
            } while (tokenCursor.tryConsume(Kind.COMMA));
        }
        tokenCursor.consume(Kind.RIGHT_BRACKET);
        return new ArrayLiteral(expressions);
    }

    private Expression parseMapLiteral() {
        Map<String, Expression> map = new HashMap<>();
        tokenCursor.consume(Kind.LEFT_BRACE);
        if (!tokenCursor.is(Kind.RIGHT_BRACE)) {
            do {
                String key = tokenCursor.current().getValue();
                tokenCursor.consume(Kind.STRING_LITERAL);
                tokenCursor.consume(Kind.COLON);
                Expression expression = parseExpression();
                map.put(key, expression);
            } while (tokenCursor.tryConsume(Kind.RIGHT_BRACE));
        }
        tokenCursor.consume(Kind.RIGHT_BRACE);
        return new MapLiteral(map);
    }

    private Expression parseIdentifier() {
        String name = tokenCursor.current().getValue();
        tokenCursor.consume(Kind.IDENTIFIER);
        return new GetVariable(name);
    }

    private Expression parseInnerExpression() {
        tokenCursor.consume(Kind.LEFT_PAREN);
        Expression expression = parseExpression();
        tokenCursor.consume(Kind.RIGHT_PAREN);
        return expression;
    }

    private Expression parsePostfix(Expression sub) {
        while (true) {
            if (tokenCursor.is(Kind.LEFT_PAREN)) {
                sub = parseCall(sub);
                continue;
            }
            if (tokenCursor.is(Kind.LEFT_BRACKET)) {
                sub = parseElement(sub);
                continue;
            }
            return sub;
        }
    }

    private Expression parseElement(Expression sub) {
        tokenCursor.consume(Kind.LEFT_BRACKET);
        Expression inner = parseExpression();
        tokenCursor.consume(Kind.RIGHT_BRACKET);
        return new GetElement(sub, inner);
    }

    private Expression parseCall(Expression sub) {
        tokenCursor.consume(Kind.LEFT_PAREN);
        List<Expression> arguments = new ArrayList<>();
        if (!tokenCursor.is(Kind.RIGHT_PAREN)) {
            do {
                arguments.add(parseExpression());
            } while (tokenCursor.tryConsume(Kind.COMMA));
        }
        tokenCursor.consume(Kind.RIGHT_PAREN);
        return new Call(sub, arguments);
    }
}
