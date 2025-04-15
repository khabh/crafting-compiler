package com.craftingcompiler.node;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.machine.CustomArray;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SetElement extends Expression {

    private Expression sub;
    private Expression index;
    private Expression value;

    @Override
    public void generate() {
        value.generate();
        sub.generate();
        index.generate();

        Generator.writeCode(Instruction.SET_ELEMENT);
    }

    @Override
    public Object interpret() {
        var object = sub.interpret();
        var number = index.interpret();
        var updatedValue = value.interpret();
        if (object instanceof CustomArray && number instanceof Number) {
            return ((CustomArray) object).set(((Number) number).intValue(), updatedValue);
        }
        return null;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("SET_ELEMENT:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("SUB:");
        sub.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("INDEX:");
        index.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("VALUE:");
        value.print(depth + 2);
    }
}
