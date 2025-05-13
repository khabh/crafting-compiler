package com.craftingcompiler.token;

import com.craftingcompiler.kind.Kind;

public class Token {
    private final Kind kind;
    private final String value;

    public Token(Kind kind, String value) {
        this.kind = kind;
        this.value = value;
    }

    public Kind getKind() {
        return kind;
    }

    public String getValue() {
        return value;
    }

    public boolean isKindEquals(Kind kind) {
        return kind.equals(this.kind);
    }
}
