package com.craftingcompiler.node;

import static com.craftingcompiler.interpreter.Interpreter.local;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Variable extends Statement {

    private final String name;
    private final Expression expression;

    @Override
    public void generate() {
        Generator.setLocal(name);
        expression.generate();
        Generator.writeCode(Instruction.SET_LOCAL, Generator.getLocal(name));
        Generator.writeCode(Instruction.POP_OPERAND);
    }

    @Override
    public void interpret() {
        local.getLast().getFirst().put(name, interpretExpression());
    }

    private Object interpretExpression() {
        Object result = expression.interpret();
        if (result instanceof Potato) {
            return result;
        }
        return new Potato(result);
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("VAR " + name + ":");
        expression.print(depth + 1);
    }
}
