package com.craftingcompiler;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.machine.Machine;
import com.craftingcompiler.node.Program;
import com.craftingcompiler.util.ObjectCodePrinter;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        String sourceCode = """
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
        Program syntaxTree = new Parser(tokens).parse();
        var objectCode = Generator.generate(syntaxTree);
        ObjectCodePrinter.printObjectCode(objectCode);
        System.out.println();

        Machine.execute(objectCode);
    }

    private static void printTokens(List<Token> tokens) {
        System.out.printf("%-12s String\n", "KIND");
        System.out.println("-".repeat(23));
        tokens.stream()
                .map(token -> String.format("%-12s %s", StringToKind.toString(token.getKind()), token.getValue()))
                .forEach(System.out::println);
    }
}
