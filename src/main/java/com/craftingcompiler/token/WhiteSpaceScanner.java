package com.craftingcompiler.token;

import com.craftingcompiler.Token;

public class WhiteSpaceScanner implements TokenScannerState {
    @Override
    public boolean canStartWith(char c) {
        return isWhiteSpace(c);
    }

    @Override
    public Token scan(Source source) {
        source.consumeWhile(this::isWhiteSpace);
        return null;
    }

    private boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }
}
