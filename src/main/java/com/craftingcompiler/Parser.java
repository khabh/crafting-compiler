package com.craftingcompiler;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {
    private final LinkedList<Token> tokens;

    public Parser(List<Token> tokens) {
        this.tokens = new LinkedList<>(tokens);
    }

    public Program parse() {
        List<Function> functions = new ArrayList<>();
        while (getCurrent().getKind() != Kind.END_OF_TOKEN) {
            Token token = getCurrent();
            if (token.isKindEquals(Kind.FUNCTION)) {
                functions.add(parseFunction());
                continue;
            }
            throw new IllegalArgumentException(token.getValue() + " 잘못된 구문입니다.");
        }
        return new Program(functions);
    }

    private Function parseFunction() {
        skipCurrent(Kind.FUNCTION);
        String functionName = getCurrent().getValue();
        skipCurrent(Kind.IDENTIFIER, Kind.LeftParen);
        List<String> parameters = parseParameters();
        skipCurrent(Kind.RIGHT_PAREN, Kind.LeftBrace);
        List<Statement> block = parseBlock();
        skipCurrent(Kind.RIGHT_BRACE);

        return new Function(functionName, parameters, block);
    }

    private List<String> parseParameters() {
        List<String> parameters = new ArrayList<>();
        if (!isCurrentKind(Kind.RIGHT_PAREN)) {
            do {
                parameters.add(getCurrent().getValue());
                skipCurrent(Kind.IDENTIFIER);
            } while (skipCurrentIf(Kind.COMMA));
        }

        return parameters;
    }

    private List<Statement> parseBlock() {
        List<Statement> statements = new ArrayList<>();
        while (!isCurrentKind(Kind.RIGHT_BRACE)) {
            if (isCurrentKind(Kind.END_OF_TOKEN)) {
                throw new IllegalArgumentException(getCurrent().getValue() + " 잘못된 구문입니다.");
            }
            if (isCurrentKind(Kind.VARIABLE)) {
                statements.add(parseVariable());
                continue;
            }
            if (isCurrentKind(Kind.FOR)) {
                statements.add(parseFor());
                continue;
            }
            if (isCurrentKind(Kind.IF)) {
                statements.add(parseIf());
                continue;
            }
            if (isCurrentKind(Kind.PRINT) || isCurrentKind(Kind.PRINT_LINE)) {
                statements.add(parsePrint());
                continue;
            }
            if (isCurrentKind(Kind.RETURN)) {
                statements.add(parseReturn());
                continue;
            }
            if (isCurrentKind(Kind.BREAK)) {
                statements.add(parseBreak());
                continue;
            }
            if (isCurrentKind(Kind.CONTINUE)) {
                statements.add(parseContinue());
                continue;
            }

            statements.add(parseExpressionStatement());
        }
        return statements;
    }

    private For parseFor() {
        skipCurrent(Kind.FOR);

        String variableName = getCurrent().getValue();
        skipCurrent(Kind.IDENTIFIER);
        skipCurrent(Kind.ASSIGNMENT);
        Expression variableExpression = parseExpression();

        if (variableExpression == null) {
            throw new IllegalArgumentException(getCurrent().getValue() + "for문에 초기화식이 없습니다.");
        }

        Variable variable = new Variable(variableName, variableExpression);
        skipCurrent(Kind.COMMA);
        Expression condition = parseExpression();
        if (condition == null) {
            throw new IllegalArgumentException(getCurrent().getValue() + "for문에 조건식이 없습니다.");
        }

        skipCurrent(Kind.COMMA);
        Expression expression = parseExpression();
        if (expression == null) {
            throw new IllegalArgumentException(getCurrent().getValue() + "for문에 증감식이 없습니다.");
        }

        skipCurrent(Kind.LeftBrace);
        List<Statement> block = parseBlock();
        skipCurrent(Kind.RIGHT_BRACE);

        return new For(variable, condition, expression, block);
    }

    private If parseIf() {
        skipCurrent(Kind.IF);
        List<Expression> conditions = new ArrayList<>();
        List<List<Statement>> blocks = new ArrayList<>();
        List<Statement> elseBlock = new ArrayList<>();
        do {
            var condition = parseExpression();
            if (condition == null) {
                throw new IllegalArgumentException("if문에 조건식이 없습니다.");
            }
            conditions.add(condition);
            skipCurrent(Kind.LeftBrace);
            blocks.add(parseBlock());
            skipCurrent(Kind.RIGHT_BRACE);
        } while (skipCurrentIf(Kind.ELIF));
        if (skipCurrentIf(Kind.ELSE)) {
            skipCurrent(Kind.LeftBrace);
            elseBlock = parseBlock();
            skipCurrent(Kind.RIGHT_BRACE);
        }
        return new If(conditions, blocks, elseBlock);
    }

    private Print parsePrint() {
        boolean lineFeed = isCurrentKind(Kind.PRINT_LINE);
        List<Expression> arguments = new ArrayList<>();
        tokens.pollFirst();
        if (!isCurrentKind(Kind.SEMICOLON)) {
            do {
                arguments.add(parseExpression());
            } while (skipCurrentIf(Kind.COMMA));
        }
        skipCurrent(Kind.SEMICOLON);
        return new Print(lineFeed, arguments);
    }

    private Return parseReturn() {
        skipCurrent(Kind.RETURN);
        var expression = parseExpression();
        if (expression == null) {
            throw new IllegalArgumentException("return문에 식이 없습니다.");
        }
        skipCurrent(Kind.SEMICOLON);
        return new Return(expression);
    }

    private Break parseBreak() {
        skipCurrent(Kind.BREAK);
        skipCurrent(Kind.SEMICOLON);

        return new Break();
    }

    private Continue parseContinue() {
        skipCurrent(Kind.CONTINUE);
        skipCurrent(Kind.SEMICOLON);

        return new Continue();
    }

    private Statement parseVariable() {
        skipCurrent(Kind.VARIABLE);
        String name = getCurrent().getValue();
        skipCurrent(Kind.IDENTIFIER, Kind.ASSIGNMENT);
        Expression expression = parseExpression();
        if (expression == null) {
            throw new IllegalArgumentException("변수 선언에 초기화식이 없습니다.");
        }
        skipCurrent(Kind.SEMICOLON);
        return new Variable(name, expression);
    }

    private Statement parseExpressionStatement() {
        Expression expression = parseExpression();
        skipCurrent(Kind.SEMICOLON);
        return new ExpressionStatement(expression);
    }

    private Expression parseExpression() {
        return parseAssignment();
    }

    private Expression parseAssignment() {
        Expression expression = parseOr();
        if (!isCurrentKind(Kind.ASSIGNMENT)) {
            return expression;
        }
        skipCurrent(Kind.ASSIGNMENT);
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
        while (skipCurrentIf(Kind.LOGICAL_OR)) {
            expression = new Or(expression, parseAnd());
        }
        return expression;
    }

    private Expression parseAnd() {
        var expression = parseRelational();
        while (skipCurrentIf(Kind.LOGICAL_AND)) {
            expression = new And(expression, parseRelational());
        }
        return expression;
    }

    private Expression parseRelational() {
        Set<Kind> operators = Set.of(Kind.EQUAL, Kind.NOT_EQUAL, Kind.LESS_THAN, Kind.GREATER_THAN, Kind.LESS_OR_EQUAL,
                Kind.GREATER_OR_EQUAL);
        var expression = parseArithmetic1();
        while (operators.contains(getCurrent().getKind())) {
            Kind kind = getCurrent().getKind();
            tokens.pollFirst();
            expression = new Relational(kind, expression, parseArithmetic1());
        }
        return expression;
    }

    private Expression parseArithmetic1() {
        Set<Kind> operators = Set.of(Kind.ADD, Kind.SUBTRACT);
        var expression = parseArithmetic2();
        while (operators.contains(getCurrent().getKind())) {
            Kind kind = getCurrent().getKind();
            tokens.pollFirst();
            expression = new Arithmetic(kind, expression, parseArithmetic2());
        }
        return expression;
    }

    private Expression parseArithmetic2() {
        Set<Kind> operators = Set.of(Kind.MULTIPLY, Kind.DIVIDE, Kind.MODULO);
        var expression = parseUnary();
        while (operators.contains(getCurrent().getKind())) {
            Kind kind = getCurrent().getKind();
            tokens.pollFirst();
            expression = new Arithmetic(kind, expression, parseUnary());
        }
        return expression;
    }

    private Expression parseUnary() {
        Set<Kind> operators = Set.of(Kind.ADD, Kind.SUBTRACT);
        if (operators.contains(getCurrent().getKind())) {
            Kind kind = getCurrent().getKind();
            tokens.pollFirst();
            Expression sub = parseUnary();
            return new Unary(kind, sub);
        }
        return parseOperand();
    }

    private Expression parseOperand() {
        if (isCurrentKind(Kind.NULL_LITERAL)) {
            return parsePostfix(parseNullLiteral());
        }
        if (isCurrentKind(Kind.TRUE_LITERAL) || isCurrentKind(Kind.FALSE_LITERAL)) {
            return parsePostfix(parseBooleanLiteral());
        }
        if (isCurrentKind(Kind.NUMBER_LITERAL)) {
            return parsePostfix(parseNumberLiteral());
        }
        if (isCurrentKind(Kind.STRING_LITERAL)) {
            return parsePostfix(parseStringLiteral());
        }
        if (isCurrentKind(Kind.LEFT_BRACKET)) {
            return parsePostfix(parseListLiteral());
        }
        if (isCurrentKind(Kind.LeftBrace)) {
            return parsePostfix(parseMapLiteral());
        }
        if (isCurrentKind(Kind.IDENTIFIER)) {
            return parsePostfix(parseIdentifier());
        }
        if (isCurrentKind(Kind.LeftParen)) {
            return parsePostfix(parseInnerExpression());
        }

        throw new IllegalArgumentException("잘못된 식입니다.");
    }

    private Expression parseNullLiteral() {
        skipCurrent(Kind.NULL_LITERAL);
        return new NullLiteral();
    }

    private Expression parseBooleanLiteral() {
        boolean value = getCurrent().isKindEquals(Kind.TRUE_LITERAL);
        tokens.pollFirst();
        return new BooleanLiteral(value);
    }

    private Expression parseNumberLiteral() {
        double value = Double.parseDouble(getCurrent().getValue());
        skipCurrent(Kind.NUMBER_LITERAL);
        return new NumberLiteral(value);
    }

    private Expression parseStringLiteral() {
        String value = getCurrent().getValue();
        skipCurrent(Kind.STRING_LITERAL);
        return new StringLiteral(value);
    }

    private Expression parseListLiteral() {
        List<Expression> expressions = new ArrayList<>();
        skipCurrent(Kind.LEFT_BRACKET);
        if (!isCurrentKind(Kind.RIGHT_BRACKET)) {
            do {
                expressions.add(parseExpression());
            } while (skipCurrentIf(Kind.COMMA));
        }
        skipCurrent(Kind.RIGHT_BRACKET);
        return new ArrayLiteral(expressions);
    }

    private Expression parseMapLiteral() {
        Map<String, Expression> map = new HashMap<>();
        skipCurrent(Kind.LeftBrace);
        if (!isCurrentKind(Kind.RIGHT_BRACE)) {
            do {
                String key = getCurrent().getValue();
                skipCurrent(Kind.STRING_LITERAL);
                skipCurrent(Kind.COLON);
                Expression expression = parseExpression();
                map.put(key, expression);
            } while (skipCurrentIf(Kind.RIGHT_BRACE));
        }
        skipCurrent(Kind.RIGHT_BRACE);
        return new MapLiteral(map);
    }

    private Expression parseIdentifier() {
        String name = getCurrent().getValue();
        skipCurrent(Kind.IDENTIFIER);
        return new GetVariable(name);
    }

    private Expression parseInnerExpression() {
        skipCurrent(Kind.LeftParen);
        Expression expression = parseExpression();
        skipCurrent(Kind.RIGHT_PAREN);
        return expression;
    }

    private Expression parsePostfix(Expression sub) {
        while (true) {
            if (isCurrentKind(Kind.LeftParen)) {
                sub = parseCall(sub);
                continue;
            }
            if (isCurrentKind(Kind.LEFT_BRACKET)) {
                sub = parseElement(sub);
                continue;
            }
            return sub;
        }
    }

    private Expression parseElement(Expression sub) {
        skipCurrent(Kind.LEFT_BRACKET);
        Expression inner = parseExpression();
        skipCurrent(Kind.RIGHT_BRACKET);
        return new GetElement(sub, inner);
    }

    private Expression parseCall(Expression sub) {
        skipCurrent(Kind.LeftParen);
        List<Expression> arguments = new ArrayList<>();
        if (!isCurrentKind(Kind.RIGHT_PAREN)) {
            do {
                arguments.add(parseExpression());
            } while (skipCurrentIf(Kind.COMMA));
        }
        skipCurrent(Kind.RIGHT_PAREN);
        return new Call(sub, arguments);
    }

    private boolean skipCurrentIf(Kind kind) {
        try {
            skipCurrent(kind);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void skipCurrent(Kind... kind) {
        Arrays.stream(kind)
                .sequential()
                .forEach(this::skipCurrent);
    }

    private void skipCurrent(Kind kind) {
        if (isCurrentKind(kind)) {
            tokens.pollFirst();
            return;
        }
        throw new IllegalArgumentException(kind + " 토큰이 필요합니다.");
    }

    private boolean isCurrentKind(Kind kind) {
        return getCurrent().isKindEquals(kind);
    }

    private Token getCurrent() {
        return tokens.peekFirst();
    }
}
