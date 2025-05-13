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
        while (getCurrent().getKind() != Kind.EndOfToken) {
            Token token = getCurrent();
            if (token.isKindEquals(Kind.Function)) {
                functions.add(parseFunction());
                continue;
            }
            throw new IllegalArgumentException(token.getValue() + " 잘못된 구문입니다.");
        }
        return new Program(functions);
    }

    private Function parseFunction() {
        skipCurrent(Kind.Function);
        String functionName = getCurrent().getValue();
        skipCurrent(Kind.Identifier, Kind.LeftParen);
        List<String> parameters = parseParameters();
        skipCurrent(Kind.RightParen, Kind.LeftBrace);
        List<Statement> block = parseBlock();
        skipCurrent(Kind.RightBrace);

        return new Function(functionName, parameters, block);
    }

    private List<String> parseParameters() {
        List<String> parameters = new ArrayList<>();
        if (!isCurrentKind(Kind.RightParen)) {
            do {
                parameters.add(getCurrent().getValue());
                skipCurrent(Kind.Identifier);
            } while (skipCurrentIf(Kind.Comma));
        }

        return parameters;
    }

    private List<Statement> parseBlock() {
        List<Statement> statements = new ArrayList<>();
        while (!isCurrentKind(Kind.RightBrace)) {
            if (isCurrentKind(Kind.EndOfToken)) {
                throw new IllegalArgumentException(getCurrent().getValue() + " 잘못된 구문입니다.");
            }
            if (isCurrentKind(Kind.Variable)) {
                statements.add(parseVariable());
                continue;
            }
            if (isCurrentKind(Kind.For)) {
                statements.add(parseFor());
                continue;
            }
            if (isCurrentKind(Kind.If)) {
                statements.add(parseIf());
                continue;
            }
            if (isCurrentKind(Kind.Print) || isCurrentKind(Kind.PrintLine)) {
                statements.add(parsePrint());
                continue;
            }
            if (isCurrentKind(Kind.Return)) {
                statements.add(parseReturn());
                continue;
            }
            if (isCurrentKind(Kind.Break)) {
                statements.add(parseBreak());
                continue;
            }
            if (isCurrentKind(Kind.Continue)) {
                statements.add(parseContinue());
                continue;
            }

            statements.add(parseExpressionStatement());
        }
        return statements;
    }

    private For parseFor() {
        skipCurrent(Kind.For);

        String variableName = getCurrent().getValue();
        skipCurrent(Kind.Identifier);
        skipCurrent(Kind.Assignment);
        Expression variableExpression = parseExpression();

        if (variableExpression == null) {
            throw new IllegalArgumentException(getCurrent().getValue() + "for문에 초기화식이 없습니다.");
        }

        Variable variable = new Variable(variableName, variableExpression);
        skipCurrent(Kind.Comma);
        Expression condition = parseExpression();
        if (condition == null) {
            throw new IllegalArgumentException(getCurrent().getValue() + "for문에 조건식이 없습니다.");
        }

        skipCurrent(Kind.Comma);
        Expression expression = parseExpression();
        if (expression == null) {
            throw new IllegalArgumentException(getCurrent().getValue() + "for문에 증감식이 없습니다.");
        }

        skipCurrent(Kind.LeftBrace);
        List<Statement> block = parseBlock();
        skipCurrent(Kind.RightBrace);

        return new For(variable, condition, expression, block);
    }

    private If parseIf() {
        skipCurrent(Kind.If);
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
            skipCurrent(Kind.RightBrace);
        } while (skipCurrentIf(Kind.Elif));
        if (skipCurrentIf(Kind.Else)) {
            skipCurrent(Kind.LeftBrace);
            elseBlock = parseBlock();
            skipCurrent(Kind.RightBrace);
        }
        return new If(conditions, blocks, elseBlock);
    }

    private Print parsePrint() {
        boolean lineFeed = isCurrentKind(Kind.PrintLine);
        List<Expression> arguments = new ArrayList<>();
        tokens.pollFirst();
        if (!isCurrentKind(Kind.Semicolon)) {
            do {
                arguments.add(parseExpression());
            } while (skipCurrentIf(Kind.Comma));
        }
        skipCurrent(Kind.Semicolon);
        return new Print(lineFeed, arguments);
    }

    private Return parseReturn() {
        skipCurrent(Kind.Return);
        var expression = parseExpression();
        if (expression == null) {
            throw new IllegalArgumentException("return문에 식이 없습니다.");
        }
        skipCurrent(Kind.Semicolon);
        return new Return(expression);
    }

    private Break parseBreak() {
        skipCurrent(Kind.Break);
        skipCurrent(Kind.Semicolon);

        return new Break();
    }

    private Continue parseContinue() {
        skipCurrent(Kind.Continue);
        skipCurrent(Kind.Semicolon);

        return new Continue();
    }

    private Statement parseVariable() {
        skipCurrent(Kind.Variable);
        String name = getCurrent().getValue();
        skipCurrent(Kind.Identifier, Kind.Assignment);
        Expression expression = parseExpression();
        if (expression == null) {
            throw new IllegalArgumentException("변수 선언에 초기화식이 없습니다.");
        }
        skipCurrent(Kind.Semicolon);
        return new Variable(name, expression);
    }

    private Statement parseExpressionStatement() {
        Expression expression = parseExpression();
        skipCurrent(Kind.Semicolon);
        return new ExpressionStatement(expression);
    }

    private Expression parseExpression() {
        return parseAssignment();
    }

    private Expression parseAssignment() {
        Expression expression = parseOr();
        if (!isCurrentKind(Kind.Assignment)) {
            return expression;
        }
        skipCurrent(Kind.Assignment);
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
        while (skipCurrentIf(Kind.LogicalOr)) {
            expression = new Or(expression, parseAnd());
        }
        return expression;
    }

    private Expression parseAnd() {
        var expression = parseRelational();
        while (skipCurrentIf(Kind.LogicalAnd)) {
            expression = new And(expression, parseRelational());
        }
        return expression;
    }

    private Expression parseRelational() {
        Set<Kind> operators = Set.of(Kind.Equal, Kind.NotEqual, Kind.LessThan, Kind.GreaterThan, Kind.LessOrEqual,
                Kind.GreaterOrEqual);
        var expression = parseArithmetic1();
        while (operators.contains(getCurrent().getKind())) {
            Kind kind = getCurrent().getKind();
            tokens.pollFirst();
            expression = new Relational(kind, expression, parseArithmetic1());
        }
        return expression;
    }

    private Expression parseArithmetic1() {
        Set<Kind> operators = Set.of(Kind.Add, Kind.Subtract);
        var expression = parseArithmetic2();
        while (operators.contains(getCurrent().getKind())) {
            Kind kind = getCurrent().getKind();
            tokens.pollFirst();
            expression = new Arithmetic(kind, expression, parseArithmetic2());
        }
        return expression;
    }

    private Expression parseArithmetic2() {
        Set<Kind> operators = Set.of(Kind.Multiply, Kind.Divide, Kind.Modulo);
        var expression = parseUnary();
        while (operators.contains(getCurrent().getKind())) {
            Kind kind = getCurrent().getKind();
            tokens.pollFirst();
            expression = new Arithmetic(kind, expression, parseUnary());
        }
        return expression;
    }

    private Expression parseUnary() {
        Set<Kind> operators = Set.of(Kind.Add, Kind.Subtract);
        if (operators.contains(getCurrent().getKind())) {
            Kind kind = getCurrent().getKind();
            tokens.pollFirst();
            Expression sub = parseUnary();
            return new Unary(kind, sub);
        }
        return parseOperand();
    }

    private Expression parseOperand() {
        if (isCurrentKind(Kind.NullLiteral)) {
            return parsePostfix(parseNullLiteral());
        }
        if (isCurrentKind(Kind.TrueLiteral) || isCurrentKind(Kind.FalseLiteral)) {
            return parsePostfix(parseBooleanLiteral());
        }
        if (isCurrentKind(Kind.NumberLiteral)) {
            return parsePostfix(parseNumberLiteral());
        }
        if (isCurrentKind(Kind.StringLiteral)) {
            return parsePostfix(parseStringLiteral());
        }
        if (isCurrentKind(Kind.LeftBraket)) {
            return parsePostfix(parseListLiteral());
        }
        if (isCurrentKind(Kind.LeftBrace)) {
            return parsePostfix(parseMapLiteral());
        }
        if (isCurrentKind(Kind.Identifier)) {
            return parsePostfix(parseIdentifier());
        }
        if (isCurrentKind(Kind.LeftParen)) {
            return parsePostfix(parseInnerExpression());
        }

        throw new IllegalArgumentException("잘못된 식입니다.");
    }

    private Expression parseNullLiteral() {
        skipCurrent(Kind.NullLiteral);
        return new NullLiteral();
    }

    private Expression parseBooleanLiteral() {
        boolean value = getCurrent().isKindEquals(Kind.TrueLiteral);
        tokens.pollFirst();
        return new BooleanLiteral(value);
    }

    private Expression parseNumberLiteral() {
        double value = Double.parseDouble(getCurrent().getValue());
        skipCurrent(Kind.NumberLiteral);
        return new NumberLiteral(value);
    }

    private Expression parseStringLiteral() {
        String value = getCurrent().getValue();
        skipCurrent(Kind.StringLiteral);
        return new StringLiteral(value);
    }

    private Expression parseListLiteral() {
        List<Expression> expressions = new ArrayList<>();
        skipCurrent(Kind.LeftBraket);
        if (!isCurrentKind(Kind.RightBraket)) {
            do {
                expressions.add(parseExpression());
            } while (skipCurrentIf(Kind.Comma));
        }
        skipCurrent(Kind.RightBraket);
        return new ArrayLiteral(expressions);
    }

    private Expression parseMapLiteral() {
        Map<String, Expression> map = new HashMap<>();
        skipCurrent(Kind.LeftBrace);
        if (!isCurrentKind(Kind.RightBrace)) {
            do {
                String key = getCurrent().getValue();
                skipCurrent(Kind.StringLiteral);
                skipCurrent(Kind.Colon);
                Expression expression = parseExpression();
                map.put(key, expression);
            } while (skipCurrentIf(Kind.RightBrace));
        }
        skipCurrent(Kind.RightBrace);
        return new MapLiteral(map);
    }

    private Expression parseIdentifier() {
        String name = getCurrent().getValue();
        skipCurrent(Kind.Identifier);
        return new GetVariable(name);
    }

    private Expression parseInnerExpression() {
        skipCurrent(Kind.LeftParen);
        Expression expression = parseExpression();
        skipCurrent(Kind.RightParen);
        return expression;
    }

    private Expression parsePostfix(Expression sub) {
        while (true) {
            if (isCurrentKind(Kind.LeftParen)) {
                sub = parseCall(sub);
                continue;
            }
            if (isCurrentKind(Kind.LeftBraket)) {
                sub = parseElement(sub);
                continue;
            }
            return sub;
        }
    }

    private Expression parseElement(Expression sub) {
        skipCurrent(Kind.LeftBraket);
        Expression inner = parseExpression();
        skipCurrent(Kind.RightBraket);
        return new GetElement(sub, inner);
    }

    private Expression parseCall(Expression sub) {
        skipCurrent(Kind.LeftParen);
        List<Expression> arguments = new ArrayList<>();
        if (!isCurrentKind(Kind.RightParen)) {
            do {
                arguments.add(parseExpression());
            } while (skipCurrentIf(Kind.Comma));
        }
        skipCurrent(Kind.RightParen);
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
