package com.craftingcompiler;

import com.craftingcompiler.kind.StringToKind;
import com.craftingcompiler.token.Token;
import com.craftingcompiler.token.TokenScanner;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        String sourceCode = """
                var x = 10;
                function main() {
                    for i = 0, i < 10, i = i + 1 {
                        if (i == 1) {
                            continue;
                        } else {
                            if (i % 2 == 0) {
                                printLine i;
                                continue;
                            }
                        }
                        printLine 'odd';
                        if (i == 7) {
                            break;
                        }
                    }
                }
                """;
        List<Token> tokens = new TokenScanner(sourceCode).scan();
        printTokens(tokens);
    }

    private static void printTokens(List<Token> tokens) {
        System.out.printf("%-12s String\n", "KIND");
        System.out.println("-".repeat(23));
        tokens.stream()
                .map(token -> String.format("%-12s %s", StringToKind.toString(token.getKind()), token.getValue()))
                .forEach(System.out::println);
    }
}
