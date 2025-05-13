package com.craftingcompiler.token;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.kind.StringToKind;

public class OperatorAndPunctuatorScanner implements TokenScannerState {

    @Override
    public boolean canStartWith(char c) {
        return isOperatorOrPunctuator(c);
    }

    @Override
    public Token scan(Source source) {
        String token = source.consumeWhile(this::isOperatorOrPunctuator);
        while (!token.isEmpty() && StringToKind.toKind(token) == Kind.Unknown) {
            int lastIndex = token.length() - 1;
            source.unconsume(token.charAt(lastIndex));
            token = token.substring(0, lastIndex);
        }
        Kind kind = StringToKind.toKind(token);
        return new Token(kind, token);
    }

    private boolean isOperatorOrPunctuator(char c) {
        return (33 <= c && c <= 47) ||
                (58 <= c && c <= 64) ||
                (91 <= c && c <= 96) ||
                (123 <= c && c <= 126);
    }
}
