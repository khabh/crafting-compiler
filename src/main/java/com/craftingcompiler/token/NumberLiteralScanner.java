package com.craftingcompiler.token;

import com.craftingcompiler.kind.Kind;

public class NumberLiteralScanner implements TokenScannerState {

    @Override
    public boolean canStartWith(char c) {
        return '0' <= c && c <= '9';
    }

    @Override
    public Token scan(Source source) {
        String token = source.consumeWhile(c -> '0' <= c && c <= '9' || c == '.');
        return new Token(Kind.NUMBER_LITERAL, token);
    }
}
