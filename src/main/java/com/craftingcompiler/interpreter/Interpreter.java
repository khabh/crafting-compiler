package com.craftingcompiler.interpreter;

import static java.util.stream.Collectors.toMap;

import com.craftingcompiler.exception.ReturnException;
import com.craftingcompiler.node.Function;
import com.craftingcompiler.node.Program;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Interpreter {

    public static Map<String, Object> global;
    public static LinkedList<LinkedList<Map<String, Object>>> local;
    public static Map<String, Function> functionTable;

    public void interpret(Program program) {
        global = new HashMap<>();
        local = new LinkedList<>();
        functionTable = program.getFunctions().stream()
                .collect(toMap(Function::getName, function -> function));
        if (!functionTable.containsKey("main")) {
            throw new IllegalArgumentException("main function not found");
        }

        LinkedList<Map<String, Object>> blockStack = new LinkedList<>();
        blockStack.addFirst(new HashMap<>());
        local.addLast(blockStack);
        try {
            functionTable.get("main").interpret();
        } catch (ReturnException e) {
            local.removeLast();
        }
    }
}
