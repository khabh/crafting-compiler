package com.craftingcompiler.node;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.exception.ReturnException;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Return extends Statement {

    private Expression expression;

    @Override
    public void generate() {
        expression.generate();
        Generator.writeCode(Instruction.RETURN);
    }

    @Override
    public void interpret() {
        throw new ReturnException(expression.interpret());
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("RETURN:");
        expression.print(depth + 1);
    }
}
