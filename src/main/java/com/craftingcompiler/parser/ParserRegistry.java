package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.parser.statement.BreakStatementParser;
import com.craftingcompiler.parser.statement.ContinueStatementParser;
import com.craftingcompiler.parser.statement.ExpressionStatementParser;
import com.craftingcompiler.parser.statement.ForStatementParser;
import com.craftingcompiler.parser.statement.FunctionParser;
import com.craftingcompiler.parser.statement.IfStatementParser;
import com.craftingcompiler.parser.statement.PrintStatementParser;
import com.craftingcompiler.parser.statement.ReturnStatementParser;
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
        PARSERS.put(Kind.FOR, new ForStatementParser());
        PARSERS.put(Kind.IF, new IfStatementParser());
        StatementParser printStatementParser = new PrintStatementParser();
        PARSERS.put(Kind.PRINT, printStatementParser);
        PARSERS.put(Kind.PRINT_LINE, printStatementParser);
        PARSERS.put(Kind.RETURN, new ReturnStatementParser());
        PARSERS.put(Kind.BREAK, new BreakStatementParser());
        PARSERS.put(Kind.CONTINUE, new ContinueStatementParser());
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
