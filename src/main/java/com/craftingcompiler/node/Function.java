package com.craftingcompiler.node;

import static com.craftingcompiler.code.Generator.codes;
import static com.craftingcompiler.code.Generator.generatorFunctions;
import static com.craftingcompiler.code.Generator.localSize;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.util.SyntaxPrinter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Function extends Statement {

    private final String name;
    private final List<String> parameters;
    private final List<Statement> block;

    @Override
    public void generate() {
        generatorFunctions.put(name, codes.size());
        var temp = Generator.writeCode(Instruction.ALLOC);

        Generator.initBlock();
        parameters.forEach(Generator::setLocal);
        block.forEach(Statement::generate);
        Generator.popBlock();

        Generator.patchOperand(temp, localSize);
        Generator.writeCode(Instruction.RETURN);
    }

    @Override
    public void interpret() {
        for (Statement block : block) {
            block.interpret();
        }
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("FUNCTION " + name + ":");
        if (!parameters.isEmpty()) {
            SyntaxPrinter.indent(depth + 1);
            System.out.print("PARAMETERS: ");
            for (String param : parameters) {
                System.out.print(param + " ");
            }
            System.out.println();
        }
        SyntaxPrinter.indent(depth + 1);
        System.out.println("BLOCK:");
        for (Statement node : block) {
            node.print(depth + 2);
        }
    }
}
