package com.craftingcompiler.node;

import java.util.List;

public class Program {

    private final List<Function> functions;

    public Program(List<Function> functions) {
        this.functions = functions;
    }

    public List<Function> getFunctions() {
        return functions;
    }
}
