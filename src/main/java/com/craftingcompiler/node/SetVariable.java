package com.craftingcompiler.node;

import static com.craftingcompiler.interpreter.Interpreter.global;
import static com.craftingcompiler.interpreter.Interpreter.local;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SetVariable extends Expression {

    private String name;
    private Expression value;

    @Override
    public void generate() {
        value.generate();
        var local = Generator.getLocal(name);
        if (local == -1) {
            throw new IllegalArgumentException("Variable " + name + " not found");
        }
        Generator.writeCode(Instruction.SET_LOCAL, local);
    }

    @Override
    public Object interpret() {
        for (var variables : local.getLast()) {
            if (variables.containsKey(name)) {
                return variables.put(name, value.interpret());
            }
        }
        if (global.containsKey(name)) {
            return global.put(name, value.interpret());
        }
        throw new IllegalArgumentException("선언되지 않은 변수입니다: " + name);
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("SET_VARIABLE: " + name);
        value.print(depth + 1);
    }
}
