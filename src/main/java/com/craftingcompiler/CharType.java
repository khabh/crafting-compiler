package com.craftingcompiler;

import java.util.function.Predicate;

public enum CharType {
    Unknown((c) -> true),
    WhiteSpace((c) -> c == ' ' || c == '\t' || c == '\r' || c == '\n'),
    NumberLiteral((c) -> '0' <= c && c <= '9' || c == '.'),
    StringLiteral((c) -> 32 <= c && c <= 126 && c != '\''),
    IdentifierAndKeyWord((c) -> '0' <= c && c <= '9' ||
            'a' <= c && c <= 'z' ||
            'A' <= c && c <= 'Z'),
    OperatorAndPunctuator((c) -> 33 <= c && c <= 47 ||
            58 <= c && c <= 64 ||
            91 <= c && c <= 96 ||
            123 <= c && c <= 126);

    private final Predicate<Character> typeMatcher;

    CharType(Predicate<Character> typeMatcher) {
        this.typeMatcher = typeMatcher;
    }

    public boolean isMatched(char input) {
        return typeMatcher.test(input);
    }
}
