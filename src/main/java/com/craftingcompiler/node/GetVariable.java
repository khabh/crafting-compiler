package com.craftingcompiler.node;

import static com.craftingcompiler.interpreter.BuiltinFunctionTable.builtinFunctionTable;
import static com.craftingcompiler.interpreter.Interpreter.functionTable;
import static com.craftingcompiler.interpreter.Interpreter.global;
import static com.craftingcompiler.interpreter.Interpreter.local;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.util.SyntaxPrinter;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetVariable extends Expression {

    private String name;

    @Override
    public void generate() {
        int local = Generator.getLocal(name);
        if (local == -1) {
            Generator.writeCode(Instruction.GET_GLOBAL, name);
            return;
        }
        Generator.writeCode(Instruction.GET_LOCAL, local);
    }

    @Override
    public Object interpret() {
        for (Map<String, Object> variables : local.getLast()) {
            if (variables.containsKey(name)) {
                return variables.get(name);
            }
        }
        if (global.containsKey(name)) {
            return global.get(name);
        }
        if (functionTable.containsKey(name)) {
            return functionTable.get(name);
        }
        if (builtinFunctionTable.containsKey(name)) {
            return builtinFunctionTable.get(name);
        }
        return null;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("GET_VARIABLE: " + name);
    }
}
