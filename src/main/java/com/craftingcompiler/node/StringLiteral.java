package com.craftingcompiler.node;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StringLiteral extends Expression {

    private String value;

    @Override
    public void generate() {
        Generator.writeCode(Instruction.PUSH_STR, value);
    }

    @Override
    public Object interpret() {
        return value;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("\"" + value + "\"");
    }
}
