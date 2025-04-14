package com.craftingcompiler.node;

import static com.craftingcompiler.code.Generator.continueStack;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.exception.ContinueException;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.Getter;

@Getter
public class Continue extends Statement {

    @Override
    public void generate() {
        if (continueStack.isEmpty()) {
            return;
        }
        var jumpCode = Generator.writeCode(Instruction.JUMP);
        continueStack.get(continueStack.size() - 1).add(jumpCode);
    }

    @Override
    public void interpret() {
        throw new ContinueException();
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("CONTINUE");
    }
}
