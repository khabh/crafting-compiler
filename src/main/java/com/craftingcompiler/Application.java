package com.craftingcompiler;

import com.craftingcompiler.interpreter.Interpreter;
import com.craftingcompiler.node.Program;
import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        String sourceCode = """
                function main() {
                    var list = [1, 2, 3];
                    printLine list[1];
                    list[2] = 4;
                    printLine list[2];
                    printLine sqrt(9);
                    printLine mul('Hello ', 3);
                    sayHello(3);
                    for i = 0, i < 5, i = i + 1 {
                        if i == 1 {
                            printLine 'one';
                            continue;
                        } elif i == 2 {
                            printLine 'two';
                            break;
                        } elif i == 3 {
                            printLine 'three';
                        } else {
                            printLine i;
                        }
                        printLine i;
                    }
                
                    var global = 10;
                    global = 4;
                    var local = 13;
                    printLine 'global: ', global;
                    printLine 'local: ', local;
                    printLine 'Hello' + ' World!';
                    printLine 1 + 2 + 3 + 4;
                    printLine 'potato ' * 10;
                    printLine true and true;
                    printLine false or true;
                    printLine false or 'TRUE!!';
                }
                
                function sayHello(j) {
                    for i = 0, i < j, i = i + 1 {
                        printLine 'Ho';
                    }
                }
                
                function mul(a, b) {
                    printLine a, b;
                    return a * b;
                }
                """;
        List<Token> tokens = new TokenScanner(sourceCode).scan();
        printTokens(tokens);
        Program syntaxTree = new Parser(tokens).parse();
        SyntaxPrinter.printSyntaxTree(syntaxTree);

        new Interpreter().interpret(syntaxTree);
    }

    private static void printTokens(List<Token> tokens) {
        System.out.printf("%-12s String\n", "KIND");
        System.out.println("-".repeat(23));
        tokens.stream()
                .map(token -> String.format("%-12s %s", StringToKind.toString(token.getKind()), token.getValue()))
                .forEach(System.out::println);
    }
}
