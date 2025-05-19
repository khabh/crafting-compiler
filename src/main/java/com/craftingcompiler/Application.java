package com.craftingcompiler;

import com.craftingcompiler.kind.StringToKind;
import com.craftingcompiler.node.Program;
import com.craftingcompiler.parser.Parser;
import com.craftingcompiler.token.Token;
import com.craftingcompiler.token.TokenScanner;
import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        String sourceCode = """
                recipe main() {
                    plant x = 10;
                    printLine add(x, 20);
                }
                
                recipe add(a, b) {
                    serve a + b;
                }
                """;
        List<Token> tokens = new TokenScanner(sourceCode).scan();
        printTokens(tokens);
        Program syntaxTree = new Parser(tokens).parse();
        SyntaxPrinter.printSyntaxTree(syntaxTree);

//        var objectCode = Generator.generate(syntaxTree);
//        ObjectCodePrinter.printObjectCode(objectCode);
//        System.out.println();
//
//        Machine.execute(objectCode);
    }

    private static void printTokens(List<Token> tokens) {
        System.out.printf("%-12s String\n", "KIND");
        System.out.println("-".repeat(23));
        tokens.stream()
                .map(token -> String.format("%-12s %s", StringToKind.toString(token.getKind()), token.getValue()))
                .forEach(System.out::println);
    }
}
