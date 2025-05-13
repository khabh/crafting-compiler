package com.craftingcompiler.kind;

import java.util.HashMap;
import java.util.Map;

public class StringToKind {
    static final Map<String, Kind> stringToKind = new HashMap<>();

    static {
        stringToKind.put("#unknown", Kind.Unknown);
        stringToKind.put("#EndOfToken", Kind.EndOfToken);

        stringToKind.put("null", Kind.NullLiteral);
        stringToKind.put("true", Kind.TrueLiteral);
        stringToKind.put("false", Kind.FalseLiteral);
        stringToKind.put("#Number", Kind.NumberLiteral);
        stringToKind.put("#String", Kind.StringLiteral);
        stringToKind.put("#identifier", Kind.Identifier);

        stringToKind.put("function", Kind.Function);
        stringToKind.put("return", Kind.Return);
        stringToKind.put("var", Kind.Variable);
        stringToKind.put("for", Kind.For);
        stringToKind.put("break", Kind.Break);
        stringToKind.put("continue", Kind.Continue);
        stringToKind.put("if", Kind.If);
        stringToKind.put("elif", Kind.Elif);
        stringToKind.put("else", Kind.Else);
        stringToKind.put("print", Kind.Print);
        stringToKind.put("printLine", Kind.PrintLine);

        stringToKind.put("and", Kind.LogicalAnd);
        stringToKind.put("or", Kind.LogicalOr);

        stringToKind.put("=", Kind.Assignment);

        stringToKind.put("+", Kind.Add);
        stringToKind.put("-", Kind.Subtract);
        stringToKind.put("*", Kind.Multiply);
        stringToKind.put("/", Kind.Divide);
        stringToKind.put("%", Kind.Modulo);

        stringToKind.put("==", Kind.Equal);
        stringToKind.put("!=", Kind.NotEqual);
        stringToKind.put("<", Kind.LessThan);
        stringToKind.put(">", Kind.GreaterThan);
        stringToKind.put("<=", Kind.LessOrEqual);
        stringToKind.put(">=", Kind.GreaterOrEqual);

        stringToKind.put(",", Kind.Comma);
        stringToKind.put(":", Kind.Colon);
        stringToKind.put(";", Kind.Semicolon);
        stringToKind.put("(", Kind.LeftParen);
        stringToKind.put(")", Kind.RightParen);
        stringToKind.put("{", Kind.LeftBrace);
        stringToKind.put("}", Kind.RightBrace);
        stringToKind.put("[", Kind.LeftBraket);
        stringToKind.put("]", Kind.RightBraket);

        stringToKind.put("recipe", Kind.Function);
        stringToKind.put("serve", Kind.Return);
        stringToKind.put("plant", Kind.Variable);
        stringToKind.put("plate", Kind.Return);
    }

    public static Kind toKind(String token) {
        if (!stringToKind.containsKey(token)) {
            return Kind.Unknown;
        }
        return stringToKind.get(token);
    }

    public static String toString(Kind kind) {
        for (Map.Entry<String, Kind> entry : StringToKind.stringToKind.entrySet()) {
            if (entry.getValue() == kind) {
                return entry.getKey();
            }
        }
        return "#unknown";
    }
}
