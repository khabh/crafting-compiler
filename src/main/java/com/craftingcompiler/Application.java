package com.craftingcompiler;

import com.craftingcompiler.interpreter.Interpreter;
import com.craftingcompiler.kind.StringToKind;
import com.craftingcompiler.node.Program;
import com.craftingcompiler.parser.Parser;
import com.craftingcompiler.token.Token;
import com.craftingcompiler.token.TokenScanner;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        String sourceCode = """
                recipe main() {
                    plant x = 10;
                    printLine x.salted;
                    salt x;
                    printLine x.salted;
                    wash x;
                    printLine x.salted;
                    plant y = [1, 2, 3];
                    salt y[1];
                    printLine 'y[1].salted = ', y[1].salted;
                }

                recipe add(a, b) {
                    serve a + b;
                }
                """;
        List<Token> tokens = new TokenScanner(sourceCode).scan();
        Program syntaxTree = new Parser(tokens).parse();
        new Interpreter().interpret(syntaxTree);
//        printTokens(tokens);
//        SyntaxPrinter.printSyntaxTree(syntaxTree);
    }

    private static void printTokens(List<Token> tokens) {
        System.out.printf("%-12s String\n", "KIND");
        System.out.println("-".repeat(23));
        tokens.stream()
                .map(token -> String.format("%-12s %s", StringToKind.toString(token.getKind()), token.getValue()))
                .forEach(System.out::println);
    }
}
