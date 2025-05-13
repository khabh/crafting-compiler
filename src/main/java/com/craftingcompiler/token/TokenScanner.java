package com.craftingcompiler.token;

import com.craftingcompiler.Kind;
import com.craftingcompiler.Token;
import java.util.ArrayList;
import java.util.List;

public class TokenScanner {

    private static final List<TokenScannerState> states = List.of(
            new WhiteSpaceScanner(),
            new NumberLiteralScanner(),
            new StringLiteralScanner(),
            new IdentifierAndKeywordScanner(),
            new OperatorAndPunctuatorScanner()
    );

    private final Source source;

    public TokenScanner(String input) {
        this.source = new Source(input);
    }

    public List<Token> scan() {
        List<Token> tokens = new ArrayList<>();
        while (source.hasNext()) {
            char current = source.peek();
            TokenScannerState state = states.stream()
                    .filter(s -> s.canStartWith(current))
                    .findFirst()
                    .orElseThrow();
            Token token = state.scan(source);
            if (token != null) {
                tokens.add(token);
            }
        }
        tokens.add(new Token(Kind.EndOfToken, ""));
        return tokens;
    }
}
