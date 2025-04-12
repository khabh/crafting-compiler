package com.craftingcompiler;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        String sourceCode = """
                function main() {
                    printLine 'Hello, World!';
                    printLine 1 + 2 * 3;
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
