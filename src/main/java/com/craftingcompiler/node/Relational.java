package com.craftingcompiler.node;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.util.SyntaxPrinter;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Relational extends Expression {

    static Map<Kind, Instruction> instructions = new HashMap<>();

    static {
        instructions.put(Kind.EQUAL, Instruction.EQ);
        instructions.put(Kind.NOT_EQUAL, Instruction.NOT_EQ);
        instructions.put(Kind.LESS_THAN, Instruction.LESS_THAN);
        instructions.put(Kind.GREATER_THAN, Instruction.GREATER_THAN);
        instructions.put(Kind.LESS_OR_EQUAL, Instruction.LESS_OR_EQ);
        instructions.put(Kind.GREATER_OR_EQUAL, Instruction.GREATER_OR_EQ);
    }

    private Kind kind;
    private Expression lhs;
    private Expression rhs;

    @Override
    public void generate() {
        lhs.generate();
        rhs.generate();
        Generator.writeCode(instructions.get(kind));
    }

    @Override
    public Object interpret() {
        Object lValue = lhs.interpret();
        Object rValue = rhs.interpret();

        return RelationalOperator.operate(kind, lValue, rValue);
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println(kind.toString() + ":");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("LHS:");
        lhs.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("RHS:");
        rhs.print(depth + 2);
    }
}
