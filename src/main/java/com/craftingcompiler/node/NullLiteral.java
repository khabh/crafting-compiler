package com.craftingcompiler.node;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NullLiteral extends Expression {

    @Override
    public void generate() {
        Generator.writeCode(Instruction.PUSH_NULL);
    }

    @Override
    public Object interpret() {
        return null;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("null");
    }
}
