package com.craftingcompiler.node;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.exception.BreakException;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.Getter;

@Getter
public class Break extends Statement {

    @Override
    public void generate() {
        if (Generator.breakStack.isEmpty()) {
            return;
        }
        var jumpCode = Generator.writeCode(Instruction.JUMP);
        Generator.breakStack.get(Generator.breakStack.size() - 1).add(jumpCode);
    }

    @Override
    public void interpret() {
        throw new BreakException();
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("BREAK");
    }
}
