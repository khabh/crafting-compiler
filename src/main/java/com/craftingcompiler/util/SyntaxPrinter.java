package com.craftingcompiler.util;

import com.craftingcompiler.node.Function;
import com.craftingcompiler.node.Program;

public class SyntaxPrinter {
    public static void indent(int depth) {
        System.out.print(" ".repeat(depth * 2));
    }

    public static void printSyntaxTree(Program program) {
        for (Function function : program.getFunctions()) {
            function.print(0);
        }
    }
}
