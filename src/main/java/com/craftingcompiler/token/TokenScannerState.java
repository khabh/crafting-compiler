package com.craftingcompiler.token;

public interface TokenScannerState {

    boolean canStartWith(char c);
    Token scan(Source source);
}
