package com.craftingcompiler.token;

import com.craftingcompiler.Token;

public interface TokenScannerState {

    boolean canStartWith(char c);
    Token scan(Source source);
}
