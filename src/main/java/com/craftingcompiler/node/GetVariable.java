package com.craftingcompiler.node;

import com.craftingcompiler.util.SyntaxPrinter;

public class GetVariable extends Expression {

    private String name;

    public GetVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("GET_VARIABLE: " + name);
    }
}
