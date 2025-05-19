package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.parser.expression.ExpressionParser;
import com.craftingcompiler.parser.statement.BreakParser;
import com.craftingcompiler.parser.statement.ContinueParser;
import com.craftingcompiler.parser.statement.ExpressionStatementParser;
import com.craftingcompiler.parser.statement.ForParser;
import com.craftingcompiler.parser.statement.FunctionParser;
import com.craftingcompiler.parser.statement.IfParser;
import com.craftingcompiler.parser.statement.PrintParser;
import com.craftingcompiler.parser.statement.ReturnParser;
import com.craftingcompiler.parser.statement.StatementParser;
import com.craftingcompiler.parser.statement.VariableDeclarationParser;
import com.craftingcompiler.token.Token;
import java.util.HashMap;
import java.util.Map;

public class ParserRegistry {

    private static final ExpressionParser EXPRESSION_PARSER = new ExpressionParser();
    private static final StatementParser DEFAULT_PARSER = new ExpressionStatementParser();
    private static final Map<Kind, StatementParser> PARSERS = new HashMap<>();

    static {
        PARSERS.put(Kind.FUNCTION, new FunctionParser());
        PARSERS.put(Kind.VARIABLE, new VariableDeclarationParser());
        PARSERS.put(Kind.FOR, new ForParser());
        PARSERS.put(Kind.IF, new IfParser());
        StatementParser printStatementParser = new PrintParser();
        PARSERS.put(Kind.PRINT, printStatementParser);
        PARSERS.put(Kind.PRINT_LINE, printStatementParser);
        PARSERS.put(Kind.RETURN, new ReturnParser());
        PARSERS.put(Kind.BREAK, new BreakParser());
        PARSERS.put(Kind.CONTINUE, new ContinueParser());
    }

    public static StatementParser getParser(Token token) {
        return getParser(token.getKind());
    }

    public static StatementParser getParser(Kind kind) {
        return PARSERS.getOrDefault(kind, DEFAULT_PARSER);
    }

    public static ExpressionParser getExpressionParser() {
        return EXPRESSION_PARSER;
    }
}
