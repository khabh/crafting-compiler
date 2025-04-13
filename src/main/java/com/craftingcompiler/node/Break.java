package com.craftingcompiler.node;

import com.craftingcompiler.exception.BreakException;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.Getter;

@Getter
public class Break extends Statement {

    @Override
    public void interpret() {
        throw new BreakException();
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("BREAK");
    }
}
