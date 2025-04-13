package com.craftingcompiler.node;

import static com.craftingcompiler.interpreter.Interpreter.local;

import com.craftingcompiler.util.SyntaxPrinter;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class If extends Statement {

    List<Expression> conditions;
    List<List<Statement>> blocks;
    List<Statement> elseBlock;

    @Override
    public void interpret() {
        int index = 0;
        while (index < conditions.size()) {
            Expression condition = conditions.get(index);
            if (!(boolean) condition.interpret()) {
                index++;
                continue;
            }
            local.getLast().addFirst(new HashMap<>());
            blocks.get(index).forEach(Statement::interpret);
            local.getLast().pollFirst();
            return;
        }
        if (elseBlock != null && !elseBlock.isEmpty()) {
            local.getLast().addFirst(new HashMap<>());
            elseBlock.forEach(Statement::interpret);
            local.getLast().pollFirst();
        }
    }

    @Override
    public void print(int depth) {
        for (int i = 0; i < conditions.size(); i++) {
            SyntaxPrinter.indent(depth);
            System.out.println(i == 0 ? "IF:" : "ELIF:");

            SyntaxPrinter.indent(depth + 1);
            System.out.println("CONDITION:");
            conditions.get(i).print(depth + 2);

            SyntaxPrinter.indent(depth + 1);
            System.out.println("BLOCK:");
            for (Statement node : blocks.get(i)) {
                node.print(depth + 2);
            }
        }

        if (elseBlock.isEmpty()) {
            return;
        }

        SyntaxPrinter.indent(depth);
        System.out.println("ELSE:");
        for (Statement node : elseBlock) {
            node.print(depth + 1);
        }
    }
}
