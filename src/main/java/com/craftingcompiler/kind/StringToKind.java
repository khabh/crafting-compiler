package com.craftingcompiler.kind;

import java.util.HashMap;
import java.util.Map;

public class StringToKind {
    static final Map<String, Kind> stringToKind = new HashMap<>();

    static {
        stringToKind.put("#unknown", Kind.UNKNOWN);
        stringToKind.put("#EndOfToken", Kind.END_OF_TOKEN);

        stringToKind.put("null", Kind.NULL_LITERAL);
        stringToKind.put("true", Kind.TRUE_LITERAL);
        stringToKind.put("false", Kind.FALSE_LITERAL);
        stringToKind.put("#Number", Kind.NUMBER_LITERAL);
        stringToKind.put("#String", Kind.STRING_LITERAL);
        stringToKind.put("#identifier", Kind.IDENTIFIER);

        stringToKind.put("return", Kind.RETURN);
        stringToKind.put("var", Kind.VARIABLE);
        stringToKind.put("for", Kind.FOR);
        stringToKind.put("break", Kind.BREAK);
        stringToKind.put("continue", Kind.CONTINUE);
        stringToKind.put("if", Kind.IF);
        stringToKind.put("elif", Kind.ELIF);
        stringToKind.put("else", Kind.ELSE);
        stringToKind.put("print", Kind.PRINT);
        stringToKind.put("printLine", Kind.PRINT_LINE);

        stringToKind.put("and", Kind.LOGICAL_AND);
        stringToKind.put("or", Kind.LOGICAL_OR);

        stringToKind.put("=", Kind.ASSIGNMENT);

        stringToKind.put("+", Kind.ADD);
        stringToKind.put("-", Kind.SUBTRACT);
        stringToKind.put("*", Kind.MULTIPLY);
        stringToKind.put("/", Kind.DIVIDE);
        stringToKind.put("%", Kind.MODULO);

        stringToKind.put("==", Kind.EQUAL);
        stringToKind.put("!=", Kind.NOT_EQUAL);
        stringToKind.put("<", Kind.LESS_THAN);
        stringToKind.put(">", Kind.GREATER_THAN);
        stringToKind.put("<=", Kind.LESS_OR_EQUAL);
        stringToKind.put(">=", Kind.GREATER_OR_EQUAL);

        stringToKind.put(",", Kind.COMMA);
        stringToKind.put(":", Kind.COLON);
        stringToKind.put(";", Kind.SEMICOLON);
        stringToKind.put("(", Kind.LEFT_PAREN);
        stringToKind.put(")", Kind.RIGHT_PAREN);
        stringToKind.put("{", Kind.LEFT_BRACE);
        stringToKind.put("}", Kind.RIGHT_BRACE);
        stringToKind.put("[", Kind.LEFT_BRACKET);
        stringToKind.put("]", Kind.RIGHT_BRACKET);

        stringToKind.put("recipe", Kind.FUNCTION);
        stringToKind.put("serve", Kind.RETURN);
        stringToKind.put("plant", Kind.VARIABLE);
        stringToKind.put("plate", Kind.RETURN);
    }

    public static Kind toKind(String token) {
        if (!stringToKind.containsKey(token)) {
            return Kind.UNKNOWN;
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
