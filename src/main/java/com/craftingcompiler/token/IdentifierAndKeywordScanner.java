package com.craftingcompiler.token;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.kind.StringToKind;

public class IdentifierAndKeywordScanner implements TokenScannerState {

    @Override
    public boolean canStartWith(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z';
    }

    @Override
    public Token scan(Source source) {
        String token = source.consumeWhile(c -> '0' <= c && c <= '9' || 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z');
        Kind kind = StringToKind.toKind(token);
        if (kind == Kind.UNKNOWN) {
            return new Token(Kind.IDENTIFIER, token);
        }
        return new Token(kind, token);
    }
}
