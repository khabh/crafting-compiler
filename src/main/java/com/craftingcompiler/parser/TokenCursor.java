package com.craftingcompiler.parser;

import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.token.Token;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TokenCursor {

    private final LinkedList<Token> tokens;

    public TokenCursor(List<Token> tokens) {
        this.tokens = new LinkedList<>(tokens);
    }

    public boolean tryConsume(Kind kind) {
        try {
            consume(kind);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void consume(Kind... kind) {
        Arrays.stream(kind)
                .sequential()
                .forEach(this::consume);
    }

    public void consume(Kind kind) {
        if (is(kind)) {
            tokens.pollFirst();
            return;
        }
        throw new IllegalArgumentException(kind + " 토큰이 필요합니다.");
    }

    public boolean is(Kind kind) {
        return current().isKindEquals(kind);
    }

    public Token current() {
        return tokens.peekFirst();
    }

    public void next() {
        tokens.pollFirst();
    }
}
