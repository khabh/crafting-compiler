package com.craftingcompiler.node;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Or extends Expression {

    private Expression lhs;
    private Expression rhs;

    @Override
    public void generate() {
        lhs.generate();
        var or = Generator.writeCode(Instruction.LOGICAL_OR);
        rhs.generate();
        Generator.patchAddress(or);
    }

    @Override
    public Object interpret() {
        if ((boolean) lhs.interpret()) {
            return true;
        }
        return rhs.interpret();
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("OR:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("LHS:");
        lhs.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("RHS:");
        rhs.print(depth + 2);
    }
}
