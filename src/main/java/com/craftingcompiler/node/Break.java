package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class Break extends Statement {
    
    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("BREAK");
    }
}
