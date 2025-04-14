package com.craftingcompiler.code;

import com.craftingcompiler.node.Function;
import com.craftingcompiler.node.Program;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Generator {

    public static List<Code> codes;
    public static Map<String, Integer> generatorFunctions;
    public static Deque<Map<String, Integer>> symbolStack;
    public static List<Integer> offsetStack;
    public static Integer localSize;
    public static List<List<Integer>> continueStack;
    public static List<List<Integer>> breakStack;

    public static Map.Entry<List<Code>, Map<String, Integer>> generate(Program program) {
        codes = new ArrayList<>();
        generatorFunctions = new HashMap<>();
        symbolStack = new LinkedList<>();
        offsetStack = new ArrayList<>();
        continueStack = new ArrayList<>();
        breakStack = new ArrayList<>();

        writeCode(Instruction.GET_GLOBAL, "main");
        writeCode(Instruction.CALL, 0);
        writeCode(Instruction.EXIT);

        program.getFunctions()
                .forEach(Function::generate);

        return Map.entry(codes, generatorFunctions);
    }

    public static int writeCode(Instruction instruction) {
        codes.add(new Code(instruction, null));
        return codes.size() - 1;
    }

    public static int writeCode(Instruction instruction, Object operand) {
        codes.add(new Code(instruction, operand));
        return codes.size() - 1;
    }

    public static void patchAddress(int codeIndex) {
        codes.get(codeIndex).setOperand(codes.size());
    }

    public static void patchOperand(int codeIndex, int operand) {
        codes.get(codeIndex).setOperand(operand);
    }

    public static void initBlock() {
        localSize = 0;
        offsetStack.add(0);
        symbolStack.addFirst(new HashMap<>());
    }

    public static void pushBlock() {
        symbolStack.addFirst(new HashMap<>());
        offsetStack.add(offsetStack.get(offsetStack.size() - 1));
    }

    public static void popBlock() {
        offsetStack.remove(offsetStack.size() - 1);
        symbolStack.removeFirst();
    }

    public static void setLocal(String name) {
        int offset = offsetStack.get(offsetStack.size() - 1);
        symbolStack.peekFirst().put(name, offset);
        offsetStack.set(offsetStack.size() - 1, ++offset);

        localSize = Math.max(localSize, offset);
    }

    public static int getLocal(String name) {
        for (Map<String, Integer> symbolTable : symbolStack) {
            if (symbolTable.containsKey(name)) {
                return symbolTable.get(name);
            }
        }
        return -1;
    }
}
