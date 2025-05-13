package com.craftingcompiler.token;

import com.craftingcompiler.Kind;
import com.craftingcompiler.Token;

public class StringLiteralScanner implements TokenScannerState {

    @Override
    public boolean canStartWith(char c) {
        return c == '\'';
    }

    @Override
    public Token scan(Source source) {
        source.consume();
        String token = source.consumeWhile(c -> 32 <= c && c <= 126 && c != '\'');
        if (source.nextIs('\'')) {
            source.consume();
            return new Token(Kind.StringLiteral, token);
        }
        throw new IllegalArgumentException("문자열의 종료 문자가 없습니다.");
    }
}
