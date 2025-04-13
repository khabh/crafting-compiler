package com.craftingcompiler.node;

import com.craftingcompiler.exception.ContinueException;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.Getter;

@Getter
public class Continue extends Statement {

    @Override
    public void interpret() {
        throw new ContinueException();
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("CONTINUE");
    }
}
