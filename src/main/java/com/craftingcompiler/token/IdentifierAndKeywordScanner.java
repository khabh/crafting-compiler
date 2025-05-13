package com.craftingcompiler.token;

import com.craftingcompiler.Kind;
import com.craftingcompiler.StringToKind;
import com.craftingcompiler.Token;

public class IdentifierAndKeywordScanner implements TokenScannerState {

    @Override
    public boolean canStartWith(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z';
    }

    @Override
    public Token scan(Source source) {
        String token = source.consumeWhile(c -> '0' <= c && c <= '9' || 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z');
        Kind kind = StringToKind.toKind(token);
        if (kind == Kind.Unknown) {
            return new Token(Kind.Identifier, token);
        }
        return new Token(kind, token);
    }
}
